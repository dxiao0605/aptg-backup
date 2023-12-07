package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

import aptg.battery.dao.BatteryTypeListDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryTypeListVO;

/**
 * Servlet implementation class getBatteryTypeList 電池類型
 */
@WebServlet("/getBatteryTypeList")
public class getBatteryTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryTypeList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryTypeList() {
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
		logger.debug("getBatteryTypeList start");
		JSONObject rspJson = new JSONObject();
		boolean rep = true;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("UserCompany:" + userCompany);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone);
					BatteryTypeListVO batteryTypeListVO = this.parseJson(req);
					boolean isAdmin = true;
					if (ToolUtil.checkAdminCompany(userCompany)) {
						batteryTypeListVO.setCompanyCode(userCompany);
						isAdmin = false;
					}
					BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
					List<DynaBean> list = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(batteryTypeListVO.getType())) {
							composeCSV(list, resource, sdf, isAdmin, response);
							rep = false;
						} else if ("check".equals(batteryTypeListVO.getType())) {
							rspJson.put("msg", "BattTypeManagement" + sdf.format(new Date()) + ".csv");
						} else {
							rspJson.put("msg", convertToJson(list));
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
		logger.debug("getBatteryTypeList end");
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
	private BatteryTypeListVO parseJson(String json) throws Exception {
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					batteryTypeListVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "BattTypeList")) {
					JSONObject batteryType = request.getJSONObject("BattTypeList");
					batteryTypeListVO.setBatteryTypeCodeArr(ToolUtil.jsonArrToSqlStr(batteryType.optJSONArray("List")));
				}
				
				batteryTypeListVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryTypeListVO;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray batteryArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject battery = new JSONObject();
				battery.put("Seq", i + 1);
				battery.put("Company", bean.get("companyname"));// 公司
				battery.put("BatteryTypeCode", bean.get("batterytypecode"));// 電池型號代碼
				battery.put("BatteryTypeName", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號

				batteryArr.put(battery);
			}
			data.put("BatteryTypeList", batteryArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組CSV
	 * 
	 * @param list
	 * @param timezone
	 * @param language
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(List<DynaBean> list, ResourceBundle resource, SimpleDateFormat sdf,
			boolean isAdmin, HttpServletResponse response) throws Exception {
		try {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				DynaBean bean = list.get(i);
				if (i == 0) {
					if(isAdmin)	
						str.append(resource.getString("1064")).append(",");// 公司
					str.append(resource.getString("1506")).append("\n");// 電池型號

				}
				if(isAdmin)	
					str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");						
				str.append(ObjectUtils.toString(bean.get("batterytypename"))).append("\n");
			}

			CsvUtil.exportCsv(str, "BattTypeManagement" + sdf.format(new Date()) + ".csv", response);
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
