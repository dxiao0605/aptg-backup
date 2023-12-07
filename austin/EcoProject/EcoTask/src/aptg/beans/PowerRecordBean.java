package aptg.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerRecordBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String DeviceID;	// deviceID
	private String RecDate;		// 紀錄日期

	private BigDecimal Iavg;	// 平均電流
	private BigDecimal Imax;	// 最大電流

	private BigDecimal Vavg;	// 平均相電壓
	private BigDecimal Vmax;	// 

	private BigDecimal Wavg;	// 功率
	private BigDecimal Wmax;	// 
	
	private BigDecimal VavgP;	// 平均線電壓
	private BigDecimal VmaxP;	// 

	private BigDecimal DemandPK;	// 尖峰需量
	private BigDecimal DemandSP;	// 半尖峰需量
	private BigDecimal DemandSatSP;	// 週六半需量
	private BigDecimal DemandOP;	// 離峰需量

	private BigDecimal MCECPK;		// 當月尖峰累積用電量
	private BigDecimal MCECSP;		// 當月半尖峰累積用電量
	private BigDecimal MCECSatSP;	// 當月週六半尖峰累積用電量
	private BigDecimal MCECOP;		// 當月離峰累積用電量
	
	private String createTime;
	
	public PowerRecordBean() {
		this.Iavg = BigDecimal.ZERO;
		this.Imax = BigDecimal.ZERO;
		this.Vavg = BigDecimal.ZERO;
		this.Vmax = BigDecimal.ZERO;
		this.Wavg = BigDecimal.ZERO;
		this.Wmax = BigDecimal.ZERO;
		this.DemandPK = BigDecimal.ZERO;
		this.DemandSP = BigDecimal.ZERO;
		this.DemandSatSP = BigDecimal.ZERO;
		this.DemandOP = BigDecimal.ZERO;
		this.MCECPK = BigDecimal.ZERO;
		this.MCECSP = BigDecimal.ZERO;
		this.MCECSatSP = BigDecimal.ZERO;
		this.MCECOP = BigDecimal.ZERO;
	}
	
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
	public void setIavg(BigDecimal iavg) {
		Iavg = iavg;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
