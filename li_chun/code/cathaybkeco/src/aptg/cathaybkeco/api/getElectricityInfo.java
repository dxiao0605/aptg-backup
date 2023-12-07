package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;

/**
 * Servlet implementation class getElectricityInfo 電力數值
 */
@WebServlet("/getElectricityInfo")
public class getElectricityInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getElectricityInfo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getElectricityInfo() {
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
		logger.debug("getElectricityInfo start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String cityGroup = ObjectUtils.toString(request.getParameter("cityGroup"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String usageCode = ObjectUtils.toString(request.getParameter("usageCode"));
			String filter = ObjectUtils.toString(request.getParameter("filter"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);
			logger.debug("CityGroup:" + cityGroup+",City:" + city + ",PostCodeNo:" + postCodeNo);
			logger.debug("BankCode: " + bankCode+ ",UsageCode:" + usageCode);
			logger.debug("Type:" + type + ",Filter:" + filter);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					List<String> filterList = Arrays.asList(filter.split(","));
					String mode = ToolUtil.getMode(filterList);
					BankInfVO bankInfVO = new BankInfVO();
					bankInfVO.setCityGroup(cityGroup);
					bankInfVO.setCityArr(ToolUtil.strToSqlStr(city));
					bankInfVO.setPostCodeNoArr(ToolUtil.strToSqlStr(postCodeNo));
					bankInfVO.setBankCodeArr(ToolUtil.strToSqlStr(bankCode));
					bankInfVO.setUsageCodeArr(usageCode);
					bankInfVO.setFilter(filter);
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> list = bankInfDAO.getElectricityInfo(bankInfVO);

					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
					if (list != null && list.size() > 0) {
						List<DynaBean> areaAndPeopleList = null;
						if("Bank".equals(mode)) {
							areaAndPeopleList = bankInfDAO.getBankAreaAndPeople(bankInfVO);
						}
						
						if ("excel".equals(type)) {
							rspJson.put("msg", composeExcel(list, areaAndPeopleList, filter, mode));						
						} else {
							rspJson.put("msg", convertToJson(list, areaAndPeopleList, filter, mode));
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
		logger.debug("getElectricityInfo end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, List<DynaBean> areaAndPeopleList, 
			String filter, String mode) throws Exception {
		JSONObject data = new JSONObject();
		try {
			Map<String, BigDecimal> areaMap = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> peopleMap = new HashMap<String, BigDecimal>();
			if("Bank".equals(mode) && areaAndPeopleList!=null) {
				for (DynaBean bean : areaAndPeopleList) {
					String key = new String();		
					if(filter.contains("City")) 
						key += bean.get("city")+"##";					
					if(filter.contains("Dist")) 
						key += bean.get("dist")+"##";				
					if(filter.contains("BankCode")) 
						key += bean.get("bankcode");	
					
					areaMap.put(key, ToolUtil.getBigDecimal(bean.get("area")));	
					peopleMap.put(key, ToolUtil.getBigDecimal(bean.get("people")));	
				}
			}

			JSONArray list = new JSONArray();
			for (DynaBean bean : rows) {
				String key = new String();		
				if(filter.contains("City")) 
					key += bean.get("city")+"##";					
				if(filter.contains("Dist")) 
					key += bean.get("dist")+"##";				
				if(filter.contains("BankCode")) 
					key += bean.get("bankcode");
				
				
				JSONObject bank = new JSONObject();

				if (filter.contains("City")) {
					bank.put("City", bean.get("city"));// 縣市
				}
				if (filter.contains("Dist")) {
					bank.put("Dist", bean.get("dist"));// 行政區
				}
				if (filter.contains("BankCode")) {
					bank.put("BankCode", bean.get("bankcode"));// 分行代碼
					bank.put("BankName", bean.get("bankname"));// 分行名稱
				}
				if (filter.contains("PowerAccount")) {
					bank.put("PowerAccount", bean.get("poweraccount"));// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					bank.put("RatePlanDesc", ObjectUtils.toString(bean.get("rateplandesc")));// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					bank.put("AccountDesc", ObjectUtils.toString(bean.get("accountdesc")));// 說明
				}
				if (filter.contains("CC")) {
					bank.put("CC", bean.get("cc") != null ? bean.get("cc") : "");// 契約容量
				}
				if (filter.contains("UsageDesc")) {
					bank.put("UsageDesc", ObjectUtils.toString(bean.get("usagedesc")));// 耗能分類
				}
				if (filter.contains("MeterName")) {
					bank.put("MeterName", ObjectUtils.toString(bean.get("metername")));// 電表名稱
				}				
				
				if("Bank".equals(mode)) {
					if (filter.contains("Area")) {
						bank.put("Area", areaMap.containsKey(key) ? areaMap.get(key) : "");// 面積
					}
					if (filter.contains("People")) {
						bank.put("People", peopleMap.containsKey(key) ? peopleMap.get(key) : "");// 員工數
					}
				}else {
					if (filter.contains("Area")) {
						bank.put("Area", bean.get("area") != null ? bean.get("area") : "");// 面積
					}
					if (filter.contains("People")) {
						bank.put("People", bean.get("people") != null ? bean.get("people") : "");// 員工數
					}
				}
				
				
				if (filter.contains("Vol")) {
					bank.put("Vol", getBigDecimal(bean.get("vol"), 0));// 電壓
				}
				if (filter.contains("Cur")) {
					bank.put("Cur", getBigDecimal(bean.get("cur"), 0));// 電流
				}
				if (filter.contains("W")) {
					bank.put("W", getBigDecimal(bean.get("w"), 1));// 功率
				}
				if (filter.contains("Var")) {
					bank.put("Var", bean.get("var") != null ? bean.get("var") : "");// 虛功
				}
				if (filter.contains("VA")) {
					bank.put("VA", bean.get("va") != null ? bean.get("va") : "");// 視在
				}
				if (filter.contains("PF")) {
					bank.put("PF", getBigDecimal(bean.get("pf"), 2).multiply(new BigDecimal(100)));// 功因
				}
				if (filter.contains("Hz")) {
					bank.put("Hz", bean.get("hz") != null ? bean.get("hz") : "");// 頻率
				}
				if (filter.contains("DF")) {
					bank.put("DF", bean.get("df") != null ? bean.get("df") : "");// 需量預測
				}
				
				if (filter.contains("MDemand")) {
					BigDecimal mdemand = ToolUtil.getBigDecimal(bean.get("mdemand"), 0, BigDecimal.ROUND_DOWN);
					BigDecimal ddemand = ToolUtil.getBigDecimal(bean.get("ddemand"), 0, BigDecimal.ROUND_DOWN);
					if(mdemand.compareTo(ddemand)>0) {
						bank.put("MDemand", mdemand);// 當月最大需量
					}else {
						bank.put("MDemand", ddemand);// 當月最大需量	
					}					
				}
				if (filter.contains("CEC")) {
					bank.put("CECPK", ToolUtil.getBigDecimal(bean.get("tpmcecpk")));// 台電尖峰用電量
					bank.put("CECSP", ToolUtil.getBigDecimal(bean.get("tpmcecsp")));// 台電半尖峰用電量
					bank.put("CECSatSP", ToolUtil.getBigDecimal(bean.get("tpmcecsatsp")));// 台電周六半尖峰用電量
					bank.put("CECOP", ToolUtil.getBigDecimal(bean.get("tpmcecop")));// 台電離峰用電量
					bank.put("CECSum", bean.get("cecsum"));// 台電用電量總計
				}
				if (filter.contains("ECO5")) {									
					bank.put("ECO5PK", ToolUtil.getBigDecimal(bean.get("mcecpk")));// ECO5尖峰用電量				
					bank.put("ECO5SP", ToolUtil.getBigDecimal(bean.get("mcecsp")));// ECO5半尖峰用電量				
					bank.put("ECO5SatSP", ToolUtil.getBigDecimal(bean.get("mcecsatsp")));// ECO5周六半尖峰用電量				
					bank.put("ECO5OP", ToolUtil.getBigDecimal(bean.get("mcecop")));// ECO5離峰用電量
					bank.put("ECO5Total", bean.get("cecsum"));// ECO5總用電量
				}
				list.put(bank);
			}
			data.put("Bank", list);
			
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
	private JSONObject composeExcel(List<DynaBean> rows, List<DynaBean> areaAndPeopleList, 
			String filter, String mode) throws Exception {
		JSONObject data = new JSONObject();
		try {
			Map<String, BigDecimal> areaMap = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> peopleMap = new HashMap<String, BigDecimal>();
			if("Bank".equals(mode) && areaAndPeopleList!=null) {
				for (DynaBean bean : areaAndPeopleList) {
					String key = new String();		
					if(filter.contains("City")) 
						key += bean.get("city")+"##";					
					if(filter.contains("Dist")) 
						key += bean.get("dist")+"##";				
					if(filter.contains("BankCode")) 
						key += bean.get("bankcode");	
					
					areaMap.put(key, ToolUtil.getBigDecimal(bean.get("area")));	
					peopleMap.put(key, ToolUtil.getBigDecimal(bean.get("people")));	
				}
			}
			
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			
			XSSFSheet sheet = workbook.createSheet("ElectricityInfo");
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyle(workbook, true, 14);//標題樣式
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 12);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12);
			int cityCol = 0;
			int distCol = 0;
			int bankCol = 0;
			int column = 0;
			XSSFRow row0 = sheet.createRow(0);
			XSSFRow row1 = sheet.createRow(1);
			if (filter.contains("City")) {
				cityCol = column;
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "縣市");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Dist")) {
				distCol = column;
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "行政區");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("BankCode")) {
				bankCol = column;
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("PowerAccount")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電號");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("RatePlanDesc")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "用電類型");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("AccountDesc")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "說明");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("CC")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "契約容量");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("UsageDesc")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "耗能分類");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("MeterName")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電表名稱");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}		
			if (filter.contains("Area")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "面積");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("People")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "員工數");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Vol")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電壓");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Cur")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電流");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			
			if (filter.contains("W")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "功率");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Var")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "虛功");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("VA")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "視在");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("PF")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "功因");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Hz")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "頻率");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("DF")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "需量預測");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			
			if (filter.contains("MDemand")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "當月最大需量");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("CEC")) {
				int first = column;
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "本月用電量(KWh)");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰");
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰");
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "週六半");
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰");
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "總計");
				sheet.addMergedRegion(new CellRangeAddress(0, 0, first, column-1));
			}
			if (filter.contains("Status")) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "狀態");
				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			
			String city = new String();
			Map<String, Integer> cityStartMap = new HashMap<String, Integer>();
			Map<String, Integer> cityEndMap = new HashMap<String, Integer>();
			String dist = new String();
			Map<String, Integer> distStartMap = new HashMap<String, Integer>();
			Map<String, Integer> distEndMap = new HashMap<String, Integer>();
			String bankCode = new String();
			Map<String, Integer> bankStartMap = new HashMap<String, Integer>();
			Map<String, Integer> bankEndMap = new HashMap<String, Integer>();
			int y = 1;
			for(DynaBean bean : rows) {
				String key = new String();		
				if(filter.contains("City")) 
					key += bean.get("city")+"##";					
				if(filter.contains("Dist")) 
					key += bean.get("dist")+"##";				
				if(filter.contains("BankCode")) 
					key += bean.get("bankcode");	
				
				XSSFRow row = sheet.createRow(++y);
				int x = 0;
				if (filter.contains("City")) {
					if(!city.equals(ObjectUtils.toString(bean.get("city")))) {
						city = ObjectUtils.toString(bean.get("city"));	
						cityStartMap.put(city, y);
					}
					cityEndMap.put(city, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("city"));// 縣市
				}
				if (filter.contains("Dist")) {
					if(!dist.equals(ObjectUtils.toString(bean.get("dist")))) {
						dist = ObjectUtils.toString(bean.get("dist"));	
						distStartMap.put(dist, y);
					}
					distEndMap.put(dist, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("dist"));// 行政區
				}
				if (filter.contains("BankCode")) {
					if(!bankCode.equals(ObjectUtils.toString(bean.get("bankcode")))) {
						bankCode = ObjectUtils.toString(bean.get("bankcode"));	
						bankStartMap.put(bankCode, y);
					}
					bankEndMap.put(bankCode, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bankCode+ObjectUtils.toString(bean.get("bankname")));// 分行代碼+分行名稱
				}
				if (filter.contains("PowerAccount")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("poweraccount"));// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("rateplandesc"));// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("accountdesc"));// 說明
				}
				if (filter.contains("CC")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("cc"));// 契約容量
				}
				if (filter.contains("UsageDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("usagedesc"));// 耗能分類
				}
				if (filter.contains("MeterName")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("metername"));// 電表名稱
				}
				if("Bank".equals(mode)) {					
					if (filter.contains("Area")) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, areaMap.get(key));// 面積
					}
					if (filter.contains("People")) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, peopleMap.get(key));// 員工數
					}
				}else {
					if (filter.contains("Area")) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("area"));// 面積
					}
					if (filter.contains("People")) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("people"));// 員工數
					}
				}
				
				
				
				if (filter.contains("Vol")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("vol"), 0));// 電壓
				}
				if (filter.contains("Cur")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("cur"), 0));//電流
				}
				
				if (filter.contains("W")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("w"), 1));// 功率
				}
				if (filter.contains("Var")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("va"));// 虛功/視在
				}
				if (filter.contains("VA")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("var"));// 虛功/視在
				}
				
				if (filter.contains("PF")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("pf"), 2).multiply(new BigDecimal(100)));// 功因
				}
				if (filter.contains("Hz")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("hz"));// 頻率
				}
				if (filter.contains("DF")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("df"));// 需量預測
				}
				
				if (filter.contains("MDemand")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mdemand"));// 當月最大需量
				}
				if (filter.contains("CEC")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecpk"));// 尖峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecsp"));// 半尖峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecsatsp"));// 周六半尖峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecop"));// 離峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("cecsum"));// 用電量總計
				}
				if (filter.contains("Status")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("status"));// 狀態
				}
			}
			
			//合併縣市儲存格
			for(String key : cityStartMap.keySet()) {
				if(cityStartMap.get(key)!=cityEndMap.get(key)) {
					sheet.addMergedRegion(new CellRangeAddress(cityStartMap.get(key), cityEndMap.get(key), cityCol, cityCol));
				}
			}
			//合併行政區儲存格
			for(String key : distStartMap.keySet()) {
				if(distStartMap.get(key)!=distEndMap.get(key)) {
					sheet.addMergedRegion(new CellRangeAddress(distStartMap.get(key), distEndMap.get(key), distCol, distCol));
				}
			}
			//合併分行儲存格
			for(String key : bankStartMap.keySet()) {
				if(bankStartMap.get(key)!=bankEndMap.get(key)) {
					sheet.addMergedRegion(new CellRangeAddress(bankStartMap.get(key), bankEndMap.get(key), bankCol, bankCol));
				}
			}
					
			//設定自動欄寬
			for (int i = 0; i <= column; i++) {
                sheet.autoSizeColumn(i, true);
                sheet.setColumnWidth(i,sheet.getColumnWidth(i)*18/10);
            }
			ExcelUtil.setSizeColumn(sheet, column);
				
			String fileName = "即時數據"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
			data.put("FileName", fileName);
			data.put("Base64", ExcelUtil.exportBase64(workbook, fileName));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

