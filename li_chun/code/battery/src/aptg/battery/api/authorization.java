package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import aptg.battery.util.AuthorizationUtil;
import aptg.battery.util.EncryptUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.AccountVO;

/**
 * Servlet implementation class authorization
 */
@WebServlet("/authorization")
public class authorization extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(authorization.class.getName());

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
			String language = ObjectUtils.toString(request.getHeader("language"));
			ResourceBundle resource = ToolUtil.getLanguage(language);
			logger.debug("account: " + account);
			if(StringUtils.isBlank(account)){
				rspJson.put("code", "10");
				rspJson.put("msg", "5018");//請輸入帳號
			}else if (StringUtils.isBlank(password)){
				rspJson.put("code", "11");
				rspJson.put("msg", "5019");//請輸入密碼
			}else{
				AccountVO accountVO = new AccountVO();
				accountVO.setSystemId(ToolUtil.getSystemId());
				accountVO.setAccount(account);
				accountVO.setEnabled("1");
				AccountDAO accountDAO = new AccountDAO();
				List<DynaBean> userInfoList = accountDAO.getAccountInfo(accountVO);
				if (userInfoList != null && userInfoList.size() != 0) {
					DynaBean bean = userInfoList.get(0);
					resource = ToolUtil.getLanguage(ObjectUtils.toString(bean.get("language")));
					if (!password.equals(EncryptUtil.decryptAES(bean.get("password").toString(), ToolUtil.getKey()))) {
						rspJson.put("code", "06");
						rspJson.put("msg", "5012");//密碼錯誤
					} else {
						boolean isOK = true;
						String timezone = ObjectUtils.toString(bean.get("timezone"));
						String disableTime = "";
						if(bean.get("disabletime")!=null) {
							SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss Z", timezone);
							SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							disableTime = ToolUtil.dateFormat(bean.get("disabletime"), sdf);
							Date sysdate = new Date();
							if(sysdate.after(sdf2.parse(bean.get("disabletime").toString()))) {
								isOK = false;
								rspJson.put("code", "33");
								rspJson.put("msg", "5064");//你的密碼已失效, 請重設密碼
							}
						}
						
						if(isOK) {
							JSONObject msg = new JSONObject(AuthorizationUtil.getAccount(ToolUtil.getSystemId(), account, bean.get("password").toString()));						
							msg.put("Language", bean.get("language"));
							msg.put("TimeZone", timezone);
							//Google API Key
							msg.put("Key", SysConfig.getInstance().getGoogleApiKey());
							msg.put("Lng", ToolUtil.getBigDecimal(bean.get("lng"), 8));// 經度
							msg.put("Lat", ToolUtil.getBigDecimal(bean.get("lat"), 8));// 緯度
							msg.put("DisableTime", disableTime);
												
							rspJson.put("msg", msg);
							rspJson.put("code", "00");
						}
					}
				} else {
					rspJson.put("code", "05");
					rspJson.put("msg", "5011");//用戶名不存在
				}
			} 
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
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
