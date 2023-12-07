package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class ControllerSetupDao extends BaseDao2 {

	public List<DynaBean> queryAllEnabledECO5Account() throws SQLException {
		String sql = "SELECT * FROM ControllerSetup where Enabled=1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryAllEnabledECO5AccountMeter() throws SQLException {
		String sql = "SELECT t2.* FROM (SELECT * FROM ControllerSetup where Enabled=1) t1 " + 
					 "inner join MeterSetup t2 " + 
					 "on t1.ECO5Account = t2.ECO5Account";
		return executeQuery(sql);
	}
}
