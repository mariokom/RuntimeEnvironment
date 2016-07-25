package eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements;

import org.w3c.dom.Element;

public class DependencyElement {
	private String ref;
	private String dependency;
	private String dependencyValue;
	
	
	public DependencyElement(String ref, String dependency, String dependencyValue) {
		this.ref = ref;
		this.dependency = dependency;
		this.dependencyValue = dependencyValue;
	}
	
	public DependencyElement() {
		this.ref = "";
		this.dependency = "";
		this.dependencyValue = "";
	}
	
	public DependencyElement(Element element) {
		this.ref = element.getAttribute("ref");
		this.dependency = element.getAttribute("dependency");
		this.dependencyValue = element.getTextContent();
	}
	

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public String getDependencyValue() {
		return dependencyValue;
	}

	public void setDependencyValue(String dependencyValue) {
		this.dependencyValue = dependencyValue;
	}

	@Override
	public String toString() {
		return "DependencyElement [ref=" + ref + ", dependency=" + dependency
				+ ", dependencyValue=" + dependencyValue + "]";
	}
	
	
	
}
