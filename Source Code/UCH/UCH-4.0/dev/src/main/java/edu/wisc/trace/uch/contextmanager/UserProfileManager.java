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
package edu.wisc.trace.uch.contextmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Manage user profiles and provides methods to get it.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
class UserProfileManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private UCH uch;
	
	private Map<String, UserProfile> userProfileMap = new TreeMap<String, UserProfile>();
	
	private UserProfile loggedUserProfile;
	
	private String userName;
	
	/**
	 * Provide the reference of UCH to local variable.
	 * 
	 * @param uch an object of UCH
	 * @param loggedUserContextData an XML formed String specifies user context data
	 * @param userName a String value of userName
	 * @param password a String value of password
	 */
	UserProfileManager(UCH uch, String loggedUserContextData, String userName, String password) {
		
		this.uch = uch;
		this.userName = userName;
		
		loggedUserProfile = new UserProfileParser().parseData(loggedUserContextData, userName, password);
		
		if(userName != null){  // Parikshit Thakur : 20110901. Load userProfile only if a user is mentioned in web.xml .
			userProfileMap.put(userName, loggedUserProfile);
			loadUserProfiles(userName);
		}	 
	}
	
	/**
	 * Get user profile for specified userName and password.
	 * 
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return an object of IProfile specifies user profile
	 */
	IProfile getUserProfile(String userName, String password) {
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return null;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return null;
		}
		
		//
		if ( loggedUserProfile != null ) {
			
			if ( userName.equals(loggedUserProfile.getUserName())
					&& password.equals(loggedUserProfile.getPassword()) ) {
				
				return loggedUserProfile.getProfile();
			}
		}
		
		UserProfile userProfile = null;
		
		synchronized (userProfileMap) {
			userProfile = userProfileMap.get(userName);
		}
		
		if ( userProfile == null ) {
			logger.warning("Unable to get User Profile for User '"+userName+"'.");
			return null;
		}
		
		if ( !password.equals( userProfile.getPassword() ) ) {
			logger.warning("Password doesn't match for User '"+userName+"'.");
			return null;
		}
		
		return userProfile.getProfile();
	}
	
	
	/**
	 * Load userProfiles locally or from resource server.
	 */
	private void loadUserProfiles(String userName) {
		
		if ( uch == null ) {
			logger.severe("UCH is null");
			return;
		}
		
		List<List<Map<String, List<String>>>> profileResources = uch.getResources( getUserPrifileProps() );
		
		if ( (profileResources == null) || (profileResources.size() == 0) ) {
			logger.warning("Profile Resources is null.");
			return;
		}
		
		parseProfileResource( profileResources.get(0) );
	}
	
	/**
	 * Get User Profile XML files from specified profile resources and parse them.
	 * 
	 * @param profileResources an object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; specifies user profile resources properties
	 */
	private void parseProfileResource(List<Map<String, List<String>>> profileResources) {
		
		if ( (profileResources == null) || (profileResources.size() == 0) ) {
			logger.warning("Profile Resources is null.");
			return;
		}
		
		for ( Map<String, List<String>> profileProps : profileResources ) {
			
			if ( profileProps == null )
				continue;
			
			String localAt = CommonUtilities.getFirstItem( profileProps.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
			
			if ( localAt == null ) {
				logger.warning("Unable to get '"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"' from User Profile Properties.");
				continue;
			}
		//	The user profile is getting find and parsed here.
			UserProfile userProfile = new UserProfileParser().parseFile( localAt.replaceAll(" ", "%20") );
			
			if ( userProfile == null ) {
				logger.warning("Unable to get User Profile from the xml file '"+localAt+"'.");
				continue;
			}
			userProfileMap.put(userName, userProfile);
			logger.info("The core keys of User Profile are : "+userProfileMap);
		}
	}
	
	/**
	 * Prepare request properties for getting user profiles and return it.
	 * 
	 * @return an object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; 
	 */
	private List<Map<String, List<String>>> getUserPrifileProps() {
		
		List<Map<String, List<String>>> propsList = new ArrayList<Map<String,List<String>>>();
		
		Map<String, List<String>> propsMap = new HashMap<String, List<String>>();
		
		propsMap.put( Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_RES_TYPE_VALUE_USER_PROFILE) );
		
		propsMap.put( Constants.PROPERTY_RES_RESOURCE_SERVER_USER_NAME, CommonUtilities.convertToList(userName) ); // Parikshit Thakur : 20110901. Added to query for userProfile with userName.
		
		propsList.add(propsMap);
		
		return propsList;
	}
}
