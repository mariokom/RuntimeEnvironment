package eu.asterics.component.actuator.uchclb.uchCommunication.session.requests;

import java.util.HashSet;
import java.util.Set;

import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.GetResourceElement;


public class GetResourcesWithPropRequest {
	private Set<GetResourceElement> getResourceElements;
	
	/**
	 * Constructor which takes as parameter the information in order to build the body of the GetResourcesWithPropetries request.
	 * 
	 * @param resourceElements
	 */
	public GetResourcesWithPropRequest(Set<GetResourceElement> resourceElements) {
		this.getResourceElements = resourceElements;
	}
	
	
	/**
	 * Constructor which initializes this object. To use this object in a request you should enter the needed information by
	 * using its methods.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 */
	public GetResourcesWithPropRequest() {
		this.getResourceElements = new HashSet<GetResourceElement>();
	}
	
	/**
	 * Returns all the {@link GetResourceElement} objects that where added.
	 * 
	 * @return
	 */
	public Set<GetResourceElement> getGetResourceElements() {
		return this.getResourceElements;
	}
	
	/**
	 * Sets multiple {@link GetResourceElement} objects.
	 * 
	 * @param getResourceElements
	 */
	public void setGetResourceElements(Set<GetResourceElement> getResourceElements) {
		this.getResourceElements = getResourceElements;
	}
	
	/**
	 * Adds a {@link GetResourceElement} to this object
	 * 
	 * @param getResourceElement
	 */
	public void addGetResourceElement(GetResourceElement getResourceElement) {
		this.getResourceElements.add(getResourceElement);
	}
	
	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * GetResourcesWithProperties request. 
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.	
	 */
	@Override
	public String toString() {
		String text = "";
		
		text += "<getDependencyValues> ";
		
		if (this.getResourceElements != null) {
			for (GetResourceElement getResourceElement: this.getResourceElements) {
				text += getResourceElement.generateXmlForGetResourceWithProp() + " ";
			}
		}
		
		text += "</getDependencyValues>";
		
		return text;
	}
}
