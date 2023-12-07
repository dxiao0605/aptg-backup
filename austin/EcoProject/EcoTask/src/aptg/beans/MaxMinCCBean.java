package aptg.beans;

import java.io.Serializable;

public class MaxMinCCBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer maxCC;
	private Integer minCC;;
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
