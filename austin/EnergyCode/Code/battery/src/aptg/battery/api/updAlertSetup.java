package aptg.battery.api;

import java.io.IOException;
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
 * Servlet implementation class updAlertSetup 修改告警條件
 */
@WebServlet("/updAlertSetup")
public class updAlertSetup extends HttpServlet {
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "alert.topics";
	private static final String PUBLISH_MQTTID = "alert.mqttid";
	private static final String PUBLISH_QOS = "alert.qos";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updAlertSetup.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updAlertSetup() {
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
		logger.debug("updAlert start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		CompanyVO companyVO =null;
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
					companyVO = this.parseJson(req, resource);
					if(companyVO.isError()) {
						JSONObject msg = convertToErrJson(companyVO.getCompanyCode());
						msg.put("Message", companyVO.getDescription());
						rspJson.put("code", companyVO.getCode());
						rspJson.put("msg", msg);
					}else {
						//修改告警條件
						CompanyDAO companyDAO = new CompanyDAO();
						companyDAO.updateAlertSetup(companyVO);
						
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
					}	
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			JSONObject msg = null;
			try {
				msg = convertToErrJson(companyVO.getCompanyCode());
			} catch (Exception e1) {
				logger.error("", e1);
			}
			msg.put("Message", resource.getString("5003"));
			rspJson.put("code", "99");
			rspJson.put("msg", msg);//保存失敗		
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("updAlertSetup end");
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
			JSONObject msg = request.getJSONObject("msg");
			
			companyVO.setCompanyCode(msg.optString("CompanyCode"));
			int impType = msg.optInt("IMPType");//20:內阻值, 21:毫內阻, 22:電導值
			double alert1 = msg.optDouble("Alert1");
			double alert2 = msg.optDouble("Alert2");
			
			if(impType==22) {
				if(17>alert1 || 1000<alert1) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5043"));//電導值[S]判定值應為17~1000
					return companyVO;
				}else if(17>alert2 || 1000<alert2) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5043"));//電導值[S]判定值應為17~1000
					return companyVO;
				}
			}else if(impType==21) {
				if(1>alert1 || 60<alert1) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5042"));//內阻mΩ判定值應為1.000~60.000
					return companyVO;
				}else if(1>alert2 || 60<alert2) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5042"));//內阻mΩ判定值應為1.000~60.000
					return companyVO;
				}
			}else {
				if(1000>alert1 || 60000<alert1) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5041"));//內阻µΩ判定值應為1000~60000
					return companyVO;
				}else if(1000>alert2 || 60000<alert2) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5041"));//內阻µΩ判定值應為1000~60000
					return companyVO;
				}
			}
						
			if(impType==22) {				
				if(alert1 <= alert2) {
					companyVO.setError(true);
					companyVO.setCode("13");
					companyVO.setDescription(resource.getString("5027"));//判定值1需大於判定值2
					return companyVO;
				}
			}else {
				if(alert1 >= alert2) {
					companyVO.setError(true);
					companyVO.setCode("13");
					companyVO.setDescription(resource.getString("5026"));//判定值1需小於判定值2
					return companyVO;					
				}
			}
			
			if(!ToolUtil.isNull(msg, "Temperature1")) {
				double temperature1 = msg.optDouble("Temperature1");
				if(0>temperature1 || 255<temperature1) {
					companyVO.setError(true);
					companyVO.setCode("26");
					companyVO.setDescription(resource.getString("5067"));//溫度判定值範圍應為0~255
					return companyVO;
				}
				companyVO.setTemperature1(msg.optString("Temperature1"));
			}
			
			
			companyVO.setTaskID("A"+StringUtils.leftPad(ToolUtil.getSequence("A")+"",9,"0"));
			companyVO.setIMPType(msg.optString("IMPType"));
			companyVO.setAlert1(msg.optString("Alert1"));
			companyVO.setAlert2(msg.optString("Alert2"));
			
			companyVO.setDisconnect(String.valueOf(msg.optInt("Disconnect")*3600));
			companyVO.setUserName(msg.optString("UserName"));
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
			mqttReq.put("CompanyCode", Integer.parseInt(companyVO.getCompanyCode()));
			mqttReq.put("IMPType", Integer.parseInt(companyVO.getIMPType()));
			mqttReq.put("Alert1", ToolUtil.getBigDecimal(companyVO.getAlert1()));
			mqttReq.put("Alert2", ToolUtil.getBigDecimal(companyVO.getAlert2()));
			mqttReq.put("Disconnect", ToolUtil.getBigDecimal(companyVO.getDisconnect()));				
			mqttReq.put("Temperature1", ToolUtil.getBigDecimal(companyVO.getTemperature1()));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return mqttReq.toString();
	}
	
	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToErrJson(String companyCode) throws Exception {
		JSONObject data = new JSONObject();
		try {
			CompanyVO companyVO = new CompanyVO();
			companyVO.setCompanyCode(companyCode);				
			CompanyDAO companyDAO = new CompanyDAO();
			List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);
			if(list!=null && !list.isEmpty()) {
				DynaBean bean = list.get(0);				
				data.put("CompanyCode", bean.get("companycode"));
				data.put("IMPType", bean.get("imptype"));
				data.put("Alert1", ToolUtil.getBigDecimal(bean.get("alert1")));
				data.put("Alert2", ToolUtil.getBigDecimal(bean.get("alert2")));
				data.put("Disconnect", ToolUtil.divide(bean.get("disconnect"), 3600, 0));		
				data.put("Temperature1", ToolUtil.getBigDecimal(bean.get("temperature1")));
			}
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
