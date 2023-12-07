package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerMonthModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String useMonth;
	private int RatePlanCode;
	
	private int UsuallyCC;
	private int SPCC;
	private int SatSPCC;
	private int OPCC;

	private BigDecimal MDemandPK;
	private BigDecimal MDemandSP;
	private BigDecimal MDemandSatSP;
	private BigDecimal MDemandOP;

	private BigDecimal MCECPK;
	private BigDecimal MCECSP;
	private BigDecimal MCECSatSP;
	private BigDecimal MCECOP;
	private BigDecimal MCEC;

	private Integer RealPlan;
	
//	private BigDecimal TPMDemandPK;
//	private BigDecimal TPMDemandSP;
//	private BigDecimal TPMDemandSatSP;
//	private BigDecimal TPMDemandOP;
//	
//	private Integer OverPK;
//	private Integer OverSP;
//	private Integer OverSatSP;
//	private Integer OverOP;
	
	public PowerMonthModel() {
//		this.OverPK = 0;
//		this.OverSP = 0;
//		this.OverSatSP = 0;
//		this.OverOP = 0;
	}
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getUseMonth() {
		return useMonth;
	}
	public void setUseMonth(String useMonth) {
		this.useMonth = useMonth;
	}
	public int getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(int ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public int getUsuallyCC() {
		return UsuallyCC;
	}
	public void setUsuallyCC(int usuallyCC) {
		UsuallyCC = usuallyCC;
	}
	public int getSPCC() {
		return SPCC;
	}
	public void setSPCC(int sPCC) {
		SPCC = sPCC;
	}
	public int getSatSPCC() {
		return SatSPCC;
	}
	public void setSatSPCC(int satSPCC) {
		SatSPCC = satSPCC;
	}
	public int getOPCC() {
		return OPCC;
	}
	public void setOPCC(int oPCC) {
		OPCC = oPCC;
	}
	public BigDecimal getMDemandPK() {
		return MDemandPK;
	}
	public void setMDemandPK(BigDecimal mDemandPK) {
		MDemandPK = mDemandPK;
	}
	public BigDecimal getMDemandSP() {
		return MDemandSP;
	}
	public void setMDemandSP(BigDecimal mDemandSP) {
		MDemandSP = mDemandSP;
	}
	public BigDecimal getMDemandSatSP() {
		return MDemandSatSP;
	}
	public void setMDemandSatSP(BigDecimal mDemandSatSP) {
		MDemandSatSP = mDemandSatSP;
	}
	public BigDecimal getMDemandOP() {
		return MDemandOP;
	}
	public void setMDemandOP(BigDecimal mDemandOP) {
		MDemandOP = mDemandOP;
	}
	public BigDecimal getMCECPK() {
		return MCECPK;
	}
	public void setMCECPK(BigDecimal mCECPK) {
		MCECPK = mCECPK;
	}
	public BigDecimal getMCECSP() {
		return MCECSP;
	}
	public void setMCECSP(BigDecimal mCECSP) {
		MCECSP = mCECSP;
	}
	public BigDecimal getMCECSatSP() {
		return MCECSatSP;
	}
	public void setMCECSatSP(BigDecimal mCECSatSP) {
		MCECSatSP = mCECSatSP;
	}
	public BigDecimal getMCECOP() {
		return MCECOP;
	}
	public void setMCECOP(BigDecimal mCECOP) {
		MCECOP = mCECOP;
	}
	public BigDecimal getMCEC() {
		return MCEC;
	}
	public void setMCEC(BigDecimal mCEC) {
		MCEC = mCEC;
	}
//	public BigDecimal getTPMDemandPK() {
//		return TPMDemandPK;
//	}
//	public void setTPMDemandPK(BigDecimal tPMDemandPK) {
//		TPMDemandPK = tPMDemandPK;
//	}
//	public BigDecimal getTPMDemandSP() {
//		return TPMDemandSP;
//	}
//	public void setTPMDemandSP(BigDecimal tPMDemandSP) {
//		TPMDemandSP = tPMDemandSP;
//	}
//	public BigDecimal getTPMDemandSatSP() {
//		return TPMDemandSatSP;
//	}
//	public void setTPMDemandSatSP(BigDecimal tPMDemandSatSP) {
//		TPMDemandSatSP = tPMDemandSatSP;
//	}
//	public BigDecimal getTPMDemandOP() {
//		return TPMDemandOP;
//	}
//
//	public void setTPMDemandOP(BigDecimal tPMDemandOP) {
//		TPMDemandOP = tPMDemandOP;
//	}
//
//	public Integer getOverPK() {
//		return OverPK;
//	}
//	public void setOverPK(Integer overPK) {
//		OverPK = overPK;
//	}
//	public Integer getOverSP() {
//		return OverSP;
//	}
//	public void setOverSP(Integer overSP) {
//		OverSP = overSP;
//	}
//	public Integer getOverSatSP() {
//		return OverSatSP;
//	}
//	public void setOverSatSP(Integer overSatSP) {
//		OverSatSP = overSatSP;
//	}
//	public Integer getOverOP() {
//		return OverOP;
//	}
//	public void setOverOP(Integer overOP) {
//		OverOP = overOP;
//	}

	public Integer getRealPlan() {
		return RealPlan;
	}
	public void setRealPlan(Integer realPlan) {
		RealPlan = realPlan;
	}
}
