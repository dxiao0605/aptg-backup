package aptg.battery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
			response.setCharacterEncoding("UTF-8");
//			response.setCharacterEncoding("gb18030");

			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));			
			out = response.getWriter();
			out.write('\ufeff');//設定BOM，解決中文亂碼問題
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
	
	
	public static void exportCsvZip(File file, String fileName, HttpServletResponse response) throws Exception {
		OutputStream out = null;
		ZipOutputStream zip = null;
		byte[] buf = new byte[1024];
		try {

            out = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            
            zip = new ZipOutputStream(out);
            File[] fileList = file.listFiles();
            
            for(File csvfile : fileList) {
            	FileInputStream in = new FileInputStream(csvfile);
            	
	            //例項化一個壓縮實體
	            ZipEntry entry = new ZipEntry(csvfile.getName());
	            //將壓縮實體放入壓縮包
	            zip.putNextEntry(entry);
	            int len;
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
	            
	            in.close();
            }

        } catch (Exception e) {
        	throw new Exception(e.toString());
        }	finally {			
			if(zip!=null) {
				zip.flush();
	            zip.close();
			}
			response.flushBuffer();
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
