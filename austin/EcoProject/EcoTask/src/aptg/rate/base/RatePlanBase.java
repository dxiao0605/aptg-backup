package aptg.rate.base;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.LampBean;
import aptg.models.ElectricityPriceModel;
import aptg.models.FcstChargeModel;
import aptg.task.FcstChargeTask;
import aptg.utils.ToolUtil;

public class RatePlanBase {
	
	private static final Logger logger = LogManager.getFormatterLogger(FcstChargeTask.class.getName());
	
	protected boolean isTest;

	protected boolean isSummer;
	protected FcstChargeModel model;
	protected ElectricityPriceModel price;
	
	/*
	 * 契約容量
	 */
	protected BigDecimal usuallyCC;		// 經常契約容量
	protected BigDecimal spcc;			// 非夏月契約容量
	protected BigDecimal satSPCC;		// 週六半尖峰契約容量
	protected BigDecimal opcc;			// 離峰契約容量
	/*
	 *  台電計法本月最大**需量
	 */
	protected BigDecimal tpmDemandPK;	// 台電計法本月最大尖峰需量
	protected BigDecimal tpmDemandSP;	// 台電計法本月最大半尖峰需量
	protected BigDecimal tpmDemandSatSP;// 台電計法本月最大週六半需量
	protected BigDecimal tpmDemandOP;	// 台電計法本月最大離峰需量
	/*
	 * 台電計法累計當月**月電量
	 */
	protected BigDecimal tpmcecPK;		// 台電計法累計至今當月尖峰累積用電量
	protected BigDecimal tpmcecSP;		//
	protected BigDecimal tpmcecSatSP;	// 台電計法累計至今當月週六半尖峰累積用電量
	protected BigDecimal tpmcecOP;		// 台電計法累計至今當月離峰累積用電量
	protected BigDecimal tpmCEC;		//
	/*
	 * 台電計法累計預測當月**月電量
	 */
	protected BigDecimal fcstMCECPK;
	protected BigDecimal fcstMCECSP;
	protected BigDecimal fcstMCECSatSP;
	protected BigDecimal fcstMCECOP;
	protected BigDecimal fcstMCEC;
	
	/*
	 * 電價
	 */
	protected BigDecimal baseChargePhase;	// 時間電價按戶計收單相or三相
	
	protected BigDecimal baseChargeUsually;	// 時間電價經常契約單價
	protected BigDecimal baseChargeSP;		// 時間電價半尖峰/非夏月契約單價
	protected BigDecimal baseChargeSatSP;	// 時間電價周六半尖峰契約單價
	protected BigDecimal baseChargeOP;		// 時間電價離峰契約單價
	
	protected BigDecimal timeCharge;		// 時間電價流動電費尖峰
	protected BigDecimal timeChargeSP;		// 時間電價流動電費半尖峰
	protected BigDecimal timeChargeSatSP;	// 時間電價流動電費週六半尖峰
	protected BigDecimal timeChargeOP;		// 時間電價流動電費離峰
	
	protected BigDecimal over2KPrice;		// 超過2000度電費單價(0.96)
	
	/*
	 * 	級距 表燈營業
	 */
	protected BigDecimal LampBStep1;	// 表燈營業級距1
	protected BigDecimal LampBStep2;	// 表燈營業級距2
	protected BigDecimal LampBStep3;	// 表燈營業級距3
	protected BigDecimal LampBPrice1;	// 度數分段-第1段電價
	protected BigDecimal LampBPrice2;	// 度數分段-第2段電價
	protected BigDecimal LampBPrice3;	// 度數分段-第3段電價
	protected BigDecimal LampBPrice4;	// 度數分段-第4段電價
	/*
	 * 	級距 表燈非營業
	 */
	protected BigDecimal LampStep1;		// 表燈非營業級距1
	protected BigDecimal LampStep2;		// 表燈非營業級距2
	protected BigDecimal LampStep3;		// 表燈非營業級距3
	protected BigDecimal LampStep4;		// 表燈非營業級距4
	protected BigDecimal LampStep5;		// 表燈非營業級距5
	protected BigDecimal LampPrice1;	// 度數分段-第1段電價
	protected BigDecimal LampPrice2;	// 度數分段-第2段電價
	protected BigDecimal LampPrice3;	// 度數分段-第3段電價
	protected BigDecimal LampPrice4;	// 度數分段-第4段電價
	protected BigDecimal LampPrice5;	// 度數分段-第5段電價
	protected BigDecimal LampPrice6;	// 度數分段-第6段電價
	
	protected List<LampBean> stepList = new ArrayList<>();

	protected static final BigDecimal Usage_UpperLimit = new BigDecimal("2000");
	
	public void printCalculateLog() {
		Path path = Paths.get("print.properties");
		File file = path.toFile();

		if (file.exists())
			this.isTest = true;
		else
			this.isTest = false;
	}
	
