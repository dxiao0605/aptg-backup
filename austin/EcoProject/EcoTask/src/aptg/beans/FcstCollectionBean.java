package aptg.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class FcstCollectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String DeviceID;
	private int PowerPhase;
	
	private Integer RatePlanCode;
	private Integer UsuallyCC;
	private Integer SPCC;
	private Integer SatSPCC;
	private Integer OPCC;
	
	private String RecDate;

	private BigDecimal MDemandPK;
	private BigDecimal MDemandSP;
	private BigDecimal MDemandSatSP;
	private BigDecimal MDemandOP;

	private BigDecimal MCECPK;
	private BigDecimal MCECSP;
	private BigDecimal MCECSatSP;
	private BigDecimal MCECOP;
	private BigDecimal MCEC;
	
	private BigDecimal FcstECO5MCECPK;
	private BigDecimal FcstECO5MCECSP;
	private BigDecimal FcstECO5MCECSatSP;
	private BigDecimal FcstECO5MCECOP;
	private BigDecimal FcstECO5MCEC;
	
	
	public FcstCollectionBean() {
		this.UsuallyCC = 0;
		this.SPCC = 0;
		this.SatSPCC = 0;
		this.OPCC = 0;
	}
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
	}
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public int getPowerPhase() {
		return PowerPhase;
	}
	public void setPowerPhase(int powerPhase) {
		PowerPhase = powerPhase;
	}
	public Integer getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(Integer ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public Integer getUsuallyCC() {
		return UsuallyCC;
	}
	public void setUsuallyCC(Integer usuallyCC) {
		UsuallyCC = usuallyCC;
	}
	public Integer getSPCC() {
		return SPCC;
	}
	public void setSPCC(Integer sPCC) {
		SPCC = sPCC;
	}
	public Integer getSatSPCC() {
		return SatSPCC;
	}
	public void setSatSPCC(Integer satSPCC) {
		SatSPCC = satSPCC;
	}
	public Integer getOPCC() {
		return OPCC;
	}
	public void setOPCC(Integer oPCC) {
		OPCC = oPCC;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
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
