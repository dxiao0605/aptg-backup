package aptg.battery.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class StatusListDAO extends BaseDAO{
	/**
	 * 取得狀態
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getStatusList() throws SQLException {
		String sql = " select StatusCode, StatusDesc from StatusList "; 
						
		return this.executeQuery(sql, null);
	}
	
}
