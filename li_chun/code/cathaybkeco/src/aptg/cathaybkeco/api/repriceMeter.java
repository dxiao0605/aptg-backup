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
 * Servlet implementation class repriceMeter 重新統計電表
 */
@WebServlet("/repriceMeter")
public class repriceMeter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(repriceMeter.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public repriceMeter() {
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
		logger.debug("repriceMeter start");
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
					if(meterSetupVO.isError()) {
						rspJson.put("code", meterSetupVO.getCode());
						rspJson.put("msg", meterSetupVO.getDescription());
					}else {				
						MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
						meterSetupDAO.repriceMeter(meterSetupVO);
						ToolUtil.addLogRecord(meterSetupVO.getUserName(), "27", "重新統計電表:"+meterSetupVO.getDeviceId());
						
						rspJson.put("code", "00");
						rspJson.put("msg", "電費重新計算中");						
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
		logger.debug("repriceMeter end");
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
			if(StringUtils.isBlank(request.optString("DeviceID"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("DeviceID不可為空");
			}else {
				meterSetupVO.setDeviceId(request.optString("DeviceID"));
			}
											
			if(StringUtils.isBlank(request.optString("PowerAccount"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("新電號不可為空");
			}else {
				meterSetupVO.setPowerAccount(request.optString("PowerAccount"));

				PowerAccountDAO powerAccountDAO = new PowerAccountDAO();
				List<DynaBean> pa = powerAccountDAO.getPowerAccountCC(request.optString("PowerAccount"));
				if (pa != null && !pa.isEmpty()) {
					DynaBean bean = pa.get(0);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					meterSetupVO.setPowerPhaseNew(ObjectUtils.toString(bean.get("powerphase")));	
					meterSetupVO.setApplyDateNew(ToolUtil.dateFormat(bean.get("applydate"), sdf));
					meterSetupVO.setRatePlanCodeNew(ObjectUtils.toString(bean.get("rateplancode")));
					if(bean.get("usuallycc")!=null)
						meterSetupVO.setUsuallyCCNew(ObjectUtils.toString(bean.get("usuallycc")));
					if(bean.get("spcc")!=null)
						meterSetupVO.setSpccNew(ObjectUtils.toString(bean.get("spcc")));	
					if(bean.get("satspcc")!=null)
						meterSetupVO.setSatspccNew(ObjectUtils.toString(bean.get("satspcc")));
					if(bean.get("opcc")!=null)									
						meterSetupVO.setOpccNew(ObjectUtils.toString(bean.get("opcc")));
				}
			}
				
			if(StringUtils.isBlank(request.optString("RepriceDate"))) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("15");
				meterSetupVO.setDescription("新電號不可為空");
			}else if (!ToolUtil.dateCheck(request.optString("RepriceDate"), "yyyy-MM")) {
				meterSetupVO.setError(true);
				meterSetupVO.setCode("18");
				meterSetupVO.setDescription("日期格式錯誤(yyyy-MM)");// 日期格式錯誤
			}else {
				meterSetupVO.setRepriceDate(request.optString("RepriceDate")+"-01");
			}
			
			
						
			meterSetupVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return meterSetupVO;
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
