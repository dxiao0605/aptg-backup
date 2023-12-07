package aptg.battery.bean;

import java.util.List;

public class StatusBean {
	private List<String> RecDate;
	private List<Integer> Status1;
	private List<Integer> Status2;
	private List<Integer> Status3;
	private List<Integer> Status4;
//	private List<Integer> Status23;
	private List<String> Label;
	private List<String> LabelE;
	private List<String> LabelJ;
	
	public List<String> getRecDate() {
		return RecDate;
	}
	public void setRecDate(List<String> recDate) {
		RecDate = recDate;
	}
	public List<Integer> getStatus1() {
		return Status1;
	}
	public void setStatus1(List<Integer> status1) {
		Status1 = status1;
	}
	public List<Integer> getStatus2() {
		return Status2;
	}
	public void setStatus2(List<Integer> status2) {
		Status2 = status2;
	}
	public List<Integer> getStatus3() {
		return Status3;
	}
	public void setStatus3(List<Integer> status3) {
		Status3 = status3;
	}
	public List<Integer> getStatus4() {
		return Status4;
	}
	public void setStatus4(List<Integer> status4) {
		Status4 = status4;
	}
	
//	public List<Integer> getStatus23() {
//		return Status23;
//	}
//	public void setStatus23(List<Integer> status23) {
//		Status23 = status23;
//	}
	public List<String> getLabel() {
		return Label;
	}
	public void setLabel(List<String> label) {
		Label = label;
	}
	public List<String> getLabelE() {
		return LabelE;
	}
	public void setLabelE(List<String> labelE) {
		LabelE = labelE;
	}
	public List<String> getLabelJ() {
		return LabelJ;
	}
	public void setLabelJ(List<String> labelJ) {
		LabelJ = labelJ;
	}
	
}
