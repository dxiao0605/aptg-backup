package aptg.cathaybkeco.util;

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
//	private boolean isTomcat = false;
//	private String jndiName = "";
//	private int maxPoolSize = 5;
	private static ConnectionPool pool = new ConnectionPool();
	
	//constructor
	private JdbcUtil() {
//		ResourceBundle dbRb = ResourceBundle.getBundle("db");
//		String apType = dbRb.getString("ap.type");
//		if (apType.equals("tomcat")){
//			this.isTomcat = true;
//			this.jndiName =  dbRb.getString("jndi.name.tomcat");
//		} else if (apType.equals("jboss")){
//			this.jndiName =  dbRb.getString("jndi.name.jboss");
//		}
//		this.maxPoolSize = Integer.valueOf(dbRb.getString("db.maxconnection"));
	}
		
//	private DataSource getDataSource() {
//        Context initContext;
//        DataSource dataSource = null;
//		try {
//			initContext = new InitialContext();
//			//Tomcat
//			if (this.isTomcat){
//				Context envContext  = (Context)initContext.lookup("java:/comp/env");
//		        dataSource = (DataSource)envContext.lookup(this.jndiName);				
//		    //JBoss
//			} else {
//		        dataSource = (DataSource)initContext.lookup(this.jndiName); 	  	
//			}      
//		} catch (NamingException e) {
//			logger.error("ERROR: Unable to lookup to Datasource." , e);
//		}
//		return dataSource;
//	}
			
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
