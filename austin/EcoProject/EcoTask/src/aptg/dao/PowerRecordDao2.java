package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.PowerRecordModel;
import aptg.utils.ToolUtil;

/*
 * for partition
 */
public class PowerRecordDao2 extends BaseDao2 {
	
	public String insertPowerRecordWithPartition(Map<String, PowerRecordModel> records) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		Connection connection = initConnection();
		List<PreparedStatement> psList = new ArrayList<>();

		String sql = "INSERT INTO PowerRecord "
				   + "(DeviceID, RecTime, "
				   + "I1, I2, I3, Iavg, "
				   + "V1, V2, V3, Vavg, "
				   + "V12, V23, V31, VavgP, "
				   + "W, Var, VA, PF, KWh, Kvarh, Hz, THVavg, THIavg, "
				   + "Mode1, Mode2, Mode3, Mode4, "
				   + "DemandPK, DemandSP, DemandSatSP, DemandOP, "
				   + "MCECPK, MCECSP, MCECSatSP, MCECOP, "
				   + "HighCECPK, HighCECSP, HighCECOP, "
				   + "CreateTime) VALUES "
				   + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		String sql2 = "INSERT INTO PowerRecordCreateTime "
					+ "(DeviceID, RecTime, ConnectTime) VALUES (?, ?, ?);";
		
		for (PowerRecordModel model: records.values()) {
			// sql2: PowerRecord
			PreparedStatement ps = initPreparedStatement(connection, sql);
			ps.setString(1, model.getDeviceID());
			ps.setString(2, model.getRecTime());
			ps.setBigDecimal(3, model.getI1());
			ps.setBigDecimal(4, model.getI2());
			ps.setBigDecimal(5, model.getI3());
			ps.setBigDecimal(6, model.getIavg());
			ps.setBigDecimal(7, model.getV1());
			ps.setBigDecimal(8, model.getV2());
			ps.setBigDecimal(9, model.getV3());
			ps.setBigDecimal(10, model.getVavg());
			ps.setBigDecimal(11, model.getV12());
			ps.setBigDecimal(12, model.getV23());
			ps.setBigDecimal(13, model.getV31());
			ps.setBigDecimal(14, model.getVavgP());
			ps.setBigDecimal(15, model.getW());
			ps.setBigDecimal(16, model.getVar());
			ps.setBigDecimal(17, model.getVA());
			ps.setBigDecimal(18, model.getPF());
			ps.setBigDecimal(19, model.getKWh());
			ps.setBigDecimal(20, model.getKvarh());
			ps.setBigDecimal(21, model.getHz());
			ps.setBigDecimal(22, model.getTHIavg());
			ps.setBigDecimal(23, model.getTHIavg());
			ps.setBigDecimal(24, model.getMode1());
			ps.setBigDecimal(25, model.getMode2());
			ps.setBigDecimal(26, model.getMode3());
			ps.setBigDecimal(27, model.getMode4());
			ps.setBigDecimal(28, model.getDemandPK());
			ps.setBigDecimal(29, model.getDemandSP());
			ps.setBigDecimal(30, model.getDemandSatSP());
			ps.setBigDecimal(31, model.getDemandOP());
			ps.setBigDecimal(32, model.getMCECPK());
			ps.setBigDecimal(33, model.getMCECSP());
			ps.setBigDecimal(34, model.getMCECSatSP());
			ps.setBigDecimal(35, model.getMCECOP());
			ps.setBigDecimal(36, model.getHighCECPK());
			ps.setBigDecimal(37, model.getHighCECSP());
			ps.setBigDecimal(38, model.getHighCECOP());
			ps.setString(39, model.getConnectTime());
			ps.addBatch();
			sqlList.add(ps.toString());
			psList.add(ps);
			
			// sql2: PowerRecordCreateTime
			PreparedStatement ps2 = initPreparedStatement(connection, sql2);
			ps2.setString(1, model.getDeviceID());
			ps2.setString(2, model.getRecTime());
			ps2.setString(3, model.getConnectTime());
			ps2.addBatch();
			sqlList.add(ps2.toString());
			psList.add(ps2);
		}
		
