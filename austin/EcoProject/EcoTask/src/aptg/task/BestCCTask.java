package aptg.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.MaxMinCCBean;
import aptg.beans.MaxUsuallyCCBean;
import aptg.dao.BestCCDao;
import aptg.manager.PowerRecordManager;
import aptg.manager.SysConfigManager;
import aptg.models.BestCCModel;
import aptg.models.FcstChargeModel;
import aptg.models.PowerAccountModel;
import aptg.models.PowerMonthModel;
import aptg.task.base.BaseFunction;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 	契約容量最佳化
 * 
 * @author austinchen
 *
 */
public class BestCCTask extends BaseFunction {

	private static final String CLASS_NAME = BestCCTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final List<Integer> RateList1 = Arrays.asList(5,6,7);
	private static final List<Integer> RateList2 = Arrays.asList(8,9);
	
	private static final String Sysconfig_MaxBestCCrows = "maxBestCCrows";
	private static Integer MaxBestCCrows;
	private int BestCCrowsRange = 5;

	private int recursiveCount;
	private int usuallyccA;
	private int C1;
	private List<BestCCModel> insertList = new ArrayList<>();
	private List<BestCCModel> updateList = new ArrayList<>();
	private List<MaxMinCCBean> deleteList = new ArrayList<>();
	
	private Map<String, BestCCModel> bestMap = new HashMap<>();
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		BestCCTask task = new BestCCTask();

		String sysconfig = SysConfigManager.getInstance().getSysconfig(Sysconfig_MaxBestCCrows);
		MaxBestCCrows = Integer.valueOf(sysconfig);
		
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
//				for ( ; startCal.compareTo(endCal)==-1 ; startCal.add(Calendar.MONTH, 1)) {
					String useMonth = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyyMM");

					Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);			// key: powerAccount (useMonth此月所有PowerAccount的PowerMonth資訊)
					
					logger.info("PowerAccount=["+powerAccount+"], 試算["+useMonth+"]契約容量最佳化");
					task.bestccCalculate(pmMap, pa);
					task.updateInsertBestCC();	// insert and update BestRatePlan
				}
			}
			
		} else {
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定月份
			
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
				Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);			// key: powerAccount (useMonth此月所有PowerAccount的PowerMonth資訊)


				for (PowerAccountModel pa: poweraccountList) {
					String powerAccount = pa.getPowerAccount();
					logger.info("指定月份["+useMonth+"], 重新試算 PowerAccount=["+powerAccount+"]契約容量最佳化");

					task.bestccCalculate(pmMap, pa);
					task.updateInsertBestCC();	// insert and update BestRatePlan.
				}
			}
		}
		

		List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
		task.calculateToMaxMinCC(paList);
		task.updateInsertBestCC();

		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}

	private void bestccCalculate(Map<String, PowerMonthModel> pmMap, PowerAccountModel pa) {
		String powerAccount = pa.getPowerAccount();
		int powerphase = pa.getPowerPhase();

		if (pmMap.containsKey(powerAccount)) {
			PowerMonthModel pm = pmMap.get(powerAccount);
			int inUsePlanCode = pm.getRatePlanCode();

			List<Integer> RateList;
			if (RateList2.contains(inUsePlanCode))
				RateList = RateList2;
			else
				RateList = RateList1;
			
			for (int rateplan: RateList) {
//				bestccCalculateRecursive(pm, powerphase, rateplan);
				bestccCalculate_Loop(pm, powerphase, rateplan);
			}
		}
	}

	private BestCCModel setBestCC(FcstChargeModel fcst) {
		BestCCModel bestCC = new BestCCModel();
		bestCC.setPowerAccount(fcst.getPowerAccount());
		bestCC.setUseMonth(fcst.getUseMonth());
		bestCC.setRatePlanCode(fcst.getRatePlanCode());
		bestCC.setUsuallyCC(fcst.getUsuallyCC());	// C1=A, C1=C0
		bestCC.setSPCC(fcst.getSPCC());
		bestCC.setSatSPCC(fcst.getSatSPCC());
		bestCC.setOPCC(fcst.getOPCC());
		bestCC.setBaseCharge(fcst.getBaseCharge());
		bestCC.setOverCharge(fcst.getOverCharge());
		bestCC.setOverPK(fcst.getOverPK());
		bestCC.setOverSP(fcst.getOverSP());
		bestCC.setOverSatSP(fcst.getOverSatSP());
		bestCC.setOverOP(fcst.getOverOP());
		bestCC.setRealPlan(fcst.getRealPlan());

		
		String key = bestCC.getPowerAccount() +"_"+ bestCC.getUseMonth() +"_"+ bestCC.getRatePlanCode() +"_"+ bestCC.getUsuallyCC();
		if (!bestMap.containsKey(key)) {
			// 查無紀錄 => 沒重複計算到
			bestMap.put(key, bestCC);
			classifyBestCC(bestCC);
		}
		return bestCC;
	}
	
	private void classifyBestCC(BestCCModel bestCC) {
		boolean isExist = DBQueryUtil.getInstance().isBestCCExist(bestCC.getPowerAccount(), bestCC.getUseMonth(), bestCC.getRatePlanCode(), bestCC.getUsuallyCC());
		if (isExist) {
			updateList.add(bestCC);
		} else {
			insertList.add(bestCC);
		}
	}
	
	private void updateInsertBestCC() {
		BestCCDao dao = new BestCCDao();

		if (getIsReprice()) {
			// 是reprice => 單筆寫入
			for (BestCCModel cc: insertList) {
				List<BestCCModel> list = new ArrayList<>();
				list.add(cc);
				List<Integer> ids = dao.insertBestCC(insertList);
				if (ids==null) {
					setFailedPowerAccount(cc.getPowerAccount());
					logger.error("##### Reprice BestCCTask Failed, PowerAccount=["+cc.getPowerAccount()+"]");
				} else {
					logger.info("Reprice BestCCTask success insert PowerAccount=["+cc.getPowerAccount()+"]");
				}
			}
			for (BestCCModel cc: updateList) {
				List<BestCCModel> list = new ArrayList<>();
				list.add(cc);
				int count = dao.updateBestCC(list);
				if (count==-1) {
					setFailedPowerAccount(cc.getPowerAccount());
					logger.error("##### Reprice BestCCTask Failed, PowerAccount=["+cc.getPowerAccount()+"]");
				} else {
					logger.info("Reprice BestCCTask success update PowerAccount=["+cc.getPowerAccount()+"]");
				}
			}
			
		} else {
			// 不是reprice，正常流程 => 批次寫入

			List<Integer> ids = (insertList.size()!=0) ? dao.insertBestCC(insertList) : new ArrayList<>();
			logger.info("BestCCTask success insertList size: "+ ids.size());
			
			int uCount = (updateList.size()!=0) ? dao.updateBestCC(updateList) : 0;
			logger.info("BestCCTask success updateList size: "+ uCount);
			
			int dCount = (deleteList.size()!=0) ? dao.deleteBestCC(deleteList) : 0;
			logger.info("BestCCTask success deleteList size: "+ dCount);
		}
		
		insertList.clear();
		updateList.clear();
		deleteList.clear();
	}
	
	

	/* ============================================================================================================================
	 * 									calculate BestCC Use Recursive
	  ============================================================================================================================ */
	/**
	 * 	計算該月的最佳契約容量
	 * 
	 * @param useMonth
	 * @param pa
	 * @param rateplan: 預計試算的RatePlanCode
	 */
	private void bestccCalculateRecursive(PowerMonthModel pm, int powerphase, int rateplan) {
		int D = 1;
		usuallyccA = 0;
		C1 = 0;
		recursiveCount = 0;

		/*
		 * ===== A ===== 
		 */
		usuallyccA = pm.getUsuallyCC();
		int A = pm.getUsuallyCC();

		// 準備計算電費內容
		BestRatePlanTask bt = new BestRatePlanTask();
		FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
		// 嘗試試算的RatePlanCode (5,6,7,8,9)
		model.setRatePlanCode(rateplan);

		// 計算原始契約的電費
		FcstChargeModel fcstB = bt.classifyRatePlan(model, model.getRatePlanCode());
		
		/*
		 * ===== B ===== 
		 */
		// 設定預設最佳契約容量資訊
		BestCCModel B = setBestCC(fcstB);
		logger.info("BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(B));
		
		/*
		 * ===== C1 ===== 
		 */
		C1 = A;
		
		try {
			recursiveCalculate(model, A, D, B);	
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("BestCC Calculate Error, Exception: \r\n"+e.getMessage());
		}
	}
	private int recursiveCalculate(FcstChargeModel model, int A, int D, BestCCModel B) {
		recursiveCount++;
		
		/*
		 * ===== C0 ===== 
		 */
		int C0 = A + D;		// C0 = A + (+-1)
		model.setUsuallyCC(C0);
		if (C0==-1) {
			logger.info("PowerAccount=["+model.getPowerAccount()+"], BestCC Calculate UsuallyCC is -1, finish !!!");
			return C1;
		}

		/*
		 * ===== E ===== 
		 */
		// 計算新契約容量的電費
		BestRatePlanTask bt = new BestRatePlanTask();
		FcstChargeModel fcstE = bt.classifyRatePlan(model, model.getRatePlanCode());
		// 設定預設最佳契約容量資訊
		BestCCModel E = setBestCC(fcstE);
		logger.info("BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
		
		
		if (recursiveCount>=MaxBestCCrows) {
			logger.info("PowerAccount=["+model.getPowerAccount()+"], BestCC Calculate is Over maxBestCCrows=["+MaxBestCCrows+"]");
			return C1;
		}
		
		// if (B>E)
		BigDecimal bestChargeB = B.getBaseCharge().add(B.getOverCharge());	// bestChargeB = baseCharge + overCharge
		BigDecimal bestChargeE = fcstE.getBaseCharge().add(fcstE.getOverCharge());	// bestChargeE = baseCharge + overCharge
		if (bestChargeB.compareTo(bestChargeE)==1) {
			// YES
			/*
			 * C1 = C0
			 */
			C1 = C0;
			
			/*
			 * update B = E
			 */
			B = E;
			
//			logger.info("%%%%%%%% new C1: "+C1);
//			logger.info("########### new B: "+JsonUtil.getInstance().convertObjectToJsonstring(B));

			// 契約容量再遞增(減), 計算再遞增(減)過後的契約容量的電費
			return recursiveCalculate(model, C0, D, B);

		} else {
			// NO
			if (C0>C1) {
				// 遞減契約容量
				D = 0 - D;

				return recursiveCalculate(model, usuallyccA, D, B);
			} else {
				// 找到最佳契約容量 => 結束
				logger.info("PowerAccount=["+B.getPowerAccount()+"], Find the Best UsuallyCC=["+C1+"]");
			}
		}

		return C1;
	}

	
	
	/* ============================================================================================================================
	 * 									calculate BestCC Use While Loop
	  ============================================================================================================================ */
	private void bestccCalculate_Loop(PowerMonthModel pm, int powerphase, int rateplan) {
		int D = 1;
		usuallyccA = 0;	// 紀錄最初usuallyCC設定
		C1 = 0;

		int spcc = pm.getSPCC();
		int satSPCC = pm.getSatSPCC();
		int opcc = pm.getOPCC();
		
		/*
		 * ===== A ===== 
		 */
		usuallyccA = pm.getUsuallyCC();
		int A = pm.getUsuallyCC();

		// 準備計算電費內容
		BestRatePlanTask bt = new BestRatePlanTask();
		FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
		// 嘗試試算的RatePlanCode (5,6,7,8,9)
		model.setRatePlanCode(rateplan);

		// 計算原始契約的電費
		FcstChargeModel fcstB = bt.classifyRatePlan(model, model.getRatePlanCode());
		
		/*
		 * ===== B ===== 
		 */
		// 設定預設最佳契約容量資訊
		BestCCModel B = setBestCC(fcstB);
		logger.info("初始 BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(B));
		
		/*
		 * ===== C1 ===== 
		 */
		C1 = A;			// 最佳契約容量
		int C0 = A + D;
		
		
		/*
		 * if 開始計算前 UsuallyCC+SPCC+SatSPCC+OPCC >= 300
		 * 		=> 紀錄一筆
		 * else
		 * 		=> 計算
		 */
		int totalCC = A + spcc + satSPCC + opcc;
		if (totalCC>=MaxBestCCrows) {
			logger.info("PowerAccount=["+B.getPowerAccount()+"], 初始值UsuallyCC+SPCC+SatSPCC+OPCC=["+totalCC+"] 即已達SysConfig MaxBestCCrows上限=["+MaxBestCCrows+"], UsuallyCC=["+C1+"] 結束計算");
			
		} else {

			while (true) {
				FcstChargeModel fcstE = loopCalculate(model, C0, D);
				// 設定預設最佳契約容量資訊
				BestCCModel E = setBestCC(fcstE);
				logger.info("計算出 BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
				
				// if (B>E)
				BigDecimal bestChargeB = B.getBaseCharge().add(B.getOverCharge());	// bestChargeB = baseCharge + overCharge
				BigDecimal bestChargeE = fcstE.getBaseCharge().add(fcstE.getOverCharge());	// bestChargeE = baseCharge + overCharge
				if (bestChargeB.compareTo(bestChargeE)==1) {
					// YES
					/*
					 * C1 = C0
					 */
					C1 = C0;
					
					// 契約容量再遞增(減), 計算再遞增(減)過後的契約容量的電費
					C0 = C0 + D;

					/*
					 * update B = E
					 */
					B = E;
					
				} else {
					// NO
					if (C0>C1) {
						// 遞減契約容量
						D = 0 - D;
						C0 = A + D;
					} else {
						// 找到最佳契約容量 => 結束
						logger.info("PowerAccount=["+B.getPowerAccount()+"], Find the Best UsuallyCC=["+C1+"]");
						break;
					}
				}
				
				if (C0==-1)
					break;
				

				totalCC = C1 + spcc + satSPCC + opcc;
				if (totalCC>=MaxBestCCrows) {
					logger.info("PowerAccount=["+B.getPowerAccount()+"], UsuallyCC+SPCC+SatSPCC+OPCC=["+totalCC+"], 已達SysConfig MaxBestCCrows上限=["+MaxBestCCrows+"], UsuallyCC=["+C1+"] 結束計算");
					break;
				}
			}
		}
		
		/*
		 * 左右多計算5筆
		 */
		calculateCC5rows(pm, powerphase, rateplan);
	}
	private FcstChargeModel loopCalculate(FcstChargeModel model, int C0, int D) {
//		recursiveCount++;

		/*
		 * ===== C0 ===== 
		 */
		model.setUsuallyCC(C0);

		/*
		 * ===== E ===== 
		 */
		// 計算新契約容量的電費
		BestRatePlanTask bt = new BestRatePlanTask();
		FcstChargeModel fcstE = bt.classifyRatePlan(model, model.getRatePlanCode());
		
		return fcstE;
	}
	
	private void calculateCC5rows(PowerMonthModel pm, int powerphase, int rateplan) {
		int spcc = pm.getSPCC();
		int satSPCC = pm.getSatSPCC();
		int opcc = pm.getOPCC();
		
		// 準備計算電費內容
		int upCC = C1+BestCCrowsRange;	// 最佳需量 向右多算五筆
		
		int totalCC = upCC + spcc + satSPCC + opcc;	// 總和不超過300
		// 如欲計算的usuallyCC上限(upCC)，和其他三個加總起來不到300 => 沒問題，直接計算
		// 如加總超過300 => 反計算出upCC應為多少
		upCC = (totalCC<MaxBestCCrows) ? upCC : (MaxBestCCrows-spcc-satSPCC-opcc);
		for (int cc=C1+1 ; cc<=upCC ; cc++) {
			BestRatePlanTask bt = new BestRatePlanTask();
			FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
			model.setRatePlanCode(rateplan);
			model.setUsuallyCC(cc);
			
			FcstChargeModel fcst = bt.classifyRatePlan(model, model.getRatePlanCode());
			BestCCModel E = setBestCC(fcst);
			
			logger.info("PowerAccount=["+pm.getPowerAccount()+"], 向右計算"+BestCCrowsRange+"筆 usuallyCC=["+cc+"], BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
		}
		
		int downCC = C1-BestCCrowsRange;	// 最佳需量 向左多算五筆
		downCC = (downCC>0) ? downCC : 0;	// 最小0
		for (int cc=C1-1 ; cc>=downCC ; cc--) {
			BestRatePlanTask bt = new BestRatePlanTask();
			FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
			model.setRatePlanCode(rateplan);
			model.setUsuallyCC(cc);
			
			FcstChargeModel fcst = bt.classifyRatePlan(model, model.getRatePlanCode());
			BestCCModel E = setBestCC(fcst);

			logger.info("PowerAccount=["+pm.getPowerAccount()+"], 向左計算"+BestCCrowsRange+"筆 usuallyCC=["+cc+"], BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
		}
	}
	
	/**
	 * 計算某月的PowerAccount的最佳契約容量 將其和其他最佳契約容量切齊
	 * 
	 * @param pmMap
	 * @param pa
	 */
	private void calculateToMaxMinCC(List<PowerAccountModel> paList) {
		// 所有的PowerAccount
		for (PowerAccountModel pa: paList) {
			String powerAccount = pa.getPowerAccount();
			int powerphase = pa.getPowerPhase();	// 單相、三相

			MaxMinCCBean maxmin = DBQueryUtil.getInstance().queryMaxMinCCByPowerAccount(powerAccount);
			if (maxmin==null)
				continue;
			else
				deleteList.add(maxmin);
				
			/*
			 * 查出此PowerAccount所有月份裡最大的UsuallyCC => query max(UsuallyCC), min(UsuallyCC)
			 */
			int maxCC = maxmin.getMaxCC();
			int minCC = maxmin.getMinCC();
			logger.info("PowerAccount=["+powerAccount+"], maxUsuallyCC=["+maxCC+"], minUsuallyCC=["+minCC+"]");

			// 查出此PowerAccount每個月所有RatePlan的UsuallyCC => query max(UsuallyCC) where PowerAccount group by useMonth, RatePlanCode
			List<MaxUsuallyCCBean> ccMonthly = DBQueryUtil.getInstance().queryAllMaxMinCC(powerAccount);

			// 每個月的檢查是否需要補做
			for (MaxUsuallyCCBean cc: ccMonthly) {
				int ratePlanCode = cc.getRatePlanCode();
				int maxUsuallyCC = cc.getMaxCC();
				int minUsuallyCC = cc.getMinCC();
				String useMonth = cc.getUseMonth();
				
				int spcc = cc.getSpcc();
				int satSPCC = cc.getSatSPCC();
				int opcc = cc.getOpcc();
				
				Map<String, PowerMonthModel> pmMap = DBQueryUtil.getInstance().getPowerMonth(useMonth);
				if (pmMap.containsKey(powerAccount)) {
					PowerMonthModel pm = pmMap.get(powerAccount);

					
					/*
					 * 向上切齊 & 上限300(maxBestCC)
					 */
					for (int calculateCC=maxUsuallyCC+1 ; calculateCC<=maxCC ; calculateCC++) {
						
						int totalCC = calculateCC + spcc + satSPCC + opcc;
						if (totalCC>=MaxBestCCrows) 	// total不得超過MaxBestCCrows (300)
							break;
						
						// 準備計算電費內容
						BestRatePlanTask bt = new BestRatePlanTask();
						FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
						model.setRatePlanCode(ratePlanCode);
						model.setUsuallyCC(calculateCC);
						
						FcstChargeModel fcst = bt.classifyRatePlan(model, model.getRatePlanCode());
						BestCCModel E = setBestCC(fcst);
						
						logger.info("PowerAccount=["+powerAccount+"], useMonth=["+useMonth+"] 切齊補算 usuallyCC=["+calculateCC+"], BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
					}
					
					/*
					 * 向下切齊 & 多算五筆
					 */
					for (int calculateCC=minUsuallyCC-1 ; calculateCC>=minCC ; calculateCC--) {
						// 準備計算電費內容
						BestRatePlanTask bt = new BestRatePlanTask();
						FcstChargeModel model = bt.setFcstChargeModel(pm, powerphase);
						model.setRatePlanCode(ratePlanCode);
						model.setUsuallyCC(calculateCC);

						FcstChargeModel fcst = bt.classifyRatePlan(model, model.getRatePlanCode());
						BestCCModel E = setBestCC(fcst);
						
						logger.info("PowerAccount=["+powerAccount+"], useMonth=["+useMonth+"] 切齊補算 usuallyCC=["+calculateCC+"], BestCC: "+JsonUtil.getInstance().convertObjectToJsonstring(E));
					}
				}
			}
		}
	}
	
}
