package aptg.battery.util;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.BatteryDAO;
import aptg.battery.dao.BatteryTypeListDAO;
import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.EventDAO;
import aptg.battery.dao.LanguageListDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.dao.RoleDAO;
import aptg.battery.dao.TimeZoneListDAO;
import aptg.battery.vo.BatteryTypeListVO;
import aptg.battery.vo.BatteryVO;
import aptg.battery.vo.CompanyVO;
import aptg.battery.vo.EventVO;
import aptg.battery.vo.NbListVO;
import aptg.battery.vo.RoleVO;

public class ListUtil {

	/**
	 * 公司下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getCompanyList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		CompanyVO companyVO = new CompanyVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			companyVO.setCompanyCode(userCompanyCode);
		CompanyDAO companyDAO = new CompanyDAO();
		List<DynaBean> rows = companyDAO.getCompanyInfo(companyVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("companycode"));
				object.put("Label", bean.get("companyname"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 公司下拉選單(業主條件)
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getCompanyList(String userCompanyCode, String admin) throws Exception {
		JSONArray list = new JSONArray();
		CompanyVO companyVO = new CompanyVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			companyVO.setCompanyCode(userCompanyCode);
		companyVO.setAdmin(admin);
		CompanyDAO companyDAO = new CompanyDAO();
		List<DynaBean> rows = companyDAO.getCompanyInfo(companyVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("companycode"));
				object.put("Label", bean.get("companyname"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 國家下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getCountryList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getCountryList(batteryVO);	
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("country"));
				object.put("Label", bean.get("country"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 地域下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getAreaList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getAreaList(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("area"));
				object.put("Label", bean.get("area"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 站台號碼下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getGroupIdList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getGroupIdList(batteryVO);	
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("groupid"));
				object.put("Label", bean.get("groupid"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 站台名稱下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getGroupNameList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getGroupNameList(batteryVO);	
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("groupname"));
				object.put("Label", bean.get("groupname"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 通訊序號下拉選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getNBIDList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		NbListVO nbListVO = new NbListVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			nbListVO.setCompanyCode(userCompanyCode);
		NbListDAO nbListDAO = new NbListDAO();
		List<DynaBean> rows = nbListDAO.getNBIDList(nbListVO);	
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("NBIDLabel", bean.get("nbid"));
				object.put("NBIDValue", bean.get("nbid"));				
				list.put(object);
			}
		}
		return list;
	}

	/**
	 * 電池組ID選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getBatteryGroupIdList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getBatteryGroupIdList(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("seqno"));
				object.put("Label", bean.get("nbid")+"_"+bean.get("batteryid"));				
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 電池組ID選單(命令)
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getCommandBattIdList(String groupInternalId, String commandId) throws Exception {
		JSONArray list = new JSONArray();
		if(StringUtils.isNotBlank(groupInternalId)) {
			BatteryVO batteryVO = new BatteryVO();
			batteryVO.setGroupInternalIdArr(ToolUtil.strToSqlStr(groupInternalId));
			if("BA".equals(commandId) || "BB".equals(commandId)) {
				batteryVO.setBatteryId("0");
			}
			BatteryDAO batteryDAO = new BatteryDAO();
			List<DynaBean> rows = batteryDAO.getBatteryGroupIdList(batteryVO);
			if (rows != null && !rows.isEmpty()) {
				for (DynaBean bean : rows) {
					JSONObject object = new JSONObject();
					object.put("Value", bean.get("nbid")+"_"+bean.get("batteryid"));
					object.put("Label", bean.get("nbid")+"_"+bean.get("batteryid"));				
					list.put(object);
				}
			}
		}
		return list;
	}

	/**
	 * 電池型號下拉選單(無指定)
	 * 
	 * @param userCompanyCode
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getBattTypeListForN(String userCompanyCode, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			batteryTypeListVO.setCompanyCode(userCompanyCode);
		BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
		List<DynaBean> rows = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString("1081"));// 無指定
			object.put("Value", "N");
			list.put(object);
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("batterytypecode"));// 電池型號代碼
				object.put("Label", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 電池型號下拉選單
	 * 
	 * @param userCompanyCode
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getBattTypeList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			batteryTypeListVO.setCompanyCode(userCompanyCode);
		BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
		List<DynaBean> rows = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
		if (rows != null && !rows.isEmpty()) {			
			for (DynaBean bean : rows) {
				JSONObject object = new JSONObject();
				object.put("Value", bean.get("batterytypecode"));// 電池型號代碼
				object.put("Label", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 命令下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getCommandIdList(String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		String[] command = {"BB","BA","B5","B3"};
		String[] commandId = {"187","186","181","179"};
		for (int i=0; i<command.length; i++) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString(commandId[i]));
			object.put("I18NCode", commandId[i]);
			object.put("Value", command[i]);
			list.put(object);
		}		
		return list;
	}
	
	/**
	 * 回應訊息下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getResponseList(String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		String[] resp = {"18","19","1077"};
		String[] respCode = {"0","1","N"};
		for (int i=0; i<resp.length; i++) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString(resp[i]));
			object.put("I18NCode", resp[i]);
			object.put("Value", respCode[i]);
			list.put(object);
		}
		return list;
	}
	
	/**
	 * 異動項目下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getModifyItemList(String userCompanyCode, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		String[] item;
		if(ToolUtil.checkAdminCompany(userCompanyCode)) {
			item = new String[]{"9","10"};
		}else {
			item = new String[]{"7","8","9","10","11","12"}; 
		}
			
		JSONArray list = new JSONArray();
		for (int i=0; i<item.length; i++) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString(item[i]));
			object.put("I18NCode", item[i]);
			object.put("Value", item[i]);
			
			list.put(object);
		}
		return list;
	}
	
	/**
	 * 告警下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getAlertList(String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		String[] alert = {"3","4","25"};
		for (int i=0; i<alert.length; i++) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString(alert[i]));
			object.put("I18NCode", alert[i]);
			object.put("Value", alert[i]);
			list.put(object);
		}
		return list;
	}
	
	/**
	 * 角色下拉選單
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getRoleList(String roleRank) throws Exception {
		int rank = ToolUtil.parseInt(roleRank);
		JSONArray list = new JSONArray();
		RoleVO roleVO = new RoleVO();
		roleVO.setSystemId(ToolUtil.getSystemId());
		if(rank<80) {
			roleVO.setRoleRankLT(roleRank);
		}else {
			roleVO.setRoleRankLE(roleRank);	
		}		
		RoleDAO roleDAO = new RoleDAO();
		List<DynaBean> rows = roleDAO.getRoleList(roleVO);
		if (rows != null && !rows.isEmpty()) {
			for (DynaBean bean : rows) {
				JSONObject object = new JSONObject();
				object.put("Label", bean.get("rolename"));// 角色名稱
				object.put("Value", bean.get("roleid"));// 角色ID

				list.put(object);				
			}
		}
		return list;
	}
	
	/**
	 * 內阻呈顯下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getIMPTypeList(String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		String[] imptype = {"20", "21", "22"};
		for (int i=0; i<imptype.length; i++) {
			JSONObject object = new JSONObject();
			object.put("Label", resource.getString(imptype[i]));
			object.put("Value", imptype[i]);
			list.put(object);
		}
		return list;
	}
	
	/**
	 * 語系下拉選單
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getLanguageList() throws Exception {
		JSONArray list = new JSONArray();
		LanguageListDAO languageListDAO = new LanguageListDAO();
		List<DynaBean> rows = languageListDAO.getLanguageList();
		if (rows != null && !rows.isEmpty()) {
			for (DynaBean bean : rows) {
				JSONObject object = new JSONObject();
				object.put("Value", bean.get("languagecode"));
				object.put("Label", bean.get("languagedesc"));

				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 時區下拉選單
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getTimeZoneList() throws Exception {
		JSONArray list = new JSONArray();
		TimeZoneListDAO timeZoneListDAO = new TimeZoneListDAO();
		List<DynaBean> rows = timeZoneListDAO.getTimeZoneList();
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("Value", bean.get("timezone"));
				object.put("Label", bean.get("timezonedesc"));

				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 公司/國家/地域/站台下拉選單
	 * @param userCompanyCode
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getGroupList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getGroupList(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();				
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("CountryLabel", ObjectUtils.toString(bean.get("country")));
				object.put("CountryValue", ObjectUtils.toString(bean.get("country")));
				object.put("AreaLabel", ObjectUtils.toString(bean.get("area")));
				object.put("AreaValue", ObjectUtils.toString(bean.get("area")));
				object.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				object.put("GroupValue", bean.get("seqno"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 公司/國家/地域/站台下拉選單(有電池組ID的)
	 * @param userCompanyCode
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getGroupListForBatt(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getGroupListForBatt(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();				
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("CountryLabel", ObjectUtils.toString(bean.get("country")));
				object.put("CountryValue", ObjectUtils.toString(bean.get("country")));
				object.put("AreaLabel", ObjectUtils.toString(bean.get("area")));
				object.put("AreaValue", ObjectUtils.toString(bean.get("area")));
				object.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));				
				object.put("GroupValue", bean.get("seqno"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 公司/國家/地域/站台下拉選單(有電池組ID的)
	 * @param userCompanyCode
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getGroupListForAlert(String userCompanyCode, String eventStatus) throws Exception {
		JSONArray list = new JSONArray();
		EventVO eventVO = new EventVO();
		if(ToolUtil.checkAdminCompany(userCompanyCode))
			eventVO.setCompanyCode(userCompanyCode);
		eventVO.setEventStatus(eventStatus);
		EventDAO eventDAO = new EventDAO();
		List<DynaBean> rows = eventDAO.getGroupListForAlert(eventVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();				
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("CountryLabel", ObjectUtils.toString(bean.get("country")));
				object.put("CountryValue", ObjectUtils.toString(bean.get("country")));
				object.put("AreaLabel", ObjectUtils.toString(bean.get("area")));
				object.put("AreaValue", ObjectUtils.toString(bean.get("area")));
				object.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				object.put("GroupValue", ObjectUtils.toString(bean.get("groupname"))+ObjectUtils.toString(bean.get("groupid")));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 電池組ID選單
	 * 
	 * @param userCompanyCode
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getBatteryList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getBatteryList(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();				
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("CountryLabel", ObjectUtils.toString(bean.get("country")));
				object.put("CountryValue", ObjectUtils.toString(bean.get("country")));
				object.put("AreaLabel", ObjectUtils.toString(bean.get("area")));
				object.put("AreaValue", ObjectUtils.toString(bean.get("area")));
				object.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				object.put("GroupValue", bean.get("groupinternalid"));				
				object.put("BatteryLabel", bean.get("nbid")+"_"+bean.get("batteryid"));
				object.put("BatteryValue", bean.get("seqno"));
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 狀態下拉選單
	 * @param language
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getStatusList(String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONArray list = new JSONArray();
		String[] status = {"1","2","3","4"};
		JSONObject object;
		for (int i=0; i<status.length; i++) {
			object = new JSONObject();
			object.put("Label", resource.getString(status[i]));
			object.put("I18NCode", status[i]);
			object.put("Value", status[i]);
			list.put(object);
		}
		return list;
	}
	
	/**
	 * 電池組管理篩選清單
	 * @param userCompanyCode
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getBattManageList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		BatteryVO batteryVO = new BatteryVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			batteryVO.setCompanyCode(userCompanyCode);
		BatteryDAO batteryDAO = new BatteryDAO();
		List<DynaBean> rows = batteryDAO.getBattManageList(batteryVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));		
				object.put("BatteryLabel", bean.get("nbid")+"_"+bean.get("batteryid"));
				object.put("BatteryValue", bean.get("seqno"));
		
				list.put(object);
			}
		}
		return list;
	}
	
	/**
	 * 接續歷史篩選清單
	 * @param userCompanyCode
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getNBGroupHisList(String userCompanyCode) throws Exception {
		JSONArray list = new JSONArray();
		NbListVO nbListVO = new NbListVO();
		if (ToolUtil.checkAdminCompany(userCompanyCode))
			nbListVO.setCompanyCode(userCompanyCode);
		NbListDAO nbListDAO = new NbListDAO();
		List<DynaBean> rows = nbListDAO.getNBGroupHisList(nbListVO);
		if (rows != null && !rows.isEmpty()) {
			JSONObject object;
			for (DynaBean bean : rows) {
				object = new JSONObject();				
				object.put("CompanyLabel", ObjectUtils.toString(bean.get("companyname")));
				object.put("CompanyValue", ObjectUtils.toString(bean.get("companycode")));
				object.put("CountryLabel", ObjectUtils.toString(bean.get("country")));
				object.put("CountryValue", ObjectUtils.toString(bean.get("country")));
				object.put("AreaLabel", ObjectUtils.toString(bean.get("area")));
				object.put("AreaValue", ObjectUtils.toString(bean.get("area")));
				object.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				object.put("GroupValue", bean.get("seqno"));				
				object.put("NBIDLabel", bean.get("nbid"));
				object.put("NBIDValue", bean.get("nbid"));
				list.put(object);
			}
		}
		return list;
	}
	
}
