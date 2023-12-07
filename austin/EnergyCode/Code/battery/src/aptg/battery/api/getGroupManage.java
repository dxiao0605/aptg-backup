package aptg.battery.api;

import java.io.IOException;
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

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;

/**
 * Servlet implementation class getGroupManage 站台管理
 */
@WebServlet("/getGroupManage")
public class getGroupManage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getGroupManage.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getGroupManage() {
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
		logger.debug("getGroupManage start");
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
					BatteryGroupVO batteryGroupVO = this.parseJson(req);
					boolean isAdmin = true;
					if (ToolUtil.checkAdminCompany(userCompany)) {
						batteryGroupVO.setCompanyCode(userCompany);
						isAdmin = false;
					}
					BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
					List<DynaBean> list = batteryGroupDAO.getGroupManage(batteryGroupVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(batteryGroupVO.getType())) {
							composeCSV(list, timezone, language, isAdmin, response);
							rep = false;
						} else if ("check".equals(batteryGroupVO.getType())) {
							rspJson.put("msg", "GroupManagement"
									+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv");
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
		logger.debug("getGroupManage end");
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return BatteryGroupVO
	 * @throws Exception
	 */
	private BatteryGroupVO parseJson(String json) throws Exception {
		BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					batteryGroupVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Country")) {
					JSONObject company = request.getJSONObject("Country");
					batteryGroupVO.setCountryArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Area")) {
					JSONObject company = request.getJSONObject("Area");
					batteryGroupVO.setAreaArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "GroupID")) {
					JSONObject company = request.getJSONObject("GroupID");
					batteryGroupVO.setGroupInternalIdArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				batteryGroupVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryGroupVO;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @param sdf
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray groupArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject group = new JSONObject();
				group.put("Seq", i + 1);
				group.put("GroupInternalID", ObjectUtils.toString(bean.get("seqno")));
				group.put("Company", ObjectUtils.toString(bean.get("companyname")));// 公司
				group.put("CompanyCode", ObjectUtils.toString(bean.get("companycode")));// 公司代碼
				group.put("Country", ObjectUtils.toString(bean.get("country")));// 國家
				group.put("Area", ObjectUtils.toString(bean.get("area")));// 地域
				group.put("GroupID", ObjectUtils.toString(bean.get("groupid")));// 站台號碼
				group.put("GroupName", ObjectUtils.toString(bean.get("groupname")));// 站台名稱
				group.put("GroupLabel", ObjectUtils.toString(bean.get("groupname"))+"/"+ObjectUtils.toString(bean.get("groupid")));
				group.put("Address", ObjectUtils.toString(bean.get("address")));// 地址
				group.put("Count", ToolUtil.parseInt(bean.get("batterycount")));// 電池組數量
				group.put("DefaultGroup", bean.get("defaultgroup"));//0:預設站台, 1:非預設站台

				groupArr.put(group);
			}
			data.put("Group", groupArr);
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
		try {
			StringBuilder str = new StringBuilder();
			if(isAdmin)	
				str.append(resource.getString("1064")).append(",");// 公司
			str.append(resource.getString("1028")).append(",")// 國家
				.append(resource.getString("1029")).append(",")// 地域
				.append(resource.getString("1013")).append(",")// 站台名稱
				.append(resource.getString("1012")).append(",")// 站台編號
				.append(resource.getString("1031")).append(",")// 地址
				.append(resource.getString("1014")).append("\n");// 電池組數
			for (DynaBean bean : list) {
				if(isAdmin)	
					str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");
				str.append(ObjectUtils.toString(bean.get("country"))).append(",")
				   .append(ObjectUtils.toString(bean.get("area"))).append(",")
				   .append(ObjectUtils.toString(bean.get("groupname"))).append(",")
				   .append(ObjectUtils.toString(bean.get("groupid"))).append(",")						
				   .append(CsvUtil.csvHandlerStr(ObjectUtils.toString(bean.get("address")))).append(",")
				   .append(ToolUtil.parseInt(bean.get("batterycount"))).append("\n");
			}

			CsvUtil.exportCsv(str,
					"GroupManagement" + ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv",
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
