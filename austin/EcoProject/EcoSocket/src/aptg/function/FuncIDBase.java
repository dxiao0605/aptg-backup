package aptg.function;

import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import aptg.handle.SendMsgHandle;
import aptg.handle.SocketTaskHandle;
import aptg.manager.SocketManager;

public class FuncIDBase extends FuncIDValid {

	/*
	 * REQUEST
	 * ex: *1;A50;00000004;ECO-CATHAYBK--0117--;IN11CATHAYBK--011117;
	 */
	private static final String RequestCode 		= "*1";
	private static final String RequestFunc 		= "{FUNC}";
	private static final String RequestCMDSN 		= "00000004";
	private static final String RequestGatewayID	= "{GATEWAY_ID}";
	private static final String RequestDeviceID		= "{DEVICE_ID}";

	protected String Request;
	
	/*
	 * RESPONSE
	 */
	private static final String ResponseCode 		= "*2";
	private static final String ResponseFunc 		= "{FUNC}";
	private static final String ResponseCMDSN 		= "{CMDSN}";
	private static final String ResponseResultCode 	= "{RESULT_CODE}";
	
	protected static final String SUCCESS_CDE 		= "0";
	protected static final String HEADER_ERROR 		= "*2;000;00000000;2001;\n";

	protected static Map<Socket, Timer> timerMap = new HashMap<Socket, Timer>();
	protected String Response;
	protected Socket socket;
	protected Date recordTime;
	
	protected String eco5Account;

	public String getEco5Account() {
		return eco5Account;
	}
	public void setEco5Account(String request) {
		String[] str = request.split(";");	// *1;S01;00000001;ES990000000000TEST00;abc123;\n
		this.eco5Account = str[3];	// ES990000000000TEST00
	}
	
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	public String getMsgSOFType(String request) {
		String[] str = request.split(";");	// #1;S01;00000001;ES990000000000TEST00;abc123;\n
		return str[0];	// #1
	}
	
	/**
	 * 至ECO5查詢的Request
	 * 
	 * @param func
	 * @param gatewayID
	 * @param deviceID
	 * @return
	 */
	public String getRequest(String func, String gatewayID, String deviceID) {
		Request = RequestCode +";"+
				  RequestFunc +";"+
				  RequestCMDSN +";"+
				  RequestGatewayID +";"+
				  RequestDeviceID +";";

		return Request.replace(RequestFunc, func)
					  .replace(RequestGatewayID, gatewayID)
					  .replace(RequestDeviceID, deviceID)
					  +"\n";
	}
	
	/**
	 * 
	 * @param func
	 * @param cmdSN
	 * @param validResult
	 * @param timestamp
	 * @return
	 */
	public String getResponse(String func, String cmdSN, String validResult, String timestamp) {
		Response = ResponseCode +";"+ 
				   ResponseFunc +";"+
				   ResponseCMDSN +";"+
				   ResponseResultCode +";";
		
		if (timestamp==null) {
			return Response.replace(ResponseFunc, func)
						   .replace(ResponseCMDSN, cmdSN)
						   .replace(ResponseResultCode, validResult)
						   + "\n";
		} else {
			return Response.replace(ResponseFunc, func)
						   .replace(ResponseCMDSN, cmdSN)
						   .replace(ResponseResultCode, validResult)
					       + timestamp +";"
						   + "\n";
		}
	}

	/**
	 * Request sof+type是否為*1
	 *  
	 * @param message
	 * @return
	 */
	public String requestSOFType(String[] message) {
		String sofType = message[0];	// *1
		if (!sofType.equals("*1")) {
			return HEADER_ERROR;
		}
		return null;
	}

	/**
	 * Response sof+type是否為*2
	 * @param message
	 * @return
	 */
	public String responseSOFType(String[] message) {
		String sofType = message[0];	// *2
		if (!sofType.equals("*2")) {
			return HEADER_ERROR;
		}
		return null;
	}
	
	/**
	 * 	取得SOFType
	 * ex: *1=>eco request, *2=>response, #1=>web request
	 * 
	 * @param message
	 * @return
	 */
	public String msgSOFType(String[] message) {
		String sofType = message[0];	// *1 or *2
		return sofType;
	}
	
	/**
	 *	取得API func代碼
	 * ex: S25=>25, A50=>50, ...
	 *
	 * @param message
	 * @return
	 */
	public String msgFuncNum(String[] message) {
		String funcNum = message[1].substring(1, message[1].length());	// ex: 50
		return funcNum;
	}
	
	/**
	 *	設定task timeout斷掉socket連線
	 *  
	 * @param func
	 */
//	public void timerCloseSocket(String func) {
//		if (eco5Account==null)
//			return;
//		
//		if (timerMap.containsKey(socket)) {
//			// 取消前一個timer
//			synchronized (timerMap) {
//				Timer timer = timerMap.get(socket);
//				timer.cancel();
//				timerMap.remove(socket);
//			}
//		}
//
//		// 計算時間
//		Calendar countdown = Calendar.getInstance();
//		countdown.setTime(recordTime);
//		if (func.contentEquals("S01")) {
//			countdown.add(Calendar.SECOND, 60);
//		} else {
//			countdown.add(Calendar.MINUTE, 5);
//		}
//		
//		// 依func設定新的timer
//		Timer timer = new Timer();
//		timer.schedule(new SocketTaskHandle(socket, recordTime, eco5Account, null), countdown.getTime());
//		synchronized (timerMap) {
//			timerMap.put(socket, timer);	// 記錄timer
//		}
//		
//		// remove socket cache
//		SocketManager.getInstance().removeSocket(eco5Account);
//	}

	
	public void sendSocketMsg(Socket socket, String response) {
        // send response to client
		SendMsgHandle resp = new SendMsgHandle(socket, response);
		resp.sendMessage();
		resp = null;
	}
}
