package de.ableitner.vlcapi.url;

import java.util.HashMap;

import de.ableitner.vlcapi.helpers.Checker;

public class RequestUrlCreatorAdapter {
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	private RequestUrlCreator requestUrlCreator;

	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	public RequestUrlCreatorAdapter(String password, String ipAddress, int portNumber) {
		this(new RequestUrlCreator(password, ipAddress, portNumber));
	}
	
	public RequestUrlCreatorAdapter(RequestUrlCreator requestUrlCreator) {
		if (requestUrlCreator == null) {
			throw new NullPointerException("The parameter requestUrlCreator must not be null!");
		}
		this.requestUrlCreator = requestUrlCreator;
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
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// public Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

	public void setPassword(String password) {
		this.requestUrlCreator.setPassword(password);
	}
	
	public String getPassword(){
		return this.requestUrlCreator.getPassword();
	}

	public void setIpAddress(String ipAddress) {
		this.requestUrlCreator.setIpAddress(ipAddress);
	}
	
	public String getIpAddress(){
		return this.requestUrlCreator.getIpAddress();
	}

	public void setPortNumber(int portNumber) {
		this.requestUrlCreator.setPortNumber(portNumber);
	}
	
	public int getPortNumber(){
		return this.requestUrlCreator.getPortNumber();
	}
	
	public String status() {
		return this.requestUrlCreator.createStatusRequestUrl();
	}
	
	public String addSubtitle(String subtitleUri){
		Checker.checkNullAndEmptiness(subtitleUri, "subtitleUri");
		CommandEnum command = CommandEnum.ADD_SUBTITLE;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command, subtitleUri));
	}
	
	public String addMrlToPlaylistAndPlayWithOptions(String mrl, AddMrlToPlaylistOptionsEnum option){
		Checker.checkNullAndEmptiness(mrl, "mrl");
		Checker.checkNull(option, "option");
		CommandEnum command = CommandEnum.ADD_MRL_AND_PLAY;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command, mrl, option.getValue()));
	}
	
	public String addMrlToPlaylist(String mrl, boolean play) {
		this.checkString(mrl, "mrl");
		String url = "";
		if (play) {
			url = this.requestUrlCreator.createStatusRequestUrl(CommandEnum.ADD_MRL_AND_PLAY,
					this.packParameters(CommandEnum.ADD_MRL_AND_PLAY, mrl));
		} else {
			url = this.requestUrlCreator.createStatusRequestUrl(CommandEnum.ADD_MRL_AND_DO_NOT_PLAY,
					this.packParameters(CommandEnum.ADD_MRL_AND_DO_NOT_PLAY, mrl));
		}
		return url;
	}
	
	public String play(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.RESUME_PLAYBACK);
	}
	
	public String pause(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.PAUSE_PLAYBACK);
	}
	
	public String playLastActiveItem(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.PLAY);
	}
	
	public String playItem(int id){
		this.checkPositiveIntegerValue(id, "id");
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.PLAY, this.packParameters(CommandEnum.PLAY, String.valueOf(id)));
	}
	
	public String stop(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.STOP);
	}
	
	public String nextItem(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.JUMP_TO_NEXT_ITEM);
	}
	
	public String previousItem(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.JUMP_TO_PREVIOUS_ITEM);
	}
	
	public String deleteItemFromPlaylist(int id){
		this.checkPositiveIntegerValue(id, "id");
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.DELETE_ITEM_FROM_PLAYLIST, this.packParameters(CommandEnum.DELETE_ITEM_FROM_PLAYLIST, String.valueOf(id)));
	}
	
	public String emptyPlaylist(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.EMPTY_PLAYLIST);
	}
	
	public String sortPlaylist(SortModeEnum sortOrder, SortModeEnum sortMode){
		this.nullCheck(sortOrder, "sortOrder");
		this.nullCheck(sortMode, "sortMode");
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.SORT_PLAYLIST, this.packParameters(CommandEnum.SORT_PLAYLIST, sortOrder.getValue(), sortMode.getValue()));
	}
	
	public String togglePause(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_PAUSE);
	}
	
	public String togglePauseAndPlayIfCurrentStateWasStop(int id){
		this.checkPositiveIntegerValue(id, "id");
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_PAUSE, this.packParameters(CommandEnum.TOGGLE_PAUSE, String.valueOf(id)));
	}
	
	public String toggleRandomPlayback(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_RANDOM_PLAYBACK);
	}
	
	public String toggleLoop(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_LOOP);
	}
	
	public String toggleRepeat(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_REPEAT);
	}
	
	public String toggleEnableServiceDiscoveryModule(ServiceDiscoveryModuleEnum serviceDiscoveryModule){
		this.nullCheck(serviceDiscoveryModule, "serviceDiscoveryModule");
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_ENABLE_SERVICE_DISCOVERY_MODULE);
	}
	
	public String toggleFullscreen(){
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.TOGGLE_FULLSCREEN);
	}
	
	public String setAudioDelay(int audioDelayInSeconds){
		CommandEnum command = CommandEnum.SET_AUDIO_DELAY;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command, String.valueOf(audioDelayInSeconds)));
	}
	
	public String setSubtitleDelay(int subtitleDelayInSeconds){
		CommandEnum command = CommandEnum.SET_SUBTITLE_DELAY;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command, String.valueOf(subtitleDelayInSeconds)));
	}
	
	public String setPlaybackRate(int playbackRate){
		Checker.checkIntegerGreater(playbackRate, "playbackRate", 0);
		CommandEnum command = CommandEnum.SET_PLAYBACK_RATE;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command, String.valueOf(playbackRate)));
	}
	
	public String setVolume(int volume){
		// TODO error handling
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.SET_VOLUME, this.packParameters(CommandEnum.SET_VOLUME, String.valueOf(volume)));
	}
	
	public String incrementVolume(int incrementValue){
		// TODO error handling
		String value = "+" + incrementValue;
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.SET_VOLUME, this.packParameters(CommandEnum.SET_VOLUME, value));
	}
	
	public String decrementVolume(int decrementValue){
		// TODO error handling
		String value = "-" + decrementValue;
		return this.requestUrlCreator.createStatusRequestUrl(CommandEnum.SET_VOLUME, this.packParameters(CommandEnum.SET_VOLUME, value));
	}
	
	public String selectTitle(int id){
		Checker.checkPositiveInteger(id, "id");
		CommandEnum command = CommandEnum.SELECT_TITLE;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  String.valueOf(id)));
	}
	
	public String selectChapter(int id){
		Checker.checkPositiveInteger(id, "id");
		CommandEnum command = CommandEnum.SELECT_CHAPTER;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  String.valueOf(id)));
	}
	
	public String selectAudioTrack(int id){
		Checker.checkPositiveInteger(id, "id");
		CommandEnum command = CommandEnum.SELECT_AUDIO_TRACK;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  String.valueOf(id)));
	}
	
	public String selectVideoTrack(int id){
		Checker.checkPositiveInteger(id, "id");
		CommandEnum command = CommandEnum.SELECT_VIDEO_TRACK;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  String.valueOf(id)));
	}
	
	public String selectSubtitleTrack(int id){
		Checker.checkPositiveInteger(id, "id");
		CommandEnum command = CommandEnum.SELECT_SUBTITLE_TRACK;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  String.valueOf(id)));
	}
	
	
	// TODO volume in percentage
	
	public String setAspectration(AspectratioEnum aspectratio){
		Checker.checkNull(aspectratio, "aspectratio");
		CommandEnum command = CommandEnum.SET_ASPECTRATIO;
		return this.requestUrlCreator.createStatusRequestUrl(command, this.packParameters(command,  aspectratio.getValue()));
		
	}
	
	// TODO seek
	
	public String playlist(){
		return this.requestUrlCreator.createPlaylistRequestUrl();
	}
	
	public String browse(){
		return this.requestUrlCreator.createBrowseRequestUrl();
	}
	
	public String vlm(){
		return this.requestUrlCreator.createVlmRequestUrl();
	}
	
	public String vlmCmd(String cmd){
		this.checkString(cmd, "cmd");	
		return this.requestUrlCreator.createVlmCmdRequestUrl(cmd);
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// private Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
		
	private void checkString(String stringToCheck, String parameterName){
		this.nullCheck(stringToCheck, parameterName);
		this.checkEmptyString(stringToCheck, parameterName);
	}
	
	private void nullCheck(Object objectToCheck, String parameterName){
		if(objectToCheck == null){
			throw new NullPointerException("The parameter " + parameterName + " must not be null!");
		}
	}
	
	private void checkEmptyString(String stringToCheck, String parameterName){
		if(stringToCheck.isEmpty()){
			throw new RuntimeException("The parameter " + parameterName + " must not be empty!");
		}
	}
	
	private void checkPositiveIntegerValue(int intToCheck, String parameterName){
		if(intToCheck < 0){
			throw new RuntimeException("The parameter " + parameterName + " must be greater euqals 0!");
		}
	}
	
	private HashMap<String, String> packParameters(CommandEnum command, String parameterValue){
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put(command.getValidParameterNames().get(0), parameterValue);
		return parameters;
	}
	
	private HashMap<String, String> packParameters(CommandEnum command, String parameterValue1, String parameterValue2){
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put(command.getValidParameterNames().get(0), parameterValue1);
		parameters.put(command.getValidParameterNames().get(1), parameterValue2);
		return parameters;
	}
	
}
