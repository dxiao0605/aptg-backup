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

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;



/**
 * Servlet implementation class updBatteryGroup 修改站台
 */
@WebServlet("/updBatteryGroup")
public class updBatteryGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updBatteryGroup.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updBatteryGroup() {
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
		logger.debug("updBatteryGroup start");
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
					BatteryGroupVO batteryGroupVO = this.parseJson(req, resource);
					if(batteryGroupVO.isError()) {
						rspJson.put("code", batteryGroupVO.getCode());
						rspJson.put("msg", batteryGroupVO.getDescription());
					}else {						
						BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
						List<DynaBean> list = batteryGroupDAO.checkGroupID(batteryGroupVO);
						if (list != null && list.size() > 0) {
							//檢核站台號碼不重複
							rspJson.put("code", "20");				
							rspJson.put("msg", resource.getString("1012") + resource.getString("5032"));// 站台號碼重複			
					    }else {					    	
							batteryGroupDAO.updBatteryGroup(batteryGroupVO);
							
							rspJson.put("code", "00");				
							rspJson.put("msg", resource.getString("5002"));//保存成功	
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
		logger.debug("updBatteryGroup end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return BatteryGroupVO
	 * @throws Exception
	 */
	private BatteryGroupVO parseJson(String json, ResourceBundle resource) throws Exception {
		BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
		try {
			JSONObject request = new JSONObject(json);
			if(ToolUtil.isNull(request, "GroupInternalID")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("5008"));//必填欄位不能為空
			}else {
				batteryGroupVO.setGroupInternalId(request.optString("GroupInternalID"));
				BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
				batteryGroupDAO.getBatteryGroup(batteryGroupVO);
				List<DynaBean> list = batteryGroupDAO.getBatteryGroup(batteryGroupVO);
				if (list != null && !list.isEmpty()) {
					batteryGroupVO.setCompanyCode(ObjectUtils.toString(list.get(0).get("companycode")));
				}				
			}
			if(ToolUtil.isNull(request, "GroupName")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("1013")+resource.getString("5008"));//站台名稱必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("GroupName"), 20)) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("25");
				batteryGroupVO.setDescription(resource.getString("1013")+resource.getString("5024"));//站台名稱長度不符
			}else {
				batteryGroupVO.setGroupName(request.optString("GroupName"));
			}
			
			if(ToolUtil.isNull(request, "GroupID")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("1012")+resource.getString("5008"));//站台號碼必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("GroupID"), 20)) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("25");
				batteryGroupVO.setDescription(resource.getString("1012")+resource.getString("5024"));//站台號碼長度不符			
			}else {
				batteryGroupVO.setGroupID(request.optString("GroupID"));
			}
		    
			if(ToolUtil.isNull(request, "Country")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("1028")+resource.getString("5008"));//國家必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("Country"), 20)) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("25");
				batteryGroupVO.setDescription(resource.getString("1028")+resource.getString("5024"));//國家長度不符
			}else {
				batteryGroupVO.setCountry(request.optString("Country"));
			}
			
			if(ToolUtil.isNull(request, "Area")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("1029")+resource.getString("5008"));//地域必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("Area"), 20)) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("25");
				batteryGroupVO.setDescription(resource.getString("1029")+resource.getString("5024"));//地域長度不符
			}else {
				batteryGroupVO.setArea(request.optString("Area"));
			}
			
			if(ToolUtil.isNull(request, "Address")) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("24");
				batteryGroupVO.setDescription(resource.getString("1031")+resource.getString("5008"));//地址必填欄位不能為空
			}else if(!ToolUtil.lengthCheck(request.optString("Address"), 200)) {
				batteryGroupVO.setError(true);
				batteryGroupVO.setCode("25");
				batteryGroupVO.setDescription(resource.getString("1031")+resource.getString("5024"));//地址長度不符
			}else {
				batteryGroupVO.setAddress(request.optString("Address"));
				JSONObject address = ToolUtil.getLonLat(request.optString("Address"));
				
				//20220118 By David
								
				if(address!=null) {
					
					if(address.optString("error").equals("1")) {
						batteryGroupVO.setError(true);
						batteryGroupVO.setCode("27");
						batteryGroupVO.setDescription(resource.getString(address.optString("error_code")));//經緯度的錯誤訊息，超過數值
					}
					else {
					if(address.optString("special-key").equals("1"))
						batteryGroupVO.setAddress(request.optString("Address").replace("@@",","));  //將原始地址@@ 用,號取代
						
					batteryGroupVO.setLat(address.optString("lat"));
					batteryGroupVO.setLng(address.optString("lng"));
					}
				}else {
					
					batteryGroupVO.setError(true);
					batteryGroupVO.setCode("27");
					batteryGroupVO.setDescription(resource.getString("5068"));//站台設定地址後查不到經緯度的錯誤訊息
                    //20220118 By David
					
				}
				
				
			}			
			
			batteryGroupVO.setDefaultGroup("1");
			batteryGroupVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryGroupVO;
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
