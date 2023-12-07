package aptg.cathaybkeco.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.net.SocketFactory;
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

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getMeterSetup 電表資料/列表
 */
@WebServlet("/getMeterSetup")
public class getMeterSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterSetup() {
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
		logger.debug("getMeterSetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId")); 
			String eco5Account = ObjectUtils.toString(request.getParameter("eco5Account"));	 
			String enabled = ObjectUtils.toString(request.getParameter("enabled"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode + ",DeviceID:" + deviceId);
			logger.debug("ECO5Account:" + eco5Account+ ",Enabled:" + enabled);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setBankCode(bankCode);
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setEco5Account(eco5Account);
					meterSetupVO.setEnabled(enabled);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getMeterSetup(meterSetupVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
					if (list != null && list.size() > 0) {
						rspJson.put("msg", convertToJson(list));											
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
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
		logger.debug("getMeterSetup end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			String powerAccount ="", deviceID = "";
			JSONArray list = new JSONArray();
			for (DynaBean bean : rows) {
				JSONObject meterSetup = new JSONObject();
				//電表資訊
				meterSetup.put("DeviceID", bean.get("deviceid"));
				meterSetup.put("ECO5Account", bean.get("eco5account"));//ECO5帳號
				meterSetup.put("MeterID", bean.get("meterid"));//電表站號
				meterSetup.put("MeterName", bean.get("metername"));//電表名稱
				meterSetup.put("MeterTypeCode", bean.get("metertypecode"));//電表型號
				meterSetup.put("MeterType", bean.get("metertype"));
				meterSetup.put("InstallPosition", ObjectUtils.toString(bean.get("installposition")));//電表安裝位置
				meterSetup.put("WiringCode", bean.get("wiringcode"));//接線方式
				meterSetup.put("WiringDesc", bean.get("wiringdesc"));
				meterSetup.put("TreeChartID", ObjectUtils.toString(bean.get("treechartid")));//樹狀圖編號
				meterSetup.put("UsageCode", bean.get("usagecode"));//耗能分類
				meterSetup.put("UsageDesc", bean.get("usagedesc"));
				meterSetup.put("PowerAccount", bean.get("poweraccount"));//電號
				meterSetup.put("RatePlanCode", bean.get("rateplancode"));//電價模式
				meterSetup.put("RatePlanDesc", bean.get("rateplandesc"));
				meterSetup.put("AreaName", ObjectUtils.toString(bean.get("areaname")));//用電區域
				meterSetup.put("Area", bean.get("area")!= null ? bean.get("area") : "");//區域面積
				meterSetup.put("People", bean.get("people")!= null ? bean.get("people") : "");//區域人數
				meterSetup.put("PowerFactorEnabled", bean.get("powerfactorenabled"));//功率因素補償費
				meterSetup.put("EnabledDesc", ToolUtil.getEnabled(bean.get("enabled")));
				meterSetup.put("Enabled", bean.get("enabled"));
				//需量測試
				meterSetup.put("DFEnabled", bean.get("dfenabled"));	//需量預測警報啟用狀態
				meterSetup.put("DFEnabledDesc", ToolUtil.getEnabled(bean.get("dfenabled")));
				meterSetup.put("DFCode", bean.get("dfcode"));	//需量預測模式
				meterSetup.put("dfname", bean.get("dfname"));
				meterSetup.put("DFCycle", bean.get("dfcycle")!= null ? bean.get("area") : "");	//需量預測週期
				meterSetup.put("DFUpLimit", bean.get("dfuplimit")!= null ? bean.get("dfuplimit") : "");	//需量預測上限
				meterSetup.put("DFLoLimit", bean.get("dflolimit")!= null ? bean.get("dflolimit") : "");	//需量預測下限

				//警報
				meterSetup.put("CurAlertEnabled", bean.get("curalertenabled"));	//電流警報啟用狀態
				meterSetup.put("CurAlertEnabledDesc", ToolUtil.getEnabled(bean.get("curalertenabled")));
				meterSetup.put("CurUpLimit", bean.get("curuplimit")!= null ? bean.get("curuplimit") : "");	//電流上限(額定電流)
				meterSetup.put("CurLoLimit", bean.get("curlolimit")!= null ? bean.get("curlolimit") : "");	//電流下限
				meterSetup.put("VolAlertEnabled", bean.get("volalertenabled"));	//電壓警報啟用狀態
				meterSetup.put("VolAlertEnabledDesc", ToolUtil.getEnabled(bean.get("volalertenabled")));
				meterSetup.put("VolAlertType", bean.get("volalerttype"));	//電壓警報類型
				meterSetup.put("VolUpLimit", bean.get("voluplimit")!= null ? bean.get("voluplimit") : "");	//電壓上限(額定電壓)
				meterSetup.put("VolLoLimit", bean.get("vollolimit")!= null ? bean.get("vollolimit") : "");	//電壓下限
				meterSetup.put("ECAlertEnabled", bean.get("ecalertenabled"));	//用電量警報啟用狀態
				meterSetup.put("ECAlertEnabledDesc", ToolUtil.getEnabled(bean.get("ecalertenabled")));
				meterSetup.put("ECUpLimit", bean.get("ecuplimit")!= null ? bean.get("ecuplimit") : "");	//用電量上限
				
				//契約容量
				meterSetup.put("UsuallyCC", bean.get("usuallycc")!= null ? bean.get("usuallycc") : "");//經常契約容量
				meterSetup.put("SPCC", bean.get("spcc")!= null ? bean.get("spcc") : "");//半尖峰契約容量(非夏月)
				meterSetup.put("SatSPCC", bean.get("satspcc")!= null ? bean.get("satspcc") : "");//週六半尖峰契約容量
				meterSetup.put("OPCC", bean.get("opcc")!= null ? bean.get("opcc") : "");//離峰契約容量

				meterSetup.put("RatedPower", bean.get("ratedpower")!= null ? bean.get("ratedpower") : "");//額定功率
				meterSetup.put("RatedVol", bean.get("ratedvol")!= null ? bean.get("ratedvol") : "");//額定電壓
				meterSetup.put("RatedCur", bean.get("ratedcur")!= null ? bean.get("ratedcur") : "");//額定電流
				meterSetup.put("EquipDesc", bean.get("equipdesc"));//用電設備說明
				
//				if(ToolUtil.add(bean.get("eventcount1"), bean.get("eventcount2")).compareTo(new BigDecimal(0))>0) {
//					meterSetup.put("Priority", 2);
//				}else if(ToolUtil.add(bean.get("eventcount3"), bean.get("eventcount4")).compareTo(new BigDecimal(0))>0) {
//					meterSetup.put("Priority", 1);
//				}else {
//					meterSetup.put("Priority", 0);
//				}		
				powerAccount = ObjectUtils.toString(bean.get("poweraccount"));
				deviceID = ObjectUtils.toString(bean.get("deviceid"));
				list.put(meterSetup);
			}
			data.put("MeterSetup", list);
			
//			this.sendSocket(powerAccount, deviceID);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	private void sendSocket(String powerAccount, String deviceID) {
		 String address = "127.0.0.1";// 連線的ip
		  int port = 7500;// 連線的port
		  try {
	            // 送出字串
//	            String request = "*1;A50;00000004;gatewayID;deviceID;";
	            String request = "#1;A50;00000004;"+powerAccount+";"+deviceID+";";
	            
	        	Socket socket = SocketFactory.getDefault().createSocket(address, port);
	        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        	out.println(request);
	        	logger.debug("client send: "+request);
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String response = reader.readLine();
	        	logger.debug("client get: "+response);
	        	
	            socket.close();
	            socket = null;
	            
	        } catch (java.io.IOException e) {
	            logger.debug("Socket連線有問題 !");
	            logger.debug("IOException :" + e.getMessage());
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
