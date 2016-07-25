package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.requests;

//TODO TO DELETE IT AT THE END

import java.util.HashSet;
import java.util.Set;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.ResourceElement;



/**
 * Class which holds the information needed for the GetResource request of the URC-HTTP protocol.
 * The toString() method should produce the valid xml data in order to be imported to the
 * GetResource request. 
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetResourceRequest {
	private Set<ResourceElement> resourceElements;

	/**
	 * Constructor which takes as parameters the information in order to build the body of the GetResource request.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param resourceElements - {@link Set} of {@link ResourceElement}
	 */
	public GetResourceRequest(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}
	
	/**
	 * Constructor which initializes this object. To use this object in a request you should enter the needed information by
	 * using its methods.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 */
	public GetResourceRequest() {
		this.resourceElements = new HashSet<ResourceElement>();
	}
	
	/**
	 * Returns all the {@link ResourceElement} objects that where added.
	 * 
	 * @return
	 */
	public Set<ResourceElement> getGetResourceElements() {
		return resourceElements;
	}
	
	/**
	 * Sets multiple {@link ResourceElement} objects.
	 * 
	 * @param resourceElements
	 */
	public void setGetResourceElements(Set<ResourceElement> resourceElements) {
		this.resourceElements = resourceElements;
	}
	
	/**
	 * Adds a {@link ResourceElement} to this object
	 * 
	 * @param resourceElement
	 */
	public void addGetResourceElement(ResourceElement resourceElement) {
		this.resourceElements.add(resourceElement);
	}
	
	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * getResource request. 
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.	
	 */
	@Override
	public String toString() {
		String text = "";
		
		text += "<resourceRequest> ";
		
		if (this.resourceElements != null) {
			for (ResourceElement resourceElement: this.resourceElements) {
				text += resourceElement.genereateXmlForGetResource() + " ";
			}
		}
		
		text += "</resourceRequest>";
		
		return text;
	}
}
