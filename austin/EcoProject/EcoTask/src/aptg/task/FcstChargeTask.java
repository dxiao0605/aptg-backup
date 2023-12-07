package aptg.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.FcstCollectionBean;
import aptg.dao.FcstChargeDao;
import aptg.manager.PowerRecordManager;
import aptg.manager.SysConfigManager;
import aptg.models.FcstChargeModel;
import aptg.models.MeterSetupModel;
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
 * 	每日記算電費&預測電費
 * 	正常流程：只需統整本月的電費&預測電費 ex: 9/5 => 9/1~9/4 電費&預測電費
 * 			  若為1號，需統整上一個月的 ex: 9/1 => 8/1~8/31電費&預測電費
 * 			 (可能有補資料狀況 => 改為由補資料月份開始重新統整)
 * 
 * 	補作流程：統整指定月份~本月份 or 上一個月(當每月1號)
 * 
 * @author austinchen
 *
 */
public class FcstChargeTask extends BaseFunction {

	private static final String CLASS_NAME = FcstChargeTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final String Sysconfig_TransferRatePlanCode = "transferRatePlanCode";
	
	private static Integer TransferRatePlanCode;
	
	private Map<String, FcstChargeModel> collectionMap = new HashMap<>();
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		FcstChargeTask task = new FcstChargeTask();

		String sysconfig = SysConfigManager.getInstance().getSysconfig(Sysconfig_TransferRatePlanCode);
		TransferRatePlanCode = Integer.valueOf(sysconfig);

		Calendar startCal = Calendar.getInstance();	// init
		
		Calendar end = Calendar.getInstance();
		end.setTime(ToolUtil.getInstance().getInitYesterday());	// 前一天00:00:00
		Calendar endCal = ToolUtil.getInstance().setDateEndTime(end);	// 時間由 00:00:00 設成 23:59:00
		
