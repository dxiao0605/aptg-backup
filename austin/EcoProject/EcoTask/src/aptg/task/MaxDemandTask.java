package aptg.task;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.PowerAccountMaxDemandDao;
import aptg.dao.PowerRecordMaxDemandDao;
import aptg.manager.PowerRecordManager;
import aptg.models.MeterSetupModel;
import aptg.models.PowerAccountMaxDemandModel;
import aptg.models.PowerAccountModel;
import aptg.models.PowerRecordMaxDemandModel;
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class MaxDemandTask extends BaseFunction {

	private static final String CLASS_NAME = MaxDemandTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private Map<String, PowerRecordMaxDemandModel> deviceDemandMap = new HashMap<>();

	private List<PowerRecordMaxDemandModel> deviceDemandList = new ArrayList<>();
	private List<PowerAccountMaxDemandModel> accountDemandList = new ArrayList<>();

	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");
		
		MaxDemandTask task = new MaxDemandTask();

		Calendar startCal = ToolUtil.getInstance().setDateEndTime(Calendar.getInstance());
		
		Calendar end = Calendar.getInstance();
		end.setTime(ToolUtil.getInstance().getInitYesterday());	// 前一天00:00:00
		Calendar endCal = ToolUtil.getInstance().setDateEndTime(end);	// 時間由 00:00:00 設成 23:59:00

		// 取得所有enabled的Meter (不分usageCode)
		List<MeterSetupModel> meterList = DBQueryUtil.getInstance().getAllEnabledMeter();
		
		if (args.length==0) {
			/*
			 * 	正常流程
			 */
			startCal.add(Calendar.DATE, -1);	// 計算前一天

			// 所有電表為單位
			for (MeterSetupModel meter: meterList) {
				String deviceID = meter.getDeviceID();

				Date recTime = PowerRecordManager.getInstance().getFirstRecord(deviceID);
				if (recTime!=null) {
					/*
					 * 	補資料狀況 => 改為由補資料月份開始重新統整
					 */
					startCal.setTime(recTime);
					startCal = ToolUtil.getInstance().setDateEndTime(startCal);
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(startCal.getTime());
				for ( ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
//				for ( ; startCal.compareTo(endCal)<=0 ; startCal.add(Calendar.DATE, 1)) {
					String recDate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");

					// 電表最大需量時間紀錄
					logger.info("排程統整 PowerRecordMaxDemand (電表為單位), '"+recDate+"' DeviceID=["+deviceID+"]");
					task.deviceMaxDemand(meter, recDate);
				}
			}

			// 所有電號為單位
			List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
			for (PowerAccountModel pa: paList) {
				String powerAccount = pa.getPowerAccount();

				Date recTime = PowerRecordManager.getInstance().getFirstPowerAccountRecord(powerAccount);
				if (recTime!=null && recTime.compareTo(endCal.getTime())==-1) {
					/*
					 * 	補資料狀況 => 改為由補資料月份開始重新統整
					 */
					startCal.setTime(recTime);
					startCal = ToolUtil.getInstance().setDateEndTime(startCal);
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(startCal.getTime());
				for ( ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
//				for ( ; startCal.compareTo(endCal)<=0 ; startCal.add(Calendar.DATE, 1)) {
					String recDate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");

					// 電表最大需量時間紀錄
					logger.info("排程統整 PowerAccountMaxDemand (電號為單位), '"+recDate+"' PowerAccount=["+powerAccount+"]");
					task.poweraccountMaxDemand(powerAccount, recDate);
				}
			}
			
		} else {
			/*
			 * 	指定起始重算日(含)~至執行日前一天(含)
			 */
			// 指定開始的日期
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定日期
			startCal = ToolUtil.getInstance().setDateEndTime(startCal);
			
			String param = (args.length>=2) ? args[1] : null;
			List<PowerAccountModel> poweraccountList = new ArrayList<>();
			if (StringUtils.isNotBlank(param) && param.equals("all")) {
				poweraccountList = task.getAllPowerAccount();
				logger.info("All PowerAccount");
				
			} else if (StringUtils.isNotBlank(param) && param.indexOf("BankCode=")!=-1) {
				String bankCode  = param.split("=")[1];
				
				meterList = task.getDeviceByBank(bankCode);
				poweraccountList = task.getPowerAccountByBank(bankCode);
				logger.info("BankCode=["+bankCode +"], PowerAccount: "+JsonUtil.getInstance().convertObjectToJsonstring(poweraccountList));
				
			} else {
				MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(param);
				
				meterList = DBQueryUtil.getInstance().getEnabledMeterSetupUsageCode1(meter.getPowerAccount());
				poweraccountList = task.getPowerAccountByPA(meter.getPowerAccount());
				logger.info("DeviceID=["+param +"], PowerAccount: "+JsonUtil.getInstance().convertObjectToJsonstring(poweraccountList));
			}
				
			// 指定結束的日期
			String endDate = (args.length>=3) ? args[2] : null;
			if (StringUtils.isNotBlank(endDate)) {
				Date date = ToolUtil.getInstance().convertStringToDate(endDate, "yyyy-MM-dd");
				endCal.setTime(date);
				endCal = ToolUtil.getInstance().setDateEndTime(endCal);
			}

			logger.info("重新統整 '"+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd")+"' ~ '"+ToolUtil.getInstance().convertDateToString(endCal.getTime(), "yyyy-MM-dd")+"'");

			Calendar cal = Calendar.getInstance();
			cal.setTime(startCal.getTime());
			for ( ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
//			for (Calendar cal=startCal ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
				String recDate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");

				// 電表最大需量時間紀錄
				for (MeterSetupModel meter: meterList) {
					String mDeviceID = meter.getDeviceID();

					logger.info("重新統整 PowerRecordMaxDemand (電表為單位), '"+recDate+"' DeviceID=["+mDeviceID+"]");
					task.deviceMaxDemand(meter, recDate);
				}

				// 所有電號為單位
				for (PowerAccountModel pa: poweraccountList) {
					String powerAccount = pa.getPowerAccount();

					logger.info("重新統整 PowerAccountMaxDemand (電號為單位), '"+recDate+"' PowerAccount=["+powerAccount+"]");
					task.poweraccountMaxDemand(powerAccount, recDate);
				}
			}
		}
		task.insertMaxDemand();

		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	/*
	 * 電"表"最大需量&時間紀錄
	 */
	private void deviceMaxDemand(MeterSetupModel meter, String recDate) {
		/*
		 * Version 2
		 */
		List<PowerRecordMaxDemandModel> filterList = new ArrayList<>();
		
		String deviceID = meter.getDeviceID();
		Date calculateDate = ToolUtil.getInstance().convertStringToDate(recDate, "yyyy-MM-dd");
		String startdate = getStartdate(calculateDate);
		String enddate = getEnddate(calculateDate);
		
		List<PowerRecordMaxDemandModel> list = DBQueryUtil.getInstance().getMaxRecordDemandList(deviceID, startdate, enddate);
		for (PowerRecordMaxDemandModel model: list) {
//			Date recTime = ToolUtil.getInstance().convertStringToDate(model.getRecTime(), "yyyy-MM-dd HH:mm");	// 不看秒數(無條件捨去)
//
//			long remainder = recTime.getTime() % (15*60*1000);
//			if (remainder==0) {
				filterList.add(model);
//			}
		}
//		filterList.sort(Comparator.comparing(PowerRecordMaxDemandModel::getMaxDemand).reversed());	// totalDemand以大到小作排序

		BigDecimal maxDemand = BigDecimal.ZERO;
		if (filterList.size()!=0)
			maxDemand = filterList.get(0).getMaxDemand();
		for (PowerRecordMaxDemandModel model: filterList) {
			if (model.getMaxDemand().compareTo(maxDemand)==-1) 
				break;
			
			deviceDemandList.add(model);
			logger.info("電表=["+deviceID+"], 計算=["+model.getRecTime()+"], MaxDemand=["+model.getMaxDemand()+"] => "+JsonUtil.getInstance().convertObjectToJsonstring(model));
		}

		/*
		 * Version 1
		 */
//		String deviceID = meter.getDeviceID();
//		
//		Date calculateDate = ToolUtil.getInstance().convertStringToDate(recDate, "yyyy-MM-dd");
//		String startdate = getStartdate(calculateDate);
//		String enddate = getEnddate(calculateDate);
//		List<PowerRecordMaxDemandModel> maxList = DBQueryUtil.getInstance().getRecordDemandList(deviceID, startdate, enddate, isJob);
//		
//		// 取出每15分鐘的record資料 (每15分鐘才有需量值)
//		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
//		for (PowerRecordMaxDemandModel max: maxList) {
//			Date recTime = ToolUtil.getInstance().convertStringToDate(max.getRecTime(), "yyyy-MM-dd HH:mm");	// 不看秒數(無條件捨去)
//
//			long remainder = recTime.getTime() % (15*60*1000);
//			if (remainder==0) {
//				list.add(max);
//			}
//		}
//		list.sort(Comparator.comparing(PowerRecordMaxDemandModel::getTotalDemand).reversed());	// totalDemand以大到小作排序
//		maxList.clear();
//		
//		// 一個device可能會有多筆相同totalDemand的紀錄
//		BigDecimal maxDemand = BigDecimal.ZERO;
//		for (PowerRecordMaxDemandModel dRecord: list) {
//			BigDecimal totalDemand = dRecord.getTotalDemand();
//			
//			if (totalDemand.compareTo(maxDemand)!=-1) {	// totalDemand >= maxDemand(default=0)
//				maxDemand = totalDemand;	// 以第一個record的totalDemand做標準(因為已經以大到小作排序)
//				
//				if (maxDemand.compareTo(BigDecimal.ZERO)!=0) {
//					deviceDemandList.add(dRecord);	// 若maxDemand=0省略不計
//				}
//				
//				String key = deviceID+"@"+recDate;
//				deviceDemandMap.put(key, dRecord);
//
//				logger.info("電表["+dRecord.getDeviceID()+"], 計算["+recDate+"] 之 PowerRecord RecTime=["+dRecord.getRecTime()+"], MaxDemand: "+JsonUtil.getInstance().convertObjectToJsonstring(dRecord));
//			} else {
//				break;
//			}
//		}
	}
	
	/*
	 * 電"號"最大需量&時間紀錄
	 */
	private void poweraccountMaxDemand(String powerAccount, String recDate) {
		/*
		 * Version 2
		 */
		Map<Date, PowerAccountMaxDemandModel> powerAccountDemandMap = new HashMap<>();	// key: recTime
		
		Date calculateDate = ToolUtil.getInstance().convertStringToDate(recDate, "yyyy-MM-dd");
		String startdate = getStartdate(calculateDate);
		String enddate = getEnddate(calculateDate);
		
		List<PowerRecordMaxDemandModel> list = DBQueryUtil.getInstance().getRecordDemandList(powerAccount, startdate, enddate);
		for (PowerRecordMaxDemandModel model: list) {
			Date recTime = ToolUtil.getInstance().convertStringToDate(model.getRecTime(), "yyyy-MM-dd HH:mm");	// 不看秒數

			// 每15分鐘的record資料 (每15分鐘才有需量值)
			long remainder = recTime.getTime() % (15*60*1000);
			if (remainder==0) {
				// set PowerAccount max
				PowerAccountMaxDemandModel paMax = new PowerAccountMaxDemandModel();
				paMax.setPowerAccount(powerAccount);
				paMax.setRecTime(model.getRecTime());
				paMax.setTotalDemand(model.getMaxDemand());
				
				if (powerAccountDemandMap.containsKey(recTime)) {
					// 已有其他device紀錄 => 取出相加
					PowerAccountMaxDemandModel paRecord = powerAccountDemandMap.get(recTime);
					
					BigDecimal totalDemand = paMax.getTotalDemand().add(paRecord.getTotalDemand());
					paRecord.setTotalDemand(totalDemand);
					
				} else {
					// 無其他device紀錄 => put
					powerAccountDemandMap.put(recTime, paMax);
				}			
			}
		}
		// map to list
		List<PowerAccountMaxDemandModel> result = powerAccountDemandMap.values().stream().collect(Collectors.toList());
		result.sort(Comparator.comparing(PowerAccountMaxDemandModel::getTotalDemand).reversed());
		
		BigDecimal maxTotalDemand = BigDecimal.ZERO;
		if (result.size()!=0) 
			maxTotalDemand = result.get(0).getTotalDemand();
		for (PowerAccountMaxDemandModel model: result) {
			if (model.getTotalDemand().compareTo(maxTotalDemand)==-1) 
				break;
			
			accountDemandList.add(model);
			logger.info("電號=["+powerAccount+"], 計算=["+recDate+"], TotalDemand=["+model.getTotalDemand()+"]");
			break;
		}
		powerAccountDemandMap.clear();
		result.clear();
		
		/*
		 * Version 1
		 */
//		List<MeterSetupModel> meterList = DBQueryUtil.getInstance().getEnabledMeterSetupUsageCode1(powerAccount);
//		
//		String recTime = null;
//		BigDecimal totalDemand = BigDecimal.ZERO;
//		for (MeterSetupModel meter: meterList) {
//			String deviceID = meter.getDeviceID();
//			String key = deviceID +"@"+ recDate;
//			
//			if (deviceDemandMap.containsKey(key)) {
//				PowerRecordMaxDemandModel dRecord = deviceDemandMap.get(key);	
//				
//				recTime = dRecord.getRecTime();
//				totalDemand = totalDemand.add(dRecord.getTotalDemand());
//			} else {
//				recTime = null;
////				logger.error("powerAccount=["+powerAccount+"], can't find deviceID=["+deviceID+"] PowerRecordMaxDemand record");
//				break;
//			}
//		}
//
//		if (recTime==null) {
//			return;
//		}
//		PowerAccountMaxDemandModel aRecord = new PowerAccountMaxDemandModel();
//		aRecord.setPowerAccount(powerAccount);
//		aRecord.setTotalDemand(totalDemand);
//		aRecord.setRecTime(recTime);
//		logger.info("計算電號["+powerAccount+"], 計算["+recDate+"] 之 PowerAccount RecTime=["+aRecord.getRecTime()+"], MaxDemand: "+JsonUtil.getInstance().convertObjectToJsonstring(aRecord));
//		
//		if (totalDemand.compareTo(BigDecimal.ZERO)!=0) {
//			accountDemandList.add(aRecord);	// 若totalDemand=0省略不計
//		}
	}
	
	
	
	
	private void insertMaxDemand() {
		insertPowerRecordMaxDemand();
		insertPowerAccountMaxDemand();
	}
	private void insertPowerRecordMaxDemand() {
		try {
			if (deviceDemandList.size()==0)
				return;

			Map<String, PowerRecordMaxDemandModel> delMap = new HashMap<>();
			PowerRecordMaxDemandDao dao = new PowerRecordMaxDemandDao();
			for (PowerRecordMaxDemandModel record: deviceDemandList) {
				String deviceID = record.getDeviceID();
				Date recDate = ToolUtil.getInstance().convertStringToDate(record.getRecTime(), "yyyy-MM-dd");
				String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd");
				
				String key = deviceID+"@"+date;
				if (!delMap.containsKey(key)) {
					dao.delByDeviceIDRecTime(deviceID, date);
					delMap.put(key, record);
				}
			}
			delMap.clear();
			
			
			if (getIsReprice()) {
				// 是reprice => 單筆寫入
				for (PowerRecordMaxDemandModel rd: deviceDemandList) {
					
					
					List<PowerRecordMaxDemandModel> list = new ArrayList<>();
					list.add(rd);
					List<Integer> ids = dao.insertMaxDemand(list);
					if (ids==null) {
						setFailedDevice(rd.getDeviceID());
						logger.error("##### Reprice PowerRecordMaxDemand Failed, DeviceID=["+rd.getDeviceID()+"]");
					} else {
						logger.info("Reprice PowerRecordMaxDemand success insert DeviceID=["+rd.getDeviceID()+"]");
					}
				}
				
			} else {
				// 不是reprice，正常流程 => 批次寫入
				List<Integer> ids = dao.insertMaxDemand(deviceDemandList);
				logger.info("PowerRecordMaxDemand success device demandList size: "+ids.size());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void insertPowerAccountMaxDemand() {
		try {
			if (accountDemandList.size()==0)
				return;

			Map<String, PowerAccountMaxDemandModel> delMap = new HashMap<>(); 
			PowerAccountMaxDemandDao dao = new PowerAccountMaxDemandDao();
			for (PowerAccountMaxDemandModel record: accountDemandList) {
				String powerAccount = record.getPowerAccount();
				Date recDate = ToolUtil.getInstance().convertStringToDate(record.getRecTime(), "yyyy-MM-dd");
				String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd");
				
				String key = powerAccount+"@"+date;
				if (!delMap.containsKey(key)) {
					dao.delByPowerAccountRecTime(powerAccount, date);	
					delMap.put(key, record);
				}
			}
			delMap.clear();
			

			if (getIsReprice()) {
				// 是reprice => 單筆寫入
				for (PowerAccountMaxDemandModel pa: accountDemandList) {
					List<PowerAccountMaxDemandModel> list = new ArrayList<>();
					list.add(pa);
					List<Integer> ids = dao.insertMaxDemand(list);
					if (ids==null) {
						setFailedDevice(pa.getPowerAccount());
						logger.error("##### Reprice PowerAccountMaxDemand Failed, PowerAccount=["+pa.getPowerAccount()+"]");
					} else {
						logger.info("Reprice PowerAccountMaxDemand success insert PowerAccount=["+pa.getPowerAccount()+"]");
					}
				}
				
			} else {
				// 不是reprice，正常流程 => 批次寫入
				List<Integer> ids = dao.insertMaxDemand(accountDemandList);
				logger.info("PowerAccountMaxDemand success poweraccount demandList size: "+ids.size());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getStartdate(Date calculateDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(calculateDate);
		String startdate = ToolUtil.getInstance().convertDateToString(start.getTime(), "yyyy-MM-dd");
		return startdate;
	}
	private String getEnddate(Date calculateDate) {
		Calendar end = Calendar.getInstance();
		end.setTime(calculateDate);
		end.add(Calendar.DATE, 1);
		String enddate = ToolUtil.getInstance().convertDateToString(end.getTime(), "yyyy-MM-dd");
		return enddate;
	}
}
