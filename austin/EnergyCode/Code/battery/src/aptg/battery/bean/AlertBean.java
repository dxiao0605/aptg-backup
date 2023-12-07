package aptg.battery.bean;

import java.math.BigDecimal;
import java.util.List;

public class AlertBean {
	private int EventSeq;
	private String EventType;
	private String EventTypeCode;
	private String Company;
	private String Country;
	private String Area;
	private String GroupID;
	private String GroupName;
	private String Address;
	private String BatteryGroupID;
	private String InstallDate;
	private String BatteryType;
	private String RecTime;	
	private List<String> IR;
	private List<String> Vol;
	private BigDecimal Temperature;
	private List<BigDecimal> Status;
	private String OccurTime;
	private String CloseTime;
	private String CloseUser;
	private String CloseContent;
	private String EventStatus;
	private String EventStatusDesc;
	private String Alert1;
	private String Alert2;
	private BigDecimal Disconnect;
	private BigDecimal Temperature1;
	private BigDecimal Lng;
	private BigDecimal Lat;
	
	public int getEventSeq() {
		return EventSeq;
	}
	public void setEventSeq(int eventSeq) {
		EventSeq = eventSeq;
	}
	public String getEventType() {
		return EventType;
	}
	public void setEventType(String eventType) {
		EventType = eventType;
	}
	public String getEventTypeCode() {
		return EventTypeCode;
	}
	public void setEventTypeCode(String eventTypeCode) {
		EventTypeCode = eventTypeCode;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getGroupID() {
		return GroupID;
	}
	public void setGroupID(String groupID) {
		GroupID = groupID;
	}
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getBatteryGroupID() {
		return BatteryGroupID;
	}
	public void setBatteryGroupID(String batteryGroupID) {
		BatteryGroupID = batteryGroupID;
	}
	public String getInstallDate() {
		return InstallDate;
	}
	public void setInstallDate(String installDate) {
		InstallDate = installDate;
	}
	public String getBatteryType() {
		return BatteryType;
	}
	public void setBatteryType(String batteryType) {
		BatteryType = batteryType;
	}
	public String getRecTime() {
		return RecTime;
	}
	public void setRecTime(String recTime) {
		RecTime = recTime;
	}
	public List<String> getIR() {
		return IR;
	}
	public void setIR(List<String> iR) {
		IR = iR;
	}
	public List<String> getVol() {
		return Vol;
	}
	public void setVol(List<String> vol) {
		Vol = vol;
	}
	public BigDecimal getTemperature() {
		return Temperature;
	}
	public void setTemperature(BigDecimal temperature) {
		Temperature = temperature;
	}
	public List<BigDecimal> getStatus() {
		return Status;
	}
	public void setStatus(List<BigDecimal> status) {
		Status = status;
	}
	public String getOccurTime() {
		return OccurTime;
	}
	public void setOccurTime(String occurTime) {
		OccurTime = occurTime;
	}
	public String getCloseTime() {
		return CloseTime;
	}
	public void setCloseTime(String closeTime) {
		CloseTime = closeTime;
	}
	public String getCloseUser() {
		return CloseUser;
	}
	public void setCloseUser(String closeUser) {
		CloseUser = closeUser;
	}
	public String getCloseContent() {
		return CloseContent;
	}
	public void setCloseContent(String closeContent) {
		CloseContent = closeContent;
	}
	public String getEventStatus() {
		return EventStatus;
	}
	public void setEventStatus(String eventStatus) {
		EventStatus = eventStatus;
	}
	public String getEventStatusDesc() {
		return EventStatusDesc;
	}
	public void setEventStatusDesc(String eventStatusDesc) {
		EventStatusDesc = eventStatusDesc;
	}
	public String getAlert1() {
		return Alert1;
	}
	public void setAlert1(String alert1) {
		Alert1 = alert1;
	}
	public String getAlert2() {
		return Alert2;
	}
	public void setAlert2(String alert2) {
		Alert2 = alert2;
	}
	public BigDecimal getDisconnect() {
		return Disconnect;
	}
	public void setDisconnect(BigDecimal disconnect) {
		Disconnect = disconnect;
	}
	public BigDecimal getTemperature1() {
		return Temperature1;
	}
	public void setTemperature1(BigDecimal temperature1) {
		Temperature1 = temperature1;
	}
	public BigDecimal getLng() {
		return Lng;
	}
	public void setLng(BigDecimal lng) {
		Lng = lng;
	}
	public BigDecimal getLat() {
		return Lat;
	}
	public void setLat(BigDecimal lat) {
		Lat = lat;
	}
}
