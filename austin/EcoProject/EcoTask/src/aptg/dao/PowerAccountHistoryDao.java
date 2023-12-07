package aptg.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.base.BaseDao2;

public class PowerAccountHistoryDao extends BaseDao2 {

	public List<DynaBean> queryPowerAccountMeterByDeviceID(String recDate, String deviceID) throws SQLException {
		String sql = "SELECT  " + 
					 " pa.PowerAccount, pa.RatePlanCode, pa.UsuallyCC, pa.SPCC, pa.SatSPCC, pa.OPCC,  " + 
					 " t5.DeviceID, t5.RecDate, t5.MDemandPK, t5.MDemandSP, t5.MDemandSatSP, t5.MDemandOP, " + 
					 " t5.MCECPK, t5.MCECSP, t5.MCECSatSP, t5.MCECOP, t5.CEC, " + 
					 " t5.FcstECO5MCECPK, t5.FcstECO5MCECSP, t5.FcstECO5MCECSatSP, t5.FcstECO5MCECOP, t5.FcstECO5MCEC, " + 
					 " t5.PowerPhase " + 
					 "FROM PowerAccountHistory pa inner join ( " + 
					 "	SELECT t4.*, t3.PowerPhase FROM PowerAccount t3 inner join ( " + 
					 "		SELECT t2.*, t1.PowerAccount FROM MeterSetup t1 inner join ( " + 
					 "			SELECT * FROM PowerRecordCollection WHERE DeviceID = '"+deviceID+"' and RecDate=str_to_date('"+recDate+"','%Y-%m-%d') " + 
					 "		) t2 on t1.UsageCode=1 and t1.Enabled=1 and t1.DeviceID = t2.DeviceID " + 
					 "	) t4 on t3.PowerAccount = t4.PowerAccount " + 
					 ") t5 on t5.PowerAccount = pa.PowerAccount and pa.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE ApplyDate < t5.RecDate and PowerAccount = t5.PowerAccount);";
		
//		System.out.println("############## SQL: "+sql);
		return executeQuery(sql);
	}
	
	public List<DynaBean> queryCollectionHistoryCC(String deviceID, String recDate) throws SQLException {
		String sql = "SELECT " +
					 "	m.DeviceID, pah.ApplyDate, pah.RatePlanCode, pah.UsuallyCC, pah.SPCC, pah.SatSPCC, pah.OPCC " +
					 "FROM MeterSetup m, PowerAccountHistory pah " + 
					 "where "+
						 "m.DeviceID='"+deviceID+"' " + 
						 "and m.PowerAccount=pah.PowerAccount " + 
						 "and pah.ApplyDate = (SELECT Max(ApplyDate) FROM PowerAccountHistory WHERE PowerAccount = m.PowerAccount and ApplyDate<str_to_date('"+recDate+"','%Y-%m-%d') );";
		
		return executeQuery(sql);
	}
}
