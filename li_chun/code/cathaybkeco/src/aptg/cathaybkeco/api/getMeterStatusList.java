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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getMeterStatusList 電表總覽狀態清單
 */
@WebServlet("/getMeterStatusList")
public class getMeterStatusList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterStatusList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterStatusList() {
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
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			logger.debug("token: " + token);			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					rspJson.put("code", "00");
					rspJson.put("msg", convertToJson());	
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
		logger.debug("getMeterStatusList rsp: " + rspJson);
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
	private JSONObject convertToJson() throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			JSONObject status1 = new JSONObject();
			status1.put("StatusCode", 1);
			status1.put("Status", "ECO5斷線");
			list.put(status1);
			JSONObject status2 = new JSONObject();
			status2.put("StatusCode", 2);
			status2.put("Status", "電表斷線");
			list.put(status2);
			JSONObject status9 = new JSONObject();
			status9.put("StatusCode", 9);
			status9.put("Status", "連線中");
			list.put(status9);
			JSONObject status10 = new JSONObject();
			status10.put("StatusCode", 10);
			status10.put("Status", "未設定");
			list.put(status10);
			JSONObject status11 = new JSONObject();
			status11.put("StatusCode", 11);
			status11.put("Status", "未啟用");
			list.put(status11);
			
			data.put("List", list);
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
