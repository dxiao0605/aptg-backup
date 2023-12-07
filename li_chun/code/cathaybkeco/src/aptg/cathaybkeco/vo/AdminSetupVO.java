package aptg.cathaybkeco.vo;

import java.util.List;

import org.json.JSONObject;

import aptg.cathaybkeco.bean.AccountTempBean;

public class AdminSetupVO {
//	private String systemId;
//	private String token;
//	private String role;
//	private String addToken;
	
	private String account;
	private String password;
	private String accountName;
	private String bankCode;
	private String rankCode;
	private String enabled;
	private String email;
	private String suspend;
	private String verifyCode;

	private boolean error;
	private String code;
	private String description;
	private String city;
	private String postCodeNo;
	private String rankCodeArr;
	private String userRank;
	private String areaCodeNo;
	private String userName;
	private List<AccountTempBean> AccountTempList;
	private JSONObject rspJson;	
	private String uuid;
	private String process;
	private String expireDate;
	private String lockTime;
	private boolean addHistory;
	private String rankCodeBE;//大於等於
	private String rankCodeBT;//大於
	private String bankCodeArr;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getRankCode() {
		return rankCode;
	}
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSuspend() {
		return suspend;
	}
	public void setSuspend(String suspend) {
		this.suspend = suspend;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCodeNo() {
		return postCodeNo;
	}
	public void setPostCodeNo(String postCodeNo) {
		this.postCodeNo = postCodeNo;
	}
	public String getRankCodeArr() {
		return rankCodeArr;
	}
	public void setRankCodeArr(String rankCodeArr) {
		this.rankCodeArr = rankCodeArr;
	}
	public String getUserRank() {
		return userRank;
	}
	public void setUserRank(String userRank) {
		this.userRank = userRank;
	}
	public String getAreaCodeNo() {
		return areaCodeNo;
	}
	public void setAreaCodeNo(String areaCodeNo) {
		this.areaCodeNo = areaCodeNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<AccountTempBean> getAccountTempList() {
		return AccountTempList;
	}
	public void setAccountTempList(List<AccountTempBean> accountTempList) {
		AccountTempList = accountTempList;
	}
	public JSONObject getRspJson() {
		return rspJson;
	}
	public void setRspJson(JSONObject rspJson) {
		this.rspJson = rspJson;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getLockTime() {
		return lockTime;
	}
	public void setLockTime(String lockTime) {
		this.lockTime = lockTime;
	}
	public boolean isAddHistory() {
		return addHistory;
	}
	public void setAddHistory(boolean addHistory) {
		this.addHistory = addHistory;
	}
	public String getRankCodeBE() {
		return rankCodeBE;
	}
	public void setRankCodeBE(String rankCodeBE) {
		this.rankCodeBE = rankCodeBE;
	}
	public String getRankCodeBT() {
		return rankCodeBT;
	}
	public void setRankCodeBT(String rankCodeBT) {
		this.rankCodeBT = rankCodeBT;
	}
	public String getBankCodeArr() {
		return bankCodeArr;
	}
	public void setBankCodeArr(String bankCodeArr) {
		this.bankCodeArr = bankCodeArr;
	}
	
}
