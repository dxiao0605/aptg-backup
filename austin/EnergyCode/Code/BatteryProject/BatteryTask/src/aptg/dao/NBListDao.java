package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class NBListDao extends BaseDao2 {

	private static final int Active	= 13;
	
	public List<DynaBean> queryActiveNBList() throws SQLException {
		String sql = "SELECT * FROM NBList where Active="+Active;
		return executeQuery(sql);
	}
}
