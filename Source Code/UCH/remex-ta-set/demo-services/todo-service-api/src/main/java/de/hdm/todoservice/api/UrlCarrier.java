package de.hdm.todoservice.api;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlRootElement(name="URL")
@XmlType(propOrder={"href"})
		
public class UrlCarrier {
	private String href;
	
	
	

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public static URL createURL(UrlCarrier uc) {
		try {
			return new URL(uc.getHref());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void addUrl(URL url) {
		href = url.toString();
		
	}

}