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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;

/**
 * Servlet implementation class getGroupSetup 站台設定
 */
@WebServlet("/getGroupSetup")
public class getGroupSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getGroupManage.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getGroupSetup() {
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
		logger.debug("getGroupSetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String companyCode = ObjectUtils.toString(request.getParameter("companyCode"));
			logger.debug("CompanyCode:" + companyCode);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
					batteryGroupVO.setCompanyCode(companyCode);
					BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
					List<DynaBean> list = batteryGroupDAO.getGroupManage(batteryGroupVO);
					if (list != null && !list.isEmpty()) {						
						rspJson.put("msg", convertToJson(list));						
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
		} 
		logger.debug("rsp: " + rspJson);
		ToolUtil.response(rspJson.toString(), response);
		logger.debug("getGroupSetup end");
	}

	

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @param sdf
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray groupArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject group = new JSONObject();
				group.put("Seq", i + 1);
				group.put("GroupInternalID", ObjectUtils.toString(bean.get("seqno")));
				group.put("Country", ObjectUtils.toString(bean.get("country")));// 國家
				group.put("Area", ObjectUtils.toString(bean.get("area")));// 地域
				group.put("GroupID", ObjectUtils.toString(bean.get("groupid")));// 站台號碼
				group.put("GroupName", ObjectUtils.toString(bean.get("groupname")));// 站台名稱
				group.put("Address", ObjectUtils.toString(bean.get("address")));// 地址
				group.put("DefaultGroup", bean.get("defaultgroup"));// 是否為預設站台(0:預設站台, 1:非預設站台)
				groupArr.put(group);
			}
			data.put("Group", groupArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
