package eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DependenciesElement {

	private String relevant;
	private String write;
	private String insert;
	
	
	public DependenciesElement(String relevant, String write, String insert) {
		this.relevant = relevant;
		this.write = write;
		this.insert = insert;
	}
	
	public DependenciesElement() {
		this.relevant = "";
		this.write = "";
		this.insert = "";
	}
	
	public DependenciesElement(Element element) {
		//should occur exactly once
		NodeList nodeList = element.getElementsByTagName("relevant");
		if (nodeList.getLength() == 1) {
			this.relevant = nodeList.item(0).getTextContent();
		}
		else {
			this.relevant = "";
		}
		
		//should occur exactly once
		nodeList = element.getElementsByTagName("write");
		if (nodeList.getLength() == 1) {
			this.write = nodeList.item(0).getTextContent();
		}
		else {
			this.write = "";
		}
		
		//should occur exactly once
		nodeList = element.getElementsByTagName("insert");
		if (nodeList.getLength() == 1) {
			this.insert = nodeList.item(0).getTextContent();
		}
		else {
			this.insert = "";
		}
	}
	

	public String getRelevant() {
		return relevant;
	}
	
	public void setRelevant(String relevant) {
		this.relevant = relevant;
	}
	
	public String getWrite() {
		return write;
	}
	
	public void setWrite(String write) {
		this.write = write;
	}
	
	public String getInsert() {
		return insert;
	}
	
	public void setInsert(String insert) {
		this.insert = insert;
	}
	
	
	@Override
	public String toString() {
		return "DependenciesElement [relevant=" + relevant + ", write=" + write
				+ ", insert=" + insert + "]";
	}
}
