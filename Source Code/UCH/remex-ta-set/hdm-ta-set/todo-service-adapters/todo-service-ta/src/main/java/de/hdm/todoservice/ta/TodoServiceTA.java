package de.hdm.todoservice.ta;

import java.util.List;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import de.hdm.todoservice.api.TodoItem;
import edu.wisc.trace.uch.util.Session;

public class TodoServiceTA extends SuperTA<TodoServiceHandler> {

	public TodoServiceTA(SuperTAFacade facade) {
		super(facade);
			}

	@Override
	public void executeCommand(String commandName, Session session, String value) {
		
		switch (commandName){
			
		case "NextTodoTitle":
			updateNextTodoSocketElement(session);
			break;
		case "NewToDoInXMin":
			TodoItem todoItem = new TodoItem(null, "", "");
				handler.setNewToDoInXMin(todoItem);
				updateNextTodoSocketElement(session);
				break;
				
		
	}
	}
	
	private void updateNextTodoSocketElement(Session session) {
		TodoItem todo = handler.getNextTodo();
		session.setValue("/NextTodoTitle", todo.getTitle( ));
		
	}

	@Override
	public void initSessionValues(Session session) {
		
		
	}

	@Override
	public void update(Session session){
		// TODO Auto-generated method stub
		
	}

}
