package aptg.battery.api;

import java.io.IOException;
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

import aptg.battery.bean.StatusNowBean;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;

/**
 * Servlet implementation class getStatusNow 取得電池狀態
 */
@WebServlet("/getStatusNow")
public class getStatusNow extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getStatusNow.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getStatusNow() {
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
		logger.debug("getStatusNow start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
//			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("UserCompany:"+userCompany);
			logger.debug("request: " + req);
			
//			logger.debug("CompanyCode: " + companyCode + ",Type: " + type);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					BatteryVO batteryVO = this.parseJson(req);
					if (ToolUtil.checkAdminCompany(userCompany))
						batteryVO.setCompanyCode(userCompany);
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> statusList = batteryDAO.getGroupStatusNow(batteryVO);
					rspJson.put("code", "00");
					if (statusList != null && !statusList.isEmpty()) {
						StatusNowBean bean = this.processData(statusList, language);
						if ("csv".equals(batteryVO.getType())) {
							composeCSV(bean, language, timezone, response);
							rep = false;
						} else if ("check".equals(batteryVO.getType())) {
							rspJson.put("msg", "Status"
									+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv");
						} else {
							rspJson.put("msg", convertToJson(bean));
						}
						rspJson.put("code", "00");
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
		} finally {
			if (rep) {
				logger.debug("rsp: " + rspJson);
				ToolUtil.response(rspJson.toString(), response);
			}
		}
		logger.debug("getStatusNow end");
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
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private StatusNowBean processData(List<DynaBean> statusList, String language) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResourceBundle resource = ToolUtil.getLanguage(language);
		StatusNowBean statusBean = new StatusNowBean();
		try {
			Map<String, Integer> groupMap = new HashMap<String, Integer>();
			int groupStatus;
			for(int i=0; i<statusList.size(); i++) {
				boolean disconnect = false;
				DynaBean bean = statusList.get(i);
				
				//判斷是否離線
				Calendar now = Calendar.getInstance();
				Calendar rectime = Calendar.getInstance();
				rectime.setTime(sdf.parse(sdf.format(bean.get("rectime"))));
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
	
				String groupKey = ObjectUtils.toString(bean.get("groupinternalid"));
				if(groupMap.containsKey(groupKey)) {
					groupStatus = groupMap.get(groupKey);
					if(!disconnect){
						int status = ToolUtil.parseInt(bean.get("status"));
						if(groupStatus==4 || status>groupStatus){
							groupMap.remove(groupKey);
							groupMap.put(groupKey, status);
						}
					}
				}else {							
					if (disconnect) {
						groupStatus = 4;									
					}else{
						groupStatus = ToolUtil.parseInt(bean.get("status"));//狀態代碼						
					}
					groupMap.put(groupKey, groupStatus);
				}
			}
			int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
			for(String groupinternalid : groupMap.keySet()) {
				int status = groupMap.get(groupinternalid);
				if (status==4) {
					count4++;
				} else if (status==3) {
					count3++;
				} else if (status==2) {
					count2++;
				} else {
					count1++;
				}
			}

			List<String> status = new ArrayList<String>();
			List<Integer> count = new ArrayList<Integer>();
			if (count1 > 0) {
				status.add(resource.getString("1"));
				count.add(count1);
			}
			if (count2 > 0) {
				status.add(resource.getString("2"));
				count.add(count2);
			}
			if (count3 > 0) {
				status.add(resource.getString("3"));
				count.add(count3);
			}
			if (count4 > 0) {
				status.add(resource.getString("4"));
				count.add(count4);
			}

			statusBean.setStatus(status);
			statusBean.setCount(count);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return statusBean;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(StatusNowBean bean) throws Exception {
		return new JSONObject(JsonUtil.getInstance().convertObjectToJsonstring(bean));
	}

	/**
	 * 組CSV
	 * 
	 * @param bean
	 * @param resource
	 * @param timezone
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(StatusNowBean bean, String language, String timezone, HttpServletResponse response)
			throws Exception {
		try {
			ResourceBundle resource = ToolUtil.getLanguage(language);
			StringBuilder str = new StringBuilder();
			List<Integer> countList = bean.getCount();
			List<String> statusList = bean.getStatus();

			str.append(resource.getString("1021")).append(",")// 狀態
					.append(resource.getString("1079")).append("\n");// 數量
			for (int i = 0; i < countList.size(); i++) {
				str.append(statusList.get(i)).append(",").append(countList.get(i)).append("\n");
			}

			CsvUtil.exportCsv(str,
					"Status" + ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv",
					response);
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
