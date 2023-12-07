package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AreaVO;

/**
 * Servlet implementation class updArea 修改區域資料
 */
@WebServlet("/updArea")
public class updArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updArea.class.getName());
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updArea() {
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
		logger.debug("updArea start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					AreaVO areaVO = this.parseJson(req);
					if(areaVO.isError()) {
						rspJson.put("code", areaVO.getCode());
						rspJson.put("msg", areaVO.getDescription());
					}else {						
						AreaDAO areaDAO = new AreaDAO();
						if(areaDAO.checkAreaCode(areaVO.getAreaCodeNo(), areaVO.getAreaCode())) {
							rspJson.put("code", "22");
							rspJson.put("msg", "區域編號已存在");
						}else if(areaDAO.checkAreaName(areaVO.getAreaCodeNo(), areaVO.getAreaName())) {
							rspJson.put("code", "22");
							rspJson.put("msg", "區域名稱已存在");						
						}else {
							areaDAO.updArea(areaVO);
							ToolUtil.addLogRecord(areaVO.getUserName(), "24", "修改"+areaVO.getAreaCode()+"-"+areaVO.getAreaName());
							
							List<DynaBean> list = areaDAO.getAreaList();
							if (list != null && !list.isEmpty()) {
								JSONObject msg = convertToJson(list);
								msg.put("Message", "Update Success");					
								rspJson.put("msg", msg);																	
								rspJson.put("code", "00");
							} else {
								rspJson.put("code", "07");
								rspJson.put("msg", "查無資料");
							}
							
							
							
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
		logger.debug("updArea end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return adminSetupVO
	 * @throws Exception
	 */
	private AreaVO parseJson(String json) throws Exception {
		AreaVO areaVO = new AreaVO();
		try {
			JSONObject request = new JSONObject(json);

			if(ToolUtil.isNull(request, "AreaCodeNo")) {
				areaVO.setError(true);
				areaVO.setCode("15");
				areaVO.setDescription("區域序號不能為空");
				return areaVO;
			}else {
				areaVO.setAreaCodeNo(request.optString("AreaCodeNo"));	
			}
			
			if(ToolUtil.isNull(request, "AreaCode")) {
				areaVO.setError(true);
				areaVO.setCode("15");
				areaVO.setDescription("區域編號不能為空");
				return areaVO;
			}else if(!ToolUtil.lengthCheck(request.optString("AreaCode"), 2)) {
				areaVO.setError(true);
				areaVO.setCode("12");
				areaVO.setDescription("區域編號超過長度限制");
				return areaVO;
			}else {
				areaVO.setAreaCode(StringUtils.upperCase(request.optString("AreaCode")));
			}
			
			if(ToolUtil.isNull(request, "AreaName")) {
				areaVO.setError(true);
				areaVO.setCode("15");
				areaVO.setDescription("區域名稱不能為空");
				return areaVO;
			}else if(!ToolUtil.lengthCheck(request.optString("AreaName"), 8)) {
				areaVO.setError(true);
				areaVO.setCode("12");
				areaVO.setDescription("區域名稱超過長度限制");
				return areaVO;
			}else {
				areaVO.setAreaName(request.optString("AreaName"));	
			}

			if(ToolUtil.isNull(request, "AccessBanks")) {
				areaVO.setError(true);
				areaVO.setCode("15");
				areaVO.setDescription("最少必須有一間分行，不可為空");
				return areaVO;
			}else {
				List<String> accessBanksList = new ArrayList<String>();
				JSONArray bankArr = request.optJSONArray("AccessBanks");
				if(bankArr.length()<=0) {
					areaVO.setError(true);
					areaVO.setCode("15");
					areaVO.setDescription("最少必須有一間分行，不可為空");
					return areaVO;
				}else {
					for(int i=0; i<bankArr.length(); i++) {
						accessBanksList.add(bankArr.getString(i));
					}				
					areaVO.setAccessBanksList(accessBanksList);
				}				
			}

			areaVO.setUserName(request.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return areaVO;
	}
	
	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		AreaDAO areaDAO = new AreaDAO();
		JSONObject data = new JSONObject();
		try {
			JSONArray areaArr = new JSONArray();
			for(int i=0; i<rows.size(); i++) {
				JSONObject area = new JSONObject();
				DynaBean bean = rows.get(i);

				String areaCodeNo = ObjectUtils.toString(bean.get("seqno"));
				area.put("AreaCodeNo", areaCodeNo);
				area.put("AreaCode", ObjectUtils.toString(bean.get("areacode")));
				area.put("AreaName", ObjectUtils.toString(bean.get("areaname")));
				
				JSONArray bankArr = new JSONArray();
				List<DynaBean> banklist = areaDAO.getAccessBanks(areaCodeNo);
				if (banklist != null && !banklist.isEmpty()) {
					for(DynaBean b:banklist) {
					JSONObject bank = new JSONObject();
					bank.put("BankCode", ObjectUtils.toString(b.get("bankcode")));
					bank.put("BankName", ObjectUtils.toString(b.get("bankname")));
					bankArr.put(bank);
					}
				}							
				area.put("AccessBanks", bankArr);
								
				JSONArray areaManager = new JSONArray();
				JSONArray areaUser = new JSONArray();
				List<DynaBean> accountList = areaDAO.getAreaAccount(areaCodeNo);
				if (accountList != null && !accountList.isEmpty()) {
					for(DynaBean a:accountList) {
					int rankCode = ToolUtil.parseInt(a.get("rankcode"));
						if(rankCode==3) {
							areaManager.put(a.get("account"));
						}else if(rankCode==4) {
							areaUser.put(a.get("account"));
						}
					}
				}	
				area.put("AreaManager", areaManager);
				area.put("AreaUser", areaUser);
								
				areaArr.put(area);
			}
			data.put("List", areaArr);
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
