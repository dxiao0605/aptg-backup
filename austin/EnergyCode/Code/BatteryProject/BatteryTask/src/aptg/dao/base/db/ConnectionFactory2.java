package aptg.dao.base.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory2 {
	
	private static final Logger logger = LogManager.getFormatterLogger(ConnectionFactory2.class.getName());
	
	private String mode = "";
	private String url = "";
	private String driverClassName = "";
	private String username = "";
	private String password = "";

    private int max; // 連接池中最大Connection數目
    private List<Connection> connections;

	private static ConnectionFactory2 instances;
	public static ConnectionFactory2 getInstance() {
		if (instances==null) {
			instances = new ConnectionFactory2();
		}
		return instances;
	}
	
    public ConnectionFactory2() {
//		ResourceBundle dbConfig = ResourceBundle.getBundle("db");
//		this.mode = dbConfig.getString("db.mode");
//
//		this.max = Integer.valueOf(dbConfig.getString("db.connectionPool"));
//		this.url = dbConfig.getString(this.mode + ".db.url");
//		this.driverClassName = dbConfig.getString(this.mode + ".db.driverClassName");
//		this.username = dbConfig.getString(this.mode + ".db.username");
//		this.password = dbConfig.getString(this.mode + ".db.password");
//		
//		try {
//			Class.forName(this.driverClassName);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		try {
			ResourceBundle dbConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get("db.properties")));
			this.mode = dbConfig.getString("db.mode");

			this.max = Integer.valueOf(dbConfig.getString("db.connectionPool"));
			this.url = dbConfig.getString(this.mode + ".db.url");
			this.driverClassName = dbConfig.getString(this.mode + ".db.driverClassName");
			this.username = dbConfig.getString(this.mode + ".db.username");
			this.password = dbConfig.getString(this.mode + ".db.password");

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Class.forName(this.driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		connections = new ArrayList<Connection>();
	}
	
    public synchronized Connection getConnection() throws SQLException {
		if (connections.size() == 0) {
			return DriverManager.getConnection(this.url, this.username, this.password);
		} else {
            int lastIndex = connections.size() - 1;
            return connections.remove(lastIndex);
		}
	}
	
    public synchronized void closeConnection(Connection conn) throws SQLException {
        if(connections.size() == max) {
            conn.close();
        } else {
            connections.add(conn);
        }
    }
}
