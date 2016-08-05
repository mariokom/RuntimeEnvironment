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

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide method to open user context.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class OpenUserContextAction {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String REQUEST_HEADER_AUTHORIZATION = "Authorization";
	private static final String REQUEST_HEADER_TOKEN_BASIC = "Basic";
	
	private static final String REQUEST_PARAMETER_FORWARD_TO = "forwardto";
	
	/**
	 * Open User Context.
	 * If request doesn't contain Basic Authentication Header then ask for basic authentication.
	 * Else if user authentication failed then response with response status 401 Authentication failed.
	 * If authentication succeeded then open a new session.
	 * If forwardto request attribute is specified then redirect to that URL.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * @param uch an Object of UCH
	 * 
	 * @throws ServletException an Object of ServletException
	 * @throws IOException an Object of IOException
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, UCH uch)
			throws ServletException, IOException {
		
		if ( uch == null ) {
			logger.severe("UCH is null when opening User Context.");
			setHttpStatus500(response);
			return;
		}
		
		String forwardTo = request.getParameter(REQUEST_PARAMETER_FORWARD_TO);
		
		String credentials = getCredentials(request);
		
		if ( credentials == null ) {
			
			logger.warning("Unable to get credentials for Basic Authentication.");
			setHttpStatus401(response);
			return;
		}
		
		int colonIndex = credentials.indexOf(":");
		if ( colonIndex == -1 ) {
		
			logger.warning("Currepted credentials.");	    	 
			setHttpStatus401(response);
			return;
		}
         
        String userName = credentials.substring(0, colonIndex );
        String password = credentials.substring( colonIndex+1 );
        
        if ( isUserValid(request, userName, password, uch) ) { // For valid User
        	
        	 openHttpSession(request, userName, password, uch);
        	 
        	 if ( forwardTo == null ) {
             	
             	setHttpStatus200(response);
             	
             } else {
             	
             	//TODO Encode URL
             	setHttpStatus301(response, forwardTo);
             }
        	 
        } else { // For invalid User
        	
        	setHttpStatus401Unauthorized(response);
        }
      
	}
	
	/**
	 * Open a new HTTP Session.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of UserName
	 * @param password a String value of Password
	 * @param uch an Object of UCH
	 */
	private void openHttpSession(HttpServletRequest request, String userName, String password, UCH uch) {
		
		if ( request == null )
			return;
		
		uch.openUserContext(request, userName, password);
	}
	
	
	/**
	 * Check whether the specified userName and password is valid.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of UserName
	 * @param password a String value of Password
	 * @param uch an Object of UCH
	 * 
	 * @return a boolean value specifies that userName and password is valid or not
	 */
	private boolean isUserValid(HttpServletRequest request, String userName, String password, UCH uch) {
		
		return uch.validateContextUser(request, userName, password);
	}
	
	
	/**
	 * Get credential of Basic Authentication from the request Object.
	 *  
	 * @param request an Object of HttpServletRequest
	 * 
	 * @return a String value of credential
	 * 
	 * @throws IOException an Object of IOException
	 */
	private String getCredentials(HttpServletRequest request) throws IOException {
		
		if ( request == null ) {
			logger.warning("Request is null.");
			return null;
		}
		
		String authHeader = request.getHeader(REQUEST_HEADER_AUTHORIZATION);
		
		if ( authHeader == null ) {
			logger.warning("Request doesn't have header '"+REQUEST_HEADER_AUTHORIZATION+"'.");
			return null;
		}
			
		StringTokenizer st = new StringTokenizer(authHeader);
	    
	    if ( !st.hasMoreTokens() ) {
	    	
	    	logger.warning("Token missing.");	    	 
	    	return null;
	    }
	    
	    String basic = st.nextToken();
	    
	    if ( !basic.equalsIgnoreCase(REQUEST_HEADER_TOKEN_BASIC) ) {
         	
	    	logger.warning("Basic Token missing.");
         	return null;
         }

	    String credentials = st.nextToken();
        
        credentials = new String( new BASE64Decoder().decodeBuffer(credentials) );
        
		return credentials;
	}
	
	
	/**
	 * Set the response status 200.
	 * 
	 * @param response an Object of HttpServletResponse
	 */
	private void setHttpStatus200(HttpServletResponse response) {
		
		logger.info(" Status code 200 OK.");
		response.setStatus(200);
	}
	
	/**
	 * Set the response status 401, with the header WWW-Authenticate and value WWW-Authenticate=Basic realm=\"Login required\"
	 * 
	 * @param response an Object of HttpServletResponse
	 */
	private void setHttpStatus401(HttpServletResponse response) {
		
		logger.warning(" Status code 401 is set. WWW-Authenticate=Basic realm=\"Login required\" ");
		response.setHeader("WWW-Authenticate","Basic realm=\"Login required\"");
		response.setStatus(401);
	}
	
	/**
	 * Set the response status 401, with the header WWW-Authenticate and value WWW-Authenticate=Unauthorized
	 * 
	 * @param response an Object of HttpServletResponse
	 */
	private void setHttpStatus401Unauthorized(HttpServletResponse response) {
		
		logger.warning(" Status code 401 is set. WWW-Authenticate=Unauthorized ");
		response.setHeader("WWW-Authenticate","Unauthorized");
		response.setStatus(401);
	}
	
	/**
	 * Set the response status 301, with the header Location and value is specified redirectUri.
	 * 
	 * @param response an Object of HttpServletResponse
	 * @param redirectUri a String value of redirect URI
	 */
	private void setHttpStatus301(HttpServletResponse response, String redirectUri) {
		
		logger.info(" Status code 301 is set. Location is '"+redirectUri+"'.");
		response.setHeader("Location", redirectUri);
		response.setStatus(301);
	}
	
	
	/**
	 * Set the response status 500.
	 * 
	 * @param response an Object of HttpServletResponse
	 */
	private void setHttpStatus500(HttpServletResponse response) {
		
		logger.info(" Status code 500 Internal Server Error.");
		response.setStatus(500);
	}
}
