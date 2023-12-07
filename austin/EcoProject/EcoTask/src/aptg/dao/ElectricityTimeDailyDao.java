package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.ElectricityTimeDailyModel;

public class ElectricityTimeDailyDao extends BaseDao2 {

	public List<DynaBean> queryElectricityTimeDaily(String recDate) throws SQLException {
		String sql = "SELECT * FROM ElectricityTimeDaily WHERE RecDate = '"+recDate+"'";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryElectricityTimeDaily(int year) throws SQLException {
		String sql = "SELECT * FROM ElectricityTimeDaily where RecDate>str_to_date('"+year+"', '%Y');";
		return executeQuery(sql);
	}

	/**
	 * 當月實際使用時間: 2021-01-01 ~ 2021-01-06
	 * 當月應使用時間: 2021-01-01 ~ 2021-02-01(不含) => (整月)
	 * 
	 * @param ratePlanCode
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> queryElectricityUseHour(String startdate, String enddate) throws SQLException {
		String sql = "SELECT RecDate, SUM(UsuallyHour) UsuallyHour, SUM(SPHour) SPHour, SUM(SatSPHour) SatSPHour, SUM(OPHour) OPHour FROM " +
				 	 "ElectricityTimeDaily where RecDate>=str_to_date('"+startdate+"','%Y-%m-%d') and RecDate<str_to_date('"+enddate+"','%Y-%m-%d')";
//		System.out.println("########## sql: "+sql);
		return executeQuery(sql);
	}
	
	public List<Integer> insertElectricityTimeDaily(List<ElectricityTimeDailyModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO ElectricityTimeDaily "+
					 "(RecDate, UsuallyHour, SPHour, SatSPHour, OPHour) "+
					 "VALUES "+
					 "(?, ?, ?, ?, ?)";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (ElectricityTimeDailyModel daily: list) {
			ps.setString(1, daily.getRecDate());
			ps.setBigDecimal(2, daily.getUsuallyHour());
			ps.setBigDecimal(3, daily.getSPHour());
			ps.setBigDecimal(4, daily.getSatSPHour());
			ps.setBigDecimal(5, daily.getOPHour());
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
		
		return batchInsert(connection, ps, sqlList);
	}
	
	public int updateElectricityTimeDaily(List<ElectricityTimeDailyModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE ElectricityTimeDaily SET "+
					 "UsuallyHour=?, SPHour=?, SatSPHour=?, OPHour=? WHERE RecDate=?";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (ElectricityTimeDailyModel daily: list) {
			ps.setBigDecimal(1, daily.getUsuallyHour());
			ps.setBigDecimal(2, daily.getSPHour());
			ps.setBigDecimal(3, daily.getSatSPHour());
			ps.setBigDecimal(4, daily.getOPHour());
			
			ps.setString(5, daily.getRecDate());
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
		
		return batchUpdate(connection, ps, sqlList);
	}
}
