package aptg.cathaybkeco.api;

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

import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getArea 區域資料
 */
@WebServlet("/getArea")
public class getArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getArea.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getArea() {
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
		logger.debug("getArea start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));			
			logger.debug("token: " + token);			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					AreaDAO areaDAO = new AreaDAO();
					List<DynaBean> list = areaDAO.getAreaList();
					if (list != null && !list.isEmpty()) {						
						rspJson.put("msg", convertToJson(list));																	
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
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
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
		logger.debug("getArea end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		AreaDAO areaDAO = new AreaDAO();
		JSONObject data = new JSONObject();
		try {
			JSONArray areaArr = new JSONArray();
			for(int i=0; i<rows.size(); i++) {
				JSONObject area = new JSONObject();
				DynaBean bean = rows.get(i);

				String areaCodeNo = ObjectUtils.toString(bean.get("seqno"));
				area.put("AreaCodeNo", areaCodeNo);
				area.put("AreaCode", ObjectUtils.toString(bean.get("areacode")));
				area.put("AreaName", ObjectUtils.toString(bean.get("areaname")));
				
				JSONArray bankArr = new JSONArray();
				List<DynaBean> banklist = areaDAO.getAccessBanks(areaCodeNo);
				if (banklist != null && !banklist.isEmpty()) {
					for(DynaBean b:banklist) {
					JSONObject bank = new JSONObject();
					bank.put("BankCode", ObjectUtils.toString(b.get("bankcode")));
					bank.put("BankName", ObjectUtils.toString(b.get("bankname")));
					bankArr.put(bank);
					}
				}							
				area.put("AccessBanks", bankArr);
								
				JSONArray areaManager = new JSONArray();
				JSONArray areaUser = new JSONArray();
				List<DynaBean> accountList = areaDAO.getAreaAccount(areaCodeNo);
				if (accountList != null && !accountList.isEmpty()) {
					for(DynaBean a:accountList) {
					int rankCode = ToolUtil.parseInt(a.get("rankcode"));
						if(rankCode==3) {
							areaManager.put(a.get("account"));
						}else if(rankCode==4) {
							areaUser.put(a.get("account"));
						}
					}
				}	
				area.put("AreaManager", areaManager);
				area.put("AreaUser", areaUser);
								
				areaArr.put(area);
			}
			data.put("List", areaArr);
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
