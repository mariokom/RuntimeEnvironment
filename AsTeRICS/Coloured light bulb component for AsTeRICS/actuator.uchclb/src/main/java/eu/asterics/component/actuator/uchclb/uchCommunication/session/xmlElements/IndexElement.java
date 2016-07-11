package eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class IndexElement {
	private String ref;
	private Set<IndexValueElement> indexValueElements;
	
	public IndexElement() {
		this.ref = "";
		this.indexValueElements = new HashSet<IndexValueElement>();
	}
	
	public IndexElement(Element element) {
		this.ref = element.getAttribute("ref");
		
		this.indexValueElements = new HashSet<IndexValueElement>();
		NodeList nodeList = element.getElementsByTagName("indexValue");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			IndexValueElement indexValueElement = new IndexValueElement((Element)nodeList.item(i));
			this.indexValueElements.add(indexValueElement);
		}
	}
	
	
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public Set<IndexValueElement> getIndexValueElements() {
		return indexValueElements;
	}
	
	public void setIndexValueElements(Set<IndexValueElement> indexValueElements) {
		this.indexValueElements = indexValueElements;
	}

	
	@Override
	public String toString() {
		return "IndexElement [ref=" + ref + ", indexValueElements="
				+ indexValueElements + "]";
	}
	
	
}
