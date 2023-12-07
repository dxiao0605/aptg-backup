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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class addPowerAccount 新增電號資訊
 */
@WebServlet("/addPowerAccount")
public class addPowerAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addPowerAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addPowerAccount() {
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
		logger.debug("addPowerAccount start");
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
					PowerAccountVO powerAccountVO = this.parseJson(req);
					if(powerAccountVO.isError()) {
						rspJson.put("code", powerAccountVO.getCode());
						rspJson.put("msg", powerAccountVO.getDescription());
					}else {			
						if("21".equals(powerAccountVO.getRatePlanCode())) {
							powerAccountVO.setPowerAccount(ToolUtil.getTpno("A"));
						}else if("22".equals(powerAccountVO.getRatePlanCode())) {
							powerAccountVO.setPowerAccount(ToolUtil.getTpno("B"));
						}else if("23".equals(powerAccountVO.getRatePlanCode())) {
							powerAccountVO.setPowerAccount(ToolUtil.getTpno("C"));
						}
						
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
						List<DynaBean> list = powerAccountDAO.checkPowerAccount(powerAccountVO.getPowerAccount());
						if(list!=null && list.size()>0) {
							rspJson.put("code", "11");
							rspJson.put("msg", "電號已存在");
						}else {
							powerAccountDAO.addPowerAccount(powerAccountVO);
							ToolUtil.addLogRecord(powerAccountVO.getUserName(), "5", "新增電號:"+powerAccountVO.getPowerAccount());
							rspJson.put("code", "00");
							rspJson.put("msg", "Insert Success");
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
		logger.debug("addPowerAccount end");
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
	private PowerAccountVO parseJson(String json) throws Exception {
		PowerAccountVO powerAccountVO = new PowerAccountVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			if(!ToolUtil.lengthCheck(msg.optString("BankCode"), 3)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("分行代號超過長度限制");
			}else {
				powerAccountVO.setBankCode(msg.optString("BankCode"));
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("PowerAccount"), 11)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("電號超過長度限制");
			}else {
				powerAccountVO.setPowerAccount(msg.optString("PowerAccount"));
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("CustomerName"), 50)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("戶名超過長度限制");
			}else {
				powerAccountVO.setCustomerName(msg.optString("CustomerName"));	
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("AccountDesc"), 50)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("說明超過長度限制");
			}else {
				powerAccountVO.setAccountDesc(msg.optString("AccountDesc"));	
			}
			
			if (!ToolUtil.numberCheck(msg.optString("PATypeCode"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("電號類別數字格式錯誤");
			}else {
				powerAccountVO.setPaTypeCode(msg.optString("PATypeCode"));
			}
			
			if (!ToolUtil.numberCheck(msg.optString("PowerPhase"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("相用電數字格式錯誤");
			}else {
				powerAccountVO.setPowerPhase(msg.optString("PowerPhase"));
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("PAAddress"), 100)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("電號地址超過長度限制");
			}else {
				powerAccountVO.setPaAddress(msg.optString("PAAddress"));	
			}
			
			
			if (!ToolUtil.numberCheck(msg.optString("RatePlanCode"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("用電種類代碼數字格式錯誤");
			}else if(ToolUtil.isNull(msg, "RatePlanCode")) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("15");
				powerAccountVO.setDescription("用電種類代碼不能為空");
			}else {
				powerAccountVO.setRatePlanCode(msg.optString("RatePlanCode"));
			}
			
			if (ToolUtil.isNull(msg, "UsuallyCC")) {
				powerAccountVO.setUsuallyCC(null);
			} else if (!ToolUtil.numberCheck(msg.optString("UsuallyCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("經常契約容量數字格式錯誤");
			}else if (msg.optInt("UsuallyCC")<0 || msg.optInt("UsuallyCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("經常契約容量數字格式錯誤");
			}else {
				powerAccountVO.setUsuallyCC(msg.optString("UsuallyCC"));
			}
			
			
			if(ToolUtil.isNull(msg, "SPCC")) {
				powerAccountVO.setSpCC(null);
			}else if (!ToolUtil.numberCheck(msg.optString("SPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("半尖峰契約容量數字格式錯誤");
			}else if (msg.optInt("SPCC")<0 || msg.optInt("SPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("半尖峰契約容量數字格式錯誤");
			}else {
				powerAccountVO.setSpCC(msg.optString("SPCC"));
			}
			
			
			if(ToolUtil.isNull(msg, "SatSPCC")) {
				powerAccountVO.setSatSPCC(null);
			}else if (!ToolUtil.numberCheck(msg.optString("SatSPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("週六半尖峰契約容量數字格式錯誤");
			}else if (msg.optInt("SatSPCC")<0 || msg.optInt("SatSPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("週六半尖峰契約容量數字格式錯誤");
			}else {
				powerAccountVO.setSatSPCC(msg.optString("SatSPCC"));
			}
			
			
			if(ToolUtil.isNull(msg, "OPCC")) {
				powerAccountVO.setOpCC(null);
			}else if (!ToolUtil.numberCheck(msg.optString("OPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("離峰契約容量數字格式錯誤");
			}else if (msg.optInt("OPCC")<0 || msg.optInt("OPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("離峰契約容量數字格式錯誤");
			}else {
				powerAccountVO.setOpCC(msg.optString("OPCC"));
			}
			String applyDate = msg.optString("ApplyDate");
			powerAccountVO.setApplyDate(applyDate);
	
			powerAccountVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return powerAccountVO;
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
