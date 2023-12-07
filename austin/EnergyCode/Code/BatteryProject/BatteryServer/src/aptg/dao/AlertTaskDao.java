package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.utils.ToolUtil;

public class AlertTaskDao extends BaseDao2 {

	private static final int Status_Success = 2;
	private static final String UpdateUserName = "BatterServer";
	
	public int updateAlertTaskSuccess(String taskID) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "Update AlertTask SET TaskStatus=?, ApplyTime=?, UpdateUserName=? where TaskID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, Status_Success);
		ps.setString(2, ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ps.setString(3, UpdateUserName);
		ps.setString(4, taskID);
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
}
