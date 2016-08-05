package org.openurc.tdm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class SimpleTDMTest {

	static String test = "test";
	@Test
public void testConvertMap(){
	Map<String,Object> testMap = new HashMap<>();
	testMap.put(test,  "${uch.resources}");
	testMap = EasyTDM.convertTargetProps(testMap);
	String converted = testMap.get(test).toString();
	
	String expected = "file:///" + System.getProperty("user.dir") + "/resources";
expected = expected	.replace("\\", "/");
assertEquals(expected, converted);	
	
}
}
