package de.ableitner.vlcapi.url;

public enum AspectratioEnum {
	
	ASPECTRATIO_1_to_1("1:1"),
	ASPECTRATIO_4_to_3("4:3"),
	ASPECTRATIO_5_to_4("5:4"),
	ASPECTRATIO_16_to_9("16:9"),
	ASPECTRATIO_16_to_10("16:10"),
	ASPECTRATIO_221_to_100("221:100"),
	ASPECTRATIO_235_to_100("235:100"),
	ASPECTRATIO_239_to_100("239:100");
	
	
	private String value;
	
	
	AspectratioEnum(String value){
		this.value = value;
	}
	
	
	String getValue(){
		return this.value;
	}
}
