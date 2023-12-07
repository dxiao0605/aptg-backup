package aptg.battery.bean;

import java.math.BigDecimal;
import java.util.List;

public class BatteryBean {
	
	private int Seq;
	private String CompanyCode;
	private String Company;
	private String Country;
	private String Area;
	private String GroupID;
	private String GroupName;
	private String GroupLabel;
	private String Address;
	private String GroupInternalID;
	private String BatteryGroupID;
	private String BattInternalID;
	private String NBID;
	private String BatteryID;
	private String InstallDate;
	private String BatteryType;
	private String RecTime;	
	private List<String> IR;
	private List<String> Vol;
	private BigDecimal Temperature;						
	private int StatusCode;
	private String StatusDesc;
	
	public int getSeq() {
		return Seq;
	}
	public void setSeq(int seq) {
		Seq = seq;
	}
	public String getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
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
	public String getGroupLabel() {
		return GroupLabel;
	}
	public void setGroupLabel(String groupLabel) {
		GroupLabel = groupLabel;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(String groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public String getBatteryGroupID() {
		return BatteryGroupID;
	}
	public void setBatteryGroupID(String batteryGroupID) {
		BatteryGroupID = batteryGroupID;
	}
	public String getBattInternalID() {
		return BattInternalID;
	}
	public void setBattInternalID(String battInternalID) {
		BattInternalID = battInternalID;
	}
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public String getBatteryID() {
		return BatteryID;
	}
	public void setBatteryID(String batteryID) {
		BatteryID = batteryID;
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
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public String getStatusDesc() {
		return StatusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		StatusDesc = statusDesc;
	}
}
