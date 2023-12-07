package aptg.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChgConfBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String TaskID;
	private int CompanyCode;
	private int IMPType;
	private BigDecimal Alert1;
	private BigDecimal Alert2;
	private Integer Disconnect;
	private Integer Temperature1;
	
	public String getTaskID() {
		return TaskID;
	}
	public void setTaskID(String taskID) {
		TaskID = taskID;
	}
	public int getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(int companyCode) {
		CompanyCode = companyCode;
	}
	public int getIMPType() {
		return IMPType;
	}
	public void setIMPType(int iMPType) {
		IMPType = iMPType;
	}
	public BigDecimal getAlert1() {
		return Alert1;
	}
	public void setAlert1(BigDecimal alert1) {
		Alert1 = alert1;
	}
	public BigDecimal getAlert2() {
		return Alert2;
	}
	public void setAlert2(BigDecimal alert2) {
		Alert2 = alert2;
	}
	public Integer getDisconnect() {
		return Disconnect;
	}
	public void setDisconnect(Integer disconnect) {
		Disconnect = disconnect;
	}
	public Integer getTemperature1() {
		return Temperature1;
	}
	public void setTemperature1(Integer temperature1) {
		Temperature1 = temperature1;
	}
}
