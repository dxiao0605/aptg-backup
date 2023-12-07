package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class BatteryGroupDao extends BaseDao2 {

	public List<DynaBean> queryBatteryGroup() throws SQLException {
		String sql = "SELECT * FROM BatteryGroup";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryBatteryGroupNBList() throws SQLException {
		String sql = "SELECT t1.SeqNo, t1.CompanyCode, t2.* FROM BatteryGroup t1 inner join NBList t2 " + 
					 "on t1.SeqNo = t2.GroupInternalID and t2.Active=13;";
		return executeQuery(sql);
	}

	public List<DynaBean> queryBatteryGroupNBGroupHis(String time) throws SQLException {
//		String sql = "SELECT t1.SeqNo, t1.CompanyCode, t2.* FROM BatteryGroup t1 inner join NBGroupHis t2 " + 
//					 "on t1.SeqNo = t2.GroupInternalID and (t2.StartTime<='"+time+"' and t2.EndTime>'"+time+"');";
		
//		String sql = "SELECT t1.SeqNo, t1.CompanyCode, t2.* FROM BatteryGroup t1 inner join NBGroupHis t2 " + 
//				 "on t1.SeqNo = t2.GroupInternalID and (t2.StartTime<=str_to_date('"+time+"','%Y-%m-%d %H:%i:%s') and t2.EndTime>str_to_date('"+time+"','%Y-%m-%d %H:%i:%s'));";
		
		String sql = "SELECT t1.SeqNo, t1.CompanyCode, t2.* FROM BatteryGroup t1, NBGroupHis t2, NBList nb " + 
				 	 "where t1.SeqNo = t2.GroupInternalID and (t2.StartTime<=str_to_date('"+time+"','%Y-%m-%d %H:%i:%s') and t2.EndTime>str_to_date('"+time+"','%Y-%m-%d %H:%i:%s')) " + 
				 	 " and nb.Active=13 and nb.NBID=t2.NBID";
		
		return executeQuery(sql);
	}
}
