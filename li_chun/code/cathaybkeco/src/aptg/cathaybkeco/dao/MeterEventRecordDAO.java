package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.MeterEventRecordVO;

public class MeterEventRecordDAO extends BaseDAO {

	/**
	 * 取得電表事件紀錄
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getMeterEventRecord(MeterEventRecordVO meterEventRecordVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select e.EventTime, ");
		sql.append(" (select ec.EventDesc from EventList ec where ec.EventCode = e.EventCode) as EventName, ");
		sql.append(" e.EventDesc ");
		sql.append(" from MeterEventRecord e ");
		sql.append(" where 1=1 ");
		
		if(StringUtils.isNotBlank(meterEventRecordVO.getStartDate())) {
			sql.append(" and e.EventTime >= ? ");
			parameterList.add(meterEventRecordVO.getStartDate());
		}
		if(StringUtils.isNotBlank(meterEventRecordVO.getEndDate())) {
			sql.append(" and e.EventTime <= ? ");
			parameterList.add(meterEventRecordVO.getEndDate());
		}
		
		sql.append(" order by e.EventTime desc ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 取得斷線紀錄
	 * 
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDisconnect() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from MeterEventTx me where me.EventStatus = 0 ");
		
		return this.executeQuery(sql.toString(), null);
	}
}
