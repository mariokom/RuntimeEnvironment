package test;

import java.util.Set;

import cy.ac.ucy.cs.seit.uchCommunication.UchSession;
import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;
import cy.ac.ucy.cs.seit.uchCommunication.UchDiscovery;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.GetValuesRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.SetValuesRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.GetElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.SetElement;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.SocketDescription;

public class CompinationTest {

	public static void main(String[] args) throws Exception {
		System.out.println("==========================================================================================");
		System.out.println("Compination Test: Discover UCH, parse UIlist, pick a component, retrieve/change a value");
		System.out.println("==========================================================================================\n");
		
		//UCH ip discovery
		Set<String> ipSet = UchDiscovery.getUchServerIPs();
		String uchIP = "";
		if (ipSet.size() < 1) {
			System.out.println("Could not detect a UCH server in the local network");
			System.exit(0);
		}
		else {
			//get the first ip (usually it would be only one)
			for (String ip: ipSet) {
				uchIP = ip;
				break;
			}
			System.out.println("\nUCH ip address: " + uchIP);
		}
		
		
		//The UCH communicator object that retrieves information from the xml files (UI list)
		UchCommunicator uchCommunicator = new UchCommunicator(uchIP, 8080, false);
		
		
		//show all the uuIDs and retrieve the first uiId in the set for testing
		System.out.println("\nAll UI Ids in the UCH server:\n" + uchCommunicator.getAllUiIds() + "\n");
		String uiId = uchCommunicator.getAllUiIds().get(0);
		System.out.println("Choose a uiID for testing:\n" + uiId + "\n");
		
		
		//retrieve the URC-HTTP protocol URI of this uiId
		String httpUrcURI = uchCommunicator.getProtocolURI(uiId, UchCommunicator.URC_HTTP);
		
		
		//get its socket description
		SocketDescription socketdescription = uchCommunicator.getSocketDescription(uiId); //get the socket description with the given id
		
		
		//get all the available variables of the component
		Set<String> variablesId = socketdescription.getVariablesIds(); //get all its variables
		System.out.println("Variable ids contained in the socket description:\n" + variablesId);
		
		
		//create a session using the UchCommunicator object and
		//retrieve the Session object using the the sessionId
		String sessionId = uchCommunicator.createSession_getRequest(httpUrcURI);
		UchSession session = uchCommunicator.getSession(sessionId); 
		System.out.println("\nSession Id:\n" + sessionId + "\n");
		
		
		//get the value for the 'currentRoomTemp' variable
		GetValuesRequest getValuesRequest = new GetValuesRequest();
		getValuesRequest.addGetElement(new GetElement("currentRoomTemp"));
		String gvrText = session.getValues(getValuesRequest);
		System.out.println("Get values response (all values):\n" + gvrText + "\n");
		//GetValuesResponse getValuesResponse = new GetValuesResponse(gvrText); //currently not working (should work in an updated UCH version)
		
		
		//set the value for the 'currentRoomTemp' variable to 111
		SetValuesRequest setValuesRequest = new SetValuesRequest();
		setValuesRequest.addSetElement(new SetElement("currentRoomTemp", "111"));
		String svrText = session.setValues(setValuesRequest);
		System.out.println("Set values response (currentRoomTemp):\n" + svrText + "\n");
		//SetValuesResponse setValuesResponse = new SetValuesResponse(svrText);//currently not working (should work in an updated UCH version)
		
		
		//get the value for the 'currentRoomTemp' variable
		getValuesRequest = new GetValuesRequest();
		getValuesRequest.addGetElement(new GetElement("currentRoomTemp"));
		gvrText = session.getValues(getValuesRequest);
		System.out.println("Get values response (all values):\n" + gvrText);
		//GetValuesResponse getValuesResponse = new GetValuesResponse(gvrText); //currently not working (should work in an updated UCH version)
		
		
		System.out.println("==========================================================================================");
		System.out.println("End of test.");
		System.out.println("==========================================================================================\n");
		
	}

}
