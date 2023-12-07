package aptg.battery.api;

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

import aptg.battery.dao.RoleDAO;
import aptg.battery.util.ListUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.RoleVO;



/**
 * Servlet implementation class getUserList 使用者頁面下拉選單
 */
@WebServlet("/getUserList")
public class getUserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getUserList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getUserList() {
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
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");					
				} else {
					String roleRank = "";
					RoleVO roleVO = new RoleVO();
					roleVO.setAccount(account);
					RoleDAO roleDAO = new RoleDAO();
					List<DynaBean> list = roleDAO.getRoleRank(roleVO);
					if (list != null && !list.isEmpty()) {
						roleRank = ObjectUtils.toString(list.get(0).get("rolerank"));
					} 					
					JSONObject data = new JSONObject();
					data.put("CompanyList", ListUtil.getCompanyList(userCompany));//公司選單
					data.put("RoleList", ListUtil.getRoleList(roleRank));//角色選單
					data.put("LanguageList", ListUtil.getLanguageList());//語系選單
					data.put("TimeZoneList", ListUtil.getTimeZoneList());//時區選單
					rspJson.put("msg", data);															
					rspJson.put("code", "00");						
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
		ToolUtil.response(rspJson.toString(), response);
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
