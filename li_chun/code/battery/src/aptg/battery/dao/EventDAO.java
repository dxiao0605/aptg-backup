package aptg.battery.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.vo.EventVO;

public class EventDAO extends BaseDAO {

	/**
	 * 取得告警筆數
	 * 
	 * @param eventVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getAlertCount(EventVO eventVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) as count ");
		sql.append(" from Event e ");
		sql.append(" where e.EventStatus = 5 ");
		
		if(StringUtils.isNotBlank(eventVO.getCompanyCode())) {
			sql.append(" and e.CompanyCode = ? ");
			parameterList.add(eventVO.getCompanyCode());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得告警資訊
	 * 
	 * @param eventVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getAlertInfo(EventVO eventVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" e.SeqNo, ");//-- 告警序號
		sql.append(" e.EventType, ");//-- 告警類型
		sql.append(" c.CompanyName, ");
		sql.append(" e.Country, ");//-- 國家
		sql.append(" e.Area, ");//-- 地域
		sql.append(" e.GroupID, ");//-- 基地台號碼
		sql.append(" e.GroupName, ");//-- 基地台名稱
		sql.append(" e.Address, ");//-- 地址
		sql.append(" e.NBID, ");//-- 通訊板序號
		sql.append(" e.BatteryID, ");
		sql.append(" e.InstallDate, ");//-- 安裝日期
		sql.append(" e.BatteryTypeName, ");//-- 電池型號
		sql.append(" br.RecTime, ");//-- 數據更新時間
		sql.append(" br.Category, ");
		sql.append(" br.OrderNo, ");
		sql.append(" br.Value, ");
		sql.append(" br.Status, ");
		sql.append(" e.CreateTime, ");//-- 發生時間
		sql.append(" e.CloseTime, ");//-- 解決時間
		sql.append(" e.CloseUser, ");//-- 解決人員
		sql.append(" e.CloseContent, ");//-- 解決方案
		sql.append(" e.EventStatus, ");//-- 告警狀態
		sql.append(" e.IMPType, ");
		sql.append(" e.Alert1, ");//-- 判定值1
		sql.append(" e.Alert2, ");//-- 判定值2
		sql.append(" e.Disconnect, ");//判斷斷線時間(秒)
		sql.append(" e.Temperature1, ");
		sql.append(" e.TimeZone, ");
		sql.append(" e.Lng, ");//-- 經度
		sql.append(" e.Lat ");//-- 緯度
		sql.append(" from Company c, ");
		sql.append("      Event e ");
		sql.append("      inner join BatteryRecord br ");
		sql.append("      on br.NBID = e.NBID ");
		sql.append("      and br.BatteryID = e.BatteryID ");
		sql.append("      and br.RecTime = e.RecordTime, ");
		sql.append("      Battery b ");
		sql.append(" where c.CompanyCode = e.CompanyCode ");		
		if(StringUtils.isNotBlank(eventVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(eventVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(eventVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+eventVO.getCompanyCodeArr()+") ");
		}
		if(StringUtils.isNotBlank(eventVO.getStartDate())) {
			sql.append(" and e.RecordTime >=  STR_TO_DATE(?,'%Y-%m-%d %H:%i')  ");
			parameterList.add(eventVO.getStartDate());
		}	
		if(StringUtils.isNotBlank(eventVO.getEndDate())) {
			sql.append(" and e.RecordTime <=  STR_TO_DATE(?,'%Y-%m-%d %H:%i')  ");
			parameterList.add(eventVO.getEndDate());
		}	
		if(StringUtils.isNotBlank(eventVO.getEventStatus())) {
			sql.append(" and e.EventStatus = ? ");
			parameterList.add(eventVO.getEventStatus());
		}
		if(StringUtils.isNotBlank(eventVO.getEventTypeArr())) {
			sql.append(" and e.EventType in ("+eventVO.getEventTypeArr()+") ");
		}

		if(StringUtils.isNotBlank(eventVO.getCountryArr())) {
			sql.append(" and e.Country in ("+eventVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(eventVO.getAreaArr())) {
			sql.append(" and e.Area in ("+eventVO.getAreaArr()+") ");
		}
		if(StringUtils.isNotBlank(eventVO.getGroupIdArr())) {
			sql.append(" and concat(e.GroupName,e.GroupID) in ("+eventVO.getGroupIdArr()+") ");
		}
		sql.append(" and b.NBID = e.NBID  ");
		sql.append(" and b.BatteryID = e.BatteryID  ");
		if(StringUtils.isNotBlank(eventVO.getBatteryGroupIdArr())) {
			sql.append(" and b.SeqNo in ("+eventVO.getBatteryGroupIdArr()+") ");
		}
		if(StringUtils.isNotBlank(eventVO.getBattInternalId())) {
			sql.append(" and b.SeqNo = ? ");
			parameterList.add(eventVO.getBattInternalId());
		}
		
		sql.append(" order by e.RecordTime desc, e.CloseTime desc, e.Country, e.Area, e.GroupID, e.NBID, e.BatteryID, br.Category, br.OrderNo ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 關閉告警
	 * @param eventVO
	 * @throws Exception
	 */
	public void closeEvent(EventVO eventVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update Event set ");
		sql.append(" CloseTime = ?, ");
		sql.append(" CloseUser = ?, ");
		sql.append(" CloseContent = ?, ");
		sql.append(" EventStatus = 6 ");
		sql.append(" where SeqNo in ("+eventVO.getEventSeqArr()+") ");

		parameterList.add(eventVO.getCloseTime());
		parameterList.add(eventVO.getUserName());
		parameterList.add(eventVO.getCloseContent());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢通訊序號是否有事件
	 * @param nbid
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> checkEvent(String nbid) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from Event where NBID = ? ");
		sql.append(" and EventStatus = 5 ");
		parameterList.add(nbid);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台篩選選單(有電池組ID的)
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupListForAlert(EventVO eventVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct ");
		sql.append(" c.CompanyCode, ");
		sql.append(" c.CompanyName, ");
		sql.append(" e.Country, ");
		sql.append(" e.Area, ");
		sql.append(" e.GroupID, ");
		sql.append(" e.GroupName ");
		sql.append(" from Company c, Event e ");
		sql.append(" where c.CompanyCode = e.CompanyCode  ");
		if(StringUtils.isNotBlank(eventVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(eventVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(eventVO.getEventStatus())) {
			sql.append(" and e.EventStatus = ? ");
			parameterList.add(eventVO.getEventStatus());
		}
		sql.append(" order by c.CompanyName ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
}
