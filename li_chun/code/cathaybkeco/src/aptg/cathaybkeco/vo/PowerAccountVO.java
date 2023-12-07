package aptg.cathaybkeco.vo;

import java.util.List;

public class PowerAccountVO {
	private String powerAccount;
	private String bankCode;
	private String ratePlanCode;
	private String ratePlanDesc;
	private String customerName;
	private String accountDesc;
	private String usuallyCC;
	private String spCC;
	private String satSPCC;
	private String opCC;
	private String userName;
	private String applyDate;
	private String paTypeCode;
	private String paTypeName;
	private String powerPhase;	
	private String powerPhaseDesc;
	private String paAddress;
	private String modifyStatus;
	private String modifyStatusDesc;
	private List<PowerAccountVO> voList;
	private String repriceDate;
	private String applyDateOld;
	private String powerPhaseOld;
	private String ratePlanCodeOld;
	private String usuallyCCOld;
	private String spCCOld;
	private String satSPCCOld;
	private String opCCOld;
	private String applyDateNew;
	private String powerPhaseNew;
	private String ratePlanCodeNew;
	private String usuallyCCNew;
	private String spCCNew;
	private String satSPCCNew;
	private String opCCNew;
	private boolean addFlag;
	
	private boolean error;
	private String code;
	private String description;
	private String date;
	
	private String powerAccountNew;
	private String powerAccountOld;
	
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getRatePlanCode() {
		return ratePlanCode;
	}
	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}
	public String getRatePlanDesc() {
		return ratePlanDesc;
	}
	public void setRatePlanDesc(String ratePlanDesc) {
		this.ratePlanDesc = ratePlanDesc;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAccountDesc() {
		return accountDesc;
	}
	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}
	public String getUsuallyCC() {
		return usuallyCC;
	}
	public void setUsuallyCC(String usuallyCC) {
		this.usuallyCC = usuallyCC;
	}
	public String getSpCC() {
		return spCC;
	}
	public void setSpCC(String spCC) {
		this.spCC = spCC;
	}
	public String getSatSPCC() {
		return satSPCC;
	}
	public void setSatSPCC(String satSPCC) {
		this.satSPCC = satSPCC;
	}
	public String getOpCC() {
		return opCC;
	}
	public void setOpCC(String opCC) {
		this.opCC = opCC;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getPaTypeCode() {
		return paTypeCode;
	}
	public void setPaTypeCode(String paTypeCode) {
		this.paTypeCode = paTypeCode;
	}
	public String getPaTypeName() {
		return paTypeName;
	}
	public void setPaTypeName(String paTypeName) {
		this.paTypeName = paTypeName;
	}
	public String getPowerPhase() {
		return powerPhase;
	}
	public void setPowerPhase(String powerPhase) {
		this.powerPhase = powerPhase;
	}
	public String getPowerPhaseDesc() {
		return powerPhaseDesc;
	}
	public void setPowerPhaseDesc(String powerPhaseDesc) {
		this.powerPhaseDesc = powerPhaseDesc;
	}
	public String getPaAddress() {
		return paAddress;
	}
	public void setPaAddress(String paAddress) {
		this.paAddress = paAddress;
	}
	public String getModifyStatus() {
		return modifyStatus;
	}
	public void setModifyStatus(String modifyStatus) {
		this.modifyStatus = modifyStatus;
	}
	public String getModifyStatusDesc() {
		return modifyStatusDesc;
	}
	public void setModifyStatusDesc(String modifyStatusDesc) {
		this.modifyStatusDesc = modifyStatusDesc;
	}
	public List<PowerAccountVO> getVoList() {
		return voList;
	}
	public void setVoList(List<PowerAccountVO> voList) {
		this.voList = voList;
	}
	public boolean isAddFlag() {
		return addFlag;
	}
	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPowerAccountNew() {
		return powerAccountNew;
	}
	public void setPowerAccountNew(String powerAccountNew) {
		this.powerAccountNew = powerAccountNew;
	}
	public String getPowerAccountOld() {
		return powerAccountOld;
	}
	public void setPowerAccountOld(String powerAccountOld) {
		this.powerAccountOld = powerAccountOld;
	}	
	public String getRepriceDate() {
		return repriceDate;
	}
	public void setRepriceDate(String repriceDate) {
		this.repriceDate = repriceDate;
	}
	public String getApplyDateOld() {
		return applyDateOld;
	}
	public void setApplyDateOld(String applyDateOld) {
		this.applyDateOld = applyDateOld;
	}
	public String getPowerPhaseOld() {
		return powerPhaseOld;
	}
	public void setPowerPhaseOld(String powerPhaseOld) {
		this.powerPhaseOld = powerPhaseOld;
	}
	public String getRatePlanCodeOld() {
		return ratePlanCodeOld;
	}
	public void setRatePlanCodeOld(String ratePlanCodeOld) {
		this.ratePlanCodeOld = ratePlanCodeOld;
	}
	public String getUsuallyCCOld() {
		return usuallyCCOld;
	}
	public void setUsuallyCCOld(String usuallyCCOld) {
		this.usuallyCCOld = usuallyCCOld;
	}
	public String getSpCCOld() {
		return spCCOld;
	}
	public void setSpCCOld(String spCCOld) {
		this.spCCOld = spCCOld;
	}
	public String getSatSPCCOld() {
		return satSPCCOld;
	}
	public void setSatSPCCOld(String satSPCCOld) {
		this.satSPCCOld = satSPCCOld;
	}
	public String getOpCCOld() {
		return opCCOld;
	}
	public void setOpCCOld(String opCCOld) {
		this.opCCOld = opCCOld;
	}
	public String getApplyDateNew() {
		return applyDateNew;
	}
	public void setApplyDateNew(String applyDateNew) {
		this.applyDateNew = applyDateNew;
	}
	public String getPowerPhaseNew() {
		return powerPhaseNew;
	}
	public void setPowerPhaseNew(String powerPhaseNew) {
		this.powerPhaseNew = powerPhaseNew;
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
	public String getSpCCNew() {
		return spCCNew;
	}
	public void setSpCCNew(String spCCNew) {
		this.spCCNew = spCCNew;
	}
	public String getSatSPCCNew() {
		return satSPCCNew;
	}
	public void setSatSPCCNew(String satSPCCNew) {
		this.satSPCCNew = satSPCCNew;
	}
	public String getOpCCNew() {
		return opCCNew;
	}
	public void setOpCCNew(String opCCNew) {
		this.opCCNew = opCCNew;
	}	
	
}
