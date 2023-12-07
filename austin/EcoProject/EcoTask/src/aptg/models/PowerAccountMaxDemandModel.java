package aptg.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class PowerAccountMaxDemandModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PowerAccount;
	private String RecTime;
	private BigDecimal TotalDemand;
	
	public PowerAccountMaxDemandModel() {
		this.TotalDemand = BigDecimal.ZERO;
	}
	
	public String getPowerAccount() {
		return PowerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		PowerAccount = powerAccount;
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
}
