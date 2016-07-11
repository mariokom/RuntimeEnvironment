package cy.ac.ucy.cs.seit.uchCommunication.session.requests;

import java.util.HashSet;
import java.util.Set;

import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.GetElement;

/**
 * Class which holds the information needed for the GetValues request of the URC-HTTP protocol.
 * The toString() method should produce the valid xml data in order to be imported to the
 * GetValues request. 
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetValuesRequest {
	private boolean emptyUpdateQueue;
	private Set<GetElement> getElements;
	private Set<String> getIndexRefs;

	/**
	 * Constructor which takes as parameters the information in order to build the body of the GetValues request.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param emptyUpdateQueue - attribute: boolean values
	 * @param getElements - {@link Set} of {@link GetElement}
	 * @param getIndexRefs - {@link Set} of Strings
	 */
	public GetValuesRequest(boolean emptyUpdateQueue,
			Set<GetElement> getElements, Set<String> getIndexRefs) {
		this.emptyUpdateQueue = emptyUpdateQueue;
		this.getElements = getElements;
		this.getIndexRefs = getIndexRefs;
	}
	
	/**
	 * Constructor which initializes this object. To use this object in a request you should enter the needed information by
	 * using its methods.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.
	 */
	public GetValuesRequest() {
		this.emptyUpdateQueue = false;
		this.getElements = new HashSet<GetElement>();
		this.getIndexRefs = new HashSet<String>();
	}
	
	/**
	 * Returns the boolean value of the emptyUpdateQueue attribute.
	 * 
	 * @return
	 */
	public boolean isEmptyUpdateQueue() {
		return emptyUpdateQueue;
	}
	
	/**
	 * Sets the boolean value of the emptyUpdateQueue attribute.
	 * 
	 * @return
	 */
	public void setEmptyUpdateQueue(boolean emptyUpdateQueue) {
		this.emptyUpdateQueue = emptyUpdateQueue;
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
	 * Retrieves all the getIndexRef String as a {@link Set}
	 * 
	 * @return
	 */
	public Set<String> getGetIndexRefs() {
		return getIndexRefs;
	}
	
	/**
	 * Sets multiple getIndexRef Strings.
	 */
	public void setGetIndexRefs(Set<String> getIndexRefs) {
		this.getIndexRefs = getIndexRefs;
	}
	
	/**
	 * Adds a getIndexRef String to this object
	 * 
	 * @param getElement
	 */
	public void addGetIndexRef(String getIndexRef) {
		this.getIndexRefs.add(getIndexRef);
	}

	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * GetValues request. 
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.	
	 */
	@Override
	public String toString() {
		String text = "";
		
		text += "<getValues emptyUpdateQueue=\""+this.emptyUpdateQueue+"\">" + " ";
		
		if (this.getElements != null) {
			for (GetElement getElement: this.getElements) {
				text += getElement.genereateXmlForGetValues() + " ";
			}
		}
		
		if (this.getIndexRefs != null) {
			for (String indexRef: this.getIndexRefs) {
				text += "<getIndex ref=\"" + indexRef +"\"/>" + " " ;
			}
		}
		
		text += "</getValues>";
		
		return text;
	}
	
}
