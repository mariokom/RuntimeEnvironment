package cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements;

import org.w3c.dom.Element;

public class ResourceElement {

	private String audience;
	private String forLang;
	private String format;
	private String valRef;
	private String date;
	private String type;
	private String forTargetInstance;
	private String publisher;
	private String creator;
	private String opRef;
	private String role;
	private String eltRef;
	private String at;
	
	private String content;
	
	/**
	 * Constructor which initializes all the possible attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param audience
	 * @param forLang
	 * @param format
	 * @param valRef
	 * @param date
	 * @param type
	 * @param forTargetInstance
	 * @param publisher
	 * @param creator
	 * @param content
	 * @param opRef
	 * @param role
	 * @param eltRef
	 * @param at
	 */
	public ResourceElement(String audience, String forLang, String format,
			String valRef, String date, String type, String forTargetInstance,
			String publisher, String creator, String content, String opRef,
			String role, String eltRef, String at) {
		this.audience = audience;
		this.forLang = forLang;
		this.format = format;
		this.valRef = valRef;
		this.date = date;
		this.type = type;
		this.forTargetInstance = forTargetInstance;
		this.publisher = publisher;
		this.creator = creator;
		this.opRef = opRef;
		this.role = role;
		this.eltRef = eltRef;
		this.at = at;
		
		this.content = content;
	}
	
	/**
	 * Constructor which initializes all the possible attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 */
	public ResourceElement() {
		this.audience = "";
		this.forLang = "";
		this.format = "";
		this.valRef = "";
		this.date = "";
		this.type = "";
		this.forTargetInstance = "";
		this.publisher = "";
		this.creator = "";
		this.opRef = "";
		this.role = "";
		this.eltRef = "";
		this.at = "";
		
		this.content = "";
	}
	
	
	/**
	 * Constructor which initializes all the possible attributes of this object by
	 * parsing the given {@link Element}.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 */
	public ResourceElement(Element element) {
		this.audience = element.getAttribute("audience");
		this.forLang = element.getAttribute("forLang");
		this.format = element.getAttribute("format");
		this.valRef = element.getAttribute("valRef");
		this.date = element.getAttribute("date");
		this.type = element.getAttribute("type");
		this.forTargetInstance = element.getAttribute("forTargetInstance");
		this.publisher = element.getAttribute("publisher");
		this.creator = element.getAttribute("creator");
		this.opRef = element.getAttribute("opRef");
		this.role = element.getAttribute("role");
		this.eltRef = element.getAttribute("eltRef");
		this.at = element.getAttribute("at");
		
		this.content = element.getTextContent();
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getForLang() {
		return forLang;
	}

	public void setForLang(String forLang) {
		this.forLang = forLang;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getValRef() {
		return valRef;
	}

	public void setValRef(String valRef) {
		this.valRef = valRef;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForTargetInstance() {
		return forTargetInstance;
	}

	public void setForTargetInstance(String forTargetInstance) {
		this.forTargetInstance = forTargetInstance;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOpRef() {
		return opRef;
	}

	public void setOpRef(String opRef) {
		this.opRef = opRef;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEltRef() {
		return eltRef;
	}

	public void setEltRef(String eltRef) {
		this.eltRef = eltRef;
	}
	
	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}
	
	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * getResource request. 
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation. 
	 * @return
	 */
	public String genereateXmlForGetResource() {
		String text = "";
		
		text = "<getResource eltRef=\""+this.eltRef+"\" valRef=\""+this.valRef+"\" " +
				"opRef=\""+this.opRef+"\" role=\""+this.role+"\" "+
				"type=\""+this.type+"\" format=\""+this.format+"\" "+
				"forLang=\""+this.forLang+"\" forTargetInstance=\""+this.forTargetInstance+"\" "+
				"creator=\""+this.creator+"\" publisher=\""+this.publisher+"\" "+
				"audience=\""+this.audience+"\" />";
		
		return text;
	}
	

	@Override
	public String toString() {
		return "ResourceElement [audience=" + audience + ", forLang=" + forLang
				+ ", format=" + format + ", valRef=" + valRef + ", date="
				+ date + ", type=" + type + ", forTargetInstance="
				+ forTargetInstance + ", publisher=" + publisher + ", creator="
				+ creator + ", opRef=" + opRef + ", role=" + role + ", eltRef="
				+ eltRef + ", at=" + at + ", content=" + content + "]";
	}

}
