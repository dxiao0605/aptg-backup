package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

public class MeterSetupDAO extends BaseDAO {
 
	/**
	 * 取得電表資訊
	 * 
	 * @param meterSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getMeterSetup(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select ");
		sql.append(" ph.RatePlanCode , ");
		sql.append(" (select rp.RatePlanDesc from RatePlanList rp where rp.RatePlanCode = ph.RatePlanCode) as RatePlanDesc, ");
		sql.append(" (select mt.MeterType from MeterTypeList mt where mt.MeterTypeCode =m. MeterTypeCode) as MeterType, ");
		sql.append(" (select w.WiringDesc from WiringList w where w.WiringCode = m.WiringCode) as WiringDesc, ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = m.UsageCode) as UsageDesc, ");
		sql.append(" (select df.DFName from DemandForcastList df where df.DFCode = m.DFCode) as DFName, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID is null and me.ECO5Account = m.ECO5Account and me.Priority = 2 and me.EventStatus = 0) as eventCount1, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID = m.DeviceID  and me.Priority = 2 and me.EventStatus = 0 ) as eventCount2, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID is null and me.ECO5Account = m.ECO5Account and me.Priority = 1 and me.EventStatus = 0) as eventCount3, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID = m.DeviceID  and me.Priority = 1 and me.EventStatus = 0 ) as eventCount4, ");
		sql.append(" p.BankCode, ");
		sql.append(" m.* ");
		sql.append(" from MeterSetup m ");
		sql.append(" left join PowerAccountHistory ph on ph.PowerAccount = m.PowerAccount ");
		sql.append(" and ph.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a ");
		sql.append(" where a.PowerAccount = m.PowerAccount ");
		sql.append(" and a.ApplyDate <= now()), ");
		sql.append(" PowerAccount p ");
		sql.append(" where m.PowerAccount = p.PowerAccount ");
		if (StringUtils.isNotBlank(meterSetupVO.getBankCode())) {
			sql.append(" and p.BankCode = ? ");
			parameterList.add(meterSetupVO.getBankCode());
		}
		if (StringUtils.isNotBlank(meterSetupVO.getDeviceId())) {
			sql.append(" and m.DeviceID = ? ");
			parameterList.add(meterSetupVO.getDeviceId());
		}
		if (StringUtils.isNotBlank(meterSetupVO.getEco5Account())) {
			sql.append(" and m.ECO5Account = ? ");
			parameterList.add(meterSetupVO.getEco5Account());
		}
		if (StringUtils.isNotBlank(meterSetupVO.getEnabled())) {
			sql.append(" and m.Enabled = ? ");
			parameterList.add(meterSetupVO.getEnabled());
		}
		sql.append(" order by m.ECO5Account, m.TreeChartID  ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 新增電表資訊
	 * 
	 * @param meterSetupVO
	 * @throws Exception
	 */
	public void addMeterSetup(MeterSetupVO meterSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO MeterSetup ( ");
			sql.append(" DeviceID, ");
			sql.append(" ECO5Account, ");
			sql.append(" MeterSerialNr, ");
			sql.append(" MeterID, ");
			sql.append(" MeterName, ");
			sql.append(" MeterTypeCode, ");
			sql.append(" InstallPosition, ");
			sql.append(" TreeChartID, ");
			sql.append(" Enabled, ");
			sql.append(" WiringCode, ");
			sql.append(" UsageCode, ");
			sql.append(" PowerAccount, ");
			sql.append(" PowerFactorEnabled, ");
			sql.append(" AreaName, ");
			sql.append(" Area, ");
			sql.append(" People, ");
			sql.append(" RatedPower, ");
			sql.append(" DFEnabled, ");
			sql.append(" DFCode, ");
			sql.append(" DFCycle, ");
			sql.append(" DFUpLimit, ");
			sql.append(" DFLoLimit, ");
			sql.append(" UsuallyCC, ");
			sql.append(" SPCC, ");
			sql.append(" SatSPCC, ");
			sql.append(" OPCC, ");
			sql.append(" CurAlertEnabled, ");
			sql.append(" CurUpLimit, ");
			sql.append(" CurLoLimit, ");
			sql.append(" VolAlertEnabled, ");
			sql.append(" VolAlertType, ");
			sql.append(" VolUpLimit, ");
			sql.append(" VolLoLimit, ");
			sql.append(" ECAlertEnabled, ");
			sql.append(" ECUpLimit, ");
			sql.append(" RatedVol, ");
			sql.append(" RatedCur, ");
			sql.append(" EquipDesc, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			
			ps = connection.prepareStatement(sql.toString());
			int i=1;			
			ps.setString(i++, meterSetupVO.getDeviceId());
			ps.setString(i++, meterSetupVO.getEco5Account());
			ps.setString(i++, meterSetupVO.getMeterID());
			ps.setString(i++, meterSetupVO.getMeterID());
			ps.setString(i++, meterSetupVO.getMeterName());
			ps.setString(i++, meterSetupVO.getMeterTypeCode());
			ps.setString(i++, meterSetupVO.getInstallPosition());
			ps.setString(i++, meterSetupVO.getTreeChartID());
			ps.setString(i++, meterSetupVO.getEnabled());
			ps.setString(i++, meterSetupVO.getWiringCode());
			ps.setString(i++, meterSetupVO.getUsageCode());
			ps.setString(i++, meterSetupVO.getPowerAccount());
			ps.setString(i++, meterSetupVO.getPowerFactorEnabled());
			ps.setString(i++, meterSetupVO.getAreaName());
			ps.setString(i++, meterSetupVO.getArea());
			ps.setString(i++, meterSetupVO.getPeople());
			ps.setString(i++, meterSetupVO.getRatedPower());
			ps.setString(i++, meterSetupVO.getDfEnabled());
			ps.setString(i++, meterSetupVO.getDfCode());
			ps.setString(i++, meterSetupVO.getDfCycle());
			ps.setString(i++, meterSetupVO.getDfUpLimit());
			ps.setString(i++, meterSetupVO.getDfLoLimit());
			ps.setString(i++, meterSetupVO.getUsuallyCC());
			ps.setString(i++, meterSetupVO.getSpcc());
			ps.setString(i++, meterSetupVO.getSatSPCC());
			ps.setString(i++, meterSetupVO.getOpcc());		
			ps.setString(i++, meterSetupVO.getCurAlertEnabled());
			ps.setString(i++, meterSetupVO.getCurUpLimit());
			ps.setString(i++, meterSetupVO.getCurLoLimit());
			ps.setString(i++, meterSetupVO.getVolAlertEnabled());
			ps.setString(i++, meterSetupVO.getVolAlertType());
			ps.setString(i++, meterSetupVO.getVolUpLimit());
			ps.setString(i++, meterSetupVO.getVolLoLimit());
			ps.setString(i++, meterSetupVO.getEcAlertEnabled());
			ps.setString(i++, meterSetupVO.getEcUpLimit());
			ps.setString(i++, meterSetupVO.getRatedVol());
			ps.setString(i++, meterSetupVO.getRatedCur());
			ps.setString(i++, meterSetupVO.getEquipDesc());
			ps.setString(i++, meterSetupVO.getUserName());
			ps.setString(i++, meterSetupVO.getUserName());
				
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 修改電表資訊
	 * 
	 * @param meterSetupVO
	 * @throws Exception
	 */
	public void updMeterSetup(MeterSetupVO meterSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
					
			int i=1;
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" UPDATE MeterSetup ");
			sql1.append(" SET ");
			sql1.append(" MeterName = ?, ");
			sql1.append(" MeterTypeCode = ?, ");
			sql1.append(" InstallPosition = ?, ");
			sql1.append(" TreeChartID = ?, ");
			sql1.append(" Enabled = ?, ");
			sql1.append(" WiringCode = ?, ");
			sql1.append(" UsageCode = ?, ");
			sql1.append(" PowerAccount = ?, ");
			sql1.append(" PowerFactorEnabled = ?, ");
			sql1.append(" AreaName = ?, ");
			sql1.append(" Area = ?, ");
			sql1.append(" People = ?, ");
			sql1.append(" RatedPower = ?, ");
			sql1.append(" DFEnabled = ?, ");
			sql1.append(" DFCode = ?, ");
			sql1.append(" DFCycle = ?, ");
			sql1.append(" DFUpLimit = ?, ");
			sql1.append(" DFLoLimit = ?, ");
			sql1.append(" UsuallyCC = ?, ");
			sql1.append(" SPCC = ?, ");
			sql1.append(" SatSPCC = ?, ");
			sql1.append(" OPCC = ?, ");
			sql1.append(" CurAlertEnabled = ?, ");
			sql1.append(" CurUpLimit = ?, ");
			sql1.append(" CurLoLimit = ?, ");
			sql1.append(" VolAlertEnabled = ?, ");
			sql1.append(" VolAlertType = ?, ");
			sql1.append(" VolUpLimit = ?, ");
			sql1.append(" VolLoLimit = ?, ");
			sql1.append(" ECAlertEnabled = ?, ");
			sql1.append(" ECUpLimit = ?, ");
			sql1.append(" RatedVol = ?, ");
			sql1.append(" RatedCur = ?, ");
			sql1.append(" EquipDesc = ?, ");
			if(StringUtils.isNotBlank(meterSetupVO.getRepriceStatus())) {
				sql1.append(" RepriceStatus = ?, ");
			}
			sql1.append(" UpdateUserName = ? ");
			sql1.append(" WHERE DeviceID = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(i++, meterSetupVO.getMeterName());
			ps1.setString(i++, meterSetupVO.getMeterTypeCode());
			ps1.setString(i++, meterSetupVO.getInstallPosition());
			ps1.setString(i++, meterSetupVO.getTreeChartID());
			ps1.setString(i++, meterSetupVO.getEnabled());
			ps1.setString(i++, meterSetupVO.getWiringCode());
			ps1.setString(i++, meterSetupVO.getUsageCode());
			ps1.setString(i++, meterSetupVO.getPowerAccount());
			ps1.setString(i++, meterSetupVO.getPowerFactorEnabled());
			ps1.setString(i++, meterSetupVO.getAreaName());
			ps1.setString(i++, meterSetupVO.getArea());
			ps1.setString(i++, meterSetupVO.getPeople());
			ps1.setString(i++, meterSetupVO.getRatedPower());
			ps1.setString(i++, meterSetupVO.getDfEnabled());
			ps1.setString(i++, meterSetupVO.getDfCode());
			ps1.setString(i++, meterSetupVO.getDfCycle());
			ps1.setString(i++, meterSetupVO.getDfUpLimit());
			ps1.setString(i++, meterSetupVO.getDfLoLimit());
			ps1.setString(i++, meterSetupVO.getUsuallyCC());
			ps1.setString(i++, meterSetupVO.getSpcc());
			ps1.setString(i++, meterSetupVO.getSatSPCC());
			ps1.setString(i++, meterSetupVO.getOpcc());
			ps1.setString(i++, meterSetupVO.getCurAlertEnabled());
			ps1.setString(i++, meterSetupVO.getCurUpLimit());
			ps1.setString(i++, meterSetupVO.getCurLoLimit());
			ps1.setString(i++, meterSetupVO.getVolAlertEnabled());
			ps1.setString(i++, meterSetupVO.getVolAlertType());
			ps1.setString(i++, meterSetupVO.getVolUpLimit());
			ps1.setString(i++, meterSetupVO.getVolLoLimit());
			ps1.setString(i++, meterSetupVO.getEcAlertEnabled());
			ps1.setString(i++, meterSetupVO.getEcUpLimit());
			ps1.setString(i++, meterSetupVO.getRatedVol());
			ps1.setString(i++, meterSetupVO.getRatedCur());
			ps1.setString(i++, meterSetupVO.getEquipDesc());
			if(StringUtils.isNotBlank(meterSetupVO.getRepriceStatus())) {
				ps1.setString(i++, meterSetupVO.getRepriceStatus());
			}
			ps1.setString(i++, meterSetupVO.getUserName());
			ps1.setString(i++, meterSetupVO.getDeviceId());
			ps1.executeUpdate();
			
			
			if("1".equals(meterSetupVO.getRepriceStatus())) {
				StringBuffer sql2 = new StringBuffer();
				sql2.append(" update PowerAccount   ");
				sql2.append(" set ModifyStatus = 3, ");
				sql2.append(" UpdateUserName = ? ");
				sql2.append(" where PowerAccount = ? ");
				ps2 = connection.prepareStatement(sql2.toString());
				ps2.setString(1, meterSetupVO.getUserName());
				ps2.setString(2, meterSetupVO.getPowerAccount());
				ps2.executeUpdate();
				
				
				StringBuffer sql3 = new StringBuffer();					
				sql3.append(" INSERT INTO RepriceTask ( ");
				sql3.append(" DeviceID, ");
				sql3.append(" PowerAccount, ");
				sql3.append(" RepriceFrom, ");
				sql3.append(" StartDate, ");
				sql3.append(" PowerPhaseNew, ");
				sql3.append(" ApplyDateNew, ");
				sql3.append(" RatePlanCodeNew, ");
				sql3.append(" UsuallyCCNew, ");
				sql3.append(" SPCCNew, ");
				sql3.append(" SatSPCCNew, ");
				sql3.append(" OPCCNew, ");
				sql3.append(" CreateUserName, ");
				sql3.append(" UpdateUserName ");
				sql3.append(" ) VALUES ( ");
				sql3.append(" ?, ?, 'Collection', ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
				sql3.append(" ) ");
				
				ps3 = connection.prepareStatement(sql3.toString());		
				ps3.setString(1, meterSetupVO.getDeviceId());
				ps3.setString(2, meterSetupVO.getPowerAccount());	
				ps3.setString(3, meterSetupVO.getRepriceDate());
				ps3.setString(4, meterSetupVO.getPowerPhaseNew());	
				ps3.setString(5, meterSetupVO.getApplyDateNew());
				ps3.setString(6, meterSetupVO.getRatePlanCodeNew());	
				ps3.setString(7, meterSetupVO.getUsuallyCCNew());
				ps3.setString(8, meterSetupVO.getSpccNew());	
				ps3.setString(9, meterSetupVO.getSatspccNew());
				ps3.setString(10, meterSetupVO.getOpccNew());	
				ps3.setString(11, meterSetupVO.getUserName());
				ps3.setString(12, meterSetupVO.getUserName());	
				ps3.executeUpdate();
			}

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 取得電表明細
	 * @param meterSetupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterDetail(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" ms.MeterName, ");//-- 電表名稱
		sql.append(" ms.InstallPosition, ");//-- 安裝位置
		sql.append(" ms.UsageCode, ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");// -- 耗能分類
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People, ");//-- 員工數
		sql.append(" ms.PowerAccount, ");//-- 電號
		sql.append(" pa.AccountDesc, ");//-- 說明
		sql.append(" (select rp.RatePlanDesc from RatePlanList rp where rp.RatePlanCode = pah.RatePlanCode) as RatePlanDesc, ");//-- 用電類型
		sql.append(" (ifnull(ms.UsuallyCC,0)+ifnull(ms.SPCC,0)+ifnull(ms.SatSPCC,0)+ifnull(ms.OPCC,0)) as CC,  ");//-- 契約容量
		sql.append(" (select mt.MeterType from MeterTypeList mt where mt.MeterTypeCode =ms.MeterTypeCode) as MeterType,  ");//-- 電表型號
		sql.append(" (select w.WiringDesc from WiringList w where w.WiringCode = ms.WiringCode) as WiringDesc  ");//-- 接線方式
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()), ");
		sql.append("      MeterSetup ms ");
		sql.append("      where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append("      and ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getPriority(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID is null and me.ECO5Account = m.ECO5Account and me.Priority = 2 and me.EventStatus = 0) as eventCount1, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID = m.DeviceID  and me.Priority = 2 and me.EventStatus = 0 ) as eventCount2, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID is null and me.ECO5Account = m.ECO5Account and me.Priority = 1 and me.EventStatus = 0) as eventCount3, ");
		sql.append(" (select count(1) from MeterEventTx me where me.DeviceID = m.DeviceID  and me.Priority = 1 and me.EventStatus = 0 ) as eventCount4 ");
		
		sql.append(" from MeterSetup m where m.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 當月電力資訊(by 日)
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityDaily(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" Area, ");
		sql.append(" People, ");
		sql.append(" RecDate,  ");
		sql.append(" sum(TPDCECPK) as TPDCECPK, ");
		sql.append(" sum(TPDCECSP) as TPDCECSP, ");
		sql.append(" sum(TPDCECSatSP) as TPDCECSatSP, ");
		sql.append(" sum(TPDCECOP) as TPDCECOP, ");
		sql.append(" sum(DCECPK) as DCECPK, ");
		sql.append(" sum(DCECSP) as DCECSP, ");
		sql.append(" sum(DCECSatSP) as DCECSatSP, ");
		sql.append(" sum(DCECOP) as DCECOP, ");
		sql.append(" sum(Demand) as Demand, ");
		sql.append(" sum(CC) as CC, ");
		sql.append(" sum(DF) as DF ");
		sql.append(" from ( ");
		sql.append(" select  ");
		sql.append(" b.Area, ");
		sql.append(" b.People, ");	
		sql.append(" pr.RecDate, ");
		sql.append(" pr.TPDCECPK, ");//-- 台電尖峰用電量
		sql.append(" pr.TPDCECSP, ");//-- 台電半尖峰用電量
		sql.append(" pr.TPDCECSatSP, ");//-- 台電周六半尖峰用電量
		sql.append(" pr.TPDCECOP, ");//-- 台電離峰用電量
		sql.append(" pr.DCECPK, ");//-- ECO5尖峰用電量
		sql.append(" pr.DCECSP, ");//-- ECO5半尖峰用電量
		sql.append(" pr.DCECSatSP, ");//-- ECO5周六半尖峰用電量
		sql.append(" pr.DCECOP, ");//-- ECO5離峰用電量		
		sql.append(" greatest(pr.DemandPK, pr.DemandSP, pr.DemandSatSP, pr.DemandOP) as Demand, ");
		sql.append(" ifnull(pah.UsuallyCC,0)+ifnull(pah.SPCC,0)+ifnull(pah.SatSPCC,0)+ifnull(pah.OPCC,0) as CC, ");
		sql.append(" pr.Mode1 as DF ");
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa  ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount  ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) , ");
		sql.append(" MeterSetup ms , ");
		sql.append(" PowerRecordCollection pr ");
		sql.append(" where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PATypeCode <= 10 ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.DeviceID = pr.DeviceID ");
		sql.append("   and ms.Enabled = 1  ");
		if(StringUtils.isNotBlank(meterSetupVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(meterSetupVO.getBankCode());	
		}
		if(StringUtils.isNotBlank(meterSetupVO.getDeviceId())) {
			sql.append(" and ms.DeviceID = ? ");
			parameterList.add(meterSetupVO.getDeviceId());	
		}else {
			sql.append(" and ms.UsageCode = 1 ");
		}
		
		if(StringUtils.isNotBlank(meterSetupVO.getStartDate()) || StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
			if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {		
				sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y%m%d')  ");
				parameterList.add(meterSetupVO.getStartDate());
			}
			if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {				
				sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y%m%d')  ");
				parameterList.add(ToolUtil.getNextDay(meterSetupVO.getEndDate()));
			}
		}else if (StringUtils.isNotBlank(meterSetupVO.getDate())) {
			sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(meterSetupVO.getDate());			
			sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y%m') ");			
			parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getDate()));
		}else {
			//預設當月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String thisMonth = sdf.format(new Date());
			sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(thisMonth);
			sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(ToolUtil.getNextMonth(thisMonth));
		}
		sql.append(" ) a ");
		sql.append(" group by RecDate ");
		sql.append(" order by RecDate ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電力資訊(by 月)
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityMonthly(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" Area, ");
		sql.append(" People, ");
		sql.append(" RecDate,  ");
		sql.append(" sum(TPMCECPK) as TPMCECPK, ");
		sql.append(" sum(TPMCECSP) as TPMCECSP, ");
		sql.append(" sum(TPMCECSatSP) as TPMCECSatSP, ");
		sql.append(" sum(TPMCECOP) as TPMCECOP, ");
		sql.append(" sum(MCECPK) as MCECPK, ");
		sql.append(" sum(MCECSP) as MCECSP, ");
		sql.append(" sum(MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(MCECOP) as MCECOP, ");
		sql.append(" sum(Demand) as Demand, ");
		sql.append(" sum(CC) as CC, ");
		sql.append(" sum(DF) as DF ");
		sql.append(" from ( ");
		sql.append(" select  ");
		sql.append(" b.Area, ");
		sql.append(" b.People, ");
		sql.append(" pr.RecDate, ");
		sql.append(" pr.TPMCECPK, ");//-- 台電尖峰用電量
		sql.append(" pr.TPMCECSP, ");//-- 台電半尖峰用電量
		sql.append(" pr.TPMCECSatSP, ");//-- 台電周六半尖峰用電量
		sql.append(" pr.TPMCECOP, ");//-- 台電離峰用電量
		sql.append(" pr.MCECPK, ");//-- ECO5尖峰用電量
		sql.append(" pr.MCECSP, ");//-- ECO5半尖峰用電量
		sql.append(" pr.MCECSatSP, ");//-- ECO5周六半尖峰用電量
		sql.append(" pr.MCECOP, ");//-- ECO5離峰用電量
		sql.append(" greatest(pr.MDemandPK, pr.MDemandSP, pr.MDemandSatSP, pr.MDemandOP) as Demand, ");
		sql.append(" ifnull(pah.UsuallyCC,0)+ifnull(pah.SPCC,0)+ifnull(pah.SatSPCC,0)+ifnull(pah.OPCC,0) as CC, ");
		sql.append(" pr.Mode1 as DF ");
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa  ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount  ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) , ");
		sql.append(" MeterSetup ms , ");
		sql.append(" PowerRecordCollection pr ");
		sql.append(" inner join (select a.DeviceID, max(a.RecDate) as MaxRecDate from PowerRecordCollection a  ");
		sql.append(" where 1=1  ");
		if(StringUtils.isNotBlank(meterSetupVO.getStartDate()) || StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
			if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {
				sql.append(" and a.RecDate >= STR_TO_DATE(?,'%Y%m') ");				
				parameterList.add(meterSetupVO.getStartDate());
			}
			if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
				sql.append(" and a.RecDate < STR_TO_DATE(?,'%Y%m') ");			
				parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getEndDate()));
			}
		}
		sql.append(" group by a.DeviceID, date_format(a.RecDate,'%Y%m')) c    ");
		sql.append(" on pr.DeviceID = c.DeviceID    ");
		sql.append(" and pr.RecDate = c.MaxRecDate ");
		sql.append(" where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PATypeCode <= 10 ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.DeviceID = pr.DeviceID ");
		sql.append("   and ms.Enabled = 1  ");
		if(StringUtils.isNotBlank(meterSetupVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(meterSetupVO.getBankCode());	
		}
		if(StringUtils.isNotBlank(meterSetupVO.getDeviceId())) {
			sql.append(" and ms.DeviceID = ? ");
			parameterList.add(meterSetupVO.getDeviceId());	
		}else {
			sql.append(" and ms.UsageCode = 1 ");
		}
		
		sql.append(" ) a ");
		sql.append(" group by DATE_FORMAT(RecDate, '%Y%m') ");
		sql.append(" order by RecDate ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	
	/**
	 * 電表報表(Header)
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterReportHeader(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" ms.MeterName, ");
		sql.append(" (select rp.RatePlanDesc from RatePlanList rp where rp.RatePlanCode = fc.RealPlan) as RatePlanDesc, ");//-- 用電類型
		sql.append(" (ifnull(ms.UsuallyCC,0)+ifnull(ms.SPCC,0)+ifnull(ms.SatSPCC,0)+ifnull(ms.OPCC,0)) as CC, ");//-- 契約容量
		sql.append(" ms.PowerAccount, ");//-- 電號
		sql.append(" pr.TPMCECPK, ");//-- 台電尖峰用電量
		sql.append(" pr.TPMCECSP, ");//-- 台電半尖峰用電量
		sql.append(" pr.TPMCECSatSP, ");//-- 台電週六半尖峰用電量
		sql.append(" pr.TPMCECOP, ");//-- 台電離峰用電量
		sql.append(" (ifnull(pr.TPMCECPK,0)+ifnull(pr.TPMCECSP,0)+ifnull(pr.TPMCECSatSP,0)+ifnull(pr.TPMCECOP,0)) as TPCECSum, ");
		sql.append(" pr.MCECPK, ");//-- 尖峰用電量
		sql.append(" pr.MCECSP, ");//-- 半尖峰用電量
		sql.append(" pr.MCECSatSP, ");//-- 週六半尖峰用電量
		sql.append(" pr.MCECOP, ");//-- 離峰用電量
		sql.append(" (ifnull(pr.MCECPK,0)+ifnull(pr.MCECSP,0)+ifnull(pr.MCECSatSP,0)+ifnull(pr.MCECOP,0)) as CECSum, ");
		sql.append(" fc.MCEC, ");//-- 目前總用電量
		sql.append(" fc.TotalCharge, ");//-- 目前電費
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People, ");//-- 員工數
		sql.append(" GREATEST(pr.TPMDemandPK,pr.TPMDemandSP,pr.TPMDemandSatSP,pr.TPMDemandOP) as MaxDemand ");//-- 最大需量
		sql.append(" from BankInf b, ");
		sql.append("      PowerAccount pa ");
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = ?  ");
		if(StringUtils.isNotBlank(meterSetupVO.getDate())) {
			parameterList.add(meterSetupVO.getDate());	
		}else {
			parameterList.add(new SimpleDateFormat("yyyyMM").format(new Date()));
		}
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount,  ");

		sql.append("      MeterSetup ms ");
		sql.append(" 	left join PowerRecordCollection pr on pr.DeviceID = ms.DeviceID  ");
		sql.append("            and pr.RecDate = (select max(c.RecDate) as maxRecDate from PowerRecordCollection c  ");
		sql.append(" 							 where c.DeviceID = pr.DeviceID  ");
		if(StringUtils.isNotBlank(meterSetupVO.getDate())) {
			sql.append(" and c.RecDate >= STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(meterSetupVO.getDate());
			sql.append(" and c.RecDate < STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getDate()));		
		}
		sql.append("                              ) ");
		sql.append("      where b.BankCode = pa.BankCode ");
		sql.append("      and pa.PowerAccount = ms.PowerAccount ");		
		sql.append(" and ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());	
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電表報表(Detail)
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterReportDetail(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" pr.RecDate, ");
		sql.append(" pr.Wmax, ");//-- 最大功率
		sql.append(" pr.Vmax, ");//-- 最大相電壓
		sql.append(" pr.VmaxP, ");//-- 最大線電壓
		sql.append(" pr.Imax, ");//-- 最大電流
		sql.append(" pr.TPDCECPK, ");//-- 台電尖峰用電量
		sql.append(" pr.TPDCECSP, ");//-- 台電半尖峰用電量
		sql.append(" pr.TPDCECSatSP, ");//-- 台電週六半尖峰用電量
		sql.append(" pr.TPDCECOP,  ");//-- 台電離峰用電量
		sql.append(" (ifnull(pr.TPDCECPK,0)+ifnull(pr.TPDCECSP,0)+ifnull(pr.TPDCECSatSP,0)+ifnull(pr.TPDCECOP,0)) as TPCECSum, ");
		sql.append(" pr.DCECPK, ");//-- 尖峰用電量
		sql.append(" pr.DCECSP, ");//-- 半尖峰用電量
		sql.append(" pr.DCECSatSP, ");//-- 週六半尖峰用電量
		sql.append(" pr.DCECOP,  ");//-- 離峰用電量
		sql.append(" (ifnull(pr.DCECPK,0)+ifnull(pr.DCECSP,0)+ifnull(pr.DCECSatSP,0)+ifnull(pr.DCECOP,0)) as CECSum, ");
		sql.append(" pr.KWh ");//-- 電表值
		sql.append(" from PowerRecordCollection pr ");
		sql.append(" where 1=1 ");
		sql.append(" and pr.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		if(StringUtils.isNotBlank(meterSetupVO.getDate())) {
			sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(meterSetupVO.getDate());
			sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getDate()));
		}
		sql.append(" order by pr.RecDate ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電表總覽
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterOverview(MeterSetupVO meterSetupVO) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" p.City, ");//-- 縣市
		sql.append(" p.Dist, ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代號
		sql.append(" b.BankName, ");//-- 分行名稱
		sql.append(" pa.PowerAccount, ");//-- 電號
		sql.append(" (select count(1) from ControllerSetup cs where cs.BankCode = b.BankCode) as ECO5Count, ");
		sql.append(" ms.ECO5Account, ");
		sql.append(" ms.MeterName, ");//-- 電表名稱
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");//-- 耗能分類
		sql.append(" ms.DeviceID, ");//-- DeviceID
		sql.append(" ms.Enabled, ");//-- 電表啟用狀態
		sql.append(" ms.ECO5Enabled, ");//-- ECO5啟用狀態
		sql.append(" pr.VavgP, ");//-- 電壓
		sql.append(" pr.Iavg, ");//-- 電流
		sql.append(" pr.W, ");//-- 功率
		sql.append(" pr.Var, ");//-- 虛功
		sql.append(" pr.VA, ");//-- 視在
		sql.append(" pr.PF, ");//-- 功因
		sql.append(" pr.Hz, ");//-- 頻率
		sql.append(" pr.Mode1, ");//-- 需量預測
		sql.append(" pr.RecTime, ");//-- 最新資料時間
		sql.append(" pr.CreateTime ");//-- 最新連線時間
		sql.append(" from PostCode p,         ");
		sql.append("      BankInf b ");
		sql.append("      left join (select ac.* from  PowerAccount ac, PowerAccountHistory pah  ");
		sql.append(" 				where pah.PowerAccount = ac.PowerAccount  ");
		sql.append(" 		              and pah.ApplyDate =  ");
		sql.append(" 						(select max(a.ApplyDate) from PowerAccountHistory a  ");
		sql.append(" 						where a.PowerAccount = ac.PowerAccount and a.ApplyDate < now()) ");
		sql.append(" 						and ac.PATypeCode <= 10 ");
		sql.append(" 		        ) pa ");
		sql.append(" 		on pa.BankCode = b.BankCode ");
		sql.append(" 	 left join (select m.*, c.Enabled as ECO5Enabled from MeterSetup m, ControllerSetup c where m.ECO5Account = c.ECO5Account ) ms ");
		sql.append(" 		on ms.PowerAccount = pa.PowerAccount      ");
		sql.append(" 	 left join (select a.* from PowerRecord a  ");
		sql.append("     inner join (select c.DeviceID, max(c.RecTime) as maxRecTime from PowerRecord c where 1=1 group by c.DeviceID) b ");
		sql.append(" 		on a.DeviceID = b.DeviceID ");
		sql.append(" 		and a.RecTime = b.maxRecTime ");
//		sql.append("        and a.RecTime >= DATE_SUB(now(), INTERVAL 5 MINUTE) ");
		sql.append(" 		 ) pr  ");
		sql.append(" 		 on pr.DeviceID = ms.DeviceID ");
		sql.append(" where b.PostCodeNo = p.seqno    ");
		if (StringUtils.isNotBlank(meterSetupVO.getCityArr())) {
			sql.append(" and p.City in (" + meterSetupVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(meterSetupVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + meterSetupVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(meterSetupVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + meterSetupVO.getBankCodeArr() + ") ");
		}
		if (StringUtils.isNotBlank(meterSetupVO.getUsageCodeArr())) {
			sql.append(" and ms.UsageCode in (" + meterSetupVO.getUsageCodeArr() + ") ");
		}
		sql.append("    order by p.City, p.Dist, b.BankCode, pa.PowerAccount, ms.ECO5Account, ms.DeviceID ");
		
		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 電表電費
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterCharge(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select   ");
		sql.append(" ms.UsageCode, ");
		sql.append(" pr.RecDate, ");
		sql.append(" fc.TPMCEC, ");//-- 電號總用電量
		sql.append(" fc.BaseCharge, ");//-- 基本電費
		sql.append(" fc.UsageCharge, ");//-- 流動電費
		sql.append(" fc.OverCharge, ");//-- 非約定電費
		sql.append(" fc.TotalCharge, ");//-- 總電費
		sql.append(" (ifnull(ms.UsuallyCC,0)+ifnull(ms.SPCC,0)+ifnull(ms.SatSPCC,0)+ifnull(ms.OPCC,0)) as CC, ");//-- 契約容量
		sql.append(" greatest(pr.TPMDemandPK, pr.TPMDemandSP, pr.TPMDemandSatSP, pr.TPMDemandOP) as MDemand,  ");//-- 最大需量
		sql.append(" pr.TPMCECPK, ");//-- 尖峰用電量(電表)
		sql.append(" pr.TPMCECSP, ");//-- 半尖峰用電量(電表)
		sql.append(" pr.TPMCECSatSP,   ");//-- 周六半尖峰用電量(電表)
		sql.append(" pr.TPMCECOP, ");//-- 離峰用電量(電表)
		sql.append(" pr.CEC ");//-- 總用電量(電表)
		sql.append(" from MeterSetup ms, ");
		sql.append("      PowerRecordCollection pr    ");
		sql.append("      inner join (select a.DeviceID, max(a.RecDate) as MaxRecDate from PowerRecordCollection a    ");
		sql.append("                  where 1=1    ");
		if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {
			sql.append(" and a.RecDate >= STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(meterSetupVO.getStartDate());
		}
		if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
			sql.append(" and a.RecDate < STR_TO_DATE(?,'%Y%m') ");
			parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getEndDate()));
		}
		sql.append("                  group by a.DeviceID, date_format(a.RecDate,'%Y%m')) c   ");
		sql.append(" 	 on pr.DeviceID = c.DeviceID   ");
		sql.append("      and pr.RecDate = c.MaxRecDate,  ");
		sql.append("      FcstCharge fc  ");
		sql.append("      inner join (select a.PowerAccount ,max(a.useTime) as maxUseTime from FcstCharge a  ");
		sql.append("                  where 1=1   ");
		if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {
			sql.append(" and a.useMonth >= ?   ");
			parameterList.add(meterSetupVO.getStartDate());
		}
		if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
			sql.append(" and a.useMonth <= ?  ");
			parameterList.add(meterSetupVO.getEndDate());
		}
		sql.append("                  group by a.PowerAccount, a.useMonth  ) d   ");
		sql.append(" 	 on fc.PowerAccount = d.PowerAccount  ");
		sql.append("      and fc.useTime = d.maxUseTime  ");
		sql.append("      where  ms.DeviceID = pr.DeviceID   ");
		sql.append("      and ms.PowerAccount = fc.PowerAccount   ");
		sql.append(" 	 and fc.useMonth = date_format(pr.RecDate,'%Y%m') ");
		sql.append("      and ms.Enabled = 1 ");
		sql.append("      and ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		sql.append(" order by fc.useTime  ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得目前最大需量
	 * @param powerAccountVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDemandNow(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
				
		sql.append(" select GREATEST(max(r.DemandPK),MAX(r.DemandSP),MAX(r.DemandSatSP),MAX(r.DemandOP)) as DemandNow  ");
		sql.append(" from MeterSetup ms USE INDEX (PowerAccount), PowerRecord r use index(`PowerRecord_UDX`) ");
		sql.append(" where ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());	
		sql.append(" and r.DeviceID = ms.DeviceID  ");
		sql.append(" and r.RecTime >= DATE_SUB(now(), INTERVAL 15 MINUTE) ");
		sql.append(" and r.RecTime < now() ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得目前契約容量
	 * @param meterSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getCC(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" sum(ifnull(c.UsuallyCC,0)+ifnull(c.SPCC,0)+ifnull(c.SatSPCC,0)+ifnull(c.OPCC,0)) as CC ");
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa ");
		if(StringUtils.isNotBlank(meterSetupVO.getStartDate())||StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
			sql.append(" left join FcstCharge c on c.PowerAccount = pa.PowerAccount   ");
			sql.append(" and c.seqno = (select max(a.seqno) from FcstCharge a where a.PowerAccount = pa.PowerAccount ");
			if("0".equals(meterSetupVO.getRecType())) {//日
				
				if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {
					sql.append(" and a.useTime >= STR_TO_DATE(?,'%Y%m%d')  ");
					parameterList.add(meterSetupVO.getStartDate());
				}
				if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
					sql.append(" and a.useTime < STR_TO_DATE(?,'%Y%m%d') ");
					parameterList.add(ToolUtil.getNextDay(meterSetupVO.getEndDate()));
				}	
			}else if("1".equals(meterSetupVO.getRecType())) {//月
				
				if(StringUtils.isNotBlank(meterSetupVO.getStartDate())) {
					sql.append(" and a.useTime >= STR_TO_DATE(?,'%Y%m')  ");
					parameterList.add(meterSetupVO.getStartDate());
				}
				if(StringUtils.isNotBlank(meterSetupVO.getEndDate())) {
					sql.append(" and a.useTime < STR_TO_DATE(?,'%Y%m') ");
					parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getEndDate()));
				}
			}
			sql.append(" ) ");	
		}else if(StringUtils.isNotBlank(meterSetupVO.getDate()) && !ToolUtil.isThisMonth(meterSetupVO.getDate())) {
			sql.append(" left join FcstCharge c on c.PowerAccount = pa.PowerAccount   ");
			sql.append(" and c.seqno = (select max(a.seqno) from FcstCharge a where a.PowerAccount = pa.PowerAccount and a.useMonth = ?) ");
			parameterList.add(meterSetupVO.getDate());
		}else {
			sql.append(" left join PowerAccountHistory c on c.PowerAccount = pa.PowerAccount ");
			sql.append(" and c.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) ");			
		}
		sql.append(" where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PATypeCode <= 10 ");
		if(StringUtils.isNotBlank(meterSetupVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(meterSetupVO.getBankCode());
		}
		if(StringUtils.isNotBlank(meterSetupVO.getDeviceId())) {
			sql.append(" and pa.PowerAccount in ( ");
			sql.append(" select ms.PowerAccount from MeterSetup ms ");
			sql.append(" where ms.Enabled = 1 ");
			sql.append(" and ms.DeviceID = ? ");
			sql.append(" ) ");
			parameterList.add(meterSetupVO.getDeviceId());
		}
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電表明細
	 * @param meterSetupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterElectricity(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People, ");//-- 員工數
		sql.append(" pr.MCECPK, ");
		sql.append(" pr.MCECSP, ");
		sql.append(" pr.MCECSatSP, ");
		sql.append(" pr.MCECOP, ");
		sql.append(" pr.CEC, ");//-- 目前用電量
		sql.append(" pr.FcstECO5MCEC, ");//-- 預測用電量
		sql.append(" fc.MCEC, ");//-- 目前總用電量
		sql.append(" fc.TotalCharge,  ");//-- 目前電費
		sql.append(" fc.FcstMCEC, ");//-- 預測總用電量
		sql.append(" fc.FcstTotalCharge,  ");//-- 預測電費
		sql.append(" GREATEST(pr.TPMDemandPK,pr.TPMDemandSP,pr.TPMDemandSatSP,pr.TPMDemandOP) as MaxDemand ");//-- 最高需量
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa ");
		
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = ?  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount,  ");
		
		parameterList.add(meterSetupVO.getDate());
		sql.append(" MeterSetup ms ");
		sql.append(" left join PowerRecordCollection pr on pr.DeviceID = ms.DeviceID  ");
		sql.append(" and pr.RecDate = (select max(c.RecDate) as maxRecDate from PowerRecordCollection c where c.DeviceID = pr.DeviceID ");
		sql.append(" and c.RecDate >= STR_TO_DATE(?,'%Y%m') ");
		parameterList.add(meterSetupVO.getDate());			
		sql.append(" and c.RecDate < STR_TO_DATE(?,'%Y%m') ");				
		parameterList.add(ToolUtil.getNextMonth(meterSetupVO.getDate()));		
		sql.append(" ) ");
		sql.append(" where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得當天最大需量
	 * @param meterSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDemandToday(MeterSetupVO meterSetupVO) throws Exception {
		String thisDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		
		sql.append(" select GREATEST(max(r.DemandPK),MAX(r.DemandSP),MAX(r.DemandSatSP),MAX(r.DemandOP)) as DemandToday  ");
		sql.append(" from MeterSetup ms USE INDEX (PowerAccount), PowerRecord r use index(`PowerRecord_UDX`) ");
		sql.append(" where ");
		sql.append(" ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());
		sql.append(" and ms.Enabled = 1 ");
		sql.append(" and ms.DeviceID = r.DeviceID  ");		
		sql.append(" and r.RecTime >= STR_TO_DATE(?,'%Y%m%d') ");
		sql.append(" and r.RecTime < STR_TO_DATE(?,'%Y%m%d') ");		
		parameterList.add(thisDay);
		parameterList.add(ToolUtil.getNextDay(thisDay));

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 當月最大需量(統計到系統日前一天)
	 * @param meterSetupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDemandMonth(MeterSetupVO meterSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String thisMonth = sdf.format(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");		
		sql.append(" GREATEST(pr.MDemandPK, pr.MDemandSP, pr.MDemandSatSP, pr.MDemandOP) as DemandMonth ");
		sql.append(" from  MeterSetup ms ");
		sql.append(" left join PowerRecordCollection pr on pr.DeviceID = ms.DeviceID  ");
		sql.append(" and pr.RecDate = (select max(c.RecDate) as maxRecDate from PowerRecordCollection c where c.DeviceID = pr.DeviceID ");
		sql.append(" and c.RecDate >= STR_TO_DATE(?,'%Y%m') ");
		parameterList.add(thisMonth);			
		sql.append(" and c.RecDate < STR_TO_DATE(?,'%Y%m') ");
		parameterList.add(ToolUtil.getNextMonth(thisMonth));		
		sql.append(" ) ");
		sql.append(" where  ms.DeviceID = ? ");
		parameterList.add(meterSetupVO.getDeviceId());

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢電表資訊
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMeterInfo(String deviceId) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");		
		sql.append(" * ");
		sql.append(" from  MeterSetup  ");
		sql.append(" where DeviceID = ? ");
		parameterList.add(deviceId);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核MeterId是否存在
	 * @param eco5Account
	 * @param meterId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> checkMeterId(String eco5Account, String meterId) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");		
		sql.append(" 1 ");
		sql.append(" from  MeterSetup  ");
		sql.append(" where ECO5Account = ? ");
		sql.append(" and MeterID = ? ");
		parameterList.add(eco5Account);
		parameterList.add(meterId);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核電號仍有關聯的電表
	 * @param eco5Account
	 * @param meterId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> checkPowerAccount(String powerAccount) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");		
		sql.append(" MeterName ");
		sql.append(" from  MeterSetup  ");
		sql.append(" where PowerAccount = ? ");
		parameterList.add(powerAccount);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 重新統計電表
	 * @param meterSetupVO
	 * @throws Exception
	 */
	public void repriceMeter(MeterSetupVO meterSetupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
					
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update PowerAccount   ");
			sql1.append(" set ModifyStatus = 3, ");
			sql1.append(" UpdateUserName = ? ");
			sql1.append(" where PowerAccount = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, meterSetupVO.getUserName());
			ps1.setString(2, meterSetupVO.getPowerAccount());
			ps1.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();					
			sql2.append(" update MeterSetup   ");
			sql2.append(" set RepriceStatus = 1, ");
			sql2.append(" UpdateUserName = ? ");
			sql2.append(" where DeviceID = ? ");
			ps2 = connection.prepareStatement(sql2.toString());		
			ps2.setString(1, meterSetupVO.getUserName());
			ps2.setString(2, meterSetupVO.getDeviceId());		
			ps2.executeUpdate();
			
			
			StringBuffer sql3 = new StringBuffer();					
			sql3.append(" INSERT INTO RepriceTask ( ");
			sql3.append(" DeviceID, ");
			sql3.append(" PowerAccount, ");
			sql3.append(" RepriceFrom, ");
			sql3.append(" StartDate, ");
			sql3.append(" PowerPhaseNew, ");
			sql3.append(" ApplyDateNew, ");
			sql3.append(" RatePlanCodeNew, ");
			sql3.append(" UsuallyCCNew, ");
			sql3.append(" SPCCNew, ");
			sql3.append(" SatSPCCNew, ");
			sql3.append(" OPCCNew, ");
			sql3.append(" CreateUserName, ");
			sql3.append(" UpdateUserName ");
			sql3.append(" ) VALUES ( ");
			sql3.append(" ?, ?, 'Collection', ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
			sql3.append(" ) ");
			
			ps3 = connection.prepareStatement(sql3.toString());		
			ps3.setString(1, meterSetupVO.getDeviceId());
			ps3.setString(2, meterSetupVO.getPowerAccount());	
			ps3.setString(3, meterSetupVO.getRepriceDate());
			ps3.setString(4, meterSetupVO.getPowerPhaseNew());	
			ps3.setString(5, meterSetupVO.getApplyDateNew());
			ps3.setString(6, meterSetupVO.getRatePlanCodeNew());	
			ps3.setString(7, meterSetupVO.getUsuallyCCNew());
			ps3.setString(8, meterSetupVO.getSpccNew());	
			ps3.setString(9, meterSetupVO.getSatspccNew());
			ps3.setString(10, meterSetupVO.getOpccNew());	
			ps3.setString(11, meterSetupVO.getUserName());
			ps3.setString(12, meterSetupVO.getUserName());	
			ps3.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(connection);
		}
	}
}
