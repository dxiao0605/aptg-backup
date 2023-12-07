package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class SysConfigDao extends BaseDao2 {

	public List<DynaBean> querySysConfig() throws SQLException {
		String sql = "SELECT * FROM SysConfig";
		return executeQuery(sql);
	}
	public List<DynaBean> querySysConfig(String paramname) throws SQLException {
		String sql = "SELECT * FROM SysConfig WHERE paramname='"+paramname+"'";
		return executeQuery(sql);
	}
	
	public int updateEco5OffLineRecordSeq() throws SQLException {
		String sql = "UPDATE SysConfig SET paramvalue=(Select max(seqno) from PowerRecord) WHERE paramname='Eco5OffLineRecordSeq';";
		return executeUpdate(sql);
	}
	
	public int updateJobIsRunningFlag(int flag) throws SQLException {
		String sql = "UPDATE SysConfig SET paramvalue='"+flag +"' where paramname='DailyJobIsRunning';";
		return executeUpdate(sql);
	}
	
	public int updateBGRepriceTaskRunningFlag(int flag) throws SQLException {
		String sql = "UPDATE SysConfig SET paramvalue='"+flag +"' where paramname='BGRepriceTaskIsRunning';";
		return executeUpdate(sql);
	}
}
