package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import aptg.cathaybkeco.dao.FcstChargeDAO;
import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class updPowerAccount 修改電號資訊
 */
@WebServlet("/updPowerAccount")
public class updPowerAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updPowerAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updPowerAccount() {
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
		logger.debug("updPowerAccount start");
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
					if (powerAccountVO.isError()) {
						rspJson.put("code", powerAccountVO.getCode());
						rspJson.put("msg", powerAccountVO.getDescription());
					} else {
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
						List<DynaBean> list = powerAccountDAO.checkApplyDate(powerAccountVO);
						if (list != null && list.size() > 0 && !powerAccountVO.getApplyDate().equals(powerAccountVO.getApplyDateOld())) {
							rspJson.put("code", "08");
							rspJson.put("msg", "電號生效日期已存在");
						} else {
							powerAccountDAO.updPowerAccount(powerAccountVO);
							ToolUtil.addLogRecord(powerAccountVO.getUserName(), "6",
									"修改電號:" + powerAccountVO.getPowerAccount());
							
							
							if("3".equals(powerAccountVO.getModifyStatus())) {
								rspJson.put("msg", "契約容量修改重新計算中");
							}else{
								rspJson.put("msg", "Update Success");
							}
							rspJson.put("code", "00");
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
		logger.debug("updPowerAccount end");
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
			if (!ToolUtil.lengthCheck(msg.optString("BankCode"), 3)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("分行代號超過長度限制");
				return powerAccountVO;
			} else {
				powerAccountVO.setBankCode(msg.optString("BankCode"));
			}

			if (!ToolUtil.lengthCheck(msg.optString("PowerAccount"), 11)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("電號超過長度限制");
				return powerAccountVO;
			} else {
				powerAccountVO.setPowerAccount(msg.optString("PowerAccount"));
			}

			if (!ToolUtil.lengthCheck(msg.optString("CustomerName"), 50)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("戶名超過長度限制");
				return powerAccountVO;
			} else {
				powerAccountVO.setCustomerName(msg.optString("CustomerName"));
			}

			if (!ToolUtil.lengthCheck(msg.optString("AccountDesc"), 50)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("說明超過長度限制");
				return powerAccountVO;
			} else {
				powerAccountVO.setAccountDesc(msg.optString("AccountDesc"));
			}

			if (!ToolUtil.numberCheck(msg.optString("PATypeCode"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("電號類別數字格式錯誤");
				return powerAccountVO;
			} else {
				powerAccountVO.setPaTypeCode(msg.optString("PATypeCode"));
			}

			if (!ToolUtil.numberCheck(msg.optString("PowerPhase"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("相用電數字格式錯誤");
				return powerAccountVO;
			} else {
				powerAccountVO.setPowerPhase(msg.optString("PowerPhase"));
			}
			if(!ToolUtil.lengthCheck(msg.optString("PAAddress"), 100)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("電號地址超過長度限制");
				return powerAccountVO;
			}else {
				powerAccountVO.setPaAddress(msg.optString("PAAddress"));	
			}
		

			if (!ToolUtil.numberCheck(msg.optString("RatePlanCode"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("用電種類代碼數字格式錯誤");
				return powerAccountVO;
			} else if (StringUtils.isBlank(msg.optString("RatePlanCode"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("15");
				powerAccountVO.setDescription("用電種類代碼不能為空");
				return powerAccountVO;
			} else {
				powerAccountVO.setRatePlanCode(msg.optString("RatePlanCode"));
			}

			if(ToolUtil.isNull(msg, "UsuallyCC")) {
				powerAccountVO.setUsuallyCC(null);
			}else if (!ToolUtil.numberCheck(msg.optString("UsuallyCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("經常契約容量數字格式錯誤");
				return powerAccountVO;
			}else if (msg.optInt("UsuallyCC")<0 || msg.optInt("UsuallyCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("經常契約容量數字格式錯誤");
			} else {
				powerAccountVO.setUsuallyCC(msg.optString("UsuallyCC"));
			}

			if (ToolUtil.isNull(msg, "SPCC")) {
				powerAccountVO.setSpCC(null);
			} else if (!ToolUtil.numberCheck(msg.optString("SPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("半尖峰契約容量數字格式錯誤");
				return powerAccountVO;
			}else if (msg.optInt("SPCC")<0 || msg.optInt("SPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("半尖峰契約容量數字格式錯誤");
			} else {
				powerAccountVO.setSpCC(msg.optString("SPCC"));
			}

			if (ToolUtil.isNull(msg, "SatSPCC")) {
				powerAccountVO.setSatSPCC(null);
			} else if (!ToolUtil.numberCheck(msg.optString("SatSPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("週六半尖峰契約容量數字格式錯誤");
				return powerAccountVO;
			}else if (msg.optInt("SatSPCC")<0 || msg.optInt("SatSPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("週六半尖峰契約容量數字格式錯誤");
			} else {
				powerAccountVO.setSatSPCC(msg.optString("SatSPCC"));
			}

			if (ToolUtil.isNull(msg, "OPCC")) {
				powerAccountVO.setOpCC(null);
			} else if (!ToolUtil.numberCheck(msg.optString("OPCC"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("離峰契約容量數字格式錯誤");
				return powerAccountVO;
			}else if (msg.optInt("OPCC")<0 || msg.optInt("OPCC")>75) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("13");
				powerAccountVO.setDescription("離峰契約容量數字格式錯誤");
			} else {
				powerAccountVO.setOpCC(msg.optString("OPCC"));
			}
			
			boolean haveMeter = false;//是否有電表
			MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
			List<DynaBean> meterList = meterSetupDAO.checkPowerAccount(powerAccountVO.getPowerAccount());
			if (meterList != null && !meterList.isEmpty()) {
				haveMeter = true;
			}
			
			boolean haveFC = false;//是否有電費
			FcstChargeDAO fcstChargeDAO = new FcstChargeDAO();
			List<DynaBean> fcList = fcstChargeDAO.checkPowerAccount(powerAccountVO.getPowerAccount());
			if (fcList != null && !fcList.isEmpty()) {
				haveFC = true;
			}
			
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String applyDate = msg.optString("ApplyDate");
			String applyDateOld = msg.optString("OldApplyDate");
			powerAccountVO.setApplyDate(applyDate);
			powerAccountVO.setApplyDateOld(applyDateOld);
			
			PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
			if (StringUtils.isNotBlank(applyDateOld)) {//修改契約容量
				powerAccountVO.setAddFlag(false);
				List<DynaBean> checkList = powerAccountDAO.checkPowerAccountCC(powerAccountVO);
				if(haveMeter && haveFC && checkList.isEmpty()) {//如果有電表，且有電費，則重算電費		
					if(sdf.parse(applyDate).before(now) || sdf.parse(applyDateOld).before(now)) {//新生效日或舊生效日小於系統日
						powerAccountVO.setModifyStatus("3");
						
						if(sdf.parse(applyDate).after(now) && sdf.parse(applyDateOld).before(now)) {
							boolean err = true;
							//舊 系統日 新 
							PowerAccountVO newVO = new PowerAccountVO();
							
							newVO.setPowerAccount(powerAccountVO.getPowerAccount());
							List<DynaBean> paList = powerAccountDAO.getPowerAccount(newVO);							
							if (paList != null && !paList.isEmpty()) {								
								for(DynaBean paBean : paList) {
									String paDate = ToolUtil.dateFormat(paBean.get("applydate"), sdf);
									if(sdf.parse(applyDateOld).after(sdf.parse(paDate)) && !applyDateOld.equals(paDate)) {
										powerAccountVO.setPowerPhaseNew(ObjectUtils.toString(paBean.get("powerphase")));	
										powerAccountVO.setApplyDateNew(ToolUtil.dateFormat(paBean.get("applydate"), sdf));
										powerAccountVO.setRatePlanCodeNew(ObjectUtils.toString(paBean.get("rateplancode")));
										if(paBean.get("usuallycc")!=null)
											powerAccountVO.setUsuallyCCNew(ObjectUtils.toString(paBean.get("usuallycc")));
										if(paBean.get("spcc")!=null)
											powerAccountVO.setSpCCNew(ObjectUtils.toString(paBean.get("spcc")));	
										if(paBean.get("satspcc")!=null)
											powerAccountVO.setSatSPCCNew(ObjectUtils.toString(paBean.get("satspcc")));
										if(paBean.get("opcc")!=null)
											powerAccountVO.setOpCCNew(ObjectUtils.toString(paBean.get("opcc")));
										err = false;
										break;										
									}									
								}
							}
							
							if(err) {
								powerAccountVO.setError(true);
								powerAccountVO.setCode("24");
								powerAccountVO.setDescription("此電號已有電費，不可調大生效日");
								return powerAccountVO;
							}
						}else {
							//新 舊 系統日
							//舊 新 系統日
							//新 系統日 舊
							powerAccountVO.setPowerPhaseNew(powerAccountVO.getPowerPhase());	
							powerAccountVO.setApplyDateNew(powerAccountVO.getApplyDate());
							powerAccountVO.setRatePlanCodeNew(powerAccountVO.getRatePlanCode());
							powerAccountVO.setUsuallyCCNew(powerAccountVO.getUsuallyCC());
							powerAccountVO.setSpCCNew(powerAccountVO.getSpCC());	
							powerAccountVO.setSatSPCCNew(powerAccountVO.getSatSPCC());
							powerAccountVO.setOpCCNew(powerAccountVO.getOpCC());
						}

						PowerAccountVO oldVO = new PowerAccountVO();
						oldVO.setPowerAccount(powerAccountVO.getPowerAccount());
						oldVO.setApplyDate(applyDateOld);
						List<DynaBean> oldPA = powerAccountDAO.getPowerAccount(oldVO);
						if (oldPA != null && !oldPA.isEmpty()) {
							DynaBean paBean = oldPA.get(0);
							if(sdf.parse(applyDate).before(sdf.parse(applyDateOld))) {
								powerAccountVO.setRepriceDate(applyDate);
							}else {
								powerAccountVO.setRepriceDate(applyDateOld);	
							}
							powerAccountVO.setPowerPhaseOld(ObjectUtils.toString(paBean.get("powerphase")));	
							powerAccountVO.setApplyDateOld(ToolUtil.dateFormat(paBean.get("applydate"), sdf));
							powerAccountVO.setRatePlanCodeOld(ObjectUtils.toString(paBean.get("rateplancode")));
							if(paBean.get("usuallycc")!=null)
								powerAccountVO.setUsuallyCCOld(ObjectUtils.toString(paBean.get("usuallycc")));
							if(paBean.get("spcc")!=null)
								powerAccountVO.setSpCCOld(ObjectUtils.toString(paBean.get("spcc")));	
							if(paBean.get("satspcc")!=null)
								powerAccountVO.setSatSPCCOld(ObjectUtils.toString(paBean.get("satspcc")));
							if(paBean.get("opcc")!=null)
								powerAccountVO.setOpCCOld(ObjectUtils.toString(paBean.get("opcc")));
						}else {
							powerAccountVO.setRepriceDate(applyDate);
						}	
					}		
					
				}		
			} else {//新增契約容量
				powerAccountVO.setAddFlag(true);
				if(sdf.parse(applyDate).before(now) && haveMeter && haveFC) {//生效日小於系統日，且有電表，則重算電費
					powerAccountVO.setModifyStatus("3");
				}				
			}
			
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
