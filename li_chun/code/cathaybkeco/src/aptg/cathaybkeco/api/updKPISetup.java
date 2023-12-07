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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.KPIDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.KpiVO;

/**
 * Servlet implementation class updKPISetup 修改目標值設定
 */
@WebServlet("/updKPISetup")
public class updKPISetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updKPISetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updKPISetup() {
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
		logger.debug("updKPISetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					KpiVO kpiVO = this.parseJson(req);
					if (kpiVO.isError()) {
						rspJson.put("code", kpiVO.getCode());
						rspJson.put("msg", kpiVO.getDescription());
					} else {
						KPIDAO kpiDAO = new KPIDAO();
						kpiDAO.updKPISetup(kpiVO);
						ToolUtil.addLogRecord(kpiVO.getUserName(), "16", "目標值設定修改");
						
						rspJson.put("code", "00");
						rspJson.put("msg", "Update Success");					
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
		logger.debug("updKPISetup end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return entryBillVO
	 * @throws Exception
	 */
	private KpiVO parseJson(String json) throws Exception {
		KpiVO kpiVO = new KpiVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			
			if(!ToolUtil.isNull(msg, "UnitPriceKPIenabled")) {
				if (!ToolUtil.isNull(msg, "UnitPriceKPI") && !ToolUtil.numberCheck(msg.optString("UnitPriceKPI"))) {
					kpiVO.setError(true);
					kpiVO.setCode("13");
					kpiVO.setDescription("電費單價目標值數字格式錯誤");
				}else {
					kpiVO.setUnitPriceKPI(msg.optString("UnitPriceKPI"));
				}				
				kpiVO.setUnitPriceKPIenabled(msg.optString("UnitPriceKPIenabled"));
			}
			
			if(!ToolUtil.isNull(msg, "EUIKPIenabled")) {
				if (!ToolUtil.isNull(msg, "EUIKPI") && !ToolUtil.numberCheck(msg.optString("EUIKPI"))) {
					kpiVO.setError(true);
					kpiVO.setCode("13");
					kpiVO.setDescription("EUI目標值數字格式錯誤");
				}else {
					kpiVO.setEuiKPI(msg.optString("EUIKPI"));
				}
				kpiVO.setEuiKPIenabled(msg.optString("EUIKPIenabled"));
			}
			
			
			if(!ToolUtil.isNull(msg, "EPUIKPIenabled")) {
				if (!ToolUtil.isNull(msg, "EPUIKPI") && !ToolUtil.numberCheck(msg.optString("EPUIKPI"))) {
					kpiVO.setError(true);
					kpiVO.setCode("13");
					kpiVO.setDescription("EPUI目標值數字格式錯誤");
				}else {
					kpiVO.setEpuiKPI(msg.optString("EPUIKPI"));
				}
				kpiVO.setEpuiKPIenabled(msg.optString("EPUIKPIenabled"));
			}			
			
			if(!ToolUtil.isNull(msg, "AirKPIenabled")) {
				if (!ToolUtil.isNull(msg, "AirKPI") && !ToolUtil.numberCheck(msg.optString("AirKPI")) && msg.optDouble("AirKPI")<0 && msg.optDouble("AirKPI")>100) {
					kpiVO.setError(true);
					kpiVO.setCode("13");
					kpiVO.setDescription("主要空調佔比目標值數字格式錯誤");
				}else {
					kpiVO.setAirKPI(msg.optString("AirKPI"));
				}
				kpiVO.setAirKPIenabled(msg.optString("AirKPIenabled"));
			}
			
			kpiVO.setUserName(msg.optString("UserName"));		
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return kpiVO;
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
