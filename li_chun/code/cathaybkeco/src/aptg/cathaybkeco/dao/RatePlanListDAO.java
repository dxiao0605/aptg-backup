package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

public class RatePlanListDAO extends BaseDAO {

	/**
	 * 取得計費方式清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getRatePlanCodeList(String limit) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from RatePlanList ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(limit)) {
			sql.append(" and RatePlanCode <= ? ");
			parameterList.add(limit);
		}
		return this.executeQuery(sql.toString(), parameterList);
	}

}
