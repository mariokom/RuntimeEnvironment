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
package edu.wisc.trace.uch.resource.uploadmanager.local;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 *  Provide methods to upload resources locally
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class LocalUploadManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String CONSTANT_NAME = "name";
	// Parikshit Thakur:20110825. Changed extension from .ucf to .rprop
	private static final String CONSTANT_EXTENSION_RPROP = ".rprop";
	
	private static final String RESPONE_PROP_NAME_STATUS = "status";
	private static final String RESPONE_PROP_NAME_STATUS_VALUE_200 = "200";
	private static final String RESPONE_PROP_NAME_STATUS_VALUE_401 = "401";
	private static final String RESPONE_PROP_NAME_STATUS_VALUE_500 = "500";
	
	
	private static final String RESPONE_PROP_NAME_RESOURCE_NAME = "resourceName";
	
	private static final String DIR_NAME_UPLOADED_RESOURCES = "uploadedResources";
	
	private ResourceManager resourceManager;
	
	private RPropFileUtil rpropFileUtil;
	
	private String uploadedResourcesDirURI;
	
	/**
	 * Constructor.
	 * Provide the reference of ResourceManager to local variable.
	 * Create a directory for store uploaded resource under local resource directory.
	 * 
	 * @param resourceManager an object of ResourceManager
	 * @param localResourceDirURI a String value of local resource directory path URI
	 */
	public LocalUploadManager(ResourceManager resourceManager, String localResourceDirURI) {
		
		this.resourceManager = resourceManager;
		
		rpropFileUtil = new RPropFileUtil();
		
		if ( localResourceDirURI == null ) {
			logger.severe("Local Resource Directory Path is null.");
			return;
		}
		
		createUploadedResourceDir(localResourceDirURI);
	}
	
	
	/**
	 * Create the specified directory if it is not exists.
	 * 
	 * @param localResourceDirURI a String value of local Resource Directory URI
	 */
	private void createUploadedResourceDir(String localResourceDirURI) {
		
		try {
			
			File resDir = new File( new URI(localResourceDirURI) );
			
			if ( !resDir.exists() ) {
				
				if ( !resDir.mkdirs() ) {
					logger.severe("Unable to create directory '"+resDir.getAbsolutePath()+"'.");
					return;
				}
			}
			
			if ( resDir.isFile() ) {
				
				if ( !resDir.delete() ) {
					logger.severe("Unable to delete file '"+resDir.getAbsolutePath()+"'.");
					return;
				}
				
				if ( !resDir.mkdir() ) {
					logger.severe("Unable to create directory '"+resDir.getAbsolutePath()+"'.");
					return;
				}
			}
			
			File uploadedResourcesDir = new File(resDir, DIR_NAME_UPLOADED_RESOURCES);
			
			if ( uploadedResourcesDir.exists() ) {
				
				if ( uploadedResourcesDir.isFile() ) {
					
					if ( !uploadedResourcesDir.delete() ) {
						logger.severe("Unable to delete file '"+uploadedResourcesDir.getAbsolutePath()+"'.");
						return;
					}
					
					if ( !uploadedResourcesDir.mkdir() ) {
						logger.severe("Unable to create directory '"+uploadedResourcesDir.getAbsolutePath()+"'.");
						return;
					}
				}
				
			} else {
				
				if ( !uploadedResourcesDir.mkdir() ) {
					logger.severe("Unable to create directory '"+uploadedResourcesDir.getAbsolutePath()+"'.");
					return;
				}
				
			}
				
			uploadedResourcesDirURI = localResourceDirURI + "/" + DIR_NAME_UPLOADED_RESOURCES;
			logger.info("Uploaded Resources Directory Path is set to '"+uploadedResourcesDirURI+"' successfully.");
			
		} catch (URISyntaxException e) {
			logger.severe("URISyntaxException : Invalid file URI '"+localResourceDirURI+"'.");
		}
		
	}
	
	
	/**
	 * Upload the Specified Resource to Local Resource Manager.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an object of Map&lt;String, String&gt; specified upload response
	 */
	public Map<String, String> uploadResource(Map<String, List<String>> props, String resourceUri) {
		
		if ( props == null ) {
			logger.warning("Props is null.");
			return null;
		}
		
		if ( resourceUri == null ) {
			logger.warning("Resource URI is null.");
			return null;
		}
		
		if ( uploadedResourcesDirURI == null ) {
			logger.warning("Local Directory for uploading resources is not found.");
			return null;
		}
		
		Map<String, List<String>> existingProps = findResources(props);
		
		if ( existingProps == null ) {
			
			return uploadNewResource(props, resourceUri);
			
		} else {
			
			return updateExistingResource(props, existingProps, resourceUri);
		}
		
	}
	
	
	/**
	 * Upload a new Resource.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an object of Map&lt;String, String&gt; specified upload response
	 */
	private Map<String, String> uploadNewResource(Map<String, List<String>> props, String resourceUri) {
		
		String destDirURI = FileUtil.createDirForResource(uploadedResourcesDirURI);
		
		return saveLocalResource( props, resourceUri, destDirURI );
	}
	
	
	/**
	 * Update the existing resource.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * @param existingProps an object of Map&lt;String, List&lt;String&gt;&gt; specifies existing resource properties
	 * @param resourceUri a String value of Resource URI
	 * 
	 * @return an object of Map&lt;String, String&gt; specified upload response
	 */
	private Map<String, String> updateExistingResource(Map<String, List<String>> props, Map<String, List<String>> existingProps, String resourceUri) {
		
		String resourceLocalAt = CommonUtilities.getFirstItem( existingProps.remove(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
		if ( resourceLocalAt == null ) {
			logger.warning("Unable to get '"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"' from existing properties.");
			return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_500);
		}
		
		Map<String, List<String>> newProps = mergeProps(props, existingProps);
		
		return saveLocalResource( newProps, resourceUri, getParentDirURI(resourceLocalAt) );
	}
	
	
	/**
	 * Copy the resource specified by resourceUri to the directory specified by destDirURI. Also create a .rprop file from props and save it to destDirURI.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * @param resourceUri a String value of resource file URI
	 * @param destDirURI a String value of destination directory URI
	 * 
	 * @return an object of Map&lt;String, String&gt; specified upload response
	 */
	private Map<String, String> saveLocalResource(Map<String, List<String>> props, String resourceUri, String destDirURI) {
		
		if ( (props == null) || (resourceUri == null) || (props == null) ) {
			logger.warning("One of the input parmaeters is null.");
			return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_401);
		}
		
		String resourceName = getResourceName(resourceUri);
		
		if ( resourceName == null ) {
			logger.warning("Unable to get ResourceName form resource URI '"+resourceUri+"'.");
			return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_401);
		}
		
		String rpropFileName = getRPropFileName(resourceName);
		
		if ( destDirURI == null ) {
			logger.warning("Unable to create local directory("+destDirURI+") for uploading new resource.");
			return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_500);
		}
		
		String rpropFileURI = destDirURI + "/" + rpropFileName;
		String destResourceURI = destDirURI + "/" + resourceName;
		
		if ( rpropFileUtil.saveRPropFile(rpropFileURI, props, resourceName) ) {
		
			try {
				
				if ( !FileUtil.copyFile( new File( new URI(resourceUri) ), new File( new URI(destResourceURI) ) ) ) {
					
					logger.warning("Unable to copy file from '"+resourceUri+"' to '"+destResourceURI+"'.");
					return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_401);
				}
				
			} catch (URISyntaxException e) {
				
				logger.warning("URISyntaxException : Invalid File URI '"+resourceUri+"' or '"+destResourceURI+"'.");
				return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_500);
			}
			
			if ( !resourceManager.addLocalResource(rpropFileURI) ) {
				
				logger.warning("Unable to add Local Resource '"+rpropFileURI+"'.");
				return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_500);
			}
			
			String resName = findNameProp(props);
			
			if ( resName != null)
				resName = Constants.LOCAL_RESOURC_NAME_PREFIX + resourceName;
			
			return preareSuccessedResponseMap(resName);
			
		} else {
			
			return prepareFailedResponseMap(RESPONE_PROP_NAME_STATUS_VALUE_500);
		}
	}
	
	
	/**
	 * Find specified resource from local resources.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * 
	 * @return an object of Map&lt;String, List&lt;String&gt;&gt; specifies found resource properties
	 */
	private Map<String, List<String>> findResources(Map<String, List<String>> props) {
		
		if ( props == null )
			return null;
		
		String name = findNameProp(props);
		
		if ( name == null ) {
			return null;
		}
		
		Map<String, List<String>> reqProps = new HashMap<String, List<String>>();
		reqProps.put(Constants.PROPERTY_RES_NAME, CommonUtilities.convertToList(name) );
		
		List<Map<String, List<String>>> resPropsList = getLocalResource(reqProps);
		
		if ( (resPropsList == null) || (resPropsList.size() == 0) ) {
			logger.info("Resource is not already exists.");
			return null;
		}
		
		return resPropsList.get(0);
	}
	
	
	/**
	 * Find the name properties and return it. 
	 * If not exists then return null. 
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * 
	 * @return a String value of Name properties
	 */
	private String findNameProp(Map<String, List<String>> props) {
		
		if ( props == null )
			return null;
		
		String name = CommonUtilities.getFirstItem( props.get(Constants.PROPERTY_RES_NAME) );
		
		if ( name == null )
			return CommonUtilities.getFirstItem( props.get(CONSTANT_NAME) );
		else
			return name;
	}
	
	
	/**
	 * Get specified local resource.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies resource properties
	 * 
	 * @return an object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; specifies requested resources properties
	 */
	private List<Map<String, List<String>>> getLocalResource(Map<String, List<String>> props) {
		
		if ( resourceManager == null ) 
			return null;
		
		props.put(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION, CommonUtilities.convertToList(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_LOCAL));
		
		List<Map<String, List<String>>> resPropsList = new ArrayList<Map<String,List<String>>>();
		
		resPropsList.add(props);
		
		List<List<Map<String, List<String>>>> retResPropsList = resourceManager.getResources(null, resPropsList);
		
		if ( (retResPropsList == null) || (retResPropsList.size() == 0) )
			return null;
		
		return retResPropsList.get(0);
	}
	
	/**
	 * Remove some properties from existing resource properties and merge both maps and return it.
	 * 
	 * @param newProps an object of Map&lt;String, List&lt;String&gt;&gt; specifies new properties of resource
	 * @param existingProps an object of Map&lt;String, List&lt;String&gt;&gt; specifies existing properties of resource
	 * 
	 * @return an object of Map&lt;String, List&lt;String&gt;&gt; specifies both properties
	 */
	private Map<String, List<String>> mergeProps(Map<String, List<String>> newProps, Map<String, List<String>> existingProps) {
		
		if ( newProps == null )
			return null;
		
		if ( existingProps == null )
			return null;
		
		existingProps.remove(Constants.PROPERTY_RESOURCE_URI);
		existingProps.remove(Constants.PROP_NAME_RESOURCE_LOCAL_AT);
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		for ( Entry<String, List<String>> entry : newProps.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			String key = entry.getKey();
			
			if ( key == null )
				continue;
			
			List<String> values = entry.getValue();
			
			if ( (values == null) || (values.size() == 0) )
				continue;
			
			returnMap.put(key, values);
		}
		
		for ( Entry<String, List<String>> entry : existingProps.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			String key = entry.getKey();
			
			if ( key == null )
				continue;
			
			List<String> values = entry.getValue();
			
			if ( (values == null) || (values.size() == 0) )
				continue;
			
			if ( returnMap.containsKey(key) ) {
				
				List<String> existingValues = returnMap.get(key);
				
				for ( String value : values ) {
					
					if ( !existingValues.contains(value) )
						existingValues.add(value);
				}
				
			} else {
				
				returnMap.put(key, values);
			}
		}
		
		return returnMap;
	}
	
	/**
	 * Prepare a response map that specifies failed status for uploading resource.
	 * 
	 * @return an object of Map&lt;String, String&gt;
	 */
	private Map<String, String> prepareFailedResponseMap(String status) {
		
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(RESPONE_PROP_NAME_STATUS, status);	
		return returnMap;
	}
	
	/**
	 * Prepare a response map that specifies success status of uploaded resource.
	 * 
	 * @param resourceName a String value specifies Resource Name
	 * 
	 * @return an object of Map&lt;String, String&gt;
	 */
	private Map<String, String> preareSuccessedResponseMap(String resourceName) {
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		returnMap.put(RESPONE_PROP_NAME_STATUS, RESPONE_PROP_NAME_STATUS_VALUE_200);	
		
		if ( resourceName != null)
			returnMap.put(RESPONE_PROP_NAME_RESOURCE_NAME, resourceName);	
		
		return returnMap;
	}
	
	/**
	 * Prepare a filename same as specified resourceName with .rprop extension.
	 * 
	 * @param resourceName a String value of resourceName
	 * 
	 * @return a String value of rprop file name
	 */
	private String getRPropFileName(String resourceName) {
		
		if ( resourceName == null )
			return null;
		
		int index = resourceName.lastIndexOf('.');
		
		if ( index == -1 )
			return resourceName + CONSTANT_EXTENSION_RPROP;
		else
			return resourceName.substring(0, index) + CONSTANT_EXTENSION_RPROP;
	}
	
	/**
	 * Get the path URI of parent directory of specified path.
	 * 
	 * @param pathURI a String value of path URI
	 * 
	 * @return a String value of parent path URI
	 */
	private String getParentDirURI(String pathURI) {
		
		if ( pathURI == null ) {
			logger.warning("Resource URI is null.");
			return null;
		}
		
		int slashIndex = pathURI.lastIndexOf('/');
		int backSlashIndex = pathURI.lastIndexOf('\\');
		
		if ( slashIndex == -1 ) {
			
			if ( backSlashIndex == -1 ) 
				return null;
			else 
				return pathURI.substring(backSlashIndex);
			
		} else {
			
			if ( backSlashIndex == -1 )
				return pathURI.substring(0, slashIndex);
			else if ( slashIndex > backSlashIndex )
				return pathURI.substring(0, slashIndex);
			else
				return pathURI.substring(0, backSlashIndex);
		}
	}
	
	/**
	 * Get resource Name from specifies resource URI.
	 * 
	 * @param resourceUri a String value of resource URI
	 * 
	 * @return a String value of resource Name.
	 */
	private String getResourceName(String resourceUri) {
		
		if ( resourceUri == null ) {
			logger.warning("Resource URI is null.");
			return null;
		}
		
		int slashIndex = resourceUri.lastIndexOf('/');
		int backSlashIndex = resourceUri.lastIndexOf('\\');
		
		if ( slashIndex == -1 ) {
			
			if ( backSlashIndex == -1 ) 
				return null;
			else 
				return resourceUri.substring(backSlashIndex+1);
			
		} else {
			
			if ( backSlashIndex == -1 )
				return resourceUri.substring(slashIndex+1);
			else if ( slashIndex > backSlashIndex )
				return resourceUri.substring(slashIndex+1);
			else
				return resourceUri.substring(backSlashIndex+1);
		}
	}
	
}
