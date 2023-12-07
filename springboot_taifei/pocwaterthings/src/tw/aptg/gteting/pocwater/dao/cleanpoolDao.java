package tw.aptg.gteting.pocwater.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import tw.aptg.gteting.pocwater.db.ConnectionFactory;
import tw.aptg.gteting.pocwater.db.DbUtil;

public class cleanpoolDao {
	
	
	private Connection connection;
	private PreparedStatement ps;
	
	public int insert(String updata) throws SQLException {
		int updateCounts = 0;
		String sql="insert into chean_pool(updata) " +
			       "values (?)";		
		try{
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, updata);
			updateCounts = ps.executeUpdate();
		}
		finally {
            DbUtil.close(ps);
            DbUtil.close(connection);
        }
		return updateCounts;
	}

}
