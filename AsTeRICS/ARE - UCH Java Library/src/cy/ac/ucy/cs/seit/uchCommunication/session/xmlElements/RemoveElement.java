package cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements;

import org.w3c.dom.Element;

/**
 * RemoveElement class to hold URC-HTTP protocol-specific information.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class RemoveElement {
	private String ref;
	private String hasDynRes;
	private String socketEltType;
	private String notifyCat;

	/**
	 * Constructor which initializes the attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param ref
	 * @param hasDynRes
	 * @param socketEltType
	 * @param notifyCat
	 */
	public RemoveElement(String ref, String hasDynRes, String socketEltType,
			String notifyCat) {
		this.ref = ref;
		this.hasDynRes = hasDynRes;
		this.socketEltType = socketEltType;
		this.notifyCat = notifyCat;
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
	public RemoveElement(Element element) {
		this.ref = element.getAttribute("ref");
		this.hasDynRes = element.getAttribute("hasDynRes");
		this.socketEltType = element.getAttribute("socketEltType");
		this.notifyCat = element.getAttribute("notifyCat");	
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
	

	public String genereateXmlForSetValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<remove ref=\""+this.ref+"\"/>";
		
		return text;
	}

	@Override
	public String toString() {
		return "RemoveElement [ref=" + ref + ", hasDynRes=" + hasDynRes
				+ ", socketEltType=" + socketEltType + ", notifyCat="
				+ notifyCat + "]";
	}

}
