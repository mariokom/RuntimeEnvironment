package eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.ResourceElement;

public class ResourceSetElement {
	private String start;
	private String count;
	private String total;
	private Set<ResourceElement> resourceElements;
	
	public ResourceSetElement() {
		this.start = "";
		this.count = "";
		this.total = "";
		this.resourceElements = new HashSet<ResourceElement>();
	}
	

	public ResourceSetElement(Element element) {
		this.start = element.getAttribute("start");
		this.count = element.getAttribute("count");
		this.total = element.getAttribute("total");
		
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


	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Set<ResourceElement> getResourceElements() {
		return resourceElements;
	}

	public void setResourceElements(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}


	@Override
	public String toString() {
		return "ResourceSetElement [start=" + start + ", count=" + count
				+ ", total=" + total + ", resourceElements=" + resourceElements
				+ "]";
	}
	
	
}
