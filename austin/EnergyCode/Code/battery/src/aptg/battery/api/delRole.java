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
import aptg.battery.vo.RoleVO;



/**
 * Servlet implementation class delRole 刪除角色
 */
@WebServlet("/delRole")
public class delRole extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delRole.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delRole() {
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
		logger.debug("delRole start");
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
					RoleVO roleVO = this.parseJson(req, timezone, language);
					if(roleVO.isError()) {
						rspJson.put("code", roleVO.getCode());
						rspJson.put("msg", roleVO.getDescription());
					}else {						
						RoleDAO roleDAO = new RoleDAO();
						roleDAO.delRole(roleVO);
							
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5005"));//刪除成功
					}	
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5006"));//刪除失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("delRole end");
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
	private RoleVO parseJson(String json, String timezone, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		RoleVO roleVO = new RoleVO();
		try {
			JSONObject request = new JSONObject(json);
						
			if(ToolUtil.isNull(request, "RoleId")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("5008"));//必填欄位不能為空
				return roleVO;	
			}else if(request.optInt("RoleId")<5) {
					roleVO.setError(true);
					roleVO.setCode("30");
					roleVO.setDescription(resource.getString("5040"));//不可刪除系統平台角色
					return roleVO;	
			}else if(checkUser(request.optString("RoleId"))) {
				roleVO.setError(true);
				roleVO.setCode("30");
				roleVO.setDescription(resource.getString("1738"));//該角色仍有使用者，不可刪除此角色
				return roleVO;	
			}else {
				roleVO.setSystemId(ToolUtil.getSystemId());
				roleVO.setRoleId(request.optString("RoleId"));
				//取得權限ID
				RoleDAO roleDAO = new RoleDAO();
				List<DynaBean> authorityList = roleDAO.getAuthorityId(roleVO);
				if (authorityList != null && !authorityList.isEmpty()) {
					roleVO.setAuthorityId(ObjectUtils.toString(authorityList.get(0).get("authorityid")));
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return roleVO;
	}
	
	/**
	 * 檢核角色是否有使用者
	 * true:有使用者, false:無使用者
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	private boolean checkUser(String roleId) throws Exception {
		AccountVO accountVO = new AccountVO();
		accountVO.setRoleId(roleId);
		AccountDAO accountDAO = new AccountDAO();
		List<DynaBean> checkUser = accountDAO.getAccount(accountVO);
		if (checkUser != null && !checkUser.isEmpty()) {
			return true;
		}else {
			return false;
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
