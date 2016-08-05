package org.openurc.tdm;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TargetList {
List<Target> target;

public List<Target> getTarget() {
	return target;			
}

public void setTarget(List<Target> target) {
	this.target = target;
}

public TargetList() {
		
target = new LinkedList<Target>();
}

public void addTarget(Target newTarget) {
	
	this.target.add(newTarget);
}

public int getSize() {
	
	return target.size();
}

}
