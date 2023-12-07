package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getEUIAndEPUIMonthly EUI&EPUI月表格
 */
@WebServlet("/getEUIAndEPUIMonthly")
public class getEUIAndEPUIMonthly extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getEUIAndEPUIMonthly.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getEUIAndEPUIMonthly() {
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
		logger.debug("getEUIAndEPUIMonthly start");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf3 = new SimpleDateFormat("MM");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));		
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			String last = ObjectUtils.toString(request.getParameter("last"));
			String pk = ObjectUtils.toString(request.getParameter("pk"));
			String sp = ObjectUtils.toString(request.getParameter("sp"));
			String satsp = ObjectUtils.toString(request.getParameter("satsp"));
			String op = ObjectUtils.toString(request.getParameter("op"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode +",DeviceID:" +deviceId+  ",date:" + start + " ~ " + end);
			logger.debug("Last:"+last+",PK"+pk+",SP"+sp+",SatSP"+satsp+",OP"+op);
			
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
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getElectricityMonthly(meterSetupVO);
					rspJson.put("code", "00");
					if (list != null && list.size() > 0) {
						JSONObject data = new JSONObject();
						BigDecimal cec, lastcec, cecSum = BigDecimal.ZERO, area = BigDecimal.ZERO, people = BigDecimal.ZERO;
						JSONArray arr = new JSONArray();
						List<DynaBean> lastList = new ArrayList<DynaBean>();
						if("1".equals(last)) {
							meterSetupVO.setStartDate(ToolUtil.getLastYearDay(start, sdf2));
							meterSetupVO.setEndDate(ToolUtil.getLastYearDay(end, sdf2));
							lastList = meterSetupDAO.getElectricityMonthly(meterSetupVO);		
						}
						int j = 0;
						for(int i=0; i<list.size(); i++) {
							JSONObject eui = new JSONObject();
							DynaBean bean = list.get(i);
							if(i==0) {
								area = ToolUtil.getBigDecimal(bean.get("area"));
								people = ToolUtil.getBigDecimal(bean.get("people"));
							}
							cec = BigDecimal.ZERO;
							if("1".equals(pk))
								cec = cec.add(ToolUtil.getBigDecimal(bean.get("tpmcecpk")));
							if("1".equals(sp))
								cec = cec.add(ToolUtil.getBigDecimal(bean.get("tpmcecsp")));
							if("1".equals(satsp))
								cec = cec.add(ToolUtil.getBigDecimal(bean.get("tpmcecsatsp")));
							if("1".equals(op))
								cec = cec.add(ToolUtil.getBigDecimal(bean.get("tpmcecop")));
							
							String recdate = ToolUtil.dateFormat(bean.get("recdate"), sdf3);
							eui.put("Seq", i+1);
							eui.put("RecDate", ToolUtil.dateFormat(bean.get("recdate"), sdf));			
							eui.put("EUI", ToolUtil.divide(cec, area, 2));
							eui.put("EPUI", ToolUtil.divide(cec, people, 2));
							cecSum = cecSum.add(cec);	
							
							if("1".equals(last) && lastList!=null && j<lastList.size()) {
								DynaBean lastBean = lastList.get(j);
								if(recdate.equals(ToolUtil.dateFormat(lastBean.get("recdate"), sdf3))){
									lastcec = BigDecimal.ZERO;
									if("1".equals(pk))
										lastcec = lastcec.add(ToolUtil.getBigDecimal(lastBean.get("tpmcecpk")));
									if("1".equals(sp))
										lastcec = lastcec.add(ToolUtil.getBigDecimal(lastBean.get("tpmcecsp")));
									if("1".equals(satsp))
										lastcec = lastcec.add(ToolUtil.getBigDecimal(lastBean.get("tpmcecsatsp")));
									if("1".equals(op))
										lastcec = lastcec.add(ToolUtil.getBigDecimal(lastBean.get("tpmcecop")));
									eui.put("LastEUI", ToolUtil.divide(lastcec, area, 2));
									eui.put("LastEPUI", ToolUtil.divide(lastcec, people, 2));
									j++;
								}else {
									eui.put("LastEUI", "");
									eui.put("LastEPUI", "");
								}
							}
							arr.put(eui);
						}
						data.put("EUIAndEPUI", arr);
						data.put("EUITotal", ToolUtil.divide(cecSum, area, 2));
						data.put("EPUITotal", ToolUtil.divide(cecSum, people, 2));
	
						rspJson.put("msg", data);
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
		logger.debug("getEUIAndEPUIMonthly end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
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
