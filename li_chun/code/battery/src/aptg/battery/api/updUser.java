package aptg.battery.api;

import java.io.IOException;
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
import org.json.JSONObject;

import aptg.battery.dao.AccountDAO;
import aptg.battery.dao.RoleDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;



/**
 * Servlet implementation class updUser 編輯使用者
 */
@WebServlet("/updUser")
public class updUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updUser.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updUser() {
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
		logger.debug("updUser start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
//			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			resource = ToolUtil.getLanguage(language);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					AccountVO accountVO = this.parseJson(req, timezone, language);
					if(accountVO.isError()) {
						rspJson.put("code", accountVO.getCode());
						rspJson.put("msg", accountVO.getDescription());
					}else {
						accountVO.setSystemId(ToolUtil.getSystemId());
						AccountDAO accountDAO = new AccountDAO();
						accountDAO.updUser(accountVO);
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5002"));//保存成功
					}	
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5003"));//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("updUser end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param timezone
	 * @param language
	 * @return
	 * @throws Exception
	 */
	private AccountVO parseJson(String json, String timezone, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		AccountVO accountVO = new AccountVO();
		try {
			JSONObject request = new JSONObject(json);
			
			if(ToolUtil.isNull(request, "Account")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1102")+resource.getString("5008"));//帳號不能為空
				return accountVO;
			}else if(!ToolUtil.lengthCheck(request.optString("Account"), 100)) {
				accountVO.setError(true);
				accountVO.setCode("25");
				accountVO.setDescription(resource.getString("1102")+resource.getString("5024"));//帳號長度不符
				return accountVO;
			}else {				
				accountVO.setAccount(request.optString("Account"));
			}

			if(ToolUtil.isNull(request, "Name")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1724")+resource.getString("5008"));//顯示名稱不能為空
				return accountVO;
			}else if(!ToolUtil.lengthCheck(request.optString("Name"), 50)) {
				accountVO.setError(true);
				accountVO.setCode("25");
				accountVO.setDescription(resource.getString("1724")+resource.getString("5024"));//顯示名稱長度不符
				return accountVO;
			}else {				
				accountVO.setName(request.optString("Name"));
			}
			
			if(!ToolUtil.isNull(request, "Mobile")) {
				if(!ToolUtil.lengthCheck(request.optString("Mobile"), 20)) {
					accountVO.setError(true);
					accountVO.setCode("25");
					accountVO.setDescription(resource.getString("1730")+resource.getString("5024"));//行動電話長度不符
					return accountVO;
				}else {				
					accountVO.setMobile(request.optString("Mobile"));
				}
			}
			
			if(ToolUtil.isNull(request, "Email")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1731")+resource.getString("5008"));//Email不能為空
				return accountVO;
			}else if(!ToolUtil.lengthCheck(request.optString("Email"), 30)) {
				accountVO.setError(true);
				accountVO.setCode("25");
				accountVO.setDescription(resource.getString("1731")+resource.getString("5024"));//Email長度不符
				return accountVO;
			}else if(!ToolUtil.emailCheck(request.optString("Email"))) {
				accountVO.setError(true);
				accountVO.setCode("27");
				accountVO.setDescription(resource.getString("5001")+resource.getString("5024"));//電子郵件格式錯誤
				return accountVO;
			}else {				
				accountVO.setEmail(request.optString("Email"));
			}
			
			if(ToolUtil.isNull(request, "RoleId")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1710")+resource.getString("5008"));//角色不能為空
				return accountVO;
			}else if(!checkRoleId(accountVO.getAccount(), request.optString("RoleId"))) {
				accountVO.setError(true);
				accountVO.setCode("31");
				accountVO.setDescription(resource.getString("5045"));//客戶公司使用者權限不可高於 Adminstrator
				return accountVO;
			}else {				
				accountVO.setRoleId(request.optString("RoleId"));
			}
			

			if(ToolUtil.isNull(request, "Language")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1118")+resource.getString("5008"));//語系不能為空
				return accountVO;				
			}else {				
				accountVO.setLanguage(request.optString("Language"));
			}

			if(ToolUtil.isNull(request, "TimeZone")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1119")+resource.getString("5008"));//時區不能為空
				return accountVO;				
			}else {				
				accountVO.setTimeZone(request.optString("TimeZone"));
			}
			
			accountVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return accountVO;
	}
	
	/**
	 * 客戶公司使用者權限不可高於 Adminstrator
	 * @param company
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	private boolean checkRoleId(String account, String roleId) throws Exception {
		String company = "";
		String systemId = ToolUtil.getSystemId();
		AccountVO accountVO = new AccountVO();
		accountVO.setSystemId(systemId);
		accountVO.setAccount(account);		
		AccountDAO accountDAO = new AccountDAO();
		List<DynaBean> accountInf = accountDAO.getAccount(accountVO);
		if(accountInf!=null && !accountInf.isEmpty()) {
			company = ObjectUtils.toString(accountInf.get(0).get("company"));			
		}
		
		if (StringUtils.isBlank(company) || ToolUtil.checkAdminCompany(company)) {
			RoleDAO roleDAO = new RoleDAO();
			List<DynaBean> role = roleDAO.getRole(systemId, roleId);
			if(role!=null && !role.isEmpty()) {
				if(ToolUtil.parseInt(role.get(0).get("rolerank"))>60) {
					return false;
				}
			}
		}
		return true;
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
