package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class BatteryRecordSummaryDao extends BaseDao2 {

	/**
	 * for DailyStatusTask
	 * 
	 * @param createTime
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> queryNewestSummarybyCreateTime(String createTime) throws SQLException {
		String sql = "select NBID, BatteryID, RecTime, TimeZone, Status, CreateTime from BatteryRecordSummary where CreateTime<=str_to_date('"+createTime+"','%Y-%m-%d %H:%i:%s') group by NBID, BatteryID;";
		
		return executeQuery(sql);
	}
	
	/**
	 * for OfflineTask & DailyStatusTask
	 * (column `Status` not used)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> queryNewestSummarybyGroup() throws SQLException {
		String sql = "SELECT NBID, BatteryID, RecTime, TimeZone, Status, CreateTime FROM BatteryRecordSummary group by NBID, BatteryID";
		return executeQuery(sql);
	}
	
}
