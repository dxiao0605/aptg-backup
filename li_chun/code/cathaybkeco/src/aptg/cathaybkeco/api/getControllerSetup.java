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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.ControllerSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.ControllerSetupVO;

/**
 * Servlet implementation class getControllerSetup ECO5資料/列表
 */
@WebServlet("/getControllerSetup")
public class getControllerSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getControllerSetup.class.getName());
	private ControllerSetupDAO controllerSetupDAO = new ControllerSetupDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getControllerSetup() {
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
		logger.debug("getControllerSetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String eco5Account = ObjectUtils.toString(request.getParameter("eco5Account"));
			logger.debug("token: " + token);
			logger.debug("bankCode: " + bankCode);
			logger.debug("ECO5Account: " + eco5Account);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ControllerSetupVO controllerSetupVO = new ControllerSetupVO();
					controllerSetupVO.setBankCode(bankCode);
					controllerSetupVO.setEco5Account(eco5Account);
					List<DynaBean> list = controllerSetupDAO.getControllerSetup(controllerSetupVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
					if (list != null && list.size() > 0) {
						rspJson.put("msg", convertToJson(list));
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
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
		logger.debug("getControllerSetup end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for (DynaBean bean : rows) {
				JSONObject eco5 = new JSONObject();
				eco5.put("ECO5Account", bean.get("eco5account"));
				eco5.put("ECO5Password", bean.get("eco5password"));
				eco5.put("IP", bean.get("ip"));
				eco5.put("InstallPosition", bean.get("installposition"));
				eco5.put("EnabledDesc", ToolUtil.getEnabled(bean.get("enabled")));
				eco5.put("Enabled", bean.get("enabled"));
				list.put(eco5);
			}
			data.put("ECO5", list);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
