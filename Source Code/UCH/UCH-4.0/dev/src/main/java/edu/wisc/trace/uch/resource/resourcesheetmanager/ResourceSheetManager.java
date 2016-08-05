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
package edu.wisc.trace.uch.resource.resourcesheetmanager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Handle all request for managing  Atomic(Resource Sheet) Resources.
 * Also Manage Resource Sheets and its resources.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class ResourceSheetManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	//private RetrievalManager retrievalManager;
	
	private ResourceManager resourceManager;
	
	//List of Resource Sheets and their properties
	private List<ResourceSheetProperties> resourceSheetPropList = new ArrayList<ResourceSheetProperties>();
	
	// eltRef Vs. List<AResourceDescription> Map
	private LinkedHashMap<String, List<AResourceDescription>> resourceMap = new LinkedHashMap<String, List<AResourceDescription>>();
	
	//List of Parsed Resource Sheet
	List<String> parsedResourceSheetUri = new ArrayList<String>();
	
	// sessionId Vs. Set<eltRef> Map (Dynamic eltRef Map.)
	private Map<String, Set<String>> sessionIdEltPathMap = new TreeMap<String, Set<String>>();;
	
	// List of Target Description URI
	private List<String> tdUriList = new ArrayList<String>();
	
	
	/**
	 * Constructor.
	 * Provide the reference of ResourceManager to the local variables.
	 * 
	 * @param resourceManager an Object of ResourceManager
	 */
	public ResourceSheetManager(ResourceManager resourceManager) {
		
		this.resourceManager = resourceManager;
	}
	

	public ResourceManager getResourceManager(){
		return resourceManager;
	}
	/**
	 * Call the same method of Resource manager.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a String value
	 */
	String convertURI(String uri) {
		
		return resourceManager.convertURI(uri);
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
		
		if ( sessionIds == null ) {
			logger.warning("sessionIds is null.");
			return;
		}
		
		if ( eltIds == null) {
			logger.warning("eltIds is null.");
			return;
		}
		
		for ( String sessionId : sessionIds ) {
		
			if ( sessionIdEltPathMap.containsKey(sessionId) ) { // sessionId already exists in Map
				
				Set<String> elementIds = sessionIdEltPathMap.get(sessionId);
				
				if ( elementIds == null )
					continue;
				
				for ( String eltId : eltIds ) {
					
					if ( eltId == null )
						continue;
					
					elementIds.add(eltId);
				}
				
			} else { // sessionId does not exist in Map
				
				Set<String> elementIds = new TreeSet<String>();
				
				for ( String elementId : eltIds ) {
					
					if ( elementId == null )
						continue;
					
					elementIds.add(elementId);
				}
				sessionIdEltPathMap.put(sessionId, elementIds);
				
			}
		}
		
	}
	
	/**
	 * Parse &lt;ResDir&gt; Element of Target Description and add it in the list of TargetDescriptions.
	 * 
	 * @param tdUri a String value of Target Description URI(tdUri)
	 */
	public void addResourceDir(String tdUri) {
		
		if ( tdUri == null ) {
			logger.warning("tdUri is null.");
			return;
		}
		
		if ( tdUriList.contains(tdUri) ) {
			return;
		} 
		
		if ( (tdUri.indexOf("http://") != -1) || (tdUri.indexOf("https://") != -1) ) { 
			
			// Retrieve from resource Server.
			String resServerAppPath = resourceManager.getResServerAppPath();
			
			if ( (resServerAppPath != null) && (tdUri.indexOf( resServerAppPath ) != -1) ) {

				String tdUriString = null;
				
				Map<String, List<String>> tdPropsMap = retrieveResource(tdUri);
				
				if ( tdPropsMap != null ) {
					tdUriString = CommonUtilities.getFirstItem( tdPropsMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
				} 
				
				if ( tdUriString == null ) {
	        		return;
	        	}
				
				tdUri = tdUriString;
				
				//String tdUriString = UCH.getRetrievalManager().retrieveResource(tdUri);
	        	/*
				String tdUriString = null;
				Map<String, List<String>> tdUriMap = null;
				
				if ( retrievalManager != null )
					tdUriMap = retrievalManager.retrieveResource(tdUri);
				
				if ( tdUriMap != null ) {
					
					tdUriString = CommonUtilities.getFirstItem( tdUriMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
				} 
	        	
	        	if ( tdUriString == null ) {
	        		return;
	        	}
	        	*/
			}
			
			
		} else {
			
			tdUri = tdUri.replaceAll(" ", "%20");
		}
		
		tdUriList.add(tdUri);
		parse(tdUri);
		
	}
	
	
	/**
	 * Parse &lt;ResDir&gt; Element of Target Description.
	 * 
	 * @param tdUri a String value of Target Description URI(tdUri)
	 */
	private boolean parse(String tdUri) {
	
		logger.info("TD URI:"+tdUri);
		
		Document tdDoc = null;
		try {
			
			URI uri = new URI(tdUri);
			tdDoc = CommonUtilities.parseDocument(uri);
			
		} catch (NullPointerException e) {
			logger.warning("NullPointerException");
		} catch (SAXException e) {
			logger.warning("SAXException");
		} catch (IOException e) {
			logger.warning("IOException");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException");
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException");
		}
		
        if (tdDoc == null) {
        	logger.warning("File is not in proper form of XML");
            return false;
        }
        
        Element root = tdDoc.getDocumentElement();

        
        if( !root.getLocalName().equals("target") )
        	return false;
           
        //Check id attribute.
        String id = root.getAttribute("id");
        if (id == null || id.trim().equals("")) {
        	logger.warning("'id' attribute is not exists in target Element.");
        	return false;
        }
        	

        //Check name attribute.
        String targetName = root.getAttribute("about");
        if (targetName == null || targetName.trim().equals("")) {
        	logger.warning("'about' attribute is not exists in target Element.");
        	return false;
        }
        
        
       
        NodeList nodeLists = root.getChildNodes();

        
        for (int i = 0; i < nodeLists.getLength(); i++) {

			Node node = nodeLists.item(i);
			String nodeName = node.getLocalName();

			if (nodeName == null)
				continue;

			//Parikshit Thakur : 20111018. Modified code to parse new structure of TD.
			/*if (nodeName.equals("RDF") && node.hasChildNodes() && (node instanceof Element)) {

				NodeList resDirNodeList = node.getChildNodes();

				for (int j = 0; j < resDirNodeList.getLength(); j++) {

					Node resDirNode = resDirNodeList.item(j);
					String resDirNodeName = resDirNode.getLocalName();
					
					if ( resDirNodeName == null )
						continue;
					
					if ( resDirNodeName.equals("ResDir") && resDirNode.hasChildNodes() ) {
						
						new ResourceDirectory(this, (Element) resDirNode, resourceSheetPropList, tdUri);
					}
					
				}
			}*/
			
			if(nodeName.equals("resSheet") && node.hasChildNodes() && (node instanceof Element)){
				
				new ResourceDirectory().parseResSheetElement(this, (Element) node, resourceSheetPropList, tdUri);
				
			}
			// 2012-09-11 : "resSvc" needs to parse in the TD.
			
			if(nodeName.equals("resSvc") && node.hasChildNodes() && (node instanceof Element)){
				String resSer = ((Element)node).getAttribute("about");
				if(resSer != null){
					
					resourceManager.setTargetResSvcMap(targetName, resSer);
					
				}
			}
			// Changes end.
		}

        logger.info("No of Resource Sheets : "+resourceSheetPropList.size() );
        return true;
	}
	
	
	/**
	 * Download Resource SHeet from Resource Server using the property forDomain and other properties for Resource Sheet.
	 * 
	 * @param forDomain a String value of forDomanin
	 */
	private boolean downloadResourceSheet(String forDomain, String forLang) {
		
		if ( forDomain == null ) {
			logger.warning("'forDomain' is null.");
			return false;
		}
		
		logger.info("Going to download Resource Sheet for '"+forDomain+"'.");
		
		Map<String, List<String>> resourceProps = new HashMap<String, List<String>>();
		
		resourceProps.put(Constants.PROPERTY_FOR_DOMAIN, CommonUtilities.convertToList(forDomain) );
		
		resourceProps.put(Constants.PROPERTY_CONFORMS_TO, CommonUtilities.convertToList(Constants.PROPERTY_CONFORMS_TO_VALUE_RESSHEET) );
		
		resourceProps.put(Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_RES_TYPE_VALUE_RESSHEET) );
		
		if ( forLang != null ) {
			
			resourceProps.put(Constants.PROPERTY_RES_FOR_LANG, CommonUtilities.convertToList(forLang) );
		} 
		
		if ( resourceManager == null )
			return false;
		
		Map<String, List<String>> resSheetProps = ResourceUtil.getSingleResource(resourceManager, resourceProps);
		//Map<String, List<String>> resSheetProps = resourceManager.getResServerResource(resourceProps);
		
		if ( (resSheetProps != null) && (resSheetProps.size() > 0) ) {
			
			addToRresourceSheetPropList(resSheetProps);
			return true;
			
		} else {
			
			return false;
		}
		
	}
	
	/**
	 * Return an Object representing matched Resource with the Resource Properties specified by 'resProps'.
	 * 
	 * @param sessionId a String value of SessionId
	 * @param resProps a Map&lt;String, String&gt; of Resource Properties
	 * @return an Object of Map&lt;String, Object&gt; representing matched Resource  
	 */	
	public Map<String, Object> getAtomicOrDynamicResource(String sessionId, Map<String, String> resProps, boolean firstTime) {
		
		String eltRef = resProps.get("eltRef");
		
		if ( eltRef == null ) {
			logger.warning("resPropMap does not contain 'eltRef'.");
			return null;
		}
		
		if ( sessionId != null ) {
			
			//Try to get as Dynamic Resource.
			
			if ( isResourceDynamic(sessionId, eltRef) ) {
				
				List<Map<String, String>> resPropMapList = new ArrayList<Map<String,String>>();
				resPropMapList.add(resProps);
				
				List<Map<String, Object>> returnList = resourceManager.getDynRes(sessionId, resPropMapList);
				
				if ( returnList == null ) {
					logger.warning("Unable to get Dynamic Resource.");
					return null;
				}
				
				Map<String, Object> returnMap = returnList.get(0);
				
				if ( returnMap == null ) {
					logger.warning("Unable to get Dynamic Resource.");
					return null;
				}
				
				return returnMap;
				
			}
			
		}
		
		List<AResourceDescription> aResDescList = resourceMap.get(eltRef);
		
		if ( aResDescList != null ) {
			
			for( AResourceDescription aResDesc : aResDescList) {
			//	logger.info(" :::::::::::::: AResDesc :::::::::::: ");
			//	aResDesc.print();
				if ( aResDesc.propMatched(resProps) ) {
					
					Map<String, Object> returnMap = new HashMap<String, Object>();
					
					String content = aResDesc.getContent();
					
					if ( content != null ) {
						
						returnMap.put("content", content );
						
					} else {
						
						String contentAt = aResDesc.getContentAt();
						
						if ( contentAt != null ) {
							
							
							String localUri = null;
							
							String resserver_appPath = resourceManager.getResServerAppPath();
							
							
							//If contentAt Uri is resource server Uri then downnload it and return local Uri.
							if ( (resserver_appPath != null) && !resserver_appPath.trim().equals("") && (contentAt.indexOf(resserver_appPath) != -1) )  {
								
								localUri = resourceManager.retrieveResource(contentAt);
								
								if ( localUri != null )
									localUri = resourceManager.convertURI(localUri);
								
							}
							
							if ( localUri == null)
								returnMap.put("contentAt", contentAt );
							else
								returnMap.put("contentAt", localUri );
							
							returnMap.put("contentAt", contentAt );
							
						} else {
							
							returnMap = null;
						}
					}
					
					return returnMap;
					
				}
			}
		}
		
		AResourceDescription aResDesc = getResourceFromResourceSheet(eltRef, resProps);
		
		if ( aResDesc != null ) {
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			String content = aResDesc.getContent();
			
			if ( content != null ) {
				
				returnMap.put("content", content );
				
			} else {
				
				String contentAt = aResDesc.getContentAt();
				
				if ( contentAt != null ) {
					
					String localUri = null;
					
					String resserver_appPath = resourceManager.getResServerAppPath();
					
					
					//If contentAt Uri is resource server Uri then downnload it and return local Uri.
					if ( (resserver_appPath != null) && !resserver_appPath.trim().equals("") && (contentAt.indexOf(resserver_appPath) != -1) )  {
						
						localUri = resourceManager.retrieveResource(contentAt);
						
						if ( localUri != null )
							localUri = resourceManager.convertURI(localUri);
						
					}
					
					if ( localUri == null)
						returnMap.put("contentAt", contentAt );
					else
						returnMap.put("contentAt", localUri );
					
					returnMap.put("contentAt", contentAt );
					
				} else {
					
					returnMap = null;
				}
			}
			
			return returnMap;
			
			
		} else {
			
			// Going for Download Resource Sheet from Resource Server.
			if ( firstTime ) {
				
				String forDomain = null;
				if ( eltRef.indexOf("#") == -1 )
					forDomain = eltRef;
				else
					forDomain = eltRef.substring(0, eltRef.indexOf("#") );
				
				
				
				String forLang = resProps.get("forLang");
				
				if ( forLang == null ) 
					forLang = "en";
				
				//logger.info("Going to download Resource Sheet of '"+forDomain+"'for Language '"+forLang+"'.");
				
				if ( downloadResourceSheet(forDomain, forLang) ) {
					return getAtomicOrDynamicResource(sessionId, resProps, false);
				} else
					return null;
				
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Check whether resource is dynamic or not. 
	 * 
	 * @param sessionId a String value of sessionId
	 * @param eltRef a String value of element Ref
	 * @return Whether resource is dynamic or not.
	 */
	private boolean isResourceDynamic(String sessionId, String eltRef) {
		
		if ( sessionId == null ) {
			logger.warning("SessionId is null.");
			return false;
		}
		
		if ( eltRef == null ) {
			logger.warning("Element Ref is null.");
			return false;
		}
		
		logger.info("Dynamic Elements :: "+sessionIdEltPathMap);
		
		Set<String> dynElements = sessionIdEltPathMap.get(sessionId);
		
		if ( dynElements == null ) {
			logger.warning("Unable to get Dynamic Element Set for session '"+sessionId+"'");
			return false;
		}
		
		logger.info("Dynamic Elements of Session '"+sessionId+"':: "+dynElements);
		
		String elementId = null;
		
		if ( eltRef.indexOf("#") != -1 )
			elementId = eltRef.substring( eltRef.indexOf("#")+1 );
		else 
			elementId = eltRef;
			
		if ( !dynElements.contains(elementId) ) {
			logger.warning("'"+eltRef+"' is not Dynamic.");
			return false;
		}
		
		logger.info("'"+eltRef+"' is Dynamic.");
		return true;
	}
	
	/**
	 * Get Matched AResourceDescription for the Resource Properties specified by 'resProps'. 
	 * 
	 * @param eltRef a String value of ElementRef
	 * @param resProps an Object of Map&lt;String, String&gt;
	 * 
	 * @return an Object of AResourceDescription
	 */
	private AResourceDescription getResourceFromResourceSheet(String eltRef, Map<String, String> resProps) {
		
		
		if ( eltRef == null ) {
			logger.warning("'eltRef' is null.");
			return null;
		}
		
		String forDomain = null;
		
		if ( eltRef.indexOf("#") == -1 ) 			
			forDomain = eltRef;	
		else 
			forDomain = eltRef.substring(0, eltRef.lastIndexOf("#") );
		
		Map<String, String> resSheetProps = new HashMap<String, String>();
		
		resSheetProps.put("forDomain", forDomain);
		
		String type = resProps.get("type");
		if ( type == null ) { //set default value of type
			type = Constants.RSHEET_TYPE_DEFAULT_VALUE;
		}
		resSheetProps.put("type", type);
		
		String forLang = resProps.get("forLang");
		if ( forLang == null ) { //set default value of forLang
			forLang = Constants.RSHEET_FOR_LANG_DEFAULT_VALUE;
		}
		resSheetProps.put("forLang", forLang);
		
		String role = resProps.get("role");
		if ( role != null ) {
			resSheetProps.put("role", role);
		}
		String format = resProps.get("format");
		if ( format != null ) {
			resSheetProps.put("format", format);
		}
		
		List<String> resSheetUriList = getMatchedResourceSheets(resSheetProps);
		
		if ( (resSheetUriList == null) || ( resSheetUriList.size() == 0) ) {
			logger.warning("Unable to get Resource Sheet.");
			return null;
		}
		
		for ( String resSheetURI : resSheetUriList ) {
			
			
			//logger.info("Parsed Res Sheets :"+parsedResourceSheetUri);
			if ( parsedResourceSheetUri.contains(resSheetURI) )
				continue;
			
			//logger.info(" Going to parse Resource Sheet : "+resSheetURI);
			AResourceDescription aResourceDescription = new ResourceSheet(this, resSheetURI).findResourceByResProps(resourceMap, resProps);
			
			if ( aResourceDescription != null ) {
				
				synchronized(parsedResourceSheetUri) {
					
					if ( !parsedResourceSheetUri.contains(resSheetURI) )
						parsedResourceSheetUri.add(resSheetURI);
				}
				
				return aResourceDescription;
			}
		}
		
		return null;
	}
	
	/**
	 * Get matched Resource Sheet URIs.
	 * 
	 * @param resSheetProps an Object of Map&lt;String, String&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	List<String> getMatchedResourceSheets(Map<String, String> resSheetProps) {
		
		if ( resSheetProps == null ) {
			return null;
		}
		
		List<String> resSheetUriList = new ArrayList<String>();
		
		synchronized (resourceSheetPropList) {
			
			for( ResourceSheetProperties resourceSheetProperties : resourceSheetPropList ) {
				
				if ( resourceSheetProperties.matched(resSheetProps) ) 
					resSheetUriList.add( resourceSheetProperties.getResSheetUri() );		
			}
		}
		
		return resSheetUriList;
	}
	
	/**
	 * Clear the specified Resources.
	 * 
	 * @param notDeletedUris an Object of List&lt;String&gt;
	 */
	public void clearAtomicResources(List<String> notDeletedUris) {
		
		if ( (notDeletedUris != null) && (notDeletedUris.size() > 0) ) {
			
			synchronized (resourceSheetPropList) {
				
				List<ResourceSheetProperties> listToDelete = new ArrayList<ResourceSheetProperties>();
				
				for ( ResourceSheetProperties resourceSheetProperties : resourceSheetPropList ) {
					
					if ( notDeletedUris.contains( resourceSheetProperties.getResSheetUri() ) )
						listToDelete.add(resourceSheetProperties);
				}
				
				for ( ResourceSheetProperties resourceSheetProperties : listToDelete ) {
					resourceSheetPropList.remove(resourceSheetProperties);
				}
			}
		}
		
		synchronized (parsedResourceSheetUri) {
			parsedResourceSheetUri.clear();
		}
		
		synchronized (resourceMap) {
			resourceMap.clear();
		}
	}
	//-----------------------Retrieval of Resource Sheet From Resource Server Starts------------------------//
	
	
	/**
	 * Get required value from the specified resSheetProps and add a new entry of ResourceSheetProperties.
	 * 
	 * @param resSheetProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public void addToRresourceSheetPropList(Map<String, List<String>> resSheetProps) {
		
		if ( resSheetProps == null ) {
			logger.warning("Resource Sheet Properties is null.");
			return;
		}
		
		String resourceLocalAt = CommonUtilities.getFirstItem( resSheetProps.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		if ( resourceLocalAt == null ) {
			logger.warning("Unable to get Resource Sheet Local URI.");
			return;
		}
		
		String resName = CommonUtilities.getFirstItem(resSheetProps.get(Constants.PROPERTY_RES_NAME));
		
		List<String> forDomainList = resSheetProps.get(Constants.PROPERTY_FOR_DOMAIN);	
		if ( (forDomainList == null) || (forDomainList.size() <= 0) ) {
			logger.warning("Unable to get the value of the property '"+Constants.PROPERTY_FOR_DOMAIN+"'.");
			return;
		}
		
		List<String> conformsToList = resSheetProps.get(Constants.PROPERTY_CONFORMS_TO);	
		if ( (conformsToList == null) || (conformsToList.size() <= 0) ) {
			
			conformsToList = new ArrayList<String>();
			conformsToList.add(Constants.PROPERTY_CONFORMS_TO_VALUE_RESSHEET);
		}
		
		List<String> forLangList = resSheetProps.get(Constants.PROPERTY_RES_FOR_LANG);	
		if ( (forLangList == null) || (forLangList.size() <= 0) ) {
			
			forLangList = new ArrayList<String>();
			forLangList.add(Constants.PROPERTY_RES_FOR_LANG_VALUE_EN);
		}
		
		List<String> typeList = resSheetProps.get(Constants.PROPERTY_DC_ELEMENTS_TYPE);	
		if ( (typeList == null) || (typeList.size() <= 0) ) {
			
			typeList = new ArrayList<String>();
			typeList.add(Constants.PROPERTY_DC_ELEMENTS_TYPE_VALUE_TEXT);
		}
		
		
		ResourceSheetProperties resourceSheetProperties = new ResourceSheetProperties();
		
		resourceSheetProperties.setResSheetUri(resourceLocalAt);
		//resourceSheetProperties.setResName(resName);
		
		resourceSheetProperties.setAResDescForDomainList(forDomainList);
		resourceSheetProperties.setConformsToList(conformsToList);
		resourceSheetProperties.setForLangList(forLangList);
		resourceSheetProperties.setTypeList(typeList);
		
		List<String> roleList = resSheetProps.get(Constants.PROPERTY_RES_ROLE);
		if ( (roleList != null) && (roleList.size() > 0) ) {
			resourceSheetProperties.setRoleList(roleList);
		} else {
			resourceSheetProperties.setRoleList(CommonUtilities.convertToList(Constants.RSHEET_PROPERTY_ROLE_VALUE_LABEL));
		}
		
		synchronized (resourceSheetPropList) {
			resourceSheetPropList.add(resourceSheetProperties);
		}
	}
	
	/**
	 * Get specified resource from resource server.
	 * 
	 * @param resourceURI a String value of Resource Server Resource URI
	 * 
	 * @return an object of Map&lt;String, List&lt;String&gt;&gt; specifies retrieved resource properties
	 */
	Map<String, List<String>> retrieveResource(String resourceURI) {
		
		if ( resourceURI == null )
			return null;
		
		String resServerAppPath = resourceManager.getResServerAppPath();
		
		if ( resourceURI.indexOf( resourceManager.getResServerAppPath() ) == -1 ) 
			return null;
		
		int index = resourceURI.indexOf('?');
		
		if ( index == -1 ) 
			return null;
		
		return ResourceUtil.getSingleResource(resourceManager, CommonUtilities.prepareKeyValueListMap( resourceURI ) );
	}

/**
 * Returns cache dir path
 * @return String value of cache dir path
 */
	public String getCacheDir() {
		
		return resourceManager.getCacheDir();
	}


	/**
	 * Calls same method of resource sheet
	 * @param resPropList an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * @return a List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt; of Resources.
	 */
	public List<List<Map<String, List<String>>>> getResourceSheet(List<Map<String, List<String>>> resPropList) {
		
		return resourceManager.getResources(null, resPropList);
	}
	
	/*
	 public Map<String, List<String>> getResServerResource(Map<String, List<String>> resourceProps) {
		
		return resourceManager.getResServerResource(resourceProps);
	}
	 */
	
	/*
	Map<String, List<String>> downloadAndGetRSheetProps(Map<String, List<String>> resSheetProps) {
		
		if ( (resSheetProps == null) || (resSheetProps.size() == 0) ) {
			
			logger.warning("Invalid Resource Sheet Properties.");
			return null;
		}
	
		if ( retrievalManager == null ) {
			logger.warning("URL of Resource Server is not available.");
			return null;
		}
		
		return retrievalManager.getResource(resSheetProps);
	}
	*/
	
	/*
	String getResourceSheetURI(Map<String, List<String>> resSheetProps) {
		
		if ( (resSheetProps == null) || (resSheetProps.size() == 0) ) {
			
			logger.warning("Invalid Resource Sheet Properties.");
			return null;
		}
	
		if ( retrievalManager == null ) {
			logger.warning("URL of Resource Server is not available.");
			return null;
		}
			
		Map<String, List<String>> returnMap = retrievalManager.getResource(resSheetProps);
		
		if ( returnMap == null ) {
			logger.warning("Unable to get resource for Resource Sheet.");
			return null;
		}
		
		return CommonUtilities.getFirstItem( returnMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
	}
	*/
	//------------------------Retrieval of Resource Sheet From Resource Server Ends-------------------------//
	
}
