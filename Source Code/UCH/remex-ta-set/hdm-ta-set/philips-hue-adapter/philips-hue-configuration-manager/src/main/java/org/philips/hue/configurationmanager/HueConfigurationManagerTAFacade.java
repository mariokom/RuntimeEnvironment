package org.philips.hue.configurationmanager;

import java.util.List;
import java.util.Map;

import org.openurc.taframework.SuperTAFacade;
import org.openurc.uch.IProfile;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.philips.hue.api.HueConstants;
import org.philips.hue.api.HueDiscoveryModule;


public class HueConfigurationManagerTAFacade extends SuperTAFacade<HueConfigurationManagerTA> {
	
	HueDiscoveryModule hueTDM;

	public HueConfigurationManagerTAFacade() {
		super("HueConfigurationManagerTAFacade");
	}
	
	@Override
	public void registerTarget(String targetId,
			Map<String, Object> targetProps,
			List<Map<String, IProfile>> contexts) throws TAException,
			TAFatalException {
			super.registerTarget(targetId, targetProps, contexts);
			hueTDM = (HueDiscoveryModule) targetProps.get(HueConstants.HUE_DISCOVERY_MODULE);
	}
	
	protected HueDiscoveryModule getDiscoveryModule() {
		return hueTDM;
	}
	@Override
	public String getSocketFriendlyName(String targetId, String socketName) {
		// not good
		return HueConstants.wcm_friendly_name; 
	}
	
	@Override
	public HueConfigurationManagerTA createSpezificTA(SuperTAFacade facade) {
		return new HueConfigurationManagerTA(facade);
	}

}
