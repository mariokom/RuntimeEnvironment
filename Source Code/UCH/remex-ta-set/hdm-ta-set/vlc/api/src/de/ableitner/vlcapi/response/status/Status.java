package de.ableitner.vlcapi.response.status;

import de.ableitner.vlcapi.helpers.Checker;

public class Status implements IStatus{
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	private boolean fullscreen;
	
	private int idOfCurrentPlaylistItem;
	
	private int timeInSeconds;
	
	private int volume;
	
	private int lengthInSeconds;
	
	private boolean random;
	
	private StateEnum state;
	
	private boolean loop;
	
	private boolean repeat;


	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	public Status(boolean fullscreen, int idOfCurrentPlaylistItem, int timeInSeconds, int volume, int lengthInSeconds, boolean random,
			StateEnum state, boolean loop, boolean repeat) {
		Checker.checkIntegerGreater(idOfCurrentPlaylistItem, "idOfCurrentPlaylistItem", 0);
		Checker.checkIntegerGreater(timeInSeconds, "timeInSeconds", -2);
		Checker.checkIntegerGreater(volume, "volume", -1);
		Checker.checkIntegerGreater(lengthInSeconds, "lengthInSeconds", -2);
		Checker.checkNull(state, "state");
		this.fullscreen = fullscreen;
		this.idOfCurrentPlaylistItem = idOfCurrentPlaylistItem;
		this.timeInSeconds = timeInSeconds;
		this.volume = volume;
		this.lengthInSeconds = lengthInSeconds;
		this.random = random;
		this.state = state;
		this.loop = loop;
		this.repeat = repeat;
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
	public boolean isFullscreen() {
		return this.fullscreen;
	}

	@Override
	public int getIdOfCurrentPlaylistItem() {
		return this.idOfCurrentPlaylistItem;
	}

	@Override
	public int getTimeInSeconds() {
		return this.timeInSeconds;
	}

	@Override
	public int getVolume() {
		return this.volume;
	}

	@Override
	public int getLengthInSeconds() {
		return this.lengthInSeconds;
	}

	@Override
	public boolean isRandom() {
		return this.random;
	}

	@Override
	public StateEnum getState() {
		return this.state;
	}

	@Override
	public boolean isLoop() {
		return this.loop;
	}

	@Override
	public boolean isRepeat() {
		return this.repeat;
	}
	
	@Override
	public String toString(){
		String result = "Status   fullscreen=" + this.fullscreen;
		result += "   idOfCurrentPlaylistItem=" + this.idOfCurrentPlaylistItem;
		result += "   timeInSeconds=" + this.timeInSeconds;
		result += "   volume=" + this.volume;
		result += "   lengthInSeconds=" + this.lengthInSeconds;
		result += "   random=" + this.random;
		result += "   state=" + this.state;
		result += "   loop=" + this.loop;
		result += "   repeat=" + this.repeat;
		return result;
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
