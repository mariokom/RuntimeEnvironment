/*
Copyright 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).  
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

*/

package org.openurc.taframework;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITAListener;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.TANotImplementedException;
import org.openurc.util.NewConstants;

import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;
import edu.wisc.trace.uch.util.socket.IUISocketElement;
import edu.wisc.trace.uch.util.socket.TargetDescription;
  
 /**
  * 
  * @author Lukas Smirek
  *
  * @param <T>
  */
public abstract class SuperTAFacade<T extends SuperTA> implements ITA	

{
	private Logger logger = LoggerUtil.getSdkLogger();
	private static String sessionIDString = "session-";
	private int noOfSessions = 0;
	
		private Map<String, TargetDescription> tdUriTargetDescriptionMap;	
		private Map<String, TADetails> targetIdDetailsMap;
		private Map<String, T> sessionIdMap;		
		
		private ITAListener taListener;
		
	
		private Map<String, String> taProps;
				private String taName;
				private Map<String, Object> targetProps;
				private String facadeName; // Name of the implementing TAFacade, set in constructor of implementing subclass
				
	private static String TARGET_DESCRIPTION = "http://openurc.org/ns/res#tdUri";

	public SuperTAFacade(String taName) {
		this.taName = taName;
		logger.info("Target adapter: " + taName + " was instanziated");
	}
	
