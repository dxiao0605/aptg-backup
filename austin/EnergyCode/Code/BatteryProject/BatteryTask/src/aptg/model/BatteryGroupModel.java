package aptg.model;

import java.io.Serializable;

public class BatteryGroupModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int SeqNo;
	private int CompanyCode;
//	GroupID;
//	GroupName;
//	Country;
//	Area;
//	Address;
//	Lng;
//	Lat;
//	DefaultGroup;
	
	public int getSeqNo() {
		return SeqNo;
	}
	public void setSeqNo(int seqNo) {
		SeqNo = seqNo;
	}
	
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
	}
}
