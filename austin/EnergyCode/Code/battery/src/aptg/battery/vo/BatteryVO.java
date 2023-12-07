package aptg.battery.vo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;


import aptg.battery.bean.BatteryHistoryBean;

public class BatteryVO {
	private String companyCode;
	private String country;
	private String area;
	private String groupInternalID;
	private String nbId;
	private String batteryId;
	private String startDate;
	private String endDate;
	private String groupID;
	private String groupName;
	private String batteryGroupId;
	private String batteryGroupIdArr;
	private String installDate;
	private String batteryTypeCode;
	private String userName;	
	private String companyCodeArr;
	private String installDateNull;
	private boolean error;
	private String code;
	private String description;
	private String type;
	private String commandID;
	private String category;
	private boolean isMap;
	private String countryArr;
	private String areaArr;
	private String groupIdArr;
	private String groupNameArr;
	private String nbIdArr;
	private String groupInternalIdArr;
	private JSONObject json;
	private String battInternalId;
	private String batteryTypeName;
	private boolean addBattTypeFlag;
	private BatteryHistoryBean historyBean;
	private List<String> rectimeArr;
	private LinkedHashMap<String, List<BigDecimal>> irMap;
	private LinkedHashMap<String, List<BigDecimal>> volMap;
	private LinkedHashMap<String, List<BigDecimal>> temperatureMap;
	private List<String> statusList;
	private boolean recTimeDesc;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGroupInternalID() {
		return groupInternalID;
	}
	public void setGroupInternalID(String groupInternalID) {
		this.groupInternalID = groupInternalID;
	}
	public String getNbId() {
		return nbId;
	}
	public void setNbId(String nbId) {
		this.nbId = nbId;
	}
	public String getBatteryId() {
		return batteryId;
	}
	public void setBatteryId(String batteryId) {
		this.batteryId = batteryId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getBatteryGroupId() {
		return batteryGroupId;
	}
	public void setBatteryGroupId(String batteryGroupId) {
		this.batteryGroupId = batteryGroupId;
	}
	public String getBatteryGroupIdArr() {
		return batteryGroupIdArr;
	}
	public void setBatteryGroupIdArr(String batteryGroupIdArr) {
		this.batteryGroupIdArr = batteryGroupIdArr;
	}
	public String getInstallDate() {
		return installDate;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	public String getBatteryTypeCode() {
		return batteryTypeCode;
	}
	public void setBatteryTypeCode(String batteryTypeCode) {
		this.batteryTypeCode = batteryTypeCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCompanyCodeArr() {
		return companyCodeArr;
	}
	public void setCompanyCodeArr(String companyCodeArr) {
		this.companyCodeArr = companyCodeArr;
	}
	public String getInstallDateNull() {
		return installDateNull;
	}
	public void setInstallDateNull(String installDateNull) {
		this.installDateNull = installDateNull;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommandID() {
		return commandID;
	}
	public void setCommandID(String commandID) {
		this.commandID = commandID;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isMap() {
		return isMap;
	}
	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}
	public String getCountryArr() {
		return countryArr;
	}
	public void setCountryArr(String countryArr) {
		this.countryArr = countryArr;
	}
	public String getAreaArr() {
		return areaArr;
	}
	public void setAreaArr(String areaArr) {
		this.areaArr = areaArr;
	}
	public String getGroupIdArr() {
		return groupIdArr;
	}
	public void setGroupIdArr(String groupIdArr) {
		this.groupIdArr = groupIdArr;
	}
	public String getGroupNameArr() {
		return groupNameArr;
	}
	public void setGroupNameArr(String groupNameArr) {
		this.groupNameArr = groupNameArr;
	}
	public String getNbIdArr() {
		return nbIdArr;
	}
	public void setNbIdArr(String nbIdArr) {
		this.nbIdArr = nbIdArr;
	}
	public String getGroupInternalIdArr() {
		return groupInternalIdArr;
	}
	public void setGroupInternalIdArr(String groupInternalIdArr) {
		this.groupInternalIdArr = groupInternalIdArr;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	public String getBattInternalId() {
		return battInternalId;
	}
	public void setBattInternalId(String battInternalId) {
		this.battInternalId = battInternalId;
	}
	public String getBatteryTypeName() {
		return batteryTypeName;
	}
	public void setBatteryTypeName(String batteryTypeName) {
		this.batteryTypeName = batteryTypeName;
	}
	public boolean isAddBattTypeFlag() {
		return addBattTypeFlag;
	}
	public void setAddBattTypeFlag(boolean addBattTypeFlag) {
		this.addBattTypeFlag = addBattTypeFlag;
	}
	public BatteryHistoryBean getHistoryBean() {
		return historyBean;
	}
	public void setHistoryBean(BatteryHistoryBean historyBean) {
		this.historyBean = historyBean;
	}
	public List<String> getRectimeArr() {
		return rectimeArr;
	}
	public void setRectimeArr(List<String> rectimeArr) {
		this.rectimeArr = rectimeArr;
	}
	public LinkedHashMap<String, List<BigDecimal>> getIrMap() {
		return irMap;
	}
	public void setIrMap(LinkedHashMap<String, List<BigDecimal>> irMap) {
		this.irMap = irMap;
	}
	public LinkedHashMap<String, List<BigDecimal>> getVolMap() {
		return volMap;
	}
	public void setVolMap(LinkedHashMap<String, List<BigDecimal>> volMap) {
		this.volMap = volMap;
	}
	public LinkedHashMap<String, List<BigDecimal>> getTemperatureMap() {
		return temperatureMap;
	}
	public void setTemperatureMap(LinkedHashMap<String, List<BigDecimal>> temperatureMap) {
		this.temperatureMap = temperatureMap;
	}
	public List<String> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
	public boolean isRecTimeDesc() {
		return recTimeDesc;
	}
	public void setRecTimeDesc(boolean recTimeDesc) {
		this.recTimeDesc = recTimeDesc;
	}
	
}
