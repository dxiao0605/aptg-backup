/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2018
 *
 * 在此設定DB連線，關閉資料庫連線則在DbUtil.java
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20180222    Gary Chang          Initial
 */


package tw.com.aptg.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * 
 *  Log4j only get the same data source,so u must impl Singleton pattern. 
 * 
 */
public class ConnectionFactoryForlog4j2 {
	
	private static interface Singleton {
		final ConnectionFactoryForlog4j2 INSTANCE = new ConnectionFactoryForlog4j2();
	}

	private String DB_USER = "";
	private String DB_PASSWORD = "";
	private String DB_URL = "";
	private boolean isMySql = false;
	private boolean isOracle = false;
	
	private final DataSource dataSource;

	/*
	 * 
	 * use lib:
	 * commons-dbcp-1.4.jar (JDK 6 only, JDBC 4)
	 * commons-pool-1.6.jar (JDK 5)
	 * 
	 */
	private ConnectionFactoryForlog4j2() {
		//init
		ResourceBundle dbRb = ResourceBundle.getBundle("db");
		String dbType = dbRb.getString("db.type");
		if (dbType.equals("mysql")) {
			this.isMySql = true;
		} else if (dbType.equals("oracle")) {
			this.isOracle = true;
		}
		String driverClass = dbRb.getString(dbType + ".jdbc.driver.class");
		this.DB_USER = dbRb.getString(dbType + ".db.user");
		this.DB_PASSWORD = dbRb.getString(dbType + ".db.password");
		this.DB_URL = dbRb.getString(dbType + ".db.url");
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Properties properties = new Properties();
		properties.setProperty("user", this.DB_USER);
		properties.setProperty("password", this.DB_PASSWORD);
		
		//PoolableConnectionFactory所需要的參數
		String validationQuery = "";
		if (this.isMySql) {
			validationQuery = "select 1";
		} else if (this.isOracle) {
			validationQuery = "select 1 from DUAL";
		}
		int validationQueryTimeout = 3;
		boolean defaultReadOnly = false;
		boolean defaultAutoCommit = false;
		
		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				this.DB_URL, properties);
		new PoolableConnectionFactory(connectionFactory, pool, null,
				validationQuery, validationQueryTimeout, defaultReadOnly, defaultAutoCommit,
				Connection.TRANSACTION_READ_COMMITTED);

		this.dataSource = new PoolingDataSource(pool);
	}
	
	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}
