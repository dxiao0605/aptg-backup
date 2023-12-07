package tw.com.aptg.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckSmsStatus {
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	private static final Logger logger = LogManager.getFormatterLogger(CheckSmsStatus.class.getName());
	
	public String GetSmsStatus(String Contract_ID) throws Exception {

			
		//String url ="http://crm09.apt.corp/cmsweb/CWS/GetVoiceBasicStatus.jsp?command=GetVoiceBasicStatus&MDN=&contractId="+Contract_ID+"&ServiceID=SMS&system=MSG&channelId=MSG";
		String url ="http://10.31.79.18:4312/cmsweb/CWS/GetVoiceBasicStatus.jsp?command=GetVoiceBasicStatus&MDN=&contractId="+Contract_ID+"&ServiceID=SMS&system=MSG&channelId=MSG";
		
		URL obj = new URL(url);
		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      	
		
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
		con.setRequestProperty("Content-Length", "length");
				
		String urlParameters="";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		logger.info("Sending request to URL : " + url);
		//logger.info("Post parameters : " + urlParameters);
		logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
	    new InputStreamReader(con.getInputStream(),"Big5"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String xml_result = response.toString();
		
		String f_str = "<ApplyStatus>Y</ApplyStatus>";
		int j = xml_result.indexOf(f_str, 1);
	 
		logger.info("Response =" +xml_result);
		
	    if (j > 0){
          logger.info("SMS Status: Y");	
          return "Y";	
		}		  
		else {
		 logger.info("SMS Status: N"); 	
		 return "N";
		}
		
	
		

	}

	
	

}
