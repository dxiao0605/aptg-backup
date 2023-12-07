package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class getLatlng {


	public static void main(String args[]) {
		try {
			System.out.println("請輸入地址:");
			Scanner sc = new Scanner(System.in);
			String address = sc.nextLine();  
			
			getLatlng.getAdressXY(address);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAdressXY(String Address) {
		URL url;

		try {
			Address = URLEncoder.encode(Address, "utf-8");
			String actionUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAuOy7ev1znTExStENSn0AeNBeRGENua6g&sensor=false&address=" + Address;

			url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("GET");
			con.setUseCaches(false);
			con.setReadTimeout(3000);
			con.setConnectTimeout(3000);

			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

//			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			/* 取得Response內容 */
			int retCode = con.getResponseCode();
			// System.out.println("JsonUtil retCode" + retCode);
			String inputLine = null;
			if (retCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

				String strResult = "";
				while (((inputLine = reader.readLine()) != null)) {
					strResult = strResult + inputLine;
				}
				// System.out.println("strResult="+strResult);
				reader.close();
				System.out.println(strResult);
//    	 //解析JSON回傳結果
//    	 JSONObject obj = new JSONObject(strResult);
//    	 
//    	 JSONArray jsonArray = obj.getJSONArray("results");
//    	 for (int i = 0; i < jsonArray.length(); i++) {
//    	 JSONObject obj2 = jsonArray.getJSONObject(i);
//    	 JSONObject obj3 = obj2.getJSONObject("geometry");
//    	 JSONObject obj4 = obj3.getJSONObject("location");
//    	 //retString[0] = obj4.getString("lat");
//    	 //retString[1] = obj4.getString("lng");
//    	 retXY[0] = obj4.getDouble("lat");
//    	 retXY[1] = obj4.getDouble("lng");
//    	 
//    	 //System.out.println("lat(經度)："+retXY[0]);
//    	 //System.out.println("lng(緯度)："+retXY[1]);
//    	 }
			}
			con.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("getAdressXY Exception" + e.toString());
		}

//    	 return retXY;

	}

}
