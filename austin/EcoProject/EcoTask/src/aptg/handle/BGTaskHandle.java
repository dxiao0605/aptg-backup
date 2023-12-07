package aptg.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.handle.base.BaseTaskHandle;
import aptg.models.BGTaskModel;
import aptg.task.CheckPowerAccountTask;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;

public class BGTaskHandle extends BaseTaskHandle {
	
	private static final String CLASS_NAME = CheckPowerAccountTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	private boolean isExecuted = true;
	
	public boolean isExecuted() {
		return isExecuted;
	}
	
	/**
	 *	修改、刪除電號
	 */
	public void checkBGTask() {
		List<BGTaskModel> list = getBGTask(TaskStatusCode_Executing);
		if (list.size()==0) {	// 已無處理中的task，此次排程可執行
			// 取出未處理的task
			list = getBGTask(TaskStatusCode_Waiting);
			logger.info("BGTask Waiting task size: "+list.size() +", StatusCode: 0=>1");
			
			// 1. 更新狀態 => 執行中: 1
			updateBGTaskStatus(list, TaskStatusCode_Executing);
			
			// 2. 判斷此為 修改 or 刪除 電號
			for (BGTaskModel bg: list) {
				String taskType = bg.getBGTaskType();
				
				boolean isSuccess = true;
				if (taskType.equals(TaskType_Del)) {
					logger.info("BGTask 刪除電號: " + JsonUtil.getInstance().convertObjectToJsonstring(bg));
					isSuccess = doDelPA(bg);
				} else {
					logger.info("BGTask 修改電號: " + JsonUtil.getInstance().convertObjectToJsonstring(bg));
					isSuccess = doFixPA(bg);
				}
			}

		} else {
			logger.info("BGTask未執行，尚有 "+list.size()+" 個排程執行中 ===> ");
			for (BGTaskModel bg: list) {
				logger.info("Job is executing: "+JsonUtil.getInstance().convertObjectToJsonstring(bg));
			}
			
			// not execute
			isExecuted = false;
		}
	}
	
	/**
	 * 1. copy PowerAccount => insert DeletedPowerAccout
	 * 2. copy PowerAccountHistory => insert DeletedPowerAccountHistory
	 * 3. delete FcstCharge, PowerMonth, BestRatePlan, BestCC, PowerAccountMaxDemand, EntryBill, PowerAccountHistory, PowerAccount
	 * 
	 * @param bg
	 * @return
	 */
	public boolean doDelPA(BGTaskModel bg) {
		List<BGTaskModel> list = new ArrayList<>();
		list.add(bg);
		
		String powerAccount = bg.getPowerAccountOld();
		logger.info("刪除電號處理中 PowerAccount=["+powerAccount+"], BGTask: "+JsonUtil.getInstance().convertObjectToJsonstring(bg));
		
		int count = DBQueryUtil.getInstance().deleteBGTaskInfo(powerAccount);
		if (count!=-1) {
			logger.info("刪除電號完成 PowerAccount=["+powerAccount+"]");
			updateBGTaskStatus(list, TaskStatusCode_Finish);
			return true;
		}

		logger.error("刪除電號失敗 PowerAccount=["+powerAccount+"]");
		updateBGTaskStatus(list, TaskStatusCode_Failed);
		sendBGTaskFailed(list);
		return false;
	}
	
	/**
	 * 1. update "PowerAccount" in Table: FcstCharge, PowerMonth, BestRatePlan, BestCC, PowerAccountMaxDemand, EntryBill, PowerAccountHistory, PowerAccount
	 * 
	 * @param bg
	 * @return
	 */
	public boolean doFixPA(BGTaskModel bg) {
		List<BGTaskModel> list = new ArrayList<>();
		list.add(bg);
		
		String oldPowerAccount = bg.getPowerAccountOld();
		String newPowerAccount = bg.getPowerAccountNew();
		logger.info("修改電號處理中 PowerAccount=["+oldPowerAccount+"] to PowerAccount=["+newPowerAccount+"], BGTask: "+JsonUtil.getInstance().convertObjectToJsonstring(bg));
	
		int count = DBQueryUtil.getInstance().updateBGTaskInfo(oldPowerAccount, newPowerAccount, ModifyStatus_Default);
		if (count!=-1) {
			updateBGTaskStatus(list, TaskStatusCode_Finish);
			logger.info("修改電號完成 PowerAccount=["+oldPowerAccount+"] to PowerAccount=["+newPowerAccount+"]");
			return true;
		}

		updateBGTaskStatus(list, TaskStatusCode_Failed);
		sendBGTaskFailed(list);
		logger.info("修改電號失敗 PowerAccount=["+oldPowerAccount+"] to PowerAccount=["+newPowerAccount+"], BGTask: "+JsonUtil.getInstance().convertObjectToJsonstring(bg));
		return false;
	}

	
	
	/**
	 *	依statusCode查詢BGTaskList
	 * 
	 * @param statusCode
	 * @return
	 */
	private List<BGTaskModel> getBGTask(int statusCode) {
		List<BGTaskModel> list = DBQueryUtil.getInstance().queryBGTask(statusCode);
		return list;
	}
}
