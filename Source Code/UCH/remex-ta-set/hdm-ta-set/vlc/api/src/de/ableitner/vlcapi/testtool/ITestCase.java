package de.ableitner.vlcapi.testtool;

import java.util.HashMap;

public interface ITestCase {
	
	public Object executeMethod(Object classObject, String className) throws NoSuchMethodException, SecurityException;
	
	public Object executeMethod(Object classObject, String className, HashMap<String, String> parameters) throws NoSuchMethodException, SecurityException;
	
}
