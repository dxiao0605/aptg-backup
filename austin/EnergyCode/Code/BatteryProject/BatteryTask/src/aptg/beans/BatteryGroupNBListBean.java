package aptg.beans;

import java.io.Serializable;

public class BatteryGroupNBListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int GroupInternalID;
	private int CompanyCode;
	private String NBID;
	private int Active;
	private String startTime;
	private String endTime;
	
	public int getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
	}
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public int getActive() {
		return Active;
	}
	public void setActive(int active) {
		Active = active;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
