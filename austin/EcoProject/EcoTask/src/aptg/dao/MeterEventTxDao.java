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

	public List<DynaBean> queryEventTx(int eventCode, int priority, int eventStatus) throws SQLException {
		String sql = "SELECT * FROM MeterEventTx WHERE EventCode="+eventCode +" and Priority="+priority +" and EventStatus="+eventStatus;
		return executeQuery(sql);
	}
	
	public int updateEventTx(List<MeterEventTxModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "Update MeterEventTx SET EventStatus=?, CloseTime=? WHERE seqno=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (MeterEventTxModel tx: list) {
			ps.setInt(1, tx.getEventStatus());
			ps.setString(2, tx.getCloseTime());
			ps.setInt(3, tx.getSeqno());

			ps.addBatch();
			sqlList.add(ps.toString());
		}
		return batchUpdate(connection, ps, sqlList);
	}
	
	public List<Integer> insertEventTx(List<MeterEventTxModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO MeterEventTx (ECO5Account, OpenTime, EventCode, Priority, EventStatus) "
				   + "VALUES (?, ?, ?, ?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (MeterEventTxModel tx: list) {
			ps.setString(1, tx.getEco5Account());
			ps.setString(2, tx.getOpenTime());
			ps.setInt(3, tx.getEventCode());
			ps.setInt(4, tx.getPriority());
			ps.setInt(5, tx.getEventStatus());

			ps.addBatch();
			sqlList.add(ps.toString());
		}
		return batchInsert(connection, ps, sqlList);
	}
}
