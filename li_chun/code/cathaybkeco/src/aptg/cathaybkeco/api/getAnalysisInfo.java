package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import aptg.cathaybkeco.vo.AnalysisVO;
import aptg.cathaybkeco.vo.BankInfVO;

/**
 * Servlet implementation class getAnalysisInfo 分析計算
 */
@WebServlet("/getAnalysisInfo")
public class getAnalysisInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getAnalysisInfo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAnalysisInfo() {
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
		logger.debug("getAnalysisInfo start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String cityGroup = ObjectUtils.toString(request.getParameter("cityGroup"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String usageCode = ObjectUtils.toString(request.getParameter("usageCode"));
			String lastCode = ObjectUtils.toString(request.getParameter("lastCode"));
			String filter = ObjectUtils.toString(request.getParameter("filter"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);
			logger.debug("CityGroup:" + cityGroup + ",City:" + city + ",PostCodeNo:" + postCodeNo);
			logger.debug("BankCode: " + bankCode);
			logger.debug("UsageCode: " + usageCode + ", LastCode:" + lastCode);
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
					bankInfVO.setUsageCodeArr(usageCode);
					bankInfVO.setBankCodeArr(ToolUtil.strToSqlStr(bankCode));
					bankInfVO.setLastCode(lastCode);
					bankInfVO.setFilter(filter);
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> list = null;
					
					if (filter.contains("UsageDesc") || filter.contains("MeterName")) {
						list = bankInfDAO.getFcstInfoByMeter(bankInfVO);
					} else {
						list = bankInfDAO.getFcstInfoByAccount(bankInfVO);						
					}

					if (list != null && list.size() > 0) {
						List<DynaBean> airList = null, areaAndPeopleList = null;
						if(filter.contains("Air")) {
							airList = bankInfDAO.getAir(bankInfVO);
						}
						if("Bank".equals(mode)) {
							areaAndPeopleList = bankInfDAO.getBankAreaAndPeople(bankInfVO);
						}		
						
						rspJson.put("code", "00");
						List<AnalysisVO> rows = beanToVO(list, airList, areaAndPeopleList, filter, mode);
						rspJson.put("count", rows != null ? rows.size() : 0);
						if ("excel".equals(type)) {
							rspJson.put("msg", composeExcel(rows, filterList));
						} else {
							rspJson.put("msg", convertToJson(rows, filterList));
						}
					} else {
						rspJson.put("code", "07");
						rspJson.put("count", 0);
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
		logger.debug("getAnalysisInfo end");
	}
	
	private List<AnalysisVO> beanToVO(List<DynaBean> rows, List<DynaBean> airRows, List<DynaBean> areaAndPeopleList, 
			String filter, String mode) throws Exception {
		List<AnalysisVO> analysisList = new ArrayList<AnalysisVO>();
		Map<String, BigDecimal> nowCEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> fcstCEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> lastCEMap = new HashMap<String, BigDecimal>();

		Map<String, BigDecimal> nowAirMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> fcstAirMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> lastAirMap = new HashMap<String, BigDecimal>();
		try {
			
			if(filter.contains("Air") && airRows!=null) {
				for (DynaBean bean : airRows) {
					String key = new String();		
					if(filter.contains("City")) 
						key += bean.get("city")+"##";					
					if(filter.contains("Dist")) 
						key += bean.get("dist")+"##";				
					if(filter.contains("BankCode")) 
						key += bean.get("bankcode")+"##";				
					if(filter.contains("PowerAccount")) 
						key += bean.get("poweraccount");					
					
					if ("1".equals(ObjectUtils.toString(bean.get("usagecode")))) {		
						nowCEMap.put(key, ToolUtil.getBigDecimal(bean.get("mcec")));
//						fcstCEMap.put(key, ToolUtil.getFcstMCEC(bean.get("mcec")));
						fcstCEMap.put(key, ToolUtil.getBigDecimal(bean.get("fcsteco5mcec")));						
						lastCEMap.put(key, ToolUtil.getBigDecimal(bean.get("lastmcec")));
					}else if ("2".equals(ObjectUtils.toString(bean.get("usagecode")))) {			
						nowAirMap.put(key, ToolUtil.getBigDecimal(bean.get("mcec")));
//						fcstAirMap.put(key, ToolUtil.getFcstMCEC(bean.get("mcec")));
						fcstAirMap.put(key, ToolUtil.getBigDecimal(bean.get("fcsteco5mcec")));
						lastAirMap.put(key, ToolUtil.getBigDecimal(bean.get("lastmcec")));			
					}
				}		
			}
			
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
						key += bean.get("bankcode")+"##";	
					
					areaMap.put(key, ToolUtil.getBigDecimal(bean.get("area")));	
					peopleMap.put(key, ToolUtil.getBigDecimal(bean.get("people")));	
				}
			}
			
			for (DynaBean bean : rows) {
				AnalysisVO analysisVO = new AnalysisVO();
				String key = new String();
				if (filter.contains("City")) 					
					key += bean.get("city")+"##";
				if (filter.contains("Dist")) 					
					key += bean.get("dist")+"##";
				if (filter.contains("BankCode")) 				
					key += bean.get("bankcode")+"##";
				if (filter.contains("PowerAccount")) 
					key += bean.get("poweraccount");
				
				if(ToolUtil.parseDouble(bean.get("mcecsp"))<=0 ||
						ToolUtil.parseDouble(bean.get("mcecsatsp"))<=0 || 
						ToolUtil.parseDouble(bean.get("mcecop"))<=0) {	
					analysisVO.setNull(true);
				}
				
				analysisVO.setCity(ObjectUtils.toString(bean.get("city")));
				analysisVO.setDist(ObjectUtils.toString(bean.get("dist")));
				analysisVO.setBankCode(ObjectUtils.toString(bean.get("bankcode")));
				analysisVO.setBankName(ObjectUtils.toString(bean.get("bankname")));
				analysisVO.setPowerAccount(ObjectUtils.toString(bean.get("poweraccount")));
				analysisVO.setRatePlanDesc(ObjectUtils.toString(bean.get("rateplandesc")));
				analysisVO.setAccountDesc(ObjectUtils.toString(bean.get("accountdesc")));
				
				if("Bank".equals(mode)) {
					if(areaMap.containsKey(key))
						analysisVO.setArea(areaMap.get(key).toString());
					if(peopleMap.containsKey(key))
						analysisVO.setPeople(peopleMap.get(key).toString());
				}else {
					if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("area"))))
						analysisVO.setArea(ObjectUtils.toString(bean.get("area")));			
					if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("people"))))
						analysisVO.setPeople(ObjectUtils.toString(bean.get("people")));
				}
				
				if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("cc"))))
					analysisVO.setCc(ObjectUtils.toString(bean.get("cc")));
				analysisVO.setUsageCode(ObjectUtils.toString(bean.get("usagecode")));
				analysisVO.setUsageDesc(ObjectUtils.toString(bean.get("usagedesc")));				
				analysisVO.setMeterName(ObjectUtils.toString(bean.get("metername")));
				
				if (filter.contains("UsageDesc") || filter.contains("MeterName")) {
					BigDecimal fcstPrice = ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2);
					BigDecimal price = ToolUtil.divide(bean.get("nowtotalcharge"), bean.get("nowmcec"), 2);
					BigDecimal lastPrice = ToolUtil.divide(bean.get("lasttotalcharge"), bean.get("lasttotalmcec"), 2);
					// by 電表
					if (filter.contains("Now")) {
						if (filter.contains("MCEC")) {
							analysisVO.setMcec(ToolUtil.getBigDecimal(bean.get("mcec")));
						}
						if (filter.contains("TotalCharge")) {
							analysisVO.setTotalCharge(price.multiply(ToolUtil.getBigDecimal(bean.get("mcec"))).setScale(0, BigDecimal.ROUND_HALF_UP));
						}
						if (filter.contains("Price")) {
							analysisVO.setPrice(price);
						}
						if (filter.contains("EUI")) {
							analysisVO.setEui(ToolUtil.divide(bean.get("mcec"), bean.get("area"), 2));
						}			
						if (filter.contains("EPUI")) {
							analysisVO.setEpui(ToolUtil.divide(bean.get("mcec"), bean.get("people"), 2));
						}
						if (filter.contains("Air") && "1".equals(ObjectUtils.toString(bean.get("usagecode")))) {
							analysisVO.setAir(ToolUtil.divide(nowAirMap.get(key),nowCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}

					if (filter.contains("Fcst")) {
						BigDecimal fcstMCEC = ToolUtil.getBigDecimal(bean.get("fcsteco5mcec"));
//						BigDecimal fcstMCEC = ToolUtil.getFcstMCEC(bean.get("mcec"));

						if (filter.contains("MCEC")) {
							analysisVO.setFcstMCEC(fcstMCEC);			
						}
						if (filter.contains("TotalCharge")) {
							analysisVO.setFcstTotalCharge(fcstPrice.multiply(fcstMCEC).setScale(0, BigDecimal.ROUND_HALF_UP));
						}
						
						if (filter.contains("Price")) {
							analysisVO.setFcstPrice(fcstPrice);
						}

						if (filter.contains("EUI")) {
							analysisVO.setFcstEUI(ToolUtil.divide(fcstMCEC, bean.get("area"), 2));
						}
						if (filter.contains("EPUI")) {
							analysisVO.setFcstEPUI(ToolUtil.divide(fcstMCEC, bean.get("people"), 2));
						}
						if (filter.contains("Air") && "1".equals(ObjectUtils.toString(bean.get("usagecode")))) {
							analysisVO.setFcstAir(ToolUtil.divide(fcstAirMap.get(key), fcstCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}

					if (filter.contains("Last")) {					
						if (filter.contains("MCEC")) {
							analysisVO.setLastMCEC(ToolUtil.getBigDecimal(bean.get("lastmcec")));
						}
						
						if (filter.contains("TotalCharge")) {
							analysisVO.setLastTotalCharge(lastPrice.multiply(ToolUtil.getBigDecimal(bean.get("lastmcec"))).setScale(0, BigDecimal.ROUND_HALF_UP));
						}
						if (filter.contains("Price")) {
							analysisVO.setLastPrice(lastPrice);
						}
						if (filter.contains("EUI")) {

							analysisVO.setLastEUI(ToolUtil.divide(bean.get("lastmcec"), bean.get("area"), 2));
						}
						if (filter.contains("EPUI")) {
							analysisVO.setLastEPUI(ToolUtil.divide(bean.get("lastmcec"), bean.get("people"), 2));
						}
						if (filter.contains("Air") && "1".equals(ObjectUtils.toString(bean.get("usagecode")))) {
							analysisVO.setLastAir(ToolUtil.divide(lastAirMap.get(key),lastCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}

				} else {
					// by 電號
//					BigDecimal fcstPrice = ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2);
					if (filter.contains("Now")) {
						if (filter.contains("MCEC")) {
							analysisVO.setMcec(ToolUtil.getBigDecimal(bean.get("mcec")));
						}
						if (filter.contains("TotalCharge")) {
							analysisVO.setTotalCharge(ToolUtil.getBigDecimal(bean.get("nowtotalcharge")).setScale(0, BigDecimal.ROUND_HALF_UP));							
						}
						if (filter.contains("Price")) {
							analysisVO.setPrice(ToolUtil.divide(bean.get("nowtotalcharge"), bean.get("mcec"), 2));
						}
						if (filter.contains("EUI")) {
							analysisVO.setEui(ToolUtil.divide(bean.get("mcec"), analysisVO.getArea(), 2));
						}
						if (filter.contains("EPUI")) {
							analysisVO.setEpui(ToolUtil.divide(bean.get("mcec"), analysisVO.getPeople(), 2));
						}
						if (filter.contains("Air")) {
							analysisVO.setAir(ToolUtil.divide(nowAirMap.get(key), nowCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}

					if (filter.contains("Fcst")) {
						if (filter.contains("MCEC")) {
							analysisVO.setFcstMCEC(ToolUtil.getBigDecimal(bean.get("fcstmcec")));
						}
						if (filter.contains("TotalCharge")) {
							analysisVO.setFcstTotalCharge(ToolUtil.getBigDecimal(bean.get("fcsttotalcharge")).setScale(0, BigDecimal.ROUND_HALF_UP));
						}
						if (filter.contains("Price")) {
							analysisVO.setFcstPrice(ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2));
						}
						if (filter.contains("EUI")) {
							analysisVO.setFcstEUI(ToolUtil.divide(bean.get("fcstmcec"), analysisVO.getArea(), 2));
						}
						if (filter.contains("EPUI")) {
							analysisVO.setFcstEPUI(ToolUtil.divide(bean.get("fcstmcec"), analysisVO.getPeople(), 2));
						}
						if (filter.contains("Air")) {
							analysisVO.setFcstAir(ToolUtil.divide(fcstAirMap.get(key), fcstCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}

					if (filter.contains("Last")) {
						if (filter.contains("MCEC")) {
							analysisVO.setLastMCEC(ToolUtil.getBigDecimal(bean.get("lastmcec")));
						}
						if (filter.contains("TotalCharge")) {
							analysisVO.setLastTotalCharge(ToolUtil.getBigDecimal(bean.get("lasttotalcharge")).setScale(0, BigDecimal.ROUND_HALF_UP));
						}
						if (filter.contains("Price")) {
							analysisVO.setLastPrice(ToolUtil.divide(bean.get("lasttotalcharge"), bean.get("lastmcec"), 2));
						}
						if (filter.contains("EUI")) {
							analysisVO.setLastEUI(ToolUtil.divide(bean.get("lastmcec"), analysisVO.getArea(), 2));
						}
						if (filter.contains("EPUI")) {
							analysisVO.setLastEPUI(ToolUtil.divide(bean.get("lastmcec"), analysisVO.getPeople(), 2));
						}
						if (filter.contains("Air")) {
							analysisVO.setLastAir(ToolUtil.divide(lastAirMap.get(key), lastCEMap.get(key), 2).multiply(new BigDecimal(100)));
						}
					}
				}	
				analysisList.add(analysisVO);
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return analysisList;
	}
	
	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<AnalysisVO> rows, List<String> filter)
			throws Exception {
		JSONObject data = new JSONObject();
		try {
			boolean isMonthFirstDay = ToolUtil.isMonthFirstDay();
			JSONArray list = new JSONArray();
			for (AnalysisVO analysisVO : rows) {
				JSONObject bank = new JSONObject();
				if (filter.contains("City")) {
					bank.put("City", analysisVO.getCity());// 縣市
				}
				if (filter.contains("Dist")) {
					bank.put("Dist", analysisVO.getDist());// 行政區
				}
				if (filter.contains("BankCode")) {
					bank.put("BankCode", analysisVO.getBankCode());// 分行代碼
					bank.put("BankName", analysisVO.getBankName());// 分行名稱
				}
				if (filter.contains("PowerAccount")) {
					bank.put("PowerAccount", analysisVO.getPowerAccount());// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					bank.put("RatePlanDesc", analysisVO.getRatePlanDesc());// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					bank.put("AccountDesc", analysisVO.getAccountDesc());// 說明
				}
				if (filter.contains("CC")) {
					bank.put("CC", StringUtils.isNotBlank(analysisVO.getCc()) ? new BigDecimal(analysisVO.getCc()): "");// 契約容量
				}
				if (filter.contains("Area")) {
					bank.put("Area", StringUtils.isNotBlank(analysisVO.getArea()) ? new BigDecimal(analysisVO.getArea()): "");// 面積
				}
				if (filter.contains("People")) {
					bank.put("People", StringUtils.isNotBlank(analysisVO.getPeople()) ? new BigDecimal(analysisVO.getPeople()): "");// 員工數
				}
				if (filter.contains("UsageDesc")) {
					bank.put("UsageDesc", analysisVO.getUsageDesc());// 耗能分類
				}
				if (filter.contains("MeterName")) {
					bank.put("MeterName", analysisVO.getMeterName());// 電表名稱
				}
				if (filter.contains("Now")) {
					if(isMonthFirstDay) {
						if (filter.contains("MCEC")) {
							bank.put("MCEC", "--");	
						}
						if (filter.contains("TotalCharge")) {
							bank.put("TotalCharge", "--");
						}
						if (filter.contains("Price")) {
							bank.put("Price", "--");
						}
						if (filter.contains("EUI")) {
							bank.put("EUI", "--");
						}
						if (filter.contains("EPUI")) {
							bank.put("EPUI", "--");
						}
						if (filter.contains("Air") && "1".equals(analysisVO.getUsageCode())) {
							bank.put("Air", "--");
						}
					}else {
						if (filter.contains("MCEC")) {
							bank.put("MCEC", analysisVO.getMcec());// 目前電量	
						}
						if (filter.contains("TotalCharge")) {
							bank.put("TotalCharge", analysisVO.getTotalCharge());// 目前電費
						}
						if (filter.contains("Price")) {
							bank.put("Price", analysisVO.getPrice());// 目前平均單價
						}
						if (filter.contains("EUI")) {
							bank.put("EUI", analysisVO.getEui());// 目前EUI
						}
						if (filter.contains("EPUI")) {
							bank.put("EPUI", analysisVO.getEpui());// 目前EPUI
						}
						if (filter.contains("Air") && "1".equals(analysisVO.getUsageCode())) {
							bank.put("Air", analysisVO.getAir());// 目前空調
						}
					}					
				}

				if (filter.contains("Fcst")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						if (filter.contains("MCEC")) {
							bank.put("FcstMCEC", "--");
						}
						if (filter.contains("TotalCharge")) {
							bank.put("FcstTotalCharge", "--");
						}
						if (filter.contains("Price")) {
							bank.put("FcstPrice", "--");
						}

						if (filter.contains("EUI")) {
							bank.put("FcstEUI", "--");
						}
						if (filter.contains("EPUI")) {
							bank.put("FcstEPUI", "--");
						}
						if (filter.contains("Air") && "1".equals(analysisVO.getUsageCode())) {
							bank.put("FcstAir", "--");
						}
					}else {
						if (filter.contains("MCEC")) {
							bank.put("FcstMCEC", analysisVO.getFcstMCEC());// 預估電量
						}
						if (filter.contains("TotalCharge")) {
							bank.put("FcstTotalCharge", analysisVO.getFcstTotalCharge());// 預估電費
						}
						if (filter.contains("Price")) {
							bank.put("FcstPrice", analysisVO.getFcstPrice());// 預估平均單價
						}

						if (filter.contains("EUI")) {
							bank.put("FcstEUI", analysisVO.getFcstEUI());// 預估EUI
						}
						if (filter.contains("EPUI")) {
							bank.put("FcstEPUI", analysisVO.getFcstEPUI());// 預估EPUI
						}
						if (filter.contains("Air") && "1".equals(analysisVO.getUsageCode())) {
							bank.put("FcstAir", analysisVO.getFcstAir());// 預估空調
						}
					}					
				}

				if (filter.contains("Last")) {
					if (filter.contains("MCEC")) {
						bank.put("LastMCEC", analysisVO.getLastMCEC());// 去年同期電量
					}
					if (filter.contains("TotalCharge")) {
						bank.put("LastTotalCharge", analysisVO.getLastTotalCharge());// 去年同期電費
					}
					if (filter.contains("Price")) {
						bank.put("LastPrice", analysisVO.getLastPrice());// 去年同期平均單價
					}
					if (filter.contains("EUI")) {

						bank.put("LastEUI", analysisVO.getLastEUI());// 去年同期EUI
					}
					if (filter.contains("EPUI")) {
						bank.put("LastEPUI", analysisVO.getLastEPUI());// 去年同期EPUI
					}
					if (filter.contains("Air") && "1".equals(analysisVO.getUsageCode())) {
						bank.put("LastAir", analysisVO.getLastAir());// 去年同期空調
					}
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
	private JSONObject composeExcel(List<AnalysisVO> rows, List<String> filter) throws Exception {
		JSONObject data = new JSONObject();
		try {
			boolean isMonthFirstDay = ToolUtil.isMonthFirstDay();
			XSSFWorkbook workbook = new XSSFWorkbook();
			
			XSSFSheet sheet = workbook.createSheet("AnalysisInfo");
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyle(workbook, true, 14);//標題樣式
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 12);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12);
			int cityCol = 0;
			int distCol = 0;
			int bankCol = 0;
			int column = 0;
			XSSFRow row0 = sheet.createRow(0);
			if (filter.contains("City")) {
				cityCol = column;
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "縣市");
			}
			if (filter.contains("Dist")) {
				distCol = column;
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "行政區");
			}
			if (filter.contains("BankCode")) {
				bankCol = column;
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
			}
			if (filter.contains("PowerAccount")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "電號");
			}
			if (filter.contains("RatePlanDesc")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "用電類型");
			}
			if (filter.contains("AccountDesc")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "說明");
			}
			if (filter.contains("CC")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "契約容量");
			}
			if (filter.contains("UsageDesc")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "耗能分類");
			}
			if (filter.contains("MeterName")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "電表名稱");
			}		
			if (filter.contains("Area")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "面積");
			}
			if (filter.contains("People")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "員工數");
			}
	
			
			if (filter.contains("Now") && filter.contains("MCEC")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前用電量");					
			}
			if (filter.contains("Fcst") && filter.contains("MCEC")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估用電量");					
			}
			if (filter.contains("Last") && filter.contains("MCEC")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期用電量");					
			}
			
			if (filter.contains("Now") && filter.contains("TotalCharge")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前電費");
			}
			if (filter.contains("Fcst") && filter.contains("TotalCharge")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估電費");
			}
			if (filter.contains("Last") && filter.contains("TotalCharge")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期電費");
			}
			
			if (filter.contains("Now") && filter.contains("Price")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前平均單價");
			}
			if (filter.contains("Fcst") && filter.contains("Price")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估平均單價");
			}
			if (filter.contains("Last") && filter.contains("Price")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期平均單價");
			}
			
			if (filter.contains("Now") && filter.contains("EUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前EUI");
			}
			if (filter.contains("Fcst") && filter.contains("EUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估EUI");
			}
			if (filter.contains("Last") && filter.contains("EUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期EUI");
			}
			
			if (filter.contains("Now") && filter.contains("EPUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前EPUI");
			}
			if (filter.contains("Fcst") && filter.contains("EPUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估EPUI");
			}
			if (filter.contains("Last") && filter.contains("EPUI")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期EPUI");
			}
			
			if (filter.contains("Now") && filter.contains("Air")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "目前空調(%)");					
			}
			if (filter.contains("Fcst") && filter.contains("Air")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "預估空調(%)");					
			}
			if (filter.contains("Last") && filter.contains("Air")) {
				ExcelUtil.createCell(row0, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "去年同期空調(%)");					
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
			int y = 0;
			for(AnalysisVO analysisVO : rows) {
				XSSFRow row = sheet.createRow(++y);
				int x = 0;
				if (filter.contains("City")) {
					if(!city.equals(analysisVO.getCity())) {
						city = analysisVO.getCity();	
						cityStartMap.put(city, y);
					}
					cityEndMap.put(city, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getCity());// 縣市
				}
				if (filter.contains("Dist")) {
					if(!dist.equals(analysisVO.getDist())) {
						dist = analysisVO.getDist();	
						distStartMap.put(dist, y);
					}
					distEndMap.put(dist, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getDist());// 行政區
				}
				if (filter.contains("BankCode")) {
					if(!bankCode.equals(analysisVO.getBankCode())) {
						bankCode = analysisVO.getBankCode();	
						bankStartMap.put(bankCode, y);
					}
					bankEndMap.put(bankCode, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bankCode+analysisVO.getBankName());// 分行代碼+分行名稱
				}
				if (filter.contains("PowerAccount")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getPowerAccount());// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getRatePlanDesc());// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getAccountDesc());// 說明
				}
				if (filter.contains("CC")) {
				}
				if (filter.contains("CC")) {
					if(StringUtils.isNotBlank(analysisVO.getCc())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getCc());// 契約容量
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 契約容量
					}
				}
				if (filter.contains("UsageDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getUsageDesc());// 耗能分類
				}
				if (filter.contains("MeterName")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, analysisVO.getMeterName());// 電表名稱
				}		
				if (filter.contains("Area")) {
					if(StringUtils.isNotBlank(analysisVO.getArea())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getArea());// 面積
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 面積
					}
				}
				if (filter.contains("People")) {
					if(StringUtils.isNotBlank(analysisVO.getPeople())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getPeople());// 員工數
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 員工數
					}
				}
				if (filter.contains("Now") && filter.contains("MCEC")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getMcec());	
					}										
				}
				if (filter.contains("Fcst") && filter.contains("MCEC")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstMCEC());
					}
				}
				if (filter.contains("Last") && filter.contains("MCEC")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastMCEC());			
				}
				
				if (filter.contains("Now") && filter.contains("TotalCharge")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getTotalCharge());
					}
				}
				if (filter.contains("Fcst") && filter.contains("TotalCharge")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstTotalCharge());
					}
				}
				if (filter.contains("Last") && filter.contains("TotalCharge")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastTotalCharge());
				}
				
				if (filter.contains("Now") && filter.contains("Price")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getPrice());
					}
				}				
				if (filter.contains("Fcst") && filter.contains("Price")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstPrice());
					}
				}	
				if (filter.contains("Last") && filter.contains("Price")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastPrice());
				}
				
				if (filter.contains("Now") && filter.contains("EUI")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getEui());
					}
				}
				if (filter.contains("Fcst") && filter.contains("EUI")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstEUI());
					}
				}
				if (filter.contains("Last") && filter.contains("EUI")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastEUI());
				}
				
				if (filter.contains("Now") && filter.contains("EPUI")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getEpui());
					}
				}
				if (filter.contains("Fcst") && filter.contains("EPUI")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstEPUI());
					}
				}
				if (filter.contains("Last") && filter.contains("EPUI")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastEPUI());
				}
				
				if (filter.contains("Now") && filter.contains("Air")) {
					if(isMonthFirstDay) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getAir());
					}
				}
				if (filter.contains("Fcst") && filter.contains("Air")) {
					if(isMonthFirstDay || analysisVO.isNull()) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_STRING, "--");
					}else {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getFcstAir());
					}
				}
				if (filter.contains("Last") && filter.contains("Air")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, analysisVO.getLastAir());					
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
				
			String fileName =  "預測數據"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
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

//	private static BigDecimal getBigDecimal(Object value) {
//		return value != null ? new BigDecimal(value.toString()) : new BigDecimal(0);
//	}
//
//	private BigDecimal getBigDecimalDivide(Object value, Object value2) {
//		BigDecimal dividend = value != null ? new BigDecimal(value.toString()) : new BigDecimal(0);
//		BigDecimal divisor = value2 != null ? new BigDecimal(value2.toString()) : new BigDecimal(1);
//		return dividend.divide(divisor.compareTo(new BigDecimal(0)) != 0 ? divisor : new BigDecimal(1), 2,
//				BigDecimal.ROUND_HALF_DOWN);
//	}

}
