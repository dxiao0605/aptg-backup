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

import aptg.cathaybkeco.dao.BestRatePlanDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BestRatePlanVO;

/**
 * Servlet implementation class getRatePlan 最適電費
 */
@WebServlet("/getRatePlan")
public class getRatePlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getRatePlan.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getRatePlan() {
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
		logger.debug("getRatePlan start");
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
					BestRatePlanVO bestRatePlanVO = new BestRatePlanVO();
					bestRatePlanVO.setPowerAccount(powerAccount);
					bestRatePlanVO.setUseMonth(useMonth);			
					BestRatePlanDAO bestRatePlanDAO = new BestRatePlanDAO();
					List<DynaBean> list = bestRatePlanDAO.getRatePlan(bestRatePlanVO);
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
		logger.debug("getRatePlan end");
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
				JSONObject bestRatePlan = new JSONObject();
				bestRatePlan.put("inUse", bean.get("inuse"));
				bestRatePlan.put("RatePlanCode", bean.get("rateplancode"));
				bestRatePlan.put("UsuallyCC", bean.get("usuallycc")!=null?bean.get("usuallycc"):0);
				bestRatePlan.put("SPCC", bean.get("spcc")!=null?bean.get("spcc"):0);
				bestRatePlan.put("SatSPCC", bean.get("satspcc")!=null?bean.get("satspcc"):0);
				bestRatePlan.put("OPCC", bean.get("opcc")!=null?bean.get("opcc"):0);
				bestRatePlan.put("TPMDemandPK", bean.get("tpmdemandpk")!=null?bean.get("tpmdemandpk"):0);
				bestRatePlan.put("TPMDemandSP", bean.get("tpmdemandsp")!=null?bean.get("tpmdemandsp"):0);
				bestRatePlan.put("TPMDemandSatSP", bean.get("tpmdemandsatsp")!=null?bean.get("tpmdemandsatsp"):0);
				bestRatePlan.put("TPMDemandOP", bean.get("tpmdemandop")!=null?bean.get("tpmdemandop"):0);
				bestRatePlan.put("TPMCECPK", bean.get("tpmcecpk")!=null?bean.get("tpmcecpk"):0);
				bestRatePlan.put("TPMCECSP", bean.get("tpmcecsp")!=null?bean.get("tpmcecsp"):0);
				bestRatePlan.put("TPMCECSatSP", bean.get("tpmcecsatsp")!=null?bean.get("tpmcecsatsp"):0);
				bestRatePlan.put("TPMCECOP", bean.get("tpmcecop")!=null?bean.get("tpmcecop"):0);
				bestRatePlan.put("TPMCEC", bean.get("tpmcec")!=null?bean.get("tpmcec"):0);
				bestRatePlan.put("BaseCharge", bean.get("basecharge")!=null?bean.get("basecharge"):0);
				bestRatePlan.put("UsageCharge", bean.get("usagecharge")!=null?bean.get("usagecharge"):0);
				bestRatePlan.put("OverCharge", bean.get("overcharge")!=null?bean.get("overcharge"):0);
				bestRatePlan.put("TotalCharge", bean.get("totalcharge")!=null?bean.get("totalcharge"):0);
				bestRatePlan.put("OverPK", bean.get("overpk"));
				bestRatePlan.put("OverSP", bean.get("oversp"));
				bestRatePlan.put("OverSatSP", bean.get("oversatsp"));
				bestRatePlan.put("OverOP", bean.get("overop"));
				bestRatePlan.put("RealPlan", bean.get("realplan")!=null?bean.get("realplan"):"");
				
				list.put(bestRatePlan);
			}
			data.put("BestRatePlan", list);
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
