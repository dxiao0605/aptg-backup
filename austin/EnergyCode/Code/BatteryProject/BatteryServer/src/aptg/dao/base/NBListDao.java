package aptg.dao.base;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class NBListDao extends BaseDao2 {

	public List<DynaBean> queryNBListGroup(String nbID) throws SQLException {
		String sql = "SELECT * FROM NBList WHERE NBID = '"+nbID+"'";
		return executeQuery(sql);
	}
}
