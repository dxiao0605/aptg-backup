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
import aptg.cathaybkeco.util.EncryptUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

/**
 * Servlet implementation class updAdminSetup 修改使用者資料
 */
@WebServlet("/updAdminSetup")
public class updAdminSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updAdminSetup.class.getName());
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updAdminSetup() {
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
		logger.debug("addAdminSetup start");
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
					AdminSetupVO adminSetupVO = this.parseJson(req);
					if(adminSetupVO.isError()) {
						rspJson.put("code", adminSetupVO.getCode());
						rspJson.put("msg", adminSetupVO.getDescription());
					}else {						
						AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
						adminSetupDAO.updAdminSetup(adminSetupVO);
						ToolUtil.addLogRecord(adminSetupVO.getUserName(), "18", "修改"+adminSetupVO.getAccount()+"帳號資訊");
						
						JSONObject msg = new JSONObject();
						//查詢使用者資訊
						AdminSetupVO accountVO = new AdminSetupVO();
						accountVO.setAccount(adminSetupVO.getAccount());
						List<DynaBean> list = adminSetupDAO.getAdminSetupList(accountVO);
						if (list != null && list.size() > 0) {
							DynaBean bean = list.get(0);
							int rankcode = ToolUtil.parseInt(bean.get("rankcode"));
							int enabled = ToolUtil.parseInt(bean.get("enabled"));
							int suspend = ToolUtil.parseInt(bean.get("suspend"));
							msg.put("Account", ObjectUtils.toString(bean.get("account")));
							msg.put("UserName", ObjectUtils.toString(bean.get("username")));
							msg.put("BankCode", ObjectUtils.toString(bean.get("bankcode")));
							msg.put("BankName", ObjectUtils.toString(bean.get("bankname")));
							msg.put("Email", ObjectUtils.toString(bean.get("email")));				
							msg.put("RankCode", rankcode);
							msg.put("RankDesc", ObjectUtils.toString(bean.get("rankdesc")));
							if(bean.get("areacode")!=null) {					
								msg.put("Area", bean.get("areacode")+"-"+bean.get("areaname"));				
							}else {
								msg.put("Area", "");		
							}							
							msg.put("Enabled", ToolUtil.getEnabled(enabled));
							msg.put("Suspend", suspend);
						}

						msg.put("Message", "Update Success");
						rspJson.put("msg", msg);
						rspJson.put("code", "00");
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
		logger.debug("updAdminSetup end");
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
			if(ToolUtil.isNull(request, "Account")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("帳號不能為空");
				return adminSetupVO;			
			}else {
				adminSetupVO.setAccount(request.getString("Account"));	
			}
			
			
			if(!ToolUtil.isNull(request, "Password")) {
				if(!ToolUtil.lengthCheck(request.optString("Password"), 50)) {
					adminSetupVO.setError(true);
					adminSetupVO.setCode("12");
					adminSetupVO.setDescription("密碼超過長度限制");
					return adminSetupVO;
				}else if(!request.optString("Password").equals(request.optString("PasswordCheck"))) {
					adminSetupVO.setError(true);
					adminSetupVO.setCode("06");
					adminSetupVO.setDescription("密碼與確認密碼不符");
					return adminSetupVO;
				}				
				adminSetupVO.setPassword(EncryptUtil.encryptAES(request.getString("Password"), ToolUtil.getKey()));
			}
			
			if(ToolUtil.isNull(request, "AccountName")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("使用者名稱不能為空");
				return adminSetupVO;
			}else if(!ToolUtil.lengthCheck(request.optString("AccountName"), 50)) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("12");
				adminSetupVO.setDescription("使用者名稱超過長度限制");
				return adminSetupVO;
			}else {
				adminSetupVO.setAccountName(request.optString("AccountName"));	
			}
			
			if(ToolUtil.isNull(request, "BankCode")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("分行不能為空");
				return adminSetupVO;
			}else {
				adminSetupVO.setBankCode(request.optString("BankCode"));
			}				
			
			if(ToolUtil.isNull(request, "Email")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("Email不能為空");
				return adminSetupVO;
			}else if(!ToolUtil.lengthCheck(request.optString("Email"), 30)) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("12");
				adminSetupVO.setDescription("Email超過長度限制");
				return adminSetupVO;
			}else if(request.optString("Email").indexOf("@")==-1) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("12");
				adminSetupVO.setDescription("Email格式不符");
				return adminSetupVO;
			}else {
				adminSetupVO.setEmail(request.optString("Email"));	
			}		
			
			if(ToolUtil.isNull(request, "RankCode")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("權限不能為空");
				return adminSetupVO;
			}else {
				adminSetupVO.setRankCode(request.optString("RankCode"));
			}
			
			if("3".equals(adminSetupVO.getRankCode())||"4".equals(adminSetupVO.getRankCode())) {
				if(ToolUtil.isNull(request, "AreaCodeNo")) {
					adminSetupVO.setError(true);
					adminSetupVO.setCode("15");
					adminSetupVO.setDescription("區域不能為空");
					return adminSetupVO;
				}else {
					adminSetupVO.setAreaCodeNo(request.optString("AreaCodeNo"));
				}
			}
			
			if(ToolUtil.isNull(request, "Suspend")) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("15");
				adminSetupVO.setDescription("停權狀態不能為空");
				return adminSetupVO;
			}else if(!("0".equals(request.optString("Suspend"))||"1".equals(request.optString("Suspend")))) {
				adminSetupVO.setError(true);
				adminSetupVO.setCode("12");
				adminSetupVO.setDescription("停權狀態格式不符");
				return adminSetupVO;
			}else {
				adminSetupVO.setSuspend(request.optString("Suspend"));//0:非停權, 1:停權
			}
			
			adminSetupVO.setUserName(request.optString("UserName"));
			
			AdminSetupVO accountVO = new AdminSetupVO();
			accountVO.setAccount(adminSetupVO.getAccount());
			AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
			List<DynaBean> adminSetupList = adminSetupDAO.getAdminSetup(accountVO);
			if(adminSetupList!=null && !adminSetupList.isEmpty()) {
				DynaBean bean = adminSetupList.get(0);
				if(!ObjectUtils.toString(bean.get("username")).equals(adminSetupVO.getAccountName()) ||
						!ObjectUtils.toString(bean.get("bankcode")).equals(adminSetupVO.getBankCode()) ||
						!ObjectUtils.toString(bean.get("rankcode")).equals(adminSetupVO.getRankCode()) ||
						!ObjectUtils.toString(bean.get("email")).equals(adminSetupVO.getEmail()) ||
						!ObjectUtils.toString(bean.get("suspend")).equals(adminSetupVO.getSuspend()) ||
						!ObjectUtils.toString(bean.get("areacodeno")).equals(adminSetupVO.getAreaCodeNo())
				  ) {
					adminSetupVO.setAddHistory(true);	
				}				
			}
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
