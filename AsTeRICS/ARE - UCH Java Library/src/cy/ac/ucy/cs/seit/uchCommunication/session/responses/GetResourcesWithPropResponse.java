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

import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.ResourceSetElement;


public class GetResourcesWithPropResponse {
	private Set<ResourceSetElement> resourceSetElements;
	
	public GetResourcesWithPropResponse() {
		this.resourceSetElements = new HashSet<ResourceSetElement>();
	}
	
	/**
	 * The constructor of this class which expects the response of the server as a string.
	 * If the xml parsing fails, the attributes of this class will be null.
	 * 
	 * @param response
	 */
	public GetResourcesWithPropResponse(String response) {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse( new InputSource( new ByteArrayInputStream(response.getBytes("utf-8"))) );
		} catch (ParserConfigurationException | SAXException | IOException e) {
			this.resourceSetElements = null;
			e.printStackTrace();
			return;
		}
		
		this.resourceSetElements = new HashSet<ResourceSetElement>();
		
		Element rootElement = document.getDocumentElement();
		
		NodeList nodeList = rootElement.getElementsByTagName("resourceSet");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ResourceSetElement resourceSetElement = new ResourceSetElement((Element)nodeList.item(i));
			this.resourceSetElements.add(resourceSetElement);
		}
	}

	public Set<ResourceSetElement> getResourceSetElements() {
		return resourceSetElements;
	}

	public void setResourceSetElements(Set<ResourceSetElement> resourceSetElements) {
		this.resourceSetElements = resourceSetElements;
	}

	@Override
	public String toString() {
		return "GetResourcesWithPropResponse [resourceSetElements="
				+ resourceSetElements + "]";
	}
	
}
