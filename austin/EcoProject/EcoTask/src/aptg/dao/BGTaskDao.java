package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.BGTaskModel;

public class BGTaskDao extends BaseDao2 {

	/**
	 *	修改、刪除電號
	 * 
	 * @param statusCode: 0 待處理，1 處理中，2 計算完成，3 取消
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> queryTaskByStatus(int statusCode) throws SQLException {
		String sql = "SELECT * FROM BGTask where StatusCode = "+statusCode;
		return executeQuery(sql);
	}
	
	public int updateStatus(List<BGTaskModel> list, int statusCode) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE BGTask SET StatusCode = ?, UpdateUserName='EcoTask' where seqno = ?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		for (BGTaskModel bg: list) {
			ps.setInt(1, statusCode);
			ps.setInt(2, bg.getSeqno());

			ps.addBatch();
			sqlList.add(ps.toString());
		}
		
		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
	
	/**
	 * 
	 * insert DeletedPowerAccount
	 * insert DeletedPowerAccountHistory
	 * delete FcstCharge
	 * delete PowerMonth
	 * delete BestRatePlan
	 * delete BestCC
	 * delete PowerAccountMaxDemand
	 * delete EntryBill
	 * delete PowerAccountHistory
	 * delete PowerAccount
	 * 
	 * @param powerAccount
	 * @return
	 * @throws SQLException
	 */
	public int delBGTaskInfo(String powerAccount) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();
		String sql = null;
		
		/*
		 * DeletedPowerAccount
		 */
		sql = "INSERT INTO DeletedPowerAccount " +
		   	  "( " +
		   	 	"PowerAccount, BankCode, CustomerName, AccountDesc, PATypeCode, " +
		   	 	"PowerPhase, PAAddress, " +
		   	 	"CreateTime, CreateUserName, UpdateTime, UpdateUserName " +
		   	  ") " +
		   	  "select " +
		   	 	"PowerAccount, BankCode, CustomerName, AccountDesc, PATypeCode, " +
		   	 	"PowerPhase, PAAddress, " + 
		   	 	"CreateTime, CreateUserName, UpdateTime, UpdateUserName " +
		   	  "from PowerAccount where PowerAccount = ?";
		PreparedStatement ps1 = initPreparedStatement(connection, sql);
		ps1.setString(1, powerAccount);
		ps1.addBatch();
		sqlList.add(ps1.toString());
		psList.add(ps1);
		
		/*
		 * DeletedPowerAccountHistory
		 */
		sql = "INSERT INTO DeletedPowerAccountHistory " +
			  "( " +
				"PowerAccount, ApplyDate, RatePlanCode, " +
				"UsuallyCC, SPCC, SatSPCC, OPCC, " +
				"CreateTime, CreateUserName, UpdateTime, UpdateUserName " +
			  ") " +
			  "select " +
			  	"PowerAccount, ApplyDate, RatePlanCode, " +
				"UsuallyCC, SPCC, SatSPCC, OPCC, " +
				"CreateTime, CreateUserName, UpdateTime, UpdateUserName " +
			  "from PowerAccountHistory where PowerAccount = ?";
		PreparedStatement ps2 = initPreparedStatement(connection, sql);
		ps2.setString(1, powerAccount);
		ps2.addBatch();
		sqlList.add(ps2.toString());
		psList.add(ps2);
		
		/*
		 * FcstCharge
		 */
		sql = "Delete From FcstCharge Where PowerAccount = ?";
		PreparedStatement ps3 = initPreparedStatement(connection, sql);
		ps3.setString(1, powerAccount);
		ps3.addBatch();
		sqlList.add(ps3.toString());
		psList.add(ps3);
		
		/*
		 * PowerMonth
		 */
		sql = "Delete From PowerMonth Where PowerAccount = ?";
		PreparedStatement ps4 = initPreparedStatement(connection, sql);
		ps4.setString(1, powerAccount);
		ps4.addBatch();
		sqlList.add(ps4.toString());
		psList.add(ps4);
		
		/*
		 * BestRatePlan
		 */
		sql = "Delete From BestRatePlan Where PowerAccount = ?";
		PreparedStatement ps5 = initPreparedStatement(connection, sql);
		ps5.setString(1, powerAccount);
		ps5.addBatch();
		sqlList.add(ps5.toString());
		psList.add(ps5);
		
		/*
		 * BestCC
		 */
		sql = "Delete From BestCC Where PowerAccount = ?";
		PreparedStatement ps6 = initPreparedStatement(connection, sql);
		ps6.setString(1, powerAccount);
		ps6.addBatch();
		sqlList.add(ps6.toString());
		psList.add(ps6);
		
		/*
		 * PowerAccountMaxDemand
		 */
		sql = "Delete From PowerAccountMaxDemand Where PowerAccount = ?";
		PreparedStatement ps7 = initPreparedStatement(connection, sql);
		ps7.setString(1, powerAccount);
		ps7.addBatch();
		sqlList.add(ps7.toString());
		psList.add(ps7);
		
		// EntryBill
		sql = "Delete From EntryBill Where PowerAccount = ?";
		PreparedStatement ps8 = initPreparedStatement(connection, sql);
		ps8.setString(1, powerAccount);
		ps8.addBatch();
		sqlList.add(ps8.toString());
		psList.add(ps8);
		
		// PowerAccountHistory
		sql = "Delete From PowerAccountHistory Where PowerAccount = ?";
		PreparedStatement ps9 = initPreparedStatement(connection, sql);
		ps9.setString(1, powerAccount);
		ps9.addBatch();
		sqlList.add(ps9.toString());
		psList.add(ps9);
		
