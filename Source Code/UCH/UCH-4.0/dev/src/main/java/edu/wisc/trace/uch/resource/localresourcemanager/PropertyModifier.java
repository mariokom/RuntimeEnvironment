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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to modify resource properties.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
class PropertyModifier {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String RESOURCE_URI_PREFIX_HTTP = "http://";
	private static final String RESOURCE_URI_PREFIX_UPPER_HTTP = "HTTP://";
	private static final String RESOURCE_URI_PREFIX_HTTPS = "https://";
	private static final String RESOURCE_URI_PREFIX_UPPER_HTTPS = "HTTPS://";
	
	private LocalResourceManager localResourceManager;
	private DownloadResourceUtil downloadResourceUtil;
	
	/**
	 * Constructor.
	 * Provide the reference of LocalResourceManager and resDirUri to local variables.
	 * 
	 * @param localResourceManager an object of LocalResourceManager
	 * @param localCacheDirURI a String value of local cache directory path URI
	 */
	PropertyModifier(LocalResourceManager localResourceManager, String localCacheDirURI) {
		
		this.localResourceManager = localResourceManager;
		downloadResourceUtil = new DownloadResourceUtil(localCacheDirURI);
		
	}
	
	/**
	 * Add new property 'resourceLocalAt' using the value of property 'http://openurc.org/ns/res#resourceUri' and return property map.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt; specified modified properties
	 */
	Map<String, List<String>> addProperties(Map<String, List<String>> props, String resDirUri) {
		
		if ( resDirUri == null ) {
			logger.warning("Resource Directory URI is null.");
			return null;
		}
		String resourceURI = getResourceURI(props);
		
		if ( resourceURI == null ) 
			return getResourceServerResource(props);
		
		
		if ( resourceURI.startsWith(RESOURCE_URI_PREFIX_HTTP) 
			|| resourceURI.startsWith(RESOURCE_URI_PREFIX_UPPER_HTTP)
			|| resourceURI.startsWith(RESOURCE_URI_PREFIX_HTTPS)
			|| resourceURI.startsWith(RESOURCE_URI_PREFIX_UPPER_HTTPS) ) {
			
			return downloadResource(resourceURI, props);
			
		} else {
			
			return addResourceLocalAt(resourceURI, resDirUri, props);
		}
		
	}
	
	/**
	 * Add new property 'resourceLocalAt' and return property map.
	 * It basically provide full file path URI instead of relative URI.
	 * 
	 * @param resourceURI a String value of Resource URI
	 * @param resDirUri a String value of Resource Directory URI
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt; specified modified properties
	 */
	/*private Map<String, List<String>> addResourceLocalAt(String resourceURI, String resDirUri, Map<String, List<String>> props) {
		
		if ( (resourceURI == null) || (resDirUri == null) || (props == null) )
			return null;
		
		String resourceLocalAt = resDirUri + "/" + getProperResourceURI(resourceURI);
		
		resourceLocalAt = addIndexFilePath(resourceLocalAt, props);
		
		props.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, CommonUtilities.convertToList(resourceLocalAt) );
		
		return props;
	}*/
	
	// 2013-02-22 : Yuvaraj - Whenever indexFile property exists then do not append resourceURI in resourceLocalAt path.
	//				Problem occurred when UIPM clients are shifted from 'cache' repository to local resource manager.
	//				It took resourceURI into consideration to prepare resourceLocalAt uri.
	
	private Map<String, List<String>> addResourceLocalAt(String resourceURI, String resDirUri, Map<String, List<String>> props) {
	
		if ( (resourceURI == null) || (resDirUri == null) || (props == null) )
			return null;
		
		String resourceLocalAt = resDirUri;
		
		if(props.get(Constants.PROPERTY_INDEX_FILE) != null){ // In case of presence of indexFile property in the resource properties.
		
			resourceLocalAt = addIndexFilePath(resourceLocalAt, props);
		}else{
			
			resourceLocalAt = resDirUri + "/" + getProperResourceURI(resourceURI);
		}
		
		props.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, CommonUtilities.convertToList(resourceLocalAt) );
		
