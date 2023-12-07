package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class ElectricityTimeDailyModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String RecDate;
	private BigDecimal UsuallyHour;
	private BigDecimal SPHour;
	private BigDecimal SatSPHour;
	private BigDecimal OPHour;
	
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
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
