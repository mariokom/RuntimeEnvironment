package eu.asterics.component.actuator.uchclb.uchCommunication;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import eu.asterics.component.actuator.uchclb.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.GetDependenciesRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.GetResourceRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.GetResourcesWithPropRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.GetUpdatesRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.GetValuesRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.SetValuesRequest;
import eu.asterics.component.actuator.uchclb.utils.HttpCommunicator;
import eu.asterics.component.actuator.uchclb.utils.HttpResponse;


//TODO untested functionality: getResources, getResoutrcesWithProperties, getUpdates, cookies
//TODO untested functionality (not supported by UCH?): getDependencyValues, suspend session, resume session
//TODO updateChanels are not supported


/**
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class UchSession {
	private String urcHttpUri;
	private String sessionId;
	private String ipAddress;
	private String portNo;
	private String includeDependenciesTypes;
	private boolean updateChannelActive;
	
	private HashMap<String, String> cookies;

	@SuppressWarnings("unused")
	private UchSession(){}
	
	/**
	 * Object that hold UCH session details and contains the UCH function to retrieve/change URC component values.
	 * 
	 * @param xmlSessionDetails - the response received by the UCH server
	 * @param urcHttpUri - the URC-HTTP uri that can be found in the description of the component to start a session
	 * with (UI list)
	 * 
	 * @throws Exception
	 */
	public UchSession(String xmlSessionDetails, String urcHttpUri) throws Exception {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new InputSource(new ByteArrayInputStream(xmlSessionDetails.getBytes("utf-8"))));
			this.urcHttpUri = urcHttpUri;
		} catch (Exception ex) {
			throw ex;
		}
		
		this.extractSessionDetails(document);
		this.updateChannelActive = false;
		this.cookies = new HashMap<String, String>();
	}
	
	/**
	 * Object that hold UCH session details and contains the UCH function to retrieve/change URC component values.
	 * 
	 * @param document - the response received by the UCH server as a {@link Document} object
	 * @param urcHttpUri - the URC-HTTP uri that can be found in the description of the component to start a session
	 * with (UI list)
	 * 
	 * @throws Exception
	 */
	public UchSession(Document document, String urcHttpUri) throws Exception {
		this.extractSessionDetails(document);
		
		this.urcHttpUri = urcHttpUri;
		this.updateChannelActive = false;
		this.cookies = new HashMap<String, String>();
	}
	
	
	private void extractSessionDetails(Document document) throws Exception {
		ArrayList<String> results;
		
		results = UchCommunicator.executeXpathExpression("/sessionInfo/session/text()", document);
		if (results.size() > 0) {
			this.sessionId = results.get(0);
		} else {
			this.sessionId = "";
		}

		results = UchCommunicator.executeXpathExpression("/sessionInfo/updateChannel/ipAddress/text()", document);
		if (results.size() > 0) {
			this.ipAddress = results.get(0);
			//results.get(0).substring(0,results.get(0).indexOf(":"));
		} else {
			this.ipAddress = "";
		}

		results = UchCommunicator.executeXpathExpression("/sessionInfo/updateChannel/portNo/text()", document);
		if (results.size() > 0) {
			this.portNo = results.get(0);
		} else {
			this.portNo = "";
		}

		results = UchCommunicator.executeXpathExpression("/sessionInfo/includeDependencies/@types", document);
		if (results.size() > 0) {
			this.includeDependenciesTypes = results.get(0);
		} else {
			this.includeDependenciesTypes = "";
		}
	}

	

	/**************************************
	 *    Object methods - start
	 **************************************/
	
	/**
	 * Returns the session id for this session
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * Returns the ip address for the update channel
	 * @return
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Returns the port number for the update channel
	 * @return
	 */
	public String getPortNo() {
		return portNo;
	}

	/**
	 * Returns the include dependencies types
	 * @return
	 */
	public String getIncludeDependenciesTypes() {
		return includeDependenciesTypes;
	}
	
	/**
	 * Returns true if the update channel is active and false otherwise
	 * @return
	 */
	public boolean isUpdateChannelActive() {
		return updateChannelActive;
	}
	
	
	/**************************************
	 *    Object methods - end
	 **************************************/

	
	
	/**************************************
	 *    Session functions - start
	 **************************************/
	
	/**
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the values of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should try to retrieve all the available values.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getValues(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		//get all values
		if ((bodyContent == null) || bodyContent.equals("")) {
			bodyContent = "<getValues><get ref=\"/\" /></getValues>";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the values of the UI Socket element. This is a simplified method
	 * in which you should specify only the path (in most case it is just the name) of the requested value.
	 * 
	 * @param refsPath - A set containing the path of the variables. If this set is empty or null, the method
	 * should try to retrieve all the available values.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getValues(Set<String> refsPath) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent = "<getValues>";
		if ((refsPath == null) || refsPath.isEmpty()) {
			bodyContent += "<get ref=\"/\" />";
		}
		else {
			for (String variablePath: refsPath) {
				bodyContent += "<get ref=\"/" + variablePath + "\" />";
			}
		}
		bodyContent += "</getValues>";
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the values of the UI Socket element.
	 * 
	 * @param getValuesRequest - A {@link GetValuesRequest} element. If this object is null,
	 * the method should try to retrieve all the available values.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getValues(GetValuesRequest getValuesRequest) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( getValuesRequest == null ) {
			bodyContent = "<getValues> <get ref=\"/\" /> <getValues>";
		}
		else {
			bodyContent = getValuesRequest.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the updates of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should try to retrieve all the available updates.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getUpdates(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		if ((bodyContent == null) || bodyContent.equals("")) {
			bodyContent = "<getUpdates><get ref=\"/\" /></getUpdates>";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getUpdates&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the updates of the UI Socket element. This is a simplified method
	 * in which you should specify only the path (in most case it is just the name) of the requested value.
	 * 
	 * @param refsPath - A set containing the path of the variables. If this set is empty or null, the method
	 * should try to retrieve all the available updates.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getUpdates(Set<String> refsPath) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent = "<getUpdates>";
		if ((refsPath == null) || refsPath.isEmpty()) {
			bodyContent += "<get ref=\"/\" />";
		}
		else {
			for (String variablePath: refsPath) {
				bodyContent += "<get ref=\"/" + variablePath + "\" />";
			}
		}
		bodyContent += "</getUpdates>";
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getUpdates&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the updates of the UI Socket element.
	 * 
	 * @param getUpdatesRequest - A {@link GetUpdatesRequest} element. If this object is null,
	 * the method should try to retrieve all the available updates.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getUpdates(GetUpdatesRequest getUpdatesRequest) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( getUpdatesRequest == null ) {
			bodyContent = "<getUpdates> <get ref=\"/\" /> <getUpdates>";
		}
		else {
			bodyContent = getUpdatesRequest.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getUpdates&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the dependency values of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should not take any action and
	 * it will return an empty string.
	 * 
	 * @return - returns the expected data if succeeded and an empty string otherwise
	 */
	public String getDependencyValues(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		if ((bodyContent == null) || bodyContent.equals("")) {
			return "";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getDependencyValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the dependency values of the UI Socket element.
	 * 
	 * @param getDependenciesRequestBody - A {@link GetDependenciesRequest} element. If this object is null,
	 * the method should not do anything and it will return an empty string.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getDependencyValues(GetDependenciesRequest getDependenciesRequestBody) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( getDependenciesRequestBody == null ) {
			return "";
		}
		else {
			bodyContent = getDependenciesRequestBody.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?getDependencyValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request to set some values of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should not take any action and
	 * it will return an empty string.
	 * 
	 * @return - returns the expected data if succeeded and an empty string otherwise
	 */
	public String setValues(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		if ((bodyContent == null) || bodyContent.equals("")) {
			return "";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?setValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Changes the values of th UI scocket element.
	 * 
	 * @param setValuesRequest - A {@link SetValuesRequest} element. If this object is null,
	 * the method should not do anything and it will return an empty string.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String setValues(SetValuesRequest setValuesRequest) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( (setValuesRequest == null) ) {
			return "";
		}
		else {
			bodyContent = setValuesRequest.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?setValues&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the resources of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should not take any action and
	 * it will return an empty string.
	 * 
	 * @return - returns the expected data if succeeded and an empty string otherwise
	 */
	public String getResource(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");


		if ((bodyContent == null) || bodyContent.equals("")) {
			return "";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?GetResources&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the resources of the UI Socket element.
	 * 
	 * @param getResourceRequest - A {@link GetResourceRequest} element. If this object is null,
	 * the method should not do anything and it will return an empty string.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getResource(GetResourceRequest getResourceRequest) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( getResourceRequest == null ) {
			return "";
		}
		else {
			bodyContent = getResourceRequest.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?GetResources&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the resources (with properties) of the UI Socket element.
	 * 
	 * @param bodyContent - The (xml) data contained inside the body of the request.
	 * To construct this xml data you should examine the URC-HTTP protocol documentation.
	 * If this string is empty or null, the method should not take any action and
	 * it will return an empty string.
	 * 
	 * @return - returns the expected data if succeeded and an empty string otherwise
	 */
	public String getResourcesWithProperties(String bodyContent) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		if ((bodyContent == null) || bodyContent.equals("")) {
			return "";
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?GetResourcesWithProps&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Sends a request asking for the the resources (with properties) of the UI Socket element.
	 * 
	 * @param getResourcesWithPropRequest - A {@link GetResourcesWithPropRequest} element. If this object is null,
	 * the method should not do anything and it will return an empty string.
	 * 
	 * @return - returns the requested data if succeeded and an empty string otherwise
	 */
	public String getResourcesWithProperties(GetResourcesWithPropRequest getResourcesWithPropRequest) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");

		String bodyContent;
		if ( getResourcesWithPropRequest == null ) {
			return "";
		}
		else {
			bodyContent = getResourcesWithPropRequest.toString();
		}
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?GetResourcesWithProps&session="+this.sessionId, null, headersMap,
					HttpCommunicator.DATATYPE_TEXT_XML, HttpCommunicator.DATATYPE_TEXT_XML, bodyContent);
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Suspends this session. <br/>
	 * <b> UCH JAVA SERVER DOES NOT IMPLEMENT THIS FUNCIONALITY YET ? </b>
	 * 
	 * @param timeout
	 * @return
	 */
	public String suspend(int timeout) {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?suspendSession&session="+this.sessionId+"&timeout="+timeout,
					null, headersMap, HttpCommunicator.DATATYPE_TEXT_XML, "", "");
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
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
	 * <u> This is a URC/HTTP-session method </u> <br/> <br/>
	 * 
	 * Resumes this session. <br/>
	 * <b> UCH JAVA SERVER DOES NOT IMPLEMENT THIS FUNCIONALITY YET ?</b>
	 * 
	 * @return
	 */
	public String resume() {
		HttpCommunicator httpCommunicator = new HttpCommunicator(urcHttpUri);

		HashMap<String, String> headersMap = new HashMap<String, String>();
		
		String sessionCookies = this.getSessionCookies();
		if (!sessionCookies.equals("")) {
			headersMap.put("Cookie", this.getSessionCookies());
		}
		headersMap.put("HTTP/1.1", "");
		
		try {
			HttpResponse httpResponse = httpCommunicator.postRequest("?resumeSession&session="+this.sessionId,
					null, headersMap, HttpCommunicator.DATATYPE_TEXT_XML, "", "");
			
			if (httpResponse.getStatusCode() == 200) {
				String responseBody = httpResponse.getBody();
				this.addCookiesFromResponse(httpResponse);
				
				return responseBody;
			}
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	
	/**************************************
	 *    Session functions - end
	 **************************************/
	
	
	
	/**************************************
	 *    Cookies handling - start
	 **************************************/
	
	//Very basic cookie-handling (Set-Cookie: cookieName1=cookieValue)
	//It cannot handle other other options (time-to-expire, domain etc)
	
	private void addCookiesFromResponse(HttpResponse httpResponse) {
		Map<String, List<String>> headers = httpResponse.getHeaders();
		List<String> cookiesToAdd = headers.get("Set-Cookie");
		if (cookiesToAdd != null) {
			for (String cookie: cookiesToAdd) {
				String cookieKey = cookie.substring(0, cookie.indexOf("="));
				String cookieValue = cookie.substring(cookie.indexOf("=")+1, cookie.length());
				
				this.cookies.put(cookieKey, cookieValue);
			}
		}
	}
	
	private String getSessionCookies() {
		String cookieString = "";
		
		for (String cookieKey: this.cookies.keySet()) {
			cookieString+=cookieKey + "=" + this.cookies.get(cookieKey) + ";";
		}
		
		if (!cookieString.equals("")) {
			cookieString = cookieString.substring(0, cookieString.length()-1);
		}
		
		return cookieString;
	}
	
	
	/**************************************
	 *    Cookies handling - end
	 **************************************/

	
	

}
