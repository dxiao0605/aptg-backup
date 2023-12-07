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

import aptg.cathaybkeco.dao.ControllerSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.ControllerSetupVO;

/**
 * Servlet implementation class addControllerSetup 新增ECO5資料
 */
@WebServlet("/addControllerSetup")
public class addControllerSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addControllerSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addControllerSetup() {
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
		logger.debug("addControllerSetup start");
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
					ControllerSetupVO controllerSetupVO = this.parseJson(req);
					if (controllerSetupVO.isError()) {
						rspJson.put("code", controllerSetupVO.getCode());
						rspJson.put("msg", controllerSetupVO.getDescription());
					} else {
						ControllerSetupVO ckeckVO = new ControllerSetupVO(); 
						ckeckVO.setEco5Account(controllerSetupVO.getEco5Account());
						ControllerSetupDAO controllerSetupDAO = new ControllerSetupDAO();
						List<DynaBean> list = controllerSetupDAO.getControllerSetup(ckeckVO);
						if (list != null && list.size() > 0) {
							rspJson.put("code", "09");
							rspJson.put("msg", "ECO5帳號已存在");
						} else {
							controllerSetupDAO.addControllerSetup(controllerSetupVO);
							ToolUtil.addLogRecord(controllerSetupVO.getUserName(), "7",
									"新增ECO-5帳號:" + controllerSetupVO.getEco5Account());

							rspJson.put("code", "00");
							rspJson.put("msg", "Insert Success");
						}
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
		logger.debug("addControllerSetup end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return controllerSetupVO
	 * @throws Exception
	 */
	private ControllerSetupVO parseJson(String json) throws Exception {
		ControllerSetupVO controllerSetupVO = new ControllerSetupVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			if(ToolUtil.isNull(msg, "ECO5Account")) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("15");
				controllerSetupVO.setDescription("ECO5帳號不能為空");
			}else if (!ToolUtil.lengthCheck(msg.optString("ECO5Account"), 20)) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("12");
				controllerSetupVO.setDescription("ECO5帳號超過長度限制");
			} else {
				controllerSetupVO.setEco5Account(msg.optString("ECO5Account"));
			}

			if(ToolUtil.isNull(msg, "ECO5Password")) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("15");
				controllerSetupVO.setDescription("ECO5密碼不能為空");
			}else if (!ToolUtil.lengthCheck(msg.optString("ECO5Password"), 6)) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("12");
				controllerSetupVO.setDescription("ECO5密碼超過長度限制");
			} else {
				controllerSetupVO.setEco5Password(msg.optString("ECO5Password"));
			}

			if (!ToolUtil.lengthCheck(msg.optString("BankCode"), 3)) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("12");
				controllerSetupVO.setDescription("分行代號超過長度限制");
			} else {
				controllerSetupVO.setBankCode(msg.optString("BankCode"));
			}

			if(ToolUtil.isNull(msg, "InstallPosition")) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("15");
				controllerSetupVO.setDescription("安裝位置不能為空");
			}else if (!ToolUtil.lengthCheck(msg.optString("InstallPosition"), 100)) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("12");
				controllerSetupVO.setDescription("安裝位置超過長度限制");
			} else {
				controllerSetupVO.setInstallPosition(msg.optString("InstallPosition"));
			}

			controllerSetupVO.setEnabled(msg.optString("Enabled"));

			if (!ToolUtil.lengthCheck(msg.optString("IP"), 16)) {
				controllerSetupVO.setError(true);
				controllerSetupVO.setCode("12");
				controllerSetupVO.setDescription("IP位址超過長度限制");
			} else {
				controllerSetupVO.setIp(msg.optString("IP"));
			}
			controllerSetupVO.setUserName(msg.optString("UserName"));

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return controllerSetupVO;
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
