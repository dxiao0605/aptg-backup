package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountMaxDemandVO;

public class PowerAccountMaxDemandDAO extends BaseDAO {
	
	/**
	 * 取得電號最大需量
	 * @param powerAccountMaxDemandVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPowerAccountDemand(PowerAccountMaxDemandVO powerAccountMaxDemandVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select d.PowerAccount, ");
		sql.append("        (ifnull(f.UsuallyCC,0)+ifnull(f.SPCC,0)+ifnull(f.SatSPCC,0)+ifnull(f.OPCC,0)) as CC, ");
		sql.append("        d.RecTime, ");
		sql.append("        d.TotalDemand ");
		sql.append(" from PowerAccountMaxDemand d ");
		sql.append("      left join FcstCharge f ");
		sql.append("      on f.PowerAccount = d.PowerAccount ");
		sql.append("      and f.useTime = (select max(a.useTime) as maxSeqNo from FcstCharge a  ");
		sql.append(" 					where a.PowerAccount = d.PowerAccount ");
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getStartDate())) {
			sql.append(" and a.useTime >= STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(powerAccountMaxDemandVO.getStartDate());
		}
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getEndDate())) {
			sql.append(" and a.useTime < STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(ToolUtil.getNextDay(powerAccountMaxDemandVO.getEndDate()));
		}
		sql.append(" ) ");
		sql.append(" where 1=1 ");		
		sql.append(" and d.PowerAccount = ? ");
		parameterList.add(powerAccountMaxDemandVO.getPowerAccount());
		
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getStartDate())) {
			sql.append(" and d.RecTime >= STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(powerAccountMaxDemandVO.getStartDate());
		}
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getEndDate())) {
			sql.append(" and d.RecTime < STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(ToolUtil.getNextDay(powerAccountMaxDemandVO.getEndDate()));
		}
		sql.append(" order by d.PowerAccount, d.RecTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電表最大需量
	 * @param powerAccountMaxDemandVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getPowerRecordDemand(PowerAccountMaxDemandVO powerAccountMaxDemandVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select m.DeviceID, ");
		sql.append("        (ifnull(m.UsuallyCC,0)+ifnull(m.SPCC,0)+ifnull(m.SatSPCC,0)+ifnull(m.OPCC,0)) as CC, ");
		sql.append("        d.RecTime, ");
		sql.append("        d.TotalDemand ");
		sql.append(" from MeterSetup m, PowerRecordMaxDemand d ");
		sql.append(" where m.DeviceID = d.DeviceID  ");		
		sql.append(" and m.DeviceID = ? ");
		parameterList.add(powerAccountMaxDemandVO.getDeviceId());
		
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getStartDate())) {
			sql.append(" and d.RecTime >= STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(powerAccountMaxDemandVO.getStartDate());
		}
		if(StringUtils.isNotBlank(powerAccountMaxDemandVO.getEndDate())) {
			sql.append(" and d.RecTime < STR_TO_DATE(?,'%Y%m%d')   ");
			parameterList.add(ToolUtil.getNextDay(powerAccountMaxDemandVO.getEndDate()));
		}
		sql.append(" order by m.DeviceID, d.RecTime ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
