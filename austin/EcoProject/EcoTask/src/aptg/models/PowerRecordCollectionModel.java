package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerRecordCollectionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String DeviceID;	// deviceID
	private String RecDate;		// 紀錄日期
	
	private BigDecimal Iavg;	// 平均電流
	private BigDecimal Imax;	// 電流最大值
	private BigDecimal Vavg;	// 平均相電壓
	private BigDecimal Vmax;	// 相電壓最大值
	private BigDecimal Wavg;	// 平均功率
	private BigDecimal Wmax;	// 功率最大值
	private BigDecimal VavgP;	// 平均線電壓的單日平均
	private BigDecimal VmaxP;	// 平均線電壓單日最大值

	private BigDecimal DemandPK;	// ECO5本日尖峰最大需量 (當日最大值)
	private BigDecimal DemandSP;	// ECO5本日半尖峰最大需量 (當日最大值)
	private BigDecimal DemandSatSP;	// ECO5本日週六半最大需量 (當日最大值)
	private BigDecimal DemandOP;	// ECO5本日離峰最大需量 (當日最大值)

	private BigDecimal MDemandPK;	// ECO5本月最大尖峰需量 (當月最大值)
	private BigDecimal MDemandSP;	// ECO5本月最大半尖峰需量 (當月最大值)
	private BigDecimal MDemandSatSP;// ECO5本月最大週六半需量 (當月最大值)
	private BigDecimal MDemandOP;	// ECO5本月最大離峰需量 (當月最大值)
	
	private BigDecimal DCECPK;		// ECO5本日尖峰累積用電量
	private BigDecimal DCECSP;		// ECO5本日半尖峰累積用電量
	private BigDecimal DCECSatSP;	// ECO5本日週六半尖峰累積用電量
	private BigDecimal DCECOP;		// ECO5本日離峰累積用電量
	private BigDecimal DCEC;		// ECO5本日總用電量
	
	private BigDecimal MCECPK;		// ECO5本月尖峰累積用電量
	private BigDecimal MCECSP;		// ECO5本月半尖峰累積用電量
	private BigDecimal MCECSatSP;	// ECO5本月週六半尖峰累積用電量
	private BigDecimal MCECOP;		// ECO5本月離峰累積用電量
	private BigDecimal CEC;			// 累積總用電量

	private BigDecimal Mode1;		// 需量預測模式1
	private BigDecimal Mode2;		// 需量預測模式2
	private BigDecimal Mode3;		// 需量預測模式3
	private BigDecimal Mode4;		// 需量預測模式4

	private BigDecimal TPDemandPK;		// 台電計法最大尖峰需量 (當日最大值)
	private BigDecimal TPDemandSP;		// 台電計法最大半尖峰最大需量 (當日最大值)
	private BigDecimal TPDemandSatSP;	// 台電計法最大週六半最大需量 (當日最大值)
	private BigDecimal TPDemandOP;		// 台電計法最大離峰最大需量 (當日最大值)

	private BigDecimal TPMDemandPK;		// 台電計法本月最大尖峰需量 (當月最大值)
	private BigDecimal TPMDemandSP;		// 台電計法本月最大半尖峰需量 (當月最大值)
	private BigDecimal TPMDemandSatSP;	// 台電計法本月最大週六半需量 (當月最大值)
	private BigDecimal TPMDemandOP;		// 台電計法本月最大離峰需量 (當月最大值)

	private BigDecimal TPDCECPK;		// 台電計法本日尖峰累積用電量
	private BigDecimal TPDCECSP;		// 台電計法本日半尖峰累積用電量
	private BigDecimal TPDCECSatSP;		// 台電計法本日週六半尖峰累積用電量
	private BigDecimal TPDCECOP;		// 台電計法本日離峰累積用電量
	
	private BigDecimal TPMCECPK;		// 台電計法本月尖峰累積用電量
	private BigDecimal TPMCECSP;		// 台電計法本月半尖峰累積用電量
	private BigDecimal TPMCECSatSP;		// 台電計法本月週六半尖峰累積用電量
	private BigDecimal TPMCECOP;		// 台電計法本月離峰累積用電量
	
	private BigDecimal KWh;				// 電表值
	
	private BigDecimal FcstECO5MCECPK;		// ECO5預測當月尖峰累積用電量
	private BigDecimal FcstECO5MCECSP;		// ECO5預測當月半尖峰累積用電量
	private BigDecimal FcstECO5MCECSatSP;	// ECO5預測當月週六半尖峰累積用電量
	private BigDecimal FcstECO5MCECOP;		// ECO5預測當月離峰累積用電量
	private BigDecimal FcstECO5MCEC;		// ECO5預測當月總用電量

	// 10/15新增
	private int RatePlanCode;
	private Integer RealPlan;
	private Integer UsuallyCC;
	private Integer SPCC;
	private Integer SatSPCC;
	private Integer OPCC;
	
	
	private String createTime;
	private String updateTime;
	
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public BigDecimal getIavg() {
		return Iavg;
	}
	public void setIavg(BigDecimal ivag) {
		Iavg = ivag;
	}
	public BigDecimal getImax() {
		return Imax;
	}
	public void setImax(BigDecimal imax) {
		Imax = imax;
	}
	public BigDecimal getVavg() {
		return Vavg;
	}
	public void setVavg(BigDecimal vavg) {
		Vavg = vavg;
	}
	public BigDecimal getVmax() {
		return Vmax;
	}
	public void setVmax(BigDecimal vmax) {
		Vmax = vmax;
	}
	public BigDecimal getWavg() {
		return Wavg;
	}
	public void setWavg(BigDecimal wavg) {
		Wavg = wavg;
	}
	public BigDecimal getWmax() {
		return Wmax;
	}
	public void setWmax(BigDecimal wmax) {
		Wmax = wmax;
	}
	public BigDecimal getVavgP() {
		return VavgP;
	}
	public void setVavgP(BigDecimal vavgP) {
		VavgP = vavgP;
	}
	public BigDecimal getVmaxP() {
		return VmaxP;
	}
	public void setVmaxP(BigDecimal vmaxP) {
		VmaxP = vmaxP;
	}
	public BigDecimal getDemandPK() {
		return DemandPK;
	}
	public void setDemandPK(BigDecimal demandPK) {
		DemandPK = demandPK;
	}
	public BigDecimal getDemandSP() {
		return DemandSP;
	}
	public void setDemandSP(BigDecimal demandSP) {
		DemandSP = demandSP;
	}
	public BigDecimal getDemandSatSP() {
		return DemandSatSP;
	}
	public void setDemandSatSP(BigDecimal demandSatSP) {
		DemandSatSP = demandSatSP;
	}
	public BigDecimal getDemandOP() {
		return DemandOP;
	}
	public void setDemandOP(BigDecimal demandOP) {
		DemandOP = demandOP;
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
	public BigDecimal getDCECPK() {
		return DCECPK;
	}
	public void setDCECPK(BigDecimal dCECPK) {
		DCECPK = dCECPK;
	}
	public BigDecimal getDCECSP() {
		return DCECSP;
	}
	public void setDCECSP(BigDecimal dCECSP) {
		DCECSP = dCECSP;
	}
	public BigDecimal getDCECSatSP() {
		return DCECSatSP;
	}
	public void setDCECSatSP(BigDecimal dCECSatSP) {
		DCECSatSP = dCECSatSP;
	}
	public BigDecimal getDCECOP() {
		return DCECOP;
	}
	public void setDCECOP(BigDecimal dCECOP) {
		DCECOP = dCECOP;
	}
	public BigDecimal getDCEC() {
		return DCEC;
	}
	public void setDCEC(BigDecimal dCEC) {
		DCEC = dCEC;
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
	public BigDecimal getCEC() {
		return CEC;
	}
	public void setCEC(BigDecimal cEC) {
		CEC = cEC;
	}
	
	public BigDecimal getMode1() {
		return Mode1;
	}
	public void setMode1(BigDecimal mode1) {
		Mode1 = mode1;
	}
	public BigDecimal getMode2() {
		return Mode2;
	}
	public void setMode2(BigDecimal mode2) {
		Mode2 = mode2;
	}
	public BigDecimal getMode3() {
		return Mode3;
	}
	public void setMode3(BigDecimal mode3) {
		Mode3 = mode3;
	}
	public BigDecimal getMode4() {
		return Mode4;
	}
	public void setMode4(BigDecimal mode4) {
		Mode4 = mode4;
	}
	
	public BigDecimal getTPDemandPK() {
		return TPDemandPK;
	}
	public void setTPDemandPK(BigDecimal tPDemandPK) {
		TPDemandPK = tPDemandPK;
	}
	public BigDecimal getTPDemandSP() {
		return TPDemandSP;
	}
	public void setTPDemandSP(BigDecimal tPDemandSP) {
		TPDemandSP = tPDemandSP;
	}
	public BigDecimal getTPDemandSatSP() {
		return TPDemandSatSP;
	}
	public void setTPDemandSatSP(BigDecimal tPDemandSatSP) {
		TPDemandSatSP = tPDemandSatSP;
	}
	public BigDecimal getTPDemandOP() {
		return TPDemandOP;
	}
	public void setTPDemandOP(BigDecimal tPDemandOP) {
		TPDemandOP = tPDemandOP;
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
	public BigDecimal getTPDCECPK() {
		return TPDCECPK;
	}
	public void setTPDCECPK(BigDecimal tPDCECPK) {
		TPDCECPK = tPDCECPK;
	}
	public BigDecimal getTPDCECSP() {
		return TPDCECSP;
	}
	public void setTPDCECSP(BigDecimal tPDCECSP) {
		TPDCECSP = tPDCECSP;
	}
	public BigDecimal getTPDCECSatSP() {
		return TPDCECSatSP;
	}
	public void setTPDCECSatSP(BigDecimal tPDCECSatSP) {
		TPDCECSatSP = tPDCECSatSP;
	}
	public BigDecimal getTPDCECOP() {
		return TPDCECOP;
	}
	public void setTPDCECOP(BigDecimal tPDCECOP) {
		TPDCECOP = tPDCECOP;
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
	public BigDecimal getKWh() {
		return KWh;
	}
	public void setKWh(BigDecimal kWh) {
		KWh = kWh;
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
	
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	public Integer getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(Integer ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public Integer getRealPlan() {
		return RealPlan;
	}
	public void setRealPlan(Integer realPlan) {
		RealPlan = realPlan;
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
}
