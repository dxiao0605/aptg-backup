package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.BestCCVO;

public class BestCCDAO extends BaseDAO {
	
	public List<DynaBean> getCC(BestCCVO bestCCVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from ");		
		sql.append(" BestCC ");	
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(bestCCVO.getPowerAccount())) {
			sql.append(" and PowerAccount = ? ");
			parameterList.add(bestCCVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(bestCCVO.getUseMonth())) {
			sql.append(" and useMonth = ? ");
			parameterList.add(bestCCVO.getUseMonth());
		}
		if (StringUtils.isNotBlank(bestCCVO.getRatePlanCode())) {
			sql.append(" and RealPlan = ? ");
			parameterList.add(bestCCVO.getRatePlanCode());
		}
		sql.append(" order by RatePlanCode ");
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得契約容量最佳化
	 * @param bestCCVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBestCC(BestCCVO bestCCVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from ( ");
		sql.append(" select ");
		sql.append(" b.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" pa.PowerAccount, ");
		sql.append(" bc.RatePlanCode as bestRatePlanCode, ");
		sql.append(" (select rp.RatePlanDesc from RatePlanList rp where rp.RatePlanCode = bc.RatePlanCode) as bestRatePlanDesc, ");//-- 最佳用電類型
		sql.append(" (ifnull(bc.UsuallyCC,0)+ifnull(bc.SPCC,0)+ifnull(bc.SatSPCC,0)+ifnull(bc.OPCC,0)) as bsetCC, ");//-- 最佳契約容量
		sql.append(" sum(bc.BaseCharge) as bestBaseCharge, ");//-- 最佳基本電費
		sql.append(" sum(bc.OverCharge) as bestOverCharge ");//-- 最佳非約定電費
		sql.append(" from BankInf b, ");
		sql.append(" 	 PostCode p, ");
		sql.append(" 	 PowerAccount pa, ");
		sql.append("      BestCC bc ");
		sql.append(" where b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = bc.PowerAccount  ");
		if (StringUtils.isNotBlank(bestCCVO.getCity())) {
			sql.append(" and p.City = ? ");
			parameterList.add(bestCCVO.getCity());
		}
		if (StringUtils.isNotBlank(bestCCVO.getPostCodeNo())) {
			sql.append(" and p.seqno = ? ");
			parameterList.add(bestCCVO.getPostCodeNo());
		}
		if (StringUtils.isNotBlank(bestCCVO.getBankCode())) {
			sql.append(" and b.BankCode = ? ");
			parameterList.add(bestCCVO.getBankCode());
		}
		if (StringUtils.isNotBlank(bestCCVO.getBankCodeArr())) {
			sql.append(" and b.BankCode in ("+bestCCVO.getBankCodeArr()+") ");
		}
		if (StringUtils.isNotBlank(bestCCVO.getPowerAccount())) {
			sql.append(" and bc.PowerAccount = ? ");
			parameterList.add(bestCCVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(bestCCVO.getRatePlanCode())) {
			sql.append(" and bc.RatePlanCode = ? ");
			parameterList.add(bestCCVO.getRatePlanCode());
		}
		if (StringUtils.isNotBlank(bestCCVO.getStartDate())) {
			sql.append(" and bc.useMonth >= ? ");
			parameterList.add(bestCCVO.getStartDate());
		}
		if (StringUtils.isNotBlank(bestCCVO.getEndDate())) {
			sql.append(" and bc.useMonth <= ? ");
			parameterList.add(bestCCVO.getEndDate());
		}
		sql.append(" group by pa.PowerAccount, bc.RatePlanCode, (ifnull(bc.UsuallyCC,0)+ifnull(bc.SPCC,0)+ifnull(bc.SatSPCC,0)+ifnull(bc.OPCC,0)) ");
		sql.append(" order by pa.PowerAccount, bc.RatePlanCode, (ifnull(bc.UsuallyCC,0)+ifnull(bc.SPCC,0)+ifnull(bc.SatSPCC,0)+ifnull(bc.OPCC,0)) ");
		sql.append(" )a ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得電號電費
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getPACharge(BestCCVO bestCCVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select   ");
		sql.append(" fc.PowerAccount,  ");
		sql.append(" b.RatePlanCode,  ");
		sql.append(" (select rp.RatePlanDesc from RatePlanList rp where rp.RatePlanCode = b.RatePlanCode) as RatePlanDesc, ");//-- 用電類型
		sql.append(" (ifnull(fc.UsuallyCC,0)+ifnull(fc.SPCC,0)+ifnull(fc.SatSPCC,0)+ifnull(fc.OPCC,0)) as CC, ");//-- 契約容量
		sql.append(" sum(fc.BaseCharge) as BaseCharge, ");//-- 基本電費		
		sql.append(" sum(fc.OverCharge) as OverCharge ");//-- 非約定電費		
		sql.append(" from FcstCharge fc inner join  ");
		sql.append(" (select a.PowerAccount ,max(a.useTime) as maxUseTime, pah.RatePlanCode from FcstCharge a, PowerAccount pa  ");
		sql.append(" inner join PowerAccountHistory pah ");
		sql.append(" on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) ");
		sql.append(" where pa.PowerAccount = a.PowerAccount   ");
		if (StringUtils.isNotBlank(bestCCVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(bestCCVO.getBankCode());
		}
		if (StringUtils.isNotBlank(bestCCVO.getBankCodeArr())) {
			sql.append(" and pa.BankCode in ("+bestCCVO.getBankCodeArr()+") ");
		}
		
		if (StringUtils.isNotBlank(bestCCVO.getStartDate())) {
			sql.append(" and a.useMonth >= ? ");
			parameterList.add(bestCCVO.getStartDate());
		}
		if (StringUtils.isNotBlank(bestCCVO.getEndDate())) {
			sql.append(" and a.useMonth <= ? ");
			parameterList.add(bestCCVO.getEndDate());
		}	
		sql.append(" group by a.PowerAccount, a.useMonth  ) b   ");
		sql.append(" on fc.PowerAccount = b.PowerAccount  ");
		sql.append(" and fc.useTime = b.maxUseTime  ");
		sql.append(" group by fc.PowerAccount  ");

		return this.executeQuery(sql.toString(), parameterList);
	}
}
