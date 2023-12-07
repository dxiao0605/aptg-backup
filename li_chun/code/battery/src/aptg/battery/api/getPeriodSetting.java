package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getPeriodSetting 時間週期設定(BA)
 */
@WebServlet("/getPeriodSetting")
public class getPeriodSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPeriodSetting.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPeriodSetting() {
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
		logger.debug("getPeriodSetting start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String batteryGroupId = ObjectUtils.toString(request.getParameter("batteryGroupId"));
			logger.debug("BatteryGroupID: "+batteryGroupId);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(batteryGroupId)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage(language);
					String[] battArr = batteryGroupId.split(",");
					if(battArr.length>1){//多筆電池組
						rspJson.put("msg", getDefaultJson());						
						rspJson.put("code", "00");
					}else {//單筆電池組
						String[] arr = batteryGroupId.split("_");
						BatteryVO batteryVO = new BatteryVO();										
						batteryVO.setNbId(arr[0]);
						batteryVO.setBatteryId(arr[1]);
						batteryVO.setCommandID("BA");
						BatteryDAO batteryDAO = new BatteryDAO();
						List<DynaBean> list = batteryDAO.getBatterySetup(batteryVO);					
						if (list != null && !list.isEmpty()) {						
							rspJson.put("msg", convertToJson(list, timezone));						
							rspJson.put("code", "00");
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
		logger.debug("getPeriodSetting end");
	}

	/**
	 * 組Json
	 * @param rows
	 * @param impType
	 * @param resource
	 * @param sdf
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows,String timezone) throws Exception {
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
		JSONObject data = new JSONObject();
		try {								
			DynaBean bean = rows.get(0);
			if(bean.get("responsetime")!=null) {
				data.put("UploadCycle", bean.get("uploadcycle")!=null?ToolUtil.getBigDecimal(bean.get("uploadcycle")):"");//主板上傳週期時間
				data.put("IRCycle", bean.get("ircycle")!=null?ToolUtil.getBigDecimal(bean.get("ircycle")):"");//內阻測試週期時間
				data.put("CommunicationCycle",
						bean.get("communicationcycle") != null ? ToolUtil.getBigDecimal(bean.get("communicationcycle"))
								: "");// 子板通信週期時間
				data.put("LastSettingTime", ToolUtil.dateFormat(bean.get("responsetime"), sdf));//最後設定時間
			}else {
				data = getDefaultJson();
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 預設時間週期設定值
	 * @return
	 * @throws Exception
	 */
	private JSONObject getDefaultJson() throws Exception {
		JSONObject data = new JSONObject();
		try {											
			data.put("UploadCycle", ToolUtil.parseInt(SysConfig.getInstance().getUploadCycle()));//主板上傳週期時間
			data.put("IRCycle", ToolUtil.parseInt(SysConfig.getInstance().getIRCycle()));//內阻測試週期時間
			data.put("CommunicationCycle", ToolUtil.parseInt(SysConfig.getInstance().getCommunicationCycle()));// 子板通信週期時間
			data.put("LastSettingTime", "NA");//最後設定時間	
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
