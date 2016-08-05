package de.hdm.todoservice.ta;

import java.net.URL;
import java.util.Date;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.hdm.todoservice.api.TodoItem;
public class Starter {

	public static void main(String[] args) {
		String url = "http://localhost:8080/sandbox/rest/todo/demo";
WebResource wrs = Client.create().resource( url);
	      TodoItem todo = new TodoItem(new Date() , "titel",  "description");
	      wrs.type( "text/xml; charset=utf-8" ).accept( "text/xml; charset=utf-8" ).post( URL.class, todo);

	
		
	}

}
