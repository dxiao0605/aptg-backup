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
import org.json.JSONObject;

import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getBatteryHistoryCheck 電池歷史檢核
 */
@WebServlet("/getBatteryHistoryCheck")
public class getBatteryHistoryCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryHistoryCheck.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryHistoryCheck() {
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
		logger.debug("getBatteryHistory start");
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
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {										
					BatteryVO batteryVO = this.parseJson(req, timezone, resource);
					if (batteryVO.isError()) {
						rspJson.put("code", batteryVO.getCode());
						rspJson.put("msg", batteryVO.getDescription());
					} else {
						if(ToolUtil.checkAdminCompany(userCompany))
							batteryVO.setCompanyCode(userCompany);					
						BatteryDAO batteryDAO = new BatteryDAO();
						List<DynaBean> list = batteryDAO.getBatteryHistoryCheck(batteryVO);
						if (list != null && !list.isEmpty()) {
							rspJson.put("msg", convertToJson(list, batteryVO, timezone));
						} else {
							rspJson.put("code", "07");
							rspJson.put("msg", resource.getString("5004"));//查無資料
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
		}
		logger.debug("rsp: " + rspJson);		
		ToolUtil.response(rspJson.toString(), response);		
		logger.debug("getBatteryHistoryCheck end");
	}
	
	/**
	 *  解析Json
	 * @param json
	 * @param timezone
	 * @param resource
	 * @return
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

				if (!ToolUtil.isNull(request, "BatteryGroupId")) {
					JSONObject batteryGroupId = request.getJSONObject("BatteryGroupId");
					batteryVO.setBatteryGroupIdArr(ToolUtil.jsonArrToSqlStr(batteryGroupId.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "RecTime")) {
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					JSONObject recTime = request.getJSONObject("RecTime");
					String radio = recTime.optString("Radio");
					if ("1".equals(radio)) {//radio選1，則帶入前端日期
						String start = recTime.optString("Start")+" "+recTime.optString("StartHH")+":"+recTime.optString("StartMM");
						String end = recTime.optString("End")+" "+recTime.optString("EndHH")+":"+recTime.optString("EndMM");

						if(ToolUtil.isNull(recTime, "Start") || ToolUtil.isNull(recTime, "StartHH") || ToolUtil.isNull(recTime, "StartMM") ||
								ToolUtil.isNull(recTime, "End") || ToolUtil.isNull(recTime, "EndHH") || ToolUtil.isNull(recTime, "EndMM")) {				
							batteryVO.setError(true);
							batteryVO.setCode("24");
							batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
							return batteryVO;
						}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm")) {
							batteryVO.setError(true);
							batteryVO.setCode("16");
							batteryVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
							return batteryVO;
						}
						
						batteryVO.setStartDate(sdf2.format(sdf.parse(start)));
						batteryVO.setEndDate(sdf2.format(sdf.parse(end)));
					}else if ("3".equals(radio)) {//預設1個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));				
					}else if ("5".equals(radio)) {//預設7天
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.DAY_OF_MONTH, -7);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}else if ("0".equals(radio)) {//預設1天
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.DAY_OF_MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}else {//radio選其他，則帶入預設前一天日期
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}				
					batteryVO.setJson(recTime);
				}else {
					batteryVO.setError(true);
					batteryVO.setCode("24");
					batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	/**
	 * 組Json
	 * @param rows
	 * @param timezone
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, BatteryVO batteryVO, String timezone) throws Exception {
		JSONObject data = new JSONObject();
		try {
			if(rows.size()>1) {//超過一筆電池組ID，則產生Excel
				data.put("FileName", "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".zip");
				data.put("BattInternalId", "");
				data.put("BatteryGroupId", "");
			}else {//只有一筆電池組ID，則跳至電池歷史第二層
				DynaBean bean = rows.get(0);
				data.put("FileName", "");
				data.put("BattInternalId", ObjectUtils.toString(bean.get("seqno")));
				data.put("BatteryGroupId", ObjectUtils.toString(bean.get("nbid"))+"_"+ObjectUtils.toString(bean.get("batteryid")));
			}
			data.put("RecTime", batteryVO.getJson());
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
