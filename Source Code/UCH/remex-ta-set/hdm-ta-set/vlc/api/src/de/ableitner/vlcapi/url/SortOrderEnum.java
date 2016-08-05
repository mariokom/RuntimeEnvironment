package de.ableitner.vlcapi.url;

public enum SortOrderEnum {

	
	NORMAL_ORDER("0"),
	REVERSE_ORDER("1");
	
	
	private String value;
	
	
	SortOrderEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
