package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aptg.bean.DelNBIDBean;
import aptg.dao.base.BaseDao2;
import aptg.utils.ToolUtil;

public class DelNBIDDao extends BaseDao2 {

	public int delNBID(List<DelNBIDBean> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();

		// ===== 1. BatteryDetail =====
		String sql1 = "Delete from Battery where NBID=?";
		PreparedStatement ps1 = initPreparedStatement(connection, sql1);
		for (DelNBIDBean del: list) {
			ps1.setString(1, del.getNBID());

			ps1.addBatch();
			sqlList.add(ps1.toString());
		}
		psList.add(ps1);
		
		// ===== 2. BatteryDetail =====
		String sql2 = "Delete from BatteryDetail where NBID=?";
		PreparedStatement ps2 = initPreparedStatement(connection, sql2);
		for (DelNBIDBean del: list) {
			ps2.setString(1, del.getNBID());

			ps2.addBatch();
			sqlList.add(ps2.toString());
		}
		psList.add(ps2);

		// ===== 4. BatteryRecordSummary =====
		String sql4 = "Delete from BatteryRecordSummary where NBID=?";
		PreparedStatement ps4 = initPreparedStatement(connection, sql4);
		for (DelNBIDBean del: list) {
			ps4.setString(1, del.getNBID());

			ps4.addBatch();
			sqlList.add(ps4.toString());
		}
		psList.add(ps4);

		// ===== 5. CommandTask =====
		String sql5 = "Delete from CommandTask where NBID=?";
		PreparedStatement ps5 = initPreparedStatement(connection, sql5);
		for (DelNBIDBean del: list) {
			ps5.setString(1, del.getNBID());

			ps5.addBatch();
			sqlList.add(ps5.toString());
		}
		psList.add(ps5);

		// ===== 6. Command =====
		String sql6 = "Delete from Command where NBID=?";
		PreparedStatement ps6 = initPreparedStatement(connection, sql6);
		for (DelNBIDBean del: list) {
			ps6.setString(1, del.getNBID());

			ps6.addBatch();
			sqlList.add(ps6.toString());
		}
		psList.add(ps6);

		// ===== 7. Event =====
		String sql7 = "Delete from Event where NBID=?";
		PreparedStatement ps7 = initPreparedStatement(connection, sql7);
		for (DelNBIDBean del: list) {
			ps7.setString(1, del.getNBID());

			ps7.addBatch();
			sqlList.add(ps7.toString());
		}
		psList.add(ps7);

		// ===== 9. BattMaxRecTime =====
		String sql9 = "Delete from BattMaxRecTime where NBID=?";
		PreparedStatement ps9 = initPreparedStatement(connection, sql9);
		for (DelNBIDBean del: list) {
			ps9.setString(1, del.getNBID());

			ps9.addBatch();
			sqlList.add(ps9.toString());
		}
		psList.add(ps9);
		
		// ===== 8. NBGroupHis =====
		String sql8 = "Update NBGroupHis set EndTime=? where NBID=? and EndTime>?";
		PreparedStatement ps8 = initPreparedStatement(connection, sql8);
		for (DelNBIDBean del: list) {
			String now = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			
			ps8.setString(1, now);
			ps8.setString(2, del.getNBID());
			ps8.setString(3, now);

			ps8.addBatch();
			sqlList.add(ps8.toString());
		}
		psList.add(ps8);
		
		
		return batchUpdate(connection, psList, sqlList);
	}
}
