package aptg.battery.api;

import java.io.File;
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
 * Servlet implementation class delUser 刪除Logo
 */
@WebServlet("/delLogo")
public class delLogo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delLogo.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delLogo() {
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
		logger.debug("delLogo start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {				
					CompanyVO companyVO = this.parseJson(req, resource);
					if (companyVO.isError()) {
						rspJson.put("msg", companyVO.getDescription());
						rspJson.put("code", companyVO.getCode());
					} else {
						CompanyDAO companyDAO = new CompanyDAO();
						List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);
						if (list != null && !list.isEmpty()) {
							//刪除舊的圖檔
							DynaBean bean = list.get(0);
							if(bean.get("logopath")!=null) {
								File image = new File(bean.get("logopath").toString());
								if(image!=null && image.exists())
									image.delete();
							}							
						}
						
						companyDAO.delLogoPath(companyVO);						
						rspJson.put("msg", resource.getString("5055"));//取消成功
						rspJson.put("code", "00");
					}															
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {			
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5006"));//刪除失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("delLogo end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private CompanyVO parseJson(String json, ResourceBundle resource) throws Exception {
		CompanyVO companyVO = new CompanyVO();
		try {
			JSONObject request = new JSONObject(json);		
			if(ToolUtil.isNull(request, "Company")) {
				companyVO.setError(true);
				companyVO.setCode("24");
				companyVO.setDescription(resource.getString("1064")+resource.getString("5008"));//公司不能為空
				return companyVO;
			}else {
				companyVO.setCompanyCode(request.optString("Company"));	
			}	
	
			companyVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return companyVO;
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
