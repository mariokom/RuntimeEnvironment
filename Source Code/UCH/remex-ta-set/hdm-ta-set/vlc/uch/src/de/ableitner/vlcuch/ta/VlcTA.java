package de.ableitner.vlcuch.ta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import de.ableitner.vlcapi.IVlcHttpApi;
import de.ableitner.vlcapi.VlcHttpApi;
import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.http.SimpleHttpClient;
import de.ableitner.vlcapi.response.ResponseHandler;
import de.ableitner.vlcapi.response.playlist.IPlaylist;
import de.ableitner.vlcapi.response.playlist.IPlaylistItem;
import de.ableitner.vlcapi.response.status.IStatus;
import de.ableitner.vlcapi.url.RequestUrlCreatorAdapter;
import de.ableitner.vlcuch.api.VlcConstants;
import de.ableitner.vlcuch.handler.VlcHandler;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;

public class VlcTA extends SuperTA<VlcHandler> {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private VlcHandler vlcHandler;
	
	private List<IPlaylistItem> channelsList;
	
	private Map<String, IPlaylistItem> channelsMap;
	
	private boolean updateSessionAttributesSteady = true;
	
	
	public VlcTA(SuperTAFacade superTAFacade) {
		super(superTAFacade);
		this.logger.log(Level.INFO, "Log Tobias in VlcTA");
		// TODO set updateSessionAttributesSteady
		//RequestUrlCreatorAdapter urlCreator = new RequestUrlCreatorAdapter("12345", "127.0.0.1", 8080);
		//IVlcHttpApi vlcHttpApi = new VlcHttpApi(urlCreator, new SimpleHttpClient(), new ResponseHandler());
		//this.vlcHandler = new VlcHandler(vlcHttpApi);
	}

	@Override
	public void useTargetProps(Map<String, Object> targetProps) {
		this.logger.log(Level.INFO, "Log Tobias in useTargetProps");
		String url = (String) targetProps.get(VlcConstants.IpAddress);
		this.logger.log(Level.INFO, "Log Tobias url=" + url);
		String password = url.substring(url.indexOf("//") + 3, url.lastIndexOf("@"));
		this.logger.log(Level.INFO, "Log Tobias password=" + password);
		String ipAddress = url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
		this.logger.log(Level.INFO, "Log Tobias ipAddress=" + ipAddress);
		int portNumber = Integer.valueOf(url.substring(url.lastIndexOf(":") + 1));
		this.logger.log(Level.INFO, "Log Tobias portNumber=" + portNumber);
		RequestUrlCreatorAdapter urlCreator = new RequestUrlCreatorAdapter(password, ipAddress, portNumber);
		IVlcHttpApi vlcHttpApi = new VlcHttpApi(urlCreator, new SimpleHttpClient(), new ResponseHandler());
		this.vlcHandler = new VlcHandler(this, vlcHttpApi);
		this.initChannels();
	}
	
	@Override
	public void executeCommand(String command, Session session, String value) {
		this.logger.log(Level.INFO, "Log Tobias command: " + command + " value: " + value);
		switch (command) {
		case VlcUchCommands.SET_CHANNEL:
			this.vlcHandler.playPlaylistItem(session, this.channelsMap.get(value));
			break;
		case VlcUchCommands.PLAY:
			if(this.channelsList.isEmpty()){
				this.vlcHandler.play(session, null);
			}else{
				this.vlcHandler.play(session, this.channelsList.get(0));	
			}
			break;
		case VlcUchCommands.STOP:
			this.vlcHandler.stop(session);
			break;
		case VlcUchCommands.SET_VOLUME:
			this.vlcHandler.setVolume(session, value);
			break;
		case VlcUchCommands.MUTE:
			//this.vlcHandler.setVolume(session, value);
			// TODO
			break;
		default:
			this.logger.log(Level.SEVERE, "The command " + command + " is unknown for the VLC Targetadapter!");
			System.err.println("The command " + command + " is unknown for the VLC Targetadapter!");
		}
		
	}

	@Override
	public void initSessionValues(Session session) {
		this.logger.log(Level.INFO, "Log Tobias in initSessionValues");
		IStatus status = this.vlcHandler.getStatus();
		int volume;
		if(status != null){
			volume = status.getVolume();
		}else{
			volume = 100;
		}
		session.setValue("/liveTVControls/channelList", this.getChannelListAsString());
		session.setValue("/volumeControls/volume", String.valueOf(volume));
		session.setValue("/volumeControls/mute", "false");	
	}

	@Override
	public void update(Session session) {
		this.logger.log(Level.INFO, "Log Tobias in update");
		if(this.updateSessionAttributesSteady){
			IStatus status = this.vlcHandler.getStatus();
			this.logger.log(Level.INFO, "Log Tobias status=" + status.toString());
		}
		
	}
	
	public void updateVariableAdapter(Session session, String path, String value){
		this.updateVariable(session, path, value);
	}
	
	public String getValueOfVariable(Session session, String path){
		return session.getValue(path).get(0).get("value");
	}
	
	private void initChannels(){
		this.logger.log(Level.INFO, "Log Tobias in initChannelMap");
		this.channelsList = new ArrayList<IPlaylistItem>();
		this.channelsMap = new HashMap<String, IPlaylistItem>();
		IPlaylist playlist = this.vlcHandler.getPlaylist();
		this.channelsList.addAll(playlist.getItems());
		if(playlist != null){
			this.logger.log(Level.INFO, "Log Tobias playlist is not null");
			this.logger.log(Level.INFO, "Log Tobias playlist size is " + playlist.getItems().size());
			for(IPlaylistItem playlistItem : playlist.getItems()){
				this.channelsMap.put(playlistItem.getName().replace(" ", "_"), playlistItem);
			}	
		}
	}
	
	private String getChannelListAsString(){
		this.logger.log(Level.INFO, "Log Tobias in getChannelListAsString");
		String channels = "";
		for(IPlaylistItem channel : this.channelsList){
			this.logger.log(Level.INFO, "Log Tobias in for loop channel=" + channel.getName());
			channels += channel.getName().replace(" ", "_");
			channels += " ";
		}
		if(channels.contains(" ")){
			channels = channels.substring(0, channels.length() - 1);
		}
		this.logger.log(Level.INFO, "Log Tobias channels=" + channels);
		this.logger.log(Level.INFO, "Log Tobias end getChannelListAsString");
		return channels;
	}

}
