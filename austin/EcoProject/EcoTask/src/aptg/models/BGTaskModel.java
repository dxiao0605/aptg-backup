package aptg.models;

import java.io.Serializable;

/**
 *	後台工作清單
 * 
 * @author austinchen
 *
 */
public class BGTaskModel implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private int seqno;
	private String BGTaskType;
	private String PowerAccountOld;
	private String PowerAccountNew;
	private int StatusCode; 	// 0 待處理，1 處理中，2 處理完成，3 取消
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getBGTaskType() {
		return BGTaskType;
	}
	public void setBGTaskType(String bGTaskType) {
		BGTaskType = bGTaskType;
	}
	public String getPowerAccountOld() {
		return PowerAccountOld;
	}
	public void setPowerAccountOld(String powerAccountOld) {
		PowerAccountOld = powerAccountOld;
	}
	public String getPowerAccountNew() {
		return PowerAccountNew;
	}
	public void setPowerAccountNew(String powerAccountNew) {
		PowerAccountNew = powerAccountNew;
	}
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}

}
