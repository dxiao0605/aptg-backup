/**
 * 字串函式類別
 */

package tw.com.aptg.utils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class StringUtil {

  /**
   * Transforms a string delimited by specified delimiter into a string array.
   *
   * @param target
   *            the string to be transformed.
   * @param delim
   *            the delimiter.
   * @return the string array transformed from the target string.
   */
  public static String[] toArray(String target, String delim) {
    if (target == null) {
      return new String[0];
    }

    StringTokenizer st = new StringTokenizer(target, delim);
    String[] result = new String[st.countTokens()];
    int i = 0;

    while (st.hasMoreTokens()) {
      result[i] = st.nextToken();
      i++;
    }

    return result;
  }

  /**
   * remove duplicated elements in source String array
   * @param source
   * @return v
   */
  public static String[] removeDuplicate(String[] source){
          Vector v=new Vector(source.length);
          for(int i=0;i<source.length;i++){
                  if(!v.contains(source[i]))
                          v.add(source[i]);
          }
          if(v.size()>0){
                  v.trimToSize();
                  String[] s=new String[0];
                  return (String[])v.toArray(s);
          }
          else
                  return null;

  }
  /**
   * Returns a string resulting from replacing all occurrences of oldStr in
   * the target string with newStr.
   *
   * @param str
   *            the original string.
   * @param oldStr
   *            the string to be replaced.
   * @param newStr
   *            the new string to be replaced with.
   * @return a string derived from the original string by replacing all
   *         occurrences of oldStr in the original string with newStr.
   */
  public static String replaceStr(String str, String oldStr, String newStr) {
    int s = 0;
    int e = 0;
    int ol = oldStr.length();
    StringBuffer result = new StringBuffer();

    while ( (e = str.indexOf(oldStr, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(newStr);
      s = e + ol;
    }
    result.append(str.substring(s));
    return result.toString();
  }

  /**
   * return "" string if param string is null
   * @param str String
   * @return String
   */
  public static String nullToSpace(String str) {
    return (str != null && !str.equals("null")) ? str : "";
  }

  /**
   * return "" string if param string is null
   * @param l long
   * @return String
   */
  public static String nullToSpace(long l) {
        String str=String.valueOf(l);
    return (str != null && !str.equals("null")) ? str : "";
  }  
  /**
   *
   * @param str String
   * @return String
   */
  public static String spaceTonull(String str) {
    return (str != null && str.trim().equals("")) ? null : str;
  }

  /**
   * loop trough entire ArrayList value and convert null value into " "
   * @param colSet ArrayList
   * @return Collection
   */
  public static Collection nullToSpace(ArrayList colSet) {
    try {
      if (colSet != null) {
        for (int i = 0; i < colSet.size(); i++) {
          if (colSet.get(i) == null || "null".equals(colSet.get(i))) {
            colSet.set(i, "");
          }
        }
      }
    }
    catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
    return colSet;
  }

  /**
   *
   * @param o Object
   * @return Object
   */
  public static Object nullToSpace(Object o) {
    return (o != null && !"null".equals(o.toString())) ? o : "";
  }

  /**
   * loop trough entire HashMap value and convert null value into " "
   * @param colSet HashMap
   * @return Map
   */
  public static Map nullToSpace(HashMap colSet) {
    String temp = "";

    if (colSet == null || colSet.size() == 0) {
      return null;
    }
    // System.out.println("null to space with size of "+colSet.size());
    Iterator it = colSet.keySet().iterator();
    while (it.hasNext()) {
      temp = "" + it.next();
      if (colSet.get(temp) == null || "null".equals(colSet.get(temp))) {
//        System.out.println("find null of " + temp);
        colSet.put(temp, "");
      }
    }

    return colSet;
  }

  /**
   *
   * @param src String
   * @param str String
   * @return int
   */
  public static int countOccurance(StringBuffer src, String str) {
    int count = -1;
    for (int index = 0; index >= 0; count++) {
      index = src.indexOf(str, index + 1);
    }
    return count;
  }

  /**
   *
   * @param value String
   * @param reqLength int
   * @param fillBy char
   * @param appendPos char
   * @return String
   */
  public static String fillStringValue(String value, int reqLength, char fillBy,
                                       char appendPos) {

    if (value == null || value.length() > reqLength) {
      return value;
    }

    StringBuffer temp = new StringBuffer();

    while (reqLength > value.length()) {
      temp.append(fillBy);
      reqLength--;
    }
    return (appendPos == 'F' || appendPos == 'f') ? temp + value : value + temp;
  }

  /**
   *
   * @param src Map
   * @return Map
   */
  public static Map trimMapValues(Map src) {

    if (src == null || src.size() == 0) {
      return src;
    }
//    System.out.println("got a map: " + src.size());
    try {

      String temp = "";
      Iterator it = src.keySet().iterator();
      while (it.hasNext()) {
        temp = "" + it.next();
        if (src.get(temp) != null) {
          // System.out.println("triming "+temp+", length "+src.get(temp).toString().length());
          src.put(temp, src.get(temp).toString().trim());
//          System.out.println("after triming "+temp+", length "+src.get(temp).toString().length());
        }
      }
    }
    catch (Exception ex) {
      System.out.println("Exception at String Util trimMap: " + ex.getMessage());
    }
    return src;
  }

  /**
   * will return true if string is null or only contain white space(s)
   * @param val String
   * @return boolean
   */
  public static boolean isEmpty(String val) {
    return (val != null && val.trim().length() > 0 && !"null".equals(val)) ? false : true;
  }



  /**
   * add by kuo
   * Expression should not contain char "~"
   * @param content String
   * @param regex String
   * @return String[]
   */
  public static String[] patternFinder(String content, String regex) {
    final String token = "~";
    StringBuffer temp = new StringBuffer("");
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(content);

    while (m.find()) {
      temp.append("~" + m.group());
    }
    return toArray(temp.toString(), token);
  }

  /**
   *
   * @param content String
   * @param regex String
   * @return String[]
   */
  public static String[] patternSpliter(String content, String regex) {
    Pattern p = Pattern.compile(regex);
    return p.split(content);
  }

  /**
   *
   * @param val StringBuffer
   * @return boolean
   */
  public static boolean isStringEmpOrNull(StringBuffer val) {
    return (val != null && val.toString().trim().length() > 0) ? false : true;
  }

  /**
   *
   * @param obj
   * @return obj
   */
  public static String checkString(Object obj) {
    if (obj == null) {
      return "";
    }
    return ( (String) obj).trim();
  }

  public static String getTWMoney(String y) {
    String numberStr = "";
    try {
      String numberName[] = {
          "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"};
      String numberUnitStr = "";
      String tempStr = "";

      int numCount = 0;
      int total = 0;

      tempStr = Integer.toString(total);
      tempStr = y;
      numberUnitStr = "拾億仟佰拾萬仟佰拾元".substring(10 - (tempStr.length()), 10);
      numberStr = "";
      for (numCount = 0; numCount < tempStr.length(); numCount++) {
        if (! (tempStr.substring(numCount, numCount + 1).equals("0"))) {
          numberStr +=
              numberName[Integer.parseInt(tempStr.substring(numCount,
              numCount + 1))] + numberUnitStr.substring(numCount, numCount + 1);
        }
        else if ( (numCount == tempStr.length() - 1)) {
          numberStr += numberUnitStr.substring(numCount, numCount + 1);
        }
        else if ( ( (tempStr.length() - numCount) == 5) ||
                 ( (tempStr.length() - numCount) == 9)) {
          if (tempStr.substring(numCount + 1, numCount + 2).equals("0")) {
            numberStr += numberUnitStr.substring(numCount, numCount + 1);
          }
          else {
            numberStr += numberUnitStr.substring(numCount, numCount + 1) + "零";
          }
        }
        else if ( ( (numCount + 1) <= tempStr.length() - 1) &&
                 (tempStr.substring(numCount + 1, numCount + 2).equals("0"))) {}
        else {
          numberStr +=
              numberName[Integer.parseInt(tempStr.substring(numCount,
              numCount + 1))];
        }
      }
      numberStr += "整";

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return numberStr;
  }

  // 身份證字號檢查傳入字串, 0：成功, 1：失敗



  public int CheckIDNO(String szIDNO){
    int result = 0;
//    char[] idList = szIDNO.toCharArray();
    // 先檢查長度.
    if (szIDNO.length() != 10)
      return 1;
    result = (checkID(szIDNO)) ? 0 : 1;//this.editrtn(idList);
    return result;
  }

    public static boolean checkID(String Chk2){
          String v[][] = {{"A", "台北市"}, {"B", "台中市"}, {"C", "基隆市"},
             {"D", "台南市"}, {"E", "高雄市"}, {"F", "台北縣"}, {"G", "宜蘭縣"},
             {"H", "桃園縣"}, {"J", "新竹縣"}, {"K", "苗栗縣"}, {"L", "台中縣"},
             {"M", "南投縣"}, {"N", "彰化縣"}, {"P", "雲林縣"}, {"Q", "嘉義縣"},
             {"R", "台南縣"}, {"S", "高雄縣"}, {"T", "屏東縣"}, {"U", "花蓮縣"},
             {"V", "台東縣"}, {"X", "澎湖縣"}, {"Y", "陽明山"}, {"W", "金門縣"},
             {"Z", "連江縣"}, {"I", "嘉義市"}, {"O", "新竹市"}
          };

          int inte = -1;
          String s1 = String.valueOf(Character.toUpperCase(Chk2.charAt(0)));
          for(int i = 0; i < 26; i++){
             if(s1.compareTo(v[i][0]) == 0){
                inte = i;
             }
          }
          int total = 0;
          int all[] = new int[11];
          String E = String.valueOf(inte + 10);
          int E1 = Integer.parseInt(String.valueOf(E.charAt(0)));
          int E2 = Integer.parseInt(String.valueOf(E.charAt(1)));
          all[0] = E1;
          all[1] = E2;
          try{
             for(int j = 2; j <= 10; j++)
                all[j] = Integer.parseInt(String.valueOf(Chk2.charAt(j - 1)));
             for(int k = 1; k <= 9; k++)
                total += all[k] * (10 - k);
             total += all[0] + all[10];
             if(total % 10 == 0)
                return true;
          }
          catch(Exception ee){;
          }
          return false;
       }

}

