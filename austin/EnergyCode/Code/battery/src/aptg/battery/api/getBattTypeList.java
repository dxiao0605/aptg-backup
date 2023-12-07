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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.BatteryTypeListDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryTypeListVO;



/**
 * Servlet implementation class getBattTypeList 電池型號下拉選單
 */
@WebServlet("/getBattTypeList")
public class getBattTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBattTypeList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBattTypeList() {
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
			String companyCode = ObjectUtils.toString(request.getParameter("companyCode"));
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {										
					rspJson.put("msg", convertToJson(companyCode));
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
		logger.debug("getBattTypeList rsp: " + rspJson);
		ToolUtil.response(rspJson.toString(), response);
	}
	
	/**
	 * 組Json
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	public JSONObject convertToJson(String companyCode) throws Exception {
		JSONObject data = new JSONObject();
		
		
		JSONArray list = new JSONArray();
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		batteryTypeListVO.setCompanyCode(companyCode);
		BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
		List<DynaBean> rows = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
		if (rows != null && !rows.isEmpty()) {			
			for (DynaBean bean : rows) {
				JSONObject object = new JSONObject();
				object.put("Value", bean.get("batterytypecode"));// 電池型號代碼
				object.put("Label", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號
				list.put(object);
			}
		}
		
		data.put("List", list);
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
