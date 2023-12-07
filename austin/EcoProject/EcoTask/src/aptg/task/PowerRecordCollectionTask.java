package aptg.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.PowerCollectionHandle;
import aptg.manager.PowerRecordManager;
import aptg.manager.SysConfigManager;
import aptg.models.MeterSetupModel;
import aptg.models.PowerRecordModel;
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 *	每日計算PowerCollection
 *	args[0]: 指定重算起始日期
 *	args[1]: 指定deviceID
 *	(ps. 可只單獨指定args[0], 不提供args[1])
 * 
 * @author austinchen
 *
 */
public class PowerRecordCollectionTask extends BaseFunction {

	private static final String CLASS_NAME = PowerRecordCollectionTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final String Sysconfig_TransferRatePlanCode = "transferRatePlanCode";
	private static Integer TransferRatePlanCode;
	
	// args[2]
	private static Date calculateEndDate;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		PowerRecordCollectionTask task = new PowerRecordCollectionTask();
		
		String sysconfig = SysConfigManager.getInstance().getSysconfig(Sysconfig_TransferRatePlanCode);
		TransferRatePlanCode = Integer.valueOf(sysconfig);
//		System.out.println("TransferRatePlanCode: "+TransferRatePlanCode);
		
		if (args.length==0) {
			/*
			 * 	每日計算，執行昨日計算
			 * 	(若前一天有更之前的資料，則從最早資料時間開始重新計算至昨天)
			 */
			Date calculateDate = ToolUtil.getInstance().getInitYesterday();	// 應被計算&統計的日期 (前一天)，ex: 現為8/20，程式執行時，整理計算8/19之資料 (即 08-19 00:00:00 ~ 08-20 00:00:00)
			logger.info("每日PowerRecordCollection Task: "+ToolUtil.getInstance().convertDateToString(calculateDate, "yyyy-MM-dd"));
			task.collection();
			
		} else {
			/*
			 * 	指定起始重算日(含)~至執行日前一天(含)
			 */
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");

			String param = (args.length>=2) ? args[1] : null;
			List<MeterSetupModel> deviceList = new ArrayList<>();
			if (StringUtils.isNotBlank(param) && param.equals("all")) {
				deviceList = task.getAllDevice();
				logger.info("All Enabled=1 Device");
				
			} else if (StringUtils.isNotBlank(param) && param.indexOf("BankCode=")!=-1) {
				String bankCode  = param.split("=")[1];
				deviceList = task.getDeviceByBank(bankCode);
				logger.info("BankCode=["+bankCode +"], Device: "+JsonUtil.getInstance().convertObjectToJsonstring(deviceList));
				
			} else {
				deviceList = task.getDevice(param);
				logger.info("DeviceID=["+param +"], Device: "+JsonUtil.getInstance().convertObjectToJsonstring(deviceList));
			}
			
			String endDate = (args.length>=3) ? args[2] : null;
			if (StringUtils.isNotBlank(endDate)) {
				// 想算到哪天就寫到哪天, ex: 2020-11-17 (含)
				calculateEndDate = ToolUtil.getInstance().convertStringToDate(endDate, "yyyy-MM-dd");
			}
			// ===================================
			
			logger.info("手動 自 '"+specifyDate+"' 重新計算DeviceID=["+param+"] PowerRecordCollection");
			task.retryCollection(deviceList, calculateDate);
		}

		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}

	/**
	 * 	排程計算
	 * 
	 * @param calculateDate
	 */
	public void collection() {
		// 應被計算&統計的日期 (前一天)，ex: 現為8/20，程式執行時，整理計算8/19之資料 (即 08-19 00:00:00 ~ 08-20 00:00:00)
		Date calculateDate = ToolUtil.getInstance().getInitYesterday();
		// 在執行的前一日結束
		Calendar lastCal = Calendar.getInstance();
		lastCal.setTime(ToolUtil.getInstance().getInitYesterday());

		/*
		 *	檢查CreateTime是否有recTime為非昨日的紀錄 
		 */
		String startdate = ToolUtil.getInstance().getStartdate(calculateDate);
		String enddate = ToolUtil.getInstance().getEnddate(calculateDate);
		List<PowerRecordModel> list = DBQueryUtil.getInstance().getRecdateList(startdate, enddate);
//		list.sort(Comparator.comparing(PowerRecordModel::getRecTime));	// recTime小到大排序

		for (PowerRecordModel pr: list) {
			String deviceID = pr.getDeviceID();
			String rectime = pr.getRecTime();
			
			Date recTime = ToolUtil.getInstance().convertStringToDate(rectime, "yyyy-MM-dd");	// 只取第一筆
			PowerRecordManager.getInstance().setFirstPowerRecord(deviceID, recTime);			// 紀錄DeviceID, recTime
		}
//		logger.info("************ DeviceID Min(RecTime) list size: "+list.size());
		
		// 取得所有enabled的Meter => 逐一統計&計算
		List<MeterSetupModel> deviceList = DBQueryUtil.getInstance().getAllEnabledMeter();
		
		for (MeterSetupModel device: deviceList) {
			String deviceID = device.getDeviceID();
			String powerAccount = device.getPowerAccount();
			
			Date recTime = PowerRecordManager.getInstance().getFirstRecord(deviceID);
			if (recTime!=null)
				PowerRecordManager.getInstance().setFirstPowerAccountRecord(powerAccount, recTime);	// 紀錄powerAccount, recTime
			
			threadPool.execute(new PowerCollectionHandle(true, TransferRatePlanCode, deviceID, powerAccount, calculateDate, lastCal.getTime()));
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	手動重算
	 * 
	 * @param calculateDate
	 */
	public void retryCollection(List<MeterSetupModel> deviceList, Date calculateDate) {
		for (MeterSetupModel device: deviceList) {
			String deviceID = device.getDeviceID();

			// 在執行的前一日結束
			Calendar lastCal = Calendar.getInstance();
			if (calculateEndDate!=null)
				lastCal.setTime(calculateEndDate);
			else
				lastCal.setTime(ToolUtil.getInstance().getInitYesterday());
			
			threadPool.execute(new PowerCollectionHandle(false, TransferRatePlanCode, deviceID, null, calculateDate, lastCal.getTime()));	
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
