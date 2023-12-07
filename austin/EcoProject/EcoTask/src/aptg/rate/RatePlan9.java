package aptg.rate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.manager.ElectricityPriceManager;
import aptg.models.ElectricityPriceModel;
import aptg.models.FcstChargeModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.rate.base.RatePlanBase;
import aptg.task.FcstChargeTask;

/**
 * 	高壓三段式
 * 
 * @author austinchen
 *
 */
public class RatePlan9 extends RatePlanBase {

	private static final String CLASS_NAME = FcstChargeTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	private static final int RatePlan = 9;

	public RatePlan9() {}
	
	public RatePlan9(FcstChargeModel model) {
		// 是否夏月
		this.isSummer = isSummer(getMonth(model.getUseMonth()));
		
		// 台電用電時段歸類
		this.model = tpmCalculate(model);

		// 取得電價表
		this.price = getPriceDetail();
		
		// isTest=true => 開啟測試log
		printCalculateLog();
	}

	public FcstChargeModel calculateTotalCharge() {
		if (price!=null) {
			calculate();
		} else {
			logger.error("ElectricityPrice Table is null, RatePlanCode=["+model.getRatePlanCode()+"]");
		}
		return model;
	}

	/**
	 * 	依用電種類(ratePlanCode)決定台電用電時段歸類
	 */
	public FcstChargeModel tpmCalculate(FcstChargeModel model) {
		List<BigDecimal> spList = new ArrayList<>();
		spList.add(model.getMDemandPK());
		spList.add(model.getMDemandSP());
		
		/*
		 * 	用電數據
		 */
		if (isSummer) {
			this.tpmDemandPK = model.getMDemandPK();			// 台電計法本月最大尖峰需量 = 尖峰
			this.tpmDemandSP = model.getMDemandSP();			// 台電計法本月最大半尖峰需量 = 半尖峰
			this.tpmDemandSatSP = model.getMDemandSatSP();		// 台電計法本月最大週六半需量 = 周六半
			this.tpmDemandOP = model.getMDemandOP();			// 台電計法本月最大離峰需量 = 離峰
			
			this.tpmcecPK = model.getMCECPK();				// 台電計法累計至今當月尖峰累積用電量 = 尖峰
			this.tpmcecSP = model.getMCECSP();				// 台電計法累計至今當月半尖峰累積用電量 = 半尖峰
			this.tpmcecSatSP = model.getMCECSatSP();		// 台電計法累計至今當月週六半尖峰累積用電量 = 周六半
			this.tpmcecOP = model.getMCECOP();				// 台電計法累計至今當月離峰累積用電量 = 離峰
			this.tpmCEC = tpmcecPK.add(tpmcecSP).add(tpmcecSatSP).add(tpmcecOP);	// 台電計法累計至今當月總累積用電量
			
		} else {
			this.tpmDemandPK = BigDecimal.ZERO;					// 台電計法本月最大尖峰需量 = 尖峰
			this.tpmDemandSP = getMaxDemand(spList);			// 台電計法本月最大半尖峰需量 = 半尖峰
			this.tpmDemandSatSP = model.getMDemandSatSP();		// 台電計法本月最大週六半需量 = 周六半
			this.tpmDemandOP = model.getMDemandOP();			// 台電計法本月最大離峰需量 = 離峰

			this.tpmcecPK = BigDecimal.ZERO;							// 台電計法累計至今當月尖峰累積用電量 = 尖峰
			this.tpmcecSP = model.getMCECPK().add(model.getMCECSP());	// 台電計法累計至今當月半尖峰累積用電量 = 半尖峰
			this.tpmcecSatSP = model.getMCECSatSP();					// 台電計法累計至今當月週六半尖峰累積用電量 = 周六半
			this.tpmcecOP = model.getMCECOP();							// 台電計法累計至今當月離峰累積用電量 = 離峰
			this.tpmCEC = tpmcecPK.add(tpmcecSP).add(tpmcecSatSP).add(tpmcecOP);	// 台電計法累計至今當月總累積用電量
		}
		model.setTPMDemandPK(tpmDemandPK);
		model.setTPMDemandSP(tpmDemandSP);
		model.setTPMDemandSatSP(tpmDemandSatSP);
		model.setTPMDemandOP(tpmDemandOP);
		
		model.setTPMCECPK(tpmcecPK);
		model.setTPMCECSP(tpmcecSP);
		model.setTPMCECSatSP(tpmcecSatSP);
		model.setTPMCECOP(tpmcecOP);
		model.setTPMCEC(tpmCEC);


		// 計算電費時需量先無條件捨去 & 計算電費時用電量先四捨五入
		tpmxxxHandle();
		
		return model;
	}

