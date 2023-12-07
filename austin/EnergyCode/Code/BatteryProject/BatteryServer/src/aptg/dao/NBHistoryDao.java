package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.bean.DelNBIDBean;
import aptg.dao.base.BaseDao2;

public class NBHistoryDao extends BaseDao2 {
	
	private static final int Already_Deleted = 24;

	public List<Integer> insertNBHistory(List<DelNBIDBean> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		String sql = "INSERT INTO NBHistory (NBID, ModifyItem) VALUES (?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		for (DelNBIDBean del: list) {
			ps.setString(1, del.getNBID());
			ps.setInt(2, Already_Deleted);
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
		return batchInsert(connection, ps, sqlList);
	}
}
