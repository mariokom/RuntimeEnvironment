package de.ableitner.vlcapi.response;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.helpers.Checker;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.playlist.IPlaylistItem;
import de.ableitner.vlcapi.response.playlist.Playlist;
import de.ableitner.vlcapi.response.playlist.PlaylistItem;
import de.ableitner.vlcapi.response.status.IStatus;
import de.ableitner.vlcapi.response.status.StateEnum;
import de.ableitner.vlcapi.response.status.Status;

public class ResponseHandler implements IResponseHandler {

	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	public ResponseHandler() {
		
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
	public IStatus createVlcStatusFromResponse(String response) throws VlcApiException {
		Checker.checkNullAndEmptiness(response, "response");
		
		boolean fullscreen = false;
		int idOfCurrentPlaylistItem = -1;
		int timeInSeconds = -1;
		int volume = -1;
		int lengthInSeconds = -1;
		boolean random = false;
		StateEnum state = null;
		boolean loop = false;
		boolean repeat = false;
		
		try{
			org.w3c.dom.Document document = this.createDocument(response);
			
			fullscreen = Boolean.valueOf(this.getNodeValue(document, "fullscreen"));
			idOfCurrentPlaylistItem = Integer.valueOf(this.getNodeValue(document, "currentplid"));
			timeInSeconds = Integer.valueOf(this.getNodeValue(document, "time"));
			volume = Integer.valueOf(this.getNodeValue(document, "volume"));
			lengthInSeconds = Integer.valueOf(this.getNodeValue(document, "length"));
			fullscreen = Boolean.valueOf(this.getNodeValue(document, "random"));
			state = StateEnum.convertStringToState(this.getNodeValue(document, "state"));
			fullscreen = Boolean.valueOf(this.getNodeValue(document, "loop"));
			fullscreen = Boolean.valueOf(this.getNodeValue(document, "repeat"));
			
		}catch(ParserConfigurationException e){
			e.printStackTrace();
			throw new VlcApiException("The status could not be parsed on account of an error in the parser's configuration! \n" + e.getMessage());
		}catch(SAXException e){
			e.printStackTrace();
			throw new VlcApiException("The status could not be parsed on account of an error durring parsing! \n" + e.getMessage());
		}catch(IOException e){
			e.printStackTrace();
			throw new VlcApiException("The status could not be parsed on account of a failed or interrupted I/O operations! \n" + e.getMessage());
		}
		
		return new Status(fullscreen, idOfCurrentPlaylistItem, timeInSeconds, volume, lengthInSeconds, random, state, loop, repeat);
	}
	
	@Override
	public IPlaylist createVlcPlaylistFromResponse(String response) throws VlcApiException {
		Checker.checkNullAndEmptiness(response, "response");
		
		String playlistName = null;
		List<IPlaylistItem> playlistItems = null;
		
		try{
			org.w3c.dom.Document document = this.createDocument(response);
			
			playlistName = this.createPlaylistNameFromXml(document);
			
			NodeList playlistItemsNodeList = document.getElementsByTagName("leaf");
			playlistItems = new ArrayList<IPlaylistItem>();
			for(int i = 0; i < playlistItemsNodeList.getLength(); i++){
				playlistItems.add(this.createPlaylistItemFromXml(playlistItemsNodeList.item(i)));
			}
		}catch(ParserConfigurationException e){
			e.printStackTrace();
			throw new VlcApiException("The playlist could not be parsed on account of an error in the parser's configuration! \n" + e.getMessage());
		}catch(SAXException e){
			e.printStackTrace();
			throw new VlcApiException("The playlist could not be parsed on account of an error durring parsing! \n" + e.getMessage());
		}catch(IOException e){
			e.printStackTrace();
			throw new VlcApiException("The playlist could not be parsed on account of a failed or interrupted I/O operations! \n" + e.getMessage());
		}
		
		return new Playlist(playlistName, playlistItems);
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
	
	private org.w3c.dom.Document createDocument(String response) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document document = documentBuilder.parse(new InputSource(new StringReader(response)));
		document.getDocumentElement().normalize();
		return document;
	}
	
	private String createPlaylistNameFromXml(org.w3c.dom.Document document){
		NamedNodeMap attributes = document.getElementsByTagName("node").item(1).getAttributes();
		//System.out.println(attributes.getNamedItem("name").getNodeValue());
		return attributes.getNamedItem("name").getNodeValue();
	}
	
	private IPlaylistItem createPlaylistItemFromXml(Node node){
		NamedNodeMap attributes = node.getAttributes();
		String name = attributes.getNamedItem("name").getNodeValue();
		int playlistId = Integer.valueOf(attributes.getNamedItem("id").getNodeValue());
		int duration = Integer.valueOf(attributes.getNamedItem("duration").getNodeValue());
		String uri = attributes.getNamedItem("uri").getNodeValue();
		boolean current = false;
		if(attributes.getNamedItem("current") != null){
			current = true;
		}
		return new PlaylistItem(name, playlistId, duration, uri, current);
	}
	
	private String getNodeValue(org.w3c.dom.Document document, String nodeName){
		NodeList nodeList = document.getElementsByTagName(nodeName);
		return nodeList.item(0).getFirstChild().getNodeValue();
	}
}
