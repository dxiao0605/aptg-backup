package aptg.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.bean.BABean;
import aptg.bean.BBBean;
import aptg.bean.BatteryGroupNBListBean;
import aptg.bean.DelNBIDBean;
import aptg.dao.AlertTaskDao;
import aptg.dao.BatteryDao;
import aptg.dao.BatteryDetailDao;
import aptg.dao.BatteryGroupDao;
import aptg.dao.BatteryRecordDao;
import aptg.dao.CommandDao;
import aptg.dao.CommandTaskDao;
import aptg.dao.CompanyDao;
import aptg.dao.DelNBIDDao;
import aptg.dao.EventDao;
import aptg.dao.NBHistoryDao;
import aptg.dao.NBListDao;
import aptg.model.BattMaxRecTimeModel;
import aptg.model.BatteryModel;
import aptg.model.BatteryRecordModel;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.CommandModel;
import aptg.model.CommandTaskModel;
import aptg.model.CompanyModel;
import aptg.model.EventModel;
import aptg.model.NBListModel;

public class DBQueryUtil {

	private static DBQueryUtil instances;
	
	private DBQueryUtil() {}
	
	public static DBQueryUtil getInstance() {
		if (instances==null) {
			synchronized (DBQueryUtil.class) {
				if (instances==null) {
					instances = new DBQueryUtil();
				}
			}
		}
		return instances;
	}

