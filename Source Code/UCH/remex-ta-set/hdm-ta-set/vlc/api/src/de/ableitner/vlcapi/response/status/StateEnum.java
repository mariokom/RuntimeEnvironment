package de.ableitner.vlcapi.response.status;

public enum StateEnum {
	
	PLAYING, PAUSED, STOPPED;
	
	
	public static StateEnum convertStringToState(String stateAsString){
		StateEnum state = null;
		String stateAsStringLowerCase = stateAsString.toLowerCase();
		switch(stateAsStringLowerCase){
		case "playing":
			state = StateEnum.PLAYING;
			break;
		case "paused":
			state = StateEnum.PAUSED;
			break;
		case "stopped":
			state = StateEnum.STOPPED;
			break;
		default:
			throw new RuntimeException("The parameter stateAsString with the value " + stateAsString + " could not be converted to a state!");
		}
		return state;
	}
}
