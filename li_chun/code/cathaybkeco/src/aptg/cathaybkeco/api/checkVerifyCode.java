package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;


/**
 * Servlet implementation class checkVerifyCode 檢核驗證碼
 */
@WebServlet("/checkVerifyCode")
public class checkVerifyCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(checkVerifyCode.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public checkVerifyCode() {
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
		logger.debug("checkVerifyCode start");
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
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					List<DynaBean> userInfoList = adminSetupDAO.getAdminSetup(adminSetupVO);
					if (userInfoList != null && userInfoList.size() != 0) {
						DynaBean bean = userInfoList.get(0);						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date sysdate = new Date();
						Date expireDate = sdf.parse(ToolUtil.dateFormat(bean.get("expiredate"), sdf));					
						if(sysdate.after(expireDate)) {
							rspJson.put("code", "25");
							rspJson.put("msg", "驗證碼已失效");
						}else {
							rspJson.put("code", "00");
							rspJson.put("msg", "Check Success");
							
						}
					} else {
						rspJson.put("code", "24");
						rspJson.put("msg", "驗證碼錯誤");
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
		logger.debug("checkVerifyCode end");
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
			adminSetupVO.setVerifyCode(request.optString("VerifyCode"));
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
