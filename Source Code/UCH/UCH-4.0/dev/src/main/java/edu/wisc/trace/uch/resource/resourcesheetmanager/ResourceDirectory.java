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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import org.openurc.uch.TAException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Parse ResourceSheet and provide method for getting matched Resource.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

class ResourceDirectory {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	
	public ResourceDirectory() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor.
	 * Parse &lt;ResDir&gt; Element of Target Description(.td) file. 
	 * And put Resource Sheet Properties in the resourceSheetPropList.
	 * 
	 * @param resourceSheetManager an Object of Resource Sheet Manager
	 * @param element an Object of Element
	 * @param resourceSheetPropList an Object of List&lt;resourceSheetPropList&gt;
	 * @param tdUri a String value of Target Description URI 
	 */
	ResourceDirectory(ResourceSheetManager resourceSheetManager, Element element, List<ResourceSheetProperties> resourceSheetPropList, String tdUri) {
		
		parseElement(resourceSheetManager, element, resourceSheetPropList, tdUri);
		
	}
	
	/**
	 * Parse &lt;ResDir&gt; Element of Target Description(.td) file. 
	 * And put Resource Sheet Properties in the resourceSheetPropList.
	 * 
	 * @param resourceSheetManager an Object of Resource Sheet Manager
	 * @param element an Object of Element
	 * @param resourceSheetPropList an Object of List&lt;resourceSheetPropList&gt;
	 * @param tdUri a String value of Target Description URI
	 */
	private void parseElement(ResourceSheetManager resourceSheetManager, Element element, List<ResourceSheetProperties> resourceSheetPropList, String tdUri) {
		
		NodeList nodeList = element.getChildNodes();
	
		for ( int i=0 ; i<nodeList.getLength() ; i++ ) {
			
			Node node = nodeList.item(i);
			String nodeName = node.getLocalName();
			
			if ( nodeName == null )
				continue;
			
			if ( nodeName.equals("ResSheet") && node.hasChildNodes() && (node instanceof Element) ) {
				
				parseResSheetElement(resourceSheetManager, (Element)node, resourceSheetPropList, tdUri);
				
			} 
		}
		
		
	}
	
	
	/**
	 * Parse &lt;ResSheet&gt; Element of Target Description(.td) File 
	 * and put Resource Sheet Properties in the resourceSheetPropList.
	 * 
	 * @param resourceSheetManager an Object of Resource Sheet Manager
	 * @param element an Object of Element
	 * @param resourceSheetPropList an Object of List&lt;resourceSheetPropList&gt;
	 * @param tdUri a String value of Target Description URI
	 */
	public void parseResSheetElement(ResourceSheetManager resourceSheetManager, Element element, List<ResourceSheetProperties> resourceSheetPropList, String tdUri) {
		
		if ( element == null ) 
			return;
		
		NodeList nodeList = element.getChildNodes();
		
		String rSheetURI = null;
		String localRSheetURI = null;
		String resName = null;
		List<String> forDomainList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		List<String> conformsToList = new ArrayList<String>();
		List<String> forLangList = new ArrayList<String>();
		List<String> roleList = new ArrayList<String>();
		List<String> formatList = new ArrayList<String>();
		// 'retrieveFrom' can be more than one in the 'resSheet' to indicate the retrieve path of the resource sheet.
		List<String> retrieveFromURIList = new ArrayList<String>();
		
		
		for( int i=0 ; i < nodeList.getLength() ; i++ ) {
			
			Node node = nodeList.item(i);
			
			if ( node == null ) 
				continue;
			
			String nodeName = node.getNodeName();
			
			if ( (nodeName == null) || (nodeName.startsWith("#") ) )
				continue;
			
			logger.info("Node Name="+nodeName);
			
			// 2012-09-13 : 'localAt' element is removed from 'resSheet'.
			
//			if ( nodeName.equals("localAt") ) {
//				
//				if ( node.getChildNodes().getLength() != 1 ) 
//					continue;
//				
//				Node firstCld = node.getFirstChild();
//				
//				if ( firstCld.getNodeType() != Node.TEXT_NODE )
//					continue;
//				
//				rSheetURI = getResourceSheetURI( firstCld.getNodeValue(), tdUri );
//			
//				// Parikshit Thakur : 20111114 : Modified code to implement resName element under resSheet element in TD.
//				/*if ( rSheetURI != null ) {
//					
//					if ( (rSheetURI.indexOf("http://") != -1) || (rSheetURI.indexOf("https://") != -1) ) { 
//						
//						
//						//Download Resource Sheet from Resource Server and make a new entry in 'resourceSheetPropList'.
//						//Map<String, List<String>> propMap = getPropMap( rSheetURI );
//						
//						//propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
//						//propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
//						
//						//Map<String, List<String>> resSheetProps = resourceSheetManager.getResServerResource(propMap);
//						
//						
//						//Parikshit Thakur: 20111021. Added code for the case when resSheet is on a server other then resserver.
//						String resServerAppPath = resourceSheetManager.getResourceManager().getResServerAppPath();
//						
//						//if ( resServerAppPath == null && !(rSheetURI.contains("?"))) {
//						if ( !(rSheetURI.contains("?"))) {	
//							logger.info("Resource Server App Path is null.");
//							try {
//								URL url = new URL(rSheetURI);
//								InputStream inputStream = url.openStream();
//								FileOutputStream fileOutputStream = new FileOutputStream("C:/"+rSheetURI.substring(rSheetURI.lastIndexOf("/")));
//								int ch;
//								while ((ch = inputStream.read()) != -1)
//								{
//								   fileOutputStream.write(ch);
//								}
//								 
//								fileOutputStream.close();
//								inputStream.close();
//							} catch (MalformedURLException e) {
//								e.printStackTrace();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//							
//							localRSheetURI = "file:///C:/"+rSheetURI.substring(rSheetURI.lastIndexOf("/")); // Change the static path
//							
//							//parseRSheet(localRSheetURI, resourceSheetManager);
//							
//							
//							
//							//return null;
//							//return ResourceUtil.getSingleResource(resourceManager, CommonUtilities.prepareKeyValueListMap( resourceURI.substring(index+1) ) );
//						}
//						
//						else if(resServerAppPath != null) {
//							Map<String, List<String>> resSheetProps = resourceSheetManager.retrieveResource(rSheetURI);
//							
//							if ( (resSheetProps != null) && (resSheetProps.size() > 0) ) 
//								resourceSheetManager.addToRresourceSheetPropList(resSheetProps);
//							
//							return;
//						}					
//						
//						
//					}
//					//Parikshit Thakur : 20111020. Added code to parse file URL for ressheet
//					if((rSheetURI.indexOf("file://") != -1) ){
//						
//						localRSheetURI = rSheetURI;
//						//parseRSheet(rSheetURI, resourceSheetManager);
//						
//					}
//					
//				} else {
//					
//					logger.warning("Unable to get Resource Sheet URI from TD.");
//					return;
//				}*/
//				
//			}

			// 2012-09-13 : 'resName' element is removed from 'resSheet'.
			
			/*else if ( nodeName.equals("resName") ) {
				
				if ( node.getChildNodes().getLength() != 1 ) 
					continue;
				
				Node firstCld = node.getFirstChild();
				
				if ( firstCld.getNodeType() != Node.TEXT_NODE )
					continue;
				
				resName = firstCld.getNodeValue();	
				
			}*/
			
			//Parikshit Thakur : 20111018. Added to parse new structure of TD.
			if( nodeName.equals("scents") && (node instanceof Element) ){
				
				NodeList scentNodes = node.getChildNodes();
				
				for( int j=0 ; j < scentNodes.getLength() ; j++ ) {
					
					Node scentNode = scentNodes.item(j);
					if ( scentNode == null ) 
						continue;
					
					String scentNodeName = scentNode.getNodeName();
					
					if(scentNodeName.equals("forDomain") && (scentNode instanceof Element)){
						
						if ( scentNode.getChildNodes().getLength() != 1 ) 
							continue;
						
						Node firstCld = scentNode.getFirstChild();
						
						if ( firstCld.getNodeType() != Node.TEXT_NODE )
							continue;
																		
						forDomainList.add( firstCld.getNodeValue() );
					}
					else if ( scentNodeName.equals("dc:type") && (scentNode instanceof Element) ) {
						
						if ( scentNode.getChildNodes().getLength() != 1 ) 
							continue;
						
						Node firstCld = scentNode.getFirstChild();
						
						if ( firstCld.getNodeType() != Node.TEXT_NODE )
							continue;
						
						typeList.add( firstCld.getNodeValue() );			
						
					}
					else if ( scentNodeName.equals("dc:format") && (scentNode instanceof Element) ) {
						
						if ( scentNode.getChildNodes().getLength() != 1 ) 
							continue;
						
						Node firstCld = scentNode.getFirstChild();
						
						if ( firstCld.getNodeType() != Node.TEXT_NODE )
							continue;
						
						formatList.add( firstCld.getNodeValue() );			
						
					}
					else if ( scentNodeName.equals("forLang") && (scentNode instanceof Element) ) {
						
						if ( scentNode.getChildNodes().getLength() != 1 ) 
							continue;
						
						Node firstCld = scentNode.getFirstChild();
						
						if ( firstCld.getNodeType() != Node.TEXT_NODE )
							continue;
						
						forLangList.add( firstCld.getNodeValue() );			
					
					}
					else if ( scentNodeName.equals("role") && (scentNode instanceof Element) ) {
						
						if ( scentNode.getChildNodes().getLength() != 1 ) 
							continue;
						
						Node firstCld = scentNode.getFirstChild();
						
						if ( firstCld.getNodeType() != Node.TEXT_NODE )
							continue;
						
						roleList.add( firstCld.getNodeValue() );			
					
					}
				}
				
			}else if ( nodeName.equals("dcterms:conformsTo") && (node instanceof Element) ) {
				
				if ( node.getChildNodes().getLength() != 1 ) 
					continue;
				
				Node firstCld = node.getFirstChild();
				
				if ( firstCld.getNodeType() != Node.TEXT_NODE )
					continue;
				
				conformsToList.add( firstCld.getNodeValue() );			
				
				// 2012-09-13 : Now 'resSheet' have 'retrieveFrom' element to indicate the retrieve path for resourc sheet.
			}else if( node.getNodeName().equals("retrieveFrom")){
				
				if( !(node instanceof Element) ) {
					logger.warning("retrieveFrom Element is not in proper Format in resSheet.");
					//throw new Exception("Socket Element is not in proper Format - retrieveFrom element missing.");
				}
				
				if( ((Element)node).getFirstChild() != null){
					
					String temp = ((Element)node).getFirstChild().getNodeValue().trim();
				
					if(temp.length() > 0){
						temp = temp.replace(" ","%20").trim();
						retrieveFromURIList.add(temp);
					}
				}
			}
		}
		
		// 2012-09-14 : Below code inside conditional statement finds the exact URI where rSheet exist and take it in further use. 
		
		if(retrieveFromURIList.size() > 0){ 
			
			for(String retrievePath : retrieveFromURIList){ // for each retrieveFrom
				
				if ( (retrievePath.indexOf("http://") != -1) || (retrievePath.indexOf("https://") != -1) ) {
					
					if(retrievePath.indexOf("?") == -1){
						
						localRSheetURI = getDocumentFromThisURI(tdUri, retrievePath); 
						
						//GetDocument for Socket Description using resource URI.
						if(localRSheetURI != null){
							
							break; 
							
						}/*else{
							
							if(isResSheetAvailableFromRSheetManager(resourceSheetManager,retrievePath)){
								return;
							}
						}*/
					}else{
						// if URI contains '?', it means this request is made using resource name.
						String rSheetName = retrievePath.substring(retrievePath.indexOf("name")+5).trim();
						
						if(isResSheetAvailableFromRSheetManager(resourceSheetManager, rSheetName))
							return;
							
						logger.severe(" 'retrieveFrom' uris in TD, at "+tdUri+" are not sufficient to find rSheet..!!! ");
						return;
					}		
								
				}else if( retrievePath.indexOf("file://") != -1 ) { 
				
					//checking for existence of file at specified URI.		
						if(isResSheetExistsAtLocalPath(retrievePath)){
							localRSheetURI = retrievePath;
							break;
						}
						
				}else{
					
					//checking for existence of file relative to TD using this path.
					String uriString = tdUri.substring(0,tdUri.lastIndexOf("/") + 1)
							+ (retrievePath.indexOf("/") == 0 ? retrievePath.substring(1) : retrievePath);

					if(isResSheetExistsAtLocalPath(uriString)){
						localRSheetURI = uriString;
						break;
					}

				}
			}
			
			if(localRSheetURI == null){
					
				if(isResSheetAvailableFromRSheetManager(resourceSheetManager,element.getAttribute("about"))){
						return;
				}
	
				logger.severe(" 'retrieveFrom' uris in TD, at "+tdUri+" are not sufficient to find rSheet..!!! ");
				return;
			}
		}
		
		
		
		//Parikshit Thakur : 20111114. Implemented support for resName element in TD
//		if(resName != null){
//			
//			Map<String, List<String>> propMap = new HashMap<String, List<String>>();
//			
//			propMap.put(Constants.PROPERTY_RES_NAME, CommonUtilities.convertToList(resName) );
//			
//			List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
//			resProps.add(propMap);
//			
//			List<List<Map<String, List<String>>>> returnProps = resourceSheetManager.getResourceSheet(resProps);
//			
//			if ( (returnProps != null) && (returnProps.size() != 0) ){
//				
//				List<Map<String, List<String>>> propList = returnProps.get(0);
//				
//				if ( (propList != null) && (propList.size() != 0) ){
//					
//					Map<String, List<String>> returnPropMap = propList.get(0);
//					
//					if ( returnPropMap != null ){
//						
//						resourceSheetManager.addToRresourceSheetPropList(returnPropMap);
//						return;
//					}
//				}
//			}
//			
//			
//			/*String resServerAppPath = resourceSheetManager.getResourceManager().getResServerAppPath();
//			if(!resServerAppPath.contains("http"))
//				resServerAppPath = "http://" + resServerAppPath;
//			
//			String resSheetURI = resServerAppPath + "/retrieve?name=" + resName;
//			Map<String, List<String>> resSheetProps = resourceSheetManager.retrieveResource(resSheetURI);
//			
//			if ( (resSheetProps != null) && (resSheetProps.size() > 0) ) {
//				resourceSheetManager.addToRresourceSheetPropList(resSheetProps);
//			
//				return;
//			
//			}*/
//		}
//		if(rSheetURI != null){
//				
//				if ( (rSheetURI.indexOf("http://") != -1) || (rSheetURI.indexOf("https://") != -1) ) { 
//					
//					if ( !(rSheetURI.contains("?"))) {	
//						
//												
//						List<Map<String, List<String>>> resPropList = new ArrayList<Map<String,List<String>>>();
//						Map<String, List<String>> resPropMap = new HashMap<String, List<String>>();
//						resPropMap.put("RES_URI_FOR_HTTPSERVER", CommonUtilities.convertToList(rSheetURI));
//						resPropList.add(resPropMap);
//						
//						List<List<Map<String, List<String>>>> returnPropList = resourceSheetManager.getResourceSheet(resPropList);
//						
//						if ( (returnPropList != null) && (returnPropList.size() != 0) ){
//							
//							List<Map<String, List<String>>> propList = returnPropList.get(0);
//							
//							if ( (propList != null) && (propList.size() != 0) ){
//								
//								Map<String, List<String>> returnPropMap = propList.get(0);
//								
//								if ( returnPropMap != null ){
//									
//									localRSheetURI = CommonUtilities.getFirstItem( returnPropMap.get("RES_LOCAL_AT") );
//									
//								}
//							}
//						}
//						
//					}
//				}
//		}
		
		
		
		// Put Resource Sheet URI in Resource Sheet Properties List.
		// Make a new entry in 'resourceSheetPropList' for this Resource Sheet.
		Map<String, List<String>> resSheetProps = new HashMap<String, List<String>>();
		
		List<String> rSheetURIList = new ArrayList<String>();
		rSheetURIList.add(localRSheetURI);
		resSheetProps.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, rSheetURIList);
		resSheetProps.put(Constants.PROPERTY_RES_NAME, CommonUtilities.convertToList(resName));
		
