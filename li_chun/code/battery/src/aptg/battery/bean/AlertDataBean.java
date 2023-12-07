package aptg.battery.bean;

import java.util.List;

public class AlertDataBean {
	private List<AlertBean> Alert;
	private int IMPType;
	
	public List<AlertBean> getAlert() {
		return Alert;
	}
	public void setAlert(List<AlertBean> alert) {
		Alert = alert;
	}
	public int getIMPType() {
		return IMPType;
	}
	public void setIMPType(int iMPType) {
		IMPType = iMPType;
	}
	
	
}
