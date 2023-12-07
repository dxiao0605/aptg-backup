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

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.dao.RankListDAO;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getRankListEdit 權限下拉選單(編輯時使用)
 */
@WebServlet("/getRankListEdit")
public class getRankListEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getRankListEdit.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getRankListEdit() {
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
			String account = ObjectUtils.toString(request.getParameter("account"));
			logger.debug("token: " + token);
			logger.debug("account: " + account);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					String rankCode = "";
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					List<DynaBean> rank = adminSetupDAO.getRankCode(account);
					if (rank != null && !rank.isEmpty()) {
						rankCode = ObjectUtils.toString(rank.get(0).get("rankcode"));
					}
					
					RankListDAO rankListDAO = new RankListDAO();
					List<DynaBean> list = rankListDAO.getRankList(rankCode);					
					if (list != null && list.size() > 0) {
						rspJson.put("msg", convertToJson(list, rankCode));
						rspJson.put("code", "00");
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
		logger.debug("getRankListEdit rsp: " + rspJson);
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
	private JSONObject convertToJson(List<DynaBean> rows, String rankCode) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();			
			for (DynaBean bean : rows) {				
				if(!("3".equals(rankCode) && "3".equals(ObjectUtils.toString(bean.get("rankcode"))))) {//區域管理者權限的人看不到區域管理者選項
					JSONObject object = new JSONObject();
					object.put("Label", bean.get("rankdesc"));
					object.put("Value", bean.get("rankcode"));
					list.put(object);
				}
			}
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
