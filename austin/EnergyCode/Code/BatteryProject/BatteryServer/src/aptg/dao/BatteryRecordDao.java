package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.model.BattMaxRecTimeModel;
import aptg.model.BatteryRecordDataModel;
import aptg.model.BatteryRecordModel;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.EventModel;

public class BatteryRecordDao extends BaseDao2 {

	public List<Integer> insertRecord(List<BatteryRecordModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO BatteryRecord ( "
				   + " NBID, BatteryID, RecTime, UploadStamp, TimeZone, "
				   + " Category, OrderNo, Value, Status"
				   + ") "
				   + "VALUES "
				   + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		for (BatteryRecordModel model: list) {
			/*
			 * 內阻
			 */
			List<BatteryRecordDataModel> iList = model.getiList();
			for (BatteryRecordDataModel data: iList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("************ (1) sql: "+ps.toString());
			
			/*
			 * 電壓
			 */
			List<BatteryRecordDataModel> vList = model.getvList();
			for (BatteryRecordDataModel data: vList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("************ (2) sql: "+ps.toString());
			
			/*
			 * 溫度
			 */
			List<BatteryRecordDataModel> tList = model.gettList();
			for (BatteryRecordDataModel data: tList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("************ (3) sql: "+ps.toString());
		}

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
	
	public int insertB168(List<BatteryRecordModel> recordList, List<BatteryRecordSummaryModel> summaryList, List<EventModel> eventList, List<BattMaxRecTimeModel> battMaxRecList) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();

		/*
		 * Record
		 */
		String sql1 = "INSERT INTO BatteryRecord ( " +
						  " NBID, BatteryID, RecTime, UploadStamp, TimeZone, " +
						  " Category, OrderNo, Value, Status" +
					  ") " +
					  "VALUES " +
					  "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement ps = initPreparedStatement(connection, sql1);
		for (BatteryRecordModel model: recordList) {
			/*
			 * 內阻
			 */
			List<BatteryRecordDataModel> iList = model.getiList();
			for (BatteryRecordDataModel data: iList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("************ (1) sql: "+ps.toString());
			
			/*
			 * 電壓
			 */
			List<BatteryRecordDataModel> vList = model.getvList();
			for (BatteryRecordDataModel data: vList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("************ (2) sql: "+ps.toString());
			
			/*
			 * 溫度
			 */
			List<BatteryRecordDataModel> tList = model.gettList();
			for (BatteryRecordDataModel data: tList) {
				ps.setString(1, model.getNbID());
				ps.setInt(2, model.getBatteryID());
				ps.setString(3, model.getRecTime());
				ps.setInt(4, model.getUploadStamp());
				ps.setInt(5, model.getTimeZone());
				
				ps.setInt(6, data.getCategory());
				ps.setInt(7, data.getOrderNo());
				ps.setBigDecimal(8, data.getValue());
				ps.setInt(9, data.getStatus());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
		}
		psList.add(ps);

		/*
		 * Summary
		 */
		String sql2 = "INSERT INTO BatteryRecordSummary ( " +
						  " NBID, BatteryID, RecTime, UploadStamp, TimeZone, " +
						  " maxIR, minIR, maxVol, minVol, temperature, status " +
					  ") VALUES " +
					  "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement ps2 = initPreparedStatement(connection, sql2);
		for (BatteryRecordSummaryModel model: summaryList) {
			ps2.setString(1, model.getNbid());
			ps2.setInt(2, model.getBatteryID());
			ps2.setString(3, model.getRecTime());
			ps2.setInt(4, model.getUploadStamp());
			ps2.setInt(5, model.getTimeZone());

			ps2.setBigDecimal(6, model.getMaxIR());
			ps2.setBigDecimal(7, model.getMinIR());
			ps2.setBigDecimal(8, model.getMaxVol());
			ps2.setBigDecimal(9, model.getMinVol());
			ps2.setBigDecimal(10, model.getTemperature());
			ps2.setInt(11, model.getStatus());
			
			ps2.addBatch();
			sqlList.add(ps2.toString());
		}
		psList.add(ps2);

		/*
		 * BattMaxRecTime
		 */
		String sql4 = "INSERT INTO BattMaxRecTime " + 
					  	"	(NBID, BatteryID, CompanyCode, MaxRecTime) " + 
					  "VALUES" + 
					  	"	(?, ?, ?, ?) " + 
					  "ON DUPLICATE KEY UPDATE " + 
						"   MaxRecTime = VALUES(MaxRecTime);";
		PreparedStatement ps4 = initPreparedStatement(connection, sql4);
		for (BattMaxRecTimeModel max: battMaxRecList) {
			ps4.setString(1, max.getNbid());
			ps4.setInt(2, max.getBatteryID());
			ps4.setInt(3, max.getCompanyCode());
			ps4.setString(4, max.getMaxRecTime());
			
			ps4.addBatch();
			sqlList.add(ps4.toString());
		}
		psList.add(ps4);
		
		/*
		 * Event
		 */
		String sql3 = "INSERT INTO Event ( " + 
						 "NBID, " + 
						 "BatteryID, " + 
						 "RecordTime, " + 
						 "EventType, " + 
						 "IMPType, " + 
						 "Alert1, " + 
						 "Alert2, " + 
						 "Disconnect, " +
						 "Temperature1, "+
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
		
		PreparedStatement ps3 = initPreparedStatement(connection, sql3);
		for (EventModel event: eventList) {
			ps3.setString(1, event.getRecordTime());
			ps3.setInt(2, event.getEventType());
			ps3.setInt(3, event.getImpType());
			ps3.setBigDecimal(4, event.getAlert1());
			ps3.setBigDecimal(5, event.getAlert2());
			ps3.setLong(6, event.getDisconnect());
			
			if (event.getTemperature1()!=null)
				ps3.setInt(7, event.getTemperature1());
			else
				ps3.setNull(7, java.sql.Types.INTEGER);
			
			ps3.setInt(8, event.getTimeZone());
			
			ps3.setString(9, event.getNbID());
			ps3.setInt(10, event.getBatteryID());
			
			ps3.addBatch();
			sqlList.add(ps3.toString());
//			logger.info("@@@@@@@@@@@@@@@@@@@ Event: "+ps3.toString());
		}
		psList.add(ps3);
		
		return batchUpdate(connection, psList, sqlList);
	}
}
