package aptg.fixdata.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class Compare2Dao extends BaseDao2 {

	public List<DynaBean> queryCompare2() throws SQLException {
		String sql = "select * from Record_Compare2";
		return executeQuery(sql);
	}
}
