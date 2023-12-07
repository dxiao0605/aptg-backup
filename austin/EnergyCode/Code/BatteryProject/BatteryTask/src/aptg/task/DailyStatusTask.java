package aptg.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.BatteryGroupNBListBean;
import aptg.beans.CompanyDisconnectBean;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.GroupDailyStatusModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 每小時檢查各站台時區已為凌晨0點的時區，各站台前一天的最後狀態(正常、警戒、需更換、離線)
 * DailyStatusTask_GroupHis
 * 
 * 手動重做: java -cp BatteryTask.jar aptg.task.DailyStatusTask "GMT" "RedoDailyRecDate"
 * (if RecDate=2021-09-21 => RedoDailyRecDate=2020-09-22)
 * 
 * @author austinchen
 *
 */
public class DailyStatusTask {

	private static final String CLASS_NAME = DailyStatusTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final List<Integer> GMT_Offset = Arrays.asList(12,11,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12);

	private static final int Online 	= 1;
	private static final int Warnning 	= 2;
	private static final int Change 	= 3;
	private static final int Offline 	= 4;
	
	private Map<Integer, List<BatteryGroupNBListBean>> groupNBListMap = new HashMap<>();// 站台其所屬的NBID與company。key: groupInternalID
	private Map<String, Integer> nbidGroupMap = new HashMap<>();						// NBID屬於哪個站台。key: nbID
	private Map<Integer, List<Integer>> groupTimezoneMap = new HashMap<>();				// 站台有哪些TimeZone。key: groupInternalID
	
	private Map<String, List<BatteryRecordSummaryModel>> summaryMap = new HashMap<>();	// 所有NBID的最新summary資料
	private Map<Integer, List<String>> timezoneNBIDMap = new HashMap<>();				// timezone

	private Map<String, CompanyDisconnectBean> disconnectMap = new HashMap<>();			// company設定的disconnect值
	
	private Map<Integer, GroupDailyStatusModel> dailyStatusMap = new HashMap<>();		// GroupDailyStatus於recDate=XXX的紀錄
	
	private Map<Integer, GroupDailyStatusModel> insertDailyStatusMap = new HashMap<>();	// 記錄各站台的GroupDailyStatus(狀態) key: GroupInternalID
	private Map<Integer, GroupDailyStatusModel> updateDailyStatusMap = new HashMap<>();	// 記錄各站台的GroupDailyStatus(狀態) key: GroupInternalID
	
