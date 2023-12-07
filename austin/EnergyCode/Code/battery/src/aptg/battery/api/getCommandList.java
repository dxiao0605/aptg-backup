package aptg.battery.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
 * Servlet implementation class getCommandList 下行命令POC
 */
@WebServlet("/getCommandList")
public class getCommandList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCommandList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCommandList() {
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
		logger.debug("getCommandList start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String companyCode = ObjectUtils.toString(request.getParameter("companyCode"));
			//2022 David 增加command 參數
			String commandId = ObjectUtils.toString(request.getParameter("commandId"));
			
			
			logger.debug("UserCompany:" + userCompany+ ",CompanyCode:"+companyCode+ ",commandId:"+commandId);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
					batteryGroupVO.setCompanyCodeArr(ToolUtil.strToSqlStr(companyCode));
					BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
					//2022 David 增加command 參數
					List<DynaBean> list = batteryGroupDAO.getCommandList(batteryGroupVO,commandId);				
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
		logger.debug("getCommandList end");
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
			JSONArray arealist = new JSONArray();
			JSONArray grouplist = new JSONArray();
			Map<String, Integer> areaMap = new LinkedHashMap<String, Integer>();
			int areaValue = 0;
			for(DynaBean bean : rows) {
				
				String areaKey = ObjectUtils.toString(bean.get("companycode"))+"@"+ObjectUtils.toString(bean.get("country"))+"/"+ObjectUtils.toString(bean.get("area"));
				JSONObject group = new JSONObject();
				group.put("Value", bean.get("seqno"));
				group.put("Label", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				
				if(areaMap.containsKey(areaKey)) {
					group.put("Area", areaMap.get(areaKey));
				}else {
					JSONObject area = new JSONObject();
					area.put("Value", areaValue);
					area.put("Label", ObjectUtils.toString(bean.get("country"))+"/"+ObjectUtils.toString(bean.get("area")));
					arealist.put(area);
					areaMap.put(areaKey, areaValue);
					group.put("Area", areaValue);
					areaValue++;
				}								
				grouplist.put(group);				
			}
			data.put("AreaList", arealist);
			data.put("GroupList", grouplist);
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
