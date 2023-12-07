package aptg.models;

import java.io.Serializable;

public class PowerAccountHistoryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String ApplyDate;
	private String BankCode;
	private Integer RatePlanCode;
	private Integer UsuallyCC;
	private Integer SPCC;
	private Integer SatSPCC;
	private Integer OPCC;
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getApplyDate() {
		return ApplyDate;
	}
	public void setApplyDate(String applyDate) {
		ApplyDate = applyDate;
	}
	public Integer getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(Integer ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public Integer getUsuallyCC() {
		return UsuallyCC;
	}
	public void setUsuallyCC(Integer usuallyCC) {
		UsuallyCC = usuallyCC;
	}
	public Integer getSPCC() {
		return SPCC;
	}
	public void setSPCC(Integer sPCC) {
		SPCC = sPCC;
	}
	public Integer getSatSPCC() {
		return SatSPCC;
	}
	public void setSatSPCC(Integer satSPCC) {
		SatSPCC = satSPCC;
	}
	public Integer getOPCC() {
		return OPCC;
	}
	public void setOPCC(Integer oPCC) {
		OPCC = oPCC;
	}
}
