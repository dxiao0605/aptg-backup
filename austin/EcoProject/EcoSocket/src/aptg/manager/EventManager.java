package aptg.manager;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.MeterEventRecordDao;
import aptg.dao.MeterEventTxDao;
import aptg.manager.MeterSetupManager;
import aptg.models.MeterEventRecordModel;
import aptg.models.MeterEventTxModel;
import aptg.models.MeterSetupModel;
import aptg.utils.DBContentDealUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class EventManager {

	private static final Logger logger = LogManager.getFormatterLogger(EventManager.class.getName());
	
	public final int EVENTPRIORITY_EVENT			= 1;	// 嚴重等級: 事件異常
	public final int EVENTPRIORITY_COMMUNICATION	= 2;	// 嚴重等級: 通訊異常

	public final int EVENTSTATUS_OPEN				= 0;	// 事件狀態: open (default)
	public final int EVENTSTATUS_CLOSE				= 1;	// 事件狀態: closed

	public final int EVENTCODE_METER_DISCONNECT 	= 1;	// 電表通訊異常
	public final int EVENTCODE_METER_RECONNECT 		= 2;	// 電表通訊恢復
	public final int EVENTCODE_ECO5_DISCONNECT 		= 3;	// ECO-5中斷
	public final int EVENTCODE_ECO5_RECONNECT 		= 4;	// ECO-5連線
	
	public final String EVENT_LOCATION				= "{%location%}";	// 電表位置
	public final String EVENT_METERID				= "{%meterid%}";	// 站號
	public final String EVENT_METERNAME				= "{%metername%}";	// 電表名稱
	
	public final String EVENTDESC_METER_DISCONNECT	= "ECO-5 「{%location%}」-電表#{%meterid%}「{%metername%}」通訊中斷";
	public final String EVENTDESC_METER_RECONNECT	= "ECO-5 「{%location%}」-電表#{%meterid%}「{%metername%}」通訊恢復";
	public final String EVENTDESC_ECO5_DISCONNECT	= "ECO-5 「{%location%}」中斷";
	public final String EVENTDESC_ECO5_RECONNECT		= "ECO-5 「{%location%}」連線";

	private Map<String, MeterEventRecordModel> offlineDeviceMap = new HashMap<>();
	
	private static EventManager instances;
	private EventManager() {
		init();
	}
	
	public void init() {
		try {
			MeterEventTxDao dao = new MeterEventTxDao();
			List<DynaBean> rows = dao.queryDisconnectedDevice();
			if (rows.size()!=0) {
				List<MeterEventRecordModel> list = DBContentDealUtil.getMeterEventRecordList(rows);
				for (MeterEventRecordModel record: list) {
					offlineDeviceMap.put(record.getDeviceID(), record);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EventManager getInstance() {
		if (instances==null) {
			synchronized (EventManager.class) {
				if (instances==null) {
					instances = new EventManager();
				}
			}
		}
		return instances;
	}

	public MeterEventRecordModel getMeterEventRecord(String eco5Account, String deviceID, String eventTime) {
		MeterEventRecordModel record = new MeterEventRecordModel();
		record.setEco5Account(eco5Account);
		record.setDeviceID(deviceID);
		Date date = ToolUtil.getInstance().convertTimestampToDate(Long.valueOf(eventTime));
		record.setEventTime(ToolUtil.getInstance().convertDateToString(date, "yyyy-MM-dd HH:mm:ss"));
		record.setEventCode(0);		// init
		record.setEventDesc(null);	// init
		return record;
	}
	
	/**
	 * EventCode=1 (電表通訊異常)
	 * 
	 * @param record
	 */
	public void recordDeviceOffline(MeterEventRecordModel record) {
		Integer openSeqno = null;
		try {
			String deviceID = record.getDeviceID();
			
			// Step1. if 斷線 => MeterEventRecord 新增一筆斷線, MeterEventTx新增一筆起始紀錄
			MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
			if (meter==null)
				return;
			
			String eventDesc = EVENTDESC_METER_DISCONNECT.replace(EVENT_LOCATION, meter.getInstallPosition()).replace(EVENT_METERID, String.valueOf(meter.getMeterId())).replace(EVENT_METERNAME, meter.getMeterName());
			record.setEventCode(EVENTCODE_METER_DISCONNECT);	// 電表通訊異常
			record.setEventDesc(eventDesc);
			
			MeterEventRecordDao dao = new MeterEventRecordDao();
			List<Integer> ids = dao.insertRecord(record);
			for (Integer id: ids) {
				openSeqno = id;
			}
			setEventRecordSeqno(record, openSeqno);
			logger.info("MeterDevice Disconnected, Add MeterEventRecord: "+JsonUtil.getInstance().convertObjectToJsonstring(record));
			
			// Step2. insert a new MeterEventTx
			MeterEventTxModel tx = getMeterEventTx(record, record.getEventTime(), null, EVENTPRIORITY_COMMUNICATION, EVENTSTATUS_OPEN, openSeqno, null);
			MeterEventTxDao txDao = new MeterEventTxDao();
			List<Integer> txIds = txDao.insertEventTx(tx);
			logger.info("MeterDevice Disconnected, Add MeterEventTx: "+JsonUtil.getInstance().convertObjectToJsonstring(tx));
			
			// 更新map
			setOfflineDevice(deviceID, record);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Record MeterDevice Disconnected Failed: "+e.getMessage());
		}
	}
	
	/**
	 * EventCode=2 (電表通訊恢復)
	 * 
	 * @param record
	 */
	public void recordDeviceOnline(MeterEventRecordModel record) {
		Integer recordSeqno = null;
		try {
			String deviceID = record.getDeviceID();
			
			// Step1. if 恢復連線 => MeterEventRecord 新增一筆連線, MeterEventTx更新先前起始的結束紀錄
			MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
			if (meter==null)
				return;
			
			String eventDesc = EVENTDESC_METER_RECONNECT.replace(EVENT_LOCATION, meter.getInstallPosition()).replace(EVENT_METERID, String.valueOf(meter.getMeterId())).replace(EVENT_METERNAME, meter.getMeterName());
			record.setEventCode(EVENTCODE_METER_RECONNECT);	// 電表通訊恢復
			record.setEventDesc(eventDesc);

			MeterEventRecordDao dao = new MeterEventRecordDao();
			List<Integer> ids = dao.insertRecord(record);
			for (Integer id: ids) {
				recordSeqno = id;
			}
			setEventRecordSeqno(record, recordSeqno);
			logger.info("MeterDevice Connected, Add MeterEventRecord: "+JsonUtil.getInstance().convertObjectToJsonstring(record));

			// Step2. update MeterEventTx closeTime, closeSeqno, eventStatus
			// 查詢起始MeterEventRecord的seqno => 即為MeterEventTx的OpenSeqno
			int openSeqno = 0;
//			List<DynaBean> rows = dao.queryRecordByMeter(deviceID, EVENTCODE_METER_DISCONNECT);
//			if (rows.size()!=0) {
//				MeterEventRecordModel openRecord = DBContentDealUtil.getMeterEventRecord(rows);
//				openSeqno = openRecord.getSeqno();
//			}
			MeterEventRecordModel openRecord = getOfflineDevice(deviceID);
			if (openRecord!=null)
				openSeqno = openRecord.getSeqno();
			
			MeterEventTxModel tx = getMeterEventTx(record, null, record.getEventTime(), EVENTPRIORITY_COMMUNICATION, EVENTSTATUS_CLOSE, openSeqno, recordSeqno);
			MeterEventTxDao txDao = new MeterEventTxDao();
			int count = txDao.updateEventTxClose(tx);
			if (count!=-1)
				logger.info("MeterDevice RE-Connected, Update MeterEventTx: "+JsonUtil.getInstance().convertObjectToJsonstring(tx));
			else
				logger.info("MeterDevice First Connected, ECO5Account:["+record.getEco5Account()+"], DeviceID:["+record.getDeviceID()+"]");

			// device online => remove map
			removeOfflineDevice(deviceID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Record MeterDevice RE-Connected Failed: "+e.getMessage());
		}
	}
	
	private MeterEventTxModel getMeterEventTx(MeterEventRecordModel record, String openTime, String closeTime, int priority, int status, Integer openSeqno, Integer closeSeqno) {
		MeterEventTxModel tx = new MeterEventTxModel();
		tx.setEco5Account(record.getEco5Account());
		tx.setDeviceID(record.getDeviceID());
		tx.setOpenTime( (!StringUtils.isBlank(openTime) ? openTime:null) );
		tx.setCloseTime( (!StringUtils.isBlank(closeTime) ? closeTime:null) );
		tx.setEventCode(record.getEventCode());
		tx.setPriority(priority);
		tx.setEventStatus(status);
		tx.setOpenSeqno(openSeqno);
		tx.setCloseSeqno(closeSeqno);
		return tx;
	}

	
	private void setEventRecordSeqno(MeterEventRecordModel record, int openSeqno) {
		record.setSeqno(openSeqno);
	}
	
	private void setOfflineDevice(String deviceID, MeterEventRecordModel record) {
		synchronized (offlineDeviceMap) {
			offlineDeviceMap.put(deviceID, record);
		}
	}
	public MeterEventRecordModel getOfflineDevice(String deviceID) {
		if (offlineDeviceMap.containsKey(deviceID)) {
			return offlineDeviceMap.get(deviceID);
		}
		return null;
	}
	private void removeOfflineDevice(String deviceID) {
		if (offlineDeviceMap.containsKey(deviceID)) {
			synchronized (offlineDeviceMap) {
				offlineDeviceMap.remove(deviceID);
			}
		}
	}
}
