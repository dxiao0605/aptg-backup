package tw.com.aptg.filter;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aptg.utils.HttpHeaderUtil;

/**
 * Servlet Filter implementation class LoggingFilter
 * 
 * 紀錄誰(Remote IP、UserAgent)有訪問本站
 */
public class LoggingFilter implements Filter {

	private static final Logger logger = LogManager.getFormatterLogger(LoggingFilter.class.getName());
	private String filter = "";
	
    /**
     * Default constructor. 
     */
    public LoggingFilter() {
        // TODO Auto-generated constructor stub
		ResourceBundle config = ResourceBundle.getBundle("config");
		this.filter = config.getString("filter.file.extension");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest)request;	
		HttpServletResponse res = (HttpServletResponse) response;
		String requestUrl ="";
		String accesslog ="";
		long startTime = System.currentTimeMillis();
		//排除靜態網頁
		/*
		String[] filterArrary = this.filter.split(";");
		Arrays.sort(filterArrary);	
		int beginIndex = req.getRequestURI().lastIndexOf(".");
		int endIndex = req.getRequestURI().length();	
		int searchResult = -1;		
		//有.的才要做search
		if (beginIndex > 0) {
			String key = req.getRequestURI().substring(beginIndex, endIndex);
			searchResult = Arrays.binarySearch(filterArrary, key);
		} 
		
		//陣列位置
		if (searchResult < 0 ) {
			requestUrl = req.getQueryString() == null ?requestUrl:(requestUrl+"?"+req.getQueryString());
			accesslog = "IP=" + HttpHeaderUtil.getClientIP(req) +",Method=" +req.getMethod() +",Headers=" +req.getMethod()+",URI=" + req.getRequestURI() +",requestUrl=" +requestUrl+" ,Agent= " + HttpHeaderUtil.getUserAgent(req);
				
		}
		*/
		
		requestUrl = req.getQueryString() == null ?requestUrl:(requestUrl+"?"+req.getQueryString());
		accesslog = "IP=" + HttpHeaderUtil.getClientIP(req) +",Method=" +req.getMethod() +",URI=" + req.getRequestURI() +",requestUrl=" +requestUrl+" ,Agent= " + HttpHeaderUtil.getUserAgent(req)+",status="+res.getStatus();
	
		logger.info(accesslog);
		
		//logger.error("error message");
		
		
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		long endTime = System.currentTimeMillis();
		logger.info((endTime-startTime)+"msec。");	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
