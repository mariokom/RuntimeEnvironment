package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList;

import java.util.ArrayList;

import org.w3c.dom.Document;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList.Protocol;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList.ProtocolInfo;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList.SocketDescription;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList.TargetDescription;

/**
 * Class that holds the URC's UrcHttp Protocol  information (UrcHttp Protocol element exists inside a protocolInfo element).
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class UrcHttpProtocolInfo implements ProtocolInfo {
	private String conformsTo;
	
	private String socketDescriptionUri;
	private SocketDescription socketDescription;
	
	private String targetDescriptionUri;
	private TargetDescription targetDescription;
	
	
	/**
	 * To prevent empty object creation
	 */
	@SuppressWarnings("unused")
	private UrcHttpProtocolInfo() { }
	
	public UrcHttpProtocolInfo(String uiId, String shortName, Document uiListDocument) throws Exception {
		ArrayList<String> results;
		
		results = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/protocol[@shortName='"+shortName+"']/protocolInfo/conformsTo/text()", uiListDocument);
		if (results.size() > 0) {
			this.conformsTo = results.get(0);
		}
		else {
			this.conformsTo = "";
		}
		
		String socketDescriptionUri = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/protocol[@shortName='"+Protocol.URC_HTTP+"']/protocolInfo/socketDescriptionAt/text()", uiListDocument).get(0);
		this.socketDescriptionUri = socketDescriptionUri;
		
		String targetDescriptionUri = UchCommunicator.executeXpathExpression("/uilist/ui[uiID='"+uiId+"']/protocol[@shortName='"+Protocol.URC_HTTP+"']/protocolInfo/targetDescriptionAt/text()", uiListDocument).get(0);	
		this.targetDescriptionUri = targetDescriptionUri;
		

		this.socketDescription = new SocketDescription(socketDescriptionUri);
		this.targetDescription = new TargetDescription(targetDescriptionUri);
	}


	
	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}

	public SocketDescription getSocketDescription() {
		return socketDescription;
	}

	public void downloadSocketDescription() throws Exception {
		this.socketDescription = new SocketDescription(socketDescriptionUri);
	}

	public String getSocketDescriptionUri() {
		return socketDescriptionUri;
	}

	public TargetDescription getTargetDescription() {
		return targetDescription;
	}

	public void downloadTargetDescription() throws Exception {
		this.targetDescription = new TargetDescription(targetDescriptionUri);
	}
	
	public String getTargetDescriptionUri() {
		return targetDescriptionUri;
	}

	
	
}
