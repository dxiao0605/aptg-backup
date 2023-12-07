package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import aptg.dao.base.BaseDao2;
import aptg.models.RepriceTaskModel;

public class RepriceTaskDao extends BaseDao2 {

	// BGTask & Reprice Status
	public static final int TaskStatusCode_Waiting 		= 0;	// 待處理
	public static final int TaskStatusCode_Executing 	= 1;	// 處理中
	public static final int TaskStatusCode_Finish 		= 2;	// 完成
	public static final int TaskStatusCode_Cancel 		= 3;	// 取消
	public static final int TaskStatusCode_Failed 		= 4;	// 失敗

	public static final String RepriceFrom_Collection = "Collection";
	
	/**
	 * 
	 * @param statusCode: 0 待處理，1 處理中，2 計算完成，3 取消
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> queryTaskByStatus(int statusCode) throws SQLException {
		String sql = "SELECT * FROM RepriceTask where StatusCode = "+statusCode;
		return executeQuery(sql);
	}

	/**
	 *	更新StatusCode: 待處理(0) => 執行中(1)
	 * 
	 * @param list
	 * @param statusCode
	 * @return
	 * @throws SQLException
	 */
	public int updateStatusCodeExecuting(List<RepriceTaskModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE RepriceTask SET StatusCode="+TaskStatusCode_Executing+", UpdateUserName='EcoTask' where Seqno=?;";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		for (RepriceTaskModel rp: list) {
			ps.setInt(1, rp.getSeqno());

			ps.addBatch();
			sqlList.add(ps.toString());
		}

		return batchUpdate(connection, ps, sqlList);
	}
	public int updateStatusCodeCancel(List<RepriceTaskModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE RepriceTask SET StatusCode="+TaskStatusCode_Cancel+", UpdateUserName='EcoTask' where Seqno=?;";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		for (RepriceTaskModel rp: list) {
			ps.setInt(1, rp.getSeqno());

			ps.addBatch();
			sqlList.add(ps.toString());
		}

		return batchUpdate(connection, ps, sqlList);
	}
	
	public int updateRepriceTaskStatus(int statusCode, String deviceID, String powerAccount, String startDate) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		String sql;
//		if (repriceFrom.equals(RepriceFrom_Collection)) {
//			sql = "UPDATE RepriceTask SET StatusCode=? where DeviceID=? and StartDate>=? and RepriceFrom=? and StatusCode=1";
//		} else {
//			sql = "UPDATE RepriceTask SET StatusCode=? where PowerAccount=? and StartDate>=? and RepriceFrom=? and StatusCode=1";
//		}

		Connection connection = initConnection();
		PreparedStatement ps;
		if (StringUtils.isNotBlank(deviceID)) {
			sql = "Update RepriceTask Set StatusCode=?, UpdateUserName='EcoTask' where DeviceID=? and PowerAccount=? and StartDate>=? and StatusCode=1";
			ps = initPreparedStatement(connection, sql);
			ps.setInt(1, statusCode);
			ps.setString(2, deviceID);
			ps.setString(3, powerAccount);
			ps.setString(4, startDate);
			
		} else {
			sql = "Update RepriceTask Set StatusCode=?, UpdateUserName='EcoTask' where DeviceID is Null and PowerAccount=? and StartDate>=? and StatusCode=1";
			ps = initPreparedStatement(connection, sql);
			ps.setInt(1, statusCode);
			ps.setString(2, powerAccount);
			ps.setString(3, startDate);
		}
		ps.addBatch();
		sqlList.add(ps.toString());
//		System.out.println("############ sql: "+ps.toString());

		return batchUpdate(connection, ps, sqlList);
	}
	public int deleteRepriceTaskInfo(RepriceTaskModel rp) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();
		String sql = null;
		
		/*
		 * PowerRecordCollection
		 */
		if (rp.getRepriceFrom().equals(RepriceFrom_Collection)) {
			sql = "Delete From PowerRecordCollection Where DeviceID=? and RecDate>=?";
			PreparedStatement ps1 = initPreparedStatement(connection, sql);
			ps1.setString(1, rp.getDeviceID());
			ps1.setString(2, rp.getStartDate());
			
			ps1.addBatch();
			sqlList.add(ps1.toString());
			psList.add(ps1);
			
//			System.out.println("### 1) "+ps1.toString());
		}
		
		/*
		 * FcstCharge
		 */
		sql = "Delete From FcstCharge Where PowerAccount=? and useTime>=?";
		PreparedStatement ps2 = initPreparedStatement(connection, sql);
		ps2.setString(1, rp.getPowerAccount());
		ps2.setString(2, rp.getStartDate());
		ps2.addBatch();
		sqlList.add(ps2.toString());
		psList.add(ps2);
		
//		System.out.println("### 2) "+ps2.toString());

		/*
		 * PowerMonth
		 */
		sql = "Delete From PowerMonth Where PowerAccount=? and useMonth>=?";
		PreparedStatement ps3 = initPreparedStatement(connection, sql);
		ps3.setString(1, rp.getPowerAccount());
		ps3.setString(2, rp.getStartDate().replace("-", "").substring(0, 6));	// ex: 202102
		ps3.addBatch();
		sqlList.add(ps3.toString());
		psList.add(ps3);
		
//		System.out.println("### 3) "+ps3.toString());
		
		/*
		 * BestRatePlan
		 */
		sql = "Delete From BestRatePlan Where PowerAccount=? and useMonth>=?";
		PreparedStatement ps4 = initPreparedStatement(connection, sql);
		ps4.setString(1, rp.getPowerAccount());
		ps4.setString(2, rp.getStartDate().replace("-", "").substring(0, 6));	// ex: 202102
		ps4.addBatch();
		sqlList.add(ps4.toString());
		psList.add(ps4);

//		System.out.println("### 4) "+ps4.toString());
		
		/*
		 * BestCC
		 */
		sql = "Delete From BestCC Where PowerAccount=? and useMonth>=?";
		PreparedStatement ps5 = initPreparedStatement(connection, sql);
		ps5.setString(1, rp.getPowerAccount());
		ps5.setString(2, rp.getStartDate().replace("-", "").substring(0, 6));	// ex: 202102
		ps5.addBatch();
		sqlList.add(ps5.toString());
		psList.add(ps5);
		
//		System.out.println("### 5) "+ps5.toString());
		
		return batchUpdate(connection, psList, sqlList);
	}
}
