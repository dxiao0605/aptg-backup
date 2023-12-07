package aptg.model;

import java.io.Serializable;

public class BattMaxRecTimeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbid;
	private int batteryID;
	private String maxRecTime;
	private int companyCode;
	
	public String getNbid() {
		return nbid;
	}
	public void setNbid(String nbid) {
		this.nbid = nbid;
	}
	public int getBatteryID() {
		return batteryID;
	}
	public void setBatteryID(int batteryID) {
		this.batteryID = batteryID;
	}
	public String getMaxRecTime() {
		return maxRecTime;
	}
	public void setMaxRecTime(String maxRecTime) {
		this.maxRecTime = maxRecTime;
	}
	public int getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(int companyCode) {
		this.companyCode = companyCode;
	}
}