	public void tpmxxxHandle() {
		// 計算電費時需量先無條件捨去
		this.tpmDemandPK = tpmDemandPK.setScale(0, BigDecimal.ROUND_FLOOR);
		this.tpmDemandSP = tpmDemandSP.setScale(0, BigDecimal.ROUND_FLOOR);
		this.tpmDemandSatSP = tpmDemandSatSP.setScale(0, BigDecimal.ROUND_FLOOR);
		this.tpmDemandOP = tpmDemandOP.setScale(0, BigDecimal.ROUND_FLOOR);
		// 計算電費時用電量先四捨五入
		this.tpmcecPK = tpmcecPK.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.tpmcecSP = tpmcecSP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.tpmcecSatSP = tpmcecSatSP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.tpmcecOP = tpmcecOP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.tpmCEC = tpmCEC.setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	public void fcstxxxHandle() {
		// 計算電費時用電量先四捨五入
		this.fcstMCECPK = fcstMCECPK.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.fcstMCECSP = fcstMCECSP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.fcstMCECSatSP = fcstMCECSatSP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.fcstMCECOP = fcstMCECOP.setScale(0, BigDecimal.ROUND_HALF_UP);
		this.fcstMCEC = fcstMCEC.setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 	取得月份
	 * 
	 * @param useMonth: (ex: 202008)
	 * @return
	 */
	public int getMonth(String useMonth) {
		return ToolUtil.getInstance().getMonth(useMonth);
	}
	public int getRecDateMonth(String recDate) {
		return ToolUtil.getInstance().getRecDataMonth(recDate);	//  (ex: 2020-08-11)
	}
	
	/**
	 * 	是否為夏季
	 * 
	 * @param month
	 * @return
	 */
	public boolean isSummer(int month) {
		return ToolUtil.getInstance().isSummer(month);
	}
	
	/**
	 * 	實際用電天數
	 * 
	 * @param useTime: yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public BigDecimal getRealUseDay(String useTime) {
		return ToolUtil.getInstance().getRealUseDay(useTime);
	}
	
	/**
	 * 	實際月份天數
	 * 
	 * @param userMonth: yyyyMM
	 * @return
	 */
	public BigDecimal getMonthActualDays(String month) {
		return ToolUtil.getInstance().getMonthActualDays(month);
	}
	
	/**
	 * 	從tpmDemand list內找出最大值
	 * 
	 * @param tpmList
	 * @return
	 */
	public BigDecimal getMaxDemand(List<BigDecimal> tpmList) {
		BigDecimal maxDemand = BigDecimal.ZERO;
		for (BigDecimal tpm: tpmList) {
			if (tpm.compareTo(maxDemand)==1)
				maxDemand = tpm;
		}
		return maxDemand;
	}
	
	/**
	 * 	計算每個級距的電費
	 * 
	 * @param start
	 * @param end
	 * @param price
	 * @return
	 */
	public BigDecimal calculatePrice(BigDecimal start, BigDecimal end, BigDecimal price, BigDecimal tpmCEC) {
		if (isTest)
			logger.info("### tpmCEC: "+tpmCEC+", 級距"+start+"~"+end+", 單價: "+price);
		
		// 總用電度數超過此級距，直接計算此級距的費用
		if (end!=null && tpmCEC.compareTo(end)==1) {
			BigDecimal lampKWH = end.subtract(start.subtract(BigDecimal.ONE));
			BigDecimal lampCharge = price.multiply(lampKWH);
			if (isTest)
				logger.info("### 超過級距: "+price+"x"+lampKWH+" ("+end+"-"+start.subtract(BigDecimal.ONE)+"), 級距費用: "+lampCharge);
			
			return lampCharge; 
		}
		// end==null (最後一個級距)
		if (end==null && tpmCEC.compareTo(start)!=-1) {
			BigDecimal lampKWH = tpmCEC.subtract(start.subtract(BigDecimal.ONE));
			BigDecimal lampCharge = price.multiply(lampKWH);
			if (isTest)
				logger.info("### 最後一級距: "+price+"x"+lampKWH+" ("+tpmCEC+"-"+start.subtract(BigDecimal.ONE)+"), 級距費用: "+lampCharge);
			
			return lampCharge; 
		}
		
		if (tpmCEC.compareTo(start)!=-1 && tpmCEC.compareTo(end)!=1) {
			// 331 <= tpmcecPK <= 700
			BigDecimal lampKWH = tpmCEC.subtract(start.subtract(BigDecimal.ONE));
			BigDecimal lampCharge = price.multiply(lampKWH);
			if (isTest)
				logger.info("### 級距內: "+price+"x"+lampKWH+" ("+tpmCEC+"-"+start.subtract(BigDecimal.ONE)+"), 級距費用: "+lampCharge);
			
			return lampCharge;
		}
		return BigDecimal.ZERO;
	}

	
	
	
	
	/*
	 * ==================================== 以下計算 ================================================
	 */
	
	/**
	 * A.基本電費 - A1. 按戶計收
	 * 
	 * @param baseChargePhase
	 * @return
	 */
	public BigDecimal userCharge(BigDecimal baseChargePhase) {
		BigDecimal userCharge = baseChargePhase;
		if (isTest) 
			logger.info("************* A1. 按戶計收: "+userCharge);
		return userCharge;
	}
	
	/**
	 * A.基本電費 - A2. 經常契約x經常契約電價
	 * 
	 * @param baseChargeUsually: 時間電價經常契約單價(or夏月)
	 * @param usuallyCC: 經常契約容量
	 * @return
	 */
	public BigDecimal usuallyCharge(BigDecimal baseChargeUsually, BigDecimal usuallyCC) {
		// 經常契約x經常契約電價
		BigDecimal usuallyCharge = baseChargeUsually.multiply(usuallyCC);
		if (isTest) 
			logger.info("************* A2. 經常契約("+baseChargeUsually+") x 經常契約電價("+usuallyCC+"): "+usuallyCharge);
		return usuallyCharge;
	}
	
	/**
	 * A.基本電費 - A3. 非夏月經常契約x非夏月契約電價
	 */
	public BigDecimal spccCharge(BigDecimal baseChargeSP, BigDecimal spcc) {
		BigDecimal spccCharge = baseChargeSP.multiply(spcc);
		if (isTest) 
			logger.info("************* A3. 非夏月經常契約("+baseChargeSP+") x 非夏月契約電價("+spcc+"): "+spccCharge);
		return spccCharge;
	}
	
	/**
	 * A.基本電費 - A4. 週六半尖峰/離峰契約容量x契約電價
	 * 
	 * @param chargeOPS: 時間電價離峰契約單價(or夏月)
	 * @param satSPCC: 周六半尖峰契約容量
	 * @param opcc: 離峰契約容量
	 * @param usuallyCC: 經常契約容量
	 * @param spcc: 非夏月契約容量
	 * @return
	 */
	public BigDecimal satOrOPCharge(BigDecimal baseChargeOP, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal satSPCC, BigDecimal opcc) {
		BigDecimal step1 = satSPCC.add(opcc);			// 周六半尖峰契約容量+離峰契約容量
		BigDecimal step2 = (usuallyCC.add(spcc)).multiply(new BigDecimal(0.5));		// (經常契約+非夏月契約容量)x0.5
		BigDecimal step3 = step1.subtract(step2);		// max(0,(X-Y))
		if (step3.compareTo(BigDecimal.ZERO)!=1) {
			step3 = BigDecimal.ZERO;
		}

		// 離峰契約容量電價*max(0,(X-Y))
		BigDecimal satOrOPCharge = baseChargeOP.multiply(step3);
		if (isTest) {
			logger.info("************* step1 X=周六半尖峰契約容量("+satSPCC+")+離峰契約容量("+opcc+")= "+step1);
			logger.info("************* step2 Y= (經常契約"+usuallyCC+" + 非夏月契約容量"+spcc+")x0.5= "+step2);
			logger.info("************* step3 Z=max(0,(X-Y))= "+step3);
			logger.info("************* A4. 週六半尖峰/離峰契約容量"+baseChargeOP+" x z:"+step3+"= "+satOrOPCharge);
		}
		return satOrOPCharge;
	}
	
	/**
	 * A.基本電費 - 基本電費小計 = A1+A2+A3+A4
	 * (四捨五入至小數第一位)
	 * 
	 * @param baseUserCharge
	 * @param baseUsuallyCharge
	 * @param baseOPCharge
	 * @return
	 */
	public BigDecimal baseCharge(BigDecimal userCharge, BigDecimal usuallyCharge, BigDecimal spccCharge, BigDecimal satOrOPCharge) {
		BigDecimal baseTotalCharge = userCharge.add(usuallyCharge).add(spccCharge).add(satOrOPCharge).setScale(1, BigDecimal.ROUND_HALF_UP);
		return baseTotalCharge;
	}
	
	
	
	/**
	 * B.流動電費 - 尖峰用電量*尖峰電價
	 * 
	 * @param timeCharge(S): 時間電價流動電費尖峰(or夏月) 
	 * @param tpmcecPK(S): 台電計法累計至今當月尖峰累積用電量
	 * @return
	 */
	public BigDecimal usagePKCharge(BigDecimal timeCharge, BigDecimal tpmcecPK) {
		BigDecimal usagePKCharge = timeCharge.multiply(tpmcecPK);	// 時間電價流動電費尖峰(夏月) * 台電計法累計至今當月尖峰累積用電量 
		if (isTest) {
//			logger.info("************* 時間電價流動電費尖峰(夏月): "+ timeCharge);
//			logger.info("************* 台電計法累計至今當月尖峰累積用電量: "+ tpmcecPK);
			logger.info("************* 尖峰累積用電量"+tpmcecPK+" x 尖峰電價"+timeCharge+": "+usagePKCharge);
		}
		return usagePKCharge;
	}

	/**
	 * B.流動電費 - 半尖峰用電量*半尖峰電價
	 * 
	 * @param timeChargeSP(S): 時間電價流動電費半尖峰(or夏月)
	 * @param tpmcecSP(S): 台電計法累計至今當月半尖峰累積用電量
	 * @return
	 */
	public BigDecimal usageSPCharge(BigDecimal timeChargeSP, BigDecimal tpmcecSP) {
		// 時間電價流動電費週六半尖峰(夏月) * 台電計法累計至今當月週六半尖峰累積用電量
		BigDecimal usageSPCharge = timeChargeSP.multiply(tpmcecSP);
		if (isTest) {
//			logger.info("************* 時間電價流動電費半尖峰(夏月): "+ timeChargeSP);
//			logger.info("************* 台電計法累計至今當月半尖峰累積用電量: "+ tpmcecSP);
			logger.info("************* 半尖峰用電量"+tpmcecSP+" x 半尖峰電價"+timeChargeSP+": "+usageSPCharge);	
		}
		return usageSPCharge;
	}

	/**
	 * B.流動電費 - 周六半尖峰用電量*周六半尖峰電價
	 * 
	 * @param timeChargeSatSP(S): 時間電價流動電費週六半尖峰(or夏月)
	 * @param tpmcecSatSP(S): 台電計法累計至今當月週六半尖峰累積用電量
	 * @return
	 */
	public BigDecimal usageSatSPCharge(BigDecimal timeChargeSatSP, BigDecimal tpmcecSatSP) {
		// 時間電價流動電費週六半尖峰(夏月) * 台電計法累計至今當月週六半尖峰累積用電量
		BigDecimal usageSatSPCharge = timeChargeSatSP.multiply(tpmcecSatSP);
		if (isTest) {
//			logger.info("************* 時間電價流動電費週六半尖峰(夏月): "+ timeChargeSatSP);
//			logger.info("************* 台電計法累計至今當月週六半尖峰累積用電量: "+ tpmcecSatSP);
			logger.info("************* 周六半尖峰用電量"+tpmcecSatSP+" x 周六半尖峰電價"+timeChargeSatSP+": "+usageSatSPCharge);
		}
		return usageSatSPCharge;
	}

	/**
	 * B.流動電費 - 離峰累積用電量*離峰電價
	 * 
	 * @param timeChargeOP(S): 時間電價流動電費離峰(or夏月)
	 * @param tpmcecOP(S): 台電計法累計至今當月離峰累積用電量
	 * @return
	 */
	public BigDecimal usageOPCharge(BigDecimal timeChargeOP, BigDecimal tpmcecOP) {
		// 時間電價流動電費離峰(夏月) * 台電計法累計至今當月離峰累積用電量
		BigDecimal usageOPCharge = timeChargeOP.multiply(tpmcecOP);
		if (isTest) {
//			logger.info("************* 時間電價流動電費離峰(夏月): "+ timeChargeOP);
//			logger.info("************* 台電計法累計至今當月離峰累積用電量: "+ tpmcecOP);
			logger.info("************* 離峰累積用電量"+tpmcecOP+" x 離峰電價"+timeChargeOP+": "+usageOPCharge);	
		}
		return usageOPCharge;
	}
	
	/**
	 * B.流動電費 - 流動電費小計 = B1+B2+B3
	 * 
	 * @param usagePKCharge
	 * @param usageSPCharge
	 * @param usageSatSPCharge
	 * @param usageOPCharge
	 * @return
	 */
	public BigDecimal usageCharge(BigDecimal usagePKCharge, BigDecimal usageSPCharge, BigDecimal usageSatSPCharge, BigDecimal usageOPCharge) {
		BigDecimal usageCharge = usagePKCharge.add(usageSPCharge).add(usageSatSPCharge).add(usageOPCharge);
		return usageCharge;
	}
	
	
	
	/**
	 * C.超約 - C1.夏月尖峰超約
	 * 
	 * @param usuallyCC: 經常契約容量
	 * @param tpmDemandPK: 尖峰最大需量
	 * @return
	 */
	public BigDecimal overUsuallyCC(BigDecimal baseChargeUsually, BigDecimal usuallyCC, BigDecimal tpmDemandPK) {
		BigDecimal step0 = usuallyCC;	// 契約容量
		BigDecimal step1 = step0.divide(new BigDecimal(10), 0, BigDecimal.ROUND_CEILING);	// 契約容量的10%(無條件進位)
		BigDecimal step2 = getOverUsuallyCC(tpmDemandPK, usuallyCC);	// 超約量 = 尖峰最大需量 - 經常契約容量

		// 在契約容量10%以下按2倍計收基本電費
		BigDecimal step3;
		if (step2.compareTo(step1)==1) {
			step3 = step1.multiply(baseChargeUsually).multiply(new BigDecimal(2));
		} else {
			step3 = step2.multiply(baseChargeUsually).multiply(new BigDecimal(2));
		}
		
		// 超過契約容量10%按3倍計收基本電費
		BigDecimal step4 = step2.subtract(step1);
		if (step4.compareTo(BigDecimal.ZERO)==1) {
			step4 = step4.multiply(baseChargeUsually).multiply(new BigDecimal(3));
		} else {
			step4 = BigDecimal.ZERO;
		}

		// 夏月尖峰超約費用
		BigDecimal overPKCharge = step3.add(step4);
		if (isTest) {
			logger.info("************* step 0 契約容量= "+step0);
			logger.info("************* step 1 契約容量的10%(無條件進位)= "+step1);
			logger.info("************* step 2 超約量=尖峰最大需量"+tpmDemandPK+" - 經常契約容量"+usuallyCC+"= "+step2);
			logger.info("************* step 3 在契約容量10%以下按2倍計收基本電費= "+step3+"\t"+ "if("+step2+">"+step1+") { "+step1+"*"+baseChargeUsually+"*2 } else { "+step2+"*"+baseChargeUsually+"*3 }");
			logger.info("************* step 4 超過契約容量10%按3倍計收基本電費= "+step4+"\t"+ "if("+step2+"-"+step1+" >0) { ("+step2+"-"+step1+")*"+baseChargeUsually+"*3} else { 0 }");
			
			logger.info("************* C1. 夏月尖峰超約費用= "+overPKCharge);	
		}
		return overPKCharge;
	}
	
	/**
	 * C.超約 - C2. 非夏月尖峰超約
	 * 
	 * @param chargeUsually
	 * @param usuallyCC
	 * @param spcc
	 * @param tpmDemandPK
	 * @return
	 */
	public BigDecimal overSPCC(BigDecimal baseChargeUsually, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal tpmDemandPK, int ratePlan) {
		BigDecimal step0 = usuallyCC.add(spcc);	// 契約容量 = 經常契約容量 + 非夏月契約容量
		BigDecimal step1 = step0.divide(new BigDecimal(10), 0, BigDecimal.ROUND_CEILING);	// 契約容量的10%(無條件進位)
		BigDecimal step2 = getOverSPCC(tpmDemandPK, usuallyCC, spcc);	// 超約量=尖峰時間最大需量-契約容量
		
		if (ratePlan==9) {
			step2 = step2.subtract(new BigDecimal(model.getOverPK()));
			if (step2.compareTo(BigDecimal.ZERO)!=1) {
				step2 = BigDecimal.ZERO;
			}
		}
		
		// 在契約容量10%以下按2倍計收基本電費
		BigDecimal step3;
		if (step2.compareTo(step1)==1) {
			step3 = step1.multiply(baseChargeUsually).multiply(new BigDecimal(2));
		} else {
			step3 = step2.multiply(baseChargeUsually).multiply(new BigDecimal(2));
		}
		
		// 超過契約容量10%按3倍計收基本電費
		BigDecimal step4 = step2.subtract(step1);
		if (step4.compareTo(BigDecimal.ZERO)==1) {
			step4 = step4.multiply(baseChargeUsually).multiply(new BigDecimal(3));
		} else {
			step4 = BigDecimal.ZERO;
		}
		
		// 非夏月尖峰超約費用
		BigDecimal overPKCharge = step3.add(step4);
		if (isTest) {
			logger.info("************* step 0 契約容量=經常契約容量"+usuallyCC+" + 非夏月契約容量"+spcc+"= "+ step0);
			logger.info("************* step 1 契約容量的10%(無條件進位)= "+ step1);
			logger.info("************* step 2 超約量=尖峰時間最大需量"+tpmDemandPK+" - 契約容量"+step0+"= "+ step2);
			logger.info("************* step 3 在契約容量10%以下按2倍計收基本電費= "+step3+"\t"+ "if("+step2+">"+step1+") { "+step1+"*"+baseChargeUsually+"*2 } else { "+step2+"*"+baseChargeUsually+"*3 }");
			logger.info("************* step 4 超過契約容量10%按3倍計收基本電費= "+step4+"\t"+ "if("+step2+"-"+step1+" >0) { ("+step2+"-"+step1+")*"+baseChargeUsually+"*3} else { 0 }");
			
			logger.info("************* C2. 非夏月尖峰超約費用= "+overPKCharge);
		}
		return overPKCharge;
	}
	
	/**
	 * C.超約 - C3. 周六半尖峰超約
	 * 
	 * @param baseChargeSatSPS
	 * @param usuallyCC
	 * @param spcc
	 * @param satSPCC
	 * @param tpmDemandSatSP
	 * @param tpmDemandPK
	 * @return
	 */
	public BigDecimal overSatSPCC(BigDecimal baseChargeSatSPS, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal satSPCC, BigDecimal tpmDemandSatSP, BigDecimal tpmDemandPK, int ratePlan) {
		BigDecimal step0 = usuallyCC.add(spcc).add(satSPCC);	// 周六半尖峰契約容量 = 經常契約容量 + 非夏月契約容量 + 周六半尖峰契約容量 (不管是否為夏月/非夏月)
		BigDecimal step1 = step0.divide(new BigDecimal(10), 0, BigDecimal.ROUND_CEILING);	// 契約容量的10%(無條件進位)
		BigDecimal step2 = getOverSatSPCC(tpmDemandSatSP, usuallyCC, spcc, satSPCC);	// 超約量 = 周六半尖峰契約容量 - 尖峰超約量
		
		// 計算取得 夏/非夏約尖峰超約量
		BigDecimal overCC;
		if (ratePlan==9) {
			BigDecimal overPK = new BigDecimal(model.getOverPK());
			BigDecimal overSP = new BigDecimal(model.getOverSP());
			
			overCC = (overPK.compareTo(overSP)==1) ? overPK : overSP;
		} else {
			overCC = (isSummer) ? getOverUsuallyCC(tpmDemandPK, usuallyCC) : getOverSPCC(tpmDemandPK, usuallyCC, spcc);
		}
		
		BigDecimal step3 = step2.subtract(overCC);	// 排除尖峰超約量後的超約量 = 周六半尖峰超約 - 夏月/非夏月尖峰超約量
		if (step3.compareTo(BigDecimal.ZERO)!=1) {
			step3 = BigDecimal.ZERO;
		}

		// 在契約容量10%以下按2倍計收基本電費
		BigDecimal step4;
		if (step3.compareTo(step1)==1) {
			step4 = step1.multiply(baseChargeSatSPS).multiply(new BigDecimal(2));
		} else {
			step4 = step3.multiply(baseChargeSatSPS).multiply(new BigDecimal(2));
		}

		// 超過契約容量10%按3倍計收基本電費
		BigDecimal step5 = step3.subtract(step1);
		if (step5.compareTo(BigDecimal.ZERO)==1) {
			step5 = step5.multiply(baseChargeSatSPS).multiply(new BigDecimal(3));
		} else {
			step5 = BigDecimal.ZERO;
		}
		
		// 周六半尖峰尖峰超約費用
		BigDecimal overSatSPCCCharge = step4.add(step5);
		if (isTest) {
			logger.info("************* step 0 周六半尖峰契約容量=經常契約容量:"+usuallyCC+" +非夏月契約容量:"+spcc+" +周六半尖峰契約容量 (不管是否為夏月/非夏月):"+satSPCC+" = "+ step0);
			logger.info("************* step 1 契約容量的10%(無條件進位): "+ step1);
			logger.info("************* step 2 超約量=周六半尖峰契約容量-尖峰超約量: "+ step2);
			logger.info("************* step 3 排除尖峰超約量後的超約量: "+ step3+"\t"+ "=> 周六半尖峰超約量:"+step2+"-夏月or非夏月超約量:"+overCC);
			logger.info("************* step 4 在契約容量10%以下按2倍計收基本電費= "+step4+"\t"+ "if("+step3+">"+step1+") { "+step1+"*"+baseChargeSatSPS+"*2 } else { "+step3+"*"+baseChargeSatSPS+"*3 }");
			logger.info("************* step 5 超過契約容量10%按3倍計收基本電費= "+step5+"\t"+ "if("+step3+"-"+step1+" >0) { ("+step3+"-"+step1+")*"+baseChargeSatSPS+"*3} else { 0 }");
			
			logger.info("************* C3. 周六半尖峰尖峰超約費用: "+overSatSPCCCharge);
		}
		return overSatSPCCCharge;
	}
	
	/**
	 * C.超約 - C4. 離峰超約
	 * 
	 * @param baseChargeOPS
	 * @param usuallyCC
	 * @param spcc
	 * @param satSPCC
	 * @param opcc
	 * @param tpmDemandOP
	 * @param tpmDemandPK
	 * @param tpmDemandSatSP
	 * @return
	 */
	public BigDecimal overOPCC(BigDecimal baseChargeOPS, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal satSPCC, BigDecimal opcc, BigDecimal tpmDemandOP, BigDecimal tpmDemandPK, BigDecimal tpmDemandSatSP, int ratePlan) {
		BigDecimal step0 = usuallyCC.add(spcc).add(satSPCC).add(opcc);	// 離峰契約容量 = 經常契約容量 + 非夏月契約容量 + 周六半尖峰契約容量 + 離峰契約容量
		BigDecimal step1 = step0.divide(new BigDecimal(10), 0, BigDecimal.ROUND_CEILING);	// 契約容量的10%(無條件進位)
		BigDecimal step2 = getOverOPCC(tpmDemandOP, usuallyCC, spcc, satSPCC, opcc);	// 超約量 = 離峰最大需量 - 離峰契約容量 - 尖峰時間或週六半尖峰時間超出瓩數之較大者

//		// 計算取得 夏/非夏約尖峰超約量
//		BigDecimal overCC = (isSummer) ?
//							getOverUsuallyCC(tpmDemandPK, usuallyCC) :
//							getOverSPCC(tpmDemandPK, usuallyCC, spcc);
//		// 計算取得 周六半尖峰超約量
//		BigDecimal overSatCC = getOverSatSPCC(tpmDemandSatSP, usuallyCC, spcc, satSPCC);
//		// 取 夏/非夏約尖峰超約量 or 周六半尖峰超約量，其較大值
//		BigDecimal max = (overCC.compareTo(overSatCC)==1) ? overCC : overSatCC;
//		BigDecimal step3 = step2.subtract(max);	// 排除尖峰時間或週六半尖峰時間超出瓩數之較大者
//		if (step3.compareTo(BigDecimal.ZERO)!=1) {
//			step3 = BigDecimal.ZERO;
//		}
		
		BigDecimal overCC=BigDecimal.ZERO, overSatCC=BigDecimal.ZERO, max=BigDecimal.ZERO;
		if (ratePlan==9) {
			BigDecimal overPK = new BigDecimal(model.getOverPK());
			BigDecimal overSP = new BigDecimal(model.getOverSP());
			BigDecimal overSatSP = new BigDecimal(model.getOverSatSP());
			
			max = (overPK.compareTo(overSP)==1) ? overPK : overSP;
			max = (max.compareTo(overSatSP)==1) ? max : overSatSP;
			
		} else {
			// 計算取得 夏/非夏約尖峰超約量
			overCC = (isSummer) ?
					 getOverUsuallyCC(tpmDemandPK, usuallyCC) :
					 getOverSPCC(tpmDemandPK, usuallyCC, spcc);
			// 計算取得 周六半尖峰超約量
			overSatCC = getOverSatSPCC(tpmDemandSatSP, usuallyCC, spcc, satSPCC);
			// 取 夏/非夏約尖峰超約量 or 周六半尖峰超約量，其較大值
			max = (overCC.compareTo(overSatCC)==1) ? overCC : overSatCC;
		}
		
		BigDecimal step3 = step2.subtract(max);	// 排除尖峰時間或週六半尖峰時間超出瓩數之較大者
		if (step3.compareTo(BigDecimal.ZERO)!=1) {
			step3 = BigDecimal.ZERO;
		}
		
		// 在契約容量10%以下按2倍計收基本電費
		BigDecimal step4;
		if (step3.compareTo(step1)==1) {
			step4 = step1.multiply(baseChargeOPS).multiply(new BigDecimal(2));
		} else {
			step4 = step3.multiply(baseChargeOPS).multiply(new BigDecimal(2));
		}
		
		// 超過契約容量10%按3倍計收基本電費
		BigDecimal step5 = step3.subtract(step1);
		if (step5.compareTo(BigDecimal.ZERO)==1) {
			step5 = step5.multiply(baseChargeOPS).multiply(new BigDecimal(3));
		} else {
			step5 = BigDecimal.ZERO;
		}

		// 離峰超約費用
		BigDecimal overOPCCCharge = step4.add(step5);
		if (isTest) {
			logger.info("************* step 0 離峰契約容量=經常契約容量:"+usuallyCC+" +非夏月契約容量:"+spcc+" +周六半尖峰契約容量:"+satSPCC+" +離峰契約容量:"+opcc+ " = "+ step0);
			logger.info("************* step 1 契約容量的10%(無條件進位): "+ step1);
			logger.info("************* step 2 超約量=離峰最大需量:"+tpmDemandOP+" -離峰契約容量:"+step0+" -尖峰時間或週六半尖峰時間超出瓩數之較大者 = "+ step2);
			logger.info("************* step 3 排除尖峰時間或週六半尖峰時間超出瓩數之較大者: "+ step3);
			if (ratePlan==9) {
				logger.info("************* step 3 排除尖峰、半尖峰、周六半尖峰超約量後的超約量: "+ step3+"\t"+ "=> 尖峰超約量: "+model.getOverPK()+", 半尖峰超約量: "+model.getOverSP()+", 周六半尖峰超約量: "+model.getOverSatSP());
			} else {
				logger.info("************* step 3 排除尖峰超約量後的超約量: "+ step3+"\t"+ "=> 離峰超約量:"+step2+"- Max(夏月or非夏月:"+overCC+", 週六半尖峰:"+overSatCC+")超約量"+"=>"+max);	
			}
			
			logger.info("************* step 4 在契約容量10%以下按2倍計收基本電費= "+step4+"\t"+ "if("+step3+">"+step1+") { "+step1+"*"+baseChargeOPS+"*2 } else { "+step3+"*"+baseChargeOPS+"*3 }");
			logger.info("************* step 5 超過契約容量10%按3倍計收基本電費= "+step5+"\t"+ "if("+step3+"-"+step1+" >0) { ("+step3+"-"+step1+")*"+baseChargeOPS+"*3} else { 0 }");
			
			logger.info("************* C4. 離峰超約費用: "+overOPCCCharge);
		}
		return overOPCCCharge;
	}
	
	/**
	 * C.超約 - 非約定電費小計 = 夏月C1+C3+C4 or 非夏月C2+C3+C4
	 * (四捨五入至小數第一位)
	 * 
	 * @param overUsuallyCharge
	 * @param overSatSPCCCharge
	 * @param overOPCCCharge
	 * @return
	 */
	public BigDecimal overCharge(BigDecimal overPKCharge, BigDecimal overSatSPCCCharge, BigDecimal overOPCCCharge) {
		BigDecimal overTotalCharge = overPKCharge.add(overSatSPCCCharge).add(overOPCCCharge).setScale(1, BigDecimal.ROUND_HALF_UP);
		return overTotalCharge;
	}
	public BigDecimal overCharge(BigDecimal overPKCharge, BigDecimal overSPCharge, BigDecimal overSatSPCCCharge, BigDecimal overOPCCCharge) {
		BigDecimal overTotalCharge = overPKCharge.add(overSPCharge).add(overSatSPCCCharge).add(overOPCCCharge).setScale(1, BigDecimal.ROUND_HALF_UP);
		return overTotalCharge;
	}
	
	/**
	 * 	加計流動電費
	 * (for RateCode=3, 4)
	 * 
	 * @param tpmCEC
	 * @return
	 */
	public BigDecimal usageOver2KCharge(BigDecimal over2KPrice, BigDecimal tpmCEC) {
		BigDecimal usageOver = tpmCEC.subtract(Usage_UpperLimit);
		if (usageOver.compareTo(BigDecimal.ZERO)==1) {
			BigDecimal usageOverCharge = usageOver.multiply(over2KPrice);
			if (isTest)
				logger.info("************* (tpmCEC:"+tpmCEC+" - "+Usage_UpperLimit+") * over2KPrice: "+over2KPrice+" = 加計流動電費: "+ usageOverCharge);

			return usageOverCharge;
		}
		if (isTest)
			logger.info("************* 加計流動電費: "+ BigDecimal.ZERO);
		
		return BigDecimal.ZERO;
	}
	
	/**
	 * 流動電費合計
	 * (四捨五入至小數第一位)
	 * 
	 * @param usageCharge
	 * @param over2KCharge
	 * @return
	 */
	public BigDecimal totalUsageCharge(BigDecimal usageCharge, BigDecimal over2KCharge) {
		BigDecimal totalUsageCharge = usageCharge.add(over2KCharge).setScale(1, BigDecimal.ROUND_HALF_UP);
		return totalUsageCharge;
	}

	public BigDecimal totalFcstUsageCharge(BigDecimal usageCharge, BigDecimal over2KCharge) {
		BigDecimal totalUsageCharge = usageCharge.add(over2KCharge).setScale(1, BigDecimal.ROUND_HALF_UP);
		return totalUsageCharge;
	}
	
	
	/**
	 * 	電費合計###
	 * (四捨五入至整數)
	 * 
	 * @param baseTotalCharge
	 * @param usageTotalCharge
	 * @param overTotalCharge
	 * @return
	 */
	public BigDecimal totalCharge(BigDecimal baseTotalCharge, BigDecimal usageTotalCharge, BigDecimal overTotalCharge) {
		BigDecimal totalCharge = baseTotalCharge.add(usageTotalCharge).add(overTotalCharge).setScale(0, BigDecimal.ROUND_HALF_UP);
		return totalCharge;
	}

	/**
	 * 	計算預測電費合計###
	 * (for RateCode=1, 2)
	 * 
	 * @param lampList
	 * @return
	 */
	public BigDecimal fcstUsageCharge(List<BigDecimal> lampList) {
		BigDecimal fcstUsageCharge = usageCharge(lampList);
		return fcstUsageCharge;
	}
	/**
	 * 	電費合計###
	 * (for RateCode=1, 2)
	 * (四捨五入至小數第一位)
	 * 
	 * @param lampList
	 * @return
	 */
	public BigDecimal usageCharge(List<BigDecimal> lampList) {
		BigDecimal usageCharge = BigDecimal.ZERO;
		for (BigDecimal lampCharge: lampList) {
			usageCharge = usageCharge.add(lampCharge);
		}
		usageCharge = usageCharge.setScale(1, BigDecimal.ROUND_HALF_UP);
		return usageCharge;
	}
	
	/**
	 * 	預測當月mcecPK, mcecSP, mcecSatSP, mcecOP, mCEC
	 * 
	 * @param tpmcecPK
	 * @param tpmcecSP
	 * @param tpmcecSatSP
	 * @param tpmcecOP
	 */
	public void fcstMCECXX(BigDecimal tpmcecPK, BigDecimal tpmcecSP, BigDecimal tpmcecSatSP, BigDecimal tpmcecOP) {
		BigDecimal useDays = getRealUseDay(model.getUseTime());					// 假設上述實際用電天數只有20天
		BigDecimal monthActualDays = getMonthActualDays(model.getUseMonth());	// 實際月份天數
		
		/*
		 *	預測當月 尖峰累積用電量
		 */
		BigDecimal fcstMCECPK = tpmcecPK.divide(useDays, 2, BigDecimal.ROUND_HALF_UP).multiply(monthActualDays).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstMCECPK(fcstMCECPK);
		/*
		 * 	預測當月 半尖峰累積用電量
		 */
		BigDecimal fcstMCECSP = tpmcecSP.divide(useDays, 2, BigDecimal.ROUND_HALF_UP).multiply(monthActualDays).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstMCECSP(fcstMCECSP);
		/*
		 * 	預測當月 週六半尖峰累積用電量
		 */
		BigDecimal fcstMCECSatSP = tpmcecSatSP.divide(useDays, 2, BigDecimal.ROUND_HALF_UP).multiply(monthActualDays).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstMCECSatSP(fcstMCECSatSP);;
		/*
		 * 	預測當月離峰累積用電量
		 */
		BigDecimal fcstMCECOP = tpmcecOP.divide(useDays, 2, BigDecimal.ROUND_HALF_UP).multiply(monthActualDays).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstMCECOP(fcstMCECOP);
		/*
		 * 	預測當月總用電量
		 */
		BigDecimal fcstMCEC = fcstMCECPK.add(fcstMCECSP).add(fcstMCECSatSP).add(fcstMCECOP).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.setFcstMCEC(fcstMCEC);

		if (isTest) {
			logger.info("************* 假設上述實際用電天數只有: "+useDays+" 天");
			logger.info("************* 實際月份天數: "+monthActualDays);

			logger.info("************* 預測當月尖峰累積用電量: "+fcstMCECPK);
			logger.info("************* 預測當月半尖峰累積用電量: "+fcstMCECSP);
			logger.info("************* 預測當月週六半尖峰累積用電量: "+fcstMCECSatSP);
			logger.info("************* 預測當月離峰累積用電量: "+fcstMCECOP);
			logger.info("************* 預測當月總累積用電量: "+fcstMCEC);
		}
	}
	
	/**
	 * 	計算預測電費合計###
	 * 
	 * @param baseTotalCharge
	 * @param fcstUsageCharge
	 * @param overTotalCharge
	 */
	public BigDecimal fcstTotalCharge(BigDecimal baseTotalCharge, BigDecimal totalFcstUsageCharge, BigDecimal overTotalCharge) {
		BigDecimal fcstTotalCharge = baseTotalCharge.add(totalFcstUsageCharge).add(overTotalCharge).setScale(0, BigDecimal.ROUND_HALF_UP);
		return fcstTotalCharge;
	}

	
	
	
	
	/**
	 * 	夏月 超約量 = 尖峰最大需量 - 經常契約容量
	 * 
	 * @param tpmDemandPK
	 * @param usuallyCC
	 * @return
	 */
	private BigDecimal getOverUsuallyCC(BigDecimal tpmDemandPK, BigDecimal usuallyCC) {
		BigDecimal overUsuallyCC = tpmDemandPK.subtract(usuallyCC);
		if (overUsuallyCC.compareTo(BigDecimal.ZERO)==-1)
			overUsuallyCC = BigDecimal.ZERO;

		model.setOverPK(overUsuallyCC.intValue());

		if (isTest)
			logger.info("************* overPK= "+model.getOverPK());
		
		return overUsuallyCC;
	}
	
	/**
	 * 	非夏月 超約量 = 尖峰時間最大需量 - 契約容量(經常契約容量+非夏月契約容量)
	 * 
	 * @param tpmDemandPK
	 * @param usuallyCC
	 * @param spcc
	 * @return
	 */
	private BigDecimal getOverSPCC(BigDecimal tpmDemandPK, BigDecimal usuallyCC, BigDecimal spcc) {
		BigDecimal overSPCC = tpmDemandPK.subtract(usuallyCC.add(spcc));
		if (overSPCC.compareTo(BigDecimal.ZERO)==-1)
			overSPCC = BigDecimal.ZERO;

		model.setOverSP(overSPCC.intValue());

		if (isTest)
			logger.info("************* overSP= "+model.getOverSP());

		return overSPCC;
	}
	
	/**
	 * 	週六 超約量 = 周六半尖峰契約容量 - 尖峰超約量
	 * 
	 * @param tpmDemandSatSP
	 * @param usuallyCC
	 * @param spcc
	 * @param satSPCC
	 * @return
	 */
	private BigDecimal getOverSatSPCC(BigDecimal tpmDemandSatSP, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal satSPCC) {
		BigDecimal overSatSPCC = tpmDemandSatSP.subtract(usuallyCC.add(spcc).add(satSPCC));
		if (overSatSPCC.compareTo(BigDecimal.ZERO)==-1)
			overSatSPCC = BigDecimal.ZERO;

		model.setOverSatSP(overSatSPCC.intValue());

		if (isTest)
			logger.info("************* overSatSP= "+model.getOverSatSP());
		
		return overSatSPCC;
	}
	
	/**
	 *	離峰 超約量 = 離峰最大需量 - 離峰契約容量 - 尖峰時間或週六半尖峰時間超出瓩數之較大者
	 *
	 * @param tpmDemandOP
	 * @param usuallyCC
	 * @param spcc
	 * @param satSPCC
	 * @param opcc
	 * @return
	 */
	private BigDecimal getOverOPCC(BigDecimal tpmDemandOP, BigDecimal usuallyCC, BigDecimal spcc, BigDecimal satSPCC, BigDecimal opcc) {
		BigDecimal overOPCC = tpmDemandOP.subtract(usuallyCC.add(spcc).add(satSPCC).add(opcc));
		if (overOPCC.compareTo(BigDecimal.ZERO)==-1)
			overOPCC = BigDecimal.ZERO;

		model.setOverOP(overOPCC.intValue());

		if (isTest)
			logger.info("************* overOP= "+model.getOverOP());
		
		return overOPCC;
	}
}
