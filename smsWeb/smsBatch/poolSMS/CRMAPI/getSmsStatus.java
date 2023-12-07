import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getSmsStatus {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		getSmsStatus http = new getSmsStatus();

		

		System.out.println("\nTesting 2 - Send Http POST request");
		http.sendPost();

	}
	
	private void sendPost() throws Exception {

		//String url = "http://10.31.80.38/ZSmartService/userservice_taiwan.asmx";
		
		//String url = "http://10.31.79.18:4312/crm-api/WEBPOS/InsertContractRemark.do?command=InsertContractRemark&mdn=0906041052&system=PPSPT&channelId=PPSPT&ContractRemark=系統轉移4G-LTE餘額-通信費基本帳本100元，通信費贈送帳本50元，數據基本帳本60MB，數據贈送帳本80MB，數據計日型原到期日:無";
		//String url = "http://10.31.79.18:4312/crm-api/WEBPOS/InsertContractRemark.do?command=InsertContractRemark&mdn=0906041052&ContractRemark=test&system=PPSPT&channelId=PPSPT";
		
		
		String url ="http://10.31.79.18:4312/cmsweb/CWS/GetVoiceBasicStatus.jsp?command=GetVoiceBasicStatus&MDN=&contractId=4G-16030900226&ServiceID=SMS&system=MSG&channelId=MSG";

		//String url ="http://10.31.79.18:4312/cmsweb/CWS/GetVoiceBasicStatus.jsp?command=GetVoiceBasicStatus&MDN=0973291018&contractId=&ServiceID=SMS&system=MSG&channelId=MSG";

		
		URL obj = new URL(url);
		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      //URL wsUrl = new URL("http://10.31.80.38/ZSmartService/userservice_taiwan.asmx");
    //	 HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();
		
		
		//add reuqest header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
		//con.setRequestProperty("SOAPAction", "http://www.ZTEsoft.com/ZSmart/QuerySubsProfile");
        con.setRequestProperty("Content-Length", "length");
		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		
		String urlParameters="";
		/*
		String urlParameters = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zsm=\"http://www.ZTEsoft.com/ZSmart\">"
                    +"<soapenv:Header>"
                    +"<zsm:AuthHeader>"
					+"<zsm:Username>ZSmart1</zsm:Username>"
					+"<zsm:Password>password</zsm:Password>"
					+"</zsm:AuthHeader>"
					+"</soapenv:Header>"
					+"<soapenv:Body>"
					+"<zsm:QuerySubsProfile>"
					+"<zsm:MSISDN>8869060105641</zsm:MSISDN>"
					+"<zsm:ICCID></zsm:ICCID>"
					+"</zsm:QuerySubsProfile>"
					+"</soapenv:Body>"
					+"</soapenv:Envelope>";
		*/
		
		
		
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream(),"Big5"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String xml_result = response.toString();
		//String a =response.toString();
		
		//String aa= new String(xml_result.getBytes("utf-8"), "utf-8");
		
		
		
		//System.out.println(new String(a.getBytes("Big5"), "ISO-8859-1"));
 //System.out.println(a.getBytes("Big5"));
 
 //System.out.println(new String(a.getBytes(), "Big5"));
 //System.out.println(new String(a.getBytes("Big5"), "Big5"));
 System.out.println(xml_result);

		
		
		
		
		//System.out.println(aa);
		
		//print result
		//System.out.println(xml_result);

	}

}
