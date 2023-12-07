package aptg.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.ReceiveMsgHandle;

public class ReceiverServer implements Runnable {
	
	private static final Logger logger = LogManager.getFormatterLogger(ReceiverServer.class.getName());

    private static ServerSocket server;

    private String mode;
	private ExecutorService threadPool;
	private int SocketPort;
	private int ThreadPoolCount;
	
	public ReceiverServer() {
		try {
			ResourceBundle dbConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get("db.properties")));
			this.mode = dbConfig.getString("db.mode");

			ResourceBundle config = new PropertyResourceBundle(Files.newInputStream(Paths.get("config.properties")));
			this.SocketPort = Integer.valueOf(config.getString(this.mode +".server.socketPort"));
			
			this.ThreadPoolCount = Integer.valueOf(config.getString(this.mode +".threadPoolCount.ReceiverServer"));
			this.threadPool = Executors.newFixedThreadPool(ThreadPoolCount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		logger.info("[SocketServer] server is ready! port: "+SocketPort +", ThreadPool count: "+ ThreadPoolCount);
		receiver();
	}
	
	private void receiver() {
        try {
            server = new ServerSocket(SocketPort);
//        	server.setSoTimeout(Timeout);

        	while (true) {
                try {
            		Socket socket = null;
                    // accept啟動listener方法開始監聽，等待客戶端連線
            		synchronized (server) {
            			socket = server.accept();
            		}

    				// thread to handle request & response
    	        	threadPool.execute(new ReceiveMsgHandle(socket));
		        	
                } catch (Exception e) {
        			e.printStackTrace();
                    logger.error("[SocketServer] Exception: "+e.getMessage());
                }
        	}
        } catch (IOException e) {
			e.printStackTrace();
            logger.error("[SocketServer] 啟動有問題 ! Exception: "+e.getMessage());
        }
	}
}
