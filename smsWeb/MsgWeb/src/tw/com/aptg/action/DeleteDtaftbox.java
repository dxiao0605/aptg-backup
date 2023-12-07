package tw.com.aptg.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import tw.com.aptg.dao.SmsDraftDao;



public class DeleteDtaftbox extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(DeleteDtaftbox.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String sid = request.getParameter("s_id");
		String cid = request.getParameter("contract");

		logger.info("http input:" +"sid=" + sid+",cid="+cid);

		SmsDraftDao smsDraftDao = new SmsDraftDao();

		JSONObject json = new JSONObject();
		
		if (chekcinput(sid,cid)) {
		try {

			if(smsDraftDao.delete_draft(sid,cid) > 0) {
			json.put("ret", "delete_draft_ok");
			logger.info("ret:"+"delete_draft_ok");
			}
			else {
				json.put("ret", "delete_draft_no_record");
				logger.info("ret:"+"delete_draft_no_record");
			}
		} catch (Exception e) {
			json.put("ret", "delete_draft_fail");

			logger.error("", e);
		}
		}else {

			json.put("ret", "del_draft_input_error");
			logger.info("ret:"+"del_draft_input_error");
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now_date = new Date();
		
		json.put("RetTime",dateFormat.format(now_date));
		logger.info("ret Json:"+json);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

		
	}
	
	
	private boolean chekcinput(String sid,String cid) {

		boolean ret = true;
		if ( sid == null || cid == null) {

			ret = false;
		}
		return ret;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
