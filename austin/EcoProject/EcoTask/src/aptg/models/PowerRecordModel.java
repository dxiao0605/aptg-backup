package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerRecordModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer seqno;
	private String DeviceID;	// deviceID
	private String RecTime;		// 紀錄日期時間
	
	private BigDecimal I1;		// 電流R
	private BigDecimal I2;		// 電流S
	private BigDecimal I3;		// 電流T
	private BigDecimal Iavg;	// 平均電流
	
	private BigDecimal V1;		// 相電壓R
	private BigDecimal V2;		// 相電壓S
	private BigDecimal V3;		// 相電壓T
	private BigDecimal Vavg;	// 平均相電壓

	private BigDecimal V12;		// 線電壓AB
	private BigDecimal V23;		// 線電壓BC
	private BigDecimal V31;		// 線電壓CA
	private BigDecimal VavgP;	// 平均線電壓

	private BigDecimal W;		// 功率
	private BigDecimal Var;		// 虛功
	private BigDecimal VA;		// 視在
	private BigDecimal PF;		// 功率因數
	private BigDecimal KWh;		// 累績總用電量
	private BigDecimal Kvarh;	// 累積總需功用電量
	private BigDecimal Hz;		// 頻率
	private BigDecimal THVavg;	// 電壓總諧波率
	private BigDecimal THIavg;	// 電流總諧波率

	private BigDecimal Mode1;	// 需量預測模式1
	private BigDecimal Mode2;	// 需量預測模式2
	private BigDecimal Mode3;	// 需量預測模式3
	private BigDecimal Mode4;	// 需量預測模式4

	private BigDecimal DemandPK;	// 尖峰需量
	private BigDecimal DemandSP;	// 半尖峰需量
	private BigDecimal DemandSatSP;	// 週六半需量
	private BigDecimal DemandOP;	// 離峰需量

	private BigDecimal MCECPK;		// 當月尖峰累積用電量
	private BigDecimal MCECSP;		// 當月半尖峰累積用電量
	private BigDecimal MCECSatSP;	// 當月週六半尖峰累積用電量
	private BigDecimal MCECOP;		// 當月離峰累積用電量

	private BigDecimal HighCECPK;	// 當日高壓三段式尖峰累積用電量
	private BigDecimal HighCECSP;	// 當日高壓三段式半尖峰累積用電量
	private BigDecimal HighCECOP;	// 當日高壓三段式離峰累積用電量

	private BigDecimal ecoFlag;		// 是否已上傳至ECO
	private BigDecimal iiiFlag;		// 是否已上傳至III

	private String powerAccount;
	private String ApplyDate;
	private Integer RatePlanCode;
	
	private String connectTime;
	private String createTime;
	private String updateTime;
	
	public PowerRecordModel() {
		this.I1 = BigDecimal.ZERO;
		this.I2 = BigDecimal.ZERO;
		this.I3 = BigDecimal.ZERO;
		this.Iavg = BigDecimal.ZERO;

		this.V1 = BigDecimal.ZERO;
		this.V2 = BigDecimal.ZERO;
		this.V3 = BigDecimal.ZERO;
		this.Vavg = BigDecimal.ZERO;

		this.V12 = BigDecimal.ZERO;
		this.V23 = BigDecimal.ZERO;
		this.V31 = BigDecimal.ZERO;
		this.VavgP = BigDecimal.ZERO;

		this.W = BigDecimal.ZERO;
		this.Var = BigDecimal.ZERO;
		this.VA = BigDecimal.ZERO;
		this.PF = BigDecimal.ZERO;
		this.KWh = BigDecimal.ZERO;
		this.Kvarh = BigDecimal.ZERO;
		this.Hz = BigDecimal.ZERO;
		this.THVavg = BigDecimal.ZERO;
		this.THIavg = BigDecimal.ZERO;

		this.Mode1 = BigDecimal.ZERO;
		this.Mode2 = BigDecimal.ZERO;
		this.Mode3 = BigDecimal.ZERO;
		this.Mode4 = BigDecimal.ZERO;

		this.DemandPK = BigDecimal.ZERO;
		this.DemandSP = BigDecimal.ZERO;
		this.DemandSatSP = BigDecimal.ZERO;
		this.DemandOP = BigDecimal.ZERO;

		this.MCECPK = BigDecimal.ZERO;
		this.MCECSP = BigDecimal.ZERO;
		this.MCECSatSP = BigDecimal.ZERO;
		this.MCECOP = BigDecimal.ZERO;

		this.HighCECPK = BigDecimal.ZERO;
		this.HighCECSP = BigDecimal.ZERO;
		this.HighCECOP = BigDecimal.ZERO;
	}
	
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
	public String getRecTime() {
		return RecTime;
	}
	public void setRecTime(String recTime) {
		RecTime = recTime;
	}
	public BigDecimal getI1() {
		return I1;
	}
	public void setI1(BigDecimal i1) {
		I1 = i1;
	}
	public BigDecimal getI2() {
		return I2;
	}
	public void setI2(BigDecimal i2) {
		I2 = i2;
	}
	public BigDecimal getI3() {
		return I3;
	}
	public void setI3(BigDecimal i3) {
		I3 = i3;
	}
	public BigDecimal getIavg() {
		return Iavg;
	}
	public void setIavg(BigDecimal iavg) {
		Iavg = iavg;
	}
	public BigDecimal getV1() {
		return V1;
	}
	public void setV1(BigDecimal v1) {
		V1 = v1;
	}
	public BigDecimal getV2() {
		return V2;
	}
	public void setV2(BigDecimal v2) {
		V2 = v2;
	}
	public BigDecimal getV3() {
		return V3;
	}
	public void setV3(BigDecimal v3) {
		V3 = v3;
	}
	public BigDecimal getVavg() {
		return Vavg;
	}
	public void setVavg(BigDecimal vavg) {
		Vavg = vavg;
	}
	public BigDecimal getV12() {
		return V12;
	}
	public void setV12(BigDecimal v12) {
		V12 = v12;
	}
	public BigDecimal getV23() {
		return V23;
	}
	public void setV23(BigDecimal v23) {
		V23 = v23;
	}
	public BigDecimal getV31() {
		return V31;
	}
	public void setV31(BigDecimal v31) {
		V31 = v31;
	}
	public BigDecimal getVavgP() {
		return VavgP;
	}
	public void setVavgP(BigDecimal vavgP) {
		VavgP = vavgP;
	}
	public BigDecimal getW() {
		return W;
	}
	public void setW(BigDecimal w) {
		W = w;
	}
	public BigDecimal getVar() {
		return Var;
	}
	public void setVar(BigDecimal var) {
		Var = var;
	}
	public BigDecimal getVA() {
		return VA;
	}
	public void setVA(BigDecimal vA) {
		VA = vA;
	}
	public BigDecimal getPF() {
		return PF;
	}
	public void setPF(BigDecimal pF) {
		PF = pF;
	}
	public BigDecimal getKWh() {
		return KWh;
	}
	public void setKWh(BigDecimal kWh) {
		KWh = kWh;
	}
	public BigDecimal getKvarh() {
		return Kvarh;
	}
	public void setKvarh(BigDecimal kvarh) {
		Kvarh = kvarh;
	}
	public BigDecimal getHz() {
		return Hz;
	}
	public void setHz(BigDecimal hz) {
		Hz = hz;
	}
	public BigDecimal getTHVavg() {
		return THVavg;
	}
	public void setTHVavg(BigDecimal tHVavg) {
		THVavg = tHVavg;
	}
	public BigDecimal getTHIavg() {
		return THIavg;
	}
	public void setTHIavg(BigDecimal tHIavg) {
		THIavg = tHIavg;
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
	public BigDecimal getHighCECPK() {
		return HighCECPK;
	}
	public void setHighCECPK(BigDecimal highCECPK) {
		HighCECPK = highCECPK;
	}
	public BigDecimal getHighCECSP() {
		return HighCECSP;
	}
	public void setHighCECSP(BigDecimal highCECSP) {
		HighCECSP = highCECSP;
	}
	public BigDecimal getHighCECOP() {
		return HighCECOP;
	}
	public void setHighCECOP(BigDecimal highCECOP) {
		HighCECOP = highCECOP;
	}
	
	public String getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
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
	
	
	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getApplyDate() {
		return ApplyDate;
	}
	public void setApplyDate(String applyDate) {
		ApplyDate = applyDate;
	}
	public Integer getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(Integer ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}

	public BigDecimal getEcoFlag() {
		return ecoFlag;
	}

	public void setEcoFlag(BigDecimal ecoFlag) {
		this.ecoFlag = ecoFlag;
	}

	public BigDecimal getIiiFlag() {
		return iiiFlag;
	}

	public void setIiiFlag(BigDecimal iiiFlag) {
		this.iiiFlag = iiiFlag;
	}
}