	/**
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() {
	
}

	public void init(ITAListener taListener, Map<String, String> taProps, Map<String, String> uchProps)
			throws TAFatalException {
	
		logger.info("initializing " + taName + " has started");
		
				if ( taListener == null ) {
					logger.warning("TA Listener is null.");
					throw new TAFatalException();
				}
				
				if ( taProps == null ) {
					logger.warning("TA Properties is null.");
					throw new TAFatalException();
				}
				
				if ( uchProps == null ) {
					logger.warning("UCH Properties is null.");
					throw new TAFatalException();
				}
				logger.info("Received taProps: " + taProps);
//				this.taName = taProps.get("http://openurc.org/ns/res#name");
				
				this.taProps = taProps;
				this.taListener = taListener;
				
				targetIdDetailsMap = new TreeMap<String, TADetails>();
								sessionIdMap	 = new HashMap<String, T> ();
				
				tdUriTargetDescriptionMap = new HashMap<String, TargetDescription>();
				targetProps = new HashMap<String, Object>();
				logger.info("Initialisation of " + taName + " completed");
			}

	public void registerTarget(String targetId, Map<String, Object> targetProps, List<Map<String, IProfile>> contexts)
			throws TAException, TAFatalException {
				
				logger.info(facadeName + " : TARGET ID  : "+targetId);
				logger.info(facadeName + ": TARGET Props: "+targetProps);
						
								if ( targetId == null ) {
					logger.severe("TargetId is null.");
					throw new TAException();
				}
			
				if ( targetProps == null ) {
					logger.severe("Target Properties is null.");
					throw new TAException();
				}
				
				// checkk if target with givn ID was alrady registered, if not continue
				TADetails<T> taDetails = targetIdDetailsMap.get(targetId);
								if ( taDetails != null ) {
					logger.severe("Target with targetId '"+targetId+"'  whas been registered before.");
					throw new TAException();
				}

								URI tduri = null;
								String uriString = (String)targetProps.get(NewConstants.TARGET_PROPS_TD_URI );
								if (uriString == null || uriString.equals("") ){
									logger.warning("Error: there is no entry for \"" +NewConstants.TARGET_PROPS_TD_URI +"\" in " + targetProps);
									return;
								}
								
				try {
					tduri = new URI(uriString);
				} catch (URISyntaxException e) {
					logger.warning("Error: invalide uri for .td file: " + uriString ); 
				}
				logger.info(uriString);
								TargetDescription targetDescription = new TargetDescription(tduri);
				tdUriTargetDescriptionMap.put(targetId, targetDescription);
				targetProps.put(targetId, targetProps);
				
								T targetAdapter = createInstanceOfTA(this,targetProps);
				taDetails = new TADetails<T>(targetId, targetProps, targetDescription, targetAdapter);
				
				targetIdDetailsMap.put(targetId, taDetails);
				
				targetAdapter.useTargetProps(targetProps);
				
				logger.info("Target with TargetID" + targetId+ " was successfuly added" );												
	}
	
	public T createInstanceOfTA(SuperTAFacade<T> superTAFacade, Map<String, Object> targetProps2)
		{
		T instance = createSpezificTA(this);
			instance.setTAFacade(superTAFacade);		
instance.addTargetProps(targetProps2);
			return instance;
	}
		
	public abstract T createSpezificTA(SuperTAFacade<T> superTAFacade);

	public Map<String, String> openSessionRequest(String targetId, String socketName, 
			Map<String, String> clientProps, Map<String, IProfile> context) throws TAFatalException {
		
	// TODO add code for contexts
	Map<String, String> openSessionReturnMap = new HashMap<String, String>();
	
	if ( targetId == null ) {
		logger.warning("TargetId is null.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	if ( socketName == null ) {
		logger.warning("SocketName is null.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	if ( clientProps == null ) {
		logger.warning("Client Properties is null.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	TADetails taDetails = getTADetails(targetId);
	
	if ( taDetails == null ) {
		logger.warning("Unable to get TA Details for the TargetId '"+targetId+"'.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	List<String> socketNames = taDetails.getTargetDescription().getSocketNames();
	if ( socketNames == null ) {
		logger.warning("Unable to get Socket Names for the TargetId '"+targetId+"'.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	if ( !socketNames.contains(socketName) ) {
		logger.warning("Target With targetId '"+targetId+"' has no socket with '"+ socketName+"'.");
		openSessionReturnMap.put("result", "R");
		return openSessionReturnMap;
	}
	
	openSessionReturnMap.put("result", "A");
	logger.info("Session request fort target " + targetId + " with socket name " + socketName + " was successful");
	return openSessionReturnMap;
}

	public void sessionOpened(String targetId, String sessionId, String socketName, 
			Map<String, String> clientProps, Map<String, IProfile> context) throws TAFatalException {
		
		if ( (targetId == null) || (sessionId == null) || (socketName == null)  ) {
			logger.warning("targetId, sessionId, socketName or clientProps is invalid.");
			return;
		}
		
				if ( clientProps == null ) {
			logger.warning("Client Properties is null.");
			return;
		}

TADetails taDetails = targetIdDetailsMap.get(targetId);
						if ( taDetails == null ) {
			logger.warning("Unable to get TA Details for the TargetId '"+targetId+"'.");
			return;
		}
		
				List<String> socketNames = taDetails.getTargetDescription().getSocketNames();
				if ( socketNames == null ) {
			logger.warning("Unable to get Socket Names for the TargetId '"+targetId+"'.");
			return;
		}
		
		if ( !socketNames.contains(socketName) ) {
			logger.warning("Target With targetId '"+targetId+"' has no socket with '"+ socketName+"'.");
			return;
		}

List<IUISocketElement> socketElements = tdUriTargetDescriptionMap.get(targetId).getSocketElements(socketName);
				if ( socketElements == null ) {
			logger.warning("Unable to get Socket Elements for Socket Name '"+socketName+"'.");
			return;
		}
		
				T targetAdapter =		(T)taDetails.getTA();
				if ( targetAdapter == null ) {
			logger.warning("Unable to get TA for the Socket Name '"+socketName+"'.");
			return;
		}
		if ( targetAdapter .isSessionExists(sessionId) ) {
			logger.warning("Session is already exists with Session Id '"+sessionId+"'.");
			return;
		}

		Session session = new Session(sessionId, targetId, taDetails.getTargetProps(), socketName, socketElements);
		noOfSessions++;
		targetAdapter.addSession(sessionId, session);
		sessionIdMap.put(sessionId, targetAdapter);
				
						targetAdapter.initSessionValues(session);
	    if (targetAdapter.getState() == Thread.State.NEW){
				targetAdapter.start();
	    }
		List<Map<String, String>> valueMap = session.getValue("/", true);
		List<String> paths = new LinkedList<String>();
		List<String> values = new LinkedList<String>();
		List<String> operations = new LinkedList<String>();
		List<Map<String, String>> props = new LinkedList<Map<String,String>>();		
		
		
		for ( Map<String,String> map : valueMap){
			paths.add(map.get("path")  );
			values.add(map.get("value")  );
operations.add("S");

		}
		


		List<String> sessionIds = new LinkedList<String>();
		sessionIds.add(sessionId);
		sendUpdatedValues(sessionIds, paths, operations, values, props);		
	}

	public List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets,
			List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent)
			throws TAFatalException {
		
	logger.info(sessionId);
	
		if ( (sessionId == null) || (includeSets == null) || (paths.size() <= 0) || (paths.size() != includeSets.size()) ) {	
			logger.info("Input arguments are invalid.");
			return null;
		}
		
		T targetAdapter = getTA(sessionId);
		
		if ( targetAdapter== null )
			return null;
		
		
		return targetAdapter.getValues(sessionId, paths, includeSets, depths, pruneIndices, pruneXMLContent);

}

	public Map<String, List<String>> setValuesRequest(String sessionId,
			boolean isValidated, List<String> paths, List<String> operations,
			List<String> reqValues) throws TAFatalException {
	
		if (sessionId == null) {
			logger.warning("SessionId is null.");
			return null;
		}

		
		T targetAdapter = sessionIdMap.get(sessionId);
		
		if( targetAdapter== null ) {	
			logger.warning("Session is not opened for "+sessionId);
			System.out.println("not opened");
			return null;
		}
		
		return targetAdapter.setValuesRequest(sessionId, isValidated, paths, operations, reqValues);
			}
	
	public String getTargetName(String targetId) throws TAFatalException {
		TargetDescription targetDescription = getTargetDescriptionByTargetId(targetId);
		
		if ( targetDescription == null )
			return null;
		
		return targetDescription.getTargetName();
}
	
public String getTargetDescriptionUri(String targetName) throws TAFatalException {
		
		TargetDescription targetDescription = getTargetDescriptionByTargetName(targetName);
		
		if ( targetDescription == null )
		return null;
		
		return targetDescription.getTargetDescriptionUri();
	
	}
public List<String> getSocketNames(String targetName) throws TAFatalException {
	TargetDescription targetDescription = getTargetDescriptionByTargetName(targetName);
	
	if ( targetDescription == null )
		return null;
	
	return targetDescription.getSocketNames();
		}
public String getSocketFriendlyName(String targetId, String socketName) {
	
	if ( (targetId == null) || (socketName == null) )
		return null;
	
	TargetDescription targetDescription = getTargetDescriptionByTargetId(targetId);
	
	if ( targetDescription == null )
		return null;
	
	
	return targetDescription.getSocketFriendlyName(socketName);
}

		public String getSocketDescriptionUri(String targetName, String socketName)
			throws TAFatalException {
				
		if ( (targetName == null) || (socketName == null) )
			return null;
		
		TargetDescription targetDescription = getTargetDescriptionByTargetName(targetName);
		
		if ( targetDescription == null )
			return null;
		
		return targetDescription.getSocketDescriptionUri(socketName);
	}
	
		public String getSocketName(String sessionId) throws TAFatalException {
			
			if ( sessionId == null )
				return null;
			
			T targetAdapter= getTA(sessionId);
			
			if ( targetAdapter== null )
				return null;
			
			return targetAdapter.getSocketName(sessionId);

	}
	
		public Map<String, String> getTAProps() {
			return taProps;
		}
		
		
		/**
		 * @see org.openurc.uch.ITA#getTargetProps(String)
		 */
		public Map<String, Object> getTargetProps(String targetId)
				throws TAFatalException {

			TADetails<T> taDetails = getTADetails(targetId);
			
			return taDetails.getTargetProps();
		}
		

