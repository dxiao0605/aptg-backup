package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class PowerAccountDao extends BaseDao2 {

	public List<DynaBean> queryPowerAccount() throws SQLException {
		String sql = "SELECT * FROM PowerAccount";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryPowerAccount(String powerAccount) throws SQLException {
		String sql = "SELECT * FROM PowerAccount where PowerAccount = '"+powerAccount+"'";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryPowerAccountByBankCode(String bankCode) throws SQLException {
		String sql = "SELECT * FROM PowerAccount where BankCode='"+bankCode+"'";
		return executeQuery(sql);
	}
	
	public int updateModifyStatus(String powerAccount, int modifyStatus) throws SQLException {
		String sql = "UPDATE PowerAccount SET ModifyStatus = "+modifyStatus +" where PowerAccount = '"+powerAccount+"'";
		return executeUpdate(sql);
	}
}
