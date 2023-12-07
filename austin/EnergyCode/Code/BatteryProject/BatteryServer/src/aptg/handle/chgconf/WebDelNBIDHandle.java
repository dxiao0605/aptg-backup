package aptg.handle.chgconf;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;

import aptg.bean.DelNBIDBean;
import aptg.manager.DeviceManager;
import aptg.manager.NBGroupManager;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;

public class WebDelNBIDHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(WebDelNBIDHandle.class.getName());

	private String topic;
	private MqttMessage message;
	private int qos;
	
	public WebDelNBIDHandle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	}
	
	@Override
	public void run() {
		String payload = new String(message.getPayload());
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+payload);

		try {
			JSONArray itemArray = new JSONArray(payload);

			List<DelNBIDBean> delList = new ArrayList<>();
			for (int i=0 ; i<itemArray.length() ; i++) {
				Object ob = itemArray.getJSONObject(i);
				
				DelNBIDBean config = (DelNBIDBean) JsonUtil.getInstance().convertStringToObject(ob.toString(), DelNBIDBean.class);
				delList.add(config);
				logger.info("Payload Detail: "+JsonUtil.getInstance().convertObjectToJsonstring(config));
			}

			// del from Table
			int executeCount = DBQueryUtil.getInstance().delNBID(delList);
			logger.info("Delete Battery, BatteryDetail, BatteryRecordSummary, BattMaxRecTime, CommandTask, Command, Event & Update NBGroupHis EndTime Success!!!");
			
			if (executeCount!=-1) {
				// cache: remove DeviceManager cache (nbbattMap key=nbID+"_"+batteryID)
				DeviceManager.getInstance().removeBattery(delList);
				
				// cache:remove NBGroupManager cache (allocationMap key=nbID)
				NBGroupManager.getInstance().removeBatteryGroupNBList(delList);
				
				// DB: add NBHistory ModifyItem=24 (已刪除)
//				logger.info("Insert NBHistory");
				DBQueryUtil.getInstance().insertNBHistory(delList);

				logger.info("Delete Success!!! NBID: "+JsonUtil.getInstance().convertObjectToJsonstring(delList));
				
			} else {
				logger.error("刪除NBID失敗!!! NBID: "+JsonUtil.getInstance().convertObjectToJsonstring(delList));
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("ChangeConfig DelNbID Update Failed.. Exception: "+e.getMessage());
		}
	}
}
