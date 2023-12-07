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
import org.json.JSONObject;

import aptg.battery.bean.AuthorityBean;
import aptg.battery.bean.ButtonBean;
import aptg.battery.dao.RoleDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.RoleVO;



/**
 * Servlet implementation class updRoleAuthority 權限設定
 */
@WebServlet("/updRoleAuthority")
public class updRoleAuthority extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updRoleAuthority.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updRoleAuthority() {
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
		logger.debug("updRoleAuthority start");
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
					RoleVO roleVO = this.parseJson(req, timezone, language);
					if(roleVO.isError()) {
						rspJson.put("code", roleVO.getCode());
						rspJson.put("msg", roleVO.getDescription());
					}else {
						RoleDAO roleDAO = new RoleDAO();
						roleDAO.updRoleAuthority(roleVO);
						
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
		logger.debug("updRoleAuthority end");
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
	private RoleVO parseJson(String json, String timezone, String language) throws Exception {
		ResourceBundle resource = ToolUtil.getLanguage(language);
		RoleVO roleVO = new RoleVO();
		try {
			JSONObject request = new JSONObject(json);
			int systemId = Integer.parseInt(ToolUtil.getSystemId());
			int roleid, authorityId = 0;
			if(ToolUtil.isNull(request, "RoleId")) {
				roleVO.setError(true);
				roleVO.setCode("24");
				roleVO.setDescription(resource.getString("1710")+resource.getString("5008"));//角色不能為空
				return roleVO;		
			}else {
				roleid = request.optInt("RoleId");
				roleVO.setRoleId(request.optString("RoleId"));
				roleVO.setSystemId(String.valueOf(systemId));
				//取得權限ID
				RoleDAO roleDAO = new RoleDAO();
				List<DynaBean> authorityList = roleDAO.getAuthorityId(roleVO);
				if (authorityList != null && !authorityList.isEmpty()) {
					DynaBean bean = authorityList.get(0);
					authorityId = ToolUtil.parseInt(bean.get("authorityid"));
					roleVO.setAuthorityId(ObjectUtils.toString(bean.get("authorityid")));
				}
			}
			
//			JSONObject p1200 = request.getJSONObject("P1200");// 總覽(目前不處理)
			JSONObject p1300 = request.getJSONObject("P1300");// 告警
			JSONObject p1400 = request.getJSONObject("P1400");// 電池數據
			JSONObject p1501 = request.getJSONObject("P1501");// 電池組管理
			JSONObject p1502 = request.getJSONObject("P1502");// 站台管理
			JSONObject p1503 = request.getJSONObject("P1503");// 通訊序號啟用/停用
			JSONObject p1504 = request.getJSONObject("P1504");// 電池參數設定
			JSONObject p1600 = request.getJSONObject("P1600");// 電池歷史
			JSONObject p1700 = request.getJSONObject("P1700");// 使用者管理
			JSONObject p1800 = request.getJSONObject("P1800");// 系統設定
			List<AuthorityBean> authorityList = new ArrayList<AuthorityBean>(); 
			List<ButtonBean> buttonList = new ArrayList<ButtonBean>();
			
			// 總覽-檢視(目前皆呈顯)
			authorityList.add(this.getAuthorityBean(systemId, authorityId, 1200, 1));
			
			//告警
			if(p1300.getBoolean("View")) {// 告警-檢視
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1300, 1));
				if(p1300.getBoolean("Edit")) {// 告警-解決告警及設定告警條件
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1301, 1));
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1302, 1));
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1303, 1));
				}else {
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1301, 0));
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1302, 0));
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1303, 0));
				}
			}
			
			//電池數據
			if(p1400.getBoolean("View")) {// 電池數據-檢視
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1400, 1));
				buttonList.add(this.getButtonBean(authorityId, 5, "QueryEvent", "電池數據_點點點__查看告警", 1));
				if(p1400.getBoolean("Edit")) {// 電池數據-電池組編輯/站台設定
					buttonList.add(this.getButtonBean(authorityId, 5, "BattEdit", "電池數據_點點點__電池組編輯", 1));
					buttonList.add(this.getButtonBean(authorityId, 5, "GroupEdit", "電池數據_點點點__站台編輯", 1));
				}else {
					buttonList.add(this.getButtonBean(authorityId, 5, "BattEdit", "電池數據_點點點__電池組編輯", 3));
					buttonList.add(this.getButtonBean(authorityId, 5, "GroupEdit", "電池數據_點點點__站台編輯", 3));
				}
				
				if(p1400.getBoolean("Settings")) {// 電池數據-電池參數設定
					buttonList.add(this.getButtonBean(authorityId, 5, "BatchCmd", "電池數據_批次發送命令", 1));
					buttonList.add(this.getButtonBean(authorityId, 5, "BB", "電池數據_點點點_內阻設定測試值", 1));
					buttonList.add(this.getButtonBean(authorityId, 5, "BA", "電池數據_點點點_時間週期設定", 1));
					buttonList.add(this.getButtonBean(authorityId, 5, "B5", "電池數據_點點點_校正電壓", 1));
					buttonList.add(this.getButtonBean(authorityId, 5, "B3", "電池數據_點點點_校正內阻", 1));
				}else {
					buttonList.add(this.getButtonBean(authorityId, 5, "BatchCmd", "電池數據_批次發送命令", 3));
					buttonList.add(this.getButtonBean(authorityId, 5, "BB", "電池數據_點點點_內阻設定測試值", 3));
					buttonList.add(this.getButtonBean(authorityId, 5, "BA", "電池數據_點點點_時間週期設定", 3));
					buttonList.add(this.getButtonBean(authorityId, 5, "B5", "電池數據_點點點_校正電壓", 3));
					buttonList.add(this.getButtonBean(authorityId, 5, "B3", "電池數據_點點點_校正內阻", 3));
				}				
			}
			
			boolean addP1500 = false;
			//電池組管理
			if(p1501.getBoolean("View")) {// 電池組管理-檢視
				addP1500 = true;
				if(p1501.getBoolean("Edit")) {// 電池組管理-異動
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1501, 1));	
				}else {
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1501, 0));
				}				
			}
			
			//站台管理
			if(p1502.getBoolean("View")) {// 站台管理-檢視
				addP1500 = true;
				if(p1502.getBoolean("Edit")) {// 站台管理-異動
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1502, 1));	
				}else {
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1502, 0));
				}				
			}
			
			//通訊序號啟用/停用
			if(p1503.getBoolean("View")) {// 通訊序號啟用/停用-檢視
				addP1500 = true;
				buttonList.add(this.getButtonBean(authorityId, 8, "P1539", "異動記錄", 1));				
				buttonList.add(this.getButtonBean(authorityId, 8, "P1572", "接續序號歷史", 1));
				if(p1503.getBoolean("Edit")) {// 通訊序號啟用/停用-異動
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1503, 1));
					buttonList.add(this.getButtonBean(authorityId, 8, "P1515", "啟用與停用", 1));
				}else {
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1503, 0));
				}
				
				if(roleid==0 || roleid==4) {
					buttonList.add(this.getButtonBean(authorityId, 8, "P1513", "匯入與分配", 1));
				}
			}
			
			//電池參數設定
			if(p1504.getBoolean("View")) {// 電池參數設定-檢視
				addP1500 = true;
				buttonList.add(this.getButtonBean(authorityId, 13, "P1559", "參數設定歷史", 1));
				if(p1504.getBoolean("Edit")) {// 電池參數設定-設定
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1504, 1));	
					buttonList.add(this.getButtonBean(authorityId, 13, "P1504", "電池參數設定", 1));
				}else {
					authorityList.add(this.getAuthorityBean(systemId, authorityId, 1504, 0));
				}				
			}
			
			//電池管理
			if(addP1500) {
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1500, 1));
			}
			
			//電池歷史
			if(p1600.getBoolean("View")) {// 電池歷史-檢視
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1600, 1));								
			}
			
			//使用者管理
			if(p1700.getBoolean("View")) {// 使用者管理-檢視
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1700, 1));
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1701, 1));	
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1702, 1));
				if(p1700.getBoolean("Edit")) {// 使用者管理-使用者異動
					buttonList.add(this.getButtonBean(authorityId, 10, "UserCreate", "使用者_新增", 1));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserEdit", "使用者_點點點_編輯", 1));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserResetpw", "使用者_點點點_重置密碼", 1));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserDel", "使用者_點點點_刪除", 1));
				}else {
					buttonList.add(this.getButtonBean(authorityId, 10, "UserCreate", "使用者_新增", 3));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserEdit", "使用者_點點點_編輯", 2));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserResetpw", "使用者_點點點_重置密碼", 3));
					buttonList.add(this.getButtonBean(authorityId, 10, "UserDel", "使用者_點點點_刪除", 3));
				}
				
				if(roleid==0 || roleid==4) {						
					buttonList.add(this.getButtonBean(authorityId, 10, "AddCompany", "使用者_新增公司", 1));
					buttonList.add(this.getButtonBean(authorityId, 11, "RoleCreate", "角色_新增", 1));
					buttonList.add(this.getButtonBean(authorityId, 11, "RolePrivilege", "角色_點點點_權限", 1));
					buttonList.add(this.getButtonBean(authorityId, 11, "RoleDel", "角色_點點點_刪除", 1));
				}else {
					buttonList.add(this.getButtonBean(authorityId, 11, "RoleCreate", "角色_新增", 3));
					buttonList.add(this.getButtonBean(authorityId, 11, "RolePrivilege", "角色_點點點_權限", 2));
					buttonList.add(this.getButtonBean(authorityId, 11, "RoleDel", "角色_點點點_刪除", 3));
				}				
			}
			
			//系統設定
			if(p1800.getBoolean("View")) {// 系統設定-檢視
				authorityList.add(this.getAuthorityBean(systemId, authorityId, 1800, 1));
				if(p1800.getBoolean("IMPType")) {// 系統設定-內阻單位設定
					buttonList.add(this.getButtonBean(authorityId, 12, "P1801", "分頁_內阻單位設定", 1));					
				}else {
					buttonList.add(this.getButtonBean(authorityId, 12, "P1801", "分頁_內阻單位設定", 2));
				}
				
				if(p1800.getBoolean("Company")) {// 系統設定-公司設定
					buttonList.add(this.getButtonBean(authorityId, 12, "P1802", "分頁_公司設定", 1));					
				}else {
					buttonList.add(this.getButtonBean(authorityId, 12, "P1802", "分頁_公司設定", 2));
				}		
				
				if(p1800.getBoolean("Command")) {// 系統設定-指令限制
					buttonList.add(this.getButtonBean(authorityId, 12, "P1815", "分頁_指令限制", 1));					
				}else {
					buttonList.add(this.getButtonBean(authorityId, 12, "P1815", "分頁_指令限制", 2));
				}
				
			}	
			
			roleVO.setAuthorityList(authorityList);
			roleVO.setButtonList(buttonList);
			roleVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return roleVO;
	}
	
	private AuthorityBean getAuthorityBean(int systemId, int authorityId, int functionId, int edit) {
		AuthorityBean authorityBean = new AuthorityBean();
		authorityBean.setSystemId(systemId);
		authorityBean.setAuthorityId(authorityId);
		authorityBean.setFunctionId(functionId);
		authorityBean.setEdit(edit);
		return authorityBean;
	}
	
	private ButtonBean getButtonBean(int authorityId, int programId, String buttonId, String buttonDesc, int enabled) {
		ButtonBean buttonBean = new ButtonBean();
		buttonBean.setAuthorityId(authorityId);
		buttonBean.setProgramId(programId);
		buttonBean.setButtonId(buttonId);
		buttonBean.setButtonDesc(buttonDesc);
		buttonBean.setEnabled(enabled);
		return buttonBean;
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
