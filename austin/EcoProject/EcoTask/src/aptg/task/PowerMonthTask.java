package aptg.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.PowerMonthDao;
import aptg.manager.PowerRecordManager;
import aptg.models.FcstChargeModel;
import aptg.models.PowerAccountModel;
import aptg.models.PowerMonthModel;
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 	每月統整PowerMonth
 * 	正常流程：只需統整上一個月即可 (可能有補資料狀況 => 改為由補資料月份開始重新統整)
 * 	補作流程：統整指定月份~上一個月份
 * 
 * @author austinchen
 *
 */
public class PowerMonthTask extends BaseFunction {

	private static final String CLASS_NAME = PowerMonthTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private List<PowerMonthModel> insertList = new ArrayList<>();
	private List<PowerMonthModel> updateList = new ArrayList<>();
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		PowerMonthTask task = new PowerMonthTask();

		Calendar startCal = Calendar.getInstance();			// ex: 2021-08-XX
		Calendar endCal = ToolUtil.getInstance().getThisMonthStart();	// ex: 2021-08-01 00:00:00
		int startMonth = ToolUtil.getInstance().getMonth(startCal.getTime());	// ex: startCal=2021-08-XX => 8
		
		if (args.length==0) {
			/*
			 * 	正常流程：只需統整上一個月即可
			 */
			startCal.add(Calendar.MONTH, -1);	// 上個月	ex: 2021-07-XX

			// 檢查所有PowerAccount
			List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
			for (PowerAccountModel pa: paList) {
				String powerAccount = pa.getPowerAccount();

				Date recTime = PowerRecordManager.getInstance().getFirstPowerAccountRecord(powerAccount);
				if (recTime!=null) {
					int beforeMonth = ToolUtil.getInstance().getMonth(recTime);
					/*
					 * 	補資料狀況(以月為單位) => 改為由補資料月份開始重新統整
					 */
					if (beforeMonth<=startMonth)
						startCal.setTime(recTime);
				}

				logger.info("=============== PowerAccount=["+powerAccount+"], 自 "+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM")+" 開始統計PowerMonth");
				// ex: 現為2020-09 => start=2020-03 ~ end=2020-09
				// ex: 現為2020-09 => start=2020-08 ~ end=2020-09 (start<end 只做到8月)
				Calendar cal = Calendar.getInstance();
				cal.setTime(startCal.getTime());
				for ( ; cal.compareTo(endCal)==-1 ; cal.add(Calendar.MONTH, 1)) {
//				for ( ; startCal.compareTo(endCal)==-1 ; startCal.add(Calendar.MONTH, 1)) {
					String useMonth = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyyMM");
					
					int year = ToolUtil.getInstance().getYear(cal.getTime());
					int month = ToolUtil.getInstance().getMonth(cal.getTime());
					
					Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);			// key: powerAccount
					Map<String, FcstChargeModel> fcMap = DBQueryUtil.getInstance().getMaxUseTimeFcstCharge(year, month);	// key: powerAccount
					
					logger.info("PowerAccount=["+powerAccount+"], 統計 "+useMonth+" 用電月統計");
					task.doPowerMonth(useMonth, pa, pmMap, fcMap);
				}
			}

		} else {
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定月份
			
			String param = (args.length>=2) ? args[1] : null;
			List<PowerAccountModel> poweraccountList = new ArrayList<>();
			if (StringUtils.isNotBlank(param) && param.equals("all")) {
//				deviceID = null;
				poweraccountList = task.getAllPowerAccount();
				logger.info("All PowerAccount");
				
			} else if (StringUtils.isNotBlank(param) && param.indexOf("BankCode=")!=-1) {
				String bankCode  = param.split("=")[1];
				poweraccountList = task.getPowerAccountByBank(bankCode);
				logger.info("BankCode=["+bankCode +"], PowerAccount: "+JsonUtil.getInstance().convertObjectToJsonstring(poweraccountList));
				
			} else {
				poweraccountList = task.getPowerAccountByDID(param);
				logger.info("DeviceID=["+param +"], PowerAccount: "+JsonUtil.getInstance().convertObjectToJsonstring(poweraccountList));
			}

			/*
			 */
			String endDate = (args.length>=3) ? args[2] : null;
			if (StringUtils.isNotBlank(endDate)) {
				Date calculateEndDate = ToolUtil.getInstance().convertStringToDate(endDate, "yyyy-MM-dd");
				endCal.setTime(calculateEndDate);
				endCal = ToolUtil.getInstance().getNextMonthStart(endCal);
			}
			
			// ex: 現為2020-09 => start=2020-03 ~ end=2020-09
			// ex: 現為2020-09 => start=2020-08 ~ end=2020-09 (start<end)
			Calendar cal = Calendar.getInstance();
			cal.setTime(startCal.getTime());
			for ( ; cal.compareTo(endCal)==-1 ; cal.add(Calendar.MONTH, 1)) {
//			for (Calendar cal=startCal ; cal.compareTo(endCal)==-1 ; cal.add(Calendar.MONTH, 1)) {
				String useMonth = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyyMM");
				
				int year = ToolUtil.getInstance().getYear(cal.getTime());
				int month = ToolUtil.getInstance().getMonth(cal.getTime());

				Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);			// Table PowerMonth紀錄 key: powerAccount
				Map<String, FcstChargeModel> fcMap = DBQueryUtil.getInstance().getMaxUseTimeFcstCharge(year, month);	// Table FcstCharge時間最大一筆紀錄 key: powerAccount
				
				
				for (PowerAccountModel pa: poweraccountList) {
					String powerAccount = pa.getPowerAccount();
					logger.info("指定月份["+month+"], PowerAccount=["+powerAccount+"] 重新統計用電月統計");
					
					task.doPowerMonth(useMonth, pa, pmMap, fcMap);
				}
				
				
//				if (!StringUtils.isBlank(deviceID)) {
//					// 檢查指定DeviceID的PowerAccount
//					MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(deviceID);
//					logger.info("指定月份["+month+"], 指定DeviceID=["+deviceID+"], PowerAccount=["+meter.getPowerAccount()+"] 重新統計用電月統計");
//
//					PowerAccountModel pa = DBQueryUtil.getInstance().getPowerAccount(meter.getPowerAccount());
//					if (pa!=null) {
//						task.doPowerMonth(useMonth, pa, pmMap, fcMap);
//					} else {
//						logger.error("指定DeviceID=["+deviceID+"], 查無此 PowerAccount=["+meter.getPowerAccount()+"]");
//					}
//					
//				} else {
//					// 檢查所有PowerAccount
//					List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
//					for (PowerAccountModel pa: paList) {
//						String powerAccount = pa.getPowerAccount();
//						logger.info("指定月份["+useMonth+"], 重新統計 PowerAccount=["+powerAccount+"] 用電月統計");
//						
//						task.doPowerMonth(useMonth, pa, pmMap, fcMap);
//					}
//				}
			}
		}
		// insert or update PowerMonth
		task.updateInsertPowerMonth();
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void doPowerMonth(String useMonth, PowerAccountModel pa, Map<String, PowerMonthModel> pmMap, Map<String, FcstChargeModel> fcMap) {
		String powerAccount = pa.getPowerAccount();

		if (fcMap.containsKey(powerAccount)) {
			// 找出最大時間一筆FcstCharge
			FcstChargeModel fcst = fcMap.get(powerAccount);
			PowerMonthModel pm = setPowerMonth(fcst);

			if (pmMap.containsKey(powerAccount)) {
				// 已有紀錄
				updateList.add(pm);
				logger.info("PowerAccount=["+powerAccount+"], useMonth=["+useMonth+"], Update PowerMonth: "+ JsonUtil.getInstance().convertObjectToJsonstring(pm));
			} else {
				// 從未有無紀錄
				insertList.add(pm);
				logger.info("PowerAccount=["+powerAccount+"], useMonth=["+useMonth+"], Insert PowerMonth: "+ JsonUtil.getInstance().convertObjectToJsonstring(pm));
			}
		} else {
			logger.error("PowerAccount=["+powerAccount+"], useMonth=["+useMonth+"], can't find FcstCharge info");
		}
	}
	
	private PowerMonthModel setPowerMonth(FcstChargeModel fcst) {
		PowerMonthModel pm = new PowerMonthModel();
		
		pm.setPowerAccount(fcst.getPowerAccount());
		pm.setUseMonth(fcst.getUseMonth());
		pm.setRatePlanCode(fcst.getRatePlanCode());
		
		pm.setUsuallyCC(fcst.getUsuallyCC());
		pm.setSPCC(fcst.getSPCC());
		pm.setSatSPCC(fcst.getSatSPCC());
		pm.setOPCC(fcst.getOPCC());
		
		pm.setMDemandPK(fcst.getMDemandPK());
		pm.setMDemandSP(fcst.getMDemandSP());
		pm.setMDemandSatSP(fcst.getMDemandSatSP());
		pm.setMDemandOP(fcst.getMDemandOP());
		
		pm.setMCECPK(fcst.getMCECPK());
		pm.setMCECSP(fcst.getMCECSP());
		pm.setMCECSatSP(fcst.getMCECSatSP());
		pm.setMCECOP(fcst.getMCECOP());
		pm.setMCEC(fcst.getMCEC());
		
		pm.setRealPlan(fcst.getRealPlan());
		
//		pm.setTPMDemandPK(fcst.getTPMDemandPK());
//		pm.setTPMDemandSP(fcst.getTPMDemandSP());
//		pm.setTPMDemandSatSP(fcst.getTPMDemandSatSP());
//		pm.setTPMDemandOP(fcst.getTPMDemandOP());
//		
//		pm.setOverPK(fcst.getOverPK());
//		pm.setOverSP(fcst.getOverSP());
//		pm.setOverSatSP(fcst.getOverSatSP());
//		pm.setOverOP(fcst.getOverOP());
		
		return pm;
	}
	
	private void updateInsertPowerMonth() {
		PowerMonthDao dao = new PowerMonthDao();

		if (getIsReprice()) {
			// 是reprice => 單筆寫入
			for (PowerMonthModel pm: insertList) {
				List<PowerMonthModel> list = new ArrayList<>();
				list.add(pm);
				List<Integer> ids = dao.insertPowerMonth(list);
				if (ids==null) {
					setFailedPowerAccount(pm.getPowerAccount());
					logger.error("##### Reprice PowerMonthTask Failed, PowerAccount=["+pm.getPowerAccount()+"]");
				} else {
					logger.info("Reprice PowerMonthTask success insert PowerAccount=["+pm.getPowerAccount()+"]");
				}
			}
			for (PowerMonthModel pm: updateList) {
				List<PowerMonthModel> list = new ArrayList<>();
				list.add(pm);
				int count = dao.updatePowerMonth(list);
				if (count==-1) {
					setFailedPowerAccount(pm.getPowerAccount());
					logger.error("##### Reprice PowerMonthTask Failed, PowerAccount=["+pm.getPowerAccount()+"]");
				} else {
					logger.info("Reprice PowerMonthTask success update PowerAccount=["+pm.getPowerAccount()+"]");
				}
			}
		} else {
			// 不是reprice，正常流程 => 批次寫入

			List<Integer> ids = (insertList.size()!=0) ? dao.insertPowerMonth(insertList) : new ArrayList<>();
			logger.info("PowerMonthTask success insertList size: "+ ids.size());

			int count = (updateList.size()!=0) ? dao.updatePowerMonth(updateList) : 0;
			logger.info("PowerMonthTask success updateList size: "+ count);
		}
		
		insertList.clear();
		updateList.clear();
	}
}
