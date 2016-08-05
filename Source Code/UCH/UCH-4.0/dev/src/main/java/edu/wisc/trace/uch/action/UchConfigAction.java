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

package edu.wisc.trace.uch.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide method to perform Different UCH Operation like GetCompatibelUIs, get username, update user, clear cache and config.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UchConfigAction {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String SERVLET_PATH_GET_COMPATIBLE_UIS = "GetCompatibleUIs";
	private static final String SERVLET_PATH_CACHE = "cache";
	private static final String SERVLET_PATH_GET_USER_NAME = "getusername";
	private static final String SERVLET_PATH_UPDATE_USER = "updateuser";
	private static final String SERVLET_PATH_CONFIG = "config";
	private static final String SERVLET_PATH_VERSION ="information";
	private ServletContext servletContext;
	/**
	 * Default Constructor.
	 */
	public UchConfigAction() {
		
	}
	
	/**
	 * Perform Different UCH Operation like GetCompatibelUIs, get username, update user, clear cache and config.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * @param servletContext an Object of ServletContext
	 * @param method a String value of method
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, 
			ServletContext servletContext, String method) {
		this.servletContext = servletContext;
		String servletPath = request.getServletPath();
		if ( servletPath == null || (servletPath.length() < 2) )
			return;
		
		servletPath = servletPath.substring(1);
		
		UCH uch = null;
		
		try {		
			
			Object obj = servletContext.getAttribute(Constants.CONSTANT_UCH);
			if ( obj instanceof UCH )
				uch = (UCH)obj;
			
		} catch(Exception e) {
			logger.severe("Unable to get UCH.");
		}
		
		if ( uch == null )
			return;
		
		
		if ( servletPath.equals(SERVLET_PATH_GET_COMPATIBLE_UIS) ) {
			
			try {
				
				String hostName = getHostName(request);
				
				String responseXML = uch.getCompatibleUIs(hostName, request);
				
				PrintWriter out = response.getWriter();
				logger.info("Final Response :"+responseXML);
				
				if ( responseXML != null )
					out.println(responseXML);
				
			} catch (IOException e) {
				logger.warning("IOException : Performing getCompatibleUIs on UCH.");
			}
			
		} else if( servletPath.equals(SERVLET_PATH_CACHE) ) {
			
			uch.handleCacheRequest(request, response);
		/*
		 * On retrieving the Ajax request to the given URL the Servlet
		 *  respond with the URC Version out of the MANIFEST InputStream
		 */
		
		} else if( servletPath.equals(SERVLET_PATH_VERSION) ) {
			try {
//				InputStream input = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
				InputStream input = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
				String uchVersion = uch.getUchVersion(input);
				String uchBuild = uch.getUchBuild(input);
				PrintWriter out = response.getWriter();
				out.write("Version: "+uchVersion + "<br> Build date: "+ uchBuild);
			} catch (IOException e) {
				logger.warning("IOException : Performing GetURCVersion on UCH.");
			}
			
		} else if( servletPath.equals(SERVLET_PATH_GET_USER_NAME) ) {
			
			uch.handleUserNameRequest(request, response);
			
		} else if( servletPath.equals(SERVLET_PATH_UPDATE_USER) ) {
			
			uch.handleUpdateUserAction(request, response);
			
		} else if ( servletPath.equals(SERVLET_PATH_CONFIG) ) {
			
			String ketValueStr = null;
			
			if ( method.equalsIgnoreCase("get") ) {
				
				ketValueStr = CommonUtilities.percentDecoding(request.getQueryString());
				
			} else {
				
				try {
					ketValueStr = getRequestBody(request);
				} catch (IOException e) {
					logger.warning("IOException : Getting Request Body.");
				}
			}
			
			if ( ketValueStr != null )
				uch.performConfigOperation(ketValueStr);
		}
		
	}
	
	/**
	 * Get the value of hostName from the request object.
	 * 
	 * @param request an Object of HttpServletRequest
	 * 
	 * @return a String value of Host Name
	 */
	private String getHostName(HttpServletRequest request) {
		
		if ( request == null )
			return null;
		
		String requestURL = request.getRequestURL().toString();
		
		if ( requestURL == null )
			return null;
		
		int doubleSlashIndex = requestURL.indexOf("//");
		
		if ( doubleSlashIndex == -1 )
			return null;
		
		int slashIndex = requestURL.indexOf("/", doubleSlashIndex+2);
		
		if ( slashIndex == -1 )
			return null;
		
		if ( slashIndex == -1 )
			return null;
		
		return requestURL.substring(doubleSlashIndex+2, slashIndex);
	}
	
	/**
	 * Return a String from HttpServletRequest's Body. 
	 * 
	 * @param request an object of HttpServletRequest
	 *  
	 * @return a String value of Request Body
	 * 
	 * @throws IOException Input/Output Exception
	 */
	private String getRequestBody(HttpServletRequest request) throws IOException {
		
		BufferedReader br = new BufferedReader(	new InputStreamReader( request.getInputStream() ) );
		
		String requestBody = "";
		String requestBodyLine = null;
		
		while ( (requestBodyLine = br.readLine()) != null ) 		
			requestBody += requestBodyLine;	
		
		return requestBody;
	}
	
}
