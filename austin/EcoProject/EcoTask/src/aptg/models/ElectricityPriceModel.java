package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class ElectricityPriceModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Year;
	private String Month;
	private int RatePlanCode;
	private BigDecimal LampPrice1;
	private BigDecimal LampPrice2;
	private BigDecimal LampPrice3;
	private BigDecimal LampPrice4;
	private BigDecimal LampPrice5;
	private BigDecimal LampPrice6;
	private BigDecimal LampPrice1S;
	private BigDecimal LampPrice2S;
	private BigDecimal LampPrice3S;
	private BigDecimal LampPrice4S;
	private BigDecimal LampPrice5S;
	private BigDecimal LampPrice6S;
	private Integer LampStep1;
	private Integer LampStep2;
	private Integer LampStep3;
	private Integer LampStep4;
	private Integer LampStep5;
	private BigDecimal LampBPrice1;
	private BigDecimal LampBPrice2;
	private BigDecimal LampBPrice3;
	private BigDecimal LampBPrice4;
	private BigDecimal LampBPrice1S;
	private BigDecimal LampBPrice2S;
	private BigDecimal LampBPrice3S;
	private BigDecimal LampBPrice4S;
	private Integer LampBStep1;
	private Integer LampBStep2;
	private Integer LampBStep3;
	private BigDecimal BaseCharge1phase;
	private BigDecimal BaseCharge3phase;
	private BigDecimal BaseChargeUsually;
	private BigDecimal BaseChargeSP;
	private BigDecimal BaseChargeSatSP;
	private BigDecimal BaseChargeOP;
	private BigDecimal BaseChargeUsuallyS;
	private BigDecimal BaseChargeSPS;
	private BigDecimal BaseChargeSatSPS;
	private BigDecimal BaseChargeOPS;
	private BigDecimal TimeCharge;
	private BigDecimal TimeChargeSP;
	private BigDecimal TimeChargeSatSP;
	private BigDecimal TimeChargeOP;
	private BigDecimal TimeChargeS;
	private BigDecimal TimeChargeSPS;
	private BigDecimal TimeChargeSatSPS;
	private BigDecimal TimeChargeOPS;
	private BigDecimal Over2KPrice;
	
	public ElectricityPriceModel() {
		this.LampStep1 = 0;
		this.LampStep2 = 0;
		this.LampStep3 = 0;
		this.LampStep4 = 0;
		this.LampStep5 = 0;
		this.LampBStep1 = 0;
		this.LampBStep2 = 0;
		this.LampBStep3 = 0;
	}
	
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getMonth() {
		return Month;
	}
	public void setMonth(String month) {
		Month = month;
	}
	public int getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(int ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public BigDecimal getLampPrice1() {
		return LampPrice1;
	}
	public void setLampPrice1(BigDecimal lampPrice1) {
		LampPrice1 = lampPrice1;
	}
	public BigDecimal getLampPrice2() {
		return LampPrice2;
	}
	public void setLampPrice2(BigDecimal lampPrice2) {
		LampPrice2 = lampPrice2;
	}
	public BigDecimal getLampPrice3() {
		return LampPrice3;
	}
	public void setLampPrice3(BigDecimal lampPrice3) {
		LampPrice3 = lampPrice3;
	}
	public BigDecimal getLampPrice4() {
		return LampPrice4;
	}
	public void setLampPrice4(BigDecimal lampPrice4) {
		LampPrice4 = lampPrice4;
	}
	public BigDecimal getLampPrice5() {
		return LampPrice5;
	}
	public void setLampPrice5(BigDecimal lampPrice5) {
		LampPrice5 = lampPrice5;
	}
	public BigDecimal getLampPrice6() {
		return LampPrice6;
	}
	public void setLampPrice6(BigDecimal lampPrice6) {
		LampPrice6 = lampPrice6;
	}
	public BigDecimal getLampPrice1S() {
		return LampPrice1S;
	}
	public void setLampPrice1S(BigDecimal lampPrice1S) {
		LampPrice1S = lampPrice1S;
	}
	public BigDecimal getLampPrice2S() {
		return LampPrice2S;
	}
	public void setLampPrice2S(BigDecimal lampPrice2S) {
		LampPrice2S = lampPrice2S;
	}
	public BigDecimal getLampPrice3S() {
		return LampPrice3S;
	}
	public void setLampPrice3S(BigDecimal lampPrice3S) {
		LampPrice3S = lampPrice3S;
	}
	public BigDecimal getLampPrice4S() {
		return LampPrice4S;
	}
	public void setLampPrice4S(BigDecimal lampPrice4S) {
		LampPrice4S = lampPrice4S;
	}
	public BigDecimal getLampPrice5S() {
		return LampPrice5S;
	}
	public void setLampPrice5S(BigDecimal lampPrice5S) {
		LampPrice5S = lampPrice5S;
	}
	public BigDecimal getLampPrice6S() {
		return LampPrice6S;
	}
	public void setLampPrice6S(BigDecimal lampPrice6S) {
		LampPrice6S = lampPrice6S;
	}
	public Integer getLampStep1() {
		return LampStep1;
	}
	public void setLampStep1(Integer lampStep1) {
		LampStep1 = lampStep1;
	}
	public Integer getLampStep2() {
		return LampStep2;
	}
	public void setLampStep2(Integer lampStep2) {
		LampStep2 = lampStep2;
	}
	public Integer getLampStep3() {
		return LampStep3;
	}
	public void setLampStep3(Integer lampStep3) {
		LampStep3 = lampStep3;
	}
	public Integer getLampStep4() {
		return LampStep4;
	}
	public void setLampStep4(Integer lampStep4) {
		LampStep4 = lampStep4;
	}
	public Integer getLampStep5() {
		return LampStep5;
	}
	public void setLampStep5(Integer lampStep5) {
		LampStep5 = lampStep5;
	}
	public BigDecimal getLampBPrice1() {
		return LampBPrice1;
	}
	public void setLampBPrice1(BigDecimal lampBPrice1) {
		LampBPrice1 = lampBPrice1;
	}
	public BigDecimal getLampBPrice2() {
		return LampBPrice2;
	}
	public void setLampBPrice2(BigDecimal lampBPrice2) {
		LampBPrice2 = lampBPrice2;
	}
	public BigDecimal getLampBPrice3() {
		return LampBPrice3;
	}
	public void setLampBPrice3(BigDecimal lampBPrice3) {
		LampBPrice3 = lampBPrice3;
	}
	public BigDecimal getLampBPrice4() {
		return LampBPrice4;
	}
	public void setLampBPrice4(BigDecimal lampBPrice4) {
		LampBPrice4 = lampBPrice4;
	}
	public BigDecimal getLampBPrice1S() {
		return LampBPrice1S;
	}
	public void setLampBPrice1S(BigDecimal lampBPrice1S) {
		LampBPrice1S = lampBPrice1S;
	}
	public BigDecimal getLampBPrice2S() {
		return LampBPrice2S;
	}
	public void setLampBPrice2S(BigDecimal lampBPrice2S) {
		LampBPrice2S = lampBPrice2S;
	}
	public BigDecimal getLampBPrice3S() {
		return LampBPrice3S;
	}
	public void setLampBPrice3S(BigDecimal lampBPrice3S) {
		LampBPrice3S = lampBPrice3S;
	}
	public BigDecimal getLampBPrice4S() {
		return LampBPrice4S;
	}
	public void setLampBPrice4S(BigDecimal lampBPrice4S) {
		LampBPrice4S = lampBPrice4S;
	}
	public Integer getLampBStep1() {
		return LampBStep1;
	}
	public void setLampBStep1(Integer lampBStep1) {
		LampBStep1 = lampBStep1;
	}
	public Integer getLampBStep2() {
		return LampBStep2;
	}
	public void setLampBStep2(Integer lampBStep2) {
		LampBStep2 = lampBStep2;
	}
	public Integer getLampBStep3() {
		return LampBStep3;
	}
	public void setLampBStep3(Integer lampBStep3) {
		LampBStep3 = lampBStep3;
	}
	public BigDecimal getBaseCharge1phase() {
		return BaseCharge1phase;
	}
	public void setBaseCharge1phase(BigDecimal baseCharge1phase) {
		BaseCharge1phase = baseCharge1phase;
	}
	public BigDecimal getBaseCharge3phase() {
		return BaseCharge3phase;
	}
	public void setBaseCharge3phase(BigDecimal baseCharge3phase) {
		BaseCharge3phase = baseCharge3phase;
	}
	public BigDecimal getBaseChargeUsually() {
		return BaseChargeUsually;
	}
	public void setBaseChargeUsually(BigDecimal baseChargeUsually) {
		BaseChargeUsually = baseChargeUsually;
	}
	public BigDecimal getBaseChargeSP() {
		return BaseChargeSP;
	}
	public void setBaseChargeSP(BigDecimal baseChargeSP) {
		BaseChargeSP = baseChargeSP;
	}
	public BigDecimal getBaseChargeSatSP() {
		return BaseChargeSatSP;
	}
	public void setBaseChargeSatSP(BigDecimal baseChargeSatSP) {
		BaseChargeSatSP = baseChargeSatSP;
	}
	public BigDecimal getBaseChargeOP() {
		return BaseChargeOP;
	}
	public void setBaseChargeOP(BigDecimal baseChargeOP) {
		BaseChargeOP = baseChargeOP;
	}
	public BigDecimal getBaseChargeUsuallyS() {
		return BaseChargeUsuallyS;
	}
	public void setBaseChargeUsuallyS(BigDecimal baseChargeUsuallyS) {
		BaseChargeUsuallyS = baseChargeUsuallyS;
	}
	public BigDecimal getBaseChargeSPS() {
		return BaseChargeSPS;
	}
	public void setBaseChargeSPS(BigDecimal baseChargeSPS) {
		BaseChargeSPS = baseChargeSPS;
	}
	public BigDecimal getBaseChargeSatSPS() {
		return BaseChargeSatSPS;
	}
	public void setBaseChargeSatSPS(BigDecimal baseChargeSatSPS) {
		BaseChargeSatSPS = baseChargeSatSPS;
	}
	public BigDecimal getBaseChargeOPS() {
		return BaseChargeOPS;
	}
	public void setBaseChargeOPS(BigDecimal baseChargeOPS) {
		BaseChargeOPS = baseChargeOPS;
	}
	public BigDecimal getTimeCharge() {
		return TimeCharge;
	}
	public void setTimeCharge(BigDecimal timeCharge) {
		TimeCharge = timeCharge;
	}
	public BigDecimal getTimeChargeSP() {
		return TimeChargeSP;
	}
	public void setTimeChargeSP(BigDecimal timeChargeSP) {
		TimeChargeSP = timeChargeSP;
	}
	public BigDecimal getTimeChargeSatSP() {
		return TimeChargeSatSP;
	}
	public void setTimeChargeSatSP(BigDecimal timeChargeSatSP) {
		TimeChargeSatSP = timeChargeSatSP;
	}
	public BigDecimal getTimeChargeOP() {
		return TimeChargeOP;
	}
	public void setTimeChargeOP(BigDecimal timeChargeOP) {
		TimeChargeOP = timeChargeOP;
	}
	public BigDecimal getTimeChargeS() {
		return TimeChargeS;
	}
	public void setTimeChargeS(BigDecimal timeChargeS) {
		TimeChargeS = timeChargeS;
	}
	public BigDecimal getTimeChargeSPS() {
		return TimeChargeSPS;
	}
	public void setTimeChargeSPS(BigDecimal timeChargeSPS) {
		TimeChargeSPS = timeChargeSPS;
	}
	public BigDecimal getTimeChargeSatSPS() {
		return TimeChargeSatSPS;
	}
	public void setTimeChargeSatSPS(BigDecimal timeChargeSatSPS) {
		TimeChargeSatSPS = timeChargeSatSPS;
	}
	public BigDecimal getTimeChargeOPS() {
		return TimeChargeOPS;
	}
	public void setTimeChargeOPS(BigDecimal timeChargeOPS) {
		TimeChargeOPS = timeChargeOPS;
	}
	public BigDecimal getOver2KPrice() {
		return Over2KPrice;
	}
	public void setOver2KPrice(BigDecimal over2kPrice) {
		Over2KPrice = over2kPrice;
	}
}
