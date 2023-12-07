package aptg.cathaybkeco.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class SysConfigDAO extends BaseDAO{
	/**
	 * 取得系統參數設定檔
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getSysConfig() throws SQLException {
		String sql = " select ParamName, ParamValue from SysConfig "; 
						
		return this.executeQuery(sql, null);
	}
	
}
