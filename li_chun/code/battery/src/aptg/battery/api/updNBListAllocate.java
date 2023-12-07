package aptg.battery.api;

import java.io.IOException;
import java.util.ArrayList;
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
import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class updNBListAllocate 分配通訊序號
 */
@WebServlet("/updNBListAllocate")
public class updNBListAllocate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updNBListAllocate.class.getName()); 
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "nbcompany.topics";
	private static final String PUBLISH_MQTTID = "nbcompany.mqttid";
	private static final String PUBLISH_QOS = "nbcompany.qos";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updNBListAllocate() {
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
		logger.debug("updNBListAllocate start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					NbListVO nbListVO = this.parseJson(req, resource);
					if(nbListVO.isError()) {
						JSONObject msg = new JSONObject();
						rspJson.put("code", nbListVO.getCode());
						msg.put("NBList", new JSONArray());
						msg.put("Message", nbListVO.getDescription());
						rspJson.put("msg", msg);						
					}else {
							NbListDAO nbListDAO = new NbListDAO();
							nbListDAO.updNBListAllocate(nbListVO);
							
							//通知MQTT
							ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
							String topic = mqttConfig.getString(PUBLISH_TOPIC);
							String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
							String qos = mqttConfig.getString(PUBLISH_QOS);
							String payload = nbListVO.getJson().toString();
							logger.debug("MQTT req:"+ payload);
							MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
							
							JSONObject msg = new JSONObject();
							msg.put("NBList", nbListVO.getJson());
							msg.put("Message", resource.getString("5002"));//保存成功
							rspJson.put("msg", msg);
							rspJson.put("code", "00");		
					}																						
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {			
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5003"));//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("updNBListAllocate end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private NbListVO parseJson(String json, ResourceBundle resource) throws Exception {
		NbListVO nbListVO = new NbListVO();
		try {
			JSONObject request = new JSONObject(json);
			String allocate = "";//16分配,17 未分配
			if(ToolUtil.isNull(request, "Allocate")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("5008"));//必填欄位不能為空
				return nbListVO;
			}else {
				allocate = request.optString("Allocate");
				nbListVO.setAllocate(allocate);
				if("16".equals(allocate)) {
					nbListVO.setModifyItem("8");//7:匯入並啟用, 8:分配, 9:啟用, 10:停用, 11:未分配, 12:刪除
				}else {
					nbListVO.setModifyItem("11");
				}				
			}
			
			BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
			String companyCode = "";
			String companyName = "";
			String groupInternalId = "";
			if("17".equals(allocate)){//16分配,17 未分配
				CompanyDAO companyDAO = new CompanyDAO();
				List<DynaBean> list = companyDAO.getDefault();
				if(list!=null && !list.isEmpty()) {
					DynaBean bean = list.get(0);
					companyCode = ObjectUtils.toString(bean.get("companycode"));
					companyName = ObjectUtils.toString(bean.get("companyname"));
					groupInternalId = ObjectUtils.toString(bean.get("seqno"));
					nbListVO.setGroupInternalId(ObjectUtils.toString(bean.get("seqno")));
					nbListVO.setCompanyCode(companyCode);
				}
			}else {			
				if(ToolUtil.isNull(request, "Company")) {
					nbListVO.setError(true);
					nbListVO.setCode("24");
					nbListVO.setDescription(resource.getString("1064")+resource.getString("5008"));//公司必填欄位不能為空
					return nbListVO;
				}else if(checkCompany(request.optString("Company"))) {					
					nbListVO.setError(true);
					nbListVO.setCode("26");
					nbListVO.setDescription(resource.getString("1064")+resource.getString("5033"));//公司值域錯誤
					return nbListVO;		
				}else {
					companyCode = request.optString("Company");
					nbListVO.setCompanyCode(companyCode);
					
					List<DynaBean> list = batteryGroupDAO.getCompanyDefaultGroup(companyCode);//取得公司預設站台
					if(list!=null && !list.isEmpty()) {
						DynaBean bean = list.get(0);
						nbListVO.setGroupInternalId(ObjectUtils.toString(bean.get("seqno")));
						groupInternalId = ObjectUtils.toString(bean.get("seqno"));
					}
				}
				
				CompanyDAO companyDAO = new CompanyDAO();
				List<DynaBean> company = companyDAO.getCompanyName(companyCode);
				if(company!=null && !company.isEmpty()) {
					DynaBean bean = company.get(0);
					companyName = ObjectUtils.toString(bean.get("companyname"));
				}
			}
			
			if(ToolUtil.isNull(request, "NBList")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("1057")+resource.getString("5008"));//通訊序號必填欄位不能為空
			}else {				
				JSONArray nbArr = request.getJSONArray("NBList");
				String nbidArr = "";
				List<String> nbList = new ArrayList<String>();
				JSONArray nbJson = new JSONArray();
				if (nbArr!=null && nbArr.length()>0) {
					String str;
					for (int i=0; i<nbArr.length(); i++) {
						str = nbArr.optString(i);
						if (StringUtils.isNotBlank(str)) {																			
							nbidArr += StringUtils.isNotBlank(nbidArr) ? ",'" + str + "'" : "'" + str + "'";
							nbList.add(str);
							JSONObject nb = new JSONObject();
							nb.put("NBID", str);
				        	nb.put("CompanyCode", Integer.parseInt(companyCode));
				        	nb.put("CompanyName", companyName);
				        	nb.put("Active", 0);
				        	nb.put("GroupInternalID", Integer.parseInt(groupInternalId));
				        	nb.put("DefaultGroup", 0);//預設站台
				        	nbJson.put(nb);	
						}							
					}
				}

				nbListVO.setNbidArr(nbidArr);
				nbListVO.setNbList(nbList);
				nbListVO.setJson(nbJson);
			}
			
			if(!ToolUtil.lengthCheck(request.optString("Remark"), 200)) {
				nbListVO.setError(true);
				nbListVO.setCode("25");
				nbListVO.setDescription(resource.getString("1062")+resource.getString("5024"));//異動備註長度不符
			}else {
				nbListVO.setRemark(request.optString("Remark"));
			}

			nbListVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return nbListVO;
	}
	
	private boolean checkCompany(String companyCode) throws Exception {
		CompanyDAO companyDAO = new CompanyDAO();
		List<DynaBean> list = companyDAO.getDefault();
		if(list!=null && !list.isEmpty()) {
			DynaBean bean = list.get(0);
			if(companyCode.equals(ObjectUtils.toString(bean.get("companycode")))) {
				return true;
			}else {
				return false;
			}
		}			
		return false;
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
