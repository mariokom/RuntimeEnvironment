package de.ableitner.vlcapi;

import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.helpers.Checker;
import de.ableitner.vlcapi.http.IHttpClient;
import de.ableitner.vlcapi.response.IResponseHandler;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.status.IStatus;
import de.ableitner.vlcapi.url.RequestUrlCreatorAdapter;

public class VlcHttpApi implements IVlcHttpApi{
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================

	private RequestUrlCreatorAdapter urlCreator;
	private IHttpClient httpClient;
	private IResponseHandler responseHandler;
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================

	public VlcHttpApi(RequestUrlCreatorAdapter urlCreator, IHttpClient httpClient, IResponseHandler responseHandler){
		Checker.checkNull(urlCreator, "urlCreator");
		Checker.checkNull(httpClient, "httpClient");
		Checker.checkNull(responseHandler, "responseHandler");
		this.urlCreator = urlCreator;
		this.httpClient = httpClient;
		this.responseHandler = responseHandler;
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
	public IStatus getStatus() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.status());
	}

	@Override
	public IPlaylist getPlaylist() throws VlcApiException {
		String response = this.httpClient.sendHttpRequest(this.urlCreator.playlist());
		return this.responseHandler.createVlcPlaylistFromResponse(response);
	}

	@Override
	public IStatus playPlaylistItem(int itemId) throws VlcApiException {
		this.checkItemId(itemId);
		return this.handleSimpleRequest(this.urlCreator.playItem(itemId));
	}

	@Override
	public IStatus playLastActivePlaylistItem() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.playLastActiveItem());
	}

	@Override
	public IStatus resumePlay() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.play());
	}

	@Override
	public IStatus pause() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.pause());
	}

	@Override
	public IStatus stop() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.stop());
	}

	@Override
	public IStatus playNextPlaylistItem() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.nextItem());
	}

	@Override
	public IStatus playPreviousPlaylistItem() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.previousItem());
	}

	@Override
	public IStatus togglePause(int itemId) throws VlcApiException {
		this.checkItemId(itemId);
		return this.handleSimpleRequest(this.urlCreator.togglePauseAndPlayIfCurrentStateWasStop(itemId));
	}

	@Override
	public IStatus togglePause() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.togglePause());
	}

	@Override
	public IStatus toggleFullscreen() throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.toggleFullscreen());
	}

	@Override
	public IStatus setVolumeAsAbsoluteValue(int absoluteVolume) throws VlcApiException {
		return this.handleSimpleRequest(this.urlCreator.setVolume(absoluteVolume));
	}

	@Override
	public IStatus incrementVolumeByValue(int incrementValue) throws VlcApiException {
		if(incrementValue < 1){
			throw new VlcApiException("The parameter incrementValue must be greater 0!");
		}
		return this.handleSimpleRequest(this.urlCreator.incrementVolume(incrementValue));
	}

	@Override
	public IStatus decrementVolumeByValue(int decrementValue) throws VlcApiException {
		if(decrementValue > -1){
			throw new VlcApiException("The parameter decrementValue must be smaller 0!");
		}
		return this.handleSimpleRequest(this.urlCreator.decrementVolume(decrementValue));
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
	
	private IStatus handleSimpleRequest(String url) throws VlcApiException{
		String response = this.httpClient.sendHttpRequest(url);
		return this.responseHandler.createVlcStatusFromResponse(response);
	}
	
	private void checkItemId(int itemId) throws VlcApiException{
		if(itemId < 0){
			throw new VlcApiException("The parameter itemId must be greater equals 0!");
		}
	}


}
