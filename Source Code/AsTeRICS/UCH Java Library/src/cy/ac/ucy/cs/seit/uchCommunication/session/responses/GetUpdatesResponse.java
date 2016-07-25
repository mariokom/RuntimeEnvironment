package cy.ac.ucy.cs.seit.uchCommunication.session.responses;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.AddElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.ForwardElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.RemoveElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.UpdateElement;

public class GetUpdatesResponse {
	private Set<AddElement> addElements;
	private Set<RemoveElement> removeElements;
	private Set<UpdateElement> updateElements;
	private Set<ForwardElement> forwardElements;
	private String abortSession;
	
	public GetUpdatesResponse() {
		this.addElements = new HashSet<AddElement>();
		this.removeElements = new HashSet<RemoveElement>();
		this.updateElements = new HashSet<UpdateElement>();
		this.forwardElements = new HashSet<ForwardElement>();
		this.abortSession = "";
	}
	
	/**
	 * The constructor of this class which expects the response of the server as a string.
	 * If the xml parsing fails, the attributes of this class will be null.
	 * 
	 * @param response
	 */
	public GetUpdatesResponse(String response) {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse( new InputSource( new ByteArrayInputStream(response.getBytes("utf-8"))) );
		} catch (ParserConfigurationException | SAXException | IOException e) {
			this.addElements = null;
			this.removeElements = null;
			this.updateElements = null;
			this.forwardElements = null;
			this.abortSession = null;
			e.printStackTrace();
			return;
		}
		
		this.addElements = new HashSet<AddElement>();
		this.removeElements = new HashSet<RemoveElement>();
		this.updateElements = new HashSet<UpdateElement>();
		this.forwardElements = new HashSet<ForwardElement>();
		this.abortSession = "";
		
		Element rootElement = document.getDocumentElement();

		//iterate and parse add elements
		NodeList nodeList = rootElement.getElementsByTagName("add");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			AddElement addElement = new AddElement((Element)nodeList.item(i));
			this.addElements.add(addElement);
		}
		
		//iterate and parse remove elements
		nodeList = rootElement.getElementsByTagName("remove");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			RemoveElement removeElement = new RemoveElement((Element)nodeList.item(i));
			this.removeElements.add(removeElement);
		}
		
		//iterate and parse update elements
		nodeList = rootElement.getElementsByTagName("update");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			UpdateElement updateElement = new UpdateElement((Element)nodeList.item(i));
			this.updateElements.add(updateElement);
		}
		
		//iterate and parse forward elements
		nodeList = rootElement.getElementsByTagName("forward");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ForwardElement forwardElement = new ForwardElement((Element)nodeList.item(i));
			this.forwardElements.add(forwardElement);
		}
		
		//should occur exactly once
		nodeList = rootElement.getElementsByTagName("abortSession");
		if (nodeList.getLength() == 1) {
			this.abortSession = nodeList.item(0).getTextContent();
		}
		else {
			this.abortSession = "";
		}
	}

	public Set<AddElement> getAddElements() {
		return addElements;
	}

	public void setAddElements(Set<AddElement> addElements) {
		this.addElements = addElements;
	}

	public Set<RemoveElement> getRemoveElements() {
		return removeElements;
	}

	public void setRemoveElements(Set<RemoveElement> removeElements) {
		this.removeElements = removeElements;
	}

	public Set<UpdateElement> getUpdateElements() {
		return updateElements;
	}

	public void setUpdateElements(Set<UpdateElement> updateElements) {
		this.updateElements = updateElements;
	}

	public Set<ForwardElement> getForwardElements() {
		return forwardElements;
	}

	public void setForwardElements(Set<ForwardElement> forwardElements) {
		this.forwardElements = forwardElements;
	}

	public String getAbortSession() {
		return abortSession;
	}

	public void setAbortSession(String abortSession) {
		this.abortSession = abortSession;
	}

	@Override
	public String toString() {
		return "GetUpdatesResponse [addElements=" + addElements
				+ ", removeElements=" + removeElements + ", updateElements="
				+ updateElements + ", forwardElements=" + forwardElements
				+ ", abortSession=" + abortSession + "]";
	}
	
	
	
}
