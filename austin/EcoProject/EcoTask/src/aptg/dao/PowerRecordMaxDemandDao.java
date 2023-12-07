package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.models.PowerRecordMaxDemandModel;
import aptg.models.RepriceTaskModel;
import aptg.utils.ToolUtil;

public class PowerRecordMaxDemandDao extends BaseDao2 {

	/**
	 * 
	 * @param deviceID
	 * @param date: yyyy-MM-dd
	 * @throws SQLException
	 */
	public void delByDeviceIDRecTime(String deviceID, String date) throws SQLException {
		String sql = "DELETE FROM PowerRecordMaxDemand WHERE DeviceID = '"+deviceID+"' and RecTime like '%"+date+"%'";
		executeUpdate(sql);
	}
//	public int delMaxDemand(List<PowerRecordMaxDemandModel> list) throws SQLException {
//		List<String> sqlList = new ArrayList<>();
//		
//		String sql = "Delete from PowerRecordMaxDemand where DeivceID = ? and RecTime like %?%";
//
//		Connection connection = initConnection();
//		PreparedStatement ps = initPreparedStatement(connection, sql);
//		
//		for (PowerRecordMaxDemandModel max: list) {
//			Date recDate = ToolUtil.getInstance().convertStringToDate(max.getRecTime(), "yyyy-MM-dd");
//			String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd");
//			
//			ps.setString(1, max.getDeviceID());
//			ps.setString(2, date);
//
//			ps.addBatch();
//			sqlList.add(ps.toString());
//		}
//		return batchUpdate(connection, ps, sqlList);
//	}
	
	public List<Integer> insertMaxDemand(List<PowerRecordMaxDemandModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO PowerRecordMaxDemand (DeviceID, RecTime, TotalDemand) " +
					 "VALUES (?, ?, ?)";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (PowerRecordMaxDemandModel record: list) {
				ps.setString(1, record.getDeviceID());
				ps.setString(2, record.getRecTime());
				ps.setBigDecimal(3, record.getMaxDemand());
				
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
