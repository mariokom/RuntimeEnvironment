package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.w3c.dom.Document;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList.Protocol;

/**
 * Class that holds the UCH's UI information.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class UI {
	private String name;
	private String uiId;
	private String fork;
	
	private HashMap<String, Protocol> protocols;

	/**
	 * To prevent empty object creation
	 */
	@SuppressWarnings("unused")
	private UI() { }
	
	public UI(Document uiListDocument, String uiId) throws Exception {
		ArrayList<String> results;
		
		this.name = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/name/text()", uiListDocument).get(0);
		this.uiId = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/uiID/text()", uiListDocument).get(0);
		this.fork = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/fork/text()", uiListDocument).get(0);
		
		this.protocols = new HashMap<String, Protocol>();
		results = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/protocol/@shortName", uiListDocument);
		for (String protocolShortName: results) {
			Protocol protocol = new Protocol(uiId, protocolShortName, uiListDocument);
			this.protocols.put(protocolShortName, protocol);
		}
		
	}
	

	public String getName() {
		return name;
	}

	public String getUiId() {
		return uiId;
	}

	public String getFork() {
		return fork;
	}

	public Protocol getUrcHttpProtocol() {
		return this.protocols.get(Protocol.URC_HTTP);
	}

	public Protocol getProtocol(String protocolShortName) {
		return this.protocols.get(protocolShortName);
	}
	
	public Set<String> getAllProtocolNames() {
		return this.protocols.keySet();
	}

}