	// -------------------------- Company --------------------------
	public List<CompanyModel> queryCompany() {
		List<CompanyModel> list = new ArrayList<>();
		try {
			CompanyDao dao = new CompanyDao();
			List<DynaBean> rows = dao.queryCompany();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getCompany(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public CompanyModel queryCompany(int companyCode) {
		List<CompanyModel> list = new ArrayList<>();
		try {
			CompanyDao dao = new CompanyDao();
			List<DynaBean> rows = dao.queryCompany(companyCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getCompany(rows);
				if (list.size()!=0)
					return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// -------------------------- NBAllocationHis & NBList --------------------------
//	public List<AllocationNBListBean> queryAllocationNBListActive() {
//		List<AllocationNBListBean> list = new ArrayList<>();
//		try {
//			NBAllocationHisDao dao = new NBAllocationHisDao();
//			List<DynaBean> rows = dao.joinAllocationNBList();
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getAllocationNBList(rows);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//	public AllocationNBListBean queryAllocationNBListActive(String nbID) {
//		List<AllocationNBListBean> list = new ArrayList<>();
//		try {
//			NBAllocationHisDao dao = new NBAllocationHisDao();
//			List<DynaBean> rows = dao.joinAllocationNBListActive(nbID);
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getAllocationNBList(rows);
//				if (list.size()!=0) {
//					return list.get(0);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public AllocationNBListBean queryAllocationNBList(String nbID) {
//		List<AllocationNBListBean> list = new ArrayList<>();
//		try {
//			NBAllocationHisDao dao = new NBAllocationHisDao();
//			List<DynaBean> rows = dao.joinAllocationNBList(nbID);
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getAllocationNBList(rows);
//				if (list.size()!=0) {
//					return list.get(0);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	// -------------------------- BatteryGroup & NBList --------------------------
	/**
	 * query all BatteryGroup join NBList active
	 * @return
	 */
	public List<BatteryGroupNBListBean> queryAllBatteryGroupNBListActive() {
		List<BatteryGroupNBListBean> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryAllBatteryGroupNBListActive();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroupNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * query all BatteryGroup join NBList by NBID & active 
	 * 
	 * @param nbID
	 * @return
	 */
	public BatteryGroupNBListBean queryBatteryGroupNBListActiveByNBID(String nbID) {
		List<BatteryGroupNBListBean> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryBatteryGroupNBListActiveByNBID(nbID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroupNBList(rows);
				if (list.size()!=0) {
					return list.get(0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * query BatteryGroup join NBList by NBID
	 * @param nbID
	 * @return
	 */
	public BatteryGroupNBListBean queryBatteryGroupNBListByNBID(String nbID) {
		List<BatteryGroupNBListBean> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryBatteryGroupNBListByNBID(nbID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroupNBList(rows);
				if (list.size()!=0) {
					return list.get(0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<NBListModel> queryNBListByNBID(String nbID) {
		List<NBListModel> list = new ArrayList<>();
		try {
			NBListDao dao = new NBListDao();
			List<DynaBean> rows = dao.queryByNBID(nbID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<NBListModel> queryNBIDByGroupInternalID(int groupInternalID) {
		List<NBListModel> list = new ArrayList<>();
		try {
			NBListDao dao = new NBListDao();
			List<DynaBean> rows = dao.queryByGroupInternalID(groupInternalID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// -------------------------- Battery --------------------------
	public List<BatteryModel> queryBattery() {
		List<BatteryModel> list = new ArrayList<>();
		try {
			BatteryDao dao = new BatteryDao();
			List<DynaBean> rows = dao.queryBattery();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBattery(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BatteryModel> queryBatteryByNBID(String nbID) {
		List<BatteryModel> list = new ArrayList<>();
		try {
			BatteryDao dao = new BatteryDao();
			List<DynaBean> rows = dao.queryByNBID(nbID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBattery(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void insertBattery(BatteryModel model) {
		// insert into Battery
		try {
			BatteryDao dao = new BatteryDao();
			dao.insertBattery(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBatteryRecords(BatteryModel model) {
		// update Battery
		try {
			BatteryDao dao = new BatteryDao();
			dao.updateBatteryRecords(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int updateBatteryBBValue(BBBean bb, String nbID, int batteryID) {
		int count = 0;
		try {
			BatteryDao dao = new BatteryDao();
			count = dao.updateBatteryBBValue(bb, nbID, batteryID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateBatteryBAValue(BABean ba, String nbID, int batteryID) {
		int count = 0;
		try {
			BatteryDao dao = new BatteryDao();
			count = dao.updateBatteryBAValue(ba, nbID, batteryID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// -------------------------- Battery & NBList --------------------------
	public List<BatteryModel> queryNBListJoinBattery(int groupInternalID) {
		List<BatteryModel> list = new ArrayList<>();
		try {
			BatteryDao dao = new BatteryDao();
			List<DynaBean> rows = dao.joinNBListBattery(groupInternalID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBattery(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// -------------------------- BatteryDetail --------------------------
	public void updateBatteryDetail(List<Object> list, String nbID, int batteryID, int category) {
		BatteryDetailDao dao = new BatteryDetailDao();
		dao.updateBatteryDetail(list, nbID, batteryID, category);
	}
	
	// -------------------------- BatteryRecord, BatteryRecordSummary -------------------------- 
	public void insertB168(List<BatteryRecordModel> recordList, List<BatteryRecordSummaryModel> summaryList, List<EventModel> eventList, List<BattMaxRecTimeModel> battMaxRecList) {
		try {
			BatteryRecordDao dao = new BatteryRecordDao();
			dao.insertB168(recordList, summaryList, eventList, battMaxRecList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
//	/**
//	 * 將上傳BatteryRecord寫入DB
//	 */
//	public void insertRecord(List<BatteryRecordModel> recordList) {
//		if (recordList.size()!=0) {
//			try {
//				BatteryRecordDao dao = new BatteryRecordDao();
//				dao.insertRecord(recordList);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 將上傳BatteryRecordSummary寫入DB
//	 */
//	public void insertSummary(List<BatteryRecordSummaryModel> collectionList) {
//		if (collectionList.size()!=0) {
//			try {
//				BatteryRecordSummaryDao dao = new BatteryRecordSummaryDao();
//				dao.insertCollection(collectionList);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	// -------------------------- CommandTask -------------------------- 
	public CommandTaskModel queryCommandTaskJoinCommand(String transactionID) {
		try {
			CommandTaskDao dao = new CommandTaskDao();
			List<DynaBean> rows = dao.joinCommandCommandTask(transactionID);
			if (rows.size()!=0) {
				List<CommandTaskModel> list = DBContentDealUtil.getCommandTask(rows);
				if (list.size()!=0)
					return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新CommandTask狀態&執行時間
	 * 
	 * @param taskID
	 * @param status
	 * @param reqTime
	 */
	public void updateCommandTask(String taskID, int status, Date reqTime) {
		try {
			CommandTaskDao dao = new CommandTaskDao();
			dao.updateCommandTask(taskID, status, reqTime);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------- Command -------------------------- 
	/**
	 * 新增一筆下行命令
	 * 
	 * @param command
	 */
	public void insertCommand(List<CommandModel> commandList) {
		try {
			CommandDao dao = new CommandDao();
			dao.insertCommand(commandList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCommandAckTime(String transactionID, String ackTime) {
		try {
			CommandDao dao = new CommandDao();
			dao.updateCommandAckTime(transactionID, ackTime);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCommandResponse(String transactionID, String respTime, int respCode, String respContent) {
		try {
			CommandDao dao = new CommandDao();
			dao.updateCommandResp(transactionID, respTime, respCode, respContent);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------- Event -------------------------- 
	public EventModel queryUnsolvedEvent(String nbID, int batteryID, int eventType) {
		try {
			EventDao dao = new EventDao();
			List<DynaBean> rows = dao.queryUnsolvedEvent(nbID, batteryID, eventType);
			if (rows.size()!=0) {
				EventModel event = DBContentDealUtil.getUnsolvedEvent(rows);
				return event;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

//	public void insertReplaceEvent(List<EventModel> list) {
//		try {
//			EventDao dao = new EventDao();
//			dao.insertReplaceEvent(list);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public void closeEvent(List<EventModel> list) {
//		try {
//			EventDao dao = new EventDao();
//			dao.closeEvent(list);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	// -------------------------- delete NBID 相關 --------------------------
	public int delNBID(List<DelNBIDBean> list) {
		try {
			DelNBIDDao dao = new DelNBIDDao();
			return dao.delNBID(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
//	public int delNBList(List<DelNBIDBean> list) {
//		try {
//			NBListDao dao = new NBListDao();
//			return dao.delNBList(list);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
	
	public void insertNBHistory(List<DelNBIDBean> list) {
		try {
			NBHistoryDao dao = new NBHistoryDao();
			dao.insertNBHistory(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------- AlertTask --------------------------
	public void updateAlertTaskSuccess(String taskID) {
		try {
			AlertTaskDao dao = new AlertTaskDao();
			dao.updateAlertTaskSuccess(taskID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
