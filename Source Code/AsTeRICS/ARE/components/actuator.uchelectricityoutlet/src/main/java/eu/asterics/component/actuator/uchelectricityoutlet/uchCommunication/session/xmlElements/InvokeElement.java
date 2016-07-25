package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements;

/**
 * InvokeElement class to hold URC-HTTP protocol-specific information.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class InvokeElement {
	private String ref;
	private String invokeMode;
	
	/**
	 * Constructor which initializes the attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param ref - the cmd path
	 * @param invokeMode - the invoke mode
	 */
	public InvokeElement(String ref, String invokeMode) {
		this.ref = ref;
		this.invokeMode = invokeMode;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getInvokeMode() {
		return invokeMode;
	}

	public void setInvokeMode(String invokeMode) {
		this.invokeMode = invokeMode;
	}

	
	public String genereateXmlForSetValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<invoke ref=\""+this.ref+"\">" + this.invokeMode + "</invoke>";
		
		return text;
	}

	@Override
	public String toString() {
		return "InvokeElement [ref=" + ref + ", invokeMode=" + invokeMode + "]";
	}
	
	
}