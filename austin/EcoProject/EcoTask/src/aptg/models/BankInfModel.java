package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankInfModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String BankCode;
	private String BankName;
	private Integer PostCodeNo;
	private String BankAddress;
	private String PhoneNr;
	private BigDecimal Area;
	private Integer People;
	
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	public Integer getPostCodeNo() {
		return PostCodeNo;
	}
	public void setPostCodeNo(Integer postCodeNo) {
		PostCodeNo = postCodeNo;
	}
	public String getBankAddress() {
		return BankAddress;
	}
	public void setBankAddress(String bankAddress) {
		BankAddress = bankAddress;
	}
	public String getPhoneNr() {
		return PhoneNr;
	}
	public void setPhoneNr(String phoneNr) {
		PhoneNr = phoneNr;
	}
	public BigDecimal getArea() {
		return Area;
	}
	public void setArea(BigDecimal area) {
		Area = area;
	}
	public Integer getPeople() {
		return People;
	}
	public void setPeople(Integer people) {
		People = people;
	}
}
