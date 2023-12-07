package aptg.battery.api;

import java.io.IOException;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.RoleDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.RoleVO;

/**
 * Servlet implementation class getRoleInfo 角色資訊
 */
@WebServlet("/getRoleInfo")
public class getRoleInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getRoleInfo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getRoleInfo() {
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
		logger.debug("getRoleInfo start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("UserCompany:" + userCompany + ",Account: " + account+ ",Type: " + type);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					RoleVO roleVO = new RoleVO();
					roleVO.setSystemId(ToolUtil.getSystemId());
					roleVO.setAccount(account);
					if (ToolUtil.checkAdminCompany(userCompany))
						roleVO.setCompanyCode(userCompany);
					
					//取得角色等級
					RoleDAO roleDAO = new RoleDAO();
					List<DynaBean> roleRank = roleDAO.getRoleRank(roleVO);
					if (roleRank != null && !roleRank.isEmpty()) {
						roleVO.setRoleRank(ObjectUtils.toString(roleRank.get(0).get("rolerank")));
					} 					
					
					List<DynaBean> list = roleDAO.getRoleInfo(roleVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(type)) {
							composeCSV(list, timezone, language, response);
							rep = false;
						} else if ("check".equals(type)) {
							rspJson.put("msg","Roles"+ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
						} else {
							rspJson.put("msg", convertToJson(list));
						}
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));// 查無資料
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
		} finally {
			if (rep) {
				logger.debug("rsp: " + rspJson);
				ToolUtil.response(rspJson.toString(), response);
			}
		}
		logger.debug("getRoleInfo end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			String url = "";
			RoleDAO roleDAO = new RoleDAO();
			List<DynaBean> roleRank = roleDAO.getProgramUrl("10");//取得使用者頁面URL
			if (roleRank != null && !roleRank.isEmpty()) {
				url = ObjectUtils.toString(roleRank.get(0).get("url"));
			} 					
			
			JSONArray roleArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject role = new JSONObject();
				role.put("Seq", i + 1);
				String roleid = ObjectUtils.toString(bean.get("roleid"));
				role.put("RoleId", roleid);// 角色ID
				role.put("RoleName", ObjectUtils.toString(bean.get("rolename")));// 角色名稱
				role.put("RoleDesc", ObjectUtils.toString(bean.get("roledesc")));// 角色中文說明				
				role.put("RoleDescE", ObjectUtils.toString(bean.get("roledesce")));//角色英文說明		
				role.put("RoleDescJ", ObjectUtils.toString(bean.get("roledescj")));//角色日文說明
				role.put("Count", ToolUtil.parseInt(bean.get("count")));// 用戶數量
				role.put("UserUrl", url);//使用者頁面URL
				role.put("QueryName", "["+ObjectUtils.toString(bean.get("rolename"))+"]");
				if("0".equals(roleid)) {
					role.put("ShowAauthority", 0);//隱藏權限
				}else {
					role.put("ShowAauthority", 1);//呈顯權限
				}
				roleArr.put(role);
			}
			data.put("Role", roleArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組CSV
	 * 
	 * @param list
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(List<DynaBean> list, String timezone, String language, HttpServletResponse response)
			throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		try {
			StringBuilder str = new StringBuilder();
			str.append(resource.getString("1703")).append(",")// 名稱
			   .append(resource.getString("1704")).append(",")// 角色中文說明
			   .append(resource.getString("1705")).append(",")// 角色英文說明
			   .append(resource.getString("1706")).append(",")// 角色日文說明
			   .append(resource.getString("1723")).append("\n");// 使用者數
			for (DynaBean bean : list) {
				str.append(ObjectUtils.toString(bean.get("rolename"))).append(",")
				   .append(ObjectUtils.toString(bean.get("roledesc"))).append(",")				
				   .append(ObjectUtils.toString(bean.get("roledesce"))).append(",")
				   .append(ObjectUtils.toString(bean.get("roledescj"))).append(",")				
				   .append(ObjectUtils.toString(bean.get("count"))).append("\n");
			}

			CsvUtil.exportCsv(str, "Roles"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv", response);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
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
