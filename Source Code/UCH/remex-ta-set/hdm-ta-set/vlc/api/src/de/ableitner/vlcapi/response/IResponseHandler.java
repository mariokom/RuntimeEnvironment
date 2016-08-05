package de.ableitner.vlcapi.response;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.status.IStatus;

public interface IResponseHandler {
	
	public IStatus createVlcStatusFromResponse(String response) throws VlcApiException;
	
	public IPlaylist createVlcPlaylistFromResponse(String response) throws VlcApiException;
	
}
