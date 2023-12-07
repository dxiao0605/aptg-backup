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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class getBank 分行資訊
 */
@WebServlet("/getBank")
public class getBank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBank.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBank() {
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
		logger.debug("getBank start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			
			logger.debug("token: " + token);			
			logger.debug("BankCode: " + bankCode);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(bankCode)) {
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
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
						List<DynaBean> poweraccountList = powerAccountDAO.getEffectivePowerAccount(powerAccountVO);//有效電號列表
						List<DynaBean> bankElectricityList = powerAccountDAO.getBankElectricity(powerAccountVO);
						List<DynaBean> maxDemandList = powerAccountDAO.getDemandNow(powerAccountVO);
						
						rspJson.put("msg", convertToJson(bankInfoList, poweraccountList, bankElectricityList, maxDemandList));
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
		logger.debug("getBank end");
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
	private JSONObject convertToJson(List<DynaBean> bankInfoList, List<DynaBean> poweraccountList, List<DynaBean> bankElectricityList, List<DynaBean> maxDemandList) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject bank = new JSONObject();	
			DynaBean bean = bankInfoList.get(0);
			bank.put("City", bean.get("city"));// 縣市
			bank.put("Dist", bean.get("dist"));// 行政區
			bank.put("BankCode", bean.get("bankcode"));// 分行代碼
			bank.put("BankName", bean.get("bankname"));// 分行名稱
			bank.put("Area",  ToolUtil.getBigDecimal(bean.get("area")));// 面積
			bank.put("People", ToolUtil.getBigDecimal(bean.get("people")));// 員工數

			JSONArray powerAccountArr = new JSONArray();
			BigDecimal cc = new BigDecimal(0);
			for(DynaBean paBean : poweraccountList) {		
				JSONObject powerAccount = new JSONObject();
				powerAccount.put("PowerAccount", paBean.get("poweraccount"));
				powerAccount.put("RatePlanDesc", paBean.get("rateplandesc"));
				powerAccount.put("AccountDesc", ObjectUtils.toString(paBean.get("accountdesc")));
				cc = cc.add(ToolUtil.getBigDecimal(paBean.get("cc")));
				powerAccount.put("CC", paBean.get("cc"));
				powerAccountArr.put(powerAccount);
			}
			bank.put("PowerAccount", powerAccountArr);
			
//			if(bankElectricityList!=null) {
//				BigDecimal demand = new BigDecimal(0);
//				for(DynaBean dmBean : maxDemandList) {		
//					demand = demand.add(ToolUtil.getBigDecimal(dmBean.get("demandnow")));
//				}
//
//				DynaBean beBean = bankElectricityList.get(0);
//				JSONObject now = new JSONObject();
//				now.put("MCEC", ToolUtil.getBigDecimal(beBean.get("mcec")));//目前總用電量
//				now.put("FcstMCEC", ToolUtil.getBigDecimal(beBean.get("fcstmcec")));//預估總用電量
//				now.put("TotalCharge", ToolUtil.getBigDecimal(beBean.get("totalcharge")));//目前電費
//				now.put("FcstTotalCharge", ToolUtil.getBigDecimal(beBean.get("fcsttotalcharge")));//預估電費
//				now.put("CC", cc);//契約容量
//				now.put("Demand", ToolUtil.getBigDecimal(demand, 0, BigDecimal.ROUND_DOWN));//目前需量
//				now.put("MaxDemand", ToolUtil.getBigDecimal(beBean.get("maxdemand"), 0, BigDecimal.ROUND_DOWN));//最高需量
//				now.put("DemandP", ToolUtil.divide(demand, cc, 2).multiply(new BigDecimal(100)));//及時需量百分比
//				now.put("MaxDemandP", ToolUtil.divide(beBean.get("maxdemand"), cc, 2).multiply(new BigDecimal(100)));//最高需量百分比
//				now.put("EUI", ToolUtil.divide(beBean.get("mcec"), bean.get("area"), 2));//目前EUI
//				now.put("EPUI", ToolUtil.divide(beBean.get("mcec"), bean.get("people"), 2));//目前EPUI
//				now.put("FcstEUI", ToolUtil.divide(beBean.get("fcstmcec"), bean.get("area"), 2));//預估EUI
//				now.put("FcstEPUI", ToolUtil.divide(beBean.get("fcstmcec"), bean.get("people"), 2));//預估EPUI	
//				bank.put("Now", now);
//				
//				JSONObject lastMonth = new JSONObject();
//				lastMonth.put("MCEC", ToolUtil.getBigDecimal(beBean.get("mceclm")));//總用電量
//				lastMonth.put("TotalCharge", ToolUtil.getBigDecimal(beBean.get("totalchargelm")));//電費
//				lastMonth.put("CC", ToolUtil.getBigDecimal(beBean.get("cclm")));//契約容量
//				lastMonth.put("MaxDemand", ToolUtil.getBigDecimal(beBean.get("demandlm"), 0, BigDecimal.ROUND_DOWN));//最高需量
//				lastMonth.put("MaxDemandP", ToolUtil.divide(beBean.get("demandlm"), beBean.get("cclm"), 2).multiply(new BigDecimal(100)));//最高需量百分比
//				lastMonth.put("EUI", ToolUtil.divide(beBean.get("mceclm"), bean.get("area"), 2));//EUI
//				lastMonth.put("EPUI", ToolUtil.divide(beBean.get("mceclm"), bean.get("people"), 2));//EPUI
//				bank.put("LastMonth", lastMonth);
//				
//				JSONObject lastYear = new JSONObject();
//				lastYear.put("MCEC", ToolUtil.getBigDecimal(beBean.get("mcecly")));//總用電量
//				lastYear.put("TotalCharge", ToolUtil.getBigDecimal(beBean.get("totalchargely")));//電費
//				lastYear.put("CC", ToolUtil.getBigDecimal(beBean.get("ccly")));//契約容量
//				lastYear.put("MaxDemand", ToolUtil.getBigDecimal(beBean.get("demandly"), 0, BigDecimal.ROUND_DOWN));//最高需量
//				lastYear.put("MaxDemandP", ToolUtil.divide(beBean.get("demandly"), beBean.get("ccly"), 2).multiply(new BigDecimal(100)));//最高需量百分比
//				lastYear.put("EUI", ToolUtil.divide(beBean.get("mcecly"), bean.get("area"), 2));//EUI
//				lastYear.put("EPUI", ToolUtil.divide(beBean.get("mcecly"), bean.get("people"), 2));//EPUI
//				bank.put("LastYear", lastYear);	
//			}
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
