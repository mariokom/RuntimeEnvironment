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

import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to validate userName and password against Resource Server Users.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UserValidation {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private String serverUrl;
	
	private static final String CONSTANTS_UTILITIES = "utilities";
	
	private static final String REQUEST_PARAMETER_NAME_ACTION = "action";
	private static final String REQUEST_PARAMETER_ACTION_VALUE_VALIDATE_USER = "validateuser";
	private static final String REQUEST_PARAMETER_NAME_USER_NAME = "userName";
	private static final String REQUEST_PARAMETER_NAME_PASSWORD = "password";
	
	/**
	 * Provide the reference of serverUrl to local variable.
	 * 
	 * @param serverUrl a String value of server URL
	 */
	public UserValidation(String serverUrl) {
		
		if ( serverUrl != null )
			this.serverUrl = serverUrl + "/" + CONSTANTS_UTILITIES;
	}
	
	/**
	 * Validate userName and password against Resource Server Users.
	 * 
	 * @param userName a String value of UserName
	 * @param password a String value of Password
	 * 
	 * @return a boolean value specifies whether userName and password is validated or not.
	 */
	public boolean validateUser(String userName, String password) {
		
		if ( serverUrl == null ) {
			logger.warning("Server URL is null.");
			return false;
		}
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return false;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return false;
		}
		
		String data = REQUEST_PARAMETER_NAME_ACTION + "=" + REQUEST_PARAMETER_ACTION_VALUE_VALIDATE_USER + "&"
					+ REQUEST_PARAMETER_NAME_USER_NAME + "=" + userName + "&"
					+ REQUEST_PARAMETER_NAME_PASSWORD + "=" + password;
		
		String queryString = new Encryption().encrypt(data);
		
		//logger.info(serverUrl);
		//logger.info(queryString);
		//String queryString = "0946095A1F10003D0500010B0A00020B00003C3D0033120209480A500105003D0000050946000A0B3C010101004711060B360A0A010E00322600020B4600050B32010B0100330E06075909464709003D010000060A000C073C011505003D12040A1306462844005032000C045000050450005A39000026020B1B0946150F00010F00040B5A000D0B0A00010E00290005060D0B46006300510400060B5A00010B0A003D0E000B07030B32";
		
		HttpsRequest request = new HttpsRequest(serverUrl);
		
		Map<String, String> resposeMap = request.sendGetRequest(queryString);
		
		if ( resposeMap == null ) {
			logger.warning("Response is null");
			return false;
		}
		
		String responseCode = resposeMap.get(HttpsRequest.CONSTANT_RESPONSE_CODE);
		
		if ( !responseCode.equals("200") ) {
			logger.warning("Response status is '"+responseCode+"'");
			return false;
			
		}
		
		//logger.info("User is validated successfully.");
		return true;
	}
	
	/*
	// Just for testing
	public static void main(String[] args) {
		
		// Only for testing  
		String url = "https://resources.trace.wisc.edu/utilities";
		//String url = "https://res.dotui.com/utilities";
		//String url = "https://192.169.2.50:8080/resserver/utilities";
		//String userName = "LiveCD2009";
		//String password = "uchworks";
		//String userName = "admin";
		//String password = "tgif#res@0428";
		String userName = "plenar";
		String password = "tgif#plenar@0428";
		//String userName = "admin";
		//String password = "admin";
		
		UserValidation userValidation = new UserValidation(url);
		logger.info( userValidation.validateUser(userName, password) );
	}
	*/
	
}
