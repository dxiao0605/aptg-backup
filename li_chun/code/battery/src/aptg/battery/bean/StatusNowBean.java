package aptg.battery.bean;

import java.util.List;

public class StatusNowBean {
	private List<String> Status;
	private List<Integer> Count;
	
	public List<String> getStatus() {
		return Status;
	}
	public void setStatus(List<String> status) {
		Status = status;
	}
	public List<Integer> getCount() {
		return Count;
	}
	public void setCount(List<Integer> count) {
		Count = count;
	}
}