		return batchUpdateReturnMessage(connection, psList, sqlList);
	}

//	public List<DynaBean> queryDailyCollection(String deviceID, String startdate, String enddate) throws SQLException {
////		String sql = "select r.DeviceID, Date(r.RecTime) RecTime from PowerRecordCreateTime c, PowerRecord r " + 
////					 "where c.CreateTime>=str_to_date('"+startdate+"','%Y-%m-%d') and c.CreateTime<str_to_date('"+enddate+"','%Y-%m-%d') " + 
////					 "and c.DeviceID='"+deviceID+"' " + 
////					 "and c.DeviceID=r.DeviceID and c.RecTime=r.RecTime ";
//
//		String sql = "select DeviceID, Min(Date(RecTime)) RecTime from PowerRecordCreateTime " + 
//					 "where CreateTime>=str_to_date('"+startdate+"','%Y-%m-%d') and CreateTime<str_to_date('"+enddate+"','%Y-%m-%d') " + 
//					 "and DeviceID='"+deviceID+"' "; 
//		
//		return executeQuery(sql);
//	}
	public List<DynaBean> queryDailyCollection(String startdate, String enddate) throws SQLException {
		String partition = "c"+enddate.substring(2, enddate.length()).replace("-", "");
		String sql = "select DeviceID, Date(Min(RecTime)) RecTime from PowerRecordCreateTime partition ("+partition+") " + 
					 "where CreateTime>=str_to_date('"+startdate+"','%Y-%m-%d') and CreateTime<str_to_date('"+enddate+"','%Y-%m-%d') group by DeviceID";
		
		return executeQuery(sql);
	}

	// 重算
	public List<DynaBean> queryBasicIVW(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT " 
	  				 +	"Iavg, Vavg, VavgP, W "
					 + "FROM PowerRecord "
					 + "WHERE DeviceID='"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')";
		
		return executeQuery(sql);
	}
//	// 非重算
//	public List<DynaBean> queryBasicIVW_Daily(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT "
//	 				 + "Iavg, Vavg, VavgP, W "
//	 				 + "from PowerRecord "
//	 				 + "where DeviceID='"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')";
// 				 
//		return executeQuery(sql);
//	}
	

	// 重算
	public List<DynaBean> queryMaxDRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT "
						 + "MAX(DemandPK) DemandPK, "
						 + "MAX(DemandSP) DemandSP, "
						 + "MAX(DemandSatSP) DemandSatSP, "
						 + "MAX(DemandOP) DemandOP, "
						 + "MAX(Mode1) Mode1 , MAX(Mode2) Mode2 , MAX(Mode3) Mode3 , MAX(Mode4) Mode4 "
					 + "FROM PowerRecord WHERE DeviceID='"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')";
		
		return executeQuery(sql);
	}
//	// 非重算
//	public List<DynaBean> queryMaxDRecord_Daily(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "select "
//						 + "MAX(DemandPK) DemandPK, "
//						 + "MAX(DemandSP) DemandSP, "
//						 + "MAX(DemandSatSP) DemandSatSP, "
//						 + "MAX(DemandOP) DemandOP, "
//						 + "MAX(Mode1) Mode1 , MAX(Mode2) Mode2 , MAX(Mode3) Mode3 , MAX(Mode4) Mode4 "
//					+ "from PowerRecord where DeviceID='"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')";
//					
//		return executeQuery(sql);
//	}

	/**
	 * 
	 * @param deviceID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws SQLException
	 */
	// 重算
	public List<DynaBean> getSpecifyDateSecondRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
				
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " +
					 "			   KWh " +
					 "		FROM PowerRecord " +
					 "		WHERE DeviceID='"+deviceID+"' "+
					 "				and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d') "+
					 "				order by RecTime asc limit 2 " +
					 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 1. sql: "+sql);
		return executeQuery(sql);
	}
