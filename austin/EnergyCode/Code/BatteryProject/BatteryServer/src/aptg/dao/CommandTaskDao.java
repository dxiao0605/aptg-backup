package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.utils.ToolUtil;

public class CommandTaskDao extends BaseDao2 {

	public List<DynaBean> joinCommandCommandTask(String transactionID) throws SQLException {
		String sql = "SELECT * FROM ( SELECT * FROM Command WHERE TransactionID = '"+transactionID+"' ) t1 inner join " +
					 "CommandTask t2 on t1.TaskID = t2.TaskID";
		return executeQuery(sql);
	}
	
	public int updateCommandTask(String taskID, int status, Date reqTime) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE CommandTask SET TaskStatus=?, ReqTime=? WHERE TaskID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, status);
		ps.setString(2, ToolUtil.getInstance().convertDateToString(reqTime, "yyyy-MM-dd HH:mm:ss"));
		ps.setString(3, taskID);
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
	
}
