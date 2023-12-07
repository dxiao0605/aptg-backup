package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import aptg.cathaybkeco.dao.PowerRecordDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerRecordVO;

/**
 * Servlet implementation class getFcstCharge 預測電費資料
 */
@WebServlet("/getFcstCharge")
public class getFcstCharge extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getFcstCharge.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getFcstCharge() {
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
		logger.debug("getFcstCharge start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("PowerAccount"));
			String useMonth = ObjectUtils.toString(request.getParameter("useMonth"));
			logger.debug("PowerAccount: " + powerAccount);
			logger.debug("useMonth: " + useMonth);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					PowerRecordVO powerRecordVO = new PowerRecordVO();
					powerRecordVO.setPowerAccount(powerAccount);
					powerRecordVO.setUseMonth(useMonth);
					PowerRecordDAO powerRecordDAO = new PowerRecordDAO();
					List<DynaBean> list = powerRecordDAO.getFcstCharge(powerRecordVO);
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
		logger.debug("getFcstCharge end");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for(DynaBean bean : rows) {
				JSONObject fcstCharge = new JSONObject();
				fcstCharge.put("PowerAccount", bean.get("poweraccount"));
				fcstCharge.put("useMonth", bean.get("usemonth"));
				fcstCharge.put("useTime", ToolUtil.dateFormat(bean.get("usetime"), sdf));
				fcstCharge.put("RatePlanCode", bean.get("rateplancode"));
				fcstCharge.put("UsuallyCC", bean.get("usuallycc")!=null?bean.get("usuallycc"):0);
				fcstCharge.put("SPCC", bean.get("spcc")!=null?bean.get("spcc"):0);
				fcstCharge.put("SatSPCC", bean.get("satspcc")!=null?bean.get("satspcc"):0);
				fcstCharge.put("OPCC", bean.get("opcc")!=null?bean.get("opcc"):0);
				fcstCharge.put("MDemandPK", bean.get("mdemandpk")!=null?bean.get("mdemandpk"):0);
				fcstCharge.put("MDemandSP", bean.get("mdemandsp")!=null?bean.get("mdemandsp"):0);
				fcstCharge.put("MDemandSatSP", bean.get("mdemandsatsp")!=null?bean.get("mdemandsatsp"):0);
				fcstCharge.put("MDemandOP", bean.get("mdemandop")!=null?bean.get("mdemandop"):0);
				fcstCharge.put("TPMDemandPK", bean.get("tpmdemandpk")!=null?bean.get("tpmdemandpk"):0);
				fcstCharge.put("TPMDemandSP", bean.get("tpmdemandsp")!=null?bean.get("tpmdemandsp"):0);
				fcstCharge.put("TPMDemandSatSP", bean.get("tpmdemandsatsp")!=null?bean.get("tpmdemandsatsp"):0);
				fcstCharge.put("TPMDemandOP", bean.get("tpmdemandop")!=null?bean.get("tpmdemandop"):0);
				fcstCharge.put("MCECPK", bean.get("mcecpk")!=null?bean.get("mcecpk"):0);
				fcstCharge.put("MCECSP", bean.get("mcecsp")!=null?bean.get("mcecsp"):0);
				fcstCharge.put("MCECSatSP", bean.get("mcecsatsp")!=null?bean.get("mcecsatsp"):0);
				fcstCharge.put("MCECOP", bean.get("mcecop")!=null?bean.get("mcecop"):0);
				fcstCharge.put("MCEC", bean.get("mcec")!=null?bean.get("mcec"):0);
				fcstCharge.put("TPMCECPK", bean.get("tpmcecpk")!=null?bean.get("tpmcecpk"):0);
				fcstCharge.put("TPMCECSP", bean.get("tpmcecsp")!=null?bean.get("tpmcecsp"):0);
				fcstCharge.put("TPMCECSatSP", bean.get("tpmcecsatsp")!=null?bean.get("tpmcecsatsp"):0);
				fcstCharge.put("TPMCECOP", bean.get("tpmcecop")!=null?bean.get("tpmcecop"):0);
				fcstCharge.put("TPMCEC", bean.get("tpmcec")!=null?bean.get("tpmcec"):0);
				fcstCharge.put("BaseCharge", bean.get("basecharge")!=null?bean.get("basecharge"):0);
				fcstCharge.put("UsageCharge", bean.get("usagecharge")!=null?bean.get("usagecharge"):0);
				fcstCharge.put("OverCharge", bean.get("overcharge")!=null?bean.get("overcharge"):0);
				fcstCharge.put("TotalCharge", bean.get("totalcharge")!=null?bean.get("totalcharge"):0);
				fcstCharge.put("FcstMCECPK", bean.get("fcstmcecpk")!=null?bean.get("fcstmcecpk"):0);
				fcstCharge.put("FcstMCECSP", bean.get("fcstmcecsp")!=null?bean.get("fcstmcecsp"):0);
				fcstCharge.put("FcstMCECSatSP", bean.get("fcstmcecsatsp")!=null?bean.get("fcstmcecsatsp"):0);
				fcstCharge.put("FcstMCECOP", bean.get("fcstmcecop")!=null?bean.get("fcstmcecop"):0);
				fcstCharge.put("FcstMCEC", bean.get("fcstmcec")!=null?bean.get("fcstmcec"):0);
				fcstCharge.put("FcstBaseCharge", bean.get("fcstbasecharge")!=null?bean.get("fcstbasecharge"):0);
				fcstCharge.put("FcstUsageCharge", bean.get("fcstusagecharge")!=null?bean.get("fcstusagecharge"):0);
				fcstCharge.put("FcstOverCharge", bean.get("fcstovercharge")!=null?bean.get("fcstovercharge"):0);
				fcstCharge.put("FcstTotalCharge", bean.get("fcsttotalcharge")!=null?bean.get("fcsttotalcharge"):0);
				fcstCharge.put("OverPK", bean.get("overpk")!=null?bean.get("overpk"):0);
				fcstCharge.put("OverSP", bean.get("oversp")!=null?bean.get("oversp"):0);
				fcstCharge.put("OverSatSP", bean.get("oversatsp")!=null?bean.get("oversatsp"):0);
				fcstCharge.put("OverOP", bean.get("overop")!=null?bean.get("overop"):0);
				fcstCharge.put("RealPlan", bean.get("realplan")!=null?bean.get("realplan"):"");

				list.put(fcstCharge);
			}
			data.put("FcstCharge", list);
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
