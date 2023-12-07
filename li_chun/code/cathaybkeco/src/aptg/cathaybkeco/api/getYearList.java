package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.cathaybkeco.bean.ListBean;
import aptg.cathaybkeco.bean.ListMsgBean;
import aptg.cathaybkeco.util.JsonUtil;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getYearList 民國年下拉選單
 */
@WebServlet("/getYearList")
public class getYearList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getYearList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getYearList() {
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
		JSONObject rspJson = new JSONObject();
		String rspStr = new String();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
//			logger.debug("token: " + token);			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					rspStr = JsonUtil.getInstance().convertObjectToJsonstring(convertToJson());	
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
		
//		logger.debug("getYearList rsp: " + rspJson);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		if(StringUtils.isNotBlank(rspStr)) {
			response.getWriter().write(rspStr);
		}else {
			response.getWriter().write(rspJson.toString());
		}
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private ListMsgBean convertToJson() throws Exception {
		ListMsgBean msgBean = new ListMsgBean();
		try {
			List<ListBean> list = new ArrayList<ListBean>();
			for(int i=0; i<=15; i++) {
				ListBean bean = new ListBean();	
				bean.setLable(String.valueOf(105+i));
				bean.setValue(String.valueOf(105+i));
				list.add(bean);
			}			
			msgBean.setMsg(list);
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