//	/**
//	 * 產生Excel
//	 * @param rows
//	 * @param filter
//	 * @param response
//	 * @throws Exception
//	 */
//	private void composeExcel(List<DynaBean> rows, String filter, HttpServletResponse response) throws Exception {
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			
//			XSSFSheet sheet = workbook.createSheet("ElectricityInfo");
//			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyle(workbook, true, 14);//標題樣式
//			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 12);
//			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12);
//			int cityCol = 0;
//			int distCol = 0;
//			int bankCol = 0;
//			int column = 0;
//			XSSFRow row0 = sheet.createRow(0);
//			XSSFRow row1 = sheet.createRow(1);
//			if (filter.contains("City")) {
//				cityCol = column;
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "縣市");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("Dist")) {
//				distCol = column;
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "行政區");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("BankCode")) {
//				bankCol = column;
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("PowerAccount")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電號");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("RatePlanDesc")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "用電類型");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("AccountDesc")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "說明");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("CC")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "契約容量");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("UsageDesc")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "耗能分類");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("MeterName")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電表名稱");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}		
//			if (filter.contains("Area")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "面積");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("People")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "員工數");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("Vol")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電壓");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("Cur")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電流");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			
//			if (filter.contains("W")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "功率");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("Var")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "虛功");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("VA")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "視在");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("PF")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "功因");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("Hz")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "頻率");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("DF")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "需量預測");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			
//			if (filter.contains("MDemand")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "當月最大需量");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			if (filter.contains("CEC")) {
//				int first = column;
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "本月用電量(KWh)");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰");
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰");
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "週六半");
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰");
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_BLANK, "");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "總計");
//				sheet.addMergedRegion(new CellRangeAddress(0, 0, first, column-1));
//			}
//			if (filter.contains("Status")) {
//				sheet.addMergedRegion(new CellRangeAddress(0, 1, column, column));
//				ExcelUtil.createCell(row0, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "狀態");
//				ExcelUtil.createCell(row1, titleStyle, column++, XSSFCell.CELL_TYPE_BLANK, "");
//			}
//			
//			String city = new String();
//			Map<String, Integer> cityStartMap = new HashMap<String, Integer>();
//			Map<String, Integer> cityEndMap = new HashMap<String, Integer>();
//			String dist = new String();
//			Map<String, Integer> distStartMap = new HashMap<String, Integer>();
//			Map<String, Integer> distEndMap = new HashMap<String, Integer>();
//			String bankCode = new String();
//			Map<String, Integer> bankStartMap = new HashMap<String, Integer>();
//			Map<String, Integer> bankEndMap = new HashMap<String, Integer>();
//			int y = 1;
//			for(DynaBean bean : rows) {
//				XSSFRow row = sheet.createRow(++y);
//				int x = 0;
//				if (filter.contains("City")) {
//					if(!city.equals(ObjectUtils.toString(bean.get("city")))) {
//						city = ObjectUtils.toString(bean.get("city"));	
//						cityStartMap.put(city, y);
//					}
//					cityEndMap.put(city, y);	
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("city"));// 縣市
//				}
//				if (filter.contains("Dist")) {
//					if(!dist.equals(ObjectUtils.toString(bean.get("dist")))) {
//						dist = ObjectUtils.toString(bean.get("dist"));	
//						distStartMap.put(dist, y);
//					}
//					distEndMap.put(dist, y);	
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("dist"));// 行政區
//				}
//				if (filter.contains("BankCode")) {
//					if(!bankCode.equals(ObjectUtils.toString(bean.get("bankcode")))) {
//						bankCode = ObjectUtils.toString(bean.get("bankcode"));	
//						bankStartMap.put(bankCode, y);
//					}
//					bankEndMap.put(bankCode, y);	
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bankCode+ObjectUtils.toString(bean.get("bankname")));// 分行代碼+分行名稱
//				}
//				if (filter.contains("PowerAccount")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("poweraccount"));// 電號
//				}
//				if (filter.contains("RatePlanDesc")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("rateplandesc"));// 用電類型
//				}
//				if (filter.contains("AccountDesc")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("accountdesc"));// 說明
//				}
//				if (filter.contains("CC")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("cc"));// 契約容量
//				}
//				if (filter.contains("UsageDesc")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("usagedesc"));// 耗能分類
//				}
//				if (filter.contains("MeterName")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("metername"));// 電表名稱
//				}		
//				if (filter.contains("Area")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("area"));// 面積
//				}
//				if (filter.contains("People")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("people"));// 員工數
//				}
//				if (filter.contains("Vol")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("vol"), 0));// 電壓
//				}
//				if (filter.contains("Cur")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("cur"), 0));//電流
//				}
//				
//				if (filter.contains("W")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("w"), 1));// 功率
//				}
//				if (filter.contains("Var")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("va"));// 虛功/視在
//				}
//				if (filter.contains("VA")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("var"));// 虛功/視在
//				}
//				
//				if (filter.contains("PF")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, getBigDecimal(bean.get("pf"), 2).multiply(new BigDecimal(100)));// 功因
//				}
//				if (filter.contains("Hz")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("hz"));// 頻率
//				}
//				if (filter.contains("DF")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("df"));// 需量預測
//				}
//				
//				if (filter.contains("MDemand")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mdemand"));// 當月最大需量
//				}
//				if (filter.contains("CEC")) {
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecpk"));// 尖峰用電量
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecsp"));// 半尖峰用電量
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecsatsp"));// 周六半尖峰用電量
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("mcecop"));// 離峰用電量
//					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, bean.get("cecsum"));// 用電量總計
//				}
//				if (filter.contains("Status")) {
//					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bean.get("status"));// 狀態
//				}
//			}
//			
//			//合併縣市儲存格
//			for(String key : cityStartMap.keySet()) {
//				if(cityStartMap.get(key)!=cityEndMap.get(key)) {
//					sheet.addMergedRegion(new CellRangeAddress(cityStartMap.get(key), cityEndMap.get(key), cityCol, cityCol));
//				}
//			}
//			//合併行政區儲存格
//			for(String key : distStartMap.keySet()) {
//				if(distStartMap.get(key)!=distEndMap.get(key)) {
//					sheet.addMergedRegion(new CellRangeAddress(distStartMap.get(key), distEndMap.get(key), distCol, distCol));
//				}
//			}
//			//合併分行儲存格
//			for(String key : bankStartMap.keySet()) {
//				if(bankStartMap.get(key)!=bankEndMap.get(key)) {
//					sheet.addMergedRegion(new CellRangeAddress(bankStartMap.get(key), bankEndMap.get(key), bankCol, bankCol));
//				}
//			}
//					
//			//設定自動欄寬
//			for (int i = 0; i <= column; i++) {
//                sheet.autoSizeColumn(i, true);
//                sheet.setColumnWidth(i,sheet.getColumnWidth(i)*18/10);
//            }
//			ExcelUtil.setSizeColumn(sheet, column);
//				
//			ExcelUtil.exportXlsx(workbook, "即時數據"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx", response);
//		} catch (Exception e) {
//			throw new Exception(e.toString());
//		}
//	}
	
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

	private BigDecimal getBigDecimal(Object value, int newScale) {
		return value != null ? new BigDecimal(value.toString()).setScale(newScale,  BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
	}

}
