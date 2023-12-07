package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.bean.AccountTempBean;
import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

public class AdminSetupDAO extends BaseDAO{
	
	/**
	 * 取得使用者資訊
	 * @param adminSetupVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getAdminSetup(AdminSetupVO adminSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		  
		sql.append(" select b.AreaName, a.* ");
		sql.append(" from AdminSetup a ");
		sql.append(" left join Area b on b.SeqNo = a.AreaCodeNo ");
		
		sql.append(" where 1=1 ");
		
		if(StringUtils.isNotBlank(adminSetupVO.getAccount())) {
			sql.append(" and a.Account = ? ");
			parameterList.add(adminSetupVO.getAccount());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getUserName())) {
			sql.append(" and a.UserName = ? ");
			parameterList.add(adminSetupVO.getUserName());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getEnabled())) {
			sql.append(" and a.Enabled = ? ");
			parameterList.add(adminSetupVO.getEnabled());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getSuspend())) {
			sql.append(" and a.Suspend = ? ");
			parameterList.add(adminSetupVO.getSuspend());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getVerifyCode())) {
			sql.append(" and a.VerifyCode = ? ");
			parameterList.add(adminSetupVO.getVerifyCode());
		}
		
		if(StringUtils.isNotBlank(adminSetupVO.getBankCodeArr())) {
			sql.append(" and a.BankCode in ("+adminSetupVO.getBankCodeArr()+") ");
		}
		if(StringUtils.isNotBlank(adminSetupVO.getBankCode())) {
			sql.append(" and a.BankCode = ? ");
			parameterList.add(adminSetupVO.getBankCode());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getRankCodeBE())) {
			sql.append(" and a.RankCode >= ? ");
			parameterList.add(adminSetupVO.getRankCodeBE());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getRankCodeBT())) {
			sql.append(" and a.RankCode > ? ");
			parameterList.add(adminSetupVO.getRankCodeBT());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增帳號
	 * @param adminSetupVO
	 * @throws Exception 
	 */
	public void addAdminSetup(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO AdminSetup ( ");
			sql.append(" Account, ");
			sql.append(" UserName, ");
			sql.append(" BankCode, ");
			sql.append(" RankCode, ");
			sql.append(" Enabled, ");
			sql.append(" Email, ");
			sql.append(" Suspend, ");			
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			if(StringUtils.isNotBlank(adminSetupVO.getAreaCodeNo())) {
				sql.append(" ,AreaCodeNo ");	
			}
			
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?,? ");
			if(StringUtils.isNotBlank(adminSetupVO.getAreaCodeNo())) {
				sql.append(" ,? ");	
			}
			sql.append(" ) ");
			
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getAccount());
			ps.setString(2, adminSetupVO.getAccountName());
			ps.setString(3, adminSetupVO.getBankCode());
			ps.setString(4, adminSetupVO.getRankCode());
			ps.setString(5, adminSetupVO.getEnabled());
			ps.setString(6, adminSetupVO.getEmail());
			ps.setString(7, adminSetupVO.getSuspend());
			ps.setString(8, adminSetupVO.getUserName());
			ps.setString(9, adminSetupVO.getUserName());
			if(StringUtils.isNotBlank(adminSetupVO.getAreaCodeNo())) {
				ps.setString(10, adminSetupVO.getAreaCodeNo());
			}
			ps.executeUpdate();
			
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" INSERT INTO Account( ");
			sql2.append(" SystemId, ");
			sql2.append(" Account, ");
			sql2.append(" UserName, ");
			sql2.append(" RoleId ");
			sql2.append(" ) VALUES (?,?,?,?) ");

