/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2018
 *
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20180323    Gary Chang          initial
 */

package tw.com.aptg.adapter;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType;

import tw.com.aptg.beans.AddressBookGroup;
import tw.com.aptg.service.PhoneBookServiceClient;
import tw.com.aptg.service.ProfileServiceClient;
import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Servlet implementation class ReceiveAuth
 */
public class ReceiveAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(ReceiveAuth.class.getName());    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiveAuth() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
		// TODO Auto-generated method stub
		
		try {
		String uuid = request.getRemoteUser();
		//Create new session
		HttpSession session = request.getSession(true);
		if (uuid != null && !uuid.equals("")) {
			//GetUserProfile
			logger.info("GetUserProfile Start");
			ProfileServiceClient wsClient = new ProfileServiceClient(uuid);
			Profile profile = wsClient.getUserProfile();
			session.setAttribute("uuid", uuid);
			session.setAttribute("profile", profile);
			
			String cid = profile.getContractID();
			String pid = profile.getPersonalID();
			String msisdn = profile.getMdn();
			logger.info("GetUserProfile ok,cid=" + cid + ",pid=" + pid +",msisdn=" +msisdn);
			
			
			//InquirePhoneBook
			try {
				
			logger.info("GetPhoneBook Start");	
			PhoneBookServiceClient pnsClient = new PhoneBookServiceClient(profile.getContractID());
			String exactGroupName = "預設群組";
			List<ContactFullType> contactsList = pnsClient.getListContacts(exactGroupName);
			List<AddressBookGroup> groupAddressBookslist = pnsClient.getGroupAddressBooks();
			session.setAttribute("contactsList", contactsList);
			session.setAttribute("groupAddressBookslist", groupAddressBookslist);
			
			logger.info("GetPhoneBook ok");
			
			}catch (Exception e) {
				
				logger.error("Get PhoneBook Error", e);
			}
		}		
		response.sendRedirect(request.getContextPath() + "/indexA.jsp");	
		
		}catch (Exception e) {
		
			logger.error("ReceiveAuth Error", e);
			
	}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
