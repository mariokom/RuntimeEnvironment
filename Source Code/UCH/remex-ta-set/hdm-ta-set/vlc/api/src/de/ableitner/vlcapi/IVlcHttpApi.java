package de.ableitner.vlcapi;

import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.status.IStatus;

public interface IVlcHttpApi {

	public IStatus getStatus() throws VlcApiException;
	
	public IPlaylist getPlaylist() throws VlcApiException;
	
	
	public IStatus playPlaylistItem(int itemId) throws VlcApiException;
	
	public IStatus playLastActivePlaylistItem() throws VlcApiException;
	
	public IStatus resumePlay() throws VlcApiException;
	
	public IStatus pause() throws VlcApiException;
	
	public IStatus stop() throws VlcApiException;
	
	public IStatus playNextPlaylistItem() throws VlcApiException;
	
	public IStatus playPreviousPlaylistItem() throws VlcApiException;
	
	
	public IStatus togglePause(int itemId) throws VlcApiException;
	
	public IStatus togglePause() throws VlcApiException;
	
	public IStatus toggleFullscreen() throws VlcApiException;
	
	
	public IStatus setVolumeAsAbsoluteValue(int absoluteVolume) throws VlcApiException;
	
	// I do not know how to implement this request
	//public IVlcStatus setVolumeAsPercentValue(int absoluteVolume) throws VlcApiException;
	
	public IStatus incrementVolumeByValue(int incrementValue) throws VlcApiException;
	
	public IStatus decrementVolumeByValue(int decrementValue) throws VlcApiException;
	

}
