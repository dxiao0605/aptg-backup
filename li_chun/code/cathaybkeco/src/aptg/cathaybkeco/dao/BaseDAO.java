package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import aptg.cathaybkeco.util.JdbcUtil;


public class BaseDAO {
	

	/**
	 * 查詢
	 * 
	 * @param sql
	 * @param parameterList
	 * @return List<DynaBean>
	 * @throws SQLException
	 */
	protected List<DynaBean> executeQuery(String sql, List<String> parameterList) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = JdbcUtil.getConnection();
			ps = connection.prepareStatement(sql.toString());
			if (parameterList != null && parameterList.size() > 0) {
				for (int i = 0; i < parameterList.size(); i++) {
					ps.setString(i + 1, parameterList.get(i));
				}
			}
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows();
		} catch (SQLException e) {
			throw new SQLException(e.toString());
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
		return rows;
	}

	/**
	 * 新刪改
	 * 
	 * @param sql
	 * @param parameterList
	 * @throws SQLException
	 */
	protected void executeUpdate(String sql, List<String> parameterList) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			ps = connection.prepareStatement(sql.toString());
			if (parameterList != null && parameterList.size() > 0) {
				for (int i = 0; i < parameterList.size(); i++) {
					ps.setString(i + 1, parameterList.get(i));
				}
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e.toString());
		} finally {
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}

	/**
	 * 新刪改批量
	 * @param sqlMap
	 * @throws SQLException
	 */
	protected void executeUpdateBatch(Map<String, List<String>> sqlMap) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			for (Map.Entry<String, List<String>> entry : sqlMap.entrySet()) {
				ps = connection.prepareStatement(entry.getKey());

				List<String> parameterList = entry.getValue();
				if (parameterList != null && parameterList.size() > 0) {
					for (int i = 0; i < parameterList.size(); i++) {
						ps.setString(i + 1, parameterList.get(i));
					}
				}
				ps.executeUpdate();
			}

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
