package de.ableitner.vlcapi.url;

public enum AddMrlToPlaylistOptionsEnum {
	
	NO_AUDIO("noaudio"),
	NO_VIDEO("novideo");
	
	
	private String value;
	
	
	AddMrlToPlaylistOptionsEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
