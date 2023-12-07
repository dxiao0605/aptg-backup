package aptg.cathaybkeco.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class DemandForcastListDAO extends BaseDAO {

	/**
	 * 取得需量預測模式對照表下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDFList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from DemandForcastList ");

		return this.executeQuery(sql.toString(), null);
	}

}
