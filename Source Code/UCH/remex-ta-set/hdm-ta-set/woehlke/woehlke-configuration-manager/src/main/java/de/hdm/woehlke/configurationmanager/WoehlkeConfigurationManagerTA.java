package de.hdm.woehlke.configurationmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import de.hdm.woehlke.api.WoehlkeConstants;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;
public class WoehlkeConfigurationManagerTA extends SuperTA<Handler> {

	private List paths;

	private Logger logger = LoggerUtil.getSdkLogger();
	
	public WoehlkeConfigurationManagerTA(SuperTAFacade facade) {
		super(facade);
		
	}

	@Override
	public void executeCommand(String commandName, Session session, String value) {
		

		switch (commandName){
		case "/addNewWebSteckdose/IpAddress":
			logger.info("commandName \"" + commandName +"\" is being executed.");			

			sendCommandStateUpdate( commandName, "inProgress"); // execution of command addNewWebsteckdose has started. Hence command stat is changed to "inProgress 
			WoehlkeConfigurationManagerTAFacade facade = (WoehlkeConfigurationManagerTAFacade) this.TAFacade;
			Map<String, String> targetMap = new HashMap<String, String>(); // Map contains all information that are needed to register a new target
			targetMap.put(WoehlkeConstants.IpAddress ,  value); //only IP of the new Websteckdose is needed
			
			facade.getDiscoveryModule().registerNewTarget(targetMap); // Discoverymodule is the WoehlkeTDM
			sendCommandStateUpdate("/addNewWebSteckdose", "done"); // command successfully executed, hence command state is changed again. 
					
		}
		
	}

	
	@Override
	public void initSessionValues(Session session) {
		
		session.setValue("addNewWebSteckdose[state]","initial" ); 
		
	}

	@Override
	public void update(Session session){
		// Nothing to update
//		TAFacade.sendUpdatedValues(sessionIdSessionMap.keySet()	  , paths, operations, values, props);
	}

}