package aptg.models;

import java.io.Serializable;

public class DeletedPowerAccountHistoryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String PowerAccount;
	private String ApplyDate;
	private int RatePlanCode;
	private int UsuallyCC;
	private int SPCC;
	private int SatSPCC;
	private int OPCC;
	private String DeletedTime;
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getApplyDate() {
		return ApplyDate;
	}
	public void setApplyDate(String applyDate) {
		ApplyDate = applyDate;
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
	public String getDeletedTime() {
		return DeletedTime;
	}
	public void setDeletedTime(String deletedTime) {
		DeletedTime = deletedTime;
	}

	
}
