package de.ableitner.vlcapi.url.test;

import de.ableitner.vlcapi.url.RequestUrlCreator;
import de.ableitner.vlcapi.url.RequestUrlCreatorAdapter;

public class TestRequestUrlCreatorAdapter {

	public static void main(String[] args) {
		RequestUrlCreatorAdapter ruca = new RequestUrlCreatorAdapter(new RequestUrlCreator("12345", "192.168.178.12", 8080));
		
		System.out.println(ruca.setVolume(20));
		
		//System.out.println(ruca.addMrlToPlaylist(null, true));
		//System.out.println(ruca.addMrlToPlaylist("", true));
		System.out.println(ruca.addMrlToPlaylist("mrl", true));
		System.out.println(ruca.addMrlToPlaylist("mrl", false));
	}

}
