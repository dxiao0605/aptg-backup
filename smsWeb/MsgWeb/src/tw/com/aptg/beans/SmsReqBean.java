package tw.com.aptg.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the sms_req database table.
 * 
 */

public class SmsReqBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int sId;
	private Timestamp cDate;
	private String msgContent;
	private String msisdnLst;
	private String sender;
	private String timeSpec;
	private Timestamp uDate;
	private String contract;
	private String status;
	private String sender_id;
	
	
	
	public SmsReqBean() {
	}
	
	public String getSender_id() {
		return this.sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	
	
	

	public int getSId() {
		return this.sId;
	}

	public void setSId(int sId) {
		this.sId = sId;
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

	public String getMsisdnLst() {
		return this.msisdnLst;
	}

	public void setMsisdnLst(String msisdnLst) {
		this.msisdnLst = msisdnLst;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTimeSpec() {
		return this.timeSpec;
	}

	public void setTimeSpec(String timeSpec) {
		this.timeSpec = timeSpec;
	}

	public Timestamp getUDate() {
		return this.uDate;
	}

	public void setUDate(Timestamp uDate) {
		this.uDate = uDate;
	}

	public String getContract() {
		return this.contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}