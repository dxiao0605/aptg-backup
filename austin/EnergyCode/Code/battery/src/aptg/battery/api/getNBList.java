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
 * Servlet implementation class getNBList 通訊序號
 */
@WebServlet("/getNBList")
public class getNBList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getNBList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getNBList() {
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
		logger.debug("getNBList start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
//			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String companyCode = ObjectUtils.toString(request.getParameter("companyCode"));
			String allocate = ObjectUtils.toString(request.getParameter("allocate"));//16分配,17 未分配
			String active = ObjectUtils.toString(request.getParameter("active"));//13 啟用, 14停用 ,15 已刪除
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));			
			logger.debug("CompanyCode:"+companyCode+",Allocate:"+allocate+",Active:"+active);
			logger.debug("Start:"+start+",End:"+end);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);				
					NbListVO nbListVO = new NbListVO();
					if(StringUtils.isNotBlank(companyCode)) {
						nbListVO.setCompanyCode(companyCode);	
					}else {
						if(ToolUtil.checkAdminCompany(userCompany))
							nbListVO.setCompanyCode(userCompany);
					}
					nbListVO.setAllocate(allocate);
					nbListVO.setActive(active);
					nbListVO.setStart(start);
					nbListVO.setEnd(end);
					NbListDAO nbListDAO = new NbListDAO();
					List<DynaBean> list = nbListDAO.getNBList(nbListVO);					
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
		logger.debug("getNBList end");
	}

	/**
	 * 組Json
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		JSONArray nbList = new JSONArray();
		try {
			for(DynaBean bean : rows) {
				JSONObject nbid = new JSONObject();
				nbid.put("Company", ObjectUtils.toString(bean.get("companyname")));
				nbid.put("NBID", ObjectUtils.toString(bean.get("nbid")));
				nbid.put("DefaultGroup", bean.get("defaultgroup"));//0:預設站台, 1:非預設站台
				nbList.put(nbid);
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
