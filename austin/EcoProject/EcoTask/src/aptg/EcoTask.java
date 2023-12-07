package aptg;

import aptg.task.PowerRecordCollectionTask;
import aptg.task.TimeDailyTask;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.manager.SysConfigManager;
import aptg.models.SysConfigModel;
import aptg.task.BestCCTask;
import aptg.task.BestRatePlanTask;
import aptg.task.FcstChargeTask;
import aptg.task.KPITask;
import aptg.task.MaxDemandTask;
import aptg.task.PowerMonthTask;

public class EcoTask {

	private static final Logger logger = LogManager.getFormatterLogger(EcoTask.class.getName());
	private static final int Sleep_Time = 30;
	
	private static final String Sysconfig_BGJobIsRunning	 	= "BGRepriceTaskIsRunning";
	
	public static void main(String[] args) {

		int isRunning = queryBGJobIsRunning();
		while(isRunning==1) {
			logger.info("每日排程未執行，尚有 BGTask & RepriceTask 排程 正在執行中");
			
			try {
				Thread.sleep(30*1000);	// 30sec
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			isRunning = queryBGJobIsRunning();
		}

		logger.info("Daily Job Start !"+"\r\n");
		
		// 正常排程開始執行 => 更新Sysconfig.JobIsRunning flag=1
		SysConfigManager.getInstance().updateJobIsRunningFlag(1);
		
		TimeDailyTask.main(args);

		sleep(PowerRecordCollectionTask.class.getName(), args);
		PowerRecordCollectionTask.main(args);
		
		sleep(FcstChargeTask.class.getName(), args);
		FcstChargeTask.main(args);
		
		sleep(KPITask.class.getName(), args);
		KPITask.main(args);
				
		sleep(PowerMonthTask.class.getName(), args);
		PowerMonthTask.main(args);
		
		sleep(BestRatePlanTask.class.getName(), args);
		BestRatePlanTask.main(args);
		
		sleep(BestCCTask.class.getName(), args);
		BestCCTask.main(args);
		
		sleep(MaxDemandTask.class.getName(), args);
		MaxDemandTask.main(args);
		
		// 正常排程執行結束 => 更新Sysconfig.JobIsRunning flag=0
		SysConfigManager.getInstance().updateJobIsRunningFlag(0);

		logger.info("Daily Job End !"+"\r\n");
	}
	
	public static void sleep(String className, String[] args) {
		if (args.length<2) {
			try {
				logger.info("Wait "+Sleep_Time+"sec to execute: "+className +"\r\n");
				Thread.sleep(Sleep_Time * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int queryBGJobIsRunning() {
		Map<String, SysConfigModel> configMap = SysConfigManager.getInstance().querySysConfig(Sysconfig_BGJobIsRunning);
		String flag = configMap.get(Sysconfig_BGJobIsRunning).getParamvalue();
		int isRunning = Integer.valueOf(flag);
		return isRunning;
	}
}
