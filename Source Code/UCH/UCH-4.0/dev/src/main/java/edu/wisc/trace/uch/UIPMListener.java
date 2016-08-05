/*

Copyright (C) 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.openurc.uch.IProfile;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.IUIPM;
import org.openurc.uch.IUIPMListener;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
import org.openurc.uch.UIPMFatalException;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.ZipUtilities;

/**
 * Implements the methods of IUIPMListener. 
 * Also maintain information about UIPMs and their session.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UIPMListener implements IUIPMListener {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private UCH uch;
	
	private Map<String, IUIPM> sessionIdUIPMMap = new TreeMap<String, IUIPM>();
	
	private List<IUIPM> uipmList = new ArrayList<IUIPM>();
	
	private Map<String, List<IUIPM>> targetIdUIPMsMap = new TreeMap<String, List<IUIPM>>();
	
	/**
	 * Constructor.
	 * Provide the reference of UCH to local variable.
	 * 
	 * @param uch an Object of UCH
	 */
	public UIPMListener(UCH uch) {
		
		this.uch = uch;
	}
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//----------------------------------- Methods invokes from IUIPM Starts --------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	/**
	 * @see org.openurc.uch.IUIPMListener#addCompatibleUI(String, String, String, List, Map, List, List)
	 */
	public boolean addCompatibleUI(String targetId, String socketName,
			String protocolShortName, List<String> uris,
			Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons, List<Map<String, IProfile>> contexts) throws UCHException {
		
		return uch.addCompatibleUI(targetId, socketName, protocolShortName, uris, protocolInfo, icons, contexts);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#closeSession(java.lang.String)
	 */
	public void closeSession(String sessionId) {
		
		uch.closeSession(sessionId);
		
		synchronized (sessionIdUIPMMap) {
			sessionIdUIPMMap.remove(sessionId);
		}
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getAvailableTargetIds(Map)
	 */
	public List<String> getAvailableTargetIds(Map<String, IProfile> context) {
		
		return uch.getAvailableTargetIds(context);
	}
	
	/**
	 * @see org.openurc.uch.IUIPMListener#getDocument(String, String, Map)
	 */
	public String getDocument(String uri, String postData, Map<String, IProfile> context) throws UCHException {

		return uch.getDocument(uri, postData, context);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getIndices(String, List)
	 */
	public List<Set<String>> getIndices(String sessionId, List<String> eltPaths) {
		
		return uch.getIndices(sessionId, eltPaths);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getIpAddress()
	 */
	public String getIpAddress() {

		return uch.getIpAddress();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getLocalUCHStore()
	 */
	public IUCHStore getLocalUCHStore() {
		
		return uch.getLocalUCHStore();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getLocators(String)
	 */
	public Map<String, String> getLocators(String targetName) {
		
		return uch.getLocators(targetName);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getResources(String, List)
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps) {
	
		return uch.getResources(sessionId, resProps);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getSocketDescriptionUri(java.lang.String, java.lang.String)
	 */
	public String getSocketDescriptionUri(String targetName, String socketName) {
		
		return uch.getSocketDescriptionUri(targetName, socketName);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getSocketFriendlyName(java.lang.String, java.lang.String)
	 */
	public String getSocketFriendlyName(String targetId, String socketName) {
		
		return uch.getSocketFriendlyName(targetId, socketName);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getSocketName(java.lang.String)
	 */
	public String getSocketName(String sessionId) {
		
		return uch.getSocketName(sessionId);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getSocketNames(java.lang.String)
	 */
	public List<String> getSocketNames(String targetName) {
		
		return uch.getSocketNames(targetName);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getTargetDescriptionUri(java.lang.String)
	 */
	public String getTargetDescriptionUri(String targetName) {
		
		return uch.getTargetDescriptionUri(targetName);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getTargetName(java.lang.String)
	 */
	public String getTargetName(String targetId) {
		
		return uch.getTargetName(targetId);
	}

	 
	/**
	 * @see org.openurc.uch.IUIPMListener#getTargetProps(java.lang.String)
	 */
	public Map<String, Object> getTargetProps(String targetId) {	

		return uch.getTargetProps(targetId);
	}

	/**
	 * Get the UCH Properties.
	 */
	public Map<String, String> getUCHProps() {
		
		return uch.getUCHProps();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#getValues(String, List, List, List, List, List)
	 */
	// 2012-09-24 : Method signature changed, added extra attributes depths, pruneIndices, pruneXMLContent according to new spec.
	
	public List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets,
			List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent) {
		
		return uch.getValues(sessionId, paths, includeSets, depths, pruneIndices, pruneXMLContent);
	}

		
	/**
	 * @see org.openurc.uch.IUIPMListener#invokeLocator(String, String)
	 */
	public void invokeLocator(String targetId, String locatorId) {
		
		uch.invokeLocator(targetId, locatorId);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#isElementAvailable(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean isElementAvailable(String targetId, String socketName, String elementId) {
		
		return uch.isElementAvailable(targetId, socketName, elementId);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#isImplemented(java.lang.String)
	 */
	public boolean isImplemented(String functionName) {

		Method[] methods = this.getClass().getMethods();
		
		for ( Method method : methods) {
			
			if( method.getName().equals(functionName) )
				return true;
		}
		
		return false;
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#openSessionRequest(IUIPM, String, String, Map, Map)
	 */
	public synchronized Map<String, String> openSessionRequest(IUIPM uipm, String targetId,
			String socketName, Map<String, String> clientProps, Map<String, IProfile> context) {

		Map<String, String> openSessionResponseMap = uch.openSessionRequest(targetId, socketName, clientProps, context);

		if ( openSessionResponseMap == null )
			return null;
		
		if ( Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_ACCEPT
				.equals( openSessionResponseMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT) ) ) {

			String sessionId = openSessionResponseMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_SESSION_ID);
			
			if ( sessionId != null ) {
				
				synchronized(sessionIdUIPMMap) {
					sessionIdUIPMMap.put(sessionId,uipm);
				}
			}
		}
		
		return openSessionResponseMap;
	}
	
	/**
	 * @see org.openurc.uch.IUIPMListener#removeCompatibleUIs(List)
	 */
	public void removeCompatibleUIs(List<String> uris) throws UCHException {
		
		uch.removeCompatibleUIs(uris);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#removeUIPM(IUIPM)
	 */
	public void removeUIPM(IUIPM uipm)throws UCHNotImplementedException {
		
		if ( uipm == null ) {
			logger.warning("UIPM is null.");
			return;
		}
		
		uch.removeUIPM(uipm);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#setValuesRequest(java.lang.String, java.util.List, java.util.List, java.util.List)
	 */
	public Map<String, List<String>> setValuesRequest(String sessionId,
			List<String> paths, List<String> operations, List<String> reqValues) {

		return uch.setValuesRequest(sessionId, paths, operations, reqValues);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#uploadResources(List, List)
	 */
	public List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> props, List<String> resourceUri) {
		
		return uch.uploadResources(props, resourceUri);
	}
	
	/**
	 * @see org.openurc.uch.IUIPMListener#startUriService(IUIPM, String, int, boolean, String, boolean, List)
	 */
	public String startUriService(IUIPM uipm, String scheme, int port,
			boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts)
			throws UCHException {
		
		return uch.startUriService(uipm, scheme, port, portIsFlexible, basePath, basePathIsFlexible, contexts);
	}

	/**

	 * @see org.openurc.uch.IUIPMListener#stopUriService(IUIPM, String)
	*/
	public void stopUriService(IUIPM uipm, String uri) throws UCHException {
		
		uch.stopUriService(uipm,uri);
	}

	/** 
	 * @see org.openurc.uch.IUIPMListener#getElementRef(String, String)
	 */
	public String getElementRef(String sessionId, String path)
			throws UCHNotImplementedException {
		
		throw new UCHNotImplementedException();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#isSessionResumable(String)
	 */
	public boolean isSessionResumable(String sessionId)
			throws UCHNotImplementedException {
		
		throw new UCHNotImplementedException();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#isSessionSuspendable(String)
	 */
	public boolean isSessionSuspendable(String sessionId)
			throws UCHNotImplementedException {
		
		throw new UCHNotImplementedException();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#resumeSession(String)
	 */
	public boolean resumeSession(String sessionId)
			throws UCHNotImplementedException {
		
		throw new UCHNotImplementedException();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#suspendSession(String, long)
	 */
	public long suspendSession(String sessionId, long suggestedTimeout)
			throws UCHNotImplementedException {
		
		throw new UCHNotImplementedException();
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#addUriServiceContexts(IUIPM, String, List)
	 */
	public void addUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) throws UCHException {
		
		uch.addUriServiceContexts(uipm, uri, contexts);
	}

	/**
	 * @see org.openurc.uch.IUIPMListener#removeUriServiceContexts(IUIPM, String, List)
	 */
	public void removeUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) throws UCHException {
		
		uch.removeUriServiceContexts(uipm, uri, contexts);
	}
	
	/**
	 * @see org.openurc.uch.IUIPMListener#getContexts()
	 */
	public List<Map<String, IProfile>> getContexts() {
		
		return getContexts();
	}
	
	/**
	 * @see org.openurc.uch.IUIPMListener#getTargetContexts(String)
	 */
	public List<Map<String, IProfile>> getTargetContexts(String targetId) {
		
		return uch.getTargetContexts(targetId);
	}
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invokes from IUIPM Ends ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invoked from UCH Starts ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Cleanup relative information about the session specified by 'sessionId'.
	 * Also call the method abortSession() of relative IUIPM.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param code a String value specifying reason of session abortion
	 */
	//void abortSession(String sessionId) { //Parikshit Thakur : 20111203. Changed signature for the change in UCH spec. 
	void abortSession(String sessionId, String code) {
		
		IUIPM uipm = sessionIdUIPMMap.get(sessionId);
		
		if ( uipm == null )
			return;
		
		try {
			
			uipm.sessionAborted(sessionId, code);
			
			synchronized (sessionIdUIPMMap) {
				sessionIdUIPMMap.remove(sessionId);
			}
			
		} catch (UIPMFatalException e) {
			
			logger.warning("UIPMFatalException : Aborting Session '"+sessionId+"'.");
		}
	}
	
	/**
	 * Call the same method on uipm
	 * 
	 * @param sessionIds a List&lt;String&gt; value of sessionIds
	 * @param eltIds a List&lt;String&gt; value of elementIds
	 * @param resources a List&lt;Map&lt;String, Object&gt;&gt; values of resources
	 *
	 */
	
	void updateDynRes(List<String> sessionIds, List<String> eltIds, List<Map<String, Object>> resources) {
		
		Map<IUIPM, List<String>> uipmSessionsMap = getUipmSessionIdsMap(sessionIds);
		
		for(IUIPM uipm : uipmSessionsMap.keySet()){
			
			List<String> sessions = uipmSessionsMap.get(uipm);
			
			if(sessions.size() > 0){
				
				uipm.updateDynRes(sessions, eltIds, resources);
			}
		}
		
	}

	/**
	 * Informs the UIPM that the values have been updated for particular Session.
	 * 
	 * @param sessionIds an object of List&lt;String&gt; specifies sessionIds
	 * @param paths an object of List&lt;String&gt; specifies paths
	 * @param operations an object of List&lt;String&gt; operations
	 * @param values an object of List&lt;String&gt; specifies values
	 * @param props a List&lt;Map&lt;String, String&gt;&gt; of map containing attributes for path e.g. socketElType(var, cmd or ntf), hasDynRes and notification type( alert, error, info).
	 */
	void updateValues(List<String> sessionIds, List<String> paths,
			List<String> operations, List<String> values, List<Map<String, String>> props) {
		Map<IUIPM, List<String>> uipmSessionIdsMap = getUipmSessionIdsMap(sessionIds);
		if ( uipmSessionIdsMap == null )
			return;
		
		for ( Entry<IUIPM, List<String>> entry : uipmSessionIdsMap.entrySet() ) {
			if ( entry == null )
				continue;
			
			
			IUIPM uipm = entry.getKey();
			List<String> sessionIdList = entry.getValue();
			if ( (uipm == null) || (sessionIdList == null) || (sessionIdList.size() == 0) ) 
				continue;
			try {
				uipm.updateValues(sessionIdList, paths, operations, values, props);
			} catch (UIPMFatalException e) {
				logger.warning("UIPMFatalException : Going to update values for sessionIds '"+sessionIdList+"'.");
			}
		}
		
	}
	
	
	/**
	 * Instantiate related IUIPMs for specified Target.
	 * 
	 * @param targetId a String value of targetId
	 * @param targetName a String value of targetName
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	synchronized void instantiateUIPMs(String targetId, String targetName, List<Map<String, IProfile>> contexts) {
		
		if ( targetId == null ) {
			logger.warning("Target Id is null.");
			return;
		}
		
		if ( targetName == null ) {
			logger.warning("Target Name is null.");
			return;
		}
		
		List<IUIPM> targetUIPMs = instantiateUIPMs(targetName, contexts);
		
		if ( targetUIPMs != null ) {
			
			for (IUIPM uipm : targetUIPMs) {
				
				if ( uipm == null )
					continue;
				
				addTargetUIPM(targetId, uipm);
			}
		}
	}
	
	/**
	 * Called when any new target is Discovered in the network and informs to relative IUIPM.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	void targetDiscovered(String targetId, List<Map<String, IProfile>> contexts) {	
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		List<IUIPM> uipms = getTargetUIPMs(targetId);
		
		if ( uipms == null ) {
			logger.info("Unable to get UIPMs for the targetId '"+targetId+"'.");
			return;
		}
		
		for ( IUIPM uipm : uipms ) {
			
			try {
				uipm.targetDiscovered(targetId, contexts);
			} catch (UIPMFatalException e) {
				logger.warning("UIPMFatalException");
			}
			
		}
		
	}
	
	/**
	 * Called when any existing target is Discarded from the network.
	 * 
	 * @param targetId a String value of targetId
	 */
	void targetDiscarded(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		List<IUIPM> uipms = getTargetUIPMs(targetId);
		
		if ( uipms == null ) {
			logger.info("Unable to get UIPMs for the targetId '"+targetId+"'.");
			return;
		}
		
		for ( IUIPM uipm : uipms ) {
			
			try {
				uipm.targetDiscarded(targetId);
			} catch (UIPMFatalException e) {
				logger.warning("UIPMFatalException");
			}
			
		}
		
		removeTargetUIPMs(targetId);
	}
	
	/**
	 * Removes all found UIPM clients and  
	 * 
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void contextsOpened(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		synchronized(uipmList) {
				
			for ( IUIPM uipm : uipmList ) {
					
				if ( uipm == null ) 
					continue;
					
				try {
					uipm.contextsOpened( cloneList(contexts) );
					uipm.finalize();
					this.removeUIPM(uipm);
					
				} catch (UIPMFatalException e) {
					logger.warning("Trying to open contexts with the UIPM '"+uipm.getClass().getName()+"'.");
				} catch (UCHNotImplementedException e) {
					logger.warning("UCHNotImplementedException : In contextOpened.");
				}
			}
			uipmList.clear();
		}
		
		// Takes user profile into consideration to find favorite UIPMs and UIPM Clients for discovered targets.
		
		for(String targetId: targetIdUIPMsMap.keySet()) {
				
			logger.info("TargetID : "+targetId);
				
			targetIdUIPMsMap.put(targetId, null);
				
			this.instantiateUIPMs(targetId, uch.getTargetName(targetId), contexts);
			
			this.targetDiscovered(targetId, contexts);
		}
	}
	
	
	/**
	 * Call the same method on all UIPMs. 
	 * 
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	/*void contextsClosed(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		synchronized(uipmList) {
			
			for ( IUIPM uipm : uipmList ) {
				
				if ( uipm == null ) 
					continue;
				
				try {
					uipm.contextsClosed( cloneList(contexts) );
				} catch (UIPMFatalException e) {
					logger.warning("Trying to close contexts with the UIPM '"+uipm.getClass().getName()+"'.");
				}
			}
		}
	}*/
	
	void contextsClosed(List<Map<String, IProfile>> contexts) {
		
		synchronized(uipmList) {
				
			for ( IUIPM uipm : uipmList ) {
					
				if ( uipm == null ) 
					continue;
					
				try {
					uipm.contextsClosed(contexts);
					uipm.finalize();
					this.removeUIPM(uipm);
					
				} catch (UIPMFatalException e) {
					logger.warning("Trying to close contexts with the UIPM '"+uipm.getClass().getName()+"'.");
				} catch (UCHNotImplementedException e) {
					logger.warning("UCHNotImplementedException : In contextClosed.");
				}
			}
			uipmList.clear();
		}
		
		// Find all UIPMs and UIPM Clients for discovered targets.
		for(String targetId: targetIdUIPMsMap.keySet()) {
				
			System.out.println("TargetID : "+targetId);
				
			targetIdUIPMsMap.put(targetId, null);
				
			this.instantiateUIPMs(targetId, uch.getTargetName(targetId), contexts);
			
			this.targetDiscovered(targetId, null);
		}
	}
	
	/**
	 * 
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		List<IUIPM> uipms = getTargetUIPMs(targetId);
		
		if ( uipms == null ) {
			logger.info("Unable to get UIPMs for the targetId '"+targetId+"'.");
			return;
		}
		
		for ( IUIPM uipm : uipms ) {
			
			if ( uipm == null ) 
				continue;
			
			try {
				uipm.targetContextsAdded(targetId, contexts);
			} catch (UIPMFatalException e) {
				logger.warning("Trying to add target contexts of target '"+targetId+"' with the UIPM '"+uipm.getClass().getName()+"'.");
			}
		}
		
	}
	
	void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		if ( contexts == null ) {
			logger.warning("Contexts is null.");
			return;
		}
		
		List<IUIPM> uipms = getTargetUIPMs(targetId);
		
		if ( uipms == null ) {
			logger.info("Unable to get UIPMs for the targetId '"+targetId+"'.");
			return;
		}
		
		for ( IUIPM uipm : uipms ) {
			
			if ( uipm == null ) 
				continue;
			
			try {
				uipm.targetContextsRemoved(targetId, contexts);
			} catch (UIPMFatalException e) {
				logger.warning("Trying to add target contexts of target '"+targetId+"' with the UIPM '"+uipm.getClass().getName()+"'.");
			}
		}
		
	}
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invoked from UCH Ends ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Download UIPM from Resource Server and instantiate it.
	 * 
	 * @param targetName a String value of Target Name
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @return whether UIPM instantiate successfully or not.
	 */
	private List<IUIPM> instantiateUIPMs(String targetName, List<Map<String, IProfile>> contexts) {

		List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
		
		Map<String, List<String>> uipmResProps = new HashMap<String, List<String>>();
		
		if (contexts != null) {// If context is present.
			
			Map<String, IProfile> map = contexts.get(0);

			for (String userName : contexts.get(0).keySet()) {
				
				IProfile profile = map.get(userName);
				
				for (String key : profile.getCoreKeys()) {
					// Add user profile content properties into the UIPM query properties.
					uipmResProps.put(key, Arrays.asList(profile.getValue(key, null).split(",")));
				}// end of inner for
			}// end of outer for
		}
		
		uipmResProps.put(Constants.PROPERTY_FOR_TARGET_NAME, CommonUtilities.convertToList(targetName) );
		uipmResProps.put(Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_RES_TYPE_VALUE_UIPM) );	
		uipmResProps.put(Constants.PROPERTY_RUNTIME_PLATFORM, CommonUtilities.convertToList(Constants.PROPERTY_PLATFORM_VALUE_JAVA) );
		
		String conformsTo = uch.getConformsTo();
		
		if ( conformsTo != null )	
			uipmResProps.put(Constants.PROPERTY_CONFORMS_TO, CommonUtilities.convertToList(conformsTo) );
		
		uipmResProps.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		uipmResProps.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );

		resProps.add(uipmResProps);
		
		List<List<Map<String, List<String>>>> uipmResList = getResources(null, resProps);
		
		if ( uipmResList == null ) {
			logger.warning("Unable to get UIPM from Resource Server.");
			return null;
		}
		
		boolean flag = false;
		
		List<IUIPM> returnList = new ArrayList<IUIPM>();
		
		resourceLoop:
		for( List<Map<String, List<String>>> retUipmPropsList : uipmResList) {
			
			if ( retUipmPropsList == null )
				continue;
			
			
			for ( Map<String, List<String>> uipmProps : retUipmPropsList ) {
				
				if ( uipmProps == null )
					continue;
				
				String uipmDLL = CommonUtilities.getFirstItem( uipmProps.get(Constants.PROPERTY_RES_DYNAMIC_LIB_CLASS) );
				
				if ( uipmDLL == null ) {
					logger.warning("Unable to get the value of '"+Constants.PROPERTY_RES_DYNAMIC_LIB_CLASS+"' from UIPM Properties.");
					continue;
				}
				
				synchronized (uipmList) {
					
					for (IUIPM uipmFromList : uipmList) {
						
						if( uipmFromList.getClass().getName().equals(uipmDLL) ) {
							
							//logger.info("Compare : "+uipm.getClass().getName()+"="+uipmClassName );
							returnList.add(uipmFromList);
							flag = true;
							continue resourceLoop;
						} else {
							//logger.info("Compare : "+uipm.getClass().getName()+"!="+uipmClassName );
						}
					}
					
					
					String jarFIlePathURI = getJarFileUri(uipmProps);
					
					// Parikshit Thakur : 20110829. Changes to get ClassLoader from uch instead of uchStore.
					/*Object object = getLocalUCHStore().getValue(Constants.CLASS_PATH_VALUE_CLASS_LOADER);
					
					if ( !(object instanceof ClassLoader) ) {
						logger.warning("Unable to get object of '"+Constants.CLASS_PATH_VALUE_CLASS_LOADER+"' from Local UCH Store.");
						continue;
					}
					
					ClassLoader classLoader = (ClassLoader)object;*/
					
					ClassLoader classLoader = uch.getClassLoader();
					if(classLoader == null ){
						return null;
					}
					
					// Parikshit Thakur : 20110829. Changes end.
					
					// Parikshit Thakur : 20110823. Changes to load multiple jars for a single resource.
					if(jarFIlePathURI.endsWith(".jar")){
						if ( !classLoader.addJar( jarFIlePathURI ) ) 
							continue;
					}
					else{
						try {
							File resDir = new File(new URI(jarFIlePathURI));
							loadJars(resDir, jarFIlePathURI, classLoader);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
					try {
						
						Class uipmClass = classLoader.loadClass(uipmDLL);
						IUIPM uipm = (IUIPM)uipmClass.newInstance();
					
						Map<String, String> newUIPMProps = new HashMap<String, String>();
						
						for ( String key : uipmProps.keySet() ) {
							
							String value = CommonUtilities.getFirstItem( uipmProps.get(key) );
							
							if ( value != null )
								newUIPMProps.put(key, value );
						}
						
					
						uipmList.add(uipm);
						returnList.add(uipm);
						try {
							
							uipm.init(this, newUIPMProps, uch.getUCHProps(), contexts);

						} catch (UIPMFatalException e) {
							
							logger.warning("UIPMFatalException");
							uipmList.remove(uipm);
						}
				
						logger.info("'"+uipmProps.get(Constants.PROPERTY_RES_NAME)+"' UIPM instantiate successfully.");
						flag = true;
						continue;
						
					} catch (InstantiationException e) {
						logger.warning("InstantiationException : Instantiate UIPM '"+uipmProps.get(Constants.PROPERTY_RES_NAME)+"'. ");
					} catch (IllegalAccessException e) {
						logger.warning("IllegalAccessException : Instantiate UIPM '"+uipmProps.get(Constants.PROPERTY_RES_NAME)+"'. ");
					} catch (Exception e) {
						e.printStackTrace();
						logger.warning("Exception : Instantiate UIPM '"+uipmProps.get(Constants.PROPERTY_RES_NAME)+"'. ");
					}
					
					logger.warning("Unable to instantiate UIPM for '"+uipmProps.get(Constants.PROPERTY_RES_NAME)+"'.");
				}
			}
		}
			
		return returnList;
	}
	
	/**
	 * Get Jar file path from specified Property Map.
	 * 
	 * @param propMap an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a String value of Jar file URI
	 */
	String getJarFileUri(Map<String, List<String>> propMap) {
		
		if ( propMap == null )
			return null;
		
		String mimeType = CommonUtilities.getFirstItem( propMap.get(Constants.PROPERTY_MIME_TYPE) );
		
		if ( mimeType == null )
			return null;
		
		String resourceLocalAt = CommonUtilities.getFirstItem( propMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
		if ( resourceLocalAt == null ) {
			logger.warning("Unable to get '"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"' for '"+propMap.get(Constants.PROPERTY_RES_NAME)+"'.");
			return null;
		}
		
		if ( mimeType.equals(Constants.PROPERTY_MIME_TYPE_VALUE_JAVA_ARCHIVE) ) {
			
			return resourceLocalAt;
			
		} else if ( mimeType.equals(Constants.PROPERTY_MIME_TYPE_VALUE_ZIP) ) {
			
			String indexFile = CommonUtilities.getFirstItem( propMap.get(Constants.PROPERTY_INDEX_FILE) );
			
			if ( indexFile == null )
				return null;
			
			if ( resourceLocalAt.indexOf(indexFile) != -1 ) {
				
				String dirPathUri = resourceLocalAt.substring(0, resourceLocalAt.indexOf(indexFile));
				
				// Parikshit Thakur : 20110823. Changes to load multiple jars for a single resource.
				// Extracting zip file, if resources are zipped.
				try {
					File dir = new File( new URI( dirPathUri ) );
					
					String[] files = dir.list();
					
					for ( String fileName : files ) {
						
						if ( fileName == null )
							continue;
						
						//if ( fileName.endsWith(".jar") )
						//	return dirPathUri + "/" + fileName;
						
						if(fileName.endsWith(".zip")){
							try{
								ZipUtilities.unzip(new File(dir + File.separator +  fileName), dir);
								//extractZipFile(dir +File.separator+  fileName, dir);
								//return dirPathUri;
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
											
					}
					
				} catch (URISyntaxException e) {
					logger.severe("URISyntaxException : finding Jar File Path : invalid URI String '"+dirPathUri+"'.");
				}
				
				return dirPathUri;
				// Change ends
				
			}
		}
		
		return null;
	}
	
	
	/**
	 * Loads the jarFiles in classLoader.
	 * @param resDir a File object for the directory containing the jar
	 * @param jarFilePathURI string uri of the directory containing the jar
	 * @param classLoader a ClassLoader object
	 * @return true is jar loaded successfully, else false
	 */
	private boolean loadJars(File resDir, String jarFilePathURI, ClassLoader classLoader ) {
		
		File[] jarFiles = resDir.listFiles(new ExtensionFilter(".jar"));

		if ((jarFiles != null) && (jarFiles.length != 0)) {

			for (File jarFile : jarFiles) {

				if (jarFile == null)
					continue;
				
				if ( !classLoader.addJar( jarFilePathURI + jarFile.getName() ) ) 
					return false;
			}
			
			return true;
		}

		// Get directories to further searching for .jar files
		File[] dirs = resDir.listFiles(new DirectoryFilter());

		if ((dirs != null) && (dirs.length != 0)) {

			for (File dir : dirs) {

				if (dir == null)
					continue;
				loadJars(dir, jarFilePathURI + dir.getName() + "/", classLoader);
			}
		}
		return true;
	}
	
	
		
	
	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------------- Utilities Methods Starts -------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//

	/**
	 * Copy entries of specified list and return a new List.
	 * 
	 * @param list an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	private List<Map<String, IProfile>> cloneList(List<Map<String, IProfile>> list) {
		
		if ( list == null )
			return null;
		
		List<Map<String, IProfile>> cloneList = new ArrayList<Map<String,IProfile>>();
		
		for ( Map<String, IProfile> map : list ) {
			
			if ( map == null )
				continue;
			
			cloneList.add( cloneMap(map) );
		}
		
		return cloneList;
	}
	
	
	/**
	 * Copy entries of specified map and return a new Map.
	 * 
	 * @param map an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of Map&lt;String, IProfile&gt;
	 */
	private Map<String, IProfile> cloneMap(Map<String, IProfile> map) {
		
		if ( map == null )
			return null;
		
		Map<String, IProfile> cloneMap = new HashMap<String, IProfile>();
		
		for ( Entry<String, IProfile> entry : map.entrySet() ) {
			
			String key = entry.getKey();
			IProfile value = entry.getValue();
			
			if ( (key == null) || (value == null) )
				continue;
			
			cloneMap.put(key, value);
		}
		
		return cloneMap;
	}
	

	/**
	 * Distribute sessionIds by UIPM and return it.
	 * 
	 * @param sessionIds an Object of List&lt;String&gt; represents sessionIds
	 * 
	 * @return an Object of Map&lt;IUIPM, List&lt;String&gt;&gt;
	 */
	private Map<IUIPM, List<String>> getUipmSessionIdsMap(List<String> sessionIds) {
	//	logger.info("sessionIds....in getUipmSessionIdsMap........."+sessionIds);
		if ( sessionIds == null ) 
			return null;
		
		Map<IUIPM, List<String>> returnMap = new HashMap<IUIPM, List<String>>();
		
		for ( String sessionId : sessionIds ) {
			//logger.info("session id in loop........." + sessionId);
			IUIPM uipm = null;
		//	logger.info("length of sessionIdUIPMMap:::" + sessionIdUIPMMap.size());
			synchronized(sessionIdUIPMMap) {
				uipm = sessionIdUIPMMap.get(sessionId);
			}
				
			if ( uipm == null )
				continue;
			
			if ( returnMap.containsKey(uipm) ) {
				
				List<String> values = returnMap.get(uipm);
				values.add(sessionId);
				
			} else {
				
				List<String> values = new ArrayList<String>();
				values.add(sessionId);
				returnMap.put(uipm, values);
			}
		}
		
		return returnMap;
	}
	
	/**
	 * Add specified targetId and UIPM 
	 * 
	 * @param targetId a String value of TargetId
	 * @param uipm an Object of IUIPM
	 */
	private void addTargetUIPM(String targetId, IUIPM uipm) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		if ( uipm == null ) {
			logger.warning("UIPM is null.");
			return;
		}
		
		synchronized (targetIdUIPMsMap) {
			
			List<IUIPM> uipms = targetIdUIPMsMap.get(targetId);
			
			if ( uipms == null ) {
				
				uipms = new ArrayList<IUIPM>();
				uipms.add(uipm);
				targetIdUIPMsMap.put(targetId, uipms);
				
			} else {
				
				if ( !uipms.contains(uipm) ) // If UIPM is not already exists then add a UIPM.
					uipms.add(uipm);
				
			}
		}
	}
	
	/**
	 * Get UIPMs for specified targetid from targetIdUIPMsMap.
	 * 
	 * @param targetId a String value of targetId
	 */
	private List<IUIPM> getTargetUIPMs(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return null;
		}
		
		synchronized (targetIdUIPMsMap) {
			return targetIdUIPMsMap.get(targetId);
		}
	}
	
	/**
	 * Remove entry of specified UIPM from targetIdUIPMsMap.
	 * 
	 * @param targetId a String value of targetId
	 */
	private void removeTargetUIPMs(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		synchronized (targetIdUIPMsMap) {
			targetIdUIPMsMap.remove(targetId);
		}
	}
	
	
	/**
	 * Implements FilenameFilter.
	 * Provide implementation of the method accept of FilenameFileter interface.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class ExtensionFilter implements FilenameFilter {
		
		private String extension;
		 
		/**
		 * Constructor.
		 * Provide reference of the local variable extension.
		 *  
		 * @param extension a String value of file extension
		 */
		private ExtensionFilter( String extension ) {
			
			this.extension = extension;             
		}
		 
		/**
		 * Check whether specified filename ends with given extension.
		 * 
		 * @param dir an Object of File that specifies a directory
		 * @param name a String value of filename
		 * 
		 * @return a boolean value specifies whether specified file name ends with given extension.
		 */
		public boolean accept(File dir, String name) {
			
			return (name.endsWith(extension));
		}
		
		
	}

	
	/**
	 * Implement directory filter.
	 * Provide implementation of the method accept of FilenameFileter interface.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class DirectoryFilter implements FilenameFilter {

		/**
		 * Default Constructor.
		 */
		private DirectoryFilter() {
			
		}
		
		/**
		 * Check whether specified filename represents directory or not.
		 * 
		 * @param dir an Object of File that specifies a directory
		 * @param name a String value of filename
		 * 
		 * @return a boolean value specifies whether specified filename represents a directory or not
		 */
		public boolean accept(File dir, String name) {
			
			if ( dir == null || name == null ) 
				return false;
			
			File f = new File(dir, name);
			
			return ( f.exists() && f.isDirectory() );
		}
		
		
	}


	@Override
	public boolean openUserContext(String authorization) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean closeUserContext(String authorization) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public List<Boolean> validateValues(String sessionId, List<String> paths,
			List<String> values) throws UCHNotImplementedException {
		// TODO Auto-generated method stub
		throw new UCHNotImplementedException();
	}



	@Override
	public List<Boolean> getDependencyValues(String sessionId,
			List<String> paths, List<String> dependencies)
			throws UCHNotImplementedException {
		// TODO Auto-generated method stub
		throw new UCHNotImplementedException();
	}

	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------------- Utilities Methods Ends --------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//

}
