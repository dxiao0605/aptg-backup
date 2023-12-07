package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.MeterEventRecordModel;

public class MeterEventRecordDao extends BaseDao2 {

	public List<Integer> insertRecord(MeterEventRecordModel record) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO MeterEventRecord (ECO5Account, DeviceID, EventTime, EventCode, EventDesc) VALUES "
					+ "(?, ?, ?, ?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		ps.setString(1, record.getEco5Account());
		ps.setString(2, record.getDeviceID());
		ps.setString(3, record.getEventTime());
		ps.setInt(4, record.getEventCode());
		ps.setString(5, record.getEventDesc());

		ps.addBatch();
		sqlList.add(ps.toString());

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
	
	public List<DynaBean> queryRecordByMeter(String deviceID, int eventCode) throws SQLException {
		String sql = "SELECT * FROM MeterEventRecord WHERE DeviceID = '"+deviceID+"' and EventCode = "+eventCode+" order by EventTime DESC";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryRecordByEco5(String eco5Account, int eventCode) throws SQLException {
		String sql = "SELECT * FROM MeterEventRecord WHERE ECO5Account = '"+eco5Account+"' and EventCode = "+eventCode+" order by EventTime DESC";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryDissconectedEco5orDevice(int eventCode) throws SQLException {
		String sql = "SELECT * FROM MeterEventRecord WHERE EventCode = "+eventCode+" order by EventTime DESC";
		return executeQuery(sql);
	}
}
