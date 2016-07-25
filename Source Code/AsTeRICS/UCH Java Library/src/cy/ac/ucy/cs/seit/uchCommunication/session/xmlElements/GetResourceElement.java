package cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements;

public class GetResourceElement {
	private String start;
	private String count;
	ResourceElement resourceElement;
	
	public GetResourceElement() {
		this.start = "";
		this.count = "";
		this.resourceElement = null;
	}
	
	public GetResourceElement(String start, String count, ResourceElement resourceElement) {
		this.start = start;
		this.count = count;
		this.resourceElement = resourceElement;
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

	public ResourceElement getResourceElements() {
		return resourceElement;
	}

	public void setResourceElements(ResourceElement resourceElement) {
		this.resourceElement = resourceElement;
	}


	public String generateXmlForGetResourceWithProp() {
		ResourceElement re = this.resourceElement;
		
		if (re == null) {
			return "";
		}
		else {
			return "<getResource start=\""+this.start+"\" count=\""+this.count+"\" " +
					"eltRef=\""+re.getEltRef()+"\" valRef=\""+re.getValRef()+"\" " +
					"opRef=\""+re.getOpRef()+"\" role=\""+re.getRole()+"\" "+
					"type=\""+re.getType()+"\" format=\""+re.getFormat()+"\" "+
					"forLang=\""+re.getForLang()+"\" forTargetInstance=\""+re.getForTargetInstance()+"\" "+
					"creator=\""+re.getCreator()+"\" publisher=\""+re.getPublisher()+"\" "+
					"audience=\""+re.getAudience()+"\" />";
		}
	}
	
}
