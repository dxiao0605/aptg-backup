package aptg.function;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.AttrBean;
import aptg.dao.MeterSetupDao;
import aptg.manager.MeterSetupManager;
import aptg.manager.SocketManager;
import aptg.models.MeterSetupModel;
import aptg.utils.JsonUtil;

/**
 * 	查詢指令
 * 
 * @author austinchen
 */
public class FuncID_A extends FuncIDBase {

	private static final Logger logger = LogManager.getFormatterLogger(FuncID_A.class.getName());

	private static final String WebRequestCode 		= "#1";
	private static final String RequestCode 		= "*1";
	private static final String ResponseCode 		= "*2";
	
	public FuncID_A(Socket socket) {
		this.socket = socket;
	}
	
	/*
	 *	收查詢eco5回傳的response
	 */
	public void queryCmd(String[] message) {
		// 檢查sof+type是為 *1 or *2 or #1
		String sofType = msgSOFType(message);
		
		if (sofType.equals(RequestCode)) {
			/*
			 * 處理Request
			 */
			String response = handleRequest(message);
			
			// 將eco查詢請求資訊回傳給eco5
			sendSocketMsg(socket, response);
			logger.info("[Socket] send response to Eco5: "+response);
			
			
		} else if (sofType.equals(ResponseCode)) {
			/*
			 * 處理Response
			 */
			String info = handleResponse(message);
			logger.info("[Socket] update info from Eco5: "+info);
			
			
		} else {
			/*
			 * 處理WebCmd工作(查詢)
			 */
			String request = handleWebCmd(message);
			
			// 取gatewayID, socket資訊
			String gatewayID = message[3];
			Socket socket = SocketManager.getInstance().getSocket(gatewayID);

			// 將web查詢or設定請求資訊傳給eco5
			if (socket!=null) {
				sendSocketMsg(socket, request);
				logger.info("[Socket] send request to Eco5: "+request);
			} else {
				logger.error("[Socket] socket is null, can't send request to Eco5: "+request);
			}
		}
	}

