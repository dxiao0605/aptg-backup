package aptg.fixdata.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerRecordModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer seqno;
	private String deviceId;	// deviceID
	private String recTime;		// 紀錄日期時間
	
	private BigDecimal i1;		// 電流R
	private BigDecimal i2;		// 電流S
	private BigDecimal i3;		// 電流T
	private BigDecimal iavg;	// 平均電流
	
	private BigDecimal v1;		// 相電壓R
	private BigDecimal v2;		// 相電壓S
	private BigDecimal v3;		// 相電壓T
	private BigDecimal vavg;	// 平均相電壓

	private BigDecimal v12;		// 線電壓AB
	private BigDecimal v23;		// 線電壓BC
	private BigDecimal v31;		// 線電壓CA
	private BigDecimal vavgP;	// 平均線電壓

	private BigDecimal w;		// 功率
	private BigDecimal var;		// 虛功
	private BigDecimal va;		// 視在
	private BigDecimal pf;		// 功率因數
	private BigDecimal kWh;		// 累績總用電量
	private BigDecimal kvarh;	// 累積總需功用電量
	private BigDecimal hz;		// 頻率
	private BigDecimal thVavg;	// 電壓總諧波率
	private BigDecimal thIavg;	// 電流總諧波率

	private BigDecimal mode1;	// 需量預測模式1
	private BigDecimal mode2;	// 需量預測模式2
	private BigDecimal mode3;	// 需量預測模式3
	private BigDecimal mode4;	// 需量預測模式4

	private BigDecimal demandPK;	// 尖峰需量
	private BigDecimal demandSP;	// 半尖峰需量
	private BigDecimal demandSatSP;	// 週六半需量
	private BigDecimal demandOP;	// 離峰需量

	private BigDecimal mcecPK;		// 當月尖峰累積用電量
	private BigDecimal mcecSP;		// 當月半尖峰累積用電量
	private BigDecimal mcecSatSP;	// 當月週六半尖峰累積用電量
	private BigDecimal mcecOP;		// 當月離峰累積用電量

	private BigDecimal highCECPK;	// 當日高壓三段式尖峰累積用電量
	private BigDecimal highCECSP;	// 當日高壓三段式半尖峰累積用電量
	private BigDecimal highCECOP;	// 當日高壓三段式離峰累積用電量

	private BigDecimal ecoFlag;		// 是否已上傳至ECO
	private BigDecimal iiiFlag;		// 是否已上傳至III

	private String createTime;
	private String updateTime;
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public BigDecimal getI1() {
		return i1;
	}
	public void setI1(BigDecimal i1) {
		this.i1 = i1;
	}
	public BigDecimal getI2() {
		return i2;
	}
	public void setI2(BigDecimal i2) {
		this.i2 = i2;
	}
	public BigDecimal getI3() {
		return i3;
	}
	public void setI3(BigDecimal i3) {
		this.i3 = i3;
	}
	public BigDecimal getIavg() {
		return iavg;
	}
	public void setIavg(BigDecimal iavg) {
		this.iavg = iavg;
	}
	public BigDecimal getV1() {
		return v1;
	}
	public void setV1(BigDecimal v1) {
		this.v1 = v1;
	}
	public BigDecimal getV2() {
		return v2;
	}
	public void setV2(BigDecimal v2) {
		this.v2 = v2;
	}
	public BigDecimal getV3() {
		return v3;
	}
	public void setV3(BigDecimal v3) {
		this.v3 = v3;
	}
	public BigDecimal getVavg() {
		return vavg;
	}
	public void setVavg(BigDecimal vavg) {
		this.vavg = vavg;
	}
	public BigDecimal getV12() {
		return v12;
	}
	public void setV12(BigDecimal v12) {
		this.v12 = v12;
	}
	public BigDecimal getV23() {
		return v23;
	}
	public void setV23(BigDecimal v23) {
		this.v23 = v23;
	}
	public BigDecimal getV31() {
		return v31;
	}
	public void setV31(BigDecimal v31) {
		this.v31 = v31;
	}
	public BigDecimal getVavgP() {
		return vavgP;
	}
	public void setVavgP(BigDecimal vavgP) {
		this.vavgP = vavgP;
	}
	public BigDecimal getW() {
		return w;
	}
	public void setW(BigDecimal w) {
		this.w = w;
	}
	public BigDecimal getVar() {
		return var;
	}
	public void setVar(BigDecimal var) {
		this.var = var;
	}
	public BigDecimal getVa() {
		return va;
	}
	public void setVa(BigDecimal va) {
		this.va = va;
	}
	public BigDecimal getPf() {
		return pf;
	}
	public void setPf(BigDecimal pf) {
		this.pf = pf;
	}
	public BigDecimal getkWh() {
		return kWh;
	}
	public void setkWh(BigDecimal kWh) {
		this.kWh = kWh;
	}
	public BigDecimal getKvarh() {
		return kvarh;
	}
	public void setKvarh(BigDecimal kvarh) {
		this.kvarh = kvarh;
	}
	public BigDecimal getHz() {
		return hz;
	}
	public void setHz(BigDecimal hz) {
		this.hz = hz;
	}
	public BigDecimal getThVavg() {
		return thVavg;
	}
	public void setThVavg(BigDecimal thVavg) {
		this.thVavg = thVavg;
	}
	public BigDecimal getThIavg() {
		return thIavg;
	}
	public void setThIavg(BigDecimal thIavg) {
		this.thIavg = thIavg;
	}
	public BigDecimal getMode1() {
		return mode1;
	}
	public void setMode1(BigDecimal mode1) {
		this.mode1 = mode1;
	}
	public BigDecimal getMode2() {
		return mode2;
	}
	public void setMode2(BigDecimal mode2) {
		this.mode2 = mode2;
	}
	public BigDecimal getMode3() {
		return mode3;
	}
	public void setMode3(BigDecimal mode3) {
		this.mode3 = mode3;
	}
	public BigDecimal getMode4() {
		return mode4;
	}
	public void setMode4(BigDecimal mode4) {
		this.mode4 = mode4;
	}
	public BigDecimal getDemandPK() {
		return demandPK;
	}
	public void setDemandPK(BigDecimal demandPK) {
		this.demandPK = demandPK;
	}
	public BigDecimal getDemandSP() {
		return demandSP;
	}
	public void setDemandSP(BigDecimal demandSP) {
		this.demandSP = demandSP;
	}
	public BigDecimal getDemandSatSP() {
		return demandSatSP;
	}
	public void setDemandSatSP(BigDecimal demandSatSP) {
		this.demandSatSP = demandSatSP;
	}
	public BigDecimal getDemandOP() {
		return demandOP;
	}
	public void setDemandOP(BigDecimal demandOP) {
		this.demandOP = demandOP;
	}
	public BigDecimal getMcecPK() {
		return mcecPK;
	}
	public void setMcecPK(BigDecimal mcecPK) {
		this.mcecPK = mcecPK;
	}
	public BigDecimal getMcecSP() {
		return mcecSP;
	}
	public void setMcecSP(BigDecimal mcecSP) {
		this.mcecSP = mcecSP;
	}
	public BigDecimal getMcecSatSP() {
		return mcecSatSP;
	}
	public void setMcecSatSP(BigDecimal mcecSatSP) {
		this.mcecSatSP = mcecSatSP;
	}
	public BigDecimal getMcecOP() {
		return mcecOP;
	}
	public void setMcecOP(BigDecimal mcecOP) {
		this.mcecOP = mcecOP;
	}
	public BigDecimal getHighCECPK() {
		return highCECPK;
	}
	public void setHighCECPK(BigDecimal highCECPK) {
		this.highCECPK = highCECPK;
	}
	public BigDecimal getHighCECSP() {
		return highCECSP;
	}
	public void setHighCECSP(BigDecimal highCECSP) {
		this.highCECSP = highCECSP;
	}
	public BigDecimal getHighCECOP() {
		return highCECOP;
	}
	public void setHighCECOP(BigDecimal highCECOP) {
		this.highCECOP = highCECOP;
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
}
