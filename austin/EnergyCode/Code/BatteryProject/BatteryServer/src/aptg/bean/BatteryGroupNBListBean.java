package aptg.bean;

import java.io.Serializable;

public class BatteryGroupNBListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int GroupInternalID;
	private int CompanyCode;
	private String NBID;
	private int Active;
	private int DefaultGroup;
	
	public int getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
	}
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public int getActive() {
		return Active;
	}
	public void setActive(int active) {
		Active = active;
	}
	public int getDefaultGroup() {
		return DefaultGroup;
	}
	public void setDefaultGroup(int defaultGroup) {
		DefaultGroup = defaultGroup;
	}
}
