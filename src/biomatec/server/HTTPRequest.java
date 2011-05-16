package biomatec.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HTTPRequest
{
	/**
	 * Sends an HTTP GET request to a url
	 *
	 * @param endpoint - The URL of the server. (Example: " http://www.yahoo.com/search")
	 * @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
	 * @return - The response from the end point
	 */
	public static String sendGetRequest(String endpoint, String requestParameters)
	{
		String result = null;
		if (endpoint.startsWith("http://"))
		{
			// Send a GET request to the servlet
			try
			{
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length () > 0)
				{
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection ();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null)
				{

					String newLine = "\n";
					sb.append(line);
					sb.append(newLine);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Reads data from the data reader and posts it to a server via POST request.
	 * data - The data you want to send
	 * endpoint - The server's address
	 * output - writes the server's response to output
	 * @return 
	 * @throws Exception
	 */
	public static String postData(String endpoint, String data) {
		String text = "";
		try {

		    // Send data
		    URL url = new URL(endpoint);
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		        text += line;
		    }
		    wr.close();
		    rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}

	/**
	 * encodes a String in format "name1=value1&name2=value2" to
	 * its url format equivalent
	 * @param data parameters do encode
	 * @return the data encoded in url format
	 */
	@SuppressWarnings("unused")
	private static String encodeData(String data) {
	    String encodedData = "";
	    String parameters[] = data.split("&");
	    for (int i=0; i<parameters.length; i++) {
	    	String aux[] = parameters[i].split("=");
	    	String encodedName = "";
	    	String encodedValue = "";
			try {
				encodedName = URLEncoder.encode(aux[0], "UTF-8");
				encodedValue = URLEncoder.encode(aux[1], "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	encodedData += encodedName+"="+encodedValue+"&";
	    }
	    // remove the last ampersand
	    encodedData = encodedData.substring(0, encodedData.length()-2);
		
	    return encodedData;
	}
	
}