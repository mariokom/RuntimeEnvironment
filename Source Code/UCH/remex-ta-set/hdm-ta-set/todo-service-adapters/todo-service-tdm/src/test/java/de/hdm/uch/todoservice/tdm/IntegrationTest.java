package de.hdm.uch.todoservice.tdm;

import org.openurc.tdmframework.DiscoveryTest;
import org.openurc.tdmframework.TestHelpers;
import org.openurc.tdmframework.TestTDM;

public class IntegrationTest {

	
	public static void main (String args[]){
		
		DiscoveryTest dt = new DiscoveryTest();
		dt.setDiscovery(new ToDoServiceDiscovery(new TestTDM(), TestHelpers.getTestTDMListener("../todo-service-ta/todo-service-ta.rprop")));
		dt.discoveryTest();

		
	}
}
