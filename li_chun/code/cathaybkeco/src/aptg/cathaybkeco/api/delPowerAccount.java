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

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerAccountDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountVO;

/**
 * Servlet implementation class delPowerAccount 刪除電號資訊
 */
@WebServlet("/delPowerAccount")
public class delPowerAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delPowerAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delPowerAccount() {
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
		logger.debug("delPowerAccount start");
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
						PowerAccountDAO powerAccountDAO = new PowerAccountDAO();						
						powerAccountDAO.delPowerAccount(powerAccountVO);
						ToolUtil.addLogRecord(powerAccountVO.getUserName(), "26", "刪除電號:"+powerAccountVO.getPowerAccount());
						
						rspJson.put("code", "00");
						rspJson.put("msg", "系統刪除電號中");						
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
		logger.debug("delPowerAccount end");
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
			if(StringUtils.isBlank(request.optString("PowerAccount"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("15");
				powerAccountVO.setDescription("電號不可為空");
			}else {
				powerAccountVO.setPowerAccount(request.optString("PowerAccount"));
			}
			
			MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
			List<DynaBean> rows = meterSetupDAO.checkPowerAccount(request.optString("PowerAccount"));
			if(rows!=null && !rows.isEmpty()) {
				int size = rows.size();
				String meterName = "";
				for(DynaBean bean : rows) {
					meterName += ((StringUtils.isNotBlank(meterName)?",":"")+ObjectUtils.toString(bean.get("metername")));
				}				
				powerAccountVO.setError(true);
				powerAccountVO.setCode("28");
				powerAccountVO.setDescription("此電號仍有"+size+"個電表:"+meterName+"，請調整電表設定後再刪除電號");
			}
						
			powerAccountVO.setUserName(request.optString("UserName"));
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