		return props;
	}
	/**
	 * Download resource from resource server of from any other server.
	 * If resource is downloaded from resource server then also get properties from resource server.
	 * Add new property 'resourceLocalAt' as local file URI. 
	 * 
	 * @param resourceURI a String value of Resource URI
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt; specified modified properties
	 */
	private Map<String, List<String>> downloadResource(String resourceURI, Map<String, List<String>> props) {
		
		if ( resourceURI == null )
			return null;
		
		if ( localResourceManager == null )
			return null;
		
		if ( localResourceManager.isUriContainsResServerAppPath(resourceURI) ) { // Download resource from resource server
			
			return getResourceServerResource(resourceURI);
			
		} else { // Download resource locally 
			
			String resourceLocalAt = getRemoteResource(resourceURI);
			
			if ( resourceLocalAt == null ) {
				logger.warning("Unable to download resource.");
				return null;
			}
			
			resourceLocalAt = addIndexFilePath(resourceLocalAt, props);
			
			props.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, CommonUtilities.convertToList(resourceLocalAt) );
			
			return props;
		}
			
	}
	
	/**
	 * Download specified resource and return its local location.
	 * 
	 * @param resourceURI a String value of Resource URI
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private String getRemoteResource(String resourceURI) {
		
		return downloadResourceUtil.downloadResource(resourceURI);
	}
	
	/**
	 * Add the value of the property 'http://openurc.org/ns/res#indexFile' to resourceLocalAt if it is exist.
	 * 
	 * @param resourceLocalAt a String value of reosurceLocalAt
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a String value of modified ReosurceLocalAt
	 */
	private String addIndexFilePath(String resourceLocalAt, Map<String, List<String>> props) {
		
		if ( (props == null) || (resourceLocalAt == null) ) 
			return null;
		
		
		String indexFile = CommonUtilities.getFirstItem( props.get(Constants.PROPERTY_INDEX_FILE) );
		
		if ( indexFile == null )
			return resourceLocalAt;
		
		return resourceLocalAt + "/" + indexFile;
	}
	
	/**
	 * Get specified resource from resource server.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource request properties.
	 * 
	 * @return an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource response properties.
	 */
	private Map<String, List<String>> getResourceServerResource(Map<String, List<String>> props) {
		
		return localResourceManager.getResServerResource(props);
	}
	
	/**
	 * Get specified resource from resource server.
	 * 
	 * @param resourceURI a String value of Resource URI
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> getResourceServerResource(String resourceURI) {
		
		if ( resourceURI == null ) 
			return null;
		
		int index = resourceURI.indexOf('?');
		
		if ( index == -1 ) {
			logger.warning("Resource Server Resource URI '"+resourceURI+"' is invalid.");
			return null;
		}
		Map<String, List<String>> propMap = CommonUtilities.prepareKeyValueListMap( resourceURI.substring(index+1) );
		
		if ( propMap == null ) {
			logger.warning("Resource Server Resource URI '"+resourceURI+"' is invalid.");
			return null;
		}
		
		return localResourceManager.getResServerResource(propMap);
	}
	
	/**
	 * Get the value of the property 'http://openurc.org/ns/res#resourceUri'.
	 * There must always one value exists for this property.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a String specifies value of resource URI
	 */
	private String getResourceURI(Map<String, List<String>> props) {
		
		if ( props == null ) {
			logger.warning("Propert Map is null.");
			return null;
		}
		
		List<String> resourceUris = props.get(Constants.PROPERTY_RESOURCE_URI);
		
		if ( resourceUris == null ) {
			logger.warning("Unable to get property '"+Constants.PROPERTY_RESOURCE_URI+"'.");
			return null;
		}
		
		if ( resourceUris.size() != 1 ) {
			logger.warning("Property '"+Constants.PROPERTY_RESOURCE_URI+"' must have only one value.");
			return null;
		}
		
		return CommonUtilities.getFirstItem(resourceUris);
	}
	
	
	/**
	 * Remove the starting slash or backslash from the resource directory URI if exists.
	 * 
	 * @param resourceURI a String value of resource URI
	 * 
	 * @return a String value
	 */
	private String getProperResourceURI(String resourceURI) {
		
		if ( resourceURI == null )
			return null;
		
		if ( resourceURI.startsWith("/") )
			return resourceURI.substring(1);
		else if ( resourceURI.startsWith("\\") )
			return resourceURI.substring(1);
		else
			return resourceURI;
	}
}
