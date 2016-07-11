package cy.ac.ucy.cs.seit.uchCommunication.uiList;


import java.util.ArrayList;

import org.w3c.dom.Document;

import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;

/**
 * Class that holds the URC's Protocol element information (Protocol element exists inside a UI element).
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class Protocol {
	private String uri;
	private String shortName;
	private ProtocolInfo protocolInfo;
	
	public static final String URC_HTTP = "URC-HTTP";
	public static final String HTTP_HTML = "HTTP/HTML";
	
	@SuppressWarnings("unused")
	private Protocol() {}
	
	protected Protocol(String uiId, String shortName, Document uiListDocument) throws Exception {
		ArrayList<String> results;
		
		this.shortName = shortName;
		
		results = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/protocol[@shortName='"+shortName+"']/uri/text()", uiListDocument);
		if (results.size() > 0) {
			this.uri = results.get(0);
		}
		else {
			this.uri = "";
		}
		
		if (shortName.equals(Protocol.URC_HTTP)) {
			this.protocolInfo = new UrcHttpProtocolInfo(uiId, shortName, uiListDocument);
		}
		else if (shortName.equals(Protocol.HTTP_HTML)) {
			this.protocolInfo = new HtmlHttpProtocolInfo(uiId, shortName, uiListDocument);
		}
	}

	public String getUri() {
		return uri;
	}

	public String getShortName() {
		return shortName;
	}

	public ProtocolInfo getProtocolInfo() {
		return protocolInfo;
	}
	
	
}
