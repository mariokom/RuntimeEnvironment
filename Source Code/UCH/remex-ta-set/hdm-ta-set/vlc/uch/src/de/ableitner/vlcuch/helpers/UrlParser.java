package de.ableitner.vlcuch.helpers;

import java.util.HashMap;

public class UrlParser {
	static String url = "http://:12345@127.0.0.1:8080";
	
	public static void main(String[] args){
		parseUrl(url);
		System.out.println(getChannelList());
	}
	
	public static HashMap<String, String> parseUrl(String url){
		HashMap<String, String> results = new HashMap<>();
		String password = url.substring(url.indexOf("//") + 3, url.lastIndexOf("@"));
		String ipAddress = url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
		String portNumber = url.substring(url.lastIndexOf(":") + 1);
		
		System.out.println(portNumber);
		System.out.println(ipAddress);
		System.out.println(password);
		return results;
	}
	
	public static String getChannelList(){
		HashMap<String, Integer> channelsMap = new HashMap<>();
		//channelsMap.put("ARD", 1);
		//channelsMap.put("ZDF", 2);
		//channelsMap.put("SWR", 3);
		String channels = "";
		for(String channel : channelsMap.keySet()){
			channels += channel;
			channels += " ";
		}
		if(channels.contains(" ")){
			channels = channels.substring(0, channels.length() - 1);
		}
		return channels;
	}
}
