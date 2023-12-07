package aptg.battery.vo;

import java.util.List;

import org.json.JSONArray;

public class CommandTaskVO {
	private String taskId;
	private String groupInternalId;
	private String nbId;
	private String batteryId;
	private String commandId;
	private String config;
	private String hexConfig;
	private String userName;
	
	private boolean error;
	private String code;
	private String description;
	private List<CommandTaskVO> taskList;
	private int records;
	private String category;
	
	private String companyCode;
	private String companyCodeArr;
	private String countryArr;
	private String areaArr;
	private String groupIdArr;
	private String groupNameArr;
	private String batteryGroupIdArr;
	private String batteryTypeCodeArr;
	private String commandIdArr;
	private String taskIDArr;
	private String startDate;
	private String endDate;
	private String responseArr;
	private String responseNull;
	private String type;
	private JSONArray taskIdArr;
	private String groupInternalIdArr;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getGroupInternalId() {
		return groupInternalId;
	}
	public void setGroupInternalId(String groupInternalId) {
		this.groupInternalId = groupInternalId;
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
	public String getCommandId() {
		return commandId;
	}
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getHexConfig() {
		return hexConfig;
	}
	public void setHexConfig(String hexConfig) {
		this.hexConfig = hexConfig;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public List<CommandTaskVO> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<CommandTaskVO> taskList) {
		this.taskList = taskList;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyCodeArr() {
		return companyCodeArr;
	}
	public void setCompanyCodeArr(String companyCodeArr) {
		this.companyCodeArr = companyCodeArr;
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
	public String getBatteryGroupIdArr() {
		return batteryGroupIdArr;
	}
	public void setBatteryGroupIdArr(String batteryGroupIdArr) {
		this.batteryGroupIdArr = batteryGroupIdArr;
	}
	public String getBatteryTypeCodeArr() {
		return batteryTypeCodeArr;
	}
	public void setBatteryTypeCodeArr(String batteryTypeCodeArr) {
		this.batteryTypeCodeArr = batteryTypeCodeArr;
	}
	public String getCommandIdArr() {
		return commandIdArr;
	}
	public void setCommandIdArr(String commandIdArr) {
		this.commandIdArr = commandIdArr;
	}
	public String getTaskIDArr() {
		return taskIDArr;
	}
	public void setTaskIDArr(String taskIDArr) {
		this.taskIDArr = taskIDArr;
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
	public String getResponseArr() {
		return responseArr;
	}
	public void setResponseArr(String responseArr) {
		this.responseArr = responseArr;
	}
	public String getResponseNull() {
		return responseNull;
	}
	public void setResponseNull(String responseNull) {
		this.responseNull = responseNull;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JSONArray getTaskIdArr() {
		return taskIdArr;
	}
	public void setTaskIdArr(JSONArray taskIdArr) {
		this.taskIdArr = taskIdArr;
	}
	public String getGroupInternalIdArr() {
		return groupInternalIdArr;
	}
	public void setGroupInternalIdArr(String groupInternalIdArr) {
		this.groupInternalIdArr = groupInternalIdArr;
	}
	
}
