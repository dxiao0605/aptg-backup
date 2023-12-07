package aptg.battery.api;

import java.io.IOException;
import java.util.ArrayList;
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

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;



/**
 * Servlet implementation class delBatteryGroup 刪除站台
 */
@WebServlet("/delBatteryGroup")
public class delBatteryGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delBatteryGroup.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public delBatteryGroup() {
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
		logger.debug("delBatteryGroup start");
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
					BatteryGroupVO batteryGroupVO = this.parseJson(req);
					
					BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
					List<BatteryGroupVO> dataList = new ArrayList<BatteryGroupVO>();
					List<String> companyList = new ArrayList<String>();
					List<DynaBean> list = batteryGroupDAO.getDefaultGroup(batteryGroupVO);
					String companyCode;
					if (list != null && !list.isEmpty()) {
						for(DynaBean bean : list) {
							BatteryGroupVO vo = new BatteryGroupVO();
							vo.setGroupInternalId(bean.get("id").toString());
							vo.setDefaultInternalId(bean.get("defaultid").toString());
							companyCode = ObjectUtils.toString(bean.get("companycode"));
							if(!companyList.contains(companyCode)) {
								companyList.add(companyCode);
							}
							dataList.add(vo);
						}
					}
					batteryGroupVO.setDataList(dataList);
					batteryGroupVO.setCompanyList(companyList);
					batteryGroupDAO.delBatteryGroup(batteryGroupVO);
											
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
		logger.debug("delBatteryGroup end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @return batteryGroupVO
	 * @throws Exception
	 */
	private BatteryGroupVO parseJson(String json) throws Exception {
		BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
		try {			
			String groupInternalId = "";
			JSONObject request = new JSONObject(json);
			JSONArray arr = request.getJSONArray("GroupInternalID");
			for(int i=0; i<arr.length(); i++) {
				String id = arr.optString(i);
				groupInternalId += ((i==0 ? "":",")+ "'"+id+"'");
			}
			
			batteryGroupVO.setGroupInternalIdArr(groupInternalId);
			batteryGroupVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryGroupVO;
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
