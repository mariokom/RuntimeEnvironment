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
package edu.wisc.trace.uch.resource.uploadmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.resource.uploadmanager.local.LocalUploadManager;
import edu.wisc.trace.uch.resource.uploadmanager.resserver.ResServerUploadManager;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to upload resources locally and on resource server.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UploadManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String UPLOAD_STATUS = "status";
	private static final String UPLOAD_STATUS_VALUE_200 = "200";
	private String resServerAppPath;
	private String resServerUserName;
	private String resServerPassword;
	
	private LocalUploadManager localUploadManager;
	
	private ResServerUploadManager resServerUploadManager;
	
	public UploadManager(ResourceManager resourceManager, String localResourceDirURI, String resServerAppPath, String userName, String password) {
	
		this.resServerAppPath = resServerAppPath;
		this.resServerUserName = userName;
		this.resServerPassword = password;
		
		resServerUploadManager = new ResServerUploadManager(resServerAppPath, userName, password);
		
		localUploadManager = new LocalUploadManager(resourceManager, localResourceDirURI);
	}
	
	/**
	 * Upload the specified Resources to the Resource Server and prepare a List of Map containing Status Code and Resource Name and return it.
	 * 
	 * @param props an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * @param owners an Object of List&lt;String&gt;
	 * @param groups an Object of List&lt;String&gt;
	 * @param rights an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, List<String> resourceUri) {
		
		if ( props == null ) {
			logger.warning("Properties is null.");
			return null;
		}
		
		if ( resourceUri == null ) {
			logger.warning("Resource URI is null.");
			return null;
		}
		
		if ( props.size() != resourceUri.size() ) {
			logger.warning("No of properties map and No of resource URIs are not equal.");
			return null;
		}
		
		List<Map<String, String>> returnList = new ArrayList<Map<String,String>>();
		
		int size = props.size();
		
		for ( int i=0 ; i<size ; i++ ) 
			returnList.add( uploadResource(props.get(i), owners, groups, rights, resourceUri.get(i)) );
		
		return returnList;
	}
	
	/**
	 * Upload resource locally or on resource server. 
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param owners an Object of List&lt;String&gt;
	 * @param groups an Object of List&lt;String&gt;
	 * @param rights an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an Object of Map&lt;String, String&gt; represents upload response
	 */
	private Map<String, String> uploadResource(Map<String, List<String>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, String resourceUri) {
		
		if ( props == null ) {
			logger.warning("Properties is null.");
			return null;
		}
		
		if ( resourceUri == null ) {
			logger.warning("Resource URI is null.");
			return null;
		}
		
		List<String> locations = props.get(Constants.RESOURCE_UPLOAD_PROP_NAME_LOCATION);
		
		//Parikshit Thakur : 20110907. Changes to upload resource to res server. Previously, was uploaded to local res server. 
		if( locations == null){
			
			if( this.resServerAppPath != null && this.resServerUserName != null && this.resServerPassword != null ){
				
				locations = new ArrayList<String>();
				locations.add(Constants.RESOURCE_UPLOAD_PROP_NAME_LOCATION_VALUE_RESOURCE_SERVER);
			}
		}
		// Changes end.
		
		if ( locations == null ) { // Default : upload resource locally.
			
			return uploadLocally(props, resourceUri);
			
		} else { // Check the value of property 'location' and take decesion.
			
			Map<String, String> localReturnMap = null;
			Map<String, String> resServerReturnMap = null;
			
			// Upload resource locally.
			if ( locations.contains(Constants.RESOURCE_UPLOAD_PROP_NAME_LOCATION_VALUE_LOCAL) ) {
				
				localReturnMap = uploadLocally(props, resourceUri);
			}
			
			// Upload resource to resource server.
			if ( locations.contains(Constants.RESOURCE_UPLOAD_PROP_NAME_LOCATION_VALUE_RESOURCE_SERVER) ) {
				
				 resServerReturnMap = uploadToResServer(props, owners, groups, rights, resourceUri);
			}
			
			// Send the response which ever is better.
			
			
			if ( localReturnMap == null ) { // if local uploading failed then return resource server response.
				
				return resServerReturnMap;
				
			} else { // find better from both.
				
				if ( UPLOAD_STATUS_VALUE_200.equals(localReturnMap.get(UPLOAD_STATUS)) ) { // if local uploading successfully then return it. 
					
					return localReturnMap;
					
				} else { // check for resource server response.
					
					if ( resServerReturnMap == null ) { // if resource server response is null then return local response(null).
						
						return localReturnMap;
						
					} else if ( UPLOAD_STATUS_VALUE_200.equals(resServerReturnMap.get(UPLOAD_STATUS)) ) { // if resource server uploading successfully then return it.
						
						return resServerReturnMap;
						
					} else { // if resource server uploading failed then return local response(null).
						
						return localReturnMap;
					}
				}
			}
			
		}
		
	}
	
	
	/**
	 * Upload the specified Resource locally and prepare a Map containing Status Code and Resource Name and return it.
	 * 
	 * @param props  an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, String> uploadLocally(Map<String, List<String>> props, String resourceUri) {
		
		if ( localUploadManager == null ) {
			logger.warning("Local Upload Manager is null.");
			return null;
		}
		
		return localUploadManager.uploadResource(props, resourceUri);
	}
	
	
	/**
	 * Upload the specified Resource to the Resource Server and prepare a Map containing Status Code and Resource Name and return it.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param owners an Object of List&lt;String&gt;
	 * @param groups an Object of List&lt;String&gt;
	 * @param rights an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, String> uploadToResServer(Map<String, List<String>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, String resourceUri) {
		
		if ( resServerUploadManager == null ) {
			logger.warning("Resource Server Upload Manager is null.");
			return null;
		}
		
		return resServerUploadManager.uploadResource(props, owners, groups, rights, resourceUri);
	}
}
