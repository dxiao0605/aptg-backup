package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

import aptg.battery.dao.CommandTaskDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CommandTaskVO;



/**
 * Servlet implementation class addIRSettingBatch 新增命令任務(內阻設定BB)
 */
@WebServlet("/addIRSettingBatch")
public class addIRSettingBatch extends HttpServlet {
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "cmdtask.topics";
	private static final String PUBLISH_MQTTID = "cmdtask.mqttid";
	private static final String PUBLISH_QOS = "cmdtask.qos";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addIRSettingBatch.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addIRSettingBatch() {
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
		logger.debug("addIRSettingBatch(BB) start");
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
						List<CommandTaskVO> taskList = commandTaskVO.getTaskList();						
						CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
						ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
						String topic = mqttConfig.getString(PUBLISH_TOPIC);
						String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
						String qos = mqttConfig.getString(PUBLISH_QOS);						
						if(taskList!=null && !taskList.isEmpty()) {
							commandTaskDAO.addCommandTaskBatch(taskList);												
							//通知MQTT							
							ExecutorService threadPool = Executors.newFixedThreadPool(20);
							for(int i=0; i<taskList.size(); i++) {
								CommandTaskVO vo = taskList.get(i);
								String payload = convertToJson(vo);								
								process process = new process(topic+vo.getTaskId(), mqttid+(i+1), qos, payload);
								threadPool.execute(process);
							}
							
							threadPool.shutdown();					 
							while (!threadPool.awaitTermination(30000, TimeUnit.SECONDS)) {
								threadPool.shutdownNow();
							}
						}
						
						JSONObject msg = new JSONObject();
						msg.put("Message", resource.getString("5002"));//保存成功
						msg.put("TaskId", commandTaskVO.getTaskIdArr());
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
		logger.debug("addIRSettingBatch(BB) end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @return
	 */
	private CommandTaskVO parseJson(String json, ResourceBundle resource) throws Exception {
		CommandTaskVO commandTaskVO = new CommandTaskVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONArray arr = request.optJSONArray("BatteryGroupID");
        	for(int i=0; i<arr.length(); i++) {
        		String batteryGroupID = arr.optString(i);
				//電池狀態檢核
				String status = ToolUtil.getBatteryStatus(batteryGroupID.split("_")[0], batteryGroupID.split("_")[1]);
				if("4".equals(status)) {
					commandTaskVO.setError(true);
					commandTaskVO.setCode("26");
					commandTaskVO.setDescription(batteryGroupID+resource.getString("1571"));//電池為離線狀態，請待電池恢復連線後再下指令!
					return commandTaskVO;
				}
        	}
						
			String commandId = request.optString("CommandID");			
			JSONObject config = new JSONObject();
			if(ToolUtil.isNull(request, "IRTestTime")||
					ToolUtil.isNull(request, "BatteryCapacity")||
					ToolUtil.isNull(request, "CorrectionValue")||
					ToolUtil.isNull(request, "Resistance")) {
				commandTaskVO.setError(true);
				commandTaskVO.setCode("24");
				commandTaskVO.setDescription(resource.getString("5008"));//必填欄位不能為空
				return commandTaskVO;
			}
			
			int irTestTime = request.optInt("IRTestTime");
			int batteryCapacity = request.optInt("BatteryCapacity");
			int correctionValue = request.optInt("CorrectionValue");
			int resistance = new BigDecimal(String.valueOf(request.optDouble("Resistance")*10)).intValue();
			if((irTestTime<5||irTestTime>200)||
					(batteryCapacity<5||batteryCapacity>250)||
					(correctionValue<10||correctionValue>10000)||
					(resistance<15||resistance>10000)) {
				commandTaskVO.setError(true);
				commandTaskVO.setCode("26");
				commandTaskVO.setDescription(resource.getString("5033"));//值域錯誤
				return commandTaskVO;
			}			
			
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
			String userName = request.optString("UserName");
			
			List<CommandTaskVO> taskList = new ArrayList<CommandTaskVO>();		
			JSONArray taskIdArr = new JSONArray();
			if(!ToolUtil.isNull(request, "BatteryGroupID")) {//電池組ID	
//				Object listArray = new JSONTokener(request.optString("BatteryGroupID")).nextValue();
//	            if (listArray instanceof JSONArray){
//	            	JSONArray arr = request.optJSONArray("BatteryGroupID");
//	            	for(int i=0; i<arr.length(); i++) {
//	            		CommandTaskVO vo = new CommandTaskVO();
//	            		String batteryGroupID = arr.optString(i);
//	            		String taskid = commandId+StringUtils.leftPad(ToolUtil.getSequence(commandId)+"",8,"0");
//						vo.setTaskId(taskid);
//						vo.setNbId(batteryGroupID.split("_")[0]);
//						vo.setBatteryId(batteryGroupID.split("_")[1]);
//						vo.setCommandId(commandId);									
//						vo.setConfig(config.toString());
//						vo.setHexConfig(StringUtils.upperCase(hexConfig));			
//						vo.setUserName(userName);
//						taskIdArr.put(taskid);
//						taskList.add(vo);
//	            	}	
//	            }else if (listArray instanceof JSONObject) {
//	            	CommandTaskVO vo = new CommandTaskVO();
//            		String batteryGroupID = request.optString("BatteryGroupID");
//            		String taskid = commandId+StringUtils.leftPad(ToolUtil.getSequence(commandId)+"",8,"0");
//					vo.setTaskId(taskid);
//					vo.setNbId(batteryGroupID.split("_")[0]);
//					vo.setBatteryId(batteryGroupID.split("_")[1]);
//					vo.setCommandId(commandId);									
//					vo.setConfig(config.toString());
//					vo.setHexConfig(StringUtils.upperCase(hexConfig));			
//					vo.setUserName(userName);
//					taskIdArr.put(taskid);
//					taskList.add(vo);	
//	            }            	            	
            	for(int i=0; i<arr.length(); i++) {
            		CommandTaskVO vo = new CommandTaskVO();
            		String batteryGroupID = arr.optString(i);
            		String taskid = commandId+StringUtils.leftPad(ToolUtil.getSequence(commandId)+"",8,"0");
					vo.setTaskId(taskid);
					vo.setNbId(batteryGroupID.split("_")[0]);
					vo.setBatteryId(batteryGroupID.split("_")[1]);
					vo.setCommandId(commandId);									
					vo.setConfig(config.toString());
					vo.setHexConfig(StringUtils.upperCase(hexConfig));			
					vo.setUserName(userName);
					taskIdArr.put(taskid);
					taskList.add(vo);
            	}
			}
			commandTaskVO.setTaskList(taskList);
			commandTaskVO.setTaskIdArr(taskIdArr);
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
	
	public static class process implements Runnable {
		private String topic;
		private String mqttid;
		private String qos;
		private String payload;

		public process(String topic, String mqttid, String qos, String payload) {
			this.topic = topic;
			this.mqttid = mqttid;
			this.qos = qos;
			this.payload = payload;
		}

		public void run() {
			try {
				logger.debug("Topic:"+ topic + ",MQTT ID:" + mqttid + ",Qos:" + qos + ",Payload:" + payload);
				MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
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
