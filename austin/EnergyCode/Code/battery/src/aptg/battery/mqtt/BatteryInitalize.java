package aptg.battery.mqtt;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import aptg.battery.bean.MqttConfigBean;


public class BatteryInitalize {

	private static final Logger logger = LogManager.getFormatterLogger(BatteryInitalize.class.getName());

	private static final String MQTT				= "mqtt";
	private static final String MQTT_SERVER		 	= "mqtt.server";
	private static final String MQTT_USERNAME	 	= "mqtt.username";
	private static final String MQTT_PASSWORD	 	= "mqtt.password";

	private static final String SUBSCRIBE_COUNT 	= "subscribe.count";
	
	private static final String SUBSCRIBE_TOPIC 		= "subscribe.topics.";
	private static final String SUBSCRIBE_MQTTID 		= "subscribe.mqttid.";
	private static final String SUBSCRIBE_QOS 			= "subscribe.qos.";
	private static final String SUBSCRIBE_THREADPOOLCOUNT = "subscribe.threadPoolCount.";

	private static final String PUBLISH_TOPIC 			= "publish.topics";
	private static final String PUBLISH_MQTTID 			= "publish.mqttid";
	private static final String PUBLISH_QOS 			= "publish.qos";
	private static final String PUBLISH_THREADPOOLCOUNT = "publish.threadPoolCount";

	private String server;
	private String host;		// tcp://10.31.129.82:1883
	private String username;	// durreciver
	private String password;	// tvmcY9VXhDr8
	private String sub_count;	// 1
	
	private static int defaultThreadPoolCount = 100;

	public void init() {
		try {
			
			// init MQTT
			logger.info("[Init] MQTT Startup");
			initMqttConfig();
			
		} catch (URISyntaxException | MqttException e) {
			e.printStackTrace();
		}
	}
	
	private void initMqttConfig() throws URISyntaxException, MqttException {
		ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
		
		this.server = mqttConfig.getString(MQTT_SERVER);
		this.username = mqttConfig.getString(MQTT_USERNAME);
		this.password = mqttConfig.getString(MQTT_PASSWORD);
		this.sub_count = mqttConfig.getString(SUBSCRIBE_COUNT);
//		this.pub_count = mqttConfig.getString(PUBLISH_COUNT);

		URI uri = new URI(server);
		this.host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());

		// Subscribe
//		initSubscribe(mqttConfig);
		
		// Publish
		initPublish(mqttConfig);
	}

	
	/**
	 * Subscribe
	 * 
	 * @param mqttConfig
	 * @throws MqttException 
	 */
	private void initSubscribe(ResourceBundle mqttConfig) throws MqttException {
		int count = Integer.parseInt(sub_count);
		for (int i=1 ; i<=count ; i++) {
			String configStr;
			// topic
			configStr = SUBSCRIBE_TOPIC + i;
			String topic = mqttConfig.getString(configStr);
			// clientid(mqttid)
			configStr = SUBSCRIBE_MQTTID + i;
			String mqttid = mqttConfig.getString(configStr);
			// qos
			configStr = SUBSCRIBE_QOS + i;
			String qos = mqttConfig.getString(configStr);
			// topic msg threadPoolCount
			configStr = SUBSCRIBE_THREADPOOLCOUNT + i;
			String threadCount = mqttConfig.getString(configStr);
			int threadPoolCount = (StringUtils.isBlank(threadCount)) ? defaultThreadPoolCount : Integer.valueOf(threadCount);
			
			MqttConnectOptions option = getMqttConnectOptions();
			MqttClient client = getMqttClient(mqttid);
			MqttConfigBean config = getMqttConfigBean(topic, mqttid, qos);
			ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolCount);
			
	    	Thread thread = new Thread(new BatterySubscribe(option, client, config, threadPool));
	    	thread.start();
		}
	}
	
	/**
	 * Publish
	 * 
	 * @param mqttConfig
	 * @throws URISyntaxException
	 */
	private void initPublish(ResourceBundle mqttConfig) throws URISyntaxException {
		String configStr;
		// topic
		configStr = PUBLISH_TOPIC;
		String topic = mqttConfig.getString(configStr);
		// clientid(mqttid)
		configStr = PUBLISH_MQTTID;
		String mqttid = mqttConfig.getString(configStr);
		// qos
		configStr = PUBLISH_QOS;
		String qos = mqttConfig.getString(configStr);
		// topic msg threadPoolCount
		configStr = PUBLISH_THREADPOOLCOUNT;
		String threadCount = mqttConfig.getString(configStr);
		int threadPoolCount = (StringUtils.isBlank(threadCount)) ? defaultThreadPoolCount : Integer.valueOf(threadCount);
		
		MqttConfigBean config = getMqttConfigBean(topic, mqttid, qos);
		ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolCount);

		Thread thread = new Thread(new BatteryCMDPublishJob(host, config, threadPool));
    	thread.start();
	}
	
	
	
	private MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions option = new MqttConnectOptions();
        option.setCleanSession(true);
        option.setUserName(username);
        option.setPassword(password.toCharArray());
        
        option.setAutomaticReconnect(true);	// reconnect
        option.setConnectionTimeout(10);
        
        return option;
	}
	
	private MqttClient getMqttClient(String clientid) throws MqttException {
		MqttClient client = new MqttClient(host, clientid, new MemoryPersistence());
		return client;
	}
	
	private MqttConfigBean getMqttConfigBean(String topic, String mqttid, String qos) {
		MqttConfigBean config = new MqttConfigBean();
		config.setUsername(username);
		config.setPassword(password);
		config.setTopic(topic);
		config.setMqttid(mqttid);
		config.setQos(qos);
		
		return config;
	}
}
