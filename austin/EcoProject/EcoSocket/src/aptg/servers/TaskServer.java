package aptg.servers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.TaskHandle;
import aptg.manager.WebCmdManager;

public class TaskServer implements Runnable {
	
	private static final Logger logger = LogManager.getFormatterLogger(TaskServer.class.getName());

    private String mode;
	private ExecutorService threadPool;
	private int ThreadPoolCount;
	
	private int cmdIntervalTime;
	
	public TaskServer() {
		try {
			ResourceBundle dbConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get("db.properties")));
			this.mode = dbConfig.getString("db.mode");

			ResourceBundle config = new PropertyResourceBundle(Files.newInputStream(Paths.get("config.properties")));
			this.ThreadPoolCount = Integer.valueOf(config.getString(this.mode +".threadPoolCount.SenderServer"));

			this.cmdIntervalTime = Integer.valueOf(config.getString("webcmd.interval.time")) * 1000;
			
			this.threadPool = Executors.newFixedThreadPool(ThreadPoolCount);
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		sender();
	}

	private void sender() {
		while (true) {
            try {
    			String cmdMsg = WebCmdManager.getInstance().getWebCmd();
    			
    			if (StringUtils.isNotBlank(cmdMsg)) {
    	        	threadPool.execute(new TaskHandle(cmdMsg));
    			} else {
    				Thread.sleep(cmdIntervalTime);
    			}
    			
            } catch(Exception e) {
    			e.printStackTrace();
            }
		}
	}
}
