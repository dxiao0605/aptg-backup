/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2015
 *
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20161028    Gary Chang          新增isDateDiffOver90days
 * 02        20171113    Gary Chang          Fix Password Management: Weak Password Policy ( 11496 )
 *                                           The password does not contain both uppercase and lowercase alphabets.
 *                                           The password does not contain special characters.
 *                                           The password does not contain the required number of tokens. 
 *                                           Set of [Letters], [Numbers], [Specialcharacters] are defined as three distinct tokens in WI. 
 *                                           To increase the password entropy, it is recommended that the password contain a minimum of 4 of these tokens in any combination.
 * 
 */

package tw.com.aptg.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author garychang
 *
 */
public class ValidatorUtil {
	public static boolean isEmptyField(String str) {
		if (str == null) {
			return true;
		}
		
		if (str.length() > 0) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidPasswordDeprecated(String password) {
		//密碼長度小於8
		if (password.length() < 8){
			return false;
		}
		
		//密碼全部是數字
		if (isNumeric(password)){
			return false;
		}
		
		//密碼全部是英文字母
		if (isAlphabet(password)){
			return false;
		}
				
		//密碼是數字或英文字母大小寫或特殊符號，除了=[]
		//if (password.matches("[0-9a-zA-Z~!@#$%^&*()_+`-\\{}|,./<>?]+")) {
		if (password.matches("[0-9a-zA-Z`-~!@#$%^&*()_+{}|\\;':\",./<>?]+")) {	
			return true;
		}
		
		return false;
	}
	
	public static boolean isLetterAndDigitCompose(String string) {
		
		//全部是數字
		if (isNumeric(string)){
			return false;
		}
		
		//全部是英文字母
		if (isAlphabet(string)){
			return false;
		}
				
		//英文字母大小寫開頭並含數字
		if(string.matches("^[a-zA-z](.*)") && string.matches("(.*)\\d+(.*)")){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 校验密码
	 * 1、长度不小于8位
	 * 2、必须以字母开头
	 * 3、必须包含特殊字符
	 * 4、必须包含数字
	 * @param pwd
	 * @return
	 */
	public static boolean isValidPasswordDeprecated2(String pwd){
		if(StringUtils.isEmpty(pwd)){
			return false;
		}
		if(pwd.length() < 8){
			return false;
		}

		if(pwd.matches("^[a-zA-z](.*)") && pwd.matches("(.*)[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:\"<>?]+(.*)") && pwd.matches("(.*)\\d+(.*)")){
			return true;
		}
	
		return false;
	}
	
	/*
	 * The password policy is:
	 * •	At least 8 chars
	 * •	Contains at least one digit
	 * •	Contains at least one lower alpha char and one upper alpha char
	 * •	Contains at least one char within a set of special chars (@#%$^ etc.)
	 * •	Does not contain space, tab, etc.
	 * 
	 * Regular Expression Test Page for Java
	 * http://www.regexplanet.com/advanced/java/index.html
	 * 
	 */

	public static boolean isValidPassword(String pwd){
		
		if(StringUtils.isEmpty(pwd)){
			return false;
		}
		
		if(pwd.length() < 8){
			return false;
		}
		
		if(pwd.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
			return true;
		}
	
		/*
		 * Explanation:
		 * ^                 # start-of-string
		 * (?=.*[0-9])       # a digit must occur at least once
		 * (?=.*[a-z])       # a lower case letter must occur at least once
		 * (?=.*[A-Z])       # an upper case letter must occur at least once
		 * (?=.*[@#$%^&+=])  # a special character must occur at least once
		 * (?=\S+$)          # no whitespace allowed in the entire string
		 * .{8,}             # anything, at least eight places though
		 * $                 # end-of-string
		 * 
		 */
		
		return false;
	}
	
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isAlphabet(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]*");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 
	 * 驗證手機門號
	 * 
	 */
	public static boolean isTwMobilePhone (String checkValue) {
		
		//必須為10碼
		Pattern pattern = Pattern.compile("^\\d{10}$",Pattern.CANON_EQ);
		Matcher m = pattern.matcher(checkValue);
		boolean response = m.matches();
		if (response) {
			//前二碼必須為09
			if (checkValue.startsWith("09")){
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 營利事業統一編號檢查程式 可至 http://www.etax.nat.gov.tw/ 查詢營業登記資料
	 * 
	 * @since 2006/07/19 
	 * 
	 * 邏輯：
	 * (一) 長度：共八位，，全部為數字型態。 
	 * (二) 計算公式
	 *  1、各數字分別乘以1,2,1,2,1,2,4,1。 
	 *  2、公式如下: D1 D2 D3 D4 D5 D6 D7 D8 * 1 2 1 2 1 2 4 1 (第一列 * 第二列)
	 * 
	 * A1 B1 A2 B2 A3 B3 A4 B4 (Bx：相乘後的十位數) + C1 C2 C3 C4 (Cx：相乘後的個位數)
	 * 
	 * X1 X2 X3 X4 X5 X6 X7 X8 (Xx：相加後的十位數) Y7 (Yx：相加後的個位數) 
	 * Z1= X1 + X2 + X3 + X4 + X5 + X6 + X7 + X8  或 
	 * Z1= X1 + X2 + X3 + X4 + X5 + X6 + Y7 + X8
	 *
	 * 3、當第 7 位數為 7 者，可取相加之倒數第二位取 0 及 1 來計算如 Z1 及 Z2 計算其和。 
	 * 
	 * 4、假如 Z1 或 Z2 能被 10整除，則表示營利事業統一編號正確。 
	 * 
	 * (三) 範例 ( 以 0 0 2 3 8 7 7 8 為例 ) 0 0 2 3 8 7 7 8  * 1 2 1 2 1 2 4 1 (第一列 * 第二列)
	 * 0 0 2 6 8 1 2 8 (Bx：相乘後的十位數) + 4 8 (Cx：相乘後的個位數)
	 * 0 0 2 6 8 5 1 8 (Xx：相加後的十位數) 0 (Yx：相加後的個位數) Z1= 0+ 0+ 2+ 6+ 8+ 5+ 1+ 8 = 30
	 * 或 Z2= 0+ 0+ 2+ 6+ 8+ 5+ 0+ 8 = 29 因 30 能被 10 整除，故營利事利統一編號正確。
	 * 
	 */
	public static boolean isValidTWBID(String twbid) {
		boolean result = false;
		String weight = "12121241";
		boolean type2 = false; // 第七個數是否為七
		if (Pattern.compile("^[0-9]{8}$").matcher(twbid).matches()) {
			int tmp = 0, sum = 0;
			for (int i = 0; i < 8; i++) {
				tmp = (twbid.charAt(i) - '0') * (weight.charAt(i) - '0');
				sum += (int) (tmp / 10) + (tmp % 10); // 取出十位數和個位數相加
				if (i == 6 && twbid.charAt(i) == '7') {
					type2 = true;
				}
			}
			if (type2) {
				if ((sum % 10) == 0 || ((sum + 1) % 10) == 0) { // 如果第七位數為7
					result = true;
				}
			} else {
				if ((sum % 10) == 0) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
	public static boolean isStartDateBeforeEndDate(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			if (d1.before(d2)) {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	
	public static boolean isStartDateAfterEndDate(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			if (d1.after(d2)) {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	
	public static boolean isDateDiffOver30days(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			long diff = d2.getTime() - d1.getTime();
			if ((diff/(1000*60*60*24)) > 30){
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	
	public static boolean isDateDiffOver90days(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			long diff = d2.getTime() - d1.getTime();
			if ((diff/(1000*60*60*24)) > 90){
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	public static boolean isDateDiffOver1months(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			//一個月前
			Date d3 = df.parse(DateUtil.adjustMonth(d2, -1));
			
			if (d1.before(d3)) {
				return true;
			}
			
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
		
	public static boolean isDateDiffOver3months(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = df.parse(startDate);
			Date d2 = df.parse(endDate);
			//三個月前
			Date d3 = df.parse(DateUtil.adjustMonth(d2, -3));
			
			if (d1.before(d3)) {
				return true;
			}
			
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	
}
