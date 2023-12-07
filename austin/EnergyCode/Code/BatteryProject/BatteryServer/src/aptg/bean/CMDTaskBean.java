package aptg.bean;

import java.io.Serializable;

public class CMDTaskBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String TaskID;
	private Integer GroupInternalID;
	private String NBID;
	private Integer BatteryID;
	private String CommandID;
	private String HexConfig;
	
	public String getTaskID() {
		return TaskID;
	}
	public void setTaskID(String taskID) {
		TaskID = taskID;
	}
	public Integer getGroupInternalID() {
		return GroupInternalID;
	}
	public void setGroupInternalID(Integer groupInternalID) {
		GroupInternalID = groupInternalID;
	}
	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
	public Integer getBatteryID() {
		return BatteryID;
	}
	public void setBatteryID(Integer batteryID) {
		BatteryID = batteryID;
	}
	public String getCommandID() {
		return CommandID;
	}
	public void setCommandID(String commandID) {
		CommandID = commandID;
	}
	public String getHexConfig() {
		return HexConfig;
	}
	public void setHexConfig(String hexConfig) {
		HexConfig = hexConfig;
	}
}
