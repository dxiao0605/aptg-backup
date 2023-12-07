package aptg.battery.mqtt;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.battery.bean.MqttConfigBean;



public class BatterySubscribe implements MqttCallbackExtended, Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(BatterySubscribe.class.getName());

	private ExecutorService threadPool;
	
	private MqttConnectOptions option;
	private MqttClient client;
	private MqttConfigBean config;
	
	private String topic;
	private String mqttid;
	private int qos;

	public BatterySubscribe(MqttConnectOptions option, MqttClient client, MqttConfigBean config, ExecutorService threadPool) {
		this.option = option;
		this.client = client;
		this.config = config;
		
		this.threadPool = threadPool;
	}
	
	public void run() {
    	try {
    		topic = config.getTopic();
    		mqttid = config.getUsername() +"_"+ config.getMqttid();
        	qos = Integer.parseInt(config.getQos());
        	
			mqttSubscribe();
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void mqttSubscribe() throws MqttException {
    	logger.info("[MqttSubscribe] topic: "+topic+", mqttid: "+mqttid+", Qos: "+qos);

    	if (!client.isConnected()) {
	        client.setCallback(this);
	        client.connect(option);
	        client.subscribe(topic, qos);

	    	logger.info("[MqttSubscribe] Success!!! topic: "+topic+", mqttid: "+mqttid+", Qos: "+qos);
		}
	}
	
	@Override
	public void connectionLost(Throwable cause) {
    	logger.error("[ConnectionLost] Connection lost because: "+ cause);
//        System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
    	logger.info("[MessageArrived] topic: "+topic +", message: "+new String(message.getPayload()));

    	// thread pool處理接收到的MQTT訊息
    	if (topic.contains("B168")) {
//        	threadPool.execute(new B168Handle(message));
        	
    	} else if (topic.contains("CMDRESP")) {
//        	threadPool.execute(new CMDRespHandle(topic, message));
    		
    	} else if (topic.contains("CMDACK")) {
//        	threadPool.execute(new CMDACKHandle(topic, message));
        	
    	} else if (topic.contains("CHNCFG")) {
//    		threadPool.execute(new WebChgCfgHandle(message));
    	}
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		logger.info("[ConnectComplete] Re-Connection Attempt: " + reconnect + ", serverURI: "+serverURI);
		if(reconnect) {
			try {
				client.subscribe(topic, qos);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}
}
