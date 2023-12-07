package aptg.models;

import java.io.Serializable;

public class MeterEventTxModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer seqno;
	private String eco5Account;		// ECO5帳號
	private String deviceID;
	private String openTime;
	private String closeTime;
	private int eventCode;
	private int priority;
	private int eventStatus;
	private Integer openSeqno;
	private Integer closeSeqno;
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getEco5Account() {
		return eco5Account;
	}
	public void setEco5Account(String eco5Account) {
		this.eco5Account = eco5Account;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public int getEventCode() {
		return eventCode;
	}
	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}
	public Integer getOpenSeqno() {
		return openSeqno;
	}
	public void setOpenSeqno(Integer openSeqno) {
		this.openSeqno = openSeqno;
	}
	public Integer getCloseSeqno() {
		return closeSeqno;
	}
	public void setCloseSeqno(Integer closeSeqno) {
		this.closeSeqno = closeSeqno;
	}
}
