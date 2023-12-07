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

import aptg.battery.dao.FilterDAO;
import aptg.battery.util.AuthorizationUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.FilterVO;



/**
 * Servlet implementation class saveFilter 保存篩選
 */
@WebServlet("/saveFilter")
public class saveFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(saveFilter.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public saveFilter() {
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
		logger.debug("saveFilter start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					FilterVO filterVO = this.parseJson(req);
					if(filterVO.isError()) {
						rspJson.put("code", filterVO.getCode());
						rspJson.put("msg", resource.getString(filterVO.getDescription()));
					}else {
						FilterDAO filterDAO = new FilterDAO();
						List<DynaBean> list = filterDAO.getFilter(filterVO);					
						if (list != null && !list.isEmpty()) {	
							filterDAO.updFilter(filterVO);
						}else {
							filterDAO.addFilter(filterVO);
						}
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5002"));//保存成功		
					}															
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {			
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5003"));//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("saveFilter end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @return batteryTypeListVO
	 * @throws Exception
	 */
	private FilterVO parseJson(String json) throws Exception {
		FilterVO filterVO = new FilterVO();
		try {
			JSONObject request = new JSONObject(json);
			filterVO.setAccount(request.optString("Account"));
			filterVO.setFunctionId(request.optString("FunctionId"));			
			JSONObject config = new JSONObject(request.optString("FilterConfig"));
			if(config.has("Type")){
				config.remove("Type");
			}
			filterVO.setFilterConfig(config.toString());
			
			if(ToolUtil.isNull(request, "FilterName")) {
				filterVO.setError(true);
				filterVO.setCode("13");
				filterVO.setDescription("5008");//必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("FilterName"), 20)) {
				filterVO.setError(true);
				filterVO.setCode("13");
				filterVO.setDescription("5009");//篩選名稱長度超過20碼
			}else {
				filterVO.setFilterName(request.optString("FilterName"));
			}
			filterVO.setUserName(AuthorizationUtil.getUserName(ToolUtil.getSystemId(), filterVO.getAccount()));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return filterVO;
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
