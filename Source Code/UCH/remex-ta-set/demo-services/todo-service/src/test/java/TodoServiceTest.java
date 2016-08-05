import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.ws.rs.core.UriInfo;

import org.github.roqs23.restws.TodoService;
import org.junit.Test;

import de.hdm.todoservice.api.TodoItem;


public class TodoServiceTest {

	@Test
	public void testAddTodolist() {
		TodoService todoService = new TodoService();
		todoService.addNewTodoList(TestHelpers.getTodoServiceUri() ,  "testList");
		TodoItem todoItem = new TodoItem(new Date(0), "testTitle", "testDescription");
		UriInfo uri = TestHelpers.getListUri();
		
todoService.getTodoList("testList").addToDo(uri, todoItem);
assertEquals(todoService.getTodoList("testList").getTodo("0"), todoItem);
}
	
	@Test
	public void testDemo(){
		TodoService todoService = new TodoService();
		todoService.setupDemoList();
		
		TodoItem demoItem = todoService.createDemoItem();
		TodoItem returnItem = todoService.getTodoList("demo").getTodo("0");  
		
		
		assertEquals(demoItem.getDueDate(), returnItem.getDueDate());
		assertEquals(demoItem.getDescription()  , returnItem.getDescription()				);
		assertEquals(demoItem.getTitle()  , returnItem.getTitle()				);
		
//		assertEquals(demoItem, returnItem);

	System.out.println("hier wird .equals getestet");

	
//assertTrue(returnItem.equals(demoItem));
		
//		assertEquals(demoItem, returnItem);
	}
}
