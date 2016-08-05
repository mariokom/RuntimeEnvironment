/*
Copyright 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).  
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

An earlier version of this software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

*/
package edu.wisc.trace.uch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITDM;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.IUIPM;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
import org.openurc.uch.UIPMFatalException;
import org.openurc.uch.UIPMNotImplementedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;





import edu.wisc.trace.uch.contextmanager.UserContextManager;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.Configuration;
import edu.wisc.trace.uch.util.LoggerUtil;
//import edu.wisc.trace.uch.action.GetDocumentAction;

/**
 * Provide a central interface to communicate with any module as well as provides intercommunication among modules. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 * 
 * 
 * 
 */
public class UCH {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String INIT_PARAMETER_DEBUG_LEVEL = "debugLevel";
	
	private static final String PRESENTATION_URL_RELATIVE_PATH = "/UCH/interfacelist/index.html";
	
	private static final String LOCAL_RESOURCE_DIRECTORY_NAME = "resources";
	
	private static final String DIR_NAME_LIB = "lib";
	
	private static final String CACHE_DIRECTORY_NAME = "cache";
		
	private static final String DEFAULT_ZERO_CONF_HOST_NAME = "uch";
	private static final String DEFAULT_ZERO_CONF_SERVICE_NAME = "UCH Zero Conf Service";
	//private static final String LOCAL_CONTEXT_FILE = "contexts.xml";
	
	// Private Static Variable required for Singleton Design Pattern 
	private static UCH uch;
	
	// Base URI of Application e.g. http://192.168.2.101/UCH
	private String baseUri;
	
	// CodeBase of Application 
	private String codebase;
	
	// IP Address
	private String ipAddress;
	
	// Port Number
	private int portNo;
	
	// UCH COnfiguration File Path URI
	private String uchConfigFileUri;
	
	// Object of UCH RUI-Server Device(Advertise UCH as a UPNP Server Device)
	private UchRuiServerDevice uchRuiServerDevice;

	// Zero-Conf(Bonjour) Server's Host Name
	private String zeroConfHostName;
	
	// Zero-Conf(Bonjour) Server's Service Name
	private String zeroConfServiceName;
	
	// Object of Zero-Conf Service(Advertise UCH as a Zero Conf Device)
	private UchZeroConfService uchZeroConfService;
	
	// Resource Server's Application Path
	private String resserver_appPath;
	
	// Resource Server's UserName
	private String resserver_username;
	
	// Resource Server's Password
	private String resserver_password;
	
	// An object of UI Service Handler
	private UIServiceHandler uiServiceHandler;
	
	// An Object of GetDocument Request Handler
	private GetDocumentRequestHandler getDocumentRequestHandler;
	
	// An Object of UCH Store
	private IUCHStore uchStore;
	
	// A Map of UCH Properties
	Map<String, String> uchProps;
	
	// An Object of TDM Listener that communicate with TDMs
	private TDMListener tdmListener;
	
	// UCH Versionnumber
	private String impVersion;
	
	//UCH Builddate
	private String build;
	
	//Properties of Manifest
	private java.util.Properties prop = new java.util.Properties();		
	
	// An Object of TA Listener that communicate with TAs
	private TAListener taListener;
	
	// An Object of UIPM Listener that communicate with UIPMs
	private UIPMListener uipmListener;
	
	// An Object of UserValidation that validate User against Resource Server Users
	private UserValidation userValidation;
	
	// An Object of UserContextManager that manages user profiles and controller profiles.
	private UserContextManager userContextManager;
	
	// An Object of ResoueceManager that manages every kind of resources.
	private ResourceManager resourceManager;
	
	// File path of Server Document Root 
	private String docRootUri;
	
	// File path of cache directory
	private String cacheDirUri;
	private FileHandler fh;
	
	// File path of local resources directory
	private String localResDirUri;
	private boolean favoriteUIPMRequestState=false;
	protected ClassLoader classLoader;

	private String configurationPath;

	private Configuration configuration;
	
	/**
	 * Returns an Instance of UCH. Singleton Pattern.
	 * 
	 * @param servletContext an Object of ServletContext
	 * @param address an object of InetAddress
	 * 
	 * @return an Object of UCH
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * 
	 * @see javax.servlet.ServletContext
	 */
	static UCH getInstance(ServletContext servletContext) throws SAXException, IOException, ParserConfigurationException {
		
		if ( servletContext == null ) {
			logger.severe("Servlet Context is null.");
			return null;
		}
		
		if ( uch == null ) 
			uch = new UCH(servletContext);
		
		return uch;
		
	}
	
