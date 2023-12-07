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

import aptg.battery.bean.AuthoritySetupBean;
import aptg.battery.bean.MsgBean;
import aptg.battery.bean.P1200;
import aptg.battery.bean.P1300;
import aptg.battery.bean.P1400;
import aptg.battery.bean.P1500;
import aptg.battery.bean.P1600;
import aptg.battery.bean.P1700;
import aptg.battery.bean.P1800;
import aptg.battery.dao.RoleDAO;
import aptg.battery.util.JsonUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.RoleVO;


/**
 * Servlet implementation class getRoleAuthority 角色權限
 */
@WebServlet("/getRoleAuthority")
public class getRoleAuthority extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getRoleAuthority.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getRoleAuthority() {
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
		logger.debug("getRoleAuthority start");
		String rspStr = new String();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String roleId = ObjectUtils.toString(request.getParameter("roleId"));
			logger.debug("RoleId: " + roleId);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(roleId)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspStr = ToolUtil.getErrRspJson("身分驗證失敗", "02");
				} else {
					RoleVO roleVO = new RoleVO();
					roleVO.setSystemId(ToolUtil.getSystemId());
					roleVO.setRoleId(roleId);

					RoleDAO roleDAO = new RoleDAO();
					List<DynaBean> roleList = roleDAO.getRole(roleVO);
					List<DynaBean> authorityList = roleDAO.getRoleAuthority(roleVO);
					if (authorityList != null && !authorityList.isEmpty()) {
						rspStr = JsonUtil.getInstance().convertObjectToJsonstring(convertToJson(roleList, authorityList, language));
					} else {
						rspStr = ToolUtil.getErrRspJson(resource.getString("5004"), "07");// 查無資料
					}
				}
			} else {
				rspStr = ToolUtil.getErrRspJson("缺少參數", "01");
			}
		} catch (Exception e) {
			rspStr = ToolUtil.getErrRspJson(e.toString(), "99");
			logger.error("", e);
		}
		logger.debug("rsp: " + rspStr);
		ToolUtil.response(rspStr, response);
		logger.debug("getRoleAuthority end");
		
	}

	/**
	 * 組Json
	 * @param roleList
	 * @param authorityList
	 * @param language
	 * @return
	 * @throws Exception
	 */
	private MsgBean convertToJson(List<DynaBean> roleList, List<DynaBean> authorityList, String language) throws Exception {
		MsgBean msgBean = new MsgBean();
		try {
			AuthoritySetupBean authorityBean = new AuthoritySetupBean();
			DynaBean roleBean = roleList.get(0);
			authorityBean.setRoleName(ObjectUtils.toString(roleBean.get("rolename")));
			if("3".equals(language)) {
				authorityBean.setRoleDesc(ObjectUtils.toString(roleBean.get("roledescj")));
			}else if("2".equals(language)) {
				authorityBean.setRoleDesc(ObjectUtils.toString(roleBean.get("roledesce")));
			}else {
				authorityBean.setRoleDesc(ObjectUtils.toString(roleBean.get("roledesc")));	
			}
			P1200 p1200 = new P1200();// 總覽
			P1300 p1300 = new P1300();// 告警
			P1400 p1400 = new P1400();// 電池數據
			P1500 p1501 = new P1500();// 電池組管理
			P1500 p1502 = new P1500();// 站台管理
			P1500 p1503 = new P1500();// 通訊序號啟用/停用
			P1500 p1504 = new P1500();// 電池參數設定
			P1600 p1600 = new P1600();// 電池歷史
			P1700 p1700 = new P1700();// 使用者管理
			P1800 p1800 = new P1800();// 系統設定
						
			for (DynaBean bean : authorityList) {
				String functionId = ObjectUtils.toString(bean.get("functionid"));
				String edit = ObjectUtils.toString(bean.get("edit"));
				String buttonId = ObjectUtils.toString(bean.get("buttonid"));
				String enabled = ObjectUtils.toString(bean.get("enabled"));
				if ("1200".equals(functionId)) {
					p1200.setView(true);// 總覽-檢視
				} else if ("1300".equals(functionId)) {
					p1300.setView(true);// 告警-檢視
				} else if (("1301".equals(functionId) || "1302".equals(functionId) || "1303".equals(functionId))
						&& "1".equals(edit)) {
					p1300.setEdit(true);// 告警-解決告警及設定告警條件
				} else if ("1400".equals(functionId)) {
					p1400.setView(true);// 電池數據-檢視
					if (("BattEdit".equals(buttonId) || "GroupEdit".equals(buttonId)) && "1".equals(enabled)) {
						p1400.setEdit(true);// 電池數據-電池組編輯/站台設定
					}
					if (("BatchCmd".equals(buttonId) || "BB".equals(buttonId) || "BA".equals(buttonId)
							|| "B5".equals(buttonId) || "B3".equals(buttonId)) && "1".equals(enabled)) {
						p1400.setSettings(true);// 電池數據-電池參數設定
					}
				} else if ("1501".equals(functionId)) {
					p1501.setView(true);// 電池組管理-檢視
					if ("1".equals(edit)) {
						p1501.setEdit(true);// 電池組管理-異動
					}
				} else if ("1502".equals(functionId)) {
					p1502.setView(true);// 站台管理-檢視
					if ("1".equals(edit)) {
						p1502.setEdit(true);// 站台管理-異動
					}
				} else if ("1503".equals(functionId)) {
					p1503.setView(true);// 通訊序號啟用/停用-檢視
					if ("P1515".equals(buttonId)) {
						p1503.setEdit(true);// 通訊序號啟用/停用-異動
					}
				} else if ("1504".equals(functionId)) {
					p1504.setView(true);// 電池參數設定-檢視
					if ("P1504".equals(buttonId)) {
						p1504.setEdit(true);// 電池參數設定-設定
					}
				} else if ("1600".equals(functionId)) {
					p1600.setView(true);// 電池歷史-檢視
				} else if ("1700".equals(functionId)) {
					p1700.setView(true);// 使用者管理-檢視
				} else if ("1701".equals(functionId)
						&& ("UserCreate".equals(buttonId) || "UserEdit".equals(buttonId)
								|| "UserResetpw".equals(buttonId) || "UserDel".equals(buttonId))
						&& "1".equals(enabled)) {
					p1700.setEdit(true);// 使用者管理-使用者異動
				} else if ("1800".equals(functionId)) {
					p1800.setView(true);// 系統設定-檢視
					if ("P1801".equals(buttonId) && "1".equals(enabled)) {
						p1800.setIMPType(true);// 系統設定-內阻單位設定
					}
					if ("P1802".equals(buttonId) && "1".equals(enabled)) {
						p1800.setCompany(true);// 系統設定-公司設定
					}
					if ("P1815".equals(buttonId) && "1".equals(enabled)) {
						p1800.setCommand(true);// 系統設定-指令限制
					}
				}
			}
			authorityBean.setP1200(p1200);
			authorityBean.setP1300(p1300);
			authorityBean.setP1400(p1400);
			authorityBean.setP1501(p1501);
			authorityBean.setP1502(p1502);
			authorityBean.setP1503(p1503);
			authorityBean.setP1504(p1504);
			authorityBean.setP1600(p1600);
			authorityBean.setP1700(p1700);
			authorityBean.setP1800(p1800);

			msgBean.setMsg(authorityBean);
			msgBean.setCode("00");			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return msgBean;
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
