package cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements;


import org.w3c.dom.Element;

public class ForwardElement {
	private String type;
	private String targetName;
	private String targetId;
	private String socketName;
	private String authorizationCode;
	
	
	public ForwardElement(String type, String targetName, String targetId,
			String socketName, String authorizationCode) {
		this.type = type;
		this.targetName = targetName;
		this.targetId = targetId;
		this.socketName = socketName;
		this.authorizationCode = authorizationCode;
	}
	
	public ForwardElement() {
		this.type = "";
		this.targetName = "";
		this.targetId = "";
		this.socketName = "";
		this.authorizationCode = "";
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
	public ForwardElement(Element element) {
		this.type = element.getAttribute("type");
		this.targetName = element.getAttribute("targetName");
		this.targetId = element.getAttribute("targetId");
		this.socketName = element.getAttribute("socketName");	
		this.authorizationCode = element.getAttribute("authorizationCode");
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getSocketName() {
		return socketName;
	}

	public void setSocketName(String socketName) {
		this.socketName = socketName;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	@Override
	public String toString() {
		return "ForwardElement [type=" + type + ", targetName=" + targetName
				+ ", targetId=" + targetId + ", socketName=" + socketName
				+ ", authorizationCode=" + authorizationCode + "]";
	}
	
}
