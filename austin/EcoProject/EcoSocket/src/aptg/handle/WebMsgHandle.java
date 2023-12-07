package aptg.handle;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.function.FuncIDBase;
import aptg.manager.WebCmdManager;

public class WebMsgHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(WebMsgHandle.class.getName());

	private Socket socket;
	private String request;
	private FuncIDBase funcBase;
	
	public WebMsgHandle(Socket socket, String request, FuncIDBase funcBase) {
		this.socket = socket;
		this.request = request;
		this.funcBase = funcBase;
	}

	@Override
	public void run() {
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

}
