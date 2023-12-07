package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class BestCCModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String useMonth;
	private int RatePlanCode;

	private int UsuallyCC;
	private int SPCC;
	private int SatSPCC;
	private int OPCC;

	private BigDecimal BaseCharge;
	private BigDecimal OverCharge;

	private Integer OverPK;
	private Integer OverSP;
	private Integer OverSatSP;
	private Integer OverOP;
	
	private int RealPlan;
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getUseMonth() {
		return useMonth;
	}
	public void setUseMonth(String useMonth) {
		this.useMonth = useMonth;
	}
	public int getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(int ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public int getUsuallyCC() {
		return UsuallyCC;
	}
	public void setUsuallyCC(int usuallyCC) {
		UsuallyCC = usuallyCC;
	}
	public int getSPCC() {
		return SPCC;
	}
	public void setSPCC(int sPCC) {
		SPCC = sPCC;
	}
	public int getSatSPCC() {
		return SatSPCC;
	}
	public void setSatSPCC(int satSPCC) {
		SatSPCC = satSPCC;
	}
	public int getOPCC() {
		return OPCC;
	}
	public void setOPCC(int oPCC) {
		OPCC = oPCC;
	}
	public BigDecimal getBaseCharge() {
		return BaseCharge;
	}
	public void setBaseCharge(BigDecimal baseCharge) {
		BaseCharge = baseCharge;
	}
	public BigDecimal getOverCharge() {
		return OverCharge;
	}
	public void setOverCharge(BigDecimal overCharge) {
		OverCharge = overCharge;
	}
	public Integer getOverPK() {
		return OverPK;
	}
	public void setOverPK(Integer overPK) {
		OverPK = overPK;
	}
	public Integer getOverSP() {
		return OverSP;
	}
	public void setOverSP(Integer overSP) {
		OverSP = overSP;
	}
	public Integer getOverSatSP() {
		return OverSatSP;
	}
	public void setOverSatSP(Integer overSatSP) {
		OverSatSP = overSatSP;
	}
	public Integer getOverOP() {
		return OverOP;
	}
	public void setOverOP(Integer overOP) {
		OverOP = overOP;
	}
	public int getRealPlan() {
		return RealPlan;
	}
	public void setRealPlan(int realPlan) {
		RealPlan = realPlan;
	}
}
