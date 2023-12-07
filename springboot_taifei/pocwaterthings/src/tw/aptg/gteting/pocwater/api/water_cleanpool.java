package tw.aptg.gteting.pocwater.api;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gvjava.org.json.JSONObject;
import tw.aptg.gteting.pocwater.dao.cleanpoolDao;

/**
 * Servlet implementation class water_cleanpool
 */
@WebServlet("/water_cleanpool")
public class water_cleanpool extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger logger = LogManager.getFormatterLogger(water_cleanpool.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public water_cleanpool() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
String outstr = "";
		
		String jsonok = "{\"result\":\"ok\"}";
		String jsonfail = "{\"result\":\"fail\"}";
		
		 try {
		
		
		String Ap_name = request.getContextPath();
		String Api_name = request.getRequestURI().substring(request.getContextPath().length());
		String URL_Str = request.getRequestURI();

		String requestUrl =request.getRequestURI();;
		
		
		logger.info("Ap_name=" + Ap_name);
		logger.info("Api_name=" + Api_name);
		logger.info("URL_Str=" + URL_Str);
		logger.info("requestUrl=" + requestUrl);
		
		
		StringBuffer GetPostData = new StringBuffer();
	    BufferedReader bufferedReader = null;
	    String content = "";

	   
	        //InputStream inputStream = request.getInputStream();
	        //inputStream.available();
	        //if (inputStream != null) {
	        bufferedReader =  request.getReader() ; //new BufferedReader(new InputStreamReader(inputStream));
	        char[] charBuffer = new char[128];
	        int bytesRead;
	        while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) {
	        	GetPostData.append(charBuffer, 0, bytesRead);
	        }
	        
	    
	    
	    String postdate = GetPostData.toString();
		
	    
	   // String SSS =new String(postdate.getBytes("UTF-8"),"UTF-8");
	    	
	    	
	    	logger.info("postdate=" + postdate);
	    	
	       
	        
	    	cleanpoolDao  cleanpooldao = new cleanpoolDao();
		
	    	cleanpooldao.insert(postdate);
	    
	   // String json = ....
	   	JSONObject jsonObject = new JSONObject(postdate);
	    
	   	String Token =jsonObject.getString("token");
	   	
	    //outstr = postdate +"--OK--"+Token;
	   	
	   	if(Token.equals("R1G@e*se2a-R82I"))
	   			outstr=jsonok;
	   	else 
	   		   outstr=jsonfail;
	   	
	   	logger.info("outstr=" + outstr);
	   
	    
	    response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(outstr);
	    
		
		 }catch(Exception e)
		 {
			 
			 
			 outstr = "fail";
			  
					// TODO Auto-generated catch block
			 e.printStackTrace();
		    	logger.error(e.getMessage());
					
					
			 response.setContentType("text/html;charset=utf-8");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonfail);
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
