package aptg.handle.b168;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.bean.BatteryGroupNBListBean;
import aptg.manager.CompanyManager;
import aptg.manager.DeviceManager;
import aptg.manager.NBGroupManager;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.CompanyModel;
import aptg.model.EventModel;
import aptg.model.BattMaxRecTimeModel;
import aptg.model.BatteryModel;
import aptg.model.BatteryRecordDataModel;
import aptg.model.BatteryRecordModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 收通訊板資料
 * 1. 收資料
 * 2. 解析資料
 * 3. 驗證nbID, battID => 新增battery至cache
 * 4. 整理BatteryRecord & BatteryRecordSummary
 * 5. 整理BattMaxRecTime
 * 6. 是否有電量、溫度告警
 * 7. 寫入DB
 * 
 * @author austinchen
 *
 */
public class B168Handle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(B168Handle.class.getName());

	private static final String MULTI_DATA_SPLIT 	= ";";
	private static final String DATA_INFO_SPLIT 	= "_";
	private static final BigDecimal S_DIVIDEND = new BigDecimal(1000000);
	private static final BigDecimal MILLI_IMP  = new BigDecimal(1000);
	// 電池數值類型
	private static final int RECORD_CATEGORY_IMP	= 1;	// 內阻
	private static final int RECORD_CATEGORY_VOL	= 2;	// 電壓
	private static final int RECORD_CATEGORY_TMP	= 3;	// 溫度
	// 電池狀態
	private static final int STATUS_NORMAL 		= 1;	// 正常
	private static final int STATUS_ALERT 		= 2;	// 警戒
	private static final int STATUS_REPLACE 	= 3;	// 需更換
//	private static final int STATUS_OFFLINE 	= 4;	// 離線
	private static final int STATUS_TEMPERATURE = 25;	// 溫度
	private static final char LF  = (char) 0x0A;	// \n
	// 預設Group
	private static final int DEFAULT_GROUPID = 0;	// 為公司的預設站台
	// 公司判定健康度依據
	private static final int COMPANY_IMPTYPE_IMP 		= 20;
	private static final int COMPANY_IMPTYPE_MILLIMP 	= 21;
	private static final int COMPANY_IMPTYPE_EC 		= 22;
