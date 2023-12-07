package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.util.JdbcUtil;
import aptg.battery.vo.BatteryTypeListVO;

public class BatteryTypeListDAO extends BaseDAO{
	/**
	 * 取得電池類型
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getBatteryTypeList(BatteryTypeListVO batteryTypeListVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select c.CompanyName, b.* ");
		sql.append(" from BatteryTypeList b, Company c ");
		sql.append(" where b.CompanyCode = c.CompanyCode ");
		if(StringUtils.isNotBlank(batteryTypeListVO.getCompanyCode())) {
			sql.append(" and b.CompanyCode = ? ");
			parameterList.add(batteryTypeListVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryTypeListVO.getCompanyCodeArr())) {
			sql.append(" and b.CompanyCode in ("+batteryTypeListVO.getCompanyCodeArr()+") ");
		}		
		
		if(StringUtils.isNotBlank(batteryTypeListVO.getBatteryTypeCodeArr()) || 
				"1".equals(batteryTypeListVO.getBatteryTypeNull())) {
			boolean orFlag = false;
			sql.append(" and ( ");
			if(StringUtils.isNotBlank(batteryTypeListVO.getBatteryTypeCodeArr())) {
				sql.append(" b.BatteryTypeCode in ("+batteryTypeListVO.getBatteryTypeCodeArr()+") ");
				orFlag = true;
			}
			
			if("1".equals(batteryTypeListVO.getBatteryTypeNull())) {
				if(orFlag) 
					sql.append(" or ");
				sql.append(" b.BatteryTypeName is null ");
			}			
			sql.append(" ) ");
		}
		
		if(StringUtils.isNotBlank(batteryTypeListVO.getBatteryTypeName())) {
			sql.append(" and b.BatteryTypeName = ? ");
			parameterList.add(batteryTypeListVO.getBatteryTypeName());
		}		
		
		sql.append(" order by c.CompanyName, b.BatteryTypeName ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增電池型號
	 * @param batteryTypeListVO
	 * @throws SQLException
	 */
	public void addBatteryType(BatteryTypeListVO batteryTypeListVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 		
		sql.append(" insert into BatteryTypeList ( ");
		sql.append(" CompanyCode, ");
		sql.append(" BatteryTypeName, ");
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		sql.append(" ) values (?,?,?,?) ");
		parameterList.add(batteryTypeListVO.getCompanyCode());
		parameterList.add(batteryTypeListVO.getBatteryTypeName());
		parameterList.add(batteryTypeListVO.getUserName());
		parameterList.add(batteryTypeListVO.getUserName());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改電池型號
	 * @param batteryTypeListVO
	 * @throws SQLException
	 */
	public void updBatteryType(BatteryTypeListVO batteryTypeListVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 		
		sql.append(" update BatteryTypeList set ");		
		sql.append(" BatteryTypeName = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where BatteryTypeCode = ? ");
		parameterList.add(batteryTypeListVO.getBatteryTypeName());
		parameterList.add(batteryTypeListVO.getUserName());
		parameterList.add(batteryTypeListVO.getBatteryTypeCode());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 刪除電池型號
	 * @param batteryTypeListVO
	 * @throws SQLException
	 */
	public void delBatteryType(BatteryTypeListVO batteryTypeListVO) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);

			StringBuffer sql = new StringBuffer();
			sql.append(" update Battery ");
			sql.append(" set BatteryTypeCode = null ");
			sql.append(" where BatteryTypeCode in ("+batteryTypeListVO.getBatteryTypeCodeArr()+") ");
			ps = connection.prepareStatement(sql.toString());
			ps.execute();			
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" delete from BatteryTypeList ");
			sql2.append(" where BatteryTypeCode in ("+batteryTypeListVO.getBatteryTypeCodeArr()+") ");
			ps2 = connection.prepareStatement(sql2.toString());			
			ps2.execute();
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
}
