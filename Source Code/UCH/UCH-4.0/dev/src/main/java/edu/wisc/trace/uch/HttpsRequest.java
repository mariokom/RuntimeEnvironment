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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import edu.wisc.trace.uch.resource.util.MyHostnameVerifier;
import edu.wisc.trace.uch.resource.util.MyTrustManager;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class HttpsRequest {
	
	private Logger logger = LoggerUtil.getSdkLogger();

	static final String CONSTANT_RESPONSE_CODE = "responseCode";
	static final String CONSTANT_RESPONSE_DATA = "responseData";
	
	private static final String HTTP_REQUEST_METHOD_GET = "GET";
	
	private static final String REQUEST_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String REQUEST_HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
	
	private String uriString;
	
	/**
	 * Provide the reference of uri to local variables.
	 * 
	 * @param uri a String value of URI.
	 */
	HttpsRequest(String uri) {
		
		this.uriString = uri;
		setSSLSettings();
	}
	
	/**
	 * Send HTTP GET request and return response code and data in a map.
	 * 
	 * @param queryString a String value of queryStrng.
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	Map<String, String> sendGetRequest(String queryString) {
		
		if ( uriString == null ) {
			logger.info("URL is null.");
			return null;
		}
		if ( queryString == null ) {
			logger.info("Query String is null.");
			return null;
		}
		
		URL url;
		
		try {
			url = new URL(uriString + "?" +queryString);
		} catch (MalformedURLException e) {
			logger.info("MalformedURLException : Invalid URL '" + uriString + "?" +queryString+"'.");
			return null;
		}
		
		try {
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			
			httpsURLConnection.setRequestMethod(HTTP_REQUEST_METHOD_GET);
			
			httpsURLConnection.setDoInput(true);    
			httpsURLConnection.setDoOutput(true);		    
			httpsURLConnection.setUseCaches(false);
			 
			httpsURLConnection.setRequestProperty(REQUEST_HEADER_CONTENT_TYPE, REQUEST_HEADER_CONTENT_TYPE_VALUE);
			
			httpsURLConnection.connect();
			
			Map<String, String> returnMap = new HashMap<String, String>();
			
			int responseCode = httpsURLConnection.getResponseCode();
			
			returnMap.put(CONSTANT_RESPONSE_CODE, String.valueOf(responseCode) );
			
			if ( responseCode == 200 )
			returnMap.put(CONSTANT_RESPONSE_DATA, getInputStreamData(httpsURLConnection.getInputStream()) );
			
			return returnMap;
			
		} catch (IOException e) {
			logger.info("IOException : Unable to Send GET Request :" +uriString + "?" +queryString);
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("Exception : Unable to Send GET Request :" +uriString + "?" +queryString);
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get data from specified inputStream.
	 * 
	 * @param is an Object of InputStream
	 * 
	 * @return a String value of data
	 */
	private String getInputStreamData( InputStream is ) {
		
		if ( is == null ) {
			logger.info("InputStream is null.");
			return null;
		}
		
		try {
			
			int ch ;
			StringBuilder sb = new StringBuilder();
			
			while( ( ch = is.read() ) != -1 )
				sb.append( (char)ch );
			
			return sb.toString();
			
		} catch (IOException e) {
			logger.info("Unable to read input stream.");
			return null;
		}
	}
	
	/**
	 * Return whether SSL settings set successfully or not.
	 * 
	 * @return whether SSL settings set successfully or not
	 */
	private boolean setSSLSettings() {
		
		try {
			
			SSLContext sslContext = SSLContext.getInstance("SSL");
			
			sslContext.init(null, new X509TrustManager[] { new MyTrustManager() }, null);
			
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			  
			HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
			
		//	Authenticator.setDefault(new MyAuthenticator());
			return true;
		
		} catch (NoSuchAlgorithmException e) {
			
			logger.info("NoSuchAlgorithmException");
			e.printStackTrace();	
			
		} catch (KeyManagementException e) {
			
			logger.info("KeyManagementException");
			e.printStackTrace();
		}
		
		return false;
	}
}
