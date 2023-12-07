package tw.com.aptg.action;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import tw.com.aptg.dao.SmsReqDao;
import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Servlet implementation class SmsSend
 */
public class GetInbox extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(GetInbox.class.getName());  
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInbox() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 HttpSession session = request.getSession();
		 Profile profile = (Profile) session.getAttribute("profile");
		
		 String cid = profile.getContractID();
		 String pid = profile.getPersonalID();
		 
		//String cid = request.getParameter("contract");

		logger.info("http input ,contract:" + cid + ",personal id=" + pid);
		
		JSONObject json = new JSONObject();
		
		if (chekcinput(cid)) {
			try {
				SmsReqDao smsReqDao = new SmsReqDao();
				List<DynaBean> list = smsReqDao.getinbox(cid,pid);

				if (list.size() == 0) {
					json.put("ret", "list_req_no_record");
				    logger.info("ret:"+"list_req_no_record");
				}
				else {
					json = convertToJson(list);
					json.put("ret", "list_req_ok");
					logger.info("ret:"+"list_req_ok");
				}
			} catch (Exception e) {
				json.put("ret", "list_req_fail");
				json.put("msg", "Query in box fail");
				logger.error("", e);
			}
		} else {

			json.put("ret", "list_req_input_error");
			logger.info("ret:"+"list_req_input_error");
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now_date = new Date();

		json.put("RetTime", dateFormat.format(now_date));

		logger.info("ret Json:"+json);
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	private JSONObject convertToJson (List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		JSONObject data = new JSONObject();
		JSONArray list = new JSONArray();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 JSONObject obj = new JSONObject();
			 obj.put("submit_t", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(bean.get("c_date")));
			 obj.put("ms_lsts", bean.get("msisdn_lst"));
			 obj.put("msg_c", bean.get("msg_content"));
			 obj.put("seq_id", bean.get("s_id"));
			 obj.put("timespec", bean.get("time_spec"));
			
			 list.put(obj);
		}
		data.put("data", list);
		return data;
	}
	
	private boolean chekcinput(String contract) {

		boolean ret = true;
		if (contract == null) {

			ret = false;
		}
		return ret;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
