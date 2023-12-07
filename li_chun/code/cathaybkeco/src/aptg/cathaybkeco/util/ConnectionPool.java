package aptg.cathaybkeco.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool {

	private int maxPoolSize = 5;
	private int connNum = 0;
	private boolean isTomcat = false;
	private String jndiName = "";

	private static final String SQL_VERIFYCONN = "select 1";

	Stack<Connection> freePool = new Stack<>();
	Set<Connection> occupiedPool = new HashSet<>();

	/**
	 * Constructor
	 * 
	 * @param databaseUrl The connection url
	 * @param userName    user name
	 * @param password    password
	 * @param maxSize     max size of the connection pool
	 */
	public ConnectionPool() {
		ResourceBundle dbRb = ResourceBundle.getBundle("db");
		String apType = dbRb.getString("ap.type");
		if (apType.equals("tomcat")) {
			this.isTomcat = true;
			this.jndiName = dbRb.getString("jndi.name.tomcat");
		} else if (apType.equals("jboss")) {
			this.jndiName = dbRb.getString("jndi.name.jboss");
		}
		
		this.maxPoolSize = Integer.valueOf(dbRb.getString("db.maxconnection"));
		initializeConnectionPool();
	}
	
	private void initializeConnectionPool(){
		try {
			while(connNum<maxPoolSize){	
				createNewConnectionForPool();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DataSource getDataSource() throws NamingException {
		Context initContext;
		DataSource dataSource = null;
		try {
			initContext = new InitialContext();
			// Tomcat
			if (this.isTomcat) {
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				dataSource = (DataSource) envContext.lookup(this.jndiName);
				// JBoss
			} else {
				dataSource = (DataSource) initContext.lookup(this.jndiName);
			}
		} catch (NamingException e) {
			throw new NamingException("ERROR: Unable to lookup to Datasource." + e);
		}
		return dataSource;
	}

	/**
	 * Get an available connection
	 * 
	 * @return An available connection
	 * @throws SQLException Fail to get an available connection
	 */
	public synchronized Connection getConnection() throws Exception{
		Connection conn = null;

		if (isFull()) {
			throw new SQLException("The connection pool is full.");
		}

		conn = getConnectionFromPool();
		
		
		// if there is no action on one connection for some time,
        // the connection is lost. By this, make sure the connection is
        // active. Otherwise reconnect it.
		conn = makeAvailable(conn);

		return conn;
	}

	/**
	 * Return a connection to the pool
	 * 
	 * @param conn The connection
	 * @throws SQLException When the connection is returned already or it isn't
	 *                      gotten from the pool.
	 */
	public synchronized void returnConnection(Connection conn) throws SQLException {
		if (conn == null) {
			throw new NullPointerException();
		}
		if (!occupiedPool.remove(conn)) {
			throw new SQLException("The connection is returned already or it isn't for this pool");
		}
		freePool.push(conn);
	}

	/**
	 * Verify if the connection is full.
	 * 
	 * @return if the connection is full
	 */
	private synchronized boolean isFull() {
		return ((freePool.size() == 0) && (connNum > maxPoolSize));
	}

	/**
	 * Create a connection for the pool
	 * 
	 * @return the new created connection
	 * @throws SQLException When fail to create a new connection.
	 */
	private Connection createNewConnectionForPool() throws Exception {
		Connection conn = createNewConnection();
		connNum++;
		freePool.add(conn);
		return conn;
	}

	/**
	 * Crate a new connection
	 * 
	 * @return the new created connection
	 * @throws SQLException    When fail to create a new connection.
	 * @throws NamingException
	 */
	private Connection createNewConnection() throws Exception {
		Connection conn = null;
		try {
			DataSource dataSource = getDataSource();
			conn = dataSource.getConnection();
		} catch (Exception e) {
			throw new Exception("ERROR: Unable to Connect to Database.", e);
		}
		return conn;
	}

	/**
	 * Get a connection from the pool. If there is no free connection, create a new connection.
	 * 
	 * @return the connection.
	 * @throws Exception 
	 */
	private Connection getConnectionFromPool() throws Exception {
		Connection conn = null;
		if (freePool.size() > 0) {
			conn = freePool.pop();
			occupiedPool.add(conn);
		}else {
			conn = createNewConnectionForPool();
		}
		return conn;
	}
	
	/**
     * Make sure the connection is available now. Otherwise, reconnect it.
     * 
     * @param conn
     *            The connection for verification.
     * @return the available connection.
     * @throws SQLException
     *             Fail to get an available connection
     */
    private Connection makeAvailable(Connection conn) throws Exception {
        if (isConnectionAvailable(conn)) {
            return conn;
        }

        // If the connection is't available, reconnect it.
        occupiedPool.remove(conn);
        connNum--;
        conn.close();

        conn = createNewConnection();
        occupiedPool.add(conn);
        connNum++;
        return conn;
    }

    /**
     * By running a sql to verify if the connection is available
     * 
     * @param conn
     *            The connection for verification
     * @return if the connection is available for now.
     * @throws SQLException 
     */
    private boolean isConnectionAvailable(Connection conn) throws SQLException {
    	Statement st = null;
        try {
        	st = conn.createStatement();
            st.executeQuery(SQL_VERIFYCONN);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
        	if(st!=null)
        		st.close();
        }
    }
}