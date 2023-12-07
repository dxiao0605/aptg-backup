package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class KPIModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal UnitPriceKPIavg;
	private BigDecimal EUIKPIavg;
	private BigDecimal EPUIKPIavg;
	private BigDecimal AirKPIavg;
	
	public BigDecimal getUnitPriceKPIavg() {
		return UnitPriceKPIavg;
	}
	public void setUnitPriceKPIavg(BigDecimal unitPriceKPIavg) {
		UnitPriceKPIavg = unitPriceKPIavg;
	}
	public BigDecimal getEUIKPIavg() {
		return EUIKPIavg;
	}
	public void setEUIKPIavg(BigDecimal eUIKPIavg) {
		EUIKPIavg = eUIKPIavg;
	}
	public BigDecimal getEPUIKPIavg() {
		return EPUIKPIavg;
	}
	public void setEPUIKPIavg(BigDecimal ePUIKPIavg) {
		EPUIKPIavg = ePUIKPIavg;
	}
	public BigDecimal getAirKPIavg() {
		return AirKPIavg;
	}
	public void setAirKPIavg(BigDecimal airKPIavg) {
		AirKPIavg = airKPIavg;
	}
}
