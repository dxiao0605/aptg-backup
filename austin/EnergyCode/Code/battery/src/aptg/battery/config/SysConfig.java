package aptg.battery.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import aptg.battery.dao.SysConfigDAO;

public class SysConfig {

	private static final String Google_API_Key = "GoogleApiKey";
	private static final String Reset_URL = "ResetUrl";
	private static final String IRTestTime = "IRTestTime";
	private static final String BatteryCapacity = "BatteryCapacity";
	private static final String CorrectionValue = "CorrectionValue";
	private static final String Resistance = "Resistance";
	private static final String UploadCycle = "UploadCycle";
	private static final String IRCycle = "IRCycle";
	private static final String CommunicationCycle = "CommunicationCycle";
	private static final String IR = "IR";
	private static final String Vol = "Vol";
	private static final String MailHost = "MailHost";
	private static final String MailSender = "MailSender";
	private static final String MailPassword = "MailPassword";
	private static final String ImagesPath = "ImagesPath";
	private static final String DisableTime = "DisableTime";
	private static final String OL = "OL";
	private static final String CsvDir = "CsvDir";
	
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

	public String getGoogleApiKey() {
		return getMapValue(Google_API_Key);
	}
	
	public String getResetUrl() {
		return getMapValue(Reset_URL);
	}
	
	public String getIRTestTime() {
		return getMapValue(IRTestTime);
	}
	
	public String getBatteryCapacity() {
		return getMapValue(BatteryCapacity);
	}

	public String getCorrectionValue() {
		return getMapValue(CorrectionValue);
	}

	public String getResistance() {
		return getMapValue(Resistance);
	}
	
	public String getUploadCycle() {
		return getMapValue(UploadCycle);
	}
	
	public String getIRCycle() {
		return getMapValue(IRCycle);
	}
	
	public String getCommunicationCycle() {
		return getMapValue(CommunicationCycle);
	}
	
	public String getIR() {
		return getMapValue(IR);
	}
	
	public String getVol() {
		return getMapValue(Vol);
	}
	
	public String getMailHost() {
		return getMapValue(MailHost);
	}
	
	public String getMailSender() {
		return getMapValue(MailSender);
	}
	
	public String getMailPassword() {
		return getMapValue(MailPassword);
	}
	
	public String getImagesPath() {
		return getMapValue(ImagesPath);
	}
	
	public String getDisableTime() {
		return getMapValue(DisableTime);
	}
	
	public String getOL() {
		return getMapValue(OL);
	}
	
	public String getCsvDir() {
		return getMapValue(CsvDir);
	}
}
