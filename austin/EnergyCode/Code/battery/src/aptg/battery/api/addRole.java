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

import aptg.battery.dao.RoleDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.RoleVO;



/**
 * Servlet implementation class addRole 新增角色
 */
@WebServlet("/addRole")
public class addRole extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addRole.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addRole() {
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
		logger.debug("addRole start");
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
						roleDAO.addRole(roleVO);
							
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
		logger.debug("addRole end");
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

			if(ToolUtil.isNull(request, "RoleName")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("1703")+resource.getString("5008"));//角色名稱不能為空
				return roleVO;
			}else if(!ToolUtil.strCheck(request.optString("RoleName"))) {
				roleVO.setError(true);
				roleVO.setCode("27");
				roleVO.setDescription(resource.getString("1703") + resource.getString("5034"));// 角色名稱格式錯誤
				return roleVO;
			}else if(!ToolUtil.lengthCheck(request.optString("RoleName"), 30)) {
				roleVO.setError(true);
				roleVO.setCode("25");
				roleVO.setDescription(resource.getString("1703")+resource.getString("5024"));//角色名稱長度不符
				return roleVO;
			}else {				
				roleVO.setRoleName(request.optString("RoleName"));
			}
			
			String systemId = ToolUtil.getSystemId();
			roleVO.setSystemId(systemId);
			RoleDAO roleDAO = new RoleDAO();
			List<DynaBean> checkName = roleDAO.checkRoleName(roleVO);
			if (checkName != null && !checkName.isEmpty()) {
				roleVO.setError(true);
				roleVO.setCode("28");
				roleVO.setDescription(resource.getString("1703")+resource.getString("5035"));//角色名稱重複
				return roleVO;
			}
			
			if(ToolUtil.isNull(request, "RoleDesc")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("1704")+resource.getString("5008"));//角色中文說明不能為空
				return roleVO;
			}else if(!ToolUtil.lengthCheck(request.optString("RoleDesc"), 30)) {
				roleVO.setError(true);
				roleVO.setCode("25");
				roleVO.setDescription(resource.getString("1704")+resource.getString("5024"));//角色中文說明長度不符
				return roleVO;
			}else {				
				roleVO.setRoleDesc(request.optString("RoleDesc"));
			}
			
			if(ToolUtil.isNull(request, "RoleDescE")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("1705")+resource.getString("5008"));//角色英文說明不能為空
				return roleVO;
			}else if(!ToolUtil.lengthCheck(request.optString("RoleDescE"), 30)) {
				roleVO.setError(true);
				roleVO.setCode("25");
				roleVO.setDescription(resource.getString("1705")+resource.getString("5024"));//角色英文說明長度不符
				return roleVO;
			}else {				
				roleVO.setRoleDescE(request.optString("RoleDescE"));
			}
			
			if(ToolUtil.isNull(request, "RoleDescJ")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("1706")+resource.getString("5008"));//角色日文說明不能為空
				return roleVO;
			}else if(!ToolUtil.lengthCheck(request.optString("RoleDescJ"), 30)) {
				roleVO.setError(true);
				roleVO.setCode("25");
				roleVO.setDescription(resource.getString("1706")+resource.getString("5024"));//角色日文說明長度不符
				return roleVO;
			}else {				
				roleVO.setRoleDescJ(request.optString("RoleDescJ"));
			}
						
			if(ToolUtil.isNull(request, "CopyRoleId")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("5039"));//必須選擇來源角色
				return roleVO;			
			}else {
				//取得角色等級
				List<DynaBean> list = roleDAO.getRole(systemId, request.optString("CopyRoleId"));
				if (list != null && !list.isEmpty()) {
					roleVO.setRoleRank(ObjectUtils.toString(list.get(0).get("rolerank")));
				}
				
				RoleVO copyRoleVO = new RoleVO();
				copyRoleVO.setSystemId(systemId);
				copyRoleVO.setRoleId(request.optString("CopyRoleId"));
				//取得複製的權限ID
				List<DynaBean> authorityList = roleDAO.getAuthorityId(copyRoleVO);
				if (authorityList != null && !authorityList.isEmpty()) {
					roleVO.setCopyAuthorityId(ObjectUtils.toString(authorityList.get(0).get("authorityid")));
				}
				
				//取得角色ID
				String roleid = "";
				List<DynaBean> roleidList = roleDAO.getMaxRoleId(roleVO.getSystemId());
				if (roleidList != null && !roleidList.isEmpty()) {
					roleid = ObjectUtils.toString(roleidList.get(0).get("roleid"));
					roleVO.setRoleId(roleid);
					roleVO.setAuthorityId(roleid);//角色ID同權限ID
				}				
			}
			roleVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return roleVO;
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
