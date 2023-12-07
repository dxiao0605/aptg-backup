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

import aptg.battery.dao.NbListDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class getGroupNBList 站台接續通訊序號
 */
@WebServlet("/getGroupNBList")
public class getGroupNBList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getGroupNBList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getGroupNBList() {
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
		logger.debug("getGroupNBList start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
//			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
//			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String groupInternalId = ObjectUtils.toString(request.getParameter("groupInternalId"));
			String exceptNBID = ObjectUtils.toString(request.getParameter("nbid"));
			logger.debug("GroupInternalId:"+groupInternalId+", ExceptNBID:"+exceptNBID);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					NbListVO nbListVO = new NbListVO();
					nbListVO.setGroupInternalId(groupInternalId);	
					NbListDAO nbListDAO = new NbListDAO();
					List<DynaBean> list = nbListDAO.getGroupNBList(nbListVO);								
										
					rspJson.put("msg", convertToJson(list, exceptNBID, language));
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
		logger.debug("rsp: " + rspJson);		
		ToolUtil.response(rspJson.toString(), response);
		logger.debug("getGroupNBList end");
	}

	/**
	 * 組Json
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String exceptNBID, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		JSONObject data = new JSONObject();	
		try {			
			JSONArray nbList = new JSONArray();
			
			if (rows != null && !rows.isEmpty()) {
				JSONObject nbid = new JSONObject();
				String str;
				nbid.put("Label", resource.getString("1420"));
				nbid.put("Value", "");
				nbList.put(nbid);
				for(DynaBean bean : rows) {
					str = ObjectUtils.toString(bean.get("nbid"));
					if(!exceptNBID.equals(str)) {
						nbid = new JSONObject();
						nbid.put("Label", str);
						nbid.put("Value", str);
						nbList.put(nbid);
					}
				}
			}
			data.put("NBList", nbList);
		}catch (Exception e) {
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
