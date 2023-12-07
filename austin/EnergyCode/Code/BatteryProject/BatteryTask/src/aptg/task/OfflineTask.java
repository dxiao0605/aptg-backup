package aptg.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.CompanyDisconnectBean;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.EventModel;
import aptg.model.NBListModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 每小時執行斷線事件
 * 
 * @author austinchen
 *
 */
public class OfflineTask {

	private static final String CLASS_NAME = OfflineTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	private static final int Offline = 4;
	// 預設Group
	private static final int DEFAULT_GROUPID = 0;	// 為公司的預設站台
	
	private List<EventModel> insertList = new ArrayList<>();
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME + " Start!");
		
		OfflineTask task = new OfflineTask();
		task.procedure();
		task.insertOfflineEvent();
		
		logger.info(CLASS_NAME + " End!");
	}
	
	public void procedure() {
		// company disconnect時間設定
//		Map<String, CompanyDisconnectBean> disconnectMap = DBQueryUtil.getInstance().queryCompanyDisconnect();	// key: nbID
		Map<String, CompanyDisconnectBean> disconnectMap = DBQueryUtil.getInstance().queryCompanyDisconnectGroupHis();	// key: nbID
		// 通訊板最新資料
		List<BatteryRecordSummaryModel> list = getBatteryRecordSummary();
		// 離線未解決事件
		Map<String, EventModel> eventMap = getUnsolvedOfflineEvent();	// key: nbID
		
		// 啟用中的NBList
		Map<String, NBListModel> activeMap = getActive13NBList();

		Date now = new Date();
		for (BatteryRecordSummaryModel br: list) {
			String nbID = br.getNbid();
			int batteryID = br.getBatteryID();
			Date createTime = br.getCreatetime();
			
			// NBID active!=13(啟用) => 不做離線判斷
			if (!activeMap.containsKey(nbID))
				continue;
			
			// 已有離線事件(且未解決)紀錄 => 略過
			String key = nbID+"_"+batteryID;
			if (eventMap.containsKey(key))
				continue;
			
			// 查找此nbID判定離線的時間間距值
			CompanyDisconnectBean cd = disconnectMap.get(nbID);
			if (cd==null) {
				logger.error("Can't find NBID=["+nbID+"] Disconnect Value");
				continue;
			}

			/*
			 *	任何公司包含RCE 預設站台內的電池組ID排除所有告警判斷
			 * (預設站台的電池組不會產生任何未解決告警 => 若預設站台的電池組 電池狀態異常，只能從電池數據、電池歷史中看到)
			 */
			if (cd.getDefaultGroup()!=DEFAULT_GROUPID) {	// defaultGroup!=0
				long disconnect = cd.getDisconnect();
				if (disconnect==0) {	// 值為0 => 不做離線判斷
					logger.info("NBID=["+nbID+"], BatteryID=["+batteryID+"], Company Disconnect=["+disconnect+"] => 不做離線判斷! ");
					continue;
				}
				
				Date startTime = ToolUtil.getInstance().convertStringToDate(cd.getStartTime(), "yyyy-MM-dd HH:mm:ss");
				Date endTime = ToolUtil.getInstance().convertStringToDate(cd.getEndTime(), "yyyy-MM-dd HH:mm:ss");

				// createTime需在NBGroupHis區間，即createTime>=startTime && createTime<endTime
				// => createTime<startTime || createTime>=endTime => continue
				if (createTime.compareTo(startTime)==-1 || createTime.compareTo(endTime)!=-1)
					continue;
				
				/*
				 * ================== compare start ====================
				 */
				logger.info("NBID=["+nbID+"], BatteryID=["+batteryID+"], 實際收Record時間=["+ToolUtil.getInstance().convertDateToString(createTime, "yyyy-MM-dd HH:mm:ss")+"]");
				long diff = (long) (now.getTime()-createTime.getTime()) / 1000;
				logger.info("NBID=["+nbID+"], BatteryID=["+batteryID+"], 實際收Record時間=["+ToolUtil.getInstance().convertDateToString(createTime, "yyyy-MM-dd HH:mm:ss")+"], Time interval: "+diff+"(sec), Company Disconnect=["+disconnect+"]");
				/*
				 * ================== compare end ====================
				 */
				
				if (diff>disconnect) {
					// add Event
					EventModel event = new EventModel();
					event.setNbID(nbID);
					event.setBatteryID(br.getBatteryID());
					event.setRecordTime(br.getRecTime());
					event.setEventType(Offline);
					event.setImpType(cd.getImpType());
					event.setDisconnect(disconnect);
					event.setTimeZone(br.getTimeZone());
					
					insertList.add(event);
					
					logger.info("NBID=["+nbID+"] is Offline: "+JsonUtil.getInstance().convertObjectToJsonstring(event));
				}
			}
		}
	}
	
	private List<BatteryRecordSummaryModel> getBatteryRecordSummary() {
		List<BatteryRecordSummaryModel> list = DBQueryUtil.getInstance().queryNewestSummarybyGroup();
		return list;
	}
	
	private Map<String, EventModel> getUnsolvedOfflineEvent() {
		Map<String, EventModel> map = DBQueryUtil.getInstance().queryUnsolvedOfflineEvent();
		return map;
	}
	
	public void insertOfflineEvent() {
		if (insertList.size()!=0) {
			DBQueryUtil.getInstance().insertOfflineEvent(insertList);
		}
	}
	
	public Map<String, NBListModel> getActive13NBList() {
		Map<String, NBListModel> activeMap = DBQueryUtil.getInstance().queryActiveNBList();
		return activeMap;
	}
}
