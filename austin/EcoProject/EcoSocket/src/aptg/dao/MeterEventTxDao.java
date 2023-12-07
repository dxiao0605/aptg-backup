package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.MeterEventTxModel;

public class MeterEventTxDao extends BaseDao2 {

	public List<DynaBean> queryDisconnectedDevice() throws SQLException {
		String sql = "SELECT t2.* FROM ( " + 
					 "	SELECT * FROM MeterEventTx WHERE EventStatus = 0 and EventCode=1 " + 
					 ") t1 inner join MeterEventRecord t2 on t1.OpenSeqno = t2.seqno;";
		return executeQuery(sql);
	}
	public List<DynaBean> queryDisconnectedEco5() throws SQLException {
		String sql = "SELECT t2.* FROM ( " + 
					 "	SELECT * FROM MeterEventTx WHERE EventStatus = 0 and EventCode=3 " + 
					 ") t1 inner join MeterEventRecord t2 on t1.OpenSeqno = t2.seqno;";
		return executeQuery(sql);
	}
	
	public List<Integer> insertEventTx(MeterEventTxModel tx) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		String sql = "INSERT INTO MeterEventTx (ECO5Account, DeviceID, OpenTime, CloseTime, EventCode, Priority, EventStatus, OpenSeqno, CloseSeqno) VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		ps.setString(1, tx.getEco5Account());
		ps.setString(2, tx.getDeviceID());
		ps.setString(3, tx.getOpenTime());
		ps.setString(4, tx.getCloseTime());
		ps.setInt(5, tx.getEventCode());
		ps.setInt(6, tx.getPriority());
		ps.setInt(7, tx.getEventStatus());
		
		if (tx.getOpenSeqno()!=null)
			ps.setInt(8, tx.getOpenSeqno());
		else
			ps.setNull(8, java.sql.Types.INTEGER);
		
		if (tx.getCloseSeqno()!=null)
			ps.setInt(9, tx.getCloseSeqno());
		else
			ps.setNull(9, java.sql.Types.INTEGER);
		
		ps.addBatch();
		sqlList.add(ps.toString());

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
	
	public int updateEventTxClose(MeterEventTxModel tx) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE MeterEventTx SET EventStatus=?, CloseTime=?, CloseSeqno=? WHERE OpenSeqno=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, tx.getEventStatus());
		ps.setString(2, tx.getCloseTime());
		ps.setInt(3, tx.getCloseSeqno());
		ps.setInt(4, tx.getOpenSeqno());

		ps.addBatch();
		sqlList.add(ps.toString());

		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
}
