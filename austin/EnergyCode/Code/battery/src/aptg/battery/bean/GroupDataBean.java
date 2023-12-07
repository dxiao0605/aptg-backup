package aptg.battery.bean;

import java.util.List;

public class GroupDataBean {
	private List<GroupBean> Battery;
	private int IMPType;
	
	public List<GroupBean> getBattery() {
		return Battery;
	}

	public void setBattery(List<GroupBean> battery) {
		Battery = battery;
	}
	public int getIMPType() {
		return IMPType;
	}

	public void setIMPType(int iMPType) {
		IMPType = iMPType;
	}	
}
