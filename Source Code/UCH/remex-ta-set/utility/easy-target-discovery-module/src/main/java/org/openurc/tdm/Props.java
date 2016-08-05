package org.openurc.tdm;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Props {
	
	List<Prop> prop;
	
	public List<Prop> getProp() {
		return prop;
	}

	public void setProp(List<Prop> prop) {
		this.prop = prop;
	}

	public Props() {
		prop = new LinkedList<Prop>();
	}

	public void addProp(Prop p) {
		prop.add(p);
		
	}
	

}