		// RepriceTask (未執行0 => 取消3)
		sql = "Update RepriceTask SET StatusCode=3, UpdateUserName='EcoTask' Where PowerAccount = ? and StatusCode=0";
		PreparedStatement ps10 = initPreparedStatement(connection, sql);
		ps10.setString(1, powerAccount);
		ps10.addBatch();
		sqlList.add(ps10.toString());
		psList.add(ps10);
		
		// PowerAccount
		sql = "Delete From PowerAccount Where PowerAccount = ?";
		PreparedStatement ps11 = initPreparedStatement(connection, sql);
		ps11.setString(1, powerAccount);
		ps11.addBatch();
		sqlList.add(ps11.toString());
		psList.add(ps11);

		return batchUpdate(connection, psList, sqlList);
	}
	
	/**
	 * update "PowerAccount" in Table: 
	 * FcstCharge, PowerMonth, BestRatePlan, BestCC, PowerAccountMaxDemand, EntryBill, PowerAccountHistory, PowerAccount
	 * 
	 * @param oldPowerAccount
	 * @param newPowerAccount
	 * @return
	 * @throws SQLException
	 */
	public int updateBGTaskInfo(String oldPowerAccount, String newPowerAccount, int modifyStatus) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();
		String sql = null;

		// Insert NewPowerAccount & update ModeifyStatus=0(default)
		sql = "INSERT INTO PowerAccount ( " +
			  " PowerAccount, BankCode, CustomerName, AccountDesc, " +
			  " PATypeCode, PowerPhase, PAAddress, ModifyStatus " +
			  ") " +
			  "select " +
			  	"?, t1.BankCode, t1.CustomerName, t1.AccountDesc, " +
			  	"t1.PATypeCode, t1.PowerPhase, t1.PAAddress, ? " +
			  "from PowerAccount t1 where t1.PowerAccount = ?";
		PreparedStatement ps1 = initPreparedStatement(connection, sql);
		ps1.setString(1, newPowerAccount);
		ps1.setInt(2, modifyStatus);
		ps1.setString(3, oldPowerAccount);
		ps1.addBatch();
		sqlList.add(ps1.toString());
		psList.add(ps1);
		
		// MeterSetup
		sql = "Update MeterSetup Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps2 = initPreparedStatement(connection, sql);
		ps2.setString(1, newPowerAccount);
		ps2.setString(2, oldPowerAccount);
		ps2.addBatch();
		sqlList.add(ps2.toString());
		psList.add(ps2);

		// FcstCharge
		sql = "Update FcstCharge Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps3 = initPreparedStatement(connection, sql);
		ps3.setString(1, newPowerAccount);
		ps3.setString(2, oldPowerAccount);
		ps3.addBatch();
		sqlList.add(ps3.toString());
		psList.add(ps3);
		
		// PowerMonth
		sql = "Update PowerMonth Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps4 = initPreparedStatement(connection, sql);
		ps4.setString(1, newPowerAccount);
		ps4.setString(2, oldPowerAccount);
		ps4.addBatch();
		sqlList.add(ps4.toString());
		psList.add(ps4);
		
		// BestRatePlan
		sql = "Update BestRatePlan Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps5 = initPreparedStatement(connection, sql);
		ps5.setString(1, newPowerAccount);
		ps5.setString(2, oldPowerAccount);
		ps5.addBatch();
		sqlList.add(ps5.toString());
		psList.add(ps5);
		
		// BestCC
		sql = "Update BestCC Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps6 = initPreparedStatement(connection, sql);
		ps6.setString(1, newPowerAccount);
		ps6.setString(2, oldPowerAccount);
		ps6.addBatch();
		sqlList.add(ps6.toString());
		psList.add(ps6);
		
		// PowerAccountMaxDemand
		sql = "Update PowerAccountMaxDemand Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps7 = initPreparedStatement(connection, sql);
		ps7.setString(1, newPowerAccount);
		ps7.setString(2, oldPowerAccount);
		ps7.addBatch();
		sqlList.add(ps7.toString());
		psList.add(ps7);
		
		// EntryBill
		sql = "Update EntryBill Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps8 = initPreparedStatement(connection, sql);
		ps8.setString(1, newPowerAccount);
		ps8.setString(2, oldPowerAccount);
		ps8.addBatch();
		sqlList.add(ps8.toString());
		psList.add(ps8);
		
		// PowerAccountHistory
		sql = "Update PowerAccountHistory Set PowerAccount = ? where PowerAccount = ?";
		PreparedStatement ps9 = initPreparedStatement(connection, sql);
		ps9.setString(1, newPowerAccount);
		ps9.setString(2, oldPowerAccount);
		ps9.addBatch();
		sqlList.add(ps9.toString());
		psList.add(ps9);
		
		// RepriceTask (未執行的 old PowerAccount 更新成 new PowerAccount)
		sql = "Update RepriceTask Set PowerAccount = ?, UpdateUserName='EcoTask' where PowerAccount = ? and StatusCode=0";
		PreparedStatement ps10 = initPreparedStatement(connection, sql);
		ps10.setString(1, newPowerAccount);
		ps10.setString(2, oldPowerAccount);
		ps10.addBatch();
		sqlList.add(ps10.toString());
		psList.add(ps10);
		
		// delete OldPowerAccount
		sql = "Delete From PowerAccount where PowerAccount = ?";
		PreparedStatement ps11 = initPreparedStatement(connection, sql);
		ps11.setString(1, oldPowerAccount);
		ps11.addBatch();
		sqlList.add(ps11.toString());
		psList.add(ps11);
		
		return batchUpdate(connection, psList, sqlList);
	}
}
