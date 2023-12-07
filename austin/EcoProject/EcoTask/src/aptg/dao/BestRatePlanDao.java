package aptg.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.BestRatePlanModel;

public class BestRatePlanDao extends BaseDao2 {

	public List<DynaBean> queryBestRatePlan(String powerAccount, String useMonth, int ratePlanCode) throws SQLException {
		String sql = "SELECT * FROM BestRatePlan WHERE PowerAccount = '"+powerAccount+"' and useMonth = '"+useMonth+"' and RatePlanCode = "+ratePlanCode;
		return executeQuery(sql);
	}
	
	public List<Integer> insertBestRatePlan(List<BestRatePlanModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO BestRatePlan " +
				 "("+
					 "PowerAccount, UseMonth, InUse, RatePlanCode, " + 
					 "UsuallyCC, SPCC, SatSPCC, OPCC, " + 
					 "TPMDemandPK, TPMDemandSP, TPMDemandSatSP, TPMDemandOP, " + 
					 "TPMCECPK, TPMCECSP, TPMCECSatSP, TPMCECOP, TPMCEC, " + 
					 "BaseCharge, UsageCharge, OverCharge, TotalCharge, " + 
					 "OverPK, OverSP, OverSatSP, OverOP, RealPlan " +
				 ") " +
				 "VALUES " +
				 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (BestRatePlanModel best: list) {
				ps.setString(1, best.getPowerAccount());
				ps.setString(2, best.getUseMonth());
				ps.setInt(3, best.getInUse());
				ps.setInt(4, best.getRatePlanCode());
				
				ps.setInt(5, best.getUsuallyCC());
				ps.setInt(6, best.getSPCC());
				ps.setInt(7, best.getSatSPCC());
				ps.setInt(8, best.getOPCC());

				ps.setBigDecimal(9, (best.getTPMDemandPK()!=null) ? best.getTPMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(10, (best.getTPMDemandSP()!=null) ? best.getTPMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(11, (best.getTPMDemandSatSP()!=null) ? best.getTPMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(12, (best.getTPMDemandOP()!=null) ? best.getTPMDemandOP() : BigDecimal.ZERO);

				ps.setBigDecimal(13, (best.getTPMCECPK()!=null) ? best.getTPMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(14, (best.getTPMCECSP()!=null) ? best.getTPMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(15, (best.getTPMCECSatSP()!=null) ? best.getTPMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(16, (best.getTPMCECOP()!=null) ? best.getTPMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(17, (best.getTPMCEC()!=null) ? best.getTPMCEC() : BigDecimal.ZERO);

				ps.setBigDecimal(18, (best.getBaseCharge()!=null) ? best.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(19, (best.getUsageCharge()!=null) ? best.getUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(20, (best.getOverCharge()!=null) ? best.getOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(21, (best.getTotalCharge()!=null) ? best.getTotalCharge() : BigDecimal.ZERO);

				ps.setInt(22, best.getOverPK());
				ps.setInt(23, best.getOverSP());
				ps.setInt(24, best.getOverSatSP());
				ps.setInt(25, best.getOverOP());
				
				ps.setInt(26, best.getRealPlan());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}

			return batchInsert(connection, ps, sqlList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int updateBestRatePlan(List<BestRatePlanModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE BestRatePlan SET " +
				 		 " inUse=?, " +
						 " UsuallyCC=?, SPCC=?, SatSPCC=?, OPCC=?, " + 
						 " TPMDemandPK=?, TPMDemandSP=?, TPMDemandSatSP=?, TPMDemandOP=?, " + 
						 " TPMCECPK=?, TPMCECSP=?, TPMCECSatSP=?, TPMCECOP=?, TPMCEC=?, " + 
						 " BaseCharge=?, UsageCharge=?, OverCharge=?, TotalCharge=?, " + 
						 " OverPK=?, OverSP=?, OverSatSP=?, OverOP=?, RealPlan=? " +
					 "WHERE PowerAccount=? and UseMonth=? and RatePlanCode=?";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (BestRatePlanModel best: list) {
				ps.setInt(1, best.getInUse());
				
				ps.setInt(2, best.getUsuallyCC());
				ps.setInt(3, best.getSPCC());
				ps.setInt(4, best.getSatSPCC());
				ps.setInt(5, best.getOPCC());

				ps.setBigDecimal(6, (best.getTPMDemandPK()!=null) ? best.getTPMDemandPK() : BigDecimal.ZERO);
				ps.setBigDecimal(7, (best.getTPMDemandSP()!=null) ? best.getTPMDemandSP() : BigDecimal.ZERO);
				ps.setBigDecimal(8, (best.getTPMDemandSatSP()!=null) ? best.getTPMDemandSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(9, (best.getTPMDemandOP()!=null) ? best.getTPMDemandOP() : BigDecimal.ZERO);

				ps.setBigDecimal(10, (best.getTPMCECPK()!=null) ? best.getTPMCECPK() : BigDecimal.ZERO);
				ps.setBigDecimal(11, (best.getTPMCECSP()!=null) ? best.getTPMCECSP() : BigDecimal.ZERO);
				ps.setBigDecimal(12, (best.getTPMCECSatSP()!=null) ? best.getTPMCECSatSP() : BigDecimal.ZERO);
				ps.setBigDecimal(13, (best.getTPMCECOP()!=null) ? best.getTPMCECOP() : BigDecimal.ZERO);
				ps.setBigDecimal(14, (best.getTPMCEC()!=null) ? best.getTPMCEC() : BigDecimal.ZERO);

				ps.setBigDecimal(15, (best.getBaseCharge()!=null) ? best.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(16, (best.getUsageCharge()!=null) ? best.getUsageCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(17, (best.getOverCharge()!=null) ? best.getOverCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(18, (best.getTotalCharge()!=null) ? best.getTotalCharge() : BigDecimal.ZERO);

				ps.setInt(19, best.getOverPK());
				ps.setInt(20, best.getOverSP());
				ps.setInt(21, best.getOverSatSP());
				ps.setInt(22, best.getOverOP());
				
				ps.setInt(23, best.getRealPlan());
				
				ps.setString(24, best.getPowerAccount());
				ps.setString(25, best.getUseMonth());
				ps.setInt(26, best.getRatePlanCode());
				
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
