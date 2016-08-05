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
package edu.wisc.trace.uch.resource.localresourcemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import edu.wisc.trace.uch.resource.util.MyHostnameVerifier;
import edu.wisc.trace.uch.resource.util.MyTrustManager;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide method to download resources.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
class DownloadResourceUtil {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String RESOURCE_URI_PREFIX_HTTP = "http://";
	private static final String RESOURCE_URI_PREFIX_UPPER_HTTP = "HTTP://";
	private static final String RESOURCE_URI_PREFIX_HTTPS = "https://";
	private static final String RESOURCE_URI_PREFIX_UPPER_HTTPS = "HTTPS://";

	private String localCacheDirURL;
	
	private boolean ready;
	private static int fileCount = 0;
	
	private static final int BUFFER_SIZE = 10240;
	
	/**
	 * Constructor.
	 * Provide the reference of localCacheDirURL to local variable.
	 * 
	 * @param localCacheDirURL a String value specifies local cache directory path URL.
	 */
	DownloadResourceUtil(String localCacheDirURL) {
		
		if ( localCacheDirURL == null )
			return;
		
		if ( !localCacheDirURL.endsWith("/") || !localCacheDirURL.endsWith("\\") )
			this.localCacheDirURL = localCacheDirURL + "/";
		else
			this.localCacheDirURL = localCacheDirURL;
		
		if ( !prepareDirectory(localCacheDirURL) ) {
			logger.warning("Unable to prepare directory '"+localCacheDirURL+"'.");
			ready = false;
		} else {
			
			if ( !setSSLSettings() ) {
				logger.severe("Unable to set SSL Settings.");
				return;
			}
			ready = true;
		}
	}
	
