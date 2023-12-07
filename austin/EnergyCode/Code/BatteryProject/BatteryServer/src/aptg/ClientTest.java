package aptg;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ClientTest {

	private String MQTT_SERVER = "mqtt://10.31.129.51:1883";
//	private String MQTT_SERVER = "mqtt://www.gtething.tw:8883";
	private String username = "sbb168";
	private String password = "vr5JcQy3HDYg";

	private String host;			// tcp://10.31.129.82:1883

	public ClientTest() throws URISyntaxException {
		URI uri = new URI(MQTT_SERVER);
		host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
		System.out.println("******* hots: "+host);
	}
	
	@SuppressWarnings("resource")
	public void sendMsg() throws MqttException {
        String qos = "2";
        String topic = "batt/v0/B168/ZZ20B00040";
        String clientid = "SVR_B168_P";
        
		String payload = "1607932986_+32_ZZ20B00040_B1680004040100000000000000000E5B3F6400000000D80E";

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);	//no persistent session
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
//	    connOpts.setKeepAliveInterval(1000);
		System.out.println("******* username: "+username + ", password: "+password);

		// MQTT Client connect
		MqttClient client = new MqttClient(host, username+"_"+clientid, new MemoryPersistence());
        client.connect(connOpts);
        
        // Set Message and QoS
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(Integer.parseInt(qos));
        
        // send(publish) message
        client.publish(topic, message); // Blocking publish
//        System.out.println("############# topic: "+topicStr);
//        System.out.println("############# message: "+message);
        
        // MQTT Client disconnect
        client.disconnect();
	}
	
	public static void main(String[] args) {
		try {
			ClientTest client = new ClientTest();
			client.sendMsg();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
