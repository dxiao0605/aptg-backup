package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
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

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getElectricityChartMonthly 電能比對圖(月)
 */
@WebServlet("/getElectricityChartMonthly")
public class getElectricityChartMonthly extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getElectricityChartMonthly.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getElectricityChartMonthly() {
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
		logger.debug("getElectricityChartMonthly start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode+",DeviceID:" +deviceId);
			logger.debug("date:" + start + " ~ " + end);	
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else if (!ToolUtil.dateCheck(start, "yyyyMM") || !ToolUtil.dateCheck(end, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setBankCode(bankCode);
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setStartDate(start);
					meterSetupVO.setEndDate(end);
					meterSetupVO.setRecType("1");//月
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getElectricityMonthly(meterSetupVO);			
					if (list != null && list.size() > 0) {		
						JSONObject msg = new JSONObject();
						List<DynaBean> cc = meterSetupDAO.getCC(meterSetupVO);
						msg.put("Now", convertToJson(list, cc));
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
						meterSetupVO.setStartDate(ToolUtil.getLastYearDay(start, sdf));
						meterSetupVO.setEndDate(ToolUtil.getLastYearDay(end, sdf));
						List<DynaBean> lastList = meterSetupDAO.getElectricityMonthly(meterSetupVO);
						List<DynaBean> lastcc = meterSetupDAO.getCC(meterSetupVO);
						msg.put("Last", convertToJson(lastList, lastcc));
						rspJson.put("msg", msg);
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
		logger.debug("getElectricityChartMonthly end");
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
	private JSONObject convertToJson(List<DynaBean> rows, List<DynaBean> ccList) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		JSONObject data = new JSONObject();
		try {	
			BigDecimal cc = BigDecimal.ZERO;
			if(ccList!=null) {
				cc = ToolUtil.getBigDecimal(ccList.get(0).get("cc"));
			}
			
			BigDecimal maxDemand = BigDecimal.ZERO;
			JSONArray recdate = new JSONArray();
			JSONArray dcecpk = new JSONArray();
			JSONArray dcecsp = new JSONArray();
			JSONArray dcecsatsp = new JSONArray();
			JSONArray dcecop = new JSONArray();
			JSONArray eco5pk = new JSONArray();
			JSONArray eco5sp = new JSONArray();
			JSONArray eco5satsp = new JSONArray();
			JSONArray eco5op = new JSONArray();
			JSONArray demand = new JSONArray();
			JSONArray df = new JSONArray();		
			for(int i=0; i<rows.size(); i++) {
				DynaBean bean = rows.get(i);
				recdate.put(ToolUtil.dateFormat(bean.get("recdate"), sdf));		
				dcecpk.put(ToolUtil.getBigDecimal(bean.get("tpmcecpk"))); 
				dcecsp.put(ToolUtil.getBigDecimal(bean.get("tpmcecsp"))); 
				dcecsatsp.put(ToolUtil.getBigDecimal(bean.get("tpmcecsatsp")));
				dcecop.put(ToolUtil.getBigDecimal(bean.get("tpmcecop"))); 
				eco5pk.put(ToolUtil.getBigDecimal(bean.get("mcecpk"))); 
				eco5sp.put(ToolUtil.getBigDecimal(bean.get("mcecsp"))); 
				eco5satsp.put(ToolUtil.getBigDecimal(bean.get("mcecsatsp")));
				eco5op.put(ToolUtil.getBigDecimal(bean.get("mcecop")));
				demand.put(ToolUtil.getBigDecimal(bean.get("demand")).setScale(0, BigDecimal.ROUND_DOWN));				
				df.put(ToolUtil.getBigDecimal(bean.get("df")));
				if(ToolUtil.getBigDecimal(bean.get("demand")).compareTo(maxDemand)>0) {
					maxDemand = ToolUtil.getBigDecimal(bean.get("demand"));
				}
			}
			data.put("RecDate", recdate);		
			data.put("DCECPK", dcecpk);// 台電尖峰用電量
			data.put("DCECSP", dcecsp);// 台電半尖峰用電量
			data.put("DCECSatSP", dcecsatsp);// 台電周六半尖峰用電量
			data.put("DCECOP", dcecop);// 台電離峰用電量
			data.put("ECO5PK", eco5pk);// ECO5尖峰用電量
			data.put("ECO5SP", eco5sp);// ECO5半尖峰用電量
			data.put("ECO5SatSP", eco5satsp);// ECO5周六半尖峰用電量
			data.put("ECO5OP", eco5op);// ECO5離峰用電量
			data.put("Demand", demand);//需量			
			data.put("DF", df);//需量預測
			data.put("MaxDemand", maxDemand.setScale(0, BigDecimal.ROUND_DOWN));//最大需量
			data.put("CC", cc);//契約容量
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