	private Integer GmtOffset;			// 想要計算的<=時區
	private String RedoDailyRecDate;	// 想要計算的GroupDailyStatus RecDate。EX: 2021-08-02 => RecDate: 2021-08-01
	private String QueryCreateTime;		// 想要計算的時區RedoDailyRecDate轉回台灣+8的時間，才是從summary裡撈取CreateTime的時間
	private boolean IsRedo;				// true: args[] not null => redo, false: every hour
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME + " Start!");

		DailyStatusTask task = new DailyStatusTask();
		
		task.setArgs(args);	// 手動參數
		
		// (1)
		task.procedure();
		// (2)
		task.printLog();
		// (3)
		task.insertUpdateGroupDailyStatus();
		
		logger.info(CLASS_NAME + " End!");
	}
	private void setArgs(String[] args) {
		if (args.length!=0) {
			/*
			 * ex: 欲計算GmtOffset=-2 (即TimeZone=-8) 的RecDate=2021-11-04 電池每日狀態
			 * 	   RedoDailyRecDate=2021-11-05 (因2021-11-05 00:00:00才有辦法統計前一天2021-11-04的每日狀態)
			 * 	   QueryCreateTime自動換算成台灣時間
			 */
			GmtOffset = Integer.valueOf(args[0]);
			RedoDailyRecDate = args[1] + " 00:00:00";
			QueryCreateTime = ToolUtil.getInstance().getTaiwanTime(GmtOffset, RedoDailyRecDate);	// 該時區GmtOffset，在欲計算的日期RedoDailyRecDate，轉換成的台灣時間QueryCreateTime
			IsRedo = true;
		} else {
			// 正常排程
			GmtOffset = findGMTOffset2();
			RedoDailyRecDate = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", GmtOffset);	// 台灣時間轉換成該時區(GmtOffset)，該時區統計的每日狀態日期(RedoDailyRecDate)
			QueryCreateTime = ToolUtil.getInstance().getTaiwanTime(GmtOffset, RedoDailyRecDate);
			IsRedo = false;
		}
	}
	
	/**
	 * (1)
	 */
	public void procedure() {
		/*
		 * -------------- Table:"BatteryGroup" join "NBList" 取得 所有站台 & Active NBID =>  groupNBListMap --------------
		 */
		getBatteryGroupNBList();
		
		/*
		 * --------------  統整NBID所有的summary (battery_0, battery_1, ...) : summaryMap-------------- 
		 */
		getRecordSummary(QueryCreateTime);	// key: nbID, value: Model_nbID_0, Model_nbID_1, ...

		/*
		 * -------------- 取得各company的disconnect設定 -------------- 
		 */
		getCompanyDisConnect();

		/*
		 * -------------- 檢查發生整點的gmtOffset -------------- 
		 */
		Date time = ToolUtil.getInstance().convertStringToDate(RedoDailyRecDate, "yyyy-MM-dd HH:mm:ss");
		Calendar recdateCal = Calendar.getInstance();
		recdateCal.setTime(time);
		recdateCal.add(Calendar.DATE, -1);

		logger.info("=================================== 目前跨日至00:00的時區為 GMT: "+GmtOffset +", TimeZone: "+ToolUtil.getInstance().convertOffsetToTimezone(GmtOffset));
		logger.info("=================================== RecDate: "+ToolUtil.getInstance().convertDateToString(recdateCal.getTime(), "yyyy-MM-dd"));
		logger.info("=================================== 撈取 BatteryRecordSummary <= CreateTime: "+QueryCreateTime);

		/*
		 * -------------- 取得目前的GroupDailyStatus --------------
		 */
		getGroupDailyStatus(recdateCal);
		
		if (GmtOffset==12) {
			/*
			 * gmtOffset=+12時區轉換成全新的一天 
			 * => 產time的前一日(recDate=date-1)狀態 & offset=-12更新前兩日(recDate=date-1)的狀態
			 */
			// 所有站台 & GMT+12 ~ GMT-12
			for (int groupInternalID: groupNBListMap.keySet()) {
				boolean isCheck = isCheck(groupInternalID, GmtOffset);
				if (!isCheck) 
					continue;	// 找不到含有<=gmtOffset的時區的NBID

				// 此站台的所有nbid
				List<BatteryGroupNBListBean> list = groupNBListMap.get(groupInternalID);
				for (BatteryGroupNBListBean bgnb: list) {
					String nbID = bgnb.getNBID();
					
					// map.get(nbID) => 判斷電池是否離線 or 其他狀態(需更換、警戒、正常)
					if (summaryMap.containsKey(nbID)) {
						checkStatus(nbID, groupInternalID, recdateCal, insertDailyStatusMap);
					}
				}
			}
			
			// 有GMT-12=>TimeZone=-48 的NBID (直接從NBID找符合時區的NBID，再從NBID找到他所屬的站台)
			GmtOffset = -12;
			int lastTimeZone = ToolUtil.getInstance().convertOffsetToTimezone(GmtOffset);	// =-48
			recdateCal.add(Calendar.DATE, -1);	// Ex: +12剛過凌晨至08-02, -12才剛到08-01
			if (timezoneNBIDMap.containsKey(lastTimeZone)) {
				List<String> nbidList = timezoneNBIDMap.get(lastTimeZone);	// 在gmt=-12(timezone=-48)的時區的所有NBID
				for (String nbID: nbidList) {
					if (nbidGroupMap.containsKey(nbID)) {
						int groupInternalID = nbidGroupMap.get(nbID);
						
						if (summaryMap.containsKey(nbID)) {
							checkStatus(nbID, groupInternalID, recdateCal, updateDailyStatusMap);
						}
					}
				}
			}
			
		} else {
			// <=gmtOffset的時區狀態都要做更新(已過的時區不再做)
			for (int groupInternalID: groupNBListMap.keySet()) {
				boolean isCheck = isCheck(groupInternalID, GmtOffset);
				if (!isCheck) 
					continue;	// 找不到含有<=gmtOffset的時區的NBID
				
				// 此站台的所有nbid
				List<BatteryGroupNBListBean> list = groupNBListMap.get(groupInternalID);
				for (BatteryGroupNBListBean bgnb: list) {
					String nbID = bgnb.getNBID();
					
					// map.get(nbID) => 判斷電池是否離線 or 其他狀態(需更換、警戒、正常)
					if (summaryMap.containsKey(nbID)) {
						checkStatus(nbID, groupInternalID, recdateCal, updateDailyStatusMap);
						
						// 中途新增的站台，尚未在第一個進入凌晨00:00時區時建立紀錄(補新增Daily Status)
						if (!dailyStatusMap.containsKey(groupInternalID)) {
							if (updateDailyStatusMap.containsKey(groupInternalID))
								insertDailyStatusMap.put(groupInternalID, updateDailyStatusMap.get(groupInternalID));
						}
					}
				}
			}
		}
	}

	/**
	 * (2)
	 */
	public void printLog() {
		for (GroupDailyStatusModel daily: insertDailyStatusMap.values()) {
			logger.info("insert: "+JsonUtil.getInstance().convertObjectToJsonstring(daily));
		}
		for (GroupDailyStatusModel daily: updateDailyStatusMap.values()) {
			logger.info("update: "+JsonUtil.getInstance().convertObjectToJsonstring(daily));
		}
	}

	/**
	 * (3)
	 */
	public void insertUpdateGroupDailyStatus() {
		if (insertDailyStatusMap.size()!=0) {
			DBQueryUtil.getInstance().insertGroupDailyStatus(new ArrayList<>(insertDailyStatusMap.values()));
		}
		if (updateDailyStatusMap.size()!=0) {
			DBQueryUtil.getInstance().updateGroupDailyStatus(new ArrayList<>(updateDailyStatusMap.values()));
		}
	}

	
	
	
	/**
	 * 取得目前為0點的 GMT offset值
	 * 
	 * @return
	 */
	private int findGMTOffset2() {
		for (int gmtOffset: GMT_Offset) {
			String timeStr = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", gmtOffset);

			Date time = ToolUtil.getInstance().convertStringToDate(timeStr, "yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(time);
			// 抓取hour為何
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			if (hour==0) {
				// 找到凌晨0點的offset
				return gmtOffset;
			}
		}
		return 0;
	}

	/**
	 * 	此站台是否包含<=目前零點時區的時區
	 * 	若有，必須更新狀態
	 * 
	 * @param groupInternalID
	 * @param gmtOffset
	 * @return
	 */
	private boolean isCheck(int groupInternalID, int gmtOffset) {
		if (groupTimezoneMap.containsKey(groupInternalID)) {
			List<Integer> timezoneList = groupTimezoneMap.get(groupInternalID);	// 此站台的所有時區
			for (int timezone: timezoneList) {
				int gmt = ToolUtil.getInstance().convertTimeZoneToGMT(String.valueOf(timezone));
				if (gmt<=gmtOffset) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 *	判斷狀態
	 * 
	 * @param nbID
	 * @param groupInternalID
	 * @param cal
	 */
	private void checkStatus(String nbID, int groupInternalID, Calendar recdateCal, Map<Integer, GroupDailyStatusModel> statusMap) {
		List<BatteryRecordSummaryModel> summaryList = summaryMap.get(nbID);	// 此nbID與其所有的batteryID SummaryRecord
		
		Date startTime = null;
		Date endTime = null;
		List<BatteryGroupNBListBean> bgnbList = groupNBListMap.get(groupInternalID);
		for (BatteryGroupNBListBean bgnb: bgnbList) {
			if (nbID.equals(bgnb.getNBID())) {
				startTime = ToolUtil.getInstance().convertStringToDate(bgnb.getStartTime(), "yyyy-MM-dd HH:mm:ss");
				endTime = ToolUtil.getInstance().convertStringToDate(bgnb.getEndTime(), "yyyy-MM-dd HH:mm:ss");
			}
		}
		
		for (BatteryRecordSummaryModel summary: summaryList) {
			int batteryID = summary.getBatteryID();
			int timeZone = summary.getTimeZone();
			int status = summary.getStatus();		// 1:正常, 2:警戒, 3:需更換
			Date createTime = summary.getCreatetime();
			Date recTime = ToolUtil.getInstance().convertStringToDate(summary.getRecTime(), "yyyy-MM-dd HH:mm:ss");	// summary資料的record時間，用來判斷在NBAllocationHis的區間為何
			
			int gmt = ToolUtil.getInstance().convertTimeZoneToGMT(String.valueOf(timeZone));
			String gmtStr = (gmt>=0) ?"GMT+"+gmt:"GMT"+gmt;

			/*
			 * -------------- 先判斷是否在NBGroupHis區間 -------------- 
			 */
			// startTime<=recTime && recTime<endTime
			if (startTime!=null && endTime!=null && recTime.compareTo(startTime)>=0 && recTime.compareTo(endTime)==-1) {
				/*
				 * -------------- summaryRecord資料計算是否超過disconnect時間，即為離線 -------------- 
				 */
				// 查找此nbID判定離線的時間間距值
				CompanyDisconnectBean cd = disconnectMap.get(nbID);
				if (cd==null) {
					logger.error("Can't find NBID=["+nbID+"] Disconnect Value");
					continue;
				}
				long disconnect = cd.getDisconnect();

				/*
				 * ================== compare start ====================
				 */
				Date now = ToolUtil.getInstance().convertStringToDate(QueryCreateTime, "yyyy-MM-dd HH:mm:ss");
//				logger.info("GroupInternalID=["+groupInternalID+"], NBID=["+nbID+"], BatteryID=["+batteryID+"], TimeZone=["+timeZone+"], 實際收Record時間=["+ToolUtil.getInstance().convertDateToString(createTime, "yyyy-MM-dd HH:mm:ss")+"]");
				long diff = (long) (now.getTime()-createTime.getTime()) / 1000;
				logger.info("GroupInternalID=["+groupInternalID+"], NBID=["+nbID+"], BatteryID=["+batteryID+"], TimeZone=["+timeZone+"] ("+gmtStr+"), 現在時間=["+QueryCreateTime+"], 實際收Record時間=["+ToolUtil.getInstance().convertDateToString(createTime, "yyyy-MM-dd HH:mm:ss")+"], Time interval: "+diff+"(sec), Company Disconnect=["+disconnect+"]");

				if (disconnect!=0 && diff>disconnect) {
					status = Offline; // 4:離線
				} else if (disconnect==0) {
					// 不做離線判斷(記一下log)
					logger.info("GroupInternalID=["+groupInternalID+"], NBID=["+nbID+"], BatteryID=["+batteryID+"], TimeZone=["+timeZone+"] ("+gmtStr+"), Company Disconnect=["+disconnect+"] => 不做離線判斷! ");
				}
				/*
				 * ================== compare end ====================
				 */
				
				// 檢查站台狀態
				if (statusMap.containsKey(groupInternalID)) {
					GroupDailyStatusModel daily = statusMap.get(groupInternalID);
					int recordStatus = daily.getStatus();
					
					if (recordStatus==Offline && status<Offline) {
						// 只要有一個不是離線 => 非離線，取最差狀態
						daily.setStatus(status);
						statusMap.put(groupInternalID, daily);
					}
					else if (recordStatus==Change) {
						// 最差狀態 => 需更換
						break;
					}
					else if (recordStatus==Warnning && status==Change) {
						// 現為警戒，若有電池狀態為需更換，取最差狀態 => 需更換
						daily.setStatus(status);
						statusMap.put(groupInternalID, daily);
					}
					else if (recordStatus==Online && (status==Warnning||status==Change)) {
						// 現為正常，若有電池狀態為警戒or需更換，取最差狀態 => 警戒or需更換
						daily.setStatus(status);
						statusMap.put(groupInternalID, daily);
					}
//					else if (recordStatus==4 && status==4) {
//						// 不需做 => 維持離線，直到有1~3的狀態
//					}
					
				} else {
					// 預設summary status為其狀態
					GroupDailyStatusModel daily = new GroupDailyStatusModel();
					daily.setGroupInternalID(groupInternalID);
					daily.setRecDate(ToolUtil.getInstance().convertDateToString(recdateCal.getTime(), "yyyy-MM-dd"));
					daily.setTimeZone(timeZone);
					daily.setStatus(status);
					
					statusMap.put(groupInternalID, daily);
				}
			}
		}
	}
	
	/**
	 * set BatteryGroup join NBList
	 * set NBID's GroupInternalID
	 */
	private void getBatteryGroupNBList() {
		List<BatteryGroupNBListBean> list = DBQueryUtil.getInstance().queryBatteryGroupNBGroupHis(QueryCreateTime) ;
		
		for (BatteryGroupNBListBean bgnb: list) {
			int groupInternalID = bgnb.getGroupInternalID();
			String nbID = bgnb.getNBID();
			
			// key:GroupInternalID => value: BatteryGroup & NBList
			if (groupNBListMap.containsKey(groupInternalID)) {
				List<BatteryGroupNBListBean> beanList = groupNBListMap.get(groupInternalID);
				beanList.add(bgnb);
			} else {
				List<BatteryGroupNBListBean> beanList = new ArrayList<>();
				beanList.add(bgnb);
				groupNBListMap.put(groupInternalID, beanList);
			}
			
			// key: NBID => value: GroupInternalID
			nbidGroupMap.put(nbID, groupInternalID);
		}
	}
	
	/**
	 *	撈取BatteryRecordSummary
	 * 
	 * @param timezone
	 * @return
	 */
	private void getRecordSummary(String createTime) {
//		List<BatteryRecordSummaryModel> list = DBQueryUtil.getInstance().queryNewstSummarybyCreateTime(createTime);
		List<BatteryRecordSummaryModel> list = (IsRedo) ? DBQueryUtil.getInstance().queryNewstSummarybyCreateTime(createTime) 	// 手動執行
														: DBQueryUtil.getInstance().queryNewestSummarybyGroup();				// 正常排程
		
		for (BatteryRecordSummaryModel summary: list) {
			String nbID = summary.getNbid();
			int timezone = summary.getTimeZone();
			
			// key: NBID
			// 統整NBID所有的summary (battery_0, battery_1, ...)
			if (summaryMap.containsKey(nbID)) {
				List<BatteryRecordSummaryModel> summaryList = summaryMap.get(nbID);
				summaryList.add(summary);
			} else {
				List<BatteryRecordSummaryModel> summaryList = new ArrayList<>();
				summaryList.add(summary);
				summaryMap.put(nbID, summaryList);
			}
			
			// key: timezone => value: List<NBID>
			if (timezoneNBIDMap.containsKey(timezone)) {
				List<String> nbidList = timezoneNBIDMap.get(timezone);
				nbidList.add(nbID);
			} else {
				List<String> nbidList = new ArrayList<>();
				nbidList.add(nbID);
				timezoneNBIDMap.put(timezone, nbidList);
			}

			if (nbidGroupMap.containsKey(nbID)) {
				int groupInternalID = nbidGroupMap.get(nbID);

				// key: GroupInternalID => value: List<TimeZone>
				if (groupTimezoneMap.containsKey(groupInternalID)) {
					List<Integer> timezoneList = groupTimezoneMap.get(groupInternalID);
					timezoneList.add(timezone);
				} else {
					List<Integer> timezoneList = new ArrayList<>();
					timezoneList.add(timezone);
					groupTimezoneMap.put(groupInternalID, timezoneList);
				}
			}
		}
	}
	
	/**
	 *	查找company設定的disconnect value
	 */
	private void getCompanyDisConnect() {
		disconnectMap = DBQueryUtil.getInstance().queryCompanyDisconnectGroupHis();
	}
	
	private void getGroupDailyStatus(Calendar recdateCal) {
		String recDate = ToolUtil.getInstance().convertDateToString(recdateCal.getTime(), "yyyy-MM-dd");
		dailyStatusMap = DBQueryUtil.getInstance().queryGroupDailyStatus(recDate);
	}
}
