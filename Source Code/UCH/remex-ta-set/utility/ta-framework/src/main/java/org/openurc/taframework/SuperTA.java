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

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;
import edu.wisc.trace.uch.util.socket.IUISocketElement;
import edu.wisc.trace.uch.util.socket.UISocketNotification;
/**
 * 
 * @author Lukas Smirek
 *
 * @param <T> 
 */
public abstract class SuperTA<T> extends Thread {
	public Logger logger = LoggerUtil.getSdkLogger();
	protected Updater updater = null;
	private static final String NOTIFICATION_STATE_ACTIVE = "active";
	public static final String NOTIFICATION_STATE_INACTIVE = "inactive";
	
	public Map<String, Session> sessionIdSessionMap;
	public SuperTAFacade TAFacade;
	protected T handler;
	private Map<String, Object> targetProps;
	protected int updateIntervall  = 2000;  //  intervall im ms, indicates  how frequently syncronisation with targets take place.  
	
	public SuperTA(SuperTAFacade superTAFacade){
		
	this.TAFacade = superTAFacade;
	sessionIdSessionMap = new HashMap<String, Session>();
	this.handler = createInstanceOfProtocolHandler();
	}
		
	public synchronized Map<String, List<String>>  setValuesRequest(String sessionId, boolean isValidated, List<String> paths,
			List<String> operations, List<String> reqValues) {
				if ( (sessionId == null) || (paths == null) || (operations == null) || (reqValues == null) 
				  || (paths.size() == 0) || ( paths.size() != operations.size() ) ||  ( paths.size() != reqValues.size() ) ) {	
					
					logger.info("Input arguments are invalid.");
					return null;
				}
				
				Session session = getSessionForSessionId(sessionId);
				
				if ( session == null ) {
					logger.warning("No session  was found for sessionId '"+sessionId+"'.");
					return null;
				}
				
				List<String> returnPathList = new ArrayList<String>();
				List<String> returnValueList = new ArrayList<String>();
				List<String> returnOperationList = new ArrayList<String>();
				
				List<String> updatedPaths = new ArrayList<String>();
				List<String> updatedValues = new ArrayList<String>();
				List<String> updatedOperations = new ArrayList<String>();
				List<Boolean> hasDynRes = new ArrayList<Boolean>();
				List<Map<String, String>> propList = new ArrayList<Map<String, String>>();
				
				for ( int i=0 ; i<paths.size(); i++ ) {
					
					String elementPath = paths.get(i);
					String reqValue = reqValues.get(i);
					String operation = operations.get(i);
					
					if ( operation.equals("K") )
						reqValue = NOTIFICATION_STATE_INACTIVE;
					
					setValue(sessionId, session, elementPath, reqValue, operation, 
							returnPathList, returnValueList, returnOperationList, 
							updatedPaths, updatedOperations, updatedValues, hasDynRes);
					
					// Changes to add attributes of elements with updates. Parikshit Thakur : 20110729
					Map<String, String> prop = new HashMap<String, String>();
					int elTypeVal = session.getElementType(paths.get(i));
					String elType = null;
					if(elTypeVal == 1)
						elType = "var";
					else if(elTypeVal == 2 || elTypeVal == 3 || elTypeVal == 4)
						elType = "cmd";
					else if(elTypeVal == 6)
						elType = "ntf";
						
					prop.put("socketEltType", elType);
					prop.put("hasDynRes", "false");
					
					if(elType.equals("ntf")) {
						
						if((paths.get(i)).startsWith("/")){
							Map<String, IUISocketElement> returnMap = new HashMap<String, IUISocketElement>();
							int success = session.getSocketElementsByElementPath(paths.get(i), returnMap);
							if (success == 0) {
								IUISocketElement sockEle = returnMap.get(paths.get(i));
								String ntfCat = ((UISocketNotification) sockEle).getCategory();
								prop.put("notifyCat", ntfCat);
							}
						}
						else{
							IUISocketElement sockEle = session.getSocketElementByElementId(paths.get(i), false);
							String ntfCat = ((UISocketNotification) sockEle).getCategory();
							prop.put("notifyCat", ntfCat);
						}
						
					}
					propList.add(prop);
					// Change ends.
				}
				
				Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
				
				returnMap.put("paths", returnPathList);
				returnMap.put("values", returnValueList);
				returnMap.put("operations", returnOperationList);
				
				// Sending Updates to other Session
				
				if ( returnPathList.size() == 0 ) // There is not any updated value, so no need to update other session. 
					return returnMap;
				
				List<String> sessionIdList = new ArrayList<String>();
				
				for ( String sesId : sessionIdSessionMap.keySet() ) {
					
					if ( !sessionId.equals(sesId) )
						sessionIdList.add(sesId);	
				}
			
				
				TAFacade.sendUpdatedValues(sessionIdList, updatedPaths, updatedOperations, updatedValues, propList);
				
				return returnMap;
			
			}

