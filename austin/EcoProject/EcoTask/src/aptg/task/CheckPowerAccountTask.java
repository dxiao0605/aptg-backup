package aptg.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.BGTaskHandle;
import aptg.handle.RepriceTaskHandle;
import aptg.manager.SysConfigManager;
import aptg.task.base.BaseFunction;

public class CheckPowerAccountTask extends BaseFunction {
	
	private static final String CLASS_NAME = CheckPowerAccountTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final String Sysconfig_JobIsRunning	 		= "DailyJobIsRunning";

	public static void main(String[] args) {
		// 檢查process有無正在執行的EcoTask.jar
		String flag = SysConfigManager.getInstance().getSysconfig(Sysconfig_JobIsRunning);
		if (flag.equals("1")) {
			logger.info("BGTask & RepriceTask未執行，尚有 每日凌晨排程 正在執行中");
			
		} else {
			SysConfigManager.getInstance().updateBGRepriceTaskRunningFlag(1);
			
			logger.info("BGTask Start !");
			BGTaskHandle bg = new BGTaskHandle();
			bg.checkBGTask();
			logger.info("BGTask End !"+ "\r\n");
			

			logger.info("RepriceTask Start !");
			RepriceTaskHandle rp = new RepriceTaskHandle();
			rp.checkRepriceTask();
			logger.info("RepriceTask End !"+ "\r\n");

			if (bg.isExecuted() && rp.isExecuted()) {
				logger.info("BGTask & RepriceTask was executed, update SysConfig BGRepriceTaskIsRunning = 0");
				SysConfigManager.getInstance().updateBGRepriceTaskRunningFlag(0);	
			}
		}
	}
}
