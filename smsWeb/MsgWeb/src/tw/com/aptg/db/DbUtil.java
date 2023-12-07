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

package tw.com.aptg.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
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
}
