package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class BankInfDao extends BaseDao2 {

	public List<DynaBean> queryBankKIPinfo(String useMonth) throws SQLException {
//		String sql = "SELECT SUM(Area) Area, SUM(People) People FROM BankInf";
		
//		String sql = "select sum(b.Area) Area,sum(b.People) People from BankInf b " + 
//					 "where " + 
//					 "exists (select 1 from FcstCharge f,PowerAccount p where useMonth='"+useMonth+"' " + 
//					 "and f.PowerAccount=p.PowerAccount and b.BankCode=p.BankCode);";
		
		String sql = "select sum(bb.Area) Area, sum(bb.People) People from ( " + 
						 "	select b.*, c.ECO5Account from BankInf b inner join ControllerSetup c  " + 
						 "	on b.BankCode=c.BankCode and c.Enabled=1 group by BankCode " + 
						 ") bb " + 
					 "where  " + 
					 "exists " +
					 "(select 1 from FcstCharge f, PowerAccount p " +
					   "where useMonth='"+useMonth+"' and f.PowerAccount=p.PowerAccount and bb.BankCode=p.BankCode);";
		
		return executeQuery(sql);
	}
}
