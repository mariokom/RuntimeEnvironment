package eu.asterics.component.actuator.uchclb.uchCommunication.session.requests;

import java.util.HashSet;
import java.util.Set;

import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.GetElement;


/**
 * Class which holds the information needed for the GetDependencyValues request of the URC-HTTP protocol.
 * The toString() method should produce the valid xml data in order to be imported to the
 * GetDependencyValues request. 
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetDependenciesRequest {
	private Set<GetElement> getElements;

	/**
	 * Constructor which takes as parameters the information in order to build the body of the GetDependencies request.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param getElements - {@link Set} of {@link GetElement}
	 */
	public GetDependenciesRequest(Set<GetElement> getElements) {
		this.getElements = getElements;
	}
	
	/**
	 * Constructor which initializes this object. To use this object in a request you should enter the needed information by
	 * using its methods.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 */
	public GetDependenciesRequest() {
		this.getElements = new HashSet<GetElement>();
	}
	
	/**
	 * Returns all the {@link GetElement} objects that where added.
	 * 
	 * @return
	 */
	public Set<GetElement> getGetElements() {
		return getElements;
	}
	
	/**
	 * Sets multiple {@link GetElement} objects.
	 * 
	 * @param getElements
	 */
	public void setGetElements(Set<GetElement> getElements) {
		this.getElements = getElements;
	}
	
	/**
	 * Adds a {@link GetElement} to this object
	 * 
	 * @param getElement
	 */
	public void addGetElement(GetElement getElement) {
		this.getElements.add(getElement);
	}
	
	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * GetElementDependencyValues request. 
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.	
	 */
	@Override
	public String toString() {
		String text = "";
		
		text += "<getDependencyValues> ";
		
		if (this.getElements != null) {
			for (GetElement getElement: this.getElements) {
				text += getElement.genereateXmlForGetDependencyValues() + " ";
			}
		}
		
		text += "</getDependencyValues>";
		
		return text;
	}
}