	/**
	 * 	取得電價表
	 */
	public ElectricityPriceModel getPriceDetail() {
		this.price = ElectricityPriceManager.getInstance().getPrice(model.getUseTime(), model.getRatePlanCode());
		if (price==null)
			return null;

		/*
		 * 	 電價表
		 */
		if (isSummer) {
//			this.baseChargePhase = price.getBaseCharge3phase();
//			this.baseChargePhase = (model.getPowerPhase()==3) ? price.getBaseCharge3phase() : price.getBaseCharge1phase();

			this.baseChargeUsually = price.getBaseChargeUsuallyS();
			this.baseChargeSP = price.getBaseChargeSPS();
			this.baseChargeSatSP = price.getBaseChargeSatSPS();
			this.baseChargeOP = price.getBaseChargeOPS();
			
			this.timeCharge = price.getTimeChargeS();
			this.timeChargeSP = price.getTimeChargeSPS();
			this.timeChargeSatSP = price.getTimeChargeSatSPS();
			this.timeChargeOP = price.getTimeChargeOPS();
			
//			this.over2KPrice = price.getOver2KPrice();

			if (isTest) {
//				logger.info("超過2000度加計流動電費單價: "+ over2KPrice);

				if (model.getPowerPhase()==3)
					logger.info("時間電價按戶計收三相: "+ baseChargePhase);
				else 
					logger.info("時間電價按戶計收單相: "+ baseChargePhase);
				logger.info("時間電價經常契約單價(夏月): "+ baseChargeUsually);
				logger.info("時間電價半尖峰/非夏月契約單價(夏月): "+ baseChargeSP);
				logger.info("時間電價周六半尖峰契約單價(夏月): "+ baseChargeSatSP);
				logger.info("時間電價離峰契約單價(夏月): "+ baseChargeOP);
				logger.info("時間電價流動電費尖峰(夏月): "+ timeCharge);
				logger.info("時間電價流動電費半尖峰(夏月): "+ timeChargeSP);
				logger.info("時間電價流動電費週六半尖峰(夏月): "+ timeChargeSatSP);
				logger.info("時間電價流動電費離峰(夏月): "+ timeChargeOP +"\r\n");
			}
		} else {
//			this.baseChargePhase = price.getBaseCharge3phase();
//			this.baseChargePhase = (model.getPowerPhase()==3) ? price.getBaseCharge3phase() : price.getBaseCharge1phase();
			
			this.baseChargeUsually = price.getBaseChargeUsually();
			this.baseChargeSP = price.getBaseChargeSP();
			this.baseChargeSatSP = price.getBaseChargeSatSP();
			this.baseChargeOP = price.getBaseChargeOP();
			
			this.timeCharge = price.getTimeCharge();
			this.timeChargeSP = price.getTimeChargeSP();
			this.timeChargeSatSP = price.getTimeChargeSatSP();
			this.timeChargeOP = price.getTimeChargeOP();
			
//			this.over2KPrice = price.getOver2KPrice();

			if (isTest) {
				if (model.getPowerPhase()==3)
					logger.info("時間電價按戶計收三相: "+ baseChargePhase);
				else 
					logger.info("時間電價按戶計收單相: "+ baseChargePhase);
				logger.info("時間電價經常契約單價(非夏月): "+ baseChargeUsually);
				logger.info("時間電價半尖峰/非夏月契約單價(非夏月): "+ baseChargeSP);
				logger.info("時間電價周六半尖峰契約單價(非夏月): "+ baseChargeSatSP);
				logger.info("時間電價離峰契約單價(非夏月): "+ baseChargeOP);
				logger.info("時間電價流動電費尖峰(非夏月): "+ timeCharge);
				logger.info("時間電價流動電費半尖峰(非夏月): "+ timeChargeSP);
				logger.info("時間電價流動電費週六半尖峰(非夏月): "+ timeChargeSatSP);
				logger.info("時間電價流動電費離峰(非夏月): "+ timeChargeOP +"\r\n");
			}
		}
		return price;
	}
	
