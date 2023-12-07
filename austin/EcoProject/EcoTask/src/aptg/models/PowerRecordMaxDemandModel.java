package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerRecordMaxDemandModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String DeviceID;
	private String RecTime;
	
	/*
	 * version 1
	 */
	private BigDecimal TotalDemand;
	
	/*
	 * version 2
	 */
	private BigDecimal MaxDemand;
	
	public PowerRecordMaxDemandModel() {
		this.TotalDemand = BigDecimal.ZERO;
		
		this.MaxDemand = BigDecimal.ZERO;
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
	
	public BigDecimal getTotalDemand() {
		return TotalDemand;
	}
	public void setTotalDemand(BigDecimal totalDemand) {
		TotalDemand = totalDemand;
	}

	public BigDecimal getMaxDemand() {
		return MaxDemand;
	}
	public void setMaxDemand(BigDecimal maxDemand) {
		MaxDemand = maxDemand;
	}
}
