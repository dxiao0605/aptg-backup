package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class MeterSetupDao extends BaseDao2 {

	public List<DynaBean> queryAllEnabledMeter() throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE Enabled = 1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryEnabledMeter(String deviceID) throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE DeviceID = '"+deviceID+"' and Enabled = 1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryAllEnabledMeterUsageCode1() throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE Enabled = 1 and UsageCode = 1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryAllEnabledMeterUsageCode1(String powerAccount) throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE PowerAccount = '"+powerAccount+"' and UsageCode = 1 and Enabled = 1";
		return executeQuery(sql);
	}

	public List<DynaBean> queryAllEnabledMeter(String powerAccount) throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE PowerAccount = '"+powerAccount+"' and Enabled = 1";
		return executeQuery(sql);
	}
	
	/*
	 * for PowerRecordCollection
	 */
	public List<DynaBean> queryEnabledMeterByBankCode(String bankCode) throws SQLException {
		String sql = "select t1.* from MeterSetup t1 inner join (" + 
					 "	SELECT * FROM ControllerSetup where BankCode='"+bankCode+"'" + 
					 ") t2 on t1.ECO5Account = t2.ECO5Account and t1.Enabled=1;";
		return executeQuery(sql);
	}
	/*
	 * for UsageCode=1
	 */
	public List<DynaBean> queryEnabledMeterUsageCode1ByBankCode(String bankCode) throws SQLException {
		String sql = "select t1.* from MeterSetup t1 inner join (" + 
					 "	SELECT * FROM ControllerSetup where BankCode='"+bankCode+"'" + 
					 ") t2 on t1.ECO5Account = t2.ECO5Account and t1.Enabled=1 and t1.UsageCode=1;";
		return executeQuery(sql);
	}
	
	/**
	 * 
	 * @param deviceID
	 * @param repriceStatus
	 * @return
	 * @throws SQLException
	 */
	public int updateRepriceStatus(String deviceID, int repriceStatus) throws SQLException {
		String sql = "UPDATE MeterSetup SET RepriceStatus="+repriceStatus +" where DeviceID='"+deviceID+"'";
		return executeUpdate(sql);
	}
}
