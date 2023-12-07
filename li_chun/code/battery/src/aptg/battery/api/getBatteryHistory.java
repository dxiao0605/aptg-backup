package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import aptg.battery.bean.BatteryHistoryBean;
import aptg.battery.bean.RecordBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ExcelUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class getBatteryHistory 電池歷史
 */
@WebServlet("/getBatteryHistory")
public class getBatteryHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryHistory.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryHistory() {
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
		logger.debug("getBatteryHistory start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());	
			logger.debug("UserCompany:"+userCompany);
			logger.debug("request: " + req);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BatteryVO batteryVO = this.parseJson(req, timezone, resource);
					if (batteryVO.isError()) {
						rspJson.put("code", batteryVO.getCode());
						rspJson.put("msg", batteryVO.getDescription());
					} else {
						BatteryDAO batteryDAO = new BatteryDAO();
						List<DynaBean> list = batteryDAO.getBatteryHistoryHeader(batteryVO);					
						if (list != null && !list.isEmpty()) {
							logger.debug("Process Data Start");
							this.processData(list, ToolUtil.getIMPType(userCompany), resource, timezone, batteryVO);
							if ("excel".equals(batteryVO.getType())) {
								logger.debug("Create Excel Start");
								composeExcel(batteryVO, timezone, language, response);
								rep = false;
							} else if ("csv".equals(batteryVO.getType())) {
								composeCSV(batteryVO, timezone, language, response);
								rep = false;
							} else if("check".equals(batteryVO.getType())) {																
								rspJson.put("msg", "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".xlsx");
							} else if("csvcheck".equals(batteryVO.getType())) {																
								rspJson.put("msg", "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
							} else if("excelcheck".equals(batteryVO.getType())) {																
								rspJson.put("msg", "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".xlsx");
							} else {
								rspJson.put("msg", convertToJson(batteryVO.getHistoryBean()));
							}
							rspJson.put("code", "00");
						} else {
							rspJson.put("code", "07");
							rspJson.put("msg", resource.getString("5004"));//查無資料
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
		} finally {
			if (rep) {
				logger.debug("rsp: " + rspJson);		
				ToolUtil.response(rspJson.toString(), response);
			}
		}
		logger.debug("getBatteryHistory end");
	}
	
	/**
	 * 解析Json
	 * 
	 * @param json
	 * @param timezone
	 * @param resource
	 * @return BatteryVO
	 * @throws Exception
	 */
	private BatteryVO parseJson(String json, String timezone, ResourceBundle resource) throws Exception {		
		BatteryVO batteryVO = new BatteryVO();
		try {
			JSONObject request = new JSONObject(json);
			if (!ToolUtil.isNull(request, "RecTime")) {
				SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				JSONObject recTime = request.getJSONObject("RecTime");
				String radio = recTime.optString("Radio");
				if ("1".equals(radio)) {//radio選1，則帶入前端日期
					String start = recTime.optString("Start")+" "+recTime.optString("StartHH")+":"+recTime.optString("StartMM")+":00";
					String end = recTime.optString("End")+" "+recTime.optString("EndHH")+":"+recTime.optString("EndMM")+":00";

					if(ToolUtil.isNull(recTime, "Start") || ToolUtil.isNull(recTime, "StartHH") || ToolUtil.isNull(recTime, "StartMM") ||
							ToolUtil.isNull(recTime, "End") || ToolUtil.isNull(recTime, "EndHH") || ToolUtil.isNull(recTime, "EndMM")) {				
						batteryVO.setError(true);
						batteryVO.setCode("24");
						batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
						return batteryVO;
					}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm:ss") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm:ss")) {
						batteryVO.setError(true);
						batteryVO.setCode("16");
						batteryVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
						return batteryVO;
					}
					
					batteryVO.setStartDate(sdf2.format(sdf.parse(start)));
					batteryVO.setEndDate(sdf2.format(sdf.parse(end)));
				}else if ("3".equals(radio)) {//預設1個月
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, 1);
					batteryVO.setEndDate(sdf2.format(cal.getTime()));
					cal.add(Calendar.MONTH, -1);
					batteryVO.setStartDate(sdf2.format(cal.getTime()));					
				}else if ("5".equals(radio)) {//預設7天
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, 1);
					batteryVO.setEndDate(sdf2.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, -7);
					batteryVO.setStartDate(sdf2.format(cal.getTime()));
				}else if ("0".equals(radio)) {//預設1天
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, 1);
					batteryVO.setEndDate(sdf2.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, -1);
					batteryVO.setStartDate(sdf2.format(cal.getTime()));
				}else {//radio選其他，則帶入預設前一天日期
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -1);
					batteryVO.setStartDate(sdf2.format(cal.getTime()));
				}
			}else {
				batteryVO.setError(true);
				batteryVO.setCode("24");
				batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
			}
							
