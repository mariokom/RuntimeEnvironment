package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.responses;

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

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.DependencyElement;


/**
 * Class created to parse and store the getDependencyValues response from the server.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetDependenciesResponse {
	private Set<DependencyElement> dependencyElements;
	
	/**
	 * The constructor of this class which expects the response of the server as a string.
	 * If the xml parsing fails, the attributes of this class will be null.
	 * 
	 * @param response
	 */
	public GetDependenciesResponse(String response) {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse( new InputSource( new ByteArrayInputStream(response.getBytes("utf-8"))) );
		} catch (ParserConfigurationException | SAXException | IOException e) {
			this.dependencyElements = null;
			e.printStackTrace();
			return;
		}
		
		this.dependencyElements = new HashSet<DependencyElement>();
		
		Element rootElement = document.getDocumentElement();
		
		//iterate and parse dependency elements
		NodeList nodeList = rootElement.getElementsByTagName("dependency");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			DependencyElement dependencyElement = new DependencyElement((Element)nodeList.item(i));
			this.dependencyElements.add(dependencyElement);
		}
	}

	public Set<DependencyElement> getDependencyElements() {
		return dependencyElements;
	}

	public void setDependencyElements(Set<DependencyElement> dependencyElements) {
		this.dependencyElements = dependencyElements;
	}

	@Override
	public String toString() {
		return "GetDependenciesResponse [dependencyElements="
				+ dependencyElements + "]";
	}
	
}
