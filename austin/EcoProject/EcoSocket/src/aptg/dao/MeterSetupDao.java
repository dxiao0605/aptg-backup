package aptg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;
import aptg.models.MeterSetupModel;

public class MeterSetupDao extends BaseDao2 {
	
	public List<DynaBean> queryAllEnabledMeter() throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE Enabled = 1";
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryEnabledMeter(String deviceID) throws SQLException {
		String sql = "SELECT * FROM MeterSetup WHERE DeviceID = '"+deviceID+"' and Enabled = 1";
		return executeQuery(sql);
	}
	
	public int updateMeter(MeterSetupModel meter) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "UPDATE MeterSetup SET " +
					 " DFEnabled=?, DFCode=?, DFUplimit=?, DFLoLimit=?, DFEnabled=?, "+
					 " UsuallyCC=?, SPCC=?, SatSPCC=?, OPCC=?, "+
					 " CurUplimit=?, CurLoLimit=?, CurAlertEnabled=?, "+
					 " VolAlertType=?, VolUpLimit=?, VolLoLimit=?, VolAlertEnabled=?, "+
					 " ECUpLimit=?, ECAlertEnabled=? "+
					 "WHERE ECO5Account=? and DeviceID=?";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);
		
		ps.setInt(1, meter.getDfEnabled());
		ps.setInt(2, meter.getDfCode());
		ps.setInt(3, meter.getDfUpLimit());
		ps.setInt(4, meter.getDfLoLimit());
		ps.setInt(5, meter.getDfEnabled());

		ps.setInt(6, meter.getUsuallyCC());
		ps.setInt(7, meter.getSpcc());
		ps.setInt(8, meter.getSatSPCC());
		ps.setInt(9, meter.getOpcc());

		ps.setBigDecimal(10, meter.getCurUpLimit());
		ps.setBigDecimal(11, meter.getCurLoLimit());
		ps.setInt(12, meter.getCurAlertEnabled());

		ps.setInt(13, meter.getVolAlertType());
		ps.setBigDecimal(14, meter.getVolUpLimit());
		ps.setBigDecimal(15, meter.getVolLoLimit());
		ps.setInt(16, meter.getVolAlertEnabled());
		
		ps.setBigDecimal(17, meter.getEcUpLimit());
		ps.setInt(18, meter.getEcAlertEnabled());

		ps.setString(19, meter.getEco5Account());
		ps.setString(20, meter.getDeviceID());

		ps.addBatch();
		sqlList.add(ps.toString());

		int count = batchUpdate(connection, ps, sqlList);
		return count;
	}
}
