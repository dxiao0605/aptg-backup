package tw.com.aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import tw.com.aptg.db.ConnectionFactory;
import tw.com.aptg.db.DbUtil;

public class SmsHistoryDao {
	
	private Connection connection;
	private PreparedStatement ps;
	
	public List<DynaBean> getHistory(String c_id) throws SQLException {
		String sql = "select logrid ,contractid,msisdn,toaddr,senderaddr,msginfo,logtime from msgweb.sms_history where contractid =? order by logtime";
		ResultSet rs = null;
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, c_id);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		return rows;
	}
	

}
