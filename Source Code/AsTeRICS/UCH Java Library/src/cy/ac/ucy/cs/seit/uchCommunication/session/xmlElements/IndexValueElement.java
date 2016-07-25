package cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IndexValueElement {
	private String indexValue;
	private Set<ResourceElement> resourceElements;
	
	
	public IndexValueElement() {
		this.indexValue = "";
		this.resourceElements = new HashSet<ResourceElement>();
	}

	public IndexValueElement(Element element) {
		this.indexValue = element.getTextContent();
		
		this.resourceElements = new HashSet<ResourceElement>();
		NodeList nodeList = element.getElementsByTagName("resource");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ResourceElement resourceElement = new ResourceElement((Element)nodeList.item(i));
			this.resourceElements.add(resourceElement);
		}
	}
	
	public String getIndexValue() {
		return indexValue;
	}
	
	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}
	
	public Set<ResourceElement> getResourceElements() {
		return resourceElements;
	}
	
	public void setResourceElements(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}

	
	@Override
	public String toString() {
		return "IndexValueElement [indexValue=" + indexValue
				+ ", resourceElements=" + resourceElements + "]";
	}
	
}
