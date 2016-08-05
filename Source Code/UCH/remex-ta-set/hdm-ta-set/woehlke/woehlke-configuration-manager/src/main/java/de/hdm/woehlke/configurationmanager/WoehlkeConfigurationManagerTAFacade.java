package de.hdm.woehlke.configurationmanager;

import java.util.List;
import java.util.Map;

import org.openurc.taframework.SuperTAFacade;
import org.openurc.uch.IProfile;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;

import de.hdm.woehlke.api.DiscoveryModule;
import de.hdm.woehlke.api.WoehlkeConstants;

public class WoehlkeConfigurationManagerTAFacade extends SuperTAFacade<WoehlkeConfigurationManagerTA> {
	DiscoveryModule woehlkeTDM;
	
public WoehlkeConfigurationManagerTAFacade() {
	super("WoehlkeConfigurationManagerTAFacade");
}
	
	
	@Override
	public void registerTarget(String targetId,
			Map<String, Object> targetProps,
			List<Map<String, IProfile>> contexts) throws TAException,
			TAFatalException {
			super.registerTarget(targetId, targetProps, contexts);
 woehlkeTDM = (DiscoveryModule) targetProps.get(WoehlkeConstants.WOEHLKE_DISCOVERY_MODULE );
	}

protected DiscoveryModule getDiscoveryModule() {
	return woehlkeTDM;
}

@Override
public String getSocketFriendlyName(String targetId, String socketName) {
	// not good
	return WoehlkeConstants.wcm_friendly_name; 
}

@Override
public WoehlkeConfigurationManagerTA createSpezificTA(SuperTAFacade facade) {
	return new WoehlkeConfigurationManagerTA(facade);
	
}

}
