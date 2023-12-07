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

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;
import aptg.cathaybkeco.vo.MeterSetupVO;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class getBankElectricity 分行用電資訊
 */
@WebServlet("/getBankElectricity")
public class getBankElectricity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBankElectricity.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBankElectricity() {
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
		logger.debug("getBankElectricity start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String date = ObjectUtils.toString(request.getParameter("date"));
			logger.debug("token: " + token);			
			logger.debug("BankCode:" + bankCode + ",Date:" +date);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(bankCode) && StringUtils.isNotBlank(date)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BankInfVO bankInfVO = new BankInfVO();
					bankInfVO.setBankCode(bankCode);
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> bankInfoList = bankInfDAO.getBankInf(bankInfVO);	
					if (bankInfoList != null && bankInfoList.size()>0) {
						PowerAccountVO powerAccountVO = new PowerAccountVO();
						powerAccountVO.setBankCode(bankCode);
						powerAccountVO.setDate(date);
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
						List<DynaBean> bankElectricityList = powerAccountDAO.getBankElectricity(powerAccountVO);
						List<DynaBean> demandNowList = new ArrayList<DynaBean>();
						List<DynaBean> demandTodayList = new ArrayList<DynaBean>();
						if(ToolUtil.isThisMonth(date)) {//當月才需要查詢目前需量
							demandNowList = powerAccountDAO.getDemandNow(powerAccountVO);
							demandTodayList = powerAccountDAO.getDemandToday(powerAccountVO);
						}

						MeterSetupVO meterSetupVO = new MeterSetupVO();
						meterSetupVO.setBankCode(bankCode);
						meterSetupVO.setDate(date);
						MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
						List<DynaBean> ccList = meterSetupDAO.getCC(meterSetupVO);
						List<DynaBean> demandMaxList = meterSetupDAO.getElectricityDaily(meterSetupVO);
						
						rspJson.put("msg", convertToJson(date, bankInfoList, ccList, bankElectricityList, demandNowList, demandTodayList, demandMaxList));
						rspJson.put("code", "00");
					} else {						
						rspJson.put("msg", "查無資料");
						rspJson.put("code", "07");
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
		logger.debug("getBankElectricity end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}
	
	/**
	 * 組Json
	 * @param date
	 * @param bankInfoList
	 * @param ccList
	 * @param bankElectricityList
	 * @param demandNowList
	 * @param demandTodayList
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(String date, List<DynaBean> bankInfoList, List<DynaBean> ccList, List<DynaBean> bankElectricityList, 
			List<DynaBean> demandNowList, List<DynaBean> demandTodayList,List<DynaBean> demandMaxList) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject bank = new JSONObject();	
			DynaBean bean = bankInfoList.get(0);
			
			BigDecimal cc = BigDecimal.ZERO;
			if(ccList!=null) {
				cc = ToolUtil.getBigDecimal(ccList.get(0).get("cc"));
			}
			
			if(bankElectricityList!=null && bankElectricityList.size()>0) {
				BigDecimal demandNow = BigDecimal.ZERO;
				BigDecimal demandMax = BigDecimal.ZERO;
				if(demandNowList!=null) {
					for(DynaBean dmBean : demandNowList) {		
						demandNow = demandNow.add(ToolUtil.getBigDecimal(dmBean.get("demandnow")));
					}
					demandNow = demandNow.setScale(0, BigDecimal.ROUND_DOWN);
				}
				
				if(demandTodayList!=null) {
					for(DynaBean dmBean : demandTodayList) {		
						demandMax = demandMax.add(ToolUtil.getBigDecimal(dmBean.get("demandtoday")));
					}
					demandMax = demandMax.setScale(0, BigDecimal.ROUND_DOWN);
				}
				
				if(demandMaxList!=null) {					
					for(DynaBean dmBean : demandMaxList) {
						if(demandMax.compareTo(ToolUtil.getBigDecimal(dmBean.get("demand")))<0) {
							demandMax = ToolUtil.getBigDecimal(dmBean.get("demand"));
						}
					}
					demandMax = demandMax.setScale(0, BigDecimal.ROUND_DOWN);
				}

				DynaBean beBean = bankElectricityList.get(0);
				bank.put("Month", date.substring(4, 6));
				if(ToolUtil.isThisMonth(date) && (
						ToolUtil.isMonthFirstDay() ||
						ToolUtil.parseDouble(beBean.get("mcecsp"))<=0 ||
						ToolUtil.parseDouble(beBean.get("mcecsatsp"))<=0 || 
						ToolUtil.parseDouble(beBean.get("mcecop"))<=0 )) {//非當月一日及四個時段都有累積用電量時，才有值									
					bank.put("MCEC", "--");
					bank.put("TotalCharge", "--");
					bank.put("EUI", "--");
					bank.put("EPUI", "--");
					bank.put("FcstMCEC", "--");				
					bank.put("FcstTotalCharge", "--");
					bank.put("FcstEUI", "--");
					bank.put("FcstEPUI", "--");
				}else {
					bank.put("MCEC", ToolUtil.getBigDecimal(beBean.get("mcec")));//目前總用電量
					bank.put("TotalCharge", ToolUtil.getBigDecimal(beBean.get("totalcharge")));//目前電費
					bank.put("EUI", ToolUtil.divide(beBean.get("mcec"), bean.get("area"), 2));//目前EUI
					bank.put("EPUI", ToolUtil.divide(beBean.get("mcec"), bean.get("people"), 2));//目前EPUI
					bank.put("FcstMCEC", ToolUtil.getBigDecimal(beBean.get("fcstmcec")));//預估總用電量				
					bank.put("FcstTotalCharge", ToolUtil.getBigDecimal(beBean.get("fcsttotalcharge")));//預估電費
					bank.put("FcstEUI", ToolUtil.divide(beBean.get("fcstmcec"), bean.get("area"), 2));//預估EUI
					bank.put("FcstEPUI", ToolUtil.divide(beBean.get("fcstmcec"), bean.get("people"), 2));//預估EPUI
				}
				bank.put("CC", cc);//契約容量
				bank.put("Demand", demandNow);//目前需量
			
				bank.put("MaxDemand", demandMax);//最高需量
				bank.put("DemandP", ToolUtil.divide(demandNow, cc, 2).multiply(new BigDecimal(100)));//及時需量百分比
				bank.put("MaxDemandP", ToolUtil.divide(demandMax, cc, 2).multiply(new BigDecimal(100)));//最高需量百分比
			}			
			data.put("Bank", bank);		
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
