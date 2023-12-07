package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Servlet implementation class getNBGroupHis 通訊模組異動紀錄
 */
@WebServlet("/getNBGroupHis")
public class getNBGroupHis extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getNBGroupHis.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getNBGroupHis() {
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
		logger.debug("getNBGroupHis start");
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
					List<DynaBean> list = nbListDAO.getNBGroupHistory(nbListVO);
					if (list != null && !list.isEmpty()) {
						if ("csv".equals(nbListVO.getType())) {
							composeCSV(list, nbListVO, timezone, resource, isAdmin, response);
							rep = false;
						} else if ("check".equals(nbListVO.getType())) {
							rspJson.put("msg", "NBGroupHis"
									+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv");
						} else {
							rspJson.put("msg", convertToJson(list, nbListVO, timezone, resource));
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
		logger.debug("getNBGroupHis end");
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
			JSONObject request = new JSONObject(json);
			if (!ToolUtil.isNull(request, "Company")) {
				JSONObject company = request.getJSONObject("Company");
				nbListVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
			}
			
			if (!ToolUtil.isNull(request, "Country")) {
				JSONObject country = request.getJSONObject("Country");
				nbListVO.setCountryArr(ToolUtil.jsonArrToSqlStr(country.optJSONArray("List")));
			}

			if (!ToolUtil.isNull(request, "Area")) {
				JSONObject area = request.getJSONObject("Area");
				nbListVO.setAreaArr(ToolUtil.jsonArrToSqlStr(area.optJSONArray("List")));
			}

			if (!ToolUtil.isNull(request, "GroupID")) {
				JSONObject groupId = request.getJSONObject("GroupID");
				nbListVO.setGroupInternalIdArr(ToolUtil.jsonArrToSqlStr(groupId.optJSONArray("List")));
			}	

			List<String> nbList = new ArrayList<String>();
			if (!ToolUtil.isNull(request, "NBID")) {
				JSONObject nbid = request.getJSONObject("NBID");					
				JSONArray arr = nbid.optJSONArray("List");
				
				if (arr!=null && arr.length()>0) {
					String str;
					for (int i=0; i<arr.length(); i++) {
						str = arr.optString(i);
						if (StringUtils.isNotBlank(str))
							nbList.add(str);
					}
				}					
			}
			nbListVO.setNbList(nbList);
			
			SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm", timezone);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (!ToolUtil.isNull(request, "PreviousTime")) {
				JSONObject previousTime = request.getJSONObject("PreviousTime");
				String radio = previousTime.optString("Radio");
				
				if ("1".equals(radio)) {
					String start = previousTime.optString("Start")+" "+previousTime.optString("StartHH")+":"+previousTime.optString("StartMM");
					String end = previousTime.optString("End")+" "+previousTime.optString("EndHH")+":"+previousTime.optString("EndMM");
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
			}else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				nbListVO.setStartDate(sdf2.format(cal.getTime()));
			}
			
			nbListVO.setType(request.optString("Type"));			
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
	private JSONObject convertToJson(List<DynaBean> rows, NbListVO nbListVO, String timezone, ResourceBundle resource) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		JSONObject data = new JSONObject();
		try {
			List<String> nbList = nbListVO.getNbList();
			JSONArray nbidArr = new JSONArray();
			int i=1;
			String id, previousId;
			for (DynaBean bean : rows) {
				JSONObject nbid = new JSONObject();
				nbid.put("Seq", i++);				
				nbid.put("Company", ObjectUtils.toString(bean.get("companyname")));// 公司
				nbid.put("Country", ObjectUtils.toString(bean.get("country")));//國家
				nbid.put("Area", ObjectUtils.toString(bean.get("area")));//地域
				nbid.put("GroupId", ObjectUtils.toString(bean.get("groupid")));//站台編號
				nbid.put("GroupName", ObjectUtils.toString(bean.get("groupname")));//站台名稱
				id = ObjectUtils.toString(bean.get("nbid"));
				previousId = ObjectUtils.toString(bean.get("previousnbid"));
				nbid.put("NBID", id);//通訊序號		
				nbid.put("PreviousNBID", previousId);//接續通訊序號		
				nbid.put("StartTime", ToolUtil.dateFormat(bean.get("starttime"), sdf));//接續開始時間	

				if(nbList.isEmpty() || nbList.contains(id) || nbList.contains(previousId))//通訊序號篩選為全選，或通訊序號和接續通訊序號為篩選的通訊序號
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
	private void composeCSV(List<DynaBean> list, NbListVO nbListVO, String timezone, ResourceBundle resource, 
			boolean isAdmin, HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		try {	
			List<String> nbList = nbListVO.getNbList();
			StringBuilder str = new StringBuilder();
			if(isAdmin)					
				str.append(resource.getString("1064")).append(",");// 公司
			str.append(resource.getString("1028")).append(",")//國家
			   .append(resource.getString("1029")).append(",")//地域
			   .append(resource.getString("1012")).append(",")//站台編號
			   .append(resource.getString("1013")).append(",")//站台名稱
			   .append(resource.getString("1057")).append(",")//通訊序號		
			   .append(resource.getString("1419")).append(",")//接續通訊序號		
			   .append(resource.getString("1574")).append("\n");//接續開始時間		
						
			String id, previousId;
			for (DynaBean bean : list) {	
				id = ObjectUtils.toString(bean.get("nbid"));
				previousId = ObjectUtils.toString(bean.get("previousnbid"));
				if(nbList.isEmpty() || nbList.contains(id) || nbList.contains(previousId)) {
					if(isAdmin)	
						str.append(ObjectUtils.toString(bean.get("companyname"))).append(",");
					str.append(ObjectUtils.toString(bean.get("country"))).append(",")
					   .append(ObjectUtils.toString(bean.get("area"))).append(",")
					   .append(ObjectUtils.toString(bean.get("groupid"))).append(",")
					   .append(ObjectUtils.toString(bean.get("groupname"))).append(",")				
					   .append(id).append(",")
					   .append(previousId).append(",")
					   .append(ToolUtil.dateFormat(bean.get("starttime"), sdf)).append("\n");	
				}
			}
			CsvUtil.exportCsv(str,
					"NBGroupHis" + ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date()) + ".csv",
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