			ps2 = connection.prepareStatement(sql2.toString());							
			ps2.setString(1, ToolUtil.getSystemId());
			ps2.setString(2, adminSetupVO.getAccount());
			ps2.setString(3, adminSetupVO.getAccountName());
			ps2.setString(4, adminSetupVO.getRankCode());
			ps2.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}

	/**
	 * 修改帳號
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void updAdminSetup(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			boolean updpwFlag = false;
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			if(adminSetupVO.isAddHistory()) {
				StringBuffer sql = new StringBuffer();
				sql.append(" INSERT INTO AdminSetupHistory( ");
				sql.append(" RecTime, ");
				sql.append(" Account, ");
				sql.append(" UserName, ");
				sql.append(" BankCode, ");
				sql.append(" RankCode, ");
				sql.append(" Enabled, ");
				sql.append(" Email, ");
				sql.append(" Suspend, ");
				sql.append(" AreaCodeNo, ");
				sql.append(" CreateUserName, ");
				sql.append(" UpdateUserName ");
				sql.append(" ) select  ");
				sql.append(" now(), ");
				sql.append(" Account, ");
				sql.append(" UserName, ");
				sql.append(" BankCode, ");
				sql.append(" RankCode, ");
				sql.append(" Enabled, ");
				sql.append(" Email, ");
				sql.append(" Suspend, ");
				sql.append(" AreaCodeNo, ");
				sql.append(" ?, ");
				sql.append(" ? ");
				sql.append(" from AdminSetup where Account = ? ");
				ps = connection.prepareStatement(sql.toString());
				ps.setString(1, adminSetupVO.getUserName());
				ps.setString(2, adminSetupVO.getUserName());
				ps.setString(3, adminSetupVO.getAccount());
				ps.executeUpdate();
			}
			
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update AdminSetup set ");
			if(StringUtils.isNotBlank(adminSetupVO.getPassword())) {
				sql1.append(" Password = ?, ");
				updpwFlag = true;
			}
			sql1.append(" UserName = ?, ");
			sql1.append(" BankCode = ?, ");
			sql1.append(" RankCode = ?, ");
			sql1.append(" Email = ?, ");
			sql1.append(" Suspend = ?, ");
			sql1.append(" UpdateUserName = ? ");
			if(StringUtils.isNotBlank(adminSetupVO.getAreaCodeNo())) {
				sql1.append(" ,AreaCodeNo = ? ");
			}
			sql1.append(" where Account = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			int i =1;
			if(updpwFlag) {
				ps1.setString(i++, adminSetupVO.getPassword());
			}
			ps1.setString(i++, adminSetupVO.getAccountName());
			ps1.setString(i++, adminSetupVO.getBankCode());
			ps1.setString(i++, adminSetupVO.getRankCode());
			ps1.setString(i++, adminSetupVO.getEmail());
			ps1.setString(i++, adminSetupVO.getSuspend());
			ps1.setString(i++, adminSetupVO.getUserName());
			if(StringUtils.isNotBlank(adminSetupVO.getAreaCodeNo())) {
				ps1.setString(i++, adminSetupVO.getAreaCodeNo());
			}
			ps1.setString(i++, adminSetupVO.getAccount());
			ps1.executeUpdate();
			
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" update Account set ");
			if(updpwFlag) {
				sql2.append(" Password = ?, ");
			}
			sql2.append(" UserName = ?, ");
			sql2.append(" RoleId = ? ");
			sql2.append(" where SystemId = ? ");
			sql2.append(" and Account = ? ");
			ps2 = connection.prepareStatement(sql2.toString());
			i =1;
			if(updpwFlag) {
				ps2.setString(i++, adminSetupVO.getPassword());
			}
			ps2.setString(i++, adminSetupVO.getAccountName());
			ps2.setString(i++, adminSetupVO.getRankCode());
			ps2.setString(i++, ToolUtil.getSystemId());
			ps2.setString(i++, adminSetupVO.getAccount());
			ps2.executeUpdate();
						
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 帳號資料
	 * @param adminSetupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAdminSetupList(AdminSetupVO adminSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" a.Account, ");
		sql.append(" a.UserName, ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" a.Email, ");
		sql.append(" r.RankCode, ");
		sql.append(" r.RankDesc, ");
		sql.append(" a.Enabled, ");
		sql.append(" a.Suspend, ");
		sql.append(" a.AreaCodeNo, ");
		sql.append(" e.AreaCode, ");
		sql.append(" e.AreaName ");
		sql.append(" from PostCode p,  ");
		sql.append(" BankInf b, ");
		sql.append(" AdminSetup a  ");
		sql.append(" left join Area e on e.seqno = a.AreaCodeNo, ");
		sql.append(" RankList r ");
		sql.append(" where p.seqno = b.PostCodeNo ");
		sql.append(" and b.BankCode = a.BankCode ");
		sql.append(" and a.RankCode = r.RankCode ");
		if(StringUtils.isNotBlank(adminSetupVO.getAccount())) {
			sql.append(" and a.Account = ? ");	
			parameterList.add(adminSetupVO.getAccount());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getCity())) {
			sql.append(" and p.City = ? ");	
			parameterList.add(adminSetupVO.getCity());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getPostCodeNo())) {
			sql.append(" and p.seqno = ? ");
			parameterList.add(adminSetupVO.getPostCodeNo());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getBankCode())) {
			sql.append(" and b.BankCode = ? ");
			parameterList.add(adminSetupVO.getBankCode());
		}
		if(StringUtils.isNotBlank(adminSetupVO.getRankCodeArr())) {
			sql.append(" and a.RankCode in ("+adminSetupVO.getRankCodeArr()+") ");			
		}
		if(StringUtils.isNotBlank(adminSetupVO.getRankCodeBE())) {
			sql.append(" and r.RankCode >= ? ");
			parameterList.add(adminSetupVO.getRankCodeBE());
		}

		sql.append(" order by b.BankCode, a.Account ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 帳號權限
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getRankCode(String account) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select RankCode, BankCode, AreaCodeNo ");
		sql.append(" from AdminSetup   ");	
		sql.append(" where Account = ? ");
		parameterList.add(account);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	
	/**
	 * 檢核帳號是否存在(存在:false, 不存在:true)
	 * @param adminSetupVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public boolean checkAdminSetup(String account) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();		
		sql.append(" select 1 from AdminSetup where  Account = ? ");
		parameterList.add(account);
		
		List<DynaBean> list = this.executeQuery(sql.toString(), parameterList);
		if(list!=null && list.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 批量新增帳號暫存檔
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void addAccountTempBatch(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
				
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO AccountTemp( ");
			sql.append(" UUID, ");
			sql.append(" Account, ");
			sql.append(" UserName, ");
			sql.append(" BankCode, ");
			sql.append(" Email, ");
			sql.append(" RankCode, ");
			sql.append(" AreaCodeNo, ");
			sql.append(" Suspend, ");
			sql.append(" ProcessFlag, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			ps = connection.prepareStatement(sql.toString());
			for(AccountTempBean bean : adminSetupVO.getAccountTempList()) {
				ps.setString(1, bean.getUuid());
				ps.setString(2, bean.getAccount());
				ps.setString(3, bean.getAccountName());
				ps.setString(4, bean.getBankCode());
				ps.setString(5, bean.getEmail());
				ps.setString(6, bean.getRankCode());
				ps.setString(7, bean.getAreaCodeNo());
				ps.setString(8, bean.getSuspend());
				ps.setString(9, bean.getProcessFlag());
				ps.setString(10, bean.getUserName());
				ps.setString(11, bean.getUserName());
				ps.addBatch();
			}
			ps.executeBatch();
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 批量新增帳號暫存檔
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void accountTempToAdminSetup(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			//新增
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO AdminSetup( ");
			sql.append(" Account, ");
			sql.append(" UserName, ");
			sql.append(" BankCode, ");
			sql.append(" RankCode, ");
			sql.append(" Enabled, ");
			sql.append(" Email, ");
			sql.append(" Suspend, ");
			sql.append(" AreaCodeNo, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" )  ");
			sql.append(" select  ");
			sql.append(" Account, ");
			sql.append(" UserName, ");
			sql.append(" BankCode, ");
			sql.append(" RankCode, ");
			sql.append(" 0, ");
			sql.append(" Email, ");
			sql.append(" Suspend, ");
			sql.append(" AreaCodeNo, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" from AccountTemp ");
			sql.append(" where UUID = ?  ");
			sql.append(" and ProcessFlag = 'A' ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getUuid());
			ps.executeUpdate();
			
			StringBuffer sql0 = new StringBuffer();
			sql0.append(" INSERT INTO Account( ");
			sql0.append(" SystemId, ");
			sql0.append(" Account, ");
			sql0.append(" UserName, ");
			sql0.append(" RoleId ");
			sql0.append(" ) ");
			sql0.append(" select  ");
			sql0.append(" ?, ");
			sql0.append(" Account, ");
			sql0.append(" UserName, ");
			sql0.append(" RankCode ");
			sql0.append(" from AccountTemp ");
			sql0.append(" where UUID = ?  ");
			sql0.append(" and ProcessFlag = 'A' ");

			ps0 = connection.prepareStatement(sql0.toString());							
			ps0.setString(1, ToolUtil.getSystemId());
			ps0.setString(2, adminSetupVO.getUuid());
			ps0.executeUpdate();
			
			
			//修改
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" INSERT INTO AdminSetupHistory( ");
			sql1.append(" RecTime, ");
			sql1.append(" Account, ");
			sql1.append(" UserName, ");
			sql1.append(" BankCode, ");
			sql1.append(" RankCode, ");
			sql1.append(" Enabled, ");
			sql1.append(" Email, ");
			sql1.append(" Suspend, ");
			sql1.append(" AreaCodeNo, ");
			sql1.append(" CreateUserName, ");
			sql1.append(" UpdateUserName ");
			sql1.append(" ) select  ");
			sql1.append(" now(), ");
			sql1.append(" a.Account, ");
			sql1.append(" a.UserName, ");
			sql1.append(" a.BankCode, ");
			sql1.append(" a.RankCode, ");
			sql1.append(" a.Enabled, ");
			sql1.append(" a.Email, ");
			sql1.append(" a.Suspend, ");
			sql1.append(" a.AreaCodeNo, ");
			sql1.append(" t.CreateUserName, ");
			sql1.append(" t.CreateUserName ");
			sql1.append(" from AdminSetup a, AccountTemp t ");
			sql1.append(" where a.Account = t.Account ");
			sql1.append(" and t.UUID = ? ");
			sql1.append(" and t.ProcessFlag in ('U','S') ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, adminSetupVO.getUuid());
			ps1.executeUpdate();
						
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" update AdminSetup a, AccountTemp t ");
			sql2.append(" set ");
			sql2.append(" a.UserName = t.UserName, ");
			sql2.append(" a.BankCode = t.BankCode, ");
			sql2.append(" a.RankCode = t.RankCode, ");
			sql2.append(" a.Email = t.Email, ");
			sql2.append(" a.Suspend = t.Suspend, ");
			sql2.append(" a.AreaCodeNo = t.AreaCodeNo, ");
			sql2.append(" a.UpdateUserName = t.CreateUserName ");
			sql2.append(" where a.Account = t.Account ");
			sql2.append(" and t.UUID = ? ");
			sql2.append(" and t.ProcessFlag = 'U' ");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, adminSetupVO.getUuid());
			ps2.executeUpdate();
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" update AdminSetup a, AccountTemp t ");
			sql3.append(" set ");
			sql3.append(" a.Suspend = t.Suspend, ");
			sql3.append(" a.UpdateUserName = t.CreateUserName ");
			sql3.append(" where a.Account = t.Account ");
			sql3.append(" and t.UUID = ? ");
			sql3.append(" and t.ProcessFlag = 'S' ");
			ps3 = connection.prepareStatement(sql3.toString());
			ps3.setString(1, adminSetupVO.getUuid());
			ps3.executeUpdate();
			
			StringBuffer sql4 = new StringBuffer();
			sql4.append(" delete from AccountTemp ");			
			sql4.append(" where UUID = ? ");
			ps4 = connection.prepareStatement(sql4.toString());
			ps4.setString(1, adminSetupVO.getUuid());
			ps4.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps0);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 刪除帳號暫存檔
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void delAccountTemp(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" delete from AccountTemp ");			
			sql.append(" where UUID = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getUuid());
			ps.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	


	
	/**
	 * 新增驗證碼
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void addVerifyCode(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update AdminSetup set ");
			sql.append(" VerifyCode = ?, ");
			sql.append(" ExpireDate = ?, ");
			sql.append(" UpdateUserName = ? ");
			sql.append(" where Account = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getVerifyCode());
			ps.setString(2, adminSetupVO.getExpireDate());
			ps.setString(3, adminSetupVO.getUserName());
			ps.setString(4, adminSetupVO.getAccount());
			ps.executeUpdate();
						
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 修改密碼
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void updPassword(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			if(adminSetupVO.isAddHistory()) {
				StringBuffer sql = new StringBuffer();
				sql.append(" INSERT INTO AdminSetupHistory( ");
				sql.append(" RecTime, ");
				sql.append(" Account, ");
				sql.append(" UserName, ");
				sql.append(" BankCode, ");
				sql.append(" RankCode, ");
				sql.append(" Enabled, ");
				sql.append(" Email, ");
				sql.append(" Suspend, ");
				sql.append(" AreaCodeNo, ");
				sql.append(" CreateUserName, ");
				sql.append(" UpdateUserName ");
				sql.append(" ) select  ");
				sql.append(" now(), ");
				sql.append(" Account, ");
				sql.append(" UserName, ");
				sql.append(" BankCode, ");
				sql.append(" RankCode, ");
				sql.append(" Enabled, ");
				sql.append(" Email, ");
				sql.append(" Suspend, ");
				sql.append(" AreaCodeNo, ");
				sql.append(" UserName, ");
				sql.append(" UserName ");
				sql.append(" from AdminSetup where Account = ? ");
				ps = connection.prepareStatement(sql.toString());
				ps.setString(1, adminSetupVO.getAccount());
				ps.executeUpdate();
			}
			
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update AdminSetup set ");
			sql1.append(" Password = ?, ");
			sql1.append(" Enabled = 1, ");
			sql1.append(" VerifyCode = null, ");
			sql1.append(" ExpireDate = null, ");
			sql1.append(" UpdateUserName = UserName ");
			sql1.append(" where Account = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, adminSetupVO.getPassword());
			ps1.setString(2, adminSetupVO.getAccount());
			ps1.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();					
			sql2.append(" update Account set ");
			sql2.append(" Password = ?, ");
			sql2.append(" Enabled = 1 ");
			sql2.append(" where SystemId = ? ");
			sql2.append(" and Account = ? ");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, adminSetupVO.getPassword());			
			ps2.setString(2, ToolUtil.getSystemId());
			ps2.setString(3, adminSetupVO.getAccount());				
			
			ps2.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 檢查匯入是否鎖定
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> checkImport() throws Exception {
		return this.executeQuery("select * from ImportLock " , null);
	}
	
	/**
	 * 匯入鎖定
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void importLock(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update ImportLock set ");
			sql.append(" Locked = 1, ");
			sql.append(" LockTime = ?, ");
			sql.append(" UpdateUserName = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getLockTime());
			ps.setString(2, adminSetupVO.getUserName());
			ps.executeUpdate();			

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 匯入解鎖
	 * @param adminSetupVO
	 * @throws Exception
	 */
	public void importUnlock(AdminSetupVO adminSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update ImportLock set ");
			sql.append(" Locked = 0, ");
			sql.append(" LockTime = null, ");
			sql.append(" UpdateUserName = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, adminSetupVO.getUserName());
			ps.executeUpdate();			

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
}
