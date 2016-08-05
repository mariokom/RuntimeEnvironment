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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Stores relative information for getting Compatible UIs.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class CompatibleUI {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private String name;
	private List<Protocol> protocolList;
	
	private String uiId;
	
	/**
	 * Stores information of all parameters mentioned in arguments.
	 * 
	 * @param uiId a String value of uiId
	 * @param protocolShortName a String value of protocolShortName
	 * @param uris a List&lt;String&gt; of URI's 
	 * @param protocolInfo a Map&lt;String, String&gt; of protocol Information
	 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information
	 */
	 //Parikshit Thakur : 20111123. Changed signature (removed socketName)
	CompatibleUI(String uiId, String protocolShortName,
			List<String> uris, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
		
		this.uiId = uiId;
		
		protocolList = new ArrayList<Protocol>();
		
		Protocol protocol = new Protocol(protocolShortName, uris, protocolInfo, icons);
		protocolList.add(protocol);
		
	}
		
	
	/**
	 * Get ID for User Interface.
	 * 
	 * @return a String value for User Interface ID.
	 */
	String getUiId() {
		return uiId;
	}

	/**
	 * Get Name for the User Interface.
	 * 
	 * @return a String value of User Interface Name.
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Get the list of protocols.
	 * 
	 * @return a List&lt;Protocol&gt;
	 * @see edu.wisc.trace.uch.CompatibleUI.Protocol
	 */
	List<Protocol> getProtocols() {
		return protocolList;
	}

	/**
	 * Adds value of protocol in a particular UI.
	 * 
	 * @param protocolShortName a String value of Protocol Short Name
	 * @param uris List&lt;String&gt; of URI's
	 * @param protocolInfo a Map&lt;String, List&lt;String&gt;&gt; of Protocol Information
	 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information
	 */
	//Parikshit Thakur : 20111123. Changed signature (removed socketName)
	//boolean addProtocol(String protocolShortName, List<String> uris, String socketName, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
	boolean addProtocol(String protocolShortName, List<String> uris, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
		
		if ( (protocolShortName == null) || (uris == null) ||(protocolInfo == null) ) {
			logger.warning("One or more parameter value is null.");
			return false;
		}
		
		if ( !checkProtocolExists(protocolShortName, uris, protocolInfo) ) {
			
			Protocol protocol = getMatchedProtocol(uris);
			
			if ( protocol != null ) {
				
				synchronized (protocolList) {
					protocol.update(protocolShortName, protocolInfo, icons);
				}
				logger.info("Existing protocol is updated.");
				
			} else {
				
				protocol = new Protocol(protocolShortName, uris, protocolInfo, icons);
				
				synchronized (protocolList) {
					protocolList.add(protocol);
				}
				
				logger.info("New Protocol is added");
				//logger.info("Protocol not Exists.");
			}
			
			return true;
		} else {
			
			return false;
			//logger.info("Protocol Exists.");
		}
		
	}
	
	/**
	 * Get the Object of Protocol if all URIs are exists else return null.
	 * 
	 * @param uris an Object of List&lt;String&gt; representing URIs
	 * 
	 * @return an Object of Protocol
	 */
	private Protocol getMatchedProtocol(List<String> uris) {
		
		if ( uris == null )
			return null;
		
		for ( Protocol protocol : protocolList ) {
			
			if ( protocol == null )
				continue;
			
			List<String> availableUris = protocol.getUris();
			
			if ( availableUris == null )
				continue;
			
			boolean uriMatched = true;
			
			for ( String uri : uris ) {
				
				if ( !availableUris.contains(uri) ) {
					uriMatched = false;
					break;
				}
			}
			
			if ( uriMatched )
				return protocol;	
		}
		
		return null;
	}
	
	/**
	 * Check whether specified protocol is already exists or not.
	 * 
	 * @param protocolShortName a String value of protocol short name
	 * @param uris an object of List&lt;String&gt; specifies URIs
	 * @param protocolInfo an object of Map&lt;String, List&lt;String&gt;&gt; specifies Protocol Info
	 * 
	 * @return a boolean value specifies whether specified protocol is already exists or not.
	 */
	//Parikshit Thakur : 20111123. Changed signature (removed socketName)
	//private boolean checkProtocolExists(String protocolShortName, List<String> uris, List<String> socketNames, Map<String, List<String>> protocolInfo) {
	private boolean checkProtocolExists(String protocolShortName, List<String> uris, Map<String, List<String>> protocolInfo) {
	
		if ( (protocolShortName == null) || (uris == null) || (protocolInfo == null) )
			return false;
		
		for( Protocol protocol : protocolList ) {
			
			if ( !protocol.getProtocolShortName().equals(protocolShortName) )
				continue;
			
			List<String> availableUris = protocol.getUris();
			
			if ( availableUris == null )
				continue;
			
			boolean uriMatched = true;
			
			for( String uri : uris ) {
				
				if ( !availableUris.contains(uri) ) {
					uriMatched = false;
					break;
				}
			}
			
			if ( !uriMatched ) 
				continue;
			
			//Parikshit Thakur: 20111123. Modified (commented) code for the new structure of UIList
			/*List<String> availableSocketNames = protocol.getSocketNames();
			
			if ( availableSocketNames == null )
				continue;
			
			boolean socketNameMatched = true;
			
			for( String socketName : socketNames ) {
				
				if ( !availableSocketNames.contains(socketName) ) {
					socketNameMatched = false;
					break;
				}
			}

			if ( !socketNameMatched )
				continue;*/
			
			Map<String, List<String>> availableProtocolInfo = protocol.getProtocolInfo();
			
			if ( availableProtocolInfo == null )
				continue;
			
			boolean protocolInfoMatched = true;
			
			for ( String key : protocolInfo.keySet() ) {
			
				if( !availableProtocolInfo.containsKey(key) ) {
					protocolInfoMatched = false;
					break;
				}
				
				List<String> availableValueList = availableProtocolInfo.get(key);
				
				if ( availableValueList == null ) {
					protocolInfoMatched = false;
					break;
				}
				
				List<String> valueList = protocolInfo.get(key);
				
				if ( valueList == null )
					continue;
				
				boolean valueMatched = true;
				for ( String value : valueList ) {
					
					if ( !availableValueList.contains(value) ) {
						valueMatched = false;
						break;
					}
				}
				
				if( !valueMatched ) {
					protocolInfoMatched = false;
					break;
				}
				
				if ( !availableProtocolInfo.get(key).equals(protocolInfo.get(key)) ) {
					protocolInfoMatched = false;
					break;
				}
				
			}
			
			if ( !protocolInfoMatched )
				continue;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove the uri of Protocol matched with the uriList specified by 'uris'
	 * 
	 * @param uris a List&lt;String&gt; of URI String
	 */
	void removeUris(List<String> uris) {
		
		List<Protocol> protocolsToRemove = new ArrayList<Protocol>();
		
		for ( Protocol protocol : protocolList ) {
			
			List<String> protocolUris = protocol.getUris();
			
			for ( String uri : uris ) {
				
				if ( protocolUris.contains(uri))
					protocolUris.remove(uri);
			}
			
			if ( protocolUris.size() == 0 )
				protocolsToRemove.add(protocol);
		}
		
		for ( Protocol protocol : protocolsToRemove )
			protocolList.remove(protocol);
		
	}
	
	
	
	/**
	 * Stores the information for the Protocol to be displayed as Compatible UIs
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $Revision: 798 $
	 */
	class Protocol {
		
		private List<String> uris;
		private Map<String, List<String>> protocolInfo;
		//private List<String> socketNames;    //Parikshit Thakur: 20111123. Modified code for the new structure of UIList 
		private String protocolShortName;
		private List<Map<String, List<String>>> icons;
		
		/**
		 * Stores value of arguments in a Protocol class.
		 * 
		 * @param protocolShortName a String value of protocolShortName
		 * @param uris List&lt;String&gt; of uri
		 * @param protocolInfo a Map&lt;String, String&gt; of protocol Information
		 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information
		 */
		//Parikshit Thakur : 20111123. Changed signature ( removed socketname) 
		Protocol(String protocolShortName, List<String> uris, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
			
			this.protocolShortName = protocolShortName;
			this.uris = uris;
			//this.socketNames = socketNames;
			
			if ( protocolInfo == null || (protocolInfo.size() <= 0) ) {
				this.protocolInfo = new TreeMap<String, List<String>>();
			}
			
			this.protocolInfo = new TreeMap<String, List<String>>();
			
			for ( String key : protocolInfo.keySet() ) {
				this.protocolInfo.put(key, protocolInfo.get(key));
			}
			
			if( this.icons == null)
				this.icons = new ArrayList<Map<String,List<String>>>();
			
			if( icons != null && icons.size() != 0)
				for ( Map<String, List<String>> icon : icons ) {
					this.icons.add(icon);
				}
			
		}
	
		/**
		 * Update state of Protocol object as par the argument values.
		 * 
		 * @param protocolShortName a String value of protocolShortName
		 * @param protocolInfo a Map&lt;String, String&gt; of protocol Information
		 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information		 
		 *  
		 */
		//Parikshit Thakur : 20111123. Changed signature (removed socketName)
		//private void update(String protocolShortName, List<String> socketNames, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
		private void update(String protocolShortName, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons) {
			
			this.protocolShortName = protocolShortName;
			//this.socketNames = socketNames;  
			
			if ( protocolInfo == null || (protocolInfo.size() <= 0) ) {
				this.protocolInfo = new TreeMap<String, List<String>>();
			}
			
			this.protocolInfo = new TreeMap<String, List<String>>();
			
			for ( String key : protocolInfo.keySet() ) {
				this.protocolInfo.put(key, protocolInfo.get(key));
			}
			
			if( this.icons == null)
				this.icons = new ArrayList<Map<String,List<String>>>();
			
			if( icons != null && icons.size() != 0)
				for ( Map<String, List<String>> icon : icons ) {
					this.icons.add(icon);
				}
			
		}
		/**
		 * Get protocolShortName.
		 * 
		 * @return a String value of protocolShortName.
		 */
		String getProtocolShortName() {
			return protocolShortName;
		}

		/**
		 * Get list of URIs.
		 * 
		 * @return a List&lt;String&gt; for uri.
		 */
		List<String> getUris() {
			return uris;
		}

		/**
		 * Get list of socket Name.
		 * 
		 * @return a List&lt;String&gt; for socket name.
		 */
		
		/*List<String> getSocketNames() {
			return socketNames;
		}*/
		
		/**
		 * Get information regarding protocol.
		 * 
		 * @return a Map&lt;String, String&gt;
		 */
		Map<String, List<String>> getProtocolInfo() {
			return protocolInfo;
		}
		
		/**
		 * Get UI basic resource icon information.
		 * 
		 * @return a List&lt;Map&lt;String, String&gt;&gt;
		 */
		List<Map<String, List<String>>> getIcons(){
			return icons;			
		}
		
	}

}
