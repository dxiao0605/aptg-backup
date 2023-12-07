package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

public class RankListDAO extends BaseDAO {

	/**
	 * 取得權限清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getRankList(String rankCode) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from RankList ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(rankCode)) {
			sql.append(" and RankCode >= ? ");
			parameterList.add(rankCode);
		}
		
		return this.executeQuery(sql.toString(), parameterList);
	}

}
