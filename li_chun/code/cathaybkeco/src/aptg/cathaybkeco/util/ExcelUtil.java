package aptg.cathaybkeco.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import aptg.cathaybkeco.config.SysConfig;
import sun.misc.BASE64Encoder;

public class ExcelUtil {

	/**
	 * 單元格樣式(標題)
	 * 
	 * @param workbook
	 * @param point
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getTitleStyle(XSSFWorkbook workbook, boolean border, int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		// 字型加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// 設定樣式;
		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 在樣式用應用設定的字型;
		style.setFont(font);
		// 設定自動換行;
		style.setWrapText(false);
		// 設定水平對齊的樣式為居中對齊;
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

		return style;
	}

	/**
	 * 單元格樣式(標題白色字體)
	 * 
	 * @param workbook
	 * @param point
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getTitleStyleW(XSSFWorkbook workbook, boolean border, int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		// 字型加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.WHITE.getIndex());
		// 設定樣式;
		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 在樣式用應用設定的字型;
		style.setFont(font);
		// 設定自動換行;
		style.setWrapText(false);
		// 設定水平對齊的樣式為居中對齊;
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(new XSSFColor(Color.DARK_GRAY));

		return style;
	}

	/**
	 * 單元格樣式
	 * 
	 * @param workbook
	 * @param alignment
	 * @param border
	 * @param point
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getTextStyle(XSSFWorkbook workbook, String alignment, boolean border, int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		} else {
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 設定自動換行;
		style.setWrapText(false);
		// 在樣式用應用設定的字型;
		style.setFont(font);

		return style;
	}
	
	/**
	 * 顏色單元格樣式
	 * 
	 * @param workbook
	 * @param alignment
	 * @param border
	 * @param point
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getTextStyle(XSSFWorkbook workbook, String alignment, boolean border, int point, short color) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		font.setColor(color);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		} else {
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 設定自動換行;
		style.setWrapText(false);
		// 在樣式用應用設定的字型;
		style.setFont(font);

		return style;
	}

	/**
	 * 單元格樣式(數字)
	 * 
	 * @param workbook
	 * @param alignment
	 * @param format
	 * @param border
	 * @param point
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getNumberStyle(XSSFWorkbook workbook, String alignment, String format, boolean border,
			int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		} else {
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}

		// 數字格式
		XSSFDataFormat dataFormat = workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat(format));

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 設定自動換行;
		style.setWrapText(false);
		// 在樣式用應用設定的字型;
		style.setFont(font);

		return style;
	}

	/**
	 * 顏色單元格樣式(數字)
	 * 
	 * @param workbook
	 * @param alignment
	 * @param format
	 * @param border
	 * @param point
	 * @param color
	 * @return XSSFCellStyle
	 */
	public static XSSFCellStyle getNumberStyle(XSSFWorkbook workbook, String alignment, String format, boolean border,
			int point, short color) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		font.setColor(color);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			// 設定左邊框;
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			// 設定右邊框;
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			// 設定頂邊框;
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		} else {
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}

		// 數字格式
		XSSFDataFormat dataFormat = workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat(format));

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 設定自動換行;
		style.setWrapText(false);
		// 在樣式用應用設定的字型;
		style.setFont(font);

		return style;
	}

	/**
	 * 創建單元格
	 * 
	 * @param row
	 * @param style
	 * @param column
	 * @param cellType
	 * @param value
	 * @return XSSFCell
	 */
	public static XSSFCell createCell(XSSFRow row, XSSFCellStyle style, int column, int cellType, Object value) {
		XSSFCell cell = row.createCell(column);
		if (style != null)
			cell.setCellStyle(style);
		switch (cellType) {
		case XSSFCell.CELL_TYPE_BLANK: {
			break;
		}
		case XSSFCell.CELL_TYPE_STRING: {
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (value != null && StringUtils.isNotBlank(value.toString()))
				cell.setCellValue(value.toString());
			break;
		}
		case XSSFCell.CELL_TYPE_NUMERIC: {
			cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
			if (value != null && StringUtils.isNotBlank(value.toString())) {
				cell.setCellValue(Double.parseDouble(value.toString()));
			} else {
				cell.setCellValue(0);
			}
			break;
		}
		default:
			break;
		}
		return cell;
	}

	/**
	 * 創建空白單元格
	 * 
	 * @param row
	 * @param style
	 * @param column
	 * @param cellType
	 * @param value
	 * @return XSSFCell
	 */
	public static XSSFCell createBlankCell(XSSFRow row, XSSFCellStyle style, int column) {
		XSSFCell cell = row.createCell(column);
		if (style != null)
			cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 輸出報表
	 * 
	 * @param workbook
	 * @param fileName
	 * @param response
	 * @throws Exception
	 */
	public static void exportXlsx(XSSFWorkbook workbook, String fileName, HttpServletResponse response)
			throws Exception {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			workbook.write(out);
		} catch (IOException ex) {
			throw new Exception(ex.toString());
		} finally {
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
	
	/**
	 * 輸出報表Base64
	 * 
	 * @param workbook
	 * @param fileName
	 * @param response
	 * @throws Exception
	 */
	public static String exportBase64(XSSFWorkbook workbook, String fileName)
			throws Exception {
		FileOutputStream out = null;
		File file = null;
		InputStream in = null;  
        byte[] data = null;  
        try {  
        	String path = SysConfig.getInstance().getTempDir()+fileName;
			file = new File(path);			
			out = new FileOutputStream(file);
			workbook.write(out);
        	
            in = new FileInputStream(file);          
            data = new byte[in.available()];  
            in.read(data);  
        }catch (IOException e)   {  
        	throw new Exception(e.toString());  
        }  finally {
			if (in != null) 
				in.close();			
			if(out!=null)
				out.close();
			if(file!=null)
				file.delete();
		}
        //對位元組陣列Base64編碼  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data).replaceAll("\r|\n", "");//返回Base64編碼過的位元組陣列字串  
	}
	

	/**
	 * 自動欄寬(中文支持)
	 * 
	 * @param sheet
	 * @param size
	 */
	public static void setSizeColumn(XSSFSheet sheet, int size) {
		for (int columnNum = 0; columnNum < size; columnNum++) {
			int columnWidth = sheet.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				XSSFRow currentRow;
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}

				if (currentRow.getCell(columnNum) != null) {
					XSSFCell currentCell = currentRow.getCell(columnNum);
					if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(columnNum, columnWidth * 256);
		}
	}

	public static String getCell(XSSFRow row, int column, int cellType) {
		XSSFCell cell = row.getCell(column);
		switch (cellType) {
		case XSSFCell.CELL_TYPE_BLANK: {
			return "";
		}
		case XSSFCell.CELL_TYPE_STRING: {
			return cell.getStringCellValue();
		}
		case XSSFCell.CELL_TYPE_NUMERIC: {
			return cell.getStringCellValue();
		}
		default:
			return "";
		}
	}

	/**
	 * 獲得Cell內容
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			// 以下是判斷資料的型別
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 數字
				value = cell.getNumericCellValue() + "";
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					if (date != null) {
						value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					} else {
						value = "";
					}
				} else {
					value = new DecimalFormat("0.##").format(cell.getNumericCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字串
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				value = cell.getBooleanCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				value = cell.getCellFormula() + "";
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				value = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				value = "非法字元";
				break;
			default:
				value = "未知型別";
				break;
			}
		}
		return value.trim();
	}
	
	/**
	 * 檢核是否為空白行
	 * @param row
	 * @param count
	 * @return boolean
	 */
	public static boolean checkBlankRow(Row row, int count) {
		boolean isNull = true;
		Cell cell;
		for(int i=0; i<count; i++) {
			cell = row.getCell(i);
			if(cell !=null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
				isNull = false;
				break;
			}			
		}		
		return isNull;
	}

}
