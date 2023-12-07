package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
 * Servlet implementation class getPowerRecordChart 電力趨勢
 */
@WebServlet("/getPowerRecordChart")
public class getPowerRecordChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerRecordChart.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerRecordChart() {
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
		logger.debug("getPowerRecordChart start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String dateformat = ObjectUtils.toString(request.getParameter("dateformat"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode +",DeviceID:" + deviceId);
			logger.debug("date:" + start + " ~ " + end + ",Dateformat:" + dateformat);
			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else if (!ToolUtil.dateCheck(start, "yyyyMMdd") || !ToolUtil.dateCheck(end, "yyyyMMdd")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMMdd)");
				} else {
					PowerRecordVO powerRecordVO = new PowerRecordVO();
					powerRecordVO.setBankCode(bankCode);
					powerRecordVO.setDeviceId(deviceId);
					powerRecordVO.setDateformat(dateformat);
					powerRecordVO.setStartDate(start);
					powerRecordVO.setEndDate(end);
					PowerRecordDAO powerRecordDAO = new PowerRecordDAO();
					List<DynaBean> list = powerRecordDAO.getPowerRecordChart(powerRecordVO);
					rspJson.put("code", "00");
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
		logger.debug("getPowerRecordChart end");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
		JSONObject data = new JSONObject();
		try {	
			JSONArray rectime = new JSONArray();
			JSONArray w = new JSONArray();
			JSONArray var = new JSONArray();
			JSONArray va = new JSONArray();
			JSONArray i1 = new JSONArray();
			JSONArray i2 = new JSONArray();
			JSONArray i3 = new JSONArray();
			JSONArray iavg = new JSONArray();
			JSONArray v1 = new JSONArray();
			JSONArray v2 = new JSONArray();
			JSONArray v3 = new JSONArray();
			JSONArray vavg = new JSONArray();
			JSONArray v12 = new JSONArray();
			JSONArray v23 = new JSONArray();
			JSONArray v31 = new JSONArray();
			JSONArray vavgP = new JSONArray();
			JSONArray mode1 = new JSONArray();
			JSONArray mode2 = new JSONArray();
			JSONArray mode3 = new JSONArray();
			JSONArray mode4 = new JSONArray();
			
			List<DynaBean> sortRows = new ArrayList<DynaBean>();			
			sortRows = rows.stream().sorted(Comparator.comparing(
		    		new Function<DynaBean, Date>() { 
						public Date apply(DynaBean bean) {						
								return ToolUtil.parseDate(bean.get("rectime"),sdf);					
						}		
			        }
		    )).collect(Collectors.toList());   
			
			for(DynaBean bean : sortRows) {		
				rectime.put(ToolUtil.dateFormat(bean.get("rectime"), sdf));
				w.put(bean.get("w")!=null?bean.get("w"):"");
				var.put(bean.get("var")!=null?bean.get("var"):"");
				va.put(bean.get("va")!=null?bean.get("va"):"");
				i1.put(bean.get("i1")!=null?bean.get("i1"):"");
				i2.put(bean.get("i2")!=null?bean.get("i2"):"");
				i3.put(bean.get("i3")!=null?bean.get("i3"):"");
				iavg.put(bean.get("iavg")!=null?bean.get("iavg"):"");
				v1.put(bean.get("v1")!=null?bean.get("v1"):"");
				v2.put(bean.get("v2")!=null?bean.get("v2"):"");
				v3.put(bean.get("v3")!=null?bean.get("v3"):"");
				vavg.put(bean.get("vavg")!=null?bean.get("vavg"):"");			
				v12.put(bean.get("v12")!=null?bean.get("v12"):"");
				v23.put(bean.get("v23")!=null?bean.get("v23"):"");
				v31.put(bean.get("v31")!=null?bean.get("v31"):"");
				vavgP.put(bean.get("vavgp")!=null?bean.get("vavgp"):"");
				mode1.put(bean.get("mode1")!=null?bean.get("mode1"):"");
				mode2.put(bean.get("mode2")!=null?bean.get("mode2"):"");
				mode3.put(bean.get("mode3")!=null?bean.get("mode3"):"");
				mode4.put(bean.get("mode4")!=null?bean.get("mode4"):"");
			}
			
			data.put("RecTime", rectime);
			data.put("W", w);//實功
			data.put("WLable", "實功(KW)");
			data.put("RP", var);//虛功
			data.put("RPLable", "虛功(Kvar)");
			data.put("VA", va);//視在
			data.put("VALable", "視在(KVA)");
			data.put("I1", i1);//電流R相
			data.put("I1Lable", "電流R相(A)");
			data.put("I2", i2);//電流S相
			data.put("I2Lable", "電流S相(A)");
			data.put("I3", i3);//電流T相
			data.put("I3Lable", "電流T相(A)");
			data.put("Iavg", iavg);//平均電流
			data.put("IavgLable", "平均電流(A)");
			data.put("V1", v1);//電壓R相
			data.put("V1Lable", "電壓R相(V)");
			data.put("V2", v2);//電壓S相
			data.put("V2Lable", "電壓S相(V)");
			data.put("V3", v3);//電壓T相
			data.put("V3Lable", "電壓T相(V)");
			data.put("Vavg", vavg);//平均電壓
			data.put("VavgLable", "平均電壓(V)");
			data.put("V12", v12);//線電壓R相
			data.put("V12Lable", "線電壓R相(V)");
			data.put("V23", v23);//線電壓S相
			data.put("V23Lable", "線電壓S相(V)");
			data.put("V31", v31);//線電壓T相
			data.put("V31Lable", "線電壓T相(V)");
			data.put("VavgP", vavgP);//平均線電壓
			data.put("VavgPLable", "平均線電壓(V)");
			data.put("Mode1", mode1);//混合式
			data.put("Mode1Lable", "混合式(KW)");
			data.put("Mode2", mode2);//浮動式
			data.put("Mode2Lable", "浮動式(KW)");
			data.put("Mode3", mode3);//固定式
			data.put("Mode3Lable", "固定式(KW)");
			data.put("Mode4", mode4);//平均式
			data.put("Mode4Lable", "平均式(KW)");
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
