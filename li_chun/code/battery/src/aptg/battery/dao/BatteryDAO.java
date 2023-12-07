package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.util.JdbcUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;

public class BatteryDAO extends BaseDAO {
	
	/**
	 * 取得電池群組資訊
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryGroup(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" c.CompanyName, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" bg.Country, ");
		sql.append(" bg.Area, ");
		sql.append(" bg.GroupID, ");
		sql.append(" bg.GroupName, ");
		sql.append(" bg.Lng, ");
		sql.append(" bg.Lat, ");
		sql.append(" nb.NBID, ");
		sql.append(" nb.GroupInternalID, ");
		sql.append(" bs.RecTime, ");
		sql.append(" bs.TimeZone, ");
		sql.append(" bs.MaxIR, ");
		sql.append(" bs.MinIR, ");
		sql.append(" bs.MaxVol, ");
		sql.append(" bs.MinVol, ");
		sql.append(" bs.Temperature, ");
		sql.append(" bs.Status, ");
		sql.append(" e.EventType, ");
		sql.append(" c.Disconnect, ");
		sql.append(" bg.DefaultGroup ");
		sql.append(" from Company c, ");
		sql.append("      BatteryGroup bg, ");
		sql.append("      NBList nb, ");
		sql.append("      NBGroupHis nh, ");
		sql.append("      Battery b ");		
		sql.append(" left join Event e  ");
		sql.append(" on e.NBID = b.NBID ");
		sql.append(" and e.BatteryID = 0 ");
		sql.append(" and e.EventType = 4 ");
		sql.append(" and e.EventStatus = 5, ");
		sql.append(" BattMaxRecTime m, ");
		sql.append(" BatteryRecordSummary bs ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		sql.append(" and c.CompanyCode = bg.CompanyCode  ");
		
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+batteryVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+batteryVO.getAreaArr()+") ");
		}
		
		
		sql.append(" and bg.SeqNo = nb.GroupInternalID ");
		if(batteryVO.isMap()) {
			sql.append(" and bg.Address is not null ");
		}
		sql.append(" and nb.Active = 13 ");
		sql.append(" and nb.NBID = nh.NBID ");		
		sql.append(" and nh.GroupInternalID = bg.SeqNo ");
		sql.append(" and nb.NBID = b.NBID ");
		
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		sql.append(" and bs.NBID = m.NBID  ");
		sql.append(" and bs.BatteryID = m.BatteryID  ");
		sql.append(" and bs.RecTime = m.MaxRecTime ");
		
		sql.append(" order by c.CompanyCode, bs.RecTime desc, bg.GroupID ");
	
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電池組資訊
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBattery(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" c.CompanyCode, ");
		sql.append(" c.CompanyName, ");
		sql.append(" bg.Country,");
		sql.append(" bg.Area, ");
		sql.append(" bg.GroupID, ");
		sql.append(" bg.GroupName, ");
		sql.append(" bg.Address, ");
		sql.append(" nb.GroupInternalID, ");
		sql.append(" nb.NBID, ");
		sql.append(" b.BatteryID, ");
		sql.append(" b.SeqNo, ");
		sql.append(" b.InstallDate, ");
		sql.append(" bt.BatteryTypeName, ");
		sql.append(" br.RecTime, ");
		sql.append(" br.TimeZone, ");
		sql.append(" br.Category, ");
		sql.append(" br.OrderNo, ");
		sql.append(" br.Value, ");
		sql.append(" br.Status, ");
		sql.append(" e.EventType, ");
		sql.append(" c.Disconnect ");
		sql.append(" from Company c, ");
		sql.append("      BatteryGroup bg, ");
		sql.append("      NBList nb, ");
		sql.append("      NBGroupHis nh, ");		
		sql.append("      Battery b ");
		sql.append(" left join Event e  ");
		sql.append(" on e.NBID = b.NBID ");
		sql.append(" and e.BatteryID = 0 ");
		sql.append(" and e.EventType = 4 ");
		sql.append(" and e.EventStatus = 5 ");
		sql.append(" left join BatteryTypeList bt on bt.BatteryTypeCode = b.BatteryTypeCode, ");
		sql.append(" BattMaxRecTime m, ");
		sql.append(" BatteryRecord br ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		sql.append(" and c.CompanyCode = bg.CompanyCode  ");
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalID())) {
			sql.append(" and bg.SeqNo = ? ");
			parameterList.add(batteryVO.getGroupInternalID());
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+batteryVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+batteryVO.getAreaArr()+") ");
		}
			
		sql.append(" and bg.SeqNo = nb.GroupInternalID ");
		sql.append(" and nb.Active = 13  ");
		sql.append(" and nb.NBID = nh.NBID ");
		sql.append(" and nh.GroupInternalID = bg.SeqNo ");
		sql.append(" and nb.NBID = b.NBID ");
		
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		sql.append(" and br.NBID = m.NBID  ");
		sql.append(" and br.BatteryID = m.BatteryID  ");
		sql.append(" and br.RecTime = m.MaxRecTime ");
//		sql.append(" and br.RecTime between nh.Starttime and nh.Endtime ");
		
		if(StringUtils.isNotBlank(batteryVO.getBatteryGroupIdArr())) {
			sql.append(" and b.SeqNo in ("+batteryVO.getBatteryGroupIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getBatteryId())) {
			sql.append(" and b.BatteryID = ? ");
			parameterList.add(batteryVO.getBatteryId());
		}
		
//		sql.append(" order by bg.Country, bg.Area, bg.GroupID, nb.NBID, br.BatteryID, br.Category, br.OrderNo ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("country"));
			}
		}).thenComparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("area"));
			}
		}).thenComparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("groupid"));
			}
		}).thenComparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("nbid"));
			}
		}).thenComparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("batteryid"));
			}
		}).thenComparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("category"));
			}
		}).thenComparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("orderno"));
			}
		})
			).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 取得電池歷史檢核
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryHistoryCheck(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ");
		sql.append(" nb.NBID, ");
		sql.append(" b.BatteryID, ");
		sql.append(" b.SeqNo ");
		sql.append(" from BatteryGroup bg, ");
		sql.append("      NBList nb, ");
		sql.append("      Battery b ");
					
		sql.append(" where 1=1 ");		
		if(StringUtils.isNotBlank(batteryVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+batteryVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+batteryVO.getAreaArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupIdArr())) {
			sql.append(" and bg.GroupID in ("+batteryVO.getGroupIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupNameArr())) {
			sql.append(" and bg.GroupName in ("+batteryVO.getGroupNameArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		sql.append(" and bg.SeqNo = nb.GroupInternalID ");
		sql.append(" and nb.NBID = b.NBID ");		
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and bg.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and bg.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		
		if(StringUtils.isNotBlank(batteryVO.getBatteryGroupIdArr())) {
			sql.append(" and b.SeqNo in ("+batteryVO.getBatteryGroupIdArr()+") ");
		}
		
								
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電池歷史電池組資訊
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryHistoryHeader(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select    ");
		sql.append(" bg.SeqNo, ");
		sql.append(" bg.Country,  ");
		sql.append(" bg.Area,  ");
		sql.append(" bg.GroupID,  ");
		sql.append(" bg.GroupName,  ");
		sql.append(" bg.Address,  ");
		sql.append(" bg.Lng,  ");
		sql.append(" bg.Lat,  ");
		sql.append(" nb.NBID, ");
		sql.append(" b.BatteryID,  ");
		sql.append(" b.InstallDate,  ");
		sql.append(" bt.BatteryTypeName, ");
		sql.append(" nb.ContinuousSeqNo ");
		sql.append(" from BatteryGroup bg,  ");
		sql.append("      NBList nb, ");
		sql.append("      Battery b  ");
		sql.append("      left join BatteryTypeList bt on bt.BatteryTypeCode = b.BatteryTypeCode ");
		sql.append(" where 1=1 	 ");
		sql.append(" and bg.SeqNo = nb.GroupInternalID  ");
		sql.append(" and nb.NBID = b.NBID ");
		sql.append(" and b.SeqNo = ? ");
		parameterList.add(batteryVO.getBattInternalId());
								
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電池歷史資訊
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryHistory(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select   ");
		sql.append(" br.NBID, ");
		sql.append(" br.BatteryID, ");
		sql.append(" br.RecTime, ");
		sql.append(" br.TimeZone, ");
		sql.append(" br.Category, ");
		sql.append(" br.OrderNo, ");
		sql.append(" br.Value, ");
		sql.append(" br.Status ");
		sql.append(" from BatteryRecord br ");
		sql.append(" where br.NBID = ? ");
		parameterList.add(batteryVO.getNbId());
		sql.append(" and br.BatteryID = ? ");
		parameterList.add(batteryVO.getBatteryId());
		if(StringUtils.isNotBlank(batteryVO.getStartDate())) {
			sql.append(" and br.RecTime >=  STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s')  ");
			parameterList.add(batteryVO.getStartDate());
		}	
		if(StringUtils.isNotBlank(batteryVO.getEndDate())) {
			sql.append(" and br.RecTime <=  STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s')  ");
			parameterList.add(batteryVO.getEndDate());
		}	
		
		sql.append(" order by br.RecTime "+ (batteryVO.isRecTimeDesc()?"desc":"asc"));
		sql.append(" , br.Category, br.OrderNo ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得群組狀態
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupStatusNow(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" nb.GroupInternalID, ");
		sql.append(" bs.RecTime, ");
		sql.append(" bs.TimeZone, ");
		sql.append(" bs.Status, ");
		sql.append(" e.EventType, ");
		sql.append(" c.Disconnect ");
		sql.append(" from Company c, ");
		sql.append("      BatteryGroup bg, ");
		sql.append("      NBList nb, ");
		sql.append("      NBGroupHis nh, ");		
		sql.append("      Battery b ");		
		sql.append(" left join Event e  ");
		sql.append(" on e.NBID = b.NBID ");
		sql.append(" and e.BatteryID = 0 ");
		sql.append(" and e.EventType = 4 ");
		sql.append(" and e.EventStatus = 5, ");
		sql.append(" BattMaxRecTime m, ");
		sql.append(" BatteryRecordSummary bs ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
				
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		sql.append(" and c.CompanyCode = bg.CompanyCode  ");
		sql.append(" and bg.SeqNo = nb.GroupInternalID  ");
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+batteryVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+batteryVO.getAreaArr()+") ");
		}
		
		sql.append(" and nb.Active = 13 ");		
		sql.append(" and nb.NBID = nh.NBID ");
		sql.append(" and nh.GroupInternalID = bg.SeqNo ");
		sql.append(" and nb.NBID = b.NBID ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		sql.append(" and bs.NBID = m.NBID  ");
		sql.append(" and bs.BatteryID = m.BatteryID  ");
		sql.append(" and bs.RecTime = m.MaxRecTime ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("groupinternalid"));
			}
		})).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 取得群組狀態
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupStatus(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select g.RecDate, Status, count(1) as count ");
		sql.append(" from BatteryGroup b, GroupDailyStatus g ");
		sql.append(" where b.SeqNo = g.GroupInternalID ");
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and b.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and b.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and b.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getCountryArr())) {
			sql.append(" and b.Country in ("+batteryVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getAreaArr())) {
			sql.append(" and b.Area in ("+batteryVO.getAreaArr()+") ");
		}
		
		if(StringUtils.isNotBlank(batteryVO.getStartDate())) {
			sql.append(" and g.RecDate >= STR_TO_DATE(?,'%Y-%m-%d') ");
			parameterList.add(batteryVO.getStartDate());
		}
		if(StringUtils.isNotBlank(batteryVO.getEndDate())) {
			sql.append(" and g.RecDate < STR_TO_DATE(?,'%Y-%m-%d')+1 ");
			parameterList.add(batteryVO.getEndDate());
		}
		sql.append(" group by g.RecDate, g.Status ");		
		sql.append(" order by g.RecDate ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	
	/**
	 * 國家選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getCountryList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct Country from BatteryGroup where 1=1 ");
		sql.append(" and DefaultGroup = 1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 地域選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getAreaList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct Area from BatteryGroup where 1=1 ");
		sql.append(" and DefaultGroup = 1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台編號選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupIdList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct GroupID from BatteryGroup where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupID())) {
			sql.append(" and GroupID like ? ");
			parameterList.add("%"+batteryVO.getGroupID()+"%");
		}
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台名稱選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupNameList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct GroupName from BatteryGroup where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupName())) {
			sql.append(" and GroupName like ? ");
			parameterList.add("%"+batteryVO.getGroupName()+"%");
		}
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電池組ID選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryGroupIdList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct b.SeqNo, b.NBID, b.BatteryID ");
		sql.append(" from BatteryGroup g,  ");
		sql.append(" NBList n, ");
		sql.append(" NBGroupHis nh, ");
		sql.append(" Battery b, ");
		sql.append(" BattMaxRecTime m ");
		sql.append(" where g.SeqNo = n.GroupInternalID ");
		sql.append(" and g.SeqNo = nh.GroupInternalID ");
		sql.append(" and n.Active = 13 ");
		sql.append(" and n.NBID = nh.NBID ");
		sql.append(" and n.NBID = b.NBID ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and g.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getBatteryId())) {
			sql.append(" and b.BatteryID = ? ");
			parameterList.add(batteryVO.getBatteryId());
		}
		if(StringUtils.isNotBlank(batteryVO.getGroupInternalIdArr())) {
			sql.append(" and g.SeqNo in ("+batteryVO.getGroupInternalIdArr()+") ");
		}
		sql.append(" order by b.NBID, b.BatteryID ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得電池組管理列表
	 * 
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBattManage(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select   ");
		sql.append(" c.CompanyName, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" nb.NBID, ");//-- 通訊板序號
		sql.append(" b.BatteryID, ");
		sql.append(" b.SeqNo, ");
		sql.append(" b.InstallDate, ");//-- 安裝日期
		sql.append(" bt.BatteryTypeCode, ");//-- 電池型號代碼
		sql.append(" bt.BatteryTypeName ");//-- 電池型號
		sql.append(" from Company c, ");
		sql.append("      BatteryGroup bg, ");
		sql.append("      NBList nb, ");
		sql.append("      Battery b ");
		sql.append(" left join BatteryTypeList bt ON bt.BatteryTypeCode = b.BatteryTypeCode ");
		sql.append(" where 1=1 ");
		sql.append(" and c.CompanyCode = bg.CompanyCode ");
		sql.append(" and bg.SeqNo = nb.GroupInternalID ");
		sql.append(" and nb.NBID = b.NBID ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryVO.getCompanyCodeArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryVO.getBatteryGroupIdArr())) {
			sql.append(" and b.SeqNo in ("+batteryVO.getBatteryGroupIdArr()+")  ");
		}
		
		if(StringUtils.isNotBlank(batteryVO.getStartDate())) {
			sql.append(" and b.InstallDate >=  STR_TO_DATE(?,'%Y-%m-%d')  ");
			parameterList.add(batteryVO.getStartDate());
		}
		if(StringUtils.isNotBlank(batteryVO.getEndDate())) {
			sql.append(" and b.InstallDate <  STR_TO_DATE(?,'%Y-%m-%d')+1  ");
			parameterList.add(batteryVO.getEndDate());
		}
		if("1".equals(batteryVO.getInstallDateNull())) {
			sql.append(" and b.InstallDate is null  ");
		}
		sql.append(" order by  nb.NBID, b.BatteryID, b.InstallDate ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public void updBattery(BatteryVO batteryVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);

			if(batteryVO.isAddBattTypeFlag()) {
				StringBuffer sql1 = new StringBuffer();
				sql1.append(" insert into BatteryTypeList ( ");
				sql1.append(" CompanyCode, ");
				sql1.append(" BatteryTypeName, ");
				sql1.append(" CreateUserName, ");
				sql1.append(" UpdateUserName ");
				sql1.append(" ) values (?,?,?,?) ");
						
				ps1 = connection.prepareStatement(sql1.toString(), Statement.RETURN_GENERATED_KEYS);				
				ps1.setString(1, batteryVO.getCompanyCode());
				ps1.setString(2, batteryVO.getBatteryTypeName());
				ps1.setString(3, batteryVO.getUserName());
				ps1.setString(4, batteryVO.getUserName());
				ps1.executeUpdate();
			
				rs = ps1.getGeneratedKeys();				
				while(rs.next()){         
					int batteryTypeCode = rs.getInt(1);
					batteryVO.setBatteryTypeCode(String.valueOf(batteryTypeCode));		            
				}
			}
			
			StringBuffer sql = new StringBuffer();			
			sql.append(" update Battery set ");
			sql.append(" InstallDate = ?,  ");
			sql.append(" BatteryTypeCode = ?, ");
			sql.append(" UpdateUserName = ? ");
			sql.append(" where NBID = ? ");
			sql.append(" and BatteryID = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, batteryVO.getInstallDate());
			ps.setString(2, batteryVO.getBatteryTypeCode());
			ps.setString(3, batteryVO.getUserName());
			ps.setString(4, batteryVO.getNbId());
			ps.setString(5, batteryVO.getBatteryId());
			ps.execute();		
				
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps1);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 電池組ID設定
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatterySetup(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.ResponseTime, b.*  ");
		sql.append(" from Battery b  ");
		sql.append(" left join Command c ");
		sql.append(" on c.SeqNo = (select max(a.SeqNo) ");
		sql.append(" from Command a, ");
		sql.append(" CommandTask t ");
		sql.append(" where a.TaskID = t.TaskID ");
		sql.append(" and a.NBID = b.NBID  ");
		sql.append(" and a.BatteryID = b.BatteryID ");
		sql.append(" and a.ResponseCode = 0 ");//成功
		sql.append(" and t.CommandID = ? ");
		sql.append(" ) ");
		sql.append(" where b.NBID = ? ");
		sql.append(" and b.BatteryID = ? ");
		parameterList.add(batteryVO.getCommandID());
		parameterList.add(batteryVO.getNbId());
		parameterList.add(batteryVO.getBatteryId());
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電池組ID明細設定
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryDetailSetup(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select co.count, c.ResponseTime, d.*  ");
		sql.append(" from Battery b ");
		sql.append(" left join Command c ");
		sql.append(" on c.SeqNo = (select max(a.SeqNo)  ");
		sql.append(" from Command a, ");
		sql.append(" CommandTask t ");
		sql.append(" where a.TaskID = t.TaskID  ");
		sql.append(" and a.NBID = b.NBID  ");
		sql.append(" and a.BatteryID = b.BatteryID  ");
		sql.append(" and a.ResponseCode = 0  ");
		sql.append(" and t.CommandID = ?) ");
		sql.append(" left join BatteryDetail d ");
		sql.append(" on b.NBID = d.NBID  ");
		sql.append(" and b.BatteryID = d.BatteryID ");
		sql.append(" and d.Category = ?, ");		
		sql.append(" (select r.NBID, r.BatteryID, count(1) as count ");
		sql.append(" from BattMaxRecTime m, BatteryRecord r ");
		sql.append(" where m.NBID = ?  ");
		sql.append(" and m.BatteryID = ? ");
		sql.append(" and r.Category = ? ");
		sql.append(" and r.NBID = m.NBID ");
		sql.append(" and r.BatteryID = m.BatteryID ");
		sql.append(" and r.RecTime = m.MaxRecTime ");
		sql.append(" ) co ");
		
		
		sql.append(" where b.NBID = ? ");
		sql.append(" and  b.BatteryID = ? ");
		sql.append(" and b.NBID = co.NBID  ");
		sql.append(" and b.BatteryID = co.BatteryID ");
//		sql.append(" order by OrderNo ");
		
		parameterList.add(batteryVO.getCommandID());
		parameterList.add(batteryVO.getCategory());
		parameterList.add(batteryVO.getNbId());
		parameterList.add(batteryVO.getBatteryId());
		parameterList.add(batteryVO.getCategory());
		parameterList.add(batteryVO.getNbId());
		parameterList.add(batteryVO.getBatteryId());
		
			
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("orderno"));
			}
		})).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 取得電池組公司
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryCompany(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select bg.CompanyCode  ");
		sql.append(" from BatteryGroup bg, ");
		sql.append(" NBList n, ");
		sql.append(" Battery b ");
		sql.append(" where bg.SeqNo = n.GroupInternalID ");
		sql.append(" and n.NBID = b.NBID ");
		sql.append(" and b.NBID = ? ");
		sql.append(" and b.BatteryID = ?  ");
	
		parameterList.add(batteryVO.getNbId());
		parameterList.add(batteryVO.getBatteryId());
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台設定選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupSetupList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct SeqNo GroupName from BatteryGroup where 1=1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台篩選選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.CompanyName, b.* from BatteryGroup b, Company c  ");
		sql.append(" where b.CompanyCode = c.CompanyCode ");
//		sql.append(" and b.DefaultGroup = 1 ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and b.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		sql.append(" order by c.CompanyName ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台篩選選單(有電池組ID的)
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupListForBatt(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.CompanyName, g.* ");
		sql.append(" from Company c, BatteryGroup g, NBList n, Battery b ");
		sql.append(" where c.CompanyCode = g.CompanyCode  ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and g.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		sql.append(" and g.SeqNo = n.GroupInternalID ");
		sql.append(" and n.NBID = b.NBID ");
		sql.append(" order by c.CompanyName ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢電池組狀態
	 * @param nbid
	 * @param batteryId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryStatus(String nbid, String batteryId) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select    ");
		sql.append(" nb.NBID, ");
		sql.append(" b.BatteryID, ");
		sql.append(" br.RecTime,  ");
		sql.append(" br.Status,   ");
		sql.append(" c.Disconnect  ");
		sql.append(" from Company c,  ");
		sql.append(" BatteryGroup bg, ");
		sql.append(" NBList nb, ");
		sql.append(" NBGroupHis nh, 		 ");
		sql.append(" Battery b,  ");
		sql.append(" BattMaxRecTime m, ");
		sql.append(" BatteryRecordSummary br ");
//		sql.append(" inner join BatteryRecordSummary br  ");
//		sql.append(" on br.NBID = b.NBID  ");
//		sql.append(" and br.BatteryID = b.BatteryID  ");
//		sql.append(" and br.RecTime = (select max(r.RecTime) from BatteryRecordSummary r ");
//		sql.append(" where  r.NBID = b.NBID and r.BatteryID = b.BatteryID) ");
		sql.append(" where c.CompanyCode = bg.CompanyCode  ");
		sql.append(" and bg.SeqNo = nb.GroupInternalID ");
		sql.append(" and nb.Active = 13   ");
		sql.append(" and nb.NBID = nh.NBID  ");
		if(StringUtils.isNotBlank(nbid)) {
			sql.append(" and nb.NBID = ? ");
			parameterList.add(nbid);
		}	
		sql.append(" and nb.NBID = b.NBID  ");
		if(StringUtils.isNotBlank(batteryId)) {
			sql.append(" and b.BatteryID = ? ");
			parameterList.add(batteryId);
		}
		sql.append(" and nh.GroupInternalID = bg.SeqNo ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and br.NBID = m.NBID  ");
		sql.append(" and br.BatteryID = m.BatteryID  ");
		sql.append(" and br.RecTime = m.MaxRecTime ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime  ");
		
		sql.append(" order by br.RecTime desc ");
	
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電池組ID選單
	 * @param batteryVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct c.CompanyName, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" g.Country, ");
		sql.append(" g.Area, ");
		sql.append(" g.GroupName, ");
		sql.append(" g.GroupId, ");
		sql.append(" n.GroupInternalID, ");
		sql.append(" b.SeqNo, ");
		sql.append(" b.NBID, ");
		sql.append(" b.BatteryID ");
		sql.append(" from BatteryGroup g,  ");
		sql.append(" NBList n, ");
		sql.append(" NBGroupHis nh, ");
		sql.append(" Battery b, ");
		sql.append(" BattMaxRecTime m, ");
		sql.append(" Company c ");
		sql.append(" where g.SeqNo = n.GroupInternalID ");
		sql.append(" and n.Active = 13 ");
		sql.append(" and n.NBID = nh.NBID ");
		sql.append(" and n.NBID = b.NBID ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and g.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		sql.append(" and g.CompanyCode = c.CompanyCode ");
		sql.append(" order by b.NBID, b.BatteryID ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 電池組管理篩選選單
	 * @param batteryVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBattManageList(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct c.CompanyName, c.CompanyCode, b.SeqNo, b.NBID, b.BatteryID ");
		sql.append(" from Company c, ");
		sql.append(" BatteryGroup g, ");
		sql.append(" NBList n, ");
		sql.append(" Battery b ");
		sql.append(" where c.CompanyCode = g.CompanyCode ");
		sql.append(" and g.SeqNo = n.GroupInternalID ");
		sql.append(" and n.Active = 13 ");
		sql.append(" and n.NBID = b.NBID ");
		if(StringUtils.isNotBlank(batteryVO.getCompanyCode())) {
			sql.append(" and g.CompanyCode = ? ");
			parameterList.add(batteryVO.getCompanyCode());
		}
		
		sql.append(" order by c.CompanyName, b.NBID, b.BatteryID ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢電池組ID
	 * @param batteryVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBattInfo(BatteryVO batteryVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select SeqNo, concat(NBID, '_', BatteryID) as BatteryGropId from Battery  ");
		sql.append(" where SeqNo in ("+batteryVO.getBatteryGroupIdArr()+") ");
		sql.append(" order by NBID, BatteryID ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
}
