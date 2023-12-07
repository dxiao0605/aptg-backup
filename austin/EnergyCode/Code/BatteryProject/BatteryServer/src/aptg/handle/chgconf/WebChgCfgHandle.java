package aptg.handle.chgconf;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aptg.bean.ChgConfBean;
import aptg.manager.CompanyManager;
import aptg.model.CompanyModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;

/**
 * 處理Web的更改公司告警判斷值的命令
 * 
 * @author austinchen
 *
 */
public class WebChgCfgHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(WebChgCfgHandle.class.getName());

	private String topic;
	private MqttMessage message;
	private int qos;

	public WebChgCfgHandle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	}

	@Override
	public void run() {
		// A_CompanyCode_ImpType_Alert1_Alert2
		String payload = new String(message.getPayload());
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+payload);
    	
		ChgConfBean config = (ChgConfBean) JsonUtil.getInstance().convertStringToObject(payload, ChgConfBean.class);
		logger.info("Payload Detail: "+JsonUtil.getInstance().convertObjectToJsonstring(config));
		
		String taskID = config.getTaskID();
		int companyCode = config.getCompanyCode();
		int impType = config.getIMPType();
		BigDecimal alert1 = config.getAlert1();
		BigDecimal alert2 = config.getAlert2();
		Integer disconnect = config.getDisconnect();
		Integer temperature1 = config.getTemperature1();
		
		// 
		CompanyModel company = CompanyManager.getInstance().getCompany(companyCode);
		if (company!=null) {
			company.setImpType(impType);
			company.setAlert1(alert1);
			company.setAlert2(alert2);
			company.setDisconnect(disconnect);
			company.setTemperature1(temperature1);
			CompanyManager.getInstance().updateCompany(company);
			
			DBQueryUtil.getInstance().updateAlertTaskSuccess(taskID);
			logger.info("Update Company IMPType=["+impType+"], Alert1=["+alert1+"], Alert2=["+alert2+"], Temperature1=["+temperature1+"], Disconnect=["+disconnect+"] Success!!!");
			
		} else {
			logger.error("查無此Company!!! CompanyCode=["+companyCode+"]");
		}
	}
}
