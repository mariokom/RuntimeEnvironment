package eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements;

/**
 * AckElement class to hold URC-HTTP protocol-specific information.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class AckElement {
	private String ref;
	private String value;
	
	/**
	 * Constructor which initializes the attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param ref - the notify path
	 * @param value - the value
	 */
	public AckElement(String ref, String value) {
		this.ref = ref;
		this.value = value;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String genereateXmlForSetValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<ack ref=\""+this.ref+"\">" + this.value + "</ack>";
		
		return text;
	}

	@Override
	public String toString() {
		return "AckElement [ref=" + ref + ", value=" + value + "]";
	}
	
}
