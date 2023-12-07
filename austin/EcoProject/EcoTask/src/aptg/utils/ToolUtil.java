package aptg.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
	 * 	時間格式String轉換成Date
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
	 * 	取得當日00:00:00時間
	 * 
	 * @return
	 */
	public Date getInitToday() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		return today.getTime();
	}

	/**
	 * 	取得昨日00:00:00時間
	 * 
	 * @return
	 */
	public Date getInitYesterday() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.DATE, -1);
		return today.getTime();
	}
	
	/**
	 * 	取得隔日00:00:00時間
	 * 
	 * @return
	 */
	public Date getInitTomorrow() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.DATE, 1);
		return today.getTime();
	}
	
	public Calendar setDateEndTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	/**
	 * 	取得當月第一天時間
	 * 
	 * @return
	 */
	public Calendar getThisMonthStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	/**
	 * 取得指定日期當月份開始時間
	 * 
	 * @param cal
	 * @return
	 */
	public Calendar getMonthStart(Calendar cal) {
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	/**
	 * 取得指定日期下個月份開始時間
	 * 
	 * @param cal
	 * @return
	 */
	public Calendar getNextMonthStart(Calendar cal) {
		cal.add(Calendar.MONTH, 1);	// 指定日期的下一個月
		cal.set(Calendar.DATE, 1);	// 指定日期的下一個月的1號
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	/**
	 * 	取得指定間隔diff天的日期
	 * 
	 * @param date
	 * @param diff
	 * @return
	 */
	public Date getSpecifyDiffDate(Date date, int diff) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, diff);
		return cal.getTime();
	}
	
	/**
	 * 取得星期x
	 * 
	 * @param date
	 * @return
	 */
	public int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 	取得當月月份
	 * 
	 * @return
	 */
	public int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) +1;
		return month;
	}
	
	/**
	 * 取得年分
	 * 
	 * @param date
	 * @return
	 */
	public int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 	取得指定日期月份
	 * 
	 * @param date
	 * @return
	 */
	public int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) +1;
		return month;
	}
	/**
	 * 	取得月份
	 * 
	 * @param useMonth: yyyyMM
	 * @return month
	 */
	public int getMonth(String useMonth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ToolUtil.getInstance().convertStringToDate(useMonth, "yyyyMM"));
		int month = cal.get(Calendar.MONTH) +1;
		return month;
	}
	/**
	 * 	取得月份
	 * 
	 * @param recDate: yyyy-MM-dd
	 * @return month
	 */
	public int getRecDataMonth(String recDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ToolUtil.getInstance().convertStringToDate(recDate, "yyyy-MM-dd"));
		int month = cal.get(Calendar.MONTH) +1;
		return month;
	}
	
	/**
	 * 	是否為夏季
	 * 
	 * @param month
	 * @return
	 */
	public boolean isSummer(int month) {
		boolean isSummer = (month>=6 && month<=9) ? true : false;
		return isSummer;
	}
	
	/**
	 * 	實際用電天數
	 * 
	 * @param useTime: yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public BigDecimal getRealUseDay(String useTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ToolUtil.getInstance().convertStringToDate(useTime, "yyyy-MM-dd HH:mm:ss"));
		int days = cal.get(Calendar.DATE);
		return new BigDecimal(days);
	}
	
	/**
	 * 	實際月份天數
	 * 
	 * @param userMonth: yyyyMM
	 * @return
	 */
	public BigDecimal getMonthActualDays(String month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ToolUtil.getInstance().convertStringToDate(month, "yyyyMM"));
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return new BigDecimal(days);
	}
	
	/**
	 * check EcoTask.jar is executing
	 * 
	 * @return
	 */
	public List<String> executingJob() {
		List<String> list = new ArrayList<>();
		try {
//			String cmd = "ps aux | grep tomcat";
			String cmd = "ps aux | grep EcoTask";
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(new String[] {"/bin/sh", "-c", cmd});
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
//				if (line.contains("Bootstrap") && line.contains("tomcat"))
//					list.add(line);
				
				if (line.contains("java") && line.contains("EcoTask.jar"))
					list.add(line);
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return list;
	}
	
	public String getUUIDTailString() {
		String uuid = UUID.randomUUID().toString();
//		return uuid.substring(uuid.lastIndexOf("-")+1);
		return uuid.replace("-", "");
	}


	public String getStartdate(Date calculateDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(calculateDate);
		String startdate = ToolUtil.getInstance().convertDateToString(start.getTime(), "yyyy-MM-dd");
		return startdate;
	}
	public String getEnddate(Date calculateDate) {
		Calendar end = Calendar.getInstance();
		end.setTime(calculateDate);
		end.add(Calendar.DATE, 1);
		String enddate = ToolUtil.getInstance().convertDateToString(end.getTime(), "yyyy-MM-dd");
		return enddate;
	}
	
	public static void main(String[] args) {
		String dateStr = "2021-11-01";
		String partition = dateStr.substring(2, dateStr.length()).replace("-", "");
		System.out.println("pattition: "+partition);
		
//		BigDecimal percent = new BigDecimal(100);
//		BigDecimal test = new BigDecimal("0.21");
//		System.out.println("@@@@@@@@@@ test: "+test);
//		test = test.multiply(percent);
//		System.out.println("@@@@@@@@@@ test: "+test +" %");
//		
//		
//		String t = "2021-03-15 15:12:19";
//		Date tDate = ToolUtil.getInstance().convertStringToDate(t, "yyyy-MM-dd HH:mm");
//		
//		System.out.println("tDate: "+tDate);
//		
//		List<PowerRecordModel> list = new ArrayList<>();
//		PowerRecordModel a = new PowerRecordModel();
//		a.setDeviceID("aaa");
//		a.setRecTime("2021-01-19");
//		list.add(a);
//
//		PowerRecordModel b = new PowerRecordModel();
//		b.setDeviceID("aaa");
//		b.setRecTime("2021-01-05");
//		list.add(b);
		
		// (1)
//		list = list.stream()
//				   .sorted(Comparator.comparing(PowerRecordModel::getRecTime))
//				   .collect(Collectors.toList());;
		// (2)		
//		list.sort(Comparator.comparing(PowerRecordModel::getRecTime));
//		
//		System.out.println("list: "+JsonUtil.getInstance().convertObjectToJsonstring(list));
		
//		String d2 = "2021-01-23 23:58:00";
//		Date date2 = ToolUtil.getInstance().convertStringToDate(d2, "yyyy-MM-dd HH:mm:ss");
//		System.out.println("########### date2: "+date2.getTime());
//		
//		long t = date2.getTime() % (15*60*1000);
//		System.out.println("########### t: "+t);
//		
//		
//
//		System.out.println("");
//		String d = "2020-01-21";
//		Date recDate = ToolUtil.getInstance().convertStringToDate(d, "yyyy-MM-dd");
//		String m = ToolUtil.getInstance().convertDateToString(recDate, "MM");
//		System.out.println("m="+ Integer.valueOf(m));
//		
//		BigDecimal test = new BigDecimal(10.345);
//		BigDecimal test2 = new BigDecimal(13.556);
//		
//		test = test.setScale(0, BigDecimal.ROUND_FLOOR);
//		test2 = test2.setScale(0, BigDecimal.ROUND_FLOOR);
//		System.out.println("test: "+test);
//		System.out.println("test2: "+test2);
	}
}
