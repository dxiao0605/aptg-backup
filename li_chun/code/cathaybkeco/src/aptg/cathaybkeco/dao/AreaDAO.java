package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.vo.AreaVO;

public class AreaDAO extends BaseDAO{
	
	/**
	 * 取得區域資訊
	 * @param adminSetupVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getArea(AreaVO areaVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from Area ");		
		sql.append(" where 1=1 ");		
		if(StringUtils.isNotBlank(areaVO.getAreaCode())) {
			sql.append(" and AreaCode = ? ");
			parameterList.add(areaVO.getAreaCode());
		}

		if(StringUtils.isNotBlank(areaVO.getEnabled())) {
			sql.append(" and Enabled = ? ");
			parameterList.add(areaVO.getEnabled());
		}
		

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增區域
	 * @param areaVO
	 * @throws Exception 
	 */
	public void addArea(AreaVO areaVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		try {
			int seqno = 0;
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO Area ( ");
			sql.append(" AreaCode, ");
			sql.append(" AreaName, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?) ");
			ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, areaVO.getAreaCode());
			ps.setString(2, areaVO.getAreaName());
			ps.setString(3, areaVO.getUserName());
			ps.setString(4, areaVO.getUserName());
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();				
			while(rs.next()){         
				seqno = rs.getInt(1);
			}
			
			List<String> accessBanksList = areaVO.getAccessBanksList();
			if(accessBanksList!=null && accessBanksList.size()>0) {
				StringBuffer sql2 = new StringBuffer();
				sql2.append(" INSERT INTO AccessBanks ( ");
				sql2.append(" AreaCodeNo, ");
				sql2.append(" BankCode, ");
				sql2.append(" CreateUserName, ");
				sql2.append(" UpdateUserName ");
				sql2.append(" ) VALUES (?,?,?,?) ");
				ps2 = connection.prepareStatement(sql2.toString());
				for(String bankCode : accessBanksList) {
					ps2.setInt(1, seqno);
					ps2.setString(2, bankCode);
					ps2.setString(3, areaVO.getUserName());
					ps2.setString(4, areaVO.getUserName());
					ps2.addBatch();
				}
				ps2.executeBatch();
			}
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}

	/**
	 * 修改區域
	 * @param areaVO
	 * @throws Exception
	 */
	public void updArea(AreaVO areaVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update Area set ");
			sql.append(" AreaCode = ?, ");			
			sql.append(" AreaName = ?, ");
			sql.append(" UpdateUserName = ? ");
			sql.append(" where seqno = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, areaVO.getAreaCode());
			ps.setString(2, areaVO.getAreaName());
			ps.setString(3, areaVO.getUserName());
			ps.setString(4, areaVO.getAreaCodeNo());
			ps.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" delete from AccessBanks ");
			sql2.append(" where AreaCodeNo = ? ");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, areaVO.getAreaCodeNo());
			ps2.executeUpdate();
			
			List<String> accessBanksList = areaVO.getAccessBanksList();
			if(accessBanksList!=null && accessBanksList.size()>0) {
				StringBuffer sql3 = new StringBuffer();
				sql3.append(" INSERT INTO AccessBanks ( ");
				sql3.append(" AreaCodeNo, ");
				sql3.append(" BankCode, ");
				sql3.append(" CreateUserName, ");
				sql3.append(" UpdateUserName ");
				sql3.append(" ) VALUES (?,?,?,?) ");
				ps3 = connection.prepareStatement(sql3.toString());
				for(String bankCode : accessBanksList) {
					ps3.setString(1, areaVO.getAreaCodeNo());
					ps3.setString(2, bankCode);
					ps3.setString(3, areaVO.getUserName());
					ps3.setString(4, areaVO.getUserName());
					ps3.addBatch();
				}
				ps3.executeBatch();
			}
				
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 刪除區域
	 * @param areaVO
	 * @throws Exception
	 */
	public void delArea(AreaVO areaVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update Area set ");
			sql.append(" Enabled = 0, ");			
			sql.append(" UpdateUserName = ? ");
			sql.append(" where seqno = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, areaVO.getUserName());
			ps.setString(2, areaVO.getAreaCodeNo());
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
	 * 區域列表
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaList() throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from Area ");		
		sql.append(" where Enabled = 1 ");
		sql.append(" order by AreaCode ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 檢核區域編號是否存在(存在:false, 不存在:true)
	 * @param areaCodeNo
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	public boolean checkAreaCode(String areaCodeNo, String areaCode) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();		
		sql.append(" select 1 from Area where Enabled = 1 ");
		sql.append(" and AreaCode = ? ");		
		parameterList.add(areaCode);
		if(StringUtils.isNotBlank(areaCodeNo)) {
			sql.append(" and seqno <> ? ");
			parameterList.add(areaCodeNo);
		}
		
		
		List<DynaBean> list = this.executeQuery(sql.toString(), parameterList);
		if(list!=null && list.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 檢核區域名稱是否存在(存在:false, 不存在:true)
	 * @param areaCodeNo
	 * @param areaName
	 * @return
	 * @throws Exception
	 */
	public boolean checkAreaName(String areaCodeNo, String areaName) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();		
		sql.append(" select 1 from Area where Enabled = 1 ");
		sql.append(" and AreaName = ? ");
		parameterList.add(areaName);
		if(StringUtils.isNotBlank(areaCodeNo)) {
			sql.append(" and seqno <> ? ");
			parameterList.add(areaCodeNo);
		}		
		List<DynaBean> list = this.executeQuery(sql.toString(), parameterList);
		if(list!=null && list.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 檢核區域管理者/使用者是否存在(存在:false, 不存在:true)
	 * @param areaCodeNo
	 * @param areaName
	 * @return
	 * @throws Exception
	 */
	public boolean checkAreaAccount(String areaCodeNo) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();		
		sql.append(" select 1 from AdminSetup where Suspend = 0 ");
		sql.append(" and AreaCodeNo = ? ");
		parameterList.add(areaCodeNo);
		
		List<DynaBean> list = this.executeQuery(sql.toString(), parameterList);
		if(list!=null && list.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 查詢管理分行
	 * @param areaNoCode
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAccessBanks(String areaCodeNo) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select a.BankCode, b.BankName ");
		sql.append(" from AccessBanks a, BankInf b ");	
		sql.append(" where a.BankCode = b.BankCode ");
		sql.append(" and a.AreaCodeNo = ? ");
		parameterList.add(areaCodeNo);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢帳號
	 * @param areaNoCode
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaAccount(String areaCodeNo) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select Account, RankCode ");
		sql.append(" from AdminSetup ");	
		sql.append(" where Suspend = 0 ");
		sql.append(" and AreaCodeNo = ? ");
		parameterList.add(areaCodeNo);

		return this.executeQuery(sql.toString(), parameterList);
	}
}
