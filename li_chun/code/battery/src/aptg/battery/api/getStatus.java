package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.json.JSONObject;

import aptg.battery.bean.StatusBean;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;

/**
 * Servlet implementation class getStatus 電池狀態變化
 */
@WebServlet("/getStatus")
public class getStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getStatus.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getStatus() {
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
		logger.debug("getStatus start");
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
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd", timezone);
					Calendar cal = Calendar.getInstance();

					BatteryVO batteryVO = this.parseJson(req);
					if (ToolUtil.checkAdminCompany(userCompany))
						batteryVO.setCompanyCode(userCompany);
					cal.add(Calendar.DAY_OF_MONTH, -1);
					batteryVO.setEndDate(sdf.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, -6);
					batteryVO.setStartDate(sdf.format(cal.getTime()));
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> statusList = batteryDAO.getGroupStatus(batteryVO);
					if (statusList != null && !statusList.isEmpty()) {
						StatusBean bean = this.processData(statusList, timezone);
						if ("csv".equals(batteryVO.getType())) {
							composeCSV(bean, resource, timezone, response);
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
		logger.debug("getStatus end");
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

	private StatusBean processData(List<DynaBean> statusList, String timezone)
			throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd", timezone);
		StatusBean statusBean = new StatusBean();
		try {
			List<String> recDate = new ArrayList<String>();
			Map<String, int[]> recMap = new LinkedHashMap<String, int[]>();//記錄每天每個狀態數量
			int[] status = null;
			for (DynaBean bean : statusList) {
				String date = ToolUtil.dateFormat(bean.get("recdate"), sdf);				
				if(recMap.containsKey(date)) {
					status = recMap.get(date);
				}else {
					status = new int[]{0,0,0,0};
					recMap.put(date, status);
					recDate.add(date);
				}				
				if ("1".equals(ObjectUtils.toString(bean.get("status")))) {
					status[0] = ToolUtil.parseInt(bean.get("count"));
				} else if ("2".equals(ObjectUtils.toString(bean.get("status")))) {
					status[1] = ToolUtil.parseInt(bean.get("count"));
				} else if ("3".equals(ObjectUtils.toString(bean.get("status")))) {
					status[2] = ToolUtil.parseInt(bean.get("count"));
				} else if ("4".equals(ObjectUtils.toString(bean.get("status")))) {
					status[3] = ToolUtil.parseInt(bean.get("count"));
				} 
			}

			List<Integer> status1 = new ArrayList<Integer>();
			List<Integer> status2 = new ArrayList<Integer>();
			List<Integer> status3 = new ArrayList<Integer>();
			List<Integer> status4 = new ArrayList<Integer>();
			for(String date : recDate) {   
				status = recMap.get(date);
				status1.add(status[0]);
				status2.add(status[1]);
				status3.add(status[2]);
				status4.add(status[3]);
			}
			ResourceBundle chinese = ToolUtil.getLanguage("1");
			ResourceBundle english = ToolUtil.getLanguage("2");
			ResourceBundle japanese = ToolUtil.getLanguage("3");
			List<String> label = new ArrayList<String>();
			List<String> labelE = new ArrayList<String>();
			List<String> labelJ = new ArrayList<String>();
			label.add(chinese.getString("1"));
			labelE.add(english.getString("1"));
			labelJ.add(japanese.getString("1"));
			label.add(chinese.getString("2"));
			labelE.add(english.getString("2"));
			labelJ.add(japanese.getString("2"));
			label.add(chinese.getString("4"));
			labelE.add(english.getString("4"));
			labelJ.add(japanese.getString("4"));
			label.add(chinese.getString("3"));
			labelE.add(english.getString("3"));
			labelJ.add(japanese.getString("3"));

			statusBean.setRecDate(recDate);
			statusBean.setStatus1(status1);
			statusBean.setStatus2(status2);
			statusBean.setStatus3(status3);
			statusBean.setStatus4(status4);
			statusBean.setLabel(label);
			statusBean.setLabelE(labelE);
			statusBean.setLabelJ(labelJ);
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
	private JSONObject convertToJson(StatusBean bean) throws Exception {
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
	private void composeCSV(StatusBean bean, ResourceBundle resource, String timezone, HttpServletResponse response)
			throws Exception {
		try {
			StringBuilder str = new StringBuilder();
			List<String> recDate = bean.getRecDate();
			List<Integer> status1 = bean.getStatus1();
			List<Integer> status2 = bean.getStatus2();
			List<Integer> status3 = bean.getStatus3();
			List<Integer> status4 = bean.getStatus4();

			str.append(resource.getString("1080")).append(",")// 日期
					.append(resource.getString("1")).append(",")// 正常
					.append(resource.getString("2")).append(",")// 警戒
					.append(resource.getString("3")).append(",")// 需更換
					.append(resource.getString("4")).append("\n");// 離線
			for (int i = 0; i < recDate.size(); i++) {
				str.append(recDate.get(i)).append(",")
				   .append(status1.get(i)).append(",")
				   .append(status2.get(i)).append(",")
				   .append(status3.get(i)).append(",")
				   .append(status4.get(i)).append("\n");
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
