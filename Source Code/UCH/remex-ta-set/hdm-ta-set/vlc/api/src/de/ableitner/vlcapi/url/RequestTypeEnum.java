package de.ableitner.vlcapi.url;

public enum RequestTypeEnum {
	
	STATUS("status.xml"),
	PLAYLIST("playlist.xml"),
	BROWSE("browse.xml"),
	VLM("vlm.xml"),
	VLM_CMD("vlm_cmd.xml");
	
	
	private String value;
	
	
	RequestTypeEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
