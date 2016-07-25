package cy.ac.ucy.cs.seit.uchCommunication.uiList;

import java.util.ArrayList;

import org.w3c.dom.Document;

import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;

/**
 * Class that holds the URC's TargetDescription element information.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class TargetDescription {
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
	
	private String socketId;
	private String socketName;
	private String socketHidden;
	
	private ResSheet resSheet;
	
	
	public TargetDescription(String uri) throws Exception {
		this.document = UchCommunicator.getDocumentFromURI(uri);
		
		this.exrtactInfoFromUri();
	}
	
	
	public void exrtactInfoFromUri() throws Exception {
		ArrayList<String> results;
		
		results = UchCommunicator.executeXpathExpression("/target/@id", document);
		if (results.size() > 0) {
			this.id = results.get(0);
		} else {
			this.id = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/@about", document);
		if (results.size() > 0) {
			this.about = results.get(0);
		} else {
			this.about = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/@schemaLocation", document);
		if (results.size() > 0) {
			this.schemaLocation = results.get(0);
		} else {
			this.schemaLocation = "";
		}

		results = UchCommunicator.executeXpathExpression("/target/@hidden", document);
		if (results.size() > 0) {
			this.hidden = results.get(0);
		} else {
			this.hidden = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/description/text()", document);
		if (results.size() > 0) {
			this.description = results.get(0);
		} else {
			this.description = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/conformsTo/text()", document);
		if (results.size() > 0) {
			this.conformsTo = results.get(0);
		} else {
			this.conformsTo = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/publisher/text()", document);
		if (results.size() > 0) {
			this.publisher = results.get(0);
		} else {
			this.publisher = "";
		}
				
		results = UchCommunicator.executeXpathExpression("/target/creator/text()", document);
		if (results.size() > 0) {
			this.creator = results.get(0);
		} else {
			this.creator = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/created/text()", document);
		if (results.size() > 0) {
			this.created = results.get(0);
		} else {
			this.created = "";
		}

		results = UchCommunicator.executeXpathExpression("/target/modified/text()", document);
		if (results.size() > 0) {
			this.modified = results.get(0);
		} else {
			this.modified = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/rights/text()", document);
		if (results.size() > 0) {
			this.rights = results.get(0);
		}
		else {
			this.rights = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/contributor/text()", document);
		this.contributors = results;
		
		
		results = UchCommunicator.executeXpathExpression("/target/socket/@id", document);
		if (results.size() > 0) {
			this.socketId = results.get(0);
		} else {
			this.socketId = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/socket/@name", document);
		if (results.size() > 0) {
			this.socketName = results.get(0);
		} else {
			this.socketName = "";
		}
		
		results = UchCommunicator.executeXpathExpression("/target/socket/@hidden", document);
		if (results.size() > 0) {
			this.socketHidden = results.get(0);
		} else {
			this.socketHidden = "";
		}
		
		this.resSheet = new ResSheet(document);
	}
	
	
	/* Getters */
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

	
	public String getSocketId() {
		return socketId;
	}

	public String getSocketName() {
		return socketName;
	}

	public String getSocketHidden() {
		return socketHidden;
	}

	public ResSheet getResSheet() {
		return resSheet;
	}
	
	
}
