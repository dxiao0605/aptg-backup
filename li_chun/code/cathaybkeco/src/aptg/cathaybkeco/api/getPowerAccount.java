package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class getPowerAccount 電號資料/列表
 */
@WebServlet("/getPowerAccount")
public class getPowerAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerAccount() {
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
		logger.debug("getPowerAccount start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String powerAccount = ObjectUtils.toString(request.getParameter("powerAccount"));
			logger.debug("token: " + token);
			logger.debug("bankCode: " + bankCode);
			logger.debug("powerAccount: " + powerAccount);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					PowerAccountVO powerAccountVO = new PowerAccountVO();
					powerAccountVO.setBankCode(bankCode);
					powerAccountVO.setPowerAccount(powerAccount);
					PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
					List<DynaBean> list = powerAccountDAO.getPowerAccount(powerAccountVO);
					
					if (list != null && list.size() > 0) {
						List<PowerAccountVO> VOList = beanToVO(list);
						rspJson.put("code", "00");
						rspJson.put("count", VOList != null ? VOList.size() : 0);
						
						rspJson.put("msg", convertToJson(VOList));
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
		logger.debug("getPowerAccount end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}
	
	private List<PowerAccountVO> beanToVO(List<DynaBean> rows)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PowerAccountVO> powerAccountList = new ArrayList<PowerAccountVO>();
		try {
			int count = 0;
			String powerAccount = new String();
			List<PowerAccountVO> detailList = null;
			for(DynaBean bean : rows) {
				
					PowerAccountVO detailVO = new PowerAccountVO();
					
					if(!powerAccount.equals(bean.get("poweraccount"))) {
						PowerAccountVO headVO = new PowerAccountVO();
						powerAccount = bean.get("poweraccount").toString();
						count = 0;
						headVO.setPowerAccount(powerAccount);
						headVO.setCustomerName(ObjectUtils.toString(bean.get("customername")));					
						headVO.setAccountDesc(ObjectUtils.toString(bean.get("accountdesc")));
						headVO.setPaTypeCode(ObjectUtils.toString(bean.get("patypecode")));
						headVO.setPaTypeName(ObjectUtils.toString(bean.get("patypename")));
						headVO.setPowerPhase(ObjectUtils.toString(bean.get("powerphase")));
						headVO.setPowerPhaseDesc(ObjectUtils.toString(bean.get("powerphasedesc")));
						headVO.setPaAddress(ObjectUtils.toString(bean.get("paaddress")));
						headVO.setModifyStatus(ObjectUtils.toString(bean.get("modifystatus")));
						headVO.setModifyStatusDesc(ObjectUtils.toString(bean.get("modifystatusdesc")));
						
						detailList = new ArrayList<PowerAccountVO>();
						headVO.setVoList(detailList);			
						powerAccountList.add(headVO);
					}
					
					detailVO.setApplyDate(ToolUtil.dateFormat(bean.get("applydate"), sdf));
					detailVO.setRatePlanDesc(ObjectUtils.toString(bean.get("rateplandesc")));
					detailVO.setRatePlanCode(ObjectUtils.toString(bean.get("rateplancode")));
					detailVO.setUsuallyCC(ObjectUtils.toString(bean.get("usuallycc")));
					detailVO.setSpCC(ObjectUtils.toString(bean.get("spcc")));
					detailVO.setSatSPCC(ObjectUtils.toString(bean.get("satspcc")));
					detailVO.setOpCC(ObjectUtils.toString(bean.get("opcc")));
					if(count<2) {
						detailList.add(detailVO);
					}
					count++;				
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return powerAccountList;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(List<PowerAccountVO> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for(int i=0; i<rows.size(); i++) {
				PowerAccountVO headVO = rows.get(i);
				JSONObject head = new JSONObject();
				head.put("Seq", i+1);
				head.put("PowerAccount", headVO.getPowerAccount());
				head.put("CustomerName", headVO.getCustomerName());					
				head.put("AccountDesc", headVO.getAccountDesc());
				head.put("PATypeCode", headVO.getPaTypeCode());
				head.put("PATypeName", headVO.getPaTypeName());
				head.put("PowerPhase", headVO.getPowerPhase());
				head.put("PowerPhaseDesc", headVO.getPowerPhaseDesc());
				head.put("PAAddress", headVO.getPaAddress());
				head.put("ModifyStatus", headVO.getModifyStatus());
				head.put("ModifyStatusDesc", headVO.getModifyStatusDesc());
				
				JSONArray listArr = new JSONArray();
				for(PowerAccountVO detailVO: headVO.getVoList()) {
					JSONObject detail = new JSONObject();					
					detail.put("ApplyDate", detailVO.getApplyDate());
					detail.put("RatePlanDesc", detailVO.getRatePlanDesc());
					detail.put("RatePlanCode", detailVO.getRatePlanCode());
					detail.put("UsuallyCC", StringUtils.isNotBlank(detailVO.getUsuallyCC()) ? new BigDecimal(detailVO.getUsuallyCC()) : "");
					detail.put("SPCC", StringUtils.isNotBlank(detailVO.getSpCC()) ? new BigDecimal(detailVO.getSpCC()) : "");
					detail.put("SatSPCC", StringUtils.isNotBlank(detailVO.getSatSPCC()) ? new BigDecimal(detailVO.getSatSPCC()) : "");
					detail.put("OPCC", StringUtils.isNotBlank(detailVO.getOpCC()) ? new BigDecimal(detailVO.getOpCC()) : "");
					listArr.put(detail);
				}
				head.put("detail", listArr);
				list.put(head);
			}
			data.put("PowerAccount", list);
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
