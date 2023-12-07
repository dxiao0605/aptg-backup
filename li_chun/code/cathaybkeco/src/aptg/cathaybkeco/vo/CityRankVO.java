package aptg.cathaybkeco.vo;

import java.math.BigDecimal;

public class CityRankVO {
	private BigDecimal area;
	private BigDecimal people;
	private BigDecimal mcecpk;
	private BigDecimal mcecsp;
	private BigDecimal mcecsatsp;
	private BigDecimal mcecop;
	private BigDecimal cecSum = BigDecimal.ZERO;
	
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public BigDecimal getPeople() {
		return people;
	}
	public void setPeople(BigDecimal people) {
		this.people = people;
	}
	public BigDecimal getMcecpk() {
		return mcecpk;
	}
	public void setMcecpk(BigDecimal mcecpk) {
		this.mcecpk = mcecpk;
	}
	public BigDecimal getMcecsp() {
		return mcecsp;
	}
	public void setMcecsp(BigDecimal mcecsp) {
		this.mcecsp = mcecsp;
	}
	public BigDecimal getMcecsatsp() {
		return mcecsatsp;
	}
	public void setMcecsatsp(BigDecimal mcecsatsp) {
		this.mcecsatsp = mcecsatsp;
	}
	public BigDecimal getMcecop() {
		return mcecop;
	}
	public void setMcecop(BigDecimal mcecop) {
		this.mcecop = mcecop;
	}
	public BigDecimal getCecSum() {
		return cecSum;
	}
	public void setCecSum(BigDecimal cecSum) {
		this.cecSum = cecSum;
	}
}
