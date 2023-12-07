package aptg.cathaybkeco.bean;

public class RecordBean {
	
	private BaseBean Cur;
	private BaseBean Vol;
	private BaseBean VolP;	
	private BaseBean Power;
	private BaseBean DF;
	private BaseBean Demand;
	private BaseBean ECO5;
	private BaseBean Other;
	
	public BaseBean getCur() {
		return Cur;
	}
	public void setCur(BaseBean cur) {
		Cur = cur;
	}
	public BaseBean getVol() {
		return Vol;
	}
	public void setVol(BaseBean vol) {
		Vol = vol;
	}
	public BaseBean getVolP() {
		return VolP;
	}
	public void setVolP(BaseBean volP) {
		VolP = volP;
	}
	public BaseBean getPower() {
		return Power;
	}
	public void setPower(BaseBean power) {
		Power = power;
	}
	public BaseBean getDF() {
		return DF;
	}
	public void setDF(BaseBean dF) {
		DF = dF;
	}
	public BaseBean getDemand() {
		return Demand;
	}
	public void setDemand(BaseBean demand) {
		Demand = demand;
	}
	public BaseBean getECO5() {
		return ECO5;
	}
	public void setECO5(BaseBean eCO5) {
		ECO5 = eCO5;
	}
	public BaseBean getOther() {
		return Other;
	}
	public void setOther(BaseBean other) {
		Other = other;
	}
}
