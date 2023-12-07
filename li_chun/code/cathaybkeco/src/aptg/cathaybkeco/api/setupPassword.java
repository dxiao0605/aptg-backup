package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.List;

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

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.util.EncryptUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

/**
 * Servlet implementation class setupPassword 設定密碼
 */
@WebServlet("/setupPassword")
public class setupPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(setupPassword.class.getName());
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public setupPassword() {
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
		logger.debug("setupPassword start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(req)) {				
				AdminSetupVO adminSetupVO = this.parseJson(req);
				if(adminSetupVO.isError()) {
					rspJson.put("code", adminSetupVO.getCode());
					rspJson.put("msg", adminSetupVO.getDescription());
				}else {						
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					adminSetupDAO.updPassword(adminSetupVO);
					ToolUtil.addLogRecord(adminSetupVO.getUserName(), "22", "設定密碼");
					
					rspJson.put("code", "00");
					rspJson.put("msg", "Update Success");
				}			
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("setupPassword end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return adminSetupVO
	 * @throws Exception
	 */
	private AdminSetupVO parseJson(String json) throws Exception {
		AdminSetupVO adminSetupVO = new AdminSetupVO();
		try {
			JSONObject request = new JSONObject(json);
			if(ToolUtil.isNull(request, "Account")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("帳號不能為空");
				return adminSetupVO;			
			}else {
				adminSetupVO.setAccount(request.getString("Account"));	
			}

			if(ToolUtil.isNull(request, "Password")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("密碼不能為空");
				return adminSetupVO;			
			}else if(!ToolUtil.lengthCheck(request.optString("Password"), 50)) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("12");
				adminSetupVO.setDescription("密碼超過長度限制");
				return adminSetupVO;
			}else if(!request.optString("Password").equals(request.optString("PasswordCheck"))) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("06");
				adminSetupVO.setDescription("密碼與確認密碼不符");
				return adminSetupVO;
			}else {		
				adminSetupVO.setPassword(EncryptUtil.encryptAES(request.getString("Password"), ToolUtil.getKey()));
			}
			
			AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
			List<DynaBean> adminSetupList = adminSetupDAO.getAdminSetup(adminSetupVO);
			if(adminSetupList!=null && !adminSetupList.isEmpty()) {
				DynaBean bean = adminSetupList.get(0);
				if(!"0".equals(ObjectUtils.toString(bean.get("enabled")))) {
					adminSetupVO.setAddHistory(true);	
				}				
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return adminSetupVO;
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