			batteryVO.setBattInternalId(request.optString("BattInternalId"));
			batteryVO.setType(request.optString("Type"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	private void processData(List<DynaBean> rows, int impType, ResourceBundle resource, String timezone, BatteryVO batteryVO) throws Exception {		
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		SimpleDateFormat date = ToolUtil.getDateFormat("yyyy/MM/dd", timezone);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {	
			BatteryHistoryBean historyBean = new BatteryHistoryBean();
			DynaBean headerBean = rows.get(0);
			historyBean.setBatteryGroupID(ObjectUtils.toString(headerBean.get("nbid"))+"_"+ObjectUtils.toString(headerBean.get("batteryid")));
			historyBean.setCountry(ObjectUtils.toString(headerBean.get("country")));//國家
			historyBean.setArea(ObjectUtils.toString(headerBean.get("area")));//地域
			historyBean.setGroupID(ObjectUtils.toString(headerBean.get("groupid")));//基地台號碼
			historyBean.setGroupName(ObjectUtils.toString(headerBean.get("groupname")));//基地台名稱
			historyBean.setInstallDate(ToolUtil.dateFormat(headerBean.get("installdate"), date));//安裝日期
			historyBean.setBatteryType(ObjectUtils.toString(headerBean.get("batterytypename")));//電池型號
			historyBean.setAddress(ObjectUtils.toString(headerBean.get("address")));//地址
			historyBean.setLng(ToolUtil.getBigDecimal(headerBean.get("lng")));//經度
			historyBean.setLat(ToolUtil.getBigDecimal(headerBean.get("lat")));//緯度
			historyBean.setIMPType(impType);
			String batteryId = ObjectUtils.toString(headerBean.get("batteryid"));
			
			NbListVO nbListVO = new NbListVO();
			nbListVO.setNbId(ObjectUtils.toString(headerBean.get("nbid")));
			nbListVO.setGroupInternalId(ObjectUtils.toString(headerBean.get("seqno")));
			nbListVO.setContinuousSeqNo(ObjectUtils.toString(headerBean.get("continuousseqno")));
			NbListDAO nbListDAO = new NbListDAO();			
			List<DynaBean> groupHisList = nbListDAO.getNBGroupHis(nbListVO);
			List<RecordBean> recordList = new ArrayList<RecordBean>();
			int seq = 0;
			RecordBean recordBean = null;
			if(groupHisList != null && !groupHisList.isEmpty()) {
				BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
				List<String> rectimeArr = new ArrayList<String>();//報表圖表使用
				LinkedHashMap<String, List<BigDecimal>> irMap = new LinkedHashMap<String, List<BigDecimal>>();//報表圖表使用
				LinkedHashMap<String, List<BigDecimal>> volMap = new LinkedHashMap<String, List<BigDecimal>>();//報表圖表使用
				LinkedHashMap<String, List<BigDecimal>> temperatureMap = new LinkedHashMap<String, List<BigDecimal>>();//報表圖表使用								
				Map<String, RecordBean> recordBeanMap = new HashMap<String, RecordBean>();
				String zone="";				
				for(DynaBean hisBean : groupHisList) {
					String groupStartTime = ToolUtil.dateFormat(hisBean.get("starttime"), sdf2);
					String groupEndTime = ToolUtil.dateFormat(hisBean.get("endtime"), sdf2);
					
					BatteryVO vo = new BatteryVO();
					vo.setNbId(ObjectUtils.toString(hisBean.get("nbid")));
					vo.setBatteryId(batteryId);
					if(sdf2.parse(batteryVO.getStartDate()).after(sdf2.parse(groupStartTime))) {
						vo.setStartDate(batteryVO.getStartDate());
					}else {
						vo.setStartDate(groupStartTime);
					}
					if(sdf2.parse(batteryVO.getEndDate()).before(sdf2.parse(groupEndTime))) {
						vo.setEndDate(batteryVO.getEndDate());
					}else {
						vo.setEndDate(groupEndTime);
					}
					vo.setRecTimeDesc(true);
					
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> reclist = batteryDAO.getBatteryHistory(vo);						
					if (reclist != null && !reclist.isEmpty()) {
						for(int i=0; i<reclist.size(); i++) {							
							DynaBean bean = reclist.get(i);
							if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
								zone = bean.get("timezone").toString();
								sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", zone);
							}
							String rectime = ToolUtil.dateFormat(bean.get("rectime"), sdf);
							if(recordBeanMap.containsKey(rectime)) {
								recordBean = recordBeanMap.get(rectime);					
							}else {		
								recordBean = new RecordBean();				
								recordBean.setSeq(++seq);
								recordBean.setBatteryGroupID(ObjectUtils.toString(bean.get("nbid"))+"_"+ObjectUtils.toString(bean.get("batteryid")));
								recordBean.setRecTime(rectime);//數據更新時間

								recordBean.setIR(new ArrayList<String>());
								recordBean.setVol(new ArrayList<String>());
								recordBean.setStatusDesc(new ArrayList<String>());
								recordBean.setStatusCode(new ArrayList<Integer>());
								
								recordBeanMap.put(rectime, recordBean);
								recordList.add(recordBean);
								rectimeArr.add(rectime);
							}
							
							String category = ObjectUtils.toString(bean.get("category"));
							String ch = "CH"+bean.get("orderno");
							if("1".equals(category)) {								
								BigDecimal ir;
								if(impType==22) {//電導值
									ir = ToolUtil.divide(1000000, bean.get("value"), 3);
								}else if(impType==21) {//毫內阻值
									ir = ToolUtil.divide(bean.get("value"), 1000, 3);
								}else {//內阻值
									ir = ToolUtil.getBigDecimal(bean.get("value"), 0);
								}
								if(ToolUtil.getBigDecimal(bean.get("value"), 0).compareTo(ol)>=0) {
									recordBean.getIR().add(ch+" OL");
								}else {						
									recordBean.getIR().add(ch+" "+ir);
								}
																		
								if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("status")))) {
									recordBean.getStatusCode().add(Integer.parseInt(bean.get("status").toString()));
									recordBean.getStatusDesc().add(resource.getString(ObjectUtils.toString(bean.get("status"))));
								}else {
									recordBean.getStatusCode().add(1);
									recordBean.getStatusDesc().add(resource.getString("1"));
								}
								this.setMap(irMap, ch, ir, seq);
							}else if("2".equals(category)) {
								BigDecimal vol = ToolUtil.divide(bean.get("value"), 1000, 2);
								recordBean.getVol().add(ch+" "+vol);//電壓
								this.setMap(volMap, ch, vol, seq);
							}else if("3".equals(category)) {
								BigDecimal temperature = ToolUtil.getBigDecimal(bean.get("value"));
								recordBean.setTemperature(temperature);//溫度
								this.setMap(temperatureMap, resource.getString("1041"), temperature, seq);
							}				
						}						
					}
				}
			
				if ("excel".equals(batteryVO.getType())){
					//將List的排序反過來
					Collections.reverse(rectimeArr);
					this.reverseMapList(irMap, seq);
					this.reverseMapList(volMap, seq);
					this.reverseMapList(temperatureMap, seq);

					batteryVO.setRectimeArr(rectimeArr);
					batteryVO.setIrMap(irMap);
					batteryVO.setVolMap(volMap);
					batteryVO.setTemperatureMap(temperatureMap);
				}
			}
			
