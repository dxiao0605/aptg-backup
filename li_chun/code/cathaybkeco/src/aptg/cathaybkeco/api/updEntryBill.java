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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.EntryBillDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.EntryBillVO;

/**
 * Servlet implementation class updEntryBill 修改電費單
 */
@WebServlet("/updEntryBill")
public class updEntryBill extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updEntryBill.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updEntryBill() {
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
		logger.debug("updEntryBill start");
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
					EntryBillVO entryBillVO = this.parseJson(req);
					if (entryBillVO.isError()) {
						rspJson.put("code", entryBillVO.getCode());
						rspJson.put("msg", entryBillVO.getDescription());
					} else {
						if(checkBillMon(entryBillVO)) {
							rspJson.put("code", "14");
							rspJson.put("msg", "電費單已存在");
						}else if(checkBillPeriod(entryBillVO)) {
							rspJson.put("code", "19");
							rspJson.put("msg", "計費日期起迄期間重疊");
						}else {
							BigDecimal months = BigDecimal.ONE;
							EntryBillDAO entryBillDAO = new EntryBillDAO();
							List<DynaBean> list = entryBillDAO.getBillMonths(entryBillVO.getPowerAccount());
							if(list!=null && !list.isEmpty()) {
								DynaBean bean = list.get(0);
								months = ToolUtil.getBigDecimal(bean.get("billmonths"));
							}
							entryBillVO.setShowCharge(ToolUtil.divide(entryBillVO.getTotalCharge(), months, 0).toString());
							entryBillVO.setShowCEC(ToolUtil.divide(entryBillVO.getCec(), months, 0).toString());
							
							entryBillDAO.updEntryBill(entryBillVO);
							ToolUtil.addLogRecord(entryBillVO.getUserName(), "14", "修改"+entryBillVO.getPowerAccount()+"電費單"+entryBillVO.getBillMon());
							
							rspJson.put("code", "00");
							rspJson.put("msg", "Update Success");
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
		logger.debug("updEntryBill end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return entryBillVO
	 * @throws Exception
	 */
	private EntryBillVO parseJson(String json) throws Exception {
		EntryBillVO entryBillVO = new EntryBillVO();
		try {
			BigDecimal cec = BigDecimal.ZERO;
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			
			if(!ToolUtil.lengthCheck(msg.optString("PowerAccount"), 11)) {
				entryBillVO.setError(true);
				entryBillVO.setCode("12");
				entryBillVO.setDescription("電號超過長度限制");
			}else {
				entryBillVO.setPowerAccount(msg.optString("PowerAccount"));	
			}
			
			entryBillVO.setOldBillMon(msg.optString("OldBillMon"));
			entryBillVO.setBillMon(msg.optString("BillMon"));
			entryBillVO.setBillStartDay(msg.optString("BillStartDay"));
			entryBillVO.setBillEndDay(msg.optString("BillEndDay"));

			if (!ToolUtil.IntegerCheck(msg.optString("BaseCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("基本電費數字格式錯誤");
			}else {
				entryBillVO.setBaseCharge(msg.optString("BaseCharge"));
			}
			if (!ToolUtil.IntegerCheck(msg.optString("UsageCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("流動電費數字格式錯誤");
			}else {
				entryBillVO.setUsageCharge(msg.optString("UsageCharge"));
			}
			if (!ToolUtil.IntegerCheck(msg.optString("OverCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("非約定電費數字格式錯誤");
			}else {
				entryBillVO.setOverCharge(msg.optString("OverCharge"));
			}
			if (!ToolUtil.IntegerCheck(msg.optString("ShareCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("分攤電費數字格式錯誤");
			}else {
				entryBillVO.setShareCharge(msg.optString("ShareCharge"));
			}
			if (!ToolUtil.IntegerCheck(msg.optString("PFCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("功因補償款數字格式錯誤");
			}else {
				entryBillVO.setPfCharge(msg.optString("PFCharge"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("TotalCharge"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("總電費數字格式錯誤");
			}else {
				entryBillVO.setTotalCharge(msg.optString("TotalCharge"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("MaxDemandPK"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("尖峰最大需量數字格式錯誤");
			}else {
				entryBillVO.setMaxDemandPK(msg.optString("MaxDemandPK"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("MaxDemandSP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("半尖峰最大需量數字格式錯誤");
			}else {
				entryBillVO.setMaxDemandSP(msg.optString("MaxDemandSP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("MaxDemandSatSP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("周六半尖峰最大需量數字格式錯誤");
			}else {
				entryBillVO.setMaxDemandSatSP(msg.optString("MaxDemandSatSP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("MaxDemandOP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("離峰最大需量數字格式錯誤");
			}else {
				entryBillVO.setMaxDemandOP(msg.optString("MaxDemandOP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("CECPK"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("尖峰累積用電量數字格式錯誤");
			}else {
				cec = ToolUtil.add(cec, msg.optString("CECPK"));
				entryBillVO.setCecPK(msg.optString("CECPK"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("CECSP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("半尖峰累積用電量數字格式錯誤");
			}else {
				cec = ToolUtil.add(cec, msg.optString("CECSP"));
				entryBillVO.setCecSP(msg.optString("CECSP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("CECSatSP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("周六半尖峰累積用電量數字格式錯誤");
			}else {
				cec = ToolUtil.add(cec, msg.optString("CECSatSP"));
				entryBillVO.setCecSatSP(msg.optString("CECSatSP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("CECOP"))) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("離峰累積用電量數字格式錯誤");
			}else {
				cec = ToolUtil.add(cec, msg.optString("CECOP"));
				entryBillVO.setCecOP(msg.optString("CECOP"));
			}
			
			if (!ToolUtil.IntegerCheck(msg.optString("PF")) && msg.optDouble("PF")<0 && msg.optDouble("PF")>100) {
				entryBillVO.setError(true);
				entryBillVO.setCode("13");
				entryBillVO.setDescription("功率因數數字格式錯誤");
			}else {
				entryBillVO.setPf(msg.optString("PF"));
			}
			entryBillVO.setCec(cec);
			entryBillVO.setUserName(msg.optString("UserName"));		
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return entryBillVO;
	}
	
	/**
	 * 檢核電費單是否存在
	 * @param entryBillVO
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkBillMon(EntryBillVO entryBillVO) throws Exception {
		EntryBillDAO entryBillDAO = new EntryBillDAO();
		List<DynaBean> list = entryBillDAO.getEntryBill(entryBillVO);
		if(list!=null && !list.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 計費日期起迄期間是否有重疊
	 * @param entryBillVO
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkBillPeriod(EntryBillVO entryBillVO) throws Exception {
		EntryBillDAO entryBillDAO = new EntryBillDAO();
		List<DynaBean> list = entryBillDAO.checkBillPeriod(entryBillVO);
		if(list!=null && !list.isEmpty()) {
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
