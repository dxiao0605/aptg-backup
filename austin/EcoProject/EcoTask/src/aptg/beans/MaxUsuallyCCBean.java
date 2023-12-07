package aptg.beans;

import java.io.Serializable;

public class MaxUsuallyCCBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer maxCC;	// max UsuallyCC
	private Integer minCC;	// min UsuallyCC
	
	private Integer spcc;
	private Integer satSPCC;
	private Integer opcc;
	
	private String powerAccount;
	private String useMonth;
	private Integer ratePlanCode;
	
	public Integer getMaxCC() {
		return maxCC;
	}
	public void setMaxCC(Integer maxCC) {
		this.maxCC = maxCC;
	}
	public Integer getMinCC() {
		return minCC;
	}
	public void setMinCC(Integer minCC) {
		this.minCC = minCC;
	}
	public Integer getSpcc() {
		return spcc;
	}
	public void setSpcc(Integer spcc) {
		this.spcc = spcc;
	}
	public Integer getSatSPCC() {
		return satSPCC;
	}
	public void setSatSPCC(Integer satSPCC) {
		this.satSPCC = satSPCC;
	}
	public Integer getOpcc() {
		return opcc;
	}
	public void setOpcc(Integer opcc) {
		this.opcc = opcc;
	}
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getUseMonth() {
		return useMonth;
	}
	public void setUseMonth(String useMonth) {
		this.useMonth = useMonth;
	}
	public Integer getRatePlanCode() {
		return ratePlanCode;
	}
	public void setRatePlanCode(Integer ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}
}
