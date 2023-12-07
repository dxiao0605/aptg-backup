package aptg;

import aptg.task.DailyStatusTask;

public class BatteryTask {

	public static void main(String[] args) {
//		try {
//			OfflineTask.main(args);	
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			DailyStatusTask.main(args);

//			DailyStatusTask2.main(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
