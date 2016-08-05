package de.hdm.todoservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import de.hdm.todoservice.api.TodoItem;
import de.hdm.todoservice.api.UrlCarrier;

public class ToDoList implements Runnable {
	private int idCounter = 0;
	private List<TodoItem> todos = new ArrayList<TodoItem>();
	private HashMap<String, TodoItem> todoMap;
TodoItem	 next_todo = null;
//private PushManager pushmanager;


public ToDoList(){
	this.todos = new LinkedList<TodoItem>();
	this.todoMap = new HashMap<String, TodoItem>();
}

	@POST
	@Consumes(MediaType.APPLICATION_XML)   
    @Produces(MediaType.APPLICATION_XML)
		public UrlCarrier addToDo(@Context UriInfo uriInfo, TodoItem todo){
				todos.add(todo);
		
	Collections.sort(this.todos);
	
	String id = "" + idCounter++;
	todoMap.put(id, todo);

	
		
//	check if the new todo is the one that must be executed next. update nextTodo variable if necessary
	if (this.next_todo == null ){ next_todo = todo;}
	else {
		Date date = todo.getDueDate();
		if ( date.after(new Date()) && date.before(next_todo.getDueDate())) {
		next_todo	= todo;
		}
	}
System.out.println("New Todo was added with Id:" + id);
	URL url = null;
	try {
		
		url = new URL(uriInfo.getRequestUri() + "/" +  id);
		System.out.println("URL: " + url);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	UrlCarrier uc  = new UrlCarrier();
	uc.addUrl(url);
	return uc;
	}
	
	@Path("NextTodo")
@GET
@Produces(MediaType.APPLICATION_XML)
	public TodoItem getNextTodo() {
		return next_todo;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		StringBuffer list = new StringBuffer();
		
		Iterator<TodoItem> todo_iterator = todos.iterator();  
		while (todo_iterator.hasNext()) {
			TodoItem todo = todo_iterator.next();
			list.append(todo.getDueDate() + ";" + todo.getDescription() + "\n"     )   ;
				}
		return list.toString();
	}
	
	public void run() {
		Date current_date;
		
		while (true){
current_date = new Date();
if (current_date.after(next_todo.getDueDate() )) {
//pushmanager.executeTodo(next_todo);
Iterator<TodoItem> todo_iterator = todos.iterator();
while (todo_iterator.hasNext()) {
	TodoItem todo = todo_iterator.next();

}
}
				}
			}
	
@GET
@Path("{id}")
@Produces(MediaType.APPLICATION_XML)
	public TodoItem getTodo( @PathParam("id") String id) {
	System.out.println("Item with id" + id +"was requested");
	System.out.println(todoMap.get(id));
	return todoMap.get(id);
			}

@Override
public boolean equals( Object o ){
  if ( o == null )
    return false;

  if ( o == this )
    return true;

  ToDoList that = (ToDoList)o;
  
  if ((that.idCounter == this.idCounter) && (that.todoMap.equals(this.todoMap) && (that.todos.equals(todos) )      )){
	  return true;
  }
  
  
  return false;
}


@Path("text")
@GET
@Produces(MediaType.TEXT_PLAIN)
public String getTodoListDescription(){
	return toString();
}

}