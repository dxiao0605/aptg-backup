package aptg.handle;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketTaskHandle {
	
	private static final Logger logger = LogManager.getFormatterLogger(SocketTaskHandle.class.getName());

	private Socket socket;
	private Date recordTime;
	private String eco5Account;
	
	public SocketTaskHandle(Socket socket, Date recordTime, String eco5Account) {
		this.socket = socket;
		this.recordTime = recordTime;
		this.eco5Account = eco5Account;
	}

	public void clientDisconnection() {
		try {
        	logger.info("[SocketTask] socket is closing, eco5Account=["+eco5Account+"], last recordTime=["+recordTime+"]");

			// 關閉socket
			if (!socket.isClosed())
				socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
        	logger.error("[SocketTask] exception: "+ e.toString());
		}
	}
}