	public Session getSessionForSessionId(String sessionId) {
		Session session = sessionIdSessionMap.get(sessionId);
		
		if(session == null){
			logger.warning("No Session Exist for sessionId ="+sessionId);
		}
		return session;
	}

	public List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets,
			List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent){
			Session session = getSessionForSessionId(sessionId);
			
			List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
			
			for ( int i=0; i<paths.size() ; i++ ) {
				
				String elementPath = paths.get(i);
	logger.info( elementPath );			
				if ( elementPath == null )
					continue;
				
				if ( elementPath == null || elementPath.trim().equals("") ) {
					logger.info("elementPath is not valid.");
					continue;
				}
				
				elementPath =elementPath.trim();
				
				boolean includeSet = includeSets.get(i);
				
				if ( elementPath.equals("/") ) { // elementPath = /
					
					logger.info("Return session Values :" + session.getValue("/", true));
					return session.getValue("/", true);
					//return session.getValue("/", includeSet);
					
				} else {
					
					List<Map<String, String>> valueList = session.getValue(elementPath, includeSet);
					
					if ( valueList != null ) 
						returnList.addAll(valueList);
					
				}
			}
			
			return returnList;
		}

/**
 * Set the specified value of specified Path.
 * Also invoke relative command on GC-100 Device.
 * 
 * @param elementPath a String value of Element Path
 * @param value a String value
 * @param session an Object of Session
 * @param returnPathList an Object of List&lt;String&gt;
 * @param returnValueList an Object of List&lt;String&gt;
 * @param returnOperationList an Object of List&lt;String&gt;
 */
public void setValue(String sessionId, Session session, String elementPath, String value, String operation,
		List<String> returnPathList, List<String> returnValueList, List<String> returnOperationList,
		List<String> updatedPaths, List<String> updatedOperations, List<String> updatedValues, List<Boolean> hasDynRes) {
	
	logger.info("Setting value of '"+elementPath+"' as '"+value+"'.");
	
	if ( (elementPath == null) || (value == null) || (session == null) || 
		 (returnPathList == null) || (returnValueList == null) || (returnOperationList == null) ) {
		return;
	}
	
	String elementId = null;
	elementPath = elementPath.trim();
	elementId = elementPath;
//	if ( elementPath.indexOf("/") == -1 ) { // e.g. elementPath = id
//				elementId = elementPath;
//			} else {
//		
//		elementId = elementPath.substring( elementPath.lastIndexOf('/')+1 );
//	}
//	
	if ( elementId.indexOf('[') != -1 ) {
		
		logger.warning("UIS doesn't contain any Dimensional Element.");
		return;
	}
	
	if ( !session.isElementExists( elementPath ) ) {
				logger.warning("Element Id '"+elementPath+"' does not exist.");
		return;
	}
	String commandName = elementId;
	executeCommand(commandName,session, value);
}
/*
 *always called when a controler sends a request to the related target.  
 *Based on the parameter command it is decided which function to call / execute on thetarget
 *@parameter path, this is the path of the addressed soccket element 
 *@parameter session, session that is used by the calling controller
 * @parameter value, new value for the socket element
 */
 public abstract void executeCommand(String commandName, Session session, String value);
	 

public void setTAFacade(SuperTAFacade facade){
this.TAFacade = facade;
}

private T createInstanceOfProtocolHandler() {
T instance = null;			
	try {
	    instance = (T) ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
	


} catch (Exception e) {
					e.printStackTrace();
}
	return instance;
	
}

/*
 *called during the initialisation of a session,
 *for all socket elements a initial value shall be set in the session object by using the session.setValue(... ) method   
 */

public abstract void initSessionValues(Session session);

public boolean isSessionExists(String sessionId){
	if ( sessionId == null )
		return false;
	
	synchronized (sessionIdSessionMap) {
		
		if ( sessionIdSessionMap.containsKey(sessionId) )
			return true;
		else
			return false;
	}


}

