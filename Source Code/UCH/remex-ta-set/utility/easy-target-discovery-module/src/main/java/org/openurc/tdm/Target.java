package org.openurc.tdm;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Target {
	List<Prop> taProps;
	List<Prop> targetProps;

	public Target() {
		taProps = new LinkedList<Prop>();
		targetProps = new LinkedList<Prop>();
	}

	@XmlElementWrapper
	@XmlElement(name="prop")
public List<Prop> getTaProps() {
	return taProps;
}


public void setTaProps(List<Prop> taProps) {
	this.taProps = taProps;
}

@XmlElementWrapper
@XmlElement(name="prop")
public List<Prop> getTargetProps() {
	return targetProps;
}


public void setTargetProps(List<Prop> targetProps) {
	this.targetProps = targetProps;
}

public Map<String,String> getTaPropsMap(){
	Map<String,String> returnMap = new HashMap<String,String>();
	for (Prop prop : taProps){
		returnMap.put(prop.getName(), prop.getValue() );	
	}
	return returnMap;
}

public Map<String, Object> getTargetPropsMap(){
	Map<String, Object> returnMap = new HashMap<String,Object >();
	for (Prop prop : targetProps){
		returnMap.put(prop.getName(), prop.getValue() );	
	}
	return returnMap;
}

public void addTaProp(Prop taProp){
	this. taProps.add(taProp);
		}

public void addTargetProp(Prop targetProp){
this.targetProps.add(targetProp);
}


}
