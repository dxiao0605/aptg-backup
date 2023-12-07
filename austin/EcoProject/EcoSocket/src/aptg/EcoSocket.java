package aptg;

import aptg.handle.DeviceHandle;
import aptg.manager.CheckManager;
import aptg.manager.SysConfigManager;
import aptg.servers.ReceiverServer;
import aptg.servers.TaskServer;

/**
 * 
 * @author austinchen
 *
 *	程式執行起始點
 */
public class EcoSocket {
    
	public static void main(String[] args) {
		// assign execute mode when startup
		execute();
	}
	
	private static void execute() {
		SysConfigManager.getInstance().init();
		
		Thread dm = new Thread(new DeviceHandle());
		dm.start();
		
		Thread receiver = new Thread(new ReceiverServer());
		receiver.start();

//		Thread task = new Thread(new TaskServer());
//		task.start();
		
		CheckManager.getInstance().initCheck();
	}
}
