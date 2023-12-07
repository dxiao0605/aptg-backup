package aptg.handle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.function.FuncIDBase;
import aptg.function.FuncID_A;
import aptg.function.FuncID_C;
import aptg.function.FuncID_S;
import aptg.manager.SocketManager;
import aptg.manager.CheckManager;
import aptg.manager.WebCmdManager;

public class ReceiveMsgHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(ReceiveMsgHandle.class.getName());

	private static final String HeaderError = "*2;000;00000000;2001;\n";

	private FuncIDBase funcBase = new FuncIDBase();
	private PrintWriter writer;

	private Socket socket;
//	private String response;
	private Date recordTime;
	private boolean flag;

	public ReceiveMsgHandle(Socket socket) {
		this.socket = socket;
		this.flag = true;
	}

	@Override
	public void run() {
        try {
        	InputStream input = socket.getInputStream();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        	String request;
            
        	while ((request = reader.readLine()) != null)
            {
            	recordTime = new Date();
                
                if (StringUtils.isNotBlank(request)) {
                	logger.info("[Socket] receive message: " + request);
                	
                	if (request.contains("*")) {
                		/*
                		 * *1, *2 => from eco request or response
                		 */
                    	String eco5Account = funcBase.getEco5Account();
                    	if (StringUtils.isBlank(eco5Account)) {
                        	funcBase.setEco5Account(request);
                    	}
                    	
//                    	// get Eco5Account (=gatewayID)
//                    	funcBase.setEco5Account(request);
//                    	if (!StringUtils.isBlank(eco5Account) && !eco5Account.equals(funcBase.getEco5Account())) {
//                    		logger.info("[Socket] receive message, ECO5Account is changed. from ["+eco5Account+"] to ["+funcBase.getEco5Account()+"]");
//                    	}
                    	
                		SocketManager.getInstance().setSocket(funcBase.getEco5Account(), socket);	// update
                		CheckManager.getInstance().setRecordTime(funcBase.getEco5Account(), recordTime);	// 2020-11-10
                    	
                    	// parse request, make response
    					parseMessage(request);
                		
                	} else if (request.contains("#")) {
                		/*
                		 * #1 => from web cmd request
                		 */
                		parseWebCmd(request);
                	}
                }
            }
        } catch (SocketException e1) {
        	e1.printStackTrace();
        	logger.error("[SocketServer] SocketException: "+e1.getMessage());
        	closeIO();
        	
        } catch (IOException e2) {
        	e2.printStackTrace();
        	logger.error("[SocketServer] IOException: "+e2.getMessage());
        	closeIO();
		}
	}

	public void parseWebCmd(String request) {
//		int semicolonCount = StringUtils.countMatches(request, ";");
		
		String[] message = request.split(";");
		String func = message[1];	// S01
		String cmdSN = message[2];	// 00000001
		
		// #1;A50;00000004;gatewayID;deviceID
		WebCmdManager.getInstance().addWebCmd(request);
		
		// "*2;A50;00000004;0;\n";
		String response = funcBase.getResponse(func, cmdSN, funcBase.getSuccessCode(), null);
	    
		SendMsgHandle send = new SendMsgHandle(socket, response);
		send.sendMessage();
    	logger.info("[Socket] send web response: "+response);
	}
	
	/**
	 * 	拆解request內容，依功能檢查
	 * 	產出response string
	 *  
	 * @param request
	 * @return
	 */
	public void parseMessage(String request) {
		int semicolonCount = StringUtils.countMatches(request, ";");
		
		String[] message = request.split(";");	// *1;S01;00000001;ES990000000000TEST00;abc123;\n
		// ex: S01, A50
		String func = message[1];
//		String funcID = message[1].substring(0, 1);	// ex: S01, A50 => S, A
		
		try {
			if (func.contains("S")) {
				/*
				 * 系統功能 request
				 */
				// handler request and generate response
				FuncID_S func_s = new FuncID_S(socket);
				func_s.systemFunc(message, semicolonCount, request);
				func_s = null;
			}
			else if (func.contains("A")) {
				/*
				 * 查詢指令 request or response
				 */
				// 1. 可能為 由Eco5送來request詢問server所設定的需量參數
				// 2. 可能為 詢問Eco5回復他所設定的需量參數
				FuncID_A func_a = new FuncID_A(socket);
				func_a.queryCmd(message);	// "#1;xxx" or "*1;xxx" or "*2;xxx"
				func_a = null;
			}
			else if (func.contains("B")) {
				
			}
			else if (func.contains("C")) {
				/*
				 * 參數設定 response
				 */
				FuncID_C func_c = new FuncID_C(socket);
				func_c.setParameter(message);	// 設定參數，eco5回傳的response
				func_c = null;
			}
			else if (func.contains("D")) {
				/*
				 * 閘道特殊指令 response
				 */
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
        	logger.error("[Request] request: "+request+", Exception: "+e.getMessage());
			closeIO();
		}
	}
	
	private void closeIO() {
		try {
			// 執行斷線
			SocketTaskHandle task = new SocketTaskHandle(socket, recordTime, funcBase.getEco5Account());
			task.clientDisconnection();
			
			// remove socket cache
			SocketManager.getInstance().removeSocket(funcBase.getEco5Account());

		} catch(Exception e) {
            e.printStackTrace();
        	logger.error("Exception: "+e.toString());
		} finally {
			try {
				if (writer!=null) {
					writer.close();
					writer = null;
				}
				if (socket!=null) {
					socket.close();
					socket = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			funcBase = null;
        	flag = false;
		}
	}
}
