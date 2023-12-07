package aptg.monitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.MeterEventTxDao;
import aptg.dao.SysConfigDao;
import aptg.manager.SysConfigManager;
import aptg.models.ControllerSetupModel;
import aptg.models.MeterEventTxModel;
import aptg.models.MeterSetupModel;
import aptg.models.PowerRecordModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class Eco5OfflineMonitor {

	private static final String CLASS_NAME = Eco5OfflineMonitor.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	// define
//	private static final String Sysconfig_Eco5OffLineRecordSeq 	= "Eco5OffLineRecordSeq";
	private static final int EventCode 			= 3;	// ECO-5中斷
	private static final int EventStatus_Open 	= 0;	// 0: open(default) / 1: close
	private static final int EventStatus_Close 	= 1;
	private static final int Priority 			= 2;	// 通訊異常
	
	// data
//	private String eco5OffLineRecordSeq;
	private Map<String, Boolean> eco5Map 			= new HashMap<>();
	private Map<String, MeterSetupModel> deviceMap 	= new HashMap<>();
	private Map<String, MeterEventTxModel> eventMap = new HashMap<>();
	
	// result
	private List<MeterEventTxModel> openEventList = new ArrayList<>();
	private List<MeterEventTxModel> closeEventList = new ArrayList<>();
	
	

	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		Eco5OfflineMonitor monitor = new Eco5OfflineMonitor();
		
		monitor.doMonitor();
		monitor.updateMeterEventTx();
//		monitor.updateEco5OffLineRecordSeq();
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void doMonitor() {
		/* ==========
		 * 		1
		 * ==========
		 * get sysconfig Eco5OffLineRecordSeq
		 * (紀錄上次取到哪一個PowerRecord的seqno)
		 */
//		this.eco5OffLineRecordSeq = SysConfigManager.getInstance().getSysconfig(Sysconfig_Eco5OffLineRecordSeq);
//		if (eco5OffLineRecordSeq==null) {
//			logger.error("Eco5OffLineRecordSeq is null ...");
//			return;
//		}
		
		/* ==========
		 * 		2
		 * ==========
		 *	取得所有ECO5Account, 並預設狀態false: offline
		 */
		this.eco5Map = getECO5DefaultStatus();		// key: eco5Account, value: status(boolean)
		
		/* ==========
		 * 		3
		 * ==========
		 * Device & Eco5Account對應表
		 */
		this.deviceMap = getDeviceECO5Account();		// key: deviceID, value: eco5Account
		
		/* ==========
		 * 		4
		 * ==========
		 * query PowerRecord & update eco5 status to online
		 * 30分鐘無資料
		 */
		Calendar cal = Calendar.getInstance();
		String enddate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
		cal.add(Calendar.MINUTE, -30);
		String startdate = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
		updateECO5status(startdate, enddate);

		/* ==========
		 * 		5
		 * ==========
		 *	撈取MeterEventTx為斷線的事件
		 * EventCode=3 (ECO-5中斷)
		 * Priority=2 (通訊異常)
		 * EventStatus=0 (open)
		 */
		this.eventMap = DBQueryUtil.getInstance().queryEventTx(EventCode, Priority, EventStatus_Open);
		
		/* ==========
		 * 		6
		 * ==========
		 *	比對eco5狀態 & 事件
		 */
		compareECO5Event();
	}
	
	private Map<String, Boolean> getECO5DefaultStatus() {
		Map<String, Boolean> map = new HashMap<>();
		
		List<ControllerSetupModel> list = DBQueryUtil.getInstance().getAllEnabledECO5Account();
		for (ControllerSetupModel cs: list) {
			String eco5Account = cs.getECO5Account();
			
			map.put(eco5Account, false);
		}
		return map;
	}
	
	private Map<String, MeterSetupModel> getDeviceECO5Account() {
		Map<String, MeterSetupModel> map = new HashMap<>();
		
		List<MeterSetupModel> list = DBQueryUtil.getInstance().getAllEnabledECO5AccountMeter();
		for (MeterSetupModel ms: list) {
			String deviceID = ms.getDeviceID();
			
			map.put(deviceID, ms);
		}
		return map;
	}
	
//	private void updateECO5status() {
//		List<PowerRecordModel> records = DBQueryUtil.getInstance().getRecordGroupbyDevice(eco5OffLineRecordSeq);
//		for (PowerRecordModel record: records) {
//			String deviceID = record.getDeviceID();
//			
//			if (deviceMap.containsKey(deviceID)) {
//				String eco5Account = deviceMap.get(deviceID).getEco5Account();
//				
//				if (eco5Map.containsKey(eco5Account)) {
//					eco5Map.put(eco5Account, true);
//				}
//			}
//		}
//	}
	private void updateECO5status(String startdate, String enddate) {
		List<PowerRecordModel> records = DBQueryUtil.getInstance().queryRecordGroupbyDeviceWithPartition(startdate, enddate);
		for (PowerRecordModel record: records) {
			String deviceID = record.getDeviceID();
			
			if (deviceMap.containsKey(deviceID)) {
				String eco5Account = deviceMap.get(deviceID).getEco5Account();
				
				if (eco5Map.containsKey(eco5Account)) {
					eco5Map.put(eco5Account, true);
				}
			}
		}
	}
	
	private void compareECO5Event() {
		for (String eco5Account: eco5Map.keySet()) {
			boolean status = eco5Map.get(eco5Account);	// true: online / false: offline
			
			if (eventMap.containsKey(eco5Account)) {
				// 找到離線的事件
				if (status==true) {
					// ECO5狀態為online => 有新資料，恢復連線
					
					// 此次撈取的資料狀態為online => 關閉事件
					MeterEventTxModel tx = eventMap.get(eco5Account);
					tx.setEventStatus(EventStatus_Close);
					tx.setCloseTime(ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					closeEventList.add(tx);

					logger.info("Close Event ECO5Account=["+eco5Account+"], MeterEventTx: "+JsonUtil.getInstance().convertObjectToJsonstring(tx));
				}
			} else {
				// 無離線事件
				if (status==false) {
					// ECO5狀態為offline => 過久無資料，斷線

					// 此次撈取的資料狀態為offline => 新增事件
					MeterEventTxModel tx = new MeterEventTxModel();
					tx.setEco5Account(eco5Account);
					tx.setOpenTime(ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					tx.setEventCode(EventCode);	// 3
					tx.setPriority(Priority);	// 2
					tx.setEventStatus(EventStatus_Open);	// 0
					openEventList.add(tx);

					logger.info("Open Event ECO5Account=["+eco5Account+"], MeterEventTx: "+JsonUtil.getInstance().convertObjectToJsonstring(tx));
				}
			}
		}
	}
	
	private void updateMeterEventTx() {
		MeterEventTxDao dao = new MeterEventTxDao();
		try {
			if (closeEventList.size()!=0)
				dao.updateEventTx(closeEventList);
			
			if (openEventList.size()!=0)
				dao.insertEventTx(openEventList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	private void updateEco5OffLineRecordSeq() {
//		SysConfigDao dao = new SysConfigDao();
//		try {
//			dao.updateEco5OffLineRecordSeq();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
