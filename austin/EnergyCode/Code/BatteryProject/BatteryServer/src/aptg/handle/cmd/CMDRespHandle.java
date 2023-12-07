package aptg.handle.cmd;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.bean.BABean;
import aptg.bean.BBBean;
import aptg.manager.DeviceManager;
import aptg.model.BatteryModel;
import aptg.model.CommandTaskModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

public class CMDRespHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(CMDRespHandle.class.getName());

	private static final String TOPIC_INFO_SPLIT 	= "/";
	private static final String DATA_INFO_SPLIT 	= "_";
	private static final String RESP_SUCCESS		= "0";	// 成功
	private static final String RESP_FAILED			= "1";	// 失敗
	private static final char LF  = (char) 0x0A;	// \n
	
	private String topic;
	private MqttMessage message;
	private int qos;

	public CMDRespHandle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	}
	
	@Override
	public void run() {
		String payload = new String(message.getPayload()).replace(String.valueOf(LF), "");
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+payload);
    	
		// batt/v0/CMDACK/{通訊版序號}/{電池ID}/{交易序號}
		String[] topics = topic.split(TOPIC_INFO_SPLIT);
		String topicNBID = topics[3];
		String topicBatteryID = topics[4];
		String topicTransactionID = topics[5];
		
		String[] resp = payload.split(DATA_INFO_SPLIT);
		String timeStamp = resp[0];
		String timeZone = resp[1];
		String nbID = resp[2];
		String respCode = resp[3];	// 0:成功, 1:失敗
		String respContent = resp[4];	// {電池回應訊息HEX 50 string}

//		Date time = ToolUtil.getInstance().convertTimestampToDate(Long.valueOf(timeStamp));
//		String respTime = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", String.valueOf(timeZone));	// 設備timezone時間
		String respTime = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");	// 系統時間
		
		/*
		 * update Command respTime, respCode, respContent
		 */
		updateCommandResp(topicTransactionID, respTime, Integer.valueOf(respCode), respContent);
    	logger.info("[Command] Update Info: transactionID=["+topicTransactionID+"], respTime=["+respTime+"], respCode=["+respCode+"] ("+getRespCode(respCode)+"), respContent=["+respContent+"] ("+getRespContent(respContent)+")");
		
		/*
		 * update Battery or BatteryDetail info
		 */
		if (respCode.equals(RESP_SUCCESS)) {
			CommandTaskModel commandTask = DBQueryUtil.getInstance().queryCommandTaskJoinCommand(topicTransactionID);
			String commandID = commandTask.getCommandID();
			String config = commandTask.getConfig();
			logger.info("Response Success, CommandTask: "+JsonUtil.getInstance().convertObjectToJsonstring(commandTask));
			
			if (commandID.contains("BB")) {
				BBBean bean = (BBBean) JsonUtil.getInstance().convertStringToObject(config, BBBean.class);
				logger.info("[Battery] Update NBID=["+nbID+"], BatteryID=["+topicBatteryID+"], Config: "+config);
				// 先檢查DB Battery有無紀錄, 無: 新增Battery & update / 有: update
				String nbIDbattID = nbID +DATA_INFO_SPLIT+ topicBatteryID;
				BatteryModel model = DeviceManager.getInstance().getBattery(nbIDbattID);
				if (model==null) {
					// 新增battery
					model = new BatteryModel(nbID, Integer.valueOf(topicBatteryID));
					DeviceManager.getInstance().addBattery(model);
				}

				// update response
				DBQueryUtil.getInstance().updateBatteryBBValue(bean, nbID, Integer.valueOf(topicBatteryID));
			
			}
			else if (commandID.contains("BA")) {
				BABean bean = (BABean) JsonUtil.getInstance().convertStringToObject(config, BABean.class);
				logger.info("[Battery] Update NBID=["+nbID+"], BatteryID=["+topicBatteryID+"], Config: "+config);
				// 先檢查DB Battery有無紀錄, 無: 新增Battery & update / 有: update
				String nbIDbattID = nbID +DATA_INFO_SPLIT+ topicBatteryID;
				BatteryModel model = DeviceManager.getInstance().getBattery(nbIDbattID);
				if (model==null) {
					// 新增battery
					model = new BatteryModel(nbID, Integer.valueOf(topicBatteryID));
					DeviceManager.getInstance().addBattery(model);
				}

				// update response
				DBQueryUtil.getInstance().updateBatteryBAValue(bean, nbID, Integer.valueOf(topicBatteryID));
				
			}
			else if (commandID.contains("B5")) {
				// 電壓: Category=2
				List<Object> values = JsonUtil.getInstance().convertStringArrayToList(config);
				logger.info("[BatteryDetail] Update NBID=["+nbID+"], BatteryID=["+topicBatteryID+"], Category=[2], Config: "+values.toString());

				// update response
				DBQueryUtil.getInstance().updateBatteryDetail(values, nbID, Integer.valueOf(topicBatteryID), 2);
				
			}
			else if (commandID.contains("B3")) {
				// 內阻: Category=1
				List<Object> values = JsonUtil.getInstance().convertStringArrayToList(config);
				logger.info("[BatteryDetail] Update NBID=["+nbID+"], BatteryID=["+topicBatteryID+"], Category=[1], Config: "+values.toString());

				// update response
				DBQueryUtil.getInstance().updateBatteryDetail(values, nbID, Integer.valueOf(topicBatteryID), 1);
			}
		}
	}

	/**
	 * update Command respTime, respCode, respContent
	 * 
	 * @param transactionID
	 * @param respTime
	 * @param respCode
	 * @param respContent
	 */
	private void updateCommandResp(String transactionID, String respTime, int respCode, String respContent) {
		DBQueryUtil.getInstance().updateCommandResponse(transactionID, respTime, respCode, respContent);
	}
	
	private String getRespCode(String respCode) {
		if (respCode.equals(RESP_SUCCESS)) {
			return "成功";
		} else {
			return "失敗";
		}
	}
	private String getRespContent(String respContent) {
		if (respContent.contains("FF")) {
			return "信號失真";
		}
		else if (respContent.contains("FE")) {
			return "斷線或無回應";
		}
		else if (respContent.contains("BB") || respContent.contains("BA") || respContent.contains("B5") || respContent.contains("B3")) {
			return respContent;
		}
		else {
			return "unknown";
		}
	}
}
