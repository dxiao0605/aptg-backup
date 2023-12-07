package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.model.BatteryRecordSummaryModel;

public class BatteryRecordSummaryDao extends BaseDao2 {
		
	public List<Integer> insertCollection(List<BatteryRecordSummaryModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO BatteryRecordSummary ( "
				   + " NBID, BatteryID, RecTime, UploadStamp, TimeZone, "
				   + " maxIR, minIR, maxVol, minVol, temperature, status "
				   + ") "
				   + "VALUES "
				   + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (BatteryRecordSummaryModel model: list) {
			ps.setString(1, model.getNbid());
			ps.setInt(2, model.getBatteryID());
			ps.setString(3, model.getRecTime());
			ps.setInt(4, model.getUploadStamp());
			ps.setInt(5, model.getTimeZone());

			ps.setBigDecimal(6, model.getMaxIR());
			ps.setBigDecimal(7, model.getMinIR());
			ps.setBigDecimal(8, model.getMaxVol());
			ps.setBigDecimal(9, model.getMinVol());
			ps.setBigDecimal(10, model.getTemperature());
			ps.setInt(11, model.getStatus());
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
//		System.out.println("************ BatteryRecordSummary sql: "+ps.toString());

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
}
