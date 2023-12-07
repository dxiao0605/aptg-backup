package aptg.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.BestRatePlanDao;
import aptg.manager.PowerRecordManager;
import aptg.models.BestRatePlanModel;
import aptg.models.FcstChargeModel;
import aptg.models.PowerAccountModel;
import aptg.models.PowerMonthModel;
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
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 	將所有PowerAccount套進所有RatePlan計算最適電費
 * 	正常流程：
 * 	補作流程：
 * 
 * @author austinchen
 *
 */
public class BestRatePlanTask extends BaseFunction {

	private static final String CLASS_NAME = BestRatePlanTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final List<Integer> RateList1 = Arrays.asList(1,2,3,4,5,6,7);
	private static final List<Integer> RateList2 = Arrays.asList(8,9);
	
	private List<BestRatePlanModel> insertList = new ArrayList<>();
	private List<BestRatePlanModel> updateList = new ArrayList<>();
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		BestRatePlanTask task = new BestRatePlanTask();

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = ToolUtil.getInstance().getThisMonthStart();
		int startMonth = ToolUtil.getInstance().getMonth(startCal.getTime());
		
		if (args.length==0) {
			/*
			 * 	正常流程：只需統整上一個月即可
			 */
			startCal.add(Calendar.MONTH, -1);	// 上個月

			// 檢查所有PowerAccount
			List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
			for (PowerAccountModel pa: paList) {
				String powerAccount = pa.getPowerAccount();

				Date recTime = PowerRecordManager.getInstance().getFirstPowerAccountRecord(powerAccount);
				if (recTime!=null && recTime.compareTo(endCal.getTime())==-1) {
					int beforeMonth = ToolUtil.getInstance().getMonth(recTime);
					/*
					 * 	補資料狀況(以月為單位) => 改為由補資料月份開始重新統整
					 */
					if (beforeMonth<=startMonth)
						startCal.setTime(recTime);
				}

				// ex: 現為2020-09 => start=2020-03 ~ end=2020-09
				// ex: 現為2020-09 => start=2020-08 ~ end=2020-09 (start<end 只做到8月)
				Calendar cal = Calendar.getInstance();
				cal.setTime(startCal.getTime());
				for ( ; cal.compareTo(endCal)==-1 ; cal.add(Calendar.MONTH, 1)) {
					String useMonth = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyyMM");

					logger.info("PowerAccount=["+powerAccount+"], 統計["+useMonth+"]最適電費");
					task.bestCalculate(useMonth, pa);
				}
			}
			
		} else {
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定月份

//			String deviceID = (args.length>=2) ? args[1] : null;
//			if (StringUtils.isNotBlank(deviceID) && deviceID.equals("all")) {
//				deviceID = null;
//			}
			String param = (args.length>=2) ? args[1] : null;
			List<PowerAccountModel> poweraccountList = new ArrayList<>();
			if (StringUtils.isNotBlank(param) && param.equals("all")) {
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
//				int month = ToolUtil.getInstance().getMonth(useMonth);

				
				for (PowerAccountModel pa: poweraccountList) {
					String powerAccount = pa.getPowerAccount();
					logger.info("指定月份["+useMonth+"], 重新統計 PowerAccount=["+powerAccount+"] 最適電費");

					task.bestCalculate(useMonth, pa);
				}
				
				
//				if (!StringUtils.isBlank(deviceID)) {
//					// 檢查指定DeviceID的PowerAccount
//					MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(deviceID);
//					logger.info("指定DeviceID=["+deviceID+"], PowerAccount=["+meter.getPowerAccount()+"], 重新統計["+useMonth+"]用電月統計");
//
//					PowerAccountModel pa = DBQueryUtil.getInstance().getPowerAccount(meter.getPowerAccount());
//					if (pa!=null) {
//						task.bestCalculate(useMonth, pa);
//					} else {
//						logger.error("指定DeviceID=["+deviceID+"], 查無此 PowerAccount=["+meter.getPowerAccount()+"]");
//					}
//					
//				} else {
//					// 檢查所有PowerAccount
//					List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
//					for (PowerAccountModel pa: paList) {
//						String powerAccount = pa.getPowerAccount();
//						logger.info("指定月份["+useMonth+"], 重新統計 PowerAccount=["+powerAccount+"] 最適電費");
//
//						task.bestCalculate(useMonth, pa);
//					}
//				}
			}
		}
		// insert or update BestRatePlan
		task.updateInsertBestRatePlan();
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	/**
	 * 	依RateList內所設定的code計算電費
	 * 
	 * @param useMonth
	 * @param pa
	 */
	private void bestCalculate(String useMonth, PowerAccountModel pa) {
		String powerAccount = pa.getPowerAccount();
		int powerphase = pa.getPowerPhase();

		Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);			// key: powerAccount
		if (pmMap.containsKey(powerAccount)) {
			PowerMonthModel pm = pmMap.get(powerAccount);
			int inUsePlanCode = pm.getRatePlanCode();	// 目前使用中的ratePlanCode
			
			List<Integer> RateList;
			if (RateList2.contains(inUsePlanCode))
				RateList = RateList2;
			else
				RateList = RateList1;
				
			// 所有ratePlan計算
			for (int tryPlanCode: RateList) {
				// 準備計算電費內容
				FcstChargeModel model = setFcstChargeModel(pm, powerphase);
				
				// 更換成欲嘗試試算的ratePlan
				model.setRatePlanCode(tryPlanCode);
				
				// 分類計算電費
				FcstChargeModel fcst = classifyRatePlan(model, inUsePlanCode);

				// 存入最適電費內容
				BestRatePlanModel best = setBestRatePlanModel(fcst, inUsePlanCode);
				logger.info("PowerAccount=["+powerAccount+"], in using RatePlanCode=["+inUsePlanCode+"], if RatePlanCode=["+tryPlanCode+"], BestRatePlan: "+JsonUtil.getInstance().convertObjectToJsonstring(best));
				
				// 查詢是否已有 最適電費紀錄
				BestRatePlanModel bestHistory = DBQueryUtil.getInstance().getBestRatePlan(powerAccount, useMonth, best.getRatePlanCode());
				if (bestHistory==null) {
					// 尚未計算 => insert
					insertList.add(best);
				} else {
					// 已有 => update
					updateList.add(best);
				}
			}
					
		} else {
			logger.error("PowerAccount=["+powerAccount+"] , useMonth=["+useMonth+"] can't find PowerMonth info");
		}
	}

	public FcstChargeModel classifyRatePlan(FcstChargeModel model, int inUsePlanCode) {
		int tryPlanCode = model.getRatePlanCode();
		
		FcstChargeModel fcst = null;
		switch (tryPlanCode) {
			// 1: 表燈營業用 
			case 1:
				RatePlan1 rate1 = new RatePlan1(model);
				fcst = rate1.calculateTotalCharge();
				break;
				
			// 2: 表燈非營業用 
			case 2:
				RatePlan2 rate2 = new RatePlan2(model);
				fcst = rate2.calculateTotalCharge();
				break;
				
			// 3: 表燈簡易二段式 
			case 3:
				RatePlan3 rate3 = new RatePlan3(model);
				fcst = rate3.calculateTotalCharge();
				break;
				
			// 4: 表燈簡易三段式 
			case 4:
				RatePlan4 rate4 = new RatePlan4(model);
				fcst = rate4.calculateTotalCharge();
				break;
				
			// 5: 表燈標準二段式 
			case 5:
				RatePlan5 rate5 = new RatePlan5(model);
				fcst = rate5.calculateTotalCharge();
				break;
				
			// 6: 低壓非時間
			case 6:
				RatePlan6 rate6 = new RatePlan6(model);
				fcst = rate6.calculateTotalCharge();
				break;
				
			// 7: 低壓二段式
			case 7:
				RatePlan7 rate7 = new RatePlan7(model);
				fcst = rate7.calculateTotalCharge();
				break;
				
			// 8: 高壓二段式
			case 8:
				RatePlan8 rate8 = new RatePlan8(model);
				fcst = rate8.calculateTotalCharge();
				break;
				
			// 9: 高壓三段式
			case 9:
				RatePlan9 rate9 = new RatePlan9(model);
				fcst = rate9.calculateTotalCharge();
				break;
				
			// 21: 自訂A-分攤
			case 21:
				RatePlan21 rate21 = new RatePlan21(model);
				fcst = rate21.calculateTotalCharge();
				break;
				
			// 22: 自訂B-公用分攤
			case 22:
				RatePlan22 rate22 = new RatePlan22(model);
				fcst = rate22.calculateTotalCharge();
				break;
				
			// 23: 自訂C-空調分攤
			case 23:
				RatePlan23 rate23 = new RatePlan23(model);
				fcst = rate23.calculateTotalCharge();
				break;
		}
		return fcst;
	}
	
	public FcstChargeModel setFcstChargeModel(PowerMonthModel pm, int powerphase) {
		FcstChargeModel fcst = new FcstChargeModel();
		
		fcst.setPowerAccount(pm.getPowerAccount());
		fcst.setUseMonth(pm.getUseMonth());

		Date useMonth = ToolUtil.getInstance().convertStringToDate(pm.getUseMonth(), "yyyyMM");
		fcst.setUseTime(ToolUtil.getInstance().convertDateToString(useMonth, "yyyy-MM"));	// *設定同useMonth
		
		fcst.setRatePlanCode(pm.getRatePlanCode());
		
		fcst.setUsuallyCC(pm.getUsuallyCC());	//45
		fcst.setSPCC(pm.getSPCC());				//0
		fcst.setSatSPCC(pm.getSatSPCC());		//0 
		fcst.setOPCC(pm.getOPCC());				//0
		
		fcst.setMDemandPK(pm.getMDemandPK());	//0
		fcst.setMDemandSP(pm.getMDemandSP());	//28.78
		fcst.setMDemandSatSP(pm.getMDemandSatSP());	//6.83
		fcst.setMDemandOP(pm.getMDemandOP());	//5.69
		
		fcst.setMCECPK(pm.getMCECPK());			//0
		fcst.setMCECSP(pm.getMCECSP());			//4503.32
		fcst.setMCECSatSP(pm.getMCECSatSP());	//326.16
		fcst.setMCECOP(pm.getMCECOP());			//1334.29
		fcst.setMCEC(pm.getMCEC());				//6163.77
		
		fcst.setPowerPhase(powerphase);			//3
		
		fcst.setRealPlan(pm.getRealPlan());		//6
		
		return fcst;
	}
	
	private BestRatePlanModel setBestRatePlanModel(FcstChargeModel fcst, int inUsePlanCode) {
		BestRatePlanModel best = new BestRatePlanModel();

		best.setPowerAccount(fcst.getPowerAccount());
		best.setUseMonth(fcst.getUseMonth());
		best.setInUse( (inUsePlanCode==fcst.getRatePlanCode()) ? 1 : 0 );	// 是否即為使用中的ratePlanCode
		best.setRatePlanCode(fcst.getRatePlanCode());
		
		best.setUsuallyCC(fcst.getUsuallyCC());
		best.setSPCC(fcst.getSPCC());
		best.setSatSPCC(fcst.getSatSPCC());
		best.setOPCC(fcst.getOPCC());
		
		best.setTPMDemandPK(fcst.getTPMDemandPK());
		best.setTPMDemandSP(fcst.getTPMDemandSP());
		best.setTPMDemandSatSP(fcst.getTPMDemandSatSP());
		best.setTPMDemandOP(fcst.getTPMDemandOP());
		
		best.setTPMCECPK(fcst.getTPMCECPK());
		best.setTPMCECSP(fcst.getTPMCECSP());
		best.setTPMCECSatSP(fcst.getTPMCECSatSP());
		best.setTPMCECOP(fcst.getTPMCECOP());
		best.setTPMCEC(fcst.getTPMCEC());
		
		best.setBaseCharge(fcst.getBaseCharge());
		best.setUsageCharge(fcst.getUsageCharge());
		best.setOverCharge(fcst.getOverCharge());
		best.setTotalCharge(fcst.getTotalCharge());
		
		best.setOverPK(fcst.getOverPK());
		best.setOverSP(fcst.getOverSP());
		best.setOverSatSP(fcst.getOverSatSP());
		best.setOverOP(fcst.getOverOP());
		
		best.setRealPlan(fcst.getRealPlan());
		
		return best;
	}

	private void updateInsertBestRatePlan() {
		BestRatePlanDao dao = new BestRatePlanDao();

		if (getIsReprice()) {
			// 是reprice => 單筆寫入
			for (BestRatePlanModel br: insertList) {
				List<BestRatePlanModel> list = new ArrayList<>();
				list.add(br);
				List<Integer> ids = dao.insertBestRatePlan(list);
				if (ids==null) {
					setFailedPowerAccount(br.getPowerAccount());
					logger.error("##### Reprice BestRatePlanTask Failed, PowerAccount=["+br.getPowerAccount()+"]");
				} else {
					logger.info("Reprice BestRatePlanTask success insert PowerAccount=["+ br.getPowerAccount()+"]");
				}
			}
			for (BestRatePlanModel br: updateList) {
				List<BestRatePlanModel> list = new ArrayList<>();
				list.add(br);
				int count = dao.updateBestRatePlan(list);
				if (count==-1) {
					setFailedPowerAccount(br.getPowerAccount());
					logger.error("##### Reprice BestRatePlanTask Failed, PowerAccount=["+br.getPowerAccount()+"]");
				} else {
					logger.info("Reprice BestRatePlanTask success update PowerAccount=["+br.getPowerAccount()+"]");
				}
			}
			
		} else {
			// 不是reprice，正常流程 => 批次寫入

			List<Integer> ids = (insertList.size()!=0) ? dao.insertBestRatePlan(insertList) : new ArrayList<>();
			logger.info("BestRatePlanTask success insertList size: "+ ids.size());

			int count = (updateList.size()!=0) ? dao.updateBestRatePlan(updateList) : 0;	
			logger.info("BestRatePlanTask success updateList size: "+ count);
		}
		
		insertList.clear();
		updateList.clear();
	}
}
