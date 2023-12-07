package aptg.battery.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttUtil {

	private static MqttUtil instances;
	private static final String MQTT				= "mqtt";
	private static final String MQTT_SERVER		 	= "mqtt.server";
	private static final String MQTT_USERNAME	 	= "mqtt.username";
	private static final String MQTT_PASSWORD	 	= "mqtt.password";
	
	
	private String server;
	private String host;		// tcp://10.31.129.82:1883
	private String username;	// sbweb
	private String password;	// reQCVWfxZP8j

	
	private MqttUtil() {		
		initMqtt();
	}
	
	public static MqttUtil getInstance() {
		if (instances==null) {
			instances = new MqttUtil();
		}
		return instances;
	}
	
	private void initMqtt()  {
		ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
		
		this.server = mqttConfig.getString(MQTT_SERVER);
		this.username = mqttConfig.getString(MQTT_USERNAME);
		this.password = mqttConfig.getString(MQTT_PASSWORD);			
		
		try {
			URI uri = new URI(server);
			this.host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	/**
	 * 發送MQTT命令至client
	 * @param payload
	 * @throws MqttException
	 */
	@SuppressWarnings("resource")
	public void sendCMD(String topic, String mqttid, String qos, String payload) throws MqttException {		
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);	//no persistent session
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		

		// MQTT Client connect
		MqttClient client = new MqttClient(host, username+"_"+mqttid, new MemoryPersistence());
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
