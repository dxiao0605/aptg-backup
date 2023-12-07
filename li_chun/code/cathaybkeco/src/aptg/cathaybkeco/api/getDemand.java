package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class getDemand 及時需量
 */
@WebServlet("/getDemand")
public class getDemand extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getDemand.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getDemand() {
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
		logger.debug("getDemand start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			logger.debug("token: " + token);			
			logger.debug("BankCode:" + bankCode + ",DeviceId:"+deviceId);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setBankCode(bankCode);
					meterSetupVO.setDeviceId(deviceId);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> cc = meterSetupDAO.getCC(meterSetupVO);
					List<DynaBean> demandNowList = new ArrayList<DynaBean>();
					List<DynaBean> demandTodayList = new ArrayList<DynaBean>();
					List<DynaBean> demandMonthList = new ArrayList<DynaBean>();
					
					if(StringUtils.isNotBlank(deviceId)) {
						demandNowList = meterSetupDAO.getDemandNow(meterSetupVO);
						demandTodayList = meterSetupDAO.getDemandToday(meterSetupVO);
						demandMonthList = meterSetupDAO.getDemandMonth(meterSetupVO);
					}else {
						PowerAccountVO powerAccountVO = new PowerAccountVO();
						powerAccountVO.setBankCode(bankCode);
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();						
						demandNowList = powerAccountDAO.getDemandNow(powerAccountVO);
						demandTodayList = powerAccountDAO.getDemandToday(powerAccountVO);
						demandMonthList = powerAccountDAO.getDemandMonth(powerAccountVO);
					}		
						
					rspJson.put("msg", convertToJson(cc, demandNowList, demandTodayList, demandMonthList));
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
		logger.debug("rsp: " + rspJson);
		logger.debug("getDemand end");
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
	private JSONObject convertToJson(List<DynaBean> ccList, List<DynaBean> demandNowList, 
			List<DynaBean> demandTodayList, List<DynaBean> demandMonthList) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject demand = new JSONObject();	
			
			BigDecimal cc = BigDecimal.ZERO;
			BigDecimal demandNow = BigDecimal.ZERO;
			BigDecimal demandToday = BigDecimal.ZERO;
			BigDecimal demandMax = BigDecimal.ZERO;
			
			if(ccList!=null) {
				cc = ToolUtil.getBigDecimal(ccList.get(0).get("cc"));
			}
			
			if(demandNowList!=null) {
				for(DynaBean dmBean : demandNowList) {		
					demandNow = demandNow.add(ToolUtil.getBigDecimal(dmBean.get("demandnow")));
				}
				demandNow.setScale(0, BigDecimal.ROUND_DOWN);
			}
			
			if(demandTodayList!=null) {
				for(DynaBean dmBean : demandTodayList) {		
					demandToday = demandToday.add(ToolUtil.getBigDecimal(dmBean.get("demandtoday")));
				}
				demandToday.setScale(0, BigDecimal.ROUND_DOWN);
			}
			
			if(demandMonthList!=null) {
				DynaBean beBean = demandMonthList.get(0);
				demandMax = ToolUtil.getBigDecimal(beBean.get("demandmonth"), 0, BigDecimal.ROUND_DOWN);
				if(demandMax.compareTo(demandToday)<0) {
					demandMax = demandToday;
				}
			}

			demand.put("CC", cc);//契約容量
			demand.put("Demand", demandNow);//目前需量
			demand.put("MaxDemand", demandMax);//最高需量
			demand.put("DemandP", ToolUtil.divide(demandNow, cc, 2).multiply(new BigDecimal(100)));//及時需量百分比
			demand.put("MaxDemandP", ToolUtil.divide(demandMax, cc, 2).multiply(new BigDecimal(100)));//最高需量百分比			
				
			data.put("Demand", demand);		
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
