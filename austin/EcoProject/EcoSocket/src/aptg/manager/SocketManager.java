package aptg.manager;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketManager {
	
	private Map<String, Socket> ecoSocketMap = new HashMap<>();

	private static SocketManager instances;
	private SocketManager() {}
	
	public static SocketManager getInstance() {
		if (instances==null) {
			synchronized (SocketManager.class) {
				if (instances==null) {
					instances = new SocketManager();
				}
			}
		}
		return instances;
	}
	
	public synchronized void setSocket(String eco5Account, Socket socket) {
		ecoSocketMap.put(eco5Account, socket);
	}
	
	public Socket getSocket(String eco5Account) {
		if (ecoSocketMap.containsKey(eco5Account)) {
			return ecoSocketMap.get(eco5Account);
		}
		return null;
	}
	
	public synchronized void removeSocket(String eco5Account) {
		if (ecoSocketMap.containsKey(eco5Account)) {
			ecoSocketMap.remove(eco5Account);
		}
	}
}
