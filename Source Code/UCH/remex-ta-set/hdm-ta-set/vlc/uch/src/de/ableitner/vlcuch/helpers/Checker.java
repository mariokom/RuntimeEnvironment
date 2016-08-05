package de.ableitner.vlcuch.helpers;

public class Checker {
	
	
	
	
	public static void checkNullAndEmptiness(String stringToCheck, String parameterName){
		Checker.checkNull(stringToCheck, parameterName);
		Checker.checkEmptiness(stringToCheck, parameterName);
	}
	
	public static void checkNull(Object objectToCheck, String parameterName){
		if(objectToCheck == null){
			throw new NullPointerException("The parameter " + parameterName + " must not be null!");
		}
	}
	
	public static void checkEmptiness(String stringToCheck, String parameterName){
		if(stringToCheck.isEmpty()){
			throw new RuntimeException("The parameter " + parameterName + " must not be empty!");
		}
	}
	
	public static void checkPositiveInteger(int intToCheck, String parameterName){
		if(intToCheck < 0){
			throw new RuntimeException("The parameter " + parameterName + " must be greater euqals 0!");
		}
	}
	
	public static void checkIntegerGreater(int intToCheck, String parameterName, int minimum){
		if(intToCheck < minimum){
			throw new RuntimeException("The parameter " + parameterName + " must be greater " + minimum + "!");
		}
	}
	
	public static void checkIntegerGreaterEquals(int intToCheck, String parameterName, int minimum){
		if(intToCheck <= minimum){
			throw new RuntimeException("The parameter " + parameterName + " must be greater euqals " + minimum + "!");
		}
	}
}
