package aptg.cathaybkeco.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;

import aptg.cathaybkeco.dao.SysConfigDAO;

public class SysConfig {

	private static final String MailHost = "MailHost";
	private static final String MailPort = "MailPort";
	private static final String MailSender = "MailSender";
	private static final String MailPassword = "MailPassword";
	private static final String TempDir = "TempDir";
	private Map<String, String> sysMap = new HashMap<String, String>();

	public SysConfig() {
		try {
			initSysconfig();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static SysConfig instances;

	public static SysConfig getInstance() {
		if (instances == null) {
			instances = new SysConfig();
		}
		return instances;
	}

	private void initSysconfig() throws SQLException {
		SysConfigDAO dao = new SysConfigDAO();
		List<DynaBean> rows = dao.getSysConfig();
		if (rows != null && !rows.isEmpty()) {
			for (DynaBean bean : rows) {
				sysMap.put(ObjectUtils.toString(bean.get("paramname")), ObjectUtils.toString(bean.get("paramvalue")));
			}
		}
	}

	private String getMapValue(String paramname) {
		if (sysMap.containsKey(paramname) && sysMap.get(paramname) != null) {
			return sysMap.get(paramname);
		}
		return null;
	}

	
	
	public String getMailHost() {
		return getMapValue(MailHost);
	}
	
	public String getMailPort() {
		return getMapValue(MailPort);
	}
	
	public String getMailSender() {
		return getMapValue(MailSender);
	}
	
	public String getMailPassword() {
		return getMapValue(MailPassword);
	}
	
	public String getTempDir() {
		return getMapValue(TempDir);
	}
}
