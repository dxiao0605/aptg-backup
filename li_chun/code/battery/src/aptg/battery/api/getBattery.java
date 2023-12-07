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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.bean.BatteryBean;
import aptg.battery.bean.BatteryDataBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getBattery 電池組數據
 */
@WebServlet("/getBattery")
public class getBattery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBattery.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBattery() {
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
		logger.debug("getBattery start");
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
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);					
					BatteryVO batteryVO = this.parseJson(req);
					boolean isAdmin = true;
					if (ToolUtil.checkAdminCompany(userCompany)) {
						batteryVO.setCompanyCode(userCompany);
						isAdmin = false;
					}
						
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> list = batteryDAO.getBattery(batteryVO);					
					if (list != null && !list.isEmpty()) {
						BatteryDataBean data = this.processData(list, language, timezone, ToolUtil.getIMPType(userCompany), batteryVO);
						if(data.getBattery()!=null && !data.getBattery().isEmpty()) {
							if ("csv".equals(batteryVO.getType())) {
								composeCSV(data, timezone, language, isAdmin, response);
								rep = false;
							} else if("check".equals(batteryVO.getType())) {
								rspJson.put("msg", "BattSet"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
							} else {
								rspJson.put("msg", convertToJson(data));
							}
							rspJson.put("code", "00");
						}else {
							rspJson.put("code", "07");
							rspJson.put("msg", resource.getString("5004"));//查無資料
						}						
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
		logger.debug("getBattery end");
	}
	
	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return BatteryVO
	 * @throws Exception
	 */
	private BatteryVO parseJson(String json) throws Exception {
		BatteryVO batteryVO = new BatteryVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					batteryVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "Country")) {
					JSONObject country = request.getJSONObject("Country");
					batteryVO.setCountryArr(ToolUtil.jsonArrToSqlStr(country.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Area")) {
					JSONObject area = request.getJSONObject("Area");
					batteryVO.setAreaArr(ToolUtil.jsonArrToSqlStr(area.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "GroupID")) {
					JSONObject groupId = request.getJSONObject("GroupID");
					batteryVO.setGroupInternalIdArr(ToolUtil.jsonArrToSqlStr(groupId.optJSONArray("List")));
				}	
				
				if (!ToolUtil.isNull(request, "Status")) {
					JSONObject status = request.getJSONObject("Status");
					JSONArray arr = status.optJSONArray("List");
					List<String> strList = new ArrayList<String>();
					if (arr!=null && arr.length()>0) {
						String str;
						for (int i=0; i<arr.length(); i++) {
							str = arr.optString(i);
							if (StringUtils.isNotBlank(str))
								strList.add(str);
						}
					}
					batteryVO.setStatusList(strList);
				}		
				
				if (!ToolUtil.isNull(request, "GroupInternalId")) {
					batteryVO.setGroupInternalID(request.optString("GroupInternalId"));	
				}
				
				batteryVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	private BatteryDataBean processData(List<DynaBean> rows, String language, String timezone, int impType, BatteryVO batteryVO) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat date = ToolUtil.getDateFormat("yyyy/MM/dd", timezone);
		ResourceBundle resource = ToolUtil.getLanguage(language);
		BatteryDataBean data = new BatteryDataBean();
		try {
			BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
			List<String> statusList = batteryVO.getStatusList();
			List<BatteryBean> batteryList = new ArrayList<BatteryBean>();
			Map<String, BatteryBean> batteryMap = new HashMap<String, BatteryBean>();
			BatteryBean batteryBean = null;
			int seq = 1;
			String groupid = "";
			String zone="";
			for(int i=0; i<rows.size(); i++) {
				boolean disconnect = false;
				DynaBean bean = rows.get(i);
				if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
					zone = bean.get("timezone").toString();
					sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", zone);
				}
				
				String batteryKey = ObjectUtils.toString(bean.get("groupinternalid"))+ObjectUtils.toString(bean.get("nbid"))+ObjectUtils.toString(bean.get("batteryid"));
				if(i==0)
					groupid = ObjectUtils.toString(bean.get("groupid"));
				
				if(batteryMap.containsKey(batteryKey)) {
					batteryBean = batteryMap.get(batteryKey);					
				}else {
					batteryBean = new BatteryBean();			
					batteryBean.setSeq(seq++);
					batteryBean.setCompanyCode(ObjectUtils.toString(bean.get("companycode")));
					batteryBean.setCompany(ObjectUtils.toString(bean.get("companyname")));
					batteryBean.setCountry(ObjectUtils.toString(bean.get("country")));//國家
					batteryBean.setArea(ObjectUtils.toString(bean.get("area")));//地域
					batteryBean.setGroupID(ObjectUtils.toString(bean.get("groupid")));//基地台號碼
					batteryBean.setGroupName(ObjectUtils.toString(bean.get("groupname")));//基地台名稱
					batteryBean.setGroupLabel(ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
					batteryBean.setAddress(ObjectUtils.toString(bean.get("address")));//地址
					batteryBean.setGroupInternalID(ObjectUtils.toString(bean.get("groupinternalid")));
					batteryBean.setBatteryGroupID(ObjectUtils.toString(bean.get("nbid"))+"_"+ObjectUtils.toString(bean.get("batteryid")));
					batteryBean.setBattInternalID(ObjectUtils.toString(bean.get("seqno")));
					batteryBean.setNBID(ObjectUtils.toString(bean.get("nbid")));
					batteryBean.setBatteryID(ObjectUtils.toString(bean.get("batteryid")));
					batteryBean.setInstallDate(ToolUtil.dateFormat(bean.get("installdate"), date));//安裝日期				
					batteryBean.setBatteryType(ObjectUtils.toString(bean.get("batterytypename")));//電池型號			
					batteryBean.setRecTime(ToolUtil.dateFormat(bean.get("rectime"), sdf));//數據更新時間				
					
					//判斷是否離線
					Calendar now = Calendar.getInstance();
					Calendar rectime = Calendar.getInstance();
					rectime.setTime(sdf2.parse(sdf2.format(bean.get("rectime"))));
					rectime.add(Calendar.SECOND, ToolUtil.parseInt(bean.get("disconnect")));
					
					//判斷是否超過離線時間，且有離線告警
					if (now.after(rectime) && "4".equals(ObjectUtils.toString(bean.get("eventtype")))) {
						disconnect = true;						
					}
					//判斷是否超過離線時間最近的半點
					if(rectime.get(Calendar.MINUTE)>30) {
						rectime.add(Calendar.HOUR, +1);
						rectime.set(Calendar.MINUTE, 0);
					}else {
						rectime.set(Calendar.MINUTE, 30);
					}
					if (now.after(rectime)) {
						disconnect = true;						
					}
					
					if(disconnect) {
						batteryBean.setStatusCode(4);
						batteryBean.setStatusDesc(resource.getString("4"));
					}
					batteryBean.setIR(new ArrayList<String>());
					batteryBean.setVol(new ArrayList<String>());
					batteryMap.put(batteryKey, batteryBean);
					batteryList.add(batteryBean);		
				}
				
				String category = ObjectUtils.toString(bean.get("category"));
				if("1".equals(category)) {
					if(ToolUtil.getBigDecimal(bean.get("value"), 0).compareTo(ol)>=0) {
						batteryBean.getIR().add("CH"+bean.get("orderno")+" OL");
					}else {
						if(impType==22) {//電導值
							batteryBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(1000000, bean.get("value"), 3));
						}else if(impType==21) {//毫內阻值
							batteryBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(bean.get("value"), 1000, 3));
						}else {//內阻值
							batteryBean.getIR().add("CH"+bean.get("orderno")+" "+ToolUtil.getBigDecimal(bean.get("value"), 0));
						}
					}
					
					int status = Integer.parseInt(bean.get("status").toString());
					if(status>batteryBean.getStatusCode()) {
						batteryBean.setStatusCode(status);
						batteryBean.setStatusDesc(resource.getString(String.valueOf(status)));
					}
				}else if("2".equals(category)) {			
					batteryBean.getVol().add("CH"+bean.get("orderno")+" "+ToolUtil.divide(bean.get("value"), 1000, 2));//電壓
				}else {
					batteryBean.setTemperature(ToolUtil.getBigDecimal(bean.get("value")));//溫度
				}
			}
			
			if(statusList!=null && statusList.size()>0) {//篩選電池狀態
				for(int i=batteryList.size()-1; i>=0; i--) {
					BatteryBean bean = batteryList.get(i);
					if(!statusList.contains(String.valueOf(bean.getStatusCode()))) {
						batteryList.remove(i);
					}
				}
			}
			data.setBattery(batteryList);
			data.setIMPType(impType);//20: 內阻值 21:毫內阻 22:電導值
			data.setGroupID(groupid);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組Json
	 * @param batteryDataBean
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(BatteryDataBean batteryDataBean) throws Exception {
		return new JSONObject(JsonUtil.getInstance().convertObjectToJsonstring(batteryDataBean));
	}
	
	/**
	 *  組CSV
	 * @param data
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(BatteryDataBean data, String timezone, String language, boolean isAdmin, HttpServletResponse response) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		try {			
			StringBuilder str = new StringBuilder();
			List<BatteryBean> batteryList = data.getBattery();
			for(int i=0; i<batteryList.size(); i++) {							
				BatteryBean batteryBean = batteryList.get(i);	
				if(i==0) {
					if(isAdmin)					
						str.append(resource.getString("1064")).append(",");// 公司
					str.append(resource.getString("1028")).append(",")//國家
					   .append(resource.getString("1029")).append(",")//地域
					   .append(resource.getString("1012")).append(",")//站台編號
					   .append(resource.getString("1013")).append(",")//站台名稱			   
					   .append(resource.getString("1031")).append(",")//地址
					   .append(resource.getString("1026")).append(",")//電池組ID
					   .append(resource.getString("1027")).append(",")//安裝日期
					   .append(resource.getString("1030")).append(",")//電池型號
					   .append(resource.getString("1015")).append(",");//數據更新時間					   
					if(data.getIMPType()==22) {//電導值
						str.append(resource.getString("1020")).append(",");
					}else if(data.getIMPType()==21) {//毫內阻值
						str.append(resource.getString("1019")).append(",");
					}else {//內阻值
						str.append(resource.getString("1016")).append(",");
					}			
					str.append(resource.getString("1017")).append(",")//電壓					  
					   .append(resource.getString("1018")).append(",")//溫度			
					   .append(resource.getString("1021")).append("\n");//電池狀態
				}
				List<String> irlList = batteryBean.getIR();
				List<String> volList = batteryBean.getVol();
				int count = irlList.size()>volList.size()?irlList.size():volList.size();				
				for(int j=0; j<count; j++) {
					if(j==0) {
						if(isAdmin)	
							str.append(batteryBean.getCompany()).append(",");
						str.append(batteryBean.getCountry()).append(",")
						   .append(batteryBean.getArea()).append(",")
						   .append(batteryBean.getGroupID()).append(",")
						   .append(batteryBean.getGroupName()).append(",")				
						   .append(CsvUtil.csvHandlerStr(batteryBean.getAddress())).append(",")
						   .append(batteryBean.getBatteryGroupID()).append(",")
						   .append(batteryBean.getInstallDate()).append(",")						
						   .append(batteryBean.getBatteryType()).append(",")												
						   .append(batteryBean.getRecTime()).append(",");
					}else {
						if(isAdmin)	
							str.append("").append(",");
						str.append("").append(",").append("").append(",").append("").append(",")
						   .append("").append(",").append("").append(",").append("").append(",")
						   .append("").append(",").append("").append(",").append("").append(",");
					}
					if(j>=irlList.size()) {
						str.append("").append(",");
					}else {
						str.append(batteryBean.getIR().get(j)).append(",");
					}
					if(j>=volList.size()) {
						str.append("").append(",");
					}else {
						str.append(batteryBean.getVol().get(j)).append(",");
					}
					if(j==0) {
						str.append(batteryBean.getTemperature()).append(",")
						   .append(batteryBean.getStatusDesc()).append("\n");
					}else {
						str.append("").append(",")
						   .append("").append("\n");
					}
				}			
			}			
			
			CsvUtil.exportCsv(str, "BattSet"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv", response);
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
