package aptg.models;

import java.io.Serializable;

public class PowerAccountModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String BankCode;
	private String CustomerName;
	private String AccountDesc;
	private Integer PATypeCode;
	private int PowerPhase;
	private int ModifyStatus;
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getAccountDesc() {
		return AccountDesc;
	}
	public void setAccountDesc(String accountDesc) {
		AccountDesc = accountDesc;
	}
	public Integer getPATypeCode() {
		return PATypeCode;
	}
	public void setPATypeCode(Integer pATypeCode) {
		PATypeCode = pATypeCode;
	}
	public int getPowerPhase() {
		return PowerPhase;
	}
	public void setPowerPhase(int PowerPhase) {
		this.PowerPhase = PowerPhase;
	}
	public int getModifyStatus() {
		return ModifyStatus;
	}
	public void setModifyStatus(int modifyStatus) {
		ModifyStatus = modifyStatus;
	}
}
