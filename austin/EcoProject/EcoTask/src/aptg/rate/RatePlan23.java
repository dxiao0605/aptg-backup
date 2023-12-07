package aptg.rate;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.models.FcstChargeModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.rate.base.RatePlanBase;
import aptg.task.FcstChargeTask;

/**
 * 	 自訂C-空調分攤
 * 
 * @author austinchen
 *
 */
public class RatePlan23 extends RatePlanBase {

	private static final String CLASS_NAME = FcstChargeTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final int RatePlanCode = 23;
	
	public RatePlan23() {}
	
	public RatePlan23(FcstChargeModel model) {
		// 是否夏月
		this.isSummer = isSummer(getMonth(model.getUseMonth()));
		
		// 台電用電時段歸類
		this.model = tpmCalculate(model);

//		// 取得電價表
//		this.price = getPriceDetail();
//		
//		// isTest=true => 開啟測試log
//		printCalculateLog();
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
		this.tpmDemandPK = model.getMDemandPK().add(model.getMDemandSP())
									  .add(model.getMDemandSatSP()).add(model.getMDemandOP());	// 台電計法本月最大尖峰需量 = 尖峰+半尖峰+周六半+離峰
		this.tpmDemandSP = BigDecimal.ZERO;									// 台電計法本月最大半尖峰需量
		this.tpmDemandSatSP = BigDecimal.ZERO;								// 台電計法本月最大週六半需量
		this.tpmDemandOP = BigDecimal.ZERO;									// 台電計法本月最大離峰需量
		
		this.tpmcecPK = model.getMCECPK().add(model.getMCECSP())
								   .add(model.getMCECSatSP()).add(model.getMCECOP());	// 台電計法累計至今當月尖峰累積用電量 = 尖峰+半尖峰+周六半+離峰
		this.tpmcecSP = BigDecimal.ZERO;										// 台電計法累計至今當月半尖峰累積用電量
		this.tpmcecSatSP = BigDecimal.ZERO;									// 台電計法累計至今當月週六半尖峰累積用電量
		this.tpmcecOP = BigDecimal.ZERO;										// 台電計法累計至今當月離峰累積用電量
		this.tpmCEC = tpmcecPK.add(tpmcecSP).add(tpmcecSatSP).add(tpmcecOP);	// 台電計法累計至今當月總累積用電量

		model.setTPMDemandPK(tpmDemandPK);
		model.setTPMDemandSP(tpmDemandSP);
		model.setTPMDemandSatSP(tpmDemandSatSP);
		model.setTPMDemandOP(tpmDemandOP);
		model.setTPMCECPK(tpmcecPK);
		model.setTPMCECSP(tpmcecSP);
		model.setTPMCECSatSP(tpmcecSatSP);
		model.setTPMCECOP(tpmcecOP);
		model.setTPMCEC(tpmCEC);
		
		return model;
	}

	/**
	 * 	夏月/非夏月 計算
	 * 
	 * @return
	 */
	private FcstChargeModel calculate() {

		return model;
	}

	public FcstChargeModel calculateFcstCharge() {

		return model;
	}

	/*
	 * 
	 */
	public PowerRecordCollectionModel tpCollectionValue(PowerRecordCollectionModel record) {
		
		return record;
	}
}
