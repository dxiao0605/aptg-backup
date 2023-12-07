package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class BatteryGroupDao extends BaseDao2 {

	public List<DynaBean> queryBatteryGroup(String nbID) throws SQLException {
		String sql = "SELECT * FROM BatteryGroup WHERE SeqNo = (SELECT GroupInternalID FROM NBList WHERE NBID = '"+nbID+"')";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryAllBatteryGroupNBListActive() throws SQLException {
		String sql = "SELECT * FROM BatteryGroup t1 inner join NBList t2 " +
					 "on t1.SeqNo=t2.GroupInternalID and t2.Active=13";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryBatteryGroupNBListActiveByNBID(String nbID) throws SQLException {
		String sql = "SELECT * FROM BatteryGroup t1 inner join NBList t2 " +
					 "on t2.NBID='"+nbID+"' and t1.SeqNo=t2.GroupInternalID and t2.Active=13";
		return executeQuery(sql);
	}

	public List<DynaBean> queryBatteryGroupNBListByNBID(String nbID) throws SQLException {
		String sql = "SELECT * FROM BatteryGroup t1 inner join NBList t2 " +
					 "on t2.NBID='"+nbID+"' and t1.SeqNo=t2.GroupInternalID";
		return executeQuery(sql);
	}
}
