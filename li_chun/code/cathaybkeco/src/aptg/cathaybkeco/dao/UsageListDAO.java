package aptg.cathaybkeco.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class UsageListDAO extends BaseDAO {

	/**
	 * 取得耗能分類清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getUsageList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from UsageList ");

		return this.executeQuery(sql.toString(), null);
	}

}