	/**
	 * 	夏月/非夏月 計算
	 * 
	 * @return
	 */
	private FcstChargeModel calculate() {
		logger.info("===== 電費 PowerAccount=["+model.getPowerAccount()+"], useTime=["+model.getUseTime()+"], RatePlanCode=["+model.getRatePlanCode()+"] =====");
		
		this.usuallyCC = new BigDecimal(model.getUsuallyCC());	// 經常契約容量
		this.spcc = new BigDecimal(model.getSPCC());			// 非夏月契約容量
		this.satSPCC = new BigDecimal(model.getSatSPCC());		// 週六半尖峰契約容量
		this.opcc = new BigDecimal(model.getOPCC());			// 離峰契約容量

		/*
		 * A.基本電費(含用戶費): baseTotalCharge
		 */
		BigDecimal userCharge = BigDecimal.ZERO;
		BigDecimal usuallyCharge = usuallyCharge(baseChargeUsually, usuallyCC);
		BigDecimal spccCharge = spccCharge(baseChargeSP, spcc);
		BigDecimal satOrOPCharge = satOrOPCharge(baseChargeOP, usuallyCC, spcc, satSPCC, opcc);
		// 基本電費小計
		BigDecimal baseCharge = baseCharge(userCharge, usuallyCharge, spccCharge, satOrOPCharge);	// 四捨五入至小數第一位
		model.setBaseCharge(baseCharge);		// 基本電費
		logger.info("基本電費小計baseCharge: "+model.getBaseCharge());

		/*
		 * B.流動電費: flowCharge = 流動電費小計
		 */
		BigDecimal usagePKCharge = usagePKCharge(timeCharge, tpmcecPK);	// 尖峰
		BigDecimal usageSPCharge = usageSPCharge(timeChargeSP, tpmcecSP);
		BigDecimal usageSatSPCharge = usageSatSPCharge(timeChargeSatSP, tpmcecSatSP);	// 周六半尖峰
		BigDecimal usageOPCharge = usageOPCharge(timeChargeOP, tpmcecOP);
		// 流動電費小計
		BigDecimal usageCharge = usageCharge(usagePKCharge, usageSPCharge, usageSatSPCharge, usageOPCharge);

		// 流動電費小計 + 加計流動電費
		BigDecimal totalUsageCharge = totalUsageCharge(usageCharge, BigDecimal.ZERO);	// 四捨五入至小數第一位
		model.setUsageCharge(totalUsageCharge);	// 流動電費小計
		logger.info("流動電費小計usageCharge: "+model.getUsageCharge());
		
		/*
		 * C.超約
		 */
		BigDecimal overPKCharge = overUsuallyCC(baseChargeUsually, usuallyCC, tpmDemandPK);
		BigDecimal overSPCharge = overSPCC(baseChargeSP, usuallyCC, spcc, tpmDemandSP, RatePlan);
		BigDecimal overSatSPCCCharge = (isSummer) ? overSatSPCC(baseChargeSatSP, usuallyCC, spcc, satSPCC, tpmDemandSatSP, tpmDemandPK, RatePlan) : overSatSPCC(baseChargeSatSP, usuallyCC, spcc, satSPCC, tpmDemandSatSP, tpmDemandSP, RatePlan);
		BigDecimal overOPCCCharge = overOPCC(baseChargeOP, usuallyCC, spcc, satSPCC, opcc, tpmDemandOP, tpmDemandPK, tpmDemandSatSP, RatePlan);

//		logger.info("######## overPKCharge: "+overPKCharge);
//		logger.info("######## overSPCharge: "+overSPCharge);
//		logger.info("######## overSatSPCCCharge: "+overSatSPCCCharge);
//		logger.info("######## overOPCCCharge: "+overOPCCCharge);
		
		// 非約定電費小計
		BigDecimal overCharge = overCharge(overPKCharge, overSPCharge, overSatSPCCCharge, overOPCCCharge);	// 四捨五入至小數第一位
		model.setOverCharge(overCharge);	// 非約定電費小計(超約電費)
		logger.info("非約定電費小計overCharge: "+model.getOverCharge());
		
		/*
		 *	電費合計#
		 */
		BigDecimal totalCharge = totalCharge(model.getBaseCharge(), model.getUsageCharge(), model.getOverCharge());	// 四捨五入至整數
		model.setTotalCharge(totalCharge);	// 電費合計
		logger.info("電費合計totalCharge: "+model.getTotalCharge());
		
		return model;
	}

