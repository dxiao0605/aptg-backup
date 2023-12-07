package aptg.rate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.LampBean;
import aptg.manager.ElectricityPriceManager;
import aptg.models.ElectricityPriceModel;
import aptg.models.FcstChargeModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.rate.base.RatePlanBase;
import aptg.task.FcstChargeTask;

/**
 * 	表燈營業用 
 * 
 * @author austinchen
 * 
 */
public class RatePlan1 extends RatePlanBase {
	
	private static final String CLASS_NAME = FcstChargeTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	public RatePlan1() {}

	public RatePlan1(FcstChargeModel model) {
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
	private FcstChargeModel tpmCalculate(FcstChargeModel model) {
		List<BigDecimal> pkList = new ArrayList<>();
		pkList.add(model.getMDemandPK());
		pkList.add(model.getMDemandSP());
		pkList.add(model.getMDemandSatSP());
		pkList.add(model.getMDemandOP());
		/*
		 * 	用電數據
		 */
		this.tpmDemandPK = getMaxDemand(pkList);		// 台電計法本月最大尖峰需量 = 尖峰+半尖峰+周六半+離峰
		this.tpmDemandSP = BigDecimal.ZERO;				// 台電計法本月最大半尖峰需量
		this.tpmDemandSatSP = BigDecimal.ZERO;			// 台電計法本月最大週六半需量
		this.tpmDemandOP = BigDecimal.ZERO;				// 台電計法本月最大離峰需量
		model.setTPMDemandPK(tpmDemandPK);
		model.setTPMDemandSP(tpmDemandSP);
		model.setTPMDemandSatSP(tpmDemandSatSP);
		model.setTPMDemandOP(tpmDemandOP);
		
		this.tpmcecPK = model.getMCECPK().add(model.getMCECSP())
							 .add(model.getMCECSatSP()).add(model.getMCECOP()); // 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰+周六半+離峰
		this.tpmcecSP = BigDecimal.ZERO;										// 台電計法累計至今當月半尖峰累積用電量
		this.tpmcecSatSP = BigDecimal.ZERO;										// 台電計法累計至今當月週六半尖峰累積用電量
		this.tpmcecOP = BigDecimal.ZERO;										// 台電計法累計至今當月離峰累積用電量
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
	private ElectricityPriceModel getPriceDetail() {
		this.price = ElectricityPriceManager.getInstance().getPrice(model.getUseTime(), model.getRatePlanCode());
		if (price==null)
			return null;
		
		/*
		 * 	 電價表
		 */
		if (isSummer) {
			this.LampBPrice1 = price.getLampBPrice1S();
			this.LampBPrice2 = price.getLampBPrice2S();
			this.LampBPrice3 = price.getLampBPrice3S();
			this.LampBPrice4 = price.getLampBPrice4S();

		} else {
			this.LampBPrice1 = price.getLampBPrice1();
			this.LampBPrice2 = price.getLampBPrice2();
			this.LampBPrice3 = price.getLampBPrice3();
			this.LampBPrice4 = price.getLampBPrice4();
		}
		/*
		 * 	用電度數級距
		 */
		this.LampBStep1 = new BigDecimal(price.getLampBStep1());	// ex: 330度
		this.LampBStep2 = new BigDecimal(price.getLampBStep2());	// ex: 700度
		this.LampBStep3 = new BigDecimal(price.getLampBStep3());	// ex: 1500度
		
		this.stepList.add(getLampBean(BigDecimal.ONE, LampBStep1, LampBPrice1));					// ex: 1~330
		this.stepList.add(getLampBean(LampBStep1.add(BigDecimal.ONE), LampBStep2, LampBPrice2));	// ex: 331~700
		this.stepList.add(getLampBean(LampBStep2.add(BigDecimal.ONE), LampBStep3, LampBPrice3));	// ex: 701~1500
		this.stepList.add(getLampBean(LampBStep3.add(BigDecimal.ONE), null, LampBPrice4));			// ex: 1501~

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
		 *	電費合計#
		 */
		List<BigDecimal> lampList = new ArrayList<>();
		for (LampBean lampBean: stepList) {
			BigDecimal start = lampBean.getStart();
			BigDecimal end = lampBean.getEnd();
			BigDecimal price = lampBean.getPrice();
			
			BigDecimal lampCharge = calculatePrice(start, end, price, tpmCEC);
			lampList.add(lampCharge);
		}
		BigDecimal usageCharge = usageCharge(lampList);
		model.setUsageCharge(usageCharge);	// 流動電費

		BigDecimal totalCharge = usageCharge.setScale(0, BigDecimal.ROUND_HALF_UP);
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
		model.setUsageCharge(model.getUsageCharge());	// 流動電費小計 = 電費合計
		
		// 預測本月用電量
//		fcstMCECXX(tpmcecPK, tpmcecSP, tpmcecSatSP, tpmcecOP);
//		or
		setFcstTPMMCEC();
		
		// 預測電費合計
//		BigDecimal fcstMCEC = model.getFcstMCEC();
		
		List<BigDecimal> lampList = new ArrayList<>();
		for (LampBean lampBean: stepList) {
			BigDecimal start = lampBean.getStart();
			BigDecimal end = lampBean.getEnd();
			BigDecimal price = lampBean.getPrice();
			
			BigDecimal lampCharge = calculatePrice(start, end, price, fcstMCEC);
			lampList.add(lampCharge);
		}
		BigDecimal fcstUsageCharge = fcstUsageCharge(lampList);
		model.setFcstUsageCharge(fcstUsageCharge);		// 預測流動電費小記

		BigDecimal fcstTotalCharge = fcstUsageCharge.setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstTotalCharge(fcstTotalCharge);	// 預測電費合計
		logger.info("預測電費合計: "+model.getFcstTotalCharge());
		
		return model;
	}
	private void setFcstTPMMCEC() {
		this.fcstMCECPK = model.getFcstECO5MCECPK().add(model.getFcstECO5MCECSP())
												   .add(model.getFcstECO5MCECSatSP())
												   .add(model.getFcstECO5MCECOP());
		this.fcstMCECSP = BigDecimal.ZERO;
		this.fcstMCECSatSP = BigDecimal.ZERO;
		this.fcstMCECOP = BigDecimal.ZERO;
		this.fcstMCEC = fcstMCECPK.add(fcstMCECSP).add(fcstMCECSatSP).add(fcstMCECOP);

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

	
	private LampBean getLampBean(BigDecimal start, BigDecimal end, BigDecimal price) {
		LampBean lampBean = new LampBean();
		lampBean.setStart(start);
		lampBean.setEnd(end);
		lampBean.setPrice(price);
		
		return lampBean;
	}

	/*
	 * 
	 */
	public PowerRecordCollectionModel tpCollectionValue(PowerRecordCollectionModel record) {
		/*
		 * ------------------------------------------------- 每日 ------------------------------------------------- 
		 */
		List<BigDecimal> demandList = new ArrayList<>();
		demandList.add(record.getDemandPK());
		demandList.add(record.getDemandSP());
		demandList.add(record.getDemandSatSP());
		demandList.add(record.getDemandOP());
		
		// 台電每日最大需量
		BigDecimal tpDemandPK = getMaxDemand(demandList);
		BigDecimal tpDemandSP = BigDecimal.ZERO;
		BigDecimal tpDemandSatSP = BigDecimal.ZERO;
		BigDecimal tpDemandOP = BigDecimal.ZERO;
		record.setTPDemandPK(tpDemandPK);
		record.setTPDemandSP(tpDemandSP);
		record.setTPDemandSatSP(tpDemandSatSP);
		record.setTPDemandOP(tpDemandOP);
		
		// 台電每日累積用電量
		BigDecimal tpdcecPK = record.getDCECPK().add(record.getDCECSP())
								    .add(record.getDCECSatSP()).add(record.getDCECOP());
		BigDecimal tpdcecSP = BigDecimal.ZERO;
		BigDecimal tpdcecSatSP = BigDecimal.ZERO;
		BigDecimal tpdcecOP = BigDecimal.ZERO;
		record.setTPDCECPK(tpdcecPK);
		record.setTPDCECSP(tpdcecSP);
		record.setTPDCECSatSP(tpdcecSatSP);
		record.setTPDCECOP(tpdcecOP);
		
		/*
		 * ------------------------------------------------- 每月 ------------------------------------------------- 
		 */
		demandList.clear();
		demandList.add(record.getMDemandPK());
		demandList.add(record.getMDemandSP());
		demandList.add(record.getMDemandSatSP());
		demandList.add(record.getMDemandOP());
		
		// 台電每月最大需量
		BigDecimal tpmDemandPK = getMaxDemand(demandList);
		BigDecimal tpmDemandSP = BigDecimal.ZERO;
		BigDecimal tpmDemandSatSP = BigDecimal.ZERO;
		BigDecimal tpmDemandOP = BigDecimal.ZERO;
		record.setTPMDemandPK(tpmDemandPK);
		record.setTPMDemandSP(tpmDemandSP);
		record.setTPMDemandSatSP(tpmDemandSatSP);
		record.setTPMDemandOP(tpmDemandOP);

		// 台電每月累積用電量
		BigDecimal tpmcecPK = record.getMCECPK().add(record.getMCECSP())
							 	    .add(record.getMCECSatSP()).add(record.getMCECOP());	// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰+周六半+離峰
		BigDecimal tpmcecSP = BigDecimal.ZERO;												// 台電計法累計至今當月半尖峰累積用電量
		BigDecimal tpmcecSatSP = BigDecimal.ZERO;											// 台電計法累計至今當月週六半尖峰累積用電量
		BigDecimal tpmcecOP = BigDecimal.ZERO;												// 台電計法累計至今當月離峰累積用電量
		record.setTPMCECPK(tpmcecPK);
		record.setTPMCECSP(tpmcecSP);
		record.setTPMCECSatSP(tpmcecSatSP);
		record.setTPMCECOP(tpmcecOP);
		
		return record;
	}
	
}
