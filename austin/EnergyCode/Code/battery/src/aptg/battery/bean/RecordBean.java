package aptg.battery.bean;

import java.math.BigDecimal;
import java.util.List;

public class RecordBean {
	private int Seq; 
	private String BatteryGroupID;
	private String RecTime;	
	private List<String> IR;
	private List<String> Vol;
	private BigDecimal Temperature;						
	private List<Integer> StatusCode;
	private List<String> StatusDesc;
	
	
	public int getSeq() {
		return Seq;
	}
	public void setSeq(int seq) {
		Seq = seq;
	}
	public String getBatteryGroupID() {
		return BatteryGroupID;
	}
	public void setBatteryGroupID(String batteryGroupID) {
		BatteryGroupID = batteryGroupID;
	}
	public String getRecTime() {
		return RecTime;
	}
	public void setRecTime(String recTime) {
		RecTime = recTime;
	}
	
	public List<String> getIR() {
		return IR;
	}
	public void setIR(List<String> iR) {
		IR = iR;
	}
	public List<String> getVol() {
		return Vol;
	}
	public void setVol(List<String> vol) {
		Vol = vol;
	}
	public BigDecimal getTemperature() {
		return Temperature;
	}
	public void setTemperature(BigDecimal temperature) {
		Temperature = temperature;
	}
	public List<Integer> getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(List<Integer> statusCode) {
		StatusCode = statusCode;
	}
	public List<String> getStatusDesc() {
		return StatusDesc;
	}
	public void setStatusDesc(List<String> statusDesc) {
		StatusDesc = statusDesc;
	}
}
