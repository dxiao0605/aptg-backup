package aptg.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.ControllerSetupDao;
import aptg.models.ControllerSetupModel;
import aptg.utils.DBContentDealUtil;

public class ControllerSetupManager {
	
	private Map<String, ControllerSetupModel> ctrlMap = new HashMap<>();	// key: eco5Account

	private static ControllerSetupManager instances;
	private ControllerSetupManager() {}
	
	public static ControllerSetupManager getInstance() {
		if (instances==null) {
			synchronized (ControllerSetupManager.class) {
				if (instances==null) {
					instances = new ControllerSetupManager();
				}
			}
		}
		return instances;
	}
	
	public ControllerSetupModel getControllerSetup(String eco5Account) {
		if (ctrlMap.containsKey(eco5Account)) {
			return ctrlMap.get(eco5Account);
		} else {
			updateEco5Account(eco5Account);
			return ctrlMap.get(eco5Account);
		}
	}

	public void init() {
		try {
			ControllerSetupDao dao = new ControllerSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledEco5();
			if (rows.size()!=0) {
				List<ControllerSetupModel> list = DBContentDealUtil.getControllerSetupList(rows);

				for (ControllerSetupModel ctrl: list) {
					ctrlMap.put(ctrl.getEco5Account(), ctrl);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateEco5Account(String eco5Account) {
		try {
			ControllerSetupDao dao = new ControllerSetupDao();
			List<DynaBean> rows = dao.queryEnabledEco5(eco5Account);
			if (rows.size()!=0) {
				List<ControllerSetupModel> list = DBContentDealUtil.getControllerSetupList(rows);

				for (ControllerSetupModel ctrl: list) {
					ctrlMap.put(ctrl.getEco5Account(), ctrl);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
