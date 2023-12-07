package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.PowerRecordCollectionModel;

public class PowerRecordCollectionDao extends BaseDao2 {

	public List<DynaBean> queryCollection(String deviceID, String recDate) throws SQLException {
		String sql = "SELECT * FROM PowerRecordCollection WHERE DeviceID = '"+deviceID+"' and RecDate=str_to_date('"+recDate+"','%Y-%m-%d')";
		return executeQuery(sql);
	}
	public List<DynaBean> queryCollection(String deviceID, String startdate, String enddate) throws SQLException {
		String sql = "SELECT "
					+ " 	 MDemandPK, "
					+ "		 MDemandSP, "
					+ "		 MDemandSatSP, "
					+ "		 MDemandOP "
					+ "FROM PowerRecordCollection "
					+ "WHERE DeviceID = '"+deviceID+"' and RecDate>=str_to_date('"+startdate+"','%Y-%m-%d') and RecDate<str_to_date('"+enddate+"','%Y-%m-%d')";

//		System.out.println("######### 1. sql: "+sql);
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryNewestCollection(String deviceID, String startdate, String enddate) throws SQLException {
		// enddate(=預計算日)
		String sql = "SELECT "
				+ " 	 MDemandPK, "
				+ "		 MDemandSP, "
				+ "		 MDemandSatSP, "
				+ "		 MDemandOP "
				+ "FROM PowerRecordCollection "
				+ "WHERE "
				+ 	"DeviceID='"+deviceID+"' and "
				+ 	"RecDate>=(SELECT Max(RecDate) FROM PowerRecordCollection WHERE DeviceID='"+deviceID+"' and RecDate<str_to_date('"+enddate+"','%Y-%m-%d')) and RecDate<str_to_date('"+enddate+"','%Y-%m-%d')";
		
//		System.out.println("######### 1. sql: "+sql);
		return executeQuery(sql);
	}
	

//	public List<DynaBean> queryCollectionDemand(String deviceID, String startdate, String enddate) throws SQLException {
//		String sql = "SELECT "
//					+ " 	 DemandPK, "
//					+ "		 DemandSP, "
//					+ "		 DemandSatSP, "
//					+ "		 DemandOP "
//					+ "FROM PowerRecordCollection "
//					+ "WHERE DeviceID = '"+deviceID+"' and RecDate>='"+startdate+"' and RecDate<'"+enddate+"'";
//
////		System.out.println("######### 1. sql: "+sql);
//		return executeQuery(sql);
//	}
	
	
	public List<DynaBean> queryCollectionKPIinfo(String startdate, String enddate) throws SQLException {
//		String sql = "SELECT SUM(CEC) CEC FROM " +
//						"(SELECT * FROM MeterSetup where UsageCode = 2) t1 " +
//					 "inner join " +
//					 	"(SELECT * FROM PowerRecordCollection where RecDate>='"+startdate+"' and RecDate<'"+enddate+"') t2 " + 
//					 "on t1.DeviceID = t2.DeviceID;";
		
		String sql = "SELECT SUM(CEC) CEC FROM" + 
					 "(SELECT * FROM MeterSetup where UsageCode = 2) t1" + 
					 " inner join" + 
					 "(SELECT DeviceID, Max(RecDate) RecDate, CEC FROM PowerRecordCollection where RecDate>=str_to_date('"+startdate+"','%Y-%m-%d') and RecDate<str_to_date('"+enddate+"','%Y-%m-%d') group by DeviceID) t2" + 
					 " on t1.DeviceID = t2.DeviceID;";
		return executeQuery(sql);
	}

	public List<Integer> insertCollection(List<PowerRecordCollectionModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO PowerRecordCollection " + 
					 "(DeviceID, RecDate, " + 
						 " Iavg, Imax, Vavg, Vmax, Wavg, Wmax, VavgP, VmaxP, " +
						 " DemandPK, DemandSP, DemandSatSP, DemandOP, " +
						 " MDemandPK, MDemandSP, MDemandSatSP, MDemandOP, " +
						 " DCECPK, DCECSP, DCECSatSP, DCECOP, DCEC, " +
						 " MCECPK, MCECSP, MCECSatSP, MCECOP, CEC, "+
						 " Mode1, Mode2, Mode3, Mode4, "+
						 " TPDemandPK, TPDemandSP, TPDemandSatSP, TPDemandOP, "+
						 " TPMDemandPK, TPMDemandSP, TPMDemandSatSP, TPMDemandOP, "+
						 " TPDCECPK, TPDCECSP, TPDCECSatSP, TPDCECOP, "+
						 " TPMCECPK, TPMCECSP, TPMCECSatSP, TPMCECOP, "+
						 " kWh, "+
						 " FcstECO5MCECPK, FcstECO5MCECSP, FcstECO5MCECSatSP, FcstECO5MCECOP, FcstECO5MCEC "+
						 " ,RatePlanCode, RealPlan, UsuallyCC, SPCC, SatSPCC, OPCC "+
					 ") " +
					 "VALUES " +
//					 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		 			 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
		 			 " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
		 			 " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
		 			 " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (PowerRecordCollectionModel record: list) {
				ps.setString(1, record.getDeviceID());
				ps.setString(2, record.getRecDate());
				
				ps.setBigDecimal(3, record.getIavg());
				ps.setBigDecimal(4, record.getImax());
				ps.setBigDecimal(5, record.getVavg());
				ps.setBigDecimal(6, record.getVmax());
				ps.setBigDecimal(7, record.getWavg());
				ps.setBigDecimal(8, record.getWmax());
				ps.setBigDecimal(9, record.getVavgP());
				ps.setBigDecimal(10, record.getVmaxP());
				
				ps.setBigDecimal(11, record.getDemandPK());
				ps.setBigDecimal(12, record.getDemandSP());
				ps.setBigDecimal(13, record.getDemandSatSP());
				ps.setBigDecimal(14, record.getDemandOP());
				
				ps.setBigDecimal(15, record.getMDemandPK());
				ps.setBigDecimal(16, record.getMDemandSP());
				ps.setBigDecimal(17, record.getMDemandSatSP());
				ps.setBigDecimal(18, record.getMDemandOP());
				
				ps.setBigDecimal(19, record.getDCECPK());
				ps.setBigDecimal(20, record.getDCECSP());
				ps.setBigDecimal(21, record.getDCECSatSP());
				ps.setBigDecimal(22, record.getDCECOP());
				ps.setBigDecimal(23, record.getDCEC());
				
				ps.setBigDecimal(24, record.getMCECPK());
				ps.setBigDecimal(25, record.getMCECSP());
				ps.setBigDecimal(26, record.getMCECSatSP());
				ps.setBigDecimal(27, record.getMCECOP());
				ps.setBigDecimal(28, record.getCEC());

				ps.setBigDecimal(29, record.getMode1());
				ps.setBigDecimal(30, record.getMode2());
				ps.setBigDecimal(31, record.getMode3());
				ps.setBigDecimal(32, record.getMode4());

				ps.setBigDecimal(33, record.getTPDemandPK());
				ps.setBigDecimal(34, record.getTPDemandSP());
				ps.setBigDecimal(35, record.getTPDemandSatSP());
				ps.setBigDecimal(36, record.getTPDemandOP());
				
				ps.setBigDecimal(37, record.getTPMDemandPK());
				ps.setBigDecimal(38, record.getTPMDemandSP());
				ps.setBigDecimal(39, record.getTPMDemandSatSP());
				ps.setBigDecimal(40, record.getTPMDemandOP());
				
				ps.setBigDecimal(41, record.getTPDCECPK());
				ps.setBigDecimal(42, record.getTPDCECSP());
				ps.setBigDecimal(43, record.getTPDCECSatSP());
				ps.setBigDecimal(44, record.getTPDCECOP());
				
				ps.setBigDecimal(45, record.getTPMCECPK());
				ps.setBigDecimal(46, record.getTPMCECSP());
				ps.setBigDecimal(47, record.getTPMCECSatSP());
				ps.setBigDecimal(48, record.getTPMCECOP());
				
				ps.setBigDecimal(49, record.getKWh());

				ps.setBigDecimal(50, record.getFcstECO5MCECPK());
				ps.setBigDecimal(51, record.getFcstECO5MCECSP());
				ps.setBigDecimal(52, record.getFcstECO5MCECSatSP());
				ps.setBigDecimal(53, record.getFcstECO5MCECOP());
				ps.setBigDecimal(54, record.getFcstECO5MCEC());

				if (record.getRatePlanCode()!=null)
					ps.setInt(55, record.getRatePlanCode());
				else
					ps.setNull(55, java.sql.Types.INTEGER);
				
				if (record.getRealPlan()!=null)
					ps.setInt(56, record.getRealPlan());
				else
					ps.setNull(56, java.sql.Types.INTEGER);
				
				if (record.getUsuallyCC()!=null)
					ps.setInt(57, record.getUsuallyCC());
				else
					ps.setNull(57, java.sql.Types.INTEGER);

				if (record.getSPCC()!=null)
					ps.setInt(58, record.getSPCC());
				else
					ps.setNull(58, java.sql.Types.INTEGER);

				if (record.getSatSPCC()!=null)
					ps.setInt(59, record.getSatSPCC());
				else
					ps.setNull(59, java.sql.Types.INTEGER);

				if (record.getOPCC()!=null)
					ps.setInt(60, record.getOPCC());
				else
					ps.setNull(60, java.sql.Types.INTEGER);
				

				ps.addBatch();
				sqlList.add(ps.toString());
			}
//			System.out.println("############### ps sql: "+ps.toString());
			
			return batchInsert(connection, ps, sqlList);		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int updateCollection(List<PowerRecordCollectionModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE PowerRecordCollection SET " + 
					 	" Iavg=?, Imax=?, Vavg=?, Vmax=?, Wavg=?, Wmax=?, VavgP=?, VmaxP=?, " +
						" DemandPK=?, DemandSP=?, DemandSatSP=?, DemandOP=?, " +
						" MDemandPK=?, MDemandSP=?, MDemandSatSP=?, MDemandOP=?, " +
						" DCECPK=?, DCECSP=?, DCECSatSP=?, DCECOP=?, DCEC=?, " +
						" MCECPK=?, MCECSP=?, MCECSatSP=?, MCECOP=?, CEC=?, " +
						" Mode1=?, Mode2=?, Mode3=?, Mode4=?, "+
						
						" TPDemandPK=?, TPDemandSP=?, TPDemandSatSP=?, TPDemandOP=?, "+
						" TPMDemandPK=?, TPMDemandSP=?, TPMDemandSatSP=?, TPMDemandOP=?, "+
						" TPDCECPK=?, TPDCECSP=?, TPDCECSatSP=?, TPDCECOP=?, "+
						" TPMCECPK=?, TPMCECSP=?, TPMCECSatSP=?, TPMCECOP=?, "+
						" kWh=?, "+
						" FcstECO5MCECPK=?, FcstECO5MCECSP=?, FcstECO5MCECSatSP=?, FcstECO5MCECOP=?, FcstECO5MCEC=? "+
						" ,RatePlanCode=?, RealPlan=?, UsuallyCC=?, SPCC=?, SatSPCC=?, OPCC=? " +
					 "WHERE DeviceID=? and RecDate=?";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (PowerRecordCollectionModel record: list) {
				ps.setBigDecimal(1, record.getIavg());
				ps.setBigDecimal(2, record.getImax());
				ps.setBigDecimal(3, record.getVavg());
				ps.setBigDecimal(4, record.getVmax());
				ps.setBigDecimal(5, record.getWavg());
				ps.setBigDecimal(6, record.getWmax());
				ps.setBigDecimal(7, record.getVavgP());
				ps.setBigDecimal(8, record.getVmaxP());
				
				ps.setBigDecimal(9, record.getDemandPK());
				ps.setBigDecimal(10, record.getDemandSP());
				ps.setBigDecimal(11, record.getDemandSatSP());
				ps.setBigDecimal(12, record.getDemandOP());
				
				ps.setBigDecimal(13, record.getMDemandPK());
				ps.setBigDecimal(14, record.getMDemandSP());
				ps.setBigDecimal(15, record.getMDemandSatSP());
				ps.setBigDecimal(16, record.getMDemandOP());
				
				ps.setBigDecimal(17, record.getDCECPK());
				ps.setBigDecimal(18, record.getDCECSP());
				ps.setBigDecimal(19, record.getDCECSatSP());
				ps.setBigDecimal(20, record.getDCECOP());
				ps.setBigDecimal(21, record.getDCEC());
				
				ps.setBigDecimal(22, record.getMCECPK());
				ps.setBigDecimal(23, record.getMCECSP());
				ps.setBigDecimal(24, record.getMCECSatSP());
				ps.setBigDecimal(25, record.getMCECOP());
				ps.setBigDecimal(26, record.getCEC());

				ps.setBigDecimal(27, record.getMode1());
				ps.setBigDecimal(28, record.getMode2());
				ps.setBigDecimal(29, record.getMode3());
				ps.setBigDecimal(30, record.getMode4());


				ps.setBigDecimal(31, record.getTPDemandPK());
				ps.setBigDecimal(32, record.getTPDemandSP());
				ps.setBigDecimal(33, record.getTPDemandSatSP());
				ps.setBigDecimal(34, record.getTPDemandOP());
				
				ps.setBigDecimal(35, record.getTPMDemandPK());
				ps.setBigDecimal(36, record.getTPMDemandSP());
				ps.setBigDecimal(37, record.getTPMDemandSatSP());
				ps.setBigDecimal(38, record.getTPMDemandOP());
				
				ps.setBigDecimal(39, record.getTPDCECPK());
				ps.setBigDecimal(40, record.getTPDCECSP());
				ps.setBigDecimal(41, record.getTPDCECSatSP());
				ps.setBigDecimal(42, record.getTPDCECOP());
				
				ps.setBigDecimal(43, record.getTPMCECPK());
				ps.setBigDecimal(44, record.getTPMCECSP());
				ps.setBigDecimal(45, record.getTPMCECSatSP());
				ps.setBigDecimal(46, record.getTPMCECOP());
				
				ps.setBigDecimal(47, record.getKWh());

				ps.setBigDecimal(48, record.getFcstECO5MCECPK());
				ps.setBigDecimal(49, record.getFcstECO5MCECSP());
				ps.setBigDecimal(50, record.getFcstECO5MCECSatSP());
				ps.setBigDecimal(51, record.getFcstECO5MCECOP());
				ps.setBigDecimal(52, record.getFcstECO5MCEC());

//				ps.setString(53, record.getDeviceID());
//				ps.setString(54, record.getRecDate());
				
				if (record.getRatePlanCode()!=null)
					ps.setInt(53, record.getRatePlanCode());
				else
					ps.setNull(53, java.sql.Types.INTEGER);
				
				if (record.getRealPlan()!=null)
					ps.setInt(54, record.getRealPlan());
				else
					ps.setNull(54, java.sql.Types.INTEGER);
				
				if (record.getUsuallyCC()!=null)
					ps.setInt(55, record.getUsuallyCC());
				else
					ps.setNull(55, java.sql.Types.INTEGER);

				if (record.getSPCC()!=null)
					ps.setInt(56, record.getSPCC());
				else
					ps.setNull(56, java.sql.Types.INTEGER);

				if (record.getSatSPCC()!=null)
					ps.setInt(57, record.getSatSPCC());
				else
					ps.setNull(57, java.sql.Types.INTEGER);

				if (record.getOPCC()!=null)
					ps.setInt(58, record.getOPCC());
				else
					ps.setNull(58, java.sql.Types.INTEGER);
				
				ps.setString(59, record.getDeviceID());
				ps.setString(60, record.getRecDate());

				ps.addBatch();
				sqlList.add(ps.toString());
			}
			
			return batchUpdate(connection, ps, sqlList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
