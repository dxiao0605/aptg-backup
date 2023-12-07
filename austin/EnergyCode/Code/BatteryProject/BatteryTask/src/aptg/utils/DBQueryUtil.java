package aptg.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.beans.BatteryGroupNBListBean;
import aptg.beans.CompanyDisconnectBean;
import aptg.dao.BatteryGroupDao;
import aptg.dao.BatteryRecordSummaryDao;
import aptg.dao.CompanyDao;
import aptg.dao.EventDao;
import aptg.dao.GroupDailyStatusDao;
import aptg.dao.NBListDao;
import aptg.model.BatteryGroupModel;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.EventModel;
import aptg.model.GroupDailyStatusModel;
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

	public List<BatteryRecordSummaryModel> queryNewestSummarybyGroup() {
		List<BatteryRecordSummaryModel> list = new ArrayList<>();
		try {
			BatteryRecordSummaryDao dao = new BatteryRecordSummaryDao();
			List<DynaBean> rows = dao.queryNewestSummarybyGroup();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryRecordSummary(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BatteryRecordSummaryModel> queryNewstSummarybyCreateTime(String createTime) {
		List<BatteryRecordSummaryModel> list = new ArrayList<>();
		try {
			BatteryRecordSummaryDao dao = new BatteryRecordSummaryDao();
			List<DynaBean> rows = dao.queryNewestSummarybyCreateTime(createTime);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryRecordSummary(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * for DailyStatusTask_GroupHis
	 */
	public Map<String, CompanyDisconnectBean> queryCompanyDisconnectGroupHis() {
		Map<String, CompanyDisconnectBean> map = new HashMap<>();
		try {
			CompanyDao dao = new CompanyDao();
			List<DynaBean> rows = dao.queryCompanyDisconnectGroupHis();
			if (rows.size()!=0) {
				map = DBContentDealUtil.getCompanyDisconnect(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	public Map<Integer, GroupDailyStatusModel> queryGroupDailyStatus(String recDate) {
		Map<Integer, GroupDailyStatusModel> map = new HashMap<>();
		try {
			GroupDailyStatusDao dao = new GroupDailyStatusDao();
			List<DynaBean> rows = dao.queryGroupDailyStatus(recDate);
			if (rows.size()!=0) {
				map = DBContentDealUtil.getGroupDailyStatus(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	/*
	 * for DailyStatusTask
	 */
	public Map<String, CompanyDisconnectBean> queryCompanyDisconnect() {
		Map<String, CompanyDisconnectBean> map = new HashMap<>();
		try {
			CompanyDao dao = new CompanyDao();
			List<DynaBean> rows = dao.queryCompanyDisconnect();
			if (rows.size()!=0) {
				map = DBContentDealUtil.getCompanyDisconnect(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public Map<String, EventModel> queryUnsolvedOfflineEvent() {
		Map<String, EventModel> map = new HashMap<>();
		try {
			EventDao dao = new EventDao();
			List<DynaBean> rows = dao.queryUnsolvedOfflineEvent();
			if (rows.size()!=0) {
				map = DBContentDealUtil.getUnsolvedOfflineEvent(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void insertOfflineEvent(List<EventModel> list) {
		try {
			EventDao dao = new EventDao();
			dao.insertOfflineEvent(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<BatteryGroupModel> queryBatteryGroup() {
		List<BatteryGroupModel> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryBatteryGroup();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<BatteryGroupNBListBean> queryBatteryGroupNBList() {
		List<BatteryGroupNBListBean> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryBatteryGroupNBList();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroupNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<BatteryGroupNBListBean> queryBatteryGroupNBGroupHis(String time) {
		List<BatteryGroupNBListBean> list = new ArrayList<>();
		try {
			BatteryGroupDao dao = new BatteryGroupDao();
			List<DynaBean> rows = dao.queryBatteryGroupNBGroupHis(time);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBatteryGroupNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean insertGroupDailyStatus(List<GroupDailyStatusModel> list) {
		try {
			GroupDailyStatusDao dao = new GroupDailyStatusDao();
			List<Integer> ids = dao.insertGroupDailyStatus(list);
			
			if (ids.size()!=0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public int updateGroupDailyStatus(List<GroupDailyStatusModel> list) {
		int count = -1;
		try {
			GroupDailyStatusDao dao = new GroupDailyStatusDao();
			count = dao.updateGroupDailyStatus(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateGroupDaily(int groupID, String recDate, int status) {
		int rows = -1;
		try {
			GroupDailyStatusDao dao = new GroupDailyStatusDao();
			rows = dao.updateGroupDaily(groupID, recDate, status);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	public Map<Integer, List<NBListModel>> queryGroupInternalActiveNBList() {
		Map<Integer, List<NBListModel>> map = new HashMap<>();
		try {
			NBListDao dao = new NBListDao();
			List<DynaBean> rows = dao.queryActiveNBList();
			
			if (rows.size()!=0) {
				List<NBListModel> list = DBContentDealUtil.getActiveNBList(rows);
				for (NBListModel nb: list) {
					int groupInternalID = nb.getGroupInternalID();
					if (map.containsKey(groupInternalID)) {
						List<NBListModel> nblist = map.get(groupInternalID);
						nblist.add(nb);
					} else {
						List<NBListModel> nblist = new ArrayList<>();
						nblist.add(nb);
						map.put(groupInternalID, nblist);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	// NBList
	public Map<String, NBListModel> queryActiveNBList() {
		Map<String, NBListModel> map = new HashMap<>();
		try {
			NBListDao dao = new NBListDao();
			List<DynaBean> rows = dao.queryActiveNBList();
			
			if (rows.size()!=0) {
				map = DBContentDealUtil.getNBList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
