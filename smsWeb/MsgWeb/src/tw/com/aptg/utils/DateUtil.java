package tw.com.aptg.utils;

import org.apache.commons.lang3.*;
import java.text.*;
import java.util.*;

/**
 * <p>Title: Taiwanese Date Conversion Until </p>
 * <p>Description: Taken western formated date(yyyy/mm/dd) to taiwan formated date (yyy/mm/dd) or reverse back</p>
 * @version 1.0
 */
public class DateUtil {

        private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        private static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyyMMdd HH:mm");
        private static SimpleDateFormat dataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        public DateUtil() {
        }

        public static java.util.Date strToDate(String date) {
                try {
                        Calendar setDate = Calendar.getInstance();
                        date = date.replaceAll("-", "/");
                        setDate.setTime(dateFormat.parse(date));
                        return setDate.getTime();
                } catch (ParseException e) {
                        e.printStackTrace();
                        return null;
                }
        }
       
        public static String getToday() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Calendar cal = Calendar.getInstance();
                return (dateFormat.format(cal.getTime())).substring(0, 10);
        }
        

        /**
         * @return Date
         */
        public static java.util.Date getTodayDate() {
                Calendar cal = Calendar.getInstance();
                return cal.getTime();
        }
       
        public static String conToTaiwanDate(Object obj) {
                if (obj == null) {
                        return "";
                } else if (obj.equals("")) {
                        return "";
                } else {
                        return conToTaiwanDate(dateFormat.format(obj));
                }
        }

        /**
         * des: Converting taiwan date format yyy/mm/dd into western date format yyy/mm/dd
         *
         * @param date String
         * @return String
         */
        public static String conToWesternDate(String date) {

                if (date == null || StringUtil.spaceTonull(date) == null) {
                        return null;
                }

                try {
                        int i = Integer.parseInt(date.substring(0, 3)) + 1911;
                        return (i + date.substring(3));
                } catch (Exception ex) {
                        return "error";
                }

        }

        /**
         * des: Converting werstent date format yyyy/mm/dd into taiwan date format yyy/mm/dd
         *
         * @param date String
         * @return String
         */
        public static String convertAdToMinguoDate(String date) {
                if (date == null || StringUtil.spaceTonull(date) == null) {
                        return null;
                }

                try {
                        int i = Integer.parseInt(date.substring(0, 4)) - 1911;
                        return (i < 100) ? ("0" + i + date.substring(4)) : (i + date.substring(4));
                } catch (Exception ex) {
                        return "error";
                }
        }

        /**
         * @param westDate String
         * @return int[]
         */
        public static int[] getIntYearMonthDay(String westDate) {
                int date[] = new int[3];

                if (westDate != null) {
                        date[0] = Integer.parseInt(westDate.substring(0, 4)); //year
                        date[1] = Integer.parseInt(westDate.substring(4, 6)); //month
                        date[2] = Integer.parseInt(westDate.substring(6, 8)); //day
                }
                return date;
        }

        /**
         * convert column value western date within array list into taiwanese date
         *
         * @param ColumPos int
         * @param list ArrayList
         * @return ArrayList
         */
        public static ArrayList conAListWesternToTWdate(int ColumPos, ArrayList list) {
                int row = Integer.parseInt(list.get(list.size() - 1).toString());
                int column = 0;
                if (list.size() - 1 > 0) {
                        column = (list.size() - 1) / row;
                }

                try {
                        //loop throug returned list and change the western date format to taiwan date format(YYYMMDD)
                        for (int i = 0; i < row; i++) {
                                list.set((ColumPos + (column * i)),
                                                 conToTaiwanDate((String) list.get(ColumPos + (i * column))));
                        }
                } catch (Exception ex) {
                        return null;
                }

                return list;
        }

        /**
         * pass in the key(s) that wish to be convert in a HashMap
         * convert column value western date within HashMap list into taiwanese date
         *
         * @param columnName String[]
         * @param map HashMap
         * @return HashMap
         */
        public static HashMap conAListWesternToTWdate(String[] columnName, HashMap map) {

                try {
                        //check user does pass in something for conversion
                        if (columnName != null) {
                                for (int i = 0; i < columnName.length; i++) {
                                        //check if key does exit for conversion
                                        if (map.containsKey(columnName[i])) {
                                                map.put(columnName[i],
                                                                conToTaiwanDate((String) map.get(columnName[i]))); //key update
                                        }
                                }
                        }
                } catch (Exception ex) {
                        return null;
                }

                return map;
        }

        /**
         * year only
         *
         * @return int
         */
        public static int getTWCurrentYearMonthDay() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Calendar cal = Calendar.getInstance();
                // retrive stage code match to that year
                int twCurrYear = Integer.parseInt((dateFormat.format(cal.getTime())).
                                substring(0, 4));
                return (twCurrYear -= 1911);

        }

        /**
         * retrurn today's day in western format yyyy/MM/dd in string format
         *
         * @return String
         */

        // Calendar轉String
        public static String calendar2Str(Calendar cal) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                return (dateFormat.format(cal.getTime())).substring(0, 10);
        }

        /**
         * retrurn today's day in western format yyyyMMdd in string format
         *
         * @return String
         */
        public static String getToday(boolean isHHmmDisplay) {
                Calendar cal = Calendar.getInstance();
                return (isHHmmDisplay) ?
                           dateTimeFormat.format(cal.getTime()) :
                           dateTimeFormat.format(cal.getTime()).substring(0, 8);
        }

        /**
         * @param isHHmmDisplayed boolean
         * @return String
         */
        public static String getTWTodayDate(boolean isHHmmDisplayed) {
                return conToTaiwanDate(getToday(isHHmmDisplayed));
        }

        /**
         * @param unknowDate Date
         * @return String
         */
        public static String toSimpleTWdateFormat(Object unknowDate) {
                if (unknowDate == null) {
                        return null;
                }
                if (! (unknowDate instanceof java.util.Date)) {
                        return "Type mismatch, Date required";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                System.out.println("date " +
                                                   conToTaiwanDate(dateFormat.format((java.util.Date)
                                                                   unknowDate)));
                return conToTaiwanDate(dateFormat.format((java.util.Date) unknowDate));
        }

        /**
         * 取得民國年表示的今天日期字串.
         *
         * @return 民國年表示的今天日期字串，例如: 0940711.
         */
        public static String getTwToday() {
                Calendar cal = Calendar.getInstance();
                int twYear = (cal.get(Calendar.YEAR) - 1911);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return StringUtils.leftPad(String.valueOf(twYear), 3, '0') +
                           StringUtils.leftPad(String.valueOf(month), 2, '0') +
                           StringUtils.leftPad(String.valueOf(day), 2, '0');
        }


        /**
         * 取得民國年表示的今天日期字串(有加/).
         *
         * @return 民國年表示的今天日期字串，例如: 094/07/11.
         */
        public static String getTwTodaySlash() {
                Calendar cal = Calendar.getInstance();
                int twYear = (cal.get(Calendar.YEAR) - 1911);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return StringUtils.leftPad(String.valueOf(twYear), 3, '0') + "/" +
                           StringUtils.leftPad(String.valueOf(month), 2, '0') + "/" +
                           StringUtils.leftPad(String.valueOf(day), 2, '0');
        }

        /**
         * 取得民國年表示的今天日期字串(有加年月日).
         *
         * @return 民國年表示的今天日期字串，例如: 094年07月11日.
         */
        public static String getTwTodayYMD() {
                Calendar cal = Calendar.getInstance();
                int twYear = (cal.get(Calendar.YEAR) - 1911);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return StringUtils.leftPad(String.valueOf(twYear), 3, '0') + "年" +
                           StringUtils.leftPad(String.valueOf(month), 2, '0') + "月" +
                           StringUtils.leftPad(String.valueOf(day), 2, '0') + "日";
        }

        /**
         * 取得以系統日期開始算起加減n天.
         *
         * @param n 增加的天數.
         * @return 以現在時間 + 增加的天數的日期，日期字串格式為民國年格式，例如: 0940711.
         */
        public static String getTwDate(int n) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, n);
                int twYear = (cal.get(Calendar.YEAR) - 1911);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return StringUtils.leftPad(String.valueOf(twYear), 3, '0') +
                           StringUtils.leftPad(String.valueOf(month), 2, '0') +
                           StringUtils.leftPad(String.valueOf(day), 2, '0');
        }

        /**
         * 取得從系統日期開始算起加減N天.
         *
         * @return Date
         */
        public static java.util.Date getDate(int n) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, n);
                return cal.getTime();
        }

        /**
         * 將民國格式日期字串轉換為日期物件.
         *
         * @param twDate 民國格式日期字串.
         * @return 日期/時間物件.
         */
        public static java.util.Date strTWDate2Date(String twDate, boolean isContainTime) {
                java.util.Date date = null;

                try {
                        if (twDate == null || twDate.length() == 0) {
                                return date;
                        }

                        String westDate = conToWesternDate(twDate);
                        date = isContainTime ? dateTimeFormat.parse(westDate) : dateFormat.parse(westDate);
                } catch (Exception e) {
                        return date;
                }

                return date;
        }

        /**
         * @param str
         * @return data
         */
        public static java.sql.Date strTWDate2SqlDate(String str) {
                java.sql.Date date = null;

                try {
                        if (str == null || str.length() == 0) {
                                return date;
                        }
                        String westDate = conToWesternDate(str);
                        date = new java.sql.Date(dateFormat.parse(westDate).getTime());
                } catch (Exception e) {
                        return date;
                }
                return date;
        }

        public static java.sql.Date strTWDate2SeqDateDetail(String str) {
                java.sql.Date date = null;
                try {
                        if (str == null || str.length() == 0) {
                                return date;
                        }
                        int i = Integer.parseInt(str.substring(0, 3)) + 1911;

                        String westDate = String.valueOf(i) + str.substring(3);
                        date = new java.sql.Date(dateTimeFormat.parse(westDate).getTime());
                } catch (Exception e) {
                        return date;
                }

                return date;
        }

        /**
         * @param str
         * @return data
         */
        public static java.sql.Timestamp strTWDate2SqlDateTime(String str) {
                java.sql.Timestamp date = null;

                try {
                        if (str == null || str.length() == 0) {
                                return date;
                        }
                        String westDate = conToWesternDate(str);
                        date = new java.sql.Timestamp(dateTimeFormat.parse(westDate).getTime());
                } catch (Exception e) {
                        e.printStackTrace();
                        return date;
                }

                return date;
        }

        /**
         * @return Date
         */
        public static java.sql.Timestamp getTodaySQLDate() {
                return (new java.sql.Timestamp(new java.util.Date().getTime()));
        }

        /**
         * 將java.util.Date轉為西元年格式的日期字串.
         *
         * @param date 日期/時間.
         * @param isHHmmDisplayed 是否把時間也做轉換.
         * @return 西元年格式的日期/時間字串，例如: 20050701 12:00:00.
         */
        public static String date2WestDateStr(java.util.Date date, boolean isHHmmDisplayed) {
                String str = "";
                if (date == null) {
                        return str;
                }
                str = (isHHmmDisplayed) ? dateTimeFormat.format(date) : dateFormat.format(date);

                return str;
        }

        /**
         * 將java.util.Date轉為民國格式的日期字串.
         *
         * @param date 日期/時間.
         * @param isHHmmDisplayed 是否把時間也做轉換.
         * @return 民國格式的日期/時間字串，例如: 0940701 12:00:00.
         */
        public static String date2TWDateStr(java.util.Date date, boolean isHHmmDisplayed) {
                return conToTaiwanDate(date2WestDateStr(date, isHHmmDisplayed));
        }

        /**
         * 將java.sql.Date轉為民國年的日期字串.
         *
         * @param date
         * @return 民國格式的日期字串，例如: 0940701.
         */
        public static String sqlDate2TWDateStr(java.sql.Date date) {
                String str = "";
                if (date == null || date.toString().length() == 0) {
                        return str;
                }

                str = conToTaiwanDate(dateFormat.format(date));

                return str;
        }

        /**
         * 將java.sql.Timestamp轉為民國年的日期字串.
         *
         * @param date Timestamp
         * @return 民國格式的日期/時間字串，例如: 0940701 12:00:00.
         */
        public static String sqlDate2TWDateStr(java.sql.Timestamp date) {
                String str = "";
                if (date == null || date.toString().length() == 0) {
                        return str;
                }

                str = conToTaiwanDate(dateTimeFormat.format(date));

                return str;
        }

        /**
         * @param date Timestamp
         * @param isHHmmDisplayed boolean
         * @return String
         */
        public static String sqlDate2TWDateStr(java.sql.Timestamp date, boolean isHHmmDisplayed) {
                String str = "";
                if (date == null || date.toString().length() == 0) {
                        return str;
                }
                str = (isHHmmDisplayed) ? dateTimeFormat.format(date) : dateFormat.format(date);
                str = conToTaiwanDate(str);

                return str;
        }

        /**
         * 日期/時間，轉換為民國年表示的日期時間字串. (包括時,分，但不包含秒)
         *
         * @param date Timestamp
         * @return String
         */
        public static String sqlDate2TWTimeStr(java.sql.Timestamp date) {
                if (date == null || date.toString().length() == 0) {
                        return "";
                }
                return conToTaiwanDate(dateTimeFormat2.format(date));
        }

        /**
         * @param objTwDate String,addDate int
         * @return dateStr2
         */
        public static String addDateObjTwDate(String objTwDate, int addDate) {
                String dateStr2 = null;
                if (objTwDate != null) {
                        try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                Date date1 = formatter.parse(DateUtil.conToWesternDate(
                                                objTwDate));

                                long longTime = date1.getTime() / 1000 + 60 * 60 * 24 * addDate;
                                date1.setTime(longTime * 1000);
                                dateStr2 = formatter.format(date1);
                                dateStr2 = DateUtil.conToTaiwanDate(dateStr2);
                        }
                        catch (Exception ex) {
                                ex.printStackTrace();
                        }
                }

                return dateStr2;
        }

        /**
         * @param westernDateStr String
         * @return String
         */
        public static String westernDateStrToFormDataWesternDateStr(String
                        westernDateStr) {
                String formDataWesternDateStr = null;

                if (westernDateStr != null) {
                        try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
                                Date date1 = formatter.parse(westernDateStr);

                                long longTime = date1.getTime() / 1000;
                                date1.setTime(longTime * 1000);
                                formDataWesternDateStr = formatter2.format(date1);
                        }
                        catch (Exception ex) {
                                ex.printStackTrace();
                        }
                }
                return formDataWesternDateStr;
        }

        /**
         * @param date String
         * @return String
         */
        public static String conToFormDataWesternDate(String date) {

                if (date == null || StringUtil.spaceTonull(date) == null) {
                        return null;
                }

                try {
                        return (westernDateStrToFormDataWesternDateStr(conToWesternDate(date)));
                } catch (Exception ex) {
                        ex.printStackTrace();
                        return "error";
                }

        }

        public static String getROCdayStr(String date, String type) {
                StringBuffer x = new StringBuffer("");
                try {
                        if (type == null) {
                                x.append("中華民國");
                                x.append(date.substring(0, 3));
                                x.append("年");
                                x.append(date.substring(3, 5));
                                x.append("月");
                                x.append(date.substring(5, 7));
                                x.append("日");
                                x.append(date.substring(8, 10));
                                x.append("時");
                                x.append(date.substring(11, 13));
                                x.append("分");
                        } else if (type.equals("01")) {
                                x.append("中華民國");
                                x.append(date.substring(0, 3));
                                x.append("年");
                                x.append(date.substring(3, 5));
                                x.append("月");
                                x.append(date.substring(5, 7));
                                x.append("日");
                                x.append(date.substring(8, 10));
                                x.append("時");
                                x.append(date.substring(11, 13));
                                x.append("分");
                        } else if (type.equals("02")) {
                                x.append(date.substring(0, 3));
                                x.append("年");
                                x.append(date.substring(3, 5));
                                x.append("月");
                                x.append(date.substring(5, 7));
                                x.append("日");
                        } else if (type.equals("03")) {
                                x.append("填發日期:");
                                x.append(date.substring(0, 3));
                                x.append("年");
                                x.append(date.substring(3, 5));
                                x.append("月");
                                x.append(date.substring(5, 7));
                                x.append("日");
                        }
                }
                catch (Exception ex) {
                        ex.printStackTrace();
                }
                return x.toString();
        }

        public static String getTodayMillisecond() {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.S");
                Date currentTime = new Date();
                return formatter.format(currentTime);
        }

        /**
         * 取出timeformat(yyyyMMdd HH:mm:ss)格式字串中的日期.
         *
         * @param timeFormat String
         * @return String
         */
        public static String getTimeFormatDate(String timeFormat) {
                if (timeFormat != null && timeFormat.length() >= 7) {
                        return timeFormat.substring(0, 7);
                } else {
                        return timeFormat;
                }
        }

        /**
         * 取出timeformat(yyyyMMdd HH:mm:ss)格式字串中的時間
         *
         * @param timeFormat String
         * @return String
         */
        public static String getTimeFormatTime(String timeFormat) {
                if (timeFormat != null && timeFormat.length() >= 16) {
                        return timeFormat.substring(8, 16);
                } else {
                        return timeFormat;
                }
        }

        /**
         * 將資料庫rs.getString的日期轉換為Calendar
         *
         * @param str
         * @return data
         */
        public static Calendar strRsDateToCalendar(String str) {
                Calendar cal = Calendar.getInstance();

                try {
                        if (str == null || str.length() == 0) {
                                return cal;
                        }
                        cal.setTimeInMillis(dataBaseFormat.parse(str).getTime());
                } catch (Exception e) {
                        e.printStackTrace();
                        return cal;
                }
                return cal;
        }

        /**
         * 根據輸入日期，加減月份數量 </p>
         * EX: 2005/02/10  -2  -> 2004/12/10
         *
         * @param date 要運算的日期.
         * @param rotate 要加減的月份數量.
         * @return 計算後日期.
         */
        public static java.util.Date rotateMonth(java.util.Date date, int rotate) {
                java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
                cal.setTime(date);
                cal.add(java.util.GregorianCalendar.MONTH, rotate);
                return cal.getTime();
        }
           
    	public static String rotateDate(java.util.Date date, int rotate) {

    		java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ROOT);
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);

    		/*
    		 * Calendar.YEAR 代表加減年 Calendar.MONTH 代表加減月份 Calendar.DATE 代表加減天數
    		 * Calendar.HOUR 代表加減小時數 Calendar.MINUTE 代表加減分鐘數 Calendar.SECOND 代表加減秒數
    		 */
    		cal.add(Calendar.DATE, rotate);
    		return (dateFormat.format(cal.getTime())).toString();
    	}
    	
    	public static String adjustMonth(java.util.Date date, int rotate) {

    		java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ROOT);
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.MONTH, rotate);
    		return (dateFormat.format(cal.getTime())).toString();
    	}

        /**
         * 取得當月份月底日期 </p>
         * EX: 2005/02/10  -> 2004/02/29
         *
         * @param date 日期.
         * @return 計算後日期.
         */
        public static java.util.Date getEndOfMonth(java.util.Date date) {
                java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
                cal.setTime(date);
                int year = cal.get(java.util.GregorianCalendar.YEAR);
                int month = cal.get(java.util.GregorianCalendar.MONTH); // ZERO-Based!
                int days = 0;
                switch (month) {
                        case 0  :
                                days = 31;
                                break;
                        case 1  :
                                days = 28;
                                break;
                        case 2  :
                                days = 31;
                                break;
                        case 3  :
                                days = 30;
                                break;
                        case 4  :
                                days = 31;
                                break;
                        case 5  :
                                days = 30;
                                break;
                        case 6  :
                                days = 31;
                                break;
                        case 7  :
                                days = 31;
                                break;
                        case 8  :
                                days = 30;
                                break;
                        case 9  :
                                days = 31;
                                break;
                        case 10 :
                                days = 30;
                                break;
                        case 11 :
                                days = 31;
                                break;
                }
                if (cal.isLeapYear(year) && month == 1) {
                        days = 29;
                }
                cal.set(java.util.GregorianCalendar.DATE, days);
                return cal.getTime();
        }
       
    /**
     * 取得YYYY/MM/DD格式
     *
     * @param strDate (String)   欲轉換日期.(EX:0950427 01:02:03)
     * @return 格式化後的YYYY/MM/DD格式 (String).
     */
    public static String getWestDate ( String strDate ) {

        StringBuffer sb = null;
        if ( strDate.length() < 7 || strDate.length() > 16) {
            return null;
        }

        sb = new StringBuffer();

        sb.append(String.valueOf(Integer.parseInt(strDate.substring(0,3))+1911));
        sb.append("/");
        sb.append(strDate.substring(3,5));
        sb.append("/");
        sb.append(strDate.substring(5,7));

        return String.valueOf(sb);
    }
    
	/**
	 * @description:convertDateFormat
	 * @author Gary Chang
	 * @param date 
	 * @return String
	 */
	
	public static String convertDateFormat(java.util.Date date) {
		return (dataBaseFormat.format(date)).toString();
	}
}