//	// EventStatus: 5:未解決 6:已解決
//	private static final int EventStatus_Unsolved 	= 5;
//	private static final int EventStatus_Solved 	= 6;
//	// Event Close Content
//	private static final String CloseContent_Reconnected = "Automatically connected";
//	// Event Close User
//	private static final String CloseUser_MqttServer = "NA";

	private List<BatteryRecordModel> recordList = new ArrayList<>();
	private List<BatteryRecordSummaryModel> summaryList = new ArrayList<>();
	private List<EventModel> eventList = new ArrayList<>();
	private List<BattMaxRecTimeModel> battMaxRecList = new ArrayList<>();

	private String topic;
	private MqttMessage message;
	private int qos;

	public B168Handle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	}
	
	@Override
	public void run() {
		String str = new String(message.getPayload()).replace(String.valueOf(LF), "");
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+str);
        
        String[] payloads = str.split(MULTI_DATA_SPLIT);	// 是否有多筆資料 => 有，以';'分隔
        for (int i=0 ; i<payloads.length ; i++ ) {
        	String payloadInfo = payloads[i];
        	if (StringUtils.isBlank(payloadInfo)) {
        		logger.error("[message] Error!! payload is null");
        		continue;
        	}
        	
        	try {
        		/*
        		 * 拆出timestamp & payload
        		 */
        		String[] info = payloadInfo.split(DATA_INFO_SPLIT);
        		String timeStamp = info[0];		// timestamp (ex: 1584697063)
        		String timeZone = info[1];		// 時區 (ex: +32 => +32*15/60=+8)
        		String nbID = info[2];			// 通訊板序號，10碼字串 （case sensitive), ex: ZZ20B00040
        		String payload = info[3];		// 內容 (ex: B16800110B150D100CD70B173142315C3141315E1914\n)
        		
        		boolean isValid = ToolUtil.getInstance().doCheckCRC(payload);
        		if (isValid) {
        			// legal
            		transferB168Data(Integer.valueOf(timeStamp), Integer.valueOf(timeZone), nbID, payload);
            		
        		} else {
            		// illegal
                	logger.error("[message] Error!! payload: " + payload +", checksum illegal");
        		}
        		
        	} catch(Exception e) {
        		e.printStackTrace();
        		logger.error("[message] Error!! payload: " +payloadInfo+ ".\n" +"Exception: " + e.toString());
        	}
        }

        // save record, summary, event toDB
        DBQueryUtil.getInstance().insertB168(recordList, summaryList, eventList, battMaxRecList);
        
        recordList.clear();
        summaryList.clear();
        eventList.clear();
        battMaxRecList.clear();
	}
	
	/**
	 * 轉換&處理資料內容
	 * 
	 * @param timestamp
	 * @param timezone
	 * @param boardID
	 * @param payload
	 */
	private void transferB168Data(int timeStamp, int timeZone, String boardID, String payload) {
		try {
			BatteryRecordModel record = new BatteryRecordModel();
			
			String commandCode = payload.substring(0, 4);	// B168
//			String nbID = payload.substring(4, 24);			// 5a5a3230333030303631, 00040401000000000000
			String battID = payload.substring(4, 6);		// FF (0~255)
			String iRecords = payload.substring(6, 8);	// internal resistance 內阻的組數
			String vRecords = payload.substring(8, 10);	// Voltage 電壓的組數
			String tRecords = payload.substring(10, 12);	// Temperature 溫度的組數
			int index = 12;
			
			/*
			 * convert to Human data
			 */
			Date uploadTime = ToolUtil.getInstance().convertTimestampToDate(Long.valueOf(timeStamp));
//			String recTime = ToolUtil.getInstance().convertDateToString(uploadTime, "yyyy-MM-dd HH:mm:ss", String.valueOf(timeZone));	// timezone時間
			String recTime = ToolUtil.getInstance().convertDateToString(uploadTime, "yyyy-MM-dd HH:mm:ss");								// 資料原始時間(未加上timezone)
//			String nbIDValue = ToolUtil.getInstance().convertHexToString(nbID);	// 5a5a3230333030303631 => ZZ20300061
			int battIDValue = Integer.parseInt(battID, 16);			// Hex(16進位) to DEC(10進位), ex: FF => 255
			int iRecordsCount = Integer.parseInt(iRecords, 16);		// 4 or 1
			int vRecordsCount = Integer.parseInt(vRecords, 16);		// 4 or 1
			int tRecordsCount = Integer.parseInt(tRecords, 16);		// 1 or 16

//			logger.info("timeStamp: "+timeStamp);
//			logger.info("timeZone: "+timeZone);
//			logger.info("boardID: "+boardID);
//			logger.info("");
//			logger.info("commandCode: "+commandCode);
//			logger.info("battID: "+battID +" => battIDValue: "+battIDValue);
//			logger.info("iRecords: "+iRecords +" => iRecordsCount: "+iRecordsCount);
//			logger.info("vRecords: "+vRecords +" => vRecordsCount: "+vRecordsCount);
//			logger.info("tRecords: "+tRecords +" => tRecordsCount: "+tRecordsCount);
//			logger.info("");

			
			BatteryGroupNBListBean bgnb = NBGroupManager.getInstance().getBatteryGroupNBList(boardID);
			
			/*
			 * 驗證nbID, battID => 新增battery?
			 */
			// 檢查是否已有此nbID&batteryID => 若無:新增。
			String nbIDbattID = boardID +DATA_INFO_SPLIT+ battIDValue;
			BatteryModel model = DeviceManager.getInstance().getBattery(nbIDbattID);
			if (model==null) {
				// 新增battery
				model = new BatteryModel(boardID, battIDValue);
				model.setIrRecords(iRecordsCount);
				model.setvRecords(vRecordsCount);
				model.settRecords(tRecordsCount);
				
				DeviceManager.getInstance().addBattery(model);
			} else {
				// compare irRecords, vRecords, tRecords value
				if (model.getIrRecords()!=iRecordsCount || model.getvRecords()!=vRecordsCount || model.gettRecords()!=tRecordsCount) {
					model.setIrRecords(iRecordsCount);
					model.setvRecords(vRecordsCount);
					model.settRecords(tRecordsCount);
					
					DeviceManager.getInstance().updBatteryRecords(model);
				}
			}
			
			/*
			 * set BatteryRecord
			 */
			record.setNbID(boardID);
			record.setBatteryID(battIDValue);
			record.setRecTime(recTime);
			record.setUploadStamp(timeStamp);
			record.setTimeZone(timeZone);
			boolean isFinish = recordValue(record, payload, index, iRecordsCount, vRecordsCount, tRecordsCount);	// 分類內阻、電壓、溫度 & 判斷狀態
			if (!isFinish)
				return;
			recordList.add(record);
			
			/*
			 * set BatteryRecordSummary
			 */
			BatteryRecordSummaryModel summary = new BatteryRecordSummaryModel();
			recordSummary(record, summary);
			summaryList.add(summary);

			/*
			 * set BattMaxRecTime
			 */
			BattMaxRecTimeModel battMaxRec = new BattMaxRecTimeModel();
			recordBattMaxRec(summary, bgnb.getCompanyCode(), battMaxRec);
			battMaxRecList.add(battMaxRec);

			if (battIDValue==0) {
				logger.info("[message] BatteryRecord (iBox): "+JsonUtil.getInstance().convertObjectToJsonstring(record));
				logger.info("[message] Summary (iBox): "+JsonUtil.getInstance().convertObjectToJsonstring(summary));
			} else {
				logger.info("[message] BatteryRecord (battery): "+JsonUtil.getInstance().convertObjectToJsonstring(record));
				logger.info("[message] Summary (battery): "+JsonUtil.getInstance().convertObjectToJsonstring(summary));
			}

			/*
			 *	任何公司包含RCE 預設站台內的電池組ID排除所有告警判斷
			 * (預設站台的電池組不會產生任何未解決告警 => 若預設站台的電池組 電池狀態異常，只能從電池數據、電池歷史中看到)
			 */
			if (bgnb.getDefaultGroup()!=DEFAULT_GROUPID) {	// defaultGroup!=0
				/*
				 *	電池狀態為須更換時檢查是否新增Event
				 */
				if (summary.getStatus()==STATUS_REPLACE) {
					int eventType = summary.getStatus();
					// 若狀態為需更換
					EventModel event = DBQueryUtil.getInstance().queryUnsolvedEvent(summary.getNbid(), summary.getBatteryID(), eventType);
					if (event==null) {
						event = new EventModel();
						event.setNbID(summary.getNbid());
						event.setBatteryID(summary.getBatteryID());
						event.setRecordTime(summary.getRecTime());
						event.setEventType(eventType);
						event.setTimeZone(summary.getTimeZone());
						
						if (bgnb!=null) {
							int companyCode = bgnb.getCompanyCode();
							CompanyModel company = CompanyManager.getInstance().getCompany(companyCode);
//							logger.info("");
//							logger.info("########## company: "+JsonUtil.getInstance().convertObjectToJsonstring(company));
//							logger.info("");
							
							event.setAlert1(company.getAlert1());
							event.setAlert2(company.getAlert2());
							event.setImpType(company.getImpType());
							event.setDisconnect(company.getDisconnect());
							event.setTemperature1(company.getTemperature1());
						}
						eventList.add(event);
						
						logger.info("[Event] 電池狀態需更換, New Event: "+JsonUtil.getInstance().convertObjectToJsonstring(event));
					}
				}
				
				/*
				 *	電池溫度事件
				 */
				// summary此筆資料的溫度
				BigDecimal summaryTemperature = summary.getTemperature();
				CompanyModel company = CompanyManager.getInstance().getCompany(bgnb.getCompanyCode());
				int temperature1 = company.getTemperature1();
//				logger.info("======================== Temperature=["+summaryTemperature+"] vs Company Temperature=["+temperature1+"]");
				// 比較 record>告警溫度 => 產生event
				if (summaryTemperature.compareTo(new BigDecimal(temperature1))==1) {
					int eventType = STATUS_TEMPERATURE;
					// 查找EventType為溫度
					EventModel event = DBQueryUtil.getInstance().queryUnsolvedEvent(summary.getNbid(), summary.getBatteryID(), eventType);
					if (event==null) {
						event = new EventModel();
						event.setNbID(summary.getNbid());
						event.setBatteryID(summary.getBatteryID());
						event.setRecordTime(summary.getRecTime());
						event.setEventType(eventType);
						event.setTimeZone(summary.getTimeZone());

						event.setAlert1(company.getAlert1());
						event.setAlert2(company.getAlert2());
						event.setImpType(company.getImpType());
						event.setDisconnect(company.getDisconnect());
						event.setTemperature1(temperature1);
						
						eventList.add(event);
						
						logger.info("[Event] 電池溫度過高, Temperature=["+summaryTemperature+"] > Company Temperature=["+temperature1+"], New Event: "+JsonUtil.getInstance().convertObjectToJsonstring(event));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("timeStamp=["+timeStamp+"], timeZone=["+timeZone+"], boardID=["+boardID+"], payload=["+payload+"], exception: "+e.getMessage());
		}
	}
	
	/**
	 * 分類內阻、電壓、溫度
	 * 
	 * @param record
	 * @param payload
	 * @param index
	 * @param iRecordsCount
	 * @param vRecordsCount
	 * @param tRecordsCount
	 */
	private boolean recordValue(BatteryRecordModel record, String payload, int index, int iRecordsCount, int vRecordsCount, int tRecordsCount) {
		List<BatteryRecordDataModel> iList = new ArrayList<>();
		for (int c=0 ; c<iRecordsCount ; c++) {
			String val = payload.substring(index, index+4);
			int value = Integer.parseInt(val, 16);		// 1 or 16
//			logger.info(val +"=>"+ value);
			
			BatteryRecordDataModel data = new BatteryRecordDataModel();
			data.setCategory(RECORD_CATEGORY_IMP);
			data.setOrderNo(c+1);
			data.setValue(new BigDecimal(value));
			
			// 判斷狀態(0:內阻值 1:電導值 2:毫內阻)
			boolean isFinish = determineBatteryStatus(record.getNbID(), record.getBatteryID(), data);
			if (!isFinish)
				return isFinish;
//			logger.info("iList["+c+"]: "+JsonUtil.getInstance().convertObjectToJsonstring(data) + "\n");
			
			iList.add(data);
			index += 4;
		}

		List<BatteryRecordDataModel> vList = new ArrayList<>();
		for (int c=0 ; c<vRecordsCount ; c++) {
			String val = payload.substring(index, index+4);
			int value = Integer.parseInt(val, 16);		// 1 or 16
//			logger.info(val +"=>"+ value);
			
			BatteryRecordDataModel data = new BatteryRecordDataModel();
			data.setCategory(RECORD_CATEGORY_VOL);
			data.setOrderNo(c+1);
			data.setValue(new BigDecimal(value));
			data.setStatus(1);	// default
//			logger.info("vList["+c+"]: "+JsonUtil.getInstance().convertObjectToJsonstring(data) + "\n");
			
			vList.add(data);
			index += 4;
		}

		List<BatteryRecordDataModel> tList = new ArrayList<>();
		for (int c=0 ; c<tRecordsCount ; c++) {
			String val = payload.substring(index, index+2);
			int value = Integer.parseInt(val, 16);		// 1 or 16
			byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
			int temperature = new Byte(bytes[3]).intValue();
//			logger.info(val +"=>"+ temperature);
			
			BatteryRecordDataModel data = new BatteryRecordDataModel();
			data.setCategory(RECORD_CATEGORY_TMP);
			data.setOrderNo(c+1);
			data.setValue(new BigDecimal(temperature));
			data.setStatus(1);	// default
//			logger.info("tList["+c+"]: "+JsonUtil.getInstance().convertObjectToJsonstring(data) + "\n");
			
			tList.add(data);
			index += 2;
		}

		record.setiList(iList);
		record.setvList(vList);
		record.settList(tList);
		
		return true;
	}
	
	private boolean determineBatteryStatus(String nbID, int batteryID, BatteryRecordDataModel data) {
		/*
		 * 取此nbID的Allocation companyCode
		 */
//		// (1)
//		// nbID => 查所屬的group
//		BatteryGroupModel group = BatteryGroupManager.getInstance().getBatteryGroup(nbID);
//		// 取得group所屬的company
//		int companyCode = group.getCompanyCode();
		
		// (2)
//		NBAllocationHisModel allocation = NBAllocationManager.getInstance().getNBAllocationHis(nbID);
		BatteryGroupNBListBean bgnb = NBGroupManager.getInstance().getBatteryGroupNBList(nbID);
		if (bgnb==null) {
			logger.error("NBID=["+nbID+"], can't find NBGoupHis info... CompanyCode is missing");
			return false;
		}
		int companyCode = bgnb.getCompanyCode();
		
		
		/*
		 * 取此company的告警值
		 */
		CompanyModel company = CompanyManager.getInstance().getCompany(companyCode);
		if (company==null) {
			logger.error("NBID=["+nbID+"], CompanyCode=["+companyCode+"] can't find Company info... Alert1&Alert2 is missing");
			return false;
		}
		
		/*
		 * 判斷電池健康度
		 */
		int impType = company.getImpType();
		BigDecimal alert1 = company.getAlert1();
		BigDecimal alert2 = company.getAlert2();
		int temperature1 = company.getTemperature1();
		logger.info("NBID=["+nbID+"], BatteryID=["+batteryID+"], CompanyCode=["+companyCode+"] alert1="+alert1 +", alert2="+alert2+", temperature1="+temperature1);
		switch (impType) {
			case COMPANY_IMPTYPE_IMP:
				// 內阻值
				determineBatteryIMPStatus(data, alert1, alert2);
			break;

			case COMPANY_IMPTYPE_MILLIMP:
				// 毫內阻
				determineBatteryMilliIMPStatus(data, alert1, alert2);
			break;

			case COMPANY_IMPTYPE_EC:
				// 電導值
				determineBatteryECStatus(data, alert1, alert2);	
			break;
			
			default:
				// 內阻值
				determineBatteryIMPStatus(data, alert1, alert2);	// default以內阻值判定
			break;
		}
		return true;
	}
	/**
	 * 依 內阻值判斷目前電池狀態
	 * 需更換: >=判定值2(10)
	 * 警戒: <判定值2 & >=判定值1(5)
	 * 正常: <判定值1
	 * 
	 * @param data
	 */
	private void determineBatteryIMPStatus(BatteryRecordDataModel data, BigDecimal alert1, BigDecimal alert2) {
		// 由內阻值來判斷
		BigDecimal value = data.getValue();
		
		if (value.compareTo(alert2)>=0) {
			data.setStatus(STATUS_REPLACE);
		} else if (value.compareTo(alert1)>=0 && value.compareTo(alert2)<0) {
			data.setStatus(STATUS_ALERT);
		} else {
			data.setStatus(STATUS_NORMAL);
		}
	}
	/**
	 * 依 毫內阻值判斷目前電池狀態
	 * 需更換: >=判定值2
	 * 警戒: <判定值2 & >=判定值1
	 * 正常: <判定值1
	 * 
	 * @param data
	 */
	private void determineBatteryMilliIMPStatus(BatteryRecordDataModel data, BigDecimal alert1, BigDecimal alert2) {
		// 由毫內阻來判斷
		BigDecimal value = data.getValue().divide(MILLI_IMP, 3, BigDecimal.ROUND_HALF_UP);
		
		if (value.compareTo(alert2)>=0) {
			data.setStatus(STATUS_REPLACE);
		} else if (value.compareTo(alert1)>=0 && value.compareTo(alert2)<0) {
			data.setStatus(STATUS_ALERT);
		} else {
			data.setStatus(STATUS_NORMAL);
		}
	}
	/**
	 * 依 電導值判斷目前電池狀態
	 * 需更換: <=判定值2
	 * 警戒: >判定值2 & <=判定值1
	 * 正常: >判定值1
	 *  
	 * @param data
	 */
	private void determineBatteryECStatus(BatteryRecordDataModel data, BigDecimal alert1, BigDecimal alert2) {
		// 由電導值來判斷
		BigDecimal value = S_DIVIDEND.divide(data.getValue(), 3, BigDecimal.ROUND_HALF_UP);
		
		if (value.compareTo(alert2)<=0) {
			// ex: 100 < 250
			data.setStatus(STATUS_REPLACE);
		} else if (value.compareTo(alert1)<=0 && value.compareTo(alert2)>0) {
			// ex: 500 <= 800 && 500 > 250
			data.setStatus(STATUS_ALERT);
		} else {
			// ex: 900 > 800
			data.setStatus(STATUS_NORMAL);
		}
	}
	
	/**
	 * 將BatteryRecord統整max&min&status資料
	 * 
	 * @param record
	 * @param col
	 */
	private void recordSummary(BatteryRecordModel record, BatteryRecordSummaryModel col) {
		List<BatteryRecordDataModel> iList = record.getiList().stream()
															  .sorted(Comparator.comparing(BatteryRecordDataModel::getValue))
															  .collect(Collectors.toList());
		List<BatteryRecordDataModel> vList = record.getvList().stream()
															  .sorted(Comparator.comparing(BatteryRecordDataModel::getValue))
															  .collect(Collectors.toList());
		List<BatteryRecordDataModel> tList = record.gettList().stream()
															  .sorted(Comparator.comparing(BatteryRecordDataModel::getValue))
															  .collect(Collectors.toList());

		List<BatteryRecordDataModel> statusList = record.getiList().stream()
															  	   .sorted(Comparator.comparing(BatteryRecordDataModel::getStatus))
															  	   .collect(Collectors.toList());
		col.setNbid(record.getNbID());
		col.setBatteryID(record.getBatteryID());
		col.setRecTime(record.getRecTime());
		col.setUploadStamp(record.getUploadStamp());
		col.setTimeZone(record.getTimeZone());
		// i
		col.setMaxIR(iList.get(iList.size()-1).getValue());
		col.setMinIR(iList.get(0).getValue());
		// v
		col.setMaxVol(vList.get(vList.size()-1).getValue());
		col.setMinVol(vList.get(0).getValue());
		// t
		col.setTemperature(tList.get(0).getValue());
		// status
		col.setStatus(statusList.get(statusList.size()-1).getStatus());	// 以最大(最差)的狀態數值為主要狀態
	}
	
	private void recordBattMaxRec(BatteryRecordSummaryModel summary, int companyCode, BattMaxRecTimeModel battMaxRec) {
		battMaxRec.setNbid(summary.getNbid());
		battMaxRec.setBatteryID(summary.getBatteryID());
		battMaxRec.setCompanyCode(companyCode);
		battMaxRec.setMaxRecTime(summary.getRecTime());
	}
}
