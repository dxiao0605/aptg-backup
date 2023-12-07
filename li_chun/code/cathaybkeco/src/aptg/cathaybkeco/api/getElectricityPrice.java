package aptg.cathaybkeco.api;

import java.io.IOException;
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

import aptg.cathaybkeco.dao.ElectricityPriceDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.ElectricityPriceVO;

/**
 * Servlet implementation class getElectricityPrice 歷年電價表
 */
@WebServlet("/getElectricityPrice")
public class getElectricityPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getElectricityPrice.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getElectricityPrice() {
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
		logger.debug("getElectricityPrice start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String ratePlanCode = ObjectUtils.toString(request.getParameter("ratePlanCode"));
			String year = ObjectUtils.toString(request.getParameter("year"));
			String month = ObjectUtils.toString(request.getParameter("month"));
			logger.debug("token: " + token);
			logger.debug("RatePlanCode: " + ratePlanCode);
			logger.debug("Year: " + year);
			logger.debug("Month: " + month);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ElectricityPriceVO electricityPriceVO = new ElectricityPriceVO();
					electricityPriceVO.setRatePlanCode(ratePlanCode);
					if(StringUtils.isNotBlank(year))
						electricityPriceVO.setYear(String.valueOf(Integer.parseInt(year)+1911));
					electricityPriceVO.setMonth(month);
					ElectricityPriceDAO electricityPriceDAO = new ElectricityPriceDAO();
					List<DynaBean> list = electricityPriceDAO.getElectricityPrice(electricityPriceVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
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
		logger.debug("getElectricityPrice end");
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
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for(DynaBean bean : rows) {
				JSONObject electricityPrice = new JSONObject();
				
				electricityPrice.put("Year", String.valueOf(ToolUtil.parseInt(bean.get("year"))-1911));
				electricityPrice.put("Month", bean.get("month"));
				electricityPrice.put("RatePlanCode", bean.get("rateplancode"));
				electricityPrice.put("RatePlanDesc", bean.get("rateplandesc"));
				
				if("1".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					//表燈營業用
					electricityPrice.put("LampBPrice1", bean.get("lampbprice1") != null ? bean.get("lampbprice1") : "");
					electricityPrice.put("LampBPrice2", bean.get("lampbprice2") != null ? bean.get("lampbprice2") : "");
					electricityPrice.put("LampBPrice3", bean.get("lampbprice3") != null ? bean.get("lampbprice3") : "");
					electricityPrice.put("LampBPrice4", bean.get("lampbprice4") != null ? bean.get("lampbprice4") : "");
					electricityPrice.put("LampBPrice1S", bean.get("lampbprice1s") != null ? bean.get("lampbprice1s") : "");
					electricityPrice.put("LampBPrice2S", bean.get("lampbprice2s") != null ? bean.get("lampbprice2s") : "");
					electricityPrice.put("LampBPrice3S", bean.get("lampbprice3s") != null ? bean.get("lampbprice3s") : "");
					electricityPrice.put("LampBPrice4S", bean.get("lampbprice4s") != null ? bean.get("lampbprice4s") : "");
					electricityPrice.put("LampBStep1", bean.get("lampbstep1") != null ? bean.get("lampbstep1") : "");
					electricityPrice.put("LampBStep2", bean.get("lampbstep2") != null ? bean.get("lampbstep2") : "");
					electricityPrice.put("LampBStep3", bean.get("lampbstep3") != null ? bean.get("lampbstep3") : "");					
				} else if("2".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//表燈非營業用
					electricityPrice.put("LampPrice1", bean.get("lampprice1") != null ? bean.get("lampprice1") : "");
					electricityPrice.put("LampPrice2", bean.get("lampprice2") != null ? bean.get("lampprice2") : "");
					electricityPrice.put("LampPrice3", bean.get("lampprice3") != null ? bean.get("lampprice3") : "");
					electricityPrice.put("LampPrice4", bean.get("lampprice4") != null ? bean.get("lampprice4") : "");
					electricityPrice.put("LampPrice5", bean.get("lampprice5") != null ? bean.get("lampprice5") : "");
					electricityPrice.put("LampPrice6", bean.get("lampprice6") != null ? bean.get("lampprice6") : "");
					electricityPrice.put("LampPrice1S", bean.get("lampprice1s") != null ? bean.get("lampprice1s") : "");
					electricityPrice.put("LampPrice2S", bean.get("lampprice2s") != null ? bean.get("lampprice2s") : "");
					electricityPrice.put("LampPrice3S", bean.get("lampprice3s") != null ? bean.get("lampprice3s") : "");
					electricityPrice.put("LampPrice4S", bean.get("lampprice4s") != null ? bean.get("lampprice4s") : "");
					electricityPrice.put("LampPrice5S", bean.get("lampprice5s") != null ? bean.get("lampprice5s") : "");
					electricityPrice.put("LampPrice6S", bean.get("lampprice6s") != null ? bean.get("lampprice6s") : "");
					electricityPrice.put("LampStep1", bean.get("lampstep1") != null ? bean.get("lampstep1") : "");
					electricityPrice.put("LampStep2", bean.get("lampstep2") != null ? bean.get("lampstep2") : "");
					electricityPrice.put("LampStep3", bean.get("lampstep3") != null ? bean.get("lampstep3") : "");
					electricityPrice.put("LampStep4", bean.get("lampstep4") != null ? bean.get("lampstep4") : "");
					electricityPrice.put("LampStep5", bean.get("lampstep5") != null ? bean.get("lampstep5") : "");
				} else if("3".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//表燈簡易二段式
					electricityPrice.put("BaseCharge3phase", bean.get("basecharge3phase") != null ? bean.get("basecharge3phase") : "");
					electricityPrice.put("TimeCharge", bean.get("timecharge") != null ? bean.get("timecharge") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
					electricityPrice.put("Over2KPrice", bean.get("over2kprice") != null ? bean.get("over2kprice") : "");
				} else if("4".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//表燈簡易三段式 
					electricityPrice.put("BaseCharge3phase", bean.get("basecharge3phase") != null ? bean.get("basecharge3phase") : "");
					electricityPrice.put("TimeChargeSP", bean.get("timechargesp") != null ? bean.get("timechargesp") : "");
					electricityPrice.put("TimeChargeSatSP", bean.get("timechargesatsp") != null ? bean.get("timechargesatsp") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeSPS", bean.get("timechargesps") != null ? bean.get("timechargesps") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
					electricityPrice.put("Over2KPrice", bean.get("over2kprice") != null ? bean.get("over2kprice") : "");
				} else if("5".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//表燈標準二段式 
					electricityPrice.put("BaseCharge1phase", bean.get("basecharge1phase") != null ? bean.get("basecharge1phase") : "");
					electricityPrice.put("BaseCharge3phase", bean.get("basecharge3phase") != null ? bean.get("basecharge3phase") : "");
					electricityPrice.put("BaseChargeUsually", bean.get("basechargeusually") != null ? bean.get("basechargeusually") : "");
					electricityPrice.put("BaseChargeSP", bean.get("basechargesp") != null ? bean.get("basechargesp") : "");
					electricityPrice.put("BaseChargeSatSP", bean.get("basechargesatsp") != null ? bean.get("basechargesatsp") : "");
					electricityPrice.put("BaseChargeOP", bean.get("basechargeop") != null ? bean.get("basechargeop") : "");
					electricityPrice.put("BaseChargeUsuallyS", bean.get("basechargeusuallys") != null ? bean.get("basechargeusuallys") : "");
					electricityPrice.put("BaseChargeSatSPS", bean.get("basechargesatsps") != null ? bean.get("basechargesatsps") : "");
					electricityPrice.put("BaseChargeOPS", bean.get("basechargeops") != null ? bean.get("basechargeops") : "");
					electricityPrice.put("TimeCharge", bean.get("timecharge") != null ? bean.get("timecharge") : "");
					electricityPrice.put("TimeChargeSatSP", bean.get("timechargesatsp") != null ? bean.get("timechargesatsp") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeSatSPS", bean.get("timechargesatsps") != null ? bean.get("timechargesatsps") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
				} else if("6".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//低壓非時間
					electricityPrice.put("BaseChargeUsually", bean.get("basechargeusually") != null ? bean.get("basechargeusually") : "");
					electricityPrice.put("BaseChargeSP", bean.get("basechargesp") != null ? bean.get("basechargesp") : "");
					electricityPrice.put("BaseChargeUsuallyS", bean.get("basechargeusuallys") != null ? bean.get("basechargeusuallys") : "");					
					electricityPrice.put("TimeCharge", bean.get("timecharge") != null ? bean.get("timecharge") : "");					
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");				
				} else if("7".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//低壓二段式
					electricityPrice.put("BaseCharge3phase", bean.get("basecharge3phase") != null ? bean.get("basecharge3phase") : "");
					electricityPrice.put("BaseChargeUsually", bean.get("basechargeusually") != null ? bean.get("basechargeusually") : "");
					electricityPrice.put("BaseChargeSP", bean.get("basechargesp") != null ? bean.get("basechargesp") : "");
					electricityPrice.put("BaseChargeSatSP", bean.get("basechargesatsp") != null ? bean.get("basechargesatsp") : "");
					electricityPrice.put("BaseChargeOP", bean.get("basechargeop") != null ? bean.get("basechargeop") : "");
					electricityPrice.put("BaseChargeUsuallyS", bean.get("basechargeusuallys") != null ? bean.get("basechargeusuallys") : "");
					electricityPrice.put("BaseChargeSatSPS", bean.get("basechargesatsps") != null ? bean.get("basechargesatsps") : "");
					electricityPrice.put("BaseChargeOPS", bean.get("basechargeops") != null ? bean.get("basechargeops") : "");
					electricityPrice.put("TimeCharge", bean.get("timecharge") != null ? bean.get("timecharge") : "");
					electricityPrice.put("TimeChargeSatSP", bean.get("timechargesatsp") != null ? bean.get("timechargesatsp") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeSatSPS", bean.get("timechargesatsps") != null ? bean.get("timechargesatsps") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
				} else if("8".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//高壓二段式
					electricityPrice.put("BaseChargeUsually", bean.get("basechargeusually") != null ? bean.get("basechargeusually") : "");
					electricityPrice.put("BaseChargeSP", bean.get("basechargesp") != null ? bean.get("basechargesp") : "");
					electricityPrice.put("BaseChargeSatSP", bean.get("basechargesatsp") != null ? bean.get("basechargesatsp") : "");
					electricityPrice.put("BaseChargeOP", bean.get("basechargeop") != null ? bean.get("basechargeop") : "");
					electricityPrice.put("BaseChargeUsuallyS", bean.get("basechargeusuallys") != null ? bean.get("basechargeusuallys") : "");
					electricityPrice.put("BaseChargeSatSPS", bean.get("basechargesatsps") != null ? bean.get("basechargesatsps") : "");
					electricityPrice.put("BaseChargeOPS", bean.get("basechargeops") != null ? bean.get("basechargeops") : "");
					electricityPrice.put("TimeCharge", bean.get("timecharge") != null ? bean.get("timecharge") : "");
					electricityPrice.put("TimeChargeSatSP", bean.get("timechargesatsp") != null ? bean.get("timechargesatsp") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeSatSPS", bean.get("timechargesatsps") != null ? bean.get("timechargesatsps") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
				} else if("9".equals(ObjectUtils.toString(bean.get("rateplancode")))){
					//高壓三段式
					electricityPrice.put("BaseChargeUsually", bean.get("basechargeusually") != null ? bean.get("basechargeusually") : "");
					electricityPrice.put("BaseChargeSP", bean.get("basechargesp") != null ? bean.get("basechargesp") : "");
					electricityPrice.put("BaseChargeSatSP", bean.get("basechargesatsp") != null ? bean.get("basechargesatsp") : "");
					electricityPrice.put("BaseChargeOP", bean.get("basechargeop") != null ? bean.get("basechargeop") : "");
					electricityPrice.put("BaseChargeUsuallyS", bean.get("basechargeusuallys") != null ? bean.get("basechargeusuallys") : "");
					electricityPrice.put("BaseChargeSPS", bean.get("basechargesps") != null ? bean.get("basechargesps") : "");
					electricityPrice.put("BaseChargeSatSPS", bean.get("basechargesatsps") != null ? bean.get("basechargesatsps") : "");
					electricityPrice.put("BaseChargeOPS", bean.get("basechargeops") != null ? bean.get("basechargeops") : "");
					electricityPrice.put("TimeChargeSP", bean.get("timechargesp") != null ? bean.get("timechargesp") : "");
					electricityPrice.put("TimeChargeSatSP", bean.get("timechargesatsp") != null ? bean.get("timechargesatsp") : "");
					electricityPrice.put("TimeChargeOP", bean.get("timechargeop") != null ? bean.get("timechargeop") : "");
					electricityPrice.put("TimeChargeS", bean.get("timecharges") != null ? bean.get("timecharges") : "");
					electricityPrice.put("TimeChargeSPS", bean.get("timechargesps") != null ? bean.get("timechargesps") : "");
					electricityPrice.put("TimeChargeSatSPS", bean.get("timechargesatsps") != null ? bean.get("timechargesatsps") : "");
					electricityPrice.put("TimeChargeOPS", bean.get("timechargeops") != null ? bean.get("timechargeops") : "");
				}
				list.put(electricityPrice);
			}
			data.put("ElectricityPrice", list);
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
