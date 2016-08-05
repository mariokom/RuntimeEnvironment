/*

OCopyright (C) 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch.resource.retrievalmanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import edu.wisc.trace.uch.resource.util.MyHostnameVerifier;
import edu.wisc.trace.uch.resource.util.MyTrustManager;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide Method to Query and Retrieve(both HTTP & HTTPS) Resources from Resource Server.
 * 
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class HttpRequestResponseUtil {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	private URL url;
	private String cacheDirUri;
	
	private String userName;
	private String password;
	
	private List<String> downloadingResources;
	private Map<String, String> uriLocalPathMap;
	
	
	private static int fileCount = 0;
	private static final int BUFFER_SIZE = 10240;
	private static long DOWNLOADING_SLEEP_TIME_SEC = 1;
	
	private boolean ready;
	

	
	/**
	 * Prepare class for Resource Query and Resource Retrieve using specified arguments.
	 * 
	 * @param resServerUrl a String value of Resource Server URL
	 * @param cacheDir a String value of Cache directory path
	 * @param userName a String value of userName
	 * @param password a String value of password
	 */
	HttpRequestResponseUtil(String resServerUrl, String cacheDir, String userName, String password) {
		
		if ( resServerUrl == null ) {
			logger.severe("Resource Server URL is null.");
			return;
		}
		
		if ( cacheDir == null ) {
			logger.severe("Cache Dir is null.");
			return;
		}
		
		resServerUrl = prepareResServerURL(resServerUrl, userName, password);
		
		try {
			url = new URL(resServerUrl);
		} catch (MalformedURLException e) {
			logger.severe("MalformedURLException : Initializing HttpRequestResponseUtil : Invalid URL '"+resServerUrl+"'.");
			return;
		}
		
		this.cacheDirUri = preparecacheDirUri(cacheDir);
		this.userName = userName;
		this.password = password;
		
		if ( (userName != null) && (password != null) ) { // Query through https://
			
			if ( !setSSLSettings() ) {
				logger.severe("Unable to set SSL Settings.");
				return;
			}
			
		} 

		uriLocalPathMap = new HashMap<String, String>();
		downloadingResources = new ArrayList<String>();
		
		ready = true;
	}
	
	/**
	 * Format specified cacheDir path properly and return it.
	 * 
	 * @param cacheDir a String value of Cache directory path
	 * 
	 * @return a String value of Cache Directory URL
	 */
	private String preparecacheDirUri(String cacheDir) {
		
		if ( cacheDir == null )
			return null;
		
		cacheDir = cacheDir.trim().replaceAll("%20", " ");
		
		if ( !cacheDir.endsWith("/") )
			cacheDir = cacheDir + "/";
		
		return cacheDir;
	}
	
	
	/**
	 * Format specified resServerUrl path properly and return it.
	 * 
	 * @param resServerUrl a String value of Resource Server URL
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return a String value of Resource Server URL
	 */
	private String prepareResServerURL(String resServerUrl, String userName, String password) {
		
		if ( resServerUrl == null )
			return null;
		
		resServerUrl = resServerUrl.trim();
		
		if ( resServerUrl.endsWith("/query") ) {
			//do nothing
		} else if ( resServerUrl.endsWith("/query/") ) {
			resServerUrl = resServerUrl.substring(0, resServerUrl.length()-1);
		} else if (  resServerUrl.endsWith("/") ) {
			resServerUrl = resServerUrl + "query";
		} else {
			resServerUrl = resServerUrl + "/query";
		}
		
		int colonIndex = resServerUrl.indexOf("://");
		
		if ( colonIndex != -1 ) 
			resServerUrl = resServerUrl.substring( colonIndex+3 );
		
		if ( (userName == null) || (password == null) ) 
			resServerUrl = "http://" + resServerUrl; 
		else 
			resServerUrl = "https://" + resServerUrl; 
		
		return resServerUrl;
	}
	
	/**
	 * Check whether class is ready for resource query or retrieve.
	 * @return
	 */
	boolean isReady() {
		return ready;
	}
	/**
	 * Send Query request on Resource Server and return response as a String.
	 * 
	 * @param requestBody a String value of Resource Server Query 
	 * @param resServer a string value of res server path
	 * @return a String value of query response.
	 */
	String queryResource(String requestBody, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer
		if ( !ready ) {
			logger.warning("Unable to Initialize class properly. So unable to query resource.");
			return null;
		}
		
		logger.info("Resource Server Query URL : "+url.toString());
		logger.info("Query Request Body : "+requestBody);
		
		if ( requestBody == null ) {
			logger.warning("queryRequestBody is null.");
			return null;
		}
		
		if ( (userName == null) || (password == null) ) { // send HTTP Query
			
			return httpQueryResource(requestBody, resServer);
			
		} else { // send HTTPS Query
			
			return httpsQueryResource(requestBody, resServer);
		}
		
	}
	
	/**
	 * Send HTTP Query request on Resource Server and return response as a String.
	 * 
	 * @param postData a String value of Http post Body
	 * 
	 * @return a String value of query response.
	 */
	private String httpQueryResource(String postData, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer
		URL resServerURL = null;
		
		if(resServer != null){
			
			resServer = prepareResServerURL(resServer, userName, password);
			
			try {
				resServerURL = new URL(resServer);
			} catch (MalformedURLException e) {
				logger.severe("MalformedURLException : Initializing HttpRequestResponseUtil : Invalid URL '"+resServer+"'.");
				
			}
			
		}
		
		if(resServerURL == null){ 
			resServerURL = url;
		}
		
		if ( resServerURL == null ) {
			logger.warning("Resource Server URL is null.");
			return null;
		}
		
		try {
			
			HttpURLConnection conn = (HttpURLConnection)resServerURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			DataOutputStream printout = new DataOutputStream( conn.getOutputStream() );
			
		   if ( postData != null )
			 	printout.writeBytes(postData);	
		   else
				printout.writeBytes("");
		  
		   BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		   StringBuilder sb = new StringBuilder();
		   String line;
		   
		   while ((line = rd.readLine()) != null) {
			  sb.append(line);
		   }
		   
		  rd.close();
		  return sb.toString();			  
			
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException");
			//e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException");
			//e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Exception");
			//e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Send HTTPS Query request on Resource Server and return response as a String.
	 * 
	 * @param postData a String value of Http post Body
	 * 
	 * @return a String value of query response.
	 */
	private String httpsQueryResource(String postData, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer
		URL resServerURL = null;
		
		if(resServer != null){
			
			resServer = prepareResServerURL(resServer, userName, password);
			
			try {
				resServerURL = new URL(resServer);
			} catch (MalformedURLException e) {
				logger.severe("MalformedURLException : Initializing HttpRequestResponseUtil : Invalid URL '"+resServer+"'.");
				
			}
			
		}
		
		if(resServerURL == null){ 
			resServerURL = url;
		}
		
		if ( resServerURL == null ) {
			logger.warning("Resource Server URL is null.");
			return null;
		}
		
		
		try {	

			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) resServerURL.openConnection();
			
			String encoding = new sun.misc.BASE64Encoder().encode ( (userName+":"+password).getBytes() );
			
			httpsURLConnection.setRequestProperty  ("Authorization", "Basic " + encoding);
			
			httpsURLConnection.setRequestMethod("POST");
			
			httpsURLConnection.setDoInput(true);    
			httpsURLConnection.setDoOutput(true);		    
			httpsURLConnection.setUseCaches(false);
			 
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			DataOutputStream dataOutputStream = new DataOutputStream( httpsURLConnection.getOutputStream() );
			
			 if ( postData != null )
				 dataOutputStream.writeBytes(postData);	
			   else
				 dataOutputStream.writeBytes("");
			
			dataOutputStream.flush();
			dataOutputStream.close();
			
			if ( httpsURLConnection.getResponseCode() != 200 ) {
				logger.warning("Query : Response code '"+httpsURLConnection.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			BufferedReader br = new BufferedReader( new InputStreamReader(httpsURLConnection.getInputStream() ) );
			
			StringBuilder sb = new StringBuilder();
			
			String line;
			
			while ( ( line = br.readLine() ) != null ){
				sb.append(line);
			}
			
			br.close();
			
			httpsURLConnection.disconnect();
			
			String queryResponse = sb.toString().trim();

			if ( queryResponse.equals("") )
				return null;
			else
				return queryResponse;
			
		} catch (IOException e) {
			logger.warning("IOException : \nRequest Data : "+ postData);
			logger.warning("Unable to send Query Request on Resource Server.");
			//e.printStackTrace();
			
		}  catch (ClassCastException e) {
			
			logger.warning("ClassCastException");
			//e.printStackTrace();
			
		} catch (Exception e) {
			logger.warning("Exception");
			//e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	
	/**
	 * Retrieve Resource from Resource Server and returns Retrieved Resource File URI.
	 * 
	 * @param retrievalUri a String value of Resource Retrieval URI
	 * @return a String value of Retrieved Resource File URI
	 */
	/*String retrieveResource(String retrievalUri) {
		//String retrieveResource(String retrievalUri,String fileName) {	
		logger.info("@@ in retrievalResource method of HTTpRequestRESPONSE-RetrievalURI="+retrievalUri);
		if ( !ready ) {
			logger.warning("Unable to Initialize class properly. So unable to query resource.");
			return null;
		}
		
		if( retrievalUri == null ) {
			logger.warning("Resource Retrieval URL is null.");
			return null;
		}
		
		if ( (userName == null) || (password == null) ) { // send HTTP Query
			
			if ( retrievalUri.startsWith("https://") )
				retrievalUri = "http" + retrievalUri.substring(5);
			
		} else { // send HTTPS Query
			
			if ( retrievalUri.startsWith("http://") )
				retrievalUri = "https" + retrievalUri.substring(4);
		}

		// If resource is already downloaded then return it  
		synchronized (uriLocalPathMap) {
			if ( uriLocalPathMap.containsKey(retrievalUri) ) {
				
				return uriLocalPathMap.get(retrievalUri);
			}
		}
		
		
		logger.info("Retrieval URL : "+retrievalUri);
		
		if ( (userName == null) || (password == null) ) { // send HTTP Query
			
			return httpRetrieveResource(retrievalUri);
			
		} else { // send HTTPS Query
			
			return httpsRetrieveResource(retrievalUri);
		}
		
	}*/
	
	/**
	 * Retrieve Resource from Resource Server and returns Retrieved Resource File URI.
	 * 
	 * @param retrievalUri a String value of Resource Retrieval URI
	 * @param fileName a String value of resource file name.
	 * @return a String value of Retrieved Resource File URI
	 */
	String retrieveResource(String retrievalUri,String fileName) {	
		logger.info("@@ HTTpRequestRESPONSE-RetrievalURI="+retrievalUri);
		if ( !ready ) {
			logger.warning("Unable to Initialize class properly. So unable to query resource.");
			return null;
		}
		
		if( retrievalUri == null ) {
			logger.warning("Resource Retrieval URL is null.");
			return null;
		}
		
		if ( (userName == null) || (password == null) ) { // send HTTP Query
			
			if ( retrievalUri.startsWith("https://") )
				retrievalUri = "http" + retrievalUri.substring(5);
			
		} else { // send HTTPS Query
			
			if ( retrievalUri.startsWith("http://") )
				retrievalUri = "https" + retrievalUri.substring(4);
		}

		// If resource is already downloaded then return it  
		synchronized (uriLocalPathMap) {
			if ( uriLocalPathMap.containsKey(retrievalUri) ) {
				
				return uriLocalPathMap.get(retrievalUri);
			}
		}
		
		
		logger.info("Retrieval URL : "+retrievalUri);
		
		if ( (userName == null) || (password == null) ) { // send HTTP Query
			
			//return httpRetrieveResource(retrievalUri);
			return httpRetrieveResource(retrievalUri,fileName);
			
		} else { // send HTTPS Query
			
			//return httpsRetrieveResource(retrievalUri);
			return httpsRetrieveResource(retrievalUri,fileName);
		}
		
	}
	
	/**
	 * Download Resource using HTTP Get method to the URL specified by globalAt.
	 * 
	 * @param globalAt a String value of globalAt
	 * 
	 * @return a String value of Downloaded local path
	 */
	/*private String httpRetrieveResource(String globalAt) {
		
		logger.info("@@## HttpRequestResponse=+ ");
		URL globalAtURL = null;
		
		try {
			globalAtURL = new URL( encodeString(globalAt) );
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Going to Retrieve Resource : Invalid URL '"+globalAt+"'.");
		}
		logger.info("#@# globalAt : "+globalAt);
		if ( globalAtURL == null ) {
			logger.warning("'"+globalAt+"' is not in proper form of URL.");
			return null;
		}
		
		// If the same resource is downloading then wait untill the resources is downloaded and return it.
		// Else push it in downloading Resources and when downloaded then delete it.
		
		boolean flag = true;
		while( flag ) {
			
			boolean waiting = false;
			logger.info("@#@ now it should print..");
			synchronized (downloadingResources) {
				
				// testing
				for(String str:downloadingResources)
				{
					logger.info("#$ - "+str);
				}
				//testing ends
				
				if ( downloadingResources.contains(globalAt) ) {
					waiting = true;
				} else {	
					downloadingResources.add(globalAt);
					break;
				}
			}
			
			try {
				Thread.sleep(DOWNLOADING_SLEEP_TIME_SEC*1000);
			} catch (InterruptedException e) {
				logger.warning("InterruptedException");
				//e.printStackTrace();
			}
			logger.info("#$# control is here...");
			if ( waiting == false ) {
				
				synchronized (uriLocalPathMap) {
					
					// testing
					Set<String> keys = uriLocalPathMap.keySet();
					for(String key:keys)
					{
						logger.info("!! "+key+" -=- "+uriLocalPathMap.get(key));
					}
					// testing ends
					
					if ( uriLocalPathMap.containsKey(globalAt) ) {
						return uriLocalPathMap.get(globalAt);
					}
				}
			}
		}
		
		try {
		
			HttpURLConnection conn;
			
			conn = (HttpURLConnection)globalAtURL.openConnection ();			
			
			conn.setRequestMethod("GET");
			
			InputStream  inputStream = conn.getInputStream();
			
			if ( conn.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+conn.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			String destDir = (cacheDirUri + getGMTDate()).replaceAll(" ", "%20");
			logger.info("@#$ cacheDirUri = "+cacheDirUri+" And destDir = "+destDir);
			URI destDirUri = null;
			
			try {
				destDirUri = new URI(destDir);
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Creating URI form URI String '"+destDir+"'.");
				return null;
			}
			
			int count = 0;
			while ( !(new File(destDirUri).mkdir() ) ) {
				
				destDir = destDir + "_" + fileCount;
				
				try {
					destDirUri = new URI(destDir);
				} catch (URISyntaxException e) {
					logger.warning("URISyntaxException : Creating URI form URI String '"+destDir+"'.");
					return null;
				}
				
				fileCount++;
				count++;
				
				if (count == 5) {
					logger.warning("Unable to create '"+destDir+"' Directory.");
					return null;
				}
			}
			
			String destinationFileUri = destDir + "/retrievedResource";
			
			File destinationFile = null;
			
			try {
				destinationFile = new File( new URI(destinationFileUri) );
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Creating URI form URI String '"+destinationFileUri+"'.");
				return null;
			}
			
			FileOutputStream out = new FileOutputStream( destinationFile );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			conn.disconnect();
			
			logger.info("Resource Downloaded as '"+destinationFile+"'.");
			
			synchronized (downloadingResources) {
				
				downloadingResources.remove(globalAt);
				
				synchronized (uriLocalPathMap) {
					uriLocalPathMap.put(globalAt, destinationFileUri);
				}
			}
			
			
			return destinationFileUri;
			
		} catch (IOException e) {
			logger.warning("IOException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (ClassCastException e) {
			logger.warning("ClassCastException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (Exception e) {
			logger.warning("Exception");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		}
		
		return null;
	}*/
	
	/**
	 * Download Resource using HTTP Get method to the URL specified by globalAt.
	 * 
	 * @param globalAt a String value of globalAt
	 * @param fileName a String value of resource file name.
	 * @return a String value of Downloaded local path
	 */
	private String httpRetrieveResource(String globalAt,String fileName) {
		
		logger.info("@@## HttpRequestResponse=+ "+fileName);
		URL globalAtURL = null;
		
		try {
			globalAtURL = new URL( encodeString(globalAt) );
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Going to Retrieve Resource : Invalid URL '"+globalAt+"'.");
		}
		logger.info("#@# globalAt : "+globalAt);
		if ( globalAtURL == null ) {
			logger.warning("'"+globalAt+"' is not in proper form of URL.");
			return null;
		}
		
		// If the same resource is downloading then wait untill the resources is downloaded and return it.
		// Else push it in downloading Resources and when downloaded then delete it.
		
		boolean flag = true;
		while( flag ) {
			
			boolean waiting = false;
			logger.info("@#@ now it should print..");
			synchronized (downloadingResources) {
				
				if ( downloadingResources.contains(globalAt) ) {
					waiting = true;
				} else {	
					downloadingResources.add(globalAt);
					break;
				}
			}
			
			try {
				Thread.sleep(DOWNLOADING_SLEEP_TIME_SEC*1000);
			} catch (InterruptedException e) {
				logger.warning("InterruptedException");
				//e.printStackTrace();
			}
			
			if ( waiting == false ) {
				
				synchronized (uriLocalPathMap) {
					
					// testing
					Set<String> keys = uriLocalPathMap.keySet();
					for(String key:keys)
					{
						logger.info("!! "+key+" -=- "+uriLocalPathMap.get(key));
					}
					// testing ends
					
					if ( uriLocalPathMap.containsKey(globalAt) ) {
						return uriLocalPathMap.get(globalAt);
					}
				}
			}
		}
		
		try {
		
			HttpURLConnection conn;
			
			conn = (HttpURLConnection)globalAtURL.openConnection ();			
			
			conn.setRequestMethod("GET");
			
			InputStream  inputStream = conn.getInputStream();
			
			if ( conn.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+conn.getResponseCode()+"' is not '200'.");
				return null;
			}
			// the 'file name' should appear in the resource directory name, if it present else the current implementation should work.
			String destDir = (fileName!=null)?(cacheDirUri + getGMTDate()+"_"+fileName).replaceAll(" ", "%20"):(cacheDirUri + getGMTDate()).replaceAll(" ", "%20");
			logger.info("@#$ cacheDirUri = "+cacheDirUri+" And destDir = "+destDir);
			URI destDirUri = null;
			
			try {
				destDirUri = new URI(destDir);
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
				logger.warning("URISyntaxException : Creating URI form URI String '"+destDir+"'.");
				return null;
			}
			
			int count = 0;
			while ( !(new File(destDirUri).mkdir() ) ) {
				
				destDir = destDir +"_"+fileCount;
				
				try {
					destDirUri = new URI(destDir);
				} catch (URISyntaxException e) {
					logger.warning("URISyntaxException : Creating URI form URI String '"+destDir+"'.");
					return null;
				}
				
				fileCount++;
				count++;
				
				if (count == 5) {
					logger.warning("Unable to create '"+destDir+"' Directory.");
					return null;
				}
			}
			
			//String destinationFileUri = destDir + "/retrievedResource";
			
			// if 'file name' is not null then it should be appended here else "retrievedResource".
			if(fileName!=null) // to continue with old implementation also.
				fileName = fileName.replaceAll(" ", "%20");
			String destinationFileUri = destDir + "/"+(fileName==null ? "retrievedResource" : fileName);
			logger.info("^#^ destinationFileURI : "+destinationFileUri);
			File destinationFile = null;
			
			try {
				destinationFile = new File( new URI(destinationFileUri) );
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Creating URI form URI String '"+destinationFileUri+"'.");
				return null;
			}
			
			FileOutputStream out = new FileOutputStream( destinationFile );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			conn.disconnect();
			
			logger.info("Resource Downloaded as '"+destinationFile+"'.");
			
			synchronized (downloadingResources) {
				
				downloadingResources.remove(globalAt);
				
				synchronized (uriLocalPathMap) {
					uriLocalPathMap.put(globalAt, destinationFileUri);
				}
			}
			
			
			return destinationFileUri;
			
		} catch (IOException e) {
			logger.warning("IOException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (ClassCastException e) {
			logger.warning("ClassCastException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (Exception e) {
			logger.warning("Exception");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		}
		
		return null;
	}
	
	/**
	 * Download Resource using HTTPS Get method to the URL specified by globalAt.
	 * 
	 * @param globalAt a String value of globalAt
	 * 
	 * @return a String value of Downloaded local path
	 */
	/*private String httpsRetrieveResource(String globalAt) {
		
		URL globalAtURL = null;
		
		try {
			globalAtURL = new URL( encodeString(globalAt) );
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Going to retrieve resource : Invalid URL '"+globalAt+"'.");
			//e.printStackTrace();
		}
		
		if ( globalAtURL == null ) {
			logger.warning("'"+globalAt+"' is not in proper form of URL.");
			return null;
		}
		
		// If the same resource is downloading then wait untill the resources is downloaded and return it.
		// Else push it in downloading Resources and when downloaded then delete it.
		
		boolean flag = true;
		while( flag ) {
			
			boolean waiting = false;
			
			synchronized (downloadingResources) {
				
				if ( downloadingResources.contains(globalAt) ) {
					waiting = true;
				} else {	
					downloadingResources.add(globalAt);
					break;
				}
			}
			
			try {
				Thread.sleep(DOWNLOADING_SLEEP_TIME_SEC*1000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				logger.warning("InterruptedException");
			}
			
			if ( waiting == false ) {
				
				synchronized (uriLocalPathMap) {
					
					if ( uriLocalPathMap.containsKey(globalAt) ) {
						return uriLocalPathMap.get(globalAt);
					}
				}
			}
		}
		
		try {
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) globalAtURL.openConnection();
			
			String encoding = new sun.misc.BASE64Encoder().encode ( (userName+":"+password).getBytes() );
			
			httpsURLConnection.setRequestProperty  ("Authorization", "Basic " + encoding);
			
			httpsURLConnection.setRequestMethod("GET");
			
			InputStream inputStream = httpsURLConnection.getInputStream();
			
			
			String destinationFile = cacheDir + fileCount + "-file";
			fileCount++;
			
			
			if ( httpsURLConnection.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+httpsURLConnection.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			// the 'file name' should appear in the resource directory name, if it present else the current implementation should work.
			//String destDir = (fileName!=null)?(cacheDirUri + getGMTDate()+"_"+fileName).replaceAll(" ", "%20"):(cacheDirUri + getGMTDate()).replaceAll(" ", "%20"); 
			String destDir = (cacheDirUri + getGMTDate()).replaceAll(" ", "%20");
			
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
			
			// if 'file name' is not null then it should be appended here else "retrievedResource".
			
			String destinationFileUri = destDir + "/retrievedResource";
			
			File destinationFile = null;
			
			try {
				destinationFile = new File( new URI(destinationFileUri) );
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Creating URI from URI String '"+destinationFileUri+"'.");
			}
			
			FileOutputStream out = new FileOutputStream( destinationFile );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			httpsURLConnection.disconnect();
			
			logger.info("Resource Downloaded as '"+destinationFile+"'.");
			
			synchronized (downloadingResources) {
				
				downloadingResources.remove(globalAt);
				
				synchronized (uriLocalPathMap) {
					uriLocalPathMap.put(globalAt, destinationFileUri);
				}
			}
			
			return destinationFileUri;
			
		} catch (IOException e) {
			logger.warning("IOException : downloading Resource '"+globalAt+"'.");
			
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (ClassCastException e) {
			logger.warning("ClassCastException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (Exception e) {
			logger.warning("Exception");
			//e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		}
		
		return null;
	}*/
	
	
	
	/**
	 * Download Resource using HTTPS Get method to the URL specified by globalAt.
	 * 
	 * @param globalAt a String value of globalAt
	 * 
	 * @return a String value of Downloaded local path
	 */
	private String httpsRetrieveResource(String globalAt,String fileName) {
		
		URL globalAtURL = null;
		
		try {
			globalAtURL = new URL( encodeString(globalAt) );
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Going to retrieve resource : Invalid URL '"+globalAt+"'.");
			//e.printStackTrace();
		}
		
		if ( globalAtURL == null ) {
			logger.warning("'"+globalAt+"' is not in proper form of URL.");
			return null;
		}
		
		// If the same resource is downloading then wait untill the resources is downloaded and return it.
		// Else push it in downloading Resources and when downloaded then delete it.
		
		boolean flag = true;
		while( flag ) {
			
			boolean waiting = false;
			
			synchronized (downloadingResources) {
				
				if ( downloadingResources.contains(globalAt) ) {
					waiting = true;
				} else {	
					downloadingResources.add(globalAt);
					break;
				}
			}
			
			try {
				Thread.sleep(DOWNLOADING_SLEEP_TIME_SEC*1000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				logger.warning("InterruptedException");
			}
			
			if ( waiting == false ) {
				
				synchronized (uriLocalPathMap) {
					
					if ( uriLocalPathMap.containsKey(globalAt) ) {
						return uriLocalPathMap.get(globalAt);
					}
				}
			}
		}
		
		try {
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) globalAtURL.openConnection();
			
			String encoding = new sun.misc.BASE64Encoder().encode ( (userName+":"+password).getBytes() );
			
			httpsURLConnection.setRequestProperty  ("Authorization", "Basic " + encoding);
			
			httpsURLConnection.setRequestMethod("GET");
			
			InputStream inputStream = httpsURLConnection.getInputStream();
			
			/*
			String destinationFile = cacheDir + fileCount + "-file";
			fileCount++;
			*/
			
			if ( httpsURLConnection.getResponseCode() != 200 ) {
				logger.warning("Retrieve : Response code '"+httpsURLConnection.getResponseCode()+"' is not '200'.");
				return null;
			}
			
			// the 'file name' should appear in the resource directory name, if it present else the current implementation should work.
			String destDir = (fileName!=null)?(cacheDirUri + getGMTDate()+"_"+fileName).replaceAll(" ", "%20"):(cacheDirUri + getGMTDate()).replaceAll(" ", "%20"); 
			//String destDir = (cacheDirUri + getGMTDate()).replaceAll(" ", "%20");
			
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
			
			// if 'file name' is not null then it should be appended here else "retrievedResource".
			fileName = fileName.replaceAll(" ", "%20");
			String destinationFileUri = destDir + "/"+(fileName==null ? "retrievedResource" : fileName);
			
			//String destinationFileUri = destDir + "/retrievedResource";
			
			File destinationFile = null;
			
			try {
				destinationFile = new File( new URI(destinationFileUri) );
			} catch (URISyntaxException e) {
				logger.warning("URISyntaxException : Creating URI from URI String '"+destinationFileUri+"'.");
			}
			
			FileOutputStream out = new FileOutputStream( destinationFile );
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				
				int bytes = inputStream.read(buffer);
				if (bytes < 0)
					break;
				
				out.write(buffer, 0, bytes);	
			}
			
			inputStream.close();
			httpsURLConnection.disconnect();
			
			logger.info("Resource Downloaded as '"+destinationFile+"'.");
			
			synchronized (downloadingResources) {
				
				downloadingResources.remove(globalAt);
				
				synchronized (uriLocalPathMap) {
					uriLocalPathMap.put(globalAt, destinationFileUri);
				}
			}
			
			return destinationFileUri;
			
		} catch (IOException e) {
			logger.warning("IOException : downloading Resource '"+globalAt+"'.");
			
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} /*catch (ClassCastException e) {
			logger.warning("ClassCastException");
			e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		} catch (Exception e) {
			logger.warning("Exception");
			//e.printStackTrace();
			synchronized (downloadingResources) {
				downloadingResources.remove(globalAt);
			}
		}*/
		
		return null;
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
			
			logger.warning("NoSuchAlgorithmException : Set SSL settings.");
			
		} catch (KeyManagementException e) {
			
			logger.warning("KeyManagementException : Set SSL settings.");
			
		}
		
		return false;
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
	 * Clear the specified resources from cache directory and returns the deleted file paths.
	 * 
	 * @param pathToDelete an Object of List&lt;String&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> clearCache(List<String> pathToDelete) {
		
		if ( pathToDelete == null )
			return null;
			
		return clear(pathToDelete);
	}
	
	/**
	 * Try to delete all files in cache directory and return those fileUri which can't be deleted because they are used by other resource. 
	 *
	 * @return an Object of List&lt;String&gt; containing not deleted file.
	 */
	public List<String> clearCache() {
		
		Collection<String> pathList = null;
		
		if ( uriLocalPathMap == null )
			return null;
		
		synchronized (uriLocalPathMap) {
			pathList = uriLocalPathMap.values();
		}
		
		return clear(pathList);
	}
	
	/**
	 * Clear all the resources specified by pathList.
	 * 
	 * @param pathList an Object of Collection&lt;String&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	private List<String> clear(Collection<String> pathList) {
		
		List<String> deletedUris = new ArrayList<String>();
		
		for ( String uriPath : pathList) {
			
			if ( uriPath.indexOf("/") != -1 ) {
				
				String parentDir = uriPath.substring(0, uriPath.lastIndexOf("/"));
				
				if ( CommonUtilities.deleteFileOrDir(parentDir) ) 
					deletedUris.add(uriPath);
				
			}
				
		}
		
		synchronized (uriLocalPathMap) {
			
			List<String> keysToDelete = new ArrayList<String>();
			
			for ( String key : uriLocalPathMap.keySet() ) {
				
				String value = uriLocalPathMap.get(key);
				
				if ( deletedUris.contains(value) ) 
					keysToDelete.add(key);	
			}
			
			for ( String key : keysToDelete ) {
				uriLocalPathMap.remove(key);
			}
		}

		return deletedUris;
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
	 * Sets the res server path to download resources.
	 * @param resServerPath
	 */
	public void setResServerPath(String resServerPath){
		resServerPath = prepareResServerURL(resServerPath, userName, password);
		try {
			url = new URL(resServerPath);
		} catch (MalformedURLException e) {
			logger.severe("MalformedURLException : Initializing HttpRequestResponseUtil : Invalid URL '"+resServerPath+"'.");
			return;
		}
	}
}
