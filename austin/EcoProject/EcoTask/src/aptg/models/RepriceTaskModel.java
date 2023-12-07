package aptg.models;

import java.io.Serializable;

/**
 *	重新計算電費工作清單
 * 
 * @author austinchen
 *
 */
public class RepriceTaskModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int seqno;
	private String DeviceID;
	private String PowerAccount;
	private String RepriceFrom;
	private String StartDate;
	private int StatusCode;
	
	private Integer PowerPhaseOld;
	private String ApplyDateOld;
	private Integer RatePlanCodeOld;
	private Integer UsuallyCCOld;
	private Integer SPCCOld;
	private Integer SatSPCCOld;
	private Integer OPCCOld;
	
	private Integer PowerPhaseNew;
	private String ApplyDateNew;
	private Integer RatePlanCodeNew;
	private Integer UsuallyCCNew;
	private Integer SPCCNew;
	private Integer SatSPCCNew;
	private Integer OPCCNew;
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getRepriceFrom() {
		return RepriceFrom;
	}
	public void setRepriceFrom(String repriceFrom) {
		RepriceFrom = repriceFrom;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public Integer getPowerPhaseOld() {
		return PowerPhaseOld;
	}
	public void setPowerPhaseOld(Integer powerPhaseOld) {
		PowerPhaseOld = powerPhaseOld;
	}
	public String getApplyDateOld() {
		return ApplyDateOld;
	}
	public void setApplyDateOld(String applyDateOld) {
		ApplyDateOld = applyDateOld;
	}
	public Integer getRatePlanCodeOld() {
		return RatePlanCodeOld;
	}
	public void setRatePlanCodeOld(Integer ratePlanCodeOld) {
		RatePlanCodeOld = ratePlanCodeOld;
	}
	public Integer getUsuallyCCOld() {
		return UsuallyCCOld;
	}
	public void setUsuallyCCOld(Integer usuallyCCOld) {
		UsuallyCCOld = usuallyCCOld;
	}
	public Integer getSPCCOld() {
		return SPCCOld;
	}
	public void setSPCCOld(Integer sPCCOld) {
		SPCCOld = sPCCOld;
	}
	public Integer getSatSPCCOld() {
		return SatSPCCOld;
	}
	public void setSatSPCCOld(Integer satSPCCOld) {
		SatSPCCOld = satSPCCOld;
	}
	public Integer getOPCCOld() {
		return OPCCOld;
	}
	public void setOPCCOld(Integer oPCCOld) {
		OPCCOld = oPCCOld;
	}
	public Integer getPowerPhaseNew() {
		return PowerPhaseNew;
	}
	public void setPowerPhaseNew(Integer powerPhaseNew) {
		PowerPhaseNew = powerPhaseNew;
	}
	public String getApplyDateNew() {
		return ApplyDateNew;
	}
	public void setApplyDateNew(String applyDateNew) {
		ApplyDateNew = applyDateNew;
	}
	public Integer getRatePlanCodeNew() {
		return RatePlanCodeNew;
	}
	public void setRatePlanCodeNew(Integer ratePlanCodeNew) {
		RatePlanCodeNew = ratePlanCodeNew;
	}
	public Integer getUsuallyCCNew() {
		return UsuallyCCNew;
	}
	public void setUsuallyCCNew(Integer usuallyCCNew) {
		UsuallyCCNew = usuallyCCNew;
	}
	public Integer getSPCCNew() {
		return SPCCNew;
	}
	public void setSPCCNew(Integer sPCCNew) {
		SPCCNew = sPCCNew;
	}
	public Integer getSatSPCCNew() {
		return SatSPCCNew;
	}
	public void setSatSPCCNew(Integer satSPCCNew) {
		SatSPCCNew = satSPCCNew;
	}
	public Integer getOPCCNew() {
		return OPCCNew;
	}
	public void setOPCCNew(Integer oPCCNew) {
		OPCCNew = oPCCNew;
	}

}
