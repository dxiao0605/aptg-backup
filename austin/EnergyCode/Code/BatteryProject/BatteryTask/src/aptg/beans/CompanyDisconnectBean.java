package aptg.beans;

import java.io.Serializable;

public class CompanyDisconnectBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbID;
	private long disconnect;
	private int companyCode;
	private int impType;
	private String startTime;
	private String endTime;
	private int defaultGroup;
	
	public String getNbID() {
		return nbID;
	}
	public void setNbID(String nbID) {
		this.nbID = nbID;
	}
	public long getDisconnect() {
		return disconnect;
	}
	public void setDisconnect(long disconnect) {
		this.disconnect = disconnect;
	}
	public int getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(int companyCode) {
		this.companyCode = companyCode;
	}
	public int getImpType() {
		return impType;
	}
	public void setImpType(int impType) {
		this.impType = impType;
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
	public int getDefaultGroup() {
		return defaultGroup;
	}
	public void setDefaultGroup(int defaultGroup) {
		this.defaultGroup = defaultGroup;
	}
}
