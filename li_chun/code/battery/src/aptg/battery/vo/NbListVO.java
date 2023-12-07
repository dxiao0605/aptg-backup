package aptg.battery.vo;

import java.util.List;

import org.json.JSONArray;

public class NbListVO {
	private String nbId;
	private String groupInternalId;
	private String allocate;
	private String active;
	private String companyCode;
	private String startTime;
	private String endTime;
	private String userName;	
	private boolean error;
	private String code;
	private String description;
	private List<NbListVO> dataList;
	private JSONArray json;
	private String nbidArr;
	private String batteryGroupIdArr;
	private String modifyItemArr;
	private String startDate;
	private String endDate;
	private String companyCodeArr;
	private String type;
	private String start;
	private String end;
	private String modifyItem;
	private String remark;
	private List<String> nbList;
	private List<String[]> nbInfoList;
	private JSONArray delJson;
	private JSONArray activeJson;
	private String previousNBID;
	private String continuousSeqNo;
	private String defaultGroupInternalId;
	private String countryArr;
	private String areaArr;
	private String groupInternalIdArr;
	private List<NbListVO> insertList;
	private List<NbListVO> updateList;
	
	public String getNbId() {
		return nbId;
	}
	public void setNbId(String nbId) {
		this.nbId = nbId;
	}
	public String getGroupInternalId() {
		return groupInternalId;
	}
	public void setGroupInternalId(String groupInternalId) {
		this.groupInternalId = groupInternalId;
	}
	public String getAllocate() {
		return allocate;
	}
	public void setAllocate(String allocate) {
		this.allocate = allocate;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public List<NbListVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<NbListVO> dataList) {
		this.dataList = dataList;
	}
	public JSONArray getJson() {
		return json;
	}
	public void setJson(JSONArray json) {
		this.json = json;
	}
	public String getNbidArr() {
		return nbidArr;
	}
	public void setNbidArr(String nbidArr) {
		this.nbidArr = nbidArr;
	}
	public String getBatteryGroupIdArr() {
		return batteryGroupIdArr;
	}
	public void setBatteryGroupIdArr(String batteryGroupIdArr) {
		this.batteryGroupIdArr = batteryGroupIdArr;
	}
	public String getModifyItemArr() {
		return modifyItemArr;
	}
	public void setModifyItemArr(String modifyItemArr) {
		this.modifyItemArr = modifyItemArr;
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
	public String getCompanyCodeArr() {
		return companyCodeArr;
	}
	public void setCompanyCodeArr(String companyCodeArr) {
		this.companyCodeArr = companyCodeArr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getModifyItem() {
		return modifyItem;
	}
	public void setModifyItem(String modifyItem) {
		this.modifyItem = modifyItem;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<String> getNbList() {
		return nbList;
	}
	public void setNbList(List<String> nbList) {
		this.nbList = nbList;
	}
	public List<String[]> getNbInfoList() {
		return nbInfoList;
	}
	public void setNbInfoList(List<String[]> nbInfoList) {
		this.nbInfoList = nbInfoList;
	}
	public JSONArray getDelJson() {
		return delJson;
	}
	public void setDelJson(JSONArray delJson) {
		this.delJson = delJson;
	}
	public JSONArray getActiveJson() {
		return activeJson;
	}
	public void setActiveJson(JSONArray activeJson) {
		this.activeJson = activeJson;
	}
	public String getPreviousNBID() {
		return previousNBID;
	}
	public void setPreviousNBID(String previousNBID) {
		this.previousNBID = previousNBID;
	}
	public String getContinuousSeqNo() {
		return continuousSeqNo;
	}
	public void setContinuousSeqNo(String continuousSeqNo) {
		this.continuousSeqNo = continuousSeqNo;
	}
	public String getDefaultGroupInternalId() {
		return defaultGroupInternalId;
	}
	public void setDefaultGroupInternalId(String defaultGroupInternalId) {
		this.defaultGroupInternalId = defaultGroupInternalId;
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
	public String getGroupInternalIdArr() {
		return groupInternalIdArr;
	}
	public void setGroupInternalIdArr(String groupInternalIdArr) {
		this.groupInternalIdArr = groupInternalIdArr;
	}
	public List<NbListVO> getInsertList() {
		return insertList;
	}
	public void setInsertList(List<NbListVO> insertList) {
		this.insertList = insertList;
	}
	public List<NbListVO> getUpdateList() {
		return updateList;
	}
	public void setUpdateList(List<NbListVO> updateList) {
		this.updateList = updateList;
	}
	
	
}
