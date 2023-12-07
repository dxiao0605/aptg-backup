package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;
import aptg.battery.vo.MapVO;

/**
 * Servlet implementation class getMapInfo 取得地圖資訊
 */
@WebServlet("/getMapInfo")
public class getMapInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMapInfo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMapInfo() {
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
		logger.debug("getMapInfo start");
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
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
					BatteryVO batteryVO = this.parseJson(req);
					if (ToolUtil.checkAdminCompany(userCompany))
						batteryVO.setCompanyCode(userCompany);
					batteryVO.setMap(true);
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> batteryList = batteryDAO.getBatteryGroup(batteryVO);
					rspJson.put("code", "00");
					if (batteryList != null && !batteryList.isEmpty()) {
						rspJson.put("msg", convertToJson(batteryList, resource, sdf, ToolUtil.getIMPType(userCompany)));
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));// 查無資料
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
		logger.debug("getMapInfo end");
		ToolUtil.response(rspJson.toString(), response);
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
				
				batteryVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}

	/**
	 * 組Json
	 * 
	 * @param areaList
	 * @param batteryList
	 * @param resource
	 * @param sdf
	 * @param impType
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> batteryList, ResourceBundle resource,
			SimpleDateFormat sdf, int impType) throws Exception {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject data = new JSONObject();
		try {
			BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
			JSONArray markerArr = new JSONArray();			
			String zone="";
			
			//計算地圖資訊
			Map<String, JSONObject> groupMap = new LinkedHashMap<String, JSONObject>();//Key:站台ID
			Map<String, MapVO> areaMap = new LinkedHashMap<String, MapVO>();//Key:公司@@國家@@地域
			JSONObject battery = null;
			for(DynaBean bean : batteryList) {
				boolean disconnect = false;
				if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {//依資料紀錄時區呈顯
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
				
				

	               //disconect =0 不判斷離線 20220214 David
					
					if (now.after(rectime) && ToolUtil.parseInt(bean.get("disconnect")) >0 ) {
						disconnect = true;						
					}
					
				
				//判斷是否超過離線時間，且有離線告警
					/*
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
				}*/
				
				BigDecimal maxVol = ToolUtil.divide(bean.get("maxvol"), 1000, 2); 
				BigDecimal minVol = ToolUtil.divide(bean.get("minvol"), 1000, 2);
				BigDecimal temperature = ToolUtil.getBigDecimal(bean.get("temperature"));
				
				if(groupMap.containsKey(groupKey)) {//相同群組資料合併計算
					battery = groupMap.get(groupKey);
					battery.put("BatteryCount", battery.getInt("BatteryCount")+1);					
					
					Date recTime = sdf.parse(ToolUtil.dateFormat(bean.get("rectime"), sdf));
					if(recTime.after(sdf.parse(battery.getString("RecTime")))) {
						battery.put("RecTime", ToolUtil.dateFormat(bean.get("rectime"), sdf));//數據更新時間	
					}					
					
					if(!disconnect){//不計算未連線的，如果全部未連線，最後一筆未連線為主
						if(battery.getInt("StatusCode")==4) {
							battery.put("MaxIMPValue", maxIMPValue);// 最大內阻
							battery.put("MinIMPValue", minIMPValue);// 最小內阻
							if(isOLMax) {
								battery.put("MaxIMP", "OL");
							}else {
								battery.put("MaxIMP", maxIMPValue);//最大內阻
							}
							if(isOLMin) {
								battery.put("MinIMP", "OL");
							}else {
								battery.put("MinIMP", minIMPValue);//最小內阻
							}	
							battery.put("MaxVol", maxVol);// 最大電壓
							battery.put("MinVol", minVol);// 最小電壓
							battery.put("MaxTemperature", temperature);// 最大溫度
							battery.put("MinTemperature", temperature);// 最小溫度
						}else {
							if(maxIMPValue.compareTo(battery.getBigDecimal("MaxIMPValue"))>0) {
								battery.put("MaxIMPValue", maxIMPValue);// 最大內阻
								if(isOLMax) {
									battery.put("MaxIMP", "OL");
								}else {
									battery.put("MaxIMP", maxIMPValue);//最大內阻
								}
							}
							if(minIMPValue.compareTo(battery.getBigDecimal("MinIMPValue"))<0) {
								battery.put("MinIMPValue", minIMPValue);// 最小內阻
								if(isOLMin) {
									battery.put("MinIMP", "OL");//最小內阻
								}else {
									battery.put("MinIMP", minIMPValue);//最小內阻
								}
							}	
							if(maxVol.compareTo(battery.getBigDecimal("MaxVol"))>0) {
								battery.put("MaxVol", maxVol);// 最大電壓
							}
							if(minVol.compareTo(battery.getBigDecimal("MinVol"))<0) {
								battery.put("MinVol", minVol);// 最小電壓
							}					
							if(temperature.compareTo(battery.getBigDecimal("MaxTemperature"))>0) {
								battery.put("MaxTemperature", temperature);// 最大溫度
							}
							if(temperature.compareTo(battery.getBigDecimal("MinTemperature"))<0) {
								battery.put("MinTemperature", temperature);// 最小溫度
							}
						}
						int status = ToolUtil.parseInt(bean.get("status"));
						if(battery.getInt("StatusCode")==4 || status>battery.getInt("StatusCode")) {
							battery.put("StatusCode", status);//狀態代碼
							battery.put("StatusDesc", resource.getString(String.valueOf(status)));//狀態說明
						}
					}
				}else {
					//地圖第二層資訊
					battery = new JSONObject();	
					battery.put("Type", "Battery");
					battery.put("Country", ObjectUtils.toString(bean.get("country")));// 國家
					battery.put("Area", ObjectUtils.toString(bean.get("area")));// 地域
					battery.put("GroupID", bean.get("groupid"));// 基地台號碼
					battery.put("GroupName", bean.get("groupname"));// 基地台名稱
					battery.put("GroupLabel", bean.get("groupname")+"/"+bean.get("groupid"));
					battery.put("Lng", bean.get("lng"));// 站台經度
					battery.put("Lat", bean.get("lat"));// 站台緯度
					battery.put("GroupInternalID", bean.get("groupinternalid"));//群組ID
					battery.put("NBID", ObjectUtils.toString(bean.get("nbid")));//通訊序號
					battery.put("BatteryCount", 1);// 電池數量
					battery.put("RecTime", ToolUtil.dateFormat(bean.get("rectime"), sdf));// 數據更新時間
					battery.put("MaxIMPValue", maxIMPValue);// 最大內阻
					battery.put("MinIMPValue", minIMPValue);// 最小內阻
					if(isOLMax) {
						battery.put("MaxIMP", "OL");
					}else {
						battery.put("MaxIMP", maxIMPValue);//最大內阻
					}
					if(isOLMin) {
						battery.put("MinIMP", "OL");
					}else {
						battery.put("MinIMP", minIMPValue);//最小內阻
					}
					battery.put("MaxVol", maxVol);// 最大電壓
					battery.put("MinVol", minVol);// 最小電壓
					battery.put("MaxTemperature", temperature);// 最大溫度
					battery.put("MinTemperature", temperature);// 最小溫度
					
					if (disconnect) {
						battery.put("StatusCode", 4);//狀態代碼
						battery.put("StatusDesc", resource.getString("4"));//狀態說明(離線)
					}else{
						battery.put("StatusCode", bean.get("status"));//狀態代碼
						battery.put("StatusDesc", resource.getString(ObjectUtils.toString(bean.get("status"))));//狀態說明
					}
					groupMap.put(groupKey, battery);
					
					//地圖第一層資訊
					if(bean.get("lng")!=null && bean.get("lat")!=null) {
						String map1Key = bean.get("companycode")+"@@"+bean.get("country")+"@@"+bean.get("area");//公司@@國家@@地域
						BigDecimal lng = ToolUtil.getBigDecimal(bean.get("lng"));
						BigDecimal lat = ToolUtil.getBigDecimal(bean.get("lat"));
						
						if(areaMap.containsKey(map1Key)) {
							MapVO mapVO = areaMap.get(map1Key);					
							if(mapVO.getMaxLng().compareTo(lng)<0) {
								mapVO.setMaxLng(lng);
							}
							if(mapVO.getMinLng().compareTo(lng)>0) {
								mapVO.setMinLng(lng);
							}							
							if(mapVO.getMaxLat().compareTo(lat)<0) {
								mapVO.setMaxLat(lat);
							}
							if(mapVO.getMinLat().compareTo(lat)>0) {
								mapVO.setMinLat(lat);
							}														
							mapVO.setCount(mapVO.getCount().add(BigDecimal.ONE));
							areaMap.put(map1Key, mapVO);
						}else {
							MapVO mapVO = new MapVO();
							mapVO.setCountry(ObjectUtils.toString(bean.get("country")));//國家
							mapVO.setArea(ObjectUtils.toString(bean.get("area")));//地域
							mapVO.setMaxLng(lng);//地域最大經度
							mapVO.setMinLng(lng);//地域最小經度
							mapVO.setMaxLat(lat);//地域最大緯度
							mapVO.setMinLat(lat);//地域最小緯度
							mapVO.setCount(BigDecimal.ONE);//數量
							areaMap.put(map1Key, mapVO);
						}
					}
				}
			}
			
			BigDecimal maxLng = BigDecimal.ZERO;//地圖最大經度
			BigDecimal minLng = BigDecimal.ZERO;//地圖最小經度
			BigDecimal maxLat = BigDecimal.ZERO;//地圖最大緯度
			BigDecimal minLat = BigDecimal.ZERO;//地圖最小緯度
			int seq = 1;
			for(String map1Key : areaMap.keySet()) {
				MapVO mapVO = areaMap.get(map1Key);
				BigDecimal groupLng = ToolUtil.divide(mapVO.getMaxLng().add(mapVO.getMinLng()), ToolUtil.BigDecimalTwo(), 8);
				BigDecimal groupLat = ToolUtil.divide(mapVO.getMaxLat().add(mapVO.getMinLat()), ToolUtil.BigDecimalTwo(), 8);
				if(seq==1) {
					maxLng = groupLng;
					minLng = groupLng;
					maxLat = groupLat;
					minLat = groupLat;
				}else {
					if(maxLng.compareTo(groupLng)<0) {
						maxLng = groupLng;
					}
					if(minLng.compareTo(groupLng)>0) {
						minLng = groupLng;
					}							
					if(maxLat.compareTo(groupLat)<0) {
						maxLat = groupLat;
					}
					if(minLat.compareTo(groupLat)>0) {
						minLat = groupLat;
					}	
				}
				
				JSONObject group = new JSONObject();
				group.put("Seq", seq++);
				group.put("Type", "Group");
				group.put("Country", mapVO.getCountry());// 國家
				group.put("Area", mapVO.getArea());// 地域
				group.put("Lng", groupLng);// 地域經度
				group.put("Lat", groupLat);// 地域緯度
				group.put("Count", mapVO.getCount());// 數量
				markerArr.put(group);
			}
			
			for(String groupKey: groupMap.keySet()) {				
				JSONObject batt = groupMap.get(groupKey);
				batt.put("Seq", seq++);
				markerArr.put(batt);
			}
			
			data.put("Marker", markerArr);
			data.put("IMPType", impType);
			data.put("Lng", ToolUtil.divide(maxLng.add(minLng), ToolUtil.BigDecimalTwo(), 8));// 地圖中心經度
			data.put("Lat", ToolUtil.divide(maxLat.add(minLat), ToolUtil.BigDecimalTwo(), 8));// 地圖中心緯度
			data.put("Zoom", this.getZoom(maxLng, maxLat, minLng, minLat));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	private final double EARTH_RADIUS = 6378137.0;//地球半徑 
	
	/**
	 * 計算兩點距離
	 * @param maxLng 最大經度
	 * @param maxLat 最大緯度
	 * @param minLng 最小經度
	 * @param minLat 最小緯度
	 * @return
	 */
	private double gps2m(double maxLng, double maxLat, double minLng, double minLat) {  
       double radLat1 = (maxLat * Math.PI / 180.0);  
       double radLat2 = (minLat * Math.PI / 180.0);  
       double latLength = radLat1 - radLat2;//緯度距離 
       double lngLength = (maxLng - minLng) * Math.PI / 180.0; //經度距離
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latLength / 2), 2)  
              + Math.cos(radLat1) * Math.cos(radLat2)  
              * Math.pow(Math.sin(lngLength / 2), 2)));//兩點距離
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    } 
	
	/**
	 * 計算Zoom
	 * @param maxLng 最大經度
	 * @param maxLat 最大緯度
	 * @param minLng 最小經度
	 * @param minLat 最小緯度
	 * @return
	 */
	private double getZoom(BigDecimal maxLng, BigDecimal maxLat, BigDecimal minLng, BigDecimal minLat) {
		int zoom = 2;
		double[] zoomArr = {50,
							100,
							200,
							500,
							1000,
							2000,
							5000,
							10000,
							20000,
							25000,
							50000,
							100000,
							200000,
							500000,
							1000000,
							2000000,
							5000000,
							10000000};
		double distance = gps2m(maxLng.doubleValue(), maxLat.doubleValue(), minLng.doubleValue(), minLat.doubleValue());  //取得兩點距離
		logger.debug("Lng:"+maxLng+"/"+minLng+",Lat:" + maxLat+"/"+minLat+",Distance:"+distance);
		if((maxLng.compareTo(minLng)==0)&&(maxLat.compareTo(minLat)==0)){
		    zoom=11;
		}else if(distance!=0) {
			for (int i = 0; i < zoomArr.length; i++) {
				if(zoomArr[i] - distance > 0){					
					zoom = 20-i;
					break;
				}
			}			
		}
		return zoom;
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
