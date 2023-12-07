package aptg.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.SysConfigDao;
import aptg.models.SysConfigModel;
import aptg.utils.DBContentDealUtil;

public class SysConfigManager {

	private static final String Sysconfig_TransferRatePlanCode 	= "transferRatePlanCode";
	private static final String Sysconfig_MaxBestCCrows 		= "maxBestCCrows";
	private static final String Sysconfig_Eco5Offline 			= "Eco5Offline";
	private static final String Sysconfig_Eco5OffLineRecordSeq 	= "Eco5OffLineRecordSeq";
	private static final String Sysconfig_JobIsRunning	 		= "DailyJobIsRunning";
	private static final String Sysconfig_BGJobIsRunning	 	= "BGRepriceTaskIsRunning";
	private static final String Sysconfig_IllegalDemandValue 	= "IllegalDemandValue";
	private static final String Sysconfig_S25LogDeleteFlag 		= "S25LogDeleteFlag";
	private static final String Sysconfig_S25LogKeepDays		= "S25LogKeepDays";
	
	private static final String TransferRatePlanCode 	= "6";
	private static final String MaxBestCCrows 			= "50";
	private static final String IllegalDemandValue 		= "300";
	private static final String S25LogDeleteFlag 		= "0";
	private static final String S25LogKeepDays			= "7";
	
	private Map<String, SysConfigModel> configMap = new HashMap<>();

	private static SysConfigManager instances;
	private SysConfigManager() {
		init();
	}

	public static SysConfigManager getInstance() {
		if (instances==null) {
			synchronized (SysConfigManager.class) {
				if (instances==null) {
					instances = new SysConfigManager();
				}
			}
		}
		return instances;
	}

	private void init() {
		try {
			SysConfigDao dao = new SysConfigDao();
			List<DynaBean> rows = dao.querySysConfig();
			if (rows.size()!=0) {
				configMap = DBContentDealUtil.getSysConfig(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * from cache
	 */
	public String getSysconfig(String paramname) {
		if (configMap.containsKey(paramname)) {
			SysConfigModel sys = configMap.get(paramname);
			return sys.getParamvalue();
		} else {
			return getDefault(paramname);
		}
	}
	public String getDefault(String paramname) {
		switch (paramname) {
			case Sysconfig_TransferRatePlanCode:
				return TransferRatePlanCode;

			case Sysconfig_MaxBestCCrows:
				return MaxBestCCrows;
				
			case Sysconfig_IllegalDemandValue:
				return IllegalDemandValue;
				
			case Sysconfig_S25LogDeleteFlag:
				return S25LogDeleteFlag;
				
			case Sysconfig_S25LogKeepDays:
				return S25LogKeepDays;
		}
		return null;
	}
	
	/*
	 * from db
	 */
	public Map<String, SysConfigModel> querySysConfig(String paramname) {
		Map<String, SysConfigModel> configMap = new HashMap<>();
		try {
			SysConfigDao dao = new SysConfigDao();
			List<DynaBean> rows = dao.querySysConfig(paramname);
			if (rows.size()!=0) {
				 configMap = DBContentDealUtil.getSysConfig(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return configMap;
	}
	
	public void updateJobIsRunningFlag(int flag) {
		SysConfigModel sys = configMap.get(Sysconfig_JobIsRunning);
		sys.setParamname(String.valueOf(flag));

		try {
			SysConfigDao dao = new SysConfigDao();
			dao.updateJobIsRunningFlag(flag);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBGRepriceTaskRunningFlag(int flag) {
		SysConfigModel sys = configMap.get(Sysconfig_BGJobIsRunning);
		sys.setParamname(String.valueOf(flag));

		try {
			SysConfigDao dao = new SysConfigDao();
			dao.updateBGRepriceTaskRunningFlag(flag);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
