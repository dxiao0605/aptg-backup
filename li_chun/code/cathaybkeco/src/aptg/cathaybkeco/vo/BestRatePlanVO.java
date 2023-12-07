package aptg.cathaybkeco.vo;

import java.math.BigDecimal;
import java.util.List;


public class BestRatePlanVO {
	
	private String powerAccount;
	private String bankCode;
	private String startDate;
	private String endDate;
	private String bankName;
	private String ratePlanCode;
	private String inUse;
	private String inUseDesc;
	private List<String> bestRatePlanList;
	private String bestRatePlanDesc;
	private BigDecimal lampBTotal;
	private BigDecimal lampTotal;
	private BigDecimal lampE2Total;
	private BigDecimal lampE3Total;
	private BigDecimal lampS2Total;
	private BigDecimal lowNTotal;
	private BigDecimal low2Total;
	private BigDecimal high2Total;
	private BigDecimal high3Total;
	private BigDecimal lampBDiff;
	private BigDecimal lampDiff;
	private BigDecimal lampE2Diff;
	private BigDecimal lampE3Diff;
	private BigDecimal lampS2Diff;
	private BigDecimal lowNDiff;
	private BigDecimal low2Diff;
	private BigDecimal high2Diff;
	private BigDecimal high3Diff;
	private BigDecimal inUseTotal;
	private BigDecimal bestTotal;
	private BigDecimal bestDiff;
	private String useMonth;
	private String city;
	private String postCodeNo;
	private String bankCodeArr;
	
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getRatePlanCode() {
		return ratePlanCode;
	}
	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}
	public String getInUse() {
		return inUse;
	}
	public void setInUse(String inUse) {
		this.inUse = inUse;
	}
	public List<String> getBestRatePlanList() {
		return bestRatePlanList;
	}
	public void setBestRatePlanList(List<String> bestRatePlanList) {
		this.bestRatePlanList = bestRatePlanList;
	}
	public BigDecimal getLampBTotal() {
		return lampBTotal;
	}
	public void setLampBTotal(BigDecimal lampBTotal) {
		this.lampBTotal = lampBTotal;
	}
	public BigDecimal getLampTotal() {
		return lampTotal;
	}
	public void setLampTotal(BigDecimal lampTotal) {
		this.lampTotal = lampTotal;
	}
	public BigDecimal getLampE2Total() {
		return lampE2Total;
	}
	public void setLampE2Total(BigDecimal lampE2Total) {
		this.lampE2Total = lampE2Total;
	}
	public BigDecimal getLampE3Total() {
		return lampE3Total;
	}
	public void setLampE3Total(BigDecimal lampE3Total) {
		this.lampE3Total = lampE3Total;
	}
	public BigDecimal getLampS2Total() {
		return lampS2Total;
	}
	public void setLampS2Total(BigDecimal lampS2Total) {
		this.lampS2Total = lampS2Total;
	}
	public BigDecimal getLowNTotal() {
		return lowNTotal;
	}
	public void setLowNTotal(BigDecimal lowNTotal) {
		this.lowNTotal = lowNTotal;
	}
	public BigDecimal getLow2Total() {
		return low2Total;
	}
	public void setLow2Total(BigDecimal low2Total) {
		this.low2Total = low2Total;
	}
	public BigDecimal getHigh2Total() {
		return high2Total;
	}
	public void setHigh2Total(BigDecimal high2Total) {
		this.high2Total = high2Total;
	}
	public BigDecimal getHigh3Total() {
		return high3Total;
	}
	public void setHigh3Total(BigDecimal high3Total) {
		this.high3Total = high3Total;
	}
	public BigDecimal getLampBDiff() {
		return lampBDiff;
	}
	public void setLampBDiff(BigDecimal lampBDiff) {
		this.lampBDiff = lampBDiff;
	}
	public BigDecimal getLampDiff() {
		return lampDiff;
	}
	public void setLampDiff(BigDecimal lampDiff) {
		this.lampDiff = lampDiff;
	}
	public BigDecimal getLampE2Diff() {
		return lampE2Diff;
	}
	public void setLampE2Diff(BigDecimal lampE2Diff) {
		this.lampE2Diff = lampE2Diff;
	}
	public BigDecimal getLampE3Diff() {
		return lampE3Diff;
	}
	public void setLampE3Diff(BigDecimal lampE3Diff) {
		this.lampE3Diff = lampE3Diff;
	}
	public BigDecimal getLampS2Diff() {
		return lampS2Diff;
	}
	public void setLampS2Diff(BigDecimal lampS2Diff) {
		this.lampS2Diff = lampS2Diff;
	}
	public BigDecimal getLowNDiff() {
		return lowNDiff;
	}
	public void setLowNDiff(BigDecimal lowNDiff) {
		this.lowNDiff = lowNDiff;
	}
	public BigDecimal getLow2Diff() {
		return low2Diff;
	}
	public void setLow2Diff(BigDecimal low2Diff) {
		this.low2Diff = low2Diff;
	}
	public BigDecimal getHigh2Diff() {
		return high2Diff;
	}
	public void setHigh2Diff(BigDecimal high2Diff) {
		this.high2Diff = high2Diff;
	}
	public BigDecimal getHigh3Diff() {
		return high3Diff;
	}
	public void setHigh3Diff(BigDecimal high3Diff) {
		this.high3Diff = high3Diff;
	}
	public BigDecimal getInUseTotal() {
		return inUseTotal;
	}
	public void setInUseTotal(BigDecimal inUseTotal) {
		this.inUseTotal = inUseTotal;
	}
	public BigDecimal getBestTotal() {
		return bestTotal;
	}
	public void setBestTotal(BigDecimal bestTotal) {
		this.bestTotal = bestTotal;
	}
	public BigDecimal getBestDiff() {
		return bestDiff;
	}
	public void setBestDiff(BigDecimal bestDiff) {
		this.bestDiff = bestDiff;
	}
	public String getUseMonth() {
		return useMonth;
	}
	public void setUseMonth(String useMonth) {
		this.useMonth = useMonth;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCodeNo() {
		return postCodeNo;
	}
	public void setPostCodeNo(String postCodeNo) {
		this.postCodeNo = postCodeNo;
	}
	
	public String getInUseDesc() {
		return inUseDesc;
	}
	public void setInUseDesc(String inUseDesc) {
		this.inUseDesc = inUseDesc;
	}
	public String getBestRatePlanDesc() {
		return bestRatePlanDesc;
	}
	public void setBestRatePlanDesc(String bestRatePlanDesc) {
		this.bestRatePlanDesc = bestRatePlanDesc;
	}
	public String getBankCodeArr() {
		return bankCodeArr;
	}
	public void setBankCodeArr(String bankCodeArr) {
		this.bankCodeArr = bankCodeArr;
	}
	
}
