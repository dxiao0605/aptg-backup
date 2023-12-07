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
import org.json.JSONObject;

import aptg.battery.dao.CompanyDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CompanyVO;



/**
 * Servlet implementation class getAlertSetup 告警條件
 */
@WebServlet("/getAlertSetup")
public class getAlertSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getAlertSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAlertSetup() {
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
		logger.debug("getAlertSetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String companyCode = ObjectUtils.toString(request.getHeader("company"));		
			logger.debug("CompanyCode:"+companyCode);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					CompanyVO companyVO = new CompanyVO();
					companyVO.setCompanyCode(companyCode);				
					CompanyDAO companyDAO = new CompanyDAO();
					List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);					
					if (list != null && !list.isEmpty()) {												
						rspJson.put("msg", convertToJson(list));						
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
		logger.debug("getAlertSetup end");		
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
			DynaBean bean = rows.get(0);				
			JSONObject alertSetup = new JSONObject();
			alertSetup.put("CompanyCode", bean.get("companycode"));
			alertSetup.put("IMPType", bean.get("imptype"));
			alertSetup.put("Alert1", ToolUtil.getBigDecimal(bean.get("alert1")));
			alertSetup.put("Alert2", ToolUtil.getBigDecimal(bean.get("alert2")));
			alertSetup.put("Disconnect", ToolUtil.divide(bean.get("disconnect"), 3600, 0));		
			alertSetup.put("Temperature1", ToolUtil.getBigDecimal(bean.get("temperature1")));
			data.put("AlertSetup", alertSetup);
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
