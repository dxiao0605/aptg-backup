package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class CompanyDao extends BaseDao2 {

	public List<DynaBean> queryCompany() throws SQLException {
		String sql = "SELECT * FROM Company";
		return executeQuery(sql);
	}

	public List<DynaBean> queryCompany(int companyCode) throws SQLException {
		String sql = "SELECT * FROM Company WHERE CompanyCode = "+companyCode;
		return executeQuery(sql);
	}
}
