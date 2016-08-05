package org.philips.hue.configurationmanager;

import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;

public class HueConfigurationManagerTA extends SuperTA<Handler>{
	private Logger logger = LoggerUtil.getSdkLogger();


	public HueConfigurationManagerTA(SuperTAFacade<HueConfigurationManagerTA> facade) {
		super(facade);
	}
	
	/**
	 * starts a new bridge search if command 'DiscoverBridge' is received
	 */
	@Override
	public void executeCommand(String commandName, Session session, String value) {
		
		switch (commandName) {
		case "/DiscoverBridge":
			HueConfigurationManagerTAFacade facade = (HueConfigurationManagerTAFacade) this.TAFacade;
			facade.getDiscoveryModule().registerNewTarget();
			break;

		default:
			logger.info("Unvalid Command: " +commandName);
			break;
		}		
	}

	@Override
	public void initSessionValues(Session session) {
		// Nothing to initialize
		
	}

	@Override
	public void update(Session session) {
		// Nothing to update
		
	}

}
