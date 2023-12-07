package aptg.cathaybkeco.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import aptg.cathaybkeco.bean.ErrorBean;
import aptg.cathaybkeco.dao.AuthorizationDAO;
import aptg.cathaybkeco.dao.LogRecordDAO;
import aptg.cathaybkeco.dao.SequenceDAO;
import aptg.cathaybkeco.vo.AuthorizationVO;
import aptg.cathaybkeco.vo.LogRecordVO;
import sun.misc.BASE64Encoder;

public class ToolUtil {
	private static final String key = "aptgcathaybkeco";

	/**
	 * 取得輸入串流資訊
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromInputStream(InputStream is) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new IOException(e);
				}
			}
		}
		return sb.toString();
	}

	public static String getSystemId() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("db");
		return rb.getString("systemId");
	}

	/**
	 * Object轉型int
	 * 
	 * @param value
	 * @return int
	 * @throws NumberFormatException
	 */
	public static int parseInt(Object value) throws NumberFormatException {
		return value != null ? Integer.parseInt(value.toString()) : 0;
	}

	/**
	 * Object轉型double
	 * 
	 * @param value
	 * @return double
	 * @throws NumberFormatException
	 */
	public static double parseDouble(Object value) throws NumberFormatException {
		return value != null ? Double.parseDouble(value.toString()) : 0;
	}

	/**
	 * Object轉型date格式字串
	 * 
	 * @param value
	 * @param format
	 * @return String
	 * @throws NumberFormatException
	 */
	public static String dateFormat(Object value, SimpleDateFormat sdf) throws NumberFormatException {
		return value != null ? sdf.format(value) : ObjectUtils.toString(value);
	}

