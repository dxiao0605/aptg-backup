package aptg.battery.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class LanguageListDAO extends BaseDAO{
	/**
	 * 取得語系
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getLanguageList() throws SQLException {
		String sql = " select LanguageCode, LanguageDesc from LanguageList "; 
						
		return this.executeQuery(sql, null);
	}
	
}
