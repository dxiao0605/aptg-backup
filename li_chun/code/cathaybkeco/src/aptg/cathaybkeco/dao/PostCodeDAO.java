package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.PostCodeVO;

public class PostCodeDAO extends BaseDAO {

	/**
	 * 取得縣市下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getCityList(PostCodeVO postCodeVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		if("all".equals(postCodeVO.getType())) {
			sql.append(" select distinct p.City ");
			sql.append(" from PostCode p ");
			sql.append(" where 1=1 ");
		}else {
			if("3".equals(postCodeVO.getRankCode()) || "4".equals(postCodeVO.getRankCode())) {//區域管理者或區域使用者
				sql.append(" select distinct p.City ");
				sql.append(" from AccessBanks a, BankInf b, PostCode p ");				
				sql.append(" where a.AreaCodeNo = ? ");
				parameterList.add(postCodeVO.getAreaCodeNo());
				sql.append(" and a.BankCode = b.BankCode ");
				sql.append(" and b.PostCodeNo = p.seqno ");
			}else {
				sql.append(" select distinct p.City ");
				sql.append(" from PostCode p, ");
				sql.append(" BankInf b ");
				sql.append(" where 1=1 ");
				sql.append(" and b.PostCodeNo = p.seqno ");
			}			
		}
		
		if (StringUtils.isNotBlank(postCodeVO.getCityGroup())) {
			sql.append(" and p.CityGroup = ? ");
			parameterList.add(postCodeVO.getCityGroup());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得地區下拉選單
	 * 
	 * @param city
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDistList(PostCodeVO postCodeVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		if("all".equals(postCodeVO.getType())) {
			sql.append(" select distinct p.seqno, p.Dist ");
			sql.append(" from PostCode p ");
			sql.append(" where 1=1 ");
		}else {
			if("3".equals(postCodeVO.getRankCode()) || "4".equals(postCodeVO.getRankCode())) {//區域管理者或區域使用者
				sql.append(" select distinct p.seqno, p.Dist ");
				sql.append(" from AccessBanks a, BankInf b, PostCode p ");				
				sql.append(" where a.AreaCodeNo = ? ");
				parameterList.add(postCodeVO.getAreaCodeNo());
				sql.append(" and a.BankCode = b.BankCode ");
				sql.append(" and b.PostCodeNo = p.seqno ");
			}else {
				sql.append(" select distinct p.seqno, p.Dist ");
				sql.append(" from PostCode p, ");
				sql.append(" BankInf b ");
				sql.append(" where 1=1 ");
				sql.append(" and b.PostCodeNo = p.seqno ");
			}
		}

		if (StringUtils.isNotBlank(postCodeVO.getCity())) {
			sql.append(" and p.City in ("+postCodeVO.getCity()+") ");
		}

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得行政區域分類下拉選單
	 * 
	 * @param postCodeVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getCityGroupList() throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct CityGroup from PostCode ");

		return this.executeQuery(sql.toString(), null);
	}

	/**
	 * 取得分行數&電號數
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBankAndPowerAccountCount() throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" p.CityGroup, ");
		sql.append(" count(distinct b.BankCode) as bankcount, ");
		sql.append(" count(distinct pa.PowerAccount) as poweraccountcount ");
		sql.append(" from BankInf b, ");
		sql.append(" PostCode p, ");
		sql.append(" PowerAccount pa ");
		sql.append(" where  b.PostCodeNo = p.seqno ");
		sql.append(" and b.BankCode = pa.BankCode ");
		sql.append(" group by p.CityGroup ");

		return this.executeQuery(sql.toString(), null);
	}
}
