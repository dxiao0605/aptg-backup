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

import aptg.battery.dao.CommandTaskDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CommandTaskVO;

/**
 * Servlet implementation class getCommandHistory 參數設定歷史
 */
@WebServlet("/getCommandHistory")
public class getCommandHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCommandHistory.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCommandHistory() {
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
		logger.debug("getCommandHistory start");
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
					CommandTaskVO commandTaskVO = parseJson(req, timezone, resource);
					if (commandTaskVO.isError()) {
						rspJson.put("code", commandTaskVO.getCode());
						rspJson.put("msg", commandTaskVO.getDescription());
					} else {
						boolean isAdmin = true;
						if (ToolUtil.checkAdminCompany(userCompany)) {
							commandTaskVO.setCompanyCode(userCompany);
							isAdmin = false;
						}							
						CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
						List<DynaBean> list = commandTaskDAO.getCommandTaskList(commandTaskVO);
						if (list != null && !list.isEmpty()) {
							if ("csv".equals(commandTaskVO.getType())) {
								composeCSV(list, timezone, language, isAdmin, response);
								rep = false;
							} else if ("check".equals(commandTaskVO.getType())) {
								rspJson.put("msg","Command"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".csv");
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
		logger.debug("getCommandHistory end");
	}

	/**
	 * 解析Json
	 * @param json
	 * @param timezone
	 * @param resource
	 * @return CommandTaskVO
	 * @throws Exception
	 */
	private CommandTaskVO parseJson(String json, String timezone, ResourceBundle resource) throws Exception {		
		CommandTaskVO commandTaskVO = new CommandTaskVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					commandTaskVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "Country")) {
					JSONObject country = request.getJSONObject("Country");
					commandTaskVO.setCountryArr(ToolUtil.jsonArrToSqlStr(country.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Area")) {
					JSONObject area = request.getJSONObject("Area");
					commandTaskVO.setAreaArr(ToolUtil.jsonArrToSqlStr(area.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "GroupID")) {
					JSONObject groupID = request.getJSONObject("GroupID");
					commandTaskVO.setGroupInternalIdArr(ToolUtil.jsonArrToSqlStr(groupID.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "Command")) {
					JSONObject command = request.getJSONObject("Command");	
					commandTaskVO.setCommandIdArr(ToolUtil.jsonArrToSqlStr(command.optJSONArray("List")));
				}
				if (!ToolUtil.isNull(request, "Response")) {
					JSONObject response = request.getJSONObject("Response");
					JSONArray arr = response.optJSONArray("List");
					String respArr = new String();
					if (arr != null && arr.length() > 0) {
						String str;
						for (int i = 0; i < arr.length(); i++) {
							str = arr.optString(i);
							if ("N".equals(str)) {
								commandTaskVO.setResponseNull("1");
							} else if (StringUtils.isNotBlank(str)) {
								respArr += StringUtils.isNotBlank(respArr) ? ",'" + str + "'"
										: "'" + str + "'";
							}
						}
					}
					commandTaskVO.setResponseArr(respArr);
				}

				SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if (!ToolUtil.isNull(request, "SendTime")) {
					JSONObject sendTime = request.getJSONObject("SendTime");
					String radio = sendTime.optString("Radio");
					
					if ("1".equals(radio)) {
						String start = sendTime.optString("Start")+" "+sendTime.optString("StartHH")+":"+sendTime.optString("StartMM");
						String end = sendTime.optString("End")+" "+sendTime.optString("EndHH")+":"+sendTime.optString("EndMM");
						if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
							commandTaskVO.setError(true);
							commandTaskVO.setCode("16");
							commandTaskVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
							return commandTaskVO;
						}
						
						commandTaskVO.setStartDate(sdf2.format(sdf.parse(start)));
						commandTaskVO.setEndDate(sdf2.format(sdf.parse(end)));
					}else if ("3".equals(radio)) {//過去一個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -1);
						commandTaskVO.setStartDate(sdf2.format(cal.getTime()));
					}
				}else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -1);
					commandTaskVO.setStartDate(sdf2.format(cal.getTime()));
				}
				
				if (!ToolUtil.isNull(request, "TaskId")) {
					commandTaskVO.setTaskIDArr(ToolUtil.jsonArrToSqlStr(request.optJSONArray("TaskId")));
				}
				commandTaskVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return commandTaskVO;
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
		ResourceBundle resource = ToolUtil.getLanguage(language);
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		JSONObject data = new JSONObject();
		try {
			JSONArray commandArr = new JSONArray();
			for (int i = 0; i < rows.size(); i++) {
				DynaBean bean = rows.get(i);
				JSONObject command = new JSONObject();
				command.put("Seq", i + 1);
				command.put("Company", ObjectUtils.toString(bean.get("companyname")));// 公司
				command.put("Country", ObjectUtils.toString(bean.get("country")));//國家
				command.put("Area", ObjectUtils.toString(bean.get("area")));//地域
				command.put("GroupID", ObjectUtils.toString(bean.get("groupid")));//站台號碼
				command.put("GroupName", ObjectUtils.toString(bean.get("groupname")));//站台名稱
				command.put("BatteryGroupID", ObjectUtils.toString(bean.get("nbid")) + "_" + ObjectUtils.toString(bean.get("batteryid")));//電池組ID				
				command.put("BatteryTypeName", ObjectUtils.toString(bean.get("batterytypename")));// 電池型號	
				String commandid = ObjectUtils.toString(bean.get("commandid"));
				command.put("Command", resource.getString(commandid)+"\n("+commandid+")");//命令
				command.put("SendTime", ToolUtil.dateFormat(bean.get("createtime"), sdf));//傳送時間
				command.put("PublishTime", ToolUtil.dateFormat(bean.get("publishtime"), sdf));//發佈時間
				command.put("AckTime", ToolUtil.dateFormat(bean.get("acktime"), sdf));//Ack時間
				command.put("ResponseTime", ToolUtil.dateFormat(bean.get("responsetime"), sdf));//Resp時間
				if("0".equals(ObjectUtils.toString(bean.get("responsecode")))) {
					command.put("Response", resource.getString("18")+"\n("+ObjectUtils.toString(bean.get("responsecontent"))+")");//回應訊息
				}else if("1".equals(ObjectUtils.toString(bean.get("responsecode")))) {
					command.put("Response", resource.getString("19")+"\n("+ObjectUtils.toString(bean.get("responsecontent"))+")");//回應訊息
				}else {
					command.put("Response", "");//回應訊息	
				}				
				command.put("Config", ObjectUtils.toString(bean.get("config"))+"\n"+ObjectUtils.toString(bean.get("hexconfig")));//"指令設定值
				commandArr.put(command);
			}
			data.put("Command", commandArr);
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
//		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd Z", timezone);
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		try {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				DynaBean bean = list.get(i);
				if (i == 0) {
					if(isAdmin)	
						str.append(resource.getString("1064")).append(",");//公司
					str.append(resource.getString("1028")).append(",")//國家
					   .append(resource.getString("1029")).append(",")//地域
					   .append(resource.getString("1012")).append(",")//站台號碼
					   .append(resource.getString("1013")).append(",")//站台名稱
					   .append(resource.getString("1026")).append(",")//電池組ID
					   .append(resource.getString("1030")).append(",")//電池型號
					   .append(resource.getString("1065")).append(",")//命令
					   .append(resource.getString("1066")).append(",")//傳送時間
					   .append(resource.getString("1067")).append(",")//發佈時間
					   .append(resource.getString("1068")).append(",")//Ack時間
					   .append(resource.getString("1069")).append(",")//Resp時間
					   .append(resource.getString("1070")).append(",")//回應訊息
					   .append(resource.getString("1071")+"Json/Hex").append("\n");//指令設定值
				}
				if(isAdmin)	
					str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");
				str.append(ObjectUtils.toString(bean.get("country"))).append(",")
				   .append(ObjectUtils.toString(bean.get("area"))).append(",")
				   .append(ObjectUtils.toString(bean.get("groupid"))).append(",")
				   .append(ObjectUtils.toString(bean.get("groupname"))).append(",")
				   .append(ObjectUtils.toString(bean.get("nbid")) + "_" + ObjectUtils.toString(bean.get("batteryid"))).append(",")				
				   .append(ObjectUtils.toString(bean.get("batterytypename"))).append(",");
				
				String commandid = ObjectUtils.toString(bean.get("commandid"));
				str.append(resource.getString(commandid)+"("+commandid+")").append(",")
				   .append(ToolUtil.dateFormat(bean.get("createtime"), sdf)).append(",")
				   .append(ToolUtil.dateFormat(bean.get("publishtime"), sdf)).append(",")
				   .append(ToolUtil.dateFormat(bean.get("acktime"), sdf)).append(",")
				   .append(ToolUtil.dateFormat(bean.get("responsetime"), sdf)).append(",");
				if("0".equals(ObjectUtils.toString(bean.get("responsecode")))) {
					str.append(resource.getString("18")+"("+ObjectUtils.toString(bean.get("responsecontent"))+")").append(",");
				}else if("1".equals(ObjectUtils.toString(bean.get("responsecode")))) {
					str.append(resource.getString("19")+"("+ObjectUtils.toString(bean.get("responsecontent"))+")").append(",");
				}else {
					str.append("").append(",");
				}						
				str.append(CsvUtil.csvHandlerStr(ObjectUtils.toString(bean.get("config"))+"/"+ObjectUtils.toString(bean.get("hexconfig")))).append("\n");
			}

			CsvUtil.exportCsv(str, "Command"
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
