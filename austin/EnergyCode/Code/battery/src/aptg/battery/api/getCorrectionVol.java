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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class getCorrectionVol 校正電壓(B5)
 */
@WebServlet("/getCorrectionVol")
public class getCorrectionVol extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCorrectionVol.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCorrectionVol() {
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
		logger.debug("getCorrectionVol start");
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
					BatteryVO batteryVO = new BatteryVO();	
					String[] arr = batteryGroupId.split("_");
					batteryVO.setNbId(arr[0]);
					batteryVO.setBatteryId(arr[1]);
					batteryVO.setCommandID("B5");
					batteryVO.setCategory("2");
					BatteryDAO batteryDAO = new BatteryDAO();
					List<DynaBean> list = batteryDAO.getBatteryDetailSetup(batteryVO);					
					if (list != null && !list.isEmpty()) {						
						rspJson.put("msg", convertToJson(list, timezone));						
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));//查無資料
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
		logger.debug("getCorrectionVol end");
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
			DynaBean batt = rows.get(0);
			int count = ToolUtil.parseInt(batt.get("count"));
			int setCo = rows.size();
			int defaultVol = ToolUtil.parseInt(SysConfig.getInstance().getVol());
			
			JSONArray arr = new JSONArray();
			if(count!=setCo) {				
				for(int i=0; i<count; i++) {
					arr.put(defaultVol);//預設
				}
				data.put("LastSettingTime", "NA");//預設NA
			}else {
				for(DynaBean bean : rows) {
					if(bean.get("correctionvalue")!=null) {
						arr.put(ToolUtil.getBigDecimal(bean.get("correctionvalue")));
					}else {
						arr.put(defaultVol);//預設
					}					
				}
				if(batt.get("responsetime")!=null) {
					data.put("LastSettingTime", ToolUtil.dateFormat(batt.get("responsetime"), sdf));//最後設定時間	
				}else {
					data.put("LastSettingTime", "NA");//預設NA
				}				
			}
						
			data.put("Vol", arr);			
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
