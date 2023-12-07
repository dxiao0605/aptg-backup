package aptg.battery.api;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Servlet implementation class delBatteryType 刪除電池型號
 */
@WebServlet("/delBatteryType")
public class delBatteryType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delBatteryType.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delBatteryType() {
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
		logger.debug("delBatteryType start");
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
					BatteryTypeListVO batteryTypeListVO = this.parseJson(req);
					
					BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
					batteryTypeListDAO.delBatteryType(batteryTypeListVO);
											
					rspJson.put("code", "00");				
					rspJson.put("msg", resource.getString("5005"));//刪除成功																	
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
		logger.debug("delBatteryType end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @return batteryTypeListVO
	 * @throws Exception
	 */
	private BatteryTypeListVO parseJson(String json) throws Exception {
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		try {
			String batteryTypeCode = "";
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			JSONArray batteryTypeCodeArr = msg.getJSONArray("BatteryTypeCode");
			for(int i=0; i<batteryTypeCodeArr.length(); i++) {
				batteryTypeCode += ((i==0 ? "":",")+ "'"+batteryTypeCodeArr.optString(i)+"'");				
			}
			
			batteryTypeListVO.setBatteryTypeCodeArr(batteryTypeCode);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryTypeListVO;
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
