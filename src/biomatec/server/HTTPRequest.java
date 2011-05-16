package biomatec.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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
				// Construct 
				StringBuffer data = new StringBuffer();

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
		//data = encodeData(data);
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
		
		
//		HttpURLConnection urlc = null;
//		try
//		{
//			urlc = (HttpURLConnection) endpoint.openConnection();
//			try
//			{
//				urlc.setRequestMethod("POST");
//			} catch (ProtocolException e)
//			{
//				throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
//			}
//			urlc.setDoOutput(true);
//			urlc.setDoInput(true);
//			urlc.setUseCaches(false);
//			urlc.setAllowUserInteraction(false);
//			urlc.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
//
//			OutputStream out = urlc.getOutputStream();
//
//			try {
//				Writer writer = new OutputStreamWriter(out, "UTF-8");
//				pipe(data, writer);
//				writer.close();
//			} catch (IOException e) {
//				throw new Exception("IOException while posting data", e);
//			} finally {
//				if (out != null)
//					out.close();
//			}
//
//			InputStream in = urlc.getInputStream();
//			try {
//				Reader reader = new InputStreamReader(in);
//				pipe(reader, output);
//				reader.close();
//			} catch (IOException e)	{
//				throw new Exception("IOException while reading response", e);
//			} finally {
//				if (in != null)
//					in.close();
//			}
//
//		} catch (IOException e) {
//			throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e);
//		} finally {
//			if (urlc != null)
//				urlc.disconnect();
//		}
	}

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
	
	/**
	 * Pipes everything from the reader to the writer via a buffer
	 */
	private static void pipe(Reader reader, Writer writer) throws IOException
	{
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0)
		{
			writer.write(buf, 0, read);
		}
		writer.flush();
	}
}