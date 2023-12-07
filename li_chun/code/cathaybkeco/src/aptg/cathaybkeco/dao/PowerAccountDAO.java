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
import aptg.cathaybkeco.vo.PowerAccountVO;

public class PowerAccountDAO extends BaseDAO {

	/**
	 * 取得電號資訊
	 * 
	 * @param powerAccountVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getPowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" p.PowerAccount,  ");
		sql.append(" p.CustomerName,  ");
		sql.append(" p.AccountDesc, ");
		sql.append(" (select pa.PATypeName from PATypeList pa where pa.PATypeCode = p.PATypeCode) as PATypeName, ");
		sql.append(" p.PATypeCode, ");
		sql.append(" (case p.PowerPhase when 1 then '單相' when 3 then '三相' end) as PowerPhaseDesc, ");
		sql.append(" p.PowerPhase, ");
		sql.append(" p.PAAddress, ");
		sql.append(" ph.ApplyDate, ");
		sql.append(" (select RatePlanDesc from RatePlanList l where l.RatePlanCode = ph.RatePlanCode ) as RatePlanDesc,  ");
		sql.append(" ph.RatePlanCode, ");
		sql.append(" ph.UsuallyCC,  ");
		sql.append(" ph.SPCC,  ");
		sql.append(" ph.SatSPCC,  ");
		sql.append(" ph.OPCC,  ");
		sql.append(" (case p.ModifyStatus when 1 then '刪除中' when 2 then '電號異動中' when 3 then '電費重新計算中' else '一般狀態' end) as ModifyStatusDesc, ");
		sql.append(" p.ModifyStatus ");
		sql.append(" from PowerAccount p  ");
		sql.append(" left join PowerAccountHistory ph on ph.PowerAccount = p.PowerAccount ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(powerAccountVO.getPowerAccount())) {
			sql.append(" and p.PowerAccount = ? ");
			parameterList.add(powerAccountVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(powerAccountVO.getBankCode())) {
			sql.append(" and p.BankCode = ? ");
			parameterList.add(powerAccountVO.getBankCode());
		}
		if(StringUtils.isNotBlank(powerAccountVO.getApplyDate())) {
			sql.append(" and ph.ApplyDate = ? ");
			parameterList.add(powerAccountVO.getApplyDate());
		}
		
		sql.append(" order by p.PowerAccount,  ph.ApplyDate desc ");
		
		

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 新增電號資訊
	 * 
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public void addPowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
					
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" INSERT INTO PowerAccount ( ");
			sql1.append(" PowerAccount, ");
			sql1.append(" BankCode, ");
			sql1.append(" CustomerName, ");
			sql1.append(" AccountDesc, ");
			sql1.append(" PATypeCode, ");
			sql1.append(" PowerPhase, ");
			sql1.append(" PAAddress, ");
			sql1.append(" CreateUserName, ");
			sql1.append(" UpdateUserName ");
			sql1.append(" )VALUES(?,?,?,?,?,?,?,?,?) ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, powerAccountVO.getPowerAccount());
			ps1.setString(2, powerAccountVO.getBankCode());
			ps1.setString(3, powerAccountVO.getCustomerName());
			ps1.setString(4, powerAccountVO.getAccountDesc());
			ps1.setString(5, powerAccountVO.getPaTypeCode());
			ps1.setString(6, powerAccountVO.getPowerPhase());
			ps1.setString(7, powerAccountVO.getPaAddress());
			ps1.setString(8, powerAccountVO.getUserName());
			ps1.setString(9, powerAccountVO.getUserName());
			ps1.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();					
			sql2.append(" INSERT INTO PowerAccountHistory ( ");
			sql2.append(" PowerAccount, ");
			sql2.append(" ApplyDate, ");
			sql2.append(" RatePlanCode, ");
			sql2.append(" UsuallyCC, ");
			sql2.append(" SPCC, ");
			sql2.append(" SatSPCC, ");
			sql2.append(" OPCC, ");
			sql2.append(" CreateUserName, ");
			sql2.append(" UpdateUserName ");
			sql2.append(" )VALUES(?,?,?,?,?,?,?,?,?) ");
			ps2 = connection.prepareStatement(sql2.toString());					
			ps2.setString(1, powerAccountVO.getPowerAccount());
			ps2.setString(2, powerAccountVO.getApplyDate());
			ps2.setString(3, powerAccountVO.getRatePlanCode());
			ps2.setString(4, powerAccountVO.getUsuallyCC());
			ps2.setString(5, powerAccountVO.getSpCC());
			ps2.setString(6, powerAccountVO.getSatSPCC());
			ps2.setString(7, powerAccountVO.getOpCC());
			ps2.setString(8, powerAccountVO.getUserName());
			ps2.setString(9, powerAccountVO.getUserName());
			ps2.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}

	/**
	 * 修改電號資訊
	 * 
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public void updPowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			int i=1;
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" UPDATE PowerAccount ");
			sql1.append(" SET ");
			sql1.append(" CustomerName = ?, ");
			sql1.append(" AccountDesc = ?, ");
			sql1.append(" PATypeCode = ?, ");
			sql1.append(" PowerPhase = ?, ");
			sql1.append(" PAAddress = ?, ");
			if("3".equals(powerAccountVO.getModifyStatus())) {
				sql1.append(" ModifyStatus = ?, ");
			}
			sql1.append(" UpdateUserName = ? ");
			sql1.append(" WHERE BankCode = ? ");
			sql1.append(" and PowerAccount = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(i++, powerAccountVO.getCustomerName());
			ps1.setString(i++, powerAccountVO.getAccountDesc());
			ps1.setString(i++, powerAccountVO.getPaTypeCode());
			ps1.setString(i++, powerAccountVO.getPowerPhase());
			ps1.setString(i++, powerAccountVO.getPaAddress());
			if("3".equals(powerAccountVO.getModifyStatus())) {
				ps1.setString(i++, powerAccountVO.getModifyStatus());
			}
			ps1.setString(i++, powerAccountVO.getUserName());
			ps1.setString(i++, powerAccountVO.getBankCode());
			ps1.setString(i++, powerAccountVO.getPowerAccount());
			ps1.executeUpdate();

			if(powerAccountVO.isAddFlag()) {
				StringBuffer sql2 = new StringBuffer();
				sql2.append(" INSERT INTO PowerAccountHistory ( ");
				sql2.append(" PowerAccount, ");
				sql2.append(" ApplyDate, ");
				sql2.append(" RatePlanCode, ");
				sql2.append(" UsuallyCC, ");
				sql2.append(" SPCC, ");
				sql2.append(" SatSPCC, ");
				sql2.append(" OPCC, ");
				sql2.append(" CreateUserName, ");
				sql2.append(" UpdateUserName ");
				sql2.append(" )VALUES(?,?,?,?,?,?,?,?,?) ");
				ps2 = connection.prepareStatement(sql2.toString());
				ps2.setString(1, powerAccountVO.getPowerAccount());
				ps2.setString(2, powerAccountVO.getApplyDate());
				ps2.setString(3, powerAccountVO.getRatePlanCode());
				ps2.setString(4, powerAccountVO.getUsuallyCC());
				ps2.setString(5, powerAccountVO.getSpCC());
				ps2.setString(6, powerAccountVO.getSatSPCC());
				ps2.setString(7, powerAccountVO.getOpCC());
				ps2.setString(8, powerAccountVO.getUserName());
				ps2.setString(9, powerAccountVO.getUserName());
				ps2.executeUpdate();
				
				if("3".equals(powerAccountVO.getModifyStatus())) {
					StringBuffer sql3 = new StringBuffer();					
					sql3.append(" INSERT INTO RepriceTask ( ");
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
					sql3.append(" ?, 'Collection', ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
					sql3.append(" ) ");				
					ps3 = connection.prepareStatement(sql3.toString());		
					ps3.setString(1, powerAccountVO.getPowerAccount());	
					ps3.setString(2, powerAccountVO.getApplyDate());
					ps3.setString(3, powerAccountVO.getPowerPhase());	
					ps3.setString(4, powerAccountVO.getApplyDate());
					ps3.setString(5, powerAccountVO.getRatePlanCode());	
					ps3.setString(6, powerAccountVO.getUsuallyCC());
					ps3.setString(7, powerAccountVO.getSpCC());	
					ps3.setString(8, powerAccountVO.getSatSPCC());
					ps3.setString(9, powerAccountVO.getOpCC());	
					ps3.setString(10, powerAccountVO.getUserName());
					ps3.setString(11, powerAccountVO.getUserName());	
					ps3.executeUpdate();
				}
			}else {			
				StringBuffer sql2 = new StringBuffer();
				sql2.append(" UPDATE PowerAccountHistory ");
				sql2.append(" SET ");
				sql2.append(" ApplyDate = ?, ");
				sql2.append(" RatePlanCode = ?, ");
				sql2.append(" UsuallyCC = ?, ");
				sql2.append(" SPCC = ?, ");
				sql2.append(" SatSPCC = ?, ");
				sql2.append(" OPCC = ?, ");
				sql2.append(" UpdateUserName = ? ");
				sql2.append(" WHERE PowerAccount = ? ");
				sql2.append(" and ApplyDate = ? ");
				ps2 = connection.prepareStatement(sql2.toString());
				ps2.setString(1, powerAccountVO.getApplyDate());
				ps2.setString(2, powerAccountVO.getRatePlanCode());
				ps2.setString(3, powerAccountVO.getUsuallyCC());
				ps2.setString(4, powerAccountVO.getSpCC());
				ps2.setString(5, powerAccountVO.getSatSPCC());
				ps2.setString(6, powerAccountVO.getOpCC());
				ps2.setString(7, powerAccountVO.getUserName());
				ps2.setString(8, powerAccountVO.getPowerAccount());
				ps2.setString(9, powerAccountVO.getApplyDateOld());
				ps2.executeUpdate();
				
				if("3".equals(powerAccountVO.getModifyStatus())) {
					StringBuffer sql3 = new StringBuffer();					
					sql3.append(" INSERT INTO RepriceTask ( ");
					sql3.append(" PowerAccount, ");
					sql3.append(" RepriceFrom, ");
					sql3.append(" StartDate, ");
					sql3.append(" PowerPhaseOld, ");
					sql3.append(" ApplyDateOld, ");
					sql3.append(" RatePlanCodeOld, ");
					sql3.append(" UsuallyCCOld, ");
					sql3.append(" SPCCOld, ");
					sql3.append(" SatSPCCOld, ");
					sql3.append(" OPCCOld, ");
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
					sql3.append(" ?, 'Collection', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
					sql3.append(" ) ");				
					ps3 = connection.prepareStatement(sql3.toString());		
					ps3.setString(1, powerAccountVO.getPowerAccount());	
					ps3.setString(2, powerAccountVO.getRepriceDate());
					ps3.setString(3, powerAccountVO.getPowerPhaseOld());	
					ps3.setString(4, powerAccountVO.getApplyDateOld());
					ps3.setString(5, powerAccountVO.getRatePlanCodeOld());	
					ps3.setString(6, powerAccountVO.getUsuallyCCOld());
					ps3.setString(7, powerAccountVO.getSpCCOld());	
					ps3.setString(8, powerAccountVO.getSatSPCCOld());
					ps3.setString(9, powerAccountVO.getOpCCOld());	
					ps3.setString(10, powerAccountVO.getPowerPhaseNew());	
					ps3.setString(11, powerAccountVO.getApplyDateNew());
					ps3.setString(12, powerAccountVO.getRatePlanCodeNew());	
					ps3.setString(13, powerAccountVO.getUsuallyCCNew());
					ps3.setString(14, powerAccountVO.getSpCCNew());	
					ps3.setString(15, powerAccountVO.getSatSPCCNew());
					ps3.setString(16, powerAccountVO.getOpCCNew());	
					ps3.setString(17, powerAccountVO.getUserName());
					ps3.setString(18, powerAccountVO.getUserName());	
					ps3.executeUpdate();
				}
			}
			
			//同步更新此電號下的電錶契約容量
			StringBuffer sql4 = new StringBuffer();					
			sql4.append(" UPDATE MeterSetup m ");
			sql4.append(" INNER JOIN PowerAccountHistory p  ");
			sql4.append(" ON p.PowerAccount = m.PowerAccount ");
			sql4.append(" AND p.ApplyDate = (SELECT MAX(a.ApplyDate) ");
			sql4.append(" FROM PowerAccountHistory a ");
			sql4.append(" WHERE a.PowerAccount = m.PowerAccount ");
			sql4.append(" AND a.ApplyDate < NOW())  ");
			sql4.append(" SET m.UsuallyCC = p.UsuallyCC, ");
			sql4.append(" m.SPCC = p.SPCC, ");
			sql4.append(" m.SatSPCC = p.SatSPCC, ");
			sql4.append(" m.OPCC = p.OPCC ");
			sql4.append(" WHERE m.PowerAccount = ? ");
			ps4 = connection.prepareStatement(sql4.toString());		
			ps4.setString(1, powerAccountVO.getPowerAccount());				
			ps4.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 檢核電號
	 * 
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public List<DynaBean> checkPowerAccount(String powerAccount) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select 1 ");
		sql.append(" from PowerAccount ");
		sql.append(" where 1=1 ");
		sql.append(" and PowerAccount = ? ");
		parameterList.add(powerAccount);
//		if (StringUtils.isNotBlank(powerAccountVO.getBankCode())) {
//			sql.append(" and BankCode = ? ");
//			parameterList.add(powerAccountVO.getBankCode());
//		}
		sql.append(" order by PowerAccount ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核電號生效日
	 * 
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public List<DynaBean> checkApplyDate(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * ");
		sql.append(" from PowerAccountHistory ");
		
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(powerAccountVO.getPowerAccount())) {
			sql.append(" and PowerAccount = ? ");
			parameterList.add(powerAccountVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(powerAccountVO.getApplyDate())) {
			sql.append(" and ApplyDate = ? ");
			parameterList.add(powerAccountVO.getApplyDate());
		}
		sql.append(" order by PowerAccount ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電號下拉選單
	 * 
	 * @param meterSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getPowerAccountList(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct PowerAccount  ");
		sql.append(" from PowerAccount ");
		sql.append(" where 1=1 ");		
		if (StringUtils.isNotBlank(powerAccountVO.getBankCode())) {
			sql.append(" and BankCode = ? ");
			parameterList.add(powerAccountVO.getBankCode());
		}
		sql.append(" and ModifyStatus <> 1 ");
		sql.append(" order by PowerAccount ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得分行電號下拉選單
	 * 
	 * @param meterSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBankPAList(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select p.PowerAccount, p.AccountDesc ");
		sql.append(" from PowerAccount p ");
		sql.append(" inner join PowerAccountHistory h on h.PowerAccount = p.PowerAccount ");
		sql.append(" and h.ApplyDate = ");
		sql.append(" (select max(a.ApplyDate) from PowerAccountHistory a ");
		sql.append(" where a.PowerAccount = p.PowerAccount and a.ApplyDate < now() ");
		sql.append(" ) ");
		sql.append(" where 1=1 ");		
		if (StringUtils.isNotBlank(powerAccountVO.getBankCode())) {
			sql.append(" and p.BankCode = ? ");
			parameterList.add(powerAccountVO.getBankCode());
		}
		sql.append(" and p.PATypeCode <= 10 ");
		sql.append(" order by p.PowerAccount ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得生效電號資訊
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getEffectivePowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" p.PowerAccount,  ");
		sql.append(" (select RatePlanDesc from RatePlanList l where l.RatePlanCode = ph.RatePlanCode ) as RatePlanDesc,  ");
		sql.append(" p.AccountDesc, ");
		sql.append(" (ifnull(ph.UsuallyCC,0)+ifnull(ph.SPCC,0)+ifnull(ph.SatSPCC,0)+ifnull(ph.OPCC,0)) as CC ");
		sql.append(" from PowerAccount p  ");
		sql.append(" left join PowerAccountHistory ph on ph.PowerAccount = p.PowerAccount ");
		sql.append(" and ph.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = p.PowerAccount and a.ApplyDate < now()) ");
		sql.append(" where 1=1 ");	
		sql.append(" and p.PATypeCode <= 10 ");
		if (StringUtils.isNotBlank(powerAccountVO.getBankCode())) {
			sql.append(" and p.BankCode = ? ");
			parameterList.add(powerAccountVO.getBankCode());
		}
		
		sql.append(" order by p.PowerAccount ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	
	
	/**
	 * 取得分行數據
	 * @param powerAccountVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBankElectricity(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" pa.BankCode, ");//-- 分行代碼
		sql.append(" sum(fc.MCECPK) as MCECPK, ");
		sql.append(" sum(fc.MCECSP) as MCECSP, ");
		sql.append(" sum(fc.MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(fc.MCECOP) as MCECOP, ");
		sql.append(" sum(fc.MCEC) as MCEC, ");//-- 目前總用電量
		sql.append(" sum(fc.FcstMCEC) as FcstMCEC, ");//-- 預估總用電量
		sql.append(" sum(fc.TotalCharge) as TotalCharge, ");//-- 目前電費
		sql.append(" sum(fc.FcstTotalCharge) as FcstTotalCharge ");//-- 預估電費
//		sql.append(" sum(GREATEST(fc.MDemandPK, fc.MDemandSP, fc.MDemandSatSP, fc.MDemandOP)) as MaxDemand ");//-- 最高需量
		sql.append(" from PowerAccount pa ");

		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = ?  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount  ");
		if(StringUtils.isNotBlank(powerAccountVO.getDate())) {
			parameterList.add(powerAccountVO.getDate());
		}else {
			parameterList.add(new SimpleDateFormat("yyyyMM").format(new Date()));
		}

		sql.append(" where pa.BankCode = ? ");
		parameterList.add(powerAccountVO.getBankCode());
		sql.append(" and pa.PATypeCode <= 10 ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	
	/**
	 * 取得目前最大需量
	 * @param powerAccountVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDemandNow(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select GREATEST(max(r.DemandPK),MAX(r.DemandSP),MAX(r.DemandSatSP),MAX(r.DemandOP)) as DemandNow  ");
		sql.append(" from PowerAccount pa USE INDEX (Bank) , MeterSetup ms USE INDEX (PowerAccount), PowerRecord r use index(`PowerRecord_UDX`) ");
		sql.append(" where ");
		sql.append(" pa.BankCode = ? ");
		parameterList.add(powerAccountVO.getBankCode());	
		sql.append(" and pa.PATypeCode <= 10 ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and r.DeviceID = ms.DeviceID  ");
		sql.append(" and ms.Enabled = 1 ");
		sql.append(" and ms.UsageCode = 1 ");
		sql.append(" and r.RecTime >= DATE_SUB(now(), INTERVAL 15 MINUTE) ");
		sql.append(" and r.RecTime < now() ");
//		sql.append(" group by ms.DeviceID ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得當天最大需量
	 * @param powerAccountVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDemandToday(PowerAccountVO powerAccountVO) throws Exception {
		String thisDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select GREATEST(max(r.DemandPK),MAX(r.DemandSP),MAX(r.DemandSatSP),MAX(r.DemandOP)) as DemandToday ");
		sql.append(" from PowerAccount pa USE INDEX (Bank) , MeterSetup ms USE INDEX (PowerAccount), PowerRecord r use index(`PowerRecord_UDX`) ");
		sql.append(" where ");
		sql.append(" pa.BankCode = ? ");
		parameterList.add(powerAccountVO.getBankCode());
		sql.append(" and pa.PATypeCode <= 10 ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and r.DeviceID = ms.DeviceID  ");
		sql.append(" and ms.Enabled = 1 ");
		sql.append(" and ms.UsageCode = 1 ");
		sql.append(" and r.RecTime >= STR_TO_DATE(? ,'%Y%m%d') ");
		sql.append(" and r.RecTime < STR_TO_DATE(? ,'%Y%m%d') ");
		parameterList.add(thisDay);
		parameterList.add(ToolUtil.getNextDay(thisDay));
		sql.append(" group by ms.DeviceID ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 當月最大需量(統計到系統日前一天)
	 * @param powerAccountVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDemandMonth(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();		
		sql.append(" select  ");
		sql.append(" sum(GREATEST(fc.MDemandPK, fc.MDemandSP, fc.MDemandSatSP, fc.MDemandOP)) as DemandMonth ");
		sql.append(" from PowerAccount pa ");
		sql.append(" left join FcstCharge fc on fc.PowerAccount = pa.PowerAccount  ");
		sql.append(" and fc.useTime = (select max(a.useTime) from FcstCharge a where a.PowerAccount = pa.PowerAccount and a.useMonth = ?)  ");
		parameterList.add(new SimpleDateFormat("yyyyMM").format(new Date()));
		sql.append(" where pa.BankCode = ? ");
		parameterList.add(powerAccountVO.getBankCode());
		sql.append(" and pa.PATypeCode <= 10 ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 刪除電號
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public void delPowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
					
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update PowerAccount   ");
			sql1.append(" set ModifyStatus = 1, ");
			sql1.append(" UpdateUserName = ? ");
			sql1.append(" where PowerAccount = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, powerAccountVO.getUserName());
			ps1.setString(2, powerAccountVO.getPowerAccount());
			ps1.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();					
			sql2.append(" INSERT INTO BGTask ( ");
			sql2.append(" BGTaskType, ");
			sql2.append(" PowerAccountOld, ");
			sql2.append(" CreateUserName, ");
			sql2.append(" UpdateUserName ");
			sql2.append(" ) VALUES ( ");
			sql2.append(" 'Del_PA', ?, ?, ? ");
			sql2.append(" ) ");
			ps2 = connection.prepareStatement(sql2.toString());		
			ps2.setString(1, powerAccountVO.getPowerAccount());
			ps2.setString(2, powerAccountVO.getUserName());
			ps2.setString(3, powerAccountVO.getUserName());
			ps2.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	
	/**
	 * 異動電號
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public void fixPowerAccount(PowerAccountVO powerAccountVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
					
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" update PowerAccount   ");
			sql1.append(" set ModifyStatus = 2, ");
			sql1.append(" UpdateUserName = ? ");
			sql1.append(" where PowerAccount = ? ");
			ps1 = connection.prepareStatement(sql1.toString());
			ps1.setString(1, powerAccountVO.getUserName());
			ps1.setString(2, powerAccountVO.getPowerAccountOld());
			ps1.executeUpdate();
			
			StringBuffer sql2 = new StringBuffer();					
			sql2.append(" INSERT INTO BGTask ( ");
			sql2.append(" BGTaskType, ");
			sql2.append(" PowerAccountOld, ");
			sql2.append(" PowerAccountNew, ");
			sql2.append(" CreateUserName, ");
			sql2.append(" UpdateUserName ");
			sql2.append(" ) VALUES ( ");
			sql2.append(" 'Fix_PA', ?, ?, ?, ? ");
			sql2.append(" ) ");
			ps2 = connection.prepareStatement(sql2.toString());		
			ps2.setString(1, powerAccountVO.getPowerAccountOld());
			ps2.setString(2, powerAccountVO.getPowerAccountNew());
			ps2.setString(3, powerAccountVO.getUserName());
			ps2.setString(4, powerAccountVO.getUserName());
			ps2.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());
		} finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps1);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 取得電號契約容量
	 * @param powerAccount
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPowerAccountCC(String powerAccount) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select p.PowerAccount, p.PowerPhase, p.AccountDesc, ");
		sql.append(" h.ApplyDate, h.RatePlanCode, h.UsuallyCC, h.SPCC, h.SatSPCC, h.OPCC ");
		sql.append(" from PowerAccount p ");
		sql.append(" inner join PowerAccountHistory h on h.PowerAccount = p.PowerAccount ");
		sql.append(" and h.ApplyDate = ");
		sql.append(" (select max(a.ApplyDate) from PowerAccountHistory a ");
		sql.append(" where a.PowerAccount = p.PowerAccount and a.ApplyDate < now() ");
		sql.append(" ) ");
		sql.append(" where 1=1 ");		
		sql.append(" and p.PowerAccount = ? ");
		parameterList.add(powerAccount);
		
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核電號是否有異動契約容量
	 * @param powerAccount
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> checkPowerAccountCC(PowerAccountVO powerAccountVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select 1 ");
		sql.append(" from PowerAccount p, ");
		sql.append(" PowerAccountHistory h ");
		sql.append(" where p.PowerAccount = h.PowerAccount ");		
		sql.append(" and p.PowerAccount = ? ");	
		sql.append(" and p.PowerPhase = ?");
		sql.append(" and h.ApplyDate = ? ");
		sql.append(" and h.RatePlanCode = ? ");
		
		parameterList.add(powerAccountVO.getPowerAccount());
		parameterList.add(powerAccountVO.getPowerPhase());
		parameterList.add(powerAccountVO.getApplyDate());
		parameterList.add(powerAccountVO.getRatePlanCode());

		if(StringUtils.isNotBlank(powerAccountVO.getUsuallyCC())) {
			sql.append(" and h.UsuallyCC = ? ");
			parameterList.add(powerAccountVO.getUsuallyCC());
		}else {
			sql.append(" and h.UsuallyCC is null ");
		}
		if(StringUtils.isNotBlank(powerAccountVO.getSpCC())) {
			sql.append(" and h.SPCC = ? ");
			parameterList.add(powerAccountVO.getSpCC());
		}else {
			sql.append(" and h.SPCC is null ");
		}
		if(StringUtils.isNotBlank(powerAccountVO.getSatSPCC())) {
			sql.append(" and h.SatSPCC = ? ");
			parameterList.add(powerAccountVO.getSatSPCC());
		}else {
			sql.append(" and h.SatSPCC is null ");
		}
		if(StringUtils.isNotBlank(powerAccountVO.getOpCC())) {
			sql.append(" and h.OPCC = ? ");
			parameterList.add(powerAccountVO.getOpCC());
		}else {
			sql.append(" and h.OPCC is null ");
		}
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
