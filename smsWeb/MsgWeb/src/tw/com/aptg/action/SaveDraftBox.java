package tw.com.aptg.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import tw.com.aptg.beans.SmsDraftBean;
import tw.com.aptg.dao.SmsDraftDao;
import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Servlet implementation class SmsSend
 */
public class SaveDraftBox extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(SaveDraftBox.class.getName());

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

		String msg = request.getParameter("draftmsg");
		String msisdn = request.getParameter("msisdn");
		String contract = request.getParameter("contract");
		JSONObject json = new JSONObject();
		logger.info("http input:" +"msisdn="+msisdn+",contract="+contract+"msg="+ msg);
		
		 HttpSession session = request.getSession();
		 Profile profile = (Profile) session.getAttribute("profile");
		

			String cid = profile.getContractID();
			String pid = profile.getPersonalID();
			
			logger.info("Get Session Variable ,contract:" + cid + ",personal id=" + pid);
		
		if (chekcinput(msg, msisdn, contract)) {
			

			SmsDraftDao smsDraftDao = new SmsDraftDao();

			SmsDraftBean smsDraftBean = new SmsDraftBean();

			smsDraftBean.setMsgContent(msg);
			smsDraftBean.setMsisdn(msisdn);
			smsDraftBean.setContract(contract);
			smsDraftBean.setPid(pid);

			try {
				if ((smsDraftDao.insert(smsDraftBean)) == 1) {
					json.put("ret", "ins_draft_ok");
					logger.info("ret:"+"ins_draft_ok");
				}

			} catch (Exception e) {
				json.put("ret", "ins_draft_fail");
				logger.error("insert draft Error", e);
			}
		} else {

			json.put("ret", "ins_draft_input_error");
			logger.info("ret:"+"ins_draft_input_error");
		}

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now_date = new Date();
		
		json.put("RetTime",dateFormat.format(now_date));
		
		logger.info("ret Json:"+json);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

	}

	private boolean chekcinput(String msg, String msisdn, String contract) {

		boolean ret = true;
		if (msisdn == null || contract == null || msg == null || msisdn.length()==0 || contract.length()==0 || msg.length()==0) {

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
