package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.bean.AuthorityBean;
import aptg.battery.bean.ButtonBean;
import aptg.battery.util.JdbcUtil;
import aptg.battery.vo.RoleVO;

public class RoleDAO extends BaseDAO{
	/**
	 * 查詢角色資訊
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRoleInfo(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select ");
		sql.append(" r.RoleId, ");
		sql.append(" r.RoleName, ");
		sql.append(" r.RoleDesc, ");
		sql.append(" r.RoleDescE, ");
		sql.append(" r.RoleDescJ, ");
		sql.append(" count(a.Account) as count ");
		sql.append(" from Role r ");
		sql.append(" left join Account a on a.RoleId = r.RoleId ");
		if(StringUtils.isNotBlank(roleVO.getCompanyCode())) {
			sql.append(" and a.Company = ? ");
			parameterList.add(roleVO.getCompanyCode());
		}
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(roleVO.getSystemId())) {
			sql.append(" and r.SystemId = ? ");
			parameterList.add(roleVO.getSystemId());
		}

		if(StringUtils.isNotBlank(roleVO.getRoleRank())) {			
			sql.append(" and r.RoleRank <= ? ");			
			parameterList.add(roleVO.getRoleRank());
		}
		
		sql.append(" group by r.RoleId ");
		sql.append(" order by r.RoleRank desc ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢角色下拉選單
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRoleList(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select RoleId, RoleName, RoleRank from Role  ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(roleVO.getSystemId())) {
			sql.append(" and SystemId = ? ");
			parameterList.add(roleVO.getSystemId());
		}
		if(StringUtils.isNotBlank(roleVO.getRoleRankLE())) {			
			sql.append(" and RoleRank <= ? ");			
			parameterList.add(roleVO.getRoleRankLE());
		}
		if(StringUtils.isNotBlank(roleVO.getRoleRankLT())) {			
			sql.append(" and RoleRank < ? ");			
			parameterList.add(roleVO.getRoleRankLT());
		}
		
		sql.append(" order by RoleRank desc ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢角色權限
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRoleAuthority(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select p.FunctionId,  ");
		sql.append("        p.Edit, ");
		sql.append("        b.ButtonId, ");
		sql.append("        b.Enabled ");
		sql.append(" from Role r, ");
		sql.append(" 	 RoleMapping rm, ");
		sql.append("      (select a.AuthorityId, a.FunctionId, a.Edit, f.ProgramId from Authority a, ");
		sql.append("      FunctionList f where  a.FunctionId = f.FunctionId) p ");
		sql.append("      left join Button b ");
		sql.append("      on b.AuthorityId = p.AuthorityId ");
		sql.append("      and b.ProgramId = p.ProgramId ");
		sql.append(" where  1=1 ");
		if(StringUtils.isNotBlank(roleVO.getSystemId())) {
			sql.append(" and r.SystemId = ? ");
			parameterList.add(roleVO.getSystemId());
		}
		if(StringUtils.isNotBlank(roleVO.getRoleId())) {			
			sql.append(" and r.RoleId = ? ");
			parameterList.add(roleVO.getRoleId());
		}
		sql.append(" and r.RoleId = rm.RoleId ");
		sql.append(" and rm.AuthorityId = p.AuthorityId ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢帳號角色等級
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRoleRank(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select r.RoleId, r.RoleRank ");
		sql.append(" from Account a, Role r ");
		sql.append(" where a.RoleId = r.RoleId ");
		if(StringUtils.isNotBlank(roleVO.getSystemId())) {
			sql.append(" and a.SystemId = ? ");
			parameterList.add(roleVO.getSystemId());
		}
		sql.append(" and a.Account = ? ");
		parameterList.add(roleVO.getAccount());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢最大角色ID
	 * @param systemId
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getMaxRoleId(String systemId) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select max(RoleId)+1 as roleid ");
		sql.append(" from Role ");
		sql.append(" where SystemId = ? ");		
		parameterList.add(systemId);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增角色
	 * @param roleVO
	 * @throws Exception
	 */
	public void addRole(RoleVO roleVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
						
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO Role ( ");
			sql.append(" SystemId, ");
			sql.append(" RoleId, ");
			sql.append(" RoleName, ");
			sql.append(" RoleDesc, ");
			sql.append(" RoleDescE, ");
			sql.append(" RoleDescJ, ");
			sql.append(" RoleRank, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?)  ");	

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, roleVO.getSystemId());
			ps.setString(2, roleVO.getRoleId());
			ps.setString(3, roleVO.getRoleName());
			ps.setString(4, roleVO.getRoleDesc());
			ps.setString(5, roleVO.getRoleDescE());
			ps.setString(6, roleVO.getRoleDescJ());
			ps.setString(7, roleVO.getRoleRank());
			ps.setString(8, roleVO.getUserName());
			ps.setString(9, roleVO.getUserName());
			ps.execute();
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" INSERT INTO RoleMapping ( ");
			sql2.append(" SystemId, ");
			sql2.append(" RoleId, ");
			sql2.append(" AuthorityId ");
			sql2.append(" ) VALUES (?,?,?)  ");	

			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, roleVO.getSystemId());
			ps2.setString(2, roleVO.getRoleId());
			ps2.setString(3, roleVO.getAuthorityId());
			ps2.execute();
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" INSERT INTO Authority ( ");
			sql3.append(" SystemId, ");
			sql3.append(" AuthorityId, ");
			sql3.append(" FunctionId, ");
			sql3.append(" Edit, ");
			sql3.append(" Operate, ");
			sql3.append(" CreateUserName, ");
			sql3.append(" UpdateUserName ");
			sql3.append(" ) ");
			sql3.append(" select SystemId, ");
			sql3.append(" ?, ");
			sql3.append(" FunctionId, ");
			sql3.append(" Edit, ");
			sql3.append(" Operate, ");
			sql3.append(" ?, ");
			sql3.append(" ? ");
			sql3.append(" from Authority ");
			sql3.append(" where SystemId = ? ");
			sql3.append(" and AuthorityId = ? ");
			ps3 = connection.prepareStatement(sql3.toString());
			ps3.setString(1, roleVO.getAuthorityId());
			ps3.setString(2, roleVO.getUserName());
			ps3.setString(3, roleVO.getUserName());
			ps3.setString(4, roleVO.getSystemId());
			ps3.setString(5, roleVO.getCopyAuthorityId());										
			ps3.execute();			
			
			StringBuffer sql4 = new StringBuffer();
			sql4.append(" INSERT INTO Button ( ");
			sql4.append(" AuthorityId, ");
			sql4.append(" ProgramId, ");
			sql4.append(" ButtonId, ");
			sql4.append(" ButtonDesc, ");
			sql4.append(" Enabled ");
			sql4.append(" ) ");
			sql4.append(" select ?, ");
			sql4.append(" ProgramId, ");
			sql4.append(" ButtonId, ");
			sql4.append(" ButtonDesc, ");
			sql4.append(" Enabled ");
			sql4.append(" from Button ");
			sql4.append(" where AuthorityId = ? ");
			ps4 = connection.prepareStatement(sql4.toString());		
			ps4.setString(1, roleVO.getAuthorityId());
			ps4.setString(2, roleVO.getCopyAuthorityId());			
			ps4.execute();			
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 刪除角色
	 * @param roleVO
	 * @throws Exception
	 */
	public void delRole(RoleVO roleVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
						
			StringBuffer sql = new StringBuffer();
			sql.append(" delete from Role  ");
			sql.append(" where SystemId = ? ");
			sql.append(" and RoleId = ? ");
			
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, roleVO.getSystemId());
			ps.setString(2, roleVO.getRoleId());		
			ps.execute();
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" delete from RoleMapping ");
			sql2.append(" where SystemId = ? ");
			sql2.append(" and RoleId = ? ");
			
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, roleVO.getSystemId());
			ps2.setString(2, roleVO.getRoleId());
			ps2.execute();
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" delete from Authority ");
			sql3.append(" where SystemId = ? ");
			sql3.append(" and AuthorityId = ? ");
			
			ps3 = connection.prepareStatement(sql3.toString());
			ps3.setString(1, roleVO.getSystemId());
			ps3.setString(2, roleVO.getAuthorityId());
			ps3.execute();
			
			StringBuffer sql4 = new StringBuffer();
			sql4.append(" delete from Button where AuthorityId = ? ");
			
			ps4 = connection.prepareStatement(sql4.toString());
			ps4.setString(1, roleVO.getAuthorityId());		
			ps4.execute();
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 查詢角色
	 * @param systemId
	 * @param roleId
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRole(String systemId, String roleId) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select * ");
		sql.append(" from Role ");
		sql.append(" where SystemId = ? ");
		sql.append(" and RoleId = ? ");
		parameterList.add(systemId);
		parameterList.add(roleId);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢角色
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getRole(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select * ");
		sql.append(" from Role ");
		sql.append(" where SystemId = ? ");
		sql.append(" and RoleId = ? ");
		parameterList.add(roleVO.getSystemId());
		parameterList.add(roleVO.getRoleId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核角色名稱
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> checkRoleName(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select 1 ");
		sql.append(" from Role ");
		sql.append(" where SystemId = ? ");
		sql.append(" and RoleName = ? ");
		parameterList.add(roleVO.getSystemId());
		parameterList.add(roleVO.getRoleName());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得權限ID
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getAuthorityId(RoleVO roleVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select AuthorityId ");
		sql.append(" from RoleMapping ");
		sql.append(" where SystemId = ? ");
		sql.append(" and RoleId = ? ");
		parameterList.add(roleVO.getSystemId());
		parameterList.add(roleVO.getRoleId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改角色權限設定
	 * @param roleVO
	 * @throws Exception
	 */
	public void updRoleAuthority(RoleVO roleVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);

			StringBuffer sql = new StringBuffer();
			sql.append(" delete from Authority ");
			sql.append(" where SystemId = ? ");
			sql.append(" and AuthorityId = ? ");
			
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, roleVO.getSystemId());
			ps.setString(2, roleVO.getAuthorityId());
			ps.execute();
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" delete from Button where AuthorityId = ? ");
			
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, roleVO.getAuthorityId());		
			ps2.execute();
			
			
			if(roleVO.getAuthorityList()!=null && roleVO.getAuthorityList().size()>0) {
				StringBuffer sql3 = new StringBuffer();
				sql3.append(" INSERT INTO Authority ( ");
				sql3.append(" SystemId, ");
				sql3.append(" AuthorityId, ");
				sql3.append(" FunctionId, ");
				sql3.append(" Edit, ");
				sql3.append(" Operate, ");
				sql3.append(" CreateUserName, ");
				sql3.append(" UpdateUserName ");
				sql3.append(" ) VALUES (?,?,?,?,?,?,?) ");
				ps3 = connection.prepareStatement(sql3.toString());
				for(AuthorityBean authority : roleVO.getAuthorityList()) {
					ps3.setInt(1, authority.getSystemId());
					ps3.setInt(2, authority.getAuthorityId());
					ps3.setInt(3, authority.getFunctionId());
					ps3.setInt(4, authority.getEdit());
					ps3.setInt(5, authority.getOperate());
					ps3.setString(6, roleVO.getUserName());	
					ps3.setString(7, roleVO.getUserName());	
					ps3.addBatch();		
				}
				ps3.executeBatch();
			}
			
			if(roleVO.getButtonList()!=null && roleVO.getButtonList().size()>0) {
				StringBuffer sql4 = new StringBuffer();
				sql4.append(" INSERT INTO Button ( ");
				sql4.append(" AuthorityId, ");
				sql4.append(" ProgramId, ");
				sql4.append(" ButtonId, ");
				sql4.append(" ButtonDesc, ");
				sql4.append(" Enabled ");
				sql4.append(" ) VALUES (?,?,?,?,?) ");
				ps4 = connection.prepareStatement(sql4.toString());
				for(ButtonBean button : roleVO.getButtonList()) {
					ps4.setInt(1, button.getAuthorityId());
					ps4.setInt(2, button.getProgramId());
					ps4.setString(3, button.getButtonId());
					ps4.setString(4, button.getButtonDesc());
					ps4.setInt(5, button.getEnabled());
					ps4.addBatch();		
				}
				ps4.executeBatch();
			}
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 取得URL
	 * @param roleVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getProgramUrl(String programId) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select Url ");
		sql.append(" from ProgramList ");
		sql.append(" where ProgramId = ? ");
		parameterList.add(programId);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
