package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.PowerMonthModel;

public class PowerMonthDao extends BaseDao2 {

	public List<DynaBean> queryByPowerMonth(String useMonth) throws SQLException {
		String sql = "SELECT * FROM PowerMonth where useMonth = '"+useMonth+"'";
		return executeQuery(sql);
	}
	
	public List<Integer> insertPowerMonth(List<PowerMonthModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO PowerMonth "+
					 "( " +
					 	"PowerAccount, useMonth, UsuallyCC, SPCC, SatSPCC, OPCC, " +
					 	"MDemandPK, MDemandSP, MDemandSatSP, MDemandOP, " +
					 	"MCECPK, MCECSP, MCECSatSP, MCECOP, MCEC, " +
						"RealPlan, RatePlanCode " +
					 ")" +
					 "VALUES " +
					 "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);
			
			for (PowerMonthModel pm: list) {
				ps.setString(1, pm.getPowerAccount());
				ps.setString(2, pm.getUseMonth());
				ps.setInt(3, pm.getUsuallyCC());
				ps.setInt(4, pm.getSPCC());
				ps.setInt(5, pm.getSatSPCC());
				ps.setInt(6, pm.getOPCC());
				
				ps.setBigDecimal(7, pm.getMDemandPK());
				ps.setBigDecimal(8, pm.getMDemandSP());
				ps.setBigDecimal(9, pm.getMDemandSatSP());
				ps.setBigDecimal(10, pm.getMDemandOP());
				
				ps.setBigDecimal(11, pm.getMCECPK());
				ps.setBigDecimal(12, pm.getMCECSP());
				ps.setBigDecimal(13, pm.getMCECSatSP());
				ps.setBigDecimal(14, pm.getMCECOP());
				ps.setBigDecimal(15, pm.getMCEC());

				ps.setInt(16, pm.getRealPlan());
				ps.setInt(17, pm.getRatePlanCode());
				
				ps.addBatch();
				sqlList.add(ps.toString());
			}
			
			return batchInsert(connection, ps, sqlList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int updatePowerMonth(List<PowerMonthModel> list) {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE PowerMonth SET " +
						 " UsuallyCC=?, SPCC=?, SatSPCC=?, OPCC=?, "+
						 " MDemandPK=?, MDemandSP=?, MDemandSatSP=?, MDemandOP=?, " +
						 " MCECPK=?, MCECSP=?, MCECSatSP=?, MCECOP=?, MCEC=?, " +
						 " RealPlan=?, RatePlanCode=? " +
					 "WHERE PowerAccount=? and useMonth=?";
		
		try {
			Connection connection = initConnection();
			PreparedStatement ps = initPreparedStatement(connection, sql);

			for (PowerMonthModel pm: list) {
				ps.setInt(1, pm.getUsuallyCC());
				ps.setInt(2, pm.getSPCC());
				ps.setInt(3, pm.getSatSPCC());
				ps.setInt(4, pm.getOPCC());
				
				ps.setBigDecimal(5, pm.getMDemandPK());
				ps.setBigDecimal(6, pm.getMDemandSP());
				ps.setBigDecimal(7, pm.getMDemandSatSP());
				ps.setBigDecimal(8, pm.getMDemandOP());
				
				ps.setBigDecimal(9, pm.getMCECPK());
				ps.setBigDecimal(10, pm.getMCECSP());
				ps.setBigDecimal(11, pm.getMCECSatSP());
				ps.setBigDecimal(12, pm.getMCECOP());
				ps.setBigDecimal(13, pm.getMCEC());

				ps.setInt(14, pm.getRealPlan());
				ps.setInt(15, pm.getRatePlanCode());
				
				ps.setString(16, pm.getPowerAccount());
				ps.setString(17, pm.getUseMonth());
				
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
