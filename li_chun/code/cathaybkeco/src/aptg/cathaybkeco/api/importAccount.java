package aptg.cathaybkeco.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

/**
 * Servlet implementation class importAccount 匯入帳號資訊
 */
@WebServlet("/importAccount")
public class importAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(importAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public importAccount() {
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
		logger.debug("importAccount start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					AdminSetupVO adminSetupVO = this.parseJson(req);
					if (adminSetupVO.isError()) {
						rspJson.put("code", adminSetupVO.getCode());
						rspJson.put("msg", adminSetupVO.getDescription());
					} else {							
						AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
						if("A".equals(adminSetupVO.getProcess())) {
							adminSetupDAO.accountTempToAdminSetup(adminSetupVO);							
							ToolUtil.addLogRecord(adminSetupVO.getUserName(), "19", "匯入帳號資訊");
							
							rspJson.put("msg", "Import Success");						
						}else {
							adminSetupDAO.delAccountTemp(adminSetupVO);
							
							rspJson.put("msg", "Cancel Success");						
						}
						rspJson.put("code", "00");
						adminSetupDAO.importUnlock(adminSetupVO);
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
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("importAccount end");
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
			if(ToolUtil.isNull(request, "UUID")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("UUID不能為空");
				return adminSetupVO;			
			}else {
				adminSetupVO.setUuid(request.getString("UUID"));	
			}
			
			if(ToolUtil.isNull(request, "Process")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("01");
				adminSetupVO.setDescription("缺少參數");
				return adminSetupVO;			
			}else {
				adminSetupVO.setProcess(request.getString("Process"));//A:確定匯入, C取消匯入
			}
			adminSetupVO.setUserName(request.optString("UserName"));
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
