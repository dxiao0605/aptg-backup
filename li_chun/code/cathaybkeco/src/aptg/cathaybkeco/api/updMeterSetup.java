package aptg.cathaybkeco.api;

import java.io.IOException;
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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class updMeterSetup 修改電表資訊
 */
@WebServlet("/updMeterSetup")
public class updMeterSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updMeterSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updMeterSetup() {
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
		logger.debug("updMeterSetup start");
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
						MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
						meterSetupDAO.updMeterSetup(meterSetupVO);
						ToolUtil.addLogRecord(meterSetupVO.getUserName(), "10", "修改電表，DeviceID:"+meterSetupVO.getDeviceId());
	
						JSONObject msg = convertToJson(meterSetupVO.getDeviceId());
						if("1".equals(meterSetupVO.getRepriceStatus())) {
							msg.put("Message", "電費重新計算中");
						}else {
							msg.put("Message", "Update Success");	
						}
						rspJson.put("msg", msg);
						rspJson.put("code", "00");
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
		logger.debug("updMeterSetup end");
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
			meterSetupVO.setDeviceId(msg.optString("DeviceID"));
			
			if(!ToolUtil.lengthCheck(msg.optString("MeterName"), 20)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("電表名稱超過長度限制");
			}else {
				meterSetupVO.setMeterName(msg.optString("MeterName"));	
			}
			meterSetupVO.setMeterTypeCode(msg.optString("MeterTypeCode"));
			
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
			}else {
				meterSetupVO.setTreeChartID(msg.optString("TreeChartID"));	
			}
			
			meterSetupVO.setEnabled(msg.optString("Enabled"));
			meterSetupVO.setWiringCode(msg.optString("WiringCode"));
			if (ToolUtil.isNull(msg, "UsageCode")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("耗能分類不能為空");
			}else {
				meterSetupVO.setUsageCode(msg.optString("UsageCode"));
			}
			
			
			if (ToolUtil.isNull(msg, "PowerAccount")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("電號不能為空");
			}else if(!ToolUtil.lengthCheck(msg.optString("PowerAccount"), 11)) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("12");
				meterSetupVO.setDescription("電號超過長度限制");
			}else {
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
			
			//是否重新計算電費
			MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
			List<DynaBean> list = meterSetupDAO.getMeterInfo(meterSetupVO.getDeviceId());
			if(list!=null && !list.isEmpty()) {
				DynaBean bean = list.get(0);
				if(!meterSetupVO.getPowerAccount().equals(ObjectUtils.toString(bean.get("poweraccount"))) ||
					!meterSetupVO.getUsageCode().equals(ObjectUtils.toString(bean.get("usagecode")))) {

					if (pa != null && !pa.isEmpty()) {
						DynaBean paBean = pa.get(0);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						meterSetupVO.setRepriceStatus("1");
						meterSetupVO.setAccountDesc(ObjectUtils.toString(paBean.get("accountdesc")));
						meterSetupVO.setRepriceDate(ToolUtil.dateFormat(paBean.get("applydate"), sdf));
						meterSetupVO.setPowerPhaseNew(ObjectUtils.toString(paBean.get("powerphase")));	
						meterSetupVO.setApplyDateNew(ToolUtil.dateFormat(paBean.get("applydate"), sdf));
						meterSetupVO.setRatePlanCodeNew(ObjectUtils.toString(paBean.get("rateplancode")));
						if(paBean.get("usuallycc")!=null)
							meterSetupVO.setUsuallyCCNew(ObjectUtils.toString(paBean.get("usuallycc")));
						if(paBean.get("spcc")!=null)
							meterSetupVO.setSpccNew(ObjectUtils.toString(paBean.get("spcc")));	
						if(paBean.get("satspcc")!=null)
							meterSetupVO.setSatspccNew(ObjectUtils.toString(paBean.get("satspcc")));
						if(paBean.get("opcc")!=null)
							meterSetupVO.setOpccNew(ObjectUtils.toString(paBean.get("opcc")));
					}
				}							
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
