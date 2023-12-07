package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.util.EmailUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;





/**
 * Servlet implementation class forgetPassword 忘記密碼
 */
@WebServlet("/forgetPassword")
public class forgetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(forgetPassword.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public forgetPassword() {
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
		logger.debug("forgetPassword start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(req)) {
				AdminSetupVO adminSetupVO  = this.parseJson(req);
				if(adminSetupVO.isError()) {
					rspJson.put("code", adminSetupVO.getCode());
					rspJson.put("msg", adminSetupVO.getDescription());
				}else {
					adminSetupVO.setSuspend("0");//非停權
					adminSetupVO.setEnabled("1");//啟用
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					List<DynaBean> userInfoList = adminSetupDAO.getAdminSetup(adminSetupVO);
					if (userInfoList != null && userInfoList.size() != 0) {
						DynaBean bean = userInfoList.get(0);						
						String verifyCode = ToolUtil.getVerifyCode();
						String accountEmail = ObjectUtils.toString(bean.get("email"));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 10);//驗證碼有效時間10分鐘
						adminSetupVO.setVerifyCode(verifyCode);
						adminSetupVO.setExpireDate(sdf.format(cal.getTime()));							
						adminSetupDAO.addVerifyCode(adminSetupVO);
							
						EmailUtil email = new EmailUtil();
						List<String> receivers = new ArrayList<>();
						receivers.add(accountEmail);
						ToolUtil.addLogRecord(adminSetupVO.getUserName(), "21", "忘記密碼");
						
						String subject = "雲端智慧能源管理系統帳號驗證信";
						String body = adminSetupVO.getUserName()+" 您好\r\n" + 
									"有收到您帳號/忘記密碼的申請\r\n" + 
									"您的驗證碼為："+verifyCode+"\r\n" + 
									"此驗證碼將在10分鐘後失效。\r\n" + 
									"如果您並未申請要重置您的密碼，請立即接洽您的系統管理員。\r\n" + 
									"這是自動發送的訊息，請勿回覆。\r\n" ;
						
						email.setToList(receivers);	// 設定收件者
						email.setSubject(subject);	// 設定信件主旨
						email.setBody(body);	// 設定信件內容
						email.sendMail();

						JSONObject msg = new JSONObject();
						msg.put("Email", accountEmail);
						rspJson.put("code", "00");
						rspJson.put("msg", msg);
						
					} else {
						rspJson.put("code", "05");
						rspJson.put("msg", "不可申請帳號/忘記密碼或已停權，請洽總行管理者");
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
		logger.debug("forgetPassword end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return adminSetupVO
	 * @throws Exception
	 */
	private AdminSetupVO parseJson(String json) throws Exception {
		AdminSetupVO adminSetupVO = new AdminSetupVO();
		try {
			JSONObject request = new JSONObject(json);
			adminSetupVO.setAccount(request.optString("Account"));
			adminSetupVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return adminSetupVO;
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
