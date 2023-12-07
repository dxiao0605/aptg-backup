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

import aptg.cathaybkeco.dao.KPIDAO;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getKPI 電費單價目標值
 */
@WebServlet("/getKPI")
public class getKPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getKPI.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getKPI() {
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
		logger.debug("getKPI start");
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
					KPIDAO kpiDAO = new KPIDAO();
					List<DynaBean> list = kpiDAO.getKPI();
					if (list != null && list.size() > 0) {
						rspJson.put("code", "00");
						rspJson.put("count", list != null ? list.size() : 0);
						
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
		logger.debug("getKPI end");
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
			DynaBean bean = rows.get(0);
			data.put("UnitPriceKPI", bean.get("unitpricekpi")!=null?bean.get("unitpricekpi"):"");
			data.put("EUIKPI", bean.get("euikpi")!=null?bean.get("euikpi"):"");
			data.put("EPUIKPI", bean.get("epuikpi")!=null?bean.get("epuikpi"):"");
			data.put("AirKPI", bean.get("airkpi")!=null?bean.get("airkpi"):"");
			data.put("WKPI", 100);

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
