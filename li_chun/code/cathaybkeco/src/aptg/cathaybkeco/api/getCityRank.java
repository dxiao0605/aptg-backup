package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;
import aptg.cathaybkeco.vo.CityRankVO;
import aptg.cathaybkeco.vo.CityVO;

/**
 * Servlet implementation class getCityRank 縣市排行
 */
@WebServlet("/getCityRank")
public class getCityRank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCityRank.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCityRank() {
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
		logger.debug("getCityRank start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);
			logger.debug("type:" + type);//W:功率, Air:空調, CEC:用電量, EUI:EUI/EPUI, 	Charge:電費,	Price:單價, Over:超約附加費
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(type)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BankInfVO bankInfVO = new BankInfVO();		
					bankInfVO.setType(type);
					if("W".equals(type)) {
						bankInfVO.setFilter("cityGroup,CC,W,DF");
					}else if("Air".equals(type)) {
						bankInfVO.setFilter("cityGroup,W,CEC,UsageDesc");
						bankInfVO.setUsageCodeArr("1,2");
					}else if("CEC".equals(type)) {
						bankInfVO.setFilter("cityGroup,ECO5");					
					}
					
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> list = new ArrayList<DynaBean>();
					if("Charge".equals(type)||"Over".equals(type)) {
						list = bankInfDAO.getCharge(bankInfVO);
					}else if("Price".equals(type)) {
						list = bankInfDAO.getPrice(bankInfVO);
					}else if("EUI".equals(type)) {
						list = bankInfDAO.getEuiAndEpui(bankInfVO);
					}else {
						list = bankInfDAO.getElectricityInfo(bankInfVO);
					}
								
					rspJson.put("code", "00");
					if (list != null && list.size() > 0) {
						rspJson.put("msg", convertToJson(list, type));
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
		logger.debug("getCityRank end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> infoList, String type) throws Exception {
		JSONObject data = new JSONObject();
		try {
			boolean isMonthFirstDay = ToolUtil.isMonthFirstDay();
			JSONObject total = new JSONObject();
			JSONArray list = new JSONArray();
			JSONArray sortList = new JSONArray();
			if("W".equals(type)) {
				BigDecimal ccSum = new BigDecimal(0);
				BigDecimal wSum = new BigDecimal(0);
				BigDecimal mDemandSum = new BigDecimal(0);
				for (DynaBean bean : infoList) {
					JSONObject city = new JSONObject();
					BigDecimal cc = ToolUtil.getBigDecimal(bean.get("cc"));
					BigDecimal w = ToolUtil.getBigDecimal(bean.get("w"), 1);
					BigDecimal mDemand = ToolUtil.getBigDecimal(bean.get("df"));
					ccSum = ccSum.add(cc);
					wSum = wSum.add(w);
					mDemandSum = mDemandSum.add(mDemand);
					city.put("CityGroup", bean.get("citygroup"));
					city.put("CC", cc);// 契約容量
					city.put("W", w);// 功率	
					city.put("MDemand", mDemand);// 當月最大需量
					city.put("DemandP", ToolUtil.divide(mDemand, cc, 2).multiply(new BigDecimal(100)));// 需量預測百分比
					list.put(city);
				}				
				total.put("CityGroup", "全區");
				total.put("CC", ccSum);// 契約容量
				total.put("W", wSum);// 功率
				total.put("MDemand", mDemandSum);// 當月最大需量
				total.put("DemandP", ToolUtil.divide(mDemandSum, ccSum, 2).multiply(new BigDecimal(100)));// 需量預測百分比
				
				sortList = sortJsonArray(list, "DemandP", true);
			}else if("Air".equals(type)) {
				Map<String, CityVO> totalMap = new HashMap<String, CityVO>();
				Map<String, CityVO> mainMap = new HashMap<String, CityVO>();
				BigDecimal wTSum = new BigDecimal(0);
				BigDecimal cecTSum = new BigDecimal(0);
				BigDecimal wMSum = new BigDecimal(0);				
				BigDecimal cecMSum = new BigDecimal(0);
				
				for (DynaBean bean : infoList) {
					if ("1".equals(ObjectUtils.toString(bean.get("usagecode")))) {					
						CityVO cityVO = new CityVO();
						cityVO.setW(ToolUtil.getBigDecimal(bean.get("w"), 1));
						cityVO.setCec(ToolUtil.getBigDecimal(bean.get("cecsum")));
						wTSum = wTSum.add(cityVO.getW());
						cecTSum = cecTSum.add(cityVO.getCec());
						totalMap.put(bean.get("citygroup").toString(), cityVO);
					}else {
						CityVO cityVO = new CityVO();
						cityVO.setW(ToolUtil.getBigDecimal(bean.get("w"), 1));
						cityVO.setCec(ToolUtil.getBigDecimal(bean.get("cecsum")));
						wMSum = wMSum.add(cityVO.getW());
						cecMSum = cecMSum.add(cityVO.getCec());
						mainMap.put(bean.get("citygroup").toString(), cityVO);
					}
				}
							
				for(String key : totalMap.keySet()){   
					JSONObject city = new JSONObject();
					BigDecimal wT = new BigDecimal(0);
					BigDecimal cecT = new BigDecimal(0);
					BigDecimal wM = new BigDecimal(0);				
					BigDecimal cecM = new BigDecimal(0);
					city.put("CityGroup", key);
					if(totalMap.containsKey(key)) {
						CityVO totalVO = totalMap.get(key);//總空調
						wT = totalVO.getW();
						cecT = totalVO.getCec();
					}
					
					if(mainMap.containsKey(key)) {
						CityVO mainVO = mainMap.get(key);//主要空調
						wM = mainVO.getW();
						cecM = mainVO.getCec();
					}
					city.put("WT", wT);// 功率
					city.put("CECT", cecT);// 用電量總計
					city.put("WM", wM);// 功率
					city.put("CECM", cecM);// 用電量總計	
					city.put("WP", ToolUtil.divide(wM, wT, 2).multiply(new BigDecimal(100)));// 功率百分比
					city.put("CECP", ToolUtil.divide(cecM, cecT, 2).multiply(new BigDecimal(100)));// 用電量總計百分比
					list.put(city);              
				}	
				
				total.put("CityGroup", "全區");
				total.put("WT", wTSum);// 功率
				total.put("CECT", cecTSum);// 用電量總計					
				total.put("WM", wMSum);// 功率
				total.put("CECM", cecMSum);// 用電量總計					
				total.put("WP", ToolUtil.divide(wMSum, wTSum, 2).multiply(new BigDecimal(100)));// 功率百分比
				total.put("CECP", ToolUtil.divide(cecMSum, cecTSum, 2).multiply(new BigDecimal(100)));// 用電量總計百分比			
				sortList = sortJsonArray(list, "WP", true);
			}else if("CEC".equals(type)) {
				BigDecimal cecpk = new BigDecimal(0);
				BigDecimal cecsp = new BigDecimal(0);
				BigDecimal cecsatsp = new BigDecimal(0);
				BigDecimal cecop = new BigDecimal(0);
				BigDecimal cecSum = new BigDecimal(0);
				for (DynaBean bean : infoList) {
					JSONObject city = new JSONObject();
					cecpk = ToolUtil.add(cecpk, bean.get("mcecpk"));
					cecsp = ToolUtil.add(cecsp, bean.get("mcecsp"));
					cecsatsp = ToolUtil.add(cecsatsp, bean.get("mcecsatsp"));
					cecop = ToolUtil.add(cecop, bean.get("mcecop"));
					cecSum = ToolUtil.add(cecSum, bean.get("cecsum"));
					city.put("CityGroup", bean.get("citygroup"));
					city.put("CECPK", ToolUtil.getBigDecimal(bean.get("mcecpk")));// 尖峰用電量
					city.put("CECSP", ToolUtil.getBigDecimal(bean.get("mcecsp")));// 半尖峰用電量
					city.put("CECSatSP", ToolUtil.getBigDecimal(bean.get("mcecsatsp")));// 周六半尖峰用電量
					city.put("CECOP", ToolUtil.getBigDecimal(bean.get("mcecop")));// 離峰用電量
					city.put("CECSum", bean.get("cecsum"));// 用電量總計
					list.put(city);
				}		
				
				total.put("CityGroup", "全區");
				total.put("CECPK", cecpk);// 尖峰用電量
				total.put("CECSP", cecsp);// 半尖峰用電量
				total.put("CECSatSP", cecsatsp);// 周六半尖峰用電量
				total.put("CECOP", cecop);// 離峰用電量
				total.put("CECSum", cecSum);// 用電量總計
				
				sortList = sortJsonArray(list, "CECSum", true);
			}else if("EUI".equals(type)) {
				BigDecimal areaSum = BigDecimal.ZERO;
				BigDecimal peopleSum = BigDecimal.ZERO;
				BigDecimal cecSum = BigDecimal.ZERO;
				boolean totalIsNotNull = false;
				Map<String, CityRankVO> cityGroupMap = new HashMap<String, CityRankVO>();
				CityRankVO cityRankVO = null;
				for (DynaBean bean : infoList) {
					boolean isNotNull = false;
					if(ToolUtil.parseDouble(bean.get("mcecsp"))>0 &&
							ToolUtil.parseDouble(bean.get("mcecsatsp"))>0 && 
							ToolUtil.parseDouble(bean.get("mcecop"))>0) {
						totalIsNotNull = true;
						isNotNull = true;						
					}
					String citygroup = ObjectUtils.toString(bean.get("citygroup"));
					if(cityGroupMap.containsKey(citygroup)) {
						cityRankVO = cityGroupMap.get(citygroup);
						cityRankVO.setArea(cityRankVO.getArea().add(ToolUtil.getBigDecimal(bean.get("area"))));
						cityRankVO.setPeople(cityRankVO.getPeople().add(ToolUtil.getBigDecimal(bean.get("people"))));
						cityRankVO.setMcecpk(cityRankVO.getMcecpk().add(ToolUtil.getBigDecimal(bean.get("mcecpk"))));
						cityRankVO.setMcecsp(cityRankVO.getMcecsp().add(ToolUtil.getBigDecimal(bean.get("mcecsp"))));
						cityRankVO.setMcecsatsp(cityRankVO.getMcecsatsp().add(ToolUtil.getBigDecimal(bean.get("mcecsatsp"))));
						cityRankVO.setMcecop(cityRankVO.getMcecop().add(ToolUtil.getBigDecimal(bean.get("mcecop"))));
						if(isNotNull)
							cityRankVO.setCecSum(cityRankVO.getCecSum().add(ToolUtil.getBigDecimal(bean.get("cecsum"))));
					}else {
						cityRankVO = new CityRankVO();
						cityRankVO.setArea(ToolUtil.getBigDecimal(bean.get("area")));
						cityRankVO.setPeople(ToolUtil.getBigDecimal(bean.get("people")));
						cityRankVO.setMcecpk(ToolUtil.getBigDecimal(bean.get("mcecpk")));
						cityRankVO.setMcecsp(ToolUtil.getBigDecimal(bean.get("mcecsp")));
						cityRankVO.setMcecsatsp(ToolUtil.getBigDecimal(bean.get("mcecsatsp")));
						cityRankVO.setMcecop(ToolUtil.getBigDecimal(bean.get("mcecop")));
						if(isNotNull) {
							cityRankVO.setCecSum(ToolUtil.getBigDecimal(bean.get("cecsum")));
						}
						cityGroupMap.put(citygroup, cityRankVO);
					}
					areaSum = ToolUtil.add(areaSum, bean.get("area"));
					peopleSum = ToolUtil.add(peopleSum, bean.get("people"));
					if(isNotNull)
						cecSum = cecSum.add(ToolUtil.getBigDecimal(bean.get("cecsum")));					
				}
				
				for(String citygroup : cityGroupMap.keySet()) {
					cityRankVO = cityGroupMap.get(citygroup);
					JSONObject city = new JSONObject();
					city.put("CityGroup", citygroup);
					city.put("Area", cityRankVO.getArea());// 面積
					city.put("People", cityRankVO.getPeople());// 員工數
					
					if(!isMonthFirstDay && 
							cityRankVO.getMcecsp().compareTo(BigDecimal.ZERO)>0 &&
							cityRankVO.getMcecsatsp().compareTo(BigDecimal.ZERO)>0 && 
							cityRankVO.getMcecop().compareTo(BigDecimal.ZERO)>0) {	
						city.put("EUI", ToolUtil.divide(cityRankVO.getCecSum(),  cityRankVO.getArea(), 2));// EUI
						city.put("EUIHidden", ToolUtil.divide(cityRankVO.getCecSum(),  cityRankVO.getArea(), 2));// EUI(隱藏欄位，排序用)
						city.put("EPUI", ToolUtil.divide(cityRankVO.getCecSum(),  cityRankVO.getPeople(), 2));// EPUI
					}else {
						city.put("EUI", "--");// EUI
						city.put("EUIHidden", new BigDecimal(-99));// EUI
						city.put("EPUI", "--");// EPUI
					}					
					list.put(city);
				}
				
				total.put("CityGroup", "全區");
				total.put("Area", areaSum);// 面積
				total.put("People", peopleSum);// 員工數
				if(isMonthFirstDay || !totalIsNotNull) {
					total.put("EUI", "--");// EUI
					total.put("EPUI", "--");// EPUI					
				}else {
					total.put("EUI", ToolUtil.divide(cecSum, areaSum, 2));// EUI
					total.put("EPUI", ToolUtil.divide(cecSum, peopleSum, 2));// EPUI		
				}
				sortList = sortJsonArray(list, "EUIHidden", true);
			}else if("Charge".equals(type)) {
				BigDecimal baseCharge = new BigDecimal(0);
				BigDecimal usageCharge = new BigDecimal(0);
				BigDecimal overCharge = new BigDecimal(0);
				BigDecimal totalCharge = new BigDecimal(0);
				for (DynaBean bean : infoList) {
					JSONObject city = new JSONObject();
					baseCharge = ToolUtil.add(baseCharge, bean.get("basecharge"));
					usageCharge = ToolUtil.add(usageCharge, bean.get("usagecharge"));
					overCharge = ToolUtil.add(overCharge, bean.get("overcharge"));
					totalCharge = ToolUtil.add(totalCharge, bean.get("totalcharge"));
					city.put("CityGroup", bean.get("citygroup"));
					city.put("BaseCharge", ToolUtil.getBigDecimal(bean.get("basecharge")));// 基本電費				
					city.put("UsageCharge", ToolUtil.getBigDecimal(bean.get("usagecharge")));// 流動電費
					city.put("OverCharge",ToolUtil.getBigDecimal(bean.get("overcharge")));// 非約定電費
					if(isMonthFirstDay) {
						city.put("TotalCharge","--");
					}else {
						city.put("TotalCharge",ToolUtil.getBigDecimal(bean.get("totalcharge")));// 總電費	
					}
					
					list.put(city);
				}			
				
				total.put("CityGroup", "全區");
				total.put("BaseCharge", baseCharge);// 基本電費				
				total.put("UsageCharge", usageCharge);// 流動電費
				total.put("OverCharge", overCharge);// 非約定電費
				if(isMonthFirstDay) {
					total.put("TotalCharge", "--");
					sortList = list;
				}else {
					total.put("TotalCharge", totalCharge);// 總電費
					sortList = sortJsonArray(list, "TotalCharge", true);
				}				
			}else if("Price".equals(type)) {
				BigDecimal mcec = new BigDecimal(0);
				BigDecimal totalCharge = new BigDecimal(0);
				BigDecimal mceclm = new BigDecimal(0);
				BigDecimal totalChargelm = new BigDecimal(0);
				BigDecimal mcecly = new BigDecimal(0);
				BigDecimal totalChargeLY = new BigDecimal(0);
				boolean totalIsNotNull = false;
				for (DynaBean bean : infoList) {
					boolean isNotNull = false;
					if(ToolUtil.parseDouble(bean.get("mcecsp"))>0 &&
							ToolUtil.parseDouble(bean.get("mcecsatsp"))>0 && 
							ToolUtil.parseDouble(bean.get("mcecop"))>0) {
						totalIsNotNull = true;
						isNotNull = true;						
					}
					JSONObject city = new JSONObject();
					mcec = ToolUtil.add(mcec, bean.get("fcstmcec"));
					totalCharge = ToolUtil.add(totalCharge, bean.get("fcsttotalcharge"));
					mceclm = ToolUtil.add(mceclm, bean.get("mceclm"));
					totalChargelm = ToolUtil.add(totalChargelm, bean.get("totalchargelm"));
					mcecly = ToolUtil.add(mcecly, bean.get("mcecly"));
					totalChargeLY = ToolUtil.add(totalChargeLY, bean.get("totalchargely"));
					city.put("CityGroup", bean.get("citygroup"));
					if(isMonthFirstDay || !isNotNull) {
						city.put("MCEC", "--");				
						city.put("TotalCharge", "--");
						city.put("Price", "--");// 目前單價
						city.put("PriceHidden", new BigDecimal(-99));// 目前單價
					}else {
						city.put("MCEC", ToolUtil.getBigDecimal(bean.get("fcstmcec")));// 預測用電量				
						city.put("TotalCharge", ToolUtil.getBigDecimal(bean.get("fcsttotalcharge")));// 預測電費
						city.put("Price", ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2));// 目前單價
						city.put("PriceHidden", ToolUtil.divide(bean.get("fcsttotalcharge"), bean.get("fcstmcec"), 2));// 目前單價(隱藏欄位，排序用)
					}
					
					city.put("MCECLM",ToolUtil.getBigDecimal(bean.get("mceclm")));// 上月用電量
					city.put("TotalChargeLM",ToolUtil.getBigDecimal(bean.get("totalchargelm")));// 上月電費
					city.put("PriceLM", ToolUtil.divide(bean.get("totalchargelm"), bean.get("mceclm"), 2));// 上月單價
					city.put("MCECLY",ToolUtil.getBigDecimal(bean.get("mcecly")));// 去年用電量
					city.put("TotalChargeLY",ToolUtil.getBigDecimal(bean.get("totalchargely")));// 去年電費
					city.put("PriceLY", ToolUtil.divide(bean.get("totalchargely"), bean.get("mcecly"), 2));// 去年單價
					list.put(city);
				}			
				total.put("CityGroup", "全區");
				if(isMonthFirstDay || !totalIsNotNull) {
					total.put("MCEC", "--");				
					total.put("TotalCharge", "--");
					total.put("Price", "--");
				}else {
					total.put("MCEC", mcec);// 預測總用電量				
					total.put("TotalCharge", totalCharge);// 預測電費
					total.put("Price", ToolUtil.divide(totalCharge, mcec, 2));// 目前單價
				}			
				
				total.put("MCECLM", mceclm);// 上月用電量
				total.put("TotalChargeLM", totalChargelm);// 上月電費
				total.put("PriceLM", ToolUtil.divide(totalChargelm, mceclm, 2));// 上月單價
				total.put("MCECLY", mcecly);// 去年用電量
				total.put("TotalChargeLY", totalChargeLY);// 去年電費
				total.put("PriceLY", ToolUtil.divide(totalChargeLY, mcecly, 2));// 去年單價
				
				sortList = sortJsonArray(list, "PriceHidden", true);
			}else if("Over".equals(type)) {				
				BigDecimal overCharge = new BigDecimal(0);
				BigDecimal cc = new BigDecimal(0);
				BigDecimal mDemand = new BigDecimal(0);
				for (DynaBean bean : infoList) {
					JSONObject city = new JSONObject();
					city.put("CityGroup", bean.get("citygroup"));
					if(isMonthFirstDay) {
						city.put("OverCharge", "--");
						city.put("CC", "--");
						city.put("MDemand", "--");
						city.put("DemandP", "--");
					}else {
						overCharge = ToolUtil.add(overCharge, bean.get("overcharge"));
						cc = ToolUtil.add(cc, bean.get("cc"));
						mDemand = ToolUtil.add(mDemand, bean.get("mdemand"));											
						city.put("OverCharge",ToolUtil.getBigDecimal(bean.get("overcharge")));// 非約定電費
						city.put("CC",ToolUtil.getBigDecimal(bean.get("cc")));//契約容量
						city.put("MDemand", ToolUtil.getBigDecimal(bean.get("mdemand"), 0, BigDecimal.ROUND_DOWN));// 當月最大需量
						city.put("DemandP", ToolUtil.divide(bean.get("mdemand"), bean.get("cc"), 2).multiply(new BigDecimal(100)));// 需量預測百分比						
					}	
					list.put(city);
				}			
				
				total.put("CityGroup", "全區");
				if(isMonthFirstDay) {
					total.put("OverCharge", "--");
					total.put("CC", "--");
					total.put("MDemand", "--");
					total.put("DemandP", "--");				
					sortList = list;
				}else {
					total.put("OverCharge", overCharge);// 非約定電費
					total.put("CC", cc);// 契約容量
					total.put("MDemand", mDemand);// 當月最大需量
					total.put("DemandP", ToolUtil.divide(mDemand, cc, 2).multiply(new BigDecimal(100)));// 需量預測百分比				
					sortList = sortJsonArray(list, "OverCharge", true);
				}
			}
			data.put("Total", total);
			data.put("CityGroup", sortList);			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 排序JsonArray
	 * @param array
	 * @param key
	 * @param reversed
	 * @return
	 */
	private JSONArray sortJsonArray(JSONArray array, String key, boolean reversed) {
		List<JSONObject> sortRows = new ArrayList<JSONObject>();
		ArrayList<JSONObject> jsons = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			try {
				jsons.add(array.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
		if (reversed) {
			sortRows = jsons.stream().sorted(Comparator.comparing(new Function<JSONObject, BigDecimal>() {
				public BigDecimal apply(JSONObject bean) {
					return bean.getBigDecimal(key);
				}
			}).reversed()).collect(Collectors.toList());
		} else {
			sortRows = jsons.stream().sorted(Comparator.comparing(new Function<JSONObject, BigDecimal>() {
				public BigDecimal apply(JSONObject bean) {
					return bean.getBigDecimal(key);
				}
			})).collect(Collectors.toList());
		}
		return new JSONArray(sortRows);
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
