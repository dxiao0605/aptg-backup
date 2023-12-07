/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2010
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20141118    Gary Chang          Initial
 */

package tw.com.aptg.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpHeaderUtil {
	
	private static final Logger logger = LogManager.getFormatterLogger(HttpHeaderUtil.class.getName());
	private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
	private static final String HEADER_HOST = "host";
	private static final String HEADER_USER_AGENT = "user-agent";
	private static final String HEADER_REFERER = "referer";
	
    public static String getClientIP(HttpServletRequest request) { 	
        String remoteAddr = request.getRemoteAddr();
        String x;
        if ((x = request.getHeader(HEADER_X_FORWARDED_FOR)) != null) {
            remoteAddr = x;
            int idx = remoteAddr.indexOf(',');
            if (idx > -1) {
                remoteAddr = remoteAddr.substring(0, idx);
            }
        }
        return remoteAddr;
    }
    
    public static String getHost(HttpServletRequest request) {
	    return request.getHeader(HEADER_HOST);
    }
    
    public static String getUserAgent(HttpServletRequest request) {
    	return request.getHeader(HEADER_USER_AGENT);
    }
    
    public static String getReferer(HttpServletRequest request) {
    	return request.getHeader(HEADER_REFERER);
    }
    
    public static void traceHeaders(HttpServletRequest request) {
		logger.debug("Header Names & Value Start");
	    Enumeration en = request.getHeaderNames();
	    while(en.hasMoreElements()) {
	          String item = (String)en.nextElement();
	          String value = request.getHeader(item);
	          logger.debug("item=" + item);
	          logger.debug("value=" + value);
	    }
	    logger.debug("Header Names & Value End"); 
    } 
}
