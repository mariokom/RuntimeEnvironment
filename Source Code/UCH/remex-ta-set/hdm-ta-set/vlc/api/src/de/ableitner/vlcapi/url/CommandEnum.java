package de.ableitner.vlcapi.url;

import java.util.ArrayList;

public enum CommandEnum {
	
	ADD_SUBTITLE("addsubtitle", 1, 1, new String[]{"val"}),
	
	ADD_MRL_AND_PLAY("in_play", 1, 2, new String[]{"input", "option"}),
	ADD_MRL_AND_DO_NOT_PLAY("in_enqueue", 1, 1, new String[]{"input"}),
	DELETE_ITEM_FROM_PLAYLIST("pl_delete", 1, 1, new String[]{"id"}),
	EMPTY_PLAYLIST("pl_empty", 0, 0),
	SORT_PLAYLIST("pl_sort", 2, 2, new String[]{"id", "val"}),
	
	PLAY("pl_play", 0, 1, new String[]{"id"}),
	STOP("pl_stop", 0, 0),
	RESUME_PLAYBACK("pl_forceresume", 0, 0),
	PAUSE_PLAYBACK("pl_forceoause", 0, 0),
	JUMP_TO_NEXT_ITEM("pl_next", 0, 0),
	JUMP_TO_PREVIOUS_ITEM("pl_previous", 0, 0),
	
	TOGGLE_PAUSE("pl_pause", 0, 1, new String[]{"id"}),
	TOGGLE_RANDOM_PLAYBACK("pl_random", 0, 0),
	TOGGLE_LOOP("pl_loop", 0, 0),
	TOGGLE_REPEAT("pl_repeat", 0, 0),
	TOGGLE_ENABLE_SERVICE_DISCOVERY_MODULE("pl_sd", 1, 1, new String[]{"val"}),
	TOGGLE_FULLSCREEN("fullscreen", 0, 0),
	
	SET_AUDIO_DELAY("audiodelay", 1, 1, new String[]{"val"}),
	SET_SUBTITLE_DELAY("subdelay", 1, 1, new String[]{"val"}),
	SET_PLAYBACK_RATE("rate", 1, 1, new String[]{"val"}),
	SET_VOLUME("volume", 1, 1, new String[]{"val"}),
	SET_ASPECTRATIO("aspectratio", 1, 1, new String[]{"val"}),
	SEEK("seek", 1, 1, new String[]{"val"}),
	
	SELECT_TITLE("title", 1, 1, new String[]{"val"}),
	SELECT_CHAPTER("chapter", 1, 1, new String[]{"val"}),
	SELECT_AUDIO_TRACK("audio_track", 1, 1, new String[]{"val"}),
	SELECT_VIDEO_TRACK("video_track", 1, 1, new String[]{"val"}),
	SELECT_SUBTITLE_TRACK("subtitle_track", 1, 1, new String[]{"val"});
	
	
	
	
	private String value;
	private int numberOfRequiredParameters;
	private int numberOfAllowedParameters;
	private ArrayList<String> validParameterNames;
	
	
	CommandEnum(String value, int numberOfRequiredParameters, int numberOfAllowedParameters){
		this.value = value;
		this.numberOfRequiredParameters = numberOfRequiredParameters;
		this.numberOfAllowedParameters = numberOfAllowedParameters;
		this.validParameterNames = null;
	}
	
	CommandEnum(String value, int numberOfRequiredParameters, int numberOfAllowedParameters, String[] validParameterNames){
		this.value = value;
		this.numberOfRequiredParameters = numberOfRequiredParameters;
		this.numberOfAllowedParameters = numberOfAllowedParameters;
		this.validParameterNames = new ArrayList<>();
		for(String n : validParameterNames){
			this.validParameterNames.add(n);
		}
	}
	
	
	String getValue(){
		return this.value;
	}
	
	int getNumberOfRequiredParameters(){
		return this.numberOfRequiredParameters;
	}
	
	int getNumberOfAllowedParameters(){
		return this.numberOfAllowedParameters;
	}
	
	// TODO change visibility to protected
	public ArrayList<String> getValidParameterNames(){
		return this.validParameterNames;
	}
	
}
