package de.ableitner.vlcapi.url.test;

import java.util.HashMap;

import de.ableitner.vlcapi.url.CommandEnum;
import de.ableitner.vlcapi.url.RequestUrlCreator;

public class TestRequestUrlCreator {

	public static void main(String[] args) {
		RequestUrlCreator ruc = new RequestUrlCreator("12345", "192.168.178.12", 8080);
		
		System.out.println(ruc.createBrowseRequestUrl());
		
		System.out.println(ruc.createPlaylistRequestUrl());
		
		System.out.println(ruc.createVlmRequestUrl());
		
		System.out.println(ruc.createVlmCmdRequestUrl("command"));
		
		System.out.println(ruc.createStatusRequestUrl());
		System.out.println(ruc.createStatusRequestUrl(CommandEnum.JUMP_TO_NEXT_ITEM));
		System.out.println(ruc.createStatusRequestUrl(CommandEnum.JUMP_TO_PREVIOUS_ITEM));
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", "4");
		System.out.println(ruc.createStatusRequestUrl(CommandEnum.PLAY, parameters));
		
		CommandEnum command = CommandEnum.TOGGLE_ENABLE_SERVICE_DISCOVERY_MODULE;
		System.out.println(command.getValidParameterNames());

	}

}
