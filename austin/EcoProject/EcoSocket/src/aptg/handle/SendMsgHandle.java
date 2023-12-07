package aptg.handle;

import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.function.FuncID_S;

public class SendMsgHandle {

	private static final Logger logger = LogManager.getFormatterLogger(SendMsgHandle.class.getName());
	
	private Socket socket;
	private String message;
	
	public SendMsgHandle(Socket socket, String message) {
		this.socket = socket;
		this.message = message;
	}

	public void sendMessage() {
        try {
            // ******* (1)
//        	BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
//        	out.write(message.getBytes());
//            out.flush();
        	
            // ******* (2)
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            out.flush();	// add 12/25
            
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	logger.error("[Response] Send Message: "+message +", Exception: "+e.getMessage() );
		}
	}
}
