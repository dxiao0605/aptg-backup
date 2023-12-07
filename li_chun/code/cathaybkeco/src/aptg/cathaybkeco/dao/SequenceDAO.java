package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import aptg.cathaybkeco.util.JdbcUtil;


public class SequenceDAO extends BaseDAO{
	
	/**
	 * 取流水號
	 * @param name
	 * @return int
	 * @throws SQLException
	 */
	public int getNextval(String name) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;						
		ResultSet rs = null;
		int sequence= 0;
		try {
			connection = JdbcUtil.getConnection();
			
			String sql = " select nextval(?) ";
			ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				sequence = rs.getInt(1);
			}
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
		return sequence;
	}

}
