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

import aptg.cathaybkeco.dao.PowerMonthDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerMonthVO;

/**
 * Servlet implementation class getPowerMonth 用電月統計
 */
@WebServlet("/getPowerMonth")
public class getPowerMonth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerMonth.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerMonth() {
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
		logger.debug("getPowerMonth start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("PowerAccount"));
			String useMonth = ObjectUtils.toString(request.getParameter("useMonth"));
			
			logger.debug("token: " + token);
			logger.debug("PowerAccount:"+powerAccount + ", useMonth:"+useMonth);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					PowerMonthVO powerMonthVO = new PowerMonthVO();
					powerMonthVO.setPowerAccount(powerAccount);
					powerMonthVO.setUseMonth(useMonth);			
					PowerMonthDAO powerMonthDAO = new PowerMonthDAO();
					List<DynaBean> list = powerMonthDAO.getPowerMonth(powerMonthVO);
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
		logger.debug("getPowerMonth end");
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
		JSONObject powerMonth = new JSONObject();
		try {
			DynaBean bean = rows.get(0);
			powerMonth.put("RatePlanCode", bean.get("rateplancode")!=null?bean.get("rateplancode"):"");
			powerMonth.put("UsuallyCC", bean.get("usuallycc")!=null?bean.get("usuallycc"):0);
			powerMonth.put("SPCC", bean.get("spcc")!=null?bean.get("spcc"):0);
			powerMonth.put("SatSPCC", bean.get("satspcc")!=null?bean.get("satspcc"):0);
			powerMonth.put("OPCC", bean.get("opcc")!=null?bean.get("opcc"):0);
			powerMonth.put("MDemandPK", bean.get("mdemandpk")!=null?bean.get("mdemandpk"):0);
			powerMonth.put("MDemandSP", bean.get("mdemandsp")!=null?bean.get("mdemandsp"):0);
			powerMonth.put("MDemandSatSP", bean.get("mdemandsatsp")!=null?bean.get("mdemandsatsp"):0);
			powerMonth.put("MDemandOP", bean.get("mdemandop")!=null?bean.get("mdemandop"):0);
			powerMonth.put("MCECPK", bean.get("mcecpk")!=null?bean.get("mcecpk"):0);
			powerMonth.put("MCECSP", bean.get("mcecsp")!=null?bean.get("mcecsp"):0);
			powerMonth.put("MCECSatSP", bean.get("mcecsatsp")!=null?bean.get("mcecsatsp"):0);
			powerMonth.put("MCECOP", bean.get("mcecop")!=null?bean.get("mcecop"):0);
			powerMonth.put("MCEC", bean.get("mcec")!=null?bean.get("mcec"):0);
			powerMonth.put("RealPlan", bean.get("realplan")!=null?bean.get("realplan"):"");
			
					
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return powerMonth;
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
