package aptg.battery.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcUtil {
	//static reference to itself
	private static final JdbcUtil instance = new JdbcUtil();
	private static final Logger logger = LogManager.getLogger(JdbcUtil.class.getName());
	private static ConnectionPool pool = new ConnectionPool();
	
	//constructor
	private JdbcUtil() {

	}
			
	private Connection createConnection() {
		Connection connection = null;
		try {			
			connection = pool.getConnection();			
		} catch (Exception e) {
			logger.error("ERROR: Unable to Connect to Database.", e);
		}
		return connection;
	}
	
	public static Connection getConnection() {	
		return instance.createConnection();
	}
	
	public static void close(Connection connection) {	
		if (connection != null) {
			try {
				pool.returnConnection(connection);
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	
}
