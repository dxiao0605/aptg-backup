package aptg.battery.api;

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

import aptg.battery.util.ListUtil;
import aptg.battery.util.ToolUtil;



/**
 * Servlet implementation class getHistoryFilter 電池歷史篩選
 */
@WebServlet("/getHistoryFilter")
public class getHistoryFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getHistoryFilter.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getHistoryFilter() {
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
		logger.debug("getHistoryFilter start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));	
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");					
				} else {										
					JSONObject data = new JSONObject();
					data.put("List", ListUtil.getBatteryList(userCompany));
					rspJson.put("msg", data);															
					rspJson.put("code", "00");						
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
		ToolUtil.response(rspJson.toString(), response);
		logger.debug("getHistoryFilter end");
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
