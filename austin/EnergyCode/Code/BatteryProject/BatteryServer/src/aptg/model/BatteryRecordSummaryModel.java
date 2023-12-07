package aptg.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BatteryRecordSummaryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int STATUS_NORMAL 	= 1;	// 正常
	
	private String nbid;
	private int batteryID;
	private String recTime;
	private int uploadStamp;
	private int timeZone;
	private BigDecimal maxIR;
	private BigDecimal minIR;
	private BigDecimal maxVol;
	private BigDecimal minVol;
	private BigDecimal temperature;
	private int status;
	
	public BatteryRecordSummaryModel() {
		this.maxIR = BigDecimal.ZERO;
		this.minIR = BigDecimal.ZERO;
		this.maxVol = BigDecimal.ZERO;
		this.minVol = BigDecimal.ZERO;
		this.temperature = BigDecimal.ZERO;
		this.status = STATUS_NORMAL;
	}
	
	public String getNbid() {
		return nbid;
	}
	public void setNbid(String nbid) {
		this.nbid = nbid;
	}
	public int getBatteryID() {
		return batteryID;
	}
	public void setBatteryID(int batteryID) {
		this.batteryID = batteryID;
	}
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public int getUploadStamp() {
		return uploadStamp;
	}
	public void setUploadStamp(int uploadStamp) {
		this.uploadStamp = uploadStamp;
	}
	public int getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}
	public BigDecimal getMaxIR() {
		return maxIR;
	}
	public void setMaxIR(BigDecimal maxIR) {
		this.maxIR = maxIR;
	}
	public BigDecimal getMinIR() {
		return minIR;
	}
	public void setMinIR(BigDecimal minIR) {
		this.minIR = minIR;
	}
	public BigDecimal getMaxVol() {
		return maxVol;
	}
	public void setMaxVol(BigDecimal maxVol) {
		this.maxVol = maxVol;
	}
	public BigDecimal getMinVol() {
		return minVol;
	}
	public void setMinVol(BigDecimal minVol) {
		this.minVol = minVol;
	}
	public BigDecimal getTemperature() {
		return temperature;
	}
	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
