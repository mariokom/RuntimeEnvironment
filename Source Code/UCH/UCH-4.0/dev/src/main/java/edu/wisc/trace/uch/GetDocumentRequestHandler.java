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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Handle the GetDocument request.
 * It gets document/resource from resource server or direct HTTP get/post request depends on request URI and/or postData.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class GetDocumentRequestHandler {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private UCH uch;
	
	/**
	 * Constructor.
	 * Provide the reference of UCH to local variable.
	 * 
	 * @param uch an Object of UCH
	 */
	GetDocumentRequestHandler(UCH uch) {
		
		this.uch = uch;
	}
	
	/**
	 * Get specified document/resource.
	 * It gets document/resource from resource server or direct HTTP get/post request depends on request URI and/or postData.
	 * 
	 * @param uri a String value of document URI
	 * @param postData a String value of post data
	 * 
	 * @return a String data of specified Document
	 */
	String getDocument(String uri, String postData) {
		
		if ( uri == null ) {
			logger.warning("Unable to get document because 'uri' is null.");
			return null;
		}
		
		if ( isUriContainsResServerAppPath(uri) ) {
		//if ( (resserver_appPath != null) && !resserver_appPath.trim().equals("") && (uri.indexOf(resserver_appPath) != -1) )  {
			
			return getResource(uri);
		    
		} else {
			
			logger.info("Getting Document");
			String responseString =  new HttpRequestUtil().getResponseData(uri, postData);
			
			logger.info("Response String ::::::::::::::::: "+responseString);
			//if ( ( (responseString == null) || responseString.trim().equals("") )
			//   && ( (uch.getResServerAppPath() == null) || uch.getResServerAppPath().trim().equals("") ) ) {
			if ( ( (responseString == null) || responseString.trim().equals("") ) ) {	//Parikshit Thakur : 20111109. Changed condition.
				return getResource(uri);
			} else {
				
				//return null; // Parikshit Thakur : modified code to return requested ducument if available at local res server. 
				return responseString;
			}
		}
	}
	
	private String getResource(String uri) {
		
		if ( uri == null )
			return null;
		
		String filePath = null;
		
		//Parikshit Thakur : 20111024. Modified code to get document with file URL.
		if(uri.contains("file://")){
			filePath = uri;
		}else{
			filePath = uch.retrieveResource(uri);
		}
		
		
		if ( filePath == null ) 
			return null;
		
	    
		try {
			
			File file = new File( new URI(filePath) );
			
			FileInputStream fstream = new FileInputStream( file );
			DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader( new InputStreamReader(in) );
		    
		    StringBuilder sb = new StringBuilder();
		    String strLine;
		    
		    while ( ( strLine = br.readLine() ) != null )
		    	sb.append( strLine + "\n" );
		    
		    br.close();
		    in.close();
		    fstream.close();
		   // logger.info("---------"+sb.toString());
		    return sb.toString();
		    
		} catch (FileNotFoundException e) {
			logger.warning("FileNotFoundException : Reading file '"+filePath+"'.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException : Reading file '"+filePath+"'.");
			e.printStackTrace();
		} catch (URISyntaxException e){
			logger.warning("URISyntaxException : Reading file '"+filePath+"'.");
			e.printStackTrace();
		}
	   
	    return null;
	}
	/**
	 * Check whether URI contains resource server application path.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a boolean value
	 */
	private boolean isUriContainsResServerAppPath(String uri) {
		
		if ( uri == null )
			return false;
		
		return uch.isUriContainsResServerAppPath(uri);
	}
	
	
}
