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

/**
 * Servlet implementation class getBankRank 分行排行
 */
@WebServlet("/getBankRank")
public class getBankRank extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBankRank.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBankRank() {
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
		logger.debug("getBankRank start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);
			logger.debug("type:" + type);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(type)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BankInfVO bankInfVO = new BankInfVO();
					bankInfVO.setGroupType("bank");
					bankInfVO.setType(type);
					if("W".equals(type)) {
						bankInfVO.setFilter("BankCode,CC,W,DF");
					}else if("Air".equals(type)) {
						bankInfVO.setFilter("BankCode,CEC,UsageDesc");
						bankInfVO.setUsageCodeArr("1,2");
					}
					
					
					BankInfDAO bankInfDAO = new BankInfDAO();
					List<DynaBean> list = new ArrayList<DynaBean>();
					if("Over".equals(type)) {
						list = bankInfDAO.getCharge(bankInfVO);
					}else if("Price".equals(type) || "CEC".equals(type) ||"Charge".equals(type)) {
						list = bankInfDAO.getPrice(bankInfVO);
					}else if("EUI".equals(type) || "EPUI".equals(type)) {
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
		logger.debug("getBankRank end");
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
			JSONArray list = new JSONArray();
			JSONArray sortList = new JSONArray();
			if("W".equals(type)) {
				for (DynaBean bean : infoList) {
					JSONObject bank = new JSONObject();
					bank.put("BankCode", bean.get("bankcode"));// 分行代碼
					bank.put("BankName", bean.get("bankname"));// 分行名稱				
					bank.put("Value", ToolUtil.divide(bean.get("df"), bean.get("cc"), 2).multiply(new BigDecimal(100)));// 需量預測百分比
				
					list.put(bank);
				}	
			}else if("Air".equals(type)) {
				Map<String, BigDecimal> totalMap = new HashMap<String, BigDecimal>();
				Map<String, BigDecimal> mainMap = new HashMap<String, BigDecimal>();
				Map<String, String> bankMap = new HashMap<String, String>();	
				
				for (DynaBean bean : infoList) {
					if(!bankMap.containsKey(ObjectUtils.toString(bean.get("bankcode")))) {
						bankMap.put(ObjectUtils.toString(bean.get("bankcode")), ObjectUtils.toString(bean.get("bankname")));
					}
					
					if ("1".equals(ObjectUtils.toString(bean.get("usagecode")))) {					
						totalMap.put(ObjectUtils.toString(bean.get("bankcode")), ToolUtil.getBigDecimal(bean.get("cecsum")));
					}else {
						mainMap.put(ObjectUtils.toString(bean.get("bankcode")), ToolUtil.getBigDecimal(bean.get("cecsum")));
					}
				}
							
				for(String key : totalMap.keySet()){   
					JSONObject bank = new JSONObject();
					BigDecimal cecT = new BigDecimal(0);
					BigDecimal cecM = new BigDecimal(0);
					bank.put("BankCode", key);// 分行代碼
					bank.put("BankName", bankMap.get(key));// 分行名稱
					if(totalMap.containsKey(key)) {
						cecT = totalMap.get(key);//總空調
					}
					
					if(mainMap.containsKey(key)) {
						cecM = mainMap.get(key);//主要空調
					}
					bank.put("Value", ToolUtil.divide(cecM, cecT, 2).multiply(new BigDecimal(100)));// 用電量總計百分比			
				
					list.put(bank);              
				}				
			}else if("EUI".equals(type)) {
				for (DynaBean bean : infoList) {
					if(!isMonthFirstDay &&
							ToolUtil.parseDouble(bean.get("mcecsp"))>0 &&
							ToolUtil.parseDouble(bean.get("mcecsatsp"))>0 && 
							ToolUtil.parseDouble(bean.get("mcecop"))>0) {
						JSONObject bank = new JSONObject();
						bank.put("BankCode", bean.get("bankcode"));// 分行代碼
						bank.put("BankName", bean.get("bankname"));// 分行名稱				
						bank.put("Value", ToolUtil.divide(bean.get("cecsum"), bean.get("area"), 2));// EUI
						
						list.put(bank);
					}
				}	
			}else if("EPUI".equals(type)) {
				for (DynaBean bean : infoList) {
					if(!isMonthFirstDay &&
							ToolUtil.parseDouble(bean.get("mcecsp"))>0 &&
							ToolUtil.parseDouble(bean.get("mcecsatsp"))>0 && 
							ToolUtil.parseDouble(bean.get("mcecop"))>0) {
						JSONObject bank = new JSONObject();
						bank.put("BankCode", bean.get("bankcode"));// 分行代碼
						bank.put("BankName", bean.get("bankname"));// 分行名稱				
						bank.put("Value", ToolUtil.divide(bean.get("cecsum"), bean.get("people"), 2));// EPUI
						
						list.put(bank);						
					}
				}	
			}else if("Price".equals(type) || "CEC".equals(type) || "Charge".equals(type)) {
				for (DynaBean bean : infoList) {
					JSONObject bank = new JSONObject();				
					bank.put("BankCode", bean.get("bankcode"));// 分行代碼
					bank.put("BankName", bean.get("bankname"));// 分行名稱				
					bank.put("Value", ToolUtil.divide(bean.get("totalchargelm"), bean.get("mceclm"), 2));// 目前單價
					
					list.put(bank);
				}			
			}else if("Over".equals(type)) {
				for (DynaBean bean : infoList) {
					JSONObject bank = new JSONObject();				
					bank.put("BankCode", bean.get("bankcode"));// 分行代碼
					bank.put("BankName", bean.get("bankname"));// 分行名稱					
					bank.put("Value", ToolUtil.divide(bean.get("mdemand"), bean.get("cc"), 2).multiply(new BigDecimal(100)));//需量預測百分比
					
					list.put(bank);
				}			
			}
			sortList = getTopFive(sortJsonArray(list, "Value", true));
			data.put("Bank", sortList);			
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

	private JSONArray getTopFive(JSONArray oriArray) {
		JSONArray newArray = new JSONArray();
		int count = oriArray.length()<=5?oriArray.length():5;
		
		for(int i=0; i<count; i++) {
			newArray.put(oriArray.get(i));
		}

		return newArray;
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
}
