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

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class updGroupSetup 站台設定
 */
@WebServlet("/updGroupSetup")
public class updGroupSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updGroupSetup.class.getName()); 
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "nbcompany.topics";
	private static final String PUBLISH_MQTTID = "nbcompany.mqttid";
	private static final String PUBLISH_QOS = "nbcompany.qos";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updGroupSetup() {
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
		logger.debug("updBatteryGroup start");
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
					NbListVO nbListVO = this.parseJson(req, resource);
					if(nbListVO.isError()) {
						rspJson.put("code", nbListVO.getCode());
						rspJson.put("msg", nbListVO.getDescription());
					}else {
						NbListDAO nbListDAO = new NbListDAO();
						nbListDAO.updGroupSetup(nbListVO);
						
						//通知MQTT
						ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
						String topic = mqttConfig.getString(PUBLISH_TOPIC);
						String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
						String qos = mqttConfig.getString(PUBLISH_QOS);
						String payload = nbListVO.getJson().toString();
						logger.debug("MQTT req:"+ payload);
						MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5036"));//保存成功	
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
		logger.debug("updGroupSetup end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private NbListVO parseJson(String json, ResourceBundle resource) throws Exception {
		NbListVO nbListVO = new NbListVO();
		JSONArray nbJson = new JSONArray();
		try {
			JSONObject request = new JSONObject(json);
			if(ToolUtil.isNull(request, "GroupInternalID")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("5008"));//必填欄位不能為空
			}else {
				nbListVO.setGroupInternalId(request.optString("GroupInternalID"));
			}
			
			if(ToolUtil.isNull(request, "NBID")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("1057")+resource.getString("5008"));//通訊序號必填欄位不能為空
			}else {
				nbListVO.setNbId(request.optString("NBID"));
			}
			
			if(!ToolUtil.isNull(request, "PreviousNBID")) {
				String previousNBID = request.optString("PreviousNBID");
				NbListDAO nbListDAO = new NbListDAO();
				List<DynaBean> list = nbListDAO.getContinuousSeqNo(previousNBID);				
				if (list != null && !list.isEmpty()) {
					DynaBean bean = list.get(0);
					nbListVO.setContinuousSeqNo(ObjectUtils.toString(bean.get("continuousseqno")));
					nbListVO.setPreviousNBID(previousNBID);
					
					List<DynaBean> defaultGroup = nbListDAO.getNBIDDefaultGroup(previousNBID);				
					if (defaultGroup != null && !defaultGroup.isEmpty()) {
						DynaBean defaultbean = defaultGroup.get(0);
						nbListVO.setDefaultGroupInternalId(ObjectUtils.toString(defaultbean.get("seqno")));
						
						JSONObject nb = new JSONObject();
						nb.put("NBID", previousNBID);
			        	nb.put("CompanyCode", ToolUtil.parseInt(defaultbean.get("companycode")));
			        	nb.put("CompanyName", ObjectUtils.toString(defaultbean.get("companyname")));
			        	nb.put("Active", 0);
			        	nb.put("GroupInternalID", ToolUtil.parseInt(defaultbean.get("seqno")));
			        	nb.put("DefaultGroup", 0);//預設站台
			        	nbJson.put(nb);	
					}
				}				
			}
			
			//查詢站台公司資訊
			BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();			
			List<DynaBean> list = batteryGroupDAO.getGroupCompanyInfo(nbListVO.getGroupInternalId());		
			if (list != null && !list.isEmpty()) {
				DynaBean bean = list.get(0);
				JSONObject nb = new JSONObject();
				nb.put("NBID", nbListVO.getNbId());
				nb.put("CompanyCode", ToolUtil.parseInt(bean.get("companycode")));
	        	nb.put("CompanyName", ObjectUtils.toString(bean.get("companyname")));
	        	nb.put("Active", 0);
	        	nb.put("GroupInternalID", Integer.parseInt(nbListVO.getGroupInternalId()));
	        	nb.put("DefaultGroup", ToolUtil.parseInt(bean.get("defaultgroup")));
	        	nbJson.put(nb);
			}	
        	
			nbListVO.setJson(nbJson);
			nbListVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return nbListVO;
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
