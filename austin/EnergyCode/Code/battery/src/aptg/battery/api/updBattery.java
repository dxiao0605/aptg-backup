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

import aptg.battery.dao.BatteryDAO;
import aptg.battery.dao.BatteryTypeListDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryTypeListVO;
import aptg.battery.vo.BatteryVO;



/**
 * Servlet implementation class updBattery 修改電池組
 */
@WebServlet("/updBattery")
public class updBattery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updBattery.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updBattery() {
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
		logger.debug("updBattery start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
//			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			resource = ToolUtil.getLanguage(language);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					BatteryVO batteryVO = this.parseJson(req, timezone, language);
					if(batteryVO.isError()) {
						rspJson.put("code", batteryVO.getCode());
						rspJson.put("msg", batteryVO.getDescription());
					}else {
						BatteryDAO batteryDAO = new BatteryDAO();
						batteryDAO.updBattery(batteryVO);
						
						rspJson.put("code", "00");				
						rspJson.put("msg", resource.getString("5002"));//保存成功
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
		logger.debug("updBattery end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param timezone
	 * @param language
	 * @return
	 * @throws Exception
	 */
	private BatteryVO parseJson(String json, String timezone, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd", timezone);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		BatteryVO batteryVO = new BatteryVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");

			if(ToolUtil.isNull(msg, "BatteryGroupID")) {
				batteryVO.setError(true);
				batteryVO.setCode("24");
				batteryVO.setDescription(resource.getString("1026")+resource.getString("5008"));//電池組ID不能為空
				return batteryVO;				
			}else {
				String[] batteryGroupID = msg.getString("BatteryGroupID").split("_");
				batteryVO.setNbId(batteryGroupID[0]);
				batteryVO.setBatteryId(batteryGroupID[1]);
			}
			
			if(ToolUtil.isNull(msg, "BatteryType")) {
				batteryVO.setBatteryTypeCode(null);
			}else {
				JSONObject batteryType = msg.getJSONObject("BatteryType");
				String name = batteryType.optString("Label");
				String code = batteryType.optString("Value");
				if("N".equals(code)) {
					batteryVO.setBatteryTypeCode(null);
				}else if (StringUtils.isNotBlank(name) && StringUtils.isBlank(code)) {
					if(!ToolUtil.strCheck(name)) {
						batteryVO.setError(true);
	        			batteryVO.setCode("27");	
	    	        	batteryVO.setDescription(resource.getString("1506")+resource.getString("5034"));//電池型號格式錯誤
	    	        	return batteryVO;	
	        		}
					
					BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();	
					BatteryDAO batteryDAO = new BatteryDAO();					
					List<DynaBean> company = batteryDAO.getBatteryCompany(batteryVO);
					if (company != null && !company.isEmpty()) {
						String companyCode = ObjectUtils.toString(company.get(0).get("companycode"));
						batteryTypeListVO.setCompanyCode(companyCode);
						batteryVO.setCompanyCode(companyCode);
					}
	
					batteryVO.setBatteryTypeName(name);
					batteryTypeListVO.setBatteryTypeName(name);
					batteryTypeListVO.setUserName(msg.optString("UserName"));
					
					BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
					List<DynaBean> checkType = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
					if (checkType != null && !checkType.isEmpty()) {
						batteryVO.setError(true);
	        			batteryVO.setCode("28");	
	    	        	batteryVO.setDescription(resource.getString("1506")+resource.getString("5035"));//電池型號重複
	    	        	return batteryVO;
					}
					batteryVO.setAddBattTypeFlag(true);
				}else if (StringUtils.isBlank(name) && StringUtils.isBlank(code)) {
					batteryVO.setBatteryTypeCode(null);
				}else {
					batteryVO.setBatteryTypeCode(code);	
				}				
			}
			if(ToolUtil.isNull(msg, "InstallDate")) {
				batteryVO.setInstallDate(null);			
			}else {
				String installDate = msg.optString("InstallDate");
				if (!ToolUtil.dateCheck(installDate, "yyyy-MM-dd")) {
					batteryVO.setError(true);
					batteryVO.setCode("16");
					batteryVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd)");// 日期格式錯誤
					return batteryVO;
				}else {
//					batteryVO.setInstallDate(ToolUtil.dateFormat(sdf.parse(installDate), sdf2));
					batteryVO.setInstallDate(ToolUtil.dateFormat(sdf2.parse(installDate), sdf2));	// edit by Austin (2021/12/02)
				}					
			}

			batteryVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
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
