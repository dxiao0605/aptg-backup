package aptg.cathaybkeco.vo;

public class MeterSetupVO {
	private String deviceId;
	private String eco5Account;
	private String meterSerialNr;
	private String meterID;
	private String meterName;
	private String meterTypeCode;
	private String installPosition; 
	private String treeChartID; 
	private String enabled;
	private String wiringCode; 
	private String usageCode;
	private String powerAccount;
	private String powerFactorEnabled; 
	private String areaName; 
	private String area;
	private String people;
	private String dfEnabled; 
	private String dfCode;
	private String dfCycle;
	private String dfUpLimit;
	private String dfLoLimit;
	private String curAlertEnabled; 
	private String curUpLimit;
	private String curLoLimit;
	private String volAlertEnabled; 
	private String volAlertType; 
	private String volUpLimit;
	private String volLoLimit;
	private String ecAlertEnabled; 
	private String ecUpLimit; 
	private String userName;
	private String ratePlanCode;
	
	private String usuallyCC;
	private String spcc;
	private String satSPCC;
	private String opcc;

	private String ratedPower;
	private String ratedVol;
	private String ratedCur;
	private String equipDesc;
	
	private boolean error;
	private String code;
	private String description;
	private String bankCode;
	private String priority;
	private String usageDesc;
	
	private String startDate;
	private String endDate;
	private String date;
	private String cityArr;
	private String postCodeNoArr;
	private String bankCodeArr;
	private String usageCodeArr;
	private String repriceDate;
	private String powerPhaseNew;
	private String applyDateNew;
	private String ratePlanCodeNew;
	private String usuallyCCNew;
	private String spccNew;
	private String satspccNew;
	private String opccNew;
	private String repriceStatus;
	private String accountDesc;
	private String recType;//0:日, 1:月
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getEco5Account() {
		return eco5Account;
	}
	public void setEco5Account(String eco5Account) {
		this.eco5Account = eco5Account;
	}
	
