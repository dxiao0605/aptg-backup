package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class BestRatePlanModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String useMonth;
	private int inUse;
	private int RatePlanCode;

	private int UsuallyCC;
	private int SPCC;
	private int SatSPCC;
	private int OPCC;
	
	private BigDecimal TPMDemandPK;
	private BigDecimal TPMDemandSP;
	private BigDecimal TPMDemandSatSP;
	private BigDecimal TPMDemandOP;

	private BigDecimal TPMCECPK;
	private BigDecimal TPMCECSP;
	private BigDecimal TPMCECSatSP;
	private BigDecimal TPMCECOP;
	private BigDecimal TPMCEC;

	private BigDecimal BaseCharge;
	private BigDecimal UsageCharge;
	private BigDecimal OverCharge;
	private BigDecimal TotalCharge;

	private Integer OverPK;
	private Integer OverSP;
	private Integer OverSatSP;
	private Integer OverOP;
	
	private Integer RealPlan;
	
	public BestRatePlanModel() {
		this.OverPK = 0;
		this.OverSP = 0;
		this.OverSatSP = 0;
		this.OverOP = 0;
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
	public int getInUse() {
		return inUse;
	}
	public void setInUse(int inUse) {
		this.inUse = inUse;
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
	public BigDecimal getTPMDemandPK() {
		return TPMDemandPK;
	}
	public void setTPMDemandPK(BigDecimal tPMDemandPK) {
		TPMDemandPK = tPMDemandPK;
	}
	public BigDecimal getTPMDemandSP() {
		return TPMDemandSP;
	}
	public void setTPMDemandSP(BigDecimal tPMDemandSP) {
		TPMDemandSP = tPMDemandSP;
	}
	public BigDecimal getTPMDemandSatSP() {
		return TPMDemandSatSP;
	}
	public void setTPMDemandSatSP(BigDecimal tPMDemandSatSP) {
		TPMDemandSatSP = tPMDemandSatSP;
	}
	public BigDecimal getTPMDemandOP() {
		return TPMDemandOP;
	}
	public void setTPMDemandOP(BigDecimal tPMDemandOP) {
		TPMDemandOP = tPMDemandOP;
	}
	public BigDecimal getTPMCECPK() {
		return TPMCECPK;
	}
	public void setTPMCECPK(BigDecimal tPMCECPK) {
		TPMCECPK = tPMCECPK;
	}
	public BigDecimal getTPMCECSP() {
		return TPMCECSP;
	}
	public void setTPMCECSP(BigDecimal tPMCECSP) {
		TPMCECSP = tPMCECSP;
	}
	public BigDecimal getTPMCECSatSP() {
		return TPMCECSatSP;
	}
	public void setTPMCECSatSP(BigDecimal tPMCECSatSP) {
		TPMCECSatSP = tPMCECSatSP;
	}
	public BigDecimal getTPMCECOP() {
		return TPMCECOP;
	}
	public void setTPMCECOP(BigDecimal tPMCECOP) {
		TPMCECOP = tPMCECOP;
	}
	public BigDecimal getTPMCEC() {
		return TPMCEC;
	}
	public void setTPMCEC(BigDecimal tPMCEC) {
		TPMCEC = tPMCEC;
	}
	public BigDecimal getBaseCharge() {
		return BaseCharge;
	}
	public void setBaseCharge(BigDecimal baseCharge) {
		BaseCharge = baseCharge;
	}
	public BigDecimal getUsageCharge() {
		return UsageCharge;
	}
	public void setUsageCharge(BigDecimal usageCharge) {
		UsageCharge = usageCharge;
	}
	public BigDecimal getOverCharge() {
		return OverCharge;
	}
	public void setOverCharge(BigDecimal overCharge) {
		OverCharge = overCharge;
	}
	public BigDecimal getTotalCharge() {
		return TotalCharge;
	}
	public void setTotalCharge(BigDecimal totalCharge) {
		TotalCharge = totalCharge;
	}
	public Integer getOverPK() {
		return OverPK;
	}
	public void setOverPK(Integer overPK) {
		OverPK = overPK;
	}
	public Integer getOverSP() {
		return OverSP;
	}
	public void setOverSP(Integer overSP) {
		OverSP = overSP;
	}
	public Integer getOverSatSP() {
		return OverSatSP;
	}
	public void setOverSatSP(Integer overSatSP) {
		OverSatSP = overSatSP;
	}
	public Integer getOverOP() {
		return OverOP;
	}
	public void setOverOP(Integer overOP) {
		OverOP = overOP;
	}

	public Integer getRealPlan() {
		return RealPlan;
	}
	public void setRealPlan(Integer realPlan) {
		RealPlan = realPlan;
	}
}
