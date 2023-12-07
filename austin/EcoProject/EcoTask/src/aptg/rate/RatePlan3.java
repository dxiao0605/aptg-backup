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
 * 	表燈簡易二段式 
 * 
 * @author austinchen
 * 
 */
public class RatePlan3 extends RatePlanBase {

	private static final String CLASS_NAME = FcstChargeTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	public RatePlan3() {}
	
	public RatePlan3(FcstChargeModel model) {
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
		List<BigDecimal> pkList = new ArrayList<>();
		pkList.add(model.getMDemandPK());
		pkList.add(model.getMDemandSP());
		List<BigDecimal> opList = new ArrayList<>();
		opList.add(model.getMDemandSatSP());
		opList.add(model.getMDemandOP());

		/*
		 * 	用電數據
		 */
		this.tpmDemandPK = getMaxDemand(pkList);		// 台電計法本月最大尖峰需量 = 尖峰or半尖峰
		this.tpmDemandSP = BigDecimal.ZERO;				// 台電計法本月最大半尖峰需量
		this.tpmDemandSatSP = BigDecimal.ZERO;			// 台電計法本月最大週六半需量
		this.tpmDemandOP = getMaxDemand(opList);		// 台電計法本月最大離峰需量 = 周六半or離峰
		model.setTPMDemandPK(tpmDemandPK);
		model.setTPMDemandSP(tpmDemandSP);
		model.setTPMDemandSatSP(tpmDemandSatSP);
		model.setTPMDemandOP(tpmDemandOP);
		
		this.tpmcecPK = model.getMCECPK().add(model.getMCECSP());				// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰
		this.tpmcecSP = BigDecimal.ZERO;										// 台電計法累計至今當月半尖峰累積用電量
		this.tpmcecSatSP = BigDecimal.ZERO;										// 台電計法累計至今當月週六半尖峰累積用電量
		this.tpmcecOP = model.getMCECSatSP().add(model.getMCECOP());			// 台電計法累計至今當月離峰累積用電量 = 周六半 + 離峰
		this.tpmCEC = tpmcecPK.add(tpmcecSP).add(tpmcecSatSP).add(tpmcecOP);	// 台電計法累計至今當月總累積用電量
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
			this.baseChargePhase = price.getBaseCharge3phase();
			
			this.timeCharge = price.getTimeChargeS();
			this.timeChargeSP = price.getTimeChargeSPS();
			this.timeChargeSatSP = price.getTimeChargeSatSPS();
			this.timeChargeOP = price.getTimeChargeOPS();
			this.over2KPrice = price.getOver2KPrice();

			if (isTest) {
				logger.info("超過2000度加計流動電費單價: "+ over2KPrice);
				
				if (model.getPowerPhase()==3)
					logger.info("時間電價按戶計收三相: "+ baseChargePhase);
				else 
					logger.info("時間電價按戶計收單相: "+ baseChargePhase);
				logger.info("時間電價流動電費尖峰(夏月): "+ timeCharge);
				logger.info("時間電價流動電費半尖峰(夏月): "+ timeChargeSP);
				logger.info("時間電價流動電費週六半尖峰(夏月): "+ timeChargeSatSP);
				logger.info("時間電價流動電費離峰(夏月): "+ timeChargeOP +"\r\n");
			}
		} else {
			this.baseChargePhase = price.getBaseCharge3phase();
			
			this.timeCharge = price.getTimeCharge();
			this.timeChargeSP = price.getTimeChargeSP();
			this.timeChargeSatSP = price.getTimeChargeSatSP();
			this.timeChargeOP = price.getTimeChargeOP();
			this.over2KPrice = price.getOver2KPrice();

			if (isTest) {
				logger.info("超過2000度加計流動電費單價: "+ over2KPrice);
				
				if (model.getPowerPhase()==3)
					logger.info("時間電價按戶計收三相: "+ baseChargePhase);
				else 
					logger.info("時間電價按戶計收單相: "+ baseChargePhase);
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
		
		/*
		 * A.基本電費(含用戶費): baseTotalCharge
		 */
		BigDecimal userCharge = userCharge(baseChargePhase);
		BigDecimal usuallyCharge = BigDecimal.ZERO;
		BigDecimal spccCharge = BigDecimal.ZERO;
		BigDecimal satOrOPCharge = BigDecimal.ZERO;
		BigDecimal baseCharge = baseCharge(userCharge, usuallyCharge, spccCharge, satOrOPCharge);	// 四捨五入至小數第一位
		model.setBaseCharge(baseCharge);	// 基本電費
		logger.info("基本電費小計baseCharge: "+model.getBaseCharge());
		
		/*
		 * B.流動電費 = 流動電費小計 + 加計流動電費
		 */
		/*
		 * 	 流動電費小計
		 */
		BigDecimal usagePKCharge = usagePKCharge(timeCharge, tpmcecPK);
		BigDecimal usageSPCharge = BigDecimal.ZERO;
		BigDecimal usageSatSPCharge = BigDecimal.ZERO;
		BigDecimal usageOPCharge = usageOPCharge(timeChargeOP, tpmcecOP);
		// 流動電費小計
		BigDecimal usageCharge = usageCharge(usagePKCharge, usageSPCharge, usageSatSPCharge, usageOPCharge);
		// 加計流動電費
		BigDecimal usageOver2KCharge = usageOver2KCharge(over2KPrice, tpmCEC);
		
		// 流動電費小計 + 加計流動電費 
		BigDecimal totalUsageCharge = totalUsageCharge(usageCharge, usageOver2KCharge);	// 四捨五入至小數第一位
		model.setUsageCharge(totalUsageCharge);	// 流動電費
		logger.info("流動電費小計usageCharge: "+model.getUsageCharge());
		
		/*
		 *	電費合計#
		 */
		BigDecimal totalCharge = totalCharge(model.getBaseCharge(), model.getUsageCharge(), BigDecimal.ZERO);	// 四捨五入至整數
		model.setTotalCharge(totalCharge);	// 電費合計
		logger.info("電費合計totalCharge: "+model.getTotalCharge());

		return model;
	}

	/**
	 * 	預測電費合計 #
	 * 
	 * @return
	 */
	public FcstChargeModel calculateFcstCharge() {
		logger.info("===== 預測電費 PowerAccount=["+model.getPowerAccount()+"], useTime=["+model.getUseTime()+"], RatePlanCode=["+model.getRatePlanCode()+"] =====");
		
		model.setFcstBaseCharge(model.getBaseCharge());	// 基本電費
		
		// 預測本月累積用電量
//		fcstMCECXX(tpmcecPK, tpmcecSP, tpmcecSatSP, tpmcecOP);
//		or
		setFcstTPMMCEC();
		
		/*
		 * 	預測 流動電費 = 預測流動電費小計fcstUsageCharge + 預測加計流動電費fcstUsageOverCharge
		 */
		/*
		 *	預測 流動電費小計
		 */
		BigDecimal fcstUsagePKCharge = usagePKCharge(timeCharge, fcstMCECPK);
		BigDecimal fcstUsageSPCharge = BigDecimal.ZERO;
		BigDecimal fcstUsageSatSPCharge = BigDecimal.ZERO;
		BigDecimal fcstUsageOPCharge = usageOPCharge(timeChargeOP, fcstMCECOP);
		// 預測 流動電費小計
		BigDecimal fcstUsageCharge = usageCharge(fcstUsagePKCharge, fcstUsageSPCharge, fcstUsageSatSPCharge, fcstUsageOPCharge);
		// 預測 流動電費超過2000度
		BigDecimal fcstUsageOver2KCharge = usageOver2KCharge(over2KPrice, fcstMCEC);
		
		// 流動電費小計 + 加計流動電費 
		BigDecimal totalFcstUsageCharge = totalFcstUsageCharge(fcstUsageCharge, fcstUsageOver2KCharge);	// 四捨五入至小數第一位
		model.setFcstUsageCharge(totalFcstUsageCharge);
		logger.info("預測流動電費小計: "+model.getFcstUsageCharge());
		
		/*
		 * 	預測電費合計 #
		 */
		BigDecimal fcstTotalCharge = fcstTotalCharge(model.getFcstBaseCharge(), model.getFcstUsageCharge(), BigDecimal.ZERO);	// 四捨五入至整數
		model.setFcstTotalCharge(fcstTotalCharge);
		logger.info("預測電費合計: "+model.getFcstTotalCharge());
		
		return model;
	}
	private void setFcstTPMMCEC() {
		this.fcstMCECPK = model.getFcstECO5MCECPK().add(model.getFcstECO5MCECSP());		// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰
		this.fcstMCECSP = BigDecimal.ZERO;												// 台電計法累計至今當月半尖峰累積用電量
		this.fcstMCECSatSP = BigDecimal.ZERO;											// 台電計法累計至今當月週六半尖峰累積用電量
		this.fcstMCECOP = model.getFcstECO5MCECSatSP().add(model.getFcstECO5MCECOP());	// 台電計法累計至今當月離峰累積用電量 = 周六半 + 離峰
		this.fcstMCEC = fcstMCECPK.add(fcstMCECSP).add(fcstMCECSatSP).add(fcstMCECOP);	// 台電計法累計至今當月總累積用電量

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
		/*
		 * ------------------------------------------------- 每日 ------------------------------------------------- 
		 */
		List<BigDecimal> demandPKList = new ArrayList<>();
		demandPKList.add(record.getDemandPK());
		demandPKList.add(record.getDemandSP());
		List<BigDecimal> demandOPList = new ArrayList<>();
		demandOPList.add(record.getDemandSatSP());
		demandOPList.add(record.getDemandOP());
		
		// 台電每日最大需量
		BigDecimal tpDemandPK = getMaxDemand(demandPKList);
		BigDecimal tpDemandSP = BigDecimal.ZERO;
		BigDecimal tpDemandSatSP = BigDecimal.ZERO;
		BigDecimal tpDemandOP = getMaxDemand(demandPKList);
		record.setTPDemandPK(tpDemandPK);
		record.setTPDemandSP(tpDemandSP);
		record.setTPDemandSatSP(tpDemandSatSP);
		record.setTPDemandOP(tpDemandOP);

		// 台電每日累積用電量
		BigDecimal tpdcecPK = record.getDCECPK().add(record.getDCECSP());
		BigDecimal tpdcecSP = BigDecimal.ZERO;
		BigDecimal tpdcecSatSP = BigDecimal.ZERO;
		BigDecimal tpdcecOP = record.getDCECSatSP().add(record.getDCECOP());
		record.setTPDCECPK(tpdcecPK);
		record.setTPDCECSP(tpdcecSP);
		record.setTPDCECSatSP(tpdcecSatSP);
		record.setTPDCECOP(tpdcecOP);

		/*
		 * ------------------------------------------------- 每月 ------------------------------------------------- 
		 */
		demandPKList.clear();
		demandPKList.add(record.getMDemandPK());
		demandPKList.add(record.getMDemandSP());
		demandOPList.clear();
		demandOPList.add(record.getMDemandSatSP());
		demandOPList.add(record.getMDemandOP());
		
		// 台電每月最大需量
		BigDecimal tpmDemandPK = getMaxDemand(demandPKList);
		BigDecimal tpmDemandSP = BigDecimal.ZERO;
		BigDecimal tpmDemandSatSP = BigDecimal.ZERO;
		BigDecimal tpmDemandOP = getMaxDemand(demandOPList);
		record.setTPMDemandPK(tpmDemandPK);
		record.setTPMDemandSP(tpmDemandSP);
		record.setTPMDemandSatSP(tpmDemandSatSP);
		record.setTPMDemandOP(tpmDemandOP);

		// 台電每月累積用電量
		BigDecimal tpmcecPK = record.getMCECPK().add(record.getMCECSP());				// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰
		BigDecimal tpmcecSP = BigDecimal.ZERO;											// 台電計法累計至今當月半尖峰累積用電量
		BigDecimal tpmcecSatSP = BigDecimal.ZERO;										// 台電計法累計至今當月週六半尖峰累積用電量
		BigDecimal tpmcecOP = record.getMCECSatSP().add(record.getMCECOP());			// 台電計法累計至今當月離峰累積用電量 = 周六半 + 離峰
		record.setTPMCECPK(tpmcecPK);
		record.setTPMCECSP(tpmcecSP);
		record.setTPMCECSatSP(tpmcecSatSP);
		record.setTPMCECOP(tpmcecOP);
		
		return record;
	}
	
}
