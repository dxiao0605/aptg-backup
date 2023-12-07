package aptg.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.FcstChargeModel;

public class FcstChargeDao extends BaseDao2 {

	public List<DynaBean> queryFcstCharge(String powerAccount, String useMonth, String useTime, int ratePlanCode) throws SQLException {
		String sql = "SELECT * FROM FcstCharge WHERE PowerAccount='"+powerAccount+"' and UseMonth='"+useMonth+"' and UseTime='"+useTime+"'";
		// PowerAccount, UseMonth, UseTime, RatePlanCode
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryMaxUseTimeFcstCharge(int year, int month) throws SQLException {
		String sql = "SELECT t1.* FROM FcstCharge t1 inner join ( " + 
					 "	SELECT Max(useTime) useTime, PowerAccount FROM FcstCharge where Year(useTime)="+year+" and Month(useTime)="+month+" group by PowerAccount " + 
					 ") t2 on t1.PowerAccount = t2.PowerAccount and t1.useTime = t2.useTime;";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryKPIinfo(String startdate, String enddate) throws SQLException {
//		String sql = "select useMonth, SUM(TotalCharge) TotalCharge, SUM(TPMCEC) TPMCEC from " + 
//					 "FcstCharge t1 " + 
//					 "inner join (select PowerAccount, Max(useTime) useTime from FcstCharge where useTime>='"+startdate+"' and useTime<'"+enddate+"' group by PowerAccount) t2 " + 
//					 "where t1.PowerAccount=t2.PowerAccount and t1.useTime=t2.useTime;";

		// for poc 不用Max(useTime)
		// poc, prod使用max(useTime)僅prod可group by
		String sql = "select useMonth, SUM(TotalCharge) TotalCharge, SUM(TPMCEC) TPMCEC from " + 
					 "FcstCharge t1 " + 
					 "inner join (select PowerAccount, useTime from FcstCharge where useTime>=str_to_date('"+startdate+"','%Y-%m-%d') and useTime<str_to_date('"+enddate+"','%Y-%m-%d') group by PowerAccount) t2 " + 
					 "where t1.PowerAccount=t2.PowerAccount and t1.useTime=t2.useTime;";
		
//		System.out.println("######### sql: "+sql);
		return executeQuery(sql);
	}
	
	public int updateFcstCharge(List<FcstChargeModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE FcstCharge SET " +
						 "UsuallyCC=?, SPCC=?, SatSPCC=?, OPCC=?, " + 
						 "MDemandPK=?, MDemandSP=?, MDemandSatSP=?, MDemandOP=?, " + 
						 "TPMDemandPK=?, TPMDemandSP=?, TPMDemandSatSP=?, TPMDemandOP=?, " + 
						 "MCECPK=?, MCECSP=?, MCECSatSP=?, MCECOP=?, MCEC=?, " + 
						 "TPMCECPK=?, TPMCECSP=?, TPMCECSatSP=?, TPMCECOP=?, TPMCEC=?, " + 
						 "BaseCharge=?, UsageCharge=?, OverCharge=?, TotalCharge=?, " + 
						 "FcstMCECPK=?, FcstMCECSP=?, FcstMCECSatSP=?, FcstMCECOP=?, FcstMCEC=?, " + 
						 "FcstBaseCharge=?, FcstUsageCharge=?, FcstOverCharge=?, FcstTotalCharge=?, " +
						 "OverPK=?, OverSP=?, OverSatSP=?, OverOP=?, RealPlan=?, RatePlanCode=? " +
					 "WHERE PowerAccount=? and UseMonth=? and UseTime=?";
		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
		
			for (FcstChargeModel model: list) {
				ps.setInt(1, model.getUsuallyCC());
				ps.setInt(2, model.getSPCC());
				ps.setInt(3, model.getSatSPCC());
				ps.setInt(4, model.getOPCC());

				ps.setBigDecimal(5, (model.getMDemandPK()!=null) ? model.getMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(6, (model.getMDemandSP()!=null) ? model.getMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(7, (model.getMDemandSatSP()!=null) ? model.getMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(8, (model.getMDemandOP()!=null) ? model.getMDemandOP() : BigDecimal.ZERO);
				
				ps.setBigDecimal(9, (model.getTPMDemandPK()!=null) ? model.getTPMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(10, (model.getTPMDemandSP()!=null) ? model.getTPMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(11, (model.getTPMDemandSatSP()!=null) ? model.getTPMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(12, (model.getTPMDemandOP()!=null) ? model.getTPMDemandOP() : BigDecimal.ZERO);
				
				ps.setBigDecimal(13, (model.getMCECPK()!=null) ? model.getMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(14, (model.getMCECSP()!=null) ? model.getMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(15, (model.getMCECSatSP()!=null) ? model.getMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(16, (model.getMCECOP()!=null) ? model.getMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(17, (model.getMCEC()!=null) ? model.getMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(18, (model.getTPMCECPK()!=null) ? model.getTPMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(19, (model.getTPMCECSP()!=null) ? model.getTPMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(20, (model.getTPMCECSatSP()!=null) ? model.getTPMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(21, (model.getTPMCECOP()!=null) ? model.getTPMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(22, (model.getTPMCEC()!=null) ? model.getTPMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(23, (model.getBaseCharge()!=null) ? model.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(24, (model.getUsageCharge()!=null) ? model.getUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(25, (model.getOverCharge()!=null) ? model.getOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(26, (model.getTotalCharge()!=null) ? model.getTotalCharge() : BigDecimal.ZERO);
				
				ps.setBigDecimal(27, (model.getFcstMCECPK()!=null) ? model.getFcstMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(28, (model.getFcstMCECSP()!=null) ? model.getFcstMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(29, (model.getFcstMCECSatSP()!=null) ? model.getFcstMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(30, (model.getFcstMCECOP()!=null) ? model.getFcstMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(31, (model.getFcstMCEC()!=null) ? model.getFcstMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(32, (model.getFcstBaseCharge()!=null) ? model.getFcstBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(33, (model.getFcstUsageCharge()!=null) ? model.getFcstUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(34, (model.getFcstOverCharge()!=null) ? model.getFcstOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(35, (model.getFcstTotalCharge()!=null) ? model.getFcstTotalCharge() : BigDecimal.ZERO);
				
				ps.setInt(36, model.getOverPK());
				ps.setInt(37, model.getOverSP());
				ps.setInt(38, model.getOverSatSP());
				ps.setInt(39, model.getOverOP());

				ps.setInt(40, model.getRealPlan());
				ps.setInt(41, model.getRatePlanCode());
						
				ps.setString(42, model.getPowerAccount());
				ps.setString(43, model.getUseMonth());
				ps.setString(44, model.getUseTime());
				
				ps.addBatch();
				sqlList.add(ps.toString());
//				System.out.println("@@@@@@@@@ fcst update sql: "+ps.toString());
			}


			return batchUpdate(connection, ps, sqlList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public List<Integer> insertFcstCharge(List<FcstChargeModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO FcstCharge " +
					 "("+
						 "PowerAccount, UseMonth, UseTime, RatePlanCode, " + 
						 "UsuallyCC, SPCC, SatSPCC, OPCC, " + 
						 "MDemandPK, MDemandSP, MDemandSatSP, MDemandOP, " + 
						 "TPMDemandPK, TPMDemandSP, TPMDemandSatSP, TPMDemandOP, " + 
						 "MCECPK, MCECSP, MCECSatSP, MCECOP, MCEC, " + 
						 "TPMCECPK, TPMCECSP, TPMCECSatSP, TPMCECOP, TPMCEC, " + 
						 "BaseCharge, UsageCharge, OverCharge, TotalCharge, " + 
						 "FcstMCECPK, FcstMCECSP, FcstMCECSatSP, FcstMCECOP, FcstMCEC, " + 
						 "FcstBaseCharge, FcstUsageCharge, FcstOverCharge, FcstTotalCharge, " +
						 "OverPK, OverSP, OverSatSP, OverOP, RealPlan " +
					 ") " +
					 "VALUES " +
					 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (FcstChargeModel model: list) {
//				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$ model: "+JsonUtil.getInstance().convertObjectToJsonstring(model));
				ps.setString(1, model.getPowerAccount());
				ps.setString(2, model.getUseMonth());
				ps.setString(3, model.getUseTime());
				
				ps.setInt(4, model.getRatePlanCode());
				ps.setInt(5, model.getUsuallyCC());
				ps.setInt(6, model.getSPCC());
				ps.setInt(7, model.getSatSPCC());
				ps.setInt(8, model.getOPCC());
				
				ps.setBigDecimal(9, (model.getMDemandPK()!=null) ? model.getMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(10, (model.getMDemandSP()!=null) ? model.getMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(11, (model.getMDemandSatSP()!=null) ? model.getMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(12, (model.getMDemandOP()!=null) ? model.getMDemandOP() : BigDecimal.ZERO);
				
				ps.setBigDecimal(13, (model.getTPMDemandPK()!=null) ? model.getTPMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(14, (model.getTPMDemandSP()!=null) ? model.getTPMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(15, (model.getTPMDemandSatSP()!=null) ? model.getTPMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(16, (model.getTPMDemandOP()!=null) ? model.getTPMDemandOP() : BigDecimal.ZERO);
				
				ps.setBigDecimal(17, (model.getMCECPK()!=null) ? model.getMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(18, (model.getMCECSP()!=null) ? model.getMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(19, (model.getMCECSatSP()!=null) ? model.getMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(20, (model.getMCECOP()!=null) ? model.getMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(21, (model.getMCEC()!=null) ? model.getMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(22, (model.getTPMCECPK()!=null) ? model.getTPMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(23, (model.getTPMCECSP()!=null) ? model.getTPMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(24, (model.getTPMCECSatSP()!=null) ? model.getTPMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(25, (model.getTPMCECOP()!=null) ? model.getTPMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(26, (model.getTPMCEC()!=null) ? model.getTPMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(27, (model.getBaseCharge()!=null) ? model.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(28, (model.getUsageCharge()!=null) ? model.getUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(29, (model.getOverCharge()!=null) ? model.getOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(30, (model.getTotalCharge()!=null) ? model.getTotalCharge() : BigDecimal.ZERO);
				
				ps.setBigDecimal(31, (model.getFcstMCECPK()!=null) ? model.getFcstMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(32, (model.getFcstMCECSP()!=null) ? model.getFcstMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(33, (model.getFcstMCECSatSP()!=null) ? model.getFcstMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(34, (model.getFcstMCECOP()!=null) ? model.getFcstMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(35, (model.getFcstMCEC()!=null) ? model.getFcstMCEC() : BigDecimal.ZERO);
				
				ps.setBigDecimal(36, (model.getFcstBaseCharge()!=null) ? model.getFcstBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(37, (model.getFcstUsageCharge()!=null) ? model.getFcstUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(38, (model.getFcstOverCharge()!=null) ? model.getFcstOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(39, (model.getFcstTotalCharge()!=null) ? model.getFcstTotalCharge() : BigDecimal.ZERO);
				
				if (model.getOverPK()!=null)
					ps.setInt(40, model.getOverPK());
				else
					ps.setInt(40, 0);

				if (model.getOverSP()!=null)
					ps.setInt(41, model.getOverSP());
				else
					ps.setInt(41, 0);

				if (model.getOverSatSP()!=null)
					ps.setInt(42, model.getOverSatSP());
				else
					ps.setInt(42, 0);

				if (model.getOverOP()!=null)
					ps.setInt(43, model.getOverOP());
				else
					ps.setInt(43, 0);

				ps.setInt(44, model.getRealPlan());
				
				ps.addBatch();
				sqlList.add(ps.toString());
//				System.out.println("@@@@@@@@@ fcst insert sql: "+ps.toString());
			}

			return batchInsert(connection, ps, sqlList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
