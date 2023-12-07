package aptg.battery.bean;

import java.math.BigDecimal;
import java.util.List;

public class BatteryHistoryBean {
	private String BatteryGroupID;
	private String Country;
	private String Area;
	private String GroupID;
	private String GroupName;
	private String InstallDate;
	private String BatteryType;
	private String Address;	
	private List<RecordBean> Record;
	private int IMPType;
	private BigDecimal Lng;
	private BigDecimal Lat;
	
	public String getBatteryGroupID() {
		return BatteryGroupID;
	}
	public void setBatteryGroupID(String batteryGroupID) {
		BatteryGroupID = batteryGroupID;
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
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public List<RecordBean> getRecord() {
		return Record;
	}
	public void setRecord(List<RecordBean> record) {
		Record = record;
	}
	public int getIMPType() {
		return IMPType;
	}
	public void setIMPType(int iMPType) {
		IMPType = iMPType;
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
