package tk.eqpic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpInvoker {

	public static String readContentByGet(String urlStr) {
		
		String allString = "";
		try {
			URL url= new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream(), "utf-8"));
				String lines = "";
				while ((lines=reader.readLine()) != null) {
					//System.out.println(lines);
					allString += lines;
				}
				reader.close();
			} else {
				allString = "";
			}
			
			connection.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return allString;
		
	}
}
