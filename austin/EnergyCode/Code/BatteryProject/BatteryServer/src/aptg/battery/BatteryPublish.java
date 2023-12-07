package aptg.battery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import aptg.bean.MqttConfigBean;
import aptg.bean.PublishBean;

public class BatteryPublish {
	
	private static final Logger logger = LogManager.getFormatterLogger(BatteryPublish.class.getName());

	private String host;
	private MqttConfigBean config;
	
	private static BatteryPublish instances;
	public static BatteryPublish getInstance() {
		if (instances==null) {
			instances = new BatteryPublish();
		}
		return instances;
	}
	
	public void init(String host, MqttConfigBean config) {
		this.host = host;
		this.config = config;
	}
	
	public String getMqttidTemplate() {
		return config.getMqttid();
	}
	
	public String getTopicTemplate() {
		return config.getTopic();
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
	public String publishMessage(PublishBean publish) {
    	String mqttid = publish.getMqttid();
    	String topic = publish.getTopic();
    	String payload = publish.getPayload();
    	String uuid = publish.getUuid();
    	
        try {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(false);	//no persistent session
			connOpts.setUserName(config.getUsername());
			connOpts.setPassword(config.getPassword().toCharArray());
	
			// MQTT Client connect
			MqttClient client = new MqttClient(host, mqttid, new MemoryPersistence());
	        client.connect(connOpts);
	
	        // Set QoS
	        MqttMessage message = new MqttMessage(payload.getBytes());
	        message.setQos(Integer.parseInt(config.getQos()));
	
	        // send(publish) message
	        client.publish(topic, message); // Blocking publish
	        
        	// MQTT Client disconnect
			client.disconnect();
			
		} catch (MqttException e) {
			e.printStackTrace();
	        logger.error("Error.. topic: "+topic+", mqttid: "+mqttid+", payload: "+payload +" (uuid="+uuid+")");
	        return uuid;
		}
        logger.info("Success.. topic: "+topic+", mqttid: "+mqttid+", payload: "+payload +" (uuid="+uuid+")");
        return uuid;
	}
}
