package aptg.utils;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
	
	public String convertDateToString(Date date, String format, int gmtVal) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
        	String gmt = (gmtVal>=0) ? "GMT+"+gmtVal : "GMT"+gmtVal;
        	
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
	public int convertTimeZoneToGMT(String timezone) {
		float gmt = (float) Integer.parseInt(timezone)*15/60;
		return (int) Math.round(gmt);
	}
	public int getServerGMT() {
		TimeZone defaultTimeZone = TimeZone.getDefault();
		int offset = defaultTimeZone.getRawOffset();
		int gmt = (offset / (15*60*1000)) * 15 / 60;
		return gmt;
	}

	/**
	 * GMT offset換算成timezone
	 * 
	 * @param gmtHr
	 * @return
	 */
	public int convertOffsetToTimezone(int gmtHr) {
		int timezone = gmtHr * 60 / 15;
		return timezone;
	}
	public Date convertTimestampToDate(long timestamp) {
		Date date = new Date(timestamp * 1000L);
		return date;
	}
	
	public String getStatusDesc(int status) {
		String desc;
		if (status==1)
			desc = "正常";
		else if (status==2)
			desc = "告警";
		else if (status==3)
			desc = "需更換";
		else
			desc = "離線";
		return desc;
	}
	
	public String getTaiwanTime(int GmtOffset, String RedoDailyRecDate) {
		Date time = convertStringToDate(RedoDailyRecDate, "yyyy-MM-dd HH:mm:ss");

		int taiwan = 8;
		int subtract = taiwan - GmtOffset;
		
		Calendar QueryCreateTimeCal = Calendar.getInstance();
		QueryCreateTimeCal.setTime(time);
		QueryCreateTimeCal.add(Calendar.HOUR_OF_DAY, subtract);
		
		return convertDateToString(QueryCreateTimeCal.getTime(), "yyyy-MM-dd HH:mm:ss");
	}

	public static void main(String[] args) {
//		int GmtOffset = ToolUtil.getInstance().findGMTOffset2();
//		String RedoDailyRecDate = ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", GmtOffset);
//		String QueryCreateTime = ToolUtil.getInstance().getTaiwanTime(GmtOffset, RedoDailyRecDate);
//		System.out.println("GmtOffset: "+GmtOffset);
//		System.out.println("RedoDailyRecDate: "+RedoDailyRecDate);
//		System.out.println("QueryCreateTime: "+QueryCreateTime);
		
//		int GmtOffset = 5;
//		String RedoDailyRecDate = "2021-08-02 00:00:00";
//		String QueryCreateTime = ToolUtil.getInstance().getTaiwanTime(GmtOffset, RedoDailyRecDate);
//		System.out.println("QueryCreateTime: "+QueryCreateTime);
		
//		int taiwan = 8;
//		Integer GmtOffset = 12;
//		String RedoDailyRecDate = "2021-07-30 00:00:00";
//		
//		Date ddd = ToolUtil.getInstance().convertStringToDate(RedoDailyRecDate, "yyyy-MM-dd HH:mm:ss");
//		System.out.println("ddd: "+ddd);
//
//		int gmtOffset = (GmtOffset!=null) ? GmtOffset : ToolUtil.getInstance().findGMTOffset2();
//		System.out.println("輸入GmtOffset: "+GmtOffset);
//		
//		String timeStr = (GmtOffset!=null) ? RedoDailyRecDate : ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss", gmtOffset);
//		System.out.println("timeStr: "+timeStr);
//		
//		Date time = ToolUtil.getInstance().convertStringToDate(timeStr, "yyyy-MM-dd HH:mm:ss");
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(time);
//		System.out.println("cal: "+ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss"));
//
//		int subtract = taiwan - GmtOffset;
//		Calendar summaryCreateCal = Calendar.getInstance();
//		summaryCreateCal.setTime(time);
//		summaryCreateCal.add(Calendar.HOUR_OF_DAY, subtract);
//		System.out.println("summaryCreateCal: "+ToolUtil.getInstance().convertDateToString(summaryCreateCal.getTime(), "yyyy-MM-dd HH:mm:ss"));
		
		
//		// 抓GMT+12 的目前時間
//		Calendar cal = Calendar.getInstance();
//		System.out.println("cal: "+ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss", -12));
//		// 抓系統GMT
//		int gmt = ToolUtil.getInstance().getServerGMT();
//		System.out.println("gmt: "+gmt);
//		
//		int lastTimeZone = ToolUtil.getInstance().convertOffsetToTimezone(-12);
//		System.out.println("lastTimeZone: "+lastTimeZone);
		
		
//		List<Integer> list1 = Arrays.asList(4, 5);
//		List<Integer> list2 = Arrays.asList(1,2,3,4);
//
//		List<Integer> list3 = new ArrayList<>();
//		list3.addAll(list2);
//		list3.addAll(list1);
//		list3 = list3.stream().distinct().collect(Collectors.toList());
//		
//		System.out.println(list3.toString());
		
		
//		int gmtOffset = -6;
//		int ttt = ToolUtil.getInstance().convertOffsetToTimezone(gmtOffset);
//		System.out.println(ttt);
//		
//		int gmt = ToolUtil.getInstance().convertTimeZoneToGMT("-28");
//		System.out.println(gmt);
		
		
//		String date = "2021-01-01 12:00:00";
//		Date dateD = ToolUtil.getInstance().convertStringToDate(date, "yyyy-MM-dd HH:mm:ss");
//		
//		Date now = new Date();
//		System.out.println("########## now: "+ToolUtil.getInstance().convertDateToString(now, "yyyy-MM-dd HH:mm:ss"));
//		
//		long diff = (long) (now.getTime()-60000);
//		System.out.println("########## diff: "+diff);
//				
//		System.out.println("########## warn time: "+ToolUtil.getInstance().convertDateToString(new Date(diff), "yyyy-MM-dd HH:mm:ss"));
		
		
		
//		String crt = "1981-01-22 14:59:16";
//		Date crtd = ToolUtil.getInstance().convertStringToDate(crt, "yyyy-MM-dd HH:mm:ss");
//
//		long diff = (long) (new Date().getTime()-crtd.getTime()) / 1000;
//		System.out.println("diff:"+ diff );
//		System.out.println("diff:"+ (new Date().getTime()-crtd.getTime()) );


		
//		int a = (int) Math.round(3.14);
//		System.out.println("a: "+a);
		
		
//		TimeZone timezone = TimeZone.getTimeZone("Asia/Tokyo");
//		System.out.println("ZoneInfo: "+timezone);
//		System.out.println("timezone.toZoneId(): "+timezone.toZoneId());
//		System.out.println("timezone.getRawOffset(): "+timezone.getRawOffset());
//		
//		System.out.println();
		
//		TimeZone defaultTimeZone = TimeZone.getDefault();
//		int offset = defaultTimeZone.getRawOffset();
//		System.out.println("defaultTimeZone.getRawOffset(): "+offset);
//		
//		int t = (offset / (15*60*1000)) * 15 / 60;
//		System.out.println("t: "+t);

//		int t2 = offset / (3600*1000);
//		System.out.println("t2: "+t2);
//
//		System.out.println();

		/*
		Calendar cal = Calendar.getInstance();
		
		List<Integer> GMT_Offset = Arrays.asList(12,11,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12);
		for (Integer offset: GMT_Offset) {
			if (offset<0) {
				cal.setTimeZone(TimeZone.getTimeZone("GMT" + offset));
			} else {
				cal.setTimeZone(TimeZone.getTimeZone("GMT+"+offset));	
			}

			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			System.out.println("offset: "+offset+", currentHour: "+currentHour);
		}
		*/
	}
}
