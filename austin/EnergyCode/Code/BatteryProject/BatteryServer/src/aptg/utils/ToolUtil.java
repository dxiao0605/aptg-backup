package aptg.utils;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class ToolUtil {

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
	 * 	時間格式Date轉換成String
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
	
	/**
	 * 	 * 時間格式Date轉換成String
	 * 
	 * @param date
	 * @param format: 希望的時間格式
	 * @param timezone: Asia/Taipei為+32
	 * @return
	 */
	public String convertDateToString(Date date, String format, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
        	int gmtVal;
        	if (!StringUtils.isBlank(timezone)) {
        		gmtVal = convertTimeZoneToGMT(timezone);
        	} else {
        		// default: Server GMT
        		gmtVal = getServerGMT();
        	}

        	String gmt = (gmtVal>=0) ? "GMT+"+gmtVal : "GMT"+gmtVal;
//    		System.out.println("############ gmt: "+gmt);
        	formatter.setTimeZone(TimeZone.getTimeZone(gmt));
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
	
	/**
	 * 	轉換時區數字
	 * ex: +32 => 32*15/60 = 8 即 GMT+8
	 * 
	 * @param timezone
	 * @return
	 */
	private int convertTimeZoneToGMT(String timezone) {
		float gmt = (float) Integer.parseInt(timezone)*15/60;
		return (int) Math.round(gmt);
	}
	private int getServerGMT() {
		TimeZone defaultTimeZone = TimeZone.getDefault();
		int offset = defaultTimeZone.getRawOffset();
		int gmt = (offset / (15*60*1000)) * 15 / 60;
		return gmt;
	}
	
	/**
	 * 將hex轉成String
	 * ex: 5a5a3230333030303631 => ZZ20300061
	 * 
	 * @param hex
	 * @return
	 */
	public String convertHexToString(String hex) {
		StringBuilder output = new StringBuilder("");
		for (int i=0; i<hex.length(); i+=2) {
			String str = hex.substring(i, i+2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}
	
	/**
	 * 將String轉成hex
	 * ex: ZZ20300061 => 5a5a3230333030303631
	 * 
	 * @param str
	 * @return
	 */
	public String convertStringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (char ch : chars) {
			hex.append(Integer.toHexString((int) ch));
		}
		return hex.toString();
	}
	
	/**
	 * 取得去除"-"的uuid
	 * 
	 * @return
	 */
	public String getTrimUUID() {
		UUID uuid  =  UUID.randomUUID();
		String id = uuid.toString();
		id = id.replace("-", "");//替換掉中間的那個橫槓
		return id;
	}

	/**
	 * 檢查payload CRC驗證是否合法
	 * 
	 * @param str
	 * @return
	 */
	public boolean doCheckCRC(String payload) {
		int start = 0;
		int end = 2;
		int count = payload.length() / 2;
		
		int xor = 0;
		for (int i=1 ; i<count ; i++) {
			String msg = payload.substring(start, end);
//			System.out.println("### msg: "+msg+ " XOR "+xor);
			
			int value = Integer.parseInt(msg, 16);
			
			xor = value ^ xor;
//			System.out.println("=> "+xor);
//			System.out.println("");
			
			start = end;
			end += 2;
		}
		
		String checkcrc = payload.substring(start, end);	// 取最後兩位
		int checkcrcValue = Integer.parseInt(checkcrc, 16);	// 轉int
//		System.out.println("checkcrcValue: "+checkcrcValue+", xor: "+xor);
		
		return (xor==checkcrcValue) ? true : false;
	}
	
	/**
	 * 取得payload的CRC驗證值
	 * 
	 * @param payload
	 * @return
	 */
	public String getPayloadCRC(String payload) {
		int start = 0;
		int end = 2;
		int count = payload.length() / 2;
		
		int xor = 0;
		for (int i=1 ; i<=count ; i++) {
			String msg = payload.substring(start, end);
			
			int value = Integer.parseInt(msg, 16);

//			System.out.println("### byte:"+msg+"=>intValue:"+value+ ", xor: "+xor);
			xor = value ^ xor;
//			System.out.println("new xor => "+xor);
//			System.out.println("");
			
			start = end;
			end += 2;
		}

		String crc = Integer.toHexString(xor);
		crc = StringUtils.leftPad(crc, 2, '0').toUpperCase();
//		System.out.println("xor: "+xor+" => crc: "+crc);
		return crc;
	}
	
	public Date convertTimestampToDate(long timestamp) {
		Date date = new Date(timestamp * 1000L);
		return date;
	}
	
	public String intToHex(int value) {
		String hex = Integer.toHexString(value);
		hex = StringUtils.leftPad(hex, 2, '0').toUpperCase();
		return hex;
	}
	
	
	/**
	 * 	轉換時區數字
	 * ex: +32 => 32*15/60 = 8 即 GMT+8
	 * 
	 * @param timezone
	 * @return
	 */
	private String getTimeZone(String timezone) {
		String gmt = "GMT";
		float temp = (float) Integer.parseInt(timezone)*15/60;
		if (temp>=0) {
			gmt = gmt + "+" + temp;
		} else {
			gmt = gmt + temp;
		}
//		System.out.println("gmt: "+gmt);
		return gmt;
	}
	
	public static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}

	private String getPayload(String commandCode, int batteryID, String hexConfig) {
		String payload = "";
    	if (commandCode.equals("BB")) {
    		payload = commandCode + hexConfig;	// 內阻設定測試值
    	}
    	else if (commandCode.equals("BA")) {
    		payload = commandCode + hexConfig;	// 時間週期設定
    	}
    	else if (commandCode.equals("B5")) {
    		payload = commandCode + ToolUtil.getInstance().intToHex(batteryID) + hexConfig;	// 校正電壓
    	}
    	else if (commandCode.equals("B3")) {
    		payload = commandCode + ToolUtil.getInstance().intToHex(batteryID) + hexConfig;	// 校正內阻
    	}
    	
		String crc = ToolUtil.getInstance().getPayloadCRC(payload);
		
		payload = payload + crc;
		return payload;
	}
	
	public static void main(String[] args) throws IOException {
		String payload = ToolUtil.getInstance().getPayload("B3", 1, "08115C15B31A0A1E61");
		System.out.println("payload: "+payload);
		
//		String test = "[\"4444\",\"5555\",\"6666\",\"4444\"]";
//		List<Object> values = JsonUtil.getInstance().convertStringArrayToList(test);
//		System.out.println("values: "+values.toString());
//
//		for (int i=0; i<values.size(); i++) {
//			
//			String correctionValue = (String) values.get(i);
//
//			System.out.println("correctionValue: "+Integer.valueOf(correctionValue));
//		}
		
		
//		String payload = "[{\"CompanyName\":\"Freestyle Tech\",\"NBID\":\"DANC100046\",\"CompanyCode\":11}]";
//		
////		payload = payload.replace(" ", "@#$");
//		System.out.println("payload: "+payload);
//		
//		try {
//			JSONArray itemArray = new JSONArray(payload);
//
//			for (int i=0 ; i<itemArray.length() ; i++) {
//				Object ob = itemArray.getJSONObject(i);
//				
//				NBCompanyBean config = (NBCompanyBean) JsonUtil.getInstance().convertStringToObject(ob.toString(), NBCompanyBean.class);
//				System.out.println("4 ob.toString(): "+ob.toString());
//				System.out.println("Payload Detail: "+JsonUtil.getInstance().convertObjectToJsonstring(config));
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		Map<String, DelNBIDBean> map = new HashMap<>();
//
//		List<DelNBIDBean> list = new ArrayList<>();
//		DelNBIDBean b1 = new DelNBIDBean();
//		b1.setNBID("test");
//		list.add(b1);
//		
//		DelNBIDBean b2 = new DelNBIDBean();
//		b2.setNBID("temp");
//		list.add(b2);
//		
//		map.put("abc_1", b1);
//		map.put("zzz_2", b2);
//		
//		boolean tf = map.keySet().stream().anyMatch(key -> key.contains("ac"));
//		System.out.println(tf);
//		
//		
//		for (String key: map.keySet()) {
//			System.out.println("key: "+key);
//		}
		
//		String key = "tem";
//		
//		List<DelNBIDBean> list = new ArrayList<>();
//		
//		DelNBIDBean b1 = new DelNBIDBean();
//		b1.setNBID("test");
//		list.add(b1);
//		
//		DelNBIDBean b2 = new DelNBIDBean();
//		b2.setNBID("temp");
//		list.add(b2);
//		
//		DelNBIDBean bbb = list.stream().filter(bean -> key.equals(bean.getNBID())).findAny().orElse(null);
//		if (bbb!=null)
//			System.out.println("bbb: "+bbb.getNBID());
//		else 
//			System.out.println("bbb is null");
		
		
//		String config3 = "[{\"NBID\":\"ZZxxxxxx1\",\"CompanyCode\":1},{\"NBID\":\"ZZxxxxxx2\",\"CompanyCode\":2},{\"NBID\":\"ZZxxxxxx3\",\"CompanyCode\":3}]";
//
//		JSONArray itemArray2 = new JSONArray(config3);
//		List<Object> list2 = itemArray2.toList();
//		System.out.println("list2: "+list2.toString());
//		System.out.println("config: "+list2.get(2));
//		
//		NBCompanyBean nb = (NBCompanyBean) JsonUtil.getInstance().convertStringToObject(list2.get(2).toString(), NBCompanyBean.class);
//		System.out.println("nb nbid: "+nb.getNBID());
//		System.out.println("nb companycode: "+nb.getCompanyCode());
//		
//		
//		
//		
//		
//		
//		
//		
//		String config = "[\"11000\",\"12000\",\"13000\",\"14000\"]";
//		List<Object> arrConfig = JsonUtil.getInstance().convertStringArrayToList(config);
//		System.out.println("config: "+arrConfig.get(0));
//		System.out.println("config: "+new BigDecimal((String) arrConfig.get(1)));
//		System.out.println("config: "+arrConfig.get(2));
//		System.out.println("config: "+arrConfig.get(3));
//
//		String config2 = "[11000, 12000, 13000, 14000]";
//		JSONArray itemArray = new JSONArray(config2);
//		List<Object> list = itemArray.toList();
//		System.out.println("config: "+(int) list.get(0));
//		
//		
//		String payload = "BA06012C003C000F";
////		String payload = "BA064650003C000F";
//		String crc = ToolUtil.getInstance().getPayloadCRC(payload);
//		System.out.println("crc: "+crc);
		
		
//		String a = "BA";
//		int av = Integer.parseInt(a, 16);
//		
//		String b = "06";
//		int bv = Integer.parseInt(b, 16);
//		
//		int xor = av ^ bv;
//		System.out.println("xor: "+xor);

//		System.out.println("============");
//		payload = "BA064650003C000F" + "99";
//		boolean check = ToolUtil.getInstance().doCheckCRC(payload);
//		System.out.println("check: "+check);
		
		
//		System.out.println("============");
//		int ms = 153;
//		String msHex = Integer.toHexString(ms);
//		System.out.println("msHex: "+ msHex);
//		
//		msHex = StringUtils.leftPad(msHex, 4, '0');
//		System.out.println("msHex: "+ msHex);
//		
//		msHex = "150";
//		int hex2int = Integer.parseInt(msHex, 16);
//		System.out.println("hex2int: "+ hex2int);
		
		
//		System.out.println("============");
//		char LF  = (char) 0x0A;	// \n
//		
//		String testStr = "FE\n";
//
//		System.out.println("testStr: "+testStr);
//		System.out.println("testStr: "+testStr.replace(String.valueOf(LF), ""));
//
//		System.out.println("============");
//		String jsonString = "[1000, 2000, 3000, 4000]";
//		List<Object> list = JsonUtil.getInstance().convertStringArrayToList(jsonString);
//		System.out.println("list: "+ list.toString());
//		for (int i=0; i<list.size(); i++) {
//			int CorrectionValue = (int) list.get(i);
//			System.out.println("list.get("+i+"): "+ CorrectionValue);
//		}
//		
//		System.out.println("============");
//		JSONArray itemArray = new JSONArray(jsonString);
//		System.out.println("itemArray: "+ itemArray.getInt(0));
//		System.out.println("itemArray: "+ itemArray.getInt(1));
//		System.out.println("itemArray: "+ itemArray.getInt(2));
//		System.out.println("itemArray: "+ itemArray.getInt(3));
		
		
//	    Float ohm_h = new Float("6553.5");
//	    String str = ohm_h.toHexString(1.0f);
//	    System.out.println("Hex String = " + str);
		
		

//    	System.out.println("============");
//		String temp2 = "0E5B";
//		int it = Integer.parseInt(temp2, 16);
//    	System.out.println("it: "+it);
//
//    	System.out.println("============");
//		BigInteger bigInt = BigInteger.valueOf(it);
//	    byte[] bytearray = (bigInt.toByteArray());
//	    for(int i=0; i<bytearray.length; i++)
//	    	System.out.println("bytearray["+i+"]="+bytearray[i]);
//
//    	System.out.println("============");
//		byte[] bytes = ByteBuffer.allocate(4).putInt(it).array();
//	    for(int i=0; i<bytes.length; i++) 
//	    	System.out.println("bytes["+i+"]="+bytes[i] +" => "+new Byte(bytes[i]).intValue());
//
//    	System.out.println("============");
//    	int num = byteArrayToInt(bytes);
//    	System.out.println("num="+num);
		
	    
	      
//	    
//		String time = "1606406552";
//		System.out.println("int time: "+Integer.valueOf(time));
//		
//		long tt = 1606406552;
//		System.out.println("int time: "+(int) tt);
//		
//		Date temp = new Date(1606406594 * 1000L);
//		System.out.println("############ date: "+ ToolUtil.getInstance().convertDateToString(temp, "yyyy-MM-dd HH:mm:ss", "36"));
//		System.out.println("############ date: "+ ToolUtil.getInstance().convertDateToString(temp, "yyyy-MM-dd HH:mm:ss", "34"));
//		System.out.println("temp: "+temp);
//		System.out.println("temp: "+ToolUtil.getInstance().convertDateToString(temp, "yyyy-MM-dd HH:mm:ss", null));
//		
//		
//		Date t = new Date();
//		t.getTime();
//		
//		String timezone = "+32";
//		System.out.println("timezone: "+Integer.valueOf(timezone));
//		
//		Date date = new Date(1584697063 *1000L);
//		System.out.println("############ taiwan date: "+ ToolUtil.getInstance().convertDateToString(date, "yyyy-MM-dd HH:mm:ss", null));
//		System.out.println("############ japan date: "+ ToolUtil.getInstance().convertDateToString(date, "yyyy-MM-dd HH:mm:ss", "36"));
//		
//		
//		String hex = "5a5a3230333030303631";
//		String outputStr = ToolUtil.getInstance().convertHexToString(hex);
//		System.out.println("@@@@@@@@@@@@@ outputStr: "+ outputStr.toString());
//		
//		String nbID = "ZZ20300061";
//		String outputHex = ToolUtil.getInstance().convertStringToHex(nbID);
//		System.out.println("@@@@@@@@@@@@@ outputHex: "+outputHex);

//		System.out.println("");
//		String dateStr = "2020-03-06 16:54:39";
//		Date date = ToolUtil.getInstance().convertStringToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
//		System.out.println("$$$$$$$$$$$$$$$$$ date: "+date);
//		System.out.println("$$$$$$$$$$$$$$$$$ timestamp: "+date.getTime() +", /1000L: "+date.getTime()/1000L);
//
//		String timestamp = "1583452874";
////		String timestamp = String.valueOf(date.getTime()/1000L);
//		Date ddd = new Date(Long.valueOf(timestamp)*1000L);
//		System.out.println("$$$$$$$$$$$$$$$$$ ddd: "+ToolUtil.getInstance().convertDateToString(ddd, "yyyy-MM-dd HH:mm:ss"));
//		
//
//		System.out.println("");
////		char CR  = (char) 0x0D;	// \r
//		char LF  = (char) 0x0A; // \n
//	
////		String payload = "B16800110BC20BF30B840B1131513160314F316B166F" + LF;
//
//		String payload = "B16800110BC20BF30B840B1131513160314F316B166F" + LF +
//						 "B16801110D6C0C980C3B0D8D31FB31EC320B325016D1" + LF ;
//		
////		String payload = "B16800110BC20BF30B840B1131513160314F316B166F" + LF +
////						 "B16801110D6C0C980C3B0D8D31FB31EC320B325016D1" + LF +
////						 "B16800110BB30C0F0BA30B1131513164314F316B16C6" + LF +
////						 "B16801110D6C0C980C3B0D2831FB31E9320B32641645" + LF ;
//
//		String[] payloads = payload.split(String.valueOf(LF));
//    	System.out.println("######### payloads length: "+payloads.length);
//        for (int i=payloads.length-1 ; i>=0 ; i-- ) {
//        	String p = payloads[i];
//	    	System.out.println("######### p: "+p + ", length: "+p.length());
//		}
//		
//    	if (payload.indexOf("68")==0) {
//    		// 開頭加上B1，去掉尾數兩碼
//    		payload = "B1".concat(payload).substring(0, 44);
//    	}
//    	System.out.println("######### payload: "+payload + ", length: "+payload.length());
//		
//		int s = 0;
//		int e = 2;
//		int count = payload.length() / 2;
//		
//		int xor = 0;
//		for (int i=1 ; i<count ; i++) {
//			String str = payload.substring(s, e);
//			System.out.println("### str: "+str+ " XOR "+xor);
//			
//			int value = Integer.parseInt(str, 16);
//			
//			xor = value ^ xor;
//			System.out.println("=> "+xor);
//			System.out.println("");
//			
//			s = e;
//			e += 2;
//		}
//		
//		String checksum = payload.substring(s, e);
//		int checksumValue = Integer.parseInt(checksum, 16);
//
//		System.out.println("checksumValue: "+checksumValue);
//		
//		if (xor==checksumValue) {
//			System.out.println("OK ");
//		} else {
//			System.out.println("Error ");
//		}
	}
}