	/**
	 * Constructor.
	 * Instantiate the UCH.
	 * 
	 * @param servletContext an Object of ServletContext
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private UCH( ServletContext servletContext ) throws SAXException, IOException, ParserConfigurationException {
		
			// Get Init Parameters of Config File
		this.configurationPath = servletContext.getRealPath(File.separator);
		this.configuration = readConfigFile(configurationPath);
		
		String appPath = servletContext.getRealPath("/");
		
		if ( appPath == null ) {
			logger.severe("Unable to get Real Path of Application from Servlet Context.");
			return;
		}
		
				// Setting codebase - path upto UCH/WEB-INF/
		setCodeBase(appPath);
		logger.info("CodeBase : "+codebase);
		
		// Not in use - start
		// Setting uchConfigFileUri - path upto UCH/WEB-INF/uch.config
		setUCHConfigFileUri();
		logger.info("UCH Config File URI : "+uchConfigFileUri);
		// Not in use - end
		
		// Setting IP Address starts //
		InetAddress inetAddress = NetworkUtil.getInetAddress();
		
		if ( ipAddress == null || ipAddress.trim().equals("") )
			ipAddress = inetAddress.getHostAddress();
		
		if ( ipAddress == null ) {
			logger.severe("IP Address is null.");
			return;
		}
		logger.info("IP Address : "+ipAddress);
		// Setting IP Address ends //
		
		
		if ( portNo <= 0 )
			portNo = 80;
		// Setting Port No ends //
		
		
		// Setting Base URI starts //
		if ( portNo == 80 )
			baseUri = "http://" + ipAddress + "/UCH/";
		else
			baseUri = "http://" + ipAddress + ":" + portNo + "/UCH/";
		// Setting Base URI ends //
		
		
		// Setting UCH Props starts //
		uchProps = prepareUCHProps(servletContext);
		
		if ( uchProps == null )
			uchProps = new HashMap<String, String>();

			docRootUri = codebase;
			uchProps.put(Constants.UCH_PROP_DOC_ROOT, docRootUri);
		
		
		String macAddress = NetworkUtil.getMacAddress(inetAddress);
		
		if ( macAddress == null ) {
			logger.severe("MAC Address is null");
			return;
		}
		
		if ( macAddress != null ) {
			uchProps.put(Constants.UCH_PROP_UCH_INSTANCE_ID, "UCH-" + macAddress);
		}
		// Setting UCH Props ends //
		
		setCacheDirUri();
		
		setLocalResDirUri();
		
		uchStore = new UCHStore();
		
		//Parikshit Thakur: 20110829. changes for new class structure of ClassLoader class. 
		
		//ClsLoader classLoader = new ClsLoader(codebase + DIR_NAME_LIB);
		//ClassLoader classLoader = new ClassLoader(codebase + DIR_NAME_LIB);
		URL[] urls = {};
		java.lang.ClassLoader parent = this.getClass().getClassLoader();
		classLoader = new ClassLoader(urls, parent);
		// Parikshit Thakur: 20110829. removed logic of getting classLoader from uchStore.
		//uchStore.setValue(Constants.CLASS_PATH_VALUE_CLASS_LOADER, classLoader);
		
		//Parikshit Thakur: 20110829. Change ends.
		
		tdmListener = new TDMListener(this);
		taListener = new TAListener(this);
		uipmListener = new UIPMListener(this);
		
		uiServiceHandler = new UIServiceHandler(this, ipAddress, portNo); // To be modified later
		getDocumentRequestHandler = new GetDocumentRequestHandler(this);
		
		// Setting Presentation URL starts //
		String presentationURL = null;
		
		if ( portNo == 80 )
			presentationURL = "http://" + ipAddress + PRESENTATION_URL_RELATIVE_PATH;
		else
			presentationURL = "http://" + ipAddress + ":" + portNo + PRESENTATION_URL_RELATIVE_PATH;
		// Setting Presentation URL ends //
		
		
		new Thread( new UCHRuiServerStarter(this, presentationURL) ).start();
		
		new Thread( new ZeroConfServiceStarter(inetAddress, PRESENTATION_URL_RELATIVE_PATH) ).start();

		
		if ( (resserver_username == null) || ( resserver_username.trim().equals("") ) ) {
			logger.severe("'web.xml' has no Init Parameter named '"+Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_USER_NAME+"'.");
			resserver_username = null;
		}
		
		if ( (resserver_password == null) || ( resserver_password.trim().equals("") ) ) {
			logger.severe("'web.xml' has no Init Parameter named '"+Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_PASSWORD+"'.");
			resserver_password = null;
		}
		
		if ( (resserver_appPath == null) || (resserver_appPath.trim().equals("")) ) {
			
			logger.severe("'config.xml' has no Init Parameter named '"+Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_APP_PATH+"'.");
		
			resserver_appPath = null;
			
		} else {
			
			resserver_appPath = resserver_appPath.trim();
			
			if ( resserver_appPath.startsWith("http://") || resserver_appPath.startsWith("HTTP://") )
				resserver_appPath = resserver_appPath.substring( "http://".length() );
			else if ( resserver_appPath.startsWith("https://") || resserver_appPath.startsWith("HTTPS://") )
				resserver_appPath = resserver_appPath.substring( "https://".length() );
			
			logger.info("resserver_appPath = " +resserver_appPath);
			userValidation = new UserValidation("https://" + resserver_appPath);
			if ( userValidation.validateUser(resserver_username, resserver_password) ) {
				logger.info("'"+resserver_username+"' is validated successfully.");
				
			} else {
				logger.warning("Failed to validate '"+resserver_username+"'.");
				resserver_username = null;
				resserver_password = null;
			}
			
		}
		
		String loggedUserContextData = null;
		if ( resserver_username != null ) {
			//provide Resource Sheet Resource, Local Resources, Resource Server Resource and Upload Resource.
			resourceManager = new ResourceManager(this, localResDirUri, resserver_appPath, cacheDirUri, resserver_username, resserver_password);
			loggedUserContextData = getLoggedUserContextData();
			
		} else {
			
			//provide Resource Sheet Resource and Local Resources.
			resourceManager = new ResourceManager(this, localResDirUri, resserver_appPath, cacheDirUri);
		}
		userContextManager = new UserContextManager(this, loggedUserContextData, resserver_username, resserver_password);
		// Instantiate local TDMs.
		new Thread( new InstantiateTDMs() ).start();
	}
	
	
	/**
	 * Get logged user context from resource server.
	 * 
	 * @return a String value specifies user context file data
	 */
	private String getLoggedUserContextData() {
		
		return resourceManager.getUserProfile();
	}
	
	
	/**
	 * Get context-params and its values which are considered as UCH Properties.
	 * 
	 * @param servletContext an Object of ServletContext
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private Map<String, String> prepareUCHProps(ServletContext servletContext) {
	
		if ( servletContext == null ) 
			return null;
		
		List<String> nonUchPropsParams = getNonUchPropsParams();
		
		if ( nonUchPropsParams == null )
			nonUchPropsParams = new ArrayList<String>();
		
		Enumeration<String> params = servletContext.getInitParameterNames();
		
		if ( params == null )
			return null;
		
		Map<String, String> uchProps = new HashMap<String, String>();
		
		while ( params.hasMoreElements() ) {
			
			String paramName = params.nextElement();
			
			if ( paramName == null )
				continue;
			
			if ( nonUchPropsParams.contains(paramName) )
				continue;
			
			String paramValue = servletContext.getInitParameter(paramName);
			
			if ( paramValue == null )
				continue;
			
			uchProps.put(paramName, paramValue);
		
		}
		
		return uchProps;
	}
	
	/**
	 * Get the names of those context-param names which are not considered in UCH Properties.
	 *  
	 * @return an Object of List&lt;String&gt;
	 */
	private List<String> getNonUchPropsParams() {
		
		List<String> nonUchPropsParams = new ArrayList<String>();
		
		nonUchPropsParams.add(INIT_PARAMETER_DEBUG_LEVEL);
		nonUchPropsParams.add(Constants.CONTEXT_INIT_PARAM_NAME_IP_ADDRESS);
		nonUchPropsParams.add(Constants.CONTEXT_INIT_PARAM_NAME_PORT_NO);
		nonUchPropsParams.add(Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_USER_NAME);
		nonUchPropsParams.add(Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_PASSWORD);
		nonUchPropsParams.add(Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_APP_PATH);
		
		return nonUchPropsParams;
	}
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods called on UCH Starts -----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Get the value of IP Address
	 */
	String getIpAddress() {
		
		if ( portNo == 80 )
			return ipAddress;
		else
			return ipAddress + ":" + portNo;
	}
	
	
	/**
	 * Get Local UCH Store.
	 * 
	 * @return an Object of IUCHStore
	 */
	IUCHStore getLocalUCHStore() {
		
		return uchStore;
	}
	
	/**
	 * Get ClassLoader object
	 * @return an Object of ClassLoader
	 */
	ClassLoader getClassLoader(){
		
		return classLoader;
	}
	
	/**
	 * 
	 * @param uri a String value of URI
	 * @param postData a String value of Post Data
	 * @param context an Object of Map&gt;String, IProfile&lt;
	 * 
	 * @return a String value of Document Data.
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String getDocument(String uri, String postData, Map<String, IProfile> context) throws UCHException {
		
		// TODO Make proper use of context
		return getDocumentRequestHandler.getDocument(uri, postData);
	}
	
	/**
	 * Get the map of UCH Properties.
	 * 
	 * @return an Object of Map&gt;String, String&lt;
	 */
	Map<String, String> getUCHProps() {
		
		return uchProps;
	}

	/**
	 * Get the value of UCH Property 'http://purl.org/dc/terms/conformsTo'.
	 * 
	 * @return a String value
	 */
	String getConformsTo() {
		
		if ( uchProps == null ) {
			logger.warning("UCH Properties is null.");
			return null;
		}
		
		return uchProps.get(Constants.PROPERTY_CONFORMS_TO);
	}
	
	/**
	 * Get UCH Configuration File URI.
	 * 
	 * @return a String value specifies UCH Configuration File URI Path
	 */
	String getUCHConfigFileUri() {
		
		return uchConfigFileUri;
	}
	
	/**
	 * Get Zero Conf Service Host Name
	 * 
	 * @return a String value specifies Zero Conf Service Host Name.
	 */
	String getZeroConfHostName() {
		logger.info("");
		return zeroConfHostName;
	}
	
	/**
	 * Get Zero Conf Service Name
	 * 
	 * @return a String value specifies Zero Conf Service Name.
	 */
	String getZeroConfServiceName() {
		logger.info("");
		return zeroConfServiceName;
	}
	
	/**
	 * Retrieve specified Resource.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a String value of Resource Data
	 */
	String retrieveResource(String uri) {
		
		if ( uri == null ) {
			logger.warning("URI is null");
			return null;
		}
		
		if ( resourceManager == null ) {
			logger.warning("Resource Manager is null.");
			return null;
		}
		
		return resourceManager.retrieveResource(uri);
	}
	
