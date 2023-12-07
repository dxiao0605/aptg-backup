package aptg.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.SysConfigDao;
import aptg.model.SysConfigModel;
import aptg.utils.DBContentDealUtil;

public class SysConfigManager {

	private static final String Sysconfig_MailHost 				= "MailHost";
	private static final String Sysconfig_MailSender 			= "MailSender";
	private static final String Sysconfig_MailPassword 			= "MailPassword";
	
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
//		switch (paramname) {
//			case Sysconfig_TransferRatePlanCode:
//				return TransferRatePlanCode;
//
//		}
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
}
