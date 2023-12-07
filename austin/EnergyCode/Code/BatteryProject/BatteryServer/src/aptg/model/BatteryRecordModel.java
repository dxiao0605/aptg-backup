package aptg.model;

import java.io.Serializable;
import java.util.List;

public class BatteryRecordModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbID;
	private int batteryID;
	private String recTime;
	private int uploadStamp;
	private int timeZone;
	private List<BatteryRecordDataModel> iList;
	private List<BatteryRecordDataModel> vList;
	private List<BatteryRecordDataModel> tList;
	
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
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public int getUploadStamp() {
		return uploadStamp;
	}
	public void setUploadStamp(int uploadStamp) {
		this.uploadStamp = uploadStamp;
	}
	public int getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}
	public List<BatteryRecordDataModel> getiList() {
		return iList;
	}
	public void setiList(List<BatteryRecordDataModel> iList) {
		this.iList = iList;
	}
	public List<BatteryRecordDataModel> getvList() {
		return vList;
	}
	public void setvList(List<BatteryRecordDataModel> vList) {
		this.vList = vList;
	}
	public List<BatteryRecordDataModel> gettList() {
		return tList;
	}
	public void settList(List<BatteryRecordDataModel> tList) {
		this.tList = tList;
	}
}
