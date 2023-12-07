package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class ElectricityTimeDao extends BaseDao2 {

	public List<DynaBean> queryElectricityTime(int summer, int dayOfweek) throws SQLException {
		String sql = "SELECT * FROM ElectricityTime WHERE Summer="+summer+" and DayOfWeek="+dayOfweek;
		return executeQuery(sql);
	}
}
