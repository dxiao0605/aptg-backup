package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.Iterator;
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

import aptg.cathaybkeco.dao.ElectricityPriceDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.ElectricityPriceVO;

/**
 * Servlet implementation class addElectricityPrice 新增歷年電價表
 */
@WebServlet("/addElectricityPrice")
public class addElectricityPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addElectricityPrice.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addElectricityPrice() {
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
		logger.debug("addElectricityPrice start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ElectricityPriceVO electricityPriceVO = this.parseJson(req);
					if(electricityPriceVO.isError()) {
						rspJson.put("code", electricityPriceVO.getCode());
						rspJson.put("msg", electricityPriceVO.getDescription());
					}else {		
						ElectricityPriceDAO electricityPriceDAO = new ElectricityPriceDAO();
						List<DynaBean> list = electricityPriceDAO.getElectricityPrice(electricityPriceVO);
						if(list!=null && list.size()>0) {
							rspJson.put("code", "17");
							rspJson.put("msg", "電價表已存在");
						}else {
							electricityPriceDAO.addElectricityPrice(electricityPriceVO);
							ToolUtil.addLogRecord(electricityPriceVO.getUserName(), "11", "新增電費單價設定，Date:"+electricityPriceVO.getYear()+electricityPriceVO.getMonth()+",RatePlanCode:"+electricityPriceVO.getRatePlanCode());
							rspJson.put("code", "00");
							
							JSONObject data = new JSONObject();
							data.put("Year", String.valueOf(ToolUtil.parseInt(electricityPriceVO.getYear())-1911));
							data.put("Month", electricityPriceVO.getMonth());										
							data.put("Message", "Insert Success");
							rspJson.put("msg", data);
						}
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
		logger.debug("addElectricityPrice end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return powerAccountVO
	 * @throws Exception
	 */
	private ElectricityPriceVO parseJson(String json) throws Exception {
		ElectricityPriceVO electricityPriceVO = new ElectricityPriceVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");	
			Iterator<String> iterator = msg.keys();
			while(iterator.hasNext()){
				String key = iterator.next();
				if (ToolUtil.isNull(msg, key) && !"UserName".equals(key)) {
					electricityPriceVO.setError(true);
					electricityPriceVO.setCode("15");
					electricityPriceVO.setDescription("數字不能為空");
					break;
				}else if (!ToolUtil.numberCheck(msg.optString(key)) && !"UserName".equals(key)) {
					electricityPriceVO.setError(true);
					electricityPriceVO.setCode("13");
					electricityPriceVO.setDescription("數字格式錯誤");
					break;
				}else if (!ToolUtil.numberLengthCheck(msg.optString(key), 5, 3) && !"UserName".equals(key)) {
					electricityPriceVO.setError(true);
					electricityPriceVO.setCode("12");
					electricityPriceVO.setDescription("超過長度限制");
					break;
				}
			}
			electricityPriceVO.setYear(String.valueOf(msg.optInt("Year")+1911));
			electricityPriceVO.setMonth(msg.optString("Month"));
			String ratePlanCode = msg.optString("RatePlanCode");
			electricityPriceVO.setRatePlanCode(ratePlanCode);		
			if("1".equals(ratePlanCode)) {
				//表燈營業用				
				electricityPriceVO.setLampBPrice2(msg.optString("LampBPrice2"));
				electricityPriceVO.setLampBPrice3(msg.optString("LampBPrice3"));
				electricityPriceVO.setLampBPrice4(msg.optString("LampBPrice4"));
				electricityPriceVO.setLampBPrice1S(msg.optString("LampBPrice1S"));
				electricityPriceVO.setLampBPrice2S(msg.optString("LampBPrice2S"));
				electricityPriceVO.setLampBPrice3S(msg.optString("LampBPrice3S"));
				electricityPriceVO.setLampBPrice4S(msg.optString("LampBPrice4S"));
				if((msg.optDouble("LampBStep2")-msg.optDouble("LampBStep1")<1)||
						(msg.optDouble("LampBStep3")-msg.optDouble("LampBStep2")<1)) {
					electricityPriceVO.setError(true);
					electricityPriceVO.setCode("13");
					electricityPriceVO.setDescription("數字格式錯誤");
				}	
				electricityPriceVO.setLampBStep1(msg.optString("LampBStep1"));
				electricityPriceVO.setLampBStep2(msg.optString("LampBStep2"));
				electricityPriceVO.setLampBStep3(msg.optString("LampBStep3"));					
			} else if("2".equals(ratePlanCode)){
				//表燈非營業用
				electricityPriceVO.setLampPrice1(msg.optString("LampPrice1"));
				electricityPriceVO.setLampPrice2(msg.optString("LampPrice2"));
				electricityPriceVO.setLampPrice3(msg.optString("LampPrice3"));
				electricityPriceVO.setLampPrice4(msg.optString("LampPrice4"));
				electricityPriceVO.setLampPrice5(msg.optString("LampPrice5"));
				electricityPriceVO.setLampPrice6(msg.optString("LampPrice6"));
				electricityPriceVO.setLampPrice1S(msg.optString("LampPrice1S"));
				electricityPriceVO.setLampPrice2S(msg.optString("LampPrice2S"));
				electricityPriceVO.setLampPrice3S(msg.optString("LampPrice3S"));
				electricityPriceVO.setLampPrice4S(msg.optString("LampPrice4S"));
				electricityPriceVO.setLampPrice5S(msg.optString("LampPrice5S"));
				electricityPriceVO.setLampPrice6S(msg.optString("LampPrice6S"));
				if((msg.optDouble("LampStep2")-msg.optDouble("LampStep1")<1)||
						(msg.optDouble("LampStep3")-msg.optDouble("LampStep2")<1) ||
						(msg.optDouble("LampStep4")-msg.optDouble("LampStep3")<1) ||
						(msg.optDouble("LampStep5")-msg.optDouble("LampStep4")<1)) {
					electricityPriceVO.setError(true);
					electricityPriceVO.setCode("13");
					electricityPriceVO.setDescription("數字格式錯誤");
				}	
				electricityPriceVO.setLampStep1(msg.optString("LampStep1"));
				electricityPriceVO.setLampStep2(msg.optString("LampStep2"));
				electricityPriceVO.setLampStep3(msg.optString("LampStep3"));
				electricityPriceVO.setLampStep4(msg.optString("LampStep4"));
				electricityPriceVO.setLampStep5(msg.optString("LampStep5"));
			} else if("3".equals(ratePlanCode)){
				//表燈簡易二段式
				electricityPriceVO.setBaseCharge3phase(msg.optString("BaseCharge3phase"));
				electricityPriceVO.setTimeCharge(msg.optString("TimeCharge"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
				electricityPriceVO.setOver2KPrice(msg.optString("Over2KPrice"));
			} else if("4".equals(ratePlanCode)){
				//表燈簡易三段式 
				electricityPriceVO.setBaseCharge3phase(msg.optString("BaseCharge3phase"));
				electricityPriceVO.setTimeChargeSP(msg.optString("TimeChargeSP"));
				electricityPriceVO.setTimeChargeSatSP(msg.optString("TimeChargeSatSP"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeSPS(msg.optString("TimeChargeSPS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
				electricityPriceVO.setOver2KPrice(msg.optString("Over2KPrice"));
			} else if("5".equals(ratePlanCode)){
				//表燈標準二段式 
				electricityPriceVO.setBaseCharge1phase(msg.optString("BaseCharge1phase"));
				electricityPriceVO.setBaseCharge3phase(msg.optString("BaseCharge3phase"));
				electricityPriceVO.setBaseChargeUsually(msg.optString("BaseChargeUsually"));
				electricityPriceVO.setBaseChargeSP(msg.optString("BaseChargeSP"));
				electricityPriceVO.setBaseChargeSatSP(msg.optString("BaseChargeSatSP"));
				electricityPriceVO.setBaseChargeOP(msg.optString("BaseChargeOP"));
				electricityPriceVO.setBaseChargeUsuallyS(msg.optString("BaseChargeUsuallyS"));
				electricityPriceVO.setBaseChargeSatSPS(msg.optString("BaseChargeSatSPS"));
				electricityPriceVO.setBaseChargeOPS(msg.optString("BaseChargeOPS"));
				electricityPriceVO.setTimeCharge(msg.optString("TimeCharge"));
				electricityPriceVO.setTimeChargeSatSP(msg.optString("TimeChargeSatSP"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeSatSPS(msg.optString("TimeChargeSatSPS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
			} else if("6".equals(ratePlanCode)){
				//低壓非時間
				electricityPriceVO.setBaseChargeUsually(msg.optString("BaseChargeUsually"));
				electricityPriceVO.setBaseChargeSP(msg.optString("BaseChargeSP"));
				electricityPriceVO.setBaseChargeUsuallyS(msg.optString("BaseChargeUsuallyS"));					
				electricityPriceVO.setTimeCharge(msg.optString("TimeCharge"));					
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));				
			} else if("7".equals(ratePlanCode)){
				//低壓二段式
				electricityPriceVO.setBaseCharge3phase(msg.optString("BaseCharge3phase"));
				electricityPriceVO.setBaseChargeUsually(msg.optString("BaseChargeUsually"));
				electricityPriceVO.setBaseChargeSP(msg.optString("BaseChargeSP"));
				electricityPriceVO.setBaseChargeSatSP(msg.optString("BaseChargeSatSP"));
				electricityPriceVO.setBaseChargeOP(msg.optString("BaseChargeOP"));
				electricityPriceVO.setBaseChargeUsuallyS(msg.optString("BaseChargeUsuallyS"));
				electricityPriceVO.setBaseChargeSatSPS(msg.optString("BaseChargeSatSPS"));
				electricityPriceVO.setBaseChargeOPS(msg.optString("BaseChargeOPS"));
				electricityPriceVO.setTimeCharge(msg.optString("TimeCharge"));
				electricityPriceVO.setTimeChargeSatSP(msg.optString("TimeChargeSatSP"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeSatSPS(msg.optString("TimeChargeSatSPS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
			} else if("8".equals(ratePlanCode)){
				//高壓二段式
				electricityPriceVO.setBaseChargeUsually(msg.optString("BaseChargeUsually"));
				electricityPriceVO.setBaseChargeSP(msg.optString("BaseChargeSP"));
				electricityPriceVO.setBaseChargeSatSP(msg.optString("BaseChargeSatSP"));
				electricityPriceVO.setBaseChargeOP(msg.optString("BaseChargeOP"));
				electricityPriceVO.setBaseChargeUsuallyS(msg.optString("BaseChargeUsuallyS"));
				electricityPriceVO.setBaseChargeSatSPS(msg.optString("BaseChargeSatSPS"));
				electricityPriceVO.setBaseChargeOPS(msg.optString("BaseChargeOPS"));
				electricityPriceVO.setTimeCharge(msg.optString("TimeCharge"));
				electricityPriceVO.setTimeChargeSatSP(msg.optString("TimeChargeSatSP"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeSatSPS(msg.optString("TimeChargeSatSPS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
			} else if("9".equals(ratePlanCode)){
				//高壓三段式			
				electricityPriceVO.setBaseChargeOP(msg.optString("BaseChargeOP"));
				electricityPriceVO.setBaseChargeUsuallyS(msg.optString("BaseChargeUsuallyS"));
				electricityPriceVO.setBaseChargeSPS(msg.optString("BaseChargeSPS"));
				electricityPriceVO.setBaseChargeSatSPS(msg.optString("BaseChargeSatSPS"));
				electricityPriceVO.setBaseChargeOPS(msg.optString("BaseChargeOPS"));
				electricityPriceVO.setTimeChargeSP(msg.optString("TimeChargeSP"));
				electricityPriceVO.setTimeChargeSatSP(msg.optString("TimeChargeSatSP"));
				electricityPriceVO.setTimeChargeOP(msg.optString("TimeChargeOP"));
				electricityPriceVO.setTimeChargeS(msg.optString("TimeChargeS"));
				electricityPriceVO.setTimeChargeSPS(msg.optString("TimeChargeSPS"));
				electricityPriceVO.setTimeChargeSatSPS(msg.optString("TimeChargeSatSPS"));
				electricityPriceVO.setTimeChargeOPS(msg.optString("TimeChargeOPS"));
				electricityPriceVO.setBaseChargeUsually(msg.optString("BaseChargeUsually"));
				electricityPriceVO.setBaseChargeSP(msg.optString("BaseChargeSP"));
				electricityPriceVO.setBaseChargeSatSP(msg.optString("BaseChargeSatSP"));
			}
			
			electricityPriceVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return electricityPriceVO;
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
