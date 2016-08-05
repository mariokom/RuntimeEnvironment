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

package edu.wisc.trace.uch.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.resource.localresourcemanager.LocalResourceManager;
import edu.wisc.trace.uch.resource.resourcesheetmanager.ResourceSheetManager;
import edu.wisc.trace.uch.resource.retrievalmanager.RetrievalManager;
import edu.wisc.trace.uch.resource.uploadmanager.UploadManager;
import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Manage Resource Sheet, Local and Resource Server Resources. 
 * Also provide methods to upload/download resources as well as get their properties.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public class ResourceManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private UCH uch;
	
	private LocalResourceManager localResourceManager;
	
	private RetrievalManager retrievalManager;
	
	private UploadManager uploadManager;
	
	private ResourceSheetManager resourceSheetManager;
	
	private String resServerAppPath;
	
	private String resServerUserName;
	
	private Map<String, String> targetResSvcMap = new HashMap<String, String>();
	
	
	/**
	 * Constructor.
	 * Assign reference of UCH to local variable.
	 * 
	 * @param uch an Object of UCH
	 * @param localResDirUri a String value of local resource directory URI path.
	 * @param resServerAppPath a String value of Resource Server Path
	 * @param cacheDirUri a String value of Cache dir path
	 */
	public ResourceManager(UCH uch, String localResDirUri, String resServerAppPath, String cacheDirUri) {
		
		this.uch = uch;
		this.resServerAppPath = resServerAppPath;
		
		retrievalManager = new RetrievalManager(resServerAppPath, cacheDirUri, null, null);
		
		resourceSheetManager =  new ResourceSheetManager(this);
		
		localResourceManager = new LocalResourceManager(this, localResDirUri);
		
		uploadManager = new UploadManager(this, localResDirUri, resServerAppPath, null, null);
	}
	
	/**
	 * Constructor.
	 * Assign reference of UCH to local variable.
	 * Instantiate ResourceRetrival class using other parameters.
	 * 
	 * @param uch an Object of UCH
	 * @param localResDirUri a String value of local resource directory URI path.
	 * @param resServerAppPath a String value of Resource Server Path
	 * @param cacheDirUri a String value of Cache dir path
	 * @param userName a String value of userName
	 * @param password a String value of password
	 */
	public ResourceManager(UCH uch, String localResDirUri, String resServerAppPath, String cacheDirUri, String userName, String password) {
		
		this.uch = uch;
		this.resServerAppPath = resServerAppPath;
		this.resServerUserName = userName; // Parikshit Thakur : 20110901. saved resServerUsername to use it as a property to query for user profile.
		
		retrievalManager = new RetrievalManager(resServerAppPath, cacheDirUri, userName, password);
		
		resourceSheetManager =  new ResourceSheetManager(this);
		
		localResourceManager = new LocalResourceManager(this, localResDirUri);
		
		uploadManager = new UploadManager(this, localResDirUri, resServerAppPath, userName, password);
	}
	
	/**
	 * Get the String value of Resource Server app path.
	 * 
	 * @return a String value
	 */
	public String getResServerAppPath() {
		
		return resServerAppPath;
	}
	
	
	/**
	 * Get the res server for a target as mentioned in resSvc element of TD
	 * @param targetName a string value for the targetName
	 * @return a string value of res server.
	 */
	public String getResServerForTarget(String targetName){
		return targetResSvcMap.get(targetName);
	}
	
	/**
	 * Set targetName resServer value in map targetResSvcMap
	 * @param targetName
	 * @param resSer
	 */
	public void setTargetResSvcMap(String targetName, String resSer) {
		
		targetResSvcMap.put(targetName, resSer);
		
	}
	
	
	/**
	 * Call the same method of UCH.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a String value of converted URI
	 */
	public String convertURI(String uri) {
		
		if ( uch != null )
			return uch.convertURI(uri);
		else
			return null;
	}
	
	/**
	 * Call the same method of UCH.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return return value from UCH
	 */
	public boolean isUriContainsResServerAppPath(String uri) {
		
		if ( uch != null )
			return uch.isUriContainsResServerAppPath(uri);
		else
			return false;
	}
	
	
	/**
	 * Parse &lt;ResDir&gt; Element of Target Description and add it in the list of TargetDescriptions.
	 * 
	 * @param tdUri a String value of Target Description URI(tdUri)
	 */
	public void addResourceDir(String tdUri) {
		
		resourceSheetManager.addResourceDir(tdUri);
	}
	
	/**
	 * Called on UCH for getting Dynamic Resource.
	 * 
	 * @param sessionId a String value of Session Id
	 * @param resProps an Object of List&lt;Map&lt;String, String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, Object&gt;&gt;
	 */
	public List<Map<String, Object>> getDynRes( String sessionId, List<Map<String, String>> resProps ) {
		
		return uch.getDynRes( sessionId, resProps );
	}
	
	
	/**
	 * Store Information about the Dynamic Resource.
	 * So when request for resource is made then first check it for Dynamic Resource.
	 * If not found then find Resource in available Resource Directories.
	 * 
	 * @param sessionIds a List&lt;String&gt; value of sessionIds
	 * @param eltIds a List&lt;String&gt; value of elementIds
	 */
	public void updateDynRes(List<String> sessionIds, List<String> eltIds) {
		
		resourceSheetManager.updateDynRes(sessionIds, eltIds);
	}
	
	
	/**
	 * Upload Specified Resources with specified Properties, Owners and Groups.
	 * 
	 * @param props an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; specifies Resource Properties
	 * @param owners an Object of List&lt;String&gt; specifies owners of Resource
	 * @param groups an Object of List&lt;String&gt; specifies groups of Resource
	 * @param rights an Object of Map&lt;String, List&lt;String&gt;&gt; specifies rights of Resource
	 * @param resourceUri a String value of local resource path URI
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, List<String> resourceUri) {		
		
		if ( uploadManager == null ) {
			logger.warning("Upload Manager is null.");
			return null;
		}
		
		List<Map<String, String>> returnMaps = uploadManager.uploadResources(props, owners, groups, rights, resourceUri);
		
		System.gc();
		
		return returnMaps;
	}
	
	
	/**
	 * Add resource derived by specifies rprop file to the local resources.
	 * 
	 * @param rpropFileURI a String value of rprop file.
	 * 
	 * @return a boolean value specifies whether local resource added successfully or not
	 */
	public boolean addLocalResource(String rpropFileURI) {
		
		if ( rpropFileURI == null ) {
			logger.warning("rprop file name is null.");
			return false;
		}
		
		if ( localResourceManager == null ) {
			logger.warning("Local Resource Manager is null.");
			return false;
		}
		
		return localResourceManager.addResource(rpropFileURI);
	}

	
	/**
	 * Get User Profile for logged user.
	 * 
	 * @return a string value specifies user profile file data.
	 */
	public String getUserProfile() {
		
		Map<String, List<String>> resProps = new HashMap<String, List<String>>();
		
		resProps.put(Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_RES_TYPE_VALUE_USER_PROFILE) );
		resProps.put(Constants.PROPERTY_RES_RESOURCE_SERVER_USER_NAME, CommonUtilities.convertToList(resServerUserName) ); // Parikshit Thakur : 20110901. Added to query for userProfile with userName.
		resProps.put(edu.wisc.trace.uch.Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		resProps.put(edu.wisc.trace.uch.Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		
		// Yuvaraj:Search user profile from local resource store, no more asking Resource Server.
		List<Map<String, List<String>>> resPropsList = getLocalResources(resProps);
		
		if ( (resPropsList == null) || (resPropsList.size() == 0) )
			return null;
		
		Map<String, List<String>> rspResProps = resPropsList.get(0);
		
		if ( rspResProps == null )
			return null;
		
		String resourceLocalAt = CommonUtilities.getFirstItem( rspResProps.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
		if ( resourceLocalAt == null ) 
			return null;
		
		return ResourceUtil.getFileData(resourceLocalAt);
	}
	
	
	/**
	 * Get Resources from Local Resources, Resource Server, Target Adapter or Resource Sheet as par the requested resource property resourceType.
	 * If request properties contains property 'http://openurc.org/ns/res#type' and its value is 'http://openurc.org/restypes#atomic' then go for atomic or dynamic resource.
	 * Else find resource from local resources and if not found then go to resource server to get it.
	 * 
	 * @param sessionId a String value of SessionId
	 * @param resPropsList an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return a List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt; of Resources.
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resPropsList) {
		
		if ( resPropsList == null ) {
			logger.warning("Resource Properties List is null");
			return null;
		}
		
		//Parikshit Thakur : 20111115. Added code to download resource from the res server mentioned in resSvc element in TD.
		String targetName = null;
		
		if(sessionId != null){
			Map<String, String> sessionIdTargetIdMap = this.uch.getSessionIdTargetIdMap();
			String targetId = sessionIdTargetIdMap.get(sessionId);
			targetName = this.uch.getTargetName(targetId);
			
		}
		else{
			for(int i= 0; i < resPropsList.size(); i++){
				
				Map<String, List<String>> resPropMap = resPropsList.get(i);
				
				
				for(Map.Entry<String, List<String>> entry : resPropMap.entrySet()){
					
					if(entry.getKey().equals(Constants.PROPERTY_FOR_TARGET_INSTANCE)){						
						String targetInstance = CommonUtilities.getFirstItem(entry.getValue());
						targetName = this.uch.getTargetName(targetInstance);
						if(targetName != null)
							break;
						
					}else if(entry.getKey().equals(Constants.PROPERTY_FOR_TARGET_NAME)){
						targetName = CommonUtilities.getFirstItem(entry.getValue());
						if(targetName != null)
							break;
						
					}else if(entry.getKey().equals(Constants.PROPERTY_FOR_SOCKET_NAME)){
						String socketName = CommonUtilities.getFirstItem(entry.getValue());
						if(socketName != null){
							
							String sessionIdTemp = uch.getSessionId(socketName);
							if(sessionIdTemp != null){
								Map<String, String> sessionIdTargetIdMap = this.uch.getSessionIdTargetIdMap();
								if(sessionIdTargetIdMap != null && sessionIdTargetIdMap.size() != 0){
									String targetId = sessionIdTargetIdMap.get(sessionIdTemp);
									targetName = this.uch.getTargetName(targetId);
									if(targetName != null)
										break;
								}
							}
							
						}
						
					}else if(entry.getKey().equals("eltRef")){
						
						String eltRef = CommonUtilities.getFirstItem(entry.getValue());
						String socketName = null;
						
						if(eltRef.contains("#"))
							socketName = eltRef.substring(0, eltRef.lastIndexOf('#'));
						else
							socketName = eltRef;
						
						if(socketName != null){
							
							String sessionIdTemp = uch.getSessionId(socketName);
							if(sessionIdTemp != null){
								Map<String, String> sessionIdTargetIdMap = this.uch.getSessionIdTargetIdMap();
								if(sessionIdTargetIdMap != null && sessionIdTargetIdMap.size() != 0){
									String targetId = sessionIdTargetIdMap.get(sessionIdTemp);
									targetName = this.uch.getTargetName(targetId);
									if(targetName != null)
										break;
								}
							}
							
						}	
					}
				}
			}
		}
		
		String resServerForTarget = null;
		
		if(targetName != null){
			
			resServerForTarget = getResServerForTarget(targetName);
		
			/*if(resServerForTarget != null && !resServerForTarget.equals(resServerAppPath)){
		
				setResServerPath(resServerForTarget);

			}*/
		}
		
		
		//Parikshit Thakur : 20111115. Changes end.
		
		List<List<Map<String, List<String>>>> returnList = new ArrayList<List<Map<String,List<String>>>>();
		
		for ( Map<String, List<String>> resProps : resPropsList ) {
			
			String resType = CommonUtilities.getFirstItem( resProps.get(Constants.PROPERTY_RES_TYPE) );
			
			if ( resType != null ) {
				
				resType = resType.trim();
				
				if ( resType.equals(Constants.PROPERTY_RES_TYPE_VALUE_ATOMIC) ) { // Get Atomic or Dynamic Resource
					
					resProps.remove(Constants.PROPERTY_RES_TYPE);
					
					returnList.add( getAtomicOrDynamicResource(sessionId, resProps) );
					
				} else { // Get Local Resource or Resource Server Resource
					
					returnList.add( getLocalOrResServerResources(resProps, resServerForTarget) );					
				}
				
			} else { // Get Local Resource or Resource Server Resource	
				
				returnList.add( getLocalOrResServerResources(resProps, resServerForTarget) );				
			}
			
		}
		
		return returnList;
	}
	
	/**
	 * Calls same method of RetrievalManager
	 * @param resServerPath
	 */
	public void setResServerPath(String resServerPath){
		retrievalManager.setResServerPath(resServerPath);
		
	}
	
	/**
	 * Decide whether to go for getting resources(locally/resource server/both) using the properties 'resourceLocation' and 'resourceType' and return got resource properties.
	 * 
	 * @param resProps an object of Map&lt;String, List&lt;String&gt;&gt; specifies requested resource properties
	 * @param resServer a String value of res server url
	 * 
	 * @return an object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; specifies resource properties
	 */
	private List<Map<String, List<String>>> getLocalOrResServerResources(Map<String, List<String>> resProps, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer
		/*
		(a) Property : resourceLocation
	    	Values : (i) local (ii) resServer (iii) all
	    	default value : all

	    */
		
		
		if ( resProps == null )
			return null;
		
		String resourceLocation = CommonUtilities.getFirstItem( resProps.remove(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION) );
		
		if ( resourceLocation == null || resourceLocation.equals(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_ALL) ) {
			// First take local resources and go for resource server resources as per the value of resourceType
			
	
			int count = getCount(resProps);
			
			List<Map<String, List<String>>> retResPropsList = getLocalResources(resProps);
			
			if ( (retResPropsList == null) || (retResPropsList.size() == 0) ) {
				// Unable to get resources locally, so get resources from resource server irrespective the value of 'resourceType'.
				
				return getResourceServerResources(resProps, resServer);
				
			} else {
				
				if ( count != -1 ) {
					
					int size = retResPropsList.size();
					
					// if asked number of resources found from local then return it 
					if ( size >= count )
						return retResPropsList;
				}
				
				List<Map<String, List<String>>> resRetResPropsList = getResourceServerResources(resProps, resServer);
				
				
				if ( (resRetResPropsList == null) || (resRetResPropsList.size() == 0) ) {
					
					// Unable to get any resource from resource server then return the local resources.
					return retResPropsList;
					
				} else {
					
					if ( count != -1 ) {
						
						int localResSize = retResPropsList.size();
						int resserverResSize = resRetResPropsList.size();
						
						//Return as many resource as specified in count
						for ( int i = 0 ; ( (i+localResSize) < count ) && (i < resserverResSize) ; i++ ) 
							retResPropsList.add( resRetResPropsList.get(i) );
						
						return retResPropsList;
						
					} else {
						
						retResPropsList.addAll(resRetResPropsList);
						
						return retResPropsList;
					}
					
				}
				
				/*
				if ( resourceType == null || resourceType.equals(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_TYPE_VALUE_FIRST_MATCH) ) {
					// If the value of 'resourceType' is 'firstMatch' or null then return resources.
					
					return retResPropsList;
					
				} else {
					// Get Resources from resource server and merge them to local resource list.
					
					List<Map<String, List<String>>> resRetResPropsList = getResourceServerResources(resProps);
					
					if ( (resRetResPropsList == null) || (resRetResPropsList.size() == 0) ) {
						
						return retResPropsList;
						
					} else {
						
						retResPropsList.addAll(resRetResPropsList);
						
						return retResPropsList; 
					}
					
				}
				*/
			}
			
		} else if ( resourceLocation.equals(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_LOCAL) ) {
			// Get resources locally irrespective the value of 'resourceType'.
			
			return getLocalResources(resProps);
			
		} else if ( resourceLocation.equals(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_RES_SERVER) ) {
			// Get resources from resource server irrespective the value of 'resourceType'.
			
			return getResourceServerResources(resProps, resServer);
			
		} else {
			// Any other option is not available, so return null.
			
			return null;
		}
		
	}
	
	
	
	/**
	 * Get the int value of property 'count' and if exists then return it.
	 * If property doesn't exist then return -1.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an int value
	 */
	private int getCount(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null )
			return -1;
		
		String startStr = CommonUtilities.getFirstItem( reqProps.get(Constants.RESOURCE_QUERY_PROP_NAME_COUNT) );
		
		if ( startStr == null )
			return -1;
		
		try {
			return Integer.parseInt(startStr);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	
	/**
	 * Get specified resource locally.
	 * 
	 * @param resProps Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of  List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> getLocalResources(Map<String, List<String>> resProps) {
		
		if ( resProps == null )
			return null;
		
		if ( localResourceManager == null) {
			logger.warning("Local Resource Manager is not available for this UCH.");
			return null;
		}
		
		return ResourceUtil.cloneList( localResourceManager.getResources(resProps) );
	}
	
	/**
	 * Get specified resource from Resource Server.
	 * 
	 * @param resProps Map&lt;String, List&lt;String&gt;&gt;
	 * @param reServer a String value of res server url
	 * 
	 * @return an Object of  List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> getResourceServerResources(Map<String, List<String>> resProps, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer	
		if ( resProps == null )
			return null;
		
		if ( retrievalManager == null ){
			logger.warning("Resource Server is not available for this UCH.");
			return null;
		}
		
		List<Map<String, List<String>>> reqList = new ArrayList<Map<String,List<String>>>();
		reqList.add(resProps);
		
		List<List<Map<String, List<String>>>> resList = retrievalManager.getResources(reqList, resServer);
		
		if ( (resList == null) || (resList.size() == 0) )
			return null;
		
		return ResourceUtil.cloneList( resList.get(0) );
	}
	
	/**
	 * Return a list of Resource Properties representing matched Resource with the Resource Properties specified by 'resourceProps'.
	 * 
	 * @param sessionId a String value of SessionId
	 * @param resourceProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> getAtomicOrDynamicResource(String sessionId, Map<String, List<String>> resProps) {
		
		Map<String, String> newResProps = ResourceUtil.convertMap(resProps);
		
		Map<String, Object> retMap = resourceSheetManager.getAtomicOrDynamicResource(sessionId, newResProps, true);
		
		if ( retMap == null )
			return null;
		
		List<Map<String, List<String>>> resList = new ArrayList<Map<String, List<String>>>();
		resList.add( ResourceUtil.convertObjectMap(retMap) );
		
		return ResourceUtil.cloneList(resList);
	}
	
	

	/**
	 * Retrieve the resource from Resource Server and save it in cache directory and return that file path.
	 * @param uri a String value of uri.
	 * 
	 * @return a String value of file path.
	 */
	public String retrieveResource(String uri) {
		
		if ( retrievalManager == null )
			return null;
		
		int index = uri.indexOf('?');
		
		if ( index == -1 ) {
			logger.warning("'"+uri+"' is invalid resource retrieval URL.");
			return null;
		}
	
		Map<String, List<String>> props = CommonUtilities.prepareKeyValueListMap( uri.substring(index+1) );
		
		if ( props == null ) {
			logger.warning("'"+uri+"' is invalid resource retrieval URL.");
			return null;
		}
		
		Map<String, List<String>> returnMap = ResourceUtil.getSingleResource(this, props);
		
		if ( returnMap == null )
			return null;
		
		return CommonUtilities.getFirstItem( returnMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
	}
	
	//-----------------------------Retrieval Manager Functions Starts---------------------------------//
	
	
	/**
	 * Retrieve only best matched Resource from Resource Server.
	 * 
	 * @param resourceProps an Object of Map&lt;String, String&gt;
	 * @return an Object of Map&lt;String, Object&gt;
	 */
	public Map<String, List<String>> getResServerResource(Map<String, List<String>> resourceProps) {
		
		if ( retrievalManager == null )
			return null;
		
		return retrievalManager.getResource(resourceProps);
	}
	
	
	//------------------------------Retrieval Manager Functions Ends----------------------------------//
	
	
	//-------------------------------------Clear Cache Starts-----------------------------------------//
	
	/**
	 * Clear all Atomic and Resource Server Resources. 
	 * 
	 * @return an Object of List&lt;String&gt; specifies deleted file URIs
	 */
	public List<String> clearCache() {
		
		logger.info("Going to clear cache.");
		
		List<String> deletedUris = new ArrayList<String>();
		
		if ( retrievalManager != null ) {
			
			deletedUris = retrievalManager.clearCache();					
		}		
		
		clearAtomicResources(deletedUris);
		clearLocalResources();
		
		return deletedUris;
	}
	
	/**
	 * Clear all the Resource Sheet resources.
	 * Also clear downloaded Resource Sheets. 
	 * 
	 * @return an Object of List&lt;String&gt; specifies deleted file URIs
	 */
	public List<String> clearResourceSheetCache() {
		
		List<String> deletedUris = new ArrayList<String>();
		
		if ( retrievalManager != null ) {
			
			deletedUris = retrievalManager.clearResourceSheetCache();			
		}		
		
		clearAtomicResources(deletedUris);
		
		return deletedUris;
	}
	
	/**
	 * Clear all UIPM Client resources.
	 * 
	 * @return an Object of List&lt;String&gt; specifies deleted file URIs
	 */
	public List<String> clearUipmClientCache() {
		
		if ( retrievalManager != null ) {
			
			return retrievalManager.clearUipmClientCache();
		} else {
			
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Clear specified type of Resources.
	 * 
	 * @param resourceType a String value of Resource Type
	 * 
	 * @return an Object of List&lt;String&gt; specifies deleted file URIs
	 */
	public List<String> clearCache(String resourceType) {
		
		if ( retrievalManager != null ) {
			
			return retrievalManager.clearCache(resourceType);
		} else {
			
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Clear the specified Resources.
	 * 
	 * @param uris an Object of List&lt;String&gt; specifies deleted file URIs
	 */
	private void clearAtomicResources(List<String> uris) {
		
		resourceSheetManager.clearAtomicResources(uris);
	}
	
	/**
	 * Clear local resources.
	 */
	private void clearLocalResources() {
		
		logger.info("Going to clear local resources.");
		localResourceManager.reload();
	}
	//--------------------------------------Clear Cache Ends------------------------------------------//

	/**
	 * Returns cache dir path
	 * @return String value of cache dir path
	 */
	public String getCacheDir() {
		return uch.getCacheDir();
	}

	
		
	
}
