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

import aptg.cathaybkeco.dao.BestCCDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BestCCVO;

/**
 * Servlet implementation class getBestCCList 契約容量最佳化列表
 */
@WebServlet("/getBestCCList")
public class getBestCCList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBestCCList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBestCCList() {
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
		logger.debug("getBestCCList start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("powerAccount"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			String ratePlanCode = ObjectUtils.toString(request.getParameter("ratePlanCode"));

			logger.debug("token: " + token);
			logger.debug("PowerAccount:" + powerAccount + ",RatePlanCode:" + ratePlanCode);
			logger.debug("date:" + start + " ~ " + end);

			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				}else if (!ToolUtil.dateCheck(start, "yyyyMM") || !ToolUtil.dateCheck(end, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				}  else {
					BestCCVO bestCCVO = new BestCCVO();
					bestCCVO.setPowerAccount(powerAccount);
					bestCCVO.setStartDate(start);
					bestCCVO.setEndDate(end);				
					bestCCVO.setRatePlanCode(ratePlanCode);
					BestCCDAO bestCCDAO = new BestCCDAO();
					List<DynaBean> list = bestCCDAO.getBestCC(bestCCVO);
					if (list != null && list.size() > 0) {
						rspJson.put("code", "00");

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
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());		
		logger.debug("getBestCCList end");
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
			JSONArray bestCCArr = new JSONArray();
			JSONArray baseChargeArr = new JSONArray();
			JSONArray overChargeArr = new JSONArray();
			JSONArray totalChargeArr = new JSONArray();
			for (int i=0; i<rows.size(); i++) {
				DynaBean bean = rows.get(i);
				bestCCArr.put(ToolUtil.getBigDecimal(bean.get("bsetcc")));
				baseChargeArr.put(ToolUtil.getBigDecimal(bean.get("bestbasecharge")));
				overChargeArr.put(ToolUtil.getBigDecimal(bean.get("bestovercharge")));
				totalChargeArr.put(ToolUtil.add(bean.get("bestbasecharge"), bean.get("bestovercharge")));
			}
			data.put("CC", bestCCArr);//契約容量
			data.put("BaseCharge", baseChargeArr);//基本電費
			data.put("OverCharge", overChargeArr);//非約定電費
			data.put("TotalCharge", totalChargeArr);//總計
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
