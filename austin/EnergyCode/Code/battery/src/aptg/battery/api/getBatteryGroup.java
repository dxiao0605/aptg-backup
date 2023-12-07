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

import aptg.battery.bean.GroupBean;
import aptg.battery.bean.GroupDataBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getBatteryGroup 電池群組數據
 */
@WebServlet("/getBatteryGroup")
public class getBatteryGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryGroup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryGroup() {
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
		logger.debug("getBatteryGroup start");
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
					List<DynaBean> list = batteryDAO.getBatteryGroup(batteryVO);					
					if (list != null && !list.isEmpty()) {
						GroupDataBean data = this.processData(list, resource, timezone, ToolUtil.getIMPType(userCompany), batteryVO);
						if(data.getBattery()!=null && !data.getBattery().isEmpty()) {
							if ("csv".equals(batteryVO.getType())) {
								composeCSV(data, timezone, resource, isAdmin, response);
								rep = false;
							} else if("check".equals(batteryVO.getType())) {
								rspJson.put("msg", "Group"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
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
		logger.debug("getBatteryGroup end");
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
					
				batteryVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	private GroupDataBean processData(List<DynaBean> rows, ResourceBundle resource, String timezone, int impType, BatteryVO batteryVO) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GroupDataBean data = new GroupDataBean();		
		try {
			BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
			List<String> statusList = batteryVO.getStatusList();
			List<GroupBean> groupList = new ArrayList<GroupBean>();
			List<String> batteryGroupID, nbid;
			Map<String, GroupBean> groupMap = new HashMap<String, GroupBean>();
			GroupBean groupBean = null;
			int seq = 1;
			String zone="";
			for(DynaBean bean : rows) {
				boolean disconnect = false;
				if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
					zone = bean.get("timezone").toString();
					sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", zone);
				}			
				String groupKey = ObjectUtils.toString(bean.get("groupinternalid"));
				BigDecimal maxIMPValue = BigDecimal.ZERO, minIMPValue = BigDecimal.ZERO;
				boolean isOLMax = false;
				boolean isOLMin = false;	
				
				if(impType==22) {//電導值
					maxIMPValue = ToolUtil.divide(1000000, bean.get("minir"), 3);
					minIMPValue = ToolUtil.divide(1000000, bean.get("maxir"), 3);
					if(ToolUtil.getBigDecimal(bean.get("minir"), 0).compareTo(ol)>=0) {
						isOLMax = true;
					}
					if(ToolUtil.getBigDecimal(bean.get("maxir"), 0).compareTo(ol)>=0) {
						isOLMin = true;
					}
				}else {
					if(impType==21) {//毫內阻值
						maxIMPValue = ToolUtil.divide(bean.get("maxir"), 1000, 3);
						minIMPValue = ToolUtil.divide(bean.get("minir"), 1000, 3);
					}else {//內阻值
						maxIMPValue = ToolUtil.getBigDecimal(bean.get("maxir"), 0);
						minIMPValue = ToolUtil.getBigDecimal(bean.get("minir"), 0);
					}					
					if(ToolUtil.getBigDecimal(bean.get("maxir"), 0).compareTo(ol)>=0) {
						isOLMax = true;
					}
					if(ToolUtil.getBigDecimal(bean.get("minir"), 0).compareTo(ol)>=0) {
						isOLMin = true;
					}
				}
				
				//判斷是否離線
				Calendar now = Calendar.getInstance();
				Calendar rectime = Calendar.getInstance();
				rectime.setTime(sdf2.parse(sdf2.format(bean.get("rectime"))));
				rectime.add(Calendar.SECOND, ToolUtil.parseInt(bean.get("disconnect")));
				
				
				//disconect =0 不判斷離線 20220211 David
				
				if (now.after(rectime) && ToolUtil.parseInt(bean.get("disconnect")) >0 ) {
					disconnect = true;						
				}
				
				//判斷是否超過離線時間，且有離線告警
				/*
				if (now.after(rectime) && "4".equals(ObjectUtils.toString(bean.get("eventtype")))) {
					disconnect = true;						
				}
				*/
				//判斷是否超過離線時間最近的半點
				
				/*
				if(rectime.get(Calendar.MINUTE)>30) {
					rectime.add(Calendar.HOUR, +1);
					rectime.set(Calendar.MINUTE, 0);
				}else {
					rectime.set(Calendar.MINUTE, 30);
				}
				
				if (now.after(rectime)) {
					disconnect = true;						
				}
				*/
				//disconect =0 不判斷離線 20220211 David
				
				BigDecimal maxVol = ToolUtil.divide(bean.get("maxvol"), 1000, 2); 
				BigDecimal minVol = ToolUtil.divide(bean.get("minvol"), 1000, 2);
				BigDecimal temperature = ToolUtil.getBigDecimal(bean.get("temperature"));
				if(groupMap.containsKey(groupKey)) {
					groupBean = groupMap.get(groupKey);
					groupBean.setBatteryCount(groupBean.getBatteryCount()+1);
					
					nbid = groupBean.getNBID();
					batteryGroupID = groupBean.getBatteryGroupID();
					if(!nbid.contains(ObjectUtils.toString(bean.get("nbid")))) {
						nbid.add(ObjectUtils.toString(bean.get("nbid")));					
						batteryGroupID.add(bean.get("nbid")+"_0");
					}
						
					Date recTime = sdf.parse(ToolUtil.dateFormat(bean.get("rectime"), sdf));
					if(recTime.after(sdf.parse(groupBean.getRecTime()))) {
						groupBean.setRecTime(ToolUtil.dateFormat(bean.get("rectime"), sdf));//數據更新時間	
					}
					
					if(!disconnect){//不計算未連線的，如果全部未連線，最後一筆未連線為主		
						int status = ToolUtil.parseInt(bean.get("status"));
						if(groupBean.getStatusCode()==4) {			
							groupBean.setMaxIMPValue(maxIMPValue);//最大內阻
							groupBean.setMinIMPValue(minIMPValue);//最小內阻
							if(isOLMax) {
								groupBean.setMaxIMP("OL");
							}else {
								groupBean.setMaxIMP(maxIMPValue.toString());//最大內阻
							}
							if(isOLMin) {
								groupBean.setMinIMP("OL");
							}else {
								groupBean.setMinIMP(minIMPValue.toString());//最小內阻
							}	
							groupBean.setMaxVol(maxVol);//最大電壓
							groupBean.setMinVol(minVol);//最小電壓
							groupBean.setMaxTemperature(temperature);//最大溫度
							groupBean.setMinTemperature(temperature);//最小溫度
						}else {						
							if(maxIMPValue.compareTo(groupBean.getMaxIMPValue())>0) {
								groupBean.setMaxIMPValue(maxIMPValue);//最大內阻
								if(isOLMax) {
									groupBean.setMaxIMP("OL");
								}else {
									groupBean.setMaxIMP(maxIMPValue.toString());//最大內阻
								}
							}
							if(minIMPValue.compareTo(groupBean.getMinIMPValue())<0) {
								groupBean.setMinIMPValue(minIMPValue);//最小內阻
								if(isOLMin) {
									groupBean.setMinIMP("OL");//最小內阻
								}else {
									groupBean.setMinIMP(minIMPValue.toString());//最小內阻
								}
							}						
							if(maxVol.compareTo(groupBean.getMaxVol())>0) {
								groupBean.setMaxVol(maxVol);//最大電壓	
							}
							if(minVol.compareTo(groupBean.getMinVol())<0) {
								groupBean.setMinVol(minVol);//最小電壓	
							}
							if(temperature.compareTo(groupBean.getMaxTemperature())>0) {
								groupBean.setMaxTemperature(temperature);//最大溫度
							}
							if(temperature.compareTo(groupBean.getMinTemperature())<0) {
								groupBean.setMinTemperature(temperature);//最小溫度
							}
						}

						if(groupBean.getStatusCode()==4 || status>groupBean.getStatusCode()) {
							groupBean.setStatusCode(status);//狀態代碼
							groupBean.setStatusDesc(resource.getString(String.valueOf(status)));//狀態說明
						}
					}
				}else {
					groupBean = new GroupBean();			
					groupBean.setSeq(seq++);
					groupBean.setCompany(ObjectUtils.toString(bean.get("companyname")));
					groupBean.setCompanyCode(ToolUtil.parseInt(bean.get("companycode")));
					groupBean.setCountry(ObjectUtils.toString(bean.get("country")));//國家
					groupBean.setArea(ObjectUtils.toString(bean.get("area")));//地域
					groupBean.setGroupID(ObjectUtils.toString(bean.get("groupid")));//基地台號碼
					groupBean.setGroupName(ObjectUtils.toString(bean.get("groupname")));//基地台名稱
					groupBean.setGroupLabel(ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
					groupBean.setGroupInternalID(ObjectUtils.toString(bean.get("groupinternalid")));//群組ID
					batteryGroupID = new ArrayList<String>();
					nbid = new ArrayList<String>();
					nbid.add(ObjectUtils.toString(bean.get("nbid")));					
					batteryGroupID.add(bean.get("nbid")+"_0");
					groupBean.setNBID(nbid);
					groupBean.setBatteryGroupID(batteryGroupID);//電池組ID(以ID=0為主)
					groupBean.setBatteryCount(1);//電池數量
					groupBean.setRecTime(ToolUtil.dateFormat(bean.get("rectime"), sdf));//數據更新時間					
					groupBean.setMaxIMPValue(maxIMPValue);//最大內阻
					groupBean.setMinIMPValue(minIMPValue);//最小內阻
					if(isOLMax) {
						groupBean.setMaxIMP("OL");
					}else {
						groupBean.setMaxIMP(maxIMPValue.toString());//最大內阻
					}
					if(isOLMin) {
						groupBean.setMinIMP("OL");
					}else {
						groupBean.setMinIMP(minIMPValue.toString());//最小內阻
					}
					
					groupBean.setMaxVol(maxVol);//最大電壓
					groupBean.setMinVol(minVol);//最小電壓
					groupBean.setMaxTemperature(temperature);//最大溫度
					groupBean.setMinTemperature(temperature);//最小溫度
	
					if(disconnect) {
						groupBean.setStatusCode(4);//狀態代碼
						groupBean.setStatusDesc(resource.getString("4"));//狀態說明(離線)	
					}else {
						groupBean.setStatusCode(ToolUtil.parseInt(bean.get("status")));//狀態代碼
						groupBean.setStatusDesc(resource.getString(ObjectUtils.toString(bean.get("status"))));//狀態說明
					}
					
					groupList.add(groupBean);
					groupMap.put(groupKey, groupBean);
				}				
			}
			
			if(statusList!=null && statusList.size()>0) {
				for(int i=groupList.size()-1; i>=0; i--) {
					GroupBean bean = groupList.get(i);
					if(!statusList.contains(String.valueOf(bean.getStatusCode()))) {
						groupList.remove(i);
					}
				}
			}
			
			data.setBattery(groupList);
			data.setIMPType(impType);//20: 內阻值 21:毫內阻 22:電導值
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 組Json
	 * @param groupDataBean
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(GroupDataBean groupDataBean) throws Exception {		
		return new JSONObject(JsonUtil.getInstance().convertObjectToJsonstring(groupDataBean));
	}
	
	/**
	 * 組CSV
	 * @param rows
	 * @param impType
	 * @param timezone
	 * @param resource
	 * @param sdf
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(GroupDataBean groupDataBean, String timezone, ResourceBundle resource, boolean isAdmin, HttpServletResponse response) throws Exception {
		try {			
			StringBuilder str = new StringBuilder();
			List<GroupBean> groupList = groupDataBean.getBattery();
			for(int i=0; i<groupList.size(); i++) {							
				GroupBean groupBean = groupList.get(i);	
				if(i==0) {
					if(isAdmin)	
						str.append(resource.getString("1064")).append(",");// 公司
					str.append(resource.getString("1028")).append(",")//國家
					   .append(resource.getString("1029")).append(",")//地域
					   .append(resource.getString("1012")).append(",")//站台編號
					   .append(resource.getString("1013")).append(",")//站台名稱			   
					   .append(resource.getString("1014")).append(",")//電池數量
					   .append(resource.getString("1015")).append(",");//數據更新時間
					   
					if(groupDataBean.getIMPType()==22) {//電導值
						str.append(resource.getString("1410")).append(",")//電導值max[S]
						   .append(resource.getString("1411")).append(",");//電導值min[S]
					}else if(groupDataBean.getIMPType()==21) {//毫內阻值
						str.append(resource.getString("1408")).append(",")//毫內阻max[mΩ]
						   .append(resource.getString("1409")).append(",");//毫內阻min[mΩ]
					}else {//內阻值
						str.append(resource.getString("1402")).append(",")//內阻max[UΩ]
						   .append(resource.getString("1403")).append(",");//內阻min[UΩ]
					}			
					str.append(resource.getString("1404")).append(",")//最大電壓
					   .append(resource.getString("1405")).append(",")//最小電壓
					   .append(resource.getString("1406")).append(",")//最大溫度
					   .append(resource.getString("1407")).append(",")//最小溫度			
					   .append(resource.getString("1021")).append("\n");//電池狀態
				}
				if(isAdmin)	
					str.append(groupBean.getCompany()).append(",");
				str.append(groupBean.getCountry()).append(",")
				   .append(groupBean.getArea()).append(",")
				   .append(groupBean.getGroupID()).append(",")
				   .append(groupBean.getGroupName()).append(",")				
				   .append(groupBean.getBatteryCount()).append(",")
				   .append(groupBean.getRecTime()).append(",")
				   .append(groupBean.getMaxIMP()).append(",")
				   .append(groupBean.getMinIMP()).append(",")
				   .append(groupBean.getMaxVol()).append(",")
				   .append(groupBean.getMinVol()).append(",")
				   .append(groupBean.getMaxTemperature()).append(",")
				   .append(groupBean.getMinTemperature()).append(",")				
				   .append(groupBean.getStatusDesc()).append("\n");							   
			}			
			
			CsvUtil.exportCsv(str, "Group"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv", response);
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
