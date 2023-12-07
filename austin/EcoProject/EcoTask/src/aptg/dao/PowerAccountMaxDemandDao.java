package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.models.PowerAccountMaxDemandModel;

public class PowerAccountMaxDemandDao extends BaseDao2 {

	/**
	 * 
	 * @param powerAccount
	 * @param date: yyyy-MM-dd
	 * @throws SQLException
	 */
	public void delByPowerAccountRecTime(String powerAccount, String date) throws SQLException {
		String sql = "DELETE FROM PowerAccountMaxDemand WHERE PowerAccount = '"+powerAccount+"' and RecTime like '%"+date+"%'";
		executeUpdate(sql);
	}

	public List<Integer> insertMaxDemand(List<PowerAccountMaxDemandModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO PowerAccountMaxDemand (PowerAccount, RecTime, TotalDemand) " +
					 "VALUES (?, ?, ?)";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (PowerAccountMaxDemandModel record: list) {
				ps.setString(1, record.getPowerAccount());
				ps.setString(2, record.getRecTime());
				ps.setBigDecimal(3, record.getTotalDemand());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
			
			return batchInsert(connection, ps, sqlList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