//	// 非重算
//	public List<DynaBean> getSpecifyDateSecondRecord_Daily(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
//					 "	SELECT t2.PowerAccount, t1.* FROM ( " +
//				
//					 "		select DeviceID, Date(RecTime) RecDate, " + 
//					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
//					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " + 
//					 "			   KWh " +
//					 "		from PowerRecord " + 
//					 "		where RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d') " + 
//					 "				and DeviceID='"+deviceID+"' " + 
//					 "				order by RecTime asc limit 2 " +
//					 
//					 "	) t1 inner join MeterSetup t2 " + 
//					 "	on t1.DeviceID = t2.DeviceID " + 
//					 ") t3 inner join PowerAccountHistory t4 " + 
//					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";
//
////		System.out.println("### 1. sql: "+sql);
//		return executeQuery(sql);
//	}
	/**
	 * 
	 * @param deviceID
	 * @param recDate
	 * @return
	 * @throws SQLException
	 */
	// 重算
	public List<DynaBean> getSpecifyDateLastRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
				
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " +
					 "			   KWh " +
					 "		FROM PowerRecord "+
					 "		WHERE DeviceID='"+deviceID+"' "+
					 "				and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d') "+
					 "				order by RecTime desc limit 1 " + 
					 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 1. sql: "+sql);
		return executeQuery(sql);
	}
//	// 非重算
//	public List<DynaBean> getSpecifyDateLastRecord_Daily(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
//					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
//				
//					 "		select DeviceID, Date(RecTime) RecDate, " + 
//					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
//					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " + 
//					 "			   KWh " +
//					 "		from PowerRecord " + 
//					 "		where RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d') " + 
//					 "				and DeviceID='"+deviceID+"' " + 
//					 "				order by RecTime desc limit 1 " +
//					 
//					 "	) t1 inner join MeterSetup t2 " + 
//					 "	on t1.DeviceID = t2.DeviceID " + 
//					 ") t3 inner join PowerAccountHistory t4 " + 
//					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";
//
////		System.out.println("### 1. sql: "+sql);
//		return executeQuery(sql);
//	}
	
	public List<DynaBean> querySpecifyDateNewestRecord(String deviceID, Date recDate) throws SQLException {
		String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM");
		
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
				
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			 DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			 MCECPK, MCECSP, MCECSatSP, MCECOP, " + 
					 "			 KWh " +
					 "		FROM PowerRecord "+
					 "		WHERE DeviceID='"+deviceID+"' "+
					 "				and RecTime<str_to_date('"+ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd")+"','%Y-%m-%d') "+
					 "				and RecTime like '"+date+"%' "+
					 "				order by RecTime desc limit 1 " +
					 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 2. sql: "+sql);
		return executeQuery(sql);
	}
//	public List<DynaBean> querySpecifyDateNewestRecord_Daily(String deviceID, Date recDate) throws SQLException {
//		String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM");
//		
//		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
//					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
//				
//					 "		select r.DeviceID, Date(r.RecTime) RecDate, " + 
//					 "			   r.DemandPK, r.DemandSP, r.DemandSatSP, r.DemandOP, " + 
//					 "			   r.MCECPK, r.MCECSP, r.MCECSatSP, r.MCECOP, " + 
//					 "			   r.KWh " +
//					 "		from PowerRecord r " + 
//					 "		where r.RecTime<str_to_date('"+ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd")+"','%Y-%m-%d') " + 
//					 "				and r.RecTime like '"+date+"%' " +
//					 "				and r.DeviceID='"+deviceID+"' " + 
//					 "				order by r.RecTime desc limit 1 " +
//					 
//					 "	) t1 inner join MeterSetup t2 " + 
//					 "	on t1.DeviceID = t2.DeviceID " + 
//					 ") t3 inner join PowerAccountHistory t4 " + 
//					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";
//
////		System.out.println("### 2. sql: "+sql);
//		return executeQuery(sql);
//	}
	
	/* 
	 * version 1
	 */