				public void sendUpdatedValues(List<String> sessionIds, List<String> paths,
			List<String> operations, List<String> values,
			List<Map<String, String>> props) { 

logger.info("UPDATES : \n Session Ids :"+sessionIds+"\nPaths :"+paths+"\nValues :"+values+"\nOperations :"+operations);
		
		taListener.updateValues(sessionIds, paths, operations, values, props);
	}

	private T getTA( String sessionId ) {
		
		if ( sessionId == null )
			return null;
		
		synchronized (sessionIdMap) {
			return sessionIdMap.get(sessionId);
		}
	}
	
	private TargetDescription getTargetDescriptionByTargetName(String targetName) {
		
		if ( targetName == null )
			return null;
		
		synchronized(targetIdDetailsMap) {
			
			for( TADetails TADetails : targetIdDetailsMap.values() ) {
				
				if ( TADetails == null )
					continue;
				
				TargetDescription targetDescription = TADetails.getTargetDescription();
				
				if ( targetDescription == null )
					continue;
				
				if ( targetName.equals( targetDescription.getTargetName() ) )
					return targetDescription;
			}
		}
		
		return null;
	}

	private TADetails getTADetails(String targetId) {
		if ( targetId == null )
			return null;
		
		synchronized(targetIdDetailsMap) {
			return targetIdDetailsMap.get(targetId);
		}
	}
	
