package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class NBListDao extends BaseDao2 {
	
	public List<DynaBean> queryByNBID(String nbID) throws SQLException {
		String sql = "SELECT * FROM NBList WHERE NBID = '"+nbID+"'";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryByGroupInternalID(int groupInternalID) throws SQLException {
		String sql = "SELECT * FROM NBList WHERE GroupInternalID = "+groupInternalID;
		return executeQuery(sql);
	}
}
