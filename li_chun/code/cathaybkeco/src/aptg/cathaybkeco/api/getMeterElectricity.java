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
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getMeterElectricity 電表用電資料
 */
@WebServlet("/getMeterElectricity")
public class getMeterElectricity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterElectricity.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterElectricity() {
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
		logger.debug("getMeterElectricity start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));			
			String date = ObjectUtils.toString(request.getParameter("date"));
			logger.debug("token: " + token);			
			logger.debug("DeviceID: " + deviceId + ",Date:" +date);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(date)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setDate(date);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> meterElectricityList = meterSetupDAO.getMeterElectricity(meterSetupVO);
					if (meterElectricityList != null && meterElectricityList.size() > 0) {
						List<DynaBean> demandNowList = new ArrayList<DynaBean>();
						List<DynaBean> demandTodayList = new ArrayList<DynaBean>();
						if(ToolUtil.isThisMonth(date)) {//當月才需要查詢目前需量
							demandNowList = meterSetupDAO.getDemandNow(meterSetupVO);
							demandTodayList = meterSetupDAO.getDemandToday(meterSetupVO);
						}
												
						List<DynaBean> ccList = meterSetupDAO.getCC(meterSetupVO);
						rspJson.put("msg", convertToJson(date, meterElectricityList, ccList, demandNowList, demandTodayList));
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
		logger.debug("rsp: " + rspJson);
		logger.debug("getMeterElectricity end");
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
	private JSONObject convertToJson(String date, List<DynaBean> meterElectricityList, List<DynaBean> ccList,
			List<DynaBean> demandNowList, List<DynaBean> demandTodayList) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject meter = new JSONObject();					
			DynaBean bean = meterElectricityList.get(0);
		
			BigDecimal cc = BigDecimal.ZERO;
			if(ccList!=null) {
				cc = ToolUtil.getBigDecimal(ccList.get(0).get("cc"));
			}
			
			BigDecimal demandNow = BigDecimal.ZERO;
			BigDecimal demandToday = BigDecimal.ZERO;
			if(demandNowList!=null) {
				for(DynaBean dmBean : demandNowList) {		
					demandNow = demandNow.add(ToolUtil.getBigDecimal(dmBean.get("demandnow")));
				}
				demandNow = demandNow.setScale(0, BigDecimal.ROUND_DOWN);
			}
			
			if(demandTodayList!=null) {
				for(DynaBean dmBean : demandTodayList) {		
					demandToday = demandToday.add(ToolUtil.getBigDecimal(dmBean.get("demandtoday")));
				}
				demandToday = demandToday.setScale(0, BigDecimal.ROUND_DOWN);
			}
		
			meter.put("Month", date.substring(4, 6));
			if(ToolUtil.isThisMonth(date) && (
					ToolUtil.isMonthFirstDay() ||
					ToolUtil.parseDouble(bean.get("mcecsp"))<=0 ||
					ToolUtil.parseDouble(bean.get("mcecsatsp"))<=0 || 
					ToolUtil.parseDouble(bean.get("mcecop"))<=0 )) {//非當月一日及四個時段都有累積用電量時，才有值									
				meter.put("MCEC", "--");
				meter.put("TotalCharge", "--");
				meter.put("EUI", "--");
				meter.put("EPUI", "--");
				meter.put("FcstMCEC", "--");			
				meter.put("FcstTotalCharge", "--");
				meter.put("FcstEPUI", "--");	
				meter.put("FcstEUI", "--");
			}else {
				meter.put("MCEC", ToolUtil.getBigDecimal(bean.get("cec")));//目前總用電量
				meter.put("TotalCharge", ToolUtil.divide(ToolUtil.multiply(bean.get("totalcharge"),bean.get("cec")), bean.get("mcec"), 0));//目前電費
				meter.put("EUI", ToolUtil.divide(bean.get("cec"), bean.get("area"), 2));//目前EUI
				meter.put("EPUI", ToolUtil.divide(bean.get("cec"), bean.get("people"), 2));//目前EPUI
				meter.put("FcstMCEC", ToolUtil.getBigDecimal(bean.get("fcsteco5mcec")));//預估總用電量			
				meter.put("FcstTotalCharge", ToolUtil.divide(ToolUtil.multiply(bean.get("fcsttotalcharge"),bean.get("fcsteco5mcec")), bean.get("fcstmcec"), 0));//預估電費
				meter.put("FcstEPUI", ToolUtil.divide(bean.get("fcsteco5mcec"), bean.get("people"), 2));//預估EPUI	
				meter.put("FcstEUI", ToolUtil.divide(bean.get("fcsteco5mcec"), bean.get("area"), 2));//預估EUI
			}
			meter.put("CC", cc);//契約容量
			meter.put("Demand", demandNow);//目前需量
			BigDecimal demandMax = ToolUtil.getBigDecimal(bean.get("maxdemand"), 0, BigDecimal.ROUND_DOWN);
			if(demandMax.compareTo(demandToday)<0) {
				demandMax = demandToday;
			}
			
			meter.put("MaxDemand", ToolUtil.getBigDecimal(demandMax, 0, BigDecimal.ROUND_DOWN));//最高需量
			meter.put("DemandP", ToolUtil.divide(demandNow, cc, 2).multiply(new BigDecimal(100)));//及時需量百分比
			meter.put("MaxDemandP", ToolUtil.divide(demandMax, cc, 2).multiply(new BigDecimal(100)));//最高需量百分比
							
			data.put("Meter", meter);		
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
