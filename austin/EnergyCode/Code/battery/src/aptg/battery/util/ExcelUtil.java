package aptg.battery.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFDateAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs;
import org.openxmlformats.schemas.drawingml.x2006.chart.STDispBlanksAs;


public class ExcelUtil {
	
	private static ExcelUtil instances;

	public static ExcelUtil getInstance() {
		if (instances == null) {
			instances = new ExcelUtil();
		}
		return instances;
	}

	/**
	 * 單元格樣式(標題)
	 * 
	 * @param workbook
	 * @param point
	 * @return XSSFCellStyle
	 */
	public CellStyle getTitleStyle(SXSSFWorkbook workbook, boolean border, int point) {
		// 設定字型
		Font font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		// 字型加粗
		font.setBold(true);
		// 設定樣式;
		CellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}
	
		// 在樣式用應用設定的字型;
		style.setFont(font);
		// 設定自動換行;
		style.setWrapText(false);
		// 設定水平對齊的樣式為居中對齊;
		style.setAlignment(HorizontalAlignment.CENTER);
		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		return style;
	}
	
	/**
	 * 單元格樣式(標題)
	 * 
	 * @param workbook
	 * @param point
	 * @return XSSFCellStyle
	 */
	public XSSFCellStyle getTitleStyle(XSSFWorkbook workbook, boolean border, int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		// 字型加粗
		font.setBold(true);
		// 設定樣式;
		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}
	
		// 在樣式用應用設定的字型;
		style.setFont(font);
		// 設定自動換行;
		style.setWrapText(false);
		// 設定水平對齊的樣式為居中對齊;
		style.setAlignment(HorizontalAlignment.CENTER);
		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);

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
	public CellStyle getTextStyle(SXSSFWorkbook workbook, String alignment, boolean border, int point) {
		// 設定字型
		Font font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);

		CellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 設定自動換行;
		style.setWrapText(false);
		// 在樣式用應用設定的字型;
		style.setFont(font);

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
	public XSSFCellStyle getTextStyle(XSSFWorkbook workbook, String alignment, boolean border, int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
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
	public CellStyle getTextStyle(SXSSFWorkbook workbook, String alignment, boolean border, int point, short color) {
		// 設定字型
		Font font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		font.setColor(color);

		CellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
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
	public XSSFCellStyle getTextStyle(XSSFWorkbook workbook, String alignment, boolean border, int point, short color) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		font.setColor(color);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
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
	public XSSFCellStyle getNumberStyle(XSSFWorkbook workbook, String alignment, String format, boolean border,
			int point) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 數字格式
		XSSFDataFormat dataFormat = workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat(format));

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
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
	public XSSFCellStyle getNumberStyle(XSSFWorkbook workbook, String alignment, String format, boolean border,
			int point, short color) {
		// 設定字型
		XSSFFont font = workbook.createFont();
		// 設定字型大小
		font.setFontHeightInPoints((short) point);
		font.setColor(color);

		XSSFCellStyle style = workbook.createCellStyle();
		if (border) {
			// 設定底邊框;
			style.setBorderBottom(BorderStyle.THIN);
			// 設定左邊框;
			style.setBorderLeft(BorderStyle.THIN);
			// 設定右邊框;
			style.setBorderRight(BorderStyle.THIN);
			// 設定頂邊框;
			style.setBorderTop(BorderStyle.THIN);
		}

		// 設定水平對齊的樣式為居中對齊;
		if ("R".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.RIGHT);
		} else if ("L".equals(alignment)) {
			style.setAlignment(HorizontalAlignment.LEFT);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
		}

		// 數字格式
		XSSFDataFormat dataFormat = workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat(format));

		// 設定垂直對齊的樣式為居中對齊;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
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
	public void createCell(XSSFRow row, CellStyle style, int column, CellType cellType, Object value) {
		
		XSSFCell cell = row.createCell(column);
		
		if (style != null)
			cell.setCellStyle(style);
		switch (cellType) {
		case BLANK: {
			break;
		}
		case STRING: {
			cell.setCellType(CellType.STRING);
			if (value != null && StringUtils.isNotBlank(value.toString()))
				cell.setCellValue(value.toString());
			break;
		}
		case NUMERIC: {
			cell.setCellType(CellType.NUMERIC);
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
	public void createBlankCell(XSSFRow row, XSSFCellStyle style, int column) {
		XSSFCell cell = row.createCell(column);
		if (style != null)
			cell.setCellStyle(style);
	}
	
	/**
	 * 輸出報表
	 * 
	 * @param workbook
	 * @param fileName
	 * @param response
	 * @throws Exception
	 */
	public static void exportXlsx(SXSSFWorkbook workbook, String fileName, HttpServletResponse response)
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
	
	public static void exportXlsxZip(XSSFWorkbook workbook, String excelName, HttpServletResponse response) throws Exception {
		OutputStream out = null;
		ZipOutputStream zip = null;
		try {
            String fieldName = excelName.substring(0, excelName.lastIndexOf("."));
            
            out = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fieldName+".zip", "UTF-8"));
            
            zip = new ZipOutputStream(out);

            //例項化一個壓縮實體
            ZipEntry entry = new ZipEntry(excelName);
            //將壓縮實體放入壓縮包
            zip.putNextEntry(entry);
            //將excel內容寫進壓縮實體
            workbook.write(zip);

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

	/**
	 * 自動欄寬(中文支持)
	 * 
	 * @param sheet
	 * @param size
	 */
	public void setSizeColumn(Sheet sheet, int size) {
		for (int columnNum = 0; columnNum < size; columnNum++) {
			int columnWidth = sheet.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				Row currentRow;
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}

				if (currentRow.getCell(columnNum) != null) {
					Cell currentCell = currentRow.getCell(columnNum);
					if (currentCell.getCellType() == CellType.STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(columnNum, columnWidth * 256 * 17/10);
		}
	}
	
	
	/**
	 * 自動欄寬(中文支持)
	 * 
	 * @param sheet
	 * @param size
	 */
	public void setSizeColumn(XSSFSheet sheet, int size) {
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
					if (currentCell.getCellType() == CellType.STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(columnNum, columnWidth * 256 * 17/10);
		}
	}

	public static String getCell(XSSFRow row, int column, CellType cellType) {
		XSSFCell cell = row.getCell(column);
		switch (cellType) {
		case BLANK: {
			return "";
		}
		case STRING: {
			return cell.getStringCellValue();
		}
		case NUMERIC: {
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
	public  String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			// 以下是判斷資料的型別
			switch (cell.getCellType()) {
			case NUMERIC: // 數字
				value = cell.getNumericCellValue() + "";
				if (DateUtil.isCellDateFormatted(cell)) {
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
			case STRING: // 字串
				value = cell.getStringCellValue();
				break;
			case BOOLEAN: // Boolean
				value = cell.getBooleanCellValue() + "";
				break;
			case FORMULA: // 公式
				value = cell.getCellFormula() + "";
				break;
			case BLANK: // 空值
				value = "";
				break;
			case ERROR: // 故障
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
	public boolean checkBlankRow(Row row, int count) {
		boolean isNull = true;
		Cell cell;
		for(int i=0; i<count; i++) {
			cell = row.getCell(i);
			if(cell !=null && cell.getCellType() != CellType.BLANK) {
				isNull = false;
				break;
			}			
		}		
		return isNull;
	}
	
	/**
	 * 產生折線圖
	 * @param wb
	 * @param sheetName
	 * @param title
	 * @param xUnit X軸單位
	 * @param yUnit Y軸單位
	 * @param xList
	 * @param yMap
	 */
	public void createLineChart(XSSFWorkbook wb, String sheetName, String title, String xUnit, String yUnit, List<String> xList, LinkedHashMap<String, List<BigDecimal>> yMap) {
		try {
			XSSFSheet sheet = wb.createSheet(sheetName);			

			//創建一個畫布
			XSSFDrawing drawing = sheet.createDrawingPatriarch();
			//前四個默認0，col1:畫布左上角位置；row1:從哪行開始創建畫布；col2:畫布寬度；row2:畫布高度
			XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 26, 25);
			//創建一個chart
			XSSFChart chart = drawing.createChart(anchor);			
			
//			//y軸標題(XDDFValueAxis無法設定位置，新增文字方塊
//			XSSFClientAnchor anchorY = drawing.createAnchor(0, 0, 0, 0, 1, 2, 2, 3);
//			XSSFTextBox textBox = drawing.createTextbox(anchorY);
//			textBox.setText(yUnit);

			//標題
			chart.setTitleText(title);
			//標題覆蓋
			chart.setTitleOverlay(false);
			chart.getCTChart().getTitle().getTx().getRich().getPArray(0).getRArray(0).getRPr().setSz(2800);//標題字體大小(28pt)
			
			//圖例位置
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.BOTTOM);
			
			//分類軸標(X軸),標題位置
			XDDFDateAxis bottomAxis = chart.createDateAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle(xUnit);
			
			//值(Y軸)軸,標題位置
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle(yUnit);		
			
			// set blank values as gaps
	        CTDispBlanksAs disp = CTDispBlanksAs.Factory.newInstance();
	        disp.setVal(STDispBlanksAs.GAP);
	        chart.getCTChart().setDispBlanksAs(disp);
			
			//LINE：折线图，
			XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
			data.setVaryColors(false);
 
			if(xList!=null && xList.size()>0) {
				//圖表加载數據，折線1
				XDDFCategoryDataSource x = XDDFDataSourcesFactory.fromArray(xList.stream().toArray(String[]::new));//X軸
				
				for(String key : yMap.keySet()) {
					List<BigDecimal> yList = yMap.get(key);
					XDDFNumericalDataSource<BigDecimal> y = XDDFDataSourcesFactory.fromArray(yList.stream().toArray(BigDecimal[]::new));//Y軸					
					XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(x, y);
					series.setTitle(key, null);//折線圖例標題
					series.setSmooth(false);//false:直線,true:曲線			
					series.setMarkerSize((short) 6);//設置標記大小			
					series.setMarkerStyle(MarkerStyle.NONE);//設置標記樣式					
				}
			}

			//繪製
			chart.plot(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
}
