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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.base.db.ConnectionFactory2;
import aptg.dao.base.db.DbUtil;

public class BaseDao2 {

	protected static final Logger logger = LogManager.getFormatterLogger(BaseDao2.class.getName());
	
	protected Connection initConnection() throws SQLException {
//		logger.error("It's TEST ~~~ ");
		Connection connection = ConnectionFactory2.getInstance().getConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	protected PreparedStatement initPreparedStatement(Connection connection, String sql) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		return ps;
	}
	
	protected List<DynaBean> executeQuery(String sql) throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = initConnection();
			ps = connection.prepareStatement(sql);
			
			//ps.setString(1, cid);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows();
			
			connection.commit();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close2(connection);
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
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		int idValue = 0;
		try {
			connection = initConnection();
//			connection.setAutoCommit(false);
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
			logger.error("Exception: "+e.getMessage() +", (sql: "+sql+")");
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close2(connection);
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
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		int count = -1;
		try {
			connection = initConnection();
//			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			count = ps.getUpdateCount();
			
			connection.commit();
			
		} catch(Exception e) {
			DbUtil.rollback(connection);
			logger.error("Exception: "+e.getMessage() +", (sql: "+sql+")");
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close2(connection);
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
	protected List<Integer> batchInsert(Connection connection, PreparedStatement ps, List<String> sqlList) {
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
			for (String sql: sqlList) {
				logger.error("Exception: "+e.getMessage() +", (sql: "+sql+")");
			}
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close2(connection);
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
	protected int batchUpdate(Connection connection, PreparedStatement ps, List<String> sqlList) {
		int count = 0;
		ResultSet rs = null;
		try {
			ps.executeBatch();

			count = ps.getUpdateCount();
			
			connection.commit();
		} catch(Exception e) {
			count = -1;
			DbUtil.rollback(connection);
			for (String sql: sqlList) {
				logger.error("Exception: "+e.getMessage() +", (sql: "+sql+")");
			}
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close2(connection);
		}
		return count;
	}

	/**
	 * 	批次更新多筆 不同的SQL
	 * 
	 * @param connection
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	protected int batchUpdate(Connection connection, List<PreparedStatement> psList, List<String> sqlList) {
		int count = 0;
		ResultSet rs = null;
		try {
			for (PreparedStatement ps: psList) {
				ps.executeBatch();
				count += ps.getUpdateCount();
			}
			
			connection.commit();
		} catch(Exception e) {
			count = -1;
			DbUtil.rollback(connection);
			for (String sql: sqlList) {
				logger.error("Exception: "+e.getMessage() +" (sql: "+sql+")");
			}
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(psList);
			DbUtil.close2(connection);
		}
		return count;
	}

	/**
	 * 	批次更新多筆 不同的SQL
	 * ( for insert PowerRecord, PowerRecordCreateTime )
	 * 
	 * @param connection
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	protected String batchUpdateReturnMessage(Connection connection, List<PreparedStatement> psList, List<String> sqlList) {
		String result = null;
		
		ResultSet rs = null;
		try {
			for (PreparedStatement ps: psList) {
				ps.executeBatch();
			}
			
			connection.commit();
			
		} catch(Exception e) {
			DbUtil.rollback(connection);
			result = e.getMessage();
			logger.error("========================== Exception: "+result);
			for (String sql: sqlList) {
				logger.error("Exception not execute sql: "+sql);
			}
			logger.error("========================== Exception End ========================== ");
			
		} finally {
			DbUtil.close(rs);
			DbUtil.close(psList);
			DbUtil.close2(connection);
		}
		return result;
	}
}
