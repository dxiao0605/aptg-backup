package aptg.cathaybkeco.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AreaVO;

/**
 * Servlet implementation class delArea 刪除區域資料
 */
@WebServlet("/delArea")
public class delArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delArea.class.getName());
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delArea() {
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
		logger.debug("delArea start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					AreaVO areaVO = this.parseJson(req);
					if(areaVO.isError()) {
						rspJson.put("code", areaVO.getCode());
						rspJson.put("msg", areaVO.getDescription());
					}else {						
						AreaDAO areaDAO = new AreaDAO();
						if(areaDAO.checkAreaAccount(areaVO.getAreaCodeNo())) {
							rspJson.put("code", "22");
							rspJson.put("msg", "無法刪除此區，請先將區域管理者/使用者移出此區");						
						}else {
							areaDAO.delArea(areaVO);
							ToolUtil.addLogRecord(areaVO.getUserName(), "25", "刪除"+areaVO.getAreaCodeNo()+"區域");
							
							rspJson.put("msg", "Delete Success");
							rspJson.put("code", "00");
						}
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
		logger.debug("delArea end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return adminSetupVO
	 * @throws Exception
	 */
	private AreaVO parseJson(String json) throws Exception {
		AreaVO areaVO = new AreaVO();
		try {
			JSONObject request = new JSONObject(json);

			if(ToolUtil.isNull(request, "AreaCodeNo")) {
				areaVO.setError(true);
				areaVO.setCode("15");
				areaVO.setDescription("區域序號不能為空");
				return areaVO;
			}else {
				areaVO.setAreaCodeNo(request.optString("AreaCodeNo"));	
			}

			areaVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return areaVO;
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
