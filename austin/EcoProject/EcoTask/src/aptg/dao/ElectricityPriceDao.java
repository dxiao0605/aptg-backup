package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class ElectricityPriceDao extends BaseDao2 {

	public List<DynaBean> queryElectricityPrice() throws SQLException {
		String sql = "SELECT * FROM ElectricityPrice";
		return executeQuery(sql);
	}
}
