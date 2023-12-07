package aptg.handle.chgconf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;

import aptg.bean.BatteryGroupNBListBean;
import aptg.bean.NBCompanyBean;
import aptg.manager.NBGroupManager;
import aptg.utils.JsonUtil;

public class WebNBCompanyHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(WebNBCompanyHandle.class.getName());

	private String topic;
	private MqttMessage message;
	private int qos;
	
	public WebNBCompanyHandle(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		this.qos = message.getQos();
	}
	
	@Override
	public void run() {

		String payload = new String(message.getPayload());
    	logger.info("[MessageArrived] topic: "+topic +", QoS: "+qos+", message: "+payload);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			JSONArray itemArray = new JSONArray(payload);

			for (int i=0 ; i<itemArray.length() ; i++) {
				Object ob = itemArray.getJSONObject(i);
				
				NBCompanyBean config = (NBCompanyBean) JsonUtil.getInstance().convertStringToObject(ob.toString(), NBCompanyBean.class);
				logger.info("Payload Detail: "+JsonUtil.getInstance().convertObjectToJsonstring(config));

		    	String nbID = config.getNBID();
		    	int companyCode = config.getCompanyCode();
		    	Integer active = config.getActive();
		    	
		    	int groupInternalID = config.getGroupInternalID();
		    	int defaultGroup = config.getDefaultGroup();
		    	
		    	if (active==null || active==0) {
			    	// active null => 狀態不變 => 只做更新companycode 
//			    	NBAllocationHisModel na = NBAllocationManager.getInstance().getNBAllocationHis(nbID);
		    		BatteryGroupNBListBean bgnb = NBGroupManager.getInstance().getBatteryGroupNBList(nbID);
			    	if (bgnb!=null) {
			    		bgnb.setCompanyCode(companyCode);
			    		bgnb.setGroupInternalID(groupInternalID);
			    		bgnb.setDefaultGroup(defaultGroup);
//			    		NBAllocationManager.getInstance().updateCompany(na);
			    		NBGroupManager.getInstance().updateCompany(bgnb);
						logger.info("Update NBGroupHis NBID=["+nbID+"], CompanyCode=["+companyCode+"], GroupInternalID=["+groupInternalID+"], DefaultGroup=["+defaultGroup+"] Success!!!");
						
			    	} else {
//			    		na = NBAllocationManager.getInstance().addNBAllocationHisActive(nbID);
			    		bgnb = NBGroupManager.getInstance().addBatteryGroupNBListActive(nbID);
			    		if (bgnb!=null)
			    			logger.info("重撈DB新增cache map: NBID=["+nbID+"], CompanyCode=["+bgnb.getCompanyCode()+"]");
			    		else
			    			logger.info("cache map查無此BatteryNBList資訊, NBID=["+nbID+"], Active!=13, 不處理");
			    	}	
		    	} else {
		    		// active not null => active狀態改變
		    		if (active==13) {
		    			// 啟用
//		    			NBAllocationHisModel na = NBAllocationManager.getInstance().addNBAllocationHis(nbID);
		    			BatteryGroupNBListBean bgnb = NBGroupManager.getInstance().addBatteryGroupNBList(nbID);
		    			if (bgnb!=null) 
		    				logger.info("NBID=["+nbID+"] Active=13(啟用) success!");
		    			else
		    				logger.error("NBID=["+nbID+"] Active=13(啟用) failed...");
		    		}
		    		else if (active==14) {
		    			// 停用
//		    			NBAllocationManager.getInstance().stopNBAllocationHis(nbID);
		    			NBGroupManager.getInstance().stopNBList(nbID);
	    				logger.info("NBID=["+nbID+"] Active=14(停用) success!");
		    		}
		    		else {
		    			// 刪除
//		    			NBAllocationManager.getInstance().stopNBAllocationHis(nbID);
		    			NBGroupManager.getInstance().stopNBList(nbID);
	    				logger.info("NBID=["+nbID+"] Active=15(刪除) success!");
		    		}
		    	}
			}
		} catch(Exception e) {
			logger.error("ChangeConfig NBCompany Update Failed.. Exception: "+e.getMessage());
			e.printStackTrace();
		}
	}

}