			if(recordList.isEmpty()) {			
				recordList.add(new RecordBean());//UI呈顯標題使用
			}
			historyBean.setRecord(recordList);
			batteryVO.setHistoryBean(historyBean);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(BatteryHistoryBean historyBean) throws Exception {		
		JSONObject data = new JSONObject();
		try {	
			data.put("Battery", new JSONObject(JsonUtil.getInstance().convertObjectToJsonstring(historyBean)));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	

	
	/**
	 * 組Excel
	 * @param historyBean
	 * @param resource
	 * @param response
	 * @throws Exception
	 */
	private void composeExcel(BatteryVO batteryVO, String timezone, String language, HttpServletResponse response) throws Exception {
		try {
			BatteryHistoryBean historyBean = batteryVO.getHistoryBean();
			XSSFWorkbook workbook = new XSSFWorkbook();
			ResourceBundle resource = ToolUtil.getLanguage(language);
			XSSFSheet sheet = workbook.createSheet(historyBean.getBatteryGroupID());
			String unit;//內阻單位
			
			ExcelUtil excelUtil = new ExcelUtil();
			XSSFCellStyle titleStyle = excelUtil.getTitleStyle(workbook, false, 14);//標題樣式
			titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221, 235, 247),null));
			titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  

			XSSFCellStyle textStyle = excelUtil.getTextStyle(workbook, "C", false, 14);
			XSSFCellStyle textStyleO = excelUtil.getTextStyle(workbook, "C", false, 14, IndexedColors.ORANGE.getIndex());
			XSSFCellStyle textStyleR = excelUtil.getTextStyle(workbook, "C", false, 14, IndexedColors.RED.getIndex());
		    
			
			XSSFRow row0 = sheet.createRow(0);	
			excelUtil.createCell(row0, titleStyle, 0, CellType.STRING, resource.getString("1026"));//電池組ID
			excelUtil.createCell(row0, titleStyle, 1, CellType.STRING, resource.getString("1028"));//國家
			excelUtil.createCell(row0, titleStyle, 2, CellType.STRING, resource.getString("1029"));//地域
			excelUtil.createCell(row0, titleStyle, 3, CellType.STRING, resource.getString("1012"));//站台編號
			excelUtil.createCell(row0, titleStyle, 4, CellType.STRING, resource.getString("1013"));//站台名稱
			excelUtil.createCell(row0, titleStyle, 5, CellType.STRING, resource.getString("1027"));//安裝日期	
			excelUtil.createCell(row0, titleStyle, 6, CellType.STRING, resource.getString("1030"));//型號
			excelUtil.createCell(row0, titleStyle, 7, CellType.STRING, resource.getString("1031"));//地址
			
			
			XSSFRow row1 = sheet.createRow(1);	
			excelUtil.createCell(row1, textStyle, 0, CellType.STRING, historyBean.getBatteryGroupID());
			excelUtil.createCell(row1, textStyle, 1, CellType.STRING, historyBean.getCountry());
			excelUtil.createCell(row1, textStyle, 2, CellType.STRING, historyBean.getArea());
			excelUtil.createCell(row1, textStyle, 3, CellType.STRING, historyBean.getGroupID());
			excelUtil.createCell(row1, textStyle, 4, CellType.STRING, historyBean.getGroupName());
			excelUtil.createCell(row1, textStyle, 5, CellType.STRING, historyBean.getInstallDate());
			excelUtil.createCell(row1, textStyle, 6, CellType.STRING, historyBean.getBatteryType());	
			excelUtil.createCell(row1, textStyle, 7, CellType.STRING, historyBean.getAddress());		
			
			XSSFRow row3 = sheet.createRow(3);	
			excelUtil.createCell(row3, titleStyle, 0, CellType.STRING, resource.getString("1015"));//數據更新時間
			if(historyBean.getIMPType()==22) {//電導值
				excelUtil.createCell(row3, titleStyle, 1, CellType.STRING, resource.getString("1020"));
				unit = "[S]";
			}else if(historyBean.getIMPType()==21) {//毫內阻值
				excelUtil.createCell(row3, titleStyle, 1, CellType.STRING, resource.getString("1019"));
				unit = "[mΩ]";
			}else {//內阻值
				excelUtil.createCell(row3, titleStyle, 1, CellType.STRING, resource.getString("1016"));
				unit = "[µΩ]";
			}				
			excelUtil.createCell(row3, titleStyle, 2, CellType.STRING, resource.getString("1017"));//電壓
			excelUtil.createCell(row3, titleStyle, 3, CellType.STRING, resource.getString("1018"));//溫度
			excelUtil.createCell(row3, titleStyle, 4, CellType.STRING, resource.getString("1021"));//電池狀態

			int rownum = 4;
			List<RecordBean> recordList = historyBean.getRecord();
			for(RecordBean bean : recordList) {			
				List<String> irlList = bean.getIR();
				List<String> volList = bean.getVol();
				List<Integer> statusList = bean.getStatusCode();
				int count = irlList.size()>volList.size()?irlList.size():volList.size();				
				for(int j=0; j<count; j++) {
					
					XSSFRow	row = sheet.createRow(rownum);			
					if(j==0) {
						excelUtil.createCell(row, textStyle, 0, CellType.STRING, bean.getRecTime());
						excelUtil.createCell(row, textStyle, 3, CellType.NUMERIC, bean.getTemperature());//溫度
					}

					if(j<irlList.size()) {
						excelUtil.createCell(row, textStyle, 1, CellType.STRING, irlList.get(j));//內阻/電導
					}
					if(j<volList.size()) {
						excelUtil.createCell(row, textStyle, 2, CellType.STRING, volList.get(j));//電壓
					}
										
					if(j<statusList.size()) {				
						String status = String.valueOf(statusList.get(j));
						if("2".equals(status)) {
							excelUtil.createCell(row, textStyleO, 4, CellType.STRING, resource.getString(status));
						}else if("3".equals(status)) {
							excelUtil.createCell(row, textStyleR, 4, CellType.STRING, resource.getString(status));
						}else {
							excelUtil.createCell(row, textStyle, 4, CellType.STRING, resource.getString(status));	
						}
					}
					rownum++;					
				}
//				if(count>0) {
//					sheet.addMergedRegion(new CellRangeAddress(rownum-count, rownum-1, 3, 3));
//					sheet.addMergedRegion(new CellRangeAddress(rownum-count, rownum-1, 6, 6));
//				}
			}
						
			if(sheet!=null) {
				//設定自動欄寬
				excelUtil.setSizeColumn(sheet, 8);
			}
			
			//產生內阻折線圖
			excelUtil.createLineChart(workbook, resource.getString("1039"), resource.getString("1601"), "", 
					unit, batteryVO.getRectimeArr(), batteryVO.getIrMap());
			
			//產生電壓折線圖
			excelUtil.createLineChart(workbook, resource.getString("1040"), resource.getString("1602"), "",
					"[V]", batteryVO.getRectimeArr(), batteryVO.getVolMap());
			
			//產生溫度折線圖
			excelUtil.createLineChart(workbook, resource.getString("1041"), resource.getString("1603"), "", 
					" [℃]", batteryVO.getRectimeArr(), batteryVO.getTemperatureMap());
			
			ExcelUtil.exportXlsx(workbook, "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".xlsx", response);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}
	
	/**
	 *  組CSV
	 * @param data
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(BatteryVO batteryVO, String timezone, String language, HttpServletResponse response) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		try {			
			BatteryHistoryBean historyBean = batteryVO.getHistoryBean();
			
			StringBuilder str = new StringBuilder();
			str.append(resource.getString("1026")).append(",")//電池組ID
			   .append(resource.getString("1028")).append(",")//國家
			   .append(resource.getString("1029")).append(",")//地域
			   .append(resource.getString("1012")).append(",")//站台編號
			   .append(resource.getString("1013")).append(",")//站台名稱
			   .append(resource.getString("1027")).append(",")//安裝日期
			   .append(resource.getString("1030")).append(",")//型號
			   .append(resource.getString("1031")).append("\n");//地址
			   
			str.append(historyBean.getBatteryGroupID()).append(",")
			   .append(historyBean.getCountry()).append(",")
			   .append(historyBean.getArea()).append(",")
			   .append(historyBean.getGroupID()).append(",")
			   .append(historyBean.getGroupName()).append(",")
			   .append(historyBean.getInstallDate()).append(",")
			   .append(historyBean.getBatteryType()).append(",")
			   .append(CsvUtil.csvHandlerStr(historyBean.getAddress())).append("\n");
			   
			str.append("\n");
			str.append(resource.getString("1015")).append(",");//數據更新時間					
			if(historyBean.getIMPType()==22) {//電導值
				str.append(resource.getString("1020")).append(",");
			}else if(historyBean.getIMPType()==21) {//毫內阻值
				str.append(resource.getString("1019")).append(",");
			}else {//內阻值
				str.append(resource.getString("1016")).append(",");
			}				
			str.append(resource.getString("1017")).append(",")//電壓
			   .append(resource.getString("1018")).append(",")//溫度
			   .append(resource.getString("1021")).append("\n");//電池狀態

			List<RecordBean> recordList = historyBean.getRecord();
			for(RecordBean bean : recordList) {	
				List<String> irlList = bean.getIR();
				List<String> volList = bean.getVol();
				List<Integer> statusList = bean.getStatusCode();
				int count = irlList.size()>volList.size()?irlList.size():volList.size();				
				for(int j=0; j<count; j++) {

					if(j==0) {
						str.append(bean.getRecTime()).append(",");
					}else {
						str.append("").append(",");
					}
					
					if(j<irlList.size()) {
						str.append(irlList.get(j)).append(",");
					}else {
						str.append("").append(",");
					}
					if(j<volList.size()) {
						str.append(volList.get(j)).append(",");
					}else {
						str.append("").append(",");
					}
					
					if(j==0) {
						str.append(bean.getTemperature()).append(",");
					}else {
						str.append("").append(",");
					}
										
					if(j<statusList.size()) {				
						String status = String.valueOf(statusList.get(j));								
						str.append(resource.getString(status)).append("\n");									
					}else {
						str.append("").append("\n");	
					}							
				}			
			}
					
			CsvUtil.exportCsv(str, "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv", response);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}
	
	private void setMap(LinkedHashMap<String, List<BigDecimal>> map, String key, BigDecimal value, int num) {		
		List<BigDecimal> list;		
		if(map.containsKey(key)) {
			list = map.get(key);
		}else {
			list = new ArrayList<BigDecimal>();			
			map.put(key, list);
		}
		if(num!=1 && num-1!=list.size()) {
			for(int j=list.size(); j<num-1; j++) {
				list.add(null);
			}
		}
		list.add(value);
	}
	
	private void reverseMapList(LinkedHashMap<String, List<BigDecimal>> map, int num) {
		List<BigDecimal> list;
		for(String key : map.keySet()) {
			list = map.get(key);
			if(num!= list.size()) {
				for(int j=list.size(); j<num; j++) {
					list.add(null);
				}
			}
	
			Collections.reverse(list);
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
