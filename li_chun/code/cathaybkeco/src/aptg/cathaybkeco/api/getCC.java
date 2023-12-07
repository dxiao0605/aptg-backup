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
 * Servlet implementation class getCC 最適電費
 */
@WebServlet("/getCC")
public class getCC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCC.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCC() {
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
		logger.debug("getCC start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("PowerAccount"));
			String useMonth = ObjectUtils.toString(request.getParameter("useMonth"));
			String ratePlanCode = ObjectUtils.toString(request.getParameter("RatePlanCode"));
			
			logger.debug("token: " + token);
			logger.debug("PowerAccount:"+powerAccount + ", useMonth:"+useMonth+", RatePlanCode:"+ratePlanCode);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BestCCVO bestCCVO = new BestCCVO();
					bestCCVO.setPowerAccount(powerAccount);
					bestCCVO.setUseMonth(useMonth);
					bestCCVO.setRatePlanCode(ratePlanCode);
					BestCCDAO bestCCDAO = new BestCCDAO();
					List<DynaBean> list = bestCCDAO.getCC(bestCCVO);
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
		logger.debug("getCC end");
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
			for(DynaBean bean : rows) {
				JSONObject bestCC = new JSONObject();
				bestCC.put("PowerAccount", bean.get("poweraccount"));
				bestCC.put("useMonth", bean.get("usemonth"));
				bestCC.put("RatePlanCode", bean.get("rateplancode"));
				bestCC.put("UsuallyCC", bean.get("usuallycc"));
				bestCC.put("SPCC", bean.get("spcc"));
				bestCC.put("SatSPCC", bean.get("satspcc"));
				bestCC.put("OPCC", bean.get("opcc"));
				bestCC.put("BaseCharge", bean.get("basecharge"));
				bestCC.put("OverCharge", bean.get("overcharge"));
				bestCC.put("OverPK", bean.get("overpk"));
				bestCC.put("OverSP", bean.get("oversp"));
				bestCC.put("OverSatSP", bean.get("oversatsp"));
				bestCC.put("OverOP", bean.get("overop"));
				bestCC.put("RealPlan", bean.get("realplan")!=null?bean.get("realplan"):"");
				
				list.put(bestCC);
			}
			data.put("BestCC", list);
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
