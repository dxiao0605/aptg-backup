package aptg.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.utils.ToolUtil;

public class PowerRecordDao extends BaseDao2 {

	public List<DynaBean> queryDailyCollection(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT DeviceID, Date(RecTime) RecTime, CreateTime " + 
					 "FROM PowerRecord WHERE DeviceID='"+deviceID+"' and CreateTime>='"+startdate+"' and CreateTime<'"+enddate+"'";
		
//		System.out.println("@@@@@@@@@@@@@ sql: "+sql);
		return executeQuery(sql);
	}
	
//	public List<DynaBean> queryBasicIVW(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT " 
//	  				 +	"Iavg, Vavg, VavgP, W "
//					 + "FROM PowerRecord "
//					 + "WHERE DeviceID='"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
//		
////		System.out.println("####### sql: "+sql);
//		return executeQuery(sql);
//	}

	public List<DynaBean> queryMaxDRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT "
						 + "MAX(DemandPK) DemandPK, "
						 + "MAX(DemandSP) DemandSP, "
						 + "MAX(DemandSatSP) DemandSatSP, "
						 + "MAX(DemandOP) DemandOP, "
						 + "MAX(Mode1) Mode1 , MAX(Mode2) Mode2 , MAX(Mode3) Mode3 , MAX(Mode4) Mode4 "
					 + "FROM PowerRecord WHERE DeviceID='"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
		
		return executeQuery(sql);
	}

	/**
	 * 
	 * @param deviceID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getSpecifyDateSecondRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " +
					 "			   KWh " +
					 "		FROM PowerRecord WHERE DeviceID='"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"' order by RecTime asc limit 2 " + 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 1. sql: "+sql);
		return executeQuery(sql);
	}
	/**
	 * 
	 * @param deviceID
	 * @param recDate
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getSpecifyDateLastRecord(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			   DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			   MCECPK, MCECSP, MCECSatSP, MCECOP, " +
					 "			   KWh " +
					 "		FROM PowerRecord WHERE DeviceID='"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"' order by RecTime desc limit 1 " + 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 1. sql: "+sql);
		return executeQuery(sql);
	}
	
	public List<DynaBean> querySpecifyDateNewestRecord(String deviceID, Date recDate) throws SQLException {
		String date = ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM");
		
		String sql = "SELECT t4.ApplyDate, t4.RatePlanCode, t3.* FROM ( " + 
					 "	SELECT t2.PowerAccount, t1.* FROM ( " + 
					 "		SELECT DeviceID, Date(RecTime) RecDate, " + 
					 "			 DemandPK, DemandSP, DemandSatSP, DemandOP, " + 
					 "			 MCECPK, MCECSP, MCECSatSP, MCECOP, " + 
					 "			 KWh " +
					 "		FROM PowerRecord WHERE DeviceID='"+deviceID+"' and RecTime<'"+ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd")+"' and RecTime like '"+date+"%' order by RecTime desc limit 1 " + 
					 "	) t1 inner join MeterSetup t2 " + 
					 "	on t1.DeviceID = t2.DeviceID " + 
					 ") t3 inner join PowerAccountHistory t4 " + 
					 "on t3.PowerAccount = t4.PowerAccount and t4.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate <= t3.RecDate and PowerAccount = t3.PowerAccount);";

//		System.out.println("### 2. sql: "+sql);
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryRecordDemand(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT DeviceID, RecTime, (DemandPK+DemandSP+DemandSatSP+DemandOP) AS TotalDemand FROM PowerRecord " + 
				 	"where DeviceID = '"+deviceID+"' and RecTime>='"+startdate+"' and RecTime<'"+enddate+"'";
		return executeQuery(sql);
	}

//	public List<DynaBean> queryRecordGroupbyDevice(String seqno) throws SQLException {
//		String sql = "select t1.* from PowerRecord t1  " + 
//					 "inner join (select max(seqno) seqno, DeviceID from PowerRecord where seqno>"+seqno+" and 1=1 group by DeviceID) t2 " + 
//					 "on t1.DeviceID = t2.DeviceID " + 
//					 "and t1.seqno = t2.seqno;";
//		return executeQuery(sql);
//	}
	public List<DynaBean> queryRecordGroupbyDeviceWithPartition(String startdate, String enddate) throws SQLException {
//		String sql = "select r.* from "+
//					   	 "("+
//					   	 	"select max(RecTime) RecTime, DeviceID from PowerRecordCreateTime where CreateTime>='"+startdate+"' and CreateTime<'"+enddate+"' and 1=1 group by DeviceID" +
//					   	 ") c, "+
//					   	 "PowerRecord r " +
//					 "where c.DeviceID=r.DeviceID and c.RecTime=r.RecTime;";

		String sql = "select max(RecTime) RecTime, DeviceID from PowerRecordCreateTime where CreateTime>='"+startdate+"' and CreateTime<'"+enddate+"' and 1=1 group by DeviceID";
		return executeQuery(sql);
	}
}
