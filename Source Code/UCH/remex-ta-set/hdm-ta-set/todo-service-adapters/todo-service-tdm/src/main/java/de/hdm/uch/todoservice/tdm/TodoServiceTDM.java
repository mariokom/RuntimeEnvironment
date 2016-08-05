package de.hdm.uch.todoservice.tdm;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.TDMFatalException;

public class TodoServiceTDM extends SuperTDM {
	
		public void startDiscovery() throws TDMFatalException {
		ToDoServiceDiscovery discovery = new ToDoServiceDiscovery(this, this.tdmListener);
		discovery.start();
		}

	public void stopDiscovery() throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}

}
