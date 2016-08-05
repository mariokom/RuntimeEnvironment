package de.hdm.todoservice.ta;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import edu.wisc.trace.uch.util.Session;

public class TodoServiceTAFacade extends SuperTAFacade<TodoServiceTA>  {

	public TodoServiceTAFacade() {
		super("TodoServiceTAFacade");
	}

	
	public TodoServiceTA createSpezificTA(SuperTAFacade facade) {
				return new TodoServiceTA(facade);
	}
}
	