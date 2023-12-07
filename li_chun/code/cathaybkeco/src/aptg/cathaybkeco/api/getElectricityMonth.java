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
 * Servlet implementation class getElectricityMonth 當月電力資訊
 */
@WebServlet("/getElectricityMonth")
public class getElectricityMonth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getElectricityMonth.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getElectricityMonth() {
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
		logger.debug("getElectricityMonth start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String date = ObjectUtils.toString(request.getParameter("date"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode + ",DeviceID:" + deviceId + ",Date:" + date);
			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setBankCode(bankCode);
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setDate(date);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getElectricityDaily(meterSetupVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
					if (list != null && list.size() > 0) {
						List<DynaBean> cc = meterSetupDAO.getCC(meterSetupVO);
						rspJson.put("msg", convertToJson(list, cc));
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
		logger.debug("getElectricityMonth end");
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
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		JSONObject data = new JSONObject();
		try {
			BigDecimal cc = BigDecimal.ZERO;
			if(ccList!=null) {
				cc = ToolUtil.getBigDecimal(ccList.get(0).get("cc"));
			}
			
			JSONArray list = new JSONArray();		
			for(DynaBean bean : rows) {
				JSONObject electricity = new JSONObject();
				electricity.put("RecDate", ToolUtil.dateFormat(bean.get("recdate"), sdf));
				electricity.put("DCECPK", ToolUtil.getBigDecimal(bean.get("tpdcecpk")));// 尖峰用電量
				electricity.put("DCECSP", ToolUtil.getBigDecimal(bean.get("tpdcecsp")));// 半尖峰用電量
				electricity.put("DCECSatSP", ToolUtil.getBigDecimal(bean.get("tpdcecsatsp")));// 周六半尖峰用電量
				electricity.put("DCECOP", ToolUtil.getBigDecimal(bean.get("tpdcecop")));// 離峰用電量
				electricity.put("Demand", ToolUtil.getBigDecimal(bean.get("demand"), 0, BigDecimal.ROUND_DOWN));//需量
				electricity.put("CC", cc);//契約容量
				list.put(electricity);	
			}
			data.put("Electricity", list);
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
