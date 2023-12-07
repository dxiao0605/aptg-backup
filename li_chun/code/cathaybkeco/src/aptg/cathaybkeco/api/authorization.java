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

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.util.Account;
import aptg.cathaybkeco.util.EncryptUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

/**
 * Servlet implementation class getWaterServer
 */
@WebServlet("/authorization")
public class authorization extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(authorization.class.getName());
	private static final String key = "aptgcathaybkeco";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public authorization() {
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
		logger.debug("login start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String account = ObjectUtils.toString(request.getHeader("account"));
			String password = ObjectUtils.toString(request.getHeader("pw"));
			logger.debug("account: " + account);
			if (StringUtils.isNotBlank(account) && StringUtils.isNotBlank(password)) {
				AdminSetupVO adminSetupVO = new AdminSetupVO();
				adminSetupVO.setAccount(account);
				adminSetupVO.setEnabled("1");

				AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
				List<DynaBean> userInfoList = adminSetupDAO.getAdminSetup(adminSetupVO);
				if (userInfoList != null && userInfoList.size() != 0) {
					DynaBean bean = userInfoList.get(0);
					if("1".equals(ObjectUtils.toString(bean.get("suspend")))) {
						rspJson.put("code", "05");
						rspJson.put("msg", "不可申請帳號/忘記密碼或已停權，請洽總行管理者");
					}else if (!password.equals(EncryptUtil.decryptAES(bean.get("password").toString(), key))) {
						rspJson.put("code", "06");
						rspJson.put("msg", "密碼錯誤");
					} else {
						rspJson.put("code", "00");
						JSONObject msg = new JSONObject(Account.getAccount(ToolUtil.getSystemId(), account, bean.get("password").toString()));
						msg.put("BankCode", bean.get("bankcode"));
						msg.put("AreaName", ObjectUtils.toString(bean.get("areaname")));						
						rspJson.put("msg", msg);

						ToolUtil.addLogRecord(ObjectUtils.toString(bean.get("username")), "1", "使用者登入");
					}
				} else {
					rspJson.put("code", "05");
					rspJson.put("msg", "帳號不存在");
				}
			} else {
				rspJson.put("code", "04");
				rspJson.put("msg", "請輸入帳號密碼");
			}
		} catch (Exception e) {
			rspJson.put("code", "03");
			rspJson.put("msg", "登入失敗");
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("login end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
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
