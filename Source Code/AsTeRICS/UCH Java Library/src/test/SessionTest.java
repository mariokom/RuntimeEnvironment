package test;


import java.util.Set;

import cy.ac.ucy.cs.seit.uchCommunication.UchSession;
import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.GetResourceRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.GetResourcesWithPropRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.GetUpdatesRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.GetValuesRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.requests.SetValuesRequest;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.GetElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.GetResourceElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.ResourceElement;
import cy.ac.ucy.cs.seit.uchCommunication.session.xmlElements.SetElement;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.SocketDescription;

/**
 * Class that tests the {@link UchSession} object and its functionality.
 * 
 * Overview:
 * 	-	retrieve the URC-HTTP protocol URI
 *  -	retrieve the URC-HTTP socket description
 *  -	create a sessions using the URC-HTTP protocol URI
 *  - 	start calling URC-HTTP function using the session object
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class SessionTest {

	public static void main(String[] args) {
		
		System.out.println("==========================================================================================");
		System.out.println("Testing the session calls.");
		System.out.println("==========================================================================================\n");
		
		
		//The UCH communicator object that retrieves information from the xml files (UI list)
		UchCommunicator uchCommunicator = null;
		
		//The object that holds the description of a specific component
		//Some information of this object will be used for the session requests
		SocketDescription socketdescription = null;
		
		//the set which holds the id of the variables in a socket description
		Set<String> variablesId = null;
		
		//first we have to retrieve the information we need from the UI list
		String httpUrcURI = "";
		try {
			uchCommunicator = new UchCommunicator();
			uchCommunicator.setStorageMode(false);
			
			//retrieve the first uiId in the set
			String uiId = uchCommunicator.getAllUiIds().get(0);
			System.out.println("uiId for testing:\n" + uiId + "\n");
			
			//retrieve the URC-HTTP protocol URI of this uiId
			httpUrcURI = uchCommunicator.getProtocolURI(uiId, UchCommunicator.URC_HTTP);
			
			//get its socket description
			socketdescription = uchCommunicator.getSocketDescription(uiId); //get the socket description with the given id
			
			variablesId = socketdescription.getVariablesIds(); //get all its variables
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("Variable ids contained in the socket description:\n" + variablesId);
		
		//create a session by using the UchCommunicator object and
		//retrieve the Session object using the the sessionId
		String sessionId = uchCommunicator.createSession_getRequest(httpUrcURI);
		UchSession session = uchCommunicator.getSession(sessionId); 
		
		System.out.println("\nSession Id:\n" + sessionId);

		
		/* Session calls */
		System.out.println("\n\n*** Start using the URC-HTTP protocol functions ***\n\n");
		
		
		//Get values from a session
		GetValuesRequest getValuesRequest = new GetValuesRequest();
		for (String variablePath: variablesId) {
			if (variablePath.equals("targetTempProgram")) { //why is it failing when i try to retrieve this value?
				continue;
			}
			getValuesRequest.addGetElement(new GetElement(variablePath));
		}
		System.out.println("Get values response (all values):\n" + session.getValues(getValuesRequest));
		
		getValuesRequest = new GetValuesRequest();
		getValuesRequest.addGetElement(new GetElement("currentRoomTemp"));
		System.out.println("Get values response (currentRoomTemp):\n" + session.getValues(getValuesRequest));
		
		
		//Get updates
		GetUpdatesRequest getUpdatesRequest = new GetUpdatesRequest();
		getUpdatesRequest.addGetElement(new GetElement("currentRoomTemp"));
		System.out.println("Get updates response (currentRoomTemp):\n" + session.getUpdates(getUpdatesRequest));
		
		
		//Set values
		SetValuesRequest setValuesRequest = new SetValuesRequest();
		setValuesRequest.addSetElement(new SetElement("currentRoomTemp", "321"));
		System.out.println("Set values response (currentRoomTemp):\n" + session.setValues(setValuesRequest));
		
		
		//Get Resource
		ResourceElement resourceElement = new ResourceElement();
		resourceElement.setEltRef("currentRoomTemp");
		GetResourceRequest getResourceRequest = new GetResourceRequest();
		getResourceRequest.addGetResourceElement(resourceElement);
		System.out.println("Get Recource response (currentRoomTemp):\n" + session.getResource(getResourceRequest));
		
		
		//Get Resource with properties
		resourceElement = new ResourceElement("audience", "forLang", "format", "valRef", "", "type", "forTargetInstance", "publisher", "creator", "", "opRef", "role", "eltRef", "");
		GetResourceElement getResourceElement = new GetResourceElement("0", "1", resourceElement);
		resourceElement.setEltRef("currentRoomTemp");
		GetResourcesWithPropRequest getResourcesWithPropRequest = new GetResourcesWithPropRequest();
		getResourcesWithPropRequest.addGetResourceElement(getResourceElement);
		System.out.println("Get Recource with Properties response (currentRoomTemp):\n" + session.getResourcesWithProperties(getResourcesWithPropRequest));
		
		
		//Get dependency values - UCH JAVA SERVER DOES NOT IMPLEMENT THIS FUNCIONALITY YET
		//System.out.println("\n GET DEPENDENCIES:\n" + session.getDependencyValues(""));
		
		
		//Suspend session - UCH JAVA SERVER DOES NOT IMPLEMENT THIS FUNCIONALITY YET
		//session.suspend(5);
		
		
		//Resume session - UCH JAVA SERVER DOES NOT IMPLEMENT THIS FUNCIONALITY YET
		//session.resume();
		
		
		uchCommunicator.closeSession(httpUrcURI, sessionId);
		
		System.out.println("==========================================================================================");
		System.out.println("End of test.");
		System.out.println("==========================================================================================\n");
		
	}
	
}
