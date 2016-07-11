package eu.asterics.component.actuator.uchclb.uchCommunication.session.responses;

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

import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.EltElement;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.IndexElement;

/**
 * Class created to parse and store the getValues response from the server.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetValuesResponse {
	private Set<EltElement> eltElements;
	private Set<IndexElement> indexElements;
	
	/**
	 * The constructor of this class which expects the response of the server as a string.
	 * If the xml parsing fails, the attributes of this class will be null.
	 * 
	 * @param response - the string response that was returned by the server
	 */
	public GetValuesResponse(String response) {
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse( new InputSource( new ByteArrayInputStream(response.getBytes("utf-8"))) );
		} catch (ParserConfigurationException | SAXException | IOException e) {
			eltElements = null;
			indexElements = null;
			e.printStackTrace();
			return;
		}
		
		this.eltElements = new HashSet<EltElement>();
		this.indexElements = new HashSet<IndexElement>();
		
		Element rootElement = document.getDocumentElement();

		//iterate and parse elt elements
		NodeList nodeList = rootElement.getElementsByTagName("elt");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			EltElement eltElement = new EltElement((Element)nodeList.item(i));
			this.eltElements.add(eltElement);
		}
		
		//iterate and parse index elements
		nodeList = rootElement.getElementsByTagName("index");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			IndexElement indexElement = new IndexElement((Element)nodeList.item(i));
			this.indexElements.add(indexElement);
		}
	}

	public Set<EltElement> getEltElements() {
		return eltElements;
	}

	public void setEltElements(Set<EltElement> eltElements) {
		this.eltElements = eltElements;
	}

	public Set<IndexElement> getIndexElements() {
		return indexElements;
	}

	public void setIndexElements(Set<IndexElement> indexElements) {
		this.indexElements = indexElements;
	}

	
	@Override
	public String toString() {
		return "GetValuesResponse [eltElements=" + eltElements
				+ ", indexElements=" + indexElements + "]";
	}
	
	
	
	
	
}
