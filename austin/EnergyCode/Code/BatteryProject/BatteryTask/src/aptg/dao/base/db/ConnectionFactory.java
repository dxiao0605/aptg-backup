package aptg.dao.base.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory {
	
	private static final Logger logger = LogManager.getFormatterLogger(ConnectionFactory.class.getSimpleName());
	
	private static final ConnectionFactory instance = new ConnectionFactory();
	private String mode = "";
	private String url = "";
	private String driverClassName = "";
	private String username = "";
	private String password = "";
	
	private ConnectionFactory() {
		ResourceBundle dbConfig = ResourceBundle.getBundle("db");
		this.mode = dbConfig.getString("db.mode");
		
		this.url = dbConfig.getString(this.mode + ".db.url");
		this.driverClassName = dbConfig.getString(this.mode + ".db.driverClassName");
		this.username = dbConfig.getString(this.mode + ".db.username");
		this.password = dbConfig.getString(this.mode + ".db.password");
	}

	private Connection createConnection() {
		Connection connection = null;
		try {
			// STEP 1: Register JDBC driver
			Class.forName(this.driverClassName);
			
			// STEP 2: 
			connection = DriverManager.getConnection(this.url, this.username, this.password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	//AP server 設定JNDI
	public static Connection getConnection() {
		return instance.createConnection();
	}
	
//	/**
//	 * 	啟動時設定db執行模式
//	 * default: db.properties => db.mode=xxxx
//	 * 
//	 * @param args: prod/poc/local
//	 */
//	public static void setMode(String[] args) {
//		if (args.length!=0) {
//			String mode = args[0];
//			if (mode.equals("prod") || mode.equals("poc") || mode.equals("local")) {
//				instance.mode = mode;
//
//				ResourceBundle dbConfig = ResourceBundle.getBundle("db");
//				instance.url = dbConfig.getString(mode + ".db.url");
//				instance.driverClassName = dbConfig.getString(mode + ".db.driverClassName");
//				instance.username = dbConfig.getString(mode + ".db.username");
//				instance.password = dbConfig.getString(mode + ".db.password");
//
//				logger.info("assign execute mode when startup, DB mode: "+instance.mode);
//			}
//		}
//	}
}
