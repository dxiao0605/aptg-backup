package aptg.models;

import java.io.Serializable;

public class DeletedPowerAccountModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String BankCode;
	private String CustomerName;
	private String AccountDesc;
	private int PATypeCode;
	private int PowerPhase;
	private String PAAddress;
	private String DeletedTime;
	
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
	public int getPATypeCode() {
		return PATypeCode;
	}
	public void setPATypeCode(int pATypeCode) {
		PATypeCode = pATypeCode;
	}
	public int getPowerPhase() {
		return PowerPhase;
	}
	public void setPowerPhase(int powerPhase) {
		PowerPhase = powerPhase;
	}
	public String getPAAddress() {
		return PAAddress;
	}
	public void setPAAddress(String pAAddress) {
		PAAddress = pAAddress;
	}
	public String getDeletedTime() {
		return DeletedTime;
	}
	public void setDeletedTime(String deletedTime) {
		DeletedTime = deletedTime;
	}

}