	public List<String> getRegisteredTargetIds() throws TAFatalException {
		
		Set<String> targetIds = null;
		
		synchronized (targetIdDetailsMap) {
			targetIds = targetIdDetailsMap.keySet();
		}
		
		List<String> targetIdList = new ArrayList<String>();
		targetIdList.addAll(targetIds);
		
		return targetIdList;
	}
	
	/**
	 * 
	 * @see org.openurc.uch.ITA#getIndices(String, List)
	 */
	public List<Set<String>> getIndices(String sessionId, List<String> eltIds) {
		return null;
	}
	
	public List<String> getSessionIds(String targetId, String socketName)
			throws TAFatalException {

		if ( (targetId == null) || (socketName == null) )
			return null;
		
		TADetails irTADetails = getTADetails(targetId);
		
		if ( irTADetails == null )
			return null;
		
		TargetDescription targetDescription = irTADetails.getTargetDescription();
		
		if ( targetDescription == null )
			return null;
		
		List<String> socketNames = targetDescription.getSocketNames();
		
		if ( (socketNames == null) || !(socketNames.contains(socketName)) )
			return null;
		
		T irTA = (T)irTADetails.getTA();
		
		if ( irTA == null )
			return null;
		
		return irTA.getSessionIds();
	}

	/**
	 * 
	 * @see org.openurc.uch.ITA#getDynRes(String, List)
	 */
	
	public List<Map<String, Object>> getDynRes(String targetId,
			List<Map<String, String>> resProps) throws TAFatalException {
	
		return null;
	}

	

	//ok
	private TargetDescription getTargetDescriptionByTargetId(String targetId) {
		
		if ( targetId == null )
			return null;
		
TADetails taDetails = getTADetails(targetId);
		
		if ( taDetails == null )
			return null;
		
		TargetDescription targetDescription = taDetails.getTargetDescription();
		
		if ( targetDescription == null )
			return null;
		
		return targetDescription;
	}

	public void invokeLocator(String arg0, String arg1) throws TAException,
			TAFatalException {
				
				
			}

	public boolean isImplemented(String functionName) {
	Method[] methods = this.getClass().getMethods();
			
			for( Method method : methods) {
			
				if( method.getName().equals(functionName) )
					return true;
			}
			return false;
	
	
			}

	public boolean isElementAvailable(String targetId, String socketName,
			String eltId) throws TAFatalException {

		if ( (targetId == null) || (socketName == null) || (eltId == null) ) {
			logger.warning("targetId, socketName or eletId is invalid.");
			return false;
		}
		
		TargetDescription targetDescription = getTargetDescriptionByTargetId(targetId);
		
		if ( targetDescription == null )
			return false;
		
		return targetDescription.isElementAvailable(socketName, eltId);
	}


