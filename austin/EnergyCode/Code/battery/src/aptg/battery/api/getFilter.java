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

import aptg.battery.dao.FilterDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.FilterVO;



/**
 * Servlet implementation class getFilter 篩選清單
 */
@WebServlet("/getFilter")
public class getFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getFilter.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getFilter() {
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
		logger.debug("getFilter start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			String functionId = ObjectUtils.toString(request.getParameter("functionId"));
			logger.debug("Account:"+account+",FunctionId:"+functionId);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					FilterVO filterVO = new FilterVO();
					filterVO.setAccount(account);
					filterVO.setFunctionId(functionId);
					FilterDAO filterDAO = new FilterDAO();
					List<DynaBean> list = filterDAO.getFilter(filterVO);					
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
		logger.debug("getFilter end");
	}

	/**
	 * 組Json
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {	
			JSONArray filterArr = new JSONArray();
			for(DynaBean bean : rows) {							
				JSONObject filter = new JSONObject();
				filter.put("FilterID", bean.get("seqno"));
				filter.put("FilterName", bean.get("filtername"));
				filter.put("FilterConfig", bean.get("filterconfig")!=null?new JSONObject(ObjectUtils.toString(bean.get("filterconfig"))):"");

				filterArr.put(filter);
			}			
			data.put("Filter", filterArr);
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
