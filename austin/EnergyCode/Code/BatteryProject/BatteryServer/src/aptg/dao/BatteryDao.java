package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.bean.BABean;
import aptg.bean.BBBean;
import aptg.dao.base.BaseDao2;
import aptg.model.BatteryModel;

public class BatteryDao extends BaseDao2 {

	public List<DynaBean> queryBattery() throws SQLException {
		String sql = "SELECT * FROM Battery";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryByNBID(String nbID) throws SQLException {
		String sql = "SELECT * FROM Battery WHERE NBID = '"+nbID+"' order by BatteryID asc";
		return executeQuery(sql);
	}

	public List<DynaBean> joinNBListBattery(int groupInternalID) throws SQLException {
		String sql = "SELECT * FROM ( SELECT * FROM NBList WHERE GroupInternalID = "+groupInternalID+" ) t1 inner join "
				   + " Battery t2 on t1.NBID = t2.NBID ";
		return executeQuery(sql);
	}
	
	public List<Integer> insertBattery(BatteryModel battery) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO Battery ( "+
						 " NBID, BatteryID " +
					 " ) VALUES " +
					 "(?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setString(1, battery.getNbID());
		ps.setInt(2, battery.getBatteryID());
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		return batchInsert(connection, ps, sqlList);
	}
	
	public int updateBatteryBBValue(BBBean bb, String nbID, int batteryID) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE Battery SET IRTestTime=?, BatteryCapacity=?, CorrectionValue=?, Resistance=? WHERE NBID=? and BatteryID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, bb.getIRTestTime());
		ps.setInt(2, bb.getBatteryCapacity());
		ps.setInt(3, bb.getCorrectionValue());
		ps.setBigDecimal(4, bb.getResistance());

		ps.setString(5, nbID);
		ps.setInt(6, batteryID);
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		return batchUpdate(connection, ps, sqlList);
	}

	public int updateBatteryBAValue(BABean ba, String nbID, int batteryID) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE Battery SET UploadCycle=?, IRCycle=?, CommunicationCycle=? WHERE NBID=? and BatteryID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, ba.getUploadCycle());
		ps.setInt(2, ba.getIRCycle());
		ps.setInt(3, ba.getCommunicationCycle());

		ps.setString(4, nbID);
		ps.setInt(5, batteryID);
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		return batchUpdate(connection, ps, sqlList);
	}

	public int updateBatteryRecords(BatteryModel battery) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE Battery SET IRRecords=?, VRecords=?, TRecords=? WHERE NBID=? and BatteryID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, battery.getIrRecords());
		ps.setInt(2, battery.getvRecords());
		ps.setInt(3, battery.gettRecords());

		ps.setString(4, battery.getNbID());
		ps.setInt(5, battery.getBatteryID());
		
		ps.addBatch();
		sqlList.add(ps.toString());
		
		return batchUpdate(connection, ps, sqlList);
	}
}
