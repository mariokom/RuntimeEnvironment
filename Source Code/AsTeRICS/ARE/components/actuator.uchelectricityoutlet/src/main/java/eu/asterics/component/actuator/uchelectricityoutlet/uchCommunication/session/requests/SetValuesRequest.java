package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.requests;

import java.util.HashSet;
import java.util.Set;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.AckElement;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.AddElement;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.InvokeElement;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.RemoveElement;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.SetElement;



/**
 * Class which holds the information needed for the SetValues request of the URC-HTTP protocol.
 * The toString() method should produce the valid xml data in order to be imported to the
 * SetValues request. 
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class SetValuesRequest {
	private Set<SetElement> setElements;
	private Set<InvokeElement> invokeElements;
	private Set<AckElement> ackElements;
	private Set<AddElement> addElements;
	private Set<RemoveElement> removeElements;
	
	/**
	 * Constructor which takes as parameter the information in order to build the body of the SetValues request.
	 * 
	 * @param setElements - {@link Set} of {@link SetElement}
	 * @param invokeElements - {@link Set} of {@link InvokeElement}
	 * @param ackElements - {@link Set} of {@link AckElement}
	 * @param addElements - {@link Set} of {@link AddElement}
	 * @param removeElements - {@link Set} of {@link RemoveElement}
	 */
	public SetValuesRequest(Set<SetElement> setElements, Set<InvokeElement> invokeElements, 
			Set<AckElement> ackElements, Set<AddElement> addElements, Set<RemoveElement> removeElements) {
		this.setElements = setElements;
		this.invokeElements = invokeElements;
		this.ackElements = ackElements;
		this.addElements = addElements;
		this.removeElements = removeElements;
	}
	
	/**
	 * Constructor which initializes this object.
	 */
	public SetValuesRequest() {
		this.setElements = new HashSet<SetElement>();
		this.invokeElements = new HashSet<InvokeElement>();
		this.ackElements = new HashSet<AckElement>();
		this.addElements = new HashSet<AddElement>();
		this.removeElements = new HashSet<RemoveElement>();
	}

	public Set<SetElement> getSetElements() {
		return setElements;
	}
	
	public void setSetElements(Set<SetElement> setElements) {
		this.setElements = setElements;
	}
	
	public void addSetElement(SetElement setElement) {
		this.setElements.add(setElement);
	}
	
	public Set<InvokeElement> getInvokeElements() {
		return invokeElements;
	}
	
	public void setInvokeElements(Set<InvokeElement> invokeElements) {
		this.invokeElements = invokeElements;
	}
	
	public void addInvokeElement(InvokeElement invokeElement) {
		this.invokeElements.add(invokeElement);
	}
	
	public Set<AckElement> getAckElements() {
		return ackElements;
	}
	
	public void setAckElements(Set<AckElement> ackElements) {
		this.ackElements = ackElements;
	}
	
	public void addAckElement(AckElement ackElement) {
		this.ackElements.add(ackElement);
	}
	
	public Set<AddElement> getAddElements() {
		return addElements;
	}
	
	public void setAddElements(Set<AddElement> addElements) {
		this.addElements = addElements;
	}
	
	public void addAddElement(AddElement addElement) {
		this.addElements.add(addElement);
	}
	
	public Set<RemoveElement> getRemoveElements() {
		return removeElements;
	}
	
	public void setRemoveElements(Set<RemoveElement> removeElements) {
		this.removeElements = removeElements;
	}
	
	public void addRemoveElement(RemoveElement removeElements) {
		this.removeElements.add(removeElements);
	}

	/**
	 * This method should produce the valid xml data in order to be imported to the
	 * SetValues request.
	 * 
	 * For more information you can examine the URC-HTTP protocol documentation.	
	 */
	@Override
	public String toString() {
		String text = "";
		
		text += "<setValues>" + " ";
		
		if (this.setElements != null) {
			for (SetElement setElement: this.setElements) {
				text += setElement.genereateXmlForSetValues() + " ";
			}
		}
		
		if (this.invokeElements != null) {
			for (InvokeElement invokeElement: this.invokeElements) {
				text += invokeElement.genereateXmlForSetValues() + " ";
			}
		}
		
		if (this.ackElements != null) {
			for (AckElement ackElement: this.ackElements) {
				text += ackElement.genereateXmlForSetValues() + " ";
			}
		}
		
		if (this.addElements != null) {
			for (AddElement addElement: this.addElements) {
				text += addElement.genereateXmlForSetValues() + " ";
			}
		}
		
		if (this.removeElements != null) {
			for (RemoveElement removeElement: this.removeElements) {
				text += removeElement.genereateXmlForSetValues() + " ";
			}
		}
		
		text += "</setValues>";
		
		return text;
	}
	
}
