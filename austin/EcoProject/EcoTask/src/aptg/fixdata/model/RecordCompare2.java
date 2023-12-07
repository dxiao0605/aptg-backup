package aptg.fixdata.model;

import java.io.Serializable;
import java.util.Date;

public class RecordCompare2 implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deviceID;
	private Date minRecTime;
	private Date newTime;
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public Date getMinRecTime() {
		return minRecTime;
	}
	public void setMinRecTime(Date minRecTime) {
		this.minRecTime = minRecTime;
	}
	public Date getNewTime() {
		return newTime;
	}
	public void setNewTime(Date newTime) {
		this.newTime = newTime;
	}
}