boolean addSession(String sessionId, Session session) {
	
	if ( (sessionId == null) || (session == null) )
		return false;
	
	synchronized (sessionIdSessionMap) {
		sessionIdSessionMap.put(sessionId, session);
	}
	
	return true;
}

String getSocketName(String sessionId) {
	
	if ( sessionId == null )
		return null;
	
	Session session = getSessionBySessionId(sessionId);
	
	if ( session == null )
		return null;
	
	return session.getSocketName();
}

private Session getSessionBySessionId(String sessionId) {
	
	if ( sessionId == null )
		return null;
	
	synchronized (sessionIdSessionMap) {
		return sessionIdSessionMap.get(sessionId);
	}
}

///////////////////// ITA InterFace Methods Starts ///////////////////////

/**
 * Get the list of sessionIds.
 * 
 * @return an Object of List&lt;String&gt;
 */
List<String> getSessionIds() {
	
	Set<String> sessionIds = null;
	
	synchronized (sessionIdSessionMap) {
		sessionIds = sessionIdSessionMap.keySet();
	}
	
	if ( sessionIds == null )
		return null;
	
	List<String> sessionIdList = new ArrayList<String>();
	
	sessionIdList.addAll(sessionIds);
	
	return sessionIdList;
}

/**
 * Close the specified Session.
 * 
 * @param sessionId a String value of SessionId
 */
void sessionClosed(String sessionId) {
	
	if ( sessionId == null )
		return;
	
	removeSession(sessionId);
}

/**
 * Remove specified Session.
 * 
 * @param sessionId a String value of Session Id
 */
private void removeSession(String sessionId) {
	
	if ( sessionId == null )
		return;
	
	synchronized (sessionIdSessionMap) {
		sessionIdSessionMap.remove(sessionId);
	}
}

public void addTargetProps(Map<String, Object> targetProps2){
this.targetProps = targetProps2;

}

public void useTargetProps(Map<String, Object> targetProps2){
	logger.info("");
}

@Override
public void run() {
	
	while (true) {
		try {
			sleep(updateIntervall);
		} catch (InterruptedException e) {
						e.printStackTrace();
		}
		synchronized (this) {
			
		
		updater = new Updater(this.TAFacade   ); // Buffer for all updated values
		for (Session session : sessionIdSessionMap.values()) {

			update(session);
								}
	
	updater.sendUpdates(); // updater sends all values to the UCH
	updater=null;
		}
		}
}

/*
 * automaticaly, regularely called to sync the session object with the connected target
 *    variables can be updated by using the method updateVariable(...)
 */
public abstract void update(Session session);

public void sendCommandStateUpdate(String commandName, String value) {
	for (Session s : sessionIdSessionMap.values()){
	s.setValue(commandName + "[state]", value);
	s.setValue(commandName, "" );
	}
	
	List<String> paths = new LinkedList<String>();
	List<String> operations = new LinkedList<String>();
		List<String> values = new LinkedList<String>();
	List<Map<String,String>> props = new LinkedList<Map<String,String>>(); 
		
			paths.add(commandName + "[state]");
//	paths.add(commandName);
	
	operations.add("S");
//	operations.add("S");
	
	values.add(value);
//	values.add("");

Map<String, String> map = new HashMap<String,String>();
map.put("socketEltType", "cmd");   
	
	props.add(map);


	
	TAFacade.sendUpdatedValues(
			sessionIdSessionMap.keySet(), 
			paths, operations, values, props);
}
/* 
 *can be called to communicate new values of socket elements back to the connected controller,
 *compares the current value with the old one stored in the session object
 *in case that these values differ, the new value is stored in the current update object   
 */
protected void updateVariable(Session session, String path, String value) {
	
	Map<String, String> props = new HashMap<String,String>();
	props.put("socketEltType", "var");   
	
	String oldValue = session.getValue(path).get(0).get("value");
	if ( !oldValue.equals(value) ){
		logger.info(path + " has changed");
	session.setValue(path, value);
	updater.add(session, path,"S",value,props);	
	}else 		if ( value == null ){
		session.setValue(path, "");
		updater.add(session, path,"R","",props);	
		}
}

public void setVariable(Session session, String path, String value) {
session.setValue(path, value);
}

}
