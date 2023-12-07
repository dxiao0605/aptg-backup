/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2014
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20141111    Gary Chang          Initial
 */

package aptg.dao.base.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DbUtil {

	public static void close2(Connection connection) {
		if (connection != null) {
			try {
				ConnectionFactory2.getInstance().closeConnection(connection);
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
				statement = null;
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}
	public static void close(List<PreparedStatement> statements) {
		if (statements.size()!=0) {
			try {
				for (Statement statement: statements) {
					statement.close();
					statement = null;	
				}
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
	
	public static void rollback(Connection connection) {
		 // 發生錯誤，撤消所有變更
	    if(connection != null) {
	        try {
	            connection.rollback();
	        } catch(SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
}
