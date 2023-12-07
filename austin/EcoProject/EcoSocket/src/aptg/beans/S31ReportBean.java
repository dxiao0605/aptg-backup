package aptg.beans;

import java.io.Serializable;

public class S31ReportBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deviceID;
	private String linkType;
	private String deviceExtType;
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public String getDeviceExtType() {
		return deviceExtType;
	}
	public void setDeviceExtType(String deviceExtType) {
		this.deviceExtType = deviceExtType;
	}
}