	public FcstChargeModel calculateFcstCharge() {
		logger.info("===== 預測電費 PowerAccount=["+model.getPowerAccount()+"], useTime=["+model.getUseTime()+"], RatePlanCode=["+model.getRatePlanCode()+"] =====");
		
		model.setFcstBaseCharge(model.getBaseCharge());	// 預測基本電費 = 基本電費
		model.setFcstOverCharge(model.getOverCharge());	// 預測超約電費 = 超約電費
		
		// 預測本月mcecPK, mcecSP, mcecSatSP, mcecOP
//		fcstMCECXX(tpmcecPK, tpmcecSP, tpmcecSatSP, tpmcecOP);
//		or
		setFcstTPMMCEC();
		
		// 預測流動電費
		BigDecimal fcstUsagePKCharge = usagePKCharge(timeCharge, fcstMCECPK);
		BigDecimal fcstUsageSPCharge = usageSPCharge(timeChargeSP, fcstMCECSP);
		BigDecimal fcstUsageSatSPCharge = usageSatSPCharge(timeChargeSatSP, fcstMCECSatSP);
		BigDecimal fcstUsageOPCharge = usageOPCharge(timeChargeOP, fcstMCECOP);
		// 預測 流動電費小計
		BigDecimal fcstUsageCharge = usageCharge(fcstUsagePKCharge, fcstUsageSPCharge, fcstUsageSatSPCharge, fcstUsageOPCharge);

		// 流動電費小計 + 加計流動電費 
		BigDecimal totalFcstUsageCharge = totalFcstUsageCharge(fcstUsageCharge, BigDecimal.ZERO);	// 四捨五入至小數第一位
		model.setFcstUsageCharge(totalFcstUsageCharge);
		
		/*
		 * 預測電費合計
		 */
		BigDecimal fcstTotalCharge = fcstTotalCharge(model.getBaseCharge(), model.getFcstUsageCharge(), model.getFcstOverCharge());
		model.setFcstTotalCharge(fcstTotalCharge);	// 預測電費合計
		logger.info("預測電費合計: "+model.getFcstTotalCharge());
		
		return model;
	}
	private void setFcstTPMMCEC() {
		if (isSummer) {
			this.fcstMCECPK = model.getFcstECO5MCECPK();									// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰
			this.fcstMCECSP = model.getFcstECO5MCECSP();									// 台電計法累計至今當月半尖峰累積用電量
			this.fcstMCECSatSP = model.getFcstECO5MCECSatSP();								// 台電計法累計至今當月週六半尖峰累積用電量 = 周六半
			this.fcstMCECOP = model.getFcstECO5MCECOP();									// 台電計法累計至今當月離峰累積用電量 = 離峰
			this.fcstMCEC = fcstMCECPK.add(fcstMCECSP).add(fcstMCECSatSP).add(fcstMCECOP);	// 台電計法累計至今當月總累積用電量	
		
		} else {
			this.fcstMCECPK = BigDecimal.ZERO;												// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰
			this.fcstMCECSP = model.getFcstECO5MCECPK().add(model.getFcstECO5MCECSP());		// 台電計法累計至今當月半尖峰累積用電量
			this.fcstMCECSatSP = model.getFcstECO5MCECSatSP();								// 台電計法累計至今當月週六半尖峰累積用電量 = 周六半
			this.fcstMCECOP = model.getFcstECO5MCECOP();									// 台電計法累計至今當月離峰累積用電量 = 離峰
			this.fcstMCEC = fcstMCECPK.add(fcstMCECSP).add(fcstMCECSatSP).add(fcstMCECOP);	// 台電計法累計至今當月總累積用電量
			
		}
		
		model.setFcstMCECPK(fcstMCECPK);
		model.setFcstMCECSP(fcstMCECSP);
		model.setFcstMCECSatSP(fcstMCECSatSP);
		model.setFcstMCECOP(fcstMCECOP);
		model.setFcstMCEC(fcstMCEC);

		// 計算電費時用電量先四捨五入
		fcstxxxHandle();
		
		if (isTest) {
			logger.info("************* 預測當月尖峰累積用電量: "+fcstMCECPK);
			logger.info("************* 預測當月半尖峰累積用電量: "+fcstMCECSP);
			logger.info("************* 預測當月週六半尖峰累積用電量: "+fcstMCECSatSP);
			logger.info("************* 預測當月離峰累積用電量: "+fcstMCECOP);
			logger.info("************* 預測當月總累積用電量: "+fcstMCEC);
		}
	}

