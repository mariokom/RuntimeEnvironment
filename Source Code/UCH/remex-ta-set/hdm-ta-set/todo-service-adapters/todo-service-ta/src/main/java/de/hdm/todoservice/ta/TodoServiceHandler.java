package de.hdm.todoservice.ta;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hdm.todoservice.api.TodoItem;
import de.hdm.todoservice.api.UrlCarrier;

public class TodoServiceHandler {
	private WebResource todoService;
	private String todoListId = "demo";
	
	public TodoServiceHandler(){
	    ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
	    todoService = client.resource("http://localhost:8383");

	}

	public TodoItem	 getNextTodo() {
		
		return todoService.path(todoListId + "/nextTodo").accept(MediaType.APPLICATION_XML).get(TodoItem.class);
	}

	public URL  setNewToDoInXMin(TodoItem todoItem) {
	
		String responseString = todoService.path("todo-service/" + todoListId)
				.type(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .entity(todoItem)
                .post(UrlCarrier.class).getHref();
		URL responseUrl = null;
		try {
			responseUrl = new URL(responseString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return responseUrl;
	}

	public boolean isTodoServiceAvailable() {
		String returnString = todoService.path("todo-service/available").accept(MediaType.TEXT_PLAIN).get(String.class);
				return returnString.equals("true");  
	}

	public TodoItem getTodoItem(String todoItemId) {
		return todoService.path("todo-service/" + todoListId + "/" +  todoItemId).accept(MediaType.APPLICATION_XML).get(TodoItem.class);
		
	}
	
}
