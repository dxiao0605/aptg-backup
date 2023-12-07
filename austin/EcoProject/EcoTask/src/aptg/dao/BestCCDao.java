package aptg.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.beans.MaxMinCCBean;
import aptg.dao.base.BaseDao2;
import aptg.models.BestCCModel;

public class BestCCDao extends BaseDao2 {

	public List<DynaBean> queryBestCC(String powerAccount, String useMonth, int ratePlanCode, int usuallyCC) throws SQLException {
		String sql = "SELECT * FROM BestCC WHERE PowerAccount = '"+powerAccount+"' and useMonth = '"+useMonth+"' and RatePlanCode = "+ratePlanCode+" and UsuallyCC = "+usuallyCC;
		return executeQuery(sql);
	}
	
	public int deleteBestCC(String powerAccount, String useMonth, int ratePlanCode) throws SQLException {
		String sql = "DELETE FROM BestCC WHERE PowerAccount = '"+powerAccount+"' and useMonth = '"+useMonth+"' and RatePlanCode = "+ratePlanCode;
		return executeUpdate(sql);
	}

	public List<DynaBean> queryMaxMinCCByPowerAccount(String powerAccount) throws SQLException {
		String sql = "SELECT PowerAccount, max(UsuallyCC) maxCC, min(UsuallyCC) minCC FROM BestCC where PowerAccount = '"+powerAccount+"'";
		return executeQuery(sql);
	}

//	public List<DynaBean> queryMaxUsuallyCCgroupbyRatePlanCode(String powerAccount, String useMonth) throws SQLException {
//		String sql = "SELECT PowerAccount, useMonth, RatePlanCode, max(UsuallyCC) as UsuallyCC FROM BestCC where PowerAccount='"+powerAccount+"' and useMonth='"+useMonth+"' group by RatePlanCode";
//		return executeQuery(sql);
//	}

	public List<DynaBean> queryAllMaxMinCC(String powerAccount) throws SQLException {
		String sql = "SELECT PowerAccount, useMonth, RatePlanCode, max(UsuallyCC) maxCC, min(UsuallyCC) minCC, SPCC, SatSPCC, OPCC  FROM BestCC where PowerAccount='"+powerAccount+"' group by RatePlanCode, useMonth order by useMonth asc";
		return executeQuery(sql);
	}
	
	public List<Integer> insertBestCC(List<BestCCModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO BestCC " +
					 "("+
						 "PowerAccount, useMonth, RatePlanCode, " + 
						 "UsuallyCC, SPCC, SatSPCC, OPCC, " + 
						 "BaseCharge, OverCharge, " + 
						 "OverPK, OverSP, OverSatSP, OverOP, RealPlan " +
					 ") " +
					 "VALUES " +
					 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (BestCCModel best: list) {
				ps.setString(1, best.getPowerAccount());
				ps.setString(2, best.getUseMonth());
				ps.setInt(3, best.getRatePlanCode());
				
				ps.setInt(4, best.getUsuallyCC());
				ps.setInt(5, best.getSPCC());
				ps.setInt(6, best.getSatSPCC());
				ps.setInt(7, best.getOPCC());
				
				ps.setBigDecimal(8, (best.getBaseCharge()!=null) ? best.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(9, (best.getOverCharge()!=null) ? best.getOverCharge() : BigDecimal.ZERO);

				ps.setInt(10, best.getOverPK());
				ps.setInt(11, best.getOverSP());
				ps.setInt(12, best.getOverSatSP());
				ps.setInt(13, best.getOverOP());
				
				ps.setInt(14, best.getRealPlan());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
			
			return batchInsert(connection, ps, sqlList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateBestCC(List<BestCCModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE BestCC SET " +
						 " SPCC=?, SatSPCC=?, OPCC=?, " + 
						 " BaseCharge=?, OverCharge=?, " + 
						 " OverPK=?, OverSP=?, OverSatSP=?, OverOP=?, RealPlan=? " +
					 "WHERE PowerAccount=? and useMonth=? and RatePlanCode=? and UsuallyCC=?";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (BestCCModel best: list) {
				ps.setInt(1, best.getSPCC());
				ps.setInt(2, best.getSatSPCC());
				ps.setInt(3, best.getOPCC());
				
				ps.setBigDecimal(4, (best.getBaseCharge()!=null) ? best.getBaseCharge() : BigDecimal.ZERO);
				ps.setBigDecimal(5, (best.getOverCharge()!=null) ? best.getOverCharge() : BigDecimal.ZERO);

				ps.setInt(6, best.getOverPK());
				ps.setInt(7, best.getOverSP());
				ps.setInt(8, best.getOverSatSP());
				ps.setInt(9, best.getOverOP());

				ps.setInt(10, best.getRealPlan());
				
				ps.setString(11, best.getPowerAccount());
				ps.setString(12, best.getUseMonth());
				ps.setInt(13, best.getRatePlanCode());
				ps.setInt(14, best.getUsuallyCC());

				ps.addBatch();
				sqlList.add(ps.toString());
			}
			
			return batchUpdate(connection, ps, sqlList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int deleteBestCC(List<MaxMinCCBean> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "Delete From BestCC " +
					 "WHERE PowerAccount=? and UsuallyCC>? and UsuallyCC<?";

		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (MaxMinCCBean maxmin: list) {
				ps.setString(1, maxmin.getPowerAccount());
				ps.setInt(2, maxmin.getMaxCC());
				ps.setInt(3, maxmin.getMinCC());

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
