package aptg.cathaybkeco.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;

public class BankInfDAO extends BaseDAO {

	/**
	 * 取得分行下拉選單
	 * 
	 * @param city
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBankList(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		if("3".equals(bankInfVO.getRankCode()) || "4".equals(bankInfVO.getRankCode())) {//區域管理者或區域使用者
			sql.append(" select b.BankCode, b.BankName ");
			sql.append(" from AccessBanks a, BankInf b, PostCode p ");
			sql.append(" where a.AreaCodeNo = ? ");
			parameterList.add(bankInfVO.getAreaCodeNo());
			sql.append(" and a.BankCode = b.BankCode ");
			sql.append(" and b.PostCodeNo = p.seqno ");

			if (StringUtils.isNotBlank(bankInfVO.getCity())) {
				sql.append(" and p.City in ("+bankInfVO.getCity()+") ");
			}
			if (StringUtils.isNotBlank(bankInfVO.getPostCodeNo())) {
				sql.append(" and b.PostCodeNo in ("+bankInfVO.getPostCodeNo()+") ");
			}
			sql.append(" order by b.BankCode ");
		}else {
			sql.append(" select b.BankCode, b.BankName ");
			sql.append(" from BankInf b, PostCode p ");
			sql.append(" where b.PostCodeNo = p.seqno ");

			if (StringUtils.isNotBlank(bankInfVO.getCity())) {
				sql.append(" and p.City in ("+bankInfVO.getCity()+") ");
			}
			if (StringUtils.isNotBlank(bankInfVO.getPostCodeNo())) {
				sql.append(" and b.PostCodeNo in ("+bankInfVO.getPostCodeNo()+") ");
			}
			if (StringUtils.isNotBlank(bankInfVO.getBankCode())) {
				sql.append(" and b.BankCode = ? ");
				parameterList.add(bankInfVO.getBankCode());
			}
			sql.append(" order by b.BankCode ");
		}
		
		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得分行資訊
	 * 
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBankInf(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select p.City, p.Dist, b.* ");
		sql.append(" from BankInf b, PostCode p ");
		sql.append(" where b.PostCodeNo = p.seqno ");

		if (StringUtils.isNotBlank(bankInfVO.getBankCode())) {
			sql.append(" and b.BankCode = ? ");
			parameterList.add(bankInfVO.getBankCode());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增分行資訊
	 * @param bankInfVO
	 * @throws Exception
	 */
	public void addBankInf(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" INSERT INTO BankInf ( ");
		sql.append(" BankCode,  ");
		sql.append(" BankName, ");
		sql.append(" PostCodeNo, ");	
		sql.append(" BankAddress, ");	
		sql.append(" PhoneNr, ");	
		sql.append(" Area, ");	
		sql.append(" People, ");
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?) ");
		
		parameterList.add(bankInfVO.getBankCode());
		parameterList.add(bankInfVO.getBankName());
		parameterList.add(bankInfVO.getPostCodeNo());
		parameterList.add(bankInfVO.getBankAddress());
		parameterList.add(bankInfVO.getPhoneNr());
		parameterList.add(bankInfVO.getArea());
		parameterList.add(bankInfVO.getPeople());
		parameterList.add(bankInfVO.getUserName());
		parameterList.add(bankInfVO.getUserName());

		this.executeUpdate(sql.toString(), parameterList);
	}

	/**
	 * 修改分行資訊
	 * 
	 * @param bankInfVO
	 * @throws Exception
	 */
	public void updBankInf(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE BankInf ");
		sql.append(" SET  ");
		sql.append(" BankName = ?, ");
		sql.append(" PostCodeNo = ?, ");	
		sql.append(" BankAddress = ?, ");	
		sql.append(" PhoneNr = ?, ");	
		sql.append(" Area = ?, ");	
		sql.append(" People = ?, ");		
		sql.append(" UpdateUserName = ? ");
		sql.append(" WHERE BankCode = ? ");

		parameterList.add(bankInfVO.getBankName());
		parameterList.add(bankInfVO.getPostCodeNo());
		parameterList.add(bankInfVO.getBankAddress());
		parameterList.add(bankInfVO.getPhoneNr());
		parameterList.add(bankInfVO.getArea());
		parameterList.add(bankInfVO.getPeople());
		parameterList.add(bankInfVO.getUserName());
		parameterList.add(bankInfVO.getBankCode());

		this.executeUpdate(sql.toString(), parameterList);
	}

	
	/**
	 * 取得電力數值資訊
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityInfo(BankInfVO bankInfVO) throws Exception {		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String thisDay = sdf.format(new Date());
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		String filter = bankInfVO.getFilter();
		
		sql.append(" select  ");
		sql.append(" CityGroup, ");
		sql.append(" City, ");
		sql.append(" Dist,  ");
		sql.append(" BankCode, ");
		sql.append(" BankName, ");
		sql.append(" PowerAccount, ");
		sql.append(" RatePlanDesc, ");
		sql.append(" RatePlanCode, ");
		sql.append(" AccountDesc, ");
		sql.append(" sum(CC) as CC, ");
		sql.append(" UsageDesc, ");
		sql.append(" UsageCode, ");
		sql.append(" DeviceID, ");
		sql.append(" MeterName, ");
		sql.append(" sum(Area) as Area, ");
		sql.append(" sum(People) as People, ");
		sql.append(" avg(Vol) as Vol, ");
		sql.append(" avg(Cur) as Cur, ");
		sql.append(" sum(W) as W, ");
		sql.append(" sum(Var) as Var, ");
		sql.append(" sum(VA) as VA, ");
		sql.append(" avg(PF) as PF, ");
		sql.append(" avg(Hz) as Hz, ");
		sql.append(" sum(DF) as DF, ");
		if (filter.contains("CEC")) {
			sql.append(" sum(TPMCECPK) as TPMCECPK, ");
			sql.append(" sum(TPMCECSP) as TPMCECSP, ");
			sql.append(" sum(TPMCECSatSP) as TPMCECSatSP, ");
			sql.append(" sum(TPMCECOP) as TPMCECOP, ");
		}
		sql.append(" sum(MCECPK) as MCECPK, ");
		sql.append(" sum(MCECSP) as MCECSP, ");
		sql.append(" sum(MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(MCECOP) as MCECOP, ");
		sql.append(" sum(CECSum) as CECSum, ");
		sql.append(" sum(DDemand) as DDemand, ");
		sql.append(" sum(MDemand) as MDemand ");
		sql.append(" from( ");		
		
		sql.append(" select  ");
		sql.append(" p.CityGroup, ");
		sql.append(" p.City, ");// -- 縣市
		sql.append(" p.Dist, ");// -- 行政區
		sql.append(" b.BankCode, ");// -- 分行代碼
		sql.append(" b.BankName, ");// -- 分行名稱
		sql.append(" pa.PowerAccount, ");// -- 電號
		sql.append(" (select RatePlanDesc from RatePlanList rp ");
		sql.append("  where rp.RatePlanCode = pah.RatePlanCode) as RatePlanDesc, ");// 用電類型
		sql.append(" pah.RatePlanCode, ");
		sql.append(" pa.AccountDesc, ");// -- 說明
		if(filter.contains("MeterName")||filter.contains("UsageDesc")) {
			sql.append(" (ifnull(ms.UsuallyCC,0)+ifnull(ms.SPCC,0)+ifnull(ms.SatSPCC,0)+ifnull(ms.OPCC,0)) as CC, ");
		}else {
			sql.append(" (ifnull(pah.UsuallyCC,0)+ifnull(pah.SPCC,0)+ifnull(pah.SatSPCC,0)+ifnull(pah.OPCC,0)) as CC, ");// -- 契約容量*
		}
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");// -- 耗能分類
		sql.append(" ms.UsageCode, ");
		sql.append(" ms.DeviceID, ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.Area, ");// -- 面積*
		sql.append(" ms.People, ");// -- 員工數*
		sql.append(" pr.VavgP as Vol, ");// -- 電壓
		sql.append(" pr.Iavg as Cur, ");// -- 電流
		sql.append(" pr.W, ");// -- 功率
		sql.append(" pr.Var, ");// -- 虛功
		sql.append(" pr.VA, ");// -- 視在
		sql.append(" pr.PF, ");// -- 功因
		sql.append(" pr.Hz, ");// -- 頻率
		sql.append(" pr.Mode1 as DF, ");// -- 需量預測
		if (filter.contains("CEC")) {
			sql.append(" (case when pah.RatePlanCode in (1,2,6,21,22,23) then (ifnull(pr.MCECPK,0)+ifnull(pr.MCECSP,0)+ifnull(pr.MCECSatSP,0)+ifnull(pr.MCECOP,0)) ");
			sql.append(" 	   when pah.RatePlanCode in (3,5,7,8) then (ifnull(pr.MCECPK,0)+ifnull(pr.MCECSP,0))  ");
			if(ToolUtil.isSummer()) {
				sql.append("       when pah.RatePlanCode in (4,9) then pr.MCECPK ");
			}
			sql.append(" 	   else 0 end) as TPMCECPK, ");//-- 台電尖峰用電量
			
			if(ToolUtil.isSummer()) {
				sql.append(" (case when pah.RatePlanCode in (4,9) then pr.MCECSP ");
			}else {
				sql.append(" (case when pah.RatePlanCode in (4,9) then (ifnull(pr.MCECPK,0)+ifnull(pr.MCECSP,0)) ");
			}		
			sql.append(" 	   else 0 end) as TPMCECSP, ");//-- 台電半尖峰用電量
			sql.append(" (case when pah.RatePlanCode in (5,7,8,9) then pr.MCECSatSP ");
			sql.append(" 	   else 0 end) as TPMCECSatSP, ");//-- 台電周六半尖峰用電量
			sql.append(" (case when pah.RatePlanCode in (3,4) then (ifnull(pr.MCECSatSP,0)+ifnull(pr.MCECOP,0)) ");
			sql.append("       when pah.RatePlanCode in (5,7,8,9) then pr.MCECOP ");
			sql.append(" 	   else 0 end) as TPMCECOP, ");//-- 台電離峰用電量
		}
		sql.append(" pr.MCECPK, ");//-- ECO5尖峰用電量
		sql.append(" pr.MCECSP, ");//-- ECO5半尖峰用電量
		sql.append(" pr.MCECSatSP, ");//-- ECO5周六半尖峰用電量
		sql.append(" pr.MCECOP, ");//-- ECO5離峰用電量
		sql.append(" (ifnull(pr.MCECPK,0)+ifnull(pr.MCECSP,0)+ifnull(pr.MCECSatSP,0)+ifnull(pr.MCECOP,0)) as CECSum, ");//-- 總用電量
		if (filter.contains("MDemand")) {
			sql.append(" (select GREATEST(max(a.DemandPK),MAX(a.DemandSP),MAX(a.DemandSatSP),MAX(a.DemandOP)) as DDemand ");
			sql.append(" from PowerRecord a use index(`PowerRecord_UDX`) where a.DeviceID = ms.DeviceID ");
			sql.append(" and a.RecTime >= STR_TO_DATE('"+thisDay+"','%Y%m%d')  ");
			sql.append(" and a.RecTime < STR_TO_DATE('"+ToolUtil.getNextDay(thisDay)+"','%Y%m%d') ");
			sql.append(" ) as DDemand, ");
			Calendar now = Calendar.getInstance();
			if(now.get(Calendar.DAY_OF_MONTH)>1) {
				now.add(Calendar.DATE, -1);
				sql.append(" (select GREATEST(b.MDemandPK,b.MDemandSP,b.MDemandSatSP,b.MDemandOP) as MDemand  ");
				sql.append(" from PowerRecordCollection b use index(`DeviceID`)  ");
				sql.append(" where b.DeviceID = ms.DeviceID and b.RecDate = '"+sdf.format(now.getTime())+"' ");
				sql.append(" ) as MDemand  ");				 
			}else {
				sql.append(" '' as MDemand ");	
			}
		}else {
			sql.append(" '' as DDemand, ");
			sql.append(" '' as MDemand ");
		}

		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = ");
		sql.append(" (select max(a.ApplyDate) from PowerAccountHistory a ");
		sql.append(" where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()), ");
		sql.append(" MeterSetup ms ");
		sql.append(" left join (select a.* from PowerRecord a inner join (select c.DeviceID, max(c.RecTime) as maxRecTime from PowerRecord c where 1=1 group by c.DeviceID) b ");
		sql.append(" on a.DeviceID = b.DeviceID ");
		sql.append(" and a.RecTime = b.maxRecTime ");
		sql.append(" and a.RecTime >= DATE_SUB(now(), INTERVAL 5 MINUTE) ) pr  ");
		sql.append(" on pr.DeviceID = ms.DeviceID ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.Enabled = 1 ");
		
		if(!(filter.contains("MeterName")||filter.contains("UsageDesc"))) {
			sql.append(" and ms.UsageCode = 1 ");
		}else {		
			if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
				sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
			}
		}
		
		if (StringUtils.isNotBlank(bankInfVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(bankInfVO.getCityGroup());
		}
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		sql.append(" ) a ");
		
		
		String groupby = new String();
		if(filter.contains("cityGroup")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"CityGroup";
		}
		if(filter.contains("City")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"PowerAccount";
		}
		if(filter.contains("RatePlanDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"RatePlanCode";
		}
		if(filter.contains("AccountDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"AccountDesc";
		}

		if(filter.contains("UsageDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"UsageCode";
		}
		if(filter.contains("MeterName")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"DeviceID";
		}
		if(StringUtils.isNotBlank(groupby))
			sql.append(" group by "+groupby);	
		
		String orderby = new String();

		if(filter.contains("PowerAccount")) {
			orderby += ",PowerAccount";
		}
		if(filter.contains("UsageDesc")) {
			orderby += ",UsageCode";
		}
		if(filter.contains("MeterName")) {
			orderby += ",DeviceID";
		}
		if(StringUtils.isNotBlank(orderby))
			sql.append(" order by City, Dist, BankCode "+orderby);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得分析計算資訊(by 電號)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getFcstInfoByAccount(BankInfVO bankInfVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String thisMonth = sdf.format(new Date());
		String lastYear = ToolUtil.getLastYear(sdf);
		String lastYearDay = ToolUtil.getLastYear(sdf2);
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" CityGroup, ");
		sql.append(" City,  ");
		sql.append(" Dist,  ");
		sql.append(" BankCode, ");
		sql.append(" BankName, ");
		sql.append(" PowerAccount, ");
		sql.append(" RatePlanDesc, ");
		sql.append(" RatePlanCode, ");
		sql.append(" AccountDesc, ");		
		sql.append(" sum(CC) as CC, ");
		sql.append(" UsageCode, ");
		sql.append(" UsageDesc, ");
		sql.append(" MeterName, ");
		sql.append(" sum(Area) as Area, ");
		sql.append(" sum(People) as People, ");
		sql.append(" sum(MCECPK) as MCECPK, ");
		sql.append(" sum(MCECSP) as MCECSP, ");
		sql.append(" sum(MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(MCECOP) as MCECOP, ");
		sql.append(" sum(MCEC) as MCEC, ");
		sql.append(" sum(NowTotalCharge) as NowTotalCharge, ");
		sql.append(" sum(FcstMCEC) as FcstMCEC, ");
		sql.append(" sum(FcstTotalCharge) as FcstTotalCharge, ");
		sql.append(" sum(LastMCEC) as LastMCEC, ");
		sql.append(" sum(LastTotalCharge) as LastTotalCharge ");
		sql.append(" from( ");
		sql.append(" select  ");
		sql.append(" p.CityGroup, ");
		sql.append(" p.City,  ");//-- 縣市
		sql.append(" p.Dist,  ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" b.BankName, ");//-- 分行名稱
		sql.append(" pa.PowerAccount, ");//-- 電號
		sql.append(" (select RatePlanDesc from RatePlanList rp where rp.RatePlanCode = pah.RatePlanCode) as RatePlanDesc, ");//-- 用電類型
		sql.append(" pah.RatePlanCode, ");
		sql.append(" pa.AccountDesc, ");//-- 說明
		sql.append(" fc.UsuallyCC, ");
		sql.append(" fc.SPCC, ");
		sql.append(" fc.SatSPCC, ");
		sql.append(" fc.OPCC, ");
		sql.append(" (ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");
		sql.append(" ms.UsageCode,  ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc,  ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People, ");//-- 員工數
		sql.append(" fc.MCECPK, ");
		sql.append(" fc.MCECSP, ");
		sql.append(" fc.MCECSatSP, ");
		sql.append(" fc.MCECOP, ");
		sql.append(" fc.MCEC as MCEC, ");//-- 目前電量
		sql.append(" fc.TotalCharge as NowTotalCharge, ");// -- 目前電費
		sql.append(" fc.FcstMCEC,  ");//-- 預估電量
		sql.append(" fc.FcstTotalCharge, ");//-- 預估電費
				
		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" eb.showCEC as LastMCEC, ");//-- 去年同期電量
			sql.append(" eb.showCharge as LastTotalCharge ");//-- 去年同期總電費
		}else {
			sql.append(" ifnull(br.TPMCEC,0) as LastMCEC, ");
			sql.append(" br.TotalCharge as LastTotalCharge ");
		}
		sql.append(" from BankInf b, ");
		sql.append("      PostCode p, ");
		sql.append("      PowerAccount pa ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) ");
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = '"+thisMonth+"'  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount  ");

		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" left join EntryBill eb on eb.PowerAccount = pa.PowerAccount  ");
			sql.append(" and eb.startBillMon <= '"+lastYearDay+"' ");
			sql.append(" and eb.endBillMon >= '"+lastYearDay+"', ");
		}else {
			sql.append(" left join BestRatePlan br on br.PowerAccount = pa.PowerAccount ");
			sql.append(" and br.inUse = 1  ");
			sql.append(" and br.useMonth = '"+lastYear+"', ");
		}
		sql.append(" (select d.PowerAccount, d.UsageCode, d.MeterName, sum(d.Area) as area, sum(d.People) as People from MeterSetup d ");
		sql.append(" where d.Enabled = 1 and d.UsageCode = 1 group by d.PowerAccount ) ms ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");

		if (StringUtils.isNotBlank(bankInfVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(bankInfVO.getCityGroup());
		}
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}
		sql.append(" ) a ");
		
		String filter = bankInfVO.getFilter();
		String groupby = new String();		
		if(filter.contains("City")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"PowerAccount";
		}
		if(filter.contains("RatePlanDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"RatePlanCode";
		}
		if(filter.contains("AccountDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"AccountDesc";
		}
		if(StringUtils.isNotBlank(groupby))
			sql.append(" group by "+groupby);
		
		String orderby = new String();
		if(filter.contains("City")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"PowerAccount";
		}
		
		if(StringUtils.isNotBlank(orderby))
			sql.append(" order by "+orderby);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得分析計算資訊 (by 電表)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getFcstInfoByMeter(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String thisMonth = sdf.format(new Date());
		String lastYear = ToolUtil.getLastYear(sdf);
		String lastYearDay = ToolUtil.getLastYear(sdf2);
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" CityGroup, ");
		sql.append(" City,  ");
		sql.append(" Dist,  ");
		sql.append(" BankCode, ");
		sql.append(" BankName, ");
		sql.append(" PowerAccount, ");
		sql.append(" RatePlanDesc, ");
		sql.append(" RatePlanCode, ");
		sql.append(" AccountDesc, ");
		sql.append(" sum(CC) as CC, ");
		sql.append(" UsageCode, ");
		sql.append(" UsageDesc, ");
		sql.append(" DeviceID, ");
		sql.append(" MeterName, ");
		sql.append(" sum(Area) as Area, ");
		sql.append(" sum(People) as People, ");
		sql.append(" sum(MCECPK) as MCECPK, ");
		sql.append(" sum(MCECSP) as MCECSP, ");
		sql.append(" sum(MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(MCECOP) as MCECOP, ");
		sql.append(" sum(MCEC) as MCEC, ");
		sql.append(" sum(FcstECO5MCEC) as FcstECO5MCEC, ");
		sql.append(" sum(NowMCEC) as NowMCEC, ");
		sql.append(" sum(NowTotalCharge) as NowTotalCharge, ");
		sql.append(" sum(FcstMCEC) as FcstMCEC, ");
		sql.append(" sum(FcstTotalCharge) as FcstTotalCharge, ");
		sql.append(" sum(LastMCEC) as LastMCEC, ");
		sql.append(" sum(LastTotalMCEC) as LastTotalMCEC, ");
		sql.append(" sum(LastTotalCharge) as LastTotalCharge ");
		sql.append(" from( ");
		
		sql.append(" select  ");
		sql.append(" p.CityGroup, ");
		sql.append(" p.City,  ");//-- 縣市
		sql.append(" p.Dist,  ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" b.BankName, ");//-- 分行名稱
		sql.append(" pa.PowerAccount, ");//-- 電號
		sql.append(" (select RatePlanDesc from RatePlanList rp where rp.RatePlanCode = pah.RatePlanCode) as RatePlanDesc, ");//-- 用電類型
		sql.append(" pah.RatePlanCode, ");
		sql.append(" pa.AccountDesc, ");//-- 說明		
		sql.append(" (ifnull(ms.UsuallyCC,0)+ifnull(ms.SPCC,0)+ifnull(ms.SatSPCC,0)+ifnull(ms.OPCC,0)) as CC,  ");//-- 契約容量
		sql.append(" ms.UsageCode, ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");//-- 耗能分類
		sql.append(" ms.DeviceID, ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People, ");//-- 員工數
		sql.append(" pr.MCECPK, ");
		sql.append(" pr.MCECSP, ");
		sql.append(" pr.MCECSatSP, ");
		sql.append(" pr.MCECOP, ");
		sql.append(" pr.CEC as MCEC, ");//-- 及時電量
		sql.append(" pr.FcstECO5MCEC, ");//-- 預測用電量
		sql.append(" fc.MCEC as NowMCEC, ");//-- 目前電量
		sql.append(" fc.TotalCharge as NowTotalCharge, ");//-- 目前電費
		sql.append(" fc.FcstMCEC, ");//-- 預估電量
		sql.append(" fc.FcstTotalCharge, ");//-- 預估電費
		
		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" (case when ms.UsageCode = 1 then eb.ShowCEC else 0 end) AS LastMCEC, ");//-- 去年同期電量
			sql.append(" (case when ms.UsageCode = 1 then eb.ShowCEC else 0 end) AS LastTotalMCEC, ");//-- 去年同期電量
			sql.append(" (case when ms.UsageCode = 1 then eb.showCharge else 0 end) AS LastTotalCharge ");//-- 去年同期電費
		}else {
			sql.append(" prc.CEC as LastMCEC, ");
			sql.append(" br.TPMCEC as LastTotalMCEC, ");
			sql.append(" br.TotalCharge as LastTotalCharge ");
		}
		
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa ");
		sql.append(" left join PowerAccountHistory pah on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) ");
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = '"+thisMonth+"'  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount  ");
	
		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" left join EntryBill eb on eb.PowerAccount = pa.PowerAccount  ");
			sql.append(" and eb.startBillMon <= '"+lastYearDay+"' ");
			sql.append(" and eb.endBillMon >= '"+lastYearDay+"', ");
		}else {
			sql.append(" left join BestRatePlan br on br.PowerAccount = pa.PowerAccount ");
			sql.append(" and br.inUse = 1  ");
			sql.append(" and br.useMonth = '"+lastYear+"', ");
		}
		
		sql.append(" MeterSetup ms  ");
		sql.append(" left join (select f.* from PowerRecordCollection f, ");
		sql.append(" (select g.DeviceID, max(g.RecDate) as MaxRecDate from PowerRecordCollection g ");
		sql.append(" where g.RecDate >= STR_TO_DATE(?, '%Y%m') ");
		parameterList.add(thisMonth);			
		sql.append(" AND g.RecDate < STR_TO_DATE(?, '%Y%m') ");
		parameterList.add(ToolUtil.getNextMonth(thisMonth));
		sql.append(" group by g.DeviceID ) h where f.DeviceID = h.DeviceID ");
		sql.append(" AND f.RecDate = h.MaxRecDate ");
		sql.append("  ) pr on pr.DeviceID = ms.DeviceID ");
		
		sql.append(" left join PowerRecordCollection prc on prc.DeviceID = ms.DeviceID ");
		sql.append(" and prc.RecDate = STR_TO_DATE('"+lastYearDay+"','%Y%m%d') ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.Enabled = 1 ");

		if (StringUtils.isNotBlank(bankInfVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(bankInfVO.getCityGroup());
		}
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}
		
		
		if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
			sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
		}
			
		sql.append("  ) a ");
		String filter = bankInfVO.getFilter();
		String groupby = new String();		
		if(filter.contains("City")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"PowerAccount";
		}
		if(filter.contains("RatePlanDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"RatePlanCode";
		}
		if(filter.contains("AccountDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"AccountDesc";
		}

		if(filter.contains("UsageDesc")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"UsageCode";
		}
		if(filter.contains("MeterName")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"DeviceID";
		}
		if(StringUtils.isNotBlank(groupby))
			sql.append(" group by "+groupby);
		
		String orderby = new String();
		if(filter.contains("City")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"PowerAccount";
		}
		if(filter.contains("UsageDesc")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"UsageCode";
		}
		if(filter.contains("MeterName")) {
			orderby += (StringUtils.isNotBlank(orderby)?",":"")+"DeviceID";
		}
		if(StringUtils.isNotBlank(orderby))
			sql.append(" order by "+orderby);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得空調佔比
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getAir(BankInfVO bankInfVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String thisMonth = sdf.format(new Date());
		String lastYearDay = ToolUtil.getLastYear(sdf2);
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" CityGroup,  ");
		sql.append(" City,   ");
		sql.append(" Dist,   ");
		sql.append(" BankCode,  ");
		sql.append(" PowerAccount,  ");
		sql.append(" UsageCode,  ");
		sql.append(" DeviceID,  ");
		sql.append(" sum(MCEC) as MCEC, ");
		sql.append(" sum(FcstECO5MCEC) as FcstECO5MCEC, ");
		sql.append(" sum(LastMCEC) as LastMCEC ");
		sql.append(" from( ");
		sql.append(" select ");
		sql.append(" p.CityGroup,  ");
		sql.append(" p.City, ");//-- 縣市
		sql.append(" p.Dist, ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" pa.PowerAccount, ");//-- 電號
		sql.append(" ms.UsageCode, ");
		sql.append(" ms.DeviceID, ");
		sql.append(" pr.CEC as MCEC, ");//-- 及時電量
		sql.append(" pr.FcstECO5MCEC, ");//-- 預測用電量
		
		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" (case when ms.UsageCode = 1 then eb.ShowCEC else 0 end) AS LastMCEC ");//-- 去年同期電量
		}else {
			sql.append(" prc.CEC as LastMCEC ");
		}

		sql.append(" from BankInf b,  ");
		sql.append(" PostCode p,  ");
		sql.append(" PowerAccount pa ");
		if("1".equals(bankInfVO.getLastCode())) {
			sql.append(" left join EntryBill eb on eb.PowerAccount = pa.PowerAccount  ");
			sql.append(" and eb.startBillMon <= '"+lastYearDay+"' ");
			sql.append(" and eb.endBillMon >= '"+lastYearDay+"' ");
		}
		sql.append(" ,MeterSetup ms ");		
		sql.append(" left join (select f.* from PowerRecordCollection f, ");
		sql.append(" (select g.DeviceID, max(g.RecDate) as MaxRecDate from PowerRecordCollection g ");
		sql.append(" where g.RecDate >= STR_TO_DATE(?, '%Y%m') ");
		parameterList.add(thisMonth);			
		sql.append(" AND g.RecDate < STR_TO_DATE(?, '%Y%m') ");
		parameterList.add(ToolUtil.getNextMonth(thisMonth));
		sql.append(" group by g.DeviceID ) h where f.DeviceID = h.DeviceID ");
		sql.append(" AND f.RecDate = h.MaxRecDate ");
		sql.append("  ) pr on pr.DeviceID = ms.DeviceID ");
		sql.append(" left join PowerRecordCollection prc on prc.DeviceID = ms.DeviceID ");
		sql.append(" and prc.RecDate = '"+lastYearDay+"'  ");//-- 去年同期
		sql.append(" where b.PostCodeNo = p.seqno  ");
		sql.append(" and b.BankCode = pa.BankCode  ");
		sql.append(" and pa.PATypeCode <= 10 ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount  ");
		sql.append(" and ms.Enabled = 1  ");
		if (StringUtils.isNotBlank(bankInfVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(bankInfVO.getCityGroup());
		}
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}
		sql.append("   ) a  ");

		String filter = bankInfVO.getFilter();
		String groupby = new String();		
		if(filter.contains("City")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"BankCode";
		}
		if(filter.contains("PowerAccount")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"PowerAccount";
		}
		groupby += (StringUtils.isNotBlank(groupby)?",":"")+"UsageCode";
		
		if(filter.contains("MeterName")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"DeviceID";
		}
		if(StringUtils.isNotBlank(groupby))
			sql.append(" group by "+groupby);
		
		sql.append(" order by City,Dist,BankCode,PowerAccount,UsageCode,DeviceID ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得地區電費資訊
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCharge(BankInfVO bankInfVO) throws Exception {
		String thisMonth = ToolUtil.getThisMonth();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" p.CityGroup,  ");
		sql.append(" p.City, ");//-- 縣市
		sql.append(" p.Dist, ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" b.BankName, ");//-- 分行名稱
		sql.append(" sum(fc.BaseCharge) as BaseCharge, ");//-- 基本電費
		sql.append(" sum(fc.UsageCharge) as UsageCharge, ");//-- 流動電費
		sql.append(" sum(fc.OverCharge) as OverCharge, ");//-- 非約定電費
		sql.append(" sum(fc.TotalCharge) as TotalCharge, ");//-- 總電費
		sql.append(" sum(ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");//-- 契約容量
		sql.append(" sum(GREATEST(fc.MDemandPK, fc.MDemandSP, fc.MDemandSatSP ,fc.MDemandOP)) as MDemand ");//-- 當月最大需量
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa use index (Bank) ");
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1 ");
		sql.append(" and c.useMonth = '"+thisMonth+"' ");
		if("Over".equals(bankInfVO.getType())) {
			sql.append(" and c.RealPlan in (select RatePlanCode from RatePlanList where HaveOverCharge = 1) ");
		}
		sql.append(" group by c.PowerAccount) b ");
		sql.append(" on a.PowerAccount = b.PowerAccount  ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");				
		sql.append(" and PATypeCode <= 10 ");
		
		if("bank".equals(bankInfVO.getGroupType())) {
			sql.append(" group by b.BankCode ");
		}else {
			sql.append(" group by p.CityGroup ");
		}
		
		
		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 取得地區單價資訊
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPrice(BankInfVO bankInfVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String thisMonth = ToolUtil.getThisMonth();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" p.CityGroup,  ");
		sql.append(" p.City, ");//-- 縣市
		sql.append(" p.Dist, ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" b.BankName, ");//-- 分行名稱
		sql.append(" sum(fc.MCECPK) as MCECPK, ");
		sql.append(" sum(fc.MCECSP) as MCECSP, ");
		sql.append(" sum(fc.MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(fc.MCECOP) as MCECOP, ");
		sql.append(" sum(fc.MCEC) as CECSum, ");//- 目前總用電量
		sql.append(" sum(fc.TotalCharge) as TotalCharge, ");//- 目前電費
		sql.append(" sum(fc.FcstMCEC) as FcstMCEC, ");//- 預測用電量
		sql.append(" sum(fc.FcstTotalCharge) as FcstTotalCharge, ");//- 預測電費
		sql.append(" sum(br.TPMCEC) as MCECLM, ");//-- 上月用電量
		sql.append(" sum(br.TotalCharge) as TotalChargeLM, ");//-- 上月電費
		sql.append(" sum(br2.TPMCEC) as MCECLY, ");//-- 去年用電量
		sql.append(" sum(br2.TotalCharge) as TotalChargeLY ");//-- 去年電費
		sql.append(" from BankInf b, ");
		sql.append("      PostCode p, ");
		sql.append("      PowerAccount pa ");		
		sql.append(" left join (select a.* from FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = '"+thisMonth+"'  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on fc.PowerAccount = pa.PowerAccount  ");		
		sql.append(" left join BestRatePlan br on br.PowerAccount = pa.PowerAccount  ");
		sql.append(" and br.inUse = 1  ");
		sql.append(" and br.useMonth = '"+ToolUtil.getLastMonth(sdf)+"' ");
		sql.append(" left join BestRatePlan br2 on br2.PowerAccount = pa.PowerAccount  ");
		sql.append(" and br2.inUse = 1  ");
		sql.append(" and br.useMonth = '"+ToolUtil.getLastYear(sdf)+"' ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");

		if("bank".equals(bankInfVO.getGroupType())) {
			sql.append(" group by b.BankCode ");
		}else {
			sql.append(" group by p.CityGroup ");
		}
		
		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 用電排行(日)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityRankDaily(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		List<String> filter = bankInfVO.getFilterList();

		sql.append(" select p.City, ");
		sql.append(" p.Dist, ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" pa.PowerAccount, ");		
		sql.append(" pa.AccountDesc, ");
		sql.append(" ms.UsageCode,  ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.DeviceID, ");
	
		sql.append(" pr.RecDate, ");
		sql.append(" pr.RealPlan, ");
		sql.append(" (select RatePlanDesc from RatePlanList rp where rp.RatePlanCode = pr.RealPlan) as RatePlanDesc, ");
		sql.append(" (ifnull(pr.UsuallyCC,0)+ifnull(pr.SPCC,0)+ifnull(pr.SatSPCC,0)+ifnull(pr.OPCC,0)) as CC, ");
		sql.append(" pr.TPDCECPK, ");
		sql.append(" pr.TPDCECSP, ");
		sql.append(" pr.TPDCECSatSP, ");
		sql.append(" pr.TPDCECOP, ");
		sql.append(" pr.DCECPK, ");
		sql.append(" pr.DCECSP, ");
		sql.append(" pr.DCECSatSP, ");
		sql.append(" pr.DCECOP, ");
		sql.append(" pr.DCEC, ");	
		sql.append(" greatest(pr.TPMDemandPK, pr.TPMDemandSP, pr.TPMDemandSatSP, pr.TPMDemandOP) as MDemand, ");
		
		if (filter.contains("Last")) {
			sql.append(" (select a.DCEC  ");
			sql.append(" from PowerRecordCollection a  ");
			sql.append(" where a.DeviceID = ms.DeviceID ");
			sql.append(" and a.RecDate = date_sub(pr.RecDate, interval 1 year)  ");			
			sql.append(" ) as Last, ");
		}else {
			sql.append(" null as Last, ");
		}

		if (filter.contains("Air")) {
			sql.append(" (case when ms.UsageCode = 1 then   ");
			sql.append(" (select sum(b.DCEC) ");
			sql.append(" from MeterSetup m ,PowerRecordCollection b ");
			sql.append(" where m.DeviceID = b.DeviceID ");
			sql.append(" and b.RecDate = pr.RecDate ");
			sql.append(" and m.PowerAccount = pa.PowerAccount ");
			sql.append(" and m.UsageCode = 2 ");
			sql.append(" ) else null end ) as Air ");
		}else {
			sql.append(" null as Air ");
		}

		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa, ");
		sql.append(" MeterSetup ms, ");
		sql.append(" PowerRecordCollection pr    ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount    ");		
		sql.append(" and ms.Enabled = 1 ");
		sql.append(" and ms.DeviceID = pr.DeviceID   ");

		if(!"Meter".equals(bankInfVO.getMode())) {
			sql.append(" and ms.UsageCode = 1 ");
		}else {		
			if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
				sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
			}
		}
		if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
			sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
		}
		
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
			sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y/%m/%d') ");
			parameterList.add(bankInfVO.getStartDate());
		}
		if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
			sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y/%m/%d') ");									
			parameterList.add(ToolUtil.getNextDay(bankInfVO.getEndDate(),"yyyy/MM/dd"));
		}	

		String orderby = new String();
		if(filter.contains("City")) {
			orderby = "p.City,";
		}
		if(filter.contains("Dist")) {
			orderby += "p.Dist,";		
		}
		sql.append(" order by "+orderby+" b.BankCode, ms.UsageCode, ms.DeviceID, pr.RecDate desc ");	
		
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 用電排行(電表)(月)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityRankMeter(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		List<String> filter = bankInfVO.getFilterList();

		sql.append(" select p.City, ");
		sql.append(" p.Dist, ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" pa.PowerAccount, ");		
		sql.append(" pa.AccountDesc, ");
		sql.append(" ms.UsageCode,  ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.DeviceID, ");
		
		sql.append(" fc.RealPlan, ");
		sql.append(" (select RatePlanDesc from RatePlanList rp where rp.RatePlanCode = fc.RealPlan) as RatePlanDesc, ");
		sql.append(" (ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");
		sql.append(" pr.TPMCECPK, ");
		sql.append(" pr.TPMCECSP, ");
		sql.append(" pr.TPMCECSatSP, ");
		sql.append(" pr.TPMCECOP, ");
		sql.append(" pr.MCECPK, ");
		sql.append(" pr.MCECSP, ");
		sql.append(" pr.MCECSatSP, ");
		sql.append(" pr.MCECOP, ");
		sql.append(" pr.CEC, ");		
		sql.append(" greatest(pr.TPMDemandPK, pr.TPMDemandSP, pr.TPMDemandSatSP, pr.TPMDemandOP) as MDemand, ");
		
		sql.append(" fc.useMonth, ");
		sql.append(" fc.useTime, ");
		sql.append(" fc.TPMCEC, ");
		sql.append(" fc.BaseCharge, ");
		sql.append(" fc.UsageCharge, ");
		sql.append(" fc.OverCharge, ");
		sql.append(" fc.TotalCharge, ");
		if (filter.contains("Last")) {
			sql.append(" (select sum(d.CEC)  ");
			sql.append(" from PowerRecordCollection d ,  ");
			sql.append(" (select a.DeviceID, max(a.RecDate) as MaxRecDate  ");
			sql.append(" from PowerRecordCollection a ");
			sql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(bankInfVO.getLastStartDate())) {
				sql.append(" and a.RecDate >= STR_TO_DATE(?,'%Y/%m') ");
				parameterList.add(bankInfVO.getLastStartDate());
			}
			if(StringUtils.isNotBlank(bankInfVO.getLastEndDate())) {
				sql.append(" and a.RecDate < STR_TO_DATE(?,'%Y/%m') ");
				parameterList.add(ToolUtil.getNextMonth(bankInfVO.getLastEndDate(),"yyyy/MM"));
			}
			sql.append(" group by a.DeviceID, date_format(a.RecDate,'%Y%m')) c   ");
			sql.append(" where d.DeviceID = c.DeviceID   ");
			sql.append(" and d.RecDate = c.MaxRecDate ");
			sql.append(" and date_format(d.RecDate,'%Y%m')  = date_format(date_sub(pr.RecDate, interval 1 year),'%Y%m') ");
			sql.append(" and d.DeviceID = ms.DeviceID ");
			sql.append(" ) as Last, ");//-- 去年同期用電量差
		}else {
			sql.append(" null as Last, ");
		}
		if (filter.contains("Air")) {
			sql.append(" (case when ms.UsageCode = 1 then   ");
			sql.append(" (select sum(b.CEC)   ");
			sql.append(" from MeterSetup m ,PowerRecordCollection b  ");
			sql.append(" where m.DeviceID = b.DeviceID ");
			sql.append(" and b.RecDate = pr.RecDate  ");
			sql.append(" and m.PowerAccount = pa.PowerAccount ");
			sql.append(" and m.UsageCode = 2 ");
			sql.append(" ) else null end ) as Air ");
		}else {
			sql.append(" null as Air ");
		}

		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa, ");
		sql.append(" MeterSetup ms, ");
		sql.append(" PowerRecordCollection pr    ");
		sql.append(" inner join (select a.DeviceID, max(a.RecDate) as MaxRecDate from PowerRecordCollection a    ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
			sql.append(" and a.RecDate >= STR_TO_DATE(?,'%Y/%m') ");
			parameterList.add(bankInfVO.getStartDate());
		}
		if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
			sql.append(" and a.RecDate < STR_TO_DATE(?,'%Y/%m') ");									
			parameterList.add(ToolUtil.getNextMonth(bankInfVO.getEndDate(), "yyyy/MM"));
		}	
		sql.append(" group by a.DeviceID, date_format(a.RecDate,'%Y/%m')) c   ");
		sql.append(" on pr.DeviceID = c.DeviceID   ");
		sql.append(" and pr.RecDate = c.MaxRecDate,  ");
		sql.append(" FcstCharge fc  ");
		sql.append(" inner join (select a.PowerAccount ,max(a.useTime) as maxUseTime from FcstCharge a  ");
		sql.append(" where 1=1 ");

		if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
			sql.append(" and a.useTime >= STR_TO_DATE(?,'%Y/%m')  ");
			parameterList.add(bankInfVO.getStartDate());
		}
		if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
			sql.append(" and a.useTime < STR_TO_DATE(?,'%Y/%m') ");
			parameterList.add(ToolUtil.getNextMonth(bankInfVO.getEndDate(), "yyyy/MM"));
		}	
		sql.append(" group by a.PowerAccount, a.useMonth  ) d   ");
		sql.append(" on fc.PowerAccount = d.PowerAccount  ");
		sql.append(" and fc.useTime = d.maxUseTime ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount    ");		
		sql.append(" and pa.PowerAccount = fc.PowerAccount   ");
		sql.append(" and ms.Enabled = 1 ");
		sql.append(" and ms.DeviceID = pr.DeviceID   ");
		sql.append(" and fc.useMonth = date_format(pr.RecDate,'%Y%m') ");
		if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
			sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
		}
		
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		
		String orderby = new String();
		if(filter.contains("City")) {
			orderby = "p.City,";
		}
		if(filter.contains("Dist")) {
			orderby += "p.Dist,";		
		}
		sql.append(" order by "+orderby+" b.BankCode, ms.UsageCode, ms.DeviceID, fc.useTime desc ");	

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 用電排行基本資料(電表)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityRankBasicMeter(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select p.City, ");//-- 縣市
		sql.append(" p.Dist, ");//-- 行政區
		sql.append(" b.BankCode, ");//-- 分行代碼
		sql.append(" b.BankName, ");//-- 分行名稱 
		sql.append(" pa.PowerAccount, ");//-- 電號
		sql.append(" pa.AccountDesc, ");//-- 說明
		sql.append(" ms.UsageCode,  ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");//-- 耗能分類
		sql.append(" ms.MeterName, ");//-- 電表名稱
		sql.append(" ms.DeviceID, ");
		sql.append(" ms.Area, ");//-- 面積
		sql.append(" ms.People ");//-- 員工數
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa, ");
		sql.append(" MeterSetup ms ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount    ");		
		sql.append(" and ms.Enabled = 1 ");
		if(!"Meter".equals(bankInfVO.getMode())) {
			sql.append(" and ms.UsageCode = 1 ");
		}else {		
			if (StringUtils.isNotBlank(bankInfVO.getUsageCodeArr())) {
				sql.append(" and ms.UsageCode in ("+bankInfVO.getUsageCodeArr()+") ");
			}
		}
		
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 用電排行(電號)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityRankPA(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		List<String> filter = bankInfVO.getFilterList();

		sql.append(" select p.City, ");
		sql.append(" p.Dist, ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" pa.PowerAccount, ");
		sql.append(" fc.RealPlan, ");
		sql.append(" (select RatePlanDesc from RatePlanList rp where rp.RatePlanCode = fc.RealPlan) as RatePlanDesc, ");
		sql.append(" pa.AccountDesc, ");
		sql.append(" ms.UsageCode,  ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");
		sql.append(" ms.MeterName, ");
		sql.append(" ms.DeviceID, ");		
		sql.append(" (ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");
		sql.append(" fc.TPMCECPK, ");
		sql.append(" fc.TPMCECSP, ");
		sql.append(" fc.TPMCECSatSP, ");
		sql.append(" fc.TPMCECOP, ");
		sql.append(" fc.MCECPK, ");
		sql.append(" fc.MCECSP, ");
		sql.append(" fc.MCECSatSP, ");
		sql.append(" fc.MCECOP, ");
		sql.append(" greatest(fc.TPMDemandPK, fc.TPMDemandSP, fc.TPMDemandSatSP, fc.TPMDemandOP) as MDemand, ");
		
		sql.append(" fc.useMonth, ");
		sql.append(" fc.useTime, ");
		sql.append(" fc.TPMCEC, ");
		sql.append(" fc.BaseCharge, ");
		sql.append(" fc.UsageCharge, ");
		sql.append(" fc.OverCharge, ");
		sql.append(" fc.TotalCharge, ");
		if (filter.contains("Last")) {
			sql.append(" (select sum(a.MCEC) ");
			sql.append(" from  PowerMonth a ");
			sql.append(" where a.PowerAccount = fc.PowerAccount ");
			sql.append(" and a.useMonth = DATE_FORMAT(date_sub(fc.useTime, interval 1 year) , '%Y%m') ");		
			sql.append(" ) as Last, ");
		}else {
			sql.append(" null as Last, ");
		}
		if (filter.contains("Air")) {
			sql.append(" (case when ms.UsageCode = 1 then   ");
			sql.append(" (select sum(b.CEC) ");
			sql.append(" from MeterSetup m ,PowerRecordCollection b ");
			sql.append(" where m.DeviceID = b.DeviceID ");
			sql.append(" and b.RecDate = STR_TO_DATE(date_format(fc.useTime,'%Y/%m/%d'),'%Y/%m/%d') ");
			sql.append(" and m.PowerAccount = pa.PowerAccount ");
			sql.append(" and m.UsageCode = 2 ");
			sql.append(" ) else null end ) as Air ");
		}else {
			sql.append(" null as Air ");
		}

		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa, ");
		sql.append(" MeterSetup ms, ");
		sql.append(" FcstCharge fc  ");
		sql.append(" inner join (select a.PowerAccount ,max(a.useTime) as maxUseTime from FcstCharge a  ");
		sql.append(" where 1=1 ");

		if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
			sql.append(" and a.useTime >= STR_TO_DATE(?,'%Y/%m') ");
			parameterList.add(bankInfVO.getStartDate());
		}
		if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
			sql.append(" and a.useTime < STR_TO_DATE(?,'%Y/%m') ");
			parameterList.add(ToolUtil.getNextMonth(bankInfVO.getEndDate(), "yyyy/MM"));
		}	
		sql.append(" group by a.PowerAccount, a.useMonth  ) d   ");
		sql.append(" on fc.PowerAccount = d.PowerAccount  ");
		sql.append(" and fc.useTime = d.maxUseTime ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount    ");		
		sql.append(" and pa.PowerAccount = fc.PowerAccount   ");
		sql.append(" and ms.Enabled = 1 ");	
		sql.append(" and ms.UsageCode = 1 ");
		
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		
		
		String orderby = new String();
		if(filter.contains("City")) {
			orderby = "p.City,";
		}
		if(filter.contains("Dist")) {
			orderby += "p.Dist,";		
		}
		sql.append(" order by "+orderby+" b.BankCode, ms.UsageCode, ms.DeviceID, fc.useTime desc ");	
		
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 用電排行基本資料(分行)
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityRankBasicBK(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select p.City, ");
		sql.append(" p.Dist, ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, "); 
		sql.append(" b.Area, ");
		sql.append(" b.People ");
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and exists (select 1 from ControllerSetup c where c.BankCode = b.BankCode and c.Enabled = 1 ) ");
		
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得分行空調用電分析
	 * 
	 * @param bankInfVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBankAir(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select ");
		sql.append(" b.Area, ");
		sql.append(" b.People, ");
		sql.append(" ms.UsageCode, ");
		sql.append(" (select u.UsageDesc from UsageList u where u.UsageCode = ms.UsageCode) as UsageDesc, ");
		sql.append(" sum(pr.MCECSP) as SP, ");
		sql.append(" sum(pr.MCECSatSP) as SatSP, ");
		sql.append(" sum(pr.MCECOP) as OP, ");
		if(bankInfVO.isEco5()) {
			sql.append(" sum(pr.MCECPK) as MCECPK, ");
			sql.append(" sum(pr.MCECSP) as MCECSP, ");
			sql.append(" sum(pr.MCECSatSP) as MCECSatSP, ");
			sql.append(" sum(pr.MCECOP) as MCECOP, ");
			sql.append(" sum(pr.FcstECO5MCECPK) as FcstMCECPK, ");
			sql.append(" sum(pr.FcstECO5MCECSP) as FcstMCECSP, ");
			sql.append(" sum(pr.FcstECO5MCECSatSP) as FcstMCECSatSP, ");
			sql.append(" sum(pr.FcstECO5MCECOP) as FcstMCECOP, ");	
		}else {
			boolean isSummer = ToolUtil.isSummer();
			sql.append(" sum(pr.TPMCECPK) as MCECPK, ");
			sql.append(" sum(pr.TPMCECSP) as MCECSP, ");
			sql.append(" sum(pr.TPMCECSatSP) as MCECSatSP, ");
			sql.append(" sum(pr.TPMCECOP) as MCECOP, ");			
			sql.append(" (case when fc.RealPlan in (1,2,6,21,22,23) then (ifnull(pr.FcstECO5MCECPK,0)+ifnull(pr.FcstECO5MCECSP,0)+ifnull(pr.FcstECO5MCECSatSP,0)+ifnull(pr.MCECOP,0)) ");
			sql.append(" 	   when fc.RealPlan in (3,5,7,8) then (ifnull(pr.FcstECO5MCECPK,0)+ifnull(pr.FcstECO5MCECSP,0))  ");
			if(isSummer) {
				sql.append(" when fc.RealPlan in (4,9) then pr.FcstECO5MCECPK ");
			}
			sql.append(" 	   else 0 end) as FcstMCECPK, ");//-- 台電尖峰預測用電量
			
			if(isSummer) {
				sql.append(" (case when fc.RealPlan in (4,9) then pr.FcstECO5MCECSP ");
			}else {
				sql.append(" (case when fc.RealPlan in (4,9) then (ifnull(pr.FcstECO5MCECPK,0)+ifnull(pr.FcstECO5MCECSP,0)) ");
			}		
			sql.append(" 	   else 0 end) as FcstMCECSP, ");//-- 台電半尖峰預測用電量
			sql.append(" (case when fc.RealPlan in (5,7,8,9) then pr.FcstECO5MCECSatSP ");
			sql.append(" 	   else 0 end) as FcstMCECSatSP, ");//-- 台電周六半尖峰預測用電量
			sql.append(" (case when fc.RealPlan in (3,4) then (ifnull(pr.FcstECO5MCECSatSP,0)+ifnull(pr.FcstECO5MCECOP,0)) ");
			sql.append("       when fc.RealPlan in (5,7,8,9) then pr.FcstECO5MCECOP ");
			sql.append(" 	   else 0 end) as FcstMCECOP, ");//-- 台電離峰預測用電量		
		}		
		sql.append(" fc.MCEC, ");
		sql.append(" fc.TotalCharge, ");
		sql.append(" fc.FcstMCEC, ");
		sql.append(" fc.FcstTotalCharge ");
		sql.append(" from BankInf b, ");
		sql.append(" PowerAccount pa ");
		sql.append(" left join FcstCharge fc on fc.PowerAccount = pa.PowerAccount ");
		sql.append(" and fc.useTime = (select max(a.useTime) from FcstCharge a where a.PowerAccount = pa.PowerAccount and a.useMonth = ?), ");
		parameterList.add(bankInfVO.getDate());
		sql.append(" MeterSetup ms ");
		sql.append(" left join PowerRecordCollection pr ");
		sql.append(" on pr.DeviceID = ms.DeviceID ");
		sql.append(" and pr.RecDate = (select max(c.RecDate) from PowerRecordCollection c ");
		sql.append(" where c.DeviceID = ms.DeviceID  ");
		sql.append(" and c.RecDate >= STR_TO_DATE(?,'%Y%m') ");
		sql.append(" and c.RecDate < STR_TO_DATE(?,'%Y%m')) ");	
		parameterList.add(bankInfVO.getDate());			
		parameterList.add(ToolUtil.getNextMonth(bankInfVO.getDate()));

		sql.append(" where b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = ms.PowerAccount ");
		sql.append(" and ms.Enabled = 1 ");

		sql.append(" and pa.BankCode = ? ");
		parameterList.add(bankInfVO.getBankCode());
		
		sql.append(" group by ms.UsageCode ");
		sql.append(" order by ms.UsageCode ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得EUI&EPUI資訊
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getEuiAndEpui(BankInfVO bankInfVO) throws Exception {
		String thisMonth = ToolUtil.getThisMonth();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" p.CityGroup,  ");	
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" b.area, ");
		sql.append(" b.people, ");
		sql.append(" sum(fc.MCECPK) as MCECPK, ");
		sql.append(" sum(fc.MCECSP) as MCECSP, ");
		sql.append(" sum(fc.MCECSatSP) as MCECSatSP, ");
		sql.append(" sum(fc.MCECOP) as MCECOP, ");
		sql.append(" sum(fc.FcstMCEC) as CECSum ");//- 預測總用電量	
		sql.append(" from BankInf b ");
		sql.append(" left join (select pa.BankCode, a.* from PowerAccount pa, FcstCharge a  ");
		sql.append(" inner join (select c.PowerAccount, max(c.useTime) as maxUseTime from FcstCharge c where 1=1  ");
		sql.append(" and c.useMonth = '"+thisMonth+"'  ");
		sql.append(" group by c.PowerAccount) b  ");
		sql.append(" on a.PowerAccount = b.PowerAccount   ");
		sql.append(" where pa.PowerAccount = a.PowerAccount ");
		sql.append(" and a.useTime = b.maxUseTime ) fc  ");
		sql.append(" on b.BankCode = fc.BankCode, ");
		sql.append("  PostCode p ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and exists (select 1 from ControllerSetup c where c.BankCode = b.BankCode and c.Enabled = 1 ) ");
		sql.append(" group by b.BankCode ");
	
		return this.executeQuery(sql.toString(), null);
	}

	public boolean checkBankExist(String bankCode) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select BankCode   ");
		sql.append(" from BankInf ");		
		sql.append(" where BankCode = ? ");
		parameterList.add(bankCode);
	
		List<DynaBean> row = this.executeQuery(sql.toString(), parameterList);		
		if(row!=null && !row.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 查詢分行最大需量
	 * @param bankInfVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBankDemandMax(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		List<String> filter = bankInfVO.getFilterList();

		sql.append(" select  a.BankCode, ");
		if (filter.contains("RecDate")) {
			if("0".equals(bankInfVO.getRecType())) {//日
				sql.append(" date_format(a.RecDate,'%Y%m%d') as RecDate, ");
			}else {//月
				sql.append(" date_format(a.RecDate,'%Y%m') as RecDate, ");
			}
		}
		sql.append(" max(a.Demand) as Demand ");
		sql.append(" from  ");
		sql.append(" ( ");
		sql.append(" select  b.BankCode,		 ");
		sql.append(" 		 b.RecDate,   ");
		sql.append(" 		 sum(b.Demand) as Demand ");
		sql.append(" 		 from (  ");
		sql.append(" 		 select   ");
		sql.append("          b.BankCode, ");
		sql.append(" 		 pr.RecDate,  ");
		sql.append(" 		 greatest(pr.DemandPK, pr.DemandSP, pr.DemandSatSP, pr.DemandOP) as Demand ");
		sql.append(" 		 from BankInf b,  ");
		sql.append("         PostCode p, ");
		sql.append(" 		 PowerAccount pa,  ");
		sql.append(" 		 MeterSetup ms ,  ");
		sql.append(" 		 PowerRecordCollection pr  ");
		sql.append(" 		 where b.PostCodeNo = p.seqno ");
		sql.append("         and b.BankCode = pa.BankCode ");
		sql.append(" 		 and pa.PATypeCode <= 10  ");
		sql.append(" 		 and pa.PowerAccount = ms.PowerAccount  ");
		sql.append(" 		 and ms.DeviceID = pr.DeviceID  ");
		sql.append(" 		   and ms.Enabled = 1   ");
		sql.append(" 			 and ms.UsageCode = 1  ");
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}		
		
		if("0".equals(bankInfVO.getRecType())) {//日
			if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
				sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y/%m/%d')  ");
				parameterList.add(bankInfVO.getStartDate());
			}
			if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
				sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y/%m/%d') 	 ");
				parameterList.add(ToolUtil.getNextDay(bankInfVO.getEndDate(),"yyyy/MM/dd"));
			}	
		}else {//月
			if(StringUtils.isNotBlank(bankInfVO.getStartDate())) {
				sql.append(" and pr.RecDate >= STR_TO_DATE(?,'%Y/%m')  ");
				parameterList.add(bankInfVO.getStartDate());
			}
			if(StringUtils.isNotBlank(bankInfVO.getEndDate())) {
				sql.append(" and pr.RecDate < STR_TO_DATE(?,'%Y/%m') 	 ");
				parameterList.add(ToolUtil.getNextMonth(bankInfVO.getEndDate(),"yyyy/MM"));
			}	
		}
		
		sql.append(" 		 ) b ");
		sql.append(" 		 group by b.BankCode, b.RecDate  ");
		sql.append(" ) a ");
		sql.append(" group by  ");
		if (filter.contains("RecDate")) {
			if("0".equals(bankInfVO.getRecType())) {//日
				sql.append(" date_format(a.RecDate,'%Y%m%d'), ");
			}else {//月
				sql.append(" date_format(a.RecDate,'%Y%m'), ");
			}
		}
		sql.append(" a.BankCode ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢分行人數面積
	 * @param bankInfVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBankAreaAndPeople(BankInfVO bankInfVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" CityGroup, ");
		sql.append(" City,  ");
		sql.append(" Dist,  ");
		sql.append(" BankCode, ");
		sql.append(" sum(Area) as Area, ");
		sql.append(" sum(People) as People ");
		sql.append(" from( ");
		sql.append(" select  ");
		sql.append(" p.CityGroup, ");
		sql.append(" p.City,  ");
		sql.append(" p.Dist,  ");
		sql.append(" b.BankCode, ");
		sql.append(" b.Area, ");
		sql.append(" b.People ");
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and exists (select 1 from ControllerSetup c where c.BankCode = b.BankCode and c.Enabled = 1 ) ");

		if (StringUtils.isNotBlank(bankInfVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(bankInfVO.getCityGroup());
		}
		if (StringUtils.isNotBlank(bankInfVO.getCityArr())) {
			sql.append(" and p.City in (" + bankInfVO.getCityArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getPostCodeNoArr())) {
			sql.append(" and b.PostCodeNo in (" + bankInfVO.getPostCodeNoArr() + ") ");
		}
		if (StringUtils.isNotBlank(bankInfVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in (" + bankInfVO.getBankCodeArr() + ") ");
		}
			
		sql.append("  ) a ");
		String filter = bankInfVO.getFilter();
		String groupby = new String();		
		if(filter.contains("City")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"City";
		}
		if(filter.contains("Dist")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"Dist";		
		}
		if(filter.contains("BankCode")) {
			groupby += (StringUtils.isNotBlank(groupby)?",":"")+"BankCode";
		}
		if(StringUtils.isNotBlank(groupby))
			sql.append(" group by "+groupby);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
