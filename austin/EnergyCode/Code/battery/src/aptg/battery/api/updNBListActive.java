package aptg.battery.api;

import java.io.IOException;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;



/**
 * Servlet implementation class updNBListActive 修改通訊序號狀態
 */
@WebServlet("/updNBListActive")
public class updNBListActive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updNBListActive.class.getName()); 
	private static final String MQTT = "mqtt";
	private static final String DEL_TOPIC = "delnbid.topics";
	private static final String DEL_MQTTID = "delnbid.mqttid";
	private static final String DEL_QOS = "delnbid.qos";

	private static final String ACTIVE_TOPIC = "nbcompany.topics";
	private static final String ACTIVE_MQTTID = "nbcompany.mqttid";
	private static final String ACTIVE_QOS = "nbcompany.qos";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updNBListActive() {
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
		logger.debug("updNBListActive start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
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
						nbListVO.setCompanyCode(userCompany);
						nbListDAO.updNBListActive(nbListVO);
						
						JSONObject msg = new JSONObject();
						msg.put("NBList", nbListVO.getJson());
						
						ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
						if("15".equals(nbListVO.getActive())) {
							// 刪除通訊序號，通知MQTT							
							String topic = mqttConfig.getString(DEL_TOPIC);
							String mqttid = mqttConfig.getString(DEL_MQTTID);
							String qos = mqttConfig.getString(DEL_QOS);

							String payload = nbListVO.getDelJson().toString();
							logger.debug("MQTT req:"+ payload);
							MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
							msg.put("Message", resource.getString("5005"));//刪除成功
						}else {
							// 修改通訊序號狀態，通知MQTT
							String topic = mqttConfig.getString(ACTIVE_TOPIC);
							String mqttid = mqttConfig.getString(ACTIVE_MQTTID);
							String qos = mqttConfig.getString(ACTIVE_QOS);

							String payload = nbListVO.getActiveJson().toString();
							logger.debug("MQTT req:"+ payload);
							MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);							
							msg.put("Message", resource.getString("5002"));//保存成功
						}
						
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
		logger.debug("updNBListActive end");
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
			String active = new String();
			if(ToolUtil.isNull(request, "Active")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("5008"));//必填欄位不能為空			
			}else {
				active = request.optString("Active");
				nbListVO.setActive(active);//13啟用, 14停用, 15已刪除
				if("15".equals(active)) {					
					nbListVO.setModifyItem("12");//7:匯入並啟用, 8:分配, 9:啟用, 10:停用, 11:未分配, 12:刪除
				}else if("14".equals(active)) {
					nbListVO.setModifyItem("10");
				}else {
					nbListVO.setModifyItem("9");
				}				
			}
			
			String rceCompanyCode = new String();
			Map<String, String> companyMap = new HashMap<String, String>();
			CompanyDAO companyDAO = new CompanyDAO();
			//查詢預設公司(RCE)					
			List<DynaBean> list = companyDAO.getDefault();
			if (list != null && !list.isEmpty()) {
				DynaBean bean = list.get(0);
				rceCompanyCode = ObjectUtils.toString(bean.get("companycode"));
			}	
						
			if(ToolUtil.isNull(request, "NBList")) {
				nbListVO.setError(true);
				nbListVO.setCode("24");
				nbListVO.setDescription(resource.getString("1057")+resource.getString("5008"));//通訊序號必填欄位不能為空
			}else {				
				JSONArray nbArr = request.getJSONArray("NBList");
				JSONArray activeJson = new JSONArray();
				String nbidArr = new String();
				List<String[]> nbInfoList = new ArrayList<String[]>();
				if (nbArr!=null && nbArr.length()>0) {
					String nbid, companyName;					
					for (int i=0; i<nbArr.length(); i++) {
						JSONObject obj = nbArr.getJSONObject(i);
						nbid = obj.optString("NBID");
						companyName = obj.optString("Company");
						if (StringUtils.isNotBlank(nbid)) {
							nbidArr += StringUtils.isNotBlank(nbidArr) ? ",'" + nbid + "'" : "'" + nbid + "'";
							String[] nb = new String[2];//0:公司代碼 1:通訊序號
							if("15".equals(active)) {//刪除固定寫RCE公司
								nb[0] = rceCompanyCode;
							}else {
								if(companyMap.containsKey(companyName)) {
									nb[0] = companyMap.get(companyName);
								}else {
									List<DynaBean> company = companyDAO.getCompanyCode(companyName);
									if (company != null && !company.isEmpty()) {
										DynaBean bean = company.get(0);
										nb[0] = ObjectUtils.toString(bean.get("companycode"));
										companyMap.put(companyName, nb[0]);										
									}else {
										nb[0] = rceCompanyCode;
									}
								}
							}		
							nb[1] = nbid;
							nbInfoList.add(nb);
							
							JSONObject nbInfo = new JSONObject();
							nbInfo.put("NBID", nb[1]);
							nbInfo.put("CompanyCode", Integer.parseInt(nb[0]));
							nbInfo.put("Active", Integer.parseInt(active));
				        	activeJson.put(nbInfo);	
						}							
					}
				}

				nbListVO.setNbidArr(nbidArr);
				nbListVO.setNbInfoList(nbInfoList);
				nbListVO.setJson(nbArr);
				nbListVO.setDelJson(nbArr);
				nbListVO.setActiveJson(activeJson);
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
