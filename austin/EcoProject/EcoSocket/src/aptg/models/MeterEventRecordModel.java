package aptg.models;

import java.io.Serializable;

public class MeterEventRecordModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer seqno;
	private String eco5Account;		// ECO5帳號
//	private int meterSerialNr;		// 電表序號
	private String deviceID;		// 若為ECO5的事件，DeviceID 存Null
	private String eventTime;		// 事件日期時間
	private int eventCode;			// 事件類型
	private String eventDesc;		// 事件描述

	private String createTime;
	private String updateTime;
	
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
//	public int getMeterSerialNr() {
//		return meterSerialNr;
//	}
//	public void setMeterSerialNr(int meterSerialNr) {
//		this.meterSerialNr = meterSerialNr;
//	}
	public String getEventTime() {
		return eventTime;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public int getEventCode() {
		return eventCode;
	}
	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
