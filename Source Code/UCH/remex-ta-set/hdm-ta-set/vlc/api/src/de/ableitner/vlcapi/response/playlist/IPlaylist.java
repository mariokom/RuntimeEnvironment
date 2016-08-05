package de.ableitner.vlcapi.response.playlist;

import java.util.List;

public interface IPlaylist {
	
	public String getName();
	
	public List<IPlaylistItem> getItems();
	
}
