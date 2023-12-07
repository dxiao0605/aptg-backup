package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class ElectricityTimeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int Summer;
	private int DayOfWeek;
	private BigDecimal UsuallyHour;
	private BigDecimal SPHour;
	private BigDecimal SatSPHour;
	private BigDecimal OPHour;
	
	public int getSummer() {
		return Summer;
	}
	public void setSummer(int summer) {
		Summer = summer;
	}
	public int getDayOfWeek() {
		return DayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		DayOfWeek = dayOfWeek;
	}
	public BigDecimal getUsuallyHour() {
		return UsuallyHour;
	}
	public void setUsuallyHour(BigDecimal usuallyHour) {
		UsuallyHour = usuallyHour;
	}
	public BigDecimal getSPHour() {
		return SPHour;
	}
	public void setSPHour(BigDecimal sPHour) {
		SPHour = sPHour;
	}
	public BigDecimal getSatSPHour() {
		return SatSPHour;
	}
	public void setSatSPHour(BigDecimal satSPHour) {
		SatSPHour = satSPHour;
	}
	public BigDecimal getOPHour() {
		return OPHour;
	}
	public void setOPHour(BigDecimal oPHour) {
		OPHour = oPHour;
	}
}