	/**
	 * 處理Request
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	private String handleRequest(String[] message) {
		// ex: 50
		String funcNum = msgFuncNum(message);
		
		if (funcNum.contentEquals("50")) {
			// 收eco5 client request需量參數請求 => return response
			return responseA50(message);

		} else if (funcNum.contentEquals("51")) {
			// 收eco5 client request需量參數請求 => return response
			return responseA51(message);
		}
		return null;
	}
	
	/**
	 * 處理Response
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	private String handleResponse(String[] message) {
		// ex: 50
		String funcNum = msgFuncNum(message);
		
		if (funcNum.contentEquals("50")) {
			// 收eco5 client request需量參數請求 => return response
			return updateA50(message);

		} else if (funcNum.contentEquals("51")) {
			// 收eco5 client request需量參數請求 => return response
			return updateA51(message);
		}
		return null;
	}

	/**
	 * 處理WebCmd工作(查詢)
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	public String handleWebCmd(String[] message) {
		// ex: 50
		String funcNum = msgFuncNum(message);
		
		if (funcNum.contentEquals("50")) {
			// 向eco5 client發需量參數請求
			return getReqA50(message);

		} else if (funcNum.contentEquals("51")) {
			// 向eco5 client發警報參數請求
			return getReqA51(message);
		}
		return null;
	}
	
	/*
	 * ==================== 需量參數查詢 ====================
	 */
	/**
	 * 取得至Eco5查詢需量參數的request內容
	 * 
	 * @param message
	 * @return
	 */
	private String getReqA50(String[] message) {
		// 取得參數
		String gatewayID = message[3];
		String deviceID = message[4];
		
		String request = getRequest("A50", gatewayID, deviceID);
		return request;
	}
	/**
	 * 收eco5 client request需量參數請求 => return response value to Eco5
	 * 
	 * @param gatewayID
	 * @param deviceID
	 * @return
	 */
	private String responseA50(String[] message) {
		// ex: *1;A50;00000004;gatewayID;deviceID;
		
		String func = message[1];	// A50
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ABCD-ECO-TEST-0026--
		String deviceID = message[4];	// DeviceID: IN11-ECO-TEST-002601
				
		// 撈取出meter
		MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		if (meter==null) 
			return "meter is null";
		
		String value = "DFEnabled_"+meter.getDfEnabled() +","+
					   "DFCode_"+meter.getDfCode() +","+
					   "DFCycle_"+meter.getDfCycle() +","+
					   "DFUpLimit_"+meter.getDfUpLimit() +","+
					   "DFLoLimit_"+meter.getDfLoLimit() +","+
					   "UsuallyCC_"+meter.getUsuallyCC() +","+
					   "SPCC_"+meter.getSpcc() +","+
					   "SatSPCC_"+meter.getSatSPCC() +","+
					   "OPCC_"+meter.getOpcc() +
					   "\n";
		
		// response to ECO5
		String response = getResponse(func, cmdSN, getSuccessCode(), null).replace("\n", value);	// "*2;A50;00000004;0;\n (去\n)+ ES110000000000TEST00;1_118,2_0,3_2,4_0,5_1,6_100,7_85,8_1,9_1,10_0,11_1,12_1,13_NULL;LF";
		
		return response;
	}
	/**
	 * 收eco5 client response需量參數值 => update to db
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	private String updateA50(String[] message) {
		String func = message[1];	// A50
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ABCD-ECO-TEST-0026--
		String deviceID = message[4];	// 設備ID: ES110000000000TEST00

		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
		String attrList = message[5];	// 回報時間: 130691738
		if (attrList.indexOf(",")!=-1) {
			// 多筆數據
			String[] attr = attrList.split(",");
			for (String attrInfo: attr) {
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		} else {
			// 一筆數據
			String[] info = attrList.split("_");
			AttrBean bean = new AttrBean();
			bean.setAttrID(info[0]);
			bean.setAttrValue(info[1]);
			attrInfoList.add(bean);
		}

		// 撈取出meter
		MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		if (meter==null) 
			return "meter is null";
			
		for (AttrBean bean: attrInfoList) {
			checkDemandAttr(meter, bean.getAttrID(), bean.getAttrValue());
		}
		
		// update MeterSetup demand Value
		try {
			MeterSetupDao dao = new MeterSetupDao();
			dao.updateMeter(meter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return JsonUtil.getInstance().convertObjectToJsonstring(meter);
	}

	/*
	 * ==================== 警報參數查詢 ====================
	 */
	/**
	 * 	取得警報參數查詢request
	 * 
	 * @param message
	 * @return
	 */
	private String getReqA51(String[] message) {
		// 取得參數
		String gatewayID = message[3];
		String deviceID = message[4];
		
		String request = getRequest("A51", gatewayID, deviceID);
		return request;
	}
	/**
	 * 收eco5 client request需量參數請求 => return response
	 * 
	 * @param gatewayID
	 * @param deviceID
	 * @return
	 */
	private String responseA51(String[] message) {
		// ex: *1;A51;00000004;gatewayID;deviceID;
		
		String func = message[1];	// A51
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ABCD-ECO-TEST-0026--
		String deviceID = message[4];	// DeviceID: IN11-ECO-TEST-002601

		// 撈取出meter
		MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		if (meter==null) 
			return "meter is null";
		
		String value = "CurAlertEnabled_"+meter.getCurAlertEnabled() +","+
					   "CurUpLimit_"+meter.getCurUpLimit() +","+
					   "CurLoLimit_"+meter.getCurLoLimit() +","+
					   "DFUpLimit_"+meter.getDfUpLimit() +","+
					   "VolAlertEnabled_"+meter.getVolAlertEnabled() +","+
					   "VolAlertType_"+meter.getVolAlertType() +","+
					   "VolUpLimit_"+meter.getVolUpLimit() +","+
					   "VolLoLimit_"+meter.getVolLoLimit() +","+
					   "ECAlertEnabled_"+meter.getEcAlertEnabled() +","+
					   "ECUpLimit_"+meter.getEcUpLimit() +
					   "\n";
		
		// response to ECO5
		String response = getResponse(func, cmdSN, getSuccessCode(), null).replace("\n", value);	// "*2;A50;00000004;0;\n (去\n)+ ES110000000000TEST00;1_118,2_0,3_2,4_0,5_1,6_100,7_85,8_1,9_1,10_0,11_1,12_1,13_NULL;LF";
		
		return response;
	}
	/**
	 * 收eco5 client response警報參數 => update to db
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	private String updateA51(String[] message) {
		String func = message[1];	// A51
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ABCD-ECO-TEST-0026--
		String deviceID = message[4];	// 設備ID: ES110000000000TEST00

		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
		String attrList = message[5];	// 回報時間: 130691738
		if (attrList.indexOf(",")!=-1) {
			// 多筆數據
			String[] attr = attrList.split(",");
			for (String attrInfo: attr) {
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		} else {
			// 一筆數據
			String[] info = attrList.split("_");
			AttrBean bean = new AttrBean();
			bean.setAttrID(info[0]);
			bean.setAttrValue(info[1]);
			attrInfoList.add(bean);
		}

		MeterSetupModel meter = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		if (meter==null) 
			return "meter is null";
		
		for (AttrBean bean: attrInfoList) {
			checkAlertAttr(meter, bean.getAttrID(), bean.getAttrValue());
		}
		
		// update MeterSetup alert Value
		try {
			MeterSetupDao dao = new MeterSetupDao();
			dao.updateMeter(meter);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return JsonUtil.getInstance().convertObjectToJsonstring(meter);
	}
}