	public boolean resumeSessionRequest(String sessionId)
			throws TAFatalException, TANotImplementedException {
		throw new TANotImplementedException();
	}

	
	/** 
	 * @see org.openurc.uch.ITA#sessionClosed(java.lang.String)
	 */
	public void sessionClosed(String sessionId) throws TAFatalException {
		
		if ( sessionId == null )
			return;
		
		T irTA = getTA(sessionId);
		
		if ( irTA == null )
			return;
		
		irTA.sessionClosed(sessionId);
		removeTA(sessionId);
	}

	public long sessionSuspended(String sessionId, long suggestedTimeout)
			throws TAException, TAFatalException, TANotImplementedException {
			throw new TANotImplementedException();
		}

	public boolean suspendSessionRequest(String sessionId)
			throws TAFatalException, TANotImplementedException {
			
			throw new TANotImplementedException();
		}
	public void targetContextsAdded(String targetId,
			List<Map<String, IProfile>> contexts) throws TAFatalException {
		
		// TODO Auto-generated method stub	
	}
	public void targetContextsRemoved(String targetId,
			List<Map<String, IProfile>> contexts) throws TAFatalException {
		
		// TODO Auto-generated method stub	
	}
	public void targetRequest(String protocol, Object request, Object response, 
			Map<String, IProfile> context) throws TAFatalException, TANotImplementedException {
		throw new TANotImplementedException();
	}

	public void unregisterTarget(String targetId) throws TAFatalException {
		
		if ( targetId == null )
			return;
		
		TADetails irTADetails = null;
		
		synchronized (targetIdDetailsMap) {
			
			irTADetails = targetIdDetailsMap.get(targetId);
			
			if ( irTADetails != null )
				targetIdDetailsMap.remove(targetId);
		}
		
		if ( irTADetails == null ) 
			return;
		
		T irTA = (T)irTADetails.getTA();
		
		if ( irTA == null ) 
			return;
		
		List<String> sessionIdList = new ArrayList<String>();
		
		synchronized (sessionIdMap) {
			
			for ( String sessionId : sessionIdMap.keySet() ) {
				
				if ( sessionId == null )
					continue;
				
				T irTargetAdapter = sessionIdMap.get(sessionId);
				
				if ( irTA.equals(irTargetAdapter) ) 
					sessionIdList.add(sessionId);
			}
		}
		
		for ( String sessionId : sessionIdList ){
			//taListener.abortSession(sessionId); //Parikshit Thakur : 20111203
			taListener.abortSession(sessionId, null);
		}
			
		
		synchronized (sessionIdMap) {
			
			for ( String sessionId : sessionIdList ) 
				sessionIdMap.remove(sessionId);
		}
	}

	private void removeTA(String sessionId) {
		
		if ( sessionId == null )
			return;
		
		synchronized (sessionIdMap) {
			sessionIdMap.remove(sessionId);
		}
	}
	
	@Override
	public Map<String, String> getLocators(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionResumed(String arg0) throws TAException, TAFatalException, TANotImplementedException {
		// TODO Auto-generated method stub
		
	}

	public void sendUpdatedValues(Set<Session> keySet, List<String> paths, List<String> operations, List<String> values,
			List<Map<String, String>> props) {
		
		List<String> sessionIds = new LinkedList<String>();
		

		for (Session session : keySet) {
			sessionIds.add(session.getSessionId());
		}
		sendUpdatedValues(sessionIds, paths, operations, values, props);
		
	}

	public void sendUpdatedValues(String sessionId, List<String> paths, List<String> operations, List<String> values,
			List<Map<String,String>> props) {
		List<String> sessionIds = new LinkedList<String>();
		sessionIds.add(sessionId);
		sendUpdatedValues(sessionIds, paths, operations, values, props);
	}
}