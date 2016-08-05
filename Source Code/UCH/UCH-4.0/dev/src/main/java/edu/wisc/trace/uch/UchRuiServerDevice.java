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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Announce the UCH(Universal Control Hub) as a UPNP Server Device.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class UchRuiServerDevice {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String UPNP_DEVICE_DESCRIPTION_FILE_NAME = "UCH-RUIServer.xml";
	private static final String UPNP_SERVICE_DESCRIPTION_FILE_NAME = "UCH-RUIServer-Service.xml";
	
	private ServerDevice serverDevice;
	
	
	/**
	 * Provide the reference of UCH to local variable.
	 * Start the UPNP server device UCH and also start announcing it.
	 * 
	 * @param uch an Object of UCH
	 * @param codebase a String value of codebase
	 * @param ipAddress a String value of IP Address
	 * @param presentationURL a String value of Presentation URL
	 * @throws InvalidDescriptionException Invalid Device Exception
	 * 
	 * @see org.cybergarage.upnp.device.InvalidDescriptionException
	 */
	UchRuiServerDevice(UCH uch, String codebase, String ipAddress, String presentationURL)throws InvalidDescriptionException {
		
		logger.info("Going to get Device files.");
		
		File deviceDescriptionFile = getDeviceDescriptionFile(codebase, ipAddress, presentationURL);
		
		if ( deviceDescriptionFile == null ) {
			logger.info("Unable to get Device Description File.");
			return;
		}
		
		logger.info("Going to start Server Device.");
		
		serverDevice = new ServerDevice(uch, deviceDescriptionFile);
	}
	
	/**
	 * Send update for UI List to the UCH.
	 * 
	 * @param uiList a String value of UCH
	 */
	void updateUIList( String uiList ) {
		
		if ( serverDevice == null )
			return;
		
		serverDevice.updateVariableUIListingUpdate(uiList);
	}
	
	
	/**
	 * Create Device Description File and Service Description File at the location specified by codebase.
	 * And return the object of Device Description File.
	 * 
	 * @param codebase a String value of codebase
	 * @param ipAddress a String value of IP Address
	 * @param presentationURL a String value of Presentation URL
	 * 
	 * @return an Object of Device Description File
	 */
	private File getDeviceDescriptionFile(String codebase,String ipAddress, String presentationURL) {
		
		if ( (codebase == null) || (presentationURL == null) )
			return null;
		
		
		if ( !codebase.endsWith("/") && !codebase.endsWith("\\") )
			codebase = codebase + "/";
		
		String deviceDescFileUriString = (codebase + UPNP_DEVICE_DESCRIPTION_FILE_NAME).replaceAll(" ", "%20");
		
		try {
		
			File deviceDescriptionFile = new File( new URI(deviceDescFileUriString) );
			
			if ( deviceDescriptionFile.exists() )
				deviceDescriptionFile.delete();
			
			PrintWriter pw = new PrintWriter(deviceDescriptionFile);
			pw.print( getDeviceDescriptionFileData(ipAddress, presentationURL) );
			pw.flush();
			pw.close();	
			
			if ( createServiceDescriptionFile(codebase) ) 
				return deviceDescriptionFile;
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Parsing Device Description File '"+deviceDescFileUriString+"'.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.warning("FileNotfoundException : Parsing Device Description File '"+deviceDescFileUriString+"'.");
		} 
		
		return null;
	}
	
	/**
	 * Create a Service Description File at the location specified by codebase.
	 * 
	 * @param codebase a String value of codebase
	 * 
	 * @return whether the Service Sescription File created successfully or not.
	 */
	private boolean createServiceDescriptionFile(String codebase) {
		
		if ( codebase == null )
			return false;
		
		String serviceDescFileUriString = (codebase + UPNP_SERVICE_DESCRIPTION_FILE_NAME).replaceAll(" ", "%20");
		
		try {
			
			File serviceDescriptionFile = new File( new URI( serviceDescFileUriString ) );
			
			if ( serviceDescriptionFile.exists() )
				serviceDescriptionFile.delete();
			
			PrintWriter pw = new PrintWriter(serviceDescriptionFile);
			pw.print( getServiceDescriptionFileData() );
			pw.flush();
			pw.close();	
			
			return true;
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Parsing Service Description File '"+serviceDescFileUriString+"'.");
		} catch (FileNotFoundException e) {
			logger.warning("FileNotfoundException : Parsing Service Description File '"+serviceDescFileUriString+"'.");
		} 
		
		return false;
	}
	
	

	
	/**
	 * Get the data to write in Device Description File.
	 * 
	 * @param ipAddress a String value of IP Address.
	 * @param presentationURL a String value of Presentation URL.
	 * 
	 * @return a String value
	 */
	private String getDeviceDescriptionFileData(String ipAddress, String presentationURL) {
		
		/*
		//String uniqueNo = getMacAddress();
		String uniqueNo = null;
		
		if ( uniqueNo == null )
			uniqueNo = String.valueOf( (int)(Math.random()*1000000) );
		*/
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root xmlns=\"urn:schemas-upnp-org:device-1-0\">");
			sb.append("<specVersion>");
					sb.append("<major>1</major>");
					sb.append("<minor>0</minor>");
			sb.append("</specVersion>");
			sb.append("<device>");
				sb.append("<deviceType>urn:schemas-upnp-org:device:RemoteUIServerDevice:1</deviceType>");
				sb.append("<friendlyName>Universal Control Hub(UCHj)</friendlyName>");
				sb.append("<manufacturer>Trace Center</manufacturer>");
				sb.append("<manufacturerURL>www.trace.wisc.edu</manufacturerURL>");
				sb.append("<modelDescription>Universal Control Hub Serving Universal Remote Console Protocol</modelDescription>");
				sb.append("<modelName>UCHj Release 1.1</modelName>");
				sb.append("<modelNumber>http://openurc.org/tools/UCHj</modelNumber>");
				sb.append("<modelURL>URL to model site</modelURL>");
				sb.append("<serialNumber>"+ipAddress+"</serialNumber>");
				sb.append("<UDN>uuid:uch:"+ipAddress+"</UDN>");
				sb.append("<serviceList>");
					sb.append("<service>");
						sb.append("<serviceType>urn:schemas-upnp-org:service:RemoteUIServer:1</serviceType>");
						sb.append("<serviceId>urn:upnp-org:serviceId:RemoteUIServer1</serviceId>");
						sb.append("<SCPDURL>/"+UPNP_SERVICE_DESCRIPTION_FILE_NAME+"</SCPDURL>");
						sb.append("<controlURL>/control</controlURL>");
						sb.append("<eventSubURL>/eventSub</eventSubURL>");
					sb.append("</service>");
				sb.append("</serviceList>");
				sb.append("<presentationURL>"+presentationURL+"</presentationURL>");
			sb.append("</device>");
		sb.append("</root>");
				
		return sb.toString();
	}
	
	/**
	 * Get the data to write in Service Description File.
	 * 
	 * @return a String value
	 */
	private String getServiceDescriptionFileData() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">");
			sb.append("<specVersion>");
				sb.append("<major>1</major>");
				sb.append("<minor>0</minor>");
			sb.append("</specVersion>");
			sb.append("<actionList>");
				sb.append("<action>");
					sb.append("<name>GetCompatibleUIs</name>");
					sb.append("<argumentList>");
						sb.append("<argument>");
							sb.append("<name>InputDeviceProfile</name>");
							sb.append("<direction>in</direction>");
							sb.append("<relatedStateVariable>A_ARG_TYPE_DeviceProfile</relatedStateVariable>");
						sb.append("</argument>");
						sb.append("<argument>");
							sb.append("<name>UIFilter</name>");
							sb.append("<direction>in</direction>");
							sb.append("<relatedStateVariable>A_ARG_TYPE_String</relatedStateVariable>");
						sb.append("</argument>");
						sb.append("<argument>");
							sb.append("<name>UIListing</name>");
							sb.append("<direction>out</direction>");
							sb.append("<relatedStateVariable>A_ARG_TYPE_CompatibleUIs</relatedStateVariable>");
						sb.append("</argument>");
					sb.append(" </argumentList>");
				sb.append("</action>");
			sb.append("</actionList>");
			sb.append("<serviceStateTable>");
				sb.append("<stateVariable sendEvents=\"yes\">");
					sb.append("<name>UIListingUpdate</name>");
					sb.append("<dataType>string</dataType>");
				sb.append("</stateVariable>");
				sb.append("<stateVariable sendEvents=\"no\">");
					sb.append("<name>A_ARG_TYPE_DeviceProfile</name>");
					sb.append("<dataType>string</dataType>");
				sb.append("</stateVariable>");
				sb.append("<stateVariable sendEvents=\"no\">");
					sb.append("<name>A_ARG_TYPE_URI</name>");
					sb.append("<dataType>uri</dataType>");
				sb.append("</stateVariable>");
				sb.append("<stateVariable sendEvents=\"no\">");
					sb.append("<name>A_ARG_TYPE_CompatibleUIs</name>");
					sb.append("<dataType>string</dataType>");
				sb.append("</stateVariable>");
				sb.append("<stateVariable sendEvents=\"no\">");
					sb.append("<name>A_ARG_TYPE_String</name>");
					sb.append("<dataType>string</dataType>");
				sb.append("</stateVariable>");
				sb.append("<stateVariable sendEvents=\"no\">");
					sb.append("<name>A_ARG_TYPE_Int</name>");
					sb.append("<dataType>int</dataType>");
				sb.append("</stateVariable>");
			sb.append("</serviceStateTable>");
		sb.append("</scpd>");
		
		return sb.toString();
	}
	
	
	/**
	 * Create a UPNP Server Device and start announce it on local network.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $Revision: 798 $
	 */
	private class ServerDevice extends Device implements ActionListener, QueryListener {
		
		private UCH uch;
		
		private static final String UPNP_STATE_VARIABLE_UI_LISTING_UPDATE = "UIListingUpdate" ;
		private static final String UPNP_ACTION_GET_COMPATIBLE_UIS = "GetCompatibleUIs" ;
		private static final String UPNP_ACTION_ARGUMENT_UI_LISTING = "UIListing" ; 
		@SuppressWarnings("unused")
		private static final String UPNP_ACTION_ARGUMENT_UI_FILTER = "UIFilter" ;
		
		/**
		 * Constructor.
		 * Create a UPNP Server Device and start announce it on local network.
		 * 
		 * @param uch an Object of UCH
		 * @param deviceDescriptionFile an Object of Device Description File
		 * @throws InvalidDescriptionException Invalid Device Exception
		 * 
		 * @see org.cybergarage.upnp.device.InvalidDescriptionException
		 */
		ServerDevice(UCH uch, File deviceDescriptionFile)throws InvalidDescriptionException {
			
			super(deviceDescriptionFile);
			
			this.uch = uch;
			
			logger.info("UCH:UchRuiServerDevice started");
			
			getAction(UPNP_ACTION_GET_COMPATIBLE_UIS).setActionListener(this);
			
			ServiceList serviceList = getServiceList();
			Service service = serviceList.getService(0);
			service.setQueryListener(this);
			
			//uiListingUpdate = this.getStateVariable(UPNP_STATE_VARIABLE_UI_LISTING_UPDATE);
			setLeaseTime(60);
			this.start();
			
			new UpnpDeviceAnnounceThread(this,10000).start();
		}
		
		private void updateVariableUIListingUpdate(String uiList) {
			
			try {
				
				StateVariable uiListingUpdate = getStateVariable(UPNP_STATE_VARIABLE_UI_LISTING_UPDATE);
				
				if ( uiListingUpdate == null )
					return;
				
				uiListingUpdate.setValue(uiList);
				
			} catch (Exception e) {
				
			}
		}
		
		/**
		 * @see org.cybergarage.upnp.control.ActionListener#actionControlReceived(org.cybergarage.upnp.Action)
		 */
		public boolean actionControlReceived(Action action) {
			
			if ( action == null )
				return false;
			
			logger.info( "UCH:UCH Device Action called: "+action.getName() );
			
			if ( action.getName().equalsIgnoreCase(UPNP_ACTION_GET_COMPATIBLE_UIS) ) {
				
				Argument uiFilter = action.getArgument(UPNP_ACTION_ARGUMENT_UI_FILTER);
				
				if ( uiFilter == null ) 
					return false;
				
				Argument uiListing = action.getArgument(UPNP_ACTION_ARGUMENT_UI_LISTING);

				if ( uiListing == null )
					return false;
				
				String compatibleUIs = getCompatibleUIs( uiFilter.getValue() );
				
				if ( compatibleUIs == null )
					return false;
				
				logger.info("UCH:Protocol String: "+compatibleUIs);
				
				uiListing.setValue(compatibleUIs);
				
				return true;			
			}
			
			return false;		
		}

		/**
		 * Get the UIList after applying the filter.
		 * If the return String is null then the format of filter is invalid.
		 * If the return String is empty String then no matched UI found.
		 * 
		 * @param filter a String value of filter.
		 * 
		 * @return a String value of filtered UIs
		 */
		private String getCompatibleUIs(String filter) {
			
			if ( uch == null )
				return "";
			
			if ( filter == null )
				return null;
			
			//filter = filter.trim();
			
			if ( filter.equals("") )
				return null;
			
			String compatibleUIs = uch.getCompatibleUIs();
			
			if ( filter.equals("*") )
				return compatibleUIs;
			
			StringTokenizer filterST = new StringTokenizer(filter, ",");
			
			List<String> filters = new ArrayList<String>();
			
			while ( filterST.hasMoreTokens() ) {
				
				String nextToken = filterST.nextToken();
				
				if ( nextToken.indexOf('=') == -1 )
					return null;
				
				filters.add( nextToken );
			}

			if ( compatibleUIs == null )
				return "";
			
			compatibleUIs = compatibleUIs.trim();
			
			String uiListCopy = compatibleUIs;
			
			if ( uiListCopy.indexOf("<uilist") == -1 )
				return "";
			
			if ( uiListCopy.indexOf("</uilist>") == -1 )
				return "";
		
			uiListCopy = uiListCopy.substring(uiListCopy.indexOf("<uilist>")+("<uilist>").length(), uiListCopy.indexOf("</uilist>") );
			
			List<String> uiList = new ArrayList<String>();
			
			while ( (uiListCopy.indexOf("<ui>") != -1) && (uiListCopy.indexOf("</ui>") != -1) ) {
				
				uiList.add(uiListCopy.substring( uiListCopy.indexOf("<ui>"), uiListCopy.indexOf("</ui>")+("</ui>").length()) );
				
				uiListCopy = uiListCopy.substring( uiListCopy.indexOf("</ui>")+("</ui>").length() );
			}
			
			List<String> returnUIs = new ArrayList<String>();
			
			for ( String ui : uiList ) {
				
				boolean isFiltered = true;
				
				for ( String f : filters ) {
					
					String name = f.substring( 0, f.indexOf('=') );
					String value = f.substring( f.indexOf('=')+1 );
					
					if ( value.trim().equals("") || (value.indexOf('=') != -1) ) // invalid filter
						return null;
					
					List<String> values = null;
					
					if ( name.indexOf('@') == -1 ) { // node filter
						
						values = getMatchedNodeValues(name, ui);
					
					} else { // attribute filter
						
						String attName = name.substring( name.indexOf('@')+1 );
						name = name.substring(0, name.indexOf('@'));
						
						values = getMatchedAttributeValues(name, attName, ui);
					}
					
					if ( (values == null) || (values.size() == 0) ) { // Check whether node and its value exists
						isFiltered = false;
						break;
					}
					
					if ( value.indexOf('*') == -1 ) { // exact match
						
						if ( !values.contains(value) ) {
							isFiltered = false;
							break;
						}
						
					} else {
						
						if ( value.startsWith("*") && value.endsWith("*") )  {
							
							value = value.substring(1, value.length()-1);
							boolean found = false;
							
							for ( String val : values ) {
								
								if ( val.indexOf(value) != -1 ) {
									found = true;
									break;
								}
							}
							
							if ( !found ) {
								isFiltered = false;
								break;
							}
							
						} else if ( value.startsWith("*") ) {
							
							value = value.substring(1);
							
							boolean found = false;
							
							for ( String val : values ) {
								
								if ( val.endsWith(value) ) {
									found = true;
									break;
								}
							}
							
							if ( !found ) {
								isFiltered = false;
								break;
							}
							
						} else if ( value.endsWith("*") ) {
							
							value = value.substring(0, value.length()-1);
							boolean found = false;
							
							for ( String val : values ) {
								
								if ( val.startsWith(value) ) {
									found = true;
									break;
								}
							}
							
							if ( !found ) {
								isFiltered = false;
								break;
							}
							
						} else {
							
							return null;
						}
						
					}
				}
					
				if ( isFiltered )
					returnUIs.add(ui);
					
			}
			
			StringBuilder returnBuffer = new StringBuilder();
			
			returnBuffer.append("<uilist>");
			
			for ( String ui : returnUIs ) 
				returnBuffer.append(ui);
			
			returnBuffer.append("</uilist>");
			
			return returnBuffer.toString();
		}
		
		/**
		 * Find the matched node value of specified node from specified ui String.
		 * 
		 * @param nodeName a String value of Node Name
		 * @param ui a String value of UI
		 * 
		 * @return an Object of List&lt;String&gt; representing matched node's value
		 */
		private List<String> getMatchedNodeValues(String nodeName, String ui) {
			
			if ( (nodeName == null) || (ui == null) )
				return null;
			
			List<String> values = new ArrayList<String>();
			
			int nodeNameIndex = -1;
			
			while ( (nodeNameIndex = ui.indexOf("<"+nodeName)) != -1 ) {
				
				int slashIndex = ui.indexOf('/', nodeNameIndex);
				int grtIndex = ui.indexOf('>', nodeNameIndex);
				
				if ( slashIndex > grtIndex ) {			
					values.add( ui.substring(grtIndex+1, ui.indexOf('<', grtIndex)) );
					ui = ui.substring( ui.indexOf('>', slashIndex)+1 );
				} else {
					ui = ui.substring( grtIndex+1 );
				}
			}
			
			return values;
		}
		
		/**
		 * Find the matched attribute value of specified node and attribute from specified ui String.
		 * 
		 * @param nodeName a String value of Node Name
		 * @param attName a String value of Attribute Name
		 * @param ui a String value of UI
		 * 
		 * @return an Object of List&lt;String&gt; representing matched attribute's value
		 */
		private List<String> getMatchedAttributeValues(String nodeName, String attName, String ui) {
			
			if ( (nodeName == null) || (attName == null) || (ui == null) )
				return null;
			
			List<String> values = new ArrayList<String>();
			
			int nodeNameIndex = -1;
			
			while ( (nodeNameIndex = ui.indexOf("<"+nodeName)) != -1 ) {
				
				int slashIndex = ui.indexOf('/', nodeNameIndex);
				int grtIndex = ui.indexOf('>', nodeNameIndex);
				
				String attString = null;
				
				if ( slashIndex > grtIndex ) {	
					attString = ui.substring(nodeNameIndex, grtIndex);
					ui = ui.substring( ui.indexOf('>', slashIndex)+1 );
				} else {
					attString = ui.substring(nodeNameIndex, slashIndex);
					ui = ui.substring( grtIndex+1 );
				}
				
				int attIndex = attString.indexOf(attName);
				
				if ( attIndex != -1 ) {
					
					int firstIndex = attString.indexOf('"', attIndex);
					
					if ( firstIndex != -1 ) {
						
						int secondIndex = attString.indexOf('"', firstIndex+1);
						
						if ( secondIndex != -1 ) {
							//logger.info(attString.substring(firstIndex+1, secondIndex)+" : "+firstIndex+" : "+secondIndex);		
							values.add( attString.substring(firstIndex+1, secondIndex) );
						}
					}
					
				}
				
			}

			return values;
		}
		
		/**
		 * @see org.cybergarage.upnp.control.QueryListener#queryControlReceived(org.cybergarage.upnp.StateVariable)
		 */
		public boolean queryControlReceived(StateVariable stateVariable) {
			
			if ( stateVariable == null )
				return false;
			
			stateVariable.setValue("New Value Temp");
			return true;
		}
		
		
		/**
		 * A Thread which keeps Announcing the presence of the UPnP Device in the Network
		 * after each interval of time specified in the Contructor
		 * 
		 * @author Parikshit Thakur & Team, Trace R&D Center
		 * @version $Revision: 798 $
		 */
		class UpnpDeviceAnnounceThread extends Thread {
			
			Device dev;
			int announcePeriodMillisec;
			
			/**
			 * Constructor.
			 * Assign the value of Device and announcePeriodMillisec to local variables.
			 * 
			 * @param dev an Object of Device.
			 * @param announcePeriodMillisec an int value of device announcement time interval.
			 */
			public UpnpDeviceAnnounceThread(Device dev, int announcePeriodMillisec) {
				this.dev = dev;
				this.announcePeriodMillisec = announcePeriodMillisec;
			}
			
			/**
			 * Announce the availability if device after specified time interval.
			 */
			public void run() {

				while (true) {
					
					try {
						dev.announce();
						//logger.info("Announce Device class: "+dev.getClass().getName());
					} catch(Exception e) {
						logger.warning("Exception : While announcing the device.");
						//e.printStackTrace();
					}
					try {
						Thread.sleep(announcePeriodMillisec);
					} catch (InterruptedException e) { 
						logger.warning("InterruptedException");
					}
				}
				
			}
			
		}
	}
}
