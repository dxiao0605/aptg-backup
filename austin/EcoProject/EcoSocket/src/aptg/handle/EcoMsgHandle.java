package aptg.handle;

import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.function.FuncIDBase;
import aptg.function.FuncID_A;
import aptg.function.FuncID_C;
import aptg.function.FuncID_S;
import aptg.manager.CheckManager;
import aptg.manager.SocketManager;

public class EcoMsgHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(EcoMsgHandle.class.getName());
	
	private Socket socket;
	private String request;
	private FuncIDBase funcBase;
	
	public EcoMsgHandle(Socket socket, String request, FuncIDBase funcBase) {
		this.socket = socket;
		this.request = request;
		this.funcBase = funcBase;
	}

	@Override
	public void run() {
		/*
		 * *1, *2 => from eco request or response
		 */
		SocketManager.getInstance().setSocket(funcBase.getEco5Account(), socket);	// update
		CheckManager.getInstance().setRecordTime(funcBase.getEco5Account(), funcBase.getRecordTime());	// 2020-11-10
    	
    	// parse request, make response
		parseMessage(request);
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
		
		try {
			if (func.contains("S")) {
				/*
				 * 系統功能 request
				 */
				// handler request and generate response
				FuncID_S func_s = new FuncID_S(socket);
				func_s.systemFunc(message, semicolonCount, request);
				
			}
			else if (func.contains("A")) {
				/*
				 * 查詢指令 request or response
				 */
				// 1. 可能為 由Eco5送來request詢問server所設定的需量參數
				// 2. 可能為 詢問Eco5回復他所設定的需量參數
				FuncID_A func_a = new FuncID_A(socket);
				func_a.queryCmd(message);	// "#1;xxx" or "*1;xxx" or "*2;xxx"
				
			}
			else if (func.contains("B")) {
				
			}
			else if (func.contains("C")) {
				/*
				 * 參數設定 response
				 */
				FuncID_C func_c = new FuncID_C(socket);
				func_c.setParameter(message);	// 設定參數，eco5回傳的response
				
			}
			else if (func.contains("D")) {
				/*
				 * 閘道特殊指令 response
				 */
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
        	logger.error("[Request] request: "+request+", Exception: "+e.getMessage());
//			closeIO();
		}
	}
}
