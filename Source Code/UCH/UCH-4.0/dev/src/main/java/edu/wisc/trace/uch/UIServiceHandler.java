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
package edu.wisc.trace.uch;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITDM;
import org.openurc.uch.IUIPM;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.TANotImplementedException;
import org.openurc.uch.TDMFatalException;
import org.openurc.uch.TDMNotImplementedException;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
import org.openurc.uch.UIPMFatalException;
import org.openurc.uch.UIPMNotImplementedException;

import edu.wisc.trace.uch.CompatibleUI.Protocol;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods for add/remove/get CompatibleUIs.
 * Also provide methods to start/stop URI service.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class UIServiceHandler {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private Map<String, CompatibleUI> targetIdCompatibleUIMap = new TreeMap<String, CompatibleUI>();
	private Map<String, IUIPM> uriIUIPMMap = new TreeMap<String, IUIPM>();
	private Map<String, ITDM> uriITDMMap = new TreeMap<String, ITDM>();
	private Map<String, ITA> uriITAMap = new TreeMap<String, ITA>();
	
	private UCH uch;
	private String ipAddress;
	private int portNo;
	
	/**
	 * Provide the reference of UCH, ipAddress and portNo to local variables.
	 * 
	 * @param uch an Object of UCH
	 * @param ipAddress a String value of IP Address
	 * @param portNo an int value of Port No
	 */
	UIServiceHandler(UCH uch, String ipAddress, int portNo) {

		this.uch = uch;
		this.ipAddress = ipAddress;
		this.portNo = portNo;
	}
	
	
	
	/**
	 * Add compatible UIs for specified target and its socket.
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
	//Parikshit Thakur : 20111123. Changed signature. Changed 'List<String> socketNames' to 'String socketName'
	synchronized boolean addCompatibleUI(String targetId, String socketName, String protocolShortName,
			List<String> uris, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons, List<Map<String, IProfile>> contexts) throws UCHException {
		
		logger.info("luxm protInfo" + protocolInfo);
		
		
		if ( (targetId == null) || (socketName == null) || (protocolShortName == null) ||
				(uris == null) || (uris.size() == 0) ||(protocolInfo == null)  ) {
			logger.warning("One or more input parameter is invalid.");
			throw new UCHException();
		}
		
		if( targetId.trim().equalsIgnoreCase("all") ) {
			
			if( protocolShortName.trim().equalsIgnoreCase("URC-HTTP") ) {
				logger.warning("targetId 'all' is not applicable for protocolShortName 'URC-HTTP'.");
				throw new UCHException();
			}
				
		}
		
		//Parikshit Thakur: 20111123. Modified code for the new structure of UIList
		String targetName = uch.getTargetName(targetId);
		String uiId = targetName + " " + socketName + " " + targetId;
		
		//if( !validateURIs(targetId, uris) ) {
		if( !validateURIs(uiId, uris) ) {
			logger.severe("One or more uri from '"+uris+"' is exists with other target.");
			return false;
		}
		
		//CompatibleUI compatibleUI = targetIdCompatibleUIMap.get(targetId);
		CompatibleUI compatibleUI = targetIdCompatibleUIMap.get(uiId);
		
		if ( compatibleUI == null ) {
			
			//Parikshit Thakur: 20111123. Modified code for the new structure of UIList
			//compatibleUI = new CompatibleUI( targetId, socketNames, protocolShortName, uris, protocolInfo, icons);
			//targetIdCompatibleUIMap.put(targetId, compatibleUI);
			compatibleUI = new CompatibleUI( uiId, protocolShortName, uris, protocolInfo, icons);
			targetIdCompatibleUIMap.put(uiId, compatibleUI);
			
			uch.updateUIList();
			return true;
			
		} else {
			
			//if ( compatibleUI.addProtocol(protocolShortName, uris, socketName, protocolInfo, icons) ) {
			if ( compatibleUI.addProtocol(protocolShortName, uris, protocolInfo, icons) ) {
				
				uch.updateUIList();
				return true;
				
			} else {
				
				return false;
			}	
		}
	}

	
	/**
	 * Check whether uris are already exists with other target. If exist then return false else return true.
	 * 
	 * @param targetId a String value of targetId
	 * @param uris an object of List&lt;String&gt; specifies uris
	 * 
	 * @return a boolean value
	 */
	private boolean validateURIs(String targetId, List<String> uris) {
		
		if ( (targetId == null) || (uris == null) || (uris.size() == 0) ) 
			return false;
		
		synchronized( targetIdCompatibleUIMap ) {
			
			for ( Entry<String, CompatibleUI> entry : targetIdCompatibleUIMap.entrySet() ) {
				
				String tarId = entry.getKey();
				
				if ( tarId == null )
					continue;
				
				CompatibleUI compatibleUI = entry.getValue();
				
				if ( compatibleUI == null )
					continue;
				
				List<Protocol> protocols = compatibleUI.getProtocols();
				
				if ( (protocols == null) || (protocols.size() == 0) )
					continue;
				
				for ( Protocol protocol : protocols ) {
					
					if ( protocol == null )
						continue;
					
					List<String> uriList = protocol.getUris();
					
					if ( (uriList == null) || (uriList.size() == 0) )
						continue;
					
					for ( String uri : uris ) {
						
						if ( uri == null )
							continue;
						
						// if uri is already exists and targetId is not same then return false.
						if ( uriList.contains(uri) && !targetId.equals(tarId) ) 
							return false;
					}
					
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Remove the specified URIs from Compatible UI list.
	 * 
	 * @param uris an Object of List&lt;String&gt; specifies List of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	void removeCompatibleUIs(List<String> uris)
		throws UCHException {
		
		if ( uris == null ) {
			logger.warning("List of URI is invalid.");
			throw new UCHException();
		}
		
		for(String uri : uris) 
			uriIUIPMMap.remove(uri);
		
		synchronized( targetIdCompatibleUIMap ) {
			
			List<String> targetIdsToRemove = new ArrayList<String>();
			
			for ( Entry<String, CompatibleUI> entry : targetIdCompatibleUIMap.entrySet() ) {
				
				String targetId = entry.getKey();
				CompatibleUI compatibleUI = entry.getValue();
				
				compatibleUI.removeUris(uris);
				
				if ( compatibleUI.getProtocols().size() == 0 )
					targetIdsToRemove.add(targetId);
			}
			
			for ( String targetId : targetIdsToRemove ) 
				targetIdCompatibleUIMap.remove(targetId);
		}
		
	}
	
	/** 
	 * The UIPM requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, the UCH will forward all messages that have this URI as its base, to targetRequest().   
	 * 
	 * @param uipm an object of IUIPM
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(IUIPM uipm, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible)
			throws UCHException {
		
		if ( (uipm == null) || (scheme == null) || (basePath == null) ) {
			logger.warning("uipm, scheme or basePath is invalid.");
			throw new UCHException();
		}
		
		String uri = getAvailableURI(basePath, port, scheme, portIsFlexible);
		
		if(uri == null) 
			return null;
		
		synchronized (uriIUIPMMap) {
			uriIUIPMMap.put(uri, uipm);
		}
		
		return uri;
	}
	
	/** 
	 * The TA requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, the UCH will forward all messages that have this URI as its base, to targetRequest().   
	 * 
	 * @param ta an object of ITA
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(ITA ta, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible)
			throws UCHException {
		
		if ( (ta == null) || (scheme == null) || (basePath == null) ) {
			logger.warning("ta, scheme or basePath is invalid.");
			throw new UCHException();
		}
		
		String uri = getAvailableURI(basePath, port, scheme, portIsFlexible);
		
		if(uri == null) 
			return null;
		
		synchronized (uriITAMap) {
			uriITAMap.put(uri, ta);
		}
		
		return uri;
		
	}
	
	/**
	 * The TA requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, the UCH will forward all messages that have this URI as its base, to targetRequest().   
	 * 
	 * @param ta an object of ITA
	 * @param scheme a String value of scheme
	 * @param port an int value of portNo.
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of UCHException
	 */
	String startUriService(ITDM tdm, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible)
			throws UCHException {
		
		if ( (tdm == null) || (scheme == null) || (basePath == null) ) {
			logger.warning("ta, scheme or basePath is invalid.");
			throw new UCHException();
		}
		
		String uri = getAvailableURI(basePath, port, scheme, portIsFlexible);
		
		if(uri == null) 
			return null;
		
		synchronized (uriITDMMap) {
			uriITDMMap.put(uri, tdm);
		}
		return uri;
		
	}
	
	/**
	 * Return a URI String which is yet not used for start service Uri.
	 * 
	 * @param basePath a String vlaue of basePath
	 * @param port a int value of port No.
	 * @param scheme a String value of scheme
	 * @param portIsFlexible a boolean value
	 * 
	 * @return a String value of URI
	 */
	private String getAvailableURI(String basePath, int port, String scheme, boolean portIsFlexible) {
		
		if (port != portNo) {
			
			if ( !portIsFlexible ) {
				logger.severe("Port is not flexible. And UCH unable to provide service on the port : "+port);
				return null;
			}
			port = portNo;
		}
		
		if ( basePath.startsWith("/") )
			basePath = basePath.substring(1);
		
		String uri = null;
		
		if ( port == 80 )	
			uri = scheme+"://"+ipAddress+"/UCH/"+basePath;	
	    else 
			uri = scheme+"://"+ipAddress+":"+port+"/UCH/"+basePath;
		
		synchronized (uriIUIPMMap) {
			
			synchronized (uriITAMap) {
				
				synchronized (uriITDMMap) {
					
					if ( uriIUIPMMap.containsKey(uri) || uriITAMap.containsKey(uri) || uriITDMMap.containsKey(uri) ) {
						
						if ( !portIsFlexible )
							return null;
						
						
						while ( true ) {
							
							int newPort = getPortNo();
							uri = scheme+"://"+ipAddress+":"+newPort+"/UCH/"+basePath;
							
							if ( !uriIUIPMMap.containsKey(uri) && !uriITAMap.containsKey(uri) && !uriITDMMap.containsKey(uri) ) 
								break;
										
						}
						
					}
				}
			}
		}
		
		return uri;
	}
	
	/**
	 * Stop URI Service on specified URI.
	 * 
	 * @param uipm an object of IUIPM
	 * @param uri a String value of URI
	 */
	void stopUriService(IUIPM uipm, String uri) throws UCHException {
			
		if ( uri == null ) {
			logger.warning("uri is invalid.");
			throw new UCHException();
		}
		
		synchronized (uriIUIPMMap) {
			uriIUIPMMap.remove(uri);
		}
	}
	
	/**
	 * Stop URI Service on specified URI.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 */
	void stopUriService(ITDM tdm, String uri) throws UCHException {
			
		if ( uri == null ) {
			logger.warning("uri is invalid.");
			throw new UCHException();
		}
		
		synchronized (uriITDMMap) {
			uriITDMMap.remove(uri);
		}
	}
	
	/**
	 * Stop URI Service on specified URI.
	 * 
	 * @param ta an object of ITA.
	 * @param uri a String value of URI
	 */
	void stopUriService(ITA ta, String uri) throws UCHException {
			
		if ( uri == null ) {
			logger.warning("uri is invalid.");
			throw new UCHException();
		}
		
		synchronized (uriITAMap) {
			uriITDMMap.remove(uri);
		}
	}
	
	/**
	 * Remove specified UIPM, its services and related compatible UIs.
	 * 
	 * @param uipm an Object of IUIPM
	 */
	void removeUIPM(IUIPM uipm) {
		
		if ( uipm == null ) {
			logger.warning("UIPM is null.");
			return;
		}
		
		List<String> urisToRemove = new ArrayList<String>();
		
		synchronized(uriIUIPMMap) {
			
			for ( Entry<String, IUIPM> entry : uriIUIPMMap.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String uri = entry.getKey();
				IUIPM iUIPM = entry.getValue();
				
				if ( uipm.equals(iUIPM) ) 
					urisToRemove.add(uri);
			}
			
			for ( String uri : urisToRemove )
				uriIUIPMMap.remove(uri);
			
		}
		
		synchronized(uriIUIPMMap) {
			
			for ( String uri : urisToRemove ) 
				uriIUIPMMap.remove(uri);
		}
		
		try {
			removeCompatibleUIs(urisToRemove);
		} catch(UCHException e) { }
	}
	
	/**
	 * Remove specified TDM, its services and related compatible UIs.
	 * 
	 * @param tdm an Object of ITDM
	 */
	void removeTDM(ITDM tdm) throws UCHNotImplementedException {
		
		if ( tdm == null ) {
			logger.warning("TDM is null.");
			return;
		}
		
		List<String> urisToRemove = new ArrayList<String>();
		
		synchronized(uriITDMMap) {
			
			for ( Entry<String, ITDM> entry : uriITDMMap.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String uri = entry.getKey();
				ITDM iTDM = entry.getValue();
				
				if ( tdm.equals(iTDM) ) 
					urisToRemove.add(uri);
			}
			
			for ( String uri : urisToRemove )
				uriITDMMap.remove(uri);
			
		}
		
		synchronized(uriITDMMap) {
			
			for ( String uri : urisToRemove ) 
				uriITDMMap.remove(uri);
		}
		
		try {
			removeCompatibleUIs(urisToRemove);
		} catch(UCHException e) { }
	}
	
	/**
	 * Remove specified TA, its services and related compatible UIs.
	 * 
	 * @param ta an Object of ITA
	 */
	void removeTA(ITA ta) {
		
		if ( ta == null ) {
			logger.warning("TA is null.");
			return;
		}
		
		List<String> urisToRemove = new ArrayList<String>();
		
		synchronized(uriITDMMap) {
			
			for ( Entry<String, ITA> entry : uriITAMap.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String uri = entry.getKey();
				ITA iTA = entry.getValue();
				
				if ( ta.equals(iTA) ) 
					urisToRemove.add(uri);
			}
			
			for ( String uri : urisToRemove )
				uriITAMap.remove(uri);
			
		}
		
		synchronized(uriITAMap) {
			
			for ( String uri : urisToRemove ) 
				uriITAMap.remove(uri);
		}
		
		try {
			removeCompatibleUIs(urisToRemove);
		} catch(UCHException e) { }
	}

	/**
	 * Get the XML string of compatible UIs for specified context.
	 * 
	 * @param hostName a String value of HostName
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return a String value represents Compatible UIs XML 
	 */
	String getCompatibleUIs(String hostName, Map<String, IProfile> context) {
		
		
		String ipAddress = uch.getIpAddress();
		
		if ( (hostName == null) || (targetIdCompatibleUIMap == null) || (targetIdCompatibleUIMap.size() <= 0) || (ipAddress == null) ) {
			
			logger.warning("One or many input parametr(s) is(are) invalid.");
			
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				 + "<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">"
				 + "</uilist>";
		}
		
		List<String> allowedUri = uch.getURIs(context);
		
		
		if ( (allowedUri == null) || (allowedUri.size() == 0) ) {
			
			logger.warning("User is not allowed for any URI.");
			
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			 + "<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">"
			 + "</uilist>";
		}
		
		int portNo = 80;
		
		if ( ipAddress.indexOf(':') != -1 ) {
			
			String pNo = ipAddress.substring( ipAddress.lastIndexOf(':')+1 );
			
			try {
				portNo = Integer.valueOf(pNo);
			} catch (Exception e) {
				
			}
		}
		
		boolean changeHostName = false;
		
		if ( portNo == 80 )
			changeHostName = !ipAddress.equals(hostName);
		else
			changeHostName = !(ipAddress+":"+portNo).equals(hostName);
		
		StringBuilder returnXML = new StringBuilder();
		
		returnXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">");
		
		synchronized( targetIdCompatibleUIMap ) {
			
			for ( CompatibleUI compatibleUI : targetIdCompatibleUIMap.values() ) {
				
				String uiXML = getUiXml(compatibleUI, hostName, allowedUri, changeHostName);
				
				if ( uiXML == null )
					continue;
				
				returnXML.append(uiXML);
			}	
			
		}
		
		returnXML.append("</uilist>");
		
		return returnXML.toString();
	}
	
	/**
	 * Get the XML string of compatible UIs for specified context.
	 * 
	 * @param hostName a String value of HostName
	 * @param queryParams filter parameters
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return a String value represents Compatible UIs XML
	 */
	public String filterCompatibleUIs(String hostName, Map<String, String[]> queryParams,  Map<String, IProfile> context) {
		
		String ipAddress = uch.getIpAddress();
		
		if ( (hostName == null) || (targetIdCompatibleUIMap == null) || (targetIdCompatibleUIMap.size() <= 0) || (ipAddress == null) ) {
			
			logger.warning("One or many input parametr(s) is(are) invalid. Also check targetIdCompatibleUIMap.");
			
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				 + "<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">"
				 + "</uilist>";
		}
		
		List<String> allowedUri = uch.getURIs(context);
		
		if ( (allowedUri == null) || (allowedUri.size() == 0) ) {
			
			logger.warning("User is not allowed for any URI.");
			
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			 + "<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">"
			 + "</uilist>";
		}
		
		int portNo = 80;
		
		if ( ipAddress.indexOf(':') != -1 ) {
			
			String pNo = ipAddress.substring( ipAddress.lastIndexOf(':')+1 );
			
			try {
				portNo = Integer.valueOf(pNo);
			} catch (Exception e) {
				
			}
		}
		
		boolean changeHostName = false;
		
		if ( portNo == 80 )
			changeHostName = !ipAddress.equals(hostName);
		else
			changeHostName = !(ipAddress+":"+portNo).equals(hostName);
		
		StringBuilder returnXML = new StringBuilder();
		
		returnXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<uilist xmlns=\"urn:schemas-upnp-org:remoteui:uilist-1-0\">");
		
		List<String> temp = new ArrayList<String>();
		
		String l[] = queryParams.get(Constants.UILIST_REQUEST_PARAMETER_TARGET_NAME);
		if(l != null && Collections.addAll(temp, l))
			System.out.println("TargetNames in Query Param : " + temp);
		
		l = queryParams.get(Constants.UILIST_REQUEST_PARAMETER_SOCKET_NAME);
		if(l != null && Collections.addAll(temp, l)) 
			System.out.println("SockettNames in Query Param : " + temp);

		l = queryParams.get(Constants.UILIST_REQUEST_PARAMETER_TARGET_ID);
		if(l != null && Collections.addAll(temp, l))
			System.out.println("TargetIDs in Query Param : " + temp);
		
		System.out.println(" => Query Parameters are : "+temp);
		
		synchronized( targetIdCompatibleUIMap ) {
			
			for ( CompatibleUI compatibleUI : targetIdCompatibleUIMap.values() ) {
				
				//check for availability of targetName, socketName and targetId in the UiID.
					String uiid = compatibleUI.getUiId();
					
					String uiidToken[] = uiid.split(" ");
					
					if( queryParams.containsKey(Constants.UILIST_REQUEST_PARAMETER_TARGET_NAME) && !((temp.contains("all") || temp.contains(uiidToken[0]))) ){
						//System.out.println(" TargetName is not matching....");
						continue;
					}

					if( queryParams.containsKey(Constants.UILIST_REQUEST_PARAMETER_SOCKET_NAME) && !((temp.contains("all") || temp.contains(uiidToken[1]))) ){
						//System.out.println(" SocketName is not matching....");
						continue;
					}

					if( queryParams.containsKey(Constants.UILIST_REQUEST_PARAMETER_TARGET_ID) && !((temp.contains("all") || temp.contains(uiidToken[2]))) ){
						//System.out.println(" TargetID is not matching....");
						continue;
					}
					
					String uiXML = getUiXml(compatibleUI, hostName, allowedUri, changeHostName);
					
					if ( uiXML == null )
						continue;
					
					returnXML.append(uiXML);
			}	
			
		}
		
		returnXML.append("</uilist>");
		
		return returnXML.toString();
	}
	
	/**
	 * Get the XML string for specified UI.
	 *  
	 * @param compatibleUI an Object of CompatibleUI
	 * @param hostName a String value of HostName
	 * @param allowedUri an object of List&lt;String&gt; represents allowed URIs for specified User
	 * @param changeHostName a boolean value represents whether go for change hostName or not
	 * 
	 * @return a String value represents UI XML 
	 */
	private String getUiXml(CompatibleUI compatibleUI, String hostName, List<String> allowedUri, boolean changeHostName) {
		
		if ( (compatibleUI == null) || (hostName == null) || (allowedUri == null) || (allowedUri.size() == 0) ) {
			
			logger.warning("One or many input parametr(s) is(are) invalid.");
			return null;
		}
		
		StringBuilder returnXML = new StringBuilder();
		
		String protocolXML = getProtocolXml(compatibleUI.getProtocols(), hostName, allowedUri, changeHostName);
		
		if ( protocolXML == null )
			return null;
		
		returnXML.append("<ui>");
		
			returnXML.append("<uiID>"+CommonUtilities.encodeString(compatibleUI.getUiId())+"</uiID>");
			
			//Parikshit Thakur: 20111123. Modified code for the new structure of UIList
			//String name = uch.getTargetFriendlyName( compatibleUI.getUiId() );
			String tarId = (compatibleUI.getUiId()).substring((compatibleUI.getUiId()).lastIndexOf(" ") + 1);
			String name = uch.getTargetFriendlyName( tarId );
			
			returnXML.append("<name>"+CommonUtilities.encodeString(name)+"</name>");
			
			// 2012-09-24 : Added <fork> tag in UIList with description.'forkValue' should always be true.
			returnXML.append("<fork>true</fork>");
			
			returnXML.append( protocolXML );
		
		returnXML.append("</ui>");
		
		return returnXML.toString();
	}
	
	/**
	 * Get the XML string for specified Protocols.
	 * 
	 * @param protocols an object of List&lt;Protocol&gt;
	 * @param hostName a String value of HostName
	 * @param allowedUri an object of List&lt;String&gt; represents allowed URIs for specified User
	 * @param changeHostName a boolean value represents whether go for change hostName or not
	 * 
	 * @return a String value represents Protocol XML 
	 */
	private String getProtocolXml(List<Protocol> protocols, String hostName, List<String> allowedUri, boolean changeHostName) {
		
		if ( (protocols == null) || (hostName == null) || (allowedUri == null) || (allowedUri.size() == 0) ) {
						logger.warning("One or many input parametr(s) is(are) invalid.");
			return null;
		}

		StringBuilder returnXML = new StringBuilder();
		
		for ( Protocol protocol : protocols ) {
			
			String protocolShortName = protocol.getProtocolShortName();
			
			if ( protocolShortName == null )
				continue;
			
			String uriXML = getUriXml(protocol.getUris(), hostName, allowedUri, changeHostName);
			
			if ( uriXML == null ) 
				continue;
			
			String protocolInfoXml = getProtocolInfoXml(protocol, protocolShortName, hostName, changeHostName);
			
			if ( protocolInfoXml == null )
				continue;
			
			protocolShortName = CommonUtilities.encodeString(protocolShortName);
			
			returnXML.append("<protocol shortName=\""+protocolShortName+"\">");
			
				returnXML.append( uriXML );
				
				returnXML.append(protocolInfoXml);
			
			returnXML.append("</protocol>");
			
		}
		
		String returnXmlString = returnXML.toString().trim();
		
		if ( returnXmlString.equals("") ) 
			return null;
		else
			return returnXmlString;
	}

	/**
	 * Get the XML string for specified URIs.
	 * 
	 * @param uris an object of List&lt;String&gt;
	 * @param hostName a String value of HostName
	 * @param allowedUri an object of List&lt;String&gt; represents allowed URIs for specified User
	 * @param changeHostName a boolean value represents whether go for change hostName or not
	 * 
	 * @return a String value represents URIs XML
	 */
	private String getUriXml(List<String> uris, String hostName, List<String> allowedUri, boolean changeHostName) {
		
		if ( (uris == null) || (uris.size() == 0) || (hostName == null) || (allowedUri == null) || (allowedUri.size() == 0) ) {
			
			logger.warning("One or many input parametr(s) is(are) invalid.");
			return null;
		}
		
		StringBuilder returnXML = new StringBuilder();
		
		for ( String uri : uris ) {
			System.out.println("urx nicht" + uri);
			if ( uri == null )
				continue;
			
			if ( !allowedUri.contains(uri) )
				continue;
			
			if ( changeHostName ) {
				
				if ( portNo == 80)
					returnXML.append("<uri>"+CommonUtilities.encodeString( replaceString(uri, ipAddress, hostName) )+"</uri>");
					//returnXML.append("<uri><![CDATA["+replaceString(uri, ipAddress, hostName) +"]]></uri>");
				else
					returnXML.append("<uri>"+CommonUtilities.encodeString( replaceString(uri, ipAddress+":"+portNo, hostName) )+"</uri>");
					//returnXML.append("<uri><![CDATA["+replaceString(uri, ipAddress+":"+portNo, hostName) +"]]></uri>");
					
			} else {
				returnXML.append("<uri>"+CommonUtilities.encodeString( uri )+"</uri>");
			}
		}
		
		String returnXmlString = returnXML.toString().trim();
		
		if ( returnXmlString.equals("") )
			return null;
		else 
			return returnXmlString;
	}
	
	/**
	 * Get the XML string for specified Protocol class and its values.
	 * 
	 * @param protocol an Object of Protocol
	 * @param protocolShortName a String value of Protocol short name
	 * @param hostName a String value of Host Name
	 * @param changeHostName a boolean value represents whether go for change hostName or not
	 * 
	 * @return a String value represents ProtocolInfo XML.
	 */
	private String getProtocolInfoXml(Protocol protocol, String protocolShortName, String hostName, boolean changeHostName) {
		
		if ( (protocol == null) || (protocolShortName == null) || (hostName == null) ) {
						logger.warning("One or many input parametr(s) is(are) invalid.");
			return null;
		}
		
		
		StringBuilder returnXML = new StringBuilder();
		
		returnXML.append("<protocolInfo>");
		
		//Prepare XML string for UI iconList.

		String iconListXML = getIconListXML(protocol);
		
		if ( iconListXML != null )
			returnXML.append(iconListXML);

		//Parikshit Thakur : 20111123. Modified code for the new structure of UIList
		//for ( String socketName : protocol.getSocketNames() ) {
		//	returnXML.append("<socketName>"+CommonUtilities.encodeString(socketName)+"</socketName>");
		//}
		
		Map<String, List<String>> protocolInfo = protocol.getProtocolInfo();
		
		if ( (protocolInfo != null) && (protocolInfo.size() > 0) ) {
			
			if ( protocolShortName.equals(Constants.PROPERTY_PROTOCOL_SHORT_NAME_VALUE_HTTP_HTML) ) {
				
				//String uipmClientName = CommonUtilities.getFirstItem( protocolInfo.get(Constants.PROP_UIPM_CLIENT_NAME));
				String resName = CommonUtilities.getFirstItem( protocolInfo.get(Constants.PROP_UIPM_CLIENT_NAME));
				
				if ( resName != null ) {
					
					//Parikshit Thakur : 20111123. Modified code for the new structure of UIList
					/*returnXML.append("<uipmClientName");
					
					String uipmClientFriendlyName = CommonUtilities.getFirstItem(protocolInfo.get(Constants.PROP_UIPM_CLIENT_FRIENDLY_NAME));			
					if ( uipmClientFriendlyName != null ) 
						returnXML.append(" friendlyName=\""+CommonUtilities.encodeString(uipmClientFriendlyName)+"\" ");
					
					
					String uipmClientDescription = CommonUtilities.getFirstItem(protocolInfo.get(Constants.PROP_UIPM_CLIENT_DESCRIPTION));
					if ( uipmClientDescription != null ) {
						returnXML.append(" desc=\""+CommonUtilities.encodeString(uipmClientDescription)+"\" ");
					}
					
					returnXML.append(">"+CommonUtilities.encodeString(uipmClientName)+"</uipmClientName>");*/
					
					
					returnXML.append("<resName>" + CommonUtilities.encodeString(resName) + "</resName>");
					String name = CommonUtilities.getFirstItem(protocolInfo.get(Constants.PROP_UIPM_CLIENT_FRIENDLY_NAME));
					if ( name != null ) 
						returnXML.append("<name>" + CommonUtilities.encodeString(name) + "</name>");
					
					String description = CommonUtilities.getFirstItem(protocolInfo.get(Constants.PROP_UIPM_CLIENT_DESCRIPTION));
					if ( description != null )
						returnXML.append("<description>" + CommonUtilities.encodeString(description) + "</description>");
					
				}
				
				for( String key : protocolInfo.keySet() ) {
					
					if ( key == null )
						continue;
					
					List<String> valueList = protocolInfo.get(key);
					
					if ( (valueList == null) || (valueList.size() <= 0) )
						continue;
					
					key = CommonUtilities.encodeString(key);
					
					if ( key.equals(Constants.PROP_UIPM_CLIENT_NAME) 
					   || key.equals(Constants.PROP_UIPM_CLIENT_FRIENDLY_NAME)
					   || key.equals(Constants.PROP_UIPM_CLIENT_DESCRIPTION) )
						continue;
					
					if ( key.equals(Constants.UI_LIST_PROPERTY_NAME_CAP_PROFILE) ) {
						for ( String value : valueList) {
							returnXML.append("<"+CommonUtilities.encodeString(key)+">"+value+"</"+CommonUtilities.encodeString(key)+">");
						}
					} else {
						
						for ( String value : valueList) {
							
								returnXML.append("<"+CommonUtilities.encodeString(key)+">"+CommonUtilities.encodeString(value)+"</"+CommonUtilities.encodeString(key)+">");
						}
					}
				}
				
			} else {
				
				
				//keys are #XML tags, values of valueList (see bellow) are elementbodies, typical examples: targetDescriptionAt or socketdescriptionAt
				for( String key : protocolInfo.keySet() ) {
					
					if ( key == null )
						continue;
					
					List<String> valueList = protocolInfo.get(key);
					
					if ( (valueList == null) || (valueList.size() <= 0) )
						continue;
					
					key = CommonUtilities.encodeString(key);
					
					for ( String value : valueList) {
						
						if ( changeHostName ) { 
							
							if ( portNo == 80)
								returnXML.append("<"+CommonUtilities.encodeString(key)+">"+CommonUtilities.encodeString( replaceString(value, ipAddress, hostName) )+"</"+CommonUtilities.encodeString(key)+">");
							else 
								returnXML.append("<"+CommonUtilities.encodeString(key)+">"+CommonUtilities.encodeString( replaceString(value, ipAddress+":"+portNo, hostName) )+"</"+CommonUtilities.encodeString(key)+">");
						
						} else {
							System.out.println("urx");
							System.out.println("K: " + key);
							System.out.println("v " + value);
							returnXML.append("<"+CommonUtilities.encodeString(key)+">"+CommonUtilities.encodeString(value)+"</"+CommonUtilities.encodeString(key)+">");
						}
						
					}
				}
			}
		}
		
		returnXML.append("</protocolInfo>");
		
		return returnXML.toString();
	}
	
	/**
	 * Get the XML string for specified basic resource icon list for UI.
	 * 
	 * @param protocol an Object of Protocol
	 * @return a String value represents IconLists XML
	 */
	private String getIconListXML(Protocol protocol) {
		
		if( protocol == null )
			return null;
		
		StringBuilder returnXML = new StringBuilder();
		
		List<Map<String, List<String>>> icons = protocol.getIcons();
		
		if( ( icons == null ) || ( icons.size() == 0) )
			return null;
		
		returnXML.append("<iconList>");
		
		for( Map<String, List<String>> iconProp : icons ) {
			
			returnXML.append("<icon>");
				
			if( iconProp.containsKey( Constants.PROPERTY_MIME_TYPE ) ) {
				String mimeType = CommonUtilities.getFirstItem( iconProp.get(Constants.PROPERTY_MIME_TYPE) ) ;
				if( mimeType != null )
					returnXML.append( "<mimetype>" + CommonUtilities.encodeString(mimeType) + "</mimetype>" );
			}		

			if( iconProp.containsKey( Constants.PROPERTY_WIDTH ) ) {
				String width = CommonUtilities.getFirstItem( iconProp.get(Constants.PROPERTY_WIDTH) ) ;
				if( width != null )
					returnXML.append( "<width>" + CommonUtilities.encodeString(width) + "</width>" );
			}
			
			if( iconProp.containsKey( Constants.PROPERTY_HEIGHT ) ) {
				String height = CommonUtilities.getFirstItem( iconProp.get(Constants.PROPERTY_HEIGHT) ) ;
				if( height != null )
					returnXML.append( "<height>" + CommonUtilities.encodeString(height) + "</height>" );
			}	
			
			if( iconProp.containsKey( Constants.PROPERTY_DEPTH )) {
				String depth = CommonUtilities.getFirstItem( iconProp.get(Constants.PROPERTY_DEPTH) ) ;
				if( depth != null )
					returnXML.append( "<depth>" + CommonUtilities.encodeString(depth) + "</depth>" );
			}
			
			if( iconProp.containsKey( Constants.PROPERTY_DC_ELEMENTS_TITLE ) ) {
				String title = CommonUtilities.getFirstItem( iconProp.get(Constants.PROPERTY_DC_ELEMENTS_TITLE) ) ;
				if( title != null )
					returnXML.append( "<title>" + CommonUtilities.encodeString(title) + "</title>" );
			}	
			
			if( iconProp.containsKey( Constants.PROP_NAME_RESOURCE_LOCAL_AT ) ) {				
				String url = CommonUtilities.getFirstItem( iconProp.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) ) ;
				if( url != null )
					returnXML.append( "<url>" + CommonUtilities.encodeString(url) + "</url>" );
			} else if( iconProp.containsKey( Constants.PROP_NAME_GLOBAL_AT ) ) {				
				String url = CommonUtilities.getFirstItem( iconProp.get(Constants.PROP_NAME_GLOBAL_AT) ) ;
				if( url != null )
					returnXML.append( "<url>" + CommonUtilities.encodeString(url) + "</url>" );
			} 			
				
			returnXML.append("</icon>");
		}
		
		returnXML.append("</iconList>");
		
		return returnXML.toString();
	}
	
	/**
	 * Replace specified subString from specified String.
	 * 
	 * @param str a String value
	 * @param oldSubStr a String value of old subString
	 * @param newSubStr a String value of new subString
	 * 
	 * @return a modified String
	 */
	private String replaceString(String str, String oldSubStr, String newSubStr ) {
		
		if ( (str == null) || (oldSubStr == null) || (newSubStr == null) )
			return null;
		
		int index = str.indexOf(oldSubStr);
		
		if ( index == -1 )
			return str;
		
		if ( newSubStr.equals("") ) {
			
			return str.substring(0, index) + newSubStr + str.substring(index+oldSubStr.length());
			
		} else {
			
			while ( index != -1 ) {
				
				str = str.substring(0, index) + newSubStr + str.substring(index+oldSubStr.length());
				
				index = str.indexOf(oldSubStr, index+newSubStr.length() );
			}
			return str;
		}
			
	}
	
	/**
     * Get an integer value of free port on the server.
     * 
     * @return an integer value of portNo.
     */
    private int getPortNo() {
    	
    	try
    	  {
    	    ServerSocket sock = new ServerSocket(0);
    	    int port = sock.getLocalPort();
    	    sock.close();
    	    return port;
    	  }
    	  catch(IOException exc)
    	  {
    		logger.warning("IOException");
    	    return 0;
    	  }
    	
    }
    
    /**
     * Handle the ControllerRequest and call relevant method of relevant TA, TDM or UIPM.
     * 
     * @param baseUri a String value of Base URI
     * @param uri a String value of request URI
     * @param request an Object of HttpServletRequest
     * @param response an Object of HttpServletResponse
     * @throws UIPMFatalException 
     * @throws UIPMNotImplementedException 
     */
    ////Parikshit Thakur : 20111205. Changed signature to serve other-than-http requests.
    //void controllerRequest(String baseUri, String uri, HttpServletRequest request, HttpServletResponse response, Map<String, IProfile> context) {
    void controllerRequest(String baseUri, String uri, Object request, Object response, Map<String, IProfile> context, String protocol) throws UIPMNotImplementedException, UIPMFatalException {
    	
    	if ( uri == null ) {
    		logger.warning("URI is null.");
    		return;
    	}
    	
    	if ( (request == null) || (response == null) ) {
    		logger.warning("Request or Response object is null.");
    		return;
    	}
    	
    	for ( Entry<String, IUIPM> entry : uriIUIPMMap.entrySet() ) {
    		
    		String uriOfMap = entry.getKey();
    		
    		if ( uriOfMap == null )
    			continue;
    		
    		if ( uriOfMap.equals(uri) ) {
    			
    			IUIPM uipm = entry.getValue();
    			
    			if ( uipm == null )
    				continue;
    			
    			//uipm.controllerRequest(request, response, context); //Parikshit Thakur : 20111205. Modified code to specify request protocol.
    			uipm.controllerRequest(protocol, request, response, context);
    			return;
    		}
    	}
    	
    	for ( Entry<String, ITA> entry : uriITAMap.entrySet() ) {
    		
    		String uriOfMap = entry.getKey();
    		
    		if ( uriOfMap == null )
    			continue;
    		
    		if ( uriOfMap.equals(uri) ) {
    			
    			ITA ta = entry.getValue();
    			
    			if ( ta == null )
    				continue;
    			
    			try {
					//ta.targetRequest(request, response, context); //Parikshit Thakur : 20111205. Modified code to specify request protocol.
    				ta.targetRequest(protocol, request, response, context);
				} catch (TAFatalException e) {
					logger.warning("TAFatalException : Handle Target Request.");
				} catch (TANotImplementedException e) {
					logger.warning("TANotImplementedException : Handle Target Request.");
				}
    			
    			return;
    		}
    	}

    	for ( Entry<String, ITDM> entry : uriITDMMap.entrySet() ) {
    		
    		String uriOfMap = entry.getKey();
    		
    		if ( uriOfMap == null )
    			continue;
    		
    		if ( uriOfMap.equals(uri) ) {
    			
    			ITDM tdm = entry.getValue();
    			
    			if ( tdm == null )
    				continue;
    			
				try {
					//tdm.targetRequest(request, response, context); //Parikshit Thakur : 20111205. Modified code to specify request protocol.
					tdm.targetRequest(protocol, request, response, context);
				} catch (TDMFatalException e) {
					logger.warning("TDMFatalException : Handle Target Request.");
				} catch (TDMNotImplementedException e) {
					logger.warning("TDMNotImplementedException : Handle Target Request.");
				}
				
    			return;
    		}
    	}

		logger.info("Error Page URI : " + baseUri + "error.html \n in response of : "+uri);
		((HttpServletResponse)response).setHeader("Location", baseUri + "error.html");
		((HttpServletResponse)response).setStatus(301);
    }

}
