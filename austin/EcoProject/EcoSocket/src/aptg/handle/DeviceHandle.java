package aptg.handle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.manager.ControllerSetupManager;
import aptg.manager.MeterSetupManager;

public class DeviceHandle implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(DeviceHandle.class.getName());

	private static final int INTERVAL_TIME = 60*60;	// 60min
	
	public DeviceHandle() {
		
	}

	@Override
	public void run() {
		while (true) {
			logger.info("Update ControllerSetup & MeterSetup info Start!!!");
			ControllerSetupManager.getInstance().init();
			MeterSetupManager.getInstance().init();
			logger.info("Update ControllerSetup & MeterSetup info End!!!");
			
        	try {
				Thread.sleep(INTERVAL_TIME *1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
