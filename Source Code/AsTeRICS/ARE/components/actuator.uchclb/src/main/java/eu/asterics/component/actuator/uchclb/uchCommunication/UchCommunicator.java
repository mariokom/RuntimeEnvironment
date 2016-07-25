package eu.asterics.component.actuator.uchclb.uchCommunication;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.asterics.component.actuator.uchclb.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchclb.uchCommunication.UchSession;
import eu.asterics.component.actuator.uchclb.uchCommunication.uiList.Protocol;
import eu.asterics.component.actuator.uchclb.uchCommunication.uiList.SocketDescription;
import eu.asterics.component.actuator.uchclb.uchCommunication.uiList.TargetDescription;
import eu.asterics.component.actuator.uchclb.uchCommunication.uiList.UI;
import eu.asterics.component.actuator.uchclb.uchCommunication.uiList.UrcHttpProtocolInfo;
import eu.asterics.component.actuator.uchclb.utils.HttpCommunicator;
import eu.asterics.component.actuator.uchclb.utils.HttpResponse;



/**
 * Class that provides methods to:
 * - retrieve information from the UCH server (out-of-session messages)
 * - creates sessions with the UCH and returns {@link UchSession} holds a
 * 	 session object for each created session
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class UchCommunicator {
	
	//UchCommunicator attributes
	private String ip;
	private int port;
	private String baseURL;
	private Document uiListDocument;
	
	private static final String contextPath = "UCH";
	
	public static final String URC_HTTP = Protocol.URC_HTTP;
	public static final String HTTP_HTML = Protocol.HTTP_HTML;
	
	//UI list objects (are stored only if storage mode is set to true)
	//key: UIid, value: UI object
	private HashMap<String, UI> storage;
	
	//If this boolean is true, the UchCommunicator stores the data that were retrieved from the
	//server to local storage. When there is a request for previously stored data, this 
	//object is fetching them from the local storage.
	//If this boolean is set to false, no data is stored and every request for data is a new
	//request to the server
	private boolean storageMode;

	//session map. key: sessionId, value: Session object
	private HashMap<String, UchSession> sessions;
	
	
	/**
	 * Initializes the {@link UchCommunicator} object.
	 * UCH's address is set to the default (http://localhost:8080/UCH).
	 * 
	 * @throws Exception
	 */
	public UchCommunicator() throws Exception {
		this.ip = "localhost";
		this.port = 8080;
		this.baseURL = "http://"+ip+":"+port+"/"+UchCommunicator.contextPath;
		
		
		this.storageMode = false;
		this.storage = new HashMap<String, UI>();
		
		this.sessions = new HashMap<String, UchSession>();
		
		init();
	}
	
	/**
	 * Initializes the {@link UchCommunicator} object.
	 * UCH's address is defined by the parameters
	 * 
	 * @param ip - the IP address that UCH runs on.
	 * @param port - the port which UCH listens to.
	 * @param storeMode - If this boolean is true, this object stores the data that were retrieved 
	 * from the server to a local storage. When there is a request for previously stored data, this 
	 * object is fetching them from the local storage. If this boolean is set to false, no data is 
	 * stored and every request for data is a new request to the server.
	 * 
	 * @throws Exception
	 */
	public UchCommunicator(String ip, int port, boolean storeMode) throws Exception {
		this.ip = ip;
		this.port = port;
		this.baseURL = "http://"+ip+":"+port+"/"+UchCommunicator.contextPath;

		this.storageMode = storeMode;
		this.storage = new HashMap<String, UI>();
		
		this.sessions = new HashMap<String, UchSession>();
		
		init();
	}

	/**
	 * Initializes this object by downloading the UI list xml file
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		this.uiListDocument = UchCommunicator.getDocumentFromURI(this.baseURL+"/GetCompatibleUIs");
	}
	
	
	
	/****************************
	 * Object methods - START 
	 ****************************/
	
	/**
	 * Returns the ip that UCH is running on
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the ip that UCH runs on
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Returns the port that UCH listens to
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port that UCH listens to
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Returns the url that UCH is running on
	 * 
	 * @return
	 */
	public String getBaseURL() {
		return this.baseURL;
	}
	
	/**
	 * Returns a boolean that specifies if this {@link UchCommunicator} object's storage mode is enabled
	 * 
	 * @return
	 */
	public boolean isStorageMode() {
		return storageMode;
	}

	/**
	 * Sets the storage mode value
	 */
	public void setStorageMode(boolean storageMode) {
		this.storageMode = storageMode;
		if (!storageMode) {
			this.storage.clear();
		}
	}
	
	/***************************
	 * Object methods - END 
	 ***************************/
	
	
	
	
	/*****************************************
	 * UCH out-of-session methods - START 
	 *****************************************/
	
	/**
	 * Returns the content in the uiList document as text
	 * 
	 * @return - {@link String}
	 */
	public String getUIListText() {
		
		String text ="";
		try {
			if ( (storageMode) && (this.uiListDocument != null) ) {
				text = getTextFromDocument(uiListDocument);
			}
			else {
					text = getTextFromDocument(getDocumentFromURI(this.baseURL+"/GetCompatibleUIs"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	
	/**
	 * Returns the content in the uiList document as a {@link Document} object
	 * 
	 * @return - {@link Document}
	 */
	public Document getUIListDocument() {

		try {
			if ( (storageMode) && (this.uiListDocument != null) ) {
				return uiListDocument;
			}
			else {
				return getDocumentFromURI(this.baseURL+"/GetCompatibleUIs");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Returns an {@link ArrayList} that contains the UI ids which are running
	 * on this UCH instance.
	 * 
	 * @return - {@link ArrayList} of {@link String}
	 */
	public ArrayList<String> getAllUiIds() {
		
		ArrayList<String> uiIdList;
		try {
			uiIdList = executeXpathExpression("/uilist/ui/uiID/text()", this.getUIListDocument());
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		
		if (storageMode) {
			for (String uiId: uiIdList) {
				if (!this.storage.containsKey(uiId)) {
					try {
						UI ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
						this.storage.put(uiId, ui);
					} catch (Exception e) {
						continue;	//cannot retrieve this specific UI from the server
					}
				}
			}
		}

		return uiIdList;
	}

	
	/**
	 * Returns the uiName of the UI with the given id
	 * 
	 * @param uiId - The id of the UI
	 * @return - {@link String}
	 */
	public String getUiName(String uiId) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				e.printStackTrace();
				return "";	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		return ui.getName();
	}

	
	/**
	 * Returns a {@link String} containing the value of the fork element inside the {@link UI} with the given id
	 * 
	 * @param uiId - The id of the UI
	 * @return - {@link String}
	 */
	public String getFork(String uiId) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				e.printStackTrace();
				return "";	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		return ui.getFork();
	}
	
	
	/**
	 * Returns a {@link Set} of {@link String} that contains the protocols supported by this UI.
	 * 
	 * @param uiId - The id of the UI
	 * @return - {@link Set} of {@link String}
	 */
	public Set<String> getProtocolNames(String uiId) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		return ui.getAllProtocolNames();
	}
	
	
	/**
	 * Returns the URI of the protocol
	 * 
	 * @param uiId - The id of the UI
	 * @param protocolName - the name of the protocol
	 * @return - {@link String}
	 */
	public String getProtocolURI(String uiId, String protocolShortName) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		return ui.getProtocol(protocolShortName).getUri();
	}
	
	
	/**
	 * Returns value of the "conformsTo" element in the specified protocol
	 * 
	 * @param uiId - the id of the UI
	 * @param protocolName - the name of the protocol
	 * @return - {@link String}
	 */
	public String getProtocolConformsTo(String uiId, String protocolShortName)  {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		
		Protocol urcHttpProtocol = ui.getUrcHttpProtocol();
		if (urcHttpProtocol != null) {
			return ((UrcHttpProtocolInfo)urcHttpProtocol.getProtocolInfo()).getConformsTo();
		}
		else {
			return "";
		}
	}
	
	
	/**
	 * Returns the contents of the socketDescriptionAt element.
	 * CAUTION: The UI must support the URC_HTTP protocol
	 * 
	 * @param uiId - The id of the UI
	 * @return
	 */
	public String getSocketDescriptionURI(String uiId) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}
		
		Protocol urcHttpProtocol = ui.getUrcHttpProtocol();
		if (urcHttpProtocol != null) {
			return ((UrcHttpProtocolInfo)urcHttpProtocol.getProtocolInfo()).getSocketDescriptionUri();
		}
		else {
			return "";
		}
	}
	
	
	/**
	 * Returns the contents of the targetDescriptionAt element.
	 * CAUTION: The UI must support the URC_HTTP protocol
	 * 
	 * @param uiId - The id of the UI
	 * @return
	 */
	public String getTargetDescriptionURI(String uiId) {
		UI ui = storage.get(uiId);
		if (ui == null) {
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}

		Protocol urcHttpProtocol = ui.getUrcHttpProtocol();
		if (urcHttpProtocol != null) {
			return ((UrcHttpProtocolInfo)urcHttpProtocol.getProtocolInfo()).getTargetDescriptionUri();
		}
		else {
			return "";
		}
	}
	
	
	/**
	 * Returns the {@link SocketDescription} object that provides methods to access
	 * the socket description information.
	 * 
	 * CAUTION: The UI must support the URC_HTTP protocol
	 * 
	 * @return - The {@link SocketDescription} object and null if the object could not be retrieved
	 */
	public SocketDescription getSocketDescription(String uiId) {
		
		UI ui = storage.get(uiId);
		if ( ui == null ) {		//this UI object does not exist in the storage map
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}

		Protocol urcHttpProtocol = ui.getUrcHttpProtocol();
		if (urcHttpProtocol != null) {
			return ((UrcHttpProtocolInfo)urcHttpProtocol.getProtocolInfo()).getSocketDescription();
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * Returns the {@link TargetDescription} object that provides methods to access
	 * the target description information.
	 * 
	 * CAUTION: The UI must support the URC_HTTP protocol
	 * 
	 * @return - The {@link TargetDescription} object and null if the object could not be retrieved
	 */
	public TargetDescription getTargetDescription(String uiId) {
		
		UI ui = storage.get(uiId);
		if ( ui == null ) {		//this UI object does not exist in the storage map
			try {
				ui = new UI(getUIListDocument(), uiId);	//try to retrieve it from the server
			} catch (Exception e) {
				return null;	//cannot retrieve this specific UI from the server
			}
			
			if (storageMode) {
				this.storage.put(uiId, ui);
			}
		}

		Protocol urcHttpProtocol = ui.getUrcHttpProtocol();
		if (urcHttpProtocol != null) {
			return ((UrcHttpProtocolInfo)urcHttpProtocol.getProtocolInfo()).getTargetDescription();
		}
		else {
			return null;
		}
	}
	
	/*****************************************
	 * UCH out-of-session methods - END 
	 *****************************************/
	
	
	
	
	/*********************************
	 * UCH in-session methods - START 
	 *********************************/
	
	/**
	 * Returns all session ids which where created by this object
	 * 
	 * @return
	 */
	public Set<String> getSessionIds() {
		return this.sessions.keySet();
	}
	
	/**
	 * Returns the {@link UchSession} object that corresponds to the given sessionId
	 * 
	 * @param sessionId - A sessionId
	 * @return
	 */
	public UchSession getSession(String sessionId) {
		return this.sessions.get(sessionId);
	}
	
	/**
	 * Removes the session with the given id from the opened sessions
	 * 
	 * @param sessionId - A sessionId
	 */
	public void closeSession(String urcHttpUri, String sessionId) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("HTTP/1.1", "");
		try {
			httpCommunicator.getRequest("?closeSessionRequest&session="+sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML);
			this.sessions.remove(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a GET sessionRequest for the given uri
	 * 
	 * @param urcHttpUri - The URC-HTTP URI of a specific UI
	 * 
	 * @return - returns the session id or an empty string if an error occurred
	 */
	public String createSession_getRequest(String urcHttpUri) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("HTTP/1.1", "");

		try {
			HttpResponse httpResponse = httpCommunicator.getRequest("?openSessionRequest", null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML);
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				UchSession newSession = new UchSession(responseBody, urcHttpUri);
				this.sessions.put(newSession.getSessionId(), newSession);

				return newSession.getSessionId();
			}
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/** 
	 * TODO NOT YET IMPLEMENTED
	 * 
	 * Sends a POST sessionRequest for the given uri
	 * 
	 * @param urcHttpUri
	 * 
	 * @return - returns the session id or an empty string if an error occurred
	 */
	public String createSession_postRequest(String urcHttpUri) {
		String dataOfRequest = "";
		
		//TODO create the data as specified by the URC-HTTP protocol and enter them in 'dataOfRequest' variable
		
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);
		HashMap<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("HTTP/1.1", "");
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?openSessionRequest", null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, dataOfRequest);
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				UchSession newSession = new UchSession(responseBody, urcHttpUri);
				this.sessions.put(newSession.getSessionId(), newSession);

				return newSession.getSessionId();
			}
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	/*********************************
	 * UCH in-session methods - END 
	 *********************************/
	
	
	
	
	/****************************
	 * XML PARSING METHODS 
	 ****************************/
	
	/**
	 * Executes an XPath expression in a specific document and returns the results as an {@link ArrayList}
	 * of {@link String}
	 * 
	 * @param query - the xpath query to execute
	 * @param uIlistDocument - the document
	 * @return
	 * @throws XPathExpressionException
	 */
	public static ArrayList<String> executeXpathExpression(String query, Document document) throws Exception {
		ArrayList<String> results = new ArrayList<String>();

		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(query);
		NodeList uiList = (NodeList)expr.evaluate(document, XPathConstants.NODESET);

		for (int i=0;i<uiList.getLength();i++) {
			Node node = uiList.item(i);
			results.add(node.getNodeValue());
		}
		
		return results;
	}
	
	/**
	 * Returns the text contained in the given document
	 * 
	 * @return
	 */
	public static String getTextFromDocument(Document document) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e1) {
			return "";
		}
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document.getDocumentElement());

		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {	
			return "";
		}

		return sw.toString();
	}
	
	/**
	 * Retrieves a {@link Document} from the given URI
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static Document getDocumentFromURI(String uri) throws Exception {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		HttpCommunicator httpCommunicator = new HttpCommunicator(uri);
		HashMap<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("HTTP/1.1", "");
		
		try {
			HttpResponse httpResponse = httpCommunicator.getRequest("", null, headersMap, HttpCommunicator.DATATYPE_APPLICATION_XML);
			if (httpResponse.getStatusCode() == 200) {
				String UIlist = httpResponse.getBody();
				
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				document = builder.parse(new InputSource(new ByteArrayInputStream(UIlist.getBytes("utf-8"))));
			}
			else {
				
			}

		} catch (Exception ex) {
			throw  ex;
		}
		return document; 
	}
	
}
