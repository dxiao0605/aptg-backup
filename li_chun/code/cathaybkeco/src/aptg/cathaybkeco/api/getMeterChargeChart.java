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
 * Servlet implementation class getMeterChargeChart 電費計算圖表(電表)
 */
@WebServlet("/getMeterChargeChart")
public class getMeterChargeChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterChargeChart.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterChargeChart() {
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
		logger.debug("getMeterChargeChart start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));

			logger.debug("token: " + token);
			logger.debug("DeviceID: " + deviceId);
			logger.debug("date:" + start + " ~ " + end);
			if (StringUtils.isNotBlank(token) || StringUtils.isNotBlank(deviceId)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else if (!ToolUtil.dateCheck(start, "yyyyMM") || !ToolUtil.dateCheck(end, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setStartDate(start);
					meterSetupVO.setEndDate(end);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getMeterCharge(meterSetupVO);
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
		logger.debug("getMeterChargeChart end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		JSONObject data = new JSONObject();
		try {
			BigDecimal baseChargeTotal = new BigDecimal(0);
			BigDecimal usageChargeTotal = new BigDecimal(0);
			BigDecimal overChargeTotal = new BigDecimal(0);
			BigDecimal totalChargeTotal = new BigDecimal(0);
			JSONArray recDateArr = new JSONArray();
			JSONArray baseChargeArr = new JSONArray();
			JSONArray usageChargeArr = new JSONArray();
			JSONArray overChargeArr = new JSONArray();
			JSONArray totalChargeArr = new JSONArray();
			for (int i=0; i<rows.size(); i++) {
				DynaBean bean = rows.get(i);
				if(i==0) {
					data.put("UsageCode", bean.get("usagecode"));
				}
				BigDecimal baseCharge = new BigDecimal(0), usageCharge = new BigDecimal(0),
						overCharge = new BigDecimal(0), totalCharge = new BigDecimal(0);

				if("1".equals(ObjectUtils.toString(bean.get("usagecode")))) {
					baseCharge = ToolUtil.getBigDecimal(bean.get("basecharge"));
					usageCharge = ToolUtil.getBigDecimal(bean.get("usagecharge"));
					overCharge = ToolUtil.getBigDecimal(bean.get("overcharge"));
					totalCharge = ToolUtil.getBigDecimal(bean.get("totalcharge"));
										
					baseChargeTotal = baseChargeTotal.add(baseCharge);
					usageChargeTotal = usageChargeTotal.add(usageCharge);
					overChargeTotal = overChargeTotal.add(overCharge);			
				}else {
					BigDecimal price = ToolUtil.divide(bean.get("totalcharge"), bean.get("tpmcec"), 2);
					totalCharge = ToolUtil.multiply(price, bean.get("cec"), 0);
				}
				totalChargeTotal = totalChargeTotal.add(totalCharge);	
							
				recDateArr.put(ToolUtil.dateFormat(bean.get("recdate"), sdf));//月份				
				baseChargeArr.put(baseCharge);//基本電費
				usageChargeArr.put(usageCharge);//流動電費
				overChargeArr.put(overCharge);//非約定電費
				totalChargeArr.put(totalCharge);//總電費
				

			}
			data.put("RecDate", recDateArr);//月份
			data.put("BaseCharge", baseChargeArr);//基本電費
			data.put("UsageCharge", usageChargeArr);//流動電費
			data.put("OverCharge", overChargeArr);//非約定電費
			data.put("TotalCharge", totalChargeArr);//總電費
			
			JSONObject total = new JSONObject();
			total.put("BaseCharge", baseChargeTotal);//基本電費
			total.put("UsageCharge", usageChargeTotal);//流動電費
			total.put("OverCharge", overChargeTotal);//非約定電費
			total.put("TotalCharge", totalChargeTotal);//總電費
			data.put("Total", total);
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