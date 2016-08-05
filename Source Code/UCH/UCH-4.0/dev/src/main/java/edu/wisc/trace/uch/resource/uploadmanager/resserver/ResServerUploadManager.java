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


package edu.wisc.trace.uch.resource.uploadmanager.resserver;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.soap.encoding.soapenc.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.util.MyHostnameVerifier;
import edu.wisc.trace.uch.resource.util.MyTrustManager;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide Method to Upload Resources to Resource Server.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class ResServerUploadManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String CONSTANT_VALUE_UPLOAD = "upload";
	
	private static final String NODE_NAME_REQUEST = "request";
	private static final String NODE_NAME_RESPONSE = "response";
	
	private static final String NODE_NAME_RESOURCE = "resource";
	private static final String NODE_NAME_RIGHTS = "rights";
	private static final String NODE_NAME_OWNER = "owner";
	private static final String NODE_NAME_GROUP = "group";
	private static final String NODE_NAME_OTHER = "other";
	private static final String NODE_NAME_OWNERS = "owners";
	private static final String NODE_NAME_GROUPS = "groups";
	private static final String NODE_NAME_PROPS = "props";
	private static final String NODE_NAME_PROP = "prop";
	private static final String NODE_NAME_RESOURCE_DATA = "resourceData";
	private static final String NODE_NAME_NAME = "name";
	private static final String NODE_NAME_DATA = "data";
	private static final String NODE_NAME_GLOBAL_AT = "globalAt";

	
	private static final String ATTRIBUTE_NAME_READ = "read";
	private static final String ATTRIBUTE_NAME_WRITE = "write";
	private static final String ATTRIBUTE_NAME_QUERY = "query";
	private static final String ATTRIBUTE_NAME_RETRIEVE = "retrieve";
	private static final String ATTRIBUTE_NAME_NAME = "name";
	private static final String ATTRIBUTE_NAME_VAL = "val";
	private static final String ATTRIBUTE_NAME_INHERIT = "inherit";
	private static final String ATTRIBUTE_NAME_STATUS = "status";
	
	private static final String ATTRIBUTE_NAME_INHERIT_VALUE_TRUE = "true";
	
	private static final String CONSTANT_VALUE_RESOURCE_NAME = "resourceName";
	
	
	private static final String REQUEST_HEADER_AUTHORIZATION = "Authorization";
	private static final String REQUEST_HEADER_AUTHORIZATION_VALUE_BASIC = "Basic";
	
	private static final String REQUEST_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String REQUEST_HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
	
	private static final String REQUEST_METHOD_POST = "POST";
	
	private static final String XML_FILE_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private static final int BUFFER_SIZE = 1024;
	
	private URL url;
	private String userName;
	private String password;
	private boolean ready;

	
	/**
	 * Store Resource Server URI, UserName and Password for further upload Request.
	 * 
	 * @param resServerUrl a String value of Resource Server URI
	 * @param userName a String value of Resource Server UserName
	 * @param password a String value of Resource Server Password
	 */
	public ResServerUploadManager(String resServerUrl, String userName, String password) {
		
		
		if ( resServerUrl == null ) {
			logger.severe("Resource Server URL is null.");
			return;
		}
		
		if ( userName == null ) {
			logger.severe("User Name is null.");
			return;
		}
		
		if ( password == null ) {
			logger.severe("Password is null.");
			return;
		}
		
		resServerUrl = prepareResServerURL(resServerUrl, userName, password);
		
		try {
			url = new URL(resServerUrl);
		} catch (MalformedURLException e) {
			logger.severe("MalformedURLException : Initialization Failed of UploadManager. URL :"+resServerUrl);
			return;
		}
	
		this.userName = userName;
		this.password = password;
		
		setSSLSettings();
		
		ready = true;
	}
	
	/**
	 * Format specified resServerUrl path properly and return it.
	 * 
	 * @param resServerUrl a String value of Resource Server URL
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return a String value of Resource Server URL
	 */
	private String prepareResServerURL(String resServerUrl, String userName, String password) {
		
		if ( resServerUrl == null )
			return null;
		
		resServerUrl = resServerUrl.trim();
		
		if ( resServerUrl.endsWith("/") )
			resServerUrl +=  CONSTANT_VALUE_UPLOAD;
		else
			resServerUrl +=  "/" + CONSTANT_VALUE_UPLOAD;
		
		if ( resServerUrl.startsWith("http://") )
			resServerUrl = "https:" + resServerUrl.substring(5);
		else if ( !resServerUrl.startsWith("https://") ) 
			resServerUrl = "https://" + resServerUrl;
		
		return resServerUrl;
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
	private List<Map<String, String>> uploadResources(List<Map<String, List<String>>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, List<String> resourceUri) {
		
		if ( (props == null) || (resourceUri == null) || (props.size() != resourceUri.size()) )
			return null;
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		for ( int i=0 ; i<props.size() ; i++ ) {
			
			returnList.add( uploadResource(props.get(i), owners, groups, rights, resourceUri.get(i)) );
		}
		
		return returnList;
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
	public synchronized Map<String, String> uploadResource(Map<String, List<String>> props, List<String> owners, List<String> groups, Map<String, List<String>> rights, String resourceUri) {
		
		if ( !ready ) {
			logger.warning("Upload Manager is unable to upload resource due to invalid URL, userName or password.");
			return null;
		}
		
		if ( props == null ) {
			logger.warning("Resources Properties is null.");
			return null;
		}
		
		if ( resourceUri == null ) {
			logger.warning("Resource Local URI is null.");
			return null;
		}
		
		if ( !isPropertyExists(Constants.PROPERTY_RES_TYPE, props) ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RES_TYPE+"' or its value is invalid.");
			return null;
		}
		
		int slashIndex = resourceUri.lastIndexOf('/');
		int backSlashIndex = resourceUri.lastIndexOf('\\');
		
		if ( (slashIndex == -1) && (backSlashIndex == -1) ) {
			logger.warning("Resource File Uri '"+resourceUri+"' is invalid.");
			return null;
		}
		
		String resourceFileName = null;
		
		if ( slashIndex > backSlashIndex )
			resourceFileName = resourceUri.substring( slashIndex+1 );
		else
			resourceFileName = resourceUri.substring( backSlashIndex+1 );
		
		URI fileURI = null;
		try {
			fileURI = new URI(resourceUri);
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : '"+resourceUri+"' is invalid URI.");
			return null;
		} 
		
		File resourceFile = new File(fileURI);
		
		if ( !resourceFile.exists() || !resourceFile.isFile() ) {
			logger.warning("Resource File '"+resourceFile.getAbsolutePath()+"' is not exist or is not a File.");
			return null;
		}
		
		byte[] propertiesXML = getPropertyXML(props);
		
		if ( propertiesXML == null )
			return null;
		
		byte[] ownersXML = getOwnersXML(owners);
		
		byte[] groupsXML = getGroupXML(groups);
		
		byte[] rightsXML = getRightsXML(rights);
		
		byte[] fileData = getBase64EncodedFileData(resourceFile);
		
		if ( fileData == null ) 
			return null;
		
		
		try {
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			
			String encoding = new sun.misc.BASE64Encoder().encode ( (userName+":"+password).getBytes() );
			
			httpsURLConnection.setRequestProperty  (REQUEST_HEADER_AUTHORIZATION, REQUEST_HEADER_AUTHORIZATION_VALUE_BASIC + " " + encoding);
			
			httpsURLConnection.setRequestMethod(REQUEST_METHOD_POST);
			
			httpsURLConnection.setDoInput(true);    
			httpsURLConnection.setDoOutput(true);		    
			httpsURLConnection.setUseCaches(false);
			 
			httpsURLConnection.setRequestProperty(REQUEST_HEADER_CONTENT_TYPE, REQUEST_HEADER_CONTENT_TYPE_VALUE);
			
			DataOutputStream out = new DataOutputStream( httpsURLConnection.getOutputStream() );
			
			out.write( (XML_FILE_HEADER + 
							"<" + NODE_NAME_REQUEST + ">" +
					        	"<" + NODE_NAME_RESOURCE + ">").getBytes() );
			
			if ( rightsXML != null )
				out.write( rightsXML );
			
			if ( ownersXML != null )
				out.write( ownersXML );
			
			if ( groupsXML != null )
				out.write( groupsXML );
			
			if ( propertiesXML != null )
				out.write( propertiesXML );
			
			out.write( ("<" + NODE_NAME_RESOURCE_DATA + ">" +
                    		"<" + NODE_NAME_NAME + ">" + resourceFileName + "</" + NODE_NAME_NAME + ">" +
                    		"<" + NODE_NAME_DATA + ">").getBytes() );
			
							out.write( fileData );
			
				out.write( ( "</" + NODE_NAME_DATA + ">" +
						"</" + NODE_NAME_RESOURCE_DATA + ">" +
					"</" + NODE_NAME_RESOURCE + ">" +
				"</" + NODE_NAME_REQUEST + ">").getBytes() );
			
			out.flush();
			out.close();
			
			if ( httpsURLConnection.getResponseCode() != 200 ) {
				logger.warning("UploadResource : Response code '"+httpsURLConnection.getResponseCode()+"' is not '200'.");
				return prepareFailureResponse( String.valueOf( httpsURLConnection.getResponseCode() ) );
			}
		
			return parseResponse( httpsURLConnection.getInputStream() );
			
		} catch (IOException e) {
			logger.warning("IOException : Unable to upload Resource on Resource Server.");
			
		} catch (Exception e1) {
			logger.warning("Exception : Unable to upload Resource on Resource Server.");
		
		}
		
		return null;
		
	}
	
	/**
	 * Prepare a Map containing Failure Status and return it.
	 * 
	 * @param failureStatus a String value of Failure Status
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, String> prepareFailureResponse(String failureStatus) {
		
		if ( failureStatus == null )
			return null;
		
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(ATTRIBUTE_NAME_STATUS, failureStatus);
		
		return returnMap;
	}
	
	/**
	 * Parse the XML data from the Input Stream and return Status Code and Resource Name in a Map.
	 * 
	 * @param is an Object of InputStream
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, String> parseResponse( InputStream is ) {
		
		Document doc = getDocument(is);
		
		if ( doc == null ) {
			logger.warning("Response is not in valid XML format.");
			return null;
		}

		Element responseElement = doc.getDocumentElement();
		
		if ( (responseElement == null) || !responseElement.hasChildNodes() ) {
			logger.warning("Unable go get Root Node.");
			return null;
		}
		
		String responseNodeName = responseElement.getNodeName();
		
		if ( (responseNodeName == null) || !responseNodeName.equals(NODE_NAME_RESPONSE) ) {
			return null;
		}
		
		String status = responseElement.getAttribute(ATTRIBUTE_NAME_STATUS);
		
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(ATTRIBUTE_NAME_STATUS, status);
		
		NodeList responseChildNodes = responseElement.getChildNodes();
		
		
		if ( responseChildNodes == null ) {
			return returnMap;
		}
		
		Node resourceNode = null;
		
		for ( int i=0 ; i<responseChildNodes.getLength() ; i++ ) {
			
			Node responseChild = responseChildNodes.item(i);
			
			if ( (responseChild == null) || (responseChild.getNodeType() != Node.ELEMENT_NODE) || !responseChild.hasChildNodes() )
				continue;
			
			String responseChildNodeName = responseChild.getNodeName();

			if ( (responseChildNodeName != null) && responseChildNodeName.equals(NODE_NAME_RESOURCE) ) {
				
				resourceNode = responseChild;
				break;
			}
		}
		
		if ( resourceNode == null ) {
			return returnMap;
		}
		
		String resourceName = ((Element)resourceNode).getAttribute(ATTRIBUTE_NAME_VAL);
		
		returnMap.put(CONSTANT_VALUE_RESOURCE_NAME, resourceName);
		
		NodeList resourceChildNodes = resourceNode.getChildNodes();
		
		if ( resourceChildNodes != null ) {
			
			for ( int i=0 ; i<resourceChildNodes.getLength() ; i++ ) {
				
				Node resourceChild = resourceChildNodes.item(i);
				
				if ( (resourceChild == null) || (resourceChild.getNodeType() != Node.ELEMENT_NODE) || !resourceChild.hasChildNodes() )
					continue;
				
				String resourceChildNodeName = resourceChild.getNodeName();

				if ( (resourceChildNodeName != null) && resourceChildNodeName.equals(NODE_NAME_GLOBAL_AT) ) {
					
					Node globalAtNode = resourceChild.getFirstChild();
					
					if ( (globalAtNode != null) &&  (globalAtNode.getNodeType() == Node.TEXT_NODE) ) {
						
						returnMap.put(NODE_NAME_GLOBAL_AT, globalAtNode.getNodeValue() );
					}
					
					break;
				}
			}
		}
		
		
		return returnMap;
	}
	
	/**
	 * Prepare an XML from the right Map and return its byte array.
	 * 
	 * @param rights an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Array of byte
	 */
	private byte[] getRightsXML(Map<String, List<String>> rights) {
		
		if ( rights == null )
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<" + NODE_NAME_RIGHTS + ">");
		
		List<String> ownerRights = rights.get(NODE_NAME_OWNER);
		
		if ( ownerRights != null ) {
			
			sb.append("<" + NODE_NAME_OWNER + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + ownerRights.contains(ATTRIBUTE_NAME_READ) + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + ownerRights.contains(ATTRIBUTE_NAME_WRITE) + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + ownerRights.contains(ATTRIBUTE_NAME_QUERY) + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + ownerRights.contains(ATTRIBUTE_NAME_RETRIEVE) + "\" " +
					  " />");
			
		} else {
			
			sb.append("<" + NODE_NAME_OWNER + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + false + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + false + "\" " +
					  " />");
		}
		
		
		List<String> groupRights = rights.get(NODE_NAME_GROUP);
		
		if ( groupRights != null ) {
			
			sb.append("<" + NODE_NAME_GROUP + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + groupRights.contains(ATTRIBUTE_NAME_READ) + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + groupRights.contains(ATTRIBUTE_NAME_WRITE) + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + groupRights.contains(ATTRIBUTE_NAME_QUERY) + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + groupRights.contains(ATTRIBUTE_NAME_RETRIEVE) + "\" " +
					  " />");
			
		} else {
			
			sb.append("<" + NODE_NAME_GROUP + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + false + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + false + "\" " +
					  " />");
		}
		
		
		List<String> otherRights = rights.get(NODE_NAME_OTHER);
		
		if ( otherRights != null ) {
			
			sb.append("<" + NODE_NAME_OTHER + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + otherRights.contains(ATTRIBUTE_NAME_READ) + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + otherRights.contains(ATTRIBUTE_NAME_WRITE) + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + otherRights.contains(ATTRIBUTE_NAME_QUERY) + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + otherRights.contains(ATTRIBUTE_NAME_RETRIEVE) + "\" " +
					  " />");
			
		} else {
			
			sb.append("<" + NODE_NAME_OTHER + 
					  " " + ATTRIBUTE_NAME_READ + "=\"" + false + "\" " + 
					  " " + ATTRIBUTE_NAME_WRITE + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_QUERY + "=\"" + false + "\" " +
					  " " + ATTRIBUTE_NAME_RETRIEVE + "=\"" + false + "\" " +
					  " />");
		}
		
		sb.append("</" + NODE_NAME_RIGHTS + ">");
		
		//logger.info("Rights : "+sb.toString());
		return sb.toString().getBytes() ;
	}
	
	/**
	 * Prepare an XML from the owner List and return its byte array.
	 * 
	 * @param owners an Object of List&lt;String&gt;
	 * 
	 * @return an Array of byte
	 */
	private byte[] getOwnersXML(List<String> owners) {
		
		if ( owners == null )
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<" + NODE_NAME_OWNERS + ">");
		
		for ( String owner : owners ) {
			
			if ( owner == null )
				continue;
			
			owner = owner.trim();
			
			if ( owner.equals("") )
				continue;
			
			sb.append("<" + NODE_NAME_OWNER + " " + ATTRIBUTE_NAME_VAL + "= \"" + owner + "\" />");
		}
		
		sb.append("</" + NODE_NAME_OWNERS + ">");
		
		//logger.info("Owners : "+sb.toString());
		return sb.toString().getBytes() ;
	}
	
	/**
	 * Prepare an XML from the group List and return its byte array.
	 * 
	 * @param groups an Object of List&lt;String&gt;
	 * 
	 * @return an Array of byte
	 */
	private byte[] getGroupXML(List<String> groups) {
		
		if ( groups == null )
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<" + NODE_NAME_GROUPS + ">");
		
		for ( String group : groups ) {
			
			if ( group == null )
				continue;
			
			group = group.trim();
			
			if ( group.equals("") )
				continue;
			
			sb.append("<" + NODE_NAME_GROUP + " " + ATTRIBUTE_NAME_VAL + "= \"" + group + "\" />");
		}
		
		sb.append("</" + NODE_NAME_GROUPS + ">");
		
		//logger.info("Groups : "+sb.toString());
		return sb.toString().getBytes() ;
	}
	
	/**
	 * Prepare an XML from the property Map and return its byte array.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Array of byte
	 */
	private byte[] getPropertyXML(Map<String, List<String>> props) {
		
		if ( props == null )
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<" + NODE_NAME_PROPS + " " + ATTRIBUTE_NAME_INHERIT + "=\"" + ATTRIBUTE_NAME_INHERIT_VALUE_TRUE + "\"" + ">");
		
		for ( String propertyName : props.keySet() ) {
			
			if ( propertyName == null )
				continue;
			
			List<String> values = props.get(propertyName);
			
			if ( values == null )
				continue;
			
			propertyName = propertyName.trim();
			
			if ( propertyName.equals("") )
				continue;
			
			for ( String value : values ) {
				
				if ( value == null )
					continue;
				
				value = value.trim();
				
				if ( value.equals("") )
					continue;
				
				sb.append("<" + NODE_NAME_PROP + " " + ATTRIBUTE_NAME_NAME + "=\"" + propertyName + "\" " + ATTRIBUTE_NAME_VAL + "=\"" + value + "\" />");
			}
			
		}
		
		sb.append("</" + NODE_NAME_PROPS + ">");
		
		//logger.info("Properties : "+sb.toString());
		return sb.toString().getBytes() ;
	}
	
	/**
	 * Return whether props contain the specified property or not.
	 * 
	 * @param propertyName a String value of Property Name
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether props contain the specified property or not.
	 */
	private boolean isPropertyExists(String propertyName, Map<String, List<String>> props) {
		
		if ( (propertyName == null) || (props == null) )
			return false;
		
		List<String> values = props.get(propertyName);
		
		if ( (values == null) || (values.size() == 0) )
			return false;
		
		for ( String value : values ) {
			
			if( value == null )
				continue;
			
			if ( value.trim().equals("") )
				continue;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Read the specified File and encode its data using Base64 Encoding.
	 * 
	 * @param file an object of File
	 * 
	 * @return an Array of byte.
	 * 
	 * @see java.io.File
	 */
	private byte[] getBase64EncodedFileData(File file) {
		
		if ( file == null )
			return null;
		
		BufferedInputStream is = null;
		
		try {
			is = new BufferedInputStream( new FileInputStream(file) );
		} catch (FileNotFoundException e1) {
			logger.warning("FileNotFoundException : Unable to read file '"+file.getAbsolutePath()+"'.");
			return null;
		}

		if ( is == null )
			return null;
		
		byte[] returnArray = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		
		try {
			
			int position = -1;
			while ( (position =is.read(buffer)) != -1 ) {
				
				if ( returnArray == null ) {
					
					if ( position != BUFFER_SIZE ) {
						
						returnArray = new byte[position];
						
						for ( int i=0 ; i<position ; i++ ) 
							returnArray[i] = buffer[i];
						
						
					} else {
						
						returnArray = buffer;
					}
				} else {
					
					int returnArrayLength = returnArray.length;
					int bufferLength = position;
					
					byte[] newArray = new byte[returnArrayLength + bufferLength];
					
					for ( int i=0 ; i<returnArrayLength ; i++ )
						newArray[i] = returnArray[i];
					
					for ( int i=0 ; i<bufferLength ; i++ )
						newArray[returnArrayLength+i] = buffer[i];
					
					returnArray = newArray;
				}
				
				buffer = new byte[BUFFER_SIZE];
				
			}
			
		} catch (IOException e) {
			logger.warning("IOException : Unable to read BufferedInputStream.");
			return null;
		}
		
		//return returnArray;
		return Base64.encode(returnArray).getBytes();
	}
	
	
	/**
	 * Prepare an XML Document Object from the XML data get from Input Stream.
	 * 
	 * @param is an Object of Input Stream
	 * 
	 * @return an Object of Document
	 * 
	 * @see org.w3c.dom.Document
	 */
	private Document getDocument(InputStream is) {
		
		if ( is == null )
			return null;
		/*	
		int i;
		try {
			while( (i = is.read()) != -1 ) {
				System.out.print((char)i);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	    try {
	    	 
		     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 return db.parse(is);
		 
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Generating XML Document from the InputStream Data.");
		} catch (SAXException e) {
			logger.warning("SAXException : Generating XML Document from the InputStream Data.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException : Generating XML Document from the InputStream Data.");
		}
		  
		return null;
	}
	
	/**
	 * Return whether SSL settings set successfully or not.
	 * 
	 * @return whether SSL settings set successfully or not
	 */
	private boolean setSSLSettings() {
		
		try {
			
			SSLContext sslContext = SSLContext.getInstance("SSL");
			
			sslContext.init(null, new X509TrustManager[] { new MyTrustManager() }, null);
			
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			  
			HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
			
		//	Authenticator.setDefault(new MyAuthenticator());
			return true;
		
		} catch (NoSuchAlgorithmException e) {
			
			logger.warning("NoSuchAlgorithmException");
			e.printStackTrace();	
			
		} catch (KeyManagementException e) {
			
			logger.warning("KeyManagementException");
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	//--------------------------------Only for testing purpose ------------------------------//
	
	private ResServerUploadManager() {
		ready = true;
		userName = "LiveCD2009";
		password = "uchworks";
		setSSLSettings();
		try {
			url = new URL("https://res.dotui.com/upload");
			//url = new URL("http://192.168.2.50/upload");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	// Just for testing
	public static void main(String[] args) {
		
		ResServerUploadManager uploadManager = new ResServerUploadManager();
		
		String resourceUri = "file:///D:/Backup/GenericTA.java";
		Map<String, List<String>> props = new HashMap<String, List<String>>();
		props.put(Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList("property Type value"));
		props.put(Constants.PROPERTY_RES_NAME, CommonUtilities.convertToList("aTestResource1"));
		
		List<String> owners = new ArrayList<String>();
		owners.add("owner A");
		owners.add("owner B");
		owners.add("owner C");
		
		List<String> groups = new ArrayList<String>();
		groups.add("group A");
		groups.add("group B");
		groups.add("group C");
		
		Map<String, List<String>> rights = new HashMap<String, List<String>>();
		List<String> ownerRights = new ArrayList<String>();
		ownerRights.add(ATTRIBUTE_NAME_READ);
		ownerRights.add(ATTRIBUTE_NAME_WRITE);
		ownerRights.add(ATTRIBUTE_NAME_QUERY);
		ownerRights.add(ATTRIBUTE_NAME_RETRIEVE);
		rights.put(NODE_NAME_OWNER, ownerRights);
		
		//logger.info( uploadManager.uploadResource(props, owners, groups, rights, resourceUri) );
	}
	*/
}