	/*
	 * 
	 */
	public PowerRecordCollectionModel tpCollectionValue(PowerRecordCollectionModel record) {
		boolean isSummer = isSummer(getRecDateMonth(record.getRecDate()));

		BigDecimal tpDemandPK, tpDemandSP, tpDemandSatSP, tpDemandOP;
		BigDecimal tpdcecPK, tpdcecSP, tpdcecSatSP, tpdcecOP;

		BigDecimal tpmDemandPK, tpmDemandSP, tpmDemandSatSP, tpmDemandOP;
		BigDecimal tpmcecPK, tpmcecSP, tpmcecSatSP, tpmcecOP;
		
		/*
		 * ------------------------------------------------- 每日 ------------------------------------------------- 
		 */
		List<BigDecimal> demandSPList = new ArrayList<>();
		demandSPList.add(record.getDemandPK());
		demandSPList.add(record.getDemandSP());
		
		if (isSummer) {
			// 台電每日最大需量
			tpDemandPK = record.getDemandPK();
			tpDemandSP = record.getDemandSP();
			tpDemandSatSP = record.getDemandSatSP();
			tpDemandOP = record.getDemandOP();
			
			// 台電每日累積用電量
			tpdcecPK = record.getDCECPK();
			tpdcecSP = record.getDCECSP();
			tpdcecSatSP = record.getDCECSatSP();
			tpdcecOP = record.getDCECOP();
			
		} else {
			// 台電每日最大需量
			tpDemandPK = BigDecimal.ZERO;
			tpDemandSP = getMaxDemand(demandSPList);
			tpDemandSatSP = record.getDemandSatSP();
			tpDemandOP = record.getDemandOP();
			
			// 台電每日累積用電量
			tpdcecPK = BigDecimal.ZERO;
			tpdcecSP = record.getDCECPK().add(record.getDCECSP());
			tpdcecSatSP = record.getDCECSatSP();
			tpdcecOP = record.getDCECOP();
		}
		
		/*
		 * ------------------------------------------------- 每月 ------------------------------------------------- 
		 */
		demandSPList.clear();
		demandSPList.add(record.getMDemandPK());
		demandSPList.add(record.getMDemandSP());
		
		if (isSummer) {
			// 台電每月最大需量
			tpmDemandPK = record.getMDemandPK();
			tpmDemandSP = record.getMDemandSP();
			tpmDemandSatSP = record.getMDemandSatSP();
			tpmDemandOP = record.getMDemandOP();

			// 台電每月累積用電量
			tpmcecPK = record.getMCECPK();
			tpmcecSP = record.getMCECSP();
			tpmcecSatSP = record.getMCECSatSP();
			tpmcecOP = record.getMCECOP();
			
		} else {
			// 台電每月最大需量
			tpmDemandPK = BigDecimal.ZERO;record.getMDemandPK();
			tpmDemandSP = getMaxDemand(demandSPList);
			tpmDemandSatSP = record.getMDemandSatSP();
			tpmDemandOP = record.getMDemandOP();

			// 台電每月累積用電量
			tpmcecPK = BigDecimal.ZERO;
			tpmcecSP = record.getMCECPK().add(record.getMCECSP());
			tpmcecSatSP = record.getMCECSatSP();
			tpmcecOP = record.getMCECOP();
		}

		record.setTPDemandPK(tpDemandPK);
		record.setTPDemandSP(tpDemandSP);
		record.setTPDemandSatSP(tpDemandSatSP);
		record.setTPDemandOP(tpDemandOP);

		record.setTPDCECPK(tpdcecPK);
		record.setTPDCECSP(tpdcecSP);
		record.setTPDCECSatSP(tpdcecSatSP);
		record.setTPDCECOP(tpdcecOP);

		record.setTPMDemandPK(tpmDemandPK);
		record.setTPMDemandSP(tpmDemandSP);
		record.setTPMDemandSatSP(tpmDemandSatSP);
		record.setTPMDemandOP(tpmDemandOP);
		
		record.setTPMCECPK(tpmcecPK);
		record.setTPMCECSP(tpmcecSP);
		record.setTPMCECSatSP(tpmcecSatSP);
		record.setTPMCECOP(tpmcecOP);
		
		return record;
	}
}
