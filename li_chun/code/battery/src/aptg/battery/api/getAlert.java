package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.json.JSONObject;

import aptg.battery.bean.AlertBean;
import aptg.battery.bean.AlertDataBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.EventDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.EventVO;



/**
 * Servlet implementation class getAlert 告警資訊
 */
@WebServlet("/getAlert")
public class getAlert extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getAlert.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAlert() {
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
		logger.debug("getAlert start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			String eventStatus = ObjectUtils.toString(request.getParameter("eventStatus"));
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);				
					EventVO eventVO = this.parseJson(req, timezone, resource);
					boolean isAdmin = true;
					if (ToolUtil.checkAdminCompany(userCompany)) {
						eventVO.setCompanyCode(userCompany);
						isAdmin = false;
					}
						
					eventVO.setEventStatus(eventStatus);
					EventDAO eventDAO = new EventDAO();
					List<DynaBean> list = eventDAO.getAlertInfo(eventVO);					
					if (list != null && !list.isEmpty()) {
						AlertDataBean alertDataBean = this.processData(list, ToolUtil.getIMPType(userCompany), language, timezone);
						if ("csv".equals(eventVO.getType())) {
							composeCSV(alertDataBean, eventStatus, timezone, language, isAdmin, response);
							rep = false;
						} else if("check".equals(eventVO.getType())) {
							if("5".equals(eventStatus)) {
								rspJson.put("msg", "Unresolved"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");	
							}else {
								rspJson.put("msg", "Resolved"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
							}							
						} else {
							rspJson.put("msg", convertToJson(alertDataBean));
						}
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));//查無資料
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
		logger.debug("getAlert end");
	}
	
	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return EventVO
	 * @throws Exception
	 */
	private EventVO parseJson(String json, String timezone, ResourceBundle resource) throws Exception {
		EventVO eventVO = new EventVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					eventVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "Country")) {
					JSONObject country = request.getJSONObject("Country");
					eventVO.setCountryArr(ToolUtil.jsonArrToSqlStr(country.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Area")) {
					JSONObject area = request.getJSONObject("Area");
					eventVO.setAreaArr(ToolUtil.jsonArrToSqlStr(area.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "GroupID")) {
					JSONObject groupId = request.getJSONObject("GroupID");
					eventVO.setGroupIdArr(ToolUtil.jsonArrToSqlStr(groupId.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Alert")) {
					JSONObject eventType = request.getJSONObject("Alert");	
					eventVO.setEventTypeArr(ToolUtil.jsonArrToSqlStr(eventType.optJSONArray("List")));
				}
				if (!ToolUtil.isNull(request, "EventStatus")) {
					eventVO.setEventStatus(request.optString("EventStatus"));
				}
				
				if (!ToolUtil.isNull(request, "BattInternalId")) {
					eventVO.setBattInternalId(request.optString("BattInternalId"));	
				}
				
				SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if (!ToolUtil.isNull(request, "RecTime")) {					
					JSONObject recTime = request.getJSONObject("RecTime");
					String radio = recTime.optString("Radio");
					if ("1".equals(radio)) {//radio選1，則帶入前端日期
						String start = recTime.optString("Start")+" "+recTime.optString("StartHH")+":"+recTime.optString("StartMM");
						String end = recTime.optString("End")+" "+recTime.optString("EndHH")+":"+recTime.optString("EndMM");

						if(ToolUtil.isNull(recTime, "Start") || ToolUtil.isNull(recTime, "StartHH") || ToolUtil.isNull(recTime, "StartMM") ||
								ToolUtil.isNull(recTime, "End") || ToolUtil.isNull(recTime, "EndHH") || ToolUtil.isNull(recTime, "EndMM")) {				
							eventVO.setError(true);
							eventVO.setCode("24");
							eventVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
							return eventVO;
						}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
							eventVO.setError(true);
							eventVO.setCode("16");
							eventVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
							return eventVO;
						}
						
						eventVO.setStartDate(sdf2.format(sdf.parse(start)));
						eventVO.setEndDate(sdf2.format(sdf.parse(end)));		
					}else if ("3".equals(radio)) {//預設1個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -1);
						eventVO.setStartDate(sdf2.format(cal.getTime()));
					}else if ("4".equals(radio)) {//預設3個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -3);
						eventVO.setStartDate(sdf2.format(cal.getTime()));
					}						
				}
				
				eventVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return eventVO;
	}
	
	private AlertDataBean processData(List<DynaBean> rows, int impType, String language, String timezone) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		SimpleDateFormat date = ToolUtil.getDateFormat("yyyy/MM/dd Z", timezone);
		ResourceBundle resource = ToolUtil.getLanguage(language);
		AlertDataBean data = new AlertDataBean();
		try {
			BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
			List<AlertBean> alertList = new ArrayList<AlertBean>();
			Map<Integer, AlertBean> alertMap = new HashMap<Integer, AlertBean>();
			AlertBean alertBean = null;
			String zone="";
			String irunit = resource.getString(String.valueOf(impType));//內阻單位
			
			BigDecimal newAlert1, newAlert2, alert1, alert2;
			for(int i=0; i<rows.size(); i++) {							
				DynaBean bean = rows.get(i);
				if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
					zone = bean.get("timezone").toString();
					sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", zone);
				}
				
				int eventSeq = ToolUtil.parseInt(bean.get("seqno"));
		
				if(alertMap.containsKey(eventSeq)) {
					alertBean = alertMap.get(eventSeq);					
				}else {				
					alertBean = new AlertBean();
					alertBean.setEventSeq(eventSeq);//告警序號
					String eventtype = ObjectUtils.toString(bean.get("eventtype"));	
					String eventstatus = ObjectUtils.toString(bean.get("eventstatus"));
					alertBean.setEventType(resource.getString(eventtype));//告警類型說明
					alertBean.setEventTypeCode(eventtype);//告警類型代碼
					alertBean.setCompany(ObjectUtils.toString(bean.get("companyname")));
					alertBean.setCountry(ObjectUtils.toString(bean.get("country")));//國家
					alertBean.setArea(ObjectUtils.toString(bean.get("area")));//地域
					alertBean.setGroupID(ObjectUtils.toString(bean.get("groupid")));//站台號碼
					alertBean.setGroupName(ObjectUtils.toString(bean.get("groupname")));//台名稱
					alertBean.setAddress(ObjectUtils.toString(bean.get("address")));//地址
					alertBean.setBatteryGroupID(ObjectUtils.toString(bean.get("nbid"))+"_"+ObjectUtils.toString(bean.get("batteryid")));					
					alertBean.setInstallDate(ToolUtil.dateFormat(bean.get("installdate"), date));//安裝日期				
					alertBean.setBatteryType(ObjectUtils.toString(bean.get("batterytypename")));//電池型號
					alertBean.setRecTime(ToolUtil.dateFormat(bean.get("rectime"), sdf));//數據更新時間	
					alertBean.setOccurTime(ToolUtil.dateFormat(bean.get("createtime"), sdf));//發生時間
					alertBean.setCloseTime(ToolUtil.dateFormat(bean.get("closetime"), sdf));//解決時間
					alertBean.setCloseUser(ObjectUtils.toString(bean.get("closeuser")));//解決人員
					alertBean.setCloseContent(ObjectUtils.toString(bean.get("closecontent")));//解決方案					
					alertBean.setEventStatus(eventstatus);//告警狀態
					if("5".equals(eventstatus)) {
						alertBean.setEventStatusDesc("Unresolved");
					}else {
						alertBean.setEventStatusDesc("Resolved");
					}	
					
					
					//---------------判定值切換
					String recImpType = ObjectUtils.toString(bean.get("imptype"));//20: 內阻值 21:毫內阻 22:電導值 
					newAlert1 = BigDecimal.ZERO;
					newAlert2 = BigDecimal.ZERO;
					alert1 = ToolUtil.getBigDecimal(bean.get("alert1"));
					alert2 = ToolUtil.getBigDecimal(bean.get("alert2"));							
					if("20".equals(recImpType) && impType==21) {
						//內阻 > 毫內阻
						newAlert1 = ToolUtil.irToMir(alert1);
						newAlert2 = ToolUtil.irToMir(alert2);
					}else if("21".equals(recImpType) && impType==20) {
						//毫內阻 > 內阻
						newAlert1 = ToolUtil.mirToIr(alert1);
						newAlert2 = ToolUtil.mirToIr(alert2);
					}else if("20".equals(recImpType) && impType==22) {
						//內阻 > 電導值
						newAlert1 = ToolUtil.irToS(alert1);
						newAlert2 = ToolUtil.irToS(alert2);
					}else if("22".equals(recImpType) && impType==20) {
						//電導值 > 內阻
						newAlert1 = ToolUtil.sToIr(alert1);
						newAlert2 = ToolUtil.sToIr(alert2);
					}else if("21".equals(recImpType) && impType==22) {
						//毫內阻 > 電導值
						newAlert1 = ToolUtil.mirToS(alert1);
						newAlert2 = ToolUtil.mirToS(alert2);
					}else if("22".equals(recImpType) && impType==21) {
						//電導值 > 毫內阻
						newAlert1 = ToolUtil.sToMir(alert1);
						newAlert2 = ToolUtil.sToMir(alert2);
					}else {
						newAlert1 = alert1;
						newAlert2 = alert2;
					}
					//---------------	
					
					if(impType==20) {//內阻值
						alertBean.setAlert1(ToolUtil.getBigDecimal(newAlert1, 0)+" "+irunit);//判定值1
						alertBean.setAlert2(ToolUtil.getBigDecimal(newAlert2, 0)+" "+irunit);//判定值2
					}else {//毫內阻值or電導值
						alertBean.setAlert1(ToolUtil.getBigDecimal(newAlert1, 3)+" "+irunit);//判定值1
						alertBean.setAlert2(ToolUtil.getBigDecimal(newAlert2, 3)+" "+irunit);//判定值2
					}
					alertBean.setDisconnect(ToolUtil.divide(bean.get("disconnect"), 3600, 0));
					alertBean.setTemperature1(ToolUtil.getBigDecimal(bean.get("temperature1"), 2));
					alertBean.setLng(ToolUtil.getBigDecimal(bean.get("lng")));//經度
					alertBean.setLat(ToolUtil.getBigDecimal(bean.get("lat")));//緯度
					alertBean.setIR(new ArrayList<String>());
					alertBean.setVol(new ArrayList<String>());
					alertBean.setStatus(new ArrayList<BigDecimal>());
					alertMap.put(eventSeq, alertBean);
					alertList.add(alertBean);		
				}
				
				String category = ObjectUtils.toString(bean.get("category"));
				if("1".equals(category)) {
					if(ToolUtil.getBigDecimal(bean.get("value"), 0).compareTo(ol)>=0) {
						alertBean.getIR().add("CH"+bean.get("orderno")+" OL");
					}else {
						if(impType==22) {//電導值
							alertBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(1000000, bean.get("value"), 3));
						}else if(impType==21) {//毫內阻值
							alertBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(bean.get("value"), 1000, 3));
						}else {//內阻值
							alertBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.getBigDecimal(bean.get("value"), 0));
						}	
					}
					alertBean.getStatus().add(ToolUtil.getBigDecimal(bean.get("status"), 0));
				}else if("2".equals(category)) {
					alertBean.getVol().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(bean.get("value"), 1000, 2));//電壓
				}else {
					alertBean.setTemperature(ToolUtil.getBigDecimal(bean.get("value")));//溫度
				}
			}
			data.setAlert(alertList);
			data.setIMPType(impType);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組Json
	 * @param alertDataBean
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(AlertDataBean alertDataBean) throws Exception {		
		return new JSONObject(JsonUtil.getInstance().convertObjectToJsonstring(alertDataBean));
	}
	
	/**組CSV
	 * 
	 * @param alertDataBean
	 * @param eventStatus
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(AlertDataBean alertDataBean, String eventStatus, String timezone, String language, boolean isAdmin, HttpServletResponse response) throws Exception {
		try {			
			ResourceBundle resource = ToolUtil.getLanguage(language);	
			StringBuilder str = new StringBuilder();
			List<AlertBean> alertList = alertDataBean.getAlert();
			for(int i=0; i<alertList.size(); i++) {							
				AlertBean alertBean = alertList.get(i);	
				if(i==0) {
					str.append(resource.getString("1304")).append(",");//告警類型
					if(isAdmin)	
						str.append(resource.getString("1064")).append(",");// 公司
					str.append(resource.getString("1028")).append(",")//國家
					   .append(resource.getString("1029")).append(",")//地域
					   .append(resource.getString("1012")).append(",")//站台編號
					   .append(resource.getString("1013")).append(",")//站台名稱
					   .append(resource.getString("1026")).append(",")//電池組ID
					   .append(resource.getString("1027")).append(",")//安裝日期
					   .append(resource.getString("1030")).append(",")//型號
					   .append(resource.getString("1036")).append(",");//數據時間					   
					if(alertDataBean.getIMPType()==22) {//電導值
						str.append(resource.getString("1020")).append(",");
					}else if(alertDataBean.getIMPType()==21) {//毫內阻值
						str.append(resource.getString("1019")).append(",");
					}else {//內阻值
						str.append(resource.getString("1016")).append(",");
					}			
					str.append(resource.getString("1017")).append(",");//電壓	
					   	
					   
					if("5".equals(eventStatus)) {
						str.append(resource.getString("1018")).append("\n");//溫度
					}else {
						str.append(resource.getString("1018")).append(",");//溫度
						str.append(resource.getString("1312")).append("\n");//解決時間
					}					   
				}
				List<String> irlList = alertBean.getIR();
				List<String> volList = alertBean.getVol();
				int count = irlList.size()>volList.size()?irlList.size():volList.size();				
				for(int j=0; j<count; j++) {
					if(j==0) {
						str.append(resource.getString(alertBean.getEventTypeCode())).append(",");
						if(isAdmin)
							str.append(alertBean.getCompany()).append(",");	
						str.append(alertBean.getCountry()).append(",")
						   .append(alertBean.getArea()).append(",")
						   .append(alertBean.getGroupID()).append(",")
						   .append(alertBean.getGroupName()).append(",")				
						   .append(alertBean.getBatteryGroupID()).append(",")
						   .append(alertBean.getInstallDate()).append(",")
			     		   .append(alertBean.getBatteryType()).append(",")			
						   .append(alertBean.getRecTime()).append(",");
						
					}else {
						str.append("").append(",");
						if(isAdmin)	
							str.append("").append(",");
						str.append("").append(",").append("").append(",").append("").append(",")
						   .append("").append(",").append("").append(",").append("").append(",")
						   .append("").append(",").append("").append(",");
					}

					if(j>=irlList.size()) {
						str.append("").append(",");
					}else {
						str.append(alertBean.getIR().get(j)).append(",");
					}
					if(j>=volList.size()) {
						str.append("").append(",");
					}else {
						str.append(alertBean.getVol().get(j)).append(",");
					}
					
					if(j==0) {
						if("5".equals(eventStatus)) {
							str.append(alertBean.getTemperature()).append("\n");
						}else {
							str.append(alertBean.getTemperature()).append(",");
							str.append(alertBean.getCloseTime()).append("\n");//解決時間
						}
					}else {
						if("5".equals(eventStatus)) {
							str.append("").append("\n");
						}else {
							str.append("").append(",");
							str.append("").append("\n");
						}
					}
				}			
			}			
			
			if("5".equals(eventStatus)) {
				CsvUtil.exportCsv(str, "Unresolved"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv", response);
			}else {
				CsvUtil.exportCsv(str, "Resolved"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv", response);
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
