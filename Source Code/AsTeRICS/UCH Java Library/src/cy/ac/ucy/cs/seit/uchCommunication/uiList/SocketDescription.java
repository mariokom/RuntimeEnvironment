package cy.ac.ucy.cs.seit.uchCommunication.uiList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.w3c.dom.Document;

import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;

/**
 * Class that holds the URC's SocketDescription element information.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class SocketDescription {
	private Document document;

	private String id;
	private String about;
	private String schemaLocation; 
	private String hidden;
	private String description;
	private String conformsTo;
	private String publisher;
	private String creator;
	private String created;
	private String modified;
	private String rights;
	private ArrayList<String> contributors; 
	
	private HashMap<String, SocketDescriptionVariable> variables;
	
	
	@SuppressWarnings("unused")
	private SocketDescription() {}
	
	public SocketDescription(String uri) throws Exception {
			this.document = UchCommunicator.getDocumentFromURI(uri);
			
			this.exrtactInfoFromUri();
	}

	public void exrtactInfoFromUri() throws Exception {
		ArrayList<String> results;
				
		results = UchCommunicator.executeXpathExpression("/uiSocket/@id", document);
		if (results.size() > 0) {
			this.id = results.get(0);
		}
		else {
			this.id = "";
		}
				
		results = UchCommunicator.executeXpathExpression("/uiSocket/@about", document);
		if (results.size() > 0) {
			this.about = results.get(0);
		}
		else {
			this.about = "";
		}

		results = UchCommunicator.executeXpathExpression("/uiSocket/@schemaLocation", document);
		if (results.size() > 0) {
			this.schemaLocation = results.get(0);
		}
		else {
			this.schemaLocation = "";
		}

		results = UchCommunicator.executeXpathExpression("/target/@hidden", document);
		if (results.size() > 0) {
			this.hidden = results.get(0);
		}
		else {
			this.hidden = "";
		}
				
		results = UchCommunicator.executeXpathExpression("/uiSocket/description/text()", document);
		if (results.size() > 0) {
			this.description = results.get(0);
		}
		else {
			this.description = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/conformsTo/text()", document);
		if (results.size() > 0) {
			this.conformsTo = results.get(0);
		}
		else {
			this.conformsTo = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/publisher/text()", document);
		if (results.size() > 0) {
			this.publisher = results.get(0);
		}
		else {
			this.publisher = "";
		}

		results = UchCommunicator.executeXpathExpression("/uiSocket/creator/text()", document);
		if (results.size() > 0) {
			this.creator = results.get(0);
		}
		else {
			this.creator = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/created/text()", document);
		if (results.size() > 0) {
			this.created = results.get(0);
		} else {
			this.created = "";
		}

		results = UchCommunicator.executeXpathExpression("/uiSocket/modified/text()", document);
		if (results.size() > 0) {
			this.modified = results.get(0);
		}
		else {
			this.modified = "";
		}
				
		results = UchCommunicator.executeXpathExpression("/uiSocket/rights/text()", document);
		if (results.size() > 0) {
			this.rights = results.get(0);
		}
		else {
			this.rights = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/uiSocket/contributor/text()", document);
		this.contributors = results;
		
		
		this.variables = new HashMap<String, SocketDescriptionVariable>();
		results = UchCommunicator.executeXpathExpression("/uiSocket/variable/@id", document);
		try {
			for (String id: results) {
				this.variables.put(id, new SocketDescriptionVariable(id, document));
			}
		} catch (Exception ex) {
			System.err.println("Error when retrieveing the SD variables. Not severe. Skipping it.");
		}
	}
	

	public Document getDocument() {
		return document;
	}
	
	public String getText() {
		return UchCommunicator.getTextFromDocument(document);
	}
	
	public String getId() {
		return id;
	}

	public String getAbout() {
		return about;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public String getHidden() {
		return hidden;
	}

	public String getDescription() {
		return description;
	}

	public String getConformsTo() {
		return conformsTo;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreated() {
		return created;
	}

	public String getModified() {
		return modified;
	}

	public String getRights() {
		return rights;
	}

	public ArrayList<String> getContributors() {
		return contributors;
	}
	
	public Set<String> getVariablesIds() {
		return variables.keySet();
	}
	
	public SocketDescriptionVariable getVariable(String id) {
		return variables.get(id);
	}
	
}
