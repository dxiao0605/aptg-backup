package aptg.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aptg.models.PowerRecordModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.ToolUtil;

public class PowerRecordManager {

	private Map<String, Date> firstRecordMap = new HashMap<>();
	private Map<String, Date> firstRecordPoweraccountMap = new HashMap<>();
	
	private static PowerRecordManager instances;
	
	public static PowerRecordManager getInstance() {
		if (instances==null) {
			synchronized (PowerRecordManager.class) {
				if (instances==null) {
					instances = new PowerRecordManager();
				}
			}
		}
		return instances;
	}
	
	/**
	 * 
	 * @param deviceID
	 * @param recTime: yyyy-MM-dd
	 */
	public void setFirstPowerRecord(String deviceID, Date recTime) {
		firstRecordMap.put(deviceID, recTime);
	}
	public Date getFirstRecord(String deviceID) {
		if (firstRecordMap.containsKey(deviceID)) {
			return firstRecordMap.get(deviceID);
		} 
		return null;
	}
	
	public void setFirstPowerAccountRecord(String powerAccount, Date recTime) {
		firstRecordPoweraccountMap.put(powerAccount, recTime);
	}
	public Date getFirstPowerAccountRecord(String powerAccount) {
		if (firstRecordPoweraccountMap.containsKey(powerAccount)) {
			return firstRecordPoweraccountMap.get(powerAccount);
		} 
		return null;
	}
	
	public PowerRecordModel getSpecifyDateSecondRecord(String deviceID, Date date) {
		String startdate = getStartdate(date);
		String enddate = getEnddate(date);
		PowerRecordModel specifyRecord = DBQueryUtil.getInstance().getSpecifyDateSecondRecord(deviceID, startdate, enddate);
		return specifyRecord;
	}
	public PowerRecordModel getSpecifyDateLastRecord(String deviceID, Date date) {
		String startdate = getStartdate(date);
		String enddate = getEnddate(date);
		PowerRecordModel specifyRecord = DBQueryUtil.getInstance().getSpecifyDateLastRecord(deviceID, startdate, enddate);
		return specifyRecord;
	}
	
	private String getStartdate(Date calculateDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(calculateDate);
		String startdate = ToolUtil.getInstance().convertDateToString(start.getTime(), "yyyy-MM-dd");
		return startdate;
	}
	private String getEnddate(Date calculateDate) {
		Calendar end = Calendar.getInstance();
		end.setTime(calculateDate);
		end.add(Calendar.DATE, 1);
		String enddate = ToolUtil.getInstance().convertDateToString(end.getTime(), "yyyy-MM-dd");
		return enddate;
	}
}
