package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerRecordVO;

public class PowerRecordDAO extends BaseDAO {
	/**
	 * 查詢電力趨勢圖
	 * @param powerRecordVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPowerRecordChart(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" pr.RecTime, ");
		sql.append(" sum(pr.W) as W, ");//-- 實功
		sql.append(" sum(pr.Var) as Var, ");//-- 虛功
		sql.append(" sum(pr.VA) as VA, ");//-- 視在
		sql.append(" avg(pr.I1) as I1, ");//-- 電流R相
		sql.append(" avg(pr.I2) as I2, ");//-- 電流S相
		sql.append(" avg(pr.I3) as I3, ");//-- 電流T相
		sql.append(" avg(pr.Iavg) as Iavg, ");//-- 平均電流
		sql.append(" avg(pr.V1) as V1, ");//-- 電壓R相
		sql.append(" avg(pr.V2) as V2, ");//-- 電壓S相
		sql.append(" avg(pr.V3) as V3, ");//-- 電壓T相
		sql.append(" avg(pr.Vavg) as Vavg, ");//-- 平均電壓
		sql.append(" avg(pr.V12) as V12, ");//-- 線電壓AB
		sql.append(" avg(pr.V23) as V23, ");//-- 線電壓BC
		sql.append(" avg(pr.V31) as V31, ");//-- 線電壓CA
		sql.append(" avg(pr.VavgP) as VavgP, ");//-- 平均線電壓
		sql.append(" sum(pr.Mode1) as Mode1, ");//-- 混合式
		sql.append(" sum(pr.Mode2) as Mode2, ");//-- 浮動式
		sql.append(" sum(pr.Mode3) as Mode3, ");//-- 固定式
		sql.append(" sum(pr.Mode4) as Mode4 ");//-- 平均式
		sql.append(" from  ");
		sql.append(" PowerAccount pa USE INDEX (Bank), ");
		sql.append(" MeterSetup ms USE INDEX (PowerAccount), ");
		sql.append(" PowerRecord pr use index(`PowerRecord_UDX`) ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(powerRecordVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(powerRecordVO.getBankCode());
		}
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.DeviceID = pr.DeviceID ");
		if(StringUtils.isNotBlank(powerRecordVO.getDeviceId())) {
			sql.append(" and ms.DeviceID = ? ");
			parameterList.add(powerRecordVO.getDeviceId());
		}else {
			sql.append(" and ms.UsageCode = 1 ");
		}	
		
		if(StringUtils.isNotBlank(powerRecordVO.getStartDate())) {			
			sql.append(" and pr.RecTime >= STR_TO_DATE(?,'%Y%m%d')  ");
			parameterList.add(powerRecordVO.getStartDate());
		}
		if(StringUtils.isNotBlank(powerRecordVO.getEndDate())) {
			sql.append(" and pr.RecTime < STR_TO_DATE(?,'%Y%m%d')  ");
			parameterList.add(ToolUtil.getNextDay(powerRecordVO.getEndDate()));
		}
		
		if(StringUtils.isNotBlank(powerRecordVO.getDateformat())) {
			sql.append(" and date_format(pr.RecTime,'%i')%? = 0 ");
			parameterList.add(powerRecordVO.getDateformat());
		}
		
		sql.append(" group by pr.RecTime ");
//		sql.append(" order by pr.RecTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public List<DynaBean> checkPowerRecord(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * from PowerRecord ");		
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(powerRecordVO.getDeviceID())) {
			sql.append(" and DeviceID = ? ");
			parameterList.add(powerRecordVO.getDeviceID());
		}
		if (StringUtils.isNotBlank(powerRecordVO.getRecTime())) {
			sql.append(" and RecTime = ? ");
			parameterList.add(powerRecordVO.getRecTime());
		}
		
		sql.append(" order by DeviceID,  RecTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getPowerRecordCollection(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * from PowerRecordCollection ");		
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(powerRecordVO.getDeviceID())) {
			sql.append(" and DeviceID = ? ");
			parameterList.add(powerRecordVO.getDeviceID());
		}
		if (StringUtils.isNotBlank(powerRecordVO.getRecDate())) {
			sql.append(" and RecDate = ? ");
			parameterList.add(powerRecordVO.getRecDate());
		}
		
		sql.append(" order by DeviceID,  RecDate ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}

	
	public void addPowerRecord(List<PowerRecordVO> VOlist) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" INSERT INTO PowerRecord ( ");
			sql1.append(" DeviceID, ");
			sql1.append(" RecTime, ");
			sql1.append(" I1, ");
			sql1.append(" I2, ");
			sql1.append(" I3, ");
			sql1.append(" Iavg, ");
			sql1.append(" V1, ");
			sql1.append(" V2, ");
			sql1.append(" V3, ");
			sql1.append(" Vavg, ");
			sql1.append(" V12, ");
			sql1.append(" V23, ");
			sql1.append(" V31, ");
			sql1.append(" VavgP, ");
			sql1.append(" W, ");
			sql1.append(" Var, ");
			sql1.append(" VA, ");
			sql1.append(" PF, ");
			sql1.append(" KWh, ");
			sql1.append(" Kvarh, ");
			sql1.append(" Hz, ");
			sql1.append(" THVavg, ");
			sql1.append(" THIavg, ");
			sql1.append(" Mode1, ");
			sql1.append(" Mode2, ");
			sql1.append(" Mode3, ");
			sql1.append(" Mode4, ");
			sql1.append(" DemandPK, ");
			sql1.append(" DemandSP, ");
			sql1.append(" DemandSatSP, ");
			sql1.append(" DemandOP, ");
			sql1.append(" MCECPK, ");
			sql1.append(" MCECSP, ");
			sql1.append(" MCECSatSP, ");
			sql1.append(" MCECOP, ");
			sql1.append(" HighCECPK, ");
			sql1.append(" HighCECSP, ");
			sql1.append(" HighCECOP ");
			sql1.append(" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
	
			ps = connection.prepareStatement(sql1.toString());
	
			for(PowerRecordVO powerRecordVO :VOlist) {
				int i=1;
				ps.setString(i++, powerRecordVO.getDeviceID());
				ps.setString(i++, powerRecordVO.getRecTime());
				ps.setString(i++, powerRecordVO.getI1());
				ps.setString(i++, powerRecordVO.getI2());
				ps.setString(i++, powerRecordVO.getI3());
				ps.setString(i++, powerRecordVO.getIavg());
				ps.setString(i++, powerRecordVO.getV1());
				ps.setString(i++, powerRecordVO.getV2());
				ps.setString(i++, powerRecordVO.getV3());
				ps.setString(i++, powerRecordVO.getVavg());
				ps.setString(i++, powerRecordVO.getV12());
				ps.setString(i++, powerRecordVO.getV23());
				ps.setString(i++, powerRecordVO.getV31());
				ps.setString(i++, powerRecordVO.getVavgP());
				ps.setString(i++, powerRecordVO.getW());
				ps.setString(i++, powerRecordVO.getVar());
				ps.setString(i++, powerRecordVO.getVA());
				ps.setString(i++, powerRecordVO.getPF());
				ps.setString(i++, powerRecordVO.getKWh());
				ps.setString(i++, powerRecordVO.getKvarh());
				ps.setString(i++, powerRecordVO.getHz());
				ps.setString(i++, powerRecordVO.getTHVavg());
				ps.setString(i++, powerRecordVO.getTHIavg());
				ps.setString(i++, powerRecordVO.getMode1());
				ps.setString(i++, powerRecordVO.getMode2());
				ps.setString(i++, powerRecordVO.getMode3());
				ps.setString(i++, powerRecordVO.getMode4());
				ps.setString(i++, powerRecordVO.getDemandPK());
				ps.setString(i++, powerRecordVO.getDemandSP());
				ps.setString(i++, powerRecordVO.getDemandSatSP());
				ps.setString(i++, powerRecordVO.getDemandOP());
				ps.setString(i++, powerRecordVO.getMCECPK());
				ps.setString(i++, powerRecordVO.getMCECSP());
				ps.setString(i++, powerRecordVO.getMCECSatSP());
				ps.setString(i++, powerRecordVO.getMECEOP());
				ps.setString(i++, powerRecordVO.getHighCECPK());
				ps.setString(i++, powerRecordVO.getHighCECSP());
				ps.setString(i++, powerRecordVO.getHighCECOP());	
				ps.addBatch();
			}
			ps.executeBatch();
			
			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	public void updPowerRecord(List<PowerRecordVO> VOlist) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update PowerRecord set  ");			
			sql1.append(" I1 = ?, ");
			sql1.append(" I2 = ?, ");
			sql1.append(" I3 = ?, ");
			sql1.append(" Iavg = ?, ");
			sql1.append(" V1 = ?, ");
			sql1.append(" V2 = ?, ");
			sql1.append(" V3 = ?, ");
			sql1.append(" Vavg = ?, ");
			sql1.append(" V12 = ?, ");
			sql1.append(" V23 = ?, ");
			sql1.append(" V31 = ?, ");
			sql1.append(" VavgP = ?, ");
			sql1.append(" W = ?, ");
			sql1.append(" Var = ?, ");
			sql1.append(" VA = ?, ");
			sql1.append(" PF = ?, ");
			sql1.append(" KWh = ?, ");
			sql1.append(" Kvarh = ?, ");
			sql1.append(" Hz = ?, ");
			sql1.append(" THVavg = ?, ");
			sql1.append(" THIavg = ?, ");
			sql1.append(" Mode1 = ?, ");
			sql1.append(" Mode2 = ?, ");
			sql1.append(" Mode3 = ?, ");
			sql1.append(" Mode4 = ?, ");
			sql1.append(" DemandPK = ?, ");
			sql1.append(" DemandSP = ?, ");
			sql1.append(" DemandSatSP = ?, ");
			sql1.append(" DemandOP = ?, ");
			sql1.append(" MCECPK = ?, ");
			sql1.append(" MCECSP = ?, ");
			sql1.append(" MCECSatSP = ?, ");
			sql1.append(" MCECOP = ?, ");
			sql1.append(" HighCECPK = ?, ");
			sql1.append(" HighCECSP = ?, ");
			sql1.append(" HighCECOP = ? ");
			sql1.append(" where DeviceID = ? ");
			sql1.append(" and RecTime = ? ");
			ps = connection.prepareStatement(sql1.toString());	
			for(PowerRecordVO powerRecordVO :VOlist) {
				int i=1;				
				ps.setString(i++, powerRecordVO.getI1());
				ps.setString(i++, powerRecordVO.getI2());
				ps.setString(i++, powerRecordVO.getI3());
				ps.setString(i++, powerRecordVO.getIavg());
				ps.setString(i++, powerRecordVO.getV1());
				ps.setString(i++, powerRecordVO.getV2());
				ps.setString(i++, powerRecordVO.getV3());
				ps.setString(i++, powerRecordVO.getVavg());
				ps.setString(i++, powerRecordVO.getV12());
				ps.setString(i++, powerRecordVO.getV23());
				ps.setString(i++, powerRecordVO.getV31());
				ps.setString(i++, powerRecordVO.getVavgP());
				ps.setString(i++, powerRecordVO.getW());
				ps.setString(i++, powerRecordVO.getVar());
				ps.setString(i++, powerRecordVO.getVA());
				ps.setString(i++, powerRecordVO.getPF());
				ps.setString(i++, powerRecordVO.getKWh());
				ps.setString(i++, powerRecordVO.getKvarh());
				ps.setString(i++, powerRecordVO.getHz());
				ps.setString(i++, powerRecordVO.getTHVavg());
				ps.setString(i++, powerRecordVO.getTHIavg());
				ps.setString(i++, powerRecordVO.getMode1());
				ps.setString(i++, powerRecordVO.getMode2());
				ps.setString(i++, powerRecordVO.getMode3());
				ps.setString(i++, powerRecordVO.getMode4());
				ps.setString(i++, powerRecordVO.getDemandPK());
				ps.setString(i++, powerRecordVO.getDemandSP());
				ps.setString(i++, powerRecordVO.getDemandSatSP());
				ps.setString(i++, powerRecordVO.getDemandOP());
				ps.setString(i++, powerRecordVO.getMCECPK());
				ps.setString(i++, powerRecordVO.getMCECSP());
				ps.setString(i++, powerRecordVO.getMCECSatSP());
				ps.setString(i++, powerRecordVO.getMECEOP());
				ps.setString(i++, powerRecordVO.getHighCECPK());
				ps.setString(i++, powerRecordVO.getHighCECSP());
				ps.setString(i++, powerRecordVO.getHighCECOP());	
				ps.setString(i++, powerRecordVO.getDeviceID());
				ps.setString(i++, powerRecordVO.getRecTime());
				ps.addBatch();
			}
			ps.executeBatch();
			
			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	public void delPowerRecord(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" DELETE FROM PowerRecord ");		
		sql.append(" where 1=1 ");
		sql.append(" and DeviceID = ? ");
		parameterList.add(powerRecordVO.getDeviceID());
		sql.append(" and RecTime = ? ");
		parameterList.add(powerRecordVO.getRecTime());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getFcstCharge(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select f.* from FcstCharge f  ");		
		sql.append(" where f.useTime = (select max(a.useTime) from FcstCharge a where a.PowerAccount = f.PowerAccount and a.useMonth = f.useMonth)  ");
		if (StringUtils.isNotBlank(powerRecordVO.getPowerAccount())) {
			sql.append(" and f.PowerAccount = ? ");
			parameterList.add(powerRecordVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(powerRecordVO.getUseMonth())) {
			sql.append(" and f.useMonth = ? ");
			parameterList.add(powerRecordVO.getUseMonth());
		}
		
		sql.append(" order by f.PowerAccount,  f.useTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢電力數值
	 * @param powerRecordVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPowerRecord(PowerRecordVO powerRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" * ");
		sql.append(" from  ");
		sql.append(" PowerRecord pr use index(`PowerRecord_UDX`) ");
		sql.append(" where pr.DeviceID = ? ");
		parameterList.add(powerRecordVO.getDeviceId());
		
		if(StringUtils.isNotBlank(powerRecordVO.getStartDate())) {			
			sql.append(" and pr.RecTime >= STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s')  ");
			parameterList.add(powerRecordVO.getStartDate());
			
		}
		if(StringUtils.isNotBlank(powerRecordVO.getEndDate())) {
			sql.append(" and pr.RecTime <= STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s')  ");
			parameterList.add(powerRecordVO.getEndDate());
		}
		
		if(StringUtils.isNotBlank(powerRecordVO.getDateformat())) {
			sql.append(" and date_format(pr.RecTime,'%i')%? = 0 ");
			parameterList.add(powerRecordVO.getDateformat());
		}
		
		sql.append(" order by pr.RecTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
