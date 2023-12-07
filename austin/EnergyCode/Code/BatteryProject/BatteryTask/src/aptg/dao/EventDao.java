package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.model.EventModel;

public class EventDao extends BaseDao2 {

	private static final int EventType_Offline = 4;
	private static final int EventStatus_Unsolved = 5;

	public List<DynaBean> queryUnsolvedOfflineEvent() throws SQLException {
		String sql = "SELECT * FROM Event WHERE EventType="+EventType_Offline +" and EventStatus="+EventStatus_Unsolved;
		return executeQuery(sql);
	}
	
	public List<Integer> insertOfflineEvent(List<EventModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		String sql = "INSERT INTO Event ( " + 
						 "NBID, " + 
						 "BatteryID, " + 
						 "RecordTime, " + 
						 "EventType, " + 
						 "IMPType, " + 
						 "Alert1, " + 
						 "Alert2, " + 
						 "Disconnect, " + 
						 "TimeZone, " + 
						 "CompanyCode, " + 
						 "GroupID, " + 
						 "GroupName, " + 
						 "Country, " + 
						 "Area, " + 
						 "Address, " +
						 "Lng, " + 
						 "Lat, " + 
						 "InstallDate, " + 
						 "BatteryTypeName " + 
					 ") " + 
					 "select  " + 
						 "b.NBID, " + 
						 "b.BatteryID, " + 
						 "?, " + 
						 "?, " + 
						 "?, " + 
						 "?, " + 
						 "?, " + 
						 "?, " + 
						 "?, " + 
						 "g.CompanyCode, " + 
						 "g.GroupID, " + 
						 "g.GroupName, " + 
						 "g.Country, " + 
						 "g.Area, " + 
						 "g.Address, " + 
						 "g.Lng, " + 
						 "g.Lat, " + 
						 "b.InstallDate, " + 
						 "bt.BatteryTypeName " + 
					 "from BatteryGroup g,  " + 
					 "NBList n,  " + 
					 "Battery b  " + 
						 "left join BatteryTypeList bt on bt.BatteryTypeCode = b.BatteryTypeCode " + 
						 "where g.SeqNo = n.GroupInternalID " + 
						 "and n.NBID = b.NBID " + 
						 "and b.NBID = ? " + 
						 "and b.BatteryID = ? ";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (EventModel event: list) {
			ps.setString(1, event.getRecordTime());
			ps.setInt(2, event.getEventType());
			ps.setInt(3, event.getImpType());
			ps.setBigDecimal(4, event.getAlert1());
			ps.setBigDecimal(5, event.getAlert2());
			ps.setLong(6, event.getDisconnect());
			ps.setInt(7, event.getTimeZone());
			
			ps.setString(8, event.getNbID());
			ps.setInt(9, event.getBatteryID());
			
			ps.addBatch();
			sqlList.add(ps.toString());
			
//			System.out.println("############ sql: "+ps.toString());
		}

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
}
