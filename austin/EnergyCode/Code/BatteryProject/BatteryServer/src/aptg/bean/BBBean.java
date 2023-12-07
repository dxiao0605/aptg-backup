package aptg.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class BBBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int IRTestTime;
	private int BatteryCapacity;
	private int CorrectionValue;
	private BigDecimal Resistance;
	
	public int getIRTestTime() {
		return IRTestTime;
	}
	public void setIRTestTime(int iRTestTime) {
		IRTestTime = iRTestTime;
	}
	public int getBatteryCapacity() {
		return BatteryCapacity;
	}
	public void setBatteryCapacity(int batteryCapacity) {
		BatteryCapacity = batteryCapacity;
	}
	public int getCorrectionValue() {
		return CorrectionValue;
	}
	public void setCorrectionValue(int correctionValue) {
		CorrectionValue = correctionValue;
	}
	public BigDecimal getResistance() {
		return Resistance;
	}
	public void setResistance(BigDecimal resistance) {
		Resistance = resistance;
	}
}