//	public List<DynaBean> queryRecordDemand(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT DeviceID, RecTime, (DemandPK + DemandSP + DemandSatSP + DemandOP) AS TotalDemand FROM PowerRecord " + 
//				 	"where DeviceID = '"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
//		return executeQuery(sql);
//	}
//	public List<DynaBean> queryRecordDemand_Daily(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT DeviceID, RecTime, (DemandPK + DemandSP + DemandSatSP + DemandOP) AS TotalDemand " +
//					"FROM PowerRecord " + 
//				 	"where DeviceID = '"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
//		return executeQuery(sql);
//	}
	
	/*
	 * version 2
	 */
	public List<DynaBean> queryMaxRecordDemand(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT DeviceID, RecTime, greatest(DemandPK, DemandSP, DemandSatSP, DemandOP) MaxDemand FROM PowerRecord " + 
//				 	 "where DeviceID = '"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";

		String sql = "SELECT DeviceID, RecTime, greatest(DemandPK, DemandSP, DemandSatSP, DemandOP) MaxDemand "+
					 "FROM PowerRecord " + 
				 	 "where DeviceID = '"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')  " +
				 	 "and greatest(DemandPK, DemandSP, DemandSatSP, DemandOP)>0 order by MaxDemand desc limit 5;";
		
		return executeQuery(sql);
	}
//	public List<DynaBean> queryMaxRecordDemand_Daily(String deviceID, String startdate, String enddate) throws SQLException {
////		String sql = "SELECT DeviceID, RecTime, greatest(DemandPK, DemandSP, DemandSatSP, DemandOP) MaxDemand "+
////					"FROM PowerRecord " + 
////				 	"where DeviceID = '"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
//
//		String sql = "SELECT DeviceID, RecTime, greatest(DemandPK, DemandSP, DemandSatSP, DemandOP) MaxDemand "+
//					"FROM PowerRecord " + 
//				 	 "where DeviceID = '"+deviceID+"' and RecTime>=str_to_date('"+startdate+"','%Y-%m-%d') and RecTime<str_to_date('"+enddate+"','%Y-%m-%d')  " +
//				 	"and greatest(DemandPK, DemandSP, DemandSatSP, DemandOP)>0 order by MaxDemand desc limit 5;";
//		return executeQuery(sql);
//	}
	public List<DynaBean> queryRecordDemand(String powerAccount, String startdate, String enddate) throws SQLException {
		String sql = "select m.PowerAccount, m.DeviceID, r.RecTime, r.DemandPK, r.DemandSP, r.DemandSatSP, r.DemandOP, greatest(r.DemandPK, r.DemandSP, r.DemandSatSP, r.DemandOP) MaxDemand " + 
					 "from MeterSetup m, PowerRecord r " + 
					 "where " + 
						 "m.PowerAccount='"+powerAccount+"' and " + 
						 "m.UsageCode=1 and m.Enabled=1 and " + 
						 "r.RecTime >=str_to_date('"+startdate+"','%Y-%m-%d') and r.RecTime<str_to_date('"+enddate+"','%Y-%m-%d') " + 
						 "and m.DeviceID=r.DeviceID;";
		
		return executeQuery(sql);
	}

	public List<DynaBean> queryRecordGroupbyDeviceWithPartition(String startdate, String enddate) throws SQLException {
		String sql = "select RecTime, DeviceID from PowerRecordCreateTime where CreateTime>=str_to_date('"+startdate+"','%Y-%m-%d %H:%i:%s') and CreateTime<str_to_date('"+enddate+"','%Y-%m-%d %H:%i:%s') and 1=1 group by DeviceID";
//		logger.info("monitor sql: "+sql);
		return executeQuery(sql);
	}
}
