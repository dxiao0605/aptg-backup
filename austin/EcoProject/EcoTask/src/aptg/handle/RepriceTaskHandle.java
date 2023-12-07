package aptg.handle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.base.BaseTaskHandle;
import aptg.models.MeterSetupModel;
import aptg.models.RepriceTaskModel;
import aptg.task.BestCCTask;
import aptg.task.BestRatePlanTask;
import aptg.task.CheckPowerAccountTask;
import aptg.task.FcstChargeTask;
import aptg.task.KPITask;
import aptg.task.MaxDemandTask;
import aptg.task.PowerMonthTask;
import aptg.task.PowerRecordCollectionTask;
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class RepriceTaskHandle extends BaseTaskHandle {
	
	private static final String CLASS_NAME = CheckPowerAccountTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private ExecutorService threadPool = Executors.newFixedThreadPool(10);
	private ExecutorService threadPool2 = Executors.newFixedThreadPool(10);

	private Map<String, RepriceTaskModel> reMap = new LinkedHashMap<>();	// 紀錄DeviceID欄位有值的task => key: deviceID
	private Map<String, RepriceTaskModel> paMap = new LinkedHashMap<>();	// 紀錄DeviceID欄位無值的task => key: deivceID
	private Map<String, Date> minMap = new HashMap<>();						// 紀錄所有DeviceID其相同的powerAccount，在此次排程裡最小的StartDate => key: powerAccount
	
	private Map<String, List<String>> paDeviceMap = new HashMap<>();		// 紀錄PowerAccount，其底下所有enabled的Device(不只usageCode=1, 其他也包含在內) => key: powerAccount, value: List of Device
	private Map<String, RepriceTaskModel> countFcstChargeTimes = new HashMap<>();	// 計算不同deviceID相同powerAccount，做collection時，已做的fcstcharge次數 => key: powerAccount
	
	private Map<String, RepriceTaskModel> repeatCancelMap = new LinkedHashMap<>();
	
	private boolean isExecuted = true;
	
	public boolean isExecuted() {
		return isExecuted;
	}
	
	/**
	 *	重算電費
	 */
	public void checkRepriceTask() {
		// 檢查RepriceTask
		List<RepriceTaskModel> list = getRepriceTask(TaskStatusCode_Executing);
		if (list.size()==0) {	// 已無處理中的task，此次排程可執行
			// 取出未處理的task
			list = getRepriceTask(TaskStatusCode_Waiting);
			logger.info("RepriceTask Waiting task size: "+list.size() +", StatusCode: 0=>1");
			
			// 1. 更新狀態 => 執行中: 1
			updateRepriceTaskExecuting(list);

			// 2. 分類collection
			for (RepriceTaskModel rp: list) {
				logger.info("RepriceTask 重算電費: "+ JsonUtil.getInstance().convertObjectToJsonstring(rp));
				
				String deviceID = rp.getDeviceID();
				String powerAccount = rp.getPowerAccount();
				String repriceFrom = rp.getRepriceFrom();
				String startDate = rp.getStartDate();
				
				if (StringUtils.isNotBlank(deviceID)) {
					// User從ECO5系統操作 (有紀錄DeviceID)
					RepriceTaskModel nrp = setRepriceTaskModel(rp, deviceID, powerAccount);
					setCollection(nrp);	// 紀錄 deviceid min startdate
					
				} else {
					// 無deviceID
					List<MeterSetupModel> meterList = DBQueryUtil.getInstance().getEnabledMeterSetup(powerAccount);
					if (meterList.size()==0) {
						logger.error("重算失敗PowerAccount=["+powerAccount+"]查無DeviceID, RepriceTask: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
						updateRepriceTaskStatus(TaskStatusCode_Failed, rp.getPowerAccount(), startDate, repriceFrom);
						sendRepriceTaskFailed(rp, powerAccount+"查無DeviceID");	// 無device => failed通知
						
					} else {
						for (MeterSetupModel meter: meterList) {
							deviceID = meter.getDeviceID();
							RepriceTaskModel nrp = setRepriceTaskModel(rp, deviceID, powerAccount);
							setCollectionPA(nrp);	// 紀錄 deviceid min startdate	
						}
					}
				}
			}
			
			delOldRecords();
			cancelRepeat();
			doCollection();
			doFcstCharge();
			
		} else {
			logger.info("RepriceTask未執行，尚有 "+list.size()+" 個排程正在執行中 ===> ");
			for (RepriceTaskModel rp: list) {
				logger.info("Job is executing: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
			}

			isExecuted = false;
		}
	}
	public void delOldRecords() {
		for (RepriceTaskModel rp: reMap.values()) {
			logger.info("Deleting PowerAccount=["+rp.getPowerAccount()+"] and DeviceID=["+rp.getDeviceID()+"] and StartDate=["+rp.getStartDate()+"] PowerRecordCollection, FcstCharge, PowerMonth, BestCC, BestRatePlan");
			DBQueryUtil.getInstance().deleteRepriceTaskInfo(rp);
		}
		for (RepriceTaskModel rp: paMap.values()) {
			logger.info("Deleting PowerAccount=["+rp.getPowerAccount()+"] and DeviceID=["+rp.getDeviceID()+"] and StartDate=["+rp.getStartDate()+"] PowerRecordCollection, FcstCharge, PowerMonth, BestCC, BestRatePlan");
			DBQueryUtil.getInstance().deleteRepriceTaskInfo(rp);
		}
	}
	public void cancelRepeat() {
		for (RepriceTaskModel rp: repeatCancelMap.values()) {
			logger.info("Cancel RepriceTask PowerAccount=["+rp.getPowerAccount()+"] and DeviceID=["+rp.getDeviceID()+"] and StartDate=["+rp.getStartDate()+"] PowerRecordCollection, FcstCharge, PowerMonth, BestCC, BestRatePlan");
		}
		List<RepriceTaskModel> list = repeatCancelMap.values().stream().collect(Collectors.toList());
		updateRepriceTaskCancel(list);
	}
	public void doCollection() {
		logger.info("--------------------------------------------------");
		logger.info("~~~~~~~~~~~~ do PowerRecordCollection ~~~~~~~~~~~~");
		logger.info("--------------------------------------------------");
		for (String deviceID: reMap.keySet()) {
			RepriceTaskModel rp = reMap.get(deviceID);
			String powerAccount = rp.getPowerAccount();
			String startDate = rp.getStartDate();
			
			if (minMap.containsKey(powerAccount)) {
				startDate = ToolUtil.getInstance().convertDateToString(minMap.get(powerAccount), "yyyy-MM-dd");
			}
			
			String[] args = new String[2];
			args[0] = startDate;
			args[1] = deviceID;

			threadPool.execute(new RePrice(args, rp, RepriceFrom_Collection, false));
		}
		for (String deviceID: paMap.keySet()) {
			RepriceTaskModel rp = paMap.get(deviceID);
			String powerAccount = rp.getPowerAccount();
			String startDate = rp.getStartDate();

			if (minMap.containsKey(powerAccount)) {
				startDate = ToolUtil.getInstance().convertDateToString(minMap.get(powerAccount), "yyyy-MM-dd");
			}

			String[] args = new String[2];
			args[0] = startDate;
			args[1] = deviceID;

			threadPool.execute(new RePrice(args, rp, RepriceFrom_Collection, true));
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void doFcstCharge() {
		logger.info("---------------------------------------------------");
		logger.info("~~~~~~~~~~~~~~~~~~ do FcstCharge ~~~~~~~~~~~~~~~~~~");
		logger.info("---------------------------------------------------");
		for (String deviceID: reMap.keySet()) {
			RepriceTaskModel rp = reMap.get(deviceID);
			String powerAccount = rp.getPowerAccount();
			String startDate = rp.getStartDate();
			
			if (minMap.containsKey(powerAccount)) {
				startDate = ToolUtil.getInstance().convertDateToString(minMap.get(powerAccount), "yyyy-MM-dd");
			}
			
			String[] args = new String[2];
			args[0] = startDate;
			args[1] = deviceID;

			threadPool2.execute(new RePrice(args, rp, RepriceFrom_FcstCharge, false));
		}
		for (String powerAccount: paDeviceMap.keySet()) {
			List<String> deviceList = paDeviceMap.get(powerAccount);
			if (deviceList.size()!=0) {
				// 取此電號的一個device做代表送至FcstCharge, PowerAccount, ...即依原規則統計
				String deviceID = deviceList.get(0);
				
				RepriceTaskModel rp = paMap.get(deviceID);
				String startDate = rp.getStartDate();

				if (minMap.containsKey(powerAccount)) {
					startDate = ToolUtil.getInstance().convertDateToString(minMap.get(powerAccount), "yyyy-MM-dd");
				}

				String[] args = new String[2];
				args[0] = startDate;
				args[1] = deviceID;

				threadPool2.execute(new RePrice(args, rp, RepriceFrom_FcstCharge, true));
			}
		}
		threadPool2.shutdown();
		try {
			threadPool2.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public class RePrice extends BaseFunction implements Runnable {

		private String[] args;
		private RepriceTaskModel rp;
		private String repriceFrom;
		private boolean isDeviceNULL;
		
		public RePrice(String[] args, RepriceTaskModel rp, String repriceFrom, boolean isDeviceNULL) {
			this.args = args;
			this.rp = rp;
			this.repriceFrom = repriceFrom;
			this.isDeviceNULL = isDeviceNULL;
		}

		@Override
		public void run() {
			// 做Reprice => 非正常流程
			setIsReprice(true);

			String powerAccount = rp.getPowerAccount();
			
			String startdate = args[0];
			String deviceID = args[1];
			
			if (repriceFrom.equals(RepriceFrom_Collection)) {
				logger.info("Do Reprice 'PowerRecordCollection' StartDate=["+startdate+"], DeviceID=["+deviceID+"], (PowerAccount="+rp.getPowerAccount()+")");
				PowerRecordCollectionTask.main(args);
				
			} else {
				if (!countFcstChargeTimes.containsKey(powerAccount)) {
					countFcstChargeTimes.put(powerAccount, rp);

					logger.info("Do Reprice 'FcstCharge' StartDate=["+startdate+"], DeviceID=["+deviceID+"], (PowerAccount="+rp.getPowerAccount()+")");
					// FcstCharge
					FcstChargeTask.main(args);
					// KPI
					KPITask.main(args);
					// PowerMonth
					PowerMonthTask.main(args);
					// BestRatePlan
					BestRatePlanTask.main(args);
					// BestCC
					BestCCTask.main(args);
					
				} else {
					logger.info("Skip Reprice FcstCharge StartDate=["+startdate+"], DeviceID=["+deviceID+"], (PowerAccount="+rp.getPowerAccount()+"). Repeat");
				}
				// MaxDemand
				MaxDemandTask.main(args);
				

				calculateFinish(deviceID, powerAccount, rp, isDeviceNULL);
			}
		}
		
		private void calculateFinish(String deviceID, String powerAccount, RepriceTaskModel rp, boolean isDeviceNULL) {
			String startDate = rp.getStartDate();

			List<String> failedDeviceList = getFailedDevice();
			List<String> failedPowerAccount = getFailedPowerAccount();
			
			if (!isDeviceNULL) {
				// 有deviceID & powerAccount
				if (!failedDeviceList.contains(deviceID) && !failedPowerAccount.contains(powerAccount)) {
					logger.info("重算電費成功: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
					updatePowerAccountModifyStatus(powerAccount, ModifyStatus_Default);
					updateMeterSetupRepriceStatus(deviceID, RepriceStatus_Default);
					updateRepriceTaskStatus(TaskStatusCode_Finish, deviceID, powerAccount, startDate);
					
				} else {
					logger.error("重算Collection, FcstCharge及後續動作失敗, DeviceID=["+deviceID+"], PowerAccount=["+powerAccount+"]");
					logger.error("重算電費失敗: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
					updateRepriceTaskStatus(TaskStatusCode_Failed, deviceID, powerAccount, startDate);
					sendRepriceTaskFailed(rp, null);
				}
				
			} else {
				// 只有powerAccount
				boolean isSuccess = true;
				
				List<String> deviceList = paDeviceMap.get(powerAccount);
				for (String device: deviceList) {
					if (failedDeviceList.contains(device)) {
						// 重作Collection出現錯誤
						logger.error("重算Collection失敗, DeviceID=["+deviceID+"]");
						isSuccess = false;
						break;
					}
				}
				
				if (failedPowerAccount.contains(powerAccount)) {
					logger.error("重算FcstCharge後續動作失敗, PowerAccount=["+powerAccount+"]");
					isSuccess = false;
				}
				
				if (isSuccess) {
					logger.info("重算電費成功: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
					updatePowerAccountModifyStatus(powerAccount, ModifyStatus_Default);
					updateMeterSetupRepriceStatus(deviceID, RepriceStatus_Default);
					updateRepriceTaskStatus(TaskStatusCode_Finish, null, powerAccount, startDate);
				} else {
					logger.error("重算電費失敗: "+JsonUtil.getInstance().convertObjectToJsonstring(rp));
					updateRepriceTaskStatus(TaskStatusCode_Failed, null, powerAccount, startDate);
					sendRepriceTaskFailed(rp, null);
				}
			}
		}
	}

	private RepriceTaskModel setRepriceTaskModel(RepriceTaskModel rp, String deviceID, String powerAccount) {
		RepriceTaskModel nrp = new RepriceTaskModel();
		nrp.setSeqno(rp.getSeqno());
		nrp.setDeviceID(deviceID);	//
		nrp.setPowerAccount(powerAccount);	// 
		nrp.setRepriceFrom(rp.getRepriceFrom());
		nrp.setStartDate(rp.getStartDate());
		
		return nrp;
	}
	private void setCollection(RepriceTaskModel rp) {
		String powerAccount = rp.getPowerAccount();
		String deviceID = rp.getDeviceID();
		Date startDate = ToolUtil.getInstance().convertStringToDate(rp.getStartDate(), "yyyy-MM-dd");

		// 將重複的存起來做取消
		if (reMap.containsKey(deviceID)) {
			repeatCancelMap.put(deviceID, reMap.get(deviceID));
		}
		// 重作collection的device
		reMap.put(deviceID, rp);
		
		// 紀錄重作的device的powerAccount再RepriceTask內最小的startDate
		if (!minMap.containsKey(powerAccount)) {
			// 過濾不重複的DeviceID (避免系統有錯，同一deviceID有多筆task)
			minMap.put(powerAccount, startDate);
			
		} else {
			Date minDate = minMap.get(powerAccount);
			if (startDate.compareTo(minDate)==-1) {
				// 找到更小的startDate => 更新紀錄
				minMap.put(powerAccount, startDate);
			}
		}
	}
	private void setCollectionPA(RepriceTaskModel rp) {
		String powerAccount = rp.getPowerAccount();
		String deviceID = rp.getDeviceID();
		Date startDate = ToolUtil.getInstance().convertStringToDate(rp.getStartDate(), "yyyy-MM-dd");

		// 將重複的存起來做取消
		if (paMap.containsKey(deviceID)) {
			repeatCancelMap.put(deviceID, paMap.get(deviceID));
		}
		// 重作collection的device
		paMap.put(deviceID, rp);
		
		// 紀錄重作的device的powerAccount再RepriceTask內最小的startDate
		if (!minMap.containsKey(powerAccount)) {
			// 過濾不重複的DeviceID (避免系統有錯，同一deviceID有多筆task)
			minMap.put(powerAccount, startDate);
			
		} else {
			Date minDate = minMap.get(powerAccount);
			if (startDate.compareTo(minDate)==-1) {
				// 找到更小的startDate => 更新紀錄
				minMap.put(powerAccount, startDate);
			}
		}

		// 統整同一個PowerAccount的Device
		if (!paDeviceMap.containsKey(powerAccount)) {
			List<String> deviceList = new ArrayList<>();
			deviceList.add(deviceID);
			paDeviceMap.put(powerAccount, deviceList);
		} else {
			List<String> deviceList = paDeviceMap.get(powerAccount);
			if (!deviceList.contains(deviceID)) {
				deviceList.add(deviceID);
			}
		}
	}

	/**
	 *	依statusCode查詢RepriceTaskList
	 *
	 * @param statusCode
	 * @return
	 */
	private List<RepriceTaskModel> getRepriceTask(int statusCode) {
		List<RepriceTaskModel> list = DBQueryUtil.getInstance().queryRepriceTask(statusCode);
		return list;
	}
}
