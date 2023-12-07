package aptg.manager;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.SocketTaskHandle;

public class CheckManager {
	
	private static final Logger logger = LogManager.getFormatterLogger(CheckManager.class.getName());

	private Map<String, Date> timerMap = new HashMap<>();
	
	private static final int TIME_OUT 	= 5;	// 5min
	private static final int MINUTE		= 60*1000;
	
	private static CheckManager instances;
	
	public static CheckManager getInstance() {
		if (instances==null) {
			synchronized (CheckManager.class) {
				if (instances==null) {
					instances = new CheckManager();
				}
			}
		}
		return instances;
	}
	
	public void initCheck() {
		while (true) {
			// 每一分鐘檢查一次
			try {
				Thread.sleep(1*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// check timeout
			checkRecordTime();
		}
	}

	public void setRecordTime(String eco5Account, Date recordTime) {
		synchronized (timerMap) {
			timerMap.put(eco5Account, recordTime);	
		}
	}
	
	private void checkRecordTime() {
		for (String eco5Account: timerMap.keySet()) {
			Date recordTime = timerMap.get(eco5Account);
			long diff = (new Date().getTime() - recordTime.getTime());
			if (diff>TIME_OUT*MINUTE){
				Socket socket = SocketManager.getInstance().getSocket(eco5Account);
				
				if (socket==null)
					continue;
				
				logger.info("[Timeout] ECO5Account=["+eco5Account+"], Socket=["+socket+"] is Timeout...");
				
				// disconnect socket
				SocketTaskHandle handle = new SocketTaskHandle(socket, recordTime, eco5Account);
				handle.clientDisconnection();

				// remove socket cache
				SocketManager.getInstance().removeSocket(eco5Account);
			}
		}
	}
}
