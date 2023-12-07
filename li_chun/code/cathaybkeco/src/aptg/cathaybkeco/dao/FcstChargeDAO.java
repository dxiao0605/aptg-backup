package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.FcstChargeVO;

public class FcstChargeDAO extends BaseDAO {

	/**
	 * 取得分行每月電費
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getFcstChargeMonth(FcstChargeVO fcstChargeVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" fc.useMonth, ");
		sql.append(" sum(fc.BaseCharge) as BaseCharge, ");//-- 基本電費
		sql.append(" sum(fc.UsageCharge) as UsageCharge, ");//-- 流動電費
		sql.append(" sum(fc.OverCharge) as OverCharge, ");//-- 非約定電費
		sql.append(" sum(fc.TotalCharge) as TotalCharge, ");//-- 總電費
		sql.append(" sum(ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");//-- 契約容量
		sql.append(" sum(GREATEST(fc.TPMDemandPK,fc.TPMDemandSP,fc.TPMDemandSatSP,fc.TPMDemandOP)) as MDemand, ");//-- 最大需量
		sql.append(" sum(fc.TPMCECPK) as TPMCECPK, ");//-- 尖峰用電量
		sql.append(" sum(fc.TPMCECSP) as TPMCECSP, ");//-- 半尖峰用電量
		sql.append(" sum(fc.TPMCECSatSP) as TPMCECSatSP, ");//-- 周六半尖峰用電量
		sql.append(" sum(fc.TPMCECOP) as TPMCECOP, ");//-- 離峰用電量
		sql.append(" sum(fc.TPMCEC) as TPMCEC ");//-- 總用電量
		sql.append(" from FcstCharge fc inner join ");
		sql.append(" (select a.PowerAccount ,max(a.useTime) as maxUseTime from FcstCharge a, PowerAccount pa ");
		sql.append(" where pa.PowerAccount = a.PowerAccount  ");
		if (StringUtils.isNotBlank(fcstChargeVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(fcstChargeVO.getBankCode());
		}
		if (StringUtils.isNotBlank(fcstChargeVO.getStartDate())) {
			sql.append(" and a.useMonth >= ? ");
			parameterList.add(fcstChargeVO.getStartDate());
		}
		if (StringUtils.isNotBlank(fcstChargeVO.getEndDate())) {
			sql.append(" and a.useMonth <= ? ");
			parameterList.add(fcstChargeVO.getEndDate());
		}		
		sql.append(" group by a.PowerAccount, a.useMonth  ) b  ");
		sql.append(" on fc.PowerAccount = b.PowerAccount ");
		sql.append(" and fc.useTime = b.maxUseTime ");
		sql.append(" group by fc.useMonth ");
		sql.append(" order by fc.useMonth ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核電號是否有電費
	 * @param eco5Account
	 * @param meterId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> checkPowerAccount(String powerAccount) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");		
		sql.append(" 1 ");
		sql.append(" from  FcstCharge  ");
		sql.append(" where PowerAccount = ? ");
		parameterList.add(powerAccount);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
}
