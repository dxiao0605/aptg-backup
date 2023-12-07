package tw.com.aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import tw.com.aptg.beans.SmsDraftBean;
import tw.com.aptg.db.ConnectionFactory;
import tw.com.aptg.db.DbUtil;


public class SmsDraftDao {

	private Connection connection;
	private PreparedStatement ps;
	
	
	public int insert(SmsDraftBean smsDraftBean) throws SQLException {
		int updateCounts = 0;
		String sql="insert into sms_draft(msisdn,contract,msg_content,status,p_id) " +
			       "values (?,?,?,?,?)";		
		try{
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, smsDraftBean.getMsisdn());
			ps.setString(2, smsDraftBean.getContract());
			ps.setString(3, smsDraftBean.getMsgContent());
			ps.setString(4, "I");
			ps.setString(5, smsDraftBean.getPid());
			
			updateCounts = ps.executeUpdate();
		}
		finally {
            DbUtil.close(ps);
            DbUtil.close(connection);
        }
		return updateCounts;
	}
	
	public int delete_draft(String sId,String cid) throws SQLException {
		String updateSql = "update sms_draft set status ='D' where s_id=? and contract=? ";
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
	
	public int update_draft(String sId,String c_txt,String cid) throws SQLException {
		String updateSql = "update sms_draft set msg_content =? where s_id=? and contract=?";
		ResultSet rs = null;
		int rows;
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(updateSql);
			ps.setString(1, c_txt);
			ps.setInt(2,Integer.valueOf(sId));
			ps.setString(3, cid);
			rows =ps.executeUpdate();
		}
		finally {
			DbUtil.close(rs);
			DbUtil.close(ps);
			DbUtil.close(connection);
		}
		
		return rows;
		
	}
	
	
	public List<DynaBean> getdraftbox(String c_id,String id) throws SQLException {
		String sql = "SELECT s_id,c_date,msg_content FROM sms_draft where contract=? and p_id=? and status <> 'D' order by c_date desc";
		ResultSet rs = null;
		List<DynaBean> rows = new ArrayList<DynaBean>();
		try {
			connection = ConnectionFactory.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, c_id);
			ps.setString(2, id);
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
