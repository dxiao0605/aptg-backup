package aptg.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class EventModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbID;
	private Integer batteryID;
	private String recordTime;
	private Integer eventType;	// 1:正常, 2:警戒, 3:需更換, 4:離線, 5:未解決, 6:已解決
	private Integer eventStatus;
	private String closeTime;
	private String closeUser;
	private String closeContent;
	private Integer impType;
	private BigDecimal alert1;
	private BigDecimal alert2;
	private Long disconnect;
	private Integer timeZone;
	
	public String getNbID() {
		return nbID;
	}
	public void setNbID(String nbID) {
		this.nbID = nbID;
	}
	public Integer getBatteryID() {
		return batteryID;
	}
	public void setBatteryID(Integer batteryID) {
		this.batteryID = batteryID;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public Integer getEventType() {
		return eventType;
	}
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	public Integer getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(Integer eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getCloseUser() {
		return closeUser;
	}
	public void setCloseUser(String closeUser) {
		this.closeUser = closeUser;
	}
	public String getCloseContent() {
		return closeContent;
	}
	public void setCloseContent(String closeContent) {
		this.closeContent = closeContent;
	}
	public Integer getImpType() {
		return impType;
	}
	public void setImpType(Integer impType) {
		this.impType = impType;
	}
	public BigDecimal getAlert1() {
		return alert1;
	}
	public void setAlert1(BigDecimal alert1) {
		this.alert1 = alert1;
	}
	public BigDecimal getAlert2() {
		return alert2;
	}
	public void setAlert2(BigDecimal alert2) {
		this.alert2 = alert2;
	}
	public Long getDisconnect() {
		return disconnect;
	}
	public void setDisconnect(Long disconnect) {
		this.disconnect = disconnect;
	}
	public Integer getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}
}
