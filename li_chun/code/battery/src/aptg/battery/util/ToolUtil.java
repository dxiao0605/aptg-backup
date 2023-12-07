package aptg.battery.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aptg.battery.bean.ErrorBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.SequenceDAO;
import aptg.battery.vo.CompanyVO;


public class ToolUtil {
	private static final String key = "aptgbattery";//AES加密的Key
	private static String adminCompany;

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
	
	public static void response(String rspStr, HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspStr);
	}

	public static String getSystemId() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("db");
		return rb.getString("systemId");
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
			check = AuthorizationUtil.checkToken(rb.getString("systemId"), token);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return check;
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
	 * 英文數字檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean numberEnglishCheck(String str) {
		return Pattern.compile("[A-Za-z0-9]*").matcher(str).matches();
	}
	/**
	 * 字串檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean strCheck(String str) {
		return Pattern.compile("[A-Za-z0-9\\-_\\s]*").matcher(str).matches();
	}
	
	/**
	 * 密碼字串檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean passwordCheck(String str) {
		return Pattern.compile("[A-Za-z0-9\\-_!@#$%^&*()_+=,.<>:;]*").matcher(str).matches();
	}
	
	/**
	 * 英文檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean englishCheck(String str) {
		return Pattern.compile("[A-Za-z\\s]*").matcher(str).matches();
	}
	
	/**
	 * 英數檢核
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean englishNumberCheck(String str) {
		return Pattern.compile("[A-Za-z0-9\\s]*").matcher(str).matches();
	}
	
	/**
	 * 取得流水號
	 * 
	 * @param name
	 * @return int
	 * @throws Exception
	 */
	public static int getSequence(String name) throws Exception {
		SequenceDAO sequenceDAO = new SequenceDAO();
		return sequenceDAO.getNextval(name);
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

	public static String jsonArrToSqlStr(JSONArray arr) {
		String valueStr = new String();
		if (arr!=null && arr.length()>0) {
			String str;
			for (int i=0; i<arr.length(); i++) {
				str = arr.optString(i);
				if (StringUtils.isNotBlank(str))
					valueStr += StringUtils.isNotBlank(valueStr) ? ",'" + str + "'" : "'" + str + "'";
			}
		}
		return valueStr;
	}
	
	public static String listToSqlStr(List<String> list) {
		String valueStr = new String();
		if (list!=null && list.size()>0) {
			String str;
			for (int i=0; i<list.size(); i++) {
				str = list.get(i);
				if (StringUtils.isNotBlank(str))
					valueStr += StringUtils.isNotBlank(valueStr) ? ",'" + str + "'" : "'" + str + "'";
			}
		}
		return valueStr;
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
	
	/**
	 * Email檢核
	 * @param value
	 * @return
	 */
	public static boolean emailCheck(String value) {
		
		if (value.indexOf("@")==-1) {
			return false;
		} else {
			return true;
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
	 * 排序JsonArray
	 * 
	 * @param array
	 * @param key
	 * @param reversed
	 * @return JSONArray
	 */
	public static JSONArray sortJsonArray(JSONArray array, String key, boolean reversed) {
		List<JSONObject> sortRows = new ArrayList<JSONObject>();
		ArrayList<JSONObject> jsons = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			try {
				jsons.add(array.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (reversed) {
			sortRows = jsons.stream().sorted(Comparator.comparing(new Function<JSONObject, BigDecimal>() {
				public BigDecimal apply(JSONObject bean) {
					return bean.getBigDecimal(key);
				}
			}).reversed()).collect(Collectors.toList());
		} else {
			sortRows = jsons.stream().sorted(Comparator.comparing(new Function<JSONObject, BigDecimal>() {
				public BigDecimal apply(JSONObject bean) {
					return bean.getBigDecimal(key);
				}
			})).collect(Collectors.toList());
		}
		return new JSONArray(sortRows);
	}

	
	/**
	 * 取得經緯度
	 * @param address
	 * @throws Exception 
	 */
	public static JSONObject getLonLat(String address) throws Exception {		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("db");
			String actionUrl = rb.getString("googleApiUrl")+"&key="+SysConfig.getInstance().getGoogleApiKey()+"&address=" + URLEncoder.encode(address, "utf-8");

			if (actionUrl.contains("https")) {
				HostnameVerifier hv = new HostnameVerifier() {
					public boolean verify(String urlHostName, SSLSession session) {
						return true;
					}
				};

				javax.net.ssl.TrustManager[] trustAllCerts = { new TrustAllTrustManager() };
				SSLContext sc = SSLContext.getInstance("SSL");
				SSLSessionContext sslsc = sc.getServerSessionContext();
				sslsc.setSessionTimeout(0);
				sc.init(null, trustAllCerts, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// 激活主機認證
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
			}
			
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("GET");
			con.setUseCaches(false);
			con.setReadTimeout(3000);
			con.setConnectTimeout(3000);

			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

//			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			/* 取得Response內容 */
			StringBuffer response = new StringBuffer();
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				String inputLine;
				
				while (((inputLine = reader.readLine()) != null)) {
					response.append(inputLine);
				}
				reader.close();
			}
			con.disconnect();

			JSONObject rep = new JSONObject(response.toString());
			JSONArray resultsArr = rep.getJSONArray("results");
			if(resultsArr!=null && resultsArr.length()>0) {
				JSONObject results = resultsArr.getJSONObject(0);
				JSONObject geometry = results.getJSONObject("geometry");
				return geometry.getJSONObject("location");
			}else {
				return null;
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}
	
	/**
	 * 取得語系
	 * @param language
	 * @return
	 * @throws Exception
	 */
	public static ResourceBundle getLanguage(String language) throws Exception {
		ResourceBundle resource = null;
		if("3".equals(language)) {
			resource = ResourceBundle.getBundle("messages_jp");
		}else if("2".equals(language)){
			resource = ResourceBundle.getBundle("messages_en");
		}else {
			resource = ResourceBundle.getBundle("messages_tw");
		}
		return resource;
	}
	
	/**
	 * 取得日期格式化
	 * @param format
	 * @param timezone
	 * @return
	 * @throws Exception
	 */
	public static SimpleDateFormat getDateFormat(String format, String timezone) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(StringUtils.isBlank(timezone)) {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		}else {
			BigDecimal zone = new BigDecimal(timezone).divide(new BigDecimal(4),0,BigDecimal.ROUND_HALF_DOWN);
			if(zone.compareTo(BigDecimal.ZERO)>0) {
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+"+zone));	
			}else {
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"+zone));
			}			
		}		
		return sdf;
	}
	
	/**
	 * 產生隨機字串
	 * @param Len
	 * @return
	 */
	public static String GetRandomString(int Len) {
        String[] baseString = {"0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y",
            "z", "A", "B", "C", "D",
            "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        int length = baseString.length;
        String randomString = "";
        for (int i = 0; i < length; i++) {
            randomString += baseString[random.nextInt(length)];
        }
        random = new Random(System.currentTimeMillis());
        String resultStr = "";
        for (int i = 0; i < Len; i++) {
            resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
        }
        return resultStr;
    }
	
	/**
	 * 取得CRC驗證碼
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public static String getCRC(String payload) throws Exception {
		int crc = 0;		
		int start = 0;
		while(start+2 <= payload.length()) {			
			int xor = Integer.parseInt(payload.substring(start, start+2), 16);
			if(start == 0) {
				crc = xor;
			}else {
				crc = crc ^ xor;
			}
			start+=2;
		}
		return Integer.toHexString(crc);
	}
	
	/**
	 * 判斷是否為業主公司，如果是業主公司(false)，則不帶公司條件，全公司查詢
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	public static boolean checkAdminCompany(String companyCode) throws Exception {
		if(adminCompany==null) {
	        CompanyDAO companyDAO = new CompanyDAO();
	        List<DynaBean> list = companyDAO.getDefault();
			if(list!=null && !list.isEmpty()) {
				DynaBean bean = list.get(0);
				adminCompany = ObjectUtils.toString(bean.get("companycode"));
			}
		}

		if(companyCode.equals(adminCompany)) {
			return false;
		}else {
			return true;
		}		
	}
	
	/**
	 * 取得IMPType
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	public static int getIMPType(String companyCode) throws Exception {
		int impType = 20;////20: 內阻值(預設) 21:毫內阻 22:電導值 
		CompanyVO companyVO = new CompanyVO();
		companyVO.setCompanyCode(companyCode);
        CompanyDAO companyDAO = new CompanyDAO();
        List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);
		if(list!=null && !list.isEmpty()) {
			DynaBean bean = list.get(0);
			impType = Integer.parseInt(ObjectUtils.toString(bean.get("imptype")));
		}
		return impType;	
	}
	
	
	public static void remove(List<String> list, String value) throws Exception {
		for(int i=0; i<list.size(); i++) {
			if(value.equals(list.get(i))) {
				list.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 內阻 > 毫內祖
	 * 1000UΩ =1 mΩ
	 * @param ir
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal irToMir(BigDecimal ir) throws Exception {
		return divide(ir, 1000, 3);
	}
	
	/**
	 * 內阻 > 電導值
	 * S電導值 = (10^6)/原始數據的內阻值
	 * @param ir
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal irToS(BigDecimal ir) throws Exception {
		return divide(1000000, ir, 3);
	}
	
	/**
	 * 毫內阻 > 內祖
	 * 1000UΩ =1 mΩ
	 * @param mir
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal mirToIr(BigDecimal mir) throws Exception {
		return multiply(mir, 1000);
	}
	
	/**
	 * 毫內阻 > 電導值
	 * 1000UΩ =1 mΩ
	 * S電導值 = (10^6)/原始數據的內阻值
	 * @param mir
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal mirToS(BigDecimal mir) throws Exception {
		return divide(1000, mir, 3);
	}
	
	/**
	 * 電導值 > 內阻
	 * S電導值 = (10^6)/原始數據的內阻值
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal sToIr(BigDecimal s) throws Exception {
		return divide(1000000, s, 0);
	}
	
	/**
	 * 電導值 > 毫內阻
	 * 1000UΩ =1 mΩ
	 * S電導值 = (10^6)/原始數據的內阻值
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal sToMir(BigDecimal s) throws Exception {
		return divide(1000, s, 3);
	}
	
	public static String getErrRspJson(String msg, String code) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setMsg(msg);
		errorBean.setCode(code);
		return JsonUtil.getInstance().convertObjectToJsonstring(errorBean);
	}

	public static BigDecimal BigDecimalTwo() {		
		return new BigDecimal(2);
	}
	
	/**
	 * 取得電池狀態
	 * @param nbid
	 * @param batteryId
	 * @return String
	 * @throws Exception
	 */
	public static String getBatteryStatus(String nbid, String batteryId) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String status = "1";
		try {
			BatteryDAO batteryDAO = new BatteryDAO();			
			List<DynaBean> list = batteryDAO.getBatteryStatus(nbid, batteryId);
			if(list!=null && !list.isEmpty()) {
				DynaBean bean = list.get(0);				
				Date now = sdf.parse(sdf.format(new Date()));
				if (now.getTime() - sdf.parse(sdf.format(bean.get("rectime"))).getTime() > ToolUtil
						.parseInt(bean.get("disconnect")) * 1000) {										
					status = "4";
				}else {
					status = ObjectUtils.toString(bean.get("status"));
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}		
		return status;
	}
	
	/**
	 * 取得密碼失效時間
	 * @return
	 */
	public static String getDisableTime() {
		int disableHour = ToolUtil.parseInt(SysConfig.getInstance().getDisableTime());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, disableHour);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}
