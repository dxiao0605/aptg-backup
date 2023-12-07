package aptg.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;

public class BatteryDetailDao extends BaseDao2 {
	
	public int updateBatteryDetail(List<Object> list, String nbID, int batteryID, int category) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE BatteryDetail SET CorrectionValue=? WHERE NBID=? and BatteryID=? and OrderNo=? and Category=?";

		int count = 0;
		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (int i=0; i<list.size(); i++) {
				int orderNo = i+1;
				
				int correctionValue = (int) list.get(i);
				ps.setBigDecimal(1, new BigDecimal(correctionValue));

				ps.setString(2, nbID);
				ps.setInt(3, batteryID);
				ps.setInt(4, orderNo);
				ps.setInt(5, category);
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
			count = batchUpdate(connection, ps, sqlList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
