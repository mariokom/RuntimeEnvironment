package eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.DependenciesElement;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.ResourceElement;


/**
 * AddElement class to hold URC-HTTP protocol-specific information.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class AddElement {
	private String ref;
	private String hasDynRes;
	private String socketEltType;
	private String notifyCat;
	
	private String value;
	private DependenciesElement dependenciesElement;
	private Set<ResourceElement> resourceElements;
	
	
	/**
	 * Constructor which initializes the attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param ref
	 * @param hasDynRes
	 * @param socketEltType
	 * @param notifyCat
	 * @param value
	 * @param dependenciesElement
	 * @param resourceElements
	 */
	public AddElement(String ref, String hasDynRes, String socketEltType,String notifyCat, String value,
			DependenciesElement dependenciesElement, Set<ResourceElement> resourceElements) {
		this.ref = ref;
		this.hasDynRes = hasDynRes;
		this.socketEltType = socketEltType;
		this.notifyCat = notifyCat;
		this.value = value;
		this.dependenciesElement = dependenciesElement;
		this.resourceElements = resourceElements;
	}
	
	public AddElement() {
		this.ref = "";
		this.hasDynRes = "";
		this.socketEltType = "";
		this.notifyCat = "";		
		this.value = "";
		this.dependenciesElement = null;
		this.resourceElements = new HashSet<ResourceElement>();
	}
	
	/**
	 * Constructor of this class which initializes its attributes
	 * by parsing the given element.
	 * If something is not following the URC-HTTP protocol, it will be
	 * initialized with: 
	 * Strings: empty string 
	 * Objects: null
	 * Sets: empty sets
	 * 
	 * @param element
	 */
	public AddElement(Element element) {
		this.ref = element.getAttribute("ref");
		this.hasDynRes = element.getAttribute("hasDynRes");
		this.socketEltType = element.getAttribute("socketEltType");
		this.notifyCat = element.getAttribute("notifyCat");	
		
		//should occur exactly once
		NodeList nodeList = element.getElementsByTagName("value");
		if (nodeList.getLength() == 1) {
			this.value = nodeList.item(0).getTextContent();
		}
		else {
			this.value = "";
		}
		
		//should occur exactly once
		nodeList = element.getElementsByTagName("dependencies");
		if (nodeList.getLength() == 1) {
			this.dependenciesElement = new DependenciesElement( (Element)nodeList.item(0));
		}
		else {
			this.dependenciesElement = null;
		}
		
		this.resourceElements = new HashSet<ResourceElement>();
		nodeList = element.getElementsByTagName("resource");
		for (int i=0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ResourceElement resourceElement = new ResourceElement((Element)nodeList.item(i));
			this.resourceElements.add(resourceElement);
		}
	}

	
	
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getHasDynRes() {
		return hasDynRes;
	}

	public void setHasDynRes(String hasDynRes) {
		this.hasDynRes = hasDynRes;
	}

	public String getSocketEltType() {
		return socketEltType;
	}

	public void setSocketEltType(String socketEltType) {
		this.socketEltType = socketEltType;
	}

	public String getNotifyCat() {
		return notifyCat;
	}

	public void setNotifyCat(String notifyCat) {
		this.notifyCat = notifyCat;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DependenciesElement getDependenciesElement() {
		return dependenciesElement;
	}

	public void setDependenciesElement(DependenciesElement dependenciesElement) {
		this.dependenciesElement = dependenciesElement;
	}

	public Set<ResourceElement> getResourceElements() {
		return resourceElements;
	}

	public void setResourceElements(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}
	

	public String genereateXmlForSetValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<add ref=\""+this.ref+"\">" + this.value + "</add>";
		
		return text;
	}

	@Override
	public String toString() {
		return "AddElement [ref=" + ref + ", hasDynRes=" + hasDynRes
				+ ", socketEltType=" + socketEltType + ", notifyCat="
				+ notifyCat + ", value=" + value + ", dependenciesElement="
				+ dependenciesElement + ", resourceElements="
				+ resourceElements + "]";
	}
	
}