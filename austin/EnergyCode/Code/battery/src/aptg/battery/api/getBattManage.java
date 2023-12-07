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

import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;

/**
 * Servlet implementation class getBattManage 電池組管理
 */
@WebServlet("/getBattManage")
public class getBattManage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBattManage.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBattManage() {
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
		logger.debug("getBattManage start");
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
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BatteryVO batteryVO = parseJson(req, timezone, resource);
					if (batteryVO.isError()) {
						rspJson.put("code", batteryVO.getCode());
						rspJson.put("msg", batteryVO.getDescription());
					} else {
						boolean isAdmin = true;
						if (ToolUtil.checkAdminCompany(userCompany)) {
							batteryVO.setCompanyCode(userCompany);
							isAdmin = false;
						}
						BatteryDAO batteryDAO = new BatteryDAO();
						List<DynaBean> list = batteryDAO.getBattManage(batteryVO);
						if (list != null && !list.isEmpty()) {
							if ("csv".equals(batteryVO.getType())) {
								composeCSV(list, timezone, language, isAdmin, response);
								rep = false;
							} else if ("check".equals(batteryVO.getType())) {
								rspJson.put("msg",
										"BattSetManagement"
												+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())
												+ ".csv");
							} else {
								rspJson.put("msg", convertToJson(list, timezone, language));
							}
							rspJson.put("code", "00");
						} else {
							rspJson.put("code", "07");
							rspJson.put("msg", resource.getString("5004"));// 查無資料
						}
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
		logger.debug("getBattManage end");
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
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					batteryVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "BatteryGroupId")) {
					JSONObject batteryGroupId = request.getJSONObject("BatteryGroupId");
					batteryVO.setBatteryGroupIdArr(ToolUtil.jsonArrToSqlStr(batteryGroupId.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "InstallDate")) {
					JSONObject installDate = request.getJSONObject("InstallDate");
					String radio = installDate.optString("Radio");
					if ("1".equals(radio)) {
						String start = installDate.optString("Start");
						String end = installDate.optString("End");
						if (!ToolUtil.dateCheck(start, "yyyy-MM-dd") || !ToolUtil.dateCheck(end, "yyyy-MM-dd")) {
							batteryVO.setError(true);
							batteryVO.setCode("16");
							batteryVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd)");// 日期格式錯誤
							return batteryVO;
						}
						
						SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd", timezone);
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
						batteryVO.setStartDate(sdf2.format(sdf.parse(start)));
						batteryVO.setEndDate(sdf2.format(sdf.parse(end)));
					} else if ("2".equals(radio)) {
						batteryVO.setInstallDateNull("1");
					}
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
	 * @param sdf
	 * @param language
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String timezone, String language) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd Z", timezone);
		JSONObject data = new JSONObject();
		try {
			JSONArray batteryArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject battery = new JSONObject();
				battery.put("Seq", i + 1);
				battery.put("Company", ObjectUtils.toString(bean.get("companyname")));// 公司
				battery.put("CompanyCode", ObjectUtils.toString(bean.get("companycode")));// 公司代碼
				battery.put("BatteryGroupID",
						ObjectUtils.toString(bean.get("nbid")) + "_" + ObjectUtils.toString(bean.get("batteryid")));// 電池組ID
				battery.put("BattInternalId", ObjectUtils.toString(bean.get("seqno")));
				battery.put("BatteryTypeName", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號		
				battery.put("BatteryTypeCode", ObjectUtils.toString(bean.get("batterytypecode")));
				battery.put("InstallDate", ToolUtil.dateFormat(bean.get("installdate"), sdf));// 安裝日期

				batteryArr.put(battery);
			}
			data.put("BatteryGroupID", batteryArr);
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
	private void composeCSV(List<DynaBean> list, String timezone, String language, boolean isAdmin, HttpServletResponse response)
			throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd Z", timezone);
		try {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				DynaBean bean = list.get(i);
				if (i == 0) {
					if(isAdmin)	
						str.append(resource.getString("1064")).append(",");// 公司
					str.append(resource.getString("1026")).append(",")// 電池組ID
					   .append(resource.getString("1030")).append(",")// 電池型號
					   .append(resource.getString("1027")).append("\n");// 安裝日期
				}
				if(isAdmin)	
					str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");
				str.append(ObjectUtils.toString(bean.get("nbid")) + "_" + ObjectUtils.toString(bean.get("batteryid"))).append(",")				
				   .append(ObjectUtils.toString(bean.get("batterytypename"))).append(",")				
				   .append(ToolUtil.dateFormat(bean.get("installdate"), sdf)).append("\n");
			}

			CsvUtil.exportCsv(str, "BattSetManagement"
					+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv", response);
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
