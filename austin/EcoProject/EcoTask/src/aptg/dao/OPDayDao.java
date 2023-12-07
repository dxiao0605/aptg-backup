package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class OPDayDao extends BaseDao2 {

	public List<DynaBean> queryOPDay(String opday) throws SQLException {
		String sql = "SELECT * FROM OPDayList WHERE OPDay = '"+opday+"'";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryOPDayByYear(int year) throws SQLException {
		String sql = "Select * From OPDayList where OPDay>str_to_date('"+year+"', '%Y');";
		return executeQuery(sql);
	}
}
