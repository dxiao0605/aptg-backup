package aptg.cathaybkeco.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class MeterTypeListDAO extends BaseDAO {

	/**
	 * 取得電表型號清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getMeterTypeList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from MeterTypeList ");

		return this.executeQuery(sql.toString(), null);
	}

}
