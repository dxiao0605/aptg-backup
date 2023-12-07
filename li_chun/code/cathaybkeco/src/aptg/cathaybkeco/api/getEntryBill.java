package aptg.cathaybkeco.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.EntryBillDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.EntryBillVO;

/**
 * Servlet implementation class getEntryBill 電費單資料/列表
 */
@WebServlet("/getEntryBill")
public class getEntryBill extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getEntryBill.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getEntryBill() {
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
		logger.debug("getEntryBill start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("powerAccount"));
			String billMon = ObjectUtils.toString(request.getParameter("billMon"));		
			logger.debug("token: " + token);			
			logger.debug("powerAccount: " + powerAccount);
			logger.debug("billMon: " + billMon);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(powerAccount) && StringUtils.isNotBlank(billMon)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					EntryBillVO entryBillVO = new EntryBillVO();
					entryBillVO.setPowerAccount(powerAccount);
					entryBillVO.setBillMon(billMon);
					EntryBillDAO entryBillDAO = new EntryBillDAO();
					List<DynaBean> list = entryBillDAO.getEntryBill(entryBillVO);
					rspJson.put("code", "00");
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
		logger.debug("getEntryBill end");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject data = new JSONObject();
		try {
			DynaBean bean = rows.get(0);
				JSONObject entryBill = new JSONObject();
				
				entryBill.put("PowerAccount", bean.get("poweraccount"));
				String billmon = ObjectUtils.toString(bean.get("billmon"));
				entryBill.put("BillMon", billmon.substring(0, 4)+"-"+billmon.substring(4, 6));//帳單月份
				entryBill.put("BillStartDay", ToolUtil.dateFormat(bean.get("billstartday"), sdf));//計費日期-起
				entryBill.put("BillEndDay", ToolUtil.dateFormat(bean.get("billendday"), sdf));//計費日期-迄
				entryBill.put("BaseCharge", ToolUtil.getBigDecimal(bean.get("basecharge")));//基本電費
				entryBill.put("UsageCharge", ToolUtil.getBigDecimal(bean.get("usagecharge")));//流動電費
				entryBill.put("OverCharge", ToolUtil.getBigDecimal(bean.get("overcharge")));//非約定電費
				entryBill.put("ShareCharge", ToolUtil.getBigDecimal(bean.get("sharecharge")));//分攤電費
				entryBill.put("PFCharge", ToolUtil.getBigDecimal(bean.get("pfcharge")));//功因補償款
				entryBill.put("TotalCharge", ToolUtil.getBigDecimal(bean.get("totalcharge")));//總電費
				entryBill.put("MaxDemandPK", ToolUtil.getBigDecimal(bean.get("maxdemandpk")));//尖峰最大需量
				entryBill.put("MaxDemandSP", ToolUtil.getBigDecimal(bean.get("maxdemandsp")));//半尖峰最大需量
				entryBill.put("MaxDemandSatSP", ToolUtil.getBigDecimal(bean.get("maxdemandsatsp")));//周六半尖峰最大需量
				entryBill.put("MaxDemandOP", ToolUtil.getBigDecimal(bean.get("maxdemandop")));//離峰最大需量
				entryBill.put("CECPK", ToolUtil.getBigDecimal(bean.get("cecpk")));//尖峰用電量
				entryBill.put("CECSP", ToolUtil.getBigDecimal(bean.get("cecsp")));//半尖峰用電量
				entryBill.put("CECSatSP", ToolUtil.getBigDecimal(bean.get("cecsatsp")));//周六半尖峰用電量
				entryBill.put("CECOP", ToolUtil.getBigDecimal(bean.get("cecop")));//離峰用電量
				entryBill.put("PF",ToolUtil.getBigDecimal(bean.get("pf")));//功率因數
			data.put("EntryBill", entryBill);
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
