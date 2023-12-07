package aptg.manager;

import java.util.HashMap;
import java.util.Map;

import aptg.model.EventModel;

public class EventManager {

//	private Map<String, EventModel> disconnectMap = new HashMap<>();	// key: nbID+_+batteryID
//	
//	private static EventManager instances;
//	public static EventManager getInstance() {
//		if (instances==null) {
//			instances = new EventManager();
//		}
//		return instances;
//	}
//	
//	public void init() {
//		
//	}
//	public synchronized void setDisconnectedEvent(String nbIDbattID, EventModel event) {
//		disconnectMap.put(nbIDbattID, event);
//	}
//	public EventModel getDisconnectedEvent(String nbIDbattID) {
//		if (disconnectMap.containsKey(nbIDbattID)) {
//			return disconnectMap.get(nbIDbattID);
//		}
//		return null;
//	}
}
