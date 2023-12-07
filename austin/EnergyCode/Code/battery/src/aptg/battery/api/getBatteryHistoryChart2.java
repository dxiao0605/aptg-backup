package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getBatteryHistoryChart 電池歷史圖表
 */
@WebServlet("/getBatteryHistoryChart2")
public class getBatteryHistoryChart2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryHistoryChart2.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryHistoryChart2() {
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
		logger.debug("getBatteryHistoryChart start");
		JSONObject rspJson = new JSONObject();
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
						if(ToolUtil.checkAdminCompany(userCompany))
							batteryVO.setCompanyCode(userCompany);										
						BatteryDAO batteryDAO = new BatteryDAO();
//						List<DynaBean> rows = batteryDAO.getBatteryHistoryChart(batteryVO);					
//						if (rows != null && !rows.isEmpty()) {							
//							rspJson.put("msg", convertToJson(rows, timezone, ToolUtil.getIMPType(userCompany)));
//							rspJson.put("code", "00");
//						} else {							
//							rspJson.put("msg", resource.getString("5004"));//查無資料
//							rspJson.put("code", "07");
//						}
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
		ToolUtil.response(rspJson.toString(), response);	
		logger.debug("getBatteryHistoryChart end");
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
			if (StringUtils.isNotBlank(json)) {	
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "RecTime")) {
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					JSONObject recTime = request.getJSONObject("RecTime");
					String radio = recTime.optString("Radio");
					if ("1".equals(radio)) {//radio選1，則帶入前端日期
						String start = recTime.optString("Start")+" "+recTime.optString("StartHH")+":"+recTime.optString("StartMM");
						String end = recTime.optString("End")+" "+recTime.optString("EndHH")+":"+recTime.optString("EndMM");

						if(ToolUtil.isNull(recTime, "Start") || ToolUtil.isNull(recTime, "StartHH") || ToolUtil.isNull(recTime, "StartMM") ||
								ToolUtil.isNull(recTime, "End") || ToolUtil.isNull(recTime, "EndHH") || ToolUtil.isNull(recTime, "EndMM")) {				
							batteryVO.setError(true);
							batteryVO.setCode("24");
							batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
							return batteryVO;
						}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
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
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	/**
	 * 組Json
	 * @param rows
	 * @param timezone
	 * @param impType
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String timezone, int impType) throws Exception {		
		JSONObject data = new JSONObject();
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
		try {		
			List<String> rectimeList = new ArrayList<String>();
			Map<String, JSONArray> irMap = new LinkedHashMap<String, JSONArray>();
			Map<String, JSONArray> volMap = new LinkedHashMap<String, JSONArray>();
			JSONArray rectimeArr = new JSONArray();
			JSONArray irArr = null, volArr = null;
			JSONArray temperatureArr = new JSONArray();
			BigDecimal ir=null, vol=null, tem=null;
			BigDecimal minIr = null, maxIr = null;
			BigDecimal minVol = null, maxVol = null;
			BigDecimal minTem = null, maxTem = null;
			String zone="";
			int num = 0;
			for(int i=0; i<rows.size(); i++) {			
				DynaBean bean = rows.get(i);
				if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
					zone = bean.get("timezone").toString();
					sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", zone);
				}
				
				String rectime = ToolUtil.dateFormat(bean.get("rectime"), sdf);
				if(!rectimeList.contains(rectime)) {
					rectimeArr.put(rectime);
					rectimeList.add(rectime);
					num++;
				}
				
				String category = ObjectUtils.toString(bean.get("category"));
				String orderNo = ObjectUtils.toString(bean.get("orderno"));
				if("1".equals(category)) {					
					if(impType==22) {//電導值
						ir = ToolUtil.divide(1000000, bean.get("value"), 3);
					}else if(impType==21) {//毫內阻值
						ir = ToolUtil.divide(bean.get("value"), 1000, 3);
					}else {//內阻值
						ir = ToolUtil.getBigDecimal(bean.get("value"), 0);
					}
					if(irMap.containsKey(orderNo)) {
						irArr = irMap.get(orderNo);
					}else {
						irArr = new JSONArray();						
						irMap.put(orderNo, irArr);						
					}
					if(num!=1 && num-1!=irArr.length()) {
						for(int j=irArr.length(); j<num-1; j++) {
							irArr.put(JSONObject.NULL);
						}
					}
					irArr.put(ir);
					if(minIr==null) {
						minIr = ir;
						maxIr = ir;
					}
					if(ir.compareTo(maxIr)>0) {
						maxIr = ir;
					}
					if(ir.compareTo(minIr)<0) {
						minIr = ir;
					}						
				}else if("2".equals(category)) {
					if(volMap.containsKey(orderNo)) {
						volArr = volMap.get(orderNo);
					}else {
						volArr = new JSONArray();						
						volMap.put(orderNo, volArr);
					}
					if(num!=1 && num-1!=volArr.length()) {
						for(int j=volArr.length(); j<num-1; j++) {
							volArr.put(JSONObject.NULL);
						}
					}
					vol = ToolUtil.divide(bean.get("value"), 1000, 2);
					volArr.put(vol);
					if(minVol==null) {
						minVol = vol;
						maxVol = vol;
					}
					if(vol.compareTo(maxVol)>0) {
						maxVol = vol;
					}
					if(vol.compareTo(minVol)<0) {
						minVol = vol;
					}
				}else {
					tem = ToolUtil.getBigDecimal(bean.get("value"));
					temperatureArr.put(tem);//溫度
					if(minTem==null) {
						minTem = tem;
						maxTem = tem;
					}
					if(tem.compareTo(maxTem)>0) {
						maxTem = tem;
					}
					if(tem.compareTo(minTem)<0) {
						minTem = tem;
					}
				}				
			}
			
			irArr = new JSONArray();			
			for(String key : irMap.keySet()){
				irArr.put(irMap.get(key));
			}
			
			volArr = new JSONArray();
			for(String key : volMap.keySet()){
				volArr.put(volMap.get(key));
			}
			
			data.put("RecTime", rectimeArr);
			data.put("IR", irArr);
			data.put("Vol", volArr);
			data.put("Temperature", temperatureArr);
			data.put("IMPType", impType);
			data.put("MaxIR", maxIr);
			data.put("MinIR", minIr);
			data.put("MaxVol", maxVol);
			data.put("MinVol", minVol);
			data.put("MaxTemperature", maxTem);
			data.put("MinTemperature", minTem);
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
