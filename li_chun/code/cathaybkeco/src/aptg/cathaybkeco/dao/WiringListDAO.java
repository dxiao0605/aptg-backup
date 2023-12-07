package aptg.cathaybkeco.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class WiringListDAO extends BaseDAO {

	/**
	 * 接線方式清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getWiringList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from WiringList ");

		return this.executeQuery(sql.toString(), null);
	}

}
