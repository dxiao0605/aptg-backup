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
 * Servlet implementation class delUser 刪除使用者
 */
@WebServlet("/delUser")
public class delUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delUser.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delUser() {
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
		logger.debug("delUser start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {				
					AccountVO accountVO = this.parseJson(req, resource);
					if (accountVO.isError()) {
						rspJson.put("msg", accountVO.getDescription());
						rspJson.put("code", accountVO.getCode());
					} else {
						AccountDAO accountDAO = new AccountDAO();
						accountDAO.delUser(accountVO);
												
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5005"));//刪除成功	
					}																					
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {			
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5006"));//刪除失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("delUser end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private AccountVO parseJson(String json, ResourceBundle resource) throws Exception {
		AccountVO accountVO = new AccountVO();
		try {
			JSONObject request = new JSONObject(json);			
			accountVO.setSystemId(ToolUtil.getSystemId());			
			if(ToolUtil.isNull(request, "DeleteAccount")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1102")+resource.getString("5008"));//帳號不能為空
			}else if(StringUtils.equals(request.optString("DeleteAccount"), request.optString("Account"))) {
				accountVO.setError(true);
				accountVO.setCode("26");
				accountVO.setDescription(resource.getString("5056"));//不可刪除自己的帳號
			}else {				
				accountVO.setAccount(request.optString("DeleteAccount"));
			}
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