		resSheetProps.put(Constants.PROPERTY_FOR_DOMAIN, forDomainList);
		resSheetProps.put(Constants.PROPERTY_DC_ELEMENTS_TYPE, typeList);
		resSheetProps.put(Constants.PROPERTY_CONFORMS_TO, conformsToList);
		resSheetProps.put(Constants.PROPERTY_RES_FOR_LANG, forLangList);
		resSheetProps.put(Constants.PROPERTY_RES_ROLE, roleList);
		// 2012-09-14 : property repeated in the map, so below statement is commented.
		//resSheetProps.put(Constants.PROPERTY_RES_ROLE, roleList);
		
		resourceSheetManager.addToRresourceSheetPropList(resSheetProps);
	}
	
	
	
	
	
	/**
	 * Get a String value of Resource Sheet URI using the value of res:localAt and Target Description URI.
	 * If localAt is HTTP URL then download Resource Sheet from Resource Server and return local file path URI.
	 * If downloaded Resource Sheet is bundled in a zip then also unzip it. 
	 * 
	 * @param localAt a String value of res:localAt 
	 * @param tdUri a String value of Target Description URI
	 * 
	 * @return a String value of Resource Sheet URI
	 */
	private String getResourceSheetURI(String localAt, String tdUri) {
		
		if ( localAt == null )
			return null;
		//Parikshit Thakur : 20111021. Added one more condition below if localAt is a file URI.
		if ( (localAt.indexOf("http://") == -1) && (localAt.indexOf("https://") == -1) && (localAt.indexOf("file://") == -1) ) {
			
			if ( tdUri.indexOf("/") != -1 ) {
				
				return ( tdUri.substring(0,tdUri.lastIndexOf("/")+1) + localAt );
	
			} else {
				return null;
			}
			
		} else {
			
			return localAt;
		}
		
	}
	
	/**
	 * Get Map of Key-Value pair of HTTP GET URI String.
	 * 
	 * @param localAt a HTTP GET URI string
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, List<String>> getPropMap(String localAt) {
		
		int index = localAt.indexOf("?");
		
		if ( index == -1 )
			return null;
		else
			return CommonUtilities.prepareKeyValueListMap( localAt.substring(index+1) );
	}
	
	
	
	/**
	 * Ask ResourceSheetManager for Resource Sheet by providing rSheetURI.
	 * 
	 * @param resourceSheetManager a reference variable for ResourceSheetManager
	 * 
	 * @param rSheetURI a String to represent rSheetURI
	 * 
	 * @return boolean value to represent the correctness of operation
	 */
	private boolean isResSheetAvailableFromRSheetManager(ResourceSheetManager resourceSheetManager, String rSheetURI) {
		
		Map<String, List<String>> propMap = new HashMap<String, List<String>>();
		
		propMap.put(Constants.PROPERTY_RES_NAME, CommonUtilities.convertToList(rSheetURI) );
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		
		
		List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
		resProps.add(propMap);
		
		List<List<Map<String, List<String>>>> returnProps = resourceSheetManager.getResourceSheet(resProps);
		
		if ( (returnProps != null) && (returnProps.size() != 0) ){
			
			List<Map<String, List<String>>> propList = returnProps.get(0);
			
			if ( (propList != null) && (propList.size() != 0) ){
				
				Map<String, List<String>> returnPropMap = propList.get(0);
				
				if ( returnPropMap != null ){
					
					resourceSheetManager.addToRresourceSheetPropList(returnPropMap);
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check whether resource sheet at specified path exist or not.
	 * 
	 * @param resSheetPath a URI string
	 * 
	 * @return boolean value to represent completeness of operation.
	 */
	
	private boolean isResSheetExistsAtLocalPath(String resSheetPath){
		
		try {
			
			if(new java.io.File(new URI(resSheetPath)).exists()){
				return true;
			}
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException");
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * Resolve the specified URI, if it is resolvable the save the resource sheet
	 * with Target Description file.
	 * 
	 * @param tdUri String representation of Target Description URI.
	 * 
	 * @param retrievePath a retrieve path to resolve.
	 * 
	 * @return String representation of local rSheet path.
	 */
	private String getDocumentFromThisURI(String tdUri, String retrievePath){
		
		String localPath = null;
		
		URL url;
		try {
			url = new URL(retrievePath);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.setDoOutput(true);
			
			conn.connect();
			
			BufferedReader rd = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
			StringBuilder sb = new StringBuilder();
			String line;
			   
			while ((line = rd.readLine()) != null) 
				sb.append(line);
			   
			rd.close();

			String rSheetData =  sb.toString();
			
			if(rSheetData != null){

				localPath = tdUri.substring(0, tdUri.lastIndexOf("/") + 1)
						+ retrievePath.substring(retrievePath.lastIndexOf("/") + 1);

				File f = new File(new URI(localPath));

				PrintWriter out = new PrintWriter(f);

				out.println(rSheetData);

				out.close();
				
				return localPath;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.severe(" Error occured..  Can not resolve URI  : "+e.getMessage());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localPath;
		
	}
	
	
}
