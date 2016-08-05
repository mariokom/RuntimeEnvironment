package edu.wisc.trace.uch.contextmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

class LocalContextManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private List<Context> localContexts;
	
	
	LocalContextManager(String contextFileURI) {
		
		if ( contextFileURI == null ) {
			logger.severe("Local Context File URI is null.");
			return;
		}
		
		localContexts = new LocalContextParser().parse(contextFileURI);
	}
	
	List<Map<String, IProfile>> getContexts(String userName, String password) {
		
		if ( (localContexts == null) || (localContexts.size() == 0) ) {
			logger.warning("Local Context List is null.");
			return null;
		}
		
		Context context = findContext(userName, password);
		
		if ( context == null ) {
			logger.warning("Unable to get local context for username '"+userName+"' and password '"+password+"'.");
			return null;
		}
		
		return createProfileList(context);
	}
	
	
	private Context findContext(String userName, String password) {
		
		if ( (userName == null) || (password == null) )
			return null;
		
		if ( localContexts == null )
			return null;
		
		synchronized (localContexts) {
			
			for ( Context context : localContexts ) {
				
				if ( context == null ) 
					continue;
				
				if ( userName.equals(context.getUserName()) && password.equals(context.getPassword()) )
					return context;
			}
		}
		
		return null;
	}
	
	
	private List<Map<String, IProfile>> createProfileList(Context context) {
		
		if ( context == null ) {
			logger.warning("Context is null.");
			return null;
		}
		
		IProfile userProfile = context.getUserProfile();
		
		if ( userProfile == null ) {
			logger.warning("Unable to get User Profile");
			return null;
		}
		
		List<IProfile> controllerProfiles = context.getControllerProfiles();
		
		if ( (controllerProfiles == null) || (controllerProfiles.size() == 0) ) {
			logger.warning("Unable to get Controller Profiles");
			return null;
		}
		
		List<Map<String, IProfile>> profileList = new ArrayList<Map<String,IProfile>>();
		
		for ( IProfile controllerProfile : controllerProfiles ) {
			
			if ( controllerProfile == null )
				continue;
			
			Map<String, IProfile> profileMap = new HashMap<String, IProfile>();
			
			profileMap.put(Constants.PROFILE_USER, userProfile);
			profileMap.put(Constants.PROFILE_CONTROLLER, controllerProfile);
			
			profileList.add(profileMap);
		}
		
		return profileList;
	}
}
