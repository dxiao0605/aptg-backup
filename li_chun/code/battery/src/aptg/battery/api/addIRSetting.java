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
import org.json.JSONObject;

import aptg.battery.dao.CommandTaskDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CommandTaskVO;



/**
 * Servlet implementation class addIRSetting 新增命令任務(內阻設定)
 */
@WebServlet("/addIRSetting")
public class addIRSetting extends HttpServlet {
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "cmdtask.topics";
	private static final String PUBLISH_MQTTID = "cmdtask.mqttid";
	private static final String PUBLISH_QOS = "cmdtask.qos";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addIRSetting.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addIRSetting() {
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
		logger.debug("addIRSetting start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
//			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					CommandTaskVO commandTaskVO = this.parseJson(req);
					if(commandTaskVO.isError()) {
						rspJson.put("code", commandTaskVO.getCode());
						rspJson.put("msg", commandTaskVO.getDescription());
					}else {
						commandTaskVO.setTaskId(commandTaskVO.getCommandId()+StringUtils.leftPad(ToolUtil.getSequence(commandTaskVO.getCommandId())+"",8,"0"));
						CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
						commandTaskDAO.addCommandTask(commandTaskVO);
						
						//通知MQTT
						String payload = convertToJson(commandTaskVO);
						logger.debug("MQTT req:"+ payload);
						ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
						String topic = mqttConfig.getString(PUBLISH_TOPIC)+commandTaskVO.getTaskId();
						String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
						String qos = mqttConfig.getString(PUBLISH_QOS);
						
						MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5002"));//保存成功								
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
		logger.debug("addIRSetting end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return bankInfVO
	 * @throws Exception
	 */
	private CommandTaskVO parseJson(String json) throws Exception {
		CommandTaskVO commandTaskVO = new CommandTaskVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			if(!ToolUtil.isNull(msg, "GroupInternalID")) {
				commandTaskVO.setGroupInternalId(msg.optString("GroupInternalID"));	
			}
			if(!ToolUtil.isNull(msg, "NBID")) {
				commandTaskVO.setNbId(msg.optString("NBID"));	
			}
			if(!ToolUtil.isNull(msg, "BatteryID")) {
				commandTaskVO.setBatteryId(msg.optString("BatteryID"));	
			}			
			commandTaskVO.setCommandId(msg.optString("CommandID"));
			
			JSONObject config = new JSONObject();
			int irTestTime = msg.optInt("IRTestTime");
			int batteryCapacity = msg.optInt("BatteryCapacity");
			int correctionValue = msg.optInt("CorrectionValue");
			int resistance = msg.optInt("Resistance");
			config.put("IRTestTime", irTestTime);
			config.put("BatteryCapacity", batteryCapacity);
			config.put("CorrectionValue", correctionValue);
			config.put("Resistance", resistance);
						
			String hexConfig = "06"+
					           StringUtils.leftPad(Integer.toHexString(irTestTime),2,"0")+
					           StringUtils.leftPad(Integer.toHexString(batteryCapacity),2,"0")+
					           StringUtils.leftPad(Integer.toHexString(correctionValue),4,"0")+
					           StringUtils.leftPad(Integer.toHexString(resistance),4,"0");
			commandTaskVO.setConfig(config.toString());
			commandTaskVO.setHexConfig(StringUtils.upperCase(hexConfig));
			
			commandTaskVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return commandTaskVO;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private String convertToJson(CommandTaskVO commandTaskVO) throws Exception {
		JSONObject mqttReq = new JSONObject();
		try {	
			mqttReq.put("TaskID", commandTaskVO.getTaskId());
			if(StringUtils.isNotBlank(commandTaskVO.getGroupInternalId())) {
				mqttReq.put("GroupInternalID", Integer.parseInt(commandTaskVO.getGroupInternalId()));
			}
			mqttReq.put("NBID", commandTaskVO.getNbId());
			if(StringUtils.isNotBlank(commandTaskVO.getBatteryId())) {
				mqttReq.put("BatteryID", Integer.parseInt(commandTaskVO.getBatteryId()));
			}
			mqttReq.put("CommandID", commandTaskVO.getCommandId());
			mqttReq.put("HexConfig", commandTaskVO.getHexConfig());				
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return mqttReq.toString();
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
