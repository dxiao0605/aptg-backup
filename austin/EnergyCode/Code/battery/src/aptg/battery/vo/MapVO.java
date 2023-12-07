package aptg.battery.vo;

import java.math.BigDecimal;

public class MapVO {
	private String country;
	private String area;
	private BigDecimal maxLng;
	private BigDecimal minLng;
	private BigDecimal maxLat;
	private BigDecimal minLat;
	private BigDecimal count;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public BigDecimal getMaxLng() {
		return maxLng;
	}
	public void setMaxLng(BigDecimal maxLng) {
		this.maxLng = maxLng;
	}
	public BigDecimal getMinLng() {
		return minLng;
	}
	public void setMinLng(BigDecimal minLng) {
		this.minLng = minLng;
	}
	public BigDecimal getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(BigDecimal maxLat) {
		this.maxLat = maxLat;
	}
	public BigDecimal getMinLat() {
		return minLat;
	}
	public void setMinLat(BigDecimal minLat) {
		this.minLat = minLat;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}	
	
}
