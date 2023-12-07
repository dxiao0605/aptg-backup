package aptg.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CompanyModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int companyCode;
	private String companyName;
	private int language;
	private int impType;	// 0: 內阻值, 1:電導值, 2:毫內阻, default 0, 畫面呈顯及健康度判斷
	private BigDecimal alert1;
	private BigDecimal alert2;
	private long disconnect;
	private String logoPath;
	private int admin;
	private int temperature1;
	
	public int getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(int companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public int getImpType() {
		return impType;
	}
	public void setImpType(int impType) {
		this.impType = impType;
	}
	public BigDecimal getAlert1() {
		return alert1;
	}
	public void setAlert1(BigDecimal alert1) {
		this.alert1 = alert1;
	}
	public BigDecimal getAlert2() {
		return alert2;
	}
	public void setAlert2(BigDecimal alert2) {
		this.alert2 = alert2;
	}
	public long getDisconnect() {
		return disconnect;
	}
	public void setDisconnect(long disconnect) {
		this.disconnect = disconnect;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public int getTemperature1() {
		return temperature1;
	}
	public void setTemperature1(int temperature1) {
		this.temperature1 = temperature1;
	}
}
