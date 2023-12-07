package aptg;

import aptg.battery.BatteryInitalize;

public class BatteryServer {

	public static void main(String[] args) {
//		System.out.println("start BatteryServer");
		BatteryInitalize battery = new BatteryInitalize();
		battery.init();
	}

}
