package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.PowerAccountMaxDemandDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerAccountMaxDemandVO;

/**
 * Servlet implementation class getPowerAccountMaxDemand 電表最大需量
 */
@WebServlet("/getPowerAccountMaxDemand")
public class getPowerAccountMaxDemand extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerAccountMaxDemand.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerAccountMaxDemand() {
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
		logger.debug("getPowerAccountMaxDemand start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String powerAccount = ObjectUtils.toString(request.getParameter("powerAccount"));		
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			logger.debug("token: " + token);
			logger.debug("PowerAccount:" + powerAccount + ",date:" + start + " ~ " + end);
			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else if (!ToolUtil.dateCheck(start, "yyyyMMdd") || !ToolUtil.dateCheck(end, "yyyyMMdd")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMMdd)");
				} else {
					PowerAccountMaxDemandVO powerAccountMaxDemandVO = new PowerAccountMaxDemandVO();
					powerAccountMaxDemandVO.setPowerAccount(powerAccount);
					powerAccountMaxDemandVO.setStartDate(start);
					powerAccountMaxDemandVO.setEndDate(end);
					PowerAccountMaxDemandDAO powerAccountMaxDemandDAO = new PowerAccountMaxDemandDAO();
					List<DynaBean> list = powerAccountMaxDemandDAO.getPowerAccountDemand(powerAccountMaxDemandVO);					
					if (list != null && list.size() > 0) {
						rspJson.put("code", "00");
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
		logger.debug("getPowerAccountMaxDemand end");
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
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		JSONObject data = new JSONObject();
		try {
			JSONArray demandArr = new JSONArray();
			for(int i=0; i<rows.size(); i++) {
				DynaBean bean = rows.get(i);	
				if(i==0) {
					data.put("PowerAccount", bean.get("poweraccount"));
					data.put("CC", bean.get("cc")!=null?bean.get("cc"):"");
				}	
				JSONObject demand = new JSONObject();
				demand.put("Seq", i+1);
				demand.put("RecDate", ToolUtil.dateFormat(bean.get("rectime"), date));
				demand.put("RecTime", ToolUtil.dateFormat(bean.get("rectime"), time));
				demand.put("Demand", ToolUtil.getBigDecimal(bean.get("totaldemand"), 0));
				demand.put("DemandPercent", ToolUtil.divide(bean.get("totaldemand"), bean.get("cc"), 2).multiply(new BigDecimal(100)));
				
				demandArr.put(demand);
			}			
			data.put("List", demandArr);
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
