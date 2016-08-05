package org.github.roqs23.restws;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.spi.resource.Singleton;

import de.hdm.todoservice.ToDoList;
import de.hdm.todoservice.api.TodoItem;


@Singleton
@Path("todo-service")
public class TodoService {
	Boolean available = true;
HashMap<String, ToDoList> todoListMap;
private ZeroConfStarter zeroConfStarter;

public TodoService(){
		todoListMap = new HashMap<String, ToDoList>();
	
}

@PostConstruct
public void init() {

this.zeroConfStarter = new ZeroConfStarter();
this.zeroConfStarter.start();
setupDemoList();
System.out.println("Demo was setup");
}

public void setupDemoList(){
URL listURL = this.addNewTodoList(getDemoServiceUri()   , "demo");

System.out.println("listURL: " + listURL);
TodoItem todoItem = createDemoItem(); 

this.getTodoList("demo").addToDo(getDemoListUri(), todoItem);

}


private UriInfo getDemoListUri() {
	
	UriInfo uri = new UriInfo() {
		@Override
		public String toString() {
			return "http://localhost:8383/todo-service/demo";
		}
		@Override
		public UriBuilder getRequestUriBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getRequestUri() {
try {
	return new URI(this.toString());
} catch (URISyntaxException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getQueryParameters(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getQueryParameters() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<PathSegment> getPathSegments(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<PathSegment> getPathSegments() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getPathParameters(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getPathParameters() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPath(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<String> getMatchedURIs(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<String> getMatchedURIs() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<Object> getMatchedResources() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public UriBuilder getBaseUriBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getBaseUri() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public UriBuilder getAbsolutePathBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getAbsolutePath() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	return uri;
}

private UriInfo getDemoServiceUri() {
	UriInfo uri = new UriInfo() {
		@Override
		public String toString(){
			return "http://localhost:8383/todo-service/";
		}
		@Override
		public UriBuilder getRequestUriBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getRequestUri() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getQueryParameters(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getQueryParameters() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<PathSegment> getPathSegments(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<PathSegment> getPathSegments() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getPathParameters(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public MultivaluedMap<String, String> getPathParameters() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPath(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<String> getMatchedURIs(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<String> getMatchedURIs() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public List<Object> getMatchedResources() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public UriBuilder getBaseUriBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getBaseUri() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public UriBuilder getAbsolutePathBuilder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public URI getAbsolutePath() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	return uri;
}



@Path("{todolistid}")
public ToDoList getTodoList(@PathParam("todolistid") String todolistid) {
	System.out.println("lusm list: " +todolistid);
	System.out.println(todoListMap);
	return todoListMap.get(todolistid);
}

@GET
@Path("status")
@Produces("text/html")
public String getStatus(){
	return "<p> todo service is online</p>"; 
}

@PreDestroy
public void destroy(){

	this.zeroConfStarter.finalize();	
}

@POST
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public URL addNewTodoList(@Context UriInfo uriInfo, String message){
	todoListMap.put(message, new ToDoList());
	URL url = null;
	try {
				url = new URL(uriInfo.toString()  +  message);
	} catch (MalformedURLException e) {
				e.printStackTrace();
	}
	return url;
}


@GET
@Path("available")
@Produces(MediaType.TEXT_PLAIN)
public String isAvailable(){
return this.available.toString();
}

public TodoItem createDemoItem() {
		return new TodoItem(new Date(0), "Demo title", "Demo description");
}


}