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
 * 01        20180105    Gary Chang          Initial
 */


package tw.com.aptg.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory {
	//static reference to itself
	private static final ConnectionFactory instance = new ConnectionFactory();
	private static final Logger logger = LogManager.getLogger(ConnectionFactory.class.getName());
	private boolean isTomcat = false;
	private String jndiName = "";
	
	//constructor
	private ConnectionFactory() {
		ResourceBundle dbRb = ResourceBundle.getBundle("db");
		String apType = dbRb.getString("ap.type");
		if (apType.equals("tomcat")){
			this.isTomcat = true;
			this.jndiName =  dbRb.getString("jndi.name.tomcat");
		} else if (apType.equals("jboss")){
			this.jndiName =  dbRb.getString("jndi.name.jboss");
		}	
	}
		
	private DataSource getDataSource() {
        Context initContext;
        DataSource dataSource = null;
		try {
			initContext = new InitialContext();
			//Tomcat
			if (this.isTomcat){
				Context envContext  = (Context)initContext.lookup("java:/comp/env");
		        dataSource = (DataSource)envContext.lookup(this.jndiName);				
		    //JBoss
			} else {
		        dataSource = (DataSource)initContext.lookup(this.jndiName); 	  	
			}      
		} catch (NamingException e) {
			logger.error("ERROR: Unable to lookup to Datasource." , e);
		}
		return dataSource;
	}
			
	private Connection createConnection() {
		Connection connection = null;
		try {
			DataSource dataSource = getDataSource();	
			connection = dataSource.getConnection();			
		} catch (SQLException e) {
			logger.error("ERROR: Unable to Connect to Database.", e);
		}
		return connection;
	}
	
	//AP server 設定JNDI
	public static Connection getConnection() {
		return instance.createConnection();
	}
		
	@SuppressWarnings("resource")
	public static void showDatabaseMetaData() {
		Connection connection = instance.createConnection();
	    ResultSet rs = null;
		try {
			DatabaseMetaData md = connection.getMetaData();
			System.out.println("getURL() - " + md.getURL());
			System.out.println("getUserName() - " + md.getUserName());
			System.out.println("getDatabaseProductVersion - " + md.getDatabaseProductVersion());
			System.out.println("getDriverMajorVersion - "+ md.getDriverMajorVersion());
			System.out.println("getDriverMinorVersion - "+ md.getDriverMinorVersion());
			System.out.println("nullAreSortedHigh - " + md.nullsAreSortedHigh());
			System.out.println("<H1>Feature Support</H1>");
			System.out.println("supportsAlterTableWithDropColumn - " + md.supportsAlterTableWithDropColumn() + "<BR>");
			System.out.println("supportsBatchUpdates - "+ md.supportsBatchUpdates());
			System.out.println("supportsTableCorrelationNames - "+ md.supportsTableCorrelationNames());
			System.out.println("supportsPositionedDelete - " + md.supportsPositionedDelete());
			System.out.println("supportsFullOuterJoins - " + md.supportsFullOuterJoins());
			System.out.println("supportsStoredProcedures - " + md.supportsStoredProcedures());
			System.out.println("supportsMixedCaseQuotedIdentifiers - " + md.supportsMixedCaseQuotedIdentifiers());
			System.out.println("supportsANSI92EntryLevelSQL - " + md.supportsANSI92EntryLevelSQL());
			System.out.println("supportsCoreSQLGrammar - " + md.supportsCoreSQLGrammar());
			System.out.println("getMaxRowSize - " + md.getMaxRowSize());
			System.out.println("getMaxStatementLength - " + md.getMaxStatementLength());
			System.out.println("getMaxTablesInSelect - " + md.getMaxTablesInSelect());
			System.out.println("getMaxConnections - " + md.getMaxConnections());
			System.out.println("getMaxCharLiteralLength - " + md.getMaxCharLiteralLength());
			System.out.println("getTableTypes()");
			rs = md.getTableTypes();
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
			System.out.println("getTables()");
			rs = md.getTables("accounts", "", "%", new String[0]);
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}
			System.out.println("Transaction Support");
			System.out.println("getDefaultTransactionIsolation() - " + md.getDefaultTransactionIsolation());
			System.out.println("dataDefinitionIgnoredInTransactions() - " + md.dataDefinitionIgnoredInTransactions());
			System.out.println("General Source Information");
			System.out.println("getMaxTablesInSelect - " + md.getMaxTablesInSelect());
			System.out.println("getMaxColumnsInTable - " + md.getMaxColumnsInTable());
			System.out.println("getTimeDateFunctions - " + md.getTimeDateFunctions());
			System.out.println("supportsCoreSQLGrammar - " + md.supportsCoreSQLGrammar());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(connection);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		showDatabaseMetaData();
	}
	
	
}
