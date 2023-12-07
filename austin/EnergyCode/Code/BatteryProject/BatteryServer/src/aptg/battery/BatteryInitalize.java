package aptg.battery;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import aptg.bean.MqttConfigBean;
import aptg.manager.CompanyManager;
import aptg.manager.DeviceManager;
import aptg.manager.NBGroupManager;

public class BatteryInitalize {

	private static final Logger logger = LogManager.getFormatterLogger(BatteryInitalize.class.getName());

	private static final String MQTT				= "mqtt";
	private static final String MQTT_SERVER		 	= "mqtt.server";
//	private static final String MQTT_USERNAME	 	= "mqtt.username";
//	private static final String MQTT_PASSWORD	 	= "mqtt.password";

	private static final String SUBSCRIBE_COUNT 	= "subscribe.count";

	private static final String SUBSCRIBE_USERNAME 		  = "subscribe.mqtt.username.";
	private static final String SUBSCRIBE_PASSWORD 		  = "subscribe.mqtt.password.";
	private static final String SUBSCRIBE_TOPIC 		  = "subscribe.topics.";
	private static final String SUBSCRIBE_MQTTID 		  = "subscribe.mqttid.";
	private static final String SUBSCRIBE_QOS 			  = "subscribe.qos.";
	private static final String SUBSCRIBE_THREADPOOLCOUNT = "subscribe.threadPoolCount";

	private static final String PUBLISH_USERNAME 		= "publish.mqtt.username";
	private static final String PUBLISH_PASSWORD 		= "publish.mqtt.password";
	private static final String PUBLISH_TOPIC 			= "publish.topics";
	private static final String PUBLISH_MQTTID 			= "publish.mqttid";
	private static final String PUBLISH_QOS 			= "publish.qos";
	private static final String PUBLISH_THREADPOOLCOUNT = "publish.threadPoolCount";

	private String server;
	private String host;		// tcp://10.31.129.82:1883
//	private String username;	// durreciver
//	private String password;	// tvmcY9VXhDr8
	private String sub_count;	// 1

	private ExecutorService subThreadPool;
	private ExecutorService pubThreadPool;
	
	private static int defaultSubThreadPoolCount = 300;
	private static int defaultPubThreadPoolCount = 100;

	public void init() {
		try {
			// init Device (所有的Battery)
			logger.info("[Init] Battery NBID & BatteryID");
			DeviceManager.getInstance().init();
			
			// init Company (所有的Company)
			logger.info("[Init] Battery Company");
			CompanyManager.getInstance().init();
			
			// init NBAllocation (NBID => 啟用中的Company與時間)
			logger.info("[Init] Battery NBAllocationHis");
//			NBAllocationManager.getInstance().init();
			NBGroupManager.getInstance().init();
			
			// init MQTT
			logger.info("[Init] MQTT Startup");
			initMqttConfig();
			
		} catch (URISyntaxException | MqttException e) {
			e.printStackTrace();
		}
	}
	
	private void initMqttConfig() throws URISyntaxException, MqttException {
		try {
			ResourceBundle mqttConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get(MQTT+".properties")));
			
			this.server = mqttConfig.getString(MQTT_SERVER);
			this.sub_count = mqttConfig.getString(SUBSCRIBE_COUNT);
	
			URI uri = new URI(server);
			this.host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
	
			/*
			 * Subscribe
			 */
			String sub_thread_count = mqttConfig.getString(SUBSCRIBE_THREADPOOLCOUNT);
			int threadPoolCount = (StringUtils.isBlank(sub_thread_count)) ? defaultSubThreadPoolCount : Integer.valueOf(sub_thread_count);
			this.subThreadPool = Executors.newFixedThreadPool(threadPoolCount);
			initSubscribe(mqttConfig, subThreadPool);
			
			/*
			 * Publish
			 */
			String pub_thread_count = mqttConfig.getString(PUBLISH_THREADPOOLCOUNT);
			threadPoolCount = (StringUtils.isBlank(pub_thread_count)) ? defaultPubThreadPoolCount : Integer.valueOf(pub_thread_count);
			this.pubThreadPool = Executors.newFixedThreadPool(threadPoolCount);
			initPublish(mqttConfig, pubThreadPool);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Subscribe
	 * 
	 * @param mqttConfig
	 * @throws MqttException 
	 */
	private void initSubscribe(ResourceBundle mqttConfig, ExecutorService threadPool) throws MqttException {
		int count = Integer.parseInt(sub_count);
		for (int i=1 ; i<=count ; i++) {
			String configStr;

			// username
			configStr = SUBSCRIBE_USERNAME + i;
			String username = mqttConfig.getString(configStr);
			// password
			configStr = SUBSCRIBE_PASSWORD + i;
			String password = mqttConfig.getString(configStr);
			// topic
			configStr = SUBSCRIBE_TOPIC + i;
			String topic = mqttConfig.getString(configStr);
			// clientid(mqttid)
			configStr = SUBSCRIBE_MQTTID + i;
			String mqttid = mqttConfig.getString(configStr);
			// qos
			configStr = SUBSCRIBE_QOS + i;
			String qos = mqttConfig.getString(configStr);
			
			MqttConnectOptions option = getMqttConnectOptions(username, password);
			MqttClient client = getMqttClient(mqttid);
			MqttConfigBean config = getMqttConfigBean(username, password, topic, mqttid, qos);
			
	    	Thread thread = new Thread(new BatterySubscribe(option, client, config, threadPool));
	    	thread.start();
		}
	}
	/**
	 * CommandTask Publish
	 * 
	 * @param mqttConfig
	 * @throws URISyntaxException
	 */
	private void initPublish(ResourceBundle mqttConfig, ExecutorService threadPool) throws URISyntaxException {
		String configStr;

		// username
		configStr = PUBLISH_USERNAME;
		String username = mqttConfig.getString(configStr);
		// password
		configStr = PUBLISH_PASSWORD;
		String password = mqttConfig.getString(configStr);
		// topic
		configStr = PUBLISH_TOPIC;
		String topic = mqttConfig.getString(configStr);
		// clientid(mqttid)
		configStr = PUBLISH_MQTTID;
		String mqttid = mqttConfig.getString(configStr);
		// qos
		configStr = PUBLISH_QOS;
		String qos = mqttConfig.getString(configStr);
		
		MqttConfigBean config = getMqttConfigBean(username, password, topic, mqttid, qos);

		BatteryPublish.getInstance().init(host, config);
//		Thread thread = new Thread(new BatteryCMDTaskJob(host, config, threadPool));
//    	thread.start();
	}
	
	
	
	private MqttConnectOptions getMqttConnectOptions(String username, String password) {
		MqttConnectOptions option = new MqttConnectOptions();
        option.setCleanSession(false);
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
	
	private MqttConfigBean getMqttConfigBean(String username, String password, String topic, String mqttid, String qos) {
		MqttConfigBean config = new MqttConfigBean();
		config.setUsername(username);
		config.setPassword(password);
		config.setTopic(topic);
		config.setMqttid(mqttid);
		config.setQos(qos);
		
		return config;
	}
}
