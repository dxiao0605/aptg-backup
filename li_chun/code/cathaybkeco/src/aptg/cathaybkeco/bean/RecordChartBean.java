package aptg.cathaybkeco.bean;

public class RecordChartBean {
	private BaseChartBean Power;
	private BaseChartBean Cur;
	private BaseChartBean Vol;
	private BaseChartBean VolP;	
	private BaseChartBean DF;
	
	public BaseChartBean getPower() {
		return Power;
	}
	public void setPower(BaseChartBean power) {
		Power = power;
	}
	public BaseChartBean getCur() {
		return Cur;
	}
	public void setCur(BaseChartBean cur) {
		Cur = cur;
	}
	public BaseChartBean getVol() {
		return Vol;
	}
	public void setVol(BaseChartBean vol) {
		Vol = vol;
	}
	public BaseChartBean getVolP() {
		return VolP;
	}
	public void setVolP(BaseChartBean volP) {
		VolP = volP;
	}
	public BaseChartBean getDF() {
		return DF;
	}
	public void setDF(BaseChartBean dF) {
		DF = dF;
	}
}
