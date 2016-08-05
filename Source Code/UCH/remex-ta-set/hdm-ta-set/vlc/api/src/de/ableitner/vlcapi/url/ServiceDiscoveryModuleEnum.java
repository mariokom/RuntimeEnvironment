package de.ableitner.vlcapi.url;

public enum ServiceDiscoveryModuleEnum {
	
	SAP("sap"),
	SHOUTCAST("shoutcast"),
	PODCAST("podcast"),
	HAL("hal");
	
	
	private String value;
	
	
	ServiceDiscoveryModuleEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
