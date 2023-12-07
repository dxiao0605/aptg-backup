package aptg.battery.bean;

import java.util.List;

public class BatteryDataBean {
	private List<BatteryBean> Battery;
	private int IMPType;
	private String GroupID;
	
	public List<BatteryBean> getBattery() {
		return Battery;
	}
	public void setBattery(List<BatteryBean> battery) {
		Battery = battery;
	}
	public int getIMPType() {
		return IMPType;
	}
	public void setIMPType(int iMPType) {
		IMPType = iMPType;
	}
	public String getGroupID() {
		return GroupID;
	}
	public void setGroupID(String groupID) {
		GroupID = groupID;
	}
	
}
