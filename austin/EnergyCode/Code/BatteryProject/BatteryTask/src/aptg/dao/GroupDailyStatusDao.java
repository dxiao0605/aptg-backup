package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.model.GroupDailyStatusModel;

public class GroupDailyStatusDao extends BaseDao2 {

	public List<DynaBean> queryGroupDailyStatus(String recDate) throws SQLException {
//		String sql = "Select * from GroupDailyStatus where RecDate='"+recDate+"'";
		String sql = "Select * from GroupDailyStatus where RecDate=str_to_date('"+recDate+"','%Y-%m-%d')";
		return executeQuery(sql);
	}
	
	public List<Integer> insertGroupDailyStatus(List<GroupDailyStatusModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO GroupDailyStatus ( "
				   + " GroupInternalID, RecDate, TimeZone, Status "
				   + ") "
				   + "VALUES "
				   + "(?, ?, ?, ?)";
		
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (GroupDailyStatusModel gd: list) {
			ps.setInt(1, gd.getGroupInternalID());
			ps.setString(2, gd.getRecDate());
			ps.setInt(3, gd.getTimeZone());
			ps.setInt(4, gd.getStatus());
			
			ps.addBatch();
			sqlList.add(ps.toString());
//			System.out.println("######### sql: "+ps.toString());
		}
	
		List<Integer> ids = batchInsert(connection, ps, sqlList);
//		System.out.println("ids: "+JsonUtil.getInstance().convertObjectToJsonstring(ids));
		return ids;
	}
	
	public int updateGroupDailyStatus(List<GroupDailyStatusModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();

		String sql = "UPDATE GroupDailyStatus SET Status=? where GroupInternalID=? and RecDate=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (GroupDailyStatusModel gd: list) {
			ps.setInt(1, gd.getStatus());
			ps.setInt(2, gd.getGroupInternalID());
			ps.setString(3, gd.getRecDate());
			
			ps.addBatch();
			sqlList.add(ps.toString());
		}
		
		return batchUpdate(connection, ps, sqlList);
	}
	
	public int updateGroupDaily(int groupID, String recDate, int status) throws SQLException {
//		String sql = "UPDATE GroupDailyStatus SET Status="+status+" where GroupInternalID="+groupID+" and RecDate='"+recDate+"'";
		String sql = "UPDATE GroupDailyStatus SET Status="+status+" where GroupInternalID="+groupID+" and RecDate=str_to_date('"+recDate+"','%Y-%m-%d')";
		return executeUpdate(sql);
	}
}
