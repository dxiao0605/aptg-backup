package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.dao.PowerRecordDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerRecordVO;

/**
 * Servlet implementation class getPowerRecord 電力數值
 */
@WebServlet("/getPowerRecord")
public class getPowerRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerRecord.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerRecord() {
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
		logger.debug("getPowerRecord start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					PowerRecordVO powerRecordVO = this.parseJson(req);
					if (powerRecordVO.isError()) {
						rspJson.put("code", powerRecordVO.getCode());
						rspJson.put("msg", powerRecordVO.getDescription());
					} else {
						MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
						List<DynaBean> meter = meterSetupDAO.getMeterInfo(powerRecordVO.getDeviceId());
						if (meter != null && meter.size() > 0) {							
							PowerRecordDAO powerRecordDAO = new PowerRecordDAO();
							List<DynaBean> recordlist = powerRecordDAO.getPowerRecord(powerRecordVO);
							if ("excel".equals(powerRecordVO.getType())) {
								rspJson.put("msg", composeExcel(meter, recordlist, powerRecordVO));
							} else {
								rspJson.put("msg", convertToJson(meter, recordlist, powerRecordVO));
							}
							rspJson.put("code", "00");
						} else {
							rspJson.put("code", "07");
							rspJson.put("msg", "查無資料");
						}
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
		logger.debug("getPowerRecord end");
	}

	/**
	 * 解析Json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private PowerRecordVO parseJson(String json) throws Exception {		
		PowerRecordVO powerRecordVO = new PowerRecordVO();
		try {
			JSONObject request = new JSONObject(json);
			
			if(ToolUtil.isNull(request, "DeviceId")) {				
				powerRecordVO.setError(true);
				powerRecordVO.setCode("15");
				powerRecordVO.setDescription("DeviceId不能為空");
				return powerRecordVO;
			}else {
				powerRecordVO.setDeviceId(request.optString("DeviceId"));	
			}

			String start = request.optString("Start")+ " " +request.optString("StartHH")+":00:00";
			String end = request.optString("End")+ " " +request.optString("EndHH")+":59:59";
			if(ToolUtil.isNull(request, "Start") || ToolUtil.isNull(request, "StartHH") ||
					ToolUtil.isNull(request, "End") || ToolUtil.isNull(request, "EndHH")) {				
				powerRecordVO.setError(true);
				powerRecordVO.setCode("15");
				powerRecordVO.setDescription("日期不能為空");
				return powerRecordVO;
			}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
				powerRecordVO.setError(true);
				powerRecordVO.setCode("18");
				powerRecordVO.setDescription("日期格式錯誤");// 日期格式錯誤
				return powerRecordVO;
			}			
			powerRecordVO.setStartDate(start);
			powerRecordVO.setEndDate(end);
			
			if(!ToolUtil.isNull(request, "Filter")) {	
				JSONArray arr = request.optJSONArray("Filter");
				List<String> strList = new ArrayList<String>();
				if (arr!=null && arr.length()>0) {
					String str;
					for (int i=0; i<arr.length(); i++) {
						str = arr.optString(i);
						if (StringUtils.isNotBlank(str))
							strList.add(str);
					}
				}				
				powerRecordVO.setFilter(strList);				
			}
			
			powerRecordVO.setDateformat(request.optString("Dateformat"));
			powerRecordVO.setType(request.optString("Type"));
			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return powerRecordVO;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(List<DynaBean> meter, List<DynaBean> rows, PowerRecordVO powerRecordVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
		JSONObject data = new JSONObject();
		try {	
			JSONArray recordArr = new JSONArray();
			DynaBean mBean = meter.get(0);
			data.put("Range", powerRecordVO.getStartDate()+"~"+powerRecordVO.getEndDate());
			data.put("DeviceId", mBean.get("deviceid"));
			data.put("InstallPosition", ObjectUtils.toString(mBean.get("installposition")));
			
			for(DynaBean bean : rows) {		
				JSONObject record = new JSONObject();
				record.put("RecTime", ToolUtil.dateFormat(bean.get("rectime"), sdf));
				record.put("I1", ToolUtil.getBigDecimal(bean.get("i1")));//電流R相
				record.put("I2", ToolUtil.getBigDecimal(bean.get("i2")));//電流S相
				record.put("I3", ToolUtil.getBigDecimal(bean.get("i3")));//電流T相
				record.put("Iavg", ToolUtil.getBigDecimal(bean.get("iavg")));//平均電流
				record.put("V1", ToolUtil.getBigDecimal(bean.get("v1")));//相電壓R相
				record.put("V2", ToolUtil.getBigDecimal(bean.get("v2")));//電壓S相
				record.put("V3", ToolUtil.getBigDecimal(bean.get("v3")));//電壓T相
				record.put("Vavg", ToolUtil.getBigDecimal(bean.get("vavg")));//平均相電壓
				record.put("V12", ToolUtil.getBigDecimal(bean.get("v12")));//線電壓R相
				record.put("V23", ToolUtil.getBigDecimal(bean.get("v23")));//線電壓S相
				record.put("V31", ToolUtil.getBigDecimal(bean.get("v31")));//線電壓T相
				record.put("VavgP", ToolUtil.getBigDecimal(bean.get("vavgp")));//平均線電壓
				record.put("W", ToolUtil.getBigDecimal(bean.get("w")));//實功
				record.put("RP", ToolUtil.getBigDecimal(bean.get("var")));//虛功
				record.put("VA", ToolUtil.getBigDecimal(bean.get("va")));//視在
				record.put("PF", ToolUtil.getBigDecimal(bean.get("pf")));//功因
				record.put("Hz", ToolUtil.getBigDecimal(bean.get("hz")));//頻率
				record.put("Mode1", ToolUtil.getBigDecimal(bean.get("mode1")));//混合式
				record.put("Mode2", ToolUtil.getBigDecimal(bean.get("mode2")));//浮動式
				record.put("Mode3", ToolUtil.getBigDecimal(bean.get("mode3")));//固定式
				record.put("Mode4", ToolUtil.getBigDecimal(bean.get("mode4")));//平均式
				record.put("DemandPK", ToolUtil.getBigDecimal(bean.get("demandpk")));//尖峰需量
				record.put("DemandSP", ToolUtil.getBigDecimal(bean.get("demandsp")));//半尖峰需量
				record.put("DemandSatSP", ToolUtil.getBigDecimal(bean.get("demandsatsp")));//週六半需量
				record.put("DemandOP", ToolUtil.getBigDecimal(bean.get("demandop")));//離峰需量
				record.put("ECO5PK", ToolUtil.getBigDecimal(bean.get("mcecpk")));//尖峰用電量
				record.put("ECO5SP", ToolUtil.getBigDecimal(bean.get("mcecsp")));//半尖峰用電量
				record.put("ECO5SatSP", ToolUtil.getBigDecimal(bean.get("mcecsatsp")));//週六半尖峰用電量
				record.put("ECO5OP", ToolUtil.getBigDecimal(bean.get("mcecop")));//離峰用電量
				record.put("KWH", ToolUtil.getBigDecimal(bean.get("kwh")));//電表值
				record.put("Kvarh", ToolUtil.getBigDecimal(bean.get("kvarh")));//無效電量
				record.put("THVavg", ToolUtil.getBigDecimal(bean.get("thvavg")));//電壓總諧波率
				record.put("THIavg", ToolUtil.getBigDecimal(bean.get("thiavg")));//電流總諧波率 
				recordArr.put(record);
			}
		
			data.put("Record", recordArr);			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 產生Excel
	 * @param meter
	 * @param rows
	 * @param powerRecordVO
	 * @param response
	 * @throws Exception
	 */
	private JSONObject composeExcel(List<DynaBean> meter, List<DynaBean> rows, PowerRecordVO powerRecordVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		JSONObject data = new JSONObject();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("PowerRecord");	
			
			XSSFCellStyle titleStyleNB = ExcelUtil.getTitleStyle(workbook, false, 26);//標題樣式
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyleW(workbook, true, 14);//標題樣式		
			XSSFCellStyle titleStyleW = ExcelUtil.getTitleStyleW(workbook, true, 14);//標題樣式
			titleStyleW.setWrapText(true);
			XSSFCellStyle styleLNB = ExcelUtil.getTextStyle(workbook, "L", false, 14);
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 14);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0.0#", true, 14);
			
			DynaBean mBean = meter.get(0);
			
			XSSFRow row0 = sheet.createRow(0);	
			ExcelUtil.createCell(row0, titleStyleNB, 0, XSSFCell.CELL_TYPE_STRING, "電力數值紀錄表");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			
			XSSFRow row1 = sheet.createRow(1);
			ExcelUtil.createCell(row1, styleLNB, 0, XSSFCell.CELL_TYPE_STRING, "時間區間:");
			ExcelUtil.createCell(row1, styleLNB, 1, XSSFCell.CELL_TYPE_STRING, powerRecordVO.getStartDate()+"~"+powerRecordVO.getEndDate());
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
			
			XSSFRow row2 = sheet.createRow(2);
			ExcelUtil.createCell(row2, styleLNB, 0, XSSFCell.CELL_TYPE_STRING, "DeviceID:");
			ExcelUtil.createCell(row2, styleLNB, 1, XSSFCell.CELL_TYPE_STRING, mBean.get("deviceid"));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
			
			XSSFRow row3 = sheet.createRow(3);
			ExcelUtil.createCell(row3, styleLNB, 0, XSSFCell.CELL_TYPE_STRING, "安裝位置:");
			ExcelUtil.createCell(row3, styleLNB, 1, XSSFCell.CELL_TYPE_STRING, ObjectUtils.toString(mBean.get("installposition")));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
			
			List<String> filter = powerRecordVO.getFilter();
			int column = 0;
			XSSFRow row4 = sheet.createRow(4);
			
			ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "時間");	
			if(filter.contains("I1")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "電流R相");
			}
			if(filter.contains("I2")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "電流S相");
			}
			if(filter.contains("I3")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "電流T相");
			}
			if(filter.contains("Iavg")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "平均電流");
			}
			if(filter.contains("V1")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "相電壓R相");
			}
			if(filter.contains("V2")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "相電壓S相");
			}
			if(filter.contains("V3")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "相電壓T相");
			}
			if(filter.contains("Vavg")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "平均相電壓");
			}
			if(filter.contains("V12")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "線電壓R相");
			}
			if(filter.contains("V23")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "線電壓S相");
			}
			if(filter.contains("V31")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "線電壓T相");
			}
			if(filter.contains("VavgP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "平均線電壓");
			}
			if(filter.contains("W")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "實功");
			}
			if(filter.contains("RP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "虛功");
			}
			if(filter.contains("VA")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "視在");
			}
			if(filter.contains("PF")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "功因");
			}
			if(filter.contains("Hz")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "頻率");
			}
			if(filter.contains("Mode1")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "混合式");
			}
			if(filter.contains("Mode2")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "浮動式");
			}
			if(filter.contains("Mode3")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "固定式");
			}
			if(filter.contains("Mode4")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "平均式");
			}
			if(filter.contains("DemandPK")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰需量");
			}
			if(filter.contains("DemandSP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰需量");
			}
			if(filter.contains("DemandSatSP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "週六半需量");
			}
			if(filter.contains("DemandOP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰需量");
			}
			if(filter.contains("ECO5PK")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰用電量");
			}
			if(filter.contains("ECO5SP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰用電量");
			}
			if(filter.contains("ECO5SatSP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "週六半尖峰用電量");
			}
			if(filter.contains("ECO5OP")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰用電量");
			}
			if(filter.contains("KWH")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "KWH");//電表值
			}
			if(filter.contains("Kvarh")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "Kvarh");//無效電量
			}
			if(filter.contains("THVavg")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "THVavg");//電壓總諧波率
			}
			if(filter.contains("THIavg")) {
				ExcelUtil.createCell(row4, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "THIavg");//電流總諧波率 
			}
			
			
			int y = 5;			
			for(DynaBean bean : rows) {
				column = 0;
				XSSFRow row = sheet.createRow(y++);
				
				ExcelUtil.createCell(row, styleL, column++, XSSFCell.CELL_TYPE_STRING, ToolUtil.dateFormat(bean.get("rectime"), sdf));				
				if(filter.contains("I1")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("i1")));//電流R相
				}
				if(filter.contains("I2")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("i2")));//電流S相
				}
				if(filter.contains("I3")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("i3")));//電流T相
				}
				if(filter.contains("Iavg")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("iavg")));//平均電流
				}
				if(filter.contains("V1")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v1")));//相電壓R相
				}
				if(filter.contains("V2")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v2")));//電壓S相
				}
				if(filter.contains("V3")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v3")));//電壓T相
				}
				if(filter.contains("Vavg")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("vavg")));//平均相電壓
				}
				if(filter.contains("V12")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v12")));//線電壓R相
				}
				if(filter.contains("V23")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v23")));//線電壓S相
				}
				if(filter.contains("V31")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("v31")));//線電壓T相
				}
				if(filter.contains("VavgP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("vavgp")));//平均線電壓
				}
				if(filter.contains("W")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("w")));//實功
				}
				if(filter.contains("RP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("var")));//虛功
				}
				if(filter.contains("VA")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("va")));//視在
				}
				if(filter.contains("PF")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("pf")));//功因
				}
				if(filter.contains("Hz")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("hz")));//頻率
				}
				if(filter.contains("Mode1")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mode1")));//混合式
				}
				if(filter.contains("Mode2")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mode2")));//浮動式
				}
				if(filter.contains("Mode3")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mode3")));//固定式
				}
				if(filter.contains("Mode4")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mode4")));//平均式
				}
				if(filter.contains("DemandPK")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("demandpk")));//尖峰需量
				}
				if(filter.contains("DemandSP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("demandsp")));//半尖峰需量
				}
				if(filter.contains("DemandSatSP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("demandsatsp")));//週六半需量
				}
				if(filter.contains("DemandOP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("demandop")));//離峰需量
				}
				if(filter.contains("ECO5PK")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mcecpk")));//尖峰用電量
				}
				if(filter.contains("ECO5SP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mcecsp")));//半尖峰用電量
				}
				if(filter.contains("ECO5SatSP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mcecsatsp")));//週六半尖峰用電量
				}
				if(filter.contains("ECO5OP")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("mcecop")));//離峰用電量
				}
				if(filter.contains("KWH")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("kwh")));//電表值
				}
				if(filter.contains("Kvarh")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("kvarh")));//無效電量
				}
				if(filter.contains("THVavg")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("thvavg")));//電壓總諧波率
				}
				if(filter.contains("THIavg")) {
					ExcelUtil.createCell(row, styleR, column++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.getBigDecimal(bean.get("thiavg")));//電流總諧波率 
				}
			}	

			ExcelUtil.setSizeColumn(sheet, column);
			for (int i = 0; i < column; i++) {
	            sheet.autoSizeColumn(i, true);
	            sheet.setColumnWidth(i,sheet.getColumnWidth(i)*16/10);
	        }
						
			String fileName = "電力數值"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
			data.put("FileName", fileName);
			data.put("Base64", ExcelUtil.exportBase64(workbook, fileName));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
