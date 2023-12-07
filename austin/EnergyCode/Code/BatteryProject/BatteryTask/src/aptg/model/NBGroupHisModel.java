package aptg.model;

import java.io.Serializable;

public class NBGroupHisModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String NBID;
	private int GroupInternalID;
	private String Starttime;
	private String Endtime;
	private String previousNBID;
	private int continuousSeqNo;
	
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public int getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public String getStarttime() {
		return Starttime;
	}
	public void setStarttime(String starttime) {
		Starttime = starttime;
	}
	public String getEndtime() {
		return Endtime;
	}
	public void setEndtime(String endtime) {
		Endtime = endtime;
	}
	public String getPreviousNBID() {
		return previousNBID;
	}
	public void setPreviousNBID(String previousNBID) {
		this.previousNBID = previousNBID;
	}
	public int getContinuousSeqNo() {
		return continuousSeqNo;
	}
	public void setContinuousSeqNo(int continuousSeqNo) {
		this.continuousSeqNo = continuousSeqNo;
	}
	
}