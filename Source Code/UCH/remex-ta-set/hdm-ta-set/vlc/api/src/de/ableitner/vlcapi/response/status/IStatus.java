package de.ableitner.vlcapi.response.status;

public interface IStatus {

	public boolean isFullscreen();
	
	public int getIdOfCurrentPlaylistItem();
	
	public int getTimeInSeconds();
	
	public int getVolume();
	
	public int getLengthInSeconds();
	
	public boolean isRandom();
	
	public StateEnum getState();
	
	public boolean isLoop();
	
	public boolean isRepeat();
	
}
