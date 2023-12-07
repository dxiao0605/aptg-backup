package aptg.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.utils.ToolUtil;

public class CompanyDao extends BaseDao2 {

	/*
	 * for DailyStatusTask_GroupHis
	 */
	public List<DynaBean> queryCompanyDisconnectGroupHis() throws SQLException {
		String date = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		
//		String sql = "select t1.NBID, t1.StartTime, t1.EndTime, t3.CompanyCode, t3.Disconnect, t3.IMPType, t2.DefaultGroup from ( " + 
//					 "		SELECT NBID, GroupInternalID, StartTime, EndTime FROM NBGroupHis where StartTime<='"+date+"' and EndTime>'"+date+"' " + 
//					 ") t1, BatteryGroup t2, Company t3 " + 
//					 "where t1.GroupInternalID = t2.SeqNo and t2.CompanyCode=t3.CompanyCode";

		String sql = "select t1.NBID, t1.StartTime, t1.EndTime, t3.CompanyCode, t3.Disconnect, t3.IMPType, t2.DefaultGroup from ( " + 
					 "		SELECT NBID, GroupInternalID, StartTime, EndTime FROM NBGroupHis where StartTime<=str_to_date('"+date+"','%Y-%m-%d %H:%i:%s') and EndTime>str_to_date('"+date+"','%Y-%m-%d %H:%i:%s') " + 
					 ") t1, BatteryGroup t2, Company t3 " + 
					 "where t1.GroupInternalID = t2.SeqNo and t2.CompanyCode=t3.CompanyCode";
		
		return executeQuery(sql);
	}
	
	/*
	 * for DailyStatusTask
	 */
	public List<DynaBean> queryCompanyDisconnect() throws SQLException {
		String date = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		
//		String sql = "SELECT d.NBID, c.CompanyCode, c.Disconnect, c.IMPType FROM Company c inner join (  " + 
//					 "	SELECT NBID, CompanyCode FROM NBAllocationHis where StartTime<='"+date+"' and EndTime>'"+date+"'" + 
//					 " ) d " + 
//					 " on c.CompanyCode = d.CompanyCode;";

		String sql = "SELECT d.NBID, c.CompanyCode, c.Disconnect, c.IMPType FROM Company c inner join (  " + 
					 "	SELECT NBID, CompanyCode FROM NBAllocationHis where StartTime<=str_to_date('"+date+"','%Y-%m-%d %H:%i:%s') and EndTime>str_to_date('"+date+"','%Y-%m-%d %H:%i:%s') " + 
					 " ) d " + 
					 " on c.CompanyCode = d.CompanyCode;";
		
		return executeQuery(sql);
	}
}
