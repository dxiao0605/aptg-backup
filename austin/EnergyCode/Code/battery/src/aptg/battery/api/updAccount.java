package aptg.battery.api;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.battery.dao.AccountDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;



/**
 * Servlet implementation class updAccount 用戶設定
 */
@WebServlet("/updAccount")
public class updAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updAccount.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updAccount() {
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
		logger.debug("updAccount start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					AccountVO accountVO = this.parseJson(req);
					if(accountVO.isError()) {
						rspJson.put("code", accountVO.getCode());
						rspJson.put("msg", resource.getString(accountVO.getDescription()));
					}else {
						accountVO.setSystemId(ToolUtil.getSystemId());
						AccountDAO accountDAO = new AccountDAO();
						accountDAO.updAccount(accountVO);
						resource = ToolUtil.getLanguage(accountVO.getLanguage());
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5002"));//保存成功					 							
					}
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5003"));//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("updAccount end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return AccountVO
	 * @throws Exception
	 */
	private AccountVO parseJson(String json) throws Exception {
		AccountVO accountVO = new AccountVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			if(ToolUtil.isNull(msg, "Account")) {
				accountVO.setError(true);
				accountVO.setCode("13");
				accountVO.setDescription("5018");//請輸入帳號
				return accountVO;
			}else {
				accountVO.setAccount(msg.optString("Account"));
			}
			accountVO.setTimeZone(msg.optString("TimeZone"));
			accountVO.setLanguage(msg.optString("Language"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return accountVO;
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
