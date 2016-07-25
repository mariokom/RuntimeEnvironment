package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements;

/**
 * GetElement class to hold URC-HTTP protocol-specific information.
 * 
 * For more information you can examine the URC-HTTP protocol documentation.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 *
 */
public class GetElement {

	private String ref;
	private int depth;
	private boolean pruneIndices;
	private boolean pruneXMLContent;
	private boolean includeSets;
	private String dependencyType;
	
	/**
	 * Constructor which initializes all the possible attributes of this object.
	 * 
	 * For more information about these attributes you can examine the URC-HTTP protocol documentation.
	 * 
	 * @param ref - ref (the name or path of the variable)
	 * @param depth - depth
	 * @param pruneIndices - pruneIndices
	 * @param pruneXMLContent - pruneXMLContent
	 * @param includeSets - includeSets
	 * @param dependencyType - dependencyType
	 */
	public GetElement(String path, int depth, boolean pruneIndices,
			boolean pruneXMLContent, boolean includeSets, String dependencyType) {
		this.ref = path;
		this.depth = depth;
		this.pruneIndices = pruneIndices;
		this.pruneXMLContent = pruneXMLContent;
		this.includeSets = includeSets;
		this.dependencyType = dependencyType;
	}
	
	/**
	 * Constructor which initializes only the ref attribute of the GetElement object.
	 * (in many cases this is all we need to specify)
	 * 
	 * @param ref - the ref value (the name or path of the variable)
	 */
	public GetElement(String ref) {
		this.ref = ref;
		this.depth = -1;
		this.pruneIndices = true;
		this.pruneXMLContent = true;
		this.includeSets = true;
		this.dependencyType = "";
	}
	
	protected GetElement() {
		this.ref = "";
		this.depth = -1;
		this.pruneIndices = true;
		this.pruneXMLContent = true;
		this.includeSets = true;
	}

	
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public boolean getPruneIndices() {
		return pruneIndices;
	}
	
	public void setPruneIndices(boolean pruneIndices) {
		this.pruneIndices = pruneIndices;
	}
	
	public boolean getPruneXMLContent() {
		return pruneXMLContent;
	}
	
	public void setPruneXMLContent(boolean pruneXMLContent) {
		this.pruneXMLContent = pruneXMLContent;
	}
	
	public boolean getIncludeSets() {
		return includeSets;
	}
	
	public void setIncludeSets(boolean includeSets) {
		this.includeSets = includeSets;
	}

	public String getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(String dependencyType) {
		this.dependencyType = dependencyType;
	}
	
	
	public String genereateXmlForGetValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<get ref=\""+this.ref+"\" depth=\""+this.depth+"\" " +
				"pruneIndices=\""+this.pruneIndices+"\" pruneXMLContent=\""+this.pruneXMLContent+"\" "+
				"includeSets=\""+this.includeSets+"\" />";
		
		return text;
	}
	
	public String genereateXmlForGetDependencyValues() {
		String text = "";
		
		if ((this.ref == null) || this.ref.equals("")) {
			return "";
		}
		
		text = "<get ref=\""+this.ref+"\" dependency=\""+this.dependencyType+"\" />";
		
		return text;
	}

	@Override
	public String toString() {
		return "GetElement [ref=" + ref + ", depth=" + depth
				+ ", pruneIndices=" + pruneIndices + ", pruneXMLContent="
				+ pruneXMLContent + ", includeSets=" + includeSets
				+ ", dependencyType=" + dependencyType + "]";
	}
	
	
}
