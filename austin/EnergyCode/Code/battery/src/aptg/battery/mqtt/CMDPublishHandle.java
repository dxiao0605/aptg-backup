package aptg.battery.mqtt;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import aptg.battery.bean.MqttConfigBean;


/**
 * 處理CommandTask內給通訊板、電池的下行命令
 *  
 * @author austinchen
 *
 */
public class CMDPublishHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(CMDPublishHandle.class.getSimpleName());

	private static final int STATUS_EXECUTING = 2;
	
	private String host;		// tcp://10.31.129.82:1883
	private String username;	// austin
	private String password;	// austin1234
	private String topic;		// 
	private String mqttid;		// = clientid
	private String qos;			// 0
	
	
	public CMDPublishHandle(String host, MqttConfigBean config) {
		
		
		this.host = host;
		this.username = config.getUsername();
		this.password = config.getPassword();
		this.topic = config.getTopic();
		this.mqttid = config.getMqttid();
		this.qos = config.getQos();
	}
	
	@Override
	public void run() {
//        String transactionID = ToolUtil.getInstance().getTrimUUID();

     	String payload = "";
        
        logger.info("CMD Context: "+payload);
		// 下行發送cmd
		if (StringUtils.isNotBlank(payload)) {
			try {
				// topic = topic + "{通訊版序號}" +"/"+ "{電池ID}" +"/"+ txid;
				String topic = this.topic;// + command.getNbID() +"/"+ command.getBatteryID() +"/"+ transactionID;
				
				// send command
				sendCMD(topic, mqttid, qos, payload);
				
			
				
			} catch (MqttException e) {
				e.printStackTrace();
			}	
		}
	}

	/**
	 * 發送MQTT命令至client
	 * 
	 * @param topic
	 * @param clientid
	 * @param qos
	 * @param payload
	 * @throws MqttException
	 */
	@SuppressWarnings("resource")
	private void sendCMD(String topic, String clientid, String qos, String payload) throws MqttException {
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);	//no persistent session
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());

		// MQTT Client connect
		MqttClient client = new MqttClient(host, username+"_"+clientid, new MemoryPersistence());
        client.connect(connOpts);

        // Set QoS
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(Integer.parseInt(qos));

        // send(publish) message
        client.publish(topic, message); // Blocking publish

        // MQTT Client disconnect
        client.disconnect();
	}
}
