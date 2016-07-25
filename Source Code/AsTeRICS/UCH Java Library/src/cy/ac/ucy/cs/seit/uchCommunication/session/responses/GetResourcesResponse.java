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

import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.ResourceElement;

/**
 * Class created to parse and store the GetResourcesResponse response from the server.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetResourcesResponse {
	private Set<ResourceElement> resourceElements;
	
	public GetResourcesResponse() {
		this.resourceElements = new HashSet<ResourceElement>();
	}
	
	/**
	 * The constructor of this class which expects the response of the server as a string.
	 * If the xml parsing fails, the attributes of this class will be null.
	 * 
	 * @param response
	 */
	public GetResourcesResponse(String response) {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse( new InputSource( new ByteArrayInputStream(response.getBytes("utf-8"))) );
		} catch (ParserConfigurationException | SAXException | IOException e) {
			this.resourceElements = null;
			e.printStackTrace();
			return;
		}
		
		this.resourceElements = new HashSet<ResourceElement>();
		
		Element rootElement = document.getDocumentElement();
		
		NodeList nodeList = rootElement.getElementsByTagName("resource");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ResourceElement resourceElement = new ResourceElement((Element)nodeList.item(i));
			this.resourceElements.add(resourceElement);
		}
	}

	public Set<ResourceElement> getResourceElements() {
		return resourceElements;
	}

	public void setResourceElements(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}

	@Override
	public String toString() {
		return "GetResourcesResponse [resourceElements=" + resourceElements
				+ "]";
	}
	
	
}
