package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
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

import aptg.cathaybkeco.dao.FcstChargeDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.FcstChargeVO;

/**
 * Servlet implementation class getCharge 電費計算
 */
@WebServlet("/getCharge")
public class getCharge extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCharge.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCharge() {
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
		logger.debug("getBestCC start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));

			logger.debug("token: " + token);
			logger.debug("BankCode: " + bankCode);
			logger.debug("date:" + start + " ~ " + end);

			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					FcstChargeVO fcstChargeVO = new FcstChargeVO();
					fcstChargeVO.setBankCode(bankCode);
					fcstChargeVO.setStartDate(start);
					fcstChargeVO.setEndDate(end);
					FcstChargeDAO fcstChargeDAO = new FcstChargeDAO();
					List<DynaBean> list = fcstChargeDAO.getFcstChargeMonth(fcstChargeVO);
					if (list != null && list.size() > 0) {
						rspJson.put("code", "00");
						rspJson.put("count", list != null ? list.size() : 0);

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
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());		
		logger.debug("getCharge end");
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
			BigDecimal baseCharge = new BigDecimal(0);
			BigDecimal usageCharge = new BigDecimal(0);
			BigDecimal overCharge = new BigDecimal(0);
			BigDecimal totalCharge = new BigDecimal(0);
			JSONArray list = new JSONArray();
			for (DynaBean bean : rows) {
				JSONObject charge = new JSONObject();
				baseCharge = baseCharge.add(ToolUtil.getBigDecimal(bean.get("basecharge")));
				usageCharge = usageCharge.add(ToolUtil.getBigDecimal(bean.get("usagecharge")));
				overCharge = overCharge.add(ToolUtil.getBigDecimal(bean.get("overcharge")));
				totalCharge = totalCharge.add(ToolUtil.getBigDecimal(bean.get("totalcharge")));
								
				charge.put("useMonth", bean.get("usemonth"));//月份
				charge.put("BaseCharge", bean.get("basecharge"));//基本電費
				charge.put("UsageCharge", bean.get("usagecharge"));//流動電費
				charge.put("OverCharge", bean.get("overcharge"));//非約定電費
				charge.put("TotalCharge", bean.get("totalcharge"));//總電費
				charge.put("CC", bean.get("cc"));//契約容量
				charge.put("MDemand", bean.get("mdemand"));//最大需量
				charge.put("MCECPK", bean.get("tpmcecpk"));//尖峰用電量
				charge.put("MCECSP", bean.get("tpmcecsp"));//半尖峰用電量
				charge.put("MCECSatSP", bean.get("tpmcecsatsp"));//周六半尖峰用電量
				charge.put("MCECOP", bean.get("tpmcecop"));//離峰用電量
				charge.put("MCEC", bean.get("tpmcec"));//總用電量

				list.put(charge);
			}
			data.put("Charge", list);
			
			JSONObject total = new JSONObject();
			total.put("BaseCharge", baseCharge);//基本電費
			total.put("UsageCharge", usageCharge);//流動電費
			total.put("OverCharge", overCharge);//非約定電費
			total.put("TotalCharge", totalCharge);//總電費
			data.put("Total", total);
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
