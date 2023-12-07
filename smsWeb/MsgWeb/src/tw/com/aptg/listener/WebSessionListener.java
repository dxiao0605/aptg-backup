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
 * 01        20180328    Gary Chang          initial
 */

package tw.com.aptg.listener;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aptg.ws.api.core.profileservice.Profile;

/**
 * Application Lifecycle Listener implementation class WebSessionListener
 *
 */
public class WebSessionListener implements HttpSessionListener, HttpSessionAttributeListener, HttpSessionBindingListener, HttpSessionActivationListener {

	private static final Logger logger = LogManager.getFormatterLogger(WebSessionListener.class.getName());
	
    /**
     * Default constructor. 
     */
    public WebSessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event)  { 
         // TODO Auto-generated method stub
    	HttpSession session = event.getSession();
    	logger.info("Session created" + " (" + session.getId() + ")");
    }

	/**
     * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event)  { 
         // TODO Auto-generated method stub
    	HttpSession session = event.getSession(); 	
    	logger.info("Session destroyed because " + (isTimedOut(session) ? "timeout" : "logout") + " (" + session.getId() + ")");
    	
    }

	/**
     * @see HttpSessionActivationListener#sessionDidActivate(HttpSessionEvent)
     */
    public void sessionDidActivate(HttpSessionEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
       	if (event.getName().equals("uuid")){
       		String uuid = (String)event.getValue();
    		HttpSession session = event.getSession();
    		logger.info("SSO login successful for uuid:" + uuid + " (" + session.getId() + ")");
    	}
    	
    	if (event.getName().equals("profile")){
    		Profile profile = (Profile)event.getValue();
    		HttpSession session = event.getSession();
    		logger.info("SSO login successful for mdn:" + profile.getMdn() + " (" + session.getId() + ")");
    	}
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionActivationListener#sessionWillPassivate(HttpSessionEvent)
     */
    public void sessionWillPassivate(HttpSessionEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    }
    
    private Boolean isTimedOut(HttpSession session) {
    	try {
    		long idle = new Date().getTime() - session.getLastAccessedTime();
    		return idle > (session.getMaxInactiveInterval() * 1000);
    	} catch (IllegalStateException e) {
    		return true;
    	}	
    }
	
}
