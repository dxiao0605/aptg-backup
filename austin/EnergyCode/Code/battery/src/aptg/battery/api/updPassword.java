package aptg.battery.api;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

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

import aptg.battery.dao.AccountDAO;
import aptg.battery.util.EncryptUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;



/**
 * Servlet implementation class updPassword 修改密碼
 */
@WebServlet("/updPassword")
public class updPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updPassword.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updPassword() {
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
		logger.debug("updPassword start");
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
						accountVO.setEnabled("1");
						AccountDAO accountDAO = new AccountDAO();
						List<DynaBean> userInfoList = accountDAO.getAccount(accountVO);
						if (userInfoList != null && userInfoList.size() != 0) {
							DynaBean bean = userInfoList.get(0);
							if (!accountVO.getOldPassword().equals(EncryptUtil.decryptAES(bean.get("password").toString(), ToolUtil.getKey()))) {
								rspJson.put("code", "06");
								rspJson.put("msg", resource.getString("5012"));//密碼錯誤
							} else {
								accountVO.setUserName(ObjectUtils.toString(bean.get("username")));
								accountDAO.updPassword(accountVO);								
								rspJson.put("code", "00");				
								rspJson.put("msg", resource.getString("5002"));//保存成功
							}
						} else {
							rspJson.put("code", "05");
							rspJson.put("msg", resource.getString("5011"));//用戶名不存在
						}									
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
		logger.debug("updPassword end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return bankInfVO
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
						
			if(ToolUtil.lengthCheck(msg.optString("NewPassword"), 7)) {
				accountVO.setError(true);
				accountVO.setCode("12");
				accountVO.setDescription("5015");//密碼長度須大於8碼(<=7)
				return accountVO;
			}else if(!ToolUtil.passwordCheck(msg.optString("NewPassword"))) {
				accountVO.setError(true);
				accountVO.setCode("12");
				accountVO.setDescription("5016");//密碼僅限輸入英數字
				return accountVO;
			}else if(StringUtils.equals(msg.optString("NewPassword"), msg.optString("Password"))) {
				accountVO.setError(true);
				accountVO.setCode("12");
				accountVO.setDescription("5017");//新密碼不可與當前密碼相同
				return accountVO;
			}else if(!StringUtils.equals(msg.optString("NewPassword"), msg.optString("NewPasswordCheck"))) {
				accountVO.setError(true);
				accountVO.setCode("12");
				accountVO.setDescription("5020");//新密碼前後不相同
				return accountVO;
			}else {	
				accountVO.setOldPassword(msg.optString("Password"));
				accountVO.setPassword(EncryptUtil.encryptAES(msg.optString("NewPassword"), ToolUtil.getKey()));
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
