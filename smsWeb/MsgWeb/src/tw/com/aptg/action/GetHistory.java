package tw.com.aptg.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import tw.com.aptg.dao.SmsHistoryDao;
import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Servlet implementation class GetHistory
 */
@WebServlet("/GetHistory")
public class GetHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	private static final Logger logger = LogManager.getFormatterLogger(GetHistory.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetHistory() {
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
		 
		 logger.info("http input ,contract:" + cid );
		 
		 JSONObject json = new JSONObject();
		 if (chekcinput(cid)) {
				try {
					SmsHistoryDao smsHistoryDao = new SmsHistoryDao();
					List<DynaBean> list = smsHistoryDao.getHistory(cid);

					if (list.size() == 0) {
						json.put("ret", "list_his_no_record");
					    logger.info("ret:"+"list_his_no_record");
					}
					else {
						json = convertToJson(list);
						json.put("ret", "list_his_ok");
						logger.info("ret:"+"list_his_ok");
					}
				} catch (Exception e) {
					json.put("ret", "list_his_fail");
					json.put("msg", "Query in his fail");
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
			
		
		
	}
	
	private boolean chekcinput(String contract) {

		boolean ret = true;
		if (contract == null) {

			ret = false;
		}
		return ret;
	}
	
	private JSONObject convertToJson (List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		JSONObject data = new JSONObject();
		JSONArray list = new JSONArray();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 JSONObject obj = new JSONObject();
			
			 obj.put("logrid", bean.get("logrid"));
			 obj.put("contractid", bean.get("contractid"));
			 obj.put("msisdn", bean.get("msisdn"));
			 obj.put("toaddr", bean.get("toaddr"));
			 obj.put("senderaddr", bean.get("senderaddr"));
			 obj.put("msginfo", bean.get("msginfo"));
			 obj.put("logtime", bean.get("logtime"));
			
			 list.put(obj);
		}
		data.put("data", list);
		return data;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
