package aptg.cathaybkeco.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public class PATypeListDAO extends BaseDAO {

	/**
	 * 電號類別清單下拉選單
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getPATypeList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from PATypeList ");

		return this.executeQuery(sql.toString(), null);
	}

}
