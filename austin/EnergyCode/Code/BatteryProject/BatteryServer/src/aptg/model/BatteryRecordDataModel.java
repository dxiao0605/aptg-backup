package aptg.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BatteryRecordDataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int category;
	private int orderNo;
	private BigDecimal value;
	private Integer status;
	
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
