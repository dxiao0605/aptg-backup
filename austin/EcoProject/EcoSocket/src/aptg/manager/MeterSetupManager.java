package aptg.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.MeterSetupDao;
import aptg.models.MeterSetupModel;
import aptg.utils.DBContentDealUtil;

public class MeterSetupManager {
	
	private Map<String, MeterSetupModel> meterMap = new HashMap<>();	// key: deviceID

	private static MeterSetupManager instances;
	private MeterSetupManager() {}
	
	public static MeterSetupManager getInstance() {
		if (instances==null) {
			synchronized (MeterSetupManager.class) {
				if (instances==null) {
					instances = new MeterSetupManager();
				}
			}
		}
		return instances;
	}
	
	public MeterSetupModel getMeterSetup(String deviceID) {
		if (meterMap.containsKey(deviceID)) {
			return meterMap.get(deviceID);
		} else {
			updateMeterSetup(deviceID);
			return meterMap.get(deviceID);
		}
	}
	
	public void init() {
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledMeter();
			if (rows.size()!=0) {
				List<MeterSetupModel> list = DBContentDealUtil.getMeterSetupList(rows);
				
				for (MeterSetupModel meter: list) {
					meterMap.put(meter.getDeviceID(), meter);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void updateMeterSetup(String deviceID) {
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryEnabledMeter(deviceID);
			if (rows.size()!=0) {
				List<MeterSetupModel> list = DBContentDealUtil.getMeterSetupList(rows);
				
				for (MeterSetupModel meter: list) {
					meterMap.put(meter.getDeviceID(), meter);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
