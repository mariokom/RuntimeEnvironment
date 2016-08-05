package de.ableitner.vlcapi.testtool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import de.ableitner.vlcapi.helpers.Checker;

public class TestCase implements ITestCase {

	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================

	private int intKey;
	private String stringKey;
	private String methodName;
	private Parameter[] parameters;
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	public TestCase(int intKey, String stringKey, String methodName){
		this(intKey, stringKey, methodName, null);
	}
	
	public TestCase(int intKey, String stringKey, String methodName, Parameter[] parameters){
		Checker.checkPositiveInteger(intKey, "intKey");
		Checker.checkNullAndEmptiness(stringKey, "stringKey");
		Checker.checkNullAndEmptiness(methodName, "methodName");
		this.intKey = intKey;
		this.stringKey = stringKey;
		this.methodName = methodName;
		this.parameters = parameters;
	}

	
	
		
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Getters and Setters
	// ============================================================================================================================================
	// ============================================================================================================================================

	// ============================================================================================================================================
	// ============================================================================================================================================
	// override Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	@Override
	public Object executeMethod(Object classObject, String className) throws NoSuchMethodException, SecurityException {
		return this.executeMethod(classObject, className, null);
	}

	@Override
	public Object executeMethod(Object classObject, String className, HashMap<String, String> parameters) throws NoSuchMethodException, SecurityException {
		Method[] allMethods = classObject.getClass().getMethods();
		ArrayList<Method> methodsWithCorrectName = new ArrayList<Method>();
		ArrayList<Method> methodsWithCorrectNameAndParameters = new ArrayList<Method>();
		
		for(Method m : allMethods){
			if(m.getName().equals(this.methodName)){
				methodsWithCorrectName.add(m);
			}
		}
		
		Method searchedMethod = null;
		for(Method m : methodsWithCorrectName){
			java.lang.reflect.Parameter[] parametersOfMethodM = m.getParameters();
			if(parametersOfMethodM.length == this.parameters.length){
				boolean match = true;
				for(int i = 0; i < parametersOfMethodM.length; i++){
					if(parametersOfMethodM[i].getName().equalsIgnoreCase(this.parameters[i].getParameterName())){
						if(parametersOfMethodM[i].getType().equals(this.parameters[i].getParameterType()) == false){
							match = false;
						}
					}else{
						match = false;
					}
				}
				if(match){
					searchedMethod = m;
					break;
				}
			}
		}
		
		if(searchedMethod != null){
			System.out.println(searchedMethod.toString());
			//searchedMethod.invoke(obj, args)
		}else{
			System.out.println("No suitable Method was found!");	
		}
		
		
		return null;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// public Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

	// ============================================================================================================================================
	// ============================================================================================================================================
	// private Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

}
