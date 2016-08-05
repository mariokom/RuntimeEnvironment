package de.ableitner.vlcapi.url;

public enum SortModeEnum {
	
	ID("Id"),
	NAME("Name"),
	AUTHOR("Author"),
	RANDOM("Random"),
	TRACK_NUMBER("Track number");
	
	
	private String value;
	
	
	SortModeEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
