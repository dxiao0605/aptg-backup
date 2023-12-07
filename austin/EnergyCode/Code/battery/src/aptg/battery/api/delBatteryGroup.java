package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import aptg.battery.dao.EventDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;
import aptg.battery.vo.EventVO;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class delBatteryGroup 刪除站台
 */
@WebServlet("/delBatteryGroup")
public class delBatteryGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(delBatteryGroup.class.getName()); 

	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "nbcompany.topics";
	private static final String PUBLISH_MQTTID = "nbcompany.mqttid";
	private static final String PUBLISH_QOS = "nbcompany.qos";
	
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
					List<String> eventGroupIDList = new ArrayList<>();	// add by Austin
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
							vo.setNbId(bean.get("nbid").toString()); // add by Austin
							vo.setCompanyCode(companyCode);	// add by Austin
							dataList.add(vo);
							
							// add by Austin
							if (!eventGroupIDList.contains(ObjectUtils.toString(bean.get("groupid"))))
								eventGroupIDList.add(ObjectUtils.toString(bean.get("groupid")));	// add by Austin (2021/12/02)
						}
					}
					batteryGroupVO.setDataList(dataList);
					batteryGroupVO.setCompanyList(companyList);
					batteryGroupDAO.delBatteryGroup(batteryGroupVO);

					/*
					 * =============== Start ===============
					 * add by Austin (2021/12/02)
					 * 
					 * update delete Group's all Event to Close
					 */
					
					// --- 關閉刪除站台的所有Event
					EventDAO eventDAO = new EventDAO();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (String groupID: eventGroupIDList) {
						EventVO eventVO = new EventVO();
						eventVO.setCloseTime(sdf.format(new Date()));
						eventVO.setCloseContent("Due to the group of this Comm. ID is changed, system resolves this alert automatically.");
						eventVO.setUserName(batteryGroupVO.getUserName());
						eventVO.setGroupIdArr(groupID);
						
						eventDAO.closeEventByGroupID(eventVO);
					}

					// --- 通知MQTT
					ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
					String topic = mqttConfig.getString(PUBLISH_TOPIC);
					String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
					String qos = mqttConfig.getString(PUBLISH_QOS);

					NbListVO nbListVO = new NbListVO();
					JSONArray nbJson = new JSONArray();
					for (BatteryGroupVO vo: batteryGroupVO.getDataList()) {
						JSONObject nb = new JSONObject();
						nb.put("NBID", vo.getNbId());
			        	nb.put("CompanyCode", Integer.parseInt(vo.getCompanyCode()));
//			        	nb.put("CompanyName", companyName);
			        	nb.put("Active", 0);
			        	nb.put("GroupInternalID", Integer.parseInt(vo.getDefaultInternalId()));
			        	nb.put("DefaultGroup", 0);//預設站台
			        	nbJson.put(nb);
					}
					nbListVO.setJson(nbJson);
					
					String payload = nbListVO.getJson().toString();
					logger.debug("Send MQTT: " + payload);
					MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
					
					/*
					 * add by Austin (2021/12/02)
					 * =============== End ===============
					 */
					
											
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
