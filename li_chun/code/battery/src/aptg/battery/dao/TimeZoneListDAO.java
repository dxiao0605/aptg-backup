package aptg.battery.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class TimeZoneListDAO extends BaseDAO{
	/**
	 * 取得時區
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getTimeZoneList() throws SQLException {
		String sql = " select TimeZone, TimeZoneDesc from TimeZoneList "; 
						
		return this.executeQuery(sql, null);
	}
	
}
