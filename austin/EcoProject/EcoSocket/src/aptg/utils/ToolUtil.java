package aptg.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.enums.CodeEnum;

public class ToolUtil {
	
	private static final Logger logger = LogManager.getFormatterLogger(ToolUtil.class.getName());

	private static ToolUtil instances;

	private ToolUtil() {}
	
	public static ToolUtil getInstance() {
		if (instances==null) {
			synchronized (ToolUtil.class) {
				if (instances==null) {
					instances = new ToolUtil();
				}
			}
		}
		return instances;
	}
	
	/**
	 * 時間格式String轉換成Date
	 * 
	 * @param dateStr
	 * @param format: 希望的時間格式
	 * @return
	 */
	public Date convertStringToDate(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
        	return formatter.parse(dateStr, new ParsePosition(0));
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
		}
	}

	/**
	 * 時間格式Date轉換成String
	 * 
	 * @param date
	 * @param format: 希望的時間格式
	 * @return
	 */
	public String convertDateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
        	String dateStr = "";
        	if (date!=null) {
        		dateStr = formatter.format(date);
        	}
        	return dateStr;
        } catch (Exception e) {
        	e.printStackTrace();
        	return "";
		}
	}

	public static String isTZOffsetFormatError(String tzOffset) {
		if (tzOffset!=null) {
			String sign = tzOffset.substring(0, 1);
			if (!sign.contentEquals("+") && !sign.contentEquals("-")) {
//				return ErrorCodeEnum.ERRCDE_S01_TZOFFSET_ERROR.getErrCode();
				return "-1";
			}
			
			if (tzOffset.contains(":")) {
				String[] info = tzOffset.split(":");
				int hour = Integer.parseInt(info[0].substring(1, info[0].length()));
				int min = Integer.parseInt(info[1]);

				if (hour<0 || hour>23) return "-1";
				if (min<0 || min>59) return "-1";

			} else {
				String hourStr = tzOffset.substring(1, tzOffset.length());
				int hour = Integer.parseInt(hourStr);

				if (hour<0 || hour>23) return "-1";
			}
		}
		return "0";
	}
	
	public Date convertTimestampToDate(long timestamp) {
		long time = timestamp * 1000L;
		Date date = new Date(time);
		return date;
	}
	
	public long convertDateToTimestamp(Date date) {
		long timestamp = date.getTime() / 1000L;
		return timestamp;
	}
	
	private static void checkUnderLineStringIllegal(String str) {
		if (str.indexOf("_")==-1) {
			System.out.println("*********** 11111111111 ");
		} else {
			String attrValue = str.substring(str.indexOf("_")+1, str.length());
			System.out.println("*********** attrValue: "+attrValue);
			if (StringUtils.isBlank(attrValue)) {
				System.out.println("*********** 11111111111 ");
			}
		}
	}

	public static void main(String[] args) {

		logger.info(CodeEnum.ERRCDE_S25_DEVICE_NOT_EXIST.value());
		System.out.println("*********** CodeEnum.ERRCDE_S25_DEVICE_NOT_EXIST.value(): "+CodeEnum.ERRCDE_S25_DEVICE_NOT_EXIST.value());
		
		
//		try {
//			SysConfigDao dao = new SysConfigDao();
//			dao.insertSysConfig();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
//		String func = "S01";
//		if (func.contains("01")) {
//			System.out.println("contain!!");	
//		} else {
//			System.out.println("not contain!!");
//		}
//		
//		String  request = "*1;S01;00000001;ES990000000000TEST00;abc123;\\n";
//		String[] message = request.split(";");	// *1;S01;00000001;ES990000000000TEST00;abc123;\n
//
//		String temp = "";
//		for (String msg: message) {
//			temp += msg + ";";
//		}
//		temp = temp.replace("*1", "#1").substring(0, temp.length()-1);
//		
//		System.out.println("temp: "+temp);
	}
}
