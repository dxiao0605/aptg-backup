package aptg.dao.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import aptg.dao.base.db.ConnectionFactory;
import aptg.dao.base.db.DbUtil;

public class BaseDao {
	protected Connection connection;
	protected PreparedStatement ps;

	protected Connection initConnection() throws SQLException {
		connection = ConnectionFactory.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}
	
	protected PreparedStatement initPreparedStatement(Connection connection, String sql) throws SQLException {
		ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		return ps;
	}
	
	
	protected List<DynaBean> executeQuery(String sql) throws SQLException {
		ResultSet rs = null;
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			//ps.setString(1, cid);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return rows;
	}
	
	/**
	 * 	新增單筆SQL
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int executeInsert(String sql) {
		int idValue = 0;
		ResultSet rs = null;
		try {
			connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//ps.setString(1, cid);
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            // Value of ID (Index 1 by table design).
	            idValue = rs.getInt(1);
	        }
	        
			connection.commit();
			
		} catch(Exception e) {
			DbUtil.rollback(connection);
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return idValue;
	}
	
	/**
	 * 	更新單筆SQL
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql) {
		int count = -1;
		ResultSet rs = null;
		try {
			connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			count = ps.getUpdateCount();
			
			connection.commit();
			
		} catch(Exception e) {
			DbUtil.rollback(connection);
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return count;
	}
	
	
	/**
	 * 	批次新增多筆SQL
	 * 
	 * @param connection
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	protected List<Integer> batchInsert(Connection connection, PreparedStatement ps) {
		List<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			ps.executeBatch();
			
			rs = ps.getGeneratedKeys();
			while(rs.next()) {
				ids.add(rs.getInt(1));
			}
			
			connection.commit();
		} catch(Exception e) {
			DbUtil.rollback(connection);
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return ids;
	}
	
	/**
	 * 	批次更新多筆SQL
	 * 
	 * @param connection
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	protected int batchUpdate(Connection connection, PreparedStatement ps) {
		int count = -1;
		ResultSet rs = null;
		try {
			int[] rows = ps.executeBatch();

			count = rows.length;
			
			connection.commit();
		} catch(Exception e) {
			DbUtil.rollback(connection);
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return count;
	}
}
