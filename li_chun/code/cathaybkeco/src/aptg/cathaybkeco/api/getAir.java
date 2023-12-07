package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;

/**
 * Servlet implementation class getAir 空調用電分析
 */
@WebServlet("/getAir")
public class getAir extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getAir.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAir() {
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
		logger.debug("getAir start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String date = ObjectUtils.toString(request.getParameter("date"));
			String pk = ObjectUtils.toString(request.getParameter("pk"));
			String sp = ObjectUtils.toString(request.getParameter("sp"));
			String satsp = ObjectUtils.toString(request.getParameter("satsp"));
			String op = ObjectUtils.toString(request.getParameter("op"));
			String eco5 = ObjectUtils.toString(request.getParameter("eco5"));
			
			logger.debug("token: " + token);			
			logger.debug("BankCode:" + bankCode+ ",Date:"+date+",PK:"+pk+",SP:"+sp+",SatSP:"+satsp+",OP:"+op+",ECO5:"+eco5);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				}else if (!ToolUtil.dateCheck(date, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				} else {
					BankInfVO bankInfVO = new BankInfVO();
					bankInfVO.setBankCode(bankCode);
					if("1".equals(eco5)) {
						bankInfVO.setEco5(true);	
					}					
					bankInfVO.setDate(date);
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> thisMonthList = bankInfDAO.getBankAir(bankInfVO);
					
					rspJson.put("code", "00");
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");	
					JSONObject msg = new JSONObject();
					msg.put("ThisMonth", convertToJson(thisMonthList, pk, sp, satsp, op, bankInfVO.getDate()));
					
					bankInfVO.setDate(ToolUtil.getLastMonthDay(date, sdf));
					List<DynaBean> lastMonthList = bankInfDAO.getBankAir(bankInfVO);
					msg.put("LastMonth", convertToJson(lastMonthList, pk, sp, satsp, op, bankInfVO.getDate()));
					
					bankInfVO.setDate(ToolUtil.getLastYearDay(date, sdf));
					List<DynaBean> lastYearList = bankInfDAO.getBankAir(bankInfVO);
					msg.put("LastYear", convertToJson(lastYearList, pk, sp, satsp, op, bankInfVO.getDate()));			
					
					rspJson.put("msg", msg);
					
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
		logger.debug("getAir end");
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
	private JSONObject convertToJson(List<DynaBean> rows, String pk, String sp, String satsp, String op, String date) throws Exception {
        DecimalFormat df = new DecimalFormat("#,###0.##");
		JSONObject air = new JSONObject();
		try {
			air.put("Month", date.substring(4, 6));
			BigDecimal area = BigDecimal.ZERO, people = BigDecimal.ZERO, cec, fcstcec, 
					price = BigDecimal.ZERO, fcstPrice = BigDecimal.ZERO,
			        totalCEC = BigDecimal.ZERO,  otherCEC = BigDecimal.ZERO;
			String usageCode;
			
			JSONArray usageArr = new JSONArray();
			JSONArray percentArr = new JSONArray();
			JSONArray titleArr = new JSONArray();
			boolean isNotNull = false;
			for(int i=0; i<rows.size(); i++) {
				DynaBean bean = rows.get(i);
				if(i==0) {
					area = ToolUtil.getBigDecimal(bean.get("area"));
					people = ToolUtil.getBigDecimal(bean.get("people"));
				}
				usageCode = ObjectUtils.toString(bean.get("usagecode"));
				cec = BigDecimal.ZERO;
				fcstcec = BigDecimal.ZERO;
				
				if(!(ToolUtil.isThisMonth(date) && (
						ToolUtil.isMonthFirstDay() ||
						ToolUtil.parseDouble(bean.get("sp"))<=0 ||
						ToolUtil.parseDouble(bean.get("satsp"))<=0 || 
						ToolUtil.parseDouble(bean.get("op"))<=0 ))) {//非當月一日及四個時段都有累積用電量時，才有值	
					isNotNull = true;
				}
				
				if("1".equals(pk)) {
					cec = cec.add(ToolUtil.getBigDecimal(bean.get("mcecpk")));
					fcstcec = fcstcec.add(ToolUtil.getBigDecimal(bean.get("fcstmcecpk")));
				}
				
				if("1".equals(sp)) {
					cec = cec.add(ToolUtil.getBigDecimal(bean.get("mcecsp")));
					fcstcec = fcstcec.add(ToolUtil.getBigDecimal(bean.get("fcstmcecsp")));
				}
					
				if("1".equals(satsp)) {
					cec = cec.add(ToolUtil.getBigDecimal(bean.get("mcecsatsp")));
					fcstcec = fcstcec.add(ToolUtil.getBigDecimal(bean.get("fcstmcecsatsp")));
				}
					
				if("1".equals(op)) {
					cec = cec.add(ToolUtil.getBigDecimal(bean.get("mcecop")));
					fcstcec = fcstcec.add(ToolUtil.getBigDecimal(bean.get("fcstmcecop")));
				}
					
				if("1".equals(usageCode)) {
					if(isNotNull) {
						//總用電
						totalCEC = cec;					
						price = ToolUtil.divide(bean.get("totalcharge"), bean.get("mcec"), 2);
						fcstPrice = ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2);
						air.put("CEC", cec);
						air.put("TotalCharge", ToolUtil.getBigDecimal(bean.get("totalcharge")));
						air.put("EUI", ToolUtil.divide(cec, area, 2));
						air.put("EPUI", ToolUtil.divide(cec, people, 2));
					}else {
						air.put("CEC", "--");
						air.put("TotalCharge", "--");
						air.put("EUI", "--");
						air.put("EPUI", "--");
					}
				}else if("2".equals(usageCode)) {
					if(isNotNull) {
						//主要空調
						air.put("AirCEC", cec);
						air.put("AirCharge", ToolUtil.multiply(price, cec, 0));
						air.put("AirFcstCharge", ToolUtil.multiply(fcstPrice, fcstcec, 0));
						air.put("AirEUI", ToolUtil.divide(cec, area, 2));
						air.put("AirEPUI", ToolUtil.divide(cec, people, 2));
					}else {
						//主要空調
						air.put("AirCEC", "--");
						air.put("AirCharge", "--");
						air.put("AirFcstCharge", "--");
						air.put("AirEUI", "--");
						air.put("AirEPUI", "--");
					}
				}									
				if(StringUtils.isNotBlank(usageCode)&&!"1".equals(usageCode)) {
					otherCEC = otherCEC.add(cec);
					usageArr.put(usageCode);
					percentArr.put(ToolUtil.divide(cec, totalCEC, 2).multiply(new BigDecimal(100)));
					titleArr.put(ObjectUtils.toString(bean.get("usagedesc"))+"-"+
								ToolUtil.divide(cec, totalCEC, 2).multiply(new BigDecimal(100))+"% "
								+df.format(cec)+"KWH");
				}
			}
			
			otherCEC = totalCEC.subtract(otherCEC);
			//其他
			if(otherCEC.compareTo(BigDecimal.ZERO)>0) {
				usageArr.put("99");
				percentArr.put(ToolUtil.divide(otherCEC, totalCEC, 2).multiply(new BigDecimal(100)));
				titleArr.put("其他-"+ToolUtil.divide(otherCEC, totalCEC, 2).multiply(new BigDecimal(100))+"% "
							+df.format(otherCEC)+"KWH");
			}
			
			air.put("Usage", usageArr);
			air.put("Percent", percentArr);
			air.put("Title", titleArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return air;
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
