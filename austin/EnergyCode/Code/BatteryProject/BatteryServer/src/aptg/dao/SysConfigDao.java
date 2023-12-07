package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class SysConfigDao extends BaseDao2 {

	public List<DynaBean> querySysConfig() throws SQLException {
		String sql = "SELECT * FROM SysConfig";
		return executeQuery(sql);
	}
	public List<DynaBean> querySysConfig(String paramname) throws SQLException {
		String sql = "SELECT * FROM SysConfig WHERE paramname='"+paramname+"'";
		return executeQuery(sql);
	}
}
