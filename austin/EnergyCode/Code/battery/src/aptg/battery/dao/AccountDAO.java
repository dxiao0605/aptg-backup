package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.util.JdbcUtil;
import aptg.battery.vo.AccountVO;

public class AccountDAO extends BaseDAO {
	
	/**
	 * 取得帳號
	 * @param accountVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getAccount(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from Account  where 1=1 ");
		
		if(StringUtils.isNotBlank(accountVO.getSystemId())) {
			sql.append(" and SystemId = ? ");
			parameterList.add(accountVO.getSystemId());
		}
		if(StringUtils.isNotBlank(accountVO.getAccount())) {
			sql.append(" and Account = ? ");
			parameterList.add(accountVO.getAccount());
		}
		if(StringUtils.isNotBlank(accountVO.getRoleId())) {
			sql.append(" and RoleId = ? ");
			parameterList.add(accountVO.getRoleId());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得帳號資訊
	 * @param accountVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getAccountInfo(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.Lng, c.Lat, a.* from Account a, Company c where a.Company = c.CompanyCode ");
		
		if(StringUtils.isNotBlank(accountVO.getSystemId())) {
			sql.append(" and a.SystemId = ? ");
			parameterList.add(accountVO.getSystemId());
		}
		if(StringUtils.isNotBlank(accountVO.getAccount())) {
			sql.append(" and a.Account = ? ");
			parameterList.add(accountVO.getAccount());
		}
		if(StringUtils.isNotBlank(accountVO.getEnabled())) {
			sql.append(" and a.Enabled = ? ");
			parameterList.add(accountVO.getEnabled());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 帳戶設定
	 * @param accountVO
	 * @throws Exception
	 */
	public void updAccount(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE Account Set ");
		if(StringUtils.isNotBlank(accountVO.getTimeZone())) {
			sql.append(" TimeZone = ?, ");
			parameterList.add(accountVO.getTimeZone());
		}
		sql.append(" Language = ? ");		
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(accountVO.getLanguage());		
		parameterList.add(accountVO.getSystemId());
		parameterList.add(accountVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 取得使用者資訊
	 * @param accountVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getUserInfo(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" a.UserName, ");
		sql.append(" a.Account, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" c.CompanyName, ");		
		sql.append(" r.RoleName, ");
		sql.append(" r.RoleRank, ");
		sql.append(" a.LastLogin, ");
		sql.append(" a.Mobile, ");
		sql.append(" a.Email, ");
		sql.append(" a.RoleId, ");
		sql.append(" a.CreateTime, ");
		sql.append(" a.TimeZone, ");
		sql.append(" a.Language ");
		sql.append(" from Account a, ");
		sql.append(" Company c, ");
		sql.append(" Role r ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(accountVO.getSystemId())) {
			sql.append(" and a.SystemId = ? ");
			parameterList.add(accountVO.getSystemId());
		}
		if(StringUtils.isNotBlank(accountVO.getCompanyCode())) {
			sql.append(" and a.Company = ? ");
			parameterList.add(accountVO.getCompanyCode());
		}
		sql.append(" and a.Company = c.CompanyCode ");
		sql.append(" and r.RoleId = a.RoleId ");
		if(StringUtils.isNotBlank(accountVO.getRoleRank())) {			
			sql.append(" and r.RoleRank <= ? ");			
			parameterList.add(accountVO.getRoleRank());
		}
		sql.append(" order by c.CompanyCode, r.RoleRank desc, a.RoleId, a.Account ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增使用資訊
	 * @param accountVO
	 * @throws Exception
	 */
	public void addUser(AccountVO accountVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			if(StringUtils.isBlank(accountVO.getCompanyCode())) {
				StringBuffer sql1 = new StringBuffer();
				sql1.append(" INSERT INTO Company ( ");
				sql1.append(" CompanyName, ");
				sql1.append(" Alert1, ");
				sql1.append(" Alert2, ");
				sql1.append(" Disconnect, ");
				sql1.append(" ShowName, ");
				sql1.append(" ShortName, ");
				sql1.append(" CreateUserName, ");
				sql1.append(" UpdateUserName ");			
				sql1.append(" ) VALUES (?, 15000, 50000, 21600, ?, ?, ?, ?)  ");	
	
				ps1 = connection.prepareStatement(sql1.toString(), Statement.RETURN_GENERATED_KEYS);
				ps1.setString(1, accountVO.getCompanyName());
				ps1.setString(2, accountVO.getShowName());
				ps1.setString(3, accountVO.getShortName());
				ps1.setString(4, accountVO.getUserName());			
				ps1.setString(5, accountVO.getUserName());
				ps1.executeUpdate();
			
				rs = ps1.getGeneratedKeys();				
				while(rs.next()){         
					int companyCode = rs.getInt(1);
					accountVO.setCompanyCode(String.valueOf(companyCode));		            
				}
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append(" INSERT INTO BatteryGroup ( ");
				sql2.append(" CompanyCode, ");
				sql2.append(" GroupName, ");
				sql2.append(" Country, ");
				sql2.append(" Area, ");
				sql2.append(" CreateUserName, ");
				sql2.append(" UpdateUserName ");
				sql2.append(" ) VALUES (?, ?, ?, ?, ?, ?)  ");	
	
				ps2 = connection.prepareStatement(sql2.toString());
				ps2.setString(1, accountVO.getCompanyCode());
				ps2.setString(2, accountVO.getShowName()+"(default)");
				ps2.setString(3, accountVO.getShowName()+"(default)");
				ps2.setString(4, accountVO.getShowName()+"(default)");
				ps2.setString(5, accountVO.getUserName());
				ps2.setString(6, accountVO.getUserName());			
				ps2.execute();
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO Account ( ");
			sql.append(" SystemId, ");
			sql.append(" Account, ");
			sql.append(" Company, ");
			sql.append(" UserName, ");
			sql.append(" Password, ");
			sql.append(" RoleId, ");
			sql.append(" Enabled, ");
			sql.append(" EnabledTime, ");	
			sql.append(" Email, ");
			sql.append(" Language, ");
			sql.append(" TimeZone, ");
			sql.append(" Mobile, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");	

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, accountVO.getSystemId());
			ps.setString(2, accountVO.getAccount());
			ps.setString(3, accountVO.getCompanyCode());
			ps.setString(4, accountVO.getName());
			ps.setString(5, accountVO.getPassword());
			ps.setString(6, accountVO.getRoleId());
			ps.setString(7, accountVO.getEnabled());
			ps.setString(8, accountVO.getEnabledTime());
			ps.setString(9, accountVO.getEmail());
			ps.setString(10, accountVO.getLanguage());
			ps.setString(11, accountVO.getTimeZone());
			ps.setString(12, accountVO.getMobile());
			ps.setString(13, accountVO.getUserName());
			ps.setString(14, accountVO.getUserName());
			ps.execute();
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 修改使用者資訊
	 * @param accountVO
	 * @throws Exception
	 */
	public void updUser(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE Account Set ");
		sql.append(" UserName = ?, ");
		sql.append(" Mobile = ?, ");
		sql.append(" Email = ?, ");
		sql.append(" RoleId = ?, ");
		sql.append(" Language = ?, ");
		sql.append(" TimeZone = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(accountVO.getName());
		parameterList.add(accountVO.getMobile());
		parameterList.add(accountVO.getEmail());
		parameterList.add(accountVO.getRoleId());
		parameterList.add(accountVO.getLanguage());		
		parameterList.add(accountVO.getTimeZone());
		parameterList.add(accountVO.getUserName());
		parameterList.add(accountVO.getSystemId());
		parameterList.add(accountVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 刪除使用者
	 * @param accountVO
	 * @throws Exception
	 */
	public void delUser(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from Account ");
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(accountVO.getSystemId());
		parameterList.add(accountVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改密碼
	 * @param accountVO
	 * @throws Exception
	 */
	public void updPassword(AccountVO accountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE Account Set ");
		if(StringUtils.isNotBlank(accountVO.getDisableTime())) {
			sql.append(" DisableTime = ?, ");
			parameterList.add(accountVO.getDisableTime());
		}else {
			sql.append(" DisableTime = null, ");	
		}
		sql.append(" Password = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(accountVO.getPassword());
		parameterList.add(accountVO.getUserName());
		parameterList.add(accountVO.getSystemId());
		parameterList.add(accountVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
}
