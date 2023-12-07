package aptg.handle.cmd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.battery.BatteryPublish;
import aptg.bean.CMDTaskBean;
import aptg.bean.PublishBean;
import aptg.model.BatteryModel;
import aptg.model.CommandModel;
import aptg.model.NBListModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class WebCMDTaskHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(WebCMDTaskHandle.class.getName());

	private static final int STATUS_EXECUTING = 2;
	
	private static final String REPLACE_STR_NBID 			= "{NBID}";
	private static final String REPLACE_STR_BATTERYID 		= "{BatteryID}";
	private static final String REPLACE_STR_TRANSACTIONID 	= "{TransactionID}";
	private static final String REPLACE_STR_COMMANDCODE 	= "{CommandCode}";

	private String topic;
	MqttMessage message;
	private int qos;
	private long threadid;
	
	public WebCMDTaskHandle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	    this.threadid = Thread.currentThread().getId();
	}

	@Override
	public void run() {
		String payload = new String(message.getPayload());
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+payload);
		
        CMDTaskBean task = (CMDTaskBean) JsonUtil.getInstance().convertStringToObject(payload, CMDTaskBean.class);
    	String commandID = task.getCommandID();

    	List<CommandModel> commandList = new ArrayList<>();
    	List<PublishBean> publishList = new ArrayList<>();
    	if (commandID.equals("BB")) {
    		cmdBB(task, commandList, publishList);	// 內阻設定測試值
    	}
    	else if (commandID.equals("BA")) {
    		cmdBA(task, commandList, publishList);	// 時間週期設定
    	}
    	else if (commandID.equals("B5")) {
    		cmdB5(task, commandList, publishList);	// 校正電壓
    	}
    	else if (commandID.equals("B3")) {
    		cmdB3(task, commandList, publishList);	// 校正內阻
    	}
    	
    	// insert Command List
    	DBQueryUtil.getInstance().insertCommand(commandList);
    	
    	// publish Command
    	for (PublishBean publish: publishList) {
    		String uuid = BatteryPublish.getInstance().publishMessage(publish);
    	}
	}
	
	/**
	 * 內阻設定測試值
	 * 
	 * @param task
	 */
	private void cmdBB(CMDTaskBean task, List<CommandModel> commandList, List<PublishBean> publishList) {
        Integer groupInternalID = task.getGroupInternalID();	// must have value
    	String nbID = task.getNBID();
//    	Integer batteryID = task.getBatteryID();	// must be '0'

    	if (nbID!=null) {
    		// groupInternalID!=null && nbID!=null && batteryID=0	=> specify battery
			cmdProcedure(task, commandList, publishList);
    		
    	} else {
    		// groupInternalID!=null && nbID==null && batteryID=0	=> all nbID's batteryID=0
    		List<NBListModel> nbList = DBQueryUtil.getInstance().queryNBIDByGroupInternalID(groupInternalID);
    		for (NBListModel model: nbList) {
    			task.setNBID(model.getNbID());

    			cmdProcedure(task, commandList, publishList);
    		}
    	}
	}
	
	/**
	 * 時間週期設定
	 * 
	 * @param task
	 */
	private void cmdBA(CMDTaskBean task, List<CommandModel> commandList, List<PublishBean> publishList) {
        Integer groupInternalID = task.getGroupInternalID();	// must have value
    	String nbID = task.getNBID();
//    	Integer batteryID = task.getBatteryID();	// must be '0'

    	if (nbID!=null) {
    		// groupInternalID!=null && nbID!=null && batteryID=0	=> specify battery
			cmdProcedure(task, commandList, publishList);
    		
    	} else {
    		// groupInternalID!=null && nbID==null && batteryID=0	=> all nbID's batteryID=0
    		List<NBListModel> nbList = DBQueryUtil.getInstance().queryNBIDByGroupInternalID(groupInternalID);
    		for (NBListModel model: nbList) {
    			task.setNBID(model.getNbID());

    			cmdProcedure(task, commandList, publishList);
    		}
    	}
	}
	
	/**
	 * 校正電壓
	 * 
	 * @param task
	 */
	private void cmdB5(CMDTaskBean task, List<CommandModel> commandList, List<PublishBean> publishList) {
        Integer groupInternalID = task.getGroupInternalID();	// must have value
    	String nbID = task.getNBID();
    	Integer batteryID = task.getBatteryID();	// if batteryID=null => all battery

    	if (nbID!=null) {
    		if (batteryID!=null) {
    			// specify battery
    			cmdProcedure(task, commandList, publishList);
    			
    		} else {
    			// all battery
    			List<BatteryModel> batteyList = DBQueryUtil.getInstance().queryBatteryByNBID(nbID);
    			for (BatteryModel model: batteyList) {
    				task.setBatteryID(model.getBatteryID());

        			cmdProcedure(task, commandList, publishList);
    			}
    		}
    	} else {
    		// all nbID && all battery
    		List<BatteryModel> batteyList = DBQueryUtil.getInstance().queryNBListJoinBattery(groupInternalID);
    		for (BatteryModel model: batteyList) {
    			task.setNBID(model.getNbID());
    			task.setBatteryID(model.getBatteryID());

    			cmdProcedure(task, commandList, publishList);
    		}
    	}
	}
	
	/**
	 * 校正內阻
	 * 
	 * @param task
	 */
	private void cmdB3(CMDTaskBean task, List<CommandModel> commandList, List<PublishBean> publishList) {
        Integer groupInternalID = task.getGroupInternalID();	// must have value
    	String nbID = task.getNBID();
    	Integer batteryID = task.getBatteryID();	// if batteryID=null => all battery

    	if (nbID!=null) {
    		if (batteryID!=null) {
    			// specify battery
    			cmdProcedure(task, commandList, publishList);
    			
    		} else {
    			// all battery
    			List<BatteryModel> batteyList = DBQueryUtil.getInstance().queryBatteryByNBID(nbID);
    			for (BatteryModel model: batteyList) {
    				task.setBatteryID(model.getBatteryID());

    				cmdProcedure(task, commandList, publishList);
    			}
    		}
    	} else {
    		// all nbID && all battery
    		List<BatteryModel> batteyList = DBQueryUtil.getInstance().queryNBListJoinBattery(groupInternalID);
    		for (BatteryModel model: batteyList) {
    			task.setNBID(model.getNbID());
    			task.setBatteryID(model.getBatteryID());

    			cmdProcedure(task, commandList, publishList);
    		}
    	}
	}
	
	/**
	 * 下行命令流程: 1. 更新CommandTask TaskStatus=1 & ReqTime
	 * 				2. MQTT下發命令
	 * 				3. 準備要insert至Command的內容
	 * 
	 * @param task
	 * @return
	 */
	private void cmdProcedure(CMDTaskBean task, List<CommandModel> commandList, List<PublishBean> publishList) {
        String taskID = task.getTaskID();
        Integer groupInternalID = task.getGroupInternalID();
    	String nbID = task.getNBID();
    	Integer batteryID = task.getBatteryID();
    	String commandID = task.getCommandID();
    	String hexConfig = task.getHexConfig();
    	
		String transactionID = getTransactionID();
		String topic = getTopic(nbID, batteryID, transactionID);
		String mqttid = getMqttID(commandID, taskID);
		String payload = getPayload(commandID, batteryID, hexConfig);
		
		// update CommandTask TaskStatus & ReqTime
		DBQueryUtil.getInstance().updateCommandTask(taskID, STATUS_EXECUTING, new Date());	// server收到task任務的時間(server時間)

		// get CommandModel
		CommandModel command = getCommand(taskID, nbID, batteryID, transactionID);
		commandList.add(command);
		logger.info("mqttid: "+mqttid+", Command: "+JsonUtil.getInstance().convertObjectToJsonstring(command));
		
		// publish
		PublishBean publish = getPublish(mqttid, topic, payload);
		publishList.add(publish);
	}
	
	/**
	 * 
	 * @return
	 */
	private String getTransactionID() {
        String transactionID = ToolUtil.getInstance().getTrimUUID();
        return transactionID;
	}
	
	/**
	 * topic = batt/v0/CMD/{NBID}/{BatteryID}/{TransactionID}
	 * 
	 * @param nbID
	 * @param batteryID
	 * @return
	 */
	private String getTopic(String nbID, int batteryID, String transactionID) {
		String topic = BatteryPublish.getInstance().getTopicTemplate().replace(REPLACE_STR_NBID, nbID)
																	  .replace(REPLACE_STR_BATTERYID, String.valueOf(batteryID))
																	  .replace(REPLACE_STR_TRANSACTIONID, transactionID);
		return topic;
	}

	/**
	 * mqttid = SVR_{CommandCode}_P_threadid
	 * 
	 * @param commandCode
	 * @return
	 */
	private String getMqttID(String commandCode, String taskID) {
		String mqttid = BatteryPublish.getInstance().getMqttidTemplate().replace(REPLACE_STR_COMMANDCODE, commandCode) +"_"+taskID;
		
		return mqttid;
	}
	
	/**
	 * payload = 1. BB&BA: commandCode + hexConfig + crc
	 * 			 2. B3&B5: commandCode + batteryID +hexConfig + crc 
	 * 
	 * @param commandCode
	 * @param hexConfig
	 * @return
	 */
	private String getPayload(String commandCode, int batteryID, String hexConfig) {
		String payload = "";
    	if (commandCode.equals("BB")) {
    		payload = commandCode + hexConfig;	// 內阻設定測試值
    	}
    	else if (commandCode.equals("BA")) {
    		payload = commandCode + hexConfig;	// 時間週期設定
    	}
    	else if (commandCode.equals("B5")) {
    		payload = commandCode + ToolUtil.getInstance().intToHex(batteryID) + hexConfig;	// 校正電壓
    	}
    	else if (commandCode.equals("B3")) {
    		payload = commandCode + ToolUtil.getInstance().intToHex(batteryID) + hexConfig;	// 校正內阻
    	}
    	
		String crc = ToolUtil.getInstance().getPayloadCRC(payload);
		
		payload = payload + crc;
		return payload;
	}
	
	/**
	 * 
	 * @param taskID
	 * @param nbID
	 * @param batteryID
	 * @return
	 */
	private CommandModel getCommand(String taskID, String nbID, int batteryID, String transactionID) {
		CommandModel command = new CommandModel();
		command.setTransactionID(transactionID);
		command.setTaskID(taskID);
		command.setNbID(nbID);
		command.setBatteryID(batteryID);
		command.setPublishTime(ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		command.setAckTime(null);
		command.setResponseTime(null);
		command.setResponseCode(null);
		command.setResponseContent(null);
		return command;
	}
	
	private PublishBean getPublish(String mqttid, String topic, String payload) {
		PublishBean publish = new PublishBean();
		publish.setMqttid(mqttid);
		publish.setTopic(topic);
		publish.setPayload(payload);
		publish.setUuid(UUID.randomUUID().toString());
		return publish;
	}
}
