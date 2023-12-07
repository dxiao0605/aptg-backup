package aptg.model;

import java.io.Serializable;

public class CommandModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String transactionID;
	private String taskID;
	private String nbID;
	private int batteryID;
	private String publishTime;
	private String ackTime;
	private String responseTime;
	private Integer responseCode;
	private String responseContent;
	
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getNbID() {
		return nbID;
	}
	public void setNbID(String nbID) {
		this.nbID = nbID;
	}
	public int getBatteryID() {
		return batteryID;
	}
	public void setBatteryID(int batteryID) {
		this.batteryID = batteryID;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getAckTime() {
		return ackTime;
	}
	public void setAckTime(String ackTime) {
		this.ackTime = ackTime;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
}