		if (args.length==0) {
			/*
			 * 	正常流程
			 */
			
			// 計算所有device
			List<MeterSetupModel> meterList = DBQueryUtil.getInstance().getEnabledMeterSetupUsageCode1();
			for (MeterSetupModel meter: meterList) {
				String powerAccount = meter.getPowerAccount();
				String deviceID = meter.getDeviceID();

				startCal = ToolUtil.getInstance().setDateEndTime(Calendar.getInstance());
				startCal.add(Calendar.DATE, -1);	// 計算前一天
				
				Date recTime = PowerRecordManager.getInstance().getFirstRecord(deviceID);
				if (recTime!=null) {
					/*
					 * 	補資料狀況 => 改為由補資料月份開始重新統整
					 */
					startCal.setTime(recTime);
					startCal = ToolUtil.getInstance().setDateEndTime(startCal);
				}
				logger.info("##### PowerAccount=["+powerAccount+"], DeviceID=["+deviceID+"], 計算電費起始日=["+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd")+"]");

				// ex: 現為2020-09-01 => start=2020-08-31 ~ end=2020-09-01 (只算31 start<end)
				// ex: 現為2020-09-28 => start=2020-09-27 ~ end=2020-09-28 (只算27 start<end)
				// ex: 現為2020-09-28 => 如有補紀錄ex:start=2020-09-15 ~ end=2020-09-28 (算15~27 start<end)
				Calendar cal = Calendar.getInstance();
				cal.setTime(startCal.getTime());
				for ( ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
//				for ( ; startCal.compareTo(endCal)<=0 ; startCal.add(Calendar.DATE, 1)) {
					String recDate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");
					
					logger.info("PowerAccount=["+powerAccount+"], DeviceID=["+deviceID+"], 計算["+recDate+"] 電費");
					List<FcstCollectionBean> list = DBQueryUtil.getInstance().getFcstChargeByDeviceID(recDate, deviceID);
					if (list.size()!=0) {
						task.fcstCharge(list);			// 整理電號底下電表為enabeld, usagecode=1的powercollection
						task.calculateFcstCharge(powerAccount, recDate);
					}
				}
			}
			
		} else {
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定日期
			startCal = ToolUtil.getInstance().setDateEndTime(startCal);

			String param = (args.length>=2) ? args[1] : null;
			List<MeterSetupModel> deviceList = new ArrayList<>();
			if (StringUtils.isNotBlank(param) && param.equals("all")) {
				deviceList = task.getAllUsageCode1Device();
				logger.info("All Enabled=1 & UsageCode=1 Device");
				
			} else if (StringUtils.isNotBlank(param) && param.indexOf("BankCode=")!=-1) {
				String bankCode  = param.split("=")[1];
				deviceList = task.getUsageCode1DeviceByBank(bankCode);
				logger.info("BankCode=["+bankCode +"], Device: "+JsonUtil.getInstance().convertObjectToJsonstring(deviceList));
				
			} else {
				deviceList = task.getUsageCode1Device(param);
				logger.info("DeviceID=["+param +"], Device: "+JsonUtil.getInstance().convertObjectToJsonstring(deviceList));
			}

			/*
			 *	想算到哪天就寫到哪天, ex: 2020-11-17 (含)
			 */
			String endDate = (args.length>=3) ? args[2] : null;
			if (StringUtils.isNotBlank(endDate)) {
				// 想算到哪天就寫到哪天, ex: 2020-11-17 (含)
				Date calculateEndDate = ToolUtil.getInstance().convertStringToDate(endDate, "yyyy-MM-dd");
				endCal.setTime(calculateEndDate);
				endCal = ToolUtil.getInstance().setDateEndTime(endCal);
			}

			// ex: 現為2020-09-01 => start=2020-08-31 ~ end=2020-09-01 (只算31 start<end)
			// ex: 現為2020-09-28 => start=2020-09-27 ~ end=2020-09-28 (只算27 start<end)
			// ex: 現為2020-09-28 => 如有補紀錄ex:start=2020-09-15 ~ end=2020-09-28 (算15~27 start<end)
			Calendar cal = Calendar.getInstance();
			cal.setTime(startCal.getTime());
			for ( ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
//			for (Calendar cal=startCal ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
				String recDate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd");
				
				for (MeterSetupModel meters: deviceList) {
					String deviceID = meters.getDeviceID();
					String powerAccount = meters.getPowerAccount();
					
					logger.info("指定日期["+recDate+"], 重新計算 PowerAccount=["+powerAccount+"], DeviceID=["+deviceID+"] 電費");
					List<FcstCollectionBean> list = DBQueryUtil.getInstance().getFcstChargeByDeviceID(recDate, deviceID);

					if (list.size()!=0) {
						task.fcstCharge(list);			// 整理電號底下電表為enabeld, usagecode=1的powercollection
						task.calculateFcstCharge(powerAccount, recDate);		// 	
					} else {
						logger.info("Can't find PowerRecordCollection, 指定日期["+recDate+"], PowerAccount=["+powerAccount+"], DeviceID=["+deviceID+"]");
					}
				}
			}
		}
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	/**
	 *	整理電號底下電表為enabeld, usagecode=1的powercollection
	 */
	private void fcstCharge(List<FcstCollectionBean> list) {
		for (FcstCollectionBean fc: list) {
			String powerAccount = fc.getPowerAccount();
			String key = powerAccount+"@"+fc.getRecDate();

			int powerPhase = fc.getPowerPhase();
			Date recDate = ToolUtil.getInstance().convertStringToDate(fc.getRecDate(), "yyyy-MM-dd");
			String useMonth = ToolUtil.getInstance().convertDateToString(recDate, "yyyyMM");
			String useTime = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd") + " 23:59:00";
			int ratePlanCode = fc.getRatePlanCode();
			int usuallyCC = fc.getUsuallyCC();
			int spcc = fc.getSPCC();
			int satSPCC = fc.getSatSPCC();
			int opcc = fc.getOPCC();
			BigDecimal mDemandPK = fc.getMDemandPK();
			BigDecimal mDemandSP = fc.getMDemandSP();
			BigDecimal mDemandSatSP = fc.getMDemandSatSP();
			BigDecimal mDemandOP = fc.getMDemandOP();
			BigDecimal mcecPK = fc.getMCECPK();
			BigDecimal mcecSP = fc.getMCECSP();
			BigDecimal mcecSatSP = fc.getMCECSatSP();
			BigDecimal mcecOP = fc.getMCECOP();
			BigDecimal mcec = fc.getMCEC();
			
			BigDecimal fcstECO5MCECPK = fc.getFcstECO5MCECPK();
			BigDecimal fcstECO5MCECSP = fc.getFcstECO5MCECSP();
			BigDecimal fcstECO5MCECSatSP = fc.getFcstECO5MCECSatSP();
			BigDecimal fcstECO5MCECOP = fc.getFcstECO5MCECOP();
			BigDecimal fcstECO5MCEC = fc.getFcstECO5MCEC();
			
			if (collectionMap.containsKey(key)) {
				// 多筆 => 需量加總
				FcstChargeModel model = collectionMap.get(key);
				
				model.setMDemandPK(model.getMDemandPK().add(mDemandPK));
				model.setMDemandSP(model.getMDemandSP().add(mDemandSP));
				model.setMDemandSatSP(model.getMDemandSatSP().add(mDemandSatSP));
				model.setMDemandOP(model.getMDemandOP().add(mDemandOP));
				
				model.setMCECPK(model.getMCECPK().add(mcecPK));
				model.setMCECSP(model.getMCECSP().add(mcecSP));
				model.setMCECSatSP(model.getMCECSatSP().add(mcecSatSP));
				model.setMCECOP(model.getMCECOP().add(mcecOP));
				model.setMCEC(model.getMCEC().add(mcec));

				model.setFcstECO5MCECPK(model.getFcstECO5MCECPK().add(fcstECO5MCECPK));
				model.setFcstECO5MCECSP(model.getFcstECO5MCECSP().add(fcstECO5MCECSP));
				model.setFcstECO5MCECSatSP(model.getFcstECO5MCECSatSP().add(fcstECO5MCECSatSP));
				model.setFcstECO5MCECOP(model.getFcstECO5MCECOP().add(fcstECO5MCECOP));
				model.setFcstECO5MCEC(model.getFcstECO5MCEC().add(fcstECO5MCEC));
				
				Date recUseTime = ToolUtil.getInstance().convertStringToDate(model.getUseTime(), "yyyy-MM-dd");
				if (recDate.after(recUseTime)) {
					model.setUseMonth(useMonth);
					model.setUseTime(useTime);
				}
				
			} else {
				FcstChargeModel model = new FcstChargeModel();
				model.setPowerAccount(powerAccount);
				model.setUseMonth(useMonth);
				model.setUseTime(useTime);
				model.setRatePlanCode(ratePlanCode);
				
				model.setUsuallyCC(usuallyCC);
				model.setSPCC(spcc);
				model.setSatSPCC(satSPCC);
				model.setOPCC(opcc);
				
				model.setMDemandPK(mDemandPK);
				model.setMDemandSP(mDemandSP);
				model.setMDemandSatSP(mDemandSatSP);
				model.setMDemandOP(mDemandOP);
				
				model.setMCECPK(mcecPK);
				model.setMCECSP(mcecSP);
				model.setMCECSatSP(mcecSatSP);
				model.setMCECOP(mcecOP);
				model.setMCEC(mcec);

				model.setPowerPhase(powerPhase);
				model.setRealPlan(ratePlanCode);

				model.setFcstECO5MCECPK(fcstECO5MCECPK);
				model.setFcstECO5MCECSP(fcstECO5MCECSP);
				model.setFcstECO5MCECSatSP(fcstECO5MCECSatSP);
				model.setFcstECO5MCECOP(fcstECO5MCECOP);
				model.setFcstECO5MCEC(fcstECO5MCEC);
				
				collectionMap.put(key, model);
			}
		}
	}

	/**
	 * 	依RatePlanCode計算電費
	 */
	private void calculateFcstCharge(String powerAccount, String recDate) {
		int count = 0;
		
		String key = powerAccount+"@"+recDate;
		if (collectionMap.containsKey(key)) {
			FcstChargeModel model = collectionMap.get(key);

			// 分類計算電費
			FcstChargeModel fcst = classifyRatePlan(model);
			
			// INSERT or UPDATE FcstCharge
			if (fcst!=null) {
				logger.info("FcstCharge info: "+JsonUtil.getInstance().convertObjectToJsonstring(fcst) + "\r\n");
				boolean alreadyExist = DBQueryUtil.getInstance().queryFcstCharge(fcst);
				if (alreadyExist) {
					// update
					count = updateFcstCharge(fcst);
				} else {
					// insert
					count = insertFcstCharge(fcst);
				}
			}
		}
		
		if (count==-1) {
			// catch exception
			setFailedPowerAccount(powerAccount);
			logger.error("##### Reprice FcstCharge Failed, PowerAccount=["+powerAccount+"]");
		}
	}
	public FcstChargeModel classifyRatePlan(FcstChargeModel model) {
		int ratePlanCode = model.getRatePlanCode();
		if (ratePlanCode==21 || ratePlanCode==22 ||ratePlanCode==23) {
			// RatePlanCode=21, 22, 23依sysconfig設定更改計算方式
			logger.info("PowerAccount:["+model.getPowerAccount()+"], Transfer RatePlanCode from:["+ratePlanCode+"]to["+TransferRatePlanCode+"]");
			model.setRatePlanCode(TransferRatePlanCode);
			
			ratePlanCode = model.getRatePlanCode();
			
		} else {
			logger.info("PowerAccount:["+model.getPowerAccount()+"], RatePlanCode=["+ratePlanCode+"]");
		}
		
		FcstChargeModel fcst = null;
		try {
			switch (ratePlanCode) {
				// 1: 表燈營業用 
				case 1:
					RatePlan1 rate1 = new RatePlan1(model);
					fcst = rate1.calculateTotalCharge();
					fcst = rate1.calculateFcstCharge();
					break;
					
				// 2: 表燈非營業用 
				case 2:
					RatePlan2 rate2 = new RatePlan2(model);
					fcst = rate2.calculateTotalCharge();
					fcst = rate2.calculateFcstCharge();
					break;
					
				// 3: 表燈簡易二段式 
				case 3:
					RatePlan3 rate3 = new RatePlan3(model);
					fcst = rate3.calculateTotalCharge();
					fcst = rate3.calculateFcstCharge();
					break;
					
				// 4: 表燈簡易三段式 
				case 4:
					RatePlan4 rate4 = new RatePlan4(model);
					fcst = rate4.calculateTotalCharge();
					fcst = rate4.calculateFcstCharge();
					break;
					
				// 5: 表燈標準二段式 
				case 5:
					RatePlan5 rate5 = new RatePlan5(model);
					fcst = rate5.calculateTotalCharge();
					fcst = rate5.calculateFcstCharge();
					break;
					
				// 6: 低壓非時間
				case 6:
					RatePlan6 rate6 = new RatePlan6(model);
					fcst = rate6.calculateTotalCharge();
					fcst = rate6.calculateFcstCharge();
					break;
					
				// 7: 低壓二段式
				case 7:
					RatePlan7 rate7 = new RatePlan7(model);
					fcst = rate7.calculateTotalCharge();
					fcst = rate7.calculateFcstCharge();
					break;
					
				// 8: 高壓二段式
				case 8:
					RatePlan8 rate8 = new RatePlan8(model);
					fcst = rate8.calculateTotalCharge();
					fcst = rate8.calculateFcstCharge();
					break;
					
				// 9: 高壓三段式
				case 9:
					RatePlan9 rate9 = new RatePlan9(model);
					fcst = rate9.calculateTotalCharge();
					fcst = rate9.calculateFcstCharge();
					break;
					
				// 21: 自訂A-分攤
				case 21:
					RatePlan21 rate21 = new RatePlan21(model);
					fcst = rate21.calculateTotalCharge();
					fcst = rate21.calculateFcstCharge();
					break;
					
				// 22: 自訂B-公用分攤
				case 22:
					RatePlan22 rate22 = new RatePlan22(model);
					fcst = rate22.calculateTotalCharge();
					fcst = rate22.calculateFcstCharge();
					break;
					
				// 23: 自訂C-空調分攤
				case 23:
					RatePlan23 rate23 = new RatePlan23(model);
					fcst = rate23.calculateTotalCharge();
					fcst = rate23.calculateFcstCharge();
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return fcst;
	}

	private int insertFcstCharge(FcstChargeModel fcst) {
		List<FcstChargeModel> list = new ArrayList<>();
		list.add(fcst);

		if (list.size()>0) {
			FcstChargeDao dao = new FcstChargeDao();
			List<Integer> ids = dao.insertFcstCharge(list);	
			if (ids!=null) {
				return ids.size();
			}
		} else {
			return 0;
		}
		return -1;
	}
	
	private int updateFcstCharge(FcstChargeModel fcst) {
		List<FcstChargeModel> list = new ArrayList<>();
		list.add(fcst);

		FcstChargeDao dao = new FcstChargeDao();
		return dao.updateFcstCharge(list);	// -1: exception
	}
}
