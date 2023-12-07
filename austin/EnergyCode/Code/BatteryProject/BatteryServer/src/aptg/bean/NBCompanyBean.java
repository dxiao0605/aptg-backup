package aptg.bean;

import java.io.Serializable;

public class NBCompanyBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String CompanyName;
	private String NBID;
	private int CompanyCode;
	private Integer Active;
	
	private int GroupInternalID;
	private int DefaultGroup;
	
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
	}
	public Integer getActive() {
		return Active;
	}
	public void setActive(Integer active) {
		Active = active;
	}
	
	public int getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public int getDefaultGroup() {
		return DefaultGroup;
	}
	public void setDefaultGroup(int defaultGroup) {
		DefaultGroup = defaultGroup;
	}
}
