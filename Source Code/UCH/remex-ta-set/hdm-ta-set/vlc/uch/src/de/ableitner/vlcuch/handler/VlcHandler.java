package de.ableitner.vlcuch.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ableitner.vlcapi.IVlcHttpApi;
import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.playlist.IPlaylistItem;
import de.ableitner.vlcapi.response.playlist.PlaylistItem;
import de.ableitner.vlcapi.response.status.IStatus;
import de.ableitner.vlcuch.helpers.Checker;
import de.ableitner.vlcuch.ta.VlcTA;
import de.ableitner.vlcuch.ta.VlcUchVariablePaths;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;

public class VlcHandler {
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	private Logger logger = LoggerUtil.getSdkLogger();
	
	private IVlcHttpApi vlc;
	
	private VlcTA vlcTargetAdapter;
	
	
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================

	public VlcHandler(VlcTA vlcTargetAdapter, IVlcHttpApi vlc){
		Checker.checkNull(vlcTargetAdapter, "vlcTargetAdapter");
		Checker.checkNull(vlc, "vlc");
		this.vlcTargetAdapter = vlcTargetAdapter;
		this.vlc = vlc;
	}
	
	public VlcHandler(){
		this.logger.log(Level.INFO, "Log Tobias in VlcHandler()");
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

	public void play(Session session, IPlaylistItem defaultPlaylistItem){
		// TODO erst checken ob es einen letzten Sender gab
		if(defaultPlaylistItem != null){
			
		}
		try {
			this.vlc.playPlaylistItem(defaultPlaylistItem.getPlaylistId());
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void playPlaylistItem(Session session, IPlaylistItem playlistItem) {
		if (playlistItem != null) {
			try {
				this.vlc.playPlaylistItem(playlistItem.getPlaylistId());
				//session.setValue(elementPath, value)
				// TODO update session
			} catch (VlcApiException e) {
				e.printStackTrace();
				this.logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	public void stop(Session session){
		try {
			this.vlc.stop();
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void setMute(Session session, boolean mute) {
		try {
			if (mute) {
				this.vlc.setVolumeAsAbsoluteValue(0);
				this.vlcTargetAdapter.updateVariableAdapter(session, VlcUchVariablePaths.MUTE, "true");
			} else {
				int volume = Integer.valueOf(this.vlcTargetAdapter.getValueOfVariable(session, VlcUchVariablePaths.VOLUME));
				this.vlc.setVolumeAsAbsoluteValue(volume);
				this.vlcTargetAdapter.updateVariableAdapter(session, VlcUchVariablePaths.MUTE, "false");
			}
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void setVolume(Session session, String value){
		try {
			int newVolume = Integer.valueOf(value);
			this.vlc.setVolumeAsAbsoluteValue(newVolume);
			this.vlcTargetAdapter.updateVariableAdapter(session, VlcUchVariablePaths.VOLUME, String.valueOf(newVolume));
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		} catch(NumberFormatException e){
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public IStatus getStatus(){
		IStatus status = null;
		try {
			status = this.vlc.getStatus();
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
		return status;
	}
	
	public IPlaylist getPlaylist(){
		IPlaylist playlist = null;
		try {
			playlist = this.vlc.getPlaylist();
		} catch (VlcApiException e) {
			e.printStackTrace();
			this.logger.log(Level.SEVERE, e.getMessage());
		}
		return playlist;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// private Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
	

}
