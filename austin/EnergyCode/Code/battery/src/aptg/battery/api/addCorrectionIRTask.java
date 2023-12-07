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

import aptg.battery.dao.CommandTaskDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CommandTaskVO;



/**
 * Servlet implementation class addCorrectionIRTask 新增命令任務(校正內阻B3)
 */
@WebServlet("/addCorrectionIRTask")
public class addCorrectionIRTask extends HttpServlet {
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "cmdtask.topics";
	private static final String PUBLISH_MQTTID = "cmdtask.mqttid";
	private static final String PUBLISH_QOS = "cmdtask.qos";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addCorrectionIRTask.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addCorrectionIRTask() {
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
		logger.debug("addCorrectionIRTask start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			resource = ToolUtil.getLanguage(language);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					CommandTaskVO commandTaskVO = this.parseJson(req, resource);
					if(commandTaskVO.isError()) {
						rspJson.put("code", commandTaskVO.getCode());
						rspJson.put("msg", commandTaskVO.getDescription());
					}else {
						CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
						commandTaskVO.setCategory("1");//內阻
						List<DynaBean> list = commandTaskDAO.getBatteryDetailCount(commandTaskVO);//查詢內阻設定筆數				
						if (list != null && !list.isEmpty()) {						
							int count = ToolUtil.parseInt(list.get(0).get("count"));
							if(count==0) {
								commandTaskDAO.addBatteryDetail(commandTaskVO);
							}else if(count != commandTaskVO.getRecords()) {//筆數不同則先刪除
								commandTaskDAO.delBatteryDetail(commandTaskVO);
								commandTaskDAO.addBatteryDetail(commandTaskVO);
							}
						}						
						commandTaskVO.setTaskId(commandTaskVO.getCommandId()+StringUtils.leftPad(ToolUtil.getSequence(commandTaskVO.getCommandId())+"",8,"0"));						
						commandTaskDAO.addCommandTask(commandTaskVO);
						
						//通知MQTT
						String payload = convertToJson(commandTaskVO);
						logger.debug("MQTT req:"+ payload);
						ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
						String topic = mqttConfig.getString(PUBLISH_TOPIC)+commandTaskVO.getTaskId();
						String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
						String qos = mqttConfig.getString(PUBLISH_QOS);
						
						MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
						
						JSONArray taskid = new JSONArray();
						taskid.put(commandTaskVO.getTaskId());
						JSONObject msg = new JSONObject();
						msg.put("Message", resource.getString("5002"));//保存成功
						msg.put("TaskId", taskid);
						rspJson.put("msg", msg);
						rspJson.put("code", "00");							
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
		logger.debug("addCorrectionIRTask end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private CommandTaskVO parseJson(String json, ResourceBundle resource) throws Exception {
		CommandTaskVO commandTaskVO = new CommandTaskVO();
		try {
			JSONObject request = new JSONObject(json);
			
			if(ToolUtil.isNull(request, "BatteryGroupID")) {
				commandTaskVO.setError(true);
				commandTaskVO.setCode("24");
				commandTaskVO.setDescription(resource.getString("5008"));//必填欄位不能為空
				return commandTaskVO;
			}
			String batteryGroupID = request.optString("BatteryGroupID");
			commandTaskVO.setNbId(batteryGroupID.split("_")[0]);
			commandTaskVO.setBatteryId(batteryGroupID.split("_")[1]);
			
			//電池組指令限制
			CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
			int b3 = 0;
			List<DynaBean> list = commandTaskDAO.getBattCommandSetup(commandTaskVO);
			if (list != null && !list.isEmpty()) {						
				b3 = ToolUtil.parseInt(list.get(0).get("b3"));//0:不限制, 1:有限制	
			}						
						
			//電池狀態檢核
			String status = ToolUtil.getBatteryStatus(commandTaskVO.getNbId(), commandTaskVO.getBatteryId());
			if("3".equals(status) && b3==1) {
				commandTaskVO.setError(true);
				commandTaskVO.setCode("26");
				commandTaskVO.setDescription(resource.getString("1570"));//電池內阻過高，請更換電池後再進行校正!
				return commandTaskVO;
			}else if("4".equals(status) && !commandTaskVO.getBatteryId().equals("0")) { //20220307 David 選擇母機時不檢查離線
				commandTaskVO.setError(true);
				commandTaskVO.setCode("26");
				commandTaskVO.setDescription(batteryGroupID+resource.getString("1571"));//電池為離線狀態，請待電池恢復連線後再下指令!
				return commandTaskVO;
			}
			commandTaskVO.setCommandId(request.optString("CommandID"));
			
			JSONArray irArr = request.getJSONArray("IR");						
			String hexConfig = StringUtils.leftPad(Integer.toHexString(irArr.length()*2),2,"0");
			for(int i=0; i<irArr.length(); i++) {
				if(irArr.isNull(i)||StringUtils.isBlank(irArr.optString(i))) {
					commandTaskVO.setError(true);
					commandTaskVO.setCode("24");
					commandTaskVO.setDescription(resource.getString("5008"));//必填欄位不能為空
					return commandTaskVO;
				}else if(irArr.optInt(i)<1000||irArr.optInt(i)>60000) {
					commandTaskVO.setError(true);
					commandTaskVO.setCode("26");
					commandTaskVO.setDescription(resource.getString("5033"));//值域錯誤
					return commandTaskVO;
				}
				hexConfig += StringUtils.leftPad(Integer.toHexString(irArr.optInt(i)),4,"0");
			}
			commandTaskVO.setRecords(irArr.length());
			commandTaskVO.setConfig(request.optString("IR"));
			commandTaskVO.setHexConfig(StringUtils.upperCase(hexConfig));			
			commandTaskVO.setUserName(request.optString("UserName"));
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
