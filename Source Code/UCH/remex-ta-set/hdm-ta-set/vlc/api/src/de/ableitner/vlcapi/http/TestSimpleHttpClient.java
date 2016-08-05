package de.ableitner.vlcapi.http;

import de.ableitner.vlcapi.exceptions.VlcApiException;

public class TestSimpleHttpClient {

	public static void main(String[] args) throws VlcApiException {
		IHttpClient c = new SimpleHttpClient();
		System.out.println(c.sendHttpRequest("http://:12345@127.0.0.1:8080/requests/status.xml"));

	}

}
