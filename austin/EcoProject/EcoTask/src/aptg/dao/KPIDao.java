package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.models.KPIModel;

public class KPIDao extends BaseDao2 {

	public int updateKPIinfo(KPIModel kpi) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE KPI SET UnitPriceKPIavg=?, EUIKPIavg=?, EPUIKPIavg=?, AirKPIavg=?";
				
		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		ps.setBigDecimal(1, kpi.getUnitPriceKPIavg());
		ps.setBigDecimal(2, kpi.getEUIKPIavg());
		ps.setBigDecimal(3, kpi.getEPUIKPIavg());
		ps.setBigDecimal(4, kpi.getAirKPIavg());
		
		ps.addBatch();
		sqlList.add(ps.toString());
//		System.out.println("sql: "+ps.toString());

		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
}
