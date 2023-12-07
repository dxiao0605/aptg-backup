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

import aptg.battery.dao.BatteryTypeListDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryTypeListVO;



/**
 * Servlet implementation class addBatteryType 新增電池型號
 */
@WebServlet("/addBatteryType")
public class addBatteryType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addBatteryType.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addBatteryType() {
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
		logger.debug("addBatteryType start");
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
					BatteryTypeListVO batteryTypeListVO = this.parseJson(req, resource);				
					if(batteryTypeListVO.isError()) {
						rspJson.put("code", batteryTypeListVO.getCode());
						rspJson.put("msg", batteryTypeListVO.getDescription());
					}else {
						BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
						batteryTypeListDAO.addBatteryType(batteryTypeListVO);
												
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
		logger.debug("addBatteryType end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Json
	 * @param json
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private BatteryTypeListVO parseJson(String json, ResourceBundle resource) throws Exception {
		BatteryTypeListVO batteryTypeListVO = new BatteryTypeListVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			if(ToolUtil.isNull(msg, "CompanyCode")) {
				batteryTypeListVO.setError(true);
				batteryTypeListVO.setCode("24");
				batteryTypeListVO.setDescription(resource.getString("1064")+resource.getString("5008"));//公司必填欄位不能為空
				return batteryTypeListVO;
			}else {
				batteryTypeListVO.setCompanyCode(msg.optString("CompanyCode"));	
			}
			
			if(ToolUtil.isNull(msg, "BatteryTypeName")) {
				batteryTypeListVO.setError(true);
				batteryTypeListVO.setCode("24");
				batteryTypeListVO.setDescription(resource.getString("1506")+resource.getString("5008"));//必填欄位不能為空
				return batteryTypeListVO;
			}else if(!ToolUtil.lengthCheck(msg.optString("BatteryTypeName"), 100)) {
				batteryTypeListVO.setError(true);
				batteryTypeListVO.setCode("25");
				batteryTypeListVO.setDescription(resource.getString("1506")+resource.getString("5024"));//長度不符
				return batteryTypeListVO;
			}else if(!ToolUtil.strCheck(msg.optString("BatteryTypeName"))) {
				batteryTypeListVO.setError(true);
				batteryTypeListVO.setCode("27");	
				batteryTypeListVO.setDescription(resource.getString("1506")+resource.getString("5034"));//電池型號格式錯誤
				return batteryTypeListVO;
			}else {
				batteryTypeListVO.setBatteryTypeName(msg.optString("BatteryTypeName"));
			}
			
			if (checkBattTypeName(batteryTypeListVO)) {//檢核電池型號重複
				batteryTypeListVO.setError(true);
				batteryTypeListVO.setCode("28");	
				batteryTypeListVO.setDescription(resource.getString("1506")+resource.getString("5035"));//電池型號重複
				return batteryTypeListVO;
			}
			
			batteryTypeListVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryTypeListVO;
	}
	
	/**
	 * 檢核電池型號重複
	 * @param batteryTypeListVO
	 * @return
	 * @throws Exception
	 */
	private boolean checkBattTypeName(BatteryTypeListVO batteryTypeListVO) throws Exception {
		BatteryTypeListDAO batteryTypeListDAO = new BatteryTypeListDAO();
		List<DynaBean> checkType = batteryTypeListDAO.getBatteryTypeList(batteryTypeListVO);
		if (checkType != null && !checkType.isEmpty()) {
        	return true;
		}else {
			return false;
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
