package aptg.handle.base;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import aptg.models.BGTaskModel;
import aptg.models.RepriceTaskModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.EmailUtil;

public class BaseTaskHandle {

	// BGTask & Reprice Status
	public static final int TaskStatusCode_Waiting 		= 0;	// 待處理
	public static final int TaskStatusCode_Executing 	= 1;	// 處理中
	public static final int TaskStatusCode_Finish 		= 2;	// 完成
	public static final int TaskStatusCode_Cancel 		= 3;	// 取消
	public static final int TaskStatusCode_Failed 		= 4;	// 失敗

	// MeterSetup
	public static final int RepriceStatus_Default		= 0;	// 預設，可編輯
	public static final int RepriceStatus_Repricing		= 1;	// 重新計算中，不可編輯
	// PowerAccount
	public static final int ModifyStatus_Default 		= 0;	// 預設，可編輯
	public static final int ModifyStatus_Deleting 		= 1;	// 刪除中
	public static final int ModifyStatus_Changing 		= 2;	// 修改中
	public static final int ModifyStatus_Calculating 	= 3;	// 重算中
	
	public static final String TaskType_Del	= "Del_PA";
	public static final String TaskType_Fix	= "Fix_PA";

	public static final String RepriceFrom_Collection = "Collection";
	public static final String RepriceFrom_FcstCharge = "FcstCharge";

	/**
	 * update BGTask Status
	 * 
	 * 0: 未處理
	 * 1: 處理中
	 * 2: 已完成
	 * 3: 已取消
	 * 4: 失敗
	 * 
	 * @param list
	 * @param statusCode
	 */
	public void updateBGTaskStatus(List<BGTaskModel> list, int statusCode) {
		DBQueryUtil.getInstance().updateBGTaskStatus(list, statusCode);
	}

	/**
	 * update RepriceTask Status
	 * 
	 * 0: 未處理
	 * 1: 處理中
	 * 2: 已完成
	 * 3: 已取消
	 * 4: 失敗
	 * 
	 * @param list
	 * @param statusCode
	 */
	public void updateRepriceTaskExecuting(List<RepriceTaskModel> list) {
		DBQueryUtil.getInstance().updateRepriceTaskExecuting(list);
	}
	public void updateRepriceTaskCancel(List<RepriceTaskModel> list) {
		DBQueryUtil.getInstance().updateRepriceTaskCancel(list);
	}
	public void updateRepriceTaskStatus(int statusCode, String deviceID, String powerAccount, String startDate) {
		DBQueryUtil.getInstance().updateRepriceTaskStatus(statusCode, deviceID, powerAccount, startDate);
	}

	/**
	 * update MeterSetup repriceStatus:編輯狀態更新
	 * 0: 目前不在重新統計狀態，畫面可編輯
	 * 1: 目前正在重新統計狀態，畫面不可編輯
	 * 
	 * @param powerAccount
	 * @param repriceStatus
	 */
	public void updateMeterSetupRepriceStatus(String deviceID, int repriceStatus) {
		DBQueryUtil.getInstance().updateMeterSetupRepriceStatus(deviceID, repriceStatus);
	}
	/**
	 * update PowerAccount modifyStatus:異動狀態更新
	 * 0: 一般狀態 可編輯 (Default)
	 * 1: 刪除中
	 * 2: 電號異動中
	 * 3: 電費重新計算中
	 * 
	 * @param modifyStatus
	 */
	public void updatePowerAccountModifyStatus(String powerAccount, int modifyStatus) {
		DBQueryUtil.getInstance().updatePowerAccountModifyStatus(powerAccount, modifyStatus);
	}
	
	public void sendBGTaskFailed(List<BGTaskModel> list) {
		for (BGTaskModel bg: list) {
			String taskType = bg.getBGTaskType();

			String subject = null, body = null;
			
			switch (taskType) {
				case TaskType_Del:
					subject = "排程刪除電號BGTask失敗通知 PowerAccount: "+bg.getPowerAccountOld();
					body = "PowerAccount: "+bg.getPowerAccountOld() +" 刪除發生錯誤...";
					break;
					
				case TaskType_Fix:
					subject = "排程修改電號BGTask失敗通知 PowerAccount: "+bg.getPowerAccountOld();
					body = "PowerAccount: "+bg.getPowerAccountOld() +" 修改 PowerAccount: "+bg.getPowerAccountNew()+" 發生錯誤...";
					break;
			}
			
			EmailUtil email = new EmailUtil();
			email.setSubject(subject);
			email.setBody(body);
			try {
//				System.out.println("########### Send Mail Start ############");
//				System.out.println("subject: "+subject);
//				System.out.println("body: "+body);
//				System.out.println("########### Send Mail End ############");
				email.sendMail();
			} catch (Exception e) {
//				logger.error("Send Email Failed..: "+body);
				e.printStackTrace();
			}
		}
	}
	public void sendRepriceTaskFailed(RepriceTaskModel rp, String description) {
		String deviceID = rp.getDeviceID();
		String powerAccount = rp.getPowerAccount();
		String repriceFrom = rp.getRepriceFrom();
		
		String subject = null, body = null;
		
		switch (repriceFrom) {
			case RepriceFrom_Collection:
				subject = "排程重算電費RepriceTask失敗通知 PowerAccount: "+powerAccount;
				body = "PowerAccount: "+powerAccount +", DeviceID: "+deviceID+", RepriceFrom: "+repriceFrom +" 發生錯誤...";
				break;
				
			case RepriceFrom_FcstCharge:
				subject = "排程重算電費RepriceTask失敗通知 PowerAccount: "+powerAccount;
				if (StringUtils.isBlank(description))
					body = "PowerAccount: "+powerAccount +", DeviceID: "+deviceID+", RepriceFrom: "+repriceFrom +" 發生錯誤...";
				else
					body = "PowerAccount: "+powerAccount +", DeviceID: "+deviceID+", RepriceFrom: "+repriceFrom +", "+description+" 發生錯誤...";
				break;
		}
		
		EmailUtil email = new EmailUtil();
		email.setSubject(subject);
		email.setBody(body);
		try {
//			System.out.println("########### Send Mail Start ############");
//			System.out.println("subject: "+subject);
//			System.out.println("body: "+body);
//			System.out.println("########### Send Mail End ############");
			email.sendMail();
		} catch (Exception e) {
//			logger.error("Send Email Failed..: "+body);
			e.printStackTrace();
		}
	}
}
