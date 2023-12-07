package aptg.cathaybkeco.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;


public class CsvUtil {
	/**
	 * 輸出CSV檔
	 * 
	 * @param workbook
	 * @param fileName
	 * @param response
	 * @throws Exception 
	 */
	public static void exportCsv(StringBuilder str, String fileName, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {			
			response.setCharacterEncoding("BIG5");
//			response.setCharacterEncoding("gb18030");

			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));			
			out = response.getWriter();
//			out.write('\ufeff');//設定BOM，解決中文亂碼問題
			out.write(str.toString());
		} catch (IOException ex) {
			throw new Exception(ex.toString());
		}finally {
			if(out!=null) {
				out.flush();
				out.close();				
			}			
		}
	}
	
	public static String csvHandlerStr(String str) {
		//csv格式如果有逗號，整體用雙引號括起來；如果裡面還有雙引號就替換成兩個雙引號，這樣匯出來的格式就不會有問題了			
		String tempDescription=str;
		//如果有逗號
		if(str.contains(",")){				
			//如果還有雙引號，先將雙引號轉義，避免兩邊加了雙引號後轉義錯誤
			if(str.contains("\"")){
				tempDescription=str.replace("\"", "\"\"");
			}
			//在將逗號轉義
			tempDescription="\""+tempDescription+"\"";
		}
		return tempDescription;
	}
	
	public static String getBOM() {
		   byte b[] = {(byte)0xEF,(byte)0xBB,(byte)0xBF};
		   return new String(b);
	}
}
