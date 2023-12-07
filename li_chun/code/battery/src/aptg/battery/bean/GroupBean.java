package aptg.battery.bean;

import java.math.BigDecimal;
import java.util.List;

public class GroupBean {
	
	private int Seq;
	private String Company;
	private int CompanyCode;
	private String Country;
	private String Area;
	private String GroupID;
	private String GroupName;
	private String GroupLabel;
	private List<String> NBID;
	private String GroupInternalID;
	private List<String> BatteryGroupID;
	private int BatteryCount;
	private String RecTime;	
	private String MaxIMP;
	private String MinIMP;
	private BigDecimal MaxIMPValue;
	private BigDecimal MinIMPValue;
	private BigDecimal MaxVol;
	private BigDecimal MinVol;
	private BigDecimal MaxTemperature;
	private BigDecimal MinTemperature;						
	private int StatusCode;
	private String StatusDesc;
	
	public int getSeq() {
		return Seq;
	}
	public void setSeq(int seq) {
		Seq = seq;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
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
	public List<String> getNBID() {
		return NBID;
	}
	public void setNBID(List<String> nBID) {
		NBID = nBID;
	}
	public String getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(String groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public List<String> getBatteryGroupID() {
		return BatteryGroupID;
	}
	public void setBatteryGroupID(List<String> batteryGroupID) {
		BatteryGroupID = batteryGroupID;
	}
	public int getBatteryCount() {
		return BatteryCount;
	}
	public void setBatteryCount(int batteryCount) {
		BatteryCount = batteryCount;
	}
	public String getRecTime() {
		return RecTime;
	}
	public void setRecTime(String recTime) {
		RecTime = recTime;
	}
	public String getMaxIMP() {
		return MaxIMP;
	}
	public void setMaxIMP(String maxIMP) {
		MaxIMP = maxIMP;
	}
	public String getMinIMP() {
		return MinIMP;
	}
	public void setMinIMP(String minIMP) {
		MinIMP = minIMP;
	}
	public BigDecimal getMaxIMPValue() {
		return MaxIMPValue;
	}
	public void setMaxIMPValue(BigDecimal maxIMPValue) {
		MaxIMPValue = maxIMPValue;
	}
	public BigDecimal getMinIMPValue() {
		return MinIMPValue;
	}
	public void setMinIMPValue(BigDecimal minIMPValue) {
		MinIMPValue = minIMPValue;
	}
	public BigDecimal getMaxVol() {
		return MaxVol;
	}
	public void setMaxVol(BigDecimal maxVol) {
		MaxVol = maxVol;
	}
	public BigDecimal getMinVol() {
		return MinVol;
	}
	public void setMinVol(BigDecimal minVol) {
		MinVol = minVol;
	}
	public BigDecimal getMaxTemperature() {
		return MaxTemperature;
	}
	public void setMaxTemperature(BigDecimal maxTemperature) {
		MaxTemperature = maxTemperature;
	}
	public BigDecimal getMinTemperature() {
		return MinTemperature;
	}
	public void setMinTemperature(BigDecimal minTemperature) {
		MinTemperature = minTemperature;
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
