package aptg.models;

import java.io.Serializable;

public class ControllerSetupModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer seqno;
	private String eco5Account;		// ECO5帳號
	private String eco5Password;	// ECO5密碼
	private String bankCode;		// 分行代號
	private String installPosition;	// 安裝位置
	private int enabled;			// 啟用狀態
	private String enabledTime;		// 啟用時間
	private String expireDate;		// 到期日
	private String ip;				// IP位址
	private String createTime;
	private String updateTime;
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getEco5Account() {
		return eco5Account;
	}
	public void setEco5Account(String eco5Account) {
		this.eco5Account = eco5Account;
	}
	public String getEco5Password() {
		return eco5Password;
	}
	public void setEco5Password(String eco5Password) {
		this.eco5Password = eco5Password;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getInstallPosition() {
		return installPosition;
	}
	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public String getEnabledTime() {
		return enabledTime;
	}
	public void setEnabledTime(String enabledTime) {
		this.enabledTime = enabledTime;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
