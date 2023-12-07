package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.model.CommandModel;

public class CommandDao extends BaseDao2 {

	public List<Integer> insertCommand(List<CommandModel> commandList) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO Command ( " +
						 " TransactionID, TaskID, NBID, BatteryID, " +
						 " PublishTime, AckTime, " +
						 " ResponseTime, ResponseCode, ResponseContent " +
					 " ) VALUES " +
					 "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (CommandModel command: commandList) {
			ps.setString(1, command.getTransactionID());
			ps.setString(2, command.getTaskID());
			ps.setString(3, command.getNbID());
			ps.setInt(4, command.getBatteryID());
			ps.setString(5, command.getPublishTime());
			ps.setString(6, command.getAckTime());
			ps.setString(7, command.getResponseTime());
			
			if (command.getResponseCode()!=null)
				ps.setInt(8, command.getResponseCode());
			else
				ps.setNull(8, java.sql.Types.INTEGER);
				
			ps.setString(9, command.getResponseContent());
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
		
		return batchInsert(connection, ps, sqlList);
	}
	
	public int updateCommandAckTime(String transactionID, String ackTime) throws SQLException {
//		String sql = "UPDATE Command SET AckTime = '"+ackTime+"' WHERE TransactionID = '"+transactionID+"'";

		String sql = "UPDATE Command SET AckTime=str_to_date('"+ackTime+"','%Y-%m-%d %H:%i:%s') WHERE TransactionID = '"+transactionID+"'";
		return executeUpdate(sql);
	}
	
	public int updateCommandResp(String transactionID, String respTime, int respCode, String respContent) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE Command SET ResponseTime=?, ResponseCode=?, ResponseContent=? WHERE TransactionID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		ps.setString(1, respTime);
		ps.setInt(2, respCode);
		ps.setString(3, respContent);
		ps.setString(4, transactionID);
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		return batchUpdate(connection, ps, sqlList);
	}
}
