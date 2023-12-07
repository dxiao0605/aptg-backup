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
 * Servlet implementation class fixPowerAccount 異動電號
 */
@WebServlet("/fixPowerAccount")
public class fixPowerAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(fixPowerAccount.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public fixPowerAccount() {
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
		logger.debug("fixPowerAccount start");
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
						List<DynaBean> list = powerAccountDAO.checkPowerAccount(powerAccountVO.getPowerAccountNew());
						if(list!=null && list.size()>0) {
							rspJson.put("code", "11");
							rspJson.put("msg", "電號已存在");
						}else {
							powerAccountDAO.fixPowerAccount(powerAccountVO);
							ToolUtil.addLogRecord(powerAccountVO.getUserName(), "27", "電號:"+powerAccountVO.getPowerAccountOld()+"修改為"+powerAccountVO.getPowerAccountNew());
							
							rspJson.put("code", "00");
							rspJson.put("msg", "電號異動中");
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
		logger.debug("fixPowerAccount end");
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
			if(StringUtils.isBlank(request.optString("PowerAccountOld"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("15");
				powerAccountVO.setDescription("舊電號不可為空");
			}else {
				powerAccountVO.setPowerAccountOld(request.optString("PowerAccountOld"));
			}
											
			if(StringUtils.isBlank(request.optString("PowerAccountNew"))) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("15");
				powerAccountVO.setDescription("新電號不可為空");
			}else if(!ToolUtil.lengthCheck(request.optString("PowerAccountNew"), 11)) {
				powerAccountVO.setError(true);
				powerAccountVO.setCode("12");
				powerAccountVO.setDescription("新電號超過長度限制");
			}else {
				powerAccountVO.setPowerAccountNew(request.optString("PowerAccountNew"));
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
