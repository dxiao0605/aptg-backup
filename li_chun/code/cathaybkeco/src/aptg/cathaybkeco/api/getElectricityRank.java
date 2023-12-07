package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.dao.KPIDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;
import aptg.cathaybkeco.vo.RankVO;

/**
 * Servlet implementation class getElectricityInfo 用電排行
 */
@WebServlet("/getElectricityRank")
public class getElectricityRank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getElectricityRank.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getElectricityRank() {
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
		logger.debug("getElectricityRank start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String usageCode = ObjectUtils.toString(request.getParameter("usageCode"));
			String filter = ObjectUtils.toString(request.getParameter("filter"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			String recType = ObjectUtils.toString(request.getParameter("recType"));//0:日,1:月
			logger.debug("token: " + token);
			logger.debug("City:" + city + ",PostCodeNo:" + postCodeNo);
			logger.debug("BankCode: " + bankCode+ ",UsageCode:" + usageCode);
			logger.debug("Type:" + type + ",Filter:" + filter);
			logger.debug("recType:"+ recType+ ",Date:" + start + " ~ " + end);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				}else if ("1".equals(recType) && (!ToolUtil.dateCheck(start, "yyyy/MM") || !ToolUtil.dateCheck(end, "yyyy/MM"))) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyy/MM)");
				}else if ("0".equals(recType) && (!ToolUtil.dateCheck(start, "yyyy/MM/dd") || !ToolUtil.dateCheck(end, "yyyy/MM/dd"))) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyy/MM/dd)");
				} else {				
					List<String> filterList = Arrays.asList(filter.split(","));
					String mode = ToolUtil.getMode(filterList);
					BankInfVO bankInfVO = new BankInfVO();
					bankInfVO.setCityArr(ToolUtil.strToSqlStr(city));
					bankInfVO.setPostCodeNoArr(ToolUtil.strToSqlStr(postCodeNo));
					bankInfVO.setBankCodeArr(ToolUtil.strToSqlStr(bankCode));
					bankInfVO.setUsageCodeArr(usageCode);
					bankInfVO.setFilterList(filterList);
					bankInfVO.setStartDate(start);
					bankInfVO.setEndDate(end);
					bankInfVO.setMode(mode);
					bankInfVO.setRecType(recType);
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> basicList, rankList;
					if("Bank".equals(mode)) {//分行
						basicList = bankInfDAO.getElectricityRankBasicBK(bankInfVO);					
					}else {//電表or電號
						basicList = bankInfDAO.getElectricityRankBasicMeter(bankInfVO);
					}
					
					if("0".equals(recType)) {//日
						rankList = bankInfDAO.getElectricityRankDaily(bankInfVO);
					}else {//月
						if("Meter".equals(mode)) {//電表
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
							bankInfVO.setLastStartDate(ToolUtil.getLastYearDay(start, sdf));
							bankInfVO.setLastEndDate(ToolUtil.getLastYearDay(end, sdf));
							rankList = bankInfDAO.getElectricityRankMeter(bankInfVO);
						}else {//電號or分行
							rankList = bankInfDAO.getElectricityRankPA(bankInfVO);
						}
					}	

					if (rankList != null && !rankList.isEmpty()) {
						List<RankVO> voList = beanToVO(basicList, rankList, bankInfVO);
						rspJson.put("code", "00");
						rspJson.put("count", voList != null ? voList.size() : 0);
						if ("excel".equals(type)) {
							rspJson.put("msg", composeExcel(voList, bankInfVO));
						} else {
							JSONObject data = convertToJson(voList, bankInfVO);
							data.put("RecType", recType);
							rspJson.put("msg", data);
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
		logger.debug("getElectricityRank end");
	}
	
	private List<RankVO> beanToVO(List<DynaBean> basicList, List<DynaBean> rankList, BankInfVO bankInfVO) throws Exception {
		List<RankVO> voList = new ArrayList<RankVO>();
		try {
			SimpleDateFormat sdf,sdf2, sdf3 = new SimpleDateFormat("yyyy/MM/dd");
			if("0".equals(bankInfVO.getRecType())) {//日
				sdf = new SimpleDateFormat("yyyy/MM/dd(E)", Locale.CHINESE);
				sdf2 = new SimpleDateFormat("yyyyMMdd");
			}else {//月
				sdf = new SimpleDateFormat("yyyy/MM");
				sdf2 = new SimpleDateFormat("yyyyMM");
			}
			List<String> filter = bankInfVO.getFilterList();
//			String groupPA="";
			Map<String, String> groupPAMap = new HashMap<String, String>();
			Map<String, RankVO> basicMap = new HashMap<String, RankVO>();
			Map<String, RankVO> rankMap = new HashMap<String, RankVO>();
			Map<String, BigDecimal> demandMap = new HashMap<String, BigDecimal>();
			if("Bank".equals(bankInfVO.getMode()) && (filter.contains("MDemand") || filter.contains("MDemandP"))) {
				BankInfDAO bankInfDAO = new BankInfDAO();
				List<DynaBean> bankDemandMaxList = bankInfDAO.getBankDemandMax(bankInfVO);				
				for (DynaBean bean : bankDemandMaxList) {
					String key = "";
					if (filter.contains("RecDate")) {
						key += bean.get("recdate");
					}
					key += ObjectUtils.toString(bean.get("bankcode"));
					demandMap.put(key, ToolUtil.getBigDecimal(bean.get("demand")));
				}
			}
			
			//人數、面積
			RankVO basicVO = null;
			for (DynaBean bean : basicList) {		
				//依key加總數值
				String key = new String();		
				if(filter.contains("City")) 
					key += bean.get("city")+"##";
				if(filter.contains("Dist")) 
					key += bean.get("dist")+"##";
				if(filter.contains("BankCode"))
					key += bean.get("bankcode")+"##";
				if(filter.contains("PowerAccount")) 
					key += bean.get("poweraccount")+"##";
				if(filter.contains("AccountDesc"))
					key += bean.get("accountdesc")+"##";
				if(filter.contains("UsageDesc")) 
					key += bean.get("usagecode")+"##";
				if(filter.contains("MeterName"))
					key += bean.get("deviceid");
						
				if(!basicMap.containsKey(key)) {
					basicVO = new RankVO();
					if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("area"))))
						basicVO.setArea(bean.get("area").toString());
					if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("people"))))
						basicVO.setPeople(bean.get("people").toString());
					
					basicMap.put(key, basicVO);
				}else {
					basicVO = basicMap.get(key);										
					basicVO.setArea(ToolUtil.add(basicVO.getArea(), bean.get("area")).toString());			
					basicVO.setPeople(ToolUtil.add(basicVO.getPeople(), bean.get("people")).toString());					
				}
			}
			
			//排行項目
			RankVO rankVO = null;
			for (DynaBean bean : rankList) {				
				String demandkey = "";				
				String key = new String();//依key加總數值
				String basicKey = new String();//依basicKey取面積、人數
				if (filter.contains("RecDate")) {
					if("0".equals(bankInfVO.getRecType())) {//日
						key += bean.get("recdate")+"##";
						demandkey += ToolUtil.dateFormat(bean.get("recdate"), sdf2);
					}else {//月
						key += bean.get("usemonth")+"##";
						demandkey += ToolUtil.dateFormat(bean.get("usetime"), sdf2);
					}
				}
				if(filter.contains("City")) {
					key += bean.get("city")+"##";
					basicKey += bean.get("city")+"##";
				}
				if(filter.contains("Dist")) { 
					key += bean.get("dist")+"##";
					basicKey += bean.get("dist")+"##";
				}
				if(filter.contains("BankCode")) {
					key += bean.get("bankcode")+"##";
					basicKey += bean.get("bankcode")+"##";
				}
				if(filter.contains("PowerAccount")) { 
					key += bean.get("poweraccount")+"##";
					basicKey += bean.get("poweraccount")+"##";
				}
				if(filter.contains("RatePlanDesc")) {
					key += bean.get("realplan")+"##";
				}
				if(filter.contains("AccountDesc")) {
					key += bean.get("accountdesc")+"##";
					basicKey += bean.get("accountdesc")+"##";
				}
				if(filter.contains("UsageDesc")) { 
					key += bean.get("usagecode")+"##";
					basicKey += bean.get("usagecode")+"##";
				}
				if(filter.contains("MeterName")) {
					key += bean.get("deviceid");
					basicKey += bean.get("deviceid");
				}
				demandkey += ObjectUtils.toString(bean.get("bankcode"));
									
				if(!rankMap.containsKey(key)) {
					rankVO = new RankVO();

					rankVO.setCity(ObjectUtils.toString(bean.get("city")));//縣市
					rankVO.setDist(ObjectUtils.toString(bean.get("dist")));//行政區
					rankVO.setBankCode(ObjectUtils.toString(bean.get("bankcode")));//分行代碼
					rankVO.setBankName(ObjectUtils.toString(bean.get("bankname")));	//分行名稱
					rankVO.setPowerAccount(ObjectUtils.toString(bean.get("poweraccount")));//電號		
					rankVO.setRateplanDesc(ObjectUtils.toString(bean.get("rateplandesc")));//用電種類
					rankVO.setAccountDesc(ObjectUtils.toString(bean.get("accountdesc")));//說明		
					rankVO.setUsageCode(ObjectUtils.toString(bean.get("usagecode")));//耗能分類代碼
					rankVO.setUsageDesc(ObjectUtils.toString(bean.get("usagedesc")));//耗能分類			
					rankVO.setMeterName(ObjectUtils.toString(bean.get("metername")));//電表名稱
					rankVO.setDeviceId(ObjectUtils.toString(bean.get("deviceid")));
					
					if(basicMap.containsKey(basicKey)) {//取得人數、面積
						basicVO = basicMap.get(basicKey);
						rankVO.setArea(basicVO.getArea());
						rankVO.setPeople(basicVO.getPeople());
					}
					
					if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("cc"))))
						rankVO.setCc(bean.get("cc").toString());
					
					if("0".equals(bankInfVO.getRecType())) {//日
						rankVO.setRecDate(ToolUtil.dateFormat(bean.get("recdate"), sdf));//日期
						rankVO.setSortDate(sdf3.parse(ToolUtil.dateFormat(bean.get("recdate"), sdf3)));//排序用日期
											
						rankVO.setMcecSum(ToolUtil.getBigDecimal(bean.get("dcec")));																
						rankVO.setMcecpk(ToolUtil.getBigDecimal(bean.get("tpdcecpk")));
						rankVO.setMcecsp(ToolUtil.getBigDecimal(bean.get("tpdcecsp")));
						rankVO.setMcecsatsp(ToolUtil.getBigDecimal(bean.get("tpdcecsatsp")));
						rankVO.setMcecop(ToolUtil.getBigDecimal(bean.get("tpdcecop")));
						rankVO.setEco5mcecSum(ToolUtil.getBigDecimal(bean.get("dcec")));
						rankVO.setEco5mcecpk(ToolUtil.getBigDecimal(bean.get("dcecpk")));
						rankVO.setEco5mcecsp(ToolUtil.getBigDecimal(bean.get("dcecsp")));
						rankVO.setEco5mcecsatsp(ToolUtil.getBigDecimal(bean.get("dcecsatsp")));
						rankVO.setEco5mcecop(ToolUtil.getBigDecimal(bean.get("dcecop")));
						
					}else {//月
						rankVO.setRecDate(ToolUtil.dateFormat(bean.get("usetime"), sdf));//日期
						rankVO.setSortDate(sdf3.parse(ToolUtil.dateFormat(bean.get("usetime"), sdf3)));//排序用日期
						
						if("Meter".equals(bankInfVO.getMode())) {
							rankVO.setMcecSum(ToolUtil.getBigDecimal(bean.get("cec")));
							rankVO.setEco5mcecSum(ToolUtil.getBigDecimal(bean.get("cec")));
						}else {
							rankVO.setMcecSum(ToolUtil.getBigDecimal(bean.get("tpmcec")));
							rankVO.setEco5mcecSum(ToolUtil.getBigDecimal(bean.get("tpmcec")));
						}					
						rankVO.setMcecpk(ToolUtil.getBigDecimal(bean.get("tpmcecpk")));
						rankVO.setMcecsp(ToolUtil.getBigDecimal(bean.get("tpmcecsp")));
						rankVO.setMcecsatsp(ToolUtil.getBigDecimal(bean.get("tpmcecsatsp")));
						rankVO.setMcecop(ToolUtil.getBigDecimal(bean.get("tpmcecop")));						
						rankVO.setEco5mcecpk(ToolUtil.getBigDecimal(bean.get("mcecpk")));
						rankVO.setEco5mcecsp(ToolUtil.getBigDecimal(bean.get("mcecsp")));
						rankVO.setEco5mcecsatsp(ToolUtil.getBigDecimal(bean.get("mcecsatsp")));
						rankVO.setEco5mcecop(ToolUtil.getBigDecimal(bean.get("mcecop")));
						
						rankVO.setBaseCharge(ToolUtil.getBigDecimal(bean.get("basecharge")));		
						rankVO.setUsageCharge(ToolUtil.getBigDecimal(bean.get("usagecharge")));			
						rankVO.setOverCharge(ToolUtil.getBigDecimal(bean.get("overcharge")));
						rankVO.setTotalCharge(ToolUtil.getBigDecimal(bean.get("totalcharge")));						
						rankVO.setPamcec(ToolUtil.getBigDecimal(bean.get("tpmcec")));//電號用電量
					}

					if("Bank".equals(bankInfVO.getMode())) {
						if(demandMap.containsKey(demandkey)) {
							rankVO.setmDemand(demandMap.get(demandkey));	
							demandMap.remove(demandkey);
						}						
					}else {
						rankVO.setmDemand(ToolUtil.getBigDecimal(bean.get("mdemand")));
					}
					rankVO.setLast(ToolUtil.getBigDecimal(bean.get("last")));//去年同期用電量差
					rankVO.setAir(ToolUtil.getBigDecimal(bean.get("air")));//主要空調用電
					
					rankMap.put(key, rankVO);
					voList.add(rankVO);					
				}else {
					rankVO = rankMap.get(key);										
					
					if("0".equals(bankInfVO.getRecType())) {//日
						rankVO.setMcecSum(rankVO.getMcecSum().add(ToolUtil.getBigDecimal(bean.get("dcec"))));
						rankVO.setMcecpk(ToolUtil.add(rankVO.getMcecpk(), bean.get("tpdcecpk")));			
						rankVO.setMcecsp(ToolUtil.add(rankVO.getMcecsp(), bean.get("tpdcecsp")));		
						rankVO.setMcecsatsp(ToolUtil.add(rankVO.getMcecsatsp(), bean.get("tpdcecsatsp")));			
						rankVO.setMcecop(ToolUtil.add(rankVO.getMcecop(), bean.get("tpdcecop")));
						rankVO.setEco5mcecSum(rankVO.getEco5mcecSum().add(ToolUtil.getBigDecimal(bean.get("dcec"))));
						rankVO.setEco5mcecpk(ToolUtil.add(rankVO.getEco5mcecpk(), bean.get("dcecpk")));			
						rankVO.setEco5mcecsp(ToolUtil.add(rankVO.getEco5mcecsp(), bean.get("dcecsp")));		
						rankVO.setEco5mcecsatsp(ToolUtil.add(rankVO.getEco5mcecsatsp(), bean.get("dcecsatsp")));			
						rankVO.setEco5mcecop(ToolUtil.add(rankVO.getEco5mcecop(), bean.get("dcecop")));
					}else {//月
						if("Meter".equals(bankInfVO.getMode())) {						
							rankVO.setMcecSum(rankVO.getMcecSum().add(ToolUtil.getBigDecimal(bean.get("cec"))));
							rankVO.setEco5mcecSum(rankVO.getEco5mcecSum().add(ToolUtil.getBigDecimal(bean.get("cec"))));	
						}else {
							rankVO.setMcecSum(rankVO.getMcecSum().add(ToolUtil.getBigDecimal(bean.get("tpmcec"))));
							rankVO.setEco5mcecSum(rankVO.getEco5mcecSum().add(ToolUtil.getBigDecimal(bean.get("tpmcec"))));
						}
						rankVO.setMcecpk(ToolUtil.add(rankVO.getMcecpk(), bean.get("tpmcecpk")));			
						rankVO.setMcecsp(ToolUtil.add(rankVO.getMcecsp(), bean.get("tpmcecsp")));		
						rankVO.setMcecsatsp(ToolUtil.add(rankVO.getMcecsatsp(), bean.get("tpmcecsatsp")));			
						rankVO.setMcecop(ToolUtil.add(rankVO.getMcecop(), bean.get("tpmcecop")));
												
						rankVO.setEco5mcecpk(ToolUtil.add(rankVO.getEco5mcecpk(), bean.get("mcecpk")));			
						rankVO.setEco5mcecsp(ToolUtil.add(rankVO.getEco5mcecsp(), bean.get("mcecsp")));		
						rankVO.setEco5mcecsatsp(ToolUtil.add(rankVO.getEco5mcecsatsp(), bean.get("mcecsatsp")));			
						rankVO.setEco5mcecop(ToolUtil.add(rankVO.getEco5mcecop(), bean.get("mcecop")));
						
						rankVO.setBaseCharge(rankVO.getBaseCharge().add(ToolUtil.getBigDecimal(bean.get("basecharge"))));			
						rankVO.setUsageCharge(rankVO.getUsageCharge().add(ToolUtil.getBigDecimal(bean.get("usagecharge"))));			
						rankVO.setOverCharge(rankVO.getOverCharge().add(ToolUtil.getBigDecimal(bean.get("overcharge"))));
						rankVO.setTotalCharge(rankVO.getTotalCharge().add(ToolUtil.getBigDecimal(bean.get("totalcharge"))));
						rankVO.setPamcec(rankVO.getPamcec().add(ToolUtil.getBigDecimal(bean.get("tpmcec"))));//電號用電量
					}
		
					
												
					if("Bank".equals(bankInfVO.getMode())) {
						if(!ObjectUtils.toString(bean.get("poweraccount")).equals(groupPAMap.get(key))) {
							rankVO.setCc(ToolUtil.add(rankVO.getCc(), bean.get("cc")).toString());
						}
						if(demandMap.containsKey(demandkey)) {
							if(rankVO.getmDemand().compareTo(demandMap.get(demandkey))<0) {
								rankVO.setmDemand(demandMap.get(demandkey));
							}
							demandMap.remove(demandkey);
						}	
					}else {
						if(rankVO.getmDemand().compareTo(ToolUtil.getBigDecimal(bean.get("mdemand")))<0) {
							rankVO.setmDemand(ToolUtil.getBigDecimal(bean.get("mdemand")));
						}
					}
					rankVO.setLast(ToolUtil.add(rankVO.getLast(), bean.get("last")));	
					rankVO.setAir(ToolUtil.add(rankVO.getAir(), bean.get("air")));											
				}
				groupPAMap.put(key, ObjectUtils.toString(bean.get("poweraccount")));
			}
			
			//排序
			voList = voList.stream().sorted(Comparator.comparing(new Function<RankVO, String>() {
				public String apply(RankVO vo) {
					if(filter.contains("City")) {//有選縣市才排序
						return vo.getCity();	
					}else {
						return "";
					}					
				}
			}).thenComparing(new Function<RankVO, String>() {
				public String apply(RankVO vo) {
					if(filter.contains("Dist")) {//有選行政區才排序
						return vo.getDist();
					}else {
						return "";
					}	
				}
			}).thenComparing(new Function<RankVO, String>() {
				public String apply(RankVO vo) {
					return vo.getBankCode();
				}
			}).thenComparing(new Function<RankVO, String>() {
				public String apply(RankVO vo) {
					return vo.getUsageCode();
				}
			}).thenComparing(new Function<RankVO, String>() {
				public String apply(RankVO vo) {
					return vo.getDeviceId();
				}
			}).thenComparing(new Function<RankVO, Date>() {
				public Date apply(RankVO vo) {					
					return vo.getSortDate();
				}
			})
					).collect(Collectors.toList());			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return voList;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<RankVO> rankList, BankInfVO bankInfVO) throws Exception {
		JSONObject data = new JSONObject();
		try {
			List<String> filter = bankInfVO.getFilterList();
			JSONArray list = new JSONArray();
			for (int i=0; i<rankList.size(); i++) {
				RankVO rankVO = rankList.get(i);
				JSONObject rank = new JSONObject();
				rank.put("Seq", i+1);
				if (filter.contains("RecDate")) {
					rank.put("RecDate", rankVO.getRecDate());// 月份/日期
				}
				if (filter.contains("City")) {
					rank.put("City", rankVO.getCity());// 縣市
				}
				if (filter.contains("Dist")) {
					rank.put("Dist", rankVO.getDist());// 行政區
				}
				if (filter.contains("BankCode")) {
					rank.put("BankCode", rankVO.getBankCode());// 分行代碼
					rank.put("BankName", rankVO.getBankName());// 分行名稱
				}
				if (filter.contains("PowerAccount")) {
					rank.put("PowerAccount", rankVO.getPowerAccount());// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					rank.put("RatePlanDesc", rankVO.getRateplanDesc());// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					rank.put("AccountDesc", rankVO.getAccountDesc());// 說明
				}
				if (filter.contains("CC")) {
					rank.put("CC", StringUtils.isNotBlank(rankVO.getCc()) ? new BigDecimal(rankVO.getCc()): "");// 契約容量
				}
				if (filter.contains("UsageDesc")) {
					rank.put("UsageDesc", rankVO.getUsageDesc());// 耗能分類
				}
				if (filter.contains("MeterName")) {
					rank.put("MeterName", rankVO.getMeterName());// 電表名稱
				}		
				if (filter.contains("Area")) {
					rank.put("Area", StringUtils.isNotBlank(rankVO.getArea()) ? new BigDecimal(rankVO.getArea()): "");// 面積
				}
				if (filter.contains("People")) {
					rank.put("People", StringUtils.isNotBlank(rankVO.getPeople()) ? new BigDecimal(rankVO.getPeople()): "");// 員工數
				}
				if (filter.contains("MCEC")) {				
					rank.put("MCECPK", rankVO.getMcecpk());// 台電尖峰用電量				
					rank.put("MCECSP", rankVO.getMcecsp());// 台電半尖峰用電量				
					rank.put("MCECSatSP", rankVO.getMcecsatsp());// 台電周六半尖峰用電量				
					rank.put("MCECOP", rankVO.getMcecop());// 台電離峰用電量
					rank.put("MCECTotal", rankVO.getMcecSum());// 台電總用電量
				}
				if (filter.contains("ECO5")) {									
					rank.put("ECO5PK", rankVO.getEco5mcecpk());// ECO5尖峰用電量				
					rank.put("ECO5SP", rankVO.getEco5mcecsp());// ECO5半尖峰用電量				
					rank.put("ECO5SatSP", rankVO.getEco5mcecsatsp());// ECO5周六半尖峰用電量				
					rank.put("ECO5OP", rankVO.getEco5mcecop());// ECO5離峰用電量
					rank.put("ECO5Total", rankVO.getEco5mcecSum());// ECO5總用電量
				}
				
				rank.put("Remark", "PA:"+rankVO.getPamcec()+",B:"+rankVO.getBaseCharge()+
						",U:"+rankVO.getUsageCharge()+",O:"+rankVO.getOverCharge()+",T:"+rankVO.getTotalCharge());//驗證用
				
				if("1".equals(bankInfVO.getRecType())) {//選擇月的時候，才有電費、平均單價					
					BigDecimal totalPrice = ToolUtil.divide(rankVO.getTotalCharge(), rankVO.getPamcec(), 2);
					if("Meter".equals(bankInfVO.getMode())) {										
						if("1".equals(rankVO.getUsageCode())) {
							BigDecimal basePrice = ToolUtil.divide(rankVO.getBaseCharge(), rankVO.getPamcec(), 2);//基本電費單價(基本電費/電號用電量)
							BigDecimal usagePrice = ToolUtil.divide(rankVO.getUsageCharge(), rankVO.getPamcec(), 2);//流動電費單價(流動電費/電號用電量)
							BigDecimal overPrice = ToolUtil.divide(rankVO.getOverCharge(), rankVO.getPamcec(), 2);//非約定電費單價(非約定電費/電號用電量)
							if (filter.contains("TotalCharge")) {						
								rank.put("TotalCharge", totalPrice.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP));				
							}
							if (filter.contains("BaseCharge")) {
								rank.put("BaseCharge", basePrice.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP));
							}
							if (filter.contains("UsageCharge")) {
								rank.put("UsageCharge", usagePrice.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP));
							}
							if (filter.contains("OverCharge")) {
								rank.put("OverCharge", overPrice.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP));
							}	
						}else {
							BigDecimal charge = totalPrice.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP);
							if (filter.contains("TotalCharge")) {						
								rank.put("TotalCharge", charge);// 總電費=流動電費				
							}
							if (filter.contains("BaseCharge")) {
								rank.put("BaseCharge", "");
							}
							if (filter.contains("UsageCharge")) {
								rank.put("UsageCharge", charge);// 流動電費
							}
							if (filter.contains("OverCharge")) {
								rank.put("OverCharge", "");
							}	
						}				
					}else {					
						if (filter.contains("TotalCharge")) {						
							rank.put("TotalCharge", rankVO.getTotalCharge());// 總電費					
						}
						if (filter.contains("BaseCharge")) {
							rank.put("BaseCharge", rankVO.getBaseCharge());// 基本電費
						}
						if (filter.contains("UsageCharge")) {
							rank.put("UsageCharge", rankVO.getUsageCharge());// 流動電費
						}
						if (filter.contains("OverCharge")) {
							rank.put("OverCharge", rankVO.getOverCharge());// 非約定電費
						}
					}
				
					if (filter.contains("Price")) {
						rank.put("Price", totalPrice);// 平均單價
					}
				}
				
				if (filter.contains("EUI")) {
					rank.put("EUI", ToolUtil.divide(rankVO.getMcecSum(), rankVO.getArea(), 2));// EUI
				}
				if (filter.contains("EPUI")) {
					rank.put("EPUI", ToolUtil.divide(rankVO.getMcecSum(), rankVO.getPeople(), 2));// EPUI
				}
				if (filter.contains("Air")) {			
					rank.put("Air", "1".equals(rankVO.getUsageCode())?ToolUtil.divide(rankVO.getAir(), rankVO.getMcecSum(), 2).multiply(new BigDecimal(100)):"");//主要空調
				}
				if (filter.contains("MDemand")) {
					rank.put("MDemand", rankVO.getmDemand());// 最大需量
				}
				if (filter.contains("MDemandP")) {
					rank.put("MDemandP", ToolUtil.divide(rankVO.getmDemand(), rankVO.getCc(), 2).multiply(new BigDecimal(100)));// 最大需量百分比
				}
				if (filter.contains("Last")) {
					rank.put("Last", ToolUtil.subtract(rankVO.getMcecSum(), rankVO.getLast()));// 去年同期用電量差
				}
				list.put(rank);
			}
			data.put("Rank", list);		
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
	private JSONObject composeExcel(List<RankVO> rankList, BankInfVO bankInfVO) throws Exception {
		JSONObject data = new JSONObject();
		try {
			List<String> filter = bankInfVO.getFilterList();
			BigDecimal unitPriceKPI = BigDecimal.ZERO,euiKPI = BigDecimal.ZERO,epuiKPI = BigDecimal.ZERO, airKPI= BigDecimal.ZERO;
			//取得KPI
			KPIDAO kpiDAO = new KPIDAO();
			List<DynaBean> kpiList = kpiDAO.getKPI();
			if(kpiList!=null && !kpiList.isEmpty()) {
				DynaBean bean = kpiList.get(0);
				unitPriceKPI = ToolUtil.getBigDecimal(bean.get("unitpricekpi"));
				euiKPI = ToolUtil.getBigDecimal(bean.get("euikpi"));
				epuiKPI = ToolUtil.getBigDecimal(bean.get("epuikpi"));
				airKPI = ToolUtil.getBigDecimal(bean.get("airkpi"));
			}			
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			
			XSSFSheet sheet = workbook.createSheet("ElectricityRank");
			XSSFCellStyle titleStyleNB = ExcelUtil.getTitleStyle(workbook, false, 14);//標題樣式(無框線)
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyle(workbook, true, 14);//標題樣式
			XSSFCellStyle styleLNB = ExcelUtil.getTextStyle(workbook, "L", false, 12);
			XSSFCellStyle styleRNB = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", false, 12);
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 12);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12);
			XSSFCellStyle styleRR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12, IndexedColors.RED.getIndex());
			XSSFCellStyle styleRG = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12, IndexedColors.GREEN.getIndex());
			
			
			XSSFRow row0 = sheet.createRow(0);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
			ExcelUtil.createCell(row0, titleStyleNB, 0, XSSFCell.CELL_TYPE_STRING, "用電排行報表");
			ExcelUtil.createBlankCell(row0, titleStyleNB, 1);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 2);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 3);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 4);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 5);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 6);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 7);
			ExcelUtil.createBlankCell(row0, titleStyleNB, 8);
			
			XSSFRow row1 = sheet.createRow(1);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 8));
			ExcelUtil.createCell(row1, titleStyleNB, 0, XSSFCell.CELL_TYPE_STRING, "期間:");
			ExcelUtil.createCell(row1, styleLNB, 1, XSSFCell.CELL_TYPE_STRING, bankInfVO.getStartDate()+"~"+bankInfVO.getEndDate());
			ExcelUtil.createBlankCell(row1, titleStyleNB, 2);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 3);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 4);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 5);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 6);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 7);
			ExcelUtil.createBlankCell(row1, titleStyleNB, 8);
			
			XSSFRow row2 = sheet.createRow(2);
			ExcelUtil.createCell(row2, titleStyleNB, 0, XSSFCell.CELL_TYPE_STRING, "目標值:");
			ExcelUtil.createCell(row2, titleStyleNB, 1, XSSFCell.CELL_TYPE_STRING, "平均單價:");
			ExcelUtil.createCell(row2, styleRNB, 2, XSSFCell.CELL_TYPE_STRING, unitPriceKPI);
			ExcelUtil.createCell(row2, titleStyleNB, 3, XSSFCell.CELL_TYPE_STRING, "EUI:");
			ExcelUtil.createCell(row2, styleRNB, 4, XSSFCell.CELL_TYPE_STRING, euiKPI);


			ExcelUtil.createCell(row2, titleStyleNB, 5, XSSFCell.CELL_TYPE_STRING, "主要空調(%):");
			ExcelUtil.createCell(row2, styleRNB, 6, XSSFCell.CELL_TYPE_STRING, airKPI);
			ExcelUtil.createCell(row2, titleStyleNB, 7, XSSFCell.CELL_TYPE_STRING, "EPUI:");
			ExcelUtil.createCell(row2, styleRNB, 8, XSSFCell.CELL_TYPE_STRING, epuiKPI);
			
			int cityCol = 0, distCol = 0, bankCol = 0,column = 0;
			XSSFRow titlerow = sheet.createRow(4);
			XSSFRow titlerow2 = sheet.createRow(5);
			if (filter.contains("RecDate")) {
				if("0".equals(bankInfVO.getRecType())) {//日
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "日期");
				}else {//月
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "月份");
				}				
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("City")) {
				cityCol = column;
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "縣市");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("Dist")) {
				distCol = column;
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "行政區");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("BankCode")) {
				bankCol = column;
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("PowerAccount")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電號");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("RatePlanDesc")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "用電類型");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("AccountDesc")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "說明");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("CC")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "契約容量");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("UsageDesc")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "耗能分類");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("MeterName")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "電表名稱");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}		
			if (filter.contains("Area")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "面積");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("People")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "員工數");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("MCEC")) {
				int start = column;
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "台電時段用電量");
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "周六半尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "總計");
				sheet.addMergedRegion(new CellRangeAddress(4, 4, start, column++));
			}
			if (filter.contains("ECO5")) {
				int start = column;
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "ECO-5時段用電量");
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "半尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "周六半尖峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column++, XSSFCell.CELL_TYPE_STRING, "離峰");
				ExcelUtil.createBlankCell(titlerow, titleStyle, column);
				ExcelUtil.createCell(titlerow2, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "總計");
				sheet.addMergedRegion(new CellRangeAddress(4, 4, start, column++));
			}
			if("1".equals(bankInfVO.getRecType())) {//選擇月的時候，才有電費、平均單價
				if (filter.contains("BaseCharge")) {
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "基本電費");
					ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
					sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
				}
				if (filter.contains("UsageCharge")) {
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "流動電費");
					ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
					sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
				}
				if (filter.contains("OverCharge")) {
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "非約定電費"); 
					ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
					sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
				}
				if (filter.contains("TotalCharge")) {
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "總電費");
					ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
					sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
				}
				if (filter.contains("Price")) {
					ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "平均單價");
					ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
					sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
				}
			}
			if (filter.contains("EUI")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "EUI");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("EPUI")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "EPUI");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("Air")) {			
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "主要空調(%)");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("MDemand")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "最大需量");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("MDemandP")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "最大需量(%)");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
			}
			if (filter.contains("Last")) {
				ExcelUtil.createCell(titlerow, titleStyle, column, XSSFCell.CELL_TYPE_STRING, "去年同期用電量差");
				ExcelUtil.createBlankCell(titlerow2, titleStyle, column);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, column, column++));
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
			int y = 5;
			for(RankVO rankVO : rankList) {
				XSSFRow row = sheet.createRow(++y);
				int x = 0;
				
				if (filter.contains("RecDate")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getRecDate());//日期
				}
				if (filter.contains("City")) {
					if(!city.equals(rankVO.getCity())) {
						city = rankVO.getCity();	
						cityStartMap.put(city, y);
					}
					cityEndMap.put(city, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, city);// 縣市
				}
				if (filter.contains("Dist")) {
					if(!dist.equals(rankVO.getDist())) {
						dist = rankVO.getDist();
						distStartMap.put(dist, y);
					}
					distEndMap.put(dist, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, dist);// 行政區
				}
				if (filter.contains("BankCode")) {
					if(!bankCode.equals(rankVO.getBankCode())) {
						bankCode = rankVO.getBankCode();	
						bankStartMap.put(bankCode, y);
					}
					bankEndMap.put(bankCode, y);	
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bankCode+rankVO.getBankName());// 分行代碼+分行名稱
				}
				if (filter.contains("PowerAccount")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getPowerAccount());// 電號
				}
				if (filter.contains("RatePlanDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getRateplanDesc());// 用電類型
				}
				if (filter.contains("AccountDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getAccountDesc());// 說明
				}
				if (filter.contains("CC")) {
					if(StringUtils.isNotBlank(rankVO.getCc())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getCc());// 契約容量
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 契約容量
					}
				}
				if (filter.contains("UsageDesc")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getUsageDesc());// 耗能分類
				}
				if (filter.contains("MeterName")) {
					ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, rankVO.getMeterName());// 電表名稱
				}		
				if (filter.contains("Area")) {
					if(StringUtils.isNotBlank(rankVO.getArea())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getArea());// 面積
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 面積
					}
				}
				if (filter.contains("People")) {
					if(StringUtils.isNotBlank(rankVO.getPeople())) {
						ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getPeople());// 員工數
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);// 員工數
					}
				}
				if (filter.contains("MCEC")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getMcecpk());// 尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getMcecsp());// 半尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getMcecsatsp());// 周六半尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getMcecop());// 離峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getMcecSum());// 總用電量
				}
				if (filter.contains("ECO5")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getEco5mcecpk());// 尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getEco5mcecsp());// 半尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getEco5mcecsatsp());// 周六半尖峰用電量			
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getEco5mcecop());// 離峰用電量
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getEco5mcecSum());// 總用電量
				}
				
				if("1".equals(bankInfVO.getRecType())) {//選擇月的時候，才有電費、平均單價
					BigDecimal price = ToolUtil.divide(rankVO.getTotalCharge(), rankVO.getPamcec(), 2);
					if("Meter".equals(bankInfVO.getMode()) && !"1".equals(rankVO.getUsageCode())) {
						BigDecimal usageCharge = price.multiply(rankVO.getMcecSum()).setScale(0, BigDecimal.ROUND_HALF_UP);					
						if (filter.contains("BaseCharge")) {
							ExcelUtil.createBlankCell(row, styleR, x++);
						}
						if (filter.contains("UsageCharge")) {
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, usageCharge);// 流動電費
						}
						if (filter.contains("OverCharge")) {
							ExcelUtil.createBlankCell(row, styleR, x++);
						}		
						if (filter.contains("TotalCharge")) {						
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, usageCharge);//總電費=流動電費				
						}
					}else {										
						if (filter.contains("BaseCharge")) {
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getBaseCharge());// 基本電費
						}
						if (filter.contains("UsageCharge")) {
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getUsageCharge());// 流動電費
						}
						if (filter.contains("OverCharge")) {
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getOverCharge());// 非約定電費
						}
						if (filter.contains("TotalCharge")) {						
							ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getTotalCharge());// 總電費
						}
					}
					
					if (filter.contains("Price")) {					
						if(price.compareTo(unitPriceKPI)>=0) {
							ExcelUtil.createCell(row, styleRR, x++, XSSFCell.CELL_TYPE_NUMERIC, price);// 平均單價
						}else {
							ExcelUtil.createCell(row, styleRG, x++, XSSFCell.CELL_TYPE_NUMERIC, price);// 平均單價
						}				
					}
				}
				if (filter.contains("EUI")) {
					BigDecimal eui = ToolUtil.divide(rankVO.getMcecSum(), rankVO.getArea(), 2);
					if(eui.compareTo(euiKPI)>=0) {
						ExcelUtil.createCell(row, styleRR, x++, XSSFCell.CELL_TYPE_NUMERIC, eui);// EUI
					}else {
						ExcelUtil.createCell(row, styleRG, x++, XSSFCell.CELL_TYPE_NUMERIC, eui);// EUI
					}				
				}
				if (filter.contains("EPUI")) {
					BigDecimal epui = ToolUtil.divide(rankVO.getMcecSum(), rankVO.getPeople(), 2);
					if(epui.compareTo(epuiKPI)>=0) {
						ExcelUtil.createCell(row, styleRR, x++, XSSFCell.CELL_TYPE_NUMERIC, epui);// EPUI
					}else {
						ExcelUtil.createCell(row, styleRG, x++, XSSFCell.CELL_TYPE_NUMERIC, epui);// EPUI	
					}
				}
				if (filter.contains("Air")) {
					BigDecimal air = ToolUtil.divide(rankVO.getAir(), rankVO.getMcecSum(), 2).multiply(new BigDecimal(100));
					if("1".equals(rankVO.getUsageCode())) {
						if(air.compareTo(airKPI)>=0) {
							ExcelUtil.createCell(row, styleRR, x++, XSSFCell.CELL_TYPE_NUMERIC, air);// 主要空調
						}else {
							ExcelUtil.createCell(row, styleRG, x++, XSSFCell.CELL_TYPE_NUMERIC, air);// 主要空調
						}						
					}else {
						ExcelUtil.createBlankCell(row, styleR, x++);
					}
				}
				if (filter.contains("MDemand")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, rankVO.getmDemand());// 最大需量
				}
				if (filter.contains("MDemandP")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.divide(rankVO.getmDemand(), rankVO.getCc(), 2).multiply(new BigDecimal(100)));// 最大需量百分比
				}
				if (filter.contains("Last")) {
					ExcelUtil.createCell(row, styleR, x++, XSSFCell.CELL_TYPE_NUMERIC, ToolUtil.subtract(rankVO.getMcecSum(), rankVO.getLast()));// 去年同期用電量差
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
				
			String fileName = "用電排行"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
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
