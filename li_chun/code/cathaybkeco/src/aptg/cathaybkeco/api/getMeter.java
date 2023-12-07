package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
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
 * Servlet implementation class getMeter 電表資訊
 */
@WebServlet("/getMeter")
public class getMeter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeter.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeter() {
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
		logger.debug("getMeter start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));			
			
			logger.debug("token: " + token);			
			logger.debug("DeviceID: " + deviceId);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(deviceId)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setDeviceId(deviceId);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> meterInfoList = meterSetupDAO.getMeterDetail(meterSetupVO);		

					rspJson.put("code", "00");
					if (meterInfoList != null && meterInfoList.size() > 0) {
						List<DynaBean> priorityList = meterSetupDAO.getPriority(meterSetupVO);
						List<DynaBean> maxDemandList = meterSetupDAO.getDemandNow(meterSetupVO);
						rspJson.put("msg", convertToJson(meterInfoList, priorityList, maxDemandList));
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
		logger.debug("getMeter end");
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
	private JSONObject convertToJson(List<DynaBean> meterInfoList, List<DynaBean> priorityList, List<DynaBean> maxDemandList) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject meter = new JSONObject();					
			DynaBean bean = meterInfoList.get(0);
			meter.put("BankCode", ObjectUtils.toString(bean.get("bankcode")));// 分行代碼
			meter.put("BankName", ObjectUtils.toString(bean.get("bankname")));// 分行名稱
			meter.put("MeterName", ObjectUtils.toString(bean.get("metername")));// 電表名稱
			meter.put("InstallPosition", ObjectUtils.toString(bean.get("installposition")));// 安裝位置
			meter.put("UsageCode", bean.get("usagecode"));// 耗能分類代碼
			meter.put("UsageDesc", bean.get("usagedesc"));// 耗能分類						
			meter.put("Area",  ToolUtil.getBigDecimal(bean.get("area")));// 面積
			meter.put("People", ToolUtil.getBigDecimal(bean.get("people")));// 員工數
			meter.put("PowerAccount", bean.get("poweraccount"));// 電號
			meter.put("AccountDesc", ObjectUtils.toString(bean.get("accountdesc")));// 說明
			meter.put("RatePlanDesc", bean.get("rateplandesc"));// 用電類型
			BigDecimal cc = ToolUtil.getBigDecimal(bean.get("cc"));
			meter.put("CC", cc);// 契約容量
			meter.put("MeterType", bean.get("metertype"));// 電表型號
			meter.put("WiringDesc", bean.get("wiringdesc"));// 接線方式
			if(priorityList!=null && priorityList.size()>0) {
				DynaBean priority = priorityList.get(0);
				if(ToolUtil.add(priority.get("eventcount1"), priority.get("eventcount2")).compareTo(new BigDecimal(0))>0) {
					meter.put("Priority", "2");
				}else if(ToolUtil.add(priority.get("eventcount3"), priority.get("eventcount4")).compareTo(new BigDecimal(0))>0) {
					meter.put("Priority", "1");
				}else {
					meter.put("Priority", "0");
				}
			}else {
				meter.put("Priority", "");
			}		
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
