package aptg.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BatteryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbID;
	private int batteryID;
	
	private String installDate;
	private Integer batteryTypeCode;
	private Integer irTestTime;
	private Integer batteryCapacity;
	private Integer correctionValue;
	private BigDecimal resistance;
	private Integer uploadCycle;
	private Integer irCycle;
	private Integer communicationCycle;
	private Integer irRecords;
	private Integer vRecords;
	private Integer tRecords;
	
	public BatteryModel() {}
	
	public BatteryModel(String nbID, int batteryID) {
		this.nbID = nbID;
		this.batteryID = batteryID;
		this.irRecords = 0;
		this.vRecords = 0;
		this.tRecords = 0;
	}
	
	public String getNbID() {
		return nbID;
	}
	public void setNbID(String nbID) {
		this.nbID = nbID;
	}
	public int getBatteryID() {
		return batteryID;
	}
	public void setBatteryID(int batteryID) {
		this.batteryID = batteryID;
	}
	public String getInstallDate() {
		return installDate;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	public Integer getBatteryTypeCode() {
		return batteryTypeCode;
	}
	public void setBatteryTypeCode(Integer batteryTypeCode) {
		this.batteryTypeCode = batteryTypeCode;
	}
	public Integer getIrTestTime() {
		return irTestTime;
	}
	public void setIrTestTime(Integer irTestTime) {
		this.irTestTime = irTestTime;
	}
	public Integer getBatteryCapacity() {
		return batteryCapacity;
	}
	public void setBatteryCapacity(Integer batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}
	public Integer getCorrectionValue() {
		return correctionValue;
	}
	public void setCorrectionValue(Integer correctionValue) {
		this.correctionValue = correctionValue;
	}
	public BigDecimal getResistance() {
		return resistance;
	}
	public void setResistance(BigDecimal resistance) {
		this.resistance = resistance;
	}
	public Integer getUploadCycle() {
		return uploadCycle;
	}
	public void setUploadCycle(Integer uploadCycle) {
		this.uploadCycle = uploadCycle;
	}
	public Integer getIrCycle() {
		return irCycle;
	}
	public void setIrCycle(Integer irCycle) {
		this.irCycle = irCycle;
	}
	public Integer getCommunicationCycle() {
		return communicationCycle;
	}
	public void setCommunicationCycle(Integer communicationCycle) {
		this.communicationCycle = communicationCycle;
	}
	public Integer getIrRecords() {
		return irRecords;
	}
	public void setIrRecords(Integer irRecords) {
		this.irRecords = irRecords;
	}
	public Integer getvRecords() {
		return vRecords;
	}
	public void setvRecords(Integer vRecords) {
		this.vRecords = vRecords;
	}
	public Integer gettRecords() {
		return tRecords;
	}
	public void settRecords(Integer tRecords) {
		this.tRecords = tRecords;
	}
}
