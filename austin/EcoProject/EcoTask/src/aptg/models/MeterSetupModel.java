package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MeterSetupModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer seqno;
	private String deviceID;
	private String eco5Account;			// ECO5帳號
	private Integer meterSerialNr;		// 電表序號
	private Integer meterId;			// 電表站號
	private String meterName;			// 電表名稱
	private Integer meterTypeCode;		// 電表清單編號
	private String installPosition;		// 安裝位置
	private String treeChartId;			// 樹狀圖編號
	private Integer enabled;			// 啟用狀態
	private Integer wiringCode;			// 接線方式代碼
	private Integer usageCode;			// 用電種類代碼
	private String powerAccount;		// 電號
	private Integer powerFactorEnabled;	// 是否計算功率因數補償費(0 代表否, 1 代表是)
	private String areaName;			// 用電區域
	private BigDecimal area;			// 用電區域面積
	private Integer people;				// 用電區域人數
	private BigDecimal ratedPower;			// 額定功率
	
	private Integer dfEnabled;			// 需量預測警報啟用狀態(0:不啟用, 1:啟用)
	private Integer dfCode;				// 需量預測模式
	private Integer dfCycle;			// 需量預測週期
	private Integer dfUpLimit;			// 需量預測上限
	private Integer dfLoLimit;			// 需量預測下限
	
	private Integer usuallyCC;			// 經常契約容量
	private Integer spcc;				// 半尖峰契約容量(非夏月)
	private Integer satSPCC;			// 週六半尖峰契約容量
	private Integer opcc;				// 離峰契約容量
	
	private Integer curAlertEnabled;	// 電流警報啟用狀態(0:不啟用, 1:啟用)
	private BigDecimal curUpLimit;		// 電流上限(額定電流)
	private BigDecimal curLoLimit;		// 電流下限
	
	private Integer volAlertEnabled;	// 電壓警報啟用狀態(0:不啟用, 1:啟用)
	private Integer volAlertType;		// 電壓警報類型
	private BigDecimal volUpLimit;		// 電壓上限(額定電壓)
	private BigDecimal volLoLimit;		// 電壓下限

	private Integer ecAlertEnabled;		// 用電量警報啟用狀態
	private BigDecimal ecUpLimit;		// 用電量上限
	
	private String createTime;
	private String updateTime;
	
	public Integer getSeqno() {
		return seqno;
	}
	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getEco5Account() {
		return eco5Account;
	}
	public void setEco5Account(String eco5Account) {
		this.eco5Account = eco5Account;
	}
	public Integer getMeterSerialNr() {
		return meterSerialNr;
	}
	public void setMeterSerialNr(Integer meterSerialNr) {
		this.meterSerialNr = meterSerialNr;
	}
	public Integer getMeterId() {
		return meterId;
	}
	public void setMeterId(Integer meterId) {
		this.meterId = meterId;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public Integer getMeterTypeCode() {
		return meterTypeCode;
	}
	public void setMeterTypeCode(Integer meterTypeCode) {
		this.meterTypeCode = meterTypeCode;
	}
	public String getInstallPosition() {
		return installPosition;
	}
	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}
	public String getTreeChartId() {
		return treeChartId;
	}
	public void setTreeChartId(String treeChartId) {
		this.treeChartId = treeChartId;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public Integer getWiringCode() {
		return wiringCode;
	}
	public void setWiringCode(Integer wiringCode) {
		this.wiringCode = wiringCode;
	}
	public Integer getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(Integer usageCode) {
		this.usageCode = usageCode;
	}
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public Integer getPowerFactorEnabled() {
		return powerFactorEnabled;
	}
	public void setPowerFactorEnabled(Integer powerFactorEnabled) {
		this.powerFactorEnabled = powerFactorEnabled;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public Integer getPeople() {
		return people;
	}
	public void setPeople(Integer people) {
		this.people = people;
	}
	public BigDecimal getRatedPower() {
		return ratedPower;
	}
	public void setRatedPower(BigDecimal ratedPower) {
		this.ratedPower = ratedPower;
	}
	public Integer getDfEnabled() {
		return dfEnabled;
	}
	public void setDfEnabled(Integer dfEnabled) {
		this.dfEnabled = dfEnabled;
	}
	public Integer getDfCode() {
		return dfCode;
	}
	public void setDfCode(Integer dfCode) {
		this.dfCode = dfCode;
	}
	public Integer getDfCycle() {
		return dfCycle;
	}
	public void setDfCycle(Integer dfCycle) {
		this.dfCycle = dfCycle;
	}
	public Integer getDfUpLimit() {
		return dfUpLimit;
	}
	public void setDfUpLimit(Integer dfUpLimit) {
		this.dfUpLimit = dfUpLimit;
	}
	public Integer getDfLoLimit() {
		return dfLoLimit;
	}
	public void setDfLoLimit(Integer dfLoLimit) {
		this.dfLoLimit = dfLoLimit;
	}
	public Integer getUsuallyCC() {
		return usuallyCC;
	}
	public void setUsuallyCC(Integer usuallyCC) {
		this.usuallyCC = usuallyCC;
	}
	public Integer getSpcc() {
		return spcc;
	}
	public void setSpcc(Integer spcc) {
		this.spcc = spcc;
	}
	public Integer getSatSPCC() {
		return satSPCC;
	}
	public void setSatSPCC(Integer satSPCC) {
		this.satSPCC = satSPCC;
	}
	public Integer getOpcc() {
		return opcc;
	}
	public void setOpcc(Integer opcc) {
		this.opcc = opcc;
	}
	public Integer getCurAlertEnabled() {
		return curAlertEnabled;
	}
	public void setCurAlertEnabled(Integer curAlertEnabled) {
		this.curAlertEnabled = curAlertEnabled;
	}
	public BigDecimal getCurUpLimit() {
		return curUpLimit;
	}
	public void setCurUpLimit(BigDecimal curUpLimit) {
		this.curUpLimit = curUpLimit;
	}
	public BigDecimal getCurLoLimit() {
		return curLoLimit;
	}
	public void setCurLoLimit(BigDecimal curLoLimit) {
		this.curLoLimit = curLoLimit;
	}
	public Integer getVolAlertEnabled() {
		return volAlertEnabled;
	}
	public void setVolAlertEnabled(Integer volAlertEnabled) {
		this.volAlertEnabled = volAlertEnabled;
	}
	public Integer getVolAlertType() {
		return volAlertType;
	}
	public void setVolAlertType(Integer volAlertType) {
		this.volAlertType = volAlertType;
	}
	public BigDecimal getVolUpLimit() {
		return volUpLimit;
	}
	public void setVolUpLimit(BigDecimal volUpLimit) {
		this.volUpLimit = volUpLimit;
	}
	public BigDecimal getVolLoLimit() {
		return volLoLimit;
	}
	public void setVolLoLimit(BigDecimal volLoLimit) {
		this.volLoLimit = volLoLimit;
	}
	public Integer getEcAlertEnabled() {
		return ecAlertEnabled;
	}
	public void setEcAlertEnabled(Integer ecAlertEnabled) {
		this.ecAlertEnabled = ecAlertEnabled;
	}
	public BigDecimal getEcUpLimit() {
		return ecUpLimit;
	}
	public void setEcUpLimit(BigDecimal ecUpLimit) {
		this.ecUpLimit = ecUpLimit;
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
