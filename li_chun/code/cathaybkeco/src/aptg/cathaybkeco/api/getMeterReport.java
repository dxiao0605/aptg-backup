package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.KPIDAO;
import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getMeterReport 電表報表
 */
@WebServlet("/getMeterReport")
public class getMeterReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterReport.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("getMeterReport start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId"));
			String date = ObjectUtils.toString(request.getParameter("date"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);			
			logger.debug("DeviceID: " + deviceId + ", Date: " + date + ", Type: "+type);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				}else if (!ToolUtil.dateCheck(date, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setDate(date);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> meterHeaderList = meterSetupDAO.getMeterReportHeader(meterSetupVO);
					List<DynaBean> meterDetailList = meterSetupDAO.getMeterReportDetail(meterSetupVO);					
					if (meterHeaderList != null && !meterHeaderList.isEmpty()) {
						rspJson.put("code", "00");		
						JSONObject msg = convertToJson(meterHeaderList, meterDetailList, date);
						if ("excel".equals(type)) {
							rspJson.put("msg", composeExcel(msg));
						} else {
							rspJson.put("msg", msg);
						}							
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
					}
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
		logger.debug("getMeterReport end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> meterHeaderList, List<DynaBean> meterDetailList, String date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd(E)", Locale.CHINESE);
		JSONObject data = new JSONObject();
		try {
			BigDecimal unitPriceKPI = BigDecimal.ZERO,euiKPI = BigDecimal.ZERO,epuiKPI = BigDecimal.ZERO;
			//取得KPI
			KPIDAO kpiDAO = new KPIDAO();
			List<DynaBean> kpiList = kpiDAO.getKPI();
			if(kpiList!=null && !kpiList.isEmpty()) {
				DynaBean bean = kpiList.get(0);
				unitPriceKPI = ToolUtil.getBigDecimal(bean.get("unitpricekpi"));
				euiKPI = ToolUtil.getBigDecimal(bean.get("euikpi"));
				epuiKPI = ToolUtil.getBigDecimal(bean.get("epuikpi"));
			}
			
			
			BigDecimal maxW = BigDecimal.ZERO, maxVol = BigDecimal.ZERO, maxVolP = BigDecimal.ZERO, maxCur = BigDecimal.ZERO;
			JSONArray recordArr = new JSONArray();
			if (meterDetailList != null && !meterDetailList.isEmpty()) {
				for(int i=0; i<meterDetailList.size(); i++) {
					DynaBean bean = meterDetailList.get(i);
					JSONObject record = new JSONObject();
					if(maxW.compareTo(ToolUtil.getBigDecimal(bean.get("wmax")))<0) {
						maxW = ToolUtil.getBigDecimal(bean.get("wmax"));
					}
					if(maxVol.compareTo(ToolUtil.getBigDecimal(bean.get("vmax")))<0) {
						maxVol = ToolUtil.getBigDecimal(bean.get("vmax"));
					}
					if(maxVolP.compareTo(ToolUtil.getBigDecimal(bean.get("vmaxp")))<0) {
						maxVolP = ToolUtil.getBigDecimal(bean.get("vmaxp"));
					}
					if(maxCur.compareTo(ToolUtil.getBigDecimal(bean.get("imax")))<0) {
						maxCur = ToolUtil.getBigDecimal(bean.get("imax"));
					}
					record.put("Seq", i+1);
//					String recdate = ToolUtil.dateFormat(bean.get("recdate"), sdf);
					record.put("RecDate", ToolUtil.dateFormat(bean.get("recdate"), sdf));//日期
					record.put("Wmax", ToolUtil.getBigDecimal(bean.get("wmax")));//功率最大值
					record.put("TPCECPK", ToolUtil.getBigDecimal(bean.get("tpdcecpk")));//台電尖峰用電量
					record.put("TPCECSP", ToolUtil.getBigDecimal(bean.get("tpdcecsp")));//台電半尖峰用電量
					record.put("TPCECSatSP", ToolUtil.getBigDecimal(bean.get("tpdcecsatsp")));//台電週六半尖峰用電量
					record.put("TPCECOP", ToolUtil.getBigDecimal(bean.get("tpdcecop")));//台電離峰用電量
					record.put("TPCECSum", ToolUtil.getBigDecimal(bean.get("tpcecsum")));//台電用電量總計
					record.put("CECPK", ToolUtil.getBigDecimal(bean.get("dcecpk")));//尖峰用電量
					record.put("CECSP", ToolUtil.getBigDecimal(bean.get("dcecsp")));//半尖峰用電量
					record.put("CECSatSP", ToolUtil.getBigDecimal(bean.get("dcecsatsp")));//週六半尖峰用電量
					record.put("CECOP", ToolUtil.getBigDecimal(bean.get("dcecop")));//離峰用電量
					record.put("CECSum", ToolUtil.getBigDecimal(bean.get("cecsum")));//用電量總計
					record.put("KWh", ToolUtil.getBigDecimal(bean.get("kwh")));//電表值
					recordArr.put(record);
				}
			}
		
			JSONObject meter = new JSONObject();
			meter.put("Date", ToolUtil.getNewDateFormat(date, "yyyyMM", "yyyy/MM"));
			meter.put("Record", recordArr);
			DynaBean bean = meterHeaderList.get(0);
			
			//分行資訊
			meter.put("BankCode", ObjectUtils.toString(bean.get("bankcode")));// 分行代碼
			meter.put("BankName", ObjectUtils.toString(bean.get("bankname")));// 分行名稱
			meter.put("MeterName", ObjectUtils.toString(bean.get("metername")));// 電表名稱
			meter.put("RatePlanDesc", ObjectUtils.toString(bean.get("rateplandesc")));// 用電類型
			meter.put("CC", bean.get("cc"));// 契約容量			
			meter.put("PowerAccount", ObjectUtils.toString(bean.get("poweraccount")));// 電號
			//用電量
			meter.put("TPMCECPK", ToolUtil.getBigDecimal(bean.get("tpmcecpk")));//台電尖峰用電量
			meter.put("TPMCECSP", ToolUtil.getBigDecimal(bean.get("tpmcecsp")));//台電半尖峰用電量
			meter.put("TPMCECSatSP", ToolUtil.getBigDecimal(bean.get("tpmcecsatsp")));//台電週六半尖峰用電量
			meter.put("TPMCECOP", ToolUtil.getBigDecimal(bean.get("tpmcecop")));//台電離峰用電量
			meter.put("TPMCECSum", ToolUtil.getBigDecimal(bean.get("tpcecsum")));//台電用電量總計			
			meter.put("MCECPK", ToolUtil.getBigDecimal(bean.get("mcecpk")));//尖峰用電量
			meter.put("MCECSP", ToolUtil.getBigDecimal(bean.get("mcecsp")));//半尖峰用電量
			meter.put("MCECSatSP", ToolUtil.getBigDecimal(bean.get("mcecsatsp")));//週六半尖峰用電量
			meter.put("MCECOP", ToolUtil.getBigDecimal(bean.get("mcecop")));//離峰用電量
			meter.put("MCECSum", ToolUtil.getBigDecimal(bean.get("cecsum")));//用電量總計		
			///用電分析
			BigDecimal area = ToolUtil.getBigDecimal(bean.get("area"));
			BigDecimal people = ToolUtil.getBigDecimal(bean.get("people"));
			BigDecimal price = ToolUtil.divide(bean.get("totalcharge"), bean.get("mcec"), 2);			
			meter.put("Charge", ToolUtil.multiply(price, bean.get("cecsum"), 0));//電費			
			meter.put("Price", price);//單價
			meter.put("PriceDiff", price.subtract(unitPriceKPI));//單價
			BigDecimal eui = ToolUtil.divide(bean.get("cecsum"), area, 2);
			meter.put("EUI", eui);			
			meter.put("EUIDiff", eui.subtract(euiKPI));
			BigDecimal epui = ToolUtil.divide(bean.get("cecsum"), people, 2);
			meter.put("EPUI", epui);
			meter.put("EPUIDiff", epui.subtract(epuiKPI));			
			
			//當月最大值
			meter.put("MaxDemand", ToolUtil.getBigDecimal(bean.get("maxdemand")));//需量
			meter.put("W", maxW);//功率
			meter.put("Vol", maxVol);//相電壓
			meter.put("VolP", maxVolP);//線電壓
			meter.put("Cur", maxCur);//電流
			data.put("Meter", meter);		
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 產生Excel
	 * @param rows
	 * @param filter
	 * @param response
	 * @throws Exception
	 */
	private JSONObject composeExcel(JSONObject msg) throws Exception {
		JSONObject data = new JSONObject();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
							
			this.createSheet(workbook, msg, "eco5");
			this.createSheet(workbook, msg, "tp");
						
			String fileName =  "月報表"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
			data.put("FileName", fileName);
			data.put("Base64", ExcelUtil.exportBase64(workbook, fileName));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	private void createSheet(XSSFWorkbook workbook, JSONObject msg, String mode) throws Exception {
		XSSFSheet sheet = null;
		try {
			if("tp".equals(mode)) {
				sheet = workbook.createSheet("台電");	
			}else {
				sheet = workbook.createSheet("ECO-5");
			}
			
			XSSFCellStyle titleStyleNB = ExcelUtil.getTitleStyle(workbook, false, 26);//標題樣式
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyleW(workbook, true, 14);//標題樣式		
			XSSFCellStyle titleStyleW = ExcelUtil.getTitleStyleW(workbook, true, 14);//標題樣式
			titleStyleW.setWrapText(true);
			XSSFCellStyle styleLNB = ExcelUtil.getTextStyle(workbook, "L", false, 14);
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 14);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0.0#", true, 14);
			XSSFCellStyle styleRR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0.0#", true, 14, IndexedColors.RED.getIndex());
			XSSFCellStyle styleRG = ExcelUtil.getNumberStyle(workbook, "R", "#,##0.0#", true, 14, IndexedColors.GREEN.getIndex());
			
			JSONObject meter = msg.getJSONObject("Meter");	
			XSSFRow row0 = sheet.createRow(0);	
			ExcelUtil.createCell(row0, titleStyleNB, 0, XSSFCell.CELL_TYPE_STRING, "月報表");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			
			XSSFRow row1 = sheet.createRow(1);
			ExcelUtil.createCell(row1, styleLNB, 0, XSSFCell.CELL_TYPE_STRING, "月份:");
			ExcelUtil.createCell(row1, styleLNB, 1, XSSFCell.CELL_TYPE_STRING, meter.getString("Date"));
			
			XSSFRow row2 = sheet.createRow(2);
			ExcelUtil.createCell(row2, titleStyle, 0, XSSFCell.CELL_TYPE_STRING, "分行資訊");
			ExcelUtil.createBlankCell(row2, styleL, 1);
			ExcelUtil.createBlankCell(row2, styleL, 2);
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row2, titleStyle, 5, XSSFCell.CELL_TYPE_STRING, "台電時段用電量");	
			}else {
				ExcelUtil.createCell(row2, titleStyle, 5, XSSFCell.CELL_TYPE_STRING, "ECO-5時段用電量");
			}			
			ExcelUtil.createBlankCell(row2, styleL, 6);
			ExcelUtil.createBlankCell(row2, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
			
			XSSFRow row3 = sheet.createRow(3);
			ExcelUtil.createCell(row3, styleL, 0, XSSFCell.CELL_TYPE_STRING, "分行");
			ExcelUtil.createCell(row3, styleR, 1, XSSFCell.CELL_TYPE_STRING, meter.getString("BankCode")+meter.getString("BankName"));
			ExcelUtil.createBlankCell(row3, styleL, 2);
			ExcelUtil.createCell(row3, styleL, 5, XSSFCell.CELL_TYPE_STRING, "尖峰");
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row3, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("TPMCECPK")+" KWH");	
			}else {
				ExcelUtil.createCell(row3, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MCECPK")+" KWH");
			}		
			ExcelUtil.createBlankCell(row3, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));
			
			XSSFRow row4 = sheet.createRow(4);
			ExcelUtil.createCell(row4, styleL, 0, XSSFCell.CELL_TYPE_STRING, "電表名稱");
			ExcelUtil.createCell(row4, styleR, 1, XSSFCell.CELL_TYPE_STRING, meter.getString("MeterName"));
			ExcelUtil.createBlankCell(row4, styleL, 2);
			ExcelUtil.createCell(row4, styleL, 5, XSSFCell.CELL_TYPE_STRING, "半尖峰");			
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row4, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("TPMCECSP")+" KWH");	
			}else {
				ExcelUtil.createCell(row4, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MCECSP")+" KWH");
			}
			ExcelUtil.createBlankCell(row4, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));
			
			XSSFRow row5 = sheet.createRow(5);
			ExcelUtil.createCell(row5, styleL, 0, XSSFCell.CELL_TYPE_STRING, "電價計費模式");
			ExcelUtil.createCell(row5, styleR, 1, XSSFCell.CELL_TYPE_STRING, meter.getString("RatePlanDesc"));
			ExcelUtil.createBlankCell(row5, styleL, 2);
			ExcelUtil.createCell(row5, styleL, 5, XSSFCell.CELL_TYPE_STRING, "周六半尖峰");			
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row5, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("TPMCECSatSP")+" KWH");	
			}else {
				ExcelUtil.createCell(row5, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MCECSatSP")+" KWH");
			}
			ExcelUtil.createBlankCell(row5, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));
			
			XSSFRow row6 = sheet.createRow(6);
			ExcelUtil.createCell(row6, styleL, 0, XSSFCell.CELL_TYPE_STRING, "契約容量");
			ExcelUtil.createCell(row6, styleR, 1, XSSFCell.CELL_TYPE_NUMERIC, meter.getBigDecimal("CC"));
			ExcelUtil.createBlankCell(row6, styleL, 2);
			ExcelUtil.createCell(row6, styleL, 5, XSSFCell.CELL_TYPE_STRING, "離峰");
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row6, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("TPMCECOP")+" KWH");	
			}else {
				ExcelUtil.createCell(row6, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MCECOP")+" KWH");
			}			
			ExcelUtil.createBlankCell(row6, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 7));
			
			XSSFRow row7 = sheet.createRow(7);
			ExcelUtil.createCell(row7, styleL, 0, XSSFCell.CELL_TYPE_STRING, "電號");
			ExcelUtil.createCell(row7, styleR, 1, XSSFCell.CELL_TYPE_STRING, meter.getString("PowerAccount"));
			ExcelUtil.createBlankCell(row7, styleL, 2);
			ExcelUtil.createCell(row7, styleL, 5, XSSFCell.CELL_TYPE_STRING, "總計");
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row7, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("TPMCECSum")+" KWH");	
			}else {
				ExcelUtil.createCell(row7, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MCECSum")+" KWH");
			}
			
			ExcelUtil.createBlankCell(row7, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 6, 7));
			
			XSSFRow row8 = sheet.createRow(8);
			ExcelUtil.createCell(row8, titleStyle, 0, XSSFCell.CELL_TYPE_STRING, "用電分析");
			ExcelUtil.createBlankCell(row8, styleL, 1);
			ExcelUtil.createBlankCell(row8, styleL, 2);
			ExcelUtil.createCell(row8, titleStyle, 5, XSSFCell.CELL_TYPE_STRING, "當月最大值");
			ExcelUtil.createBlankCell(row8, styleL, 6);
			ExcelUtil.createBlankCell(row8, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(8, 8, 5, 7));
			
			XSSFRow row9 = sheet.createRow(9);
			ExcelUtil.createCell(row9, styleL, 0, XSSFCell.CELL_TYPE_STRING, "電費");
			ExcelUtil.createCell(row9, styleR, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("Charge")+"元");
			ExcelUtil.createBlankCell(row9, styleL, 2);
			ExcelUtil.createCell(row9, styleL, 5, XSSFCell.CELL_TYPE_STRING, "需量最大值");
			ExcelUtil.createCell(row9, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("MaxDemand")+" kw");
			ExcelUtil.createBlankCell(row9, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(9, 9, 6, 7));
			
			XSSFRow row10 = sheet.createRow(10);
			ExcelUtil.createCell(row10, styleL, 0, XSSFCell.CELL_TYPE_STRING, "每度電單價");
			BigDecimal priceDiff =meter.getBigDecimal("PriceDiff");
			if(priceDiff.compareTo(BigDecimal.ZERO)>=0) {
				ExcelUtil.createCell(row10, styleRR, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("Price")+"m元(↑"+priceDiff.abs()+")");
			}else {
				ExcelUtil.createCell(row10, styleRG, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("Price")+"m元(↓"+priceDiff.abs()+")");
			}	
			ExcelUtil.createBlankCell(row10, styleL, 2);
			ExcelUtil.createCell(row10, styleL, 5, XSSFCell.CELL_TYPE_STRING, "功率最大值");
			ExcelUtil.createCell(row10, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("W")+" kw");
			ExcelUtil.createBlankCell(row10, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(10, 10, 6, 7));
			
			XSSFRow row11 = sheet.createRow(11);
			ExcelUtil.createCell(row11, styleL, 0, XSSFCell.CELL_TYPE_STRING, "EUI");
			BigDecimal euiDiff =meter.getBigDecimal("EUIDiff");
			if(euiDiff.compareTo(BigDecimal.ZERO)>=0) {
				ExcelUtil.createCell(row11, styleRR, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("EUI")+" KWH/坪(↑"+euiDiff.abs()+")");
			}else {
				ExcelUtil.createCell(row11, styleRG, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("EUI")+" KWH/坪(↓"+euiDiff.abs()+")");
			}
			
			ExcelUtil.createBlankCell(row11, styleL, 2);
			ExcelUtil.createCell(row11, styleL, 5, XSSFCell.CELL_TYPE_STRING, "相電壓最大值");
			ExcelUtil.createCell(row11, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("Vol")+" V");
			ExcelUtil.createBlankCell(row11, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(11, 11, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(11, 11, 6, 7));
			
			XSSFRow row12 = sheet.createRow(12);
			ExcelUtil.createCell(row12, styleL, 0, XSSFCell.CELL_TYPE_STRING, "EPUI");
			BigDecimal epuiDiff =meter.getBigDecimal("EPUIDiff");
			if(epuiDiff.compareTo(BigDecimal.ZERO)>=0) {
				ExcelUtil.createCell(row12, styleRR, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("EPUI")+" KWH/人(↑"+epuiDiff.abs()+")");
			}else {
				ExcelUtil.createCell(row12, styleRG, 1, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("EPUI")+" KWH/人(↓"+epuiDiff.abs()+")");	
			}			
			ExcelUtil.createBlankCell(row12, styleL, 2);
			ExcelUtil.createCell(row12, styleL, 5, XSSFCell.CELL_TYPE_STRING, "線電壓最大值");
			ExcelUtil.createCell(row12, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("VolP")+" V");
			ExcelUtil.createBlankCell(row12, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(12, 12, 6, 7));
			
			XSSFRow row13 = sheet.createRow(13);
			ExcelUtil.createCell(row13, styleL, 5, XSSFCell.CELL_TYPE_STRING, "電流最大值");
			ExcelUtil.createCell(row13, styleR, 6, XSSFCell.CELL_TYPE_STRING, meter.getBigDecimal("Cur")+" A");
			ExcelUtil.createBlankCell(row13, styleL, 7);
			sheet.addMergedRegion(new CellRangeAddress(13, 13, 6, 7));
			
			XSSFRow row15 = sheet.createRow(15);
			ExcelUtil.createCell(row15, titleStyle, 0, XSSFCell.CELL_TYPE_STRING, "日期");
			ExcelUtil.createCell(row15, titleStyleW, 1, XSSFCell.CELL_TYPE_STRING, "功率\n最大值");			
			if("tp".equals(mode)) {
				ExcelUtil.createCell(row15, titleStyle, 2, XSSFCell.CELL_TYPE_STRING, "台電時段用電量(KWH)");	
			}else {
				ExcelUtil.createCell(row15, titleStyle, 2, XSSFCell.CELL_TYPE_STRING, "ECO-5時段用電量(KWH)");
			}
			ExcelUtil.createBlankCell(row15, titleStyle, 3);
			ExcelUtil.createBlankCell(row15, titleStyle, 4);
			ExcelUtil.createBlankCell(row15, titleStyle, 5);
			ExcelUtil.createBlankCell(row15, titleStyle, 6);
			ExcelUtil.createCell(row15, titleStyle, 7, XSSFCell.CELL_TYPE_STRING, "電表值");
			XSSFRow row16 = sheet.createRow(16);
			ExcelUtil.createBlankCell(row16, titleStyle, 0);
			ExcelUtil.createBlankCell(row16, titleStyle, 1);
			ExcelUtil.createCell(row16, titleStyle, 2, XSSFCell.CELL_TYPE_STRING, "尖峰");
			ExcelUtil.createCell(row16, titleStyle, 3, XSSFCell.CELL_TYPE_STRING, "半尖峰");
			ExcelUtil.createCell(row16, titleStyle, 4, XSSFCell.CELL_TYPE_STRING, "周六半尖峰");
			ExcelUtil.createCell(row16, titleStyle, 5, XSSFCell.CELL_TYPE_STRING, "離峰");
			ExcelUtil.createCell(row16, titleStyle, 6, XSSFCell.CELL_TYPE_STRING, "總計");
			ExcelUtil.createBlankCell(row16, titleStyle, 7);
			sheet.addMergedRegion(new CellRangeAddress(15, 16, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(15, 16, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(15, 16, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(15, 15, 2, 6));			
			
			JSONArray recordArr = meter.getJSONArray("Record");
			int y = 17;
			for(int i=0; i<recordArr.length(); i++) {
				JSONObject record =  recordArr.getJSONObject(i);
				XSSFRow row = sheet.createRow(y++);
				ExcelUtil.createCell(row, styleL, 0, XSSFCell.CELL_TYPE_STRING, record.opt("RecDate"));
				ExcelUtil.createCell(row, styleR, 1, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("Wmax"));
				if("tp".equals(mode)) {
					ExcelUtil.createCell(row, styleR, 2, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("TPCECPK"));
					ExcelUtil.createCell(row, styleR, 3, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("TPCECSP"));
					ExcelUtil.createCell(row, styleR, 4, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("TPCECSatSP"));
					ExcelUtil.createCell(row, styleR, 5, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("TPCECOP"));
					ExcelUtil.createCell(row, styleR, 6, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("TPCECSum"));
				}else {
					ExcelUtil.createCell(row, styleR, 2, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("CECPK"));
					ExcelUtil.createCell(row, styleR, 3, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("CECSP"));
					ExcelUtil.createCell(row, styleR, 4, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("CECSatSP"));
					ExcelUtil.createCell(row, styleR, 5, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("CECOP"));
					ExcelUtil.createCell(row, styleR, 6, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("CECSum"));
				}				
				ExcelUtil.createCell(row, styleR, 7, XSSFCell.CELL_TYPE_NUMERIC, record.getBigDecimal("KWh"));
			}
			
			//設定自動欄寬
			ExcelUtil.setSizeColumn(sheet, 8);
			for (int i = 0; i <= 8; i++) {
	            sheet.autoSizeColumn(i, true);
	            sheet.setColumnWidth(i,sheet.getColumnWidth(i)*18/10);
	        }
			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
