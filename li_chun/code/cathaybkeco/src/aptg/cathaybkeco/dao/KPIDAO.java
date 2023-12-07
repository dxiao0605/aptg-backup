package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.KpiVO;

public class KPIDAO extends BaseDAO {

	/**
	 * 節能目標值
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getKPI() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select  ");
		sql.append(" (case when UnitPriceKPIenabled = 0 then UnitPriceKPIavg else UnitPriceKPI end ) as UnitPriceKPI, ");
		sql.append(" (case when EUIKPIenabled = 0 then EUIKPIavg else EUIKPI end ) as EUIKPI, ");
		sql.append(" (case when EPUIKPIenabled = 0 then EPUIKPIavg else EPUIKPI end ) as EPUIKPI, ");
		sql.append(" (case when AirKPIenabled = 0 then AirKPIavg else AirKPI end ) as AirKPI ");
		sql.append(" from KPI ");

		return this.executeQuery(sql.toString(), null);
	}

	/**
	 * 節能目標值設定
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getKPISetup() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from KPI ");
		
		return this.executeQuery(sql.toString(), null);
	}
	
	public void updKPISetup(KpiVO kpiVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE KPI ");
		sql.append(" SET ");
		if(StringUtils.isNotBlank(kpiVO.getUnitPriceKPIenabled())) {
			sql.append(" UnitPriceKPI = ?, ");
			sql.append(" UnitPriceKPIenabled = ?, ");
			parameterList.add(kpiVO.getUnitPriceKPI());
			parameterList.add(kpiVO.getUnitPriceKPIenabled());
		}
		if(StringUtils.isNotBlank(kpiVO.getEuiKPIenabled())) {
			sql.append(" EUIKPI = ?, ");
			sql.append(" EUIKPIenabled = ?, ");
			parameterList.add(kpiVO.getEuiKPI());
			parameterList.add(kpiVO.getEuiKPIenabled());
			
		}
		if(StringUtils.isNotBlank(kpiVO.getEpuiKPIenabled())) {
			sql.append(" EPUIKPI = ?, ");
			sql.append(" EPUIKPIenabled = ?, ");
			parameterList.add(kpiVO.getEpuiKPI());
			parameterList.add(kpiVO.getEpuiKPIenabled());
			
		}
		if(StringUtils.isNotBlank(kpiVO.getAirKPIenabled())) {
			sql.append(" AirKPI = ?, ");
			sql.append(" AirKPIenabled = ?, ");
			parameterList.add(kpiVO.getAirKPI());
			parameterList.add(kpiVO.getAirKPIenabled());
		}
		
		sql.append(" UpdateUserName = ? ");
		parameterList.add(kpiVO.getUserName());
		
		sql.append(" where seqno = 1 ");
		
		this.executeUpdate(sql.toString(), parameterList); 
	}
}
