package aptg.handle;

import java.sql.SQLException;
import java.util.List;

import aptg.dao.PowerRecordDao;
import aptg.models.PowerRecordModel;

public class S25Handle implements Runnable {
	
	private PowerRecordModel power;

	public S25Handle(PowerRecordModel power) {
		this.power = power;
	}
	
	@Override
	public void run() {
		// ************* insert db
		PowerRecordDao dao = new PowerRecordDao();
		try {
			List<Integer> ids = dao.insertPowerRecord(power);
			power = null;
			dao = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
