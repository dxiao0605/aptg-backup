package aptg.battery.api;

import java.io.IOException;
import java.math.BigDecimal;
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

import aptg.battery.dao.CompanyDAO;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CompanyVO;



/**
 * Servlet implementation class updIMPType 修改內阻呈現方式
 */
@WebServlet("/updIMPType")
public class updIMPType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updIMPType.class.getName()); 
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "alert.topics";
	private static final String PUBLISH_MQTTID = "alert.mqttid";
	private static final String PUBLISH_QOS = "alert.qos";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updIMPType() {
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
		logger.debug("updIMPType start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			resource = ToolUtil.getLanguage(language);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					CompanyVO companyVO = this.parseJson(req, resource);
					if(companyVO.isError()) {
						rspJson.put("code", companyVO.getCode());
						rspJson.put("msg", companyVO.getDescription());
					}else {
						CompanyDAO companyDAO = new CompanyDAO();
						List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);					
						if (list != null && !list.isEmpty()) {
							DynaBean bean = list.get(0);
							//計算告警值
							String impType = ObjectUtils.toString(bean.get("imptype"));//20: 內阻值 21:毫內阻 22:電導值 
							BigDecimal newAlert1 = BigDecimal.ZERO;
							BigDecimal newAlert2 = BigDecimal.ZERO;
							BigDecimal alert1 = ToolUtil.getBigDecimal(bean.get("alert1"));
							BigDecimal alert2 = ToolUtil.getBigDecimal(bean.get("alert2"));							
							if("20".equals(impType) && "21".equals(companyVO.getIMPType())) {
								//內阻 > 毫內阻
								newAlert1 = ToolUtil.irToMir(alert1);
								newAlert2 = ToolUtil.irToMir(alert2);
							}else if("21".equals(impType) && "20".equals(companyVO.getIMPType())) {
								//毫內阻 > 內阻
								newAlert1 = ToolUtil.mirToIr(alert1);
								newAlert2 = ToolUtil.mirToIr(alert2);
							}else if("20".equals(impType) && "22".equals(companyVO.getIMPType())) {
								//內阻 > 電導值
								newAlert1 = ToolUtil.irToS(alert1);
								newAlert2 = ToolUtil.irToS(alert2);
							}else if("22".equals(impType) && "20".equals(companyVO.getIMPType())) {
								//電導值 > 內阻
								newAlert1 = ToolUtil.sToIr(alert1);
								newAlert2 = ToolUtil.sToIr(alert2);
							}else if("21".equals(impType) && "22".equals(companyVO.getIMPType())) {
								//毫內阻 > 電導值
								newAlert1 = ToolUtil.mirToS(alert1);
								newAlert2 = ToolUtil.mirToS(alert2);
							}else if("22".equals(impType) && "21".equals(companyVO.getIMPType())) {
								//電導值 > 毫內阻
								newAlert1 = ToolUtil.sToMir(alert1);
								newAlert2 = ToolUtil.sToMir(alert2);
							}else {
								newAlert1 = alert1;
								newAlert2 = alert2;
							}
							companyVO.setAlert1(newAlert1.toString());
							companyVO.setAlert2(newAlert2.toString());
							companyVO.setDisconnect(ObjectUtils.toString(bean.get("disconnect")));
							
							//修改告警設定
							companyDAO.updIMPType(companyVO);
							
							//通知MQTT
							String payload = convertToJson(companyVO);
							logger.debug("MQTT req:"+ payload);
							ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
							String topic = mqttConfig.getString(PUBLISH_TOPIC)+companyVO.getTaskID();
							String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
							String qos = mqttConfig.getString(PUBLISH_QOS);
							
							MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
							
							rspJson.put("code", "00");				
							rspJson.put("msg", resource.getString("5002"));//保存成功		
						}else {
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
			rspJson.put("msg", resource.getString("5003"));//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("updIMPType end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private CompanyVO parseJson(String json, ResourceBundle resource) throws Exception {
		CompanyVO companyVO = new CompanyVO();
		try {
			JSONObject request = new JSONObject(json);
			if(ToolUtil.isNull(request, "Company")) {
				companyVO.setError(true);
				companyVO.setCode("24");
				companyVO.setDescription(resource.getString("5008"));//必填欄位不能為空
			}else {
				companyVO.setCompanyCode(request.optString("Company"));
			}
			if(ToolUtil.isNull(request, "IMPType")) {
				companyVO.setError(true);
				companyVO.setCode("24");
				companyVO.setDescription(resource.getString("5008"));//必填欄位不能為空
			}else {
				companyVO.setIMPType(request.optString("IMPType"));
			}
			companyVO.setTaskID("A"+StringUtils.leftPad(ToolUtil.getSequence("A")+"",9,"0"));
			companyVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return companyVO;
	}
	
	/**
	 * 組Json
	 * @param companyVO
	 * @return
	 * @throws Exception
	 */
	private String convertToJson(CompanyVO companyVO) throws Exception {
		JSONObject mqttReq = new JSONObject();
		try {	
			mqttReq.put("TaskID", companyVO.getTaskID());
			mqttReq.put("CompanyCode", companyVO.getCompanyCode());
			mqttReq.put("IMPType", Integer.parseInt(companyVO.getIMPType()));
			mqttReq.put("Alert1", ToolUtil.getBigDecimal(companyVO.getAlert1()));
			mqttReq.put("Alert2", ToolUtil.getBigDecimal(companyVO.getAlert2()));
			mqttReq.put("Disconnect", ToolUtil.getBigDecimal(companyVO.getDisconnect()));				
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return mqttReq.toString();
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
