package de.ableitner.vlcapi.response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import de.ableitner.vlcapi.exceptions.VlcApiException;

public class TestResponseHandler {

	public static void main(String[] args) throws VlcApiException, IOException {
		ResponseHandler responseHandler = new ResponseHandler();
		/*String response = "<node ro=\"rw\" name=\"Undefiniert\" id=\"1\"><node ro=\"ro\" name=\"Wiedergabeliste\" id=\"2\">";
		response += "<leaf ro=\"rw\" name=\"ZDF\" id=\"5\" duration=\"-1\" uri=\"dvb-t://frequency=570000000\" current=\"current\"/>";
		response += "<leaf ro=\"rw\" name=\"3sat\" id=\"6\" duration=\"-1\" uri=\"dvb-t://frequency=570000000\"/>";
		response += "<leaf ro=\"rw\" name=\"ZDFinfo\" id=\"7\" duration=\"-1\" uri=\"dvb-t://frequency=570000000\"/>";
		response += "<leaf ro=\"rw\" name=\"neo/KiKA\" id=\"8\" duration=\"-1\" uri=\"dvb-t://frequency=570000000\"/>";
		response += "</node>";
		response += "<node ro=\"ro\" name=\"Medienbibliothek\" id=\"3\"/>";
		response += "</node>";
		System.out.println(responseHandler.createVlcPlaylistFromResponse(response).toString());
		*/
		
		//File xml = new File("status.xml");
		//File xml = new File("playlist.xml");
		
		BufferedReader reader = new BufferedReader(new FileReader ("playlist.xml"));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		    stringBuilder.append(ls);
		}
		reader.close();
		
		//System.out.println(responseHandler.createVlcStatusFromResponse(stringBuilder.toString()).toString());
		System.out.println(responseHandler.createVlcPlaylistFromResponse(stringBuilder.toString()).toString());
	}

}
