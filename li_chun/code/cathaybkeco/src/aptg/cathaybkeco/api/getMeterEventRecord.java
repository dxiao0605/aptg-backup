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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.MeterEventRecordDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterEventRecordVO;

/**
 * Servlet implementation class getMeterEventRecord 電表事件紀錄
 */
@WebServlet("/getMeterEventRecord")
public class getMeterEventRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterEventRecord.class.getName());
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterEventRecord() {
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
		logger.debug("getMeterEventRecord start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			logger.debug("token: " + token);
			logger.debug("start: " + start + ", end: " + end);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterEventRecordVO meterEventRecordVO = new MeterEventRecordVO();
					meterEventRecordVO.setStartDate(start);
					meterEventRecordVO.setEndDate(end);
					MeterEventRecordDAO meterEventRecordDAO = new MeterEventRecordDAO();
					List<DynaBean> list = meterEventRecordDAO.getMeterEventRecord(meterEventRecordVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
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
		logger.debug("getMeterEventRecord end");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for (DynaBean bean : rows) {
				JSONObject event = new JSONObject();
				event.put("EventTime", ToolUtil.dateFormat(bean.get("eventtime"), sdf));
				event.put("EventName", bean.get("eventname"));
				event.put("EventDesc", bean.get("eventdesc"));
				list.put(event);
			}
			data.put("MeterEventRecord", list);
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
