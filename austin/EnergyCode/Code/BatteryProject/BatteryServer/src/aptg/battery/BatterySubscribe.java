package aptg.battery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.bean.MqttConfigBean;
import aptg.handle.b168.B168Handle;
import aptg.handle.chgconf.WebChgCfgHandle;
import aptg.handle.chgconf.WebDelNBIDHandle;
import aptg.handle.chgconf.WebNBCompanyHandle;
import aptg.handle.cmd.CMDACKHandle;
import aptg.handle.cmd.CMDRespHandle;
import aptg.handle.cmd.WebCMDTaskHandle;
import aptg.utils.EmailUtil;

/**
 * 訂閱處理收到通訊板、電池上傳的
 * 1. B168電池資訊
 * 2. CMDRESP
 * 3. CMDACK
 * 
 * 訂閱處理收到WEB所發出電池狀態告警值更新指令
 * 1. CHNCFG
 * 
 * @author austinchen
 *
 */
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
		topic = config.getTopic();
		mqttid = config.getUsername() +"_"+ config.getMqttid();
    	qos = Integer.parseInt(config.getQos());
    	
		while(true) {
	    	// check connection
	    	try {
		    	if (!client.isConnected()) {
					// not connect => do connect
			    	try {
						mqttSubscribe();

			    	} catch (MqttException e) {
						e.printStackTrace();
						
						logger.error("[MqttSubscribe] Failed!!! Exception: "+e.toString());
						sendMail();
					}
			    	
					Thread.sleep(30*1000);		// 30 sec
					
		    	} else {
					// connected => pass
					Thread.sleep(5*60*1000);	// 5 min
		    	}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	private void mqttSubscribe() throws MqttException {

    	if (!client.isConnected()) {
        	logger.info("[MqttSubscribe] topic: "+topic+", mqttid: "+mqttid+", Qos: "+qos);
        	
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
    	/* ========================================= *
    	 * 					upload					 *
    	 * ========================================= */
    	if (topic.contains("B168")) {
    		// b168
        	threadPool.execute(new B168Handle(topic, message));
    		
    	/* ========================================= *
    	 * 					cmd						 *
    	 * ========================================= */
    	} else if (topic.contains("CMDTASK")) {
    		// task
    		threadPool.execute(new WebCMDTaskHandle(topic, message));
        	
    	} else if (topic.contains("CMDRESP")) {
    		// resp
        	threadPool.execute(new CMDRespHandle(topic, message));
    		
    	} else if (topic.contains("CMDACK")) {
    		// ack
        	threadPool.execute(new CMDACKHandle(topic, message));
        	
    	/* ========================================= *
    	 * 					config					 *
    	 * ========================================= */
    	} else if (topic.contains("CHNCFG")) {
    		// chgcfg
    		threadPool.execute(new WebChgCfgHandle(topic, message));

    	} else if (topic.contains("NBCOMPANY")) {
    		// nbcompany
    		threadPool.execute(new WebNBCompanyHandle(topic, message));
    		
    	} else if (topic.contains("DELNBID")) {
    		// delNBID
    		threadPool.execute(new WebDelNBIDHandle(topic, message));
    		
    	} else {
        	logger.info("\n"+"[MessageArrived] topic: "+topic +", message: "+new String(message.getPayload()));
    	}
    	
//    	threadPool.shutdown();
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		logger.info("[ConnectComplete] Re-Connection Attempt: " + reconnect + ", serverURI: "+serverURI);
		if(reconnect) {
			try {
		        client.setCallback(this);
		        client.connect(option);
				client.subscribe(topic, qos);
			} catch (MqttException e) {
				e.printStackTrace();

				logger.error("[ConnectComplete] Re-Connection Attempt: " + reconnect + ", serverURI: "+serverURI +", Exception: "+e.toString());
				sendMail();
			}
		}
	}
	
	
	

	/**
	 * 發送mail通知
	 */
	public void sendMail() {
		String subject = "BatteryServer connected to MQTT server failed, 異常通知: "+topic;

//		String Mail_To_List_1 = "austinchen@aptg.com.tw";
		
		String body = "topic: "+ topic +"\n"+
					  "clientid: " + mqttid +"\n"+
					  "qos: "+qos;
		
//		// ========== 目前hardcode我自己 ==========
//		List<String> toList = new ArrayList<>();
//		toList.add(Mail_To_List_1);
//		// =========================================
		
		EmailUtil email = new EmailUtil();
		email.setSubject(subject);
		email.setBody(body);
//		email.setToList(toList);
		try {
			logger.info("主旨: "+subject + ", 內容: \n"+body);
			email.sendMail();
		} catch (Exception e) {
//			logger.error("Send Email Failed..: "+body);
			e.printStackTrace();
		}
	}
}
