package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.uiList;

import java.util.ArrayList;

import org.w3c.dom.Document;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchCommunicator;

/**
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class SocketDescriptionVariable {
	private String id;
	private String type;
	private String write;
	private String relevant;
	private String description;
	private String descriptionLanguage;
	
	
	@SuppressWarnings("unused")
	private SocketDescriptionVariable() { }
	
	
	public SocketDescriptionVariable(String id, Document document) throws Exception {
		this.id = id;

		this.exrtactInfoFromDocument(id, document);
	}
	
	
	private void exrtactInfoFromDocument(String id, Document document) throws Exception {
		
		ArrayList<String> results;
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable[@id='"+id+"']/@type", document);
		if (results.size() > 0) {
			this.type = results.get(0);
		}
		else {
			this.type = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable[@id='"+id+"']/dependency/write/text()", document);
		if (results.size() > 0) {
			this.write = results.get(0);
		}
		else {
			this.write = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable[@id='"+id+"']/dependency/relevant/text()", document);
		if (results.size() > 0) {
			this.relevant = results.get(0);
		}
		else {
			this.relevant = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable[@id='"+id+"']/description/text()", document);
		if (results.size() > 0) {
			this.description = results.get(0);
		}
		else {
			this.description = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable[@id='"+id+"']/description/@lang", document);
		if (results.size() > 0) {
			this.descriptionLanguage = results.get(0);
		}
		else {
			this.descriptionLanguage = "";
		}
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getWrite() {
		return write;
	}
	
	public String getRelevant() {
		return relevant;
	}
	
	public String getDescription() {
		return description;
	}

	public String getDescriptionLanguage() {
		return descriptionLanguage;
	}
	
}
