package aptg.cathaybkeco.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.AuthorizationDAO;
import aptg.cathaybkeco.vo.AuthorizationVO;


public class Account {

	/**
	 * 取得帳戶資訊
	 * @param systemId
	 * @param account
	 * @param password
	 * @return String
	 * @throws Exception
	 */
	public static String getAccount(String systemId, String account, String password) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		JSONObject rspJson = new JSONObject();
		try {
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setSystemId(systemId);
			authorizationVO.setAccount(account);
			authorizationVO.setPassword(password);
			authorizationVO.setEnabled("1");
			// 查詢登入者資訊
			List<DynaBean> userInfoList = authorizationDAO.getAccount(authorizationVO);
			if (userInfoList != null && userInfoList.size() != 0) {
				DynaBean bean = userInfoList.get(0);
				rspJson.put("SystemId", bean.get("systemid"));
				rspJson.put("Company", bean.get("company"));
				JSONObject userInfo = new JSONObject();
				userInfo.put("UserName", bean.get("username"));
				userInfo.put("RoleId", bean.get("roleid"));
				Date tokenDisableTime = bean.get("tokendisabletime") != null
						? sdf.parse(sdf.format(bean.get("tokendisabletime")))
						: null;
				if (bean.get("token") == null
						|| (tokenDisableTime != null && tokenDisableTime.compareTo(new Date()) < 0)) {
					String token = Token.getToken(password);
					authorizationVO.setAddToken(token);
					authorizationDAO.updateToken(authorizationVO);
					userInfo.put("Token", token);
				} else {
					userInfo.put("Token", bean.get("token"));
				}
				rspJson.put("Account", userInfo);

				authorizationVO.setRoleId(ObjectUtils.toString(bean.get("roleid")));
				List<DynaBean> authorityInfoList = authorizationDAO.getAuthorityInfo(authorizationVO);
				if (authorityInfoList != null && authorityInfoList.size() != 0) {
					JSONArray functionList = new JSONArray();
					for (DynaBean functionBean : authorityInfoList) {
						JSONObject function = new JSONObject();
						function.put("FunctionId", functionBean.get("functionid"));
						function.put("FunctionName", functionBean.get("functionname"));
						function.put("Type", functionBean.get("type"));
						function.put("ParentId",
								functionBean.get("parentid") != null ? functionBean.get("parentid") : "");
						function.put("Url", ObjectUtils.toString(functionBean.get("url")));
						function.put("IconPath", ObjectUtils.toString(functionBean.get("iconpath")));
						function.put("Edit", functionBean.get("edit"));
						function.put("Operate", functionBean.get("operate"));
						function.put("Sort", functionBean.get("sort"));
						function.put("Button", getButton(ObjectUtils.toString(functionBean.get("authorityid")),
								ObjectUtils.toString(functionBean.get("programid"))));

						functionList.put(function);
					}
					rspJson.put("FunctionList", functionList);
				}

				authorizationDAO.updateLoginTime(authorizationVO);// 修改最後登入時間
			} else {
				throw new Exception("帳號不存在");
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return rspJson.toString();
	}

	private static JSONObject getButton(String authorityId, String programId) throws Exception {
		JSONObject button = new JSONObject();
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setProgramId(programId);
			authorizationVO.setAuthorityId(authorityId);
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			List<DynaBean> buttionList = authorizationDAO.getButtonInfo(authorizationVO);
			if (buttionList != null && buttionList.size() != 0) {
				for (DynaBean bean : buttionList) {
					button.put(bean.get("buttonid").toString(), bean.get("enabled"));
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return button;
	}
	
	/**
	 * 新增帳戶
	 * @param json
	 * @throws Exception
	 */
	public static void addAccount(String json) throws Exception {
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			JSONObject request = new JSONObject(json);
			authorizationVO.setSystemId(request.getString("SystemId"));
			authorizationVO.setAccount(request.getString("Account"));
			authorizationVO.setCompany(request.optString("Company"));
			authorizationVO.setUserName(request.getString("UserName"));
			authorizationVO.setPassword(request.getString("Password"));
			authorizationVO.setRoleId(request.getString("RoleId"));
			authorizationVO.setEnabled(request.optString("Enabled"));
			if("1".equals(authorizationVO.getEnabled())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				authorizationVO.setEnabledTime(sdf.format(new Date()));
			}else {
				authorizationVO.setEnabledTime(null);
			}
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			authorizationDAO.addAccount(authorizationVO);
			
		}catch (Exception e) {
			throw new Exception(e.toString());
		}
	}
	
	/**
	 * 修改帳戶
	 * @param json
	 * @throws Exception
	 */
	public static void updAccount(String json) throws Exception {
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			JSONObject request = new JSONObject(json);
			authorizationVO.setSystemId(request.getString("SystemId"));
			authorizationVO.setAccount(request.getString("Account"));
			authorizationVO.setCompany(request.optString("Company"));
			authorizationVO.setUserName(request.getString("UserName"));
			authorizationVO.setPassword(request.getString("Password"));
			authorizationVO.setRoleId(request.getString("RoleId"));
			authorizationVO.setEnabled(request.optString("Enabled"));
			if("1".equals(authorizationVO.getEnabled())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				authorizationVO.setEnabledTime(sdf.format(new Date()));
			}else {
				authorizationVO.setEnabledTime(null);
			}
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			authorizationDAO.updAccount(authorizationVO);
			
		}catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * 修改密碼
	 * @param systemId
	 * @param account
	 * @param password
	 * @throws Exception
	 */
	public static void updPassword(String systemId, String account, String password) throws Exception {
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setSystemId(systemId);
			authorizationVO.setAccount(account);
			authorizationVO.setPassword(password);
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			authorizationDAO.updPassword(authorizationVO);		
		}catch (Exception e) {
			throw new Exception(e.toString());
		}
	}
	

}