	/**
	 * CHeck whether URI contains Resource Server App Path.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a boolean value specifies whether URI contains Resource Server App Path
	 */
	public boolean isUriContainsResServerAppPath(String uri) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return false;
		}
		
		if ( resserver_appPath == null ) {
			logger.warning("Resource Server AppPath is null.");
			return false;
		}
		
		if ( uri.indexOf(resserver_appPath) == -1 )
			return false;
		else
			return true;
	}
	
	/**
	 * Get the value of Resource Server App Path.
	 * 
	 * @return a String value of Resource Server App Path
	 */
	String getResServerAppPath() {
		
		return resserver_appPath;
	}
	
	
	/**
	 * Convert the uri from docRootUri to baseUri and return it.
	 * 
	 * docRootUri specifies file path of UCH location in local file system.
	 * baseUri specifies http path of UCH.
	 * 
	 * @param uri a String value of uri
	 * @return a String uri converted from file uri to http uri
	 */
	public String convertURI( String uri ) {
		
		if ( uri == null ) return uri;
		
		String oldUri = uri;
		
		if ( uri.contains(docRootUri) || uri.contains(uri.replaceAll(" ","%20"))){
String newUri =  baseUri + uri.substring( docRootUri.length() );
logger.info("Converted file uri " + oldUri + " to " + newUri + ".");
return newUri;
		} else {
			return uri;
		}
	}
	
	/**
	 * Returns sessionIdTargetId map
	 * @return Map of sessionId vs TargetId
	 */
	public Map<String, String> getSessionIdTargetIdMap(){
		
		return taListener.getSessionIdTargetIdMap();
		
	}
	
	/**
	 * Returns sessionId related to a socket
	 * @param socketName
	 * @return String value of sessionId
	 */
	public String getSessionId(String socketName){
		return taListener.getSessionId(socketName);
	}
	
	/**
	 * Returns cacheDirUri
	 * @return String value of cacheDirUri
	 */
	public String getCacheDir() {
		
		return cacheDirUri;
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------------- Methods called on UCH Ends ------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------- Methods called from Servlets and Actions Starts -------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	/**
	 * It finds related context if session is associated with request.
	 * And select appropriate IUIPM, ITA or ITDM for further handling.
	 * 
	 * @param protocol a String value of protocol
	 * @param requestObject an Object of Request
	 * @param responseObject an Object of Response
	 * 
	 * @see javax.servlet.http.HttpServletRequest
	 * @see javax.servlet.http.HttpServletResponse
	 */
	//public void postRequest(HttpServletRequest request, HttpServletResponse response) {  //Parikshit Thakur : 20111205. Changed signature to support other-than http requests.
	public void postRequest(String protocol, Object requestObject, Object responseObject) {
		
		if ( (requestObject == null) || (responseObject == null) ) {
			logger.warning("Request or Response is null.");
			return;
		}
		
		String uri = null;
		Map<String, IProfile> context = null;
		
		
		if(protocol.equals("http")){
			
			HttpServletRequest request = (HttpServletRequest)requestObject;
			HttpServletResponse response = (HttpServletResponse)responseObject;
			
			uri = request.getRequestURI();
			logger.info("Request to UCH : "+uri);
			
			if ( uri == null )
				return;
			
			uri = CommonUtilities.percentDecoding(uri);
			
			uri = baseUri + uri.substring( uri.indexOf("/", 1)+1 );
			
			
			String sessionId = getSessionId(request);
			
			if ( sessionId != null )
				context = userContextManager.getContext(sessionId);
			
			
			//Parikshit Thakur : 20111201. Modified code to serve out-of-session getResource request by UCH.
			
			String requestURI = request.getRequestURI();
			if ( requestURI == null )
				return;

			requestURI = CommonUtilities.percentDecoding(requestURI);
			String queryString = request.getQueryString();
			
			if( queryString == null && requestURI.indexOf("GetResources") != -1 ) {
				serveGetResourceReq(request, response);
				return;
			}
				
			if(queryString != null && requestURI.indexOf("GetDocument") != -1){
				serveGetDocumentReq(request, response);
				return;
			}
			
			if(queryString == null && requestURI.indexOf("InvokeLocator") != -1){
				serveInvokeLocatorReq(request, response);
				return;
			}
			//Change ends
			
		}
		
		//Parikshit Thakur : 20111205. Modified code to serve other-than-http request.
		try {
			uiServiceHandler.controllerRequest(baseUri, uri, requestObject, responseObject, context, protocol);
		} catch (UIPMNotImplementedException e) {
			logger.warning("UIPMNotImplementedException : Handle Post Request.");
		} catch (UIPMFatalException e) {
			logger.warning("UIPMFatalException : Handle Post Request.");
		}
	}
	
	
	
	
	//Parikshit Thakur : 20111201 . Modified code to serve out-of-session getResource request by UCH.
	
	public void serveInvokeLocatorReq(HttpServletRequest request, HttpServletResponse response){
		
		String responseXML = null;
		String queryString = request.getQueryString();
				
		responseXML = processInvokeLocatorRequest( queryString );

		if ( responseXML == null )
			return;
		
		try {
			OutputStream os = response.getOutputStream();

			logger.info("Final Response :"+responseXML);
			response.setHeader("Content-Type", "application/urc-http+xml; charset=utf-8");
			if ( responseXML != null ) {
				os.write(responseXML.getBytes("UTF-8"));
			}

		} catch (IOException e) {
			logger.warning("IOException");
		}
	}
	
	/**
	 * Parse the requestBody and get the response by calling invokeLocator() 
	 * and prepare a response String according to URC-HTTP spec 1.0 that will return to URC-HTTP UIPM.<br><br>
	 *
	 * @param queryString a String value of requestBody
	 * @return a String representing the response of involeLocator Request
	 */
    public String processInvokeLocatorRequest(String queryString) {
		
		if ( queryString == null || queryString.trim().equals("") ) {
			logger.warning("queryString is null.");
			return null;
		}
		
		Document doc = null ;
		
		try {
			doc = CommonUtilities.parseXml(queryString);
		} catch (NullPointerException e1) {
			logger.warning("NullPointerException");
		} catch (SAXException e1) {
			logger.warning("SAXException");
		} catch (IOException e1) {
			logger.warning("IOException");
		} catch (ParserConfigurationException e1) {
			logger.warning("ParserConfigurationException");
		}
		
		if ( doc.getDocumentElement()== null || !doc.getDocumentElement().getNodeName().equals("invokeLocator") )
			return null;
		
		Element invokeLocator = doc.getDocumentElement(); 
		
		String targetId = invokeLocator.getAttribute("targetId");
		String locatorId = invokeLocator.getAttribute("locatorId");
				
		
		if ( (targetId == null) || (locatorId == null) ) {
			logger.warning("targetId or locatorId is not available.");
			return null;
		}
		
		invokeLocator(targetId, locatorId);
		
		return "<locatorInvoked/>";
    }

	
	/**
	 * Processes out-of-session getDocument request
	 * 
	 * @param request an object of HttpServletRequest
	 * @param response an object of HttpServletResponse
	 */
	public void serveGetDocumentReq(HttpServletRequest request, HttpServletResponse response){
		
		String responseXML = null;
		String queryString = request.getQueryString();
		
		responseXML = processGetDocumentRequest( queryString, null);

		if ( responseXML == null )
			return;
		
		try {
			OutputStream os = response.getOutputStream();

			logger.info("Final Response :"+responseXML);
			response.setHeader("Content-Type", "application/urc-http+xml; charset=utf-8");
			if ( responseXML != null ) {
				os.write(responseXML.getBytes("UTF-8"));
			}

		} catch (IOException e) {
			logger.warning("IOException");
		}
	}
	

	/**
	 * Processes out-of-session getResource request
	 * 
	 * @param request an object of HttpServletRequest
	 * @param response an object of HttpServletResponse
	 */
	public void serveGetResourceReq(HttpServletRequest request, HttpServletResponse response){
		
		String responseXML = null;
		String requestBody = null;
		try {
			requestBody = getRequestBody( request.getInputStream() );
		} catch (IOException e) {
			logger.warning("IOException");
		}
		
		String forLang = request.getHeader("accept-language");
		responseXML = processGetResourceRequest( requestBody, forLang );
		
		if ( responseXML == null )
			return;
		
		try {
			OutputStream os = response.getOutputStream();

			logger.info("Final Response :"+responseXML);
			response.setHeader("Content-Type", "application/urc-http+xml; charset=utf-8");
			if ( responseXML != null ) {

				os.write(responseXML.getBytes("UTF-8"));
			}

		} catch (IOException e) {
			logger.warning("IOException");
		}
	}
	
	
	/**
	 * Returns request body
	 * 
	 * @param inputStream an object of InputStream
	 * @return String value of request body
	 * @throws IOException
	 */
	public static String getRequestBody(InputStream inputStream)
			throws IOException {

		if (inputStream != null) {

			String requestGetValuesLine = "";
			String requestGetValues = "";

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputStream));

			while ((requestGetValuesLine = br.readLine()) != null) {
				requestGetValues = requestGetValues + requestGetValuesLine;
			}

			return requestGetValues;

		} else {
			logger.warning("Input Stream is null.");
		}
		return null;
	}
	
	/**
	 * Processes the getDocument request and returns xml string for the document.
	 * @param queryString
	 * @param requestBody 
	 * @return String value of xml document
	 */
	public String processGetDocumentRequest(String queryString, String requestBody) {
		
		if ((queryString.indexOf("url") != -1) && (queryString.indexOf("=", queryString.indexOf("url")) != -1)) {

			String uri = queryString.substring(queryString.indexOf("=", queryString.indexOf("url")) + 1);

			if ( uri == null || (uri.trim().equals("") ) ) {
				logger.warning("'"+uri+"' is Invalid URI.");
				return null;
			}
			try {
				return getDocument(uri, requestBody, null);
			} catch (UCHException e) {
				logger.warning("UCHException");
			}
		} else {
			logger.info("Invalid Request for getDocument.");
		}
		 
		return null;
	}
	
	/**
	 * Processes the getDocument request and returns xml string for the document.
	 * @param requestBody String representing request body
	 * @param forLang Language of resource
	 * @return String value of xml document
	 */
	public String processGetResourceRequest( String requestBody, String forLang){
		
		if ( requestBody == null || requestBody.trim().equals("") ) {
			logger.warning("requestBody is null.");
			return null;
		}
		List<Map<String, List<String>>> resourceRequestList = parseResourceRequest(requestBody, forLang);
		
		if ( resourceRequestList == null )
			return null;
	
		List<List<Map<String, List<String>>>> resources = getResources(null, resourceRequestList);
		
		String responseStr =  prepareResponseString(resources);
		
		return responseStr;
		
	}
	
	
	/** 
	 * Parse the request and prepare List of Resource Properties.
	 * 
	 * @param requestString a String value of requestString
	 * @param forLang a String value of forLang
	 * 
	 * @return List&lt;Map&lt;String, String&gt;&gt; of Resource Properties
	 */
	private List<Map<String, List<String>>> parseResourceRequest(String requestString, String forLang) {
		
		Document doc = null ;
		
		try {
			doc = CommonUtilities.parseXml(requestString);
		} catch (NullPointerException e1) {
			logger.warning("NullPointerException");
		} catch (SAXException e1) {
			logger.warning("SAXException");
		} catch (IOException e1) {
			logger.warning("IOException");
		} catch (ParserConfigurationException e1) {
			logger.warning("ParserConfigurationException");
		}
		
		if ( doc == null ) {
			logger.warning("Request Body is not in proper XML Format.");
			return null;
		}
		
		if ( !doc.getDocumentElement().getNodeName().equals("getResources") )
			return null;
		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		
		if (nodeList.getLength() < 1) {
			return null;
		}
		
		List<Map<String, List<String>>> getResourcesRequestList = new ArrayList<Map<String, List<String>>>();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			
			if (node.getNodeName().equals("resource") && (node instanceof Element) && node.getChildNodes().getLength() == 0) {
				
				Element e = (Element)node;
				
				Map<String, List<String>> resProps = new HashMap<String, List<String>>();
				
				NamedNodeMap namedNodemap = e.getAttributes();
				
				for( int j=0 ; j<namedNodemap.getLength() ; j++ ) {
					
					Node n = namedNodemap.item(j);
					
					String key = n.getNodeName();
					String value = n.getFirstChild().getNodeValue();
					
					if ( (key != null) && (value != null) ) {
						
						key = key.trim();
						value = value.trim();
						
						List<String> valueList = resProps.get(key);
						
						if ( valueList == null ) {
							
							valueList = new ArrayList<String>();
							valueList.add(value);
							resProps.put(key, valueList);
							
						} else {
							
							valueList.add(value);
						}
					}
				}
				
				List<String> valueList = new ArrayList<String>();
				
				valueList.add(Constants.PROPERTY_RES_TYPE_VALUE_ATOMIC );
				
				resProps.put(Constants.PROPERTY_RES_TYPE, valueList);
				
				List<String> forLangValueList = resProps.get("forLang");
				
				if ( forLangValueList == null ) { // Set the default value of 'forLang'
					
					forLangValueList = new ArrayList<String>();
					forLangValueList.add(forLang);
					resProps.put("forLang", forLangValueList);
				}
				
				getResourcesRequestList.add(resProps);
				
			}
		}
		
		return getResourcesRequestList;
	}
	
	
	/**
	 * Prepare a Response String from ResourceResponseList.
	 * 
	 * @param getResourceResponseList List&lt;String&gt; of ResourceResponseList
	 * @return a String value of Response
	 */	
	private String prepareResponseString(List<List<Map<String, List<String>>>> resourceResponseList) {
		
		String resourceString = prepareResourceString(resourceResponseList);
		
		if ( resourceString == null )
			return null;
		
		return "<resources>"+resourceString+"</resources>";
	}
	
	/**
	 * Prepare a Response String from ResourceResponseList.
	 * 
	 * @param getResourceResponseList List&lt;String&gt; of ResourceResponseList
	 * @return a String value of Response
	 */	
	static String prepareResourceString(List<List<Map<String, List<String>>>> resourceResponseList) {
		
		if ( resourceResponseList == null ) 
			return null;
		
		StringBuilder responseXmlBuffer = new StringBuilder();
		
		for ( List<Map<String, List<String>>> resourceList : resourceResponseList ) {
			
			if ( (resourceList == null) || (resourceList.size() <= 0 ) ) {
				
				responseXmlBuffer.append("<resource />");
				continue;
			}
			
			Map<String, List<String>> resourceMap = resourceList.get(0);
			
			if ( resourceMap == null ) {
				
				responseXmlBuffer.append("<resource />");
	
			} else if ( resourceMap.containsKey("content") ) {
				
				List<String> contentValueList = resourceMap.get("content");
				
				if ( (contentValueList == null) || (contentValueList.size() <= 0) )
					responseXmlBuffer.append("<resource />");
				else
					responseXmlBuffer.append("<resource>"+contentValueList.get(0)+"</resource>");
				
				
			} else if ( resourceMap.containsKey("contentAt") ) {
				
				List<String> contentAtValueList = resourceMap.get("contentAt");
				
				if ( (contentAtValueList == null) || (contentAtValueList.size() <= 0) )
					responseXmlBuffer.append("<resource />");
				else
					responseXmlBuffer.append("<resource at=\""+contentAtValueList.get(0)+"\" />");
				
			}
		}
		
		return responseXmlBuffer.toString();
	}
	
	
	 //Parikshit Thakur : 20111201 .Change ends
	
	
	
	
	/**
	 * Handle The http://...cache?name=[resourceName].
	 * Get the specified resource from Resource Server if it is not already downloaded.
	 * If it is a zip file then unzip it and get the index file and redirect with proper URL of that file.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 */
	public void handleCacheRequest(HttpServletRequest request, HttpServletResponse response) {
		
		if( (request == null) || (response == null) ) {
			logger.warning("Request or Response object is null.");
			return;
		}
		
		String queryString = request.getQueryString();
		logger.info("UCH : QueryString :"+queryString);
		if ( queryString == null ) {
			logger.warning("Query String is null.");
			return;
		}
		
		// Change on 2010-09-15 by Sandip 
		Map<String, List<String>> resProps = percentDecodeValues( CommonUtilities.prepareKeyValueListMap(queryString) );
		
		List<String> queryParams = resProps.remove(Constants.CONSTANTS_QUERY_PARAMS);
		
		if ( resProps == null ) {
			logger.warning("Unable to get Resource Properties from Query String.");
			return;
		}
		
		Map<String, List<String>> returnProps = ResourceUtil.getSingleResource(resourceManager, resProps);
		
		if ( (returnProps == null) || (returnProps.size() <= 0) ) {
			logger.warning("Unable to get Resource for Resource Properties.");
			return;
		}
		
		String fileUri = CommonUtilities.getPropValueFromPropMap(Constants.PROP_NAME_RESOURCE_LOCAL_AT, returnProps);
		
		if ( fileUri == null ) {
			logger.warning("Unable to get local file path for resource.");
			return;
		}
		
		String redirectUri = convertURI(fileUri);
	//	logger.info("UCH : redirectUri : "+redirectUri);
		if ( redirectUri == null ) {
			logger.warning("Unable to get retrieval URL for resource.");
			return;
		}
		
		if ( queryParams != null ) {
			
			String paramStr = CommonUtilities.getFirstItem(queryParams);
			
			if ( paramStr != null ) {
				
				if ( redirectUri.indexOf('?') == -1 ) 
					redirectUri += "?" + CommonUtilities.decodeString(paramStr);
				else
					redirectUri += "&" + CommonUtilities.decodeString(paramStr);
			}
		}
		//logger.info("UCH : redirectUri : "+redirectUri);
		response.setHeader("Location", redirectUri);
		response.setStatus(301);
	}
	
	
	/**
	 * Respond with Resource Server UserName.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 */
	public void handleUserNameRequest(HttpServletRequest request, HttpServletResponse response) {
		
		if( (request == null) || (response == null) ) {
			logger.warning("Request or Response object is null.");
			return;
		}
		
		try {
			
			Writer writer = response.getWriter();
			
			if ( resserver_username == null )
				writer.write(Constants.RESSERVER_USER_STATUS_GUEST);
			else
				writer.write(resserver_username);
			
			writer.flush();
			
		} catch (IOException e) {
			logger.warning("Unable to get Writer from response.");
			//e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Update the User Info to communicate with Resource Server.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 */
	public void handleUpdateUserAction(HttpServletRequest request, HttpServletResponse response) {
		
		if( (request == null) || (response == null) ) {
			logger.warning("Request or Response object is null.");
			return;
		}
		String userName = request.getParameter(Constants.REQUEST_PARAM_NAME_RESSERVER_USERNAME);
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			response.setStatus(400);
			return;
		}
		
		String password = request.getParameter(Constants.REQUEST_PARAM_NAME_RESSERVER_PASSWORD);
		
		if ( password == null ) {
			logger.warning("Password is null.");
			response.setStatus(400);
			return;
		}
		
		if ( userValidation == null ) {
			logger.warning("Resorce Server is not available to validate.");
			response.setStatus(400);
			return;
		}
		
		if ( userValidation.validateUser(userName, password) ) {
			
			logger.info("User '"+userName+"' is validated successfully.");
			
			resserver_username = userName;
			resserver_password = password;
			
			resourceManager = new ResourceManager(this, localResDirUri, resserver_appPath, cacheDirUri, resserver_username, resserver_password);
			
			configuration.setUserName(resserver_username);
configuration.setPwd(resserver_password);
try {
	configuration.writeToFile(new File(configurationPath));
} catch (JAXBException e) {
	logger.severe("Failed to write new configuration file ");
	}
			
			return;
			
		} else {
			
			logger.warning("Failed to validate User '"+userName+"'.");
			response.setStatus(400);
			return;
		}
		
	}
	
	/**
	 * Parse the keyValue String(key1=value1&...&keyN=valueN).
	 * And clear all or specified type resources from cache.
	 * 
	 * @param keyValueStr a Key Value String
	 */
	public void performConfigOperation(String keyValueStr) {
		
		Map<String, String> keyValueMap = CommonUtilities.prepareKeyValueMap(keyValueStr);
		//logger.info("Going to Perform Config Operation : "+keyValueMap);
	
		if ( (keyValueMap == null) || (keyValueMap.size() == 0) ) {
			return;
		}
		
		if ( keyValueMap.containsKey("action") ) {
			
			String action = keyValueMap.get("action");
			
			if ( action == null ) {
				return;
			}
			
			action = action.toLowerCase();
			
			if ( action.equals("clearcache") ) {
				
				String type = keyValueMap.get("type");
				
				if ( type == null ) { // clear whole cache
					clearCache();
				} else {
					
					if ( type.equals(Constants.PROPERTY_RES_TYPE_VALUE_RESSHEET) ) {
						clearResourceSheetCache();
					} else if ( type.equals(Constants.PROPERTY_RES_TYPE_VALUE_UIPM_CLIENT) ) {
						clearUipmClientCache();
					} else {
						clearCache(type);
					}
				}
			}
		}
		
	}
	
	/**
	 * Call the same method of this class with passing hostName and  HttpServletRequest null.
	 * 
	 * @return a String representing Compatible UIs
	 */
	public String getCompatibleUIs() {
		
		return getCompatibleUIs(null, null);
	}
	
	/**
	 * Call the same method of this class with passing HttpServletRequest null.
	 * 
	 * @param hostName a String value of HostName
	 * 
	 * @return a String representing Compatible UIs
	 */
	public String getCompatibleUIs(String hostName) {
		
		return getCompatibleUIs(hostName, null);
	}
	
	/**
	 * Get compatible UIs XML response for specified hostName and request user.
	 * Get SessionId from the request Object and get related context if session is exist.
	 * And find compatible UIs for specified user if user context exists else return compatible UIs for anonymous User.
	 * 
	 * @param hostName a String value of HostName
	 * @param request an Object of HttpServletRequest
	 * 
	 * @return a String representing Compatible UIs
	 */
	public String getCompatibleUIs(String hostName, HttpServletRequest request) {
		
		if ( hostName == null ) 
			hostName = getIpAddress();
		
		/*
		 Yuvaraj : 2013-04-22 - Get Compatible UI request can contains the parameters for filtering UI List.
		 Below is the reference implementation for this feature.
	     */
		if((request!=null) && (request.getQueryString() != null)) {
			
			Map<String, String[]> queryParamMap = request.getParameterMap();
			
			/*
			 * Prashant Goswami : 20140506. Code change to resolve problem while removing parameter directly from request parameter map using iterator
			 * Here we are using copy of request parameter map
			 */
			// code start
			Map<String, String[]> queryParams = new HashMap<String, String[]>();
			
			queryParams.putAll(queryParamMap);
			// code end
			
			String[] paramValue = queryParams.get(Constants.UILIST_REQUEST_PARAMETER_FAVORITEUIPMCLIENTS); 
			
			if(paramValue != null  && !paramValue[0].equalsIgnoreCase(String.valueOf(favoriteUIPMRequestState))) { // favorite UIPM clients implementation block.
				
				favoriteUIPMRequestState  = Boolean.valueOf(paramValue[0]);
				
				loadFavoriteUIPM();
			}
			
			Iterator<String> iterator = queryParams.keySet().iterator();
			
			while(iterator.hasNext()) {// Removing the favorite request parameter from query parameters.
				
				String key = iterator.next();
				
				if(key.contains(Constants.UILIST_REQUEST_PARAMETER_FAVORITEUIPMCLIENTS)) {
					
					iterator.remove();
				}
			}

			if(paramValue.length > 1)
				return uiServiceHandler.filterCompatibleUIs(hostName, queryParams, userContextManager.getContext( getSessionId(request) ));
			
			//return uiServiceHandler.getCompatibleUIs(hostName, userContextManager.getContext( getSessionId(request) ) );
		}
		
		//logger.info("uch : getCompatibleUIs : hostName : "+hostName);
		if ( request == null ) {
		//	logger.info("uch : getCompatibleUIs : request is null : "+uiServiceHandler.getCompatibleUIs(hostName, userContextManager.getContext( getSessionId(request) ) ));
			return uiServiceHandler.getCompatibleUIs(hostName, null);
			
		} else {
		//	logger.info("uch : getCompatibleUIs : "+uiServiceHandler.getCompatibleUIs(hostName, userContextManager.getContext( getSessionId(request) ) ));
			//return uiServiceHandler.getCompatibleUIs(hostName, userContextManager.getContext( getSessionId(request) ) );
			if(favoriteUIPMRequestState)
				return uiServiceHandler.getCompatibleUIs(hostName, getUserContexts().get(0));
			else
				return uiServiceHandler.getCompatibleUIs(hostName, null);
				
		}
		
	}
	
	/**
	 * Based on the query parameter value open/close contexts.
	 */
	private void loadFavoriteUIPM() {
		
		if(favoriteUIPMRequestState) {// Favorite UIPM request
			
			uipmListener.contextsOpened(getUserContexts());
		}else {
			
			uipmListener.contextsClosed(null);
		}
	}
	
	/**
	 * Retrieving the Version of the UCH out of the MANIFEST.MF
	 * @throws IOException 
	 */
	 public String getUchVersion(InputStream input) {
		 
		 try{
			prop.load(input);
			impVersion = prop.getProperty("Version");
		 } catch(IOException e){
			 
		 }
		 return impVersion;
	 }
	 
	 /**
		 * Retrieving the Build Date of the UCH out of the MANIFEST.MF
		 * @throws IOException 
		 */
	 public String getUchBuild(InputStream input) {
		 
		 try{
			prop.load(input);
			build = prop.getProperty("Build_date");
		 } catch(IOException e){
			 
		 }
		 return build;
	 }
	 
	 private static String showMap(Attributes map) {
	        String version = "init";
		 	if (map == null) return "no Objects";
	        for (Map.Entry<Object, Object> e : map.entrySet()) {
	        	
	        	version = (String)e.getValue();
	        }
	        
	        return version;
	    }
	//------------------------------------------------------------------------------------------------------------//
	//----------------------------- Methods called from Servlets and Actions Ends --------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	



	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------- Methods invoked on TDM Listener Starts -----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	

	/**
	 * Call the same method on TDMListener
	 * 
	 * @param targetId a String value of Target Id
	 * 
	 * @return an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	List<Map<String, IProfile>> getTargetContexts(String targetId) {
		
		return tdmListener.getTargetContexts(targetId);
	}

	
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------- Methods invoked on TDM Listener Ends ------------------------------------//
	//------------------------------------------------------------------------------------------------------------//





	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------- Methods invoked on TA Listener Starts ------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetProps an Object of Map&lt;String, Object&gt; specifies Target Properties
	 * @param taProps an Object of Map&lt;String, String&gt; specifies TA Properties
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 * 
	 * @return a String value specifies Target Id
	 */
	String targetDiscovered(Map<String, Object> targetProps, Map<String, String> taProps, List<Map<String, IProfile>> contexts) {
		
		return taListener.targetDiscovered(targetProps, taProps, contexts);
	}
	
	
	/**
	 * Call the same method on TAListener and UIPMListener.
	 * 
	 * @param targetId a String value of Target Id
	 */
	void targetDiscarded(String targetId) {
		
		taListener.targetDiscarded(targetId);
		uipmListener.targetDiscarded(targetId);
	}
	

	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetId a String value of Target Id
	 * @param socketName a String value of Socket Name
	 * @param clientProps an Object of Map&lt;String, String&gt; specifies client properties
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of Map&lt;String, String&gt; specifies session information 
	 */
	Map<String, String> openSessionRequest(String targetId,
			String socketName, Map<String, String> clientProps, Map<String, IProfile> context) {
		
		return taListener.openSessionRequest(targetId, socketName, clientProps, context);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param sessionId a String value of Session Id
	 * @param paths an Object of List&lt;String&gt; specifies paths
	 * @param includeSets an Object of List&lt;String&gt; specifies boolean values of includeSets
	 * @param depths a List&lt;Integer&gt; of depths
	 * @param pruneIndices a List&lt;Boolean&gt; of pruneIndices
	 * @param pruneXMLContent a List&lt;Boolean&gt; of pruneXMLContent
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt; specifies values of paths
	 */
	// Changed returnType to include element attributes. Parikshit Thakur
	// 2012-09-24 : added extra attributes depths, pruneIndices, pruneXMLContent according to new spec.
	List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets, List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent) {
		
		return taListener.getValues(sessionId, paths, includeSets, depths, pruneIndices, pruneXMLContent);
	}

	
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths List&lt;String&gt; a List of elementPaths
	 * @param operations List&lt;String&gt; a List of operations (allowed operations are "S", "A", "R", "I" or "K" )
	 * @param reqValues List&lt;String&gt; a List of Requested Values
	 * 
	 * @return Map&lt;String, List&lt;String&gt;&gt; of set Values
	 */
	Map<String, List<String>> setValuesRequest(String sessionId,
			List<String> paths, List<String> operations, List<String> reqValues) {	
		
		return taListener.setValuesRequest(sessionId, paths, operations, reqValues);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param eltIds a List&lt;String&gt; of elementIds
	 * @param indexNos a List&lt;Integer&gt; of indexNo
	 * 
	 * @return a List&lt;Set&lt;Integer&gt; of indices
	 */
	List<Set<String>> getIndices(String sessionId, List<String> eltPaths) {

		return taListener.getIndices(sessionId, eltPaths);
	}
	
	/**
	 * Call sessionClosed on TAListener.
	 * 
	 * @param sessionId a String value of sessionId
	 */
	void closeSession(String sessionId) {
		
		taListener.closeSession(sessionId);
	}
	
	/**
	 * Call the same method on TAListener.
	 * Prepare returned URI String in proper HTTP format and return it.
	 * 
	 * @param targetName a String value of targetName
	 * @param socketName a String value of socketName
	 * 
	 * @return a String representing Socket Description URI
	 */
	String getSocketDescriptionUri(String targetName, String socketName) {
		
		String sdUri = taListener.getSocketDescriptionUri(targetName, socketName);
		
		if ( sdUri == null ) {
			logger.warning("Unable to get Socket Description URI for target '"+targetName+"' and socket '"+socketName+"'.");
			return null;
		}
		
		return convertURI(sdUri);		
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * 
	 * @return a String value of Socket Friendly Name
	 */
	String getSocketFriendlyName(String targetId, String socketName) {
		
		return taListener.getSocketFriendlyName(targetId, socketName);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return a String of SocketName
	 */
	String getSocketName(String sessionId) {
		
		return taListener.getSocketName(sessionId);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return List&lt;String&gt; of SocketName
	 */
	List<String> getSocketNames(String targetName) {
		
		return taListener.getSocketNames(targetName);
	}
	
	/**
	 * Call the same method on TAListener.
	 * Prepare returned URI String in proper HTTP format and return it.
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return a String representing Socket Description URI
	 */
	String getTargetDescriptionUri(String targetName) {
		
		String tdUri = taListener.getTargetDescriptionUri(targetName);
		
		if ( tdUri == null ) {
			logger.warning("Unable to get Target Description URI for target '"+targetName+"'.");
			return null;
		}
		
		return convertURI(tdUri);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a String value of targetName
	 *
	 */
	public String getTargetName(String targetId) {
		
		return taListener.getTargetName(targetId);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return a String value of target friendly Name
	 */
	String getTargetFriendlyName( String targetName) {
		
		return taListener.getTargetFriendlyName(targetName);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a Map&lt;String, Object&gt; value of Target Properties
	 */
	Map<String, Object> getTargetProps(String targetId) {	
		
		return taListener.getTargetProps(targetId);
		
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param eltId a String value of eltId
	 * 
	 * @return boolean whether specified socketElement is exists or not
	 */
	boolean isElementAvailable(String targetId, String socketName, String elementId) {
		
		return taListener.isElementAvailable(targetId, socketName, elementId);
	}
	
	/**
	 * Call the same method on TAListener. 
	 * 
	 * @param targetId a String value of targetId
	 * @param locatorId a String value of locaterId
	 * 
	 */
	void invokeLocator(String targetId, String locatorId) {
		
		taListener.invokeLocator(targetId, locatorId);
	}
	
	/**
	 * Call the same method on TAListener. 
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return a Map&lt;string, String&gt; of  LocatorID V/S Locator Type
	 */
	Map<String, String> getLocators(String targetName) {
		
		return taListener.getLocators(targetName);
	}
	
	/**
	 * Call the same method on TAListener.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param resProps  a List&lt;Map&lt;String, String&gt;&gt; value of Resource Properties
	 * @return a List&lt;Object&gt; of resources
	 */
	public List<Map<String, Object>> getDynRes( String sessionId, List<Map<String, String>> resProps ) {
		
		return taListener.getDynRes( sessionId, resProps );
	}
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------- Methods invoked on TA Listener Ends -------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------- Methods invoked on UIPM Listener Starts -----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * Call sessionAbort method of UIPMListener.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param code a String value specifying reason of session abortion
	 */
	//void abortSession(String sessionId) { //Parikshit Thakur : 20111203. Changed signature for the change in UCH spec. 
	void abortSession(String sessionId, String code) {
		
		uipmListener.abortSession(sessionId, code);
	}
	
	/**
	 * Call the same method on UIPMListener.
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds
	 * @param paths a List&lt;String&gt; of paths
	 * @param operations a List&lt;String&gt; of operations
	 * @param values a List&lt;String&gt; of values
	 * @param props a List&lt;Map&lt;String, String&gt;&gt; of map containing attributes for path e.g. socketElType(var, cmd or ntf), hasDynRes and notification type( alert, error, info).
	 */
	void updateValues(List<String> sessionIds, List<String> paths,
			List<String> operations, List<String> values, List<Map<String, String>> props) {
		uipmListener.updateValues(sessionIds, paths, operations, values, props);
	}
	
	/**
	 * Call the same method on UIPMListener.
	 * 
	 * @param targetId a String value of targetId
	 * @param targetName a String value of targetName
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void instantiateUIPMs(String targetId, String targetName, List<Map<String, IProfile>> contexts) {
		
		uipmListener.instantiateUIPMs(targetId, targetName, contexts);
	}
	
	/**
	 * Call the same method on UIPMListener.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	void targetDiscovered(String targetId, List<Map<String, IProfile>> contexts) {	
		
		uipmListener.targetDiscovered(targetId, contexts);
	}
	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------- Methods invoked on UIPM Listener Stops -----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------- Methods invoked on UIServiceHandler Starts ---------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Call the same method on UIServiceHandler.
	 * 
	 * @param targetId a String value of Target Id
	 * @param socketName an Object of String specifies Socket Name
	 * @param protocolShortName a String value of Protocol Short Name
	 * @param uris an Object of List&lt;String&gt; specifies URIs
	 * @param protocolInfo an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of Contexts for which the user interface shall be added to the UIList
	 * 
	 * @return a boolean value specifies whether compatible UI added successfully or not.
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	//Parikshit Thakur : 20111123. Changed signature. Changed List<String> socketNames to String socketName
	boolean addCompatibleUI(String targetId, String socketName,
			String protocolShortName, List<String> uris,
			Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons, List<Map<String, IProfile>> contexts) throws UCHException {
		
		return uiServiceHandler.addCompatibleUI(targetId, socketName, protocolShortName, uris, protocolInfo, icons, contexts);
	}
	
	/**
	 * Call the same method on UIServiceHandler.
	 * 
	 * @param uris an Object of List&lt;String&gt; specifies List of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	void removeCompatibleUIs(List<String> uris) throws UCHException {
		
		uiServiceHandler.removeCompatibleUIs(uris);
	}
	
	/** 
	 * Call the same method on UIServiceHandler.
	 * 
	 * @param uipm an object of IUIPM
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * @param contexts List of contexts for which the URI service is available.
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(IUIPM uipm, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts)
			throws UCHException {
		
		return uiServiceHandler.startUriService(uipm, scheme, port, portIsFlexible, basePath, basePathIsFlexible);
	}
	
	/**
	 * Call the same method on UIServiceHandler.   
	 * 
	 * @param tdm an object of ITDM
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * @param contexts List of contexts for which the URI service is available. 
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(ITDM tdm, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts)
			throws UCHException {
		
		return uiServiceHandler.startUriService(tdm, scheme, port, portIsFlexible, basePath, basePathIsFlexible);
	}
	
	/** 
	 * Call the same method on UIServiceHandler.     
	 * 
	 * @param ta an object of ITA
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * @param contexts List of contexts for which the URI service is available. 
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(ITA ta, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts)
			throws UCHException {
		
		return uiServiceHandler.startUriService(ta, scheme, port, portIsFlexible, basePath, basePathIsFlexible);
	}

	/**
	 * Call the same method on UIServiceHandler.  
	 * 
	 * @param ta an object of ITA 
	 * @param uri a String value of URI
	 */
	void stopUriService(ITA ta, String uri) throws UCHException {
		
		uiServiceHandler.stopUriService(ta, uri);
	}
	
	/**
	 * Call the same method on UIServiceHandler.  
	 * 
	 * @param tdm an object of ITDM 
	 * @param uri a String value of URI
	 */
	void stopUriService(ITDM tdm, String uri) throws UCHException{

		uiServiceHandler.stopUriService(tdm, uri);
	}
	
	/**
	 * Call the same method on UIServiceHandler.  
	 * 
	 * @param uipm an object of IUIPM 
	 * @param uri a String value of URI
	 */
	void stopUriService(IUIPM uipm, String uri) throws UCHException{

		uiServiceHandler.stopUriService(uipm, uri);
	}
	
	
	/**
	 * 
	 * Call the same method on UIServiceHandler.  
	 * 
	 * @param tdm an Object of ITDM
	 */
	void removeTDM(ITDM tdm) throws UCHNotImplementedException {
		
		uiServiceHandler.removeTDM(tdm);
	}
	
	/**
	 * Call the same method on UIServiceHandler. 
	 * 
	 * @param ta an Object of ITA
	 */
	void removeTA(ITA ta) {
		
		uiServiceHandler.removeTA(ta);
	}

	/**
	 * Call the same method on UIServiceHandler. 
	 * 
	 * @param uipm an Object of IUIPM
	 */
	void removeUIPM(IUIPM uipm) {
		
		uiServiceHandler.removeUIPM(uipm);
	}
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------- Methods invoked on UIServiceHandler Ends ----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------- Methods invoked on ResourceManager Starts ----------------------------------//
	//------------------------------------------------------------------------------------------------------------//

	/**
	 * Get Resources from Local Resources, Resource Server, Target Adapter or Resource Sheet as par the requested resource property resourceType.
	 * If request properties contains property 'http://myurc.org/ns/res#type' and its value is 'http://myurc.org/restypes#atomic' then go for atomic or dynamic resource.
	 * Else find resource from local resources and if not found then go to resource server to get it.
	 * Filter the responded Resources.
	 * 
	 * @param sessionId a String value of SessionId
	 * @param resProps an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return a List&lt;List&lt;Map&lt;String, Object&gt;&gt;&gt; of Resources.
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps) {
		
		List<List<Map<String, List<String>>>> retResProps = resourceManager.getResources(sessionId, resProps);
		
		if ( retResProps != null )
			new ResourceFilter().filter(retResProps);
		
		return retResProps;
	}
	
	/**
	 * Get Resources from Local Resources, Resource Server, Target Adapter or Resource Sheet as par the requested resource property resourceType.
	 * If request properties contains property 'http://openurc.org/ns/res#type' and its value is 'http://openurc.org/restypes#atomic' then go for atomic or dynamic resource.
	 * Else find resource from local resources and if not found then go to resource server to get it.
	 *
	 * @param resProps an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return a List&lt;List&lt;Map&lt;String, Object&gt;&gt;&gt; of Resources.
	 */
	public List<List<Map<String, List<String>>>> getResources(List<Map<String, List<String>>> resProps) {
		
		return resourceManager.getResources(null, resProps);
	}
	
	/**
	 * Call the same method on ResourceManager. 
	 * 
	 * @param props an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; specifies Resource Properties
	 * @param owners an Object of List&lt;String&gt; specifies owners of Resource
	 * @param groups an Object of List&lt;String&gt; specifies groups of Resource
	 * @param rights an Object of Map&lt;String, List&lt;String&gt;&gt; specifies rights of Resource
	 * @param resourceUri a String value of local resource path URI
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> props, List<String> resourceUri) {
		
		return resourceManager.uploadResources(props, null, null, null, resourceUri);
	}
	
	/**
	 * Call the same method on ResourceManager. 
	 * 
	 * @param tdUri a String value of Target Description URI(tdUri)
	 */
	void addResourceDir(String tdUri) {
	
		resourceManager.addResourceDir(tdUri);
	}
	
	/**
	 * Call the same method on uipmListener.
	 * 
	 * @param sessionIds a List&lt;String&gt; value of sessionIds
	 * @param eltIds a List&lt;String&gt; value of elementIds
	 * @param resources a List&lt;Map&lt;String, Object&gt;&gt; values of resources
	 */
	void updateDynRes(List<String> sessionIds, List<String> eltIds, List<Map<String, Object>> resources) {
		
		if(resources != null)
			uipmListener.updateDynRes(sessionIds, eltIds, resources);
		else
			resourceManager.updateDynRes(sessionIds, eltIds);
	}
	
	
	/**
	 * Call the same method on ResourceManager.
	 * 
	 * @param sessionIds a List&lt;String&gt; value of sessionIds
	 * @param eltIds a List&lt;String&gt; value of elementIds
	 */
	void updateDynRes(List<String> sessionIds, List<String> eltIds) {
		
		resourceManager.updateDynRes(sessionIds, eltIds);
	}
	
	
	
	/**
	 * Clear all resources from cache.
	 */
	private void clearCache() {
		
		resourceManager.clearCache();
		//clearCompatibleUIs(deletedUris);
		//clearUipmClientCompatibleUIs();
	}
	
	/**
	 * Clear UIPM-Client from cache.
	 */
	private void clearUipmClientCache() {
		
		resourceManager.clearUipmClientCache();
		//clearCompatibleUIs(deletedUris);
		//clearUipmClientCompatibleUIs();
	}
	
	/**
	 * Clear Resource Sheet from cache.
	 */
	private void clearResourceSheetCache() {
		
		resourceManager.clearResourceSheetCache();
	}
	
	/**
	 * Clear specified type of Resources from cache.
	 * 
	 * @param resourceType a String value of Resource Type.
	 */
	private void clearCache(String resourceType) {
		
		resourceManager.clearCache(resourceType);
	}
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------- Methods invoked on ResourceManager Ends -----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------ Methods invoked on UchRuiServerDevice Starts --------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Call the same method of UchRuiServerDevice.
	 * 
	 * @param uiList a String value of UCH
	 */
	void updateUIList() {
		
		//uchRuiServerDevice.updateUIList( getCompatibleUIs() );
	}
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------- Methods invoked on UchRuiServerDevice Ends ---------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------ Methods invoked on UchContextManager Starts ---------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Open Context with specified User.
	 * Also open a session with same user.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 */
	public void openUserContext(HttpServletRequest request, String userName, String password) {
		
		userContextManager.openUserContext(request, userName, password);
	}
	
	/**
	 * Get session from request and found user from it and close context with that user.
	 * Also close same session.
	 * 
	 * @param request an Object of HttpServletRequest
	 */
	public void closeUserContext(HttpServletRequest request) {
		
		userContextManager.closeUserContext(request);
	}
	
	/**
	 * Check whether specified userName and password is valid or not.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return a boolean value specifies whether userName and password is valid or not.
	 */
    public boolean validateContextUser(HttpServletRequest request, String userName, String password) {
		
		return userContextManager.validateUser(request, userName, password);
	}

    /**
	 * Get all Contexts.
	 * 
	 * @return an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	List<Map<String, IProfile>> getContexts() {
		
		return userContextManager.getContexts();
	}
	
	/**
	 * Call the same method on UserContextManager, TAListener and UIPMListenr.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) {
		
		userContextManager.targetContextsAdded(targetId, contexts);
		taListener.targetContextsAdded(targetId, contexts);
		uipmListener.targetContextsAdded(targetId, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager, TAListener and UIPMListenr.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) {
		
		userContextManager.targetContextsRemoved(targetId, contexts);
		taListener.targetContextsAdded(targetId, contexts);
		uipmListener.targetContextsRemoved(targetId, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param uipm an object of IUIPM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void addUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) {
		//logger.info("------------------URI : " + uri + "\n-------------------Contexts :"+contexts);
		userContextManager.addUriServiceContexts(uipm, uri, contexts);
	}

	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void addUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) {
		//logger.info("------------------URI : " + uri + "\n-------------------Contexts :"+contexts);
		userContextManager.addUriServiceContexts(ta, uri, contexts);
	}

	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void addUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) {
		//logger.info("------------------URI : " + uri + "\n-------------------Contexts :"+contexts);
		userContextManager.addUriServiceContexts(tdm, uri, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param uipm an object of IUIPM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void removeUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) {
		
		userContextManager.removeUriServiceContexts(uipm, uri, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void removeUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) {
		
		userContextManager.removeUriServiceContexts(tdm, uri, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void removeUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) {
		
		userContextManager.removeUriServiceContexts(ta, uri, contexts);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	List<String> getAvailableTargetIds(Map<String, IProfile> context) {
		
		return userContextManager.getAvailableTargetIds(context);
	}
	
	/**
	 * Call the same method on UserContextManager.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	List<String> getURIs(Map<String, IProfile> context) {
		
		return userContextManager.getURIs(context);
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------- Methods invoked on UchContextManager Ends ----------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	//------------------------------------------------------------------------------------------------------------//
	//----------------------------- Methods invoked from UchContextManager Starts --------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	

	/**
	 * Call the same method on TDMListener and UIPMListener. That will further call the same methods on all TAs and UIPMs.
	 * 
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void contextOpened(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		tdmListener.contextsOpened(contexts);
		uipmListener.contextsOpened(contexts);
	}
	
	/**
	 * Call the same method on TDMListener and UIPMListener. That will further call the same methods on all TAs and UIPMs.
	 * 
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void contextClosed(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		tdmListener.contextsClosed(contexts);
		uipmListener.contextsClosed(contexts);
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------ Methods invoked from UchContextManager Ends ---------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Private Methods of UCH Starts ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Set the value of Codebase.
	 * 
	 * @param appPath a String value of Application Path
	 */
	private void setCodeBase(String appPath) {
		
		if ( appPath == null )
			return;
		
		appPath = appPath.trim();
		
		if ( appPath.startsWith("/") )
			codebase = "file://" + CommonUtilities.replaceAll(appPath, '\\', '/').replaceAll(" ", "%20");
		else
			codebase = "file:///" + CommonUtilities.replaceAll(appPath, '\\', '/').replaceAll(" ", "%20");
		   
	}
	
	
	/**
	 * Set the value of UCH config File Path.
	 */
	private void setUCHConfigFileUri() {
		
		if ( codebase == null )
			return;
		
		uchConfigFileUri = codebase + Constants.CONSTANT_UCH_CONFIG_FILE_NAME; 
	}
	
	
	/**
	 * Set the value of Cache Directory Path.
	 * If it is exists then clear its sub directories and files.
	 * If not exists then create it.
	 */
	private void setCacheDirUri() {
		
		if ( docRootUri == null ) {
			logger.severe("Document Root URI is null.");
			return;
		}
		
		cacheDirUri = (docRootUri + CACHE_DIRECTORY_NAME).replace(" ", "%20");
		
		try {
			
			File cacheDir = new File( new URI( cacheDirUri ) );
			
			if ( cacheDir.exists() ) {
				
				if ( cacheDir.isFile() ) {
					
					cacheDir.delete();
					cacheDir.mkdir();
					
				} else {
					
					CommonUtilities.deleteSubDirectories(cacheDirUri);
				}
				
			} else {
				cacheDir.mkdir();
			}
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Instantiating Resource Manager; Invalid URI '" + cacheDirUri + "'.");
			logger.severe("Unable to instantiate Resource Manager.");
			cacheDirUri = null;
		}
	}
	
	
    /**
     * Set the value of Local Resources Directory Path.
     */
	private void setLocalResDirUri() {
		
		if ( docRootUri == null ) {
			logger.severe("Document Root URI is null.");
			return;
		}

		localResDirUri = (docRootUri + LOCAL_RESOURCE_DIRECTORY_NAME).replace(" ", "%20");

	}
	
	
	/**
	 * Get sessionId from the request if session is exists with specified request.
	 * 
	 * @param request an Object of Request
	 * 
	 * @return a String value of SessionId
	 */
	private String getSessionId(HttpServletRequest request) {
		
		if ( request == null ) {
			logger.warning("Request is null.");
			return null;
		}
		
		HttpSession session = request.getSession(false);
		
		if ( session == null ) {
			logger.info("No any session is associated with specified request.");
			return null;
		}
		
		return session.getId();
	}
	
	/**
	 * Percent decode keys and their values of specifies map and return in a new map.
	 * 
	 * @param map an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt; contains percent decoded keys and their values
	 */
	private Map<String, List<String>> percentDecodeValues(Map<String, List<String>> map) {
		
		if ( map == null )
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		for ( Entry<String, List<String>> entry : map.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			returnMap.put(CommonUtilities.percentDecoding(entry.getKey()), percentDecodeValues(entry.getValue()) );
		}
		
		return returnMap;
	}
	
	/**
	 * Percent decode values of specifies list and return in a new list.
	 * 
	 * @param list an Object of List&lt;String&gt;
	 * 
	 * @return an Object of List&lt;String&gt; contains percent decoded values
	 */
	private List<String> percentDecodeValues(List<String> list) {
		
		if ( list == null )
			return null;
		
		List<String> returnList = new ArrayList<String>();
		
		for ( String value : list )
			returnList.add( CommonUtilities.percentDecoding(value) );
		
		return returnList;
	}
	
	// Just for testing
	void displayLocalStringValues() {
		
		logger.info("Base URI : " + baseUri);
		logger.info("IP Address : " + ipAddress);
		logger.info("Port No : " + portNo);
		logger.info("Codebase URI : " + codebase);
		logger.info("Doc Root URI : " + docRootUri);
		logger.info("Cache Directory URI : " + cacheDirUri);
		logger.info("Local Resource Directory URI : " + localResDirUri);
		logger.info("UCH Config File URI : " + uchConfigFileUri);
	}
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------------- Private Methods of UCH Ends -----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Private Threads of UCH Starts ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Start UCH RUI server in different thread.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class UCHRuiServerStarter implements Runnable {
		
		private UCH uch;
		private String presentationURL;
		
		/**
		 * Provide the reference of UCH and presentationURL to local variable.
		 * 
		 * @param uch an Object of UCH
		 * @param presentationURL a String value of presentation URL
		 */
		UCHRuiServerStarter(UCH uch, String presentationURL) {
			
			this.uch = uch;
			this.presentationURL = presentationURL;
		}
		
		/**
		 * Instantiate the object of UchRuiServerDevice.
		 */
		public void run() {
			
			////////////////////////Advertise UCH as a UPNP Server Device starts //////////////////////////////
			try {
				logger.info("Going to start UCH-RUI Server.");
				uchRuiServerDevice = new UchRuiServerDevice(uch, codebase, ipAddress, presentationURL);
				logger.info("UCH-RUI Server started successfully.");
			} catch (Exception e) {
				logger.severe("Exception : when instantiating UCH RUI Server Device.");
			}
			///////////////////////// Advertise UCH as a UPNP Server Device ends ///////////////////////////////
		}
	}
	
	/**
	 * Start Zero Conf Service in different Thread.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class ZeroConfServiceStarter implements Runnable {
		
		private InetAddress address;
		private String presentationURL;
		
		/**
		 * Provide the reference of InetAddress and presentationURL to local variables.
		 * 
		 * @param address an Object of InetAddress
		 * @param presentationURL a String value of presentation URL
		 */
		private ZeroConfServiceStarter(InetAddress address, String presentationURL) {
			logger.info("lusm: " + address.getHostName() + "; " + presentationURL);
			
			
			this.address = address;
			this.presentationURL = presentationURL;
		}
		
		/**
		 * Instantiate the object of UchZeroConfService.
		 */
		public void run() {
			
			try {
				
				uchZeroConfService = new UchZeroConfService(address, DEFAULT_ZERO_CONF_HOST_NAME, portNo, DEFAULT_ZERO_CONF_SERVICE_NAME, presentationURL);
				
				zeroConfHostName = uchZeroConfService.getHostName();
				zeroConfServiceName = uchZeroConfService.getServiceName();
			} catch (Exception e) {
				
			}
		}
	}

	/**
	 * Instantiate TDMs in different thread.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class InstantiateTDMs implements Runnable {
		
		/**
		 * Default Constructor.
		 */
		private InstantiateTDMs() {
			
		}
		
		/**
		 * Instantiate TDMs
		 */
		public void run() {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warning("InterruptedException");
			}
			
			if ( tdmListener != null )
				tdmListener.instantiateTDMs();
		}
	}
//Yuvaraj.start
	
	
	/**
	 * Returns user context configured in the deployment descriptor.
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of username verses profile map. 
	 */
	public List<Map<String, IProfile>> getUserContexts() {
		
		List<Map<String, IProfile>> contextList = new ArrayList<Map<String,IProfile>>();
		
		IProfile userProfile = userContextManager.getUserProfile(resserver_username, resserver_password);
		
		Map<String, IProfile> contextMap = new HashMap<String, IProfile>();
		
		contextMap.put(resserver_username, userProfile);
		
		contextList.add(contextMap);
		
		return contextList;
		
	}
//Yuvaraj.end
	/**  
	 * initialize Servlets and add them to Tomcat
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 */
	private Configuration readConfigFile(String path) throws SAXException, IOException, ParserConfigurationException{
		File file = new File( path + "config.xml");
		
		Configuration config = null;
		try {
			config = Configuration.readConfigFile(file);
		} catch (Exception e){
					Configuration.getDefaultConfiguration();
		}
			
			
			
					this.resserver_appPath = config.getAppPath();
			this.resserver_username = config.getUserName();
			this.resserver_password = config.getPwd() ;
			try {
				this.portNo = Integer.parseInt(config.getPort()  );
			} catch(Exception e) {
				portNo = 80;
			}
			this.ipAddress = config.getIpAdress();
		
		
	return config;		
	}

	public void stopUCH() {
		
		uchZeroConfService.finalize(); 
	}	
	

	
	
	//------------------------------------------------------------------------------------------------------------//
	//-------------------------------------- Private Threads of UCH Ends -----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
}
