package tw.com.aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aptg.beans.SmsReqBean;
import tw.com.aptg.db.ConnectionFactory;
import tw.com.aptg.db.DbUtil;


public class SmsReqDao {

	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(SmsReqDao.class.getName());  
	
	private Connection connection;
	private PreparedStatement ps;
	
	
	public int insert(SmsReqBean smsReqBean) throws SQLException {
		int updateCounts = 0;
		String sql="insert into sms_req(msisdn_lst,msg_content,time_spec,contract,status,sender,sender_id) " +
			       "values (?,?,?,?,?,?,?)";		
		try{
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, smsReqBean.getMsisdnLst());
			ps.setString(2, smsReqBean.getMsgContent());
			ps.setString(3, smsReqBean.getTimeSpec());
			ps.setString(4, smsReqBean.getContract());
			ps.setString(5, smsReqBean.getStatus());
			ps.setString(6, smsReqBean.getSender());
			ps.setString(7, smsReqBean.getSender_id());
			
			updateCounts = ps.executeUpdate();
		}
		finally {
            DbUtil.close(ps);
            DbUtil.close(connection);
        }
		return updateCounts;
	}
	
	public int delete(String sId,String cid) throws SQLException {
		String updateSql = "update sms_req set status ='D' where s_id=? and contract=?";
		ResultSet rs = null;
		int rows;
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(updateSql);
			ps.setInt(1, Integer.valueOf(sId));
			ps.setString(2, cid);
			rows =ps.executeUpdate();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		
		return rows;
	}
	
	
	
	public List<DynaBean> getinbox(String cid,String id) throws SQLException {
		
		logger.info("Db Query Start" + cid + ",personal id=" + id);
		
		String sql = "SELECT s_id,c_date,msisdn_lst,msg_content,time_spec FROM sms_req where contract=?  and sender_id =? and status <> 'D' order by c_date desc";
		ResultSet rs = null;
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, cid);
			ps.setString(2, id);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
			logger.info("Db Query Stop" );
		}
		return rows;
	}
	
	
	
	
}