	/**
	 * 日期檢核
	 * 
	 * @param date
	 * @param format
	 * @return boolean
	 */
	public static boolean dateCheck(String date, String format) {
		try {
			SimpleDateFormat dFormat = new SimpleDateFormat(format);
			dFormat.setLenient(false);
			dFormat.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 數字檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean numberCheck(String number) {
		boolean isInt = Pattern.compile("^-?[0-9]+").matcher(number).matches();
		boolean isDouble = Pattern.compile("^-?[0-9]+.?[0-9]+").matcher(number).matches();
		return isInt || isDouble;
	}
	
	/**
	 * 整數檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean IntegerCheck(String number) {
		return Pattern.compile("^-?[0-9]+").matcher(number).matches();
	}
	
	/**
	 * 數字長度檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean numberLengthCheck(String number,int m, int d) {
		String[] numberArr = number.split("//.");
		int integer = numberArr[0].length();
		int decimal = numberArr.length>1?numberArr[1].length():0;
		if(integer>m || decimal>d) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * 取得流水號
	 * 
	 * @param name
	 * @return String
	 * @throws Exception
	 */
	public static String getTpno(String name) throws Exception {
		String tpno = "";
		try {
			SequenceDAO sequenceDAO = new SequenceDAO();
			int sequence = sequenceDAO.getNextval(name);
			tpno = name + StringUtils.leftPad(String.valueOf(sequence), 4, "0");
		} catch (SQLException e) {
			throw new Exception(e.toString());
		}
		return tpno;
	}

	/**
	 * 取得序列
	 * 
	 * @param name
	 * @return int
	 * @throws Exception
	 */
	public static int getSequence(String name) throws Exception {
		SequenceDAO sequenceDAO = new SequenceDAO();
		return sequenceDAO.getNextval(name);
	}

	/**
	 * Enabled代碼轉換說明
	 * 
	 * @param value
	 * @return String
	 * @throws Exception
	 */
	public static String getEnabled(Object value) throws Exception {
		String enabled = "";
		try {
			if ("0".equals(ObjectUtils.toString(value))) {
				enabled = "停用";
			} else if ("1".equals(ObjectUtils.toString(value))) {
				enabled = "啟用";
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return enabled;
	}

	public static String strToSqlStr(String value) {
		String valueStr = new String();
		if (StringUtils.isNotBlank(value)) {
			String[] valueArr = value.split(",");
			for (String str : valueArr) {
				if (StringUtils.isNotBlank(str))
					valueStr += StringUtils.isNotBlank(valueStr) ? ",'" + str + "'" : "'" + str + "'";
			}
		}
		return valueStr;
	}

	/**
	 * 新增操作紀錄
	 * 
	 * @param userName
	 * @param actionCode
	 * @param actionContent
	 * @throws Exception
	 */
	public static void addLogRecord(String userName, String actionCode, String actionContent) throws Exception {
		try {
			LogRecordVO logRecordVO = new LogRecordVO();
			logRecordVO.setUserName(userName);
			logRecordVO.setActionCode(actionCode);
			logRecordVO.setActionContent(actionContent);

			LogRecordDAO logRecordDAO = new LogRecordDAO();
			logRecordDAO.addLogRecord(logRecordVO);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * 長度檢核
	 * 
	 * @param value
	 * @param length
	 * @return boolean
	 */
	public static boolean lengthCheck(String value, int length) {
		if (value.length() > length) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 長度檢核
	 * 
	 * @param value
	 * @param length
	 * @return boolean
	 */
	public static boolean lengthCheck(String value, int length, int length2) {
		if (length2 > value.length() && value.length() > length) {
			return true;
		} else {
			return false;
		}
	}

	public static String getKey() {
		return key;
	}

	/**
	 * 判斷Json欄位是否為空
	 * 
	 * @param json
	 * @param key
	 * @return boolean
	 */
	public static boolean isNull(JSONObject json, String key) {
		boolean flag = false;
		if (json.isNull(key) || StringUtils.isBlank(json.optString(key)))
			flag = true;

		return flag;
	}

	public static BigDecimal getBigDecimal(Object value) {
		return value != null ? new BigDecimal(value.toString()) : BigDecimal.ZERO;
	}

	public static BigDecimal getBigDecimal(Object value, int newScale) {
		return value != null ? new BigDecimal(value.toString()).setScale(newScale, BigDecimal.ROUND_HALF_UP)
				: BigDecimal.ZERO;
	}

	public static BigDecimal getBigDecimal(Object value, int newScale, int roundingMode) {
		return value != null ? new BigDecimal(value.toString()).setScale(newScale, roundingMode) : BigDecimal.ZERO;
	}

	public static BigDecimal divide(BigDecimal value1, BigDecimal value2, int newScale) {
		BigDecimal dividend = value1 != null ? value1 : BigDecimal.ZERO;
		BigDecimal divisor = value2 != null ? value2 : BigDecimal.ZERO;
		return dividend.divide(divisor.compareTo(BigDecimal.ZERO) != 0 ? divisor : BigDecimal.ONE, newScale,
				BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal divide(Object value1, Object value2, int newScale) {
		BigDecimal dividend = value1 != null ? new BigDecimal(value1.toString()) : BigDecimal.ZERO;
		BigDecimal divisor = value2 != null ? new BigDecimal(value2.toString()) : BigDecimal.ZERO;
		return dividend.divide(divisor.compareTo(BigDecimal.ZERO) != 0 ? divisor : BigDecimal.ONE, newScale,
				BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal multiply(Object value1, Object value2) {
		return (value1 != null ? new BigDecimal(value1.toString()) : BigDecimal.ZERO)
				.multiply(value2 != null ? new BigDecimal(value2.toString()) : BigDecimal.ZERO);
	}
	
	public static BigDecimal multiply(Object value1, Object value2, int newScale) {
		BigDecimal mul = (value1 != null ? new BigDecimal(value1.toString()) : BigDecimal.ZERO)
				.multiply(value2 != null ? new BigDecimal(value2.toString()) : BigDecimal.ZERO);
		return mul.setScale(newScale, BigDecimal.ROUND_HALF_DOWN);
	}
	
	public static BigDecimal multiply(BigDecimal value1, BigDecimal value2, int newScale) {
		return value1.multiply(value2).setScale(newScale, BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal add(Object value1, Object value2) {
		BigDecimal add1 = value1 != null ? new BigDecimal(value1.toString()) : BigDecimal.ZERO;
		BigDecimal add2 = value2 != null ? new BigDecimal(value2.toString()) : BigDecimal.ZERO;
		return add1.add(add2);
	}
	
	public static BigDecimal subtract(Object value1, Object value2) {
		BigDecimal sub1 = value1 != null ? new BigDecimal(value1.toString()) : BigDecimal.ZERO;
		BigDecimal sub2 = value2 != null ? new BigDecimal(value2.toString()) : BigDecimal.ZERO;
		return sub1.subtract(sub2);
	}

	/**
	 * 判斷是否為夏月
	 * 
	 * @return
	 */
	public static boolean isSummer() {
		Calendar now = Calendar.getInstance();

		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.MONTH, 5);
		begin.set(Calendar.DAY_OF_MONTH, 1);
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);

		Calendar end = Calendar.getInstance();
		end.set(Calendar.MONTH, 9);
		end.set(Calendar.DAY_OF_MONTH, 1);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);

		return now.after(begin) && now.before(end);
	}

	/**
	 * 檢核Token
	 * 
	 * @param systemId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static boolean checkToken(String token) throws Exception {
		boolean check = false;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("db");
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setToken(token);
			authorizationVO.setSystemId(rb.getString("systemId"));
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			check = authorizationDAO.checkToken(authorizationVO);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return check;
	}

	/**
	 * 取得當月天數
	 */
	public static int getMonthDays() {
		return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得當天
	 */
	public static int getDays() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}


	/**
	 * 取得前一天日期
	 */
	public static String getLastDay(SimpleDateFormat sdf) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);

		return sdf.format(now.getTime());
	}

	/**
	 * 取得上個月最後一天日期
	 */
	public static String getLastMonth(SimpleDateFormat sdf) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, 1); // 設為這個月第一天
		now.add(Calendar.DATE, -1); // 倒回一天 = 上個月最後一天

		return sdf.format(now.getTime());
	}

	/**
	 * 取得去年這個月最後一天日期
	 */
	public static String getLastYear(SimpleDateFormat sdf) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, -1);
		now.add(Calendar.MONTH, +1);
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.add(Calendar.DATE, -1);

		return sdf.format(now.getTime());
	}

	/**
	 * 取得去年這個日期
	 */
	public static String getLastYearDay(String date, SimpleDateFormat sdf) throws Exception {
		Calendar now = Calendar.getInstance();
		try {
			now.setTime(sdf.parse(date));
			now.add(Calendar.YEAR, -1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(now.getTime());
	}
	
	/**
	 * 取得上個月這個日期
	 */
	public static String getLastMonthDay(String date, SimpleDateFormat sdf) throws Exception {
		Calendar now = Calendar.getInstance();
		try {
			now.setTime(sdf.parse(date));
			now.add(Calendar.MONTH, -1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(now.getTime());
	}
	
	/**
	 * 取得這個月
	 */
	public static String getThisMonth() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar now = Calendar.getInstance();
		
		return sdf.format(now.getTime());
	}
	
	/**
	 * 取得下個月日期
	 */
	public static String getNextMonth() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar now = Calendar.getInstance();
		try {
			now.add(Calendar.MONTH, 1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(now.getTime());
	}
	
	/**
	 * 取得下個月日期
	 */
	public static String getNextMonth(String date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.MONTH, 1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(cal.getTime());
	}
	
	/**
	 * 取得下個月日期
	 */
	public static String getNextMonth(String date, String dateformat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.MONTH, 1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(cal.getTime());
	}
	
	/**
	 * 取得明天日期
	 */
	public static String getNextDay(String date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar now = Calendar.getInstance();
		try {
			now.setTime(sdf.parse(date));
			now.add(Calendar.DAY_OF_MONTH, 1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(now.getTime());
	}
	
	/**
	 * 取得明天日期
	 */
	public static String getNextDay(String date, String dateformat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		Calendar now = Calendar.getInstance();
		try {
			now.setTime(sdf.parse(date));
			now.add(Calendar.DAY_OF_MONTH, 1);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return sdf.format(now.getTime());
	}
	
	public static String getMode(List<String> filter) throws Exception {
		String mode = "";
		if (filter.contains("MeterName")||filter.contains("UsageDesc")) {
			mode = "Meter";
		} else if (filter.contains("PowerAccount")||filter.contains("RatePlanDesc")) {
			mode = "PowerAccount";
		} else {
			mode = "Bank";
		}
		return mode;
	}
	
	public static String getNewDateFormat(String date, String oldFormat, String newFormat) throws Exception {
		SimpleDateFormat oldsdf = new SimpleDateFormat(oldFormat);
		SimpleDateFormat newsdf = new SimpleDateFormat(newFormat);
		return newsdf.format(oldsdf.parse(date));
	}
	
	/**
	 * Object轉型date格式
	 * 
	 * @param value
	 * @param format
	 * @return String
	 * @throws NumberFormatException
	 */
	public static Date parseDate(Object value, SimpleDateFormat sdf) throws NumberFormatException {
		try {
			return value != null ? sdf.parse(sdf.format(value)) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 是否為當月第一天
	 * @return
	 */
	public static boolean isMonthFirstDay() {		
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_MONTH)==1) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 是否為當月
	 * @param date(yyyyMM)
	 * @return
	 */
	public static boolean isThisMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar now = Calendar.getInstance();
		if(date.equals(sdf.format(now.getTime()))) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String getErrRspJson(String msg, String code) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setMsg(msg);
		errorBean.setCode(code);
		return JsonUtil.getInstance().convertObjectToJsonstring(errorBean);
	}
	

	
	/**
	 * 產生6碼隨機英數字驗證碼
	 * @param Len
	 * @return
	 */
	public static String getVerifyCode() {
        String[] baseString = {
        		"0", "1", "2", "3", "4", 
        		"5", "6", "7", "8", "9",            
                "A", "B", "C", "D", "E", 
                "F", "G", "H", "I", "J", 
                "K", "L", "M", "N", "O", 
                "P", "Q", "R", "S", "T", 
                "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        int length = baseString.length;
        String randomString = "";
        for (int i = 0; i < length; i++) {
            randomString += baseString[random.nextInt(length)];
        }
        random = new Random(System.currentTimeMillis());
        String resultStr = "";
        for (int i = 0; i < 6; i++) {
            resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
        }
        return resultStr;
    }
	
	/**
	 * 判斷是否為空陣列
	 * @param arr
	 * @return
	 */
	public static boolean isBlankArray(String[] arr) {
		boolean isBlank = true;
		if(arr!=null) {
			for(String value : arr) {
				if(StringUtils.isNotBlank(value)) {
					isBlank = false;
				}
			}
		}
		return isBlank;
	}
	
	/**
	 * 檔案轉Base64
	 * 
	 * @param workbook
	 * @param fileName
	 * @param response
	 * @throws Exception
	 */
	public static String getFileBase64(File file)
			throws Exception {		
		InputStream in = null;  
        byte[] data = null;  
        try {    	
            in = new FileInputStream(file);          
            data = new byte[in.available()];  
            in.read(data);  
        }catch (IOException e)   {  
        	throw new Exception(e.toString());  
        }  finally {
			if (in != null) 
				in.close();			
		}
        //對位元組陣列Base64編碼  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data).replaceAll("\r|\n", "");//返回Base64編碼過的位元組陣列字串  
	}
}
