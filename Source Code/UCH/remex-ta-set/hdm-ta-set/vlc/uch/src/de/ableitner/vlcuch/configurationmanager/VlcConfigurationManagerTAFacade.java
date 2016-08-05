package de.ableitner.vlcuch.configurationmanager;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTAFacade;
import org.openurc.uch.IProfile;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;

import de.ableitner.vlcuch.api.DiscoveryModule;
import de.ableitner.vlcuch.api.VlcConstants;
import edu.wisc.trace.uch.util.LoggerUtil;


public class VlcConfigurationManagerTAFacade extends SuperTAFacade<VlcConfigurationManagerTA> {
	public Logger logger = LoggerUtil.getSdkLogger();
	DiscoveryModule woehlkeTDM;

	public VlcConfigurationManagerTAFacade() {
		super("VlcConfigurationManagerTAFacade");
	}

	@Override
	public void registerTarget(String targetId, Map<String, Object> targetProps, List<Map<String, IProfile>> contexts)
			throws TAException, TAFatalException {
		
		this.logger.log(Level.INFO, "Log Tobias: test");
		
		if(targetId == null){
			this.logger.log(Level.INFO, "Log Tobias: targetId is null!");	
		}
		if(targetProps == null){
			this.logger.log(Level.INFO, "Log Tobias: targetProps is null!");	
		}
		if(contexts == null){
			this.logger.log(Level.INFO, "Log Tobias: contexts is null!");	
		}
		
		if(woehlkeTDM == null){
			this.logger.log(Level.INFO, "Log Tobias: woehlkeTDM is null!");	
		}
		
		super.registerTarget(targetId, targetProps, contexts);
		woehlkeTDM = (DiscoveryModule) targetProps.get(VlcConstants.VLC_DISCOVERY_MODULE);
		
		if(woehlkeTDM == null){
			this.logger.log(Level.INFO, "Log Tobias: woehlkeTDM is null!");	
		}
	}

	protected DiscoveryModule getDiscoveryModule() {
		return woehlkeTDM;
	}

	@Override
	public String getSocketFriendlyName(String targetId, String socketName) {
		// not good
		return VlcConstants.WCM_FRIENDLY_NAME;
	}

	@Override
	public VlcConfigurationManagerTA createSpezificTA(SuperTAFacade facade) {
		return new VlcConfigurationManagerTA(facade);

	}

}
