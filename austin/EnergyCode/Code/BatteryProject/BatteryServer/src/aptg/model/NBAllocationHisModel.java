package aptg.model;

import java.io.Serializable;

public class NBAllocationHisModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String NBID;
	private int CompanyCode;
	private String Starttime;
	private String Endtime;
	
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
	public String getStarttime() {
		return Starttime;
	}
	public void setStarttime(String starttime) {
		Starttime = starttime;
	}
	public String getEndtime() {
		return Endtime;
	}
	public void setEndtime(String endtime) {
		Endtime = endtime;
	}
}
