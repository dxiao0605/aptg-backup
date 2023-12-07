package aptg.model;

import java.io.Serializable;

public class GroupDailyStatusModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int groupInternalID;
	private String recDate;
	private int timeZone;
	private int status;
	
	public int getGroupInternalID() {
		return groupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		this.groupInternalID = groupInternalID;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public int getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