	/**
	 * Create specified directory if it is not exists.
	 * 
	 * @param dirURI a String path of Directory URI
	 * 
	 * @return a boolean value specifies whether directory is proper or not
	 */
	private boolean prepareDirectory(String dirURI) {
		
		if ( dirURI == null ) {
			logger.warning("Directory URI is null.");
			return false;
		}
		
		File dir = null;
		
		try {
			dir = new File( new URI(dirURI) );
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : '"+dirURI+"' URI is invalid.");
		}
		
		if ( dir == null ) {
			logger.warning("Unable to get File object for the URI '"+dirURI+"'.");
			return false;
		}
		
		if ( !dir.exists() ) {
			
			if ( !dir.mkdirs() ) {
				logger.warning("Unable to create dorectory '"+dirURI+"'.");
				return false;
			}
			
		} else if ( !dir.isDirectory() ) {
			
			if ( !dir.delete() ) {
				logger.warning("Unable to delete directory '"+dirURI+"'.");
				return false;
			}
			
			if ( !dir.mkdirs() ) {
				logger.warning("Unable to create dorectory '"+dirURI+"'.");
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Download specified resource in local file system and return local file path.
	 * 
	 * @param resourceURL a String value of Resource URL
	 * 
	 * @return a String URL of local resource path.
	 */
	String downloadResource(String resourceURL) {
	
		if ( resourceURL == null ) {
			logger.warning("Resource URL is null.");
			return null;
		}
		
		if ( !ready ) {
			logger.severe("Utility is not ready to download resource.");
			return null;
		}
		
		if ( resourceURL.startsWith(RESOURCE_URI_PREFIX_HTTP) || resourceURL.startsWith(RESOURCE_URI_PREFIX_UPPER_HTTP) ) {
			
			return getResourceByHttpRequest(resourceURL);
			
		} else if ( resourceURL.startsWith(RESOURCE_URI_PREFIX_HTTPS) || resourceURL.startsWith(RESOURCE_URI_PREFIX_UPPER_HTTPS) ) {
			
			return getResourceByHttpsRequest(resourceURL);
		}
		
		logger.warning("'"+resourceURL+"' is not HTTP URL.");
		return null;
	}
	
	/**
	 * 
	 * @param resourceURL a String value of Resource URL
	 * 
	 * @return a String value of local file path URL.
	 */
	private String getResourceByHttpRequest(String resourceURL) {
	
		try {
			
			URL url = new URL(resourceURL);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
			conn.connect();
			
			InputStream  inputStream = conn.getInputStream();
			
			if ( conn.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+conn.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			String destFileURI = getDestFileURI();
			
			if ( destFileURI == null ) {
				logger.warning("Unable to create local file for Resource for resource URL '"+resourceURL+"'.");
				return null;
			}
			
			FileOutputStream out = new FileOutputStream( new File( new URI(destFileURI) ) );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			conn.disconnect();
			
			logger.info("Resource Downloaded as '"+destFileURI+"'.");
			
			return destFileURI;
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Invalid URL '"+resourceURL+"'.");
			//e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException : Going to download resource : Resource URL '"+resourceURL+"'.");
		} catch (Exception e) {
			logger.warning("Exception : Going to download resource : Resource URL '"+resourceURL+"'.");
		}
		
		return null;
	}
	
	/**
	 * Get specified resource.
	 * 
	 * @param resourceURL a String value of resource URL in HTTP format
	 * 
	 * @return a String specifies local resource path URI
	 */
	private String getResourceByHttpsRequest(String resourceURL) {
		
		try {
			
			URL url = new URL( encodeString(resourceURL) );
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setRequestMethod("GET");
			InputStream inputStream = httpsURLConnection.getInputStream();
			
			if ( httpsURLConnection.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+httpsURLConnection.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			String destFileURI = getDestFileURI();
			
			if ( destFileURI == null ) {
				logger.warning("Unable to create local file for Resource for resource URL '"+resourceURL+"'.");
				return null;
			}
			
			FileOutputStream out = new FileOutputStream( new File( new URI(destFileURI) ) );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			httpsURLConnection.disconnect();
			
			logger.info("Resource Downloaded as '"+destFileURI+"'.");
			return destFileURI;
			
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Going to download resource : Invalid URL '"+resourceURL+"'.");
		} catch (IOException e) {
			logger.warning("IOException : Going to download resource : Resource URL '"+resourceURL+"'.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Exception : Going to download resource : Resource URL '"+resourceURL+"'.");
		}
		
		return null;
	}
	
	
		private String getDestFileURI() {
		
		String destDir = (localCacheDirURL + getGMTDate()).replaceAll(" ", "%20");
		
		URI destDirUri = null;
		
		try {
			destDirUri = new URI(destDir);
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Creating URI form the URI String '"+destDir+"'.");
			return null;
		}
		
		int count = 0;
		while ( !(new File(destDirUri).mkdir() ) ) {
			
			destDir = destDir + "_" + fileCount;
			
			try {
				destDirUri = new URI(destDir);
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Going to retrieve resource '"+destDir+"'.");
				return null;
			}
			
			fileCount++;
			count++;
			
			if (count == 5) {
				logger.warning("Unable to create '"+destDir+"' Directory.");
				return null;
			}
		}
		
		return destDir + "/retrievedResource";
	}
	
	/**
	 * Get GMT value of current Data.
	 * 
	 * @return a GMT String of current Data.
	 */
	private String getGMTDate() {
		
		Date date = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date GMTDate = convertToGmt(date);
		
		return getDateWithTimeAndZone( dateFormat.format(GMTDate).replaceAll(":", "_").replaceAll("-", "_") );
		
	}
	
	
	/**
	 * Convert Date in GMT format.
	 * 
	 * @param date an Object of Data.
	 * @return Date in GMT format.
	 * 
	 * @see java.util.Date
	 */
	private Date convertToGmt( Date date ) {

		TimeZone tz = TimeZone.getDefault();

		Date ret = new Date( date.getTime() - tz.getRawOffset() );

		// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
		if ( tz.inDaylightTime( ret )){

			Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

			// check to make sure we have not crossed back into standard time
			// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
			if ( tz.inDaylightTime( dstDate )) {

				ret = dstDate;

			}
		}

		return ret;
	}
	
	
	/**
	 * Return the String value of specified Date with time and zone.
	 * 
	 * @param date a String value of date
	 * 
	 * @return a String value of Date with time and zone.
	 */
	private String getDateWithTimeAndZone( String date ) {
		
		if ( date == null )
			return null;
		
		if ( date.indexOf(" ") == -1 )
			return date;
		
		return date.substring( 0, date.indexOf(" ") ) + "T" + date.substring( date.indexOf(" ")+1 )+ "Z";
	}
	
	
	/**
	 * Replace specified characters from the string.
	 * 
	 * @param in a String value
	 * @return a String value
	 */
	private String encodeString(String in) {
		
		if ( in == null )
			return null;
		
		in = in.replace("#", "%23");
		in = in.replace(" ", "%20");
		
		return in;
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
			
			logger.info("NoSuchAlgorithmException : Set SSL settings.");
			
		} catch (KeyManagementException e) {
			
			logger.info("KeyManagementException : Set SSL settings.");
			
		}
		
		return false;
	}
	
	/*
	//Just for testing
	public static void main(String[] args) {
		
		String cacheDirURI = "file:///D:/test/";
		DownloadResourceUtil downloadResourceUtil = new DownloadResourceUtil(cacheDirURI);
		
		logger.info( downloadResourceUtil.downloadResource("https://gmail.com") );
	}
	*/
}
