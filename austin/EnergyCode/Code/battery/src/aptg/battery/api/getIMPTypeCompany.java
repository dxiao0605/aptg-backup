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

import aptg.battery.dao.CompanyDAO;
import aptg.battery.util.ListUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CompanyVO;



/**
 * Servlet implementation class getIMPTypeCompany 內阻呈現公司下拉選單
 */
@WebServlet("/getIMPTypeCompany")
public class getIMPTypeCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getIMPTypeCompany.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getIMPTypeCompany() {
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
		logger.debug("getIMPTypeCompany start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));			
			logger.debug("UserCompany:"+userCompany);		
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					CompanyVO companyVO = new CompanyVO();
					if(ToolUtil.checkAdminCompany(userCompany))
						companyVO.setCompanyCode(userCompany);
					CompanyDAO companyDAO = new CompanyDAO();
					List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);					
					if (list != null && !list.isEmpty()) {
						JSONObject data = convertToJson(list);
						data.put("Company", ListUtil.getCompanyList(userCompany, ""));
						data.put("IMPType", ListUtil.getIMPTypeList(language));
						rspJson.put("msg", data);						
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));//查無資料
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
		logger.debug("getIMPTypeCompany end");		
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			
			JSONArray list = new JSONArray();			
			for(DynaBean bean : rows) {
				JSONObject company = new JSONObject();
				company.put("CompanyCode", bean.get("companycode"));
				company.put("IMPType", bean.get("imptype"));
				list.put(company);
			}
			data.put("Mapping", list);			
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
