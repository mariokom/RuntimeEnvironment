package org.openurc.tdm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class propTest {
@Test
public void testConstructor(){
	String name = "testName";
			String value = "testValue";
	
	Prop prop = new Prop(name, value);

	assertEquals(name, prop.getName() );
	assertEquals(value, prop.getValue() );
}

@Test
public void testSettersAndGetters(){
	String name = "testName";
	String value = "testValue";

Prop prop = new Prop();

prop.setName(name);;
prop.setValue(value);
assertEquals(name, prop.getName());
assertEquals(value, prop.getValue());
}
}
