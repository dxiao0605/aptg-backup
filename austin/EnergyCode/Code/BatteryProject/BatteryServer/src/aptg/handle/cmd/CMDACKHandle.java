package aptg.handle.cmd;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.model.CommandModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.ToolUtil;

public class CMDACKHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(CMDACKHandle.class.getName());

	private static final String TOPIC_INFO_SPLIT 	= "/";
	private static final String DATA_INFO_SPLIT 	= "_";
	private static final char LF  = (char) 0x0A;	// \n

	private String topic;
	private MqttMessage message;
	private int qos;

	public CMDACKHandle(String topic, MqttMessage message) {
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
		
		String[] ack = payload.split(DATA_INFO_SPLIT);
		String timeStamp = ack[0];
		String timeZone = ack[1];
		String nbID = ack[2];
		String ackMsg = ack[3];

//		Date time = ToolUtil.getInstance().convertTimestampToDate(Long.valueOf(timeStamp));
//		String ackTime = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", String.valueOf(timeZone));	// 設備timezone時間
		String ackTime = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");	// 系統時間
		
		// 更新Command下行命令的交易紀錄之ackTime
		updateCommandAck(topicTransactionID, ackTime);
		logger.info("Update Command Info: transactionID=["+topicTransactionID+"], ackTime=["+ackTime+"]");
	}
	
	/**
	 * update Command ackTime
	 * 
	 * @param transactionID
	 * @param ackTime
	 */
	private void updateCommandAck(String transactionID, String ackTime) {
		DBQueryUtil.getInstance().updateCommandAckTime(transactionID, ackTime);
	}
}
