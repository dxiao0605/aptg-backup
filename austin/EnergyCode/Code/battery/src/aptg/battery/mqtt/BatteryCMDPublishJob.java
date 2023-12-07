package aptg.battery.mqtt;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.battery.bean.MqttConfigBean;


public class BatteryCMDPublishJob implements Runnable {

	private static final Logger logger = LogManager.getFormatterLogger(BatteryCMDPublishJob.class.getName());

	private static final int CHECK_TIME = 10;
	
	private String host;
	private MqttConfigBean config;
	private ExecutorService threadPool;

	public BatteryCMDPublishJob(String host, MqttConfigBean config, ExecutorService threadPool) throws URISyntaxException {
		this.host = host;
		this.config = config;

		this.threadPool = threadPool;
	}
	
	public void run() {
		while (true) {
            try {
				// query CommandTask table
//            	List<CommandTaskModel> tasks = DBQueryUtil.getInstance().queryCommandTask();
				
//				if (tasks.size()!=0) {
//					for (CommandTaskModel task: tasks) {
						threadPool.execute(new CMDPublishHandle( host, config));	
//					}
//				} else {
//	            	Thread.sleep(CHECK_TIME *1000);
//				}
            } catch(Exception e) {
    			e.printStackTrace();
            }
		}
	}
}