	public String getMeterSerialNr() {
		return meterSerialNr;
	}
	public void setMeterSerialNr(String meterSerialNr) {
		this.meterSerialNr = meterSerialNr;
	}
	public String getMeterID() {
		return meterID;
	}
	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public String getMeterTypeCode() {
		return meterTypeCode;
	}
	public void setMeterTypeCode(String meterTypeCode) {
		this.meterTypeCode = meterTypeCode;
	}
	public String getInstallPosition() {
		return installPosition;
	}
	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}
	public String getTreeChartID() {
		return treeChartID;
	}
	public void setTreeChartID(String treeChartID) {
		this.treeChartID = treeChartID;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getWiringCode() {
		return wiringCode;
	}
	public void setWiringCode(String wiringCode) {
		this.wiringCode = wiringCode;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getPowerFactorEnabled() {
		return powerFactorEnabled;
	}
	public void setPowerFactorEnabled(String powerFactorEnabled) {
		this.powerFactorEnabled = powerFactorEnabled;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getDfEnabled() {
		return dfEnabled;
	}
	public void setDfEnabled(String dfEnabled) {
		this.dfEnabled = dfEnabled;
	}
	public String getDfCode() {
		return dfCode;
	}
	public void setDfCode(String dfCode) {
		this.dfCode = dfCode;
	}
	public String getDfCycle() {
		return dfCycle;
	}
	public void setDfCycle(String dfCycle) {
		this.dfCycle = dfCycle;
	}
	public String getDfUpLimit() {
		return dfUpLimit;
	}
	public void setDfUpLimit(String dfUpLimit) {
		this.dfUpLimit = dfUpLimit;
	}
	public String getDfLoLimit() {
		return dfLoLimit;
	}
	public void setDfLoLimit(String dfLoLimit) {
		this.dfLoLimit = dfLoLimit;
	}
	public String getCurAlertEnabled() {
		return curAlertEnabled;
	}
	public void setCurAlertEnabled(String curAlertEnabled) {
		this.curAlertEnabled = curAlertEnabled;
	}
	public String getCurUpLimit() {
		return curUpLimit;
	}
	public void setCurUpLimit(String curUpLimit) {
		this.curUpLimit = curUpLimit;
	}
	public String getCurLoLimit() {
		return curLoLimit;
	}
	public void setCurLoLimit(String curLoLimit) {
		this.curLoLimit = curLoLimit;
	}
	public String getVolAlertEnabled() {
		return volAlertEnabled;
	}
	public void setVolAlertEnabled(String volAlertEnabled) {
		this.volAlertEnabled = volAlertEnabled;
	}
	public String getVolAlertType() {
		return volAlertType;
	}
	public void setVolAlertType(String volAlertType) {
		this.volAlertType = volAlertType;
	}
	public String getVolUpLimit() {
		return volUpLimit;
	}
	public void setVolUpLimit(String volUpLimit) {
		this.volUpLimit = volUpLimit;
	}
	public String getVolLoLimit() {
		return volLoLimit;
	}
	public void setVolLoLimit(String volLoLimit) {
		this.volLoLimit = volLoLimit;
	}
	public String getEcAlertEnabled() {
		return ecAlertEnabled;
	}
	public void setEcAlertEnabled(String ecAlertEnabled) {
		this.ecAlertEnabled = ecAlertEnabled;
	}
	public String getEcUpLimit() {
		return ecUpLimit;
	}
	public void setEcUpLimit(String ecUpLimit) {
		this.ecUpLimit = ecUpLimit;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRatePlanCode() {
		return ratePlanCode;
	}
	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}
	public String getUsuallyCC() {
		return usuallyCC;
	}
	public void setUsuallyCC(String usuallyCC) {
		this.usuallyCC = usuallyCC;
	}
	public String getSpcc() {
		return spcc;
	}
	public void setSpcc(String spcc) {
		this.spcc = spcc;
	}
	public String getSatSPCC() {
		return satSPCC;
	}
	public void setSatSPCC(String satSPCC) {
		this.satSPCC = satSPCC;
	}
	public String getOpcc() {
		return opcc;
	}
	public void setOpcc(String opcc) {
		this.opcc = opcc;
	}
	public String getRatedPower() {
		return ratedPower;
	}
	public void setRatedPower(String ratedPower) {
		this.ratedPower = ratedPower;
	}
	public String getRatedVol() {
		return ratedVol;
	}
	public void setRatedVol(String ratedVol) {
		this.ratedVol = ratedVol;
	}
	public String getRatedCur() {
		return ratedCur;
	}
	public void setRatedCur(String ratedCur) {
		this.ratedCur = ratedCur;
	}
	public String getEquipDesc() {
		return equipDesc;
	}
	public void setEquipDesc(String equipDesc) {
		this.equipDesc = equipDesc;
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
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getUsageDesc() {
		return usageDesc;
	}
	public void setUsageDesc(String usageDesc) {
		this.usageDesc = usageDesc;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCityArr() {
		return cityArr;
	}
	public void setCityArr(String cityArr) {
		this.cityArr = cityArr;
	}
	public String getPostCodeNoArr() {
		return postCodeNoArr;
	}
	public void setPostCodeNoArr(String postCodeNoArr) {
		this.postCodeNoArr = postCodeNoArr;
	}
	public String getBankCodeArr() {
		return bankCodeArr;
	}
	public void setBankCodeArr(String bankCodeArr) {
		this.bankCodeArr = bankCodeArr;
	}
	public String getUsageCodeArr() {
		return usageCodeArr;
	}
	public void setUsageCodeArr(String usageCodeArr) {
		this.usageCodeArr = usageCodeArr;
	}
	public String getRepriceDate() {
		return repriceDate;
	}
	public void setRepriceDate(String repriceDate) {
		this.repriceDate = repriceDate;
	}
	public String getPowerPhaseNew() {
		return powerPhaseNew;
	}
	public void setPowerPhaseNew(String powerPhaseNew) {
		this.powerPhaseNew = powerPhaseNew;
	}
	public String getApplyDateNew() {
		return applyDateNew;
	}
	public void setApplyDateNew(String applyDateNew) {
		this.applyDateNew = applyDateNew;
	}
	public String getRatePlanCodeNew() {
		return ratePlanCodeNew;
	}
	public void setRatePlanCodeNew(String ratePlanCodeNew) {
		this.ratePlanCodeNew = ratePlanCodeNew;
	}
	public String getUsuallyCCNew() {
		return usuallyCCNew;
	}
	public void setUsuallyCCNew(String usuallyCCNew) {
		this.usuallyCCNew = usuallyCCNew;
	}
	public String getSpccNew() {
		return spccNew;
	}
	public void setSpccNew(String spccNew) {
		this.spccNew = spccNew;
	}
	public String getSatspccNew() {
		return satspccNew;
	}
	public void setSatspccNew(String satspccNew) {
		this.satspccNew = satspccNew;
	}
	public String getOpccNew() {
		return opccNew;
	}
	public void setOpccNew(String opccNew) {
		this.opccNew = opccNew;
	}
	public String getRepriceStatus() {
		return repriceStatus;
	}
	public void setRepriceStatus(String repriceStatus) {
		this.repriceStatus = repriceStatus;
	}
	public String getAccountDesc() {
		return accountDesc;
	}
	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}
	public String getRecType() {
		return recType;
	}
	public void setRecType(String recType) {
		this.recType = recType;
	}
	
}
