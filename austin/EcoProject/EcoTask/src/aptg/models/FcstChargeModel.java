package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class FcstChargeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String useMonth;
	private String useTime;
	private int RatePlanCode;
	
	private int UsuallyCC;
	private int SPCC;
	private int SatSPCC;
	private int OPCC;
	
	private BigDecimal MDemandPK;
	private BigDecimal MDemandSP;
	private BigDecimal MDemandSatSP;
	private BigDecimal MDemandOP;
	
	private BigDecimal TPMDemandPK;
	private BigDecimal TPMDemandSP;
	private BigDecimal TPMDemandSatSP;
	private BigDecimal TPMDemandOP;
	
	private BigDecimal MCECPK;
	private BigDecimal MCECSP;
	private BigDecimal MCECSatSP;
	private BigDecimal MCECOP;
	private BigDecimal MCEC;
	
	private BigDecimal TPMCECPK;
	private BigDecimal TPMCECSP;
	private BigDecimal TPMCECSatSP;
	private BigDecimal TPMCECOP;
	private BigDecimal TPMCEC;

	private BigDecimal BaseCharge;	// 基本電費
	private BigDecimal UsageCharge;	// 流動電費
	private BigDecimal OverCharge;	// 非約定電費
	private BigDecimal TotalCharge;
	
	private BigDecimal FcstMCECPK;
	private BigDecimal FcstMCECSP;
	private BigDecimal FcstMCECSatSP;
	private BigDecimal FcstMCECOP;
	private BigDecimal FcstMCEC;
	
	private BigDecimal FcstBaseCharge;
	private BigDecimal FcstUsageCharge;
	private BigDecimal FcstOverCharge;
	private BigDecimal FcstTotalCharge;
	
	private Integer OverPK;
	private Integer OverSP;
	private Integer OverSatSP;
	private Integer OverOP;
	
	private Integer RealPlan;
	
	private int PowerPhase;
	
	private BigDecimal FcstECO5MCECPK;
	private BigDecimal FcstECO5MCECSP;
	private BigDecimal FcstECO5MCECSatSP;
	private BigDecimal FcstECO5MCECOP;
	private BigDecimal FcstECO5MCEC;
	
	public FcstChargeModel() {
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
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
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
	public BigDecimal getFcstMCECPK() {
		return FcstMCECPK;
	}
	public void setFcstMCECPK(BigDecimal fcstMCECPK) {
		FcstMCECPK = fcstMCECPK;
	}
	public BigDecimal getFcstMCECSP() {
		return FcstMCECSP;
	}
	public void setFcstMCECSP(BigDecimal fcstMCECSP) {
		FcstMCECSP = fcstMCECSP;
	}
	public BigDecimal getFcstMCECSatSP() {
		return FcstMCECSatSP;
	}
	public void setFcstMCECSatSP(BigDecimal fcstMCECSatSP) {
		FcstMCECSatSP = fcstMCECSatSP;
	}
	public BigDecimal getFcstMCECOP() {
		return FcstMCECOP;
	}
	public void setFcstMCECOP(BigDecimal fcstMCECOP) {
		FcstMCECOP = fcstMCECOP;
	}
	public BigDecimal getFcstMCEC() {
		return FcstMCEC;
	}
	public void setFcstMCEC(BigDecimal fcstMCEC) {
		FcstMCEC = fcstMCEC;
	}
	public BigDecimal getFcstBaseCharge() {
		return FcstBaseCharge;
	}
	public void setFcstBaseCharge(BigDecimal fcstBaseCharge) {
		FcstBaseCharge = fcstBaseCharge;
	}
	public BigDecimal getFcstUsageCharge() {
		return FcstUsageCharge;
	}
	public void setFcstUsageCharge(BigDecimal fcstUsageCharge) {
		FcstUsageCharge = fcstUsageCharge;
	}
	public BigDecimal getFcstOverCharge() {
		return FcstOverCharge;
	}
	public void setFcstOverCharge(BigDecimal fcstOverCharge) {
		FcstOverCharge = fcstOverCharge;
	}
	public BigDecimal getFcstTotalCharge() {
		return FcstTotalCharge;
	}
	public void setFcstTotalCharge(BigDecimal fcstTotalCharge) {
		FcstTotalCharge = fcstTotalCharge;
	}
	public int getPowerPhase() {
		return PowerPhase;
	}
	public void setPowerPhase(int powerPhase) {
		PowerPhase = powerPhase;
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
	
	public BigDecimal getFcstECO5MCECPK() {
		return FcstECO5MCECPK;
	}
	public void setFcstECO5MCECPK(BigDecimal fcstECO5MCECPK) {
		FcstECO5MCECPK = fcstECO5MCECPK;
	}
	public BigDecimal getFcstECO5MCECSP() {
		return FcstECO5MCECSP;
	}
	public void setFcstECO5MCECSP(BigDecimal fcstECO5MCECSP) {
		FcstECO5MCECSP = fcstECO5MCECSP;
	}
	public BigDecimal getFcstECO5MCECSatSP() {
		return FcstECO5MCECSatSP;
	}
	public void setFcstECO5MCECSatSP(BigDecimal fcstECO5MCECSatSP) {
		FcstECO5MCECSatSP = fcstECO5MCECSatSP;
	}
	public BigDecimal getFcstECO5MCECOP() {
		return FcstECO5MCECOP;
	}
	public void setFcstECO5MCECOP(BigDecimal fcstECO5MCECOP) {
		FcstECO5MCECOP = fcstECO5MCECOP;
	}
	public BigDecimal getFcstECO5MCEC() {
		return FcstECO5MCEC;
	}
	public void setFcstECO5MCEC(BigDecimal fcstECO5MCEC) {
		FcstECO5MCEC = fcstECO5MCEC;
	}
}
