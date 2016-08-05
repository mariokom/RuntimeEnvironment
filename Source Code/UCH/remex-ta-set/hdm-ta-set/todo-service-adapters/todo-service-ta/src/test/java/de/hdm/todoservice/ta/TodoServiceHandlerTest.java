package de.hdm.todoservice.ta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Date;

import org.junit.Test;

import de.hdm.todoservice.api.TodoItem;


public class TodoServiceHandlerTest {

	@Test
	public void testTodoServiceHandlerIntegration() {

		TodoItem todoItem = new TodoItem(new Date(new Date().getTime() +10000   ) , "title: TestTodo", "Tis is a test");
		TodoServiceHandler todoServiceHandler = new TodoServiceHandler();
URL		 u =  todoServiceHandler.setNewToDoInXMin(todoItem);
String uString = u.toString();
uString = uString.substring(uString.lastIndexOf("/")+1);
System.out.println("ustring");
System.out.println(uString);

assertEquals(todoServiceHandler.getTodoItem(uString)  , todoItem); 
		
	}
	
	@Test
	public void isTodoServiceAvailable(){
		TodoServiceHandler todoServiceHandler = new TodoServiceHandler();
		assertTrue(todoServiceHandler.isTodoServiceAvailable() );
	}
	
	@Test
	public void testDemoTodoItem() {
		TodoServiceHandler todoServiceHandler = new TodoServiceHandler();
		todoServiceHandler.getTodoItem("0");
		
	
	}
	
	}
