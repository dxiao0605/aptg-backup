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
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import tw.com.aptg.beans.SmsReqBean;
import tw.com.aptg.dao.SmsReqDao;
import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Servlet implementation class SmsSend
 */
public class SmsSend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(SmsSend.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmsSend() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		 HttpSession session = request.getSession();
		 Profile profile = (Profile) session.getAttribute("profile");
		
		String msisdns = request.getParameter("Bnumber").replaceAll("&quot;","\"");
		String sender = request.getParameter("Anumber");
		//String cid = request.getParameter("contract");
		
		String cid = profile.getContractID();
		String pid = profile.getPersonalID();
		String pstatus = profile.getContractStatusCode();
		String msg = request.getParameter("msg").replaceAll("&#61;", "=");
		       msg=msg.replaceAll("&#40;", "\\(");
		       msg=msg.replaceAll("&#41;", "\\)");
		       msg=msg.replaceAll("&#39;", "'");
		       msg=msg.replaceAll("&quot;", "\"");

		String timespec = request.getParameter("timespec").replaceAll("&quot;","\"");
		logger.info("http input:" + "sender=" + sender + ",pid=" + pid + ",pstatus=" + pstatus+ ",msisdn=" + msisdns + ",contract=" + cid + ",msg=" + msg + ",timespec=" + timespec);
		
		
		 Gson gson = new Gson();
        
		 String profilegson= gson.toJson(profile);
       
		logger.info("profile=" + profilegson);

		JSONObject json = new JSONObject();
		
		String ret_reson="";
		
		CheckSmsStatus getCustSmsStatus = new CheckSmsStatus();

		try {
			
			/*
			if(false)
			{
				logger.error("CRM PASS");				
			} else 
			*/
			
			
			if(getCustSmsStatus.GetSmsStatus(cid).equals("N")) {
				
				 json.put("ret", "無簡訊服務");
					logger.error("無簡訊服務");
				
			}
			else if(!pstatus.equals("9")) {
				
			   json.put("ret", "合約異常");
				logger.error("合約異常");
			}	

      else if (!chekcinput(msg, msisdns, cid,sender,profile)) {

			   json.put("ret", "input_data_null_error");
				logger.info("ret:" + "input_data_null_error");
				
			} else {
				
				SmsReqDao smsReqDao = new SmsReqDao();

				SmsReqBean smsReqBean = new SmsReqBean();
				smsReqBean.setContract(cid);
				smsReqBean.setMsisdnLst(msisdns);
				smsReqBean.setMsgContent(msg);
				smsReqBean.setTimeSpec(timespec);
				smsReqBean.setStatus("I");
				smsReqBean.setSender(sender);
				smsReqBean.setSender_id(pid);
				
					if ((smsReqDao.insert(smsReqBean)) == 1) {
						json.put("ret", "ins_req_ok");
						logger.info("ret:" + "ins_req_ok");
					}
					else
					json.put("ret", "ins_req_fail");	
			}
		} catch (JSONException e) {
			
			json.put("ret", "System Error");
			e.printStackTrace();
		} catch (Exception e) {
			
			json.put("ret", "System Error");
			e.printStackTrace();
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now_date = new Date();
		
		json.put("RetTime",dateFormat.format(now_date));
		
		logger.info("ret Json:"+json);
		
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	
	
	private boolean chekcinput(String msg, String msisdn, String contract, String sender, Profile in_profile) {

		boolean ret = true;
		if (msisdn == null || contract == null || msg == null|| sender == null || msisdn.length()==0 || contract.length()==0 || msg.length()==0 ||sender.length()==0) 
		{

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
