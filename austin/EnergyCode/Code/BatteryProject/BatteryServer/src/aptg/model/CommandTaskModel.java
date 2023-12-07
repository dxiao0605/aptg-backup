package aptg.model;

import java.io.Serializable;

public class CommandTaskModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taskID;
	private Integer groupInternalID;
	private String nbID;
	private int batteryID;
	private String commandID;
	private int taskStatus;
	private String reqTime;
	private String config;
	private String hexConfig;
	
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public Integer getGroupInternalID() {
		return groupInternalID;
	}
	public void setGroupInternalID(Integer groupInternalID) {
		this.groupInternalID = groupInternalID;
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
	public String getCommandID() {
		return commandID;
	}
	public void setCommandID(String commandID) {
		this.commandID = commandID;
	}
	public int getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getHexConfig() {
		return hexConfig;
	}
	public void setHexConfig(String hexConfig) {
		this.hexConfig = hexConfig;
	}
}
