package de.ableitner.vlcapi.response.playlist;

public interface IPlaylistItem {
	
	public String getName();
	
	public int getPlaylistId();
	
	public int getDuration();
	
	public String getUri();
	
	public boolean isCurrent();
	
}
