package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.PowerMonthVO;

public class PowerMonthDAO extends BaseDAO {

	public List<DynaBean> getPowerMonth(PowerMonthVO powerMonthVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from ");		
		sql.append(" PowerMonth ");	
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(powerMonthVO.getPowerAccount())) {
			sql.append(" and PowerAccount = ? ");
			parameterList.add(powerMonthVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(powerMonthVO.getUseMonth())) {
			sql.append(" and useMonth = ? ");
			parameterList.add(powerMonthVO.getUseMonth());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}

}
