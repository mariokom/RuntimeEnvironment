package de.hdm.todoservice.api;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoItem implements Comparable<TodoItem> {
private String description;
private Date dueDate;
private String title;

public TodoItem(){
	
}

public TodoItem(Date dueDate, String title, String description) {
	super();
	this.description = description;
	this.dueDate = dueDate;
	this.title = title;
}


public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

public  void setDescription(String description) {
	this.description = description;
}

public String getDescription() {
	return description;
}
public Date getDueDate() {
	return dueDate;
}

public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
}


public int compareTo(TodoItem todo ) {
	// TODO Auto-generated method stub
	return dueDate.compareTo(todo.getDueDate());
}

@Override
	public boolean equals(Object obj) {
	if ( obj == null )
	    return false;

	  if ( obj == this )
	    return true;

	
try{
	TodoItem todoItem = (TodoItem)obj;
	if ((this.getDueDate().equals(todoItem.getDueDate()  )&& (this.getDescription()  ).equals(todoItem.getDescription() )&& (this.getTitle()).equals(todoItem.getTitle() ))   ){
		System.out.println("identical");
		return true;
	}
		
	} catch (Exception e){
e.printStackTrace();
	}
		
		return false; 
	}

public String toString(){
	return dueDate + ": " + title + "\n" + description;
}
}
