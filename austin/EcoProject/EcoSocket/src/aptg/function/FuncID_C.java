package aptg.function;

import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.manager.SocketManager;
import aptg.manager.WebCmdManager;

/**
 * 	參數設定後ECO回復的設定結果
 * 
 * @author austinchen
 */
public class FuncID_C extends FuncIDBase {

	private static final Logger logger = LogManager.getFormatterLogger(FuncID_C.class.getName());

	private static final String WebRequestCode 		= "#1";
	private static final String RequestCode 		= "*1";
	private static final String ResponseCode 		= "*2";
	
	public FuncID_C(Socket socket) {
		this.socket = socket;
	}
	
	public void setParameter(String[] message) {
		// 檢查sof+type是為 *2 or #1
		String sofType = msgSOFType(message);
		
		if (sofType.equals(ResponseCode)) {
			/*
			 * 處理設定參數後Eco回Socket Server的Response
			 */
			String info = handleResponse(message);
			logger.info("[Socket] update info from Eco5: "+info);
			
			/*
			 * ECO5回復response後socket server再次查詢
			 */
			String request = reCheckResponse(message);
			logger.info("[Socket] re-send request to Eco5: "+request);
			
		} else {
			/*
			 * 處理WebCmd工作(設定)
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
			updateC50(message);

		} else if (funcNum.contentEquals("51")) {
			// 收eco5 client request需量參數請求 => return response
			updateC51(message);
		}
		return null;
	}

	/**
	 * 重送查詢A50 or A51再次確認更新
	 * 
	 * @param message
	 * @return
	 */
	private String reCheckResponse(String[] message) {
		FuncID_A func_a = new FuncID_A(socket);
		String request = func_a.handleWebCmd(message);
		
		if (request!=null)
			WebCmdManager.getInstance().addWebCmd(request);
		
		return request;
	}
	
	/**
	 * 處理WebCmd工作(設定)
	 * 
	 * @param message
	 * @param semicolonCount
	 * @return
	 */
	private String handleWebCmd(String[] message) {
		// ex: 50
		String funcNum = msgFuncNum(message);
		
		if (funcNum.contentEquals("50")) {
			// 向eco5 client發需量參數請求
			return getReqC50(message);

		} else if (funcNum.contentEquals("51")) {
			// 向eco5 client發警報參數請求
			return getReqC51(message);
		}
		return null;
	}

	/*
	 * ==================== 需量參數設定 ====================
	 */
	/**
	 * 組合至Eco5查詢需量參數設定的request內容
	 * 
	 * @param message
	 * @return
	 */
	private String getReqC50(String[] message) {
		// 取得參數
		String request = "";
		for (String msg: message) {
			request += msg + ";";
		}
		request = request.replace(WebRequestCode, RequestCode).substring(0, request.length()-1);
		return request;
	}
	/**
	 * 	需量參數設定
	 * 
	 * @param message
	 * @return
	 */
	private void updateC50(String[] message) {
		// *2;C50;00000004;0;ES110000000000TEST00;LF
		String successCode = message[3];
		String deviceID = message[4];
		
		// success or failed => DO NOTHING
		if (successCode.equals(SUCCESS_CDE)) {
			logger.info("[C50] set DeviceID=["+deviceID+"] demand success");
		} else {
			logger.error("[C50] set DeviceID=["+deviceID+"] demand failed");
		}
	}


	/*
	 * ==================== 警報參數設定 ====================
	 */
	/**
	 * 組合至Eco5查詢警報參數設定的request內容
	 * 
	 * @param message
	 * @return
	 */
	private String getReqC51(String[] message) {
		// 取得參數
		String request = "";
		for (String msg: message) {
			request += msg + ";";
		}
		request = request.replace(WebRequestCode, RequestCode).substring(0, request.length()-1);
		return request;
	}
	/**
	 * 	警報參數設定
	 * 
	 * @param message
	 * @return
	 */
	private void updateC51(String[] message) {
		// *2;C51;00000004;0;ES110000000000TEST00;LF 
		String successCode = message[3];
		String deviceID = message[4];
		
		// success or failed => DO NOTHING
		if (successCode.equals(SUCCESS_CDE)) {
			logger.info("[C51] set DeviceID=["+deviceID+"] alert success");
		} else {
			logger.error("[C51] set DeviceID=["+deviceID+"] alert failed");
		}
	}
}
