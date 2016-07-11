package test;

import java.util.ArrayList;





import cy.ac.ucy.cs.seit.uchCommunication.UchCommunicator;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.ResSheet;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.SocketDescription;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.SocketDescriptionVariable;
import cy.ac.ucy.cs.seit.uchCommunication.uiList.TargetDescription;

/**
 * Class that test the {@link UchCommunicator} object.
 * 
 * @author Marios Komodromos
 * 		   email: mkomod05@cs.ucy.ac.cy
 * 
 */
public class UiListTest {

	public static void main(String[] args) {
		
		System.out.println("==========================================================================================");
		System.out.println("Testing out-of-session calls.");
		System.out.println("==========================================================================================\n\n");
		
		long startTime = System.currentTimeMillis();
				
		UchCommunicator uchCommunicator = null;
		try {
			uchCommunicator = new UchCommunicator();
			uchCommunicator.setStorageMode(false);
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			
			//UIList methods
			System.out.println("========== UI-list ==========");
			
			//System.out.println(uchCommunicator.getUIListText());
			System.out.println("\nAll UI Ids:\n" + uchCommunicator.getAllUiIds() + "\n");
			
			String uiId = uchCommunicator.getAllUiIds().get(0);
			System.out.println("Choose a uiID for testing:\n" + uiId + "\n");
			
			String uiName = uchCommunicator.getUiName(uiId);
			System.out.println("Name of the a UI:\n" + uiName + "\n");
			
			System.out.println("Available protocol Names of "+uiName+":\n" + uchCommunicator.getProtocolNames(uiId) + "\n");
			System.out.println("URC-HTTP protocol uri of "+uiName+":\n" + uchCommunicator.getProtocolURI(uiId, UchCommunicator.URC_HTTP) + "\n");
			System.out.println("HTML protocol uri of "+uiName+":\n" + uchCommunicator.getProtocolURI(uiId, UchCommunicator.HTTP_HTML) + "\n");
			System.out.println("SocketDescription uri of "+uiName+":\n" + uchCommunicator.getSocketDescriptionURI(uiId) + "\n");
			System.out.println("TargetDescription uri of "+uiName+":\n" + uchCommunicator.getTargetDescriptionURI(uiId) + "\n");
			
			
			
			
			//Socket Description methods
			System.out.println("\n\n\n\n  ========== Socket description ==========");
			SocketDescription socketDescription = uchCommunicator.getSocketDescription(uiId);
			
			//System.out.println(socketDescription.getText());
			System.out.println("\n  id: "+socketDescription.getId());
			System.out.println("\n  about: "+socketDescription.getAbout());
			System.out.println("\n  descritpion: "+socketDescription.getDescription());
			System.out.println("\n  creator: "+socketDescription.getCreator());
			
			ArrayList<String> variableIds = new ArrayList<String>(socketDescription.getVariablesIds());
			System.out.println("\n  variable Ids: "+variableIds);
			
			SocketDescriptionVariable sDvariable = socketDescription.getVariable(variableIds.get(0));
			System.out.println("\n      variable["+variableIds.get(0)+"] id: " + sDvariable.getId());
			System.out.println("\n      variable["+variableIds.get(0)+"] description: "+sDvariable.getDescription());
			
			
			
			
			//Target Description methods
			System.out.println("\n\n\n\n  ========== Target description ==========");
			TargetDescription targetDescription = uchCommunicator.getTargetDescription(uiId);
			
			//System.out.println(targetDescription.getText());
			System.out.println("\n  id: "+targetDescription.getId());
			System.out.println("\n  about: "+targetDescription.getAbout());
			System.out.println("\n  description: "+targetDescription.getDescription());
			System.out.println("  creator: "+targetDescription.getCreator());
			
			ResSheet resSheet = targetDescription.getResSheet();
			System.out.println("\n      ResSheet about: "+resSheet.getAbout());
			System.out.println("\n      ResSheet conforms to: "+resSheet.getConformsTo());
			System.out.println("\n      ResSheet language: "+resSheet.getForLang());
			System.out.println("\n      ResSheet types: "+resSheet.getTypes());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\n\ntotal execution time: " + (System.currentTimeMillis() - startTime) );
		
		
		System.out.println("\n==========================================================================================");
		System.out.println("End of test.");
		System.out.println("==========================================================================================\n");
		
	}

}
