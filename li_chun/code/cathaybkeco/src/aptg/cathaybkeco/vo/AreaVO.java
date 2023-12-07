package aptg.cathaybkeco.vo;

import java.util.List;

public class AreaVO {
	private String areaCodeNo;
	private String areaCode;
	private String areaName;
	private String enabled;
	private List<String> accessBanksList;
	private String userName;

	private boolean error;
	private String code;
	private String description;
	
	
	public String getAreaCodeNo() {
		return areaCodeNo;
	}
	public void setAreaCodeNo(String areaCodeNo) {
		this.areaCodeNo = areaCodeNo;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public List<String> getAccessBanksList() {
		return accessBanksList;
	}
	public void setAccessBanksList(List<String> accessBanksList) {
		this.accessBanksList = accessBanksList;
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
