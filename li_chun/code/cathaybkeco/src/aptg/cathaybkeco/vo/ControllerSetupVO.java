package aptg.cathaybkeco.vo;

public class ControllerSetupVO {
	private String eco5Account;
	private String eco5Password;
	private String bankCode;
	private String installPosition;
	private String enabled;
	private String ip;
	private String userName;
	
	private boolean error;
	private String code;
	private String description;
	
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
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	
}
