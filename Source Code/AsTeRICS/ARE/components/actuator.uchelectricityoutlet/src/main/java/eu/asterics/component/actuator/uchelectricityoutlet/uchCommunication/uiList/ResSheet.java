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
public class ResSheet {
	private String about;
	private String conformsTo;
	private String retrieveFrom;

	private String forLang;
	private String forDomain;
	
	private ArrayList<String> types;
	
	
	public ResSheet(Document document) throws Exception {
		this.exrtactInfoFromDoc(document);
	}

	private void exrtactInfoFromDoc(Document document) throws Exception {
		ArrayList<String> results;
		
		results = UchCommunicator.executeXpathExpression("/target/resSheet/@about", document);
		if (results.size() > 0) {
			this.about = results.get(0);
		} else {
			this.about = "";
		}

		results = UchCommunicator.executeXpathExpression("/target/resSheet/conformsTo/text()", document);
		if (results.size() > 0) {
			this.conformsTo = results.get(0);
		} else {
			this.conformsTo = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/resSheet/retrieveFrom/text()", document);
		if (results.size() > 0) {
			this.retrieveFrom = results.get(0);
		} else {
			this.retrieveFrom = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/resSheet/scents/forLang/text()", document);
		if (results.size() > 0) {
			this.forLang = results.get(0);
		} else {
			this.forLang = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/resSheet/scents/forDomain/text()", document);
		if (results.size() > 0) {
			this.forDomain = results.get(0);
		} else {
			this.forDomain = "";
		}
		
		this.types = UchCommunicator.executeXpathExpression("/target/resSheet/scents/type/text()", document);
	}

	
	public String getAbout() {
		return about;
	}

	public String getConformsTo() {
		return conformsTo;
	}

	public String getRetrieveFrom() {
		return retrieveFrom;
	}

	public String getForLang() {
		return forLang;
	}

	public String getForDomain() {
		return forDomain;
	}

	public ArrayList<String> getTypes() {
		return types;
	}
	
}
