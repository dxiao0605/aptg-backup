package tw.com.aptg.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the sms_draft database table.
 * 
 */

public class SmsDraftBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int sId;


	private Timestamp cDate;
	private String msgContent;
	private String status;
	private Timestamp uDate;
	private String msisdn;
	private String contract;
	private String pid;
	
	
	
	public SmsDraftBean() {
	}

	public int getSId() {
		return this.sId;
	}

	public void setSId(int sId) {
		this.sId = sId;
	}

	
	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	
	public Timestamp getCDate() {
		return this.cDate;
	}

	public void setCDate(Timestamp cDate) {
		this.cDate = cDate;
	}

	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUDate() {
		return this.uDate;
	}

	public void setUDate(Timestamp uDate) {
		this.uDate = uDate;
	}
	
	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public String getContract() {
		return this.contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

}