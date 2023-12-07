package aptg.cathaybkeco.vo;

import java.math.BigDecimal;
import java.util.List;

public class EntryBillVO {
	private String powerAccount;
	private String billMon;
	private String billStartDay;
	private String billEndDay;
	private String baseCharge;
	private String usageCharge;
	private String overCharge;
	private String shareCharge;
	private String pfCharge;
	private String totalCharge;
	private String maxDemandPK;
	private String maxDemandSP;
	private String maxDemandSatSP;
	private String maxDemandOP;
	private String cecPK;
	private String cecSP;
	private String cecSatSP;
	private String cecOP;
	private String pf;
	private String startBillMon;
	private String endBillMon;
	private String showCharge;
	private String showCEC;
	private String oldBillMon;
	private String userName;
	private BigDecimal cec;
	
	private boolean error;
	private String code;
	private String description;
	private List<EntryBillVO> dataList;
	
	public String getPowerAccount() {
		return powerAccount;
	}
	public void setPowerAccount(String powerAccount) {
		this.powerAccount = powerAccount;
	}
	public String getBillMon() {
		return billMon;
	}
	public void setBillMon(String billMon) {
		this.billMon = billMon;
	}
	public String getBillStartDay() {
		return billStartDay;
	}
	public void setBillStartDay(String billStartDay) {
		this.billStartDay = billStartDay;
	}
	public String getBillEndDay() {
		return billEndDay;
	}
	public void setBillEndDay(String billEndDay) {
		this.billEndDay = billEndDay;
	}
	public String getBaseCharge() {
		return baseCharge;
	}
	public void setBaseCharge(String baseCharge) {
		this.baseCharge = baseCharge;
	}
	public String getUsageCharge() {
		return usageCharge;
	}
	public void setUsageCharge(String usageCharge) {
		this.usageCharge = usageCharge;
	}
	public String getOverCharge() {
		return overCharge;
	}
	public void setOverCharge(String overCharge) {
		this.overCharge = overCharge;
	}
	public String getShareCharge() {
		return shareCharge;
	}
	public void setShareCharge(String shareCharge) {
		this.shareCharge = shareCharge;
	}
	public String getPfCharge() {
		return pfCharge;
	}
	public void setPfCharge(String pfCharge) {
		this.pfCharge = pfCharge;
	}
	public String getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(String totalCharge) {
		this.totalCharge = totalCharge;
	}
	public String getMaxDemandPK() {
		return maxDemandPK;
	}
	public void setMaxDemandPK(String maxDemandPK) {
		this.maxDemandPK = maxDemandPK;
	}
	public String getMaxDemandSP() {
		return maxDemandSP;
	}
	public void setMaxDemandSP(String maxDemandSP) {
		this.maxDemandSP = maxDemandSP;
	}
	public String getMaxDemandSatSP() {
		return maxDemandSatSP;
	}
	public void setMaxDemandSatSP(String maxDemandSatSP) {
		this.maxDemandSatSP = maxDemandSatSP;
	}
	public String getMaxDemandOP() {
		return maxDemandOP;
	}
	public void setMaxDemandOP(String maxDemandOP) {
		this.maxDemandOP = maxDemandOP;
	}
	public String getCecPK() {
		return cecPK;
	}
	public void setCecPK(String cecPK) {
		this.cecPK = cecPK;
	}
	public String getCecSP() {
		return cecSP;
	}
	public void setCecSP(String cecSP) {
		this.cecSP = cecSP;
	}
	public String getCecSatSP() {
		return cecSatSP;
	}
	public void setCecSatSP(String cecSatSP) {
		this.cecSatSP = cecSatSP;
	}
	public String getCecOP() {
		return cecOP;
	}
	public void setCecOP(String cecOP) {
		this.cecOP = cecOP;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getStartBillMon() {
		return startBillMon;
	}
	public void setStartBillMon(String startBillMon) {
		this.startBillMon = startBillMon;
	}
	public String getEndBillMon() {
		return endBillMon;
	}
	public void setEndBillMon(String endBillMon) {
		this.endBillMon = endBillMon;
	}
	public String getShowCharge() {
		return showCharge;
	}
	public void setShowCharge(String showCharge) {
		this.showCharge = showCharge;
	}
	public String getShowCEC() {
		return showCEC;
	}
	public void setShowCEC(String showCEC) {
		this.showCEC = showCEC;
	}
	public String getOldBillMon() {
		return oldBillMon;
	}
	public void setOldBillMon(String oldBillMon) {
		this.oldBillMon = oldBillMon;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getCec() {
		return cec;
	}
	public void setCec(BigDecimal cec) {
		this.cec = cec;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<EntryBillVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<EntryBillVO> dataList) {
		this.dataList = dataList;
	}
}
