package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import aptg.battery.dao.NbListDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;

/**
 * Servlet implementation class getNBHistory 通訊模組異動紀錄
 */
@WebServlet("/getNBHistory")
public class getNBHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getNBHistory.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getNBHistory() {
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
		logger.debug("getNBHistory start");
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
					NbListVO nbListVO = this.parseJson(req, timezone, resource);
					boolean isAdmin = true;
					if (ToolUtil.checkAdminCompany(userCompany)) {
						nbListVO.setCompanyCode(userCompany);
						isAdmin = false;
					}					
					NbListDAO nbListDAO = new NbListDAO();
					List<DynaBean> list = nbListDAO.getNBHistory(nbListVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(nbListVO.getType())) {
							composeCSV(list, timezone, resource, isAdmin, response);
							rep = false;
						} else if ("check".equals(nbListVO.getType())) {
							rspJson.put("msg", "NBHistory"
									+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv");
						} else {
							rspJson.put("msg", convertToJson(list, timezone, resource));
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
		logger.debug("getNBHistory end");
	}

	/**
	 * 解析Json
	 * @param json
	 * @param timezone
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private NbListVO parseJson(String json, String timezone, ResourceBundle resource) throws Exception {
		NbListVO nbListVO = new NbListVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					nbListVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "BatteryGroupId")) {
					JSONObject batteryGroupId = request.getJSONObject("BatteryGroupId");
					nbListVO.setBatteryGroupIdArr(ToolUtil.jsonArrToSqlStr(batteryGroupId.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "NBID")) {
					JSONObject nbid = request.getJSONObject("NBID");
					nbListVO.setNbidArr(ToolUtil.jsonArrToSqlStr(nbid.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "ModifyItem")) {
					JSONObject modifyItem = request.getJSONObject("ModifyItem");
					nbListVO.setModifyItemArr(ToolUtil.jsonArrToSqlStr(modifyItem.optJSONArray("List")));
				}
				
				SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if (!ToolUtil.isNull(request, "ModifyTime")) {
					JSONObject modifyTime = request.getJSONObject("ModifyTime");
					String radio = modifyTime.optString("Radio");
					if ("1".equals(radio)) {
						String start = modifyTime.optString("Start")+" "+modifyTime.optString("StartHH")+":"+modifyTime.optString("StartMM");
						String end = modifyTime.optString("End")+" "+modifyTime.optString("EndHH")+":"+modifyTime.optString("EndMM");
						if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
							nbListVO.setError(true);
							nbListVO.setCode("16");
							nbListVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
							return nbListVO;
						}
						nbListVO.setStartDate(sdf2.format(sdf.parse(start)));
						nbListVO.setEndDate(sdf2.format(sdf.parse(end)));
					}else if ("3".equals(radio)) {//過去一個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -1);
						nbListVO.setStartDate(sdf2.format(cal.getTime()));
					}
				}else {//預設前一個月日期
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -1);
					nbListVO.setStartDate(sdf2.format(cal.getTime()));
				}	

				nbListVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return nbListVO;
	}

	/**
	 * 組Json
	 * @param rows
	 * @param timezone
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String timezone, ResourceBundle resource) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		JSONObject data = new JSONObject();
		try {
			JSONArray nbidArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject nbid = new JSONObject();
				nbid.put("Seq", i + 1);
				
				nbid.put("NBID", ObjectUtils.toString(bean.get("nbid")));//通訊序號
				
				String active = ObjectUtils.toString(bean.get("active"));//13啟用, 14停用 ,15刪除			
				if(bean.get("batteryid")!=null) {
					nbid.put("BatteryGroupID", bean.get("nbid")+"_"+bean.get("batteryid"));//電池組ID	
					if("13".equals(active) || "14".equals(active)) {
						nbid.put("Link", true);
					}else {
						nbid.put("Link", false);						
					}
				}else {					
					nbid.put("BatteryGroupID", "");//電池組ID
					nbid.put("Link", false);
				}
				nbid.put("BattInternalId", ObjectUtils.toString(bean.get("battinternalid")));
				nbid.put("ModifyItem", resource.getString(ObjectUtils.toString(bean.get("modifyitem"))));//異動項目
				nbid.put("AllocateCompany", ObjectUtils.toString(bean.get("allocatecompany")));//序號歸屬公司				
				nbid.put("ModifyTime", ToolUtil.dateFormat(bean.get("createtime"), sdf));//異動時間
				nbid.put("Company", ObjectUtils.toString(bean.get("companyname")));//異動公司
				nbid.put("ModifyName", ObjectUtils.toString(bean.get("createusername")));//異動人員
				nbid.put("ModifyRemark", ObjectUtils.toString(bean.get("remark")));//異動備註

				nbidArr.put(nbid);
			}
			data.put("NBID", nbidArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 組CSV
	 * @param list
	 * @param timezone
	 * @param resource
	 * @param response
	 * @throws Exception
	 */
	private void composeCSV(List<DynaBean> list, String timezone, ResourceBundle resource, boolean isAdmin, HttpServletResponse response)
			throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		try {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				DynaBean bean = list.get(i);
				if (i == 0) {
					str.append(resource.getString("1057")).append(",")// 通訊序號
					   .append(resource.getString("1058")).append(",")// 電池組ID(母板)
					   .append(resource.getString("1059")).append(",")// 異動項目
					   .append(resource.getString("1537")).append(",")// 序號歸屬公司
					   .append(resource.getString("1060")).append(",");// 異動時間
					if(isAdmin)
						str.append(resource.getString("1538")).append(",");// 異動公司
					str.append(resource.getString("1061")).append(",")// 異動人員
					   .append(resource.getString("1062")).append("\n");// 異動備註
				}
				str.append(ObjectUtils.toString(bean.get("nbid"))).append(",");
				if(bean.get("batteryid")!=null) {
					str.append(bean.get("nbid")+"_"+bean.get("batteryid")).append(",");
				}else {
					str.append("").append(",");
				}	
				str.append(resource.getString(ObjectUtils.toString(bean.get("modifyitem")))).append(",")
				   .append(ObjectUtils.toString(bean.get("allocatecompany"))).append(",")				   
				   .append(ToolUtil.dateFormat(bean.get("createtime"), sdf)).append(",");
				if(isAdmin)
					str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");
				str.append(ObjectUtils.toString(bean.get("createusername"))).append(",")
				   .append(CsvUtil.csvHandlerStr(ObjectUtils.toString(bean.get("remark")))).append("\n");
			}
			CsvUtil.exportCsv(str,
					"NBHistory" + ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv",
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
