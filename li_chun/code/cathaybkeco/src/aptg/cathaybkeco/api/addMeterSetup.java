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

import aptg.cathaybkeco.dao.ControllerSetupDAO;
import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.ControllerSetupVO;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class addMeterSetup 新增電表資訊
 */
@WebServlet("/addMeterSetup")
public class addMeterSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addMeterSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addMeterSetup() {
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
		logger.debug("addMeterSetup start");
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
					MeterSetupVO meterSetupVO = this.parseJson(req);
					if (meterSetupVO.isError()) {
						rspJson.put("code", meterSetupVO.getCode());
						rspJson.put("msg", meterSetupVO.getDescription());
					} else {
						ControllerSetupVO controllerSetupVO = new ControllerSetupVO();
						controllerSetupVO.setEco5Account(meterSetupVO.getEco5Account());
						ControllerSetupDAO controllerSetupDAO = new ControllerSetupDAO();						
						List<DynaBean> list = controllerSetupDAO.getControllerSetup(controllerSetupVO);
						if(list==null || list.size()==0 ) {
							rspJson.put("code", "16");
							rspJson.put("msg", "ECO5帳號不存在");
						}else {
							MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
							meterSetupDAO.addMeterSetup(meterSetupVO);
							ToolUtil.addLogRecord(meterSetupVO.getUserName(), "9", "新增電表，DeviceID:"+meterSetupVO.getDeviceId());
														
							rspJson.put("code", "00");
							rspJson.put("msg", convertToJson(meterSetupVO.getDeviceId()));
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
		logger.debug("addMeterSetup end");
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
	private MeterSetupVO parseJson(String json) throws Exception {
		MeterSetupVO meterSetupVO = new MeterSetupVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");	
			
			String eco5Account = "";				
			if (ToolUtil.isNull(msg, "ECO5Account")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("ECO5帳號不能為空");
			}else {
				eco5Account = msg.optString("ECO5Account");				
				meterSetupVO.setEco5Account(eco5Account);
			}
			
			if (!ToolUtil.numberCheck(msg.optString("MeterID"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電表站號數字格式錯誤");
			}else if (msg.optInt("MeterID")<1 || msg.optInt("MeterID")>99) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電表站號數字格式錯誤");
			}else {
				String meterId = msg.optString("MeterID");
				if(this.checkMeterId(eco5Account, meterId)) {
					meterSetupVO.setError(true);
					meterSetupVO.setCode("22");
					meterSetupVO.setDescription("站號重複，請修改站號後再新增電表，或請洽系統維護商");
				}else {
					meterSetupVO.setMeterID(meterId);				
					meterSetupVO.setDeviceId("IN11"+eco5Account.substring(4, 18)+StringUtils.leftPad(meterId, 2, "0"));
				}
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("MeterName"), 20)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("電表名稱超過長度限制");
			}else if (ToolUtil.isNull(msg, "MeterName")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("電表名稱不能為空");
			}else{
				meterSetupVO.setMeterName(msg.optString("MeterName"));	
			}
			
			
			if (ToolUtil.isNull(msg, "MeterTypeCode")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("電表型號不能為空");
			}else {
				meterSetupVO.setMeterTypeCode(msg.optString("MeterTypeCode"));
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("InstallPosition"), 100)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("安裝位置超過長度限制");
			}else {
				meterSetupVO.setInstallPosition(msg.optString("InstallPosition"));	
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("TreeChartID"), 11)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("樹狀圖編號超過長度限制");
			}else if (ToolUtil.isNull(msg, "TreeChartID")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("樹狀圖編號不能為空");
			}else{
				meterSetupVO.setTreeChartID(msg.optString("TreeChartID"));	
			}
			
			meterSetupVO.setEnabled(msg.optString("Enabled"));
			if (ToolUtil.isNull(msg, "TreeChartID")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("接線方式不能為空");
			}else{
				meterSetupVO.setWiringCode(msg.optString("WiringCode"));
			}
			if (ToolUtil.isNull(msg, "TreeChartID")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("耗能分類不能為空");
			}else{
				meterSetupVO.setUsageCode(msg.optString("UsageCode"));
			}
					
			if(!ToolUtil.lengthCheck(msg.optString("PowerAccount"), 11)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("電號超過長度限制");
			}else if (ToolUtil.isNull(msg, "PowerAccount")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("電號不能為空");
			}else{
				meterSetupVO.setPowerAccount(msg.optString("PowerAccount"));	
			}
			
			meterSetupVO.setPowerFactorEnabled(msg.optString("PowerFactorEnabled"));
			
			if(!ToolUtil.lengthCheck(msg.optString("AreaName"), 100)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("用電區域超過長度限制");
			}else {
				meterSetupVO.setAreaName(msg.optString("AreaName"));	
			}
			
			if(ToolUtil.isNull(msg, "Area")) {
				meterSetupVO.setArea(null);
			}else if (!ToolUtil.numberCheck(msg.optString("Area"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("用電區域面積數字格式錯誤");
			}else {
				meterSetupVO.setArea(msg.optString("Area"));	
			}
			
			if(ToolUtil.isNull(msg, "People")) {
				meterSetupVO.setPeople(null);
			}else if (!ToolUtil.numberCheck(msg.optString("People"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("用電區域人數數字格式錯誤");
			}else {
				meterSetupVO.setPeople(msg.optString("People"));
			}
			
			
			if(!ToolUtil.isNull(msg, "DFEnabled")) {
				meterSetupVO.setDfEnabled(msg.optString("DFEnabled"));
			}
			if(!ToolUtil.isNull(msg, "DFCode")) {
				meterSetupVO.setDfCode(msg.optString("DFCode"));
			}
			
			
			if(ToolUtil.isNull(msg, "DFCycle")) {
				meterSetupVO.setDfCycle(null);
			}else if (!ToolUtil.numberCheck(msg.optString("DFCycle"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("需量預測週期數字格式錯誤");
			}else {
				meterSetupVO.setDfCycle(msg.optString("DFCycle"));
			}
			
			if(ToolUtil.isNull(msg, "DFUpLimit")) {
				meterSetupVO.setDfUpLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("DFUpLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("需量預測上限數字格式錯誤");
			}else {
				meterSetupVO.setDfUpLimit(msg.optString("DFUpLimit"));
			}
			
			if(ToolUtil.isNull(msg, "DFLoLimit")) {
				meterSetupVO.setDfLoLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("DFLoLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("需量預測下限數字格式錯誤");
			}else {
				meterSetupVO.setDfLoLimit(msg.optString("DFLoLimit"));
			}
			
			if(!ToolUtil.isNull(msg, "CurAlertEnabled")) {
				meterSetupVO.setCurAlertEnabled(msg.optString("CurAlertEnabled"));
			}
			
			if(ToolUtil.isNull(msg, "CurUpLimit")) {
				meterSetupVO.setCurUpLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("CurUpLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電流上限數字格式錯誤");
			}else {
				meterSetupVO.setCurUpLimit(msg.optString("CurUpLimit"));
			}
			if(ToolUtil.isNull(msg, "CurLoLimit")) {
				meterSetupVO.setCurLoLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("CurLoLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電流下限數字格式錯誤");
			}else {
				meterSetupVO.setCurLoLimit(msg.optString("CurLoLimit"));
			}
			
			if(!ToolUtil.isNull(msg, "VolAlertEnabled")) {
				meterSetupVO.setVolAlertEnabled(msg.optString("VolAlertEnabled"));
			}
			if(!ToolUtil.isNull(msg, "VolAlertType")) {
				meterSetupVO.setVolAlertType(msg.optString("VolAlertType"));
			}
			
			
			if(ToolUtil.isNull(msg, "VolUpLimit")) {
				meterSetupVO.setVolUpLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("VolUpLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電壓上限數字格式錯誤");
			}else {
				meterSetupVO.setVolUpLimit(msg.optString("VolUpLimit"));
			}
			if(ToolUtil.isNull(msg, "VolLoLimit")) {
				meterSetupVO.setVolLoLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("VolLoLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("電壓下限數字格式錯誤");
			}else {
				meterSetupVO.setVolLoLimit(msg.optString("VolLoLimit"));
			}	
			if(!ToolUtil.isNull(msg, "ECAlertEnabled")) {
				meterSetupVO.setEcAlertEnabled(msg.optString("ECAlertEnabled"));
			}
			
			if(ToolUtil.isNull(msg, "ECUpLimit")) {
				meterSetupVO.setEcUpLimit(null);
			}else if (!ToolUtil.numberCheck(msg.optString("ECUpLimit"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("用電量上限數字格式錯誤");
			}else {
				meterSetupVO.setEcUpLimit(msg.optString("ECUpLimit"));
			}	
//			if(ToolUtil.isNull(msg, "UsuallyCC")) {
//				meterSetupVO.setUsuallyCC(null);
//			}else if (!ToolUtil.numberCheck(msg.optString("UsuallyCC"))) {
//				meterSetupVO.setError(true);
//				meterSetupVO.setCode("13");
//				meterSetupVO.setDescription("經常契約容量數字格式錯誤");
//			}else {
//				meterSetupVO.setUsuallyCC(msg.optString("UsuallyCC"));
//			}
//			if(ToolUtil.isNull(msg, "SPCC")) {
//				meterSetupVO.setSpcc(null);
//			}else if (!ToolUtil.numberCheck(msg.optString("SPCC"))) {
//				meterSetupVO.setError(true);
//				meterSetupVO.setCode("13");
//				meterSetupVO.setDescription("半尖峰契約容量數字格式錯誤");
//			}else {
//				meterSetupVO.setSpcc(msg.optString("SPCC"));
//			}
//			if(ToolUtil.isNull(msg, "SatSPCC")) {
//				meterSetupVO.setSatSPCC(null);
//			}else if (!ToolUtil.numberCheck(msg.optString("SatSPCC"))) {
//				meterSetupVO.setError(true);
//				meterSetupVO.setCode("13");
//				meterSetupVO.setDescription("週六半尖峰契約容量數字格式錯誤");
//			}else {
//				meterSetupVO.setSatSPCC(msg.optString("SatSPCC"));
//			}
//			if(ToolUtil.isNull(msg, "OPCC")) {
//				meterSetupVO.setOpcc(null);
//			}else if (!ToolUtil.numberCheck(msg.optString("OPCC"))) {
//				meterSetupVO.setError(true);
//				meterSetupVO.setCode("13");
//				meterSetupVO.setDescription("離峰契約容量數字格式錯誤");
//			}else {
//				meterSetupVO.setOpcc(msg.optString("OPCC"));
//			}
			if(ToolUtil.isNull(msg, "RatedPower")) {
				meterSetupVO.setRatedPower(null);
			}else if (!ToolUtil.numberCheck(msg.optString("RatedPower"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("額定功率數字格式錯誤");
			}else {
				meterSetupVO.setRatedPower(msg.optString("RatedPower"));
			}			
			if(ToolUtil.isNull(msg, "RatedVol")) {
				meterSetupVO.setRatedVol(null);
			}else if (!ToolUtil.numberCheck(msg.optString("RatedVol"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("額定電壓數字格式錯誤");
			}else {
				meterSetupVO.setRatedVol(msg.optString("RatedVol"));
			}
			if(ToolUtil.isNull(msg, "RatedCur")) {
				meterSetupVO.setRatedCur(null);
			}else if (!ToolUtil.numberCheck(msg.optString("RatedCur"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("13");
				meterSetupVO.setDescription("額定電流數字格式錯誤");
			}else {
				meterSetupVO.setRatedCur(msg.optString("RatedCur"));
			}
			if(!ToolUtil.lengthCheck(msg.optString("EquipDesc"), 30)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("用電設備說明超過長度限制");
			}else {
				meterSetupVO.setEquipDesc(msg.optString("EquipDesc"));	
			}
			
			//取得電號契約容量
			PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
			List<DynaBean> pa = powerAccountDAO.getPowerAccountCC(meterSetupVO.getPowerAccount());
			if (pa != null && !pa.isEmpty()) {
				DynaBean paBean = pa.get(0);
				
				if(paBean.get("usuallycc")!=null)
					meterSetupVO.setUsuallyCC(ObjectUtils.toString(paBean.get("usuallycc")));
				if(paBean.get("spcc")!=null)
					meterSetupVO.setSpcc(ObjectUtils.toString(paBean.get("spcc")));	
				if(paBean.get("satspcc")!=null)
					meterSetupVO.setSatSPCC(ObjectUtils.toString(paBean.get("satspcc")));
				if(paBean.get("opcc")!=null)
					meterSetupVO.setOpcc(ObjectUtils.toString(paBean.get("opcc")));
			}
			
			meterSetupVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return meterSetupVO;
	}
	
	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(String deviceId) throws Exception {
		JSONObject data = new JSONObject();
		try {
			MeterSetupVO meterSetupVO = new MeterSetupVO();
			meterSetupVO.setDeviceId(deviceId);
			MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
			List<DynaBean> rows = meterSetupDAO.getMeterSetup(meterSetupVO);
			if(rows!=null && !rows.isEmpty()) {
				DynaBean bean = rows.get(0);
				data.put("DeviceID", bean.get("deviceid"));
				data.put("ECO5Account", bean.get("eco5account"));
				data.put("BankCode", bean.get("bankcode"));
				data.put("Enabled", bean.get("enabled"));				
			}
			data.put("Message", "Insert Success");
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 檢核MeterId是否存在(true:存在, false:不存在)
	 * @param eco5Account
	 * @param meterId
	 * @return
	 * @throws Exception
	 */
	private boolean checkMeterId(String eco5Account, String meterId) throws Exception {
		MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
		List<DynaBean> rows = meterSetupDAO.checkMeterId(eco5Account, meterId);
		if(rows!=null && !rows.isEmpty()) {
			return true;
		}else {
			return false;
		}
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
