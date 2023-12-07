package aptg.battery.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.AccountDAO;
import aptg.battery.dao.AuthorizationDAO;
import aptg.battery.vo.AccountVO;
import aptg.battery.vo.AuthorizationVO;


public class AuthorizationUtil {

	/**
	 * 取得帳戶資訊
	 * @param systemId
	 * @param account
	 * @param password
	 * @return String
	 * @throws Exception
	 */
	public static String getAccount(String systemId, String account, String password) throws Exception {		
		JSONObject rspJson = new JSONObject();
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setSystemId(systemId);
			authorizationVO.setAccount(account);
			authorizationVO.setPassword(password);
			authorizationVO.setEnabled("1");
			// 查詢登入者資訊
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			List<DynaBean> userInfoList = authorizationDAO.getAccount(authorizationVO);
			if (userInfoList != null && userInfoList.size() != 0) {
				DynaBean bean = userInfoList.get(0);
				rspJson.put("SystemId", bean.get("systemid"));
				rspJson.put("Company", bean.get("company"));
				rspJson.put("CompanyName", bean.get("companyname"));
				JSONObject userInfo = new JSONObject();
				userInfo.put("UserName", bean.get("username"));
				userInfo.put("RoleId", bean.get("roleid"));
				if (bean.get("token") == null) {
					String token = getToken(password);
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
	 * 取得Token
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String token = new String();
		try {
			 token = EncryptUtil.encryptSHA256(str + sdf.format(new Date()));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return token;
	}
	
	/**
	 * 檢核Token
	 * @param systemId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static boolean checkToken(String systemId, String token) throws Exception {
		boolean check = false;
		try {
			
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setToken(token);
			authorizationVO.setSystemId(systemId);
			
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			check = authorizationDAO.checkToken(authorizationVO);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return check;
	}

	/**
	 * 取得使用者名稱
	 * @param systemId
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public static String getUserName(String systemId, String account) throws Exception {
		String userName = "";
		AccountVO accountVO = new AccountVO();
		accountVO.setSystemId(systemId);
		accountVO.setAccount(account);
        AccountDAO accountDAO = new AccountDAO();
        List<DynaBean> list = accountDAO.getAccount(accountVO);
		if(list!=null && !list.isEmpty()) {
			DynaBean bean = list.get(0);
			userName = ObjectUtils.toString(bean.get("username"));
		}
		return userName;	
	}
}
