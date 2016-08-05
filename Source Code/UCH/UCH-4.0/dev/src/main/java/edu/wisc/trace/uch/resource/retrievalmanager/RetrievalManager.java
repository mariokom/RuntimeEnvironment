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

package edu.wisc.trace.uch.resource.retrievalmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.ZipUtilities;

/**
 * Provide Method to Query and Retrieve Resources from Resource Server.
 * Also cache the Resources and its properties and provides methods to clear resources.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class RetrievalManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	//private static RetrievalManager retrievalManager;
	
	private static HttpRequestResponseUtil httpRequestResponseUtil;
	
	private Map<String, ResourceDetails> downloadedResourceMap;
	
	private String cacheDir;
	
	
	private static String COUNT_DEFAULT_VALUE_1 = "1";
	private static String START_DEFAULT_VALUE_1 = "1";
	

	/**
	 * Instantiate the Object of HttpRequestResponseUtil.
	 * 
	 * @param resServerUrl a String value of Resource Server URL
	 * @param cacheDir a String value of Cache directory path
	 * @param userName a String value of userName
	 * @param password a String value of password
	 */
	public RetrievalManager(String resServerUrl, String cacheDir, String userName, String password) {
		
		
		httpRequestResponseUtil = new HttpRequestResponseUtil(resServerUrl, cacheDir, userName, password);
		downloadedResourceMap = new HashMap<String, ResourceDetails>();
		this.cacheDir = cacheDir;
	}
	
	/**
	 * Get Specified Resources from Resource Server.
	 * 
	 * @param resPropList an Object of List&lt;Map&lt;String, String&gt;&gt;
	 * @param resServer a string value of res server url
	 * 
	 * @return an Object of List&lt;List&lt;Map&lt;String, Object&gt;&gt;&gt;
	 */
	public List<List<Map<String, List<String>>>> getResources(List<Map<String, List<String>>> resPropList, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer				
		if ( resPropList == null ) {
			logger.warning("Resource Property List is null.");
			return null;
		}
		
		if(resPropList.get(0).containsKey("RES_URI_FOR_HTTPSERVER")){
			
			return getResourceFromHttpServer(resPropList);
			
		}
		
		
		List<Boolean> resourceDownloadMap = new ArrayList<Boolean>();
		
		//String queriesResponse = sendQueriesRequest(resPropList, resourceDownloadMap, resServer); Yuvaraj.
		
		// Properties in the  user profile content and properties used for resources are not same, to find UIPM and UIPM Clients.
		// To make it generic we are replacing it with actual property at the time of querying the resource.
		
		String queriesResponse = sendQueriesRequest(ResourceUtil.cloneList(resPropList), resourceDownloadMap, resServer);
		
		logger.info("@@ QueriesResponse : "+queriesResponse);
		
		List<List<Map<String, List<String>>>> queriesReturnPropList = parseQueriesResponse(queriesResponse);
		
		if ( queriesReturnPropList == null ) {
			logger.warning("Queries Response is null.");
			return null;
		}
		
		if ( resourceDownloadMap.size() != queriesReturnPropList.size() ) {
			logger.warning("No. of requested resources and No. of responded resources are not same.");
			return null;
		}
		
		List<List<Map<String, List<String>>>> returnList = new ArrayList<List<Map<String, List<String>>>>();
		
		int i = 0;
		for ( List<Map<String, List<String>>> responseList : queriesReturnPropList ) {
			logger.info("In For Loop...!!!!");
			boolean downloadResource = resourceDownloadMap.get(i);
			i++;
			
			if ( responseList == null ) {
				returnList.add(null);
				continue;
			}
			
			List<Map<String, List<String>>> returnResponseList = new ArrayList<Map<String,List<String>>>();
			
			for ( Map<String, List<String>> resourceMap : responseList ) {
				
				if ( resourceMap == null ) {
					returnResponseList.add(null);
					continue;
				}
				
				Map<String, List<String>> returnResourceMap = new HashMap<String, List<String>>();
				
				returnResourceMap.putAll(resourceMap);
				
				if ( downloadResource ) { // Going to retrieve resouece from resource server.
					
					String globalAt = CommonUtilities.getFirstItem( resourceMap.get(Constants.PROP_NAME_GLOBAL_AT) );
					String fileName = CommonUtilities.getFirstItem( resourceMap.get(Constants.FILE_NAME) );
					logger.info("%^% FileName : "+fileName);
						
					if ( globalAt != null ) {
						
						String resourcePathUri = null;
						
						if ( httpRequestResponseUtil.isReady() )
							resourcePathUri = httpRequestResponseUtil.retrieveResource( CommonUtilities.decodeString(globalAt),fileName );
						
						logger.info("$$ => "+resourcePathUri);
						
						if ( resourcePathUri != null ) {
							
							List<String> resourcePathUriList = new ArrayList<String>();
							resourcePathUriList.add(resourcePathUri);
							returnResourceMap.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, resourcePathUriList );	
							
							processDownloadedResource(returnResourceMap);
							
							// .rprop file creation code 
							// If .rprop file is created for given resource then it returns true.Else false 
							if(generateRProp(resourceMap,resourcePathUriList ))	{
								
								logger.info("rprop file is created successfully for : "+resourcePathUri);
							}else{
								
								logger.info("Error in rprop file creation...");
							}// end of .rprop file creation code
						}
					}
					
					
				} else {
					
					List<String> valueList = new ArrayList<String>();	
					valueList.add("false");
					returnResourceMap.put(Constants.RESOURCE_QUERY_PROP_NAME_GET_CONTENT, valueList);
				}
					
				cacheDownloadedResource(returnResourceMap);
				returnResponseList.add(returnResourceMap);
			}
			
			returnList.add(returnResponseList);
		}
		
		return returnList;
	}
	
	/**
	 * Create rprop file using properties and write it to specific File.
	 * 
	 * @return a boolean value specifies rprop file is written successfully or not.
	 * 
	 * @param properties specifies the properties of the particular resource.
	 * @param rpropFileUriList specifies the list rProp file URI.
	 */
	private boolean generateRProp(Map<String, List<String>> properties,List<String> rpropFileUriList){
		if ( properties == null ) {
			logger.warning("Properties are null.");
			return false;
		}
		
		if ( rpropFileUriList == null ) {
			logger.warning("rprop file uri list is null.");
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(String rpropFileUri : rpropFileUriList){
			
			sb.append( Constants.CONSTANT_XML_HEADER);
			
			sb.append("<" + Constants.NODE_NAME_PROPS + ">" );
			
			for ( Entry<String, List<String>> entry : properties.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String name = entry.getKey();
				
				if ( name == null )
					continue;
				
				List<String> values = entry.getValue();
				
				if ( values == null )
					continue;
				
				for ( String value : values ) {
					
					if ( value == null )
						continue;
					
					sb.append("<" + Constants.NODE_NAME_PROP + ">" +
								"<" + Constants.NODE_NAME_NAME + ">" + xmlEncode(name) + "</" + Constants.NODE_NAME_NAME + ">" +
								"<" + Constants.NODE_NAME_VALUE + ">" + xmlEncode(value) + "</" + Constants.NODE_NAME_VALUE + ">" +
							  "</" + Constants.NODE_NAME_PROP + ">" );
				}
			}
			//set 'http://openurc.org/ns/res#resourceUri' property having value 'http://openurc.org/ns/res#filename' property value.
			
			if(properties.containsKey(Constants.FILE_NAME)){
				
				String fileName = properties.get(Constants.FILE_NAME).get(0);
				if(fileName!=null)
			sb.append("<" + Constants.NODE_NAME_PROP + ">" +
					"<" + Constants.NODE_NAME_NAME + ">" + xmlEncode(Constants.PROPERTY_RESOURCE_URI) + "</" + Constants.NODE_NAME_NAME + ">" +
					"<" + Constants.NODE_NAME_VALUE + ">" + xmlEncode(fileName) + "</" + Constants.NODE_NAME_VALUE + ">" +
				  "</" + Constants.NODE_NAME_PROP + ">" );
				
			}else{
				
				sb.append("<" + Constants.NODE_NAME_PROP + ">" +
						"<" + Constants.NODE_NAME_NAME + ">" + xmlEncode(Constants.PROPERTY_RESOURCE_URI) + "</" + Constants.NODE_NAME_NAME + ">" +
						"<" + Constants.NODE_NAME_VALUE + ">" + "retrievedResource" + "</" + Constants.NODE_NAME_VALUE + ">" +
					  "</" + Constants.NODE_NAME_PROP + ">" );
			}
			
			sb.append("</" + Constants.NODE_NAME_PROPS + ">" );
			if(!writeRPropFile( sb.toString() ,rpropFileUri))
				return false;
		}
		return true;
	}

	/**
	 * XML encode specified String.
	 * 
	 * @param in a String value
	 * 
	 * @return an xml encoded string value
	 */
	private String xmlEncode(String in) {
		
		if ( in == null )
			return null;
		
		in = in.replace("&", "&amp;");
		in = in.replace("<", "&lt;");
		in = in.replace(">", "&gt;");
		in = in.replace("'", "&apos;");
		in = in.replace("\"", "&quot;");
		
		return in;
	}
	
	/**
	 * Write file data in .rprop(UCH config file) XML file .
	 * 
	 * @param fileData a String value specifies file data
	 * @param filePathUri a String value specifies file URI
	 * 
	 * @return a boolean value specifies whether rprop file written successfully
	 * 
	 */
	private boolean writeRPropFile(String fileData,String filePathUri){
		
		if ( fileData == null ) {
			logger.warning("File data is null.");
			return false;
		}
		
		try {
			FileWriter fw = null;
			// Prikshit Thakur : 20110825. changed file extension from .ucf to .rprop.
			filePathUri +=".rprop";
			logger.info("$$$ file path uri : "+filePathUri);
			
			File destinationFile = new File( new URI(filePathUri));
			logger.info("#$# File is created.");
			fw = new FileWriter(destinationFile);
			
			
			fw.write( fileData );
			fw.flush();
			fw.close();
			
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("IOException: Writing rprop file "+filePathUri);
			return false;
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException: Writing rprop file "+filePathUri);
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * Process the downloaded Resource for further utilize.
	 * For example unzip the zip file.
	 * 
	 * @param resourceProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private void processDownloadedResource(Map<String, List<String>> resourceProps) {
		
		if ( resourceProps == null )
			return;
		
		String resourceLocalAt = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
		if ( resourceLocalAt == null )
			return;
		
		String mimeType = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROPERTY_MIME_TYPE) );
		 
		logger.info("@@ MimeType : "+mimeType);
		
		if ( mimeType != null ) {
			
			if ( mimeType.equals(Constants.PROPERTY_MIME_TYPE_VALUE_ZIP) ) { // If mimeType is zip then unzip it.
				// Use new zip Utility
				String zipFileUriString = resourceLocalAt;
				
				String outDirUriString = null;
				
				if ( resourceLocalAt.indexOf('/') != -1 ) {
					outDirUriString = resourceLocalAt.substring(0, resourceLocalAt.lastIndexOf('/') );
					
				} else if ( resourceLocalAt.indexOf('\\') != -1 ) {
					outDirUriString = resourceLocalAt.substring(0, resourceLocalAt.lastIndexOf('\\') );
					
				} else {
					
					logger.warning("'resourceLocalAt' is not proper.");
					return;
				}
				
				URI zipFileUri = null;
				
				try {
					zipFileUri = new URI( zipFileUriString.replaceAll(" ", "%20") );
				} catch (URISyntaxException e) {
					logger.warning("URISyntaxException");
					logger.warning("'"+zipFileUri+"' is not in proper format of URI.");
					return;
				}
				
				URI outDirUri = null;
				
				try {
					outDirUri = new URI( outDirUriString.replaceAll(" ", "%20") );
				} catch (URISyntaxException e) {
					logger.warning("URISyntaxException");
					logger.warning("'"+outDirUri+"' is not in proper format of URI.");
					return;
				}
				
				if ( (zipFileUri == null) || (outDirUri == null) )
					return;
				
				File outDir = new File(outDirUri);
				
				if ( outDir.exists() && (outDir.list().length > 1) ) {	// Not need to unzip already unzipped file.		
					logger.info("No need to unzip file '"+outDir.getAbsolutePath()+"'. It is already unzipped.");
					
				} else {
				
					if ( !ZipUtilities.unzip(zipFileUri, outDirUri) ) {
						logger.warning("Unable to unzip the zip file '"+zipFileUri+"'.");
						return;
					}
				}
				
				String indexFile = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROPERTY_INDEX_FILE) );

				logger.info("@@@ indexFile name : "+indexFile);
				
				if ( indexFile != null ) {
					resourceLocalAt = resourceLocalAt.substring(0, resourceLocalAt.lastIndexOf("/")+1) + indexFile;
					List<String> resourceLocalAtValueList = new ArrayList<String>();
					resourceLocalAtValueList.add(resourceLocalAt);
					resourceProps.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, resourceLocalAtValueList);					
				}
			}
		}
	}
	
	/**
	 * Get Specified Resource from Resource Server.
	 * 
	 * @param resPropMap an Object of Map&lt;String, String&gt;
	 * @return an Object of Map&lt;String, Object&gt;
	 */
	public Map<String, List<String>> getResource(Map<String, List<String>> resPropMap) {
		logger.info("@!@ in getResource...");
		showMap(resPropMap);
		if ( resPropMap == null ) {
			logger.warning("Resource Property Map is null.");
			return null;
		}
		
		List<Map<String, List<String>>> resPropList = new ArrayList<Map<String,List<String>>>();
		
		resPropList.add(resPropMap);
		
		List<List<Map<String, List<String>>>> returnedList = getResources(resPropList, null);
		
		if ( (returnedList == null) || (returnedList.size() <= 0) ) {
			logger.warning("Returned Properties List is null.");
			return null;
		}
		
		List<Map<String, List<String>>> returnedFirstList = returnedList.get(0);
		
		if ( (returnedFirstList == null) || (returnedFirstList.size() <= 0) ) {
			logger.warning("Returned Properties List is empty.");
			return null;
		}
		
		return returnedFirstList.get(0);
	}
	
	
	/**
	 * Retrieve the specified Resource from Resource Server.
	 * 
	 * @param retrievalUri a String value of Resource Retrieval URI
	 * 
	 * @return a URI String specifying the local path or Resource
	 */
	public Map<String, List<String>> retrieveResource(String retrievalUri) {
		
		Map<String, List<String>> propMap = getPropMap(retrievalUri);
		
		if ( (propMap == null) || (propMap.size() <= 0) ) {
			return null;
		}
		
		String name = CommonUtilities.getFirstItem( propMap.get("name") );
		
		if( name == null ) {
			logger.warning("Unable to get 'name' from retrieval Uri '"+retrievalUri+"'.");
			return null;
		}
		
		ResourceDetails resourceDetails = null;
		
		synchronized (downloadedResourceMap) {
			
			resourceDetails = downloadedResourceMap.get(name);
		}
		
		if ( resourceDetails == null )// Get resource - first query and retrieve 
			return getResource(propMap);
		
		Map<String, List<String>> cachePropMap = resourceDetails.getProperties();
		
		if ( cachePropMap == null )// Get resource - first query and retrieve
			return getResource(propMap);
		
		if ( !resourceDetails.isDownloaded() ) {
			
			String resourceLocalAt = null;
			
			if ( httpRequestResponseUtil.isReady() )
				resourceLocalAt = httpRequestResponseUtil.retrieveResource( resourceDetails.getGlobalAt() , "FileName");
			
			if ( resourceLocalAt == null ) {
				logger.warning("Unable to retrieve resource '"+resourceDetails.getGlobalAt()+"'.");
				return null;
			}
			
			List<String> resourceLocalAtValueList = new ArrayList<String>();
			resourceLocalAtValueList.add(resourceLocalAt);
			cachePropMap.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, resourceLocalAtValueList);
			
			processDownloadedResource(cachePropMap);
			
			resourceDetails.setResourceLocalAt(resourceLocalAt);
			resourceDetails.setDownloaded(true);		
		}
	
		return cachePropMap;
	}
	
	/**
	 * Retriever the specified Resource.
	 * 
	 * @param retrievalUri a String value of Retrieval URL
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> getPropMap(String retrievalUri) {
		
		if ( retrievalUri == null )
			return null;
		
		if( retrievalUri.indexOf("?") == -1 )
			return null;
		
		return prepareKeyValueMap( retrievalUri.substring(retrievalUri.indexOf("?")+1) );	
	}
	
	/**
	 * get key-value Map by parsing specified
	 * 
	 * @param str a String value
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> prepareKeyValueMap(String str) {
		
		if ( str == null )
			return null;
		
		if ( str.indexOf("=") == -1 ) 
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		while ( str.indexOf("&") != -1 ) {
			
			String key = str.substring(0, str.indexOf("=")).trim();
			String value = str.substring(str.indexOf("=")+1, str.indexOf("&")).trim();	
			
			if ( returnMap.containsKey(key) ) {
				
				List<String> valueList = returnMap.get(key);
				valueList.add(value);
				
			} else {
				
				List<String> valueList = new ArrayList<String>();
				valueList.add(value);
				returnMap.put(key, valueList);
			}
			
			
			str = str.substring(str.indexOf("&")+1);
		}
		
		String key = str.substring(0, str.indexOf("=")).trim();
		String value = str.substring(str.indexOf("=")+1).trim();
		
		if ( returnMap.containsKey(key) ) {
			
			List<String> valueList = returnMap.get(key);
			valueList.add(value);
			
		} else {
			
			List<String> valueList = new ArrayList<String>();
			valueList.add(value);
			returnMap.put(key, valueList);
		}
		
		return returnMap;
	}

	/**
	 * Prepare &lt;Queries&gt; request according to Resource Server Spec and send Query Request on ResourceServer.
	 * Also return response as a String.
	 * 
	 * @param resPropList an Object of List&lt;Map&lt;String, String&gt;&gt;
	 * @param resServer a String value of res server url
	 * 
	 * @return a String value of query response.
	 */
	private String sendQueriesRequest(List<Map<String, List<String>>> resPropList, List<Boolean> resourceDownloadMap, String resServer) {
		// Parikshit Thakur : 20111119. Added parameter resServer
		String queryRequestBody = prepareQueriesRequestBody(resPropList, resourceDownloadMap);
		//logger.info("Request Body : "+queryRequestBody);
		
		if ( httpRequestResponseUtil.isReady() )
			return httpRequestResponseUtil.queryResource(queryRequestBody, resServer);
		else
			return null;
	}
	
	/**
	 * Parse the resource query response and prepare a ResourcesMapList.
	 * 
	 * @param queriesResponse a String value of Resource Query Response
	 * 
	 * @return an Object of List&lt;List&lt;Map&lt;String, String&gt;&gt;&gt;
	 */
	private List<List<Map<String, List<String>>>> parseQueriesResponse(String queriesResponse) {
		
		if ( queriesResponse == null ) {
			logger.warning("Queries Response is null.");
			return null;
		}
				
		Document doc = null;
		
		try {
			doc = CommonUtilities.parseXml(queriesResponse);
		} catch (NullPointerException e) {
			logger.info("NullPointerException");
			//e.printStackTrace();
		} catch (SAXException e) {
			logger.info("SAXException");
			//e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException");
			//e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.info("ParserConfigurationException");
			//e.printStackTrace();
		}
		
		if ( doc == null ) {
			logger.warning("Query Response is not in proper XML format.");
			return null;
		}
		
		Element root = doc.getDocumentElement();
		
		if ( (root == null) || !("responses").equals( root.getNodeName() ) ) {
			logger.warning("Response has no rootNode named 'responses'.");
			return null;
		}
		
		List<List<Map<String, List<String>>>> returnList = new ArrayList<List<Map<String,List<String>>>>();
		
		NodeList responseNodeList = root.getChildNodes();
		
		if ( responseNodeList == null ) {
			logger.warning("'<responses>' node has no child node.");
			return null;
		}
		
		for ( int i=0 ; i<responseNodeList.getLength() ; i++ ) {
			
			Node responseNode = responseNodeList.item(i);
			
			String nodeName = responseNode.getNodeName();
			
			if ( (nodeName == null) || !nodeName.equals("response") )
				continue;
			
			returnList.add( parseResponseNode(responseNode) );
		}
		
		return returnList;	
	}
	
	/**
	 * Parse the Response Node and return ResourceMapList.
	 * 
	 * @param responseNode an Object of node
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	private List<Map<String, List<String>>> parseResponseNode(Node responseNode) {
		
		if ( responseNode == null || !(responseNode instanceof Element) ) {
			logger.warning("Response Node is null.");
			return null;
		}
		
		NodeList resourceNodeList = responseNode.getChildNodes();
		
		if ( resourceNodeList == null ) {
			logger.warning("'<response>' node has no child node.");
			return null;
		}

		Element responseElement = (Element)responseNode;
		
		String referenceId = responseElement.getAttribute(Constants.RESOURCE_QUERY_PROP_NAME_REFERENCEID);
		
		List<String> referenceIdValueList = null;
		if ( (referenceId != null) && !referenceId.trim().equals("") ) {
			
			referenceIdValueList = new ArrayList<String>();
			referenceIdValueList.add(referenceId);	
		}
		
		
		String start = responseElement.getAttribute(Constants.RESOURCE_QUERY_PROP_NAME_START);
		List<String> startValueList = null;
		if ( (start != null) && !start.trim().equals("") ) {
			
			startValueList = new ArrayList<String>();
			startValueList.add(start);	
		}
		
		
		String count = responseElement.getAttribute(Constants.RESOURCE_QUERY_PROP_NAME_COUNT);
		List<String> countValueList = null;
		if ( (count != null) && !count.trim().equals("") ) {
			
			countValueList = new ArrayList<String>();
			countValueList.add(count);	
		}

		
		String total = responseElement.getAttribute(Constants.RESOURCE_QUERY_PROP_NAME_TOTAL);
		List<String> totalValueList = null;
		if ( (total != null) && !total.trim().equals("") ) {
			
			totalValueList = new ArrayList<String>();
			totalValueList.add(total);	
		}

		List<Map<String, List<String>>> returnList = new ArrayList<Map<String,List<String>>>();
		
		for ( int i=0 ; i<resourceNodeList.getLength() ; i++ ) {
			
			Node resourceNode = resourceNodeList.item(i);
			
			String nodeName = resourceNode.getNodeName();
			
			if ( (nodeName == null) || !nodeName.equals("resource") )
				continue;
			
			Map<String, List<String>> returnedProps = parseResourceNode(resourceNode);
			
			if ( referenceIdValueList != null )
				returnedProps.put(Constants.RESOURCE_QUERY_PROP_NAME_REFERENCEID, referenceIdValueList);
			
			if ( startValueList != null )
				returnedProps.put(Constants.RESOURCE_QUERY_PROP_NAME_START, startValueList);
			
			if ( countValueList != null )
				returnedProps.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, countValueList);
			
			if ( totalValueList != null )
				returnedProps.put(Constants.RESOURCE_QUERY_PROP_NAME_TOTAL, totalValueList);
			
			returnList.add(returnedProps);
		}
		
		return returnList;
	}
	
	/**
	 * Parse the Resource Node and return ResourceMap.
	 * 
	 * @param resourceNode an Object of Node
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> parseResourceNode(Node resourceNode) {
		
		if ( (resourceNode == null) || !(resourceNode instanceof Element) ) {
			logger.warning("Resource Node is null.");
			return null;
		}
		
		NodeList resourceNodeList = resourceNode.getChildNodes();
		
		if ( resourceNodeList == null ) {
			logger.warning("'<resource>' node has no child node.");
			return null;
		}
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		Element resourceElement = (Element)resourceNode;
		
		String index = resourceElement.getAttribute(Constants.RESOURCE_QUERY_PROP_NAME_INDEX);
		
		if ( (index != null) && !index.trim().equals("") ) {
			
			List<String> indexValueList = new ArrayList<String>();
			indexValueList.add(index);
			returnMap.put(Constants.RESOURCE_QUERY_PROP_NAME_INDEX, indexValueList);
		}
		
		String globalAt = null;
		
		for ( int i=0 ; i<resourceNodeList.getLength() ; i++ ) {
			
			Node resourceChildNode = resourceNodeList.item(i);
			
			String resourceChildNodeName = resourceChildNode.getNodeName();
			
			if ( (resourceChildNodeName == null) || (resourceChildNodeName.equals("#text")) || !(resourceChildNode instanceof Element) )
				continue;
			
			if ( resourceChildNodeName.equals("prop") ) {
				
				String propName = ((Element)resourceChildNode).getAttribute("name");
				String propValue = ((Element)resourceChildNode).getAttribute("val");
				
				if ( (propName == null) || (propValue == null) ) 
					continue;
				
				propName = propName.trim();
				propValue = propValue.trim();
				
				if ( returnMap.containsKey(propName) ) {
					
					List<String> propValueList = returnMap.get(propName);
					propValueList.add(propValue);
					
				} else {
					
					List<String> propValueList = new ArrayList<String>();
					propValueList.add(propValue);
					returnMap.put(propName, propValueList);
				}
				
			} else if ( resourceChildNodeName.equals(Constants.PROP_NAME_GLOBAL_AT) && resourceChildNode.hasChildNodes() && (resourceChildNode.getChildNodes().getLength() == 1) ) {
				
				globalAt = resourceChildNode.getFirstChild().getNodeValue();
			}
		}
		
		if ( globalAt == null ) {
			logger.warning("Unable to get 'GlobalAt' from Query Response.");
			return null;
		}
		
		List<String> globalAtValueList = new ArrayList<String>();
		globalAtValueList.add(globalAt);
		returnMap.put(Constants.PROP_NAME_GLOBAL_AT, globalAtValueList);
		
		return returnMap;
	}
	
	
	/**
	 * Prepare the query String from specified resPropList and also make entry in the resourceDownloadList.
	 * 
	 * @param resPropList an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * @param resourceDownloadList an Object of List&lt;Boolean&gt;
	 * 
	 * @return a String value of Resource Query Request Body
	 */
	private String prepareQueriesRequestBody(List<Map<String, List<String>>> resPropList, List<Boolean> resourceDownloadList) {
		
		if ( (resPropList == null) || (resPropList.size() <= 0) ) {
			logger.warning("resPropList is null.");
			return null;
		}
		
		StringBuilder queries = new StringBuilder();
		
		queries.append("<queries>");
		
		
		for(Map<String, List<String>> propMap : resPropList ) {
			
			if ( propMap == null )
				continue;
			
			if ( propMap.containsKey(Constants.RESOURCE_QUERY_PROP_NAME_GET_CONTENT) ) {
				
				String getContent = CommonUtilities.getFirstItem( propMap.get(Constants.RESOURCE_QUERY_PROP_NAME_GET_CONTENT) );
				propMap.remove(Constants.RESOURCE_QUERY_PROP_NAME_GET_CONTENT);
				
				if ( (getContent != null) && getContent.trim().toLowerCase().equals("false") ) 
					resourceDownloadList.add(false);
				else 
					resourceDownloadList.add(true);
				
			} else {
				
				resourceDownloadList.add(true);
			}
			
			String referenceId = null;
			if ( propMap.containsKey(Constants.RESOURCE_QUERY_PROP_NAME_REFERENCEID) ) {
				
				referenceId = CommonUtilities.getFirstItem( propMap.get(Constants.RESOURCE_QUERY_PROP_NAME_REFERENCEID) );
				propMap.remove(Constants.RESOURCE_QUERY_PROP_NAME_REFERENCEID);
			}
			
			String start = null;
			if ( propMap.containsKey(Constants.RESOURCE_QUERY_PROP_NAME_START) ) {
				
				start = CommonUtilities.getFirstItem( propMap.get(Constants.RESOURCE_QUERY_PROP_NAME_START) );
				propMap.remove(Constants.RESOURCE_QUERY_PROP_NAME_START);
			}
			
			String count = null;
			if ( propMap.containsKey(Constants.RESOURCE_QUERY_PROP_NAME_COUNT) ) {
				
				count = CommonUtilities.getFirstItem( propMap.get(Constants.RESOURCE_QUERY_PROP_NAME_COUNT) );
				propMap.remove(Constants.RESOURCE_QUERY_PROP_NAME_COUNT);
			}
			
			if ( referenceId == null ) {
				
				if ( (start == null) && (count == null) ) {
					
					queries.append("<query>");
					
				} else {
					
					if ( start == null )
						queries.append("<query start=\""+START_DEFAULT_VALUE_1+"\" count=\""+count+"\" >");
					else if ( count == null ) 
						queries.append("<query start=\""+start+"\" count=\""+COUNT_DEFAULT_VALUE_1+"\" >");
					else
						queries.append("<query start=\""+start+"\" count=\""+count+"\" >");
				}
				
			} else {
				
				if ( (start == null) && (count == null) )
					queries.append("<query ref=\""+CommonUtilities.encodeString(referenceId)+"\" start=\""+START_DEFAULT_VALUE_1+"\" count=\""+COUNT_DEFAULT_VALUE_1+"\" >");
				else if ( start == null )
					queries.append("<query ref=\""+CommonUtilities.encodeString(referenceId)+"\" start=\""+START_DEFAULT_VALUE_1+"\" count=\""+count+"\" >");
				else if ( count == null )
					queries.append("<query ref=\""+CommonUtilities.encodeString(referenceId)+"\" start=\""+start+"\" count=\""+COUNT_DEFAULT_VALUE_1+"\" >");
				else
					queries.append("<query ref=\""+CommonUtilities.encodeString(referenceId)+"\" start=\""+start+"\" count=\""+count+"\" >");
			}
			
			for ( String key : propMap.keySet() ) {
				
				List<String> valueList = propMap.get(key);
				
				if ( (key == null) || (valueList == null) )
					continue;
				
				for ( String value : valueList ) {
					
					if ( value != null )
						queries.append("<prop name=\""+CommonUtilities.encodeString(key.trim())+"\" val=\""+CommonUtilities.encodeString(value.trim())+"\" wgt=\"1\" />");
				}
				
			}
			
			queries.append("</query>");
		}
		
		queries.append("</queries>");
		
		return queries.toString();
	}
	
	
	/**
	 * Try to delete all files in cache directory and return those fileUri which can't be deleted because they are used by other resource. 
	 *
	 * @return an Object of List&lt;String&gt; containing not deleted file.
	 */
	public List<String> clearCache() {
		
		if ( httpRequestResponseUtil != null ) {
			
			List<String> deletedUris = httpRequestResponseUtil.clearCache();
			
			if ( deletedUris == null )
				return new ArrayList<String>();
			
			synchronized (downloadedResourceMap) {
				
				downloadedResourceMap.clear();
				/*
				List<String> resourceNamesToDelete = new ArrayList<String>();
				
				for ( String resourceName : downloadedResourceMap.keySet() ) {
					
					ResourceDetails reosurceDetails = downloadedResourceMap.get(resourceName);
					
					if ( reosurceDetails.isDownloaded() 
					  && deletedUris.contains(reosurceDetails.getResourceLocalAt()) ) {
						
						resourceNamesToDelete.add(resourceName);	
					}
				}
				
				for ( String resourceName : resourceNamesToDelete ) {
					
					downloadedResourceMap.remove(resourceName);
				}
				*/
			}

			return deletedUris;
			
		} else { 
			
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Clear Resource Sheet Cache and return deleted resource paths.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> clearResourceSheetCache() {
		
		return clearByResourceTypeFromCache(Constants.PROPERTY_RES_TYPE_VALUE_RESSHEET);
	}
	
	/**
	 * Clear UIPM Client Cache and return deleted resource paths.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> clearUipmClientCache() {
		
		return clearByResourceTypeFromCache(Constants.PROPERTY_RES_TYPE_VALUE_UIPM_CLIENT);
	}
	
	/**
	 * Clear specified type or resources from cache and return deleted resource paths.
	 * 
	 * @param resourceType a String value of Resource Type
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> clearCache(String resourceType) {
		
		return clearByResourceTypeFromCache(resourceType);
	}
	
	/**
	 * Clear specified type or resources from cache and return deleted resource paths.
	 * 
	 * @param resourceType a String value of Resource Type
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	private List<String> clearByResourceTypeFromCache(String resourceType) {
		
		if ( resourceType == null )
			return new ArrayList<String>();
		
		if ( httpRequestResponseUtil != null ) {
			
			List<String> localPathList = new ArrayList<String>();
			
			synchronized (downloadedResourceMap) {
				
				for ( String resourceName : downloadedResourceMap.keySet() ) {
					
					ResourceDetails reosurceDetails = downloadedResourceMap.get(resourceName);
					
					if ( reosurceDetails.isDownloaded() 
					  && resourceType.equals(reosurceDetails.getResourceType()) ) {
						
						localPathList.add( reosurceDetails.getResourceLocalAt() );	
					}
				}
			}
			
			List<String> deletedUris = httpRequestResponseUtil.clearCache(localPathList);
			
			if ( deletedUris == null )
				return new ArrayList<String>();
			
			synchronized (downloadedResourceMap) {
				
				List<String> resourceNamesToDelete = new ArrayList<String>();
				
				for ( String resourceName : downloadedResourceMap.keySet() ) {
					
					ResourceDetails reosurceDetails = downloadedResourceMap.get(resourceName);
					
					if ( resourceType.equals(reosurceDetails.getResourceType()) ) {
						
						resourceNamesToDelete.add(resourceName);	
					}
					/*
					if ( reosurceDetails.isDownloaded() 
					  && resourceType.equals(reosurceDetails.getResourceType()) 
					  && deletedUris.contains(reosurceDetails.getResourceLocalAt()) ) {
						
						resourceNamesToDelete.add(resourceName);	
					}
					*/
				}
				
				for ( String resourceName : resourceNamesToDelete ) {
					
					downloadedResourceMap.remove(resourceName);
				}
			}
			
			return deletedUris;
			
		} else {
			
			return new ArrayList<String>();
		}
		
	}
	
	/**
	 * Cache the specified resource.
	 * 
	 * @param resourceProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private void cacheDownloadedResource(Map<String, List<String>> resourceProps) {
		

		if ( resourceProps == null ) {
			logger.warning("Downloaded Resource Properties is null.");
			return;
		}
		
		String name = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROPERTY_RES_NAME) );
		
		if ( name == null ) {
			logger.warning("Resource Properties has no property named '"+Constants.PROPERTY_RES_NAME+"'.");
			return;
		}
		
		String resourceType = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROPERTY_RES_TYPE) );
		
		if ( resourceType == null ) {
			logger.warning("Resource Properties has no property named '"+Constants.PROPERTY_RES_TYPE+"'.");
			return;
		}
		
		String globalAt = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROP_NAME_GLOBAL_AT) );
		
		if ( globalAt == null ) {
			logger.warning("Resource Properties has no property named '"+Constants.PROP_NAME_GLOBAL_AT+"'.");
			return;
		}
		
		String modifiedAt = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROPERTY_DC_TERMS_MODIFIED) );
		
		if ( modifiedAt == null ) {
			logger.warning("Resource Properties has no property named '"+Constants.PROPERTY_DC_TERMS_MODIFIED+"'.");
			return;
		}
		
		String resourceLocalAt = CommonUtilities.getFirstItem( resourceProps.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		logger.info("@@ Resource Local AT : cacheDownloadResource = "+resourceLocalAt);
		
		ResourceDetails resourceDetails = new ResourceDetails();
		
		resourceDetails.setProperties(resourceProps);
		resourceDetails.setGlobalAt(globalAt);
		resourceDetails.setModifiedAt(modifiedAt);
		resourceDetails.setResourceType(resourceType);
		
		if ( resourceLocalAt == null ) {
			
			resourceDetails.setDownloaded(false);
		} else {	
			
			resourceDetails.setResourceLocalAt(resourceLocalAt);
			resourceDetails.setDownloaded(true);
		}
		
		synchronized (downloadedResourceMap) {
			
			if ( downloadedResourceMap.containsKey(name) ) {
				
				ResourceDetails cacheResourceDetails = downloadedResourceMap.get(name); 
				
				if ( modifiedAt.compareTo(cacheResourceDetails.getModifiedAt()) > 0 ) {
					
					downloadedResourceMap.put(name, resourceDetails);
					
				} else if ( !cacheResourceDetails.isDownloaded() && (resourceLocalAt != null) ) {
					
					cacheResourceDetails.setDownloaded(true);
					cacheResourceDetails.setResourceLocalAt(resourceLocalAt);
				}
				
			} else {
				
				downloadedResourceMap.put(name, resourceDetails);
			}
		}
	}
	
	// utility method to see the map.
	void showMap(Map<String,List<String>> resourceProps)
	{
		Set<String> keys = resourceProps.keySet();
		for(String key:keys)
		{
			System.out.print(key +" : ");
			for(String val:resourceProps.get(key))
				logger.info(val+",");
		}
	}
	
	/**
	 * Calls same method of HttpRequestResponseUtil
	 * @param resServerPath
	 */
	public void setResServerPath(String resServerPath){
		httpRequestResponseUtil.setResServerPath(resServerPath);
	}

	
	/**
	 * Gets the resource from external http server.
	 * @param resPropList p[roperties of resource.
	 * @return properties of downloaded resource.
	 */
	public List<List<Map<String, List<String>>>> getResourceFromHttpServer(List<Map<String, List<String>>> resPropList) {
		
		String localResURI ;
		Map<String, List<String>> resProps = resPropList.get(0);
		String resURI = CommonUtilities.getFirstItem(resProps.get("RES_URI_FOR_HTTPSERVER"));
		
			try {
				URL url = new URL(resURI);
				InputStream inputStream = url.openStream();
				FileOutputStream fileOutputStream = new FileOutputStream(cacheDir.substring(8) + "/" + resURI.substring(resURI.lastIndexOf("/")));
				int ch;
				while ((ch = inputStream.read()) != -1)
				{
				   fileOutputStream.write(ch);
				}
				 
				fileOutputStream.close();
				inputStream.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			localResURI = cacheDir + "/"+resURI.substring(resURI.lastIndexOf("/"));
			
			List<Map<String, List<String>>> returnPropList = new ArrayList<Map<String,List<String>>>();
			Map<String, List<String>> returnProps = new HashMap<String, List<String>>();
			returnProps.put("RES_LOCAL_AT", CommonUtilities.convertToList(localResURI));
			returnPropList.add(returnProps);
			List<List<Map<String, List<String>>>> returnList = new ArrayList<List<Map<String,List<String>>>>();
			returnList.add(returnPropList);
		
		return returnList;
	}
	
	
}
