package aptg.cathaybkeco.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfUtil {
	
	/**
	 * 建立字體
	 * @param point
	 * @param style
	 * @param color
	 * @return Font
	 * @throws Exception
	 */
	public static Font getFont(int point, int style, BaseColor color) throws Exception {
		Font font;
		try {
			// Font.NORMAL:正常
			// Font.BOLD:粗體
			// Font.ITALIC:斜體
			BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont,
					AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
			font = new Font(baseFont, point, style, color);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return font;
	}
	
	/**
	 * 建立單元格
	 * @param table
	 * @param font
	 * @param value
	 */
	public static PdfPCell createCell(Font font, String alignment, String value) {
		PdfPCell cell = new PdfPCell(new Paragraph(value, font));
		
		if ("R".equals(alignment)) {
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		} else if ("L".equals(alignment)) {
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		} else {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
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
	public static void exportPDF(ByteArrayOutputStream baos, String fileName, HttpServletResponse response)
			throws Exception {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

			baos.writeTo(response.getOutputStream());
		} catch (IOException ex) {
			throw new Exception(ex.toString());
		}
	}
}
