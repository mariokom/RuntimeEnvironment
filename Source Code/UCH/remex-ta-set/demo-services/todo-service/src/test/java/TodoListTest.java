import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.core.UriInfo;

import org.junit.Test;

import de.hdm.todoservice.ToDoList;
import de.hdm.todoservice.api.TodoItem;
import de.hdm.todoservice.api.UrlCarrier;


public class TodoListTest {
@Test
public void testAddTodoItems() throws MalformedURLException{
	
	ToDoList testList = new ToDoList();
	TodoItem todoItem = new TodoItem(new Date(), "title", "description");
	UriInfo uri = TestHelpers.getListUri("demo");
	
	
UrlCarrier uc  = 	testList.addToDo(uri, todoItem);
URL url = uc.createURL(uc);
URL expected = new URL("http://localhost:8383/todo-service/demo/0");
	

assertEquals(expected, url);
	assertEquals(testList.getTodo("0"), todoItem );
}

	@Test
	public void testSortNextItem(){
	
	long time = System.currentTimeMillis();
	TodoItem firstItem = new TodoItem(new Date(time + 100), "first Item", "description");
	TodoItem seccondItem = new TodoItem(new Date(time + 200), "2nd  Item", "description");
	TodoItem thirdItem = new TodoItem(new Date(time + 300), "3nd  Item", "description");
	
	UriInfo	uri = TestHelpers.getListUri();
	
	ToDoList list = new ToDoList();
	
	list.addToDo(uri, thirdItem);
	assertEquals(thirdItem, list.getNextTodo());

	list.addToDo(uri, firstItem);
	assertEquals(firstItem, list.getNextTodo());

	
	list.addToDo(uri, seccondItem);
	assertEquals(firstItem, list.getNextTodo());
	
	
}

@Test
public void testEquals() {
	ToDoList list1 = new ToDoList();
	ToDoList list2 = new ToDoList();
	assertEquals(list1,list2  );
	TodoItem todo1 = new TodoItem(new Date(0), "title", "description");
	TodoItem todo2 = new TodoItem(new Date(0), "title", "description");
	
	UriInfo uri = TestHelpers.getListUri();
	list1.addToDo(uri, todo1);
	list2.addToDo(uri, todo1);
	assertEquals(list1,list2  );
}

@Test
public void testIdCounter() throws MalformedURLException{
	ToDoList testList = new ToDoList();
	TodoItem todoItem = new TodoItem(new Date(), "title", "description");
	UriInfo uri = TestHelpers.getListUri("demo");
	
	
UrlCarrier uc  = 	testList.addToDo(uri, todoItem);
URL url = uc.createURL(uc);
URL expected = new URL("http://localhost:8383/todo-service/demo/0");
	

assertEquals(expected, url);
	assertEquals(testList.getTodo("0"), todoItem );

	TodoItem todoItem2 = new TodoItem(new Date(), "title2", "description2");
	
uc  = 	testList.addToDo(uri, todoItem2	);
URL url2 = uc.createURL(uc);
URL expected2 = new URL("http://localhost:8383/todo-service/demo/1");
	

assertEquals(expected2, url2);
	assertEquals(testList.getTodo("1"), todoItem2 );


}

}
