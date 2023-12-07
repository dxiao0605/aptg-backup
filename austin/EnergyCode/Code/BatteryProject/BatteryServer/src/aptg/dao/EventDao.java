package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class EventDao extends BaseDao2 {

	private static final int EventStatus_Unsolved = 5;
	
	public List<DynaBean> queryUnsolvedEvent(String nbID, int batteryID, int eventType) throws SQLException {
		String sql = "SELECT * FROM Event WHERE NBID='"+nbID+"' and BatteryID="+batteryID+" and EventType="+eventType+" and EventStatus="+EventStatus_Unsolved;
		return executeQuery(sql);
	}
}
