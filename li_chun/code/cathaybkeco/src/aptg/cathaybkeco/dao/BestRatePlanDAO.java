package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.BestRatePlanVO;

public class BestRatePlanDAO extends BaseDAO {

	/**
	 * 取得最適電費
	 * 
	 * @param bestRatePlanVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBestRatePlan(BestRatePlanVO bestRatePlanVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from ( ");
		sql.append(" select pa.BankCode, ");
		sql.append(" b.BankName, ");
		sql.append(" pa.PowerAccount, ");
		sql.append(" br.RatePlanCode, ");
		
		sql.append(" sum(br.TotalCharge) as TotalCharge, ");		
		sql.append(" ifnull(sum(br.TotalCharge),0)-ifnull(sum(br2.TotalCharge),0) as DiffCharge, ");
		sql.append(" (pah.RatePlanCode) AS inUse, ");
		sql.append(" sum(br2.TotalCharge) as inUseTotal ");
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa ");
		sql.append(" inner join PowerAccountHistory pah  ");
		sql.append(" on pah.PowerAccount = pa.PowerAccount ");
		sql.append(" and pah.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()), ");
		sql.append(" BestRatePlan br, ");
		sql.append(" BestRatePlan br2 ");
		sql.append(" where  b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" and pa.PowerAccount = br.PowerAccount  ");
		sql.append(" and br2.PowerAccount = br.PowerAccount ");
		sql.append(" and br2.useMonth = br.useMonth ");
		sql.append(" and br2.RealPlan = pah.RatePlanCode ");
		sql.append(" and br2.inUse = 1 ");
		
		
		if (StringUtils.isNotBlank(bestRatePlanVO.getCity())) {
			sql.append(" and p.City = ? ");
			parameterList.add(bestRatePlanVO.getCity());
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getPostCodeNo())) {
			sql.append(" and b.PostCodeNo = ? ");
			parameterList.add(bestRatePlanVO.getPostCodeNo());
		}
		
		if (StringUtils.isNotBlank(bestRatePlanVO.getBankCode())) {
			sql.append(" and pa.BankCode = ? ");
			parameterList.add(bestRatePlanVO.getBankCode());
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getBankCodeArr())) {
			sql.append(" and pa.BankCode in ("+bestRatePlanVO.getBankCodeArr()+") ");
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getPowerAccount())) {
			sql.append(" and pa.PowerAccount = ? ");
			parameterList.add(bestRatePlanVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getRatePlanCode())) {
			sql.append(" and br.RatePlanCode in ("+bestRatePlanVO.getRatePlanCode()+") ");			
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getStartDate())) {
			sql.append(" and br.useMonth >= ? ");
			parameterList.add(bestRatePlanVO.getStartDate());
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getEndDate())) {
			sql.append(" and br.useMonth <= ? ");
			parameterList.add(bestRatePlanVO.getEndDate());
		}
		
		sql.append(" group by pa.PowerAccount, br.RatePlanCode ");
		sql.append(" order by pa.PowerAccount, br.RatePlanCode ");
		sql.append(" ) a ");
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getRatePlan(BestRatePlanVO bestRatePlanVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from ");		
		sql.append(" BestRatePlan br ");	
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(bestRatePlanVO.getPowerAccount())) {
			sql.append(" and br.PowerAccount = ? ");
			parameterList.add(bestRatePlanVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(bestRatePlanVO.getUseMonth())) {
			sql.append(" and br.useMonth = ? ");
			parameterList.add(bestRatePlanVO.getUseMonth());
		}

		sql.append(" order by br.RealPlan ");
		return this.executeQuery(sql.toString(), parameterList);
	}

}
