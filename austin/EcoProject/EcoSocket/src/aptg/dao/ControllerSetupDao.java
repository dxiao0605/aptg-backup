package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class ControllerSetupDao extends BaseDao2 {

	public List<DynaBean> queryAllEnabledEco5() throws SQLException {
		String sql = "SELECT * FROM ControllerSetup WHERE Enabled = 1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryEnabledEco5(String eco5Account) throws SQLException {
		String sql = "SELECT * FROM ControllerSetup WHERE ECO5Account = '"+eco5Account+"' and Enabled = 1";
		return executeQuery(sql);
	}
}
