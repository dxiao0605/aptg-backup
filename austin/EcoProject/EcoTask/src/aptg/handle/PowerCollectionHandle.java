package aptg.handle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.PowerRecordCollectionDao;
import aptg.dao.PowerRecordDao2;
import aptg.manager.PowerRecordManager;
import aptg.manager.SysConfigManager;
import aptg.models.ElectricityTimeDailyModel;
import aptg.models.PowerAccountHistoryModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.models.PowerRecordModel;
import aptg.rate.RatePlan1;
import aptg.rate.RatePlan2;
import aptg.rate.RatePlan21;
import aptg.rate.RatePlan22;
import aptg.rate.RatePlan23;
import aptg.rate.RatePlan3;
import aptg.rate.RatePlan4;
import aptg.rate.RatePlan5;
import aptg.rate.RatePlan6;
import aptg.rate.RatePlan7;
import aptg.rate.RatePlan8;
import aptg.rate.RatePlan9;
import aptg.task.PowerRecordCollectionTask;
import aptg.task.base.BaseFunction;
import aptg.utils.DBContentDealUtil;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class PowerCollectionHandle extends BaseFunction implements Runnable {
	
	private static final Logger logger = LogManager.getFormatterLogger(PowerRecordCollectionTask.class.getName());

	private static final String Sysconfig_TransferRatePlanCode = "transferRatePlanCode";

	private Integer TransferRatePlanCode;	// default: 6
	
	private Map<String, PowerRecordCollectionModel> colMap = new HashMap<>();
	
	private List<PowerRecordCollectionModel> insertList = new ArrayList<>();
	private List<PowerRecordCollectionModel> updateList = new ArrayList<>();

	private boolean isJob;
	private int transferRatePlanCode;
	private String deviceID;
	private String powerAccount;
	private Date calculateDate;
	private Date calculateEndDate;
	
	public PowerCollectionHandle(boolean isJob, int transferRatePlanCode, String deviceID, String powerAccount, Date calculateDate, Date calculateEndDate) {
		this.isJob = isJob;
		this.transferRatePlanCode = transferRatePlanCode;
		this.deviceID = deviceID;
		this.powerAccount = powerAccount;
		this.calculateDate = calculateDate;
		this.calculateEndDate = calculateEndDate;

		String sysconfig = SysConfigManager.getInstance().getSysconfig(Sysconfig_TransferRatePlanCode);
		TransferRatePlanCode = Integer.valueOf(sysconfig);
	}
	
	@Override
	public void run() {
		List<PowerRecordCollectionModel> recordList;
		
		if (isJob)
			recordList = collection();		// 排程
		else
			recordList = retryCollection();	// 重算
		
		classifyInsertRecordCollection(recordList);
		updateInsertRecordCollection();
	}
	
	public List<PowerRecordCollectionModel> collection() {
		List<PowerRecordCollectionModel> recordList = new ArrayList<>();

		Date recTime = PowerRecordManager.getInstance().getFirstRecord(deviceID);
		if (recTime!=null && recTime.compareTo(calculateEndDate)==-1) {
			/*
			 * 	有calculateDate以外的record資料，從此record開始之後的皆須重算
			 */
			String time_first = ToolUtil.getInstance().convertDateToString(recTime, "yyyy-MM-dd");				// 第一筆(取第一筆的時間)
			Calendar firstCal = Calendar.getInstance();
			firstCal.setTime(recTime);			// 第一筆的時間
			
			String yesterday_first = ToolUtil.getInstance().convertDateToString(calculateEndDate, "yyyy-MM-dd");	// 最後一筆(取昨天的時間)
			Calendar lastCal = Calendar.getInstance();
			lastCal.setTime(calculateEndDate);		// 最後一筆的時間
			
			logger.info("PowerAccount=["+powerAccount+"], DeviceID=["+deviceID+"], 自'"+time_first+"' ~ '"+yesterday_first+"' 重新計算Collection");
			for (Calendar startCal=firstCal ; !startCal.after(lastCal) ; startCal.add(Calendar.DATE, 1)) {
				Calendar recCal = Calendar.getInstance();
				recCal.setTime(startCal.getTime());

				collectionProcess(deviceID, recCal, startCal.getTime(), recordList);
			}
		} else {
			/*
			 * 	只計算昨天
			 */
			Calendar recCal = Calendar.getInstance();
			recCal.setTime(calculateDate);
			logger.info("DeviceID=["+deviceID+"], 計算 '"+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"' Collection");
			
			collectionProcess(deviceID, recCal, calculateDate, recordList);
		}
		return recordList;
	}
	private void collectionProcess(String deviceID, Calendar recCal, Date calculateDate, List<PowerRecordCollectionModel> recordList) {
		// *********** 昨天最後一筆
		PowerRecordModel specifyLastRecord = PowerRecordManager.getInstance().getSpecifyDateLastRecord(deviceID, recCal.getTime());		// 取得指定日期最後一筆PowerRecord
		if (specifyLastRecord==null) {
			// 不做collection計算 => do nothing
//			logger.error("DeviceID=["+deviceID+"], PowerRecord '"+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"' is null");
			logger.error("無法找到DeviceID=["+deviceID+"], 計算日["+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"] 的最後一筆 PowerRecord");
			return;
		}
		logger.info("DeviceID=["+deviceID+"], 最後一筆 PowerRecord '"+specifyLastRecord.getRecTime()+"': "+JsonUtil.getInstance().convertObjectToJsonstring(specifyLastRecord));

		// *********** 昨天第二筆
		PowerRecordModel specifySecRecord = PowerRecordManager.getInstance().getSpecifyDateSecondRecord(deviceID, recCal.getTime());
		if (specifySecRecord==null) {
			logger.error("無法找到DeviceID=["+deviceID+"], 計算日["+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"] 的第二筆 PowerRecord");
			return;
		}
		logger.info("DeviceID=["+deviceID+"], 第二筆 PowerRecord '"+specifySecRecord.getRecTime()+"': "+JsonUtil.getInstance().convertObjectToJsonstring(specifySecRecord));
		
		// =========== 計算每日(尖峰, 半尖峰, 週六半尖峰, 離峰)累積用電量, 每日總用電量
		PowerRecordCollectionModel record = calculateDCEC(deviceID, calculateDate, specifyLastRecord, specifySecRecord);
		if (record!=null) {
			logger.info("DeviceID=["+deviceID+"], PowerRecordCollection: "+JsonUtil.getInstance().convertObjectToJsonstring(record));
			recordList.add(record);
		}
	}

	/**
	 * 	手動重算
	 * 
	 * @param calculateDate
	 */
	public List<PowerRecordCollectionModel> retryCollection() {
		List<PowerRecordCollectionModel> recordList = new ArrayList<>();
		
		// 重算指定日
		Calendar firstCal = Calendar.getInstance();
		firstCal.setTime(calculateDate);
		// 在執行的前一日結束
		Calendar lastCal = Calendar.getInstance();
		lastCal.setTime(calculateEndDate);

		logger.info("DeviceID=["+deviceID+"], 自'"+ToolUtil.getInstance().convertDateToString(firstCal.getTime(), "yyyy-MM-dd")+" ~ "+ToolUtil.getInstance().convertDateToString(lastCal.getTime(), "yyyy-MM-dd")+"' 重新計算Collection");
		for (Calendar startCal=firstCal ; !startCal.after(lastCal) ; startCal.add(Calendar.DATE, 1)) {
			Calendar recCal = Calendar.getInstance();
			recCal.setTime(startCal.getTime());
			
			// *********** 指定日最後一筆
			PowerRecordModel specifyLastRecord = PowerRecordManager.getInstance().getSpecifyDateLastRecord(deviceID, recCal.getTime());	// 取得指定日期最後一筆PowerRecord
			if (specifyLastRecord==null) {
				// 不做collection計算 => do nothing
				logger.error("無法找到DeviceID=["+deviceID+"], 計算日["+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"] 的最後一筆 PowerRecord");
				continue;
			}
			logger.info("DeviceID=["+deviceID+"], 最後一筆 PowerRecord '"+specifyLastRecord.getRecTime()+"': "+JsonUtil.getInstance().convertObjectToJsonstring(specifyLastRecord));

			// *********** 指定日第二筆 (有最後一筆一定有第一筆 => 即第一筆=最後一筆)
			PowerRecordModel specifySecRecord = PowerRecordManager.getInstance().getSpecifyDateSecondRecord(deviceID, recCal.getTime());	// 取得指定日期前一天的最後一筆PowerRecord
			if (specifySecRecord==null) {
				logger.error("無法找到DeviceID=["+deviceID+"], 計算日["+ToolUtil.getInstance().convertDateToString(recCal.getTime(), "yyyy-MM-dd")+"] 的第二筆 PowerRecord");
				continue;
			}
			logger.info("DeviceID=["+deviceID+"], 第二筆 PowerRecord '"+specifySecRecord.getRecTime()+"': "+JsonUtil.getInstance().convertObjectToJsonstring(specifySecRecord));
			
			// =========== 計算每日(尖峰, 半尖峰, 週六半尖峰, 離峰)累積用電量, 每日總用電量
			PowerRecordCollectionModel record = calculateDCEC(deviceID, startCal.getTime(), specifyLastRecord, specifySecRecord);
			if (record!=null) {
				logger.info("DeviceID=["+deviceID+"], PowerRecordCollection: "+JsonUtil.getInstance().convertObjectToJsonstring(record));
				recordList.add(record);
			}
		}
		return recordList;
	}
	
	
	public void classifyInsertRecordCollection(List<PowerRecordCollectionModel> recordList) {
		PowerRecordCollectionDao dao = new PowerRecordCollectionDao();
		for (PowerRecordCollectionModel record: recordList) {
			String deviceID = record.getDeviceID();
			String recDate = record.getRecDate();

			try {
				List<DynaBean> rows = dao.queryCollection(deviceID, recDate);
				if (rows.size()!=0) {
					// update
					updateList.add(record);
				} else {
					// insert
					insertList.add(record);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void updateInsertRecordCollection() {
		PowerRecordCollectionDao dao = new PowerRecordCollectionDao();

		if (insertList.size()!=0) {
			List<Integer> ids = dao.insertCollection(insertList);
			if (ids==null) {
				// 執行失敗
				setFailedDevice(deviceID);
				logger.error("##### Reprice PowerRecordCollection Failed, DeviceID=["+deviceID+"]");
			} else {
				// 執行成功
				setSuccessDevice(deviceID);
				logger.info("PowerRecordCollection success insertList size: "+ ids.size());
			}
		}

		if (updateList.size()!=0) {
			int count = dao.updateCollection(updateList);
			if (count==-1) {
				// 執行失敗
				setFailedDevice(deviceID);
				logger.error("##### Reprice PowerRecordCollection Failed, DeviceID=["+deviceID+"]");	
			} else {
				// 執行成功
				setSuccessDevice(deviceID);
				logger.info("PowerRecordCollection success updateList size: "+ count);
			}
		}

		insertList.clear();
		updateList.clear();
	}
	
	

	/**
	 * 
	 * @param deviceID
	 * @param recDate
	 * 
	 * 	若recDate=2020-08-24 (需統計計算的日期) => 取08-23最大值 & 08-24最大值相減，即為08-24的XX累積用電量
	 */
	private PowerRecordCollectionModel calculateDCEC(String deviceID, Date recDate, PowerRecordModel specifyLastRecord, PowerRecordModel specifySecRecord) {
		Calendar recCal = Calendar.getInstance();
		recCal.setTime(recDate);
		int date = recCal.get(Calendar.DATE);	// 日期

		String startdate = ToolUtil.getInstance().getStartdate(recDate);
		String enddate = ToolUtil.getInstance().getEnddate(recDate);
		logger.info("------------ query IVW avg & max 1 --- "+deviceID+", "+startdate);
		List<PowerRecordModel> list = DBQueryUtil.getInstance().queryBasicIVW(deviceID, startdate, enddate);
		logger.info("------------ query IVW avg & max 2 --- "+deviceID+", "+startdate);
		BigDecimal totalI = BigDecimal.ZERO;
		BigDecimal totalV = BigDecimal.ZERO;
		BigDecimal totalP = BigDecimal.ZERO;
		BigDecimal totalW = BigDecimal.ZERO;
		
		BigDecimal Iavg = BigDecimal.ZERO;
		BigDecimal Vavg = BigDecimal.ZERO;
		BigDecimal Pavg = BigDecimal.ZERO;
		BigDecimal Wavg = BigDecimal.ZERO;
		
		BigDecimal Imax = BigDecimal.ZERO;
		BigDecimal Vmax = BigDecimal.ZERO;
		BigDecimal Pmax = BigDecimal.ZERO;
		BigDecimal Wmax = BigDecimal.ZERO;


		for (PowerRecordModel record: list) {
			BigDecimal I = record.getIavg();
			BigDecimal V = record.getVavg();
			BigDecimal P = record.getVavgP();
			BigDecimal W = record.getW();
			
			totalI = totalI.add(I);
			totalV = totalV.add(V);
			totalP = totalP.add(P);
			totalW = totalW.add(W);

			if (I.compareTo(Imax)>=0) {
				Imax = I;
			}
			if (V.compareTo(Vmax)>=0) {
				Vmax = V;
			}
			if (P.compareTo(Pmax)>=0) {
				Pmax = P;
			}
			if (W.compareTo(Wmax)>=0) {
				Wmax = W;
			}
		}
		Iavg = totalI.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
		Vavg = totalV.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
		Pavg = totalP.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
		Wavg = totalW.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
//		System.out.println("############## basicRecord: "+JsonUtil.getInstance().convertObjectToJsonstring(basicRecord));
		
		/* =========================================================================
		 * 								回傳結果
		 * ========================================================================= */
		PowerRecordCollectionModel recordCollection = new PowerRecordCollectionModel();
		recordCollection.setDeviceID(deviceID);
		recordCollection.setRecDate(ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd"));
		
		/*
		 * avg(Iavg), max(Iavg)
		 * avg(Vavg), max(Vavg)
		 * avg(W), max(W)
		 */
		recordCollection.setIavg(Iavg);
		recordCollection.setImax(Imax);
		recordCollection.setVavg(Vavg);
		recordCollection.setVmax(Vmax);
		recordCollection.setVavgP(Pavg);
		recordCollection.setVmaxP(Pmax);
		recordCollection.setWavg(Wavg);
		recordCollection.setWmax(Wmax);
		
		/*
		 *	===== 計算ECO5當日累積用電量
		 *	if RecDate 為每月1日，
		 *		日累計結果 = 當日最後一筆PowerRecord.MCEC
		 *	else
		 * 		日累計結果 = 取當日最後一筆PowerRecord.MCECPK-前一天最後一筆PowerRecord.MCECPK
		 */
		BigDecimal dcecPK = (date==1) ? specifyLastRecord.getMCECPK() : specifyLastRecord.getMCECPK().subtract(specifySecRecord.getMCECPK());				// DCEC PK (本日尖峰累積用電量)
		BigDecimal dcecSP = (date==1) ? specifyLastRecord.getMCECSP() : specifyLastRecord.getMCECSP().subtract(specifySecRecord.getMCECSP());				// DCEC SP (本日半尖峰累積用電量)
		BigDecimal dcecSatSP = (date==1) ? specifyLastRecord.getMCECSatSP() : specifyLastRecord.getMCECSatSP().subtract(specifySecRecord.getMCECSatSP());	// DCEC SatSP (本日週六半尖峰累積用電量)
		BigDecimal dcecOP = (date==1) ? specifyLastRecord.getMCECOP() : specifyLastRecord.getMCECOP().subtract(specifySecRecord.getMCECOP());				// DCEC OP (本日離峰累積用電量)
		BigDecimal dcec = dcecPK.add(dcecSP).add(dcecSatSP).add(dcecOP);					// DCEC (本日總用電量)
		recordCollection.setDCECPK(dcecPK);
		recordCollection.setDCECSP(dcecSP);
		recordCollection.setDCECSatSP(dcecSatSP);
		recordCollection.setDCECOP(dcecOP);
		recordCollection.setDCEC(dcec);
		
		/*
		 *	===== 計算ECO5當月累積用電量 
		 *  if RecDate 為每月1日
		 *  	只算本日累積用電量
		 *  else
		 *  	拿前一日的本月累積用電量+本日累積用電量
		 */
		BigDecimal mcecPK = (date==1) ? dcecPK : specifySecRecord.getMCECPK().add(dcecPK);
		BigDecimal mcecSP = (date==1) ? dcecSP : specifySecRecord.getMCECSP().add(dcecSP);
		BigDecimal mcecSatSP = (date==1) ? dcecSatSP : specifySecRecord.getMCECSatSP().add(dcecSatSP);
		BigDecimal mcecOP = (date==1) ? dcecOP : specifySecRecord.getMCECOP().add(dcecOP);
		BigDecimal cec = mcecPK.add(mcecSP).add(mcecSatSP).add(mcecOP);
		recordCollection.setMCECPK(mcecPK);
		recordCollection.setMCECSP(mcecSP);
		recordCollection.setMCECSatSP(mcecSatSP);
		recordCollection.setMCECOP(mcecOP);
		recordCollection.setCEC(cec);
//		System.out.println();
//		System.out.println("############# eco5 daily: PK="+mcecPK+", SP="+mcecSP+", SatSP="+mcecSatSP+", OP="+mcecOP+", cec="+cec);
//		System.out.println();

		/*
		 * 更新ECO5當日&當月最大需量
		 */
		getMaxDemand(recordCollection, recDate);
		
		/*
		 * 台電每日最大需量、每月最大需量、台電每日累積用電量、台電每月累積用電量
		 */
		logger.info("------------ getTPValue 1 --- "+deviceID+", "+startdate);
		getTPValue(recordCollection, specifyLastRecord.getRatePlanCode());
		logger.info("------------ getTPValue 2 --- "+deviceID+", "+startdate);
		
		/*
		 * kWh
		 */
		recordCollection.setKWh(specifyLastRecord.getKWh());	// 每日最後的電表值
		
		/*
		 * ECO5預測當月尖峰, 半尖峰, 週六半尖峰, 離峰累積用電量 & 總用電量
		 */
		getFcstECO5MCEC(recDate, recordCollection);	// 尚未上版2021-01-25註解
//		System.out.println();
//		System.out.println("############# fcst eco5 month: fcstEco5PK="+recordCollection.getFcstECO5MCECPK()+", fcstEco5SP="+recordCollection.getFcstECO5MCECSP()+", fcstEco5SatSP="+recordCollection.getFcstECO5MCECSatSP()+
//														", fcstEco5OP="+recordCollection.getFcstECO5MCECOP()+", fcstEco5mcec="+recordCollection.getFcstECO5MCEC());
//		System.out.println();
		
		/*
		 * set DeviceID=>PowerAccount's RatePlan, UsuallyCC, SPCC, SatSPCC, OPCC
		 */
		getRatePlanAndCC(recordCollection, deviceID, recordCollection.getRecDate());
		
		
		String key = deviceID +"_"+ recordCollection.getRecDate();
		colMap.put(key, recordCollection);
		
		return recordCollection;
	}
	
	/*
	 * 當日最大需量
	 * &
	 * 當月最大需量 (基於前幾天的collection需量值為正確為原則，若前面有誤或有修改，需重執行整個月份)
	 */
	private void getMaxDemand(PowerRecordCollectionModel record, Date recDate) {
//		int year = ToolUtil.getInstance().getYear(recDate);
//		int month = ToolUtil.getInstance().getMonth(recDate);
		
		PowerRecordDao2 dao = new PowerRecordDao2();
		List<DynaBean> rows = new ArrayList<>();
		try {
			// 本日最大需量
			String startdate = ToolUtil.getInstance().getStartdate(recDate);
			String enddate = ToolUtil.getInstance().getEnddate(recDate);
			logger.info("------------ queryMaxDRecord date 1 --- "+deviceID+", "+startdate);
			rows = dao.queryMaxDRecord(record.getDeviceID(), startdate, enddate);
			logger.info("------------ queryMaxDRecord date 2 --- "+deviceID+", "+startdate);
			if (rows.size()!=0) {
				PowerRecordModel dRecord = DBContentDealUtil.getMaxPowerRecord(rows);
				
				record.setDemandPK(dRecord.getDemandPK());
				record.setDemandSP(dRecord.getDemandSP());
				record.setDemandSatSP(dRecord.getDemandSatSP());
				record.setDemandOP(dRecord.getDemandOP());
				
				record.setMode1(dRecord.getMode1());
				record.setMode2(dRecord.getMode2());
				record.setMode3(dRecord.getMode3());
				record.setMode4(dRecord.getMode4());
			}
			
			
			
			// 本月最大需量
			BigDecimal mdemandPK = BigDecimal.ZERO;
			BigDecimal mdemandSP = BigDecimal.ZERO;
			BigDecimal mdemandSatSP = BigDecimal.ZERO;
			BigDecimal mdemandOP = BigDecimal.ZERO;

			// (1) 找"今天之前"統計好的PowerRecordCollection
			Calendar start = Calendar.getInstance();
			start.setTime(recDate);
			int date = start.get(Calendar.DATE);	// 計算日的日期
			
			start.add(Calendar.DATE, -1);	// 找前一天統計好的collection
			startdate = ToolUtil.getInstance().convertDateToString(start.getTime(), "yyyy-MM-dd");
			
			Calendar end = Calendar.getInstance();
			end.setTime(recDate);
			enddate = ToolUtil.getInstance().convertDateToString(end.getTime(), "yyyy-MM-dd");

			logger.info("------------ queryMaxMRecord month 1 --- "+deviceID+", "+enddate);
			// 每月1號只需與1號當日比較即可=>跳至(2)
			if (date>1) {
				// > 1號 =>檢查colMap 或 map查無從db撈取前一天collection
				String key = deviceID +"_"+ startdate;
				PowerRecordCollectionModel ystCol = null;
				if (colMap.containsKey(key)) {
					ystCol = colMap.get(key);
				} else {
					List<PowerRecordCollectionModel> list = new ArrayList<>();
					PowerRecordCollectionDao colDao = new PowerRecordCollectionDao();
//					rows = colDao.queryCollection(record.getDeviceID(), startdate, enddate);	// ex: recDate=2021-01-16 => 找前一天，即01-15 , >=startdate(2021-01-15) ~ <enddate(2021-01-16)
//					if (rows.size()!=0) {
//						list = DBContentDealUtil.getCollectionList(rows);
//						ystCol = list.get(0);
//					} else {
//						// DB內找無前一天collection，往前找最新一筆
//						
//					}
					rows = colDao.queryNewestCollection(record.getDeviceID(), startdate, enddate);	// ex: recDate=2021-01-16 => 找前一天，即01-15 , >=startdate(2021-01-15) ~ <enddate(2021-01-16)
					if (rows.size()!=0) {
						list = DBContentDealUtil.getCollectionMDemandList(rows);
						ystCol = list.get(0);
					}
				}
				if (ystCol!=null) {
					if (ystCol.getMDemandPK().compareTo(mdemandPK)>=0) {
						mdemandPK = ystCol.getMDemandPK();
					}
					if (ystCol.getMDemandSP().compareTo(mdemandSP)>=0) {
						mdemandSP = ystCol.getMDemandSP();
					}
					if (ystCol.getMDemandSatSP().compareTo(mdemandSatSP)>=0) {
						mdemandSatSP = ystCol.getMDemandSatSP();
					}
					if (ystCol.getMDemandOP().compareTo(mdemandOP)>=0) {
						mdemandOP = ystCol.getMDemandOP();
					}
				}
			}

			// (2) 和本日的做比較
			if (record.getDemandPK().compareTo(mdemandPK)>=0) {
				mdemandPK = record.getDemandPK();
			}
			if (record.getDemandSP().compareTo(mdemandSP)>=0) {
				mdemandSP = record.getDemandSP();
			}
			if (record.getDemandSatSP().compareTo(mdemandSatSP)>=0) {
				mdemandSatSP = record.getDemandSatSP();
			}
			if (record.getDemandOP().compareTo(mdemandOP)>=0) {
				mdemandOP = record.getDemandOP();
			}
			record.setMDemandPK(mdemandPK);
			record.setMDemandSP(mdemandSP);
			record.setMDemandSatSP(mdemandSatSP);
			record.setMDemandOP(mdemandOP);
			logger.info("------------ queryMaxMRecord month 2 --- "+deviceID+", "+enddate);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 台電每日最大需量、每月最大需量、台電每日累積用電量、台電每月累積用電量
	 */
	private void getTPValue(PowerRecordCollectionModel record, int ratePlanCode) {
		if (ratePlanCode==21 || ratePlanCode==22 ||ratePlanCode==23) {
			// RatePlanCode=21, 22, 23依sysconfig設定更改計算方式
			ratePlanCode = transferRatePlanCode;
		}
		
		switch (ratePlanCode) {
			// 1: 表燈營業用 
			case 1:
				RatePlan1 rate1 = new RatePlan1();
				record = rate1.tpCollectionValue(record);
				break;
				
			// 2: 表燈非營業用 
			case 2:
				RatePlan2 rate2 = new RatePlan2();
				record = rate2.tpCollectionValue(record);
				break;
				
			// 3: 表燈簡易二段式 
			case 3:
				RatePlan3 rate3 = new RatePlan3();
				record = rate3.tpCollectionValue(record);
				break;
				
			// 4: 表燈簡易三段式 
			case 4:
				RatePlan4 rate4 = new RatePlan4();
				record = rate4.tpCollectionValue(record);
				break;
				
			// 5: 表燈標準二段式 
			case 5:
				RatePlan5 rate5 = new RatePlan5();
				record = rate5.tpCollectionValue(record);
				break;
				
			// 6: 低壓非時間
			case 6:
				RatePlan6 rate6 = new RatePlan6();
				record = rate6.tpCollectionValue(record);
				break;
				
			// 7: 低壓二段式
			case 7:
				RatePlan7 rate7 = new RatePlan7();
				record = rate7.tpCollectionValue(record);
				break;
				
			// 8: 高壓二段式
			case 8:
				RatePlan8 rate8 = new RatePlan8();
				record = rate8.tpCollectionValue(record);
				break;
				
			// 9: 高壓三段式
			case 9:
				RatePlan9 rate9 = new RatePlan9();
				record = rate9.tpCollectionValue(record);
				break;
				
			// 21: 自訂A-分攤
			case 21:
				RatePlan21 rate21 = new RatePlan21();
				record = rate21.tpCollectionValue(record);
				break;
				
			// 22: 自訂B-公用分攤
			case 22:
				RatePlan22 rate22 = new RatePlan22();
				record = rate22.tpCollectionValue(record);
				break;
				
			// 23: 自訂C-空調分攤
			case 23:
				RatePlan23 rate23 = new RatePlan23();
				record = rate23.tpCollectionValue(record);
				break;
		}
	}
	private void getFcstECO5MCEC(Date recDate, PowerRecordCollectionModel recordCollection) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(recDate);
		cal.add(Calendar.DATE, 1);

		String untildate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");

		cal.setTime(recDate);
		Calendar startCal = ToolUtil.getInstance().getMonthStart(cal);		// 2021-01-01
		String startdate = ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd");

		cal.setTime(recDate);
		Calendar endCal = ToolUtil.getInstance().getNextMonthStart(cal);	// 2021-02-01
		String enddate = ToolUtil.getInstance().convertDateToString(endCal.getTime(), "yyyy-MM-dd");

		// 實際累積時間
		logger.info("------------ queryElectricityUseHour date 1 --- "+deviceID +", "+startdate +" ~ "+ untildate);
		ElectricityTimeDailyModel actual = DBQueryUtil.getInstance().queryElectricityUseHour(startdate, untildate);	// 本月實際用電時間
		logger.info("------------ queryElectricityUseHour date 2 --- "+deviceID +", "+startdate +" ~ "+ untildate);
		if (actual==null) {
			logger.error("Actual ElectricityTimeDaily is null, startdate=["+startdate+"], endDate=["+untildate+"]");
			return;
		}
			
		// 該月應總用電時間
		logger.info("------------ queryElectricityUseHour month 1 --- "+deviceID +", "+startdate +" ~ "+ enddate);
		ElectricityTimeDailyModel total = DBQueryUtil.getInstance().queryElectricityUseHour(startdate, enddate);	// 本月應總用電時間
		logger.info("------------ queryElectricityUseHour month 2 --- "+deviceID +", "+startdate +" ~ "+ enddate);
		if (total==null) {
			logger.error("Total ElectricityTimeDaily is null, startdate=["+startdate+"], endDate=["+enddate+"]");
			return;
		}

		// pk
		BigDecimal pkMultiple = BigDecimal.ZERO;
		if (actual.getUsuallyHour().compareTo(BigDecimal.ZERO)==1)
			pkMultiple = total.getUsuallyHour().divide(actual.getUsuallyHour(), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal fcstECO5MCECPK = recordCollection.getMCECPK().multiply(pkMultiple).setScale(2, BigDecimal.ROUND_HALF_UP);

//		logger.info("=============================================================");
//		logger.info("*********** total.getUsuallyHour(): "+total.getUsuallyHour());
//		logger.info("*********** total.getUsuallyHour(): "+total.getUsuallyHour());
//		
//		logger.info("*********** total.getUsuallyHour(): "+total.getUsuallyHour());
//		logger.info("*********** actual.getUsuallyHour(): "+actual.getUsuallyHour());
//		
//		logger.info("*********** pkMultiple: "+pkMultiple);
//		
//		logger.info("*********** recordCollection.getMCECPK(): "+recordCollection.getMCECPK());
//		
//		logger.info("*********** fcstECO5MCECPK: "+fcstECO5MCECPK);
//		logger.info("=============================================================");
				
		// sp
		BigDecimal spMultiple = BigDecimal.ZERO;
		if (actual.getSPHour().compareTo(BigDecimal.ZERO)==1)
			spMultiple = total.getSPHour().divide(actual.getSPHour(), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal fcstECO5MCECSP = recordCollection.getMCECSP().multiply(spMultiple).setScale(2, BigDecimal.ROUND_HALF_UP);

		// satsp
		BigDecimal satspMultiple = BigDecimal.ZERO;
		if (actual.getSatSPHour().compareTo(BigDecimal.ZERO)==1)
			satspMultiple = total.getSatSPHour().divide(actual.getSatSPHour(), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal fcstECO5MCECSatSP = recordCollection.getMCECSatSP().multiply(satspMultiple).setScale(2, BigDecimal.ROUND_HALF_UP);

		// op
		BigDecimal opMultiple = BigDecimal.ZERO;
		if (actual.getOPHour().compareTo(BigDecimal.ZERO)==1)
			opMultiple = total.getOPHour().divide(actual.getOPHour(), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal fcstECO5MCECOP = recordCollection.getMCECOP().multiply(opMultiple).setScale(2, BigDecimal.ROUND_HALF_UP);

		BigDecimal fcstECO5MCEC = fcstECO5MCECPK.add(fcstECO5MCECSP).add(fcstECO5MCECSatSP).add(fcstECO5MCECOP);

//		System.out.println();
//		System.out.println("############# pkMultiple="+pkMultiple+", spMultiple="+spMultiple+", satspMultiple="+satspMultiple+", opMultiple="+opMultiple);
//		System.out.println();
				
		recordCollection.setFcstECO5MCECPK(fcstECO5MCECPK);			// ECO5計法預估當月累積用電量(PK)
		recordCollection.setFcstECO5MCECSP(fcstECO5MCECSP);			// ECO5計法預估當月累積用電量(SP)
		recordCollection.setFcstECO5MCECSatSP(fcstECO5MCECSatSP);	// ECO5計法預估當月累積用電量(SatSP)
		recordCollection.setFcstECO5MCECOP(fcstECO5MCECOP);			// ECO5計法預估當月累積用電量(OP)
		recordCollection.setFcstECO5MCEC(fcstECO5MCEC);				// ECO5計法預估當月累積用電量(MCEC)
	}
	
	private void getRatePlanAndCC(PowerRecordCollectionModel recordCollection, String deviceID, String recDate) {
		PowerAccountHistoryModel pah = DBQueryUtil.getInstance().queryCollectionHistoryCC(deviceID, recDate);
		if (pah!=null) {
			recordCollection.setRatePlanCode(pah.getRatePlanCode());
			recordCollection.setRealPlan(pah.getRatePlanCode());
			recordCollection.setUsuallyCC(pah.getUsuallyCC());
			recordCollection.setSPCC(pah.getSPCC());
			recordCollection.setSatSPCC(pah.getSatSPCC());
			recordCollection.setOPCC(pah.getOPCC());

			int ratePlanCode = recordCollection.getRatePlanCode();
			if (ratePlanCode==21 || ratePlanCode==22 ||ratePlanCode==23)
				recordCollection.setRatePlanCode(TransferRatePlanCode);
		}
	}
}
