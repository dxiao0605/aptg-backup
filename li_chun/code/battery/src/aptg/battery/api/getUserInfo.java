package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.AccountDAO;
import aptg.battery.dao.RoleDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;
import aptg.battery.vo.RoleVO;

/**
 * Servlet implementation class getUserInfo 使用者資訊
 */
@WebServlet("/getUserInfo")
public class getUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getUserInfo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("getUserInfo start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("UserCompany:" + userCompany + ",Account: " + account+",Type: " + type);			
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					String systemId =ToolUtil.getSystemId();
					RoleVO roleVO = new RoleVO();
					roleVO.setSystemId(systemId);
					roleVO.setAccount(account);
						
					//取得角色等級
					RoleDAO roleDAO = new RoleDAO();
					AccountVO accountVO = new AccountVO();
					String rank = "";
					List<DynaBean> roleRank = roleDAO.getRoleRank(roleVO);
					if (roleRank != null && !roleRank.isEmpty()) {
						rank = ObjectUtils.toString(roleRank.get(0).get("rolerank"));
						accountVO.setRoleRank(rank);
					} 		
						
					accountVO.setSystemId(systemId);

					if (ToolUtil.checkAdminCompany(userCompany)) {
						accountVO.setCompanyCode(userCompany);
					}
					
										
					AccountDAO accountDAO = new AccountDAO();
					List<DynaBean> list = accountDAO.getUserInfo(accountVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(type)) {
							composeCSV(list, timezone, language, response);
							rep = false;
						} else if ("check".equals(type)) {
							rspJson.put("msg","User"+ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
						} else {
							rspJson.put("msg", convertToJson(list, account, rank, timezone));
						}
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));// 查無資料
					}			
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			logger.error("", e);
		} finally {
			if (rep) {
				logger.debug("rsp: " + rspJson);
				ToolUtil.response(rspJson.toString(), response);
			}
		}
		logger.debug("getUserInfo end");
	}

	/**
	 * 組Json
	 * @param rows
	 * @param timezone
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String account, String roleRank, String timezone) throws Exception {
		JSONObject data = new JSONObject();
		try {
			int userRank = ToolUtil.parseInt(roleRank);
			SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
			SimpleDateFormat sdf2 = ToolUtil.getDateFormat("yyyy-MM-dd", timezone);
			JSONArray userArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject user = new JSONObject();
				user.put("Seq", i + 1);
				user.put("UserName", ObjectUtils.toString(bean.get("username")));// 名稱
				user.put("Account", ObjectUtils.toString(bean.get("account")));// 帳號
				user.put("CompanyName", ObjectUtils.toString(bean.get("companyname")));// 公司名稱			
				user.put("RoleName", ObjectUtils.toString(bean.get("rolename")));//角色		
				user.put("LastLogin", ToolUtil.dateFormat(bean.get("lastlogin"), sdf));//最後登入時間				
				user.put("CompanyCode", ObjectUtils.toString(bean.get("companycode")));//公司代碼
				user.put("Mobile", ObjectUtils.toString(bean.get("mobile")));//行動電話
				user.put("Email", ObjectUtils.toString(bean.get("email")));//電子郵件
				user.put("RoleId", bean.get("roleid"));//角色
				user.put("CreateTime", ToolUtil.dateFormat(bean.get("createtime"), sdf2));//建立日期
				user.put("TimeZone", ObjectUtils.toString(bean.get("timezone")));//語言
				user.put("Language", ObjectUtils.toString(bean.get("language")));//時區
				if(userRank>=80 && userRank>=ToolUtil.parseInt(bean.get("rolerank"))) {
					user.put("EditUser", true);
					user.put("DelUser", true);
					user.put("ResetUser", true);				
				}else if(userRank<80 && (account.equals(ObjectUtils.toString(bean.get("account"))) ||
						userRank>ToolUtil.parseInt(bean.get("rolerank")))) {
					user.put("EditUser", true);
					user.put("DelUser", true);
					user.put("ResetUser", true);
				}else {
					user.put("EditUser", false);
					user.put("DelUser", false);
					user.put("ResetUser", false);
				}
				
				if(account.equals(ObjectUtils.toString(bean.get("account")))){//自己的帳號不能刪自己的
					user.put("DelUser", false);
				}
						
				userArr.put(user);
			}
			data.put("User", userArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組CSV
	 * 
	 * @param list
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(List<DynaBean> list, String timezone, String language, HttpServletResponse response)
			throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
		try {
			StringBuilder str = new StringBuilder();
			str.append(resource.getString("1724")).append(",")// 顯示名稱
			   .append(resource.getString("1727")).append(",")// 帳號
			   .append(resource.getString("1064")).append(",")// 公司
			   .append(resource.getString("1710")).append(",")// 角色
			   .append(resource.getString("1728")).append("\n");// 最後登入時間
			for (DynaBean bean : list) {
				str.append(ObjectUtils.toString(bean.get("username"))).append(",")
				   .append(ObjectUtils.toString(bean.get("account"))).append(",")				
				   .append(ObjectUtils.toString(bean.get("companyname"))).append(",")
				   .append(ObjectUtils.toString(bean.get("rolename"))).append(",")				
				   .append(ToolUtil.dateFormat(bean.get("lastlogin"), sdf)).append("\n");
			}

			CsvUtil.exportCsv(str, "User"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv", response);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
