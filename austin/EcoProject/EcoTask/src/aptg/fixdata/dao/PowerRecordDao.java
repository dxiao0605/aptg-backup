package aptg.fixdata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aptg.dao.base.BaseDao2;
import aptg.fixdata.model.PowerRecordModel;

public class PowerRecordDao extends BaseDao2 {

	public List<Integer> insertPowerRecord(List<PowerRecordModel> list) throws SQLException {
		List<String> sqlList = new ArrayList<>();
		
		String sql = "INSERT INTO PowerRecord_supply "
				   + "(DeviceID, RecTime, "
				   + "I1, I2, I3, Iavg, "
				   + "V1, V2, V3, Vavg, "
				   + "V12, V23, V31, VavgP, "
				   + "W, Var, VA, PF, KWh, Kvarh, Hz, THVavg, THIavg, "
				   + "Mode1, Mode2, Mode3, Mode4, "
				   + "DemandPK, DemandSP, DemandSatSP, DemandOP, "
				   + "MCECPK, MCECSP, MCECSatSP, MCECOP, "
				   + "HighCECPK, HighCECSP, HighCECOP) VALUES "
				   + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection connection = initConnection();
		PreparedStatement ps = initPreparedStatement(connection, sql);

		for (PowerRecordModel model: list) {
			ps.setString(1, model.getDeviceId());
			ps.setString(2, model.getRecTime());
			ps.setBigDecimal(3, model.getI1());
			ps.setBigDecimal(4, model.getI2());
			ps.setBigDecimal(5, model.getI3());
			ps.setBigDecimal(6, model.getIavg());
			ps.setBigDecimal(7, model.getV1());
			ps.setBigDecimal(8, model.getV2());
			ps.setBigDecimal(9, model.getV3());
			ps.setBigDecimal(10, model.getVavg());
			ps.setBigDecimal(11, model.getV12());
			ps.setBigDecimal(12, model.getV23());
			ps.setBigDecimal(13, model.getV31());
			ps.setBigDecimal(14, model.getVavgP());
			ps.setBigDecimal(15, model.getW());
			ps.setBigDecimal(16, model.getVar());
			ps.setBigDecimal(17, model.getVa());
			ps.setBigDecimal(18, model.getPf());
			ps.setBigDecimal(19, model.getkWh());
			ps.setBigDecimal(20, model.getKvarh());
			ps.setBigDecimal(21, model.getHz());
			ps.setBigDecimal(22, model.getThIavg());
			ps.setBigDecimal(23, model.getThIavg());
			ps.setBigDecimal(24, model.getMode1());
			ps.setBigDecimal(25, model.getMode2());
			ps.setBigDecimal(26, model.getMode3());
			ps.setBigDecimal(27, model.getMode4());
			ps.setBigDecimal(28, model.getDemandPK());
			ps.setBigDecimal(29, model.getDemandSP());
			ps.setBigDecimal(30, model.getDemandSatSP());
			ps.setBigDecimal(31, model.getDemandOP());
			ps.setBigDecimal(32, model.getMcecPK());
			ps.setBigDecimal(33, model.getMcecSP());
			ps.setBigDecimal(34, model.getMcecSatSP());
			ps.setBigDecimal(35, model.getMcecOP());
			ps.setBigDecimal(36, model.getHighCECPK());
			ps.setBigDecimal(37, model.getHighCECSP());
			ps.setBigDecimal(38, model.getHighCECOP());

			ps.addBatch();
			sqlList.add(ps.toString());
		}

		List<Integer> ids = batchInsert(connection, ps, sqlList);
		return ids;
	}
}
