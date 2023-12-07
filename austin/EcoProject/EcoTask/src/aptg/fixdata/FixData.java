package aptg.fixdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.fixdata.beans.AttrBean;
import aptg.fixdata.dao.Compare2Dao;
import aptg.fixdata.dao.PowerRecordDao;
import aptg.fixdata.model.PowerRecordModel;
import aptg.fixdata.model.RecordCompare2;
import aptg.utils.ToolUtil;

public class FixData {

	private static final String CLASS_NAME = FixData.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	private String FilePath = "/opt/cubeco/shell/parseSockLog/socketlog/info";
	private BigDecimal thousand = new BigDecimal("1000");
	private static Date BeforeRecordTime;
	
	private Map<String, RecordCompare2> compare2Map = new HashMap<>();

	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");
		
		FixData fix = new FixData();
		
		logger.info("[QueryCompare2]");
		fix.queryCompare2();
//		logger.info("[QueryCompare2] compare2Map size: "+fix.compare2Map.size());

		try {
			Thread.sleep(10 *1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.info("[ParseFile & insert]");
		BeforeRecordTime = ToolUtil.getInstance().convertStringToDate("2021-02-24 16:30:00", "yyyy-MM-dd HH:mm:ss");
		fix.parseFile();
		
		logger.info(CLASS_NAME+" Task Finish !");
	}
	
	/*
	 * 1
	 */
	private void queryCompare2() {
		try {
			Compare2Dao dao = new Compare2Dao();
			List<DynaBean> rows = dao.queryCompare2();
			if (rows.size()!=0) {
				compare2Map = ContentDetail.getCompare2(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 2
	 */
	private void parseFile() {
//		for (String deviceID: compare2Map.keySet()) {
//			logger.info("##### deviceID: "+deviceID);
//		}
//		logger.info("-----------------------------------------");
		
		Map<String, PowerRecordModel> recordMap = new HashMap<>();

		File dir = new File(FilePath);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();	// 取得資料夾底下所有檔案

			for(int i=0; i<files.length ; i++) {
				File file = files[i];

				if (file.isFile()) {
					String filename = file.getName();	// apt_2020072012.txt
					logger.info("Parsing file: "+ filename);

					// 對檔案做解析
					FileReader fr = null;
					BufferedReader br = null;

					try {
						String absolutePath = file.getAbsolutePath();
						fr = new FileReader(absolutePath);
						br = new BufferedReader(fr);

						while (br.ready()) {
							String request = br.readLine();

							int semicolonCount = StringUtils.countMatches(request, ";");
							String[] message = request.split(";");	// *1;S01;00000001;ES990000000000TEST00;abc123;\n
							String deviceID = message[4];	// 設備ID: ES110000000000TEST00
							String reportTime = message[6];	// 回報時間: 130691738

							Date time = new Date(Long.valueOf(reportTime) * 1000L);
							
							if (compare2Map.containsKey(deviceID)) {
								RecordCompare2 compare = compare2Map.get(deviceID);
								Date minRecTime = compare.getMinRecTime();
								Date newTime = compare.getNewTime();
								
								
								if (newTime==null) {
									// time<BeforeRecordTime
									if (time.compareTo(BeforeRecordTime)==-1) {
										PowerRecordModel record = updateS25(message, semicolonCount);	// 電表數據 => insert to db
										
										String key = deviceID +"_"+ ToolUtil.getInstance().convertDateToString(time, "yyyy-MM-dd HH:mm:ss");
										recordMap.put(key, record);
									}
								} else {
									// minRecTime<=time && time<newTime
									if (time.compareTo(minRecTime)>=0 && time.compareTo(newTime)==-1) {
										PowerRecordModel record = updateS25(message, semicolonCount);	// 電表數據 => insert to db

										String key = deviceID +"_"+ ToolUtil.getInstance().convertDateToString(time, "yyyy-MM-dd HH:mm:ss");
										recordMap.put(key, record);
									}
								}
							} else {
								logger.info("not found DeviceID=["+deviceID+"] in RecordCompare2");
								PowerRecordModel record = updateS25(message, semicolonCount);	// 電表數據 => insert to db
								
								String key = deviceID +"_"+ ToolUtil.getInstance().convertDateToString(time, "yyyy-MM-dd HH:mm:ss");
								recordMap.put(key, record);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("read file ["+file.getName()+"] error" +"\r\n"+ "Exception:\r\n"+e.getMessage());
					} finally {
						try {
							br.close();
							fr.close();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (recordMap.size()!=0) {
				List<PowerRecordModel> insertRecords = new ArrayList<PowerRecordModel>(recordMap.values());
				insert(insertRecords);
			}
		}
	}

	private PowerRecordModel updateS25(String[] message, int semicolonCount) throws Exception {
		String func = message[1];	// S25
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String deviceID = message[4];	// 設備ID: ES110000000000TEST00
		String reportTime = message[6];	// 回報時間: 130691738

		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
		String attrList = message[5];	// 電力數值ID_電力數值: 4_0.9,6_123,7_123,8_123,9_123,14_123,15_12,27_123,28_12,40_123,41_12
		if (attrList.indexOf(",")!=-1) {
			// 多筆數據
			String[] attr = attrList.split(",");
			for (String attrInfo: attr) {
//				validResult = checkUnderLineStringIllegal(attrInfo);
//				if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
				
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		} else {
			// (1S2501)
//			validResult = checkUnderLineStringIllegal(attrList);
//			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			
			// 一筆數據
			String[] info = attrList.split("_");
			AttrBean bean = new AttrBean();
			bean.setAttrID(info[0]);
			bean.setAttrValue(info[1]);
			attrInfoList.add(bean);
		}

		// 檢查時間格式 (2002)
//		validResult = checkTimestamp(reportTime);
//		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

//		// 撈取出meter
//		MeterSetupModel meterMdl = MeterSetupManager.getInstance().getMeterSetup(deviceID);
//		
//		// Device是否存在 (1S2503)
//		validResult = isDeviceExist(meterMdl, "S25");
//		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		
		// AttrID未定義於Table1 (1S2502, 2017)
		PowerRecordModel power = new PowerRecordModel();
		for (AttrBean bean: attrInfoList) {
//			validResult = checkAttrID(power, bean.getAttrID(), bean.getAttrValue());
//			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			checkAttrID(power, bean.getAttrID(), bean.getAttrValue());
		}
		attrInfoList = null;
		
		// 裝置為單相電表，量測回報屬性含三相屬性 (1S2507) (僅留server warning, 不回應給client 錯誤代碼)
		// TODO: 如何判斷是 單相電表/三相電表 (S25-通用電力量測回報)
		
		
		// db info
		power.setDeviceId(deviceID);
		String time = ToolUtil.getInstance().convertDateToString(new Date(Long.valueOf(reportTime) * 1000L), "yyyy-MM-dd HH:mm:ss");
		power.setRecTime(time);
		
//		PowerRecordDao dao = new PowerRecordDao();
//		List<Integer> ids = dao.insertPowerRecord(power);
//		return getResponse(func, cmdSN, validResult, null);
		return power;
		
	}
	private String checkAttrID(PowerRecordModel power, String attrID, String attrValue) {
		attrID = attrID.toLowerCase();
		switch(attrID) {
			case "i1":	// 三相之A(R)相電流
				power.setI1(new BigDecimal(attrValue));	// I1	電流R
				break;
			case "i2":	// 三相之B(S)相電流
				power.setI2(new BigDecimal(attrValue)); // I2	電流S
				break;
			case "i3":	// 三相之C(T)相電流
				power.setI3(new BigDecimal(attrValue));	// I3	電流T
				break;
			case "iavg":		// 單相三線：平均電流, 三相三線：平均電流, 三相四線：平均電流
				power.setIavg(new BigDecimal(attrValue));	// Iavg	平均電流
				break;

			case "v1":	// 三相之A(R)相電壓
				power.setV1(new BigDecimal(attrValue));	// V1	相電壓R
				break;
			case "v2":	// 三相之B(S)相電壓
				power.setV2(new BigDecimal(attrValue));	// V2	相電壓S
				break;
			case "v3":	// 三相之C(T)相電壓
				power.setV3(new BigDecimal(attrValue));	// V3	相電壓T
				break;
			case "vavg":	// 電壓: 直流電壓
				power.setVavg(new BigDecimal(attrValue));	// Vavg	平均相電壓
				break;
				
			case "v12":	// 三相：A 相( R 相)到 B 相(S相)線電壓, 單位V
				power.setV12(new BigDecimal(attrValue));	// V12	線電壓AB
				break;
			case "v23":	// 三相：B 相( S 相)到 C 相(T相)線電壓, 單位V
				power.setV23(new BigDecimal(attrValue));	// V23	線電壓BC
				break;
			case "v31":	// 三相：C 相( T 相)到 A 相(R相)線電壓, 單位V
				power.setV31(new BigDecimal(attrValue));	// V31	線電壓CA
				break;
			case "vavgp":		// 三相三線：平均線電壓, 三相四線：平均線電壓, 單位V
				power.setVavgP(new BigDecimal(attrValue));	// VavgP	平均線電壓
				break;

			case "w":	// 實功率: 直流功率
				power.setW(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "var":
				power.setVar(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "va":
				power.setVa(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "pf":
				power.setPf(new BigDecimal(attrValue));
				break;

			case "kwh":	// 正相累積電能: 直流累積能
				power.setkWh(new BigDecimal(attrValue));
				break;
			case "kvarh":
				power.setKvarh(new BigDecimal(attrValue));
				break;
			case "hz":
				power.setHz(new BigDecimal(attrValue));
				break;
			case "thdvavg":
				power.setThIavg(new BigDecimal(attrValue));
				break;
			case "thdiavg":
				power.setThIavg(new BigDecimal(attrValue));
				break;

			case "mode1":
				power.setMode1(new BigDecimal(attrValue));
				break;
			case "mode2":
				power.setMode2(new BigDecimal(attrValue));
				break;
			case "mode3":
				power.setMode3(new BigDecimal(attrValue));
				break;
			case "mode4":
				power.setMode4(new BigDecimal(attrValue));
				break;
				
			case "demand":
				power.setDemandPK(new BigDecimal(attrValue));
				break;
			case "demandhalf":
				power.setDemandSP(new BigDecimal(attrValue));
				break;
			case "demandsathalf":
				power.setDemandSatSP(new BigDecimal(attrValue));
				break;
			case "demandoff":
				power.setDemandOP(new BigDecimal(attrValue));
				break;

			case "rushhour":
				power.setMcecPK(new BigDecimal(attrValue));
				break;
			case "halfhour":
				power.setMcecSP(new BigDecimal(attrValue));
				break;
			case "sathalfhour":
				power.setMcecSatSP(new BigDecimal(attrValue));
				break;
			case "offhour":
				power.setMcecOP(new BigDecimal(attrValue));
				break;
				
			case "rushhourtoday":
				power.setHighCECPK(new BigDecimal(attrValue));
				break;
			case "halfhourtoday":
				power.setHighCECSP(new BigDecimal(attrValue));
				break;
			case "offhourtoday":
				power.setHighCECOP(new BigDecimal(attrValue));
				break;
				
			default:
				// AttrID不在Table1 的定義內 (1S2502)
				return "error";
		}
		return "success";
	}
	
	private void insert(List<PowerRecordModel> list) {
		try {
			PowerRecordDao dao = new PowerRecordDao();
			dao.insertPowerRecord(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
