/*

Copyright (C) 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to send HTTP request and get response code and response data.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class HttpRequestUtil {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * Send HTTP Request to specified URL and Get Response Code.
	 * If specified request body is null then sends a HTTP Get request else sends a HTTP Post Request.
	 * If it fails to connect or get Response code then returns -1 else returns response code.
	 * 
	 * @param urlString a String URL
	 * @param requestBody request body
	 * 
	 * @return int value of Response Code
	 */
	int getResponseCode(String urlString, String requestBody) {
		
		HttpURLConnection conn = getHttpURLConnection(urlString, requestBody);
		
		if ( conn == null )
			return -1;
		
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			logger.warning("IOException : getting Response Code of the URL Connection of URL '"+urlString+"'.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Exception : getting Response Code of the URL Connection of URL '"+urlString+"'.");
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Send HTTP Request to specified URL and Get Response Data.
	 * If specified request body is null then sends a HTTP Get request else sends a HTTP Post Request.
	 * If it fails to connect or get Response data then returns null else returns response code.
	 * 
	 * @param urlString a String URL
	 * @param requestBody request body
	 * 
	 * @return String value of Response Data
	 */
	String getResponseData(String urlString, String requestBody) {
		
		HttpURLConnection conn = getHttpURLConnection(urlString, requestBody);
		
		if ( conn == null )
			return null;

		logger.info("cont: " + conn);
		try {
						BufferedReader rd = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
			StringBuilder sb = new StringBuilder();
			String line;
			   
			while ((line = rd.readLine()) != null) 
				sb.append(line);
			   
			   
			rd.close();

			return sb.toString();	
			  
		} catch (IOException e) {
			logger.warning("IOException : getting Response Data of the URL Connection of URL '"+urlString+"'.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Exception : getting Response Data of the URL Connection of URL '"+urlString+"'.");
			//e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Get an Object of HttpURLConnection for specified URL and request body.
	 * 
	 * @param urlString a String URL
	 * @param requestBody request body
	 * 
	 * @return an Object of HttpURLConnection
	 */
	private HttpURLConnection getHttpURLConnection(String urlString, String requestBody) {
		
		if ( urlString == null ) {
			logger.warning("HTTP URL is null.");
			return null;
		}
		
		try {
			
			URL url = new URL(urlString);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			if ( requestBody == null ) {
				
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				
			} else {
				
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				
				DataOutputStream printout = new DataOutputStream( conn.getOutputStream() );
				printout.writeBytes(requestBody);
				printout.flush();
			}
			
			conn.connect();
			
			return conn;
			
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : opening URL connection with URL '"+urlString+"'.");
			//e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException : opening URL connection with URL '"+urlString+"'.");
			//e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Exception : opening URL connection with URL '"+urlString+"'.");
			//e.printStackTrace();
		}
		
		return null;
	}
	/*
	// Just for testing
	public static void main(String[] args) {
		
		//logger.info( new HttpRequestUtil().getResponseData("http://www.google.co.in", null) );
		
		// For Get Request
		//String urlString = "http://res.dotui.com/resserver/utilities?0946095A1F10003D0500010B0A00020B00003C3D0033120209480A500105003D0000050946000A0B3C010101004711060B360A0A010E00322600020B4600050B32010B0100330E06075909464709003D010000060A000C073C011505003D12040A1306462844005032000C045000050450005A39000026020B1B0946150F00010F00040B5A000D0B0A00010E00290005060D0B46006300510400060B5A00010B0A003D0E000B07030B32";
		
		//logger.info( new HttpRequestUtil().getResponseCode(urlString, null) );
		
		
		
		// For Post Request
		//String urlString = "http://192.168.2.50/resserver/utilities";
		//String requestBody = "0946095A1F10003D0500010B0A00020B00003C3D0033120209480A500105003D0000050946000A0B3C010101004711060B360A0A010E00322600020B4600050B32010B0100330E06075909464709003D010000060A000C073C011505003D12040A1306462844005032000C045000050450005A39000026020B1B0946150F00010F00040B5A000D0B0A00010E00290005060D0B46006300510400060B5A00010B0A003D0E000B07030B32";
		//logger.info( new HttpRequestUtil().getResponseCode(urlString, requestBody) );
		
		
		
		//String urlString = "http://192.168.2.50/resserver/utilities?action=validateuser";
		//String requestBody = "0B4C0B32290100330E0005075000050946005B09003D01050613073C3D05003D1200020A0A00000646001E4400283204045604500039000A26000C0B14000A094600010F000B0F010B610B0A470E0029000000060A00010B46003263005104000B5D0B0A290E00510700020B32";
		//logger.info( new HttpRequestUtil().getResponseCode(urlString, requestBody) );
		
		
		
		//String urlString = "http://192.168.2.50/resserver/query";
		//String requestBody = "0B4C0B32290100330E0005075000050946005B09003D01050613073C3D05003D1200020A0A00000646001E4400283204045604500039000A26000C0B14000A094600010F000B0F010B610B0A470E0029000000060A00010B46003263005104000B5D0B0A290E00510700020B32";
		//logger.info( new HttpRequestUtil().getResponseCode(urlString, requestBody) );
		
		
	}
	*/
}
