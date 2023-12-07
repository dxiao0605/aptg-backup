package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;

public class DeletedPowerAccountHistoryDao extends BaseDao2 {

	public List<Integer> insertDeletedPowerAccountHistory() throws SQLException {
		List<String> sqlList = new ArrayList<>();

		String sql = "";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		ps.addBatch();
		sqlList.add(ps.toString());

		return batchInsert(connection, ps, sqlList);
	}
}
