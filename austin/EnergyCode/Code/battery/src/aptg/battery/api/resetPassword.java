package aptg.battery.api;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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

import aptg.battery.config.SysConfig;
import aptg.battery.dao.AccountDAO;
import aptg.battery.util.EmailUtil;
import aptg.battery.util.EncryptUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;



/**
 * Servlet implementation class resetPassword 重置密碼
 */
@WebServlet("/resetPassword")
public class resetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(resetPassword.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public resetPassword() {
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
		logger.debug("resetPassword start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(req)) {
				ResourceBundle resource = ToolUtil.getLanguage(language);			
				AccountVO accountVO = this.parseJson(req, resource);
				if(accountVO.isError()) {
					rspJson.put("code", accountVO.getCode());
					rspJson.put("msg", resource.getString(accountVO.getDescription()));
				}else {
					AccountDAO accountDAO = new AccountDAO();
					List<DynaBean> userInfoList = accountDAO.getAccountInfo(accountVO);
					if (userInfoList != null && userInfoList.size() != 0) {
						DynaBean bean = userInfoList.get(0);
						ResourceBundle userResource = ToolUtil.getLanguage(ObjectUtils.toString(bean.get("language")));
						String userName = ObjectUtils.toString(bean.get("username"));
						String newPassword = ToolUtil.GetRandomString(12);
						accountVO.setSystemId(ToolUtil.getSystemId());
						accountVO.setPassword(EncryptUtil.encryptAES(newPassword, ToolUtil.getKey()));
						accountVO.setDisableTime(ToolUtil.getDisableTime());
						accountDAO.updPassword(accountVO);
						
						EmailUtil email = new EmailUtil();
						List<String> receivers = new ArrayList<>();
						receivers.add(ObjectUtils.toString(bean.get("email")));
						
						MessageFormat subjectmf = new MessageFormat(userResource.getString("mail.subject"));							
						String subject = subjectmf.format(new Object[] { userName });
						MessageFormat bodymf = new MessageFormat(userResource.getString("mail.body"));
						String body = bodymf.format(new Object[] { userName, newPassword,SysConfig.getInstance().getResetUrl() });
						
						email.setToList(receivers);	// 設定收件者
						email.setSubject(subject);	// 設定信件主旨
						email.setBody(body);	// 設定信件內容
						email.sendMail();
						
						rspJson.put("code", "00");
						rspJson.put("msg", resource.getString("5063"));//新的密碼已寄至該帳號的郵箱
						
					} else {
						rspJson.put("code", "05");
						rspJson.put("msg", resource.getString("5011"));//用戶名不存在
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
		logger.debug("resetPassword end");
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
			if(ToolUtil.isNull(request, "Account")) {
				accountVO.setError(true);
				accountVO.setCode("24");
				accountVO.setDescription(resource.getString("1102")+resource.getString("5008"));//帳號不能為空
			}else {				
				accountVO.setAccount(request.optString("Account"));
			}
			accountVO.setEnabled("1");
			accountVO.setUserName(request.optString("UserName"));
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
