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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITAListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;

import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Implements the methods of ITAListener.
 * Also maintain information about targets and its sessions.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class TAListener implements ITAListener {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private UCH uch;
	
	private List<String> removedSessionIdList = new ArrayList<String>();
	
	private List<TargetDetail> targetDetailList = new ArrayList<TargetDetail>();
	
	private Map<String, String> sessionIdTargetIdMap = new TreeMap<String, String>();
	
	private Map<String, String> sessionIdSocketNameMap = new TreeMap<String, String>();
	
	/**
	 * Constructor.
	 * Provide the reference of UCH to local variable.
	 * 
	 * @param uch an Object of UCH
	 */
	TAListener(UCH uch) {

		this.uch = uch;
	}
	
	/**
	 * Returns sessionIdTargetIdMap
	 * @return treeMap for sessionId TargetId
	 */
	public Map<String, String> getSessionIdTargetIdMap(){
		return sessionIdTargetIdMap;
	}
	
	/**
	 * Returns sessionId for the socketName
	 * @param socketName
	 * @return String value of session Id
	 */
	public String getSessionId(String socketName){
		
		for(Map.Entry<String, String> entry : this.sessionIdSocketNameMap.entrySet()){
			if(entry.getValue().equals(socketName)){
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invokes from ITA Starts ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * @see org.openurc.uch.ITAListener#getDocument(String, String, Map)
	 */
	public String getDocument(String uri, String postData, Map<String, IProfile> context) throws UCHException {
		
		return uch.getDocument(uri, postData, context);
	}

	/**
	 * @see org.openurc.uch.ITAListener#getIpAddress()
	 */
	public String getIpAddress() {
		
		return uch.getIpAddress();
	}

	/**
	 * @see org.openurc.uch.ITAListener#getLocalUCHStore()
	 */
	public IUCHStore getLocalUCHStore() {
		
		return uch.getLocalUCHStore();
	}

	/**
	 * @see org.openurc.uch.ITAListener#getResources(String, List)
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps) {
	
		return uch.getResources(sessionId, resProps);
	}

	/**
	 * @see org.openurc.uch.ITAListener#getUCHProps()
	 */
	public Map<String, String> getUCHProps() {
		
		return uch.getUCHProps();
	}

	/**
	 * @see org.openurc.uch.ITAListener#isImplemented(java.lang.String)
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
	 * @see org.openurc.uch.ITAListener#startUriService(ITA, String, int, boolean, String, boolean, List)
	 */
	public String startUriService(ITA ta, String scheme, int port, boolean portIsFlexible, String basePath,
			boolean basePathIsFlexible, List<Map<String, IProfile>> contexts) throws UCHException {
		
		return uch.startUriService(ta, scheme, port, portIsFlexible, basePath, basePathIsFlexible, contexts);
	}

	/**
	 * @see org.openurc.uch.ITAListener#stopUriService(ITA, String)
	 */
	public void stopUriService(ITA ta, String uri) throws UCHException {
		
		uch.stopUriService(ta, uri);
	}
	
	/**
	 * @see org.openurc.uch.ITAListener#uploadResources(List, List)
	 */
	public List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> props, List<String> resourceUri) {
		
		return uch.uploadResources(props, resourceUri);
	}
	
	/**
	 * @see org.openurc.uch.ITAListener#abortSession(java.lang.String, java.lang.String)
	 */
	//public void abortSession(String sessionId) { //Parikshit Thakur : 20111203. Changed signature for the change in UCH spec. 
	public void abortSession(String sessionId, String code) {
		
		synchronized (removedSessionIdList) {
			removedSessionIdList.add(sessionId);
		}
		
		uch.abortSession(sessionId, code);
	}

	/**
	 * @see org.openurc.uch.ITAListener#sessionForwardRequest(java.lang.String, java.util.Map)
	 */
	public void sessionForwardRequest(String sessionId,
			Map<String, String> forwardInfo) {
		
		
	}

	/**
	 * @see org.openurc.uch.ITAListener#setValidation(ITA, boolean)
	 */
	public boolean setValidation(ITA ta, boolean activate) {
		
		return false;
	}
	
	/**

	 * Signals that the set of resources pertaining to a specific socket element has been changed.
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds
	 * @param eltIds a List&lt;String&gt; of elementIds
	 */
	public void updateDynRes(List<String> sessionIds, List<String> eltIds) {
		
		uch.updateDynRes(sessionIds, eltIds);
	}
	
	/**
	 * @see org.openurc.uch.ITAListener#updateDynRes(List, List, List)
	 */
	public void updateDynRes(List<String> sessionIds, List<String> eltIds, List<Map<String, Object>> resources) {
		
		uch.updateDynRes(sessionIds, eltIds, resources);
	}

	/**
	 * @see org.openurc.uch.ITAListener#updateValues(java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	public void updateValues(List<String> sessionIds, List<String> paths,
			List<String> operations, List<String> values,
			List<Map<String, String>> props) {
		
		uch.updateValues(sessionIds, paths, operations, values, props);
	}
	
	/**
	 * @see org.openurc.uch.ITAListener#addUriServiceContexts(ITA, String, List)
	 */
	public void addUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) throws UCHException {

		uch.addUriServiceContexts(ta, uri, contexts);
	}

	/**
	 * @see org.openurc.uch.ITAListener#removeUriServiceContexts(ITA, String, List)
	 */
	public void removeUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) throws UCHException {
		
		uch.removeUriServiceContexts(ta, uri, contexts);
	}
	
	/**
	 * @see org.openurc.uch.ITAListener#getTargetContexts(String)
	 */
	public List<Map<String, IProfile>> getTargetContexts(String targetId) {

		return uch.getTargetContexts(targetId);
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invokes from ITA Ends ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	

	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invoked from UCH Starts ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Find relative TA for specified targetProps and taProps. And if it is not instantiated then instantiate it.
	 * Also register specified target to that TA.
	 * 
	 * @param targetProps an object of Map&lt;String, Object&gt; that specifies target properties
	 * @param taProps an object of Map&lt;String, String&gt; that specifies TA properties
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt; that specifies contexts
	 * 
	 * @return a String value that specifies targetId
	 */
	String targetDiscovered(Map<String, Object> targetProps, Map<String, String> taProps, List<Map<String, IProfile>> contexts) {
		
		logger.info("Target discovered in TargetManager named '"+targetProps.get(Constants.PROPERTY_RES_TARGET_FRIENDLY_NAME)+"'.");
		logger.info("Target Properties = "+targetProps);
		
		Object targetInstance = targetProps.get(Constants.PROPERTY_RES_INSTANCE_ID);
		
		if ( targetInstance == null ) {
			logger.warning("Unable to get '"+Constants.PROPERTY_RES_INSTANCE_ID+"' from Target Properties.");
			return null;
		}
		
		String targetId = targetInstance.toString().trim();
		
		if ( targetId.toString().trim().equalsIgnoreCase(Constants.PROPERTY_RES_INSTANCE_ID_VALUE_ALL) ) {
			logger.warning("'"+targetId+"' is not a valid value for '"+Constants.PROPERTY_RES_INSTANCE_ID+"'.");
			return null;
		}
		
		return instantiateTA( targetId, targetProps, taProps, contexts);
	}	
	
	
	/**
	 * Unregister the specified target. Also remove it from target list.
	 * 
	 * @param targetId a String value of targetId
	 */
	void targetDiscarded(String targetId) {
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null )
			return;
		
		if ( targetDetail != null ) {
			
			ITA ta = targetDetail.getITA();
			
			if ( ta != null ) {
				try {
					ta.unregisterTarget(targetId);
				} catch (TAFatalException e) {
					logger.severe("TAFatalException : Unregistering Target");
				}
			}
			
			synchronized (targetDetailList) {
				targetDetailList.remove(targetDetail);
			}
			
		}
		
	}
	
	/**
	 * Open a new Session with specified target and its socket.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param clientProps Map&lt;String, String&gt;
	 * 
	 * @return  Map&lt;String, String&gt;
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	Map<String, String> openSessionRequest(String targetId,
			String socketName, Map<String, String> clientProps, Map<String, IProfile> context) {
		logger.info("TAListener : openSessionRequest targetId:=============================="+targetId);
		logger.info("TAListener : openSessionRequest socketName:=============================="+socketName);
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if( targetDetail == null ) {
			logger.warning("TA not found for targetId '"+targetId+"'");
			return null;
		}
		
		ITA ta = targetDetail.getITA();
		
		Map<String, String> openSessionReturnMap = null;;
		
		try {
			
			openSessionReturnMap = ta.openSessionRequest(targetId, socketName, clientProps, context);
			
		} catch (TAFatalException e) {
			
			logger.severe("TAFatalException : Opening session with target '"+targetId+"' and socket '"+socketName+"'.");
			targetDiscarded(targetId);
			return null;
		}
		
		if ( openSessionReturnMap == null || openSessionReturnMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT) == null )
			return null;
	
		if ( openSessionReturnMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT).equals(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_REJECT) )
			return openSessionReturnMap;
		
		if ( openSessionReturnMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT).equals(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_ACCEPT) ) {
			
			String sessionId = generateRandomSessionId();
			
			try {
			
				ta.sessionOpened(targetId, sessionId, socketName, clientProps, context);
			} catch (TAFatalException e) {
				
				logger.severe("TAFatalException : Opening session with target '"+targetId+"' and socket '"+socketName+"'.");
				targetDiscarded(targetId);
				return null;
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.severe("Exception : Opening session with target '"+targetId+"' and socket '"+socketName+"'.");
				return null;
			}
			
			logger.info( "Opening session with sessionId '" + sessionId + "'.");
			
			openSessionReturnMap.put(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_SESSION_ID, sessionId);
		
			synchronized (sessionIdTargetIdMap) {
				sessionIdTargetIdMap.put(sessionId, targetId);
			}
			
			synchronized (sessionIdSocketNameMap) {
				sessionIdSocketNameMap.put(sessionId, socketName);
			}
			
			return openSessionReturnMap;
		} 
		
		// For Future Implementation
		if( openSessionReturnMap.get(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT).equals(Constants.OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_FAILED) ) {
			return openSessionReturnMap;
		} 
	
		return null;
	}
	
	/**
	 * Returns a List containing value, state and attributes of the specified socket elements.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths List&lt;String&gt; 
	 * @return Map&lt;String, String&gt;
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	// Changed return Type. Parikshit Thakur
	
	// 2012-09-24 : Method signature changed, added extra attributes depths, pruneIndices, pruneXMLContent according to new spec.
	List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets,
			List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent) {
		
		if ( (sessionId == null) || (paths == null) || (includeSets == null) || (paths.size() != includeSets.size()) 
				|| paths.size()!=depths.size() || paths.size()!=pruneIndices.size() || paths.size()!=pruneXMLContent.size()) {
			
			logger.info("Input arguments are invalid.");
			return null;
		}
		
		String targetId = null;
		
		synchronized(sessionIdTargetIdMap) {
			
			targetId = sessionIdTargetIdMap.get(sessionId);
		}
		
		if ( targetId == null) {
			logger.warning(" TargetId not found for sessionId '"+sessionId+"'.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null || targetDetail.getITA() == null ) {
			logger.warning("TA not found for targetId '"+targetId+"'.");
			return null;
		}
		
		try {
			
			return targetDetail.getITA().getValues(sessionId, paths, includeSets, depths, pruneIndices, pruneXMLContent);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : getting value for '"+targetId +"' failed.");
			return null;
		}
	}
	
		
	
	/**
	 * Change the value/state of specified target.
	 * The requested operations are coded as cross-indexed arrays of paths, operations, identifiers and requested new values.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths an Object of List&lt;String&gt; 
	 * @param operations an Object of List&lt;String&gt; 
	 * @param reqValues an Object of List&lt;String&gt; 
	 * @return Map&lt;String, an Object of List&lt;String&gt;&gt; 
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	Map<String, List<String>> setValuesRequest(String sessionId,
			List<String> paths, List<String> operations, List<String> reqValues) {	
		
		if ( sessionId == null || paths == null ||
			 operations == null || reqValues == null || paths.size()==0 ||
			!(paths.size()==operations.size() &&  paths.size()==reqValues.size()) ) {
					
			logger.info("Input arguments are invalid.");
			return null;
		}
		
		String targetId = null;
		
		synchronized(sessionIdTargetIdMap) {
			
			targetId = sessionIdTargetIdMap.get(sessionId);
		}
		
		if ( targetId == null) {
			logger.warning(" TargetId not found for sessionId '"+sessionId+"'.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null || targetDetail.getITA() == null ) {
			logger.warning("TA not found for targetId '"+targetId+"'.");
			return null;
		}	
		
		try {
			
			return targetDetail.getITA().setValuesRequest(sessionId, true, paths, operations, reqValues);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : setting value for '"+targetId+"' failed.");
			return null;
		}
		
	}
	
	/**
	 * Get IndexNames available on specific level of dimension of dimensional Element.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param eltIds a List&lt;String&gt; of elementIds
	 * @param indexNos a List&lt;Integer&gt; of indexNo
	 * @return a List&lt;Set&lt;Integer&gt; of indices 
	 */
	List<Set<String>> getIndices(String sessionId, List<String> eltPaths) {
		
		if ( (sessionId == null) || (eltPaths == null) ) {
			
			logger.info("Input arguments are invalid.");
			return null;
		}
		
		String targetId = null;
		
		synchronized (sessionIdTargetIdMap) {
			targetId = sessionIdTargetIdMap.get(sessionId);
		}
		
		if ( targetId == null )
			return null;
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null )
			return null;
		 
		return targetDetail.getITA().getIndices(sessionId, eltPaths);
	}
	
	/**
	 * Closes the session with specified sessionId.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	void closeSession(String sessionId){
		
		if ( sessionId == null ) {
			
			logger.warning("Session Id is null.");
			return;
		}
		
		String targetId = null;
		
		synchronized (sessionIdTargetIdMap) {
			targetId = sessionIdTargetIdMap.get(sessionId);
		}
		
		if ( targetId == null) {
			logger.warning(" TargetId not found for sessionId '"+sessionId+"'.");
			return;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null || targetDetail.getITA() == null ) {
			logger.warning("TA does not found for targetId '"+targetId+"'.");
			return;
		}
		
		try {
			
			targetDetail.getITA().sessionClosed(sessionId);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : closing session '"+sessionId+"'.");
		}
		
		synchronized (sessionIdTargetIdMap) {
			sessionIdTargetIdMap.remove(sessionId);
		}
		
		synchronized (sessionIdSocketNameMap) {
			sessionIdSocketNameMap.remove(sessionId);
		}
		
		synchronized (removedSessionIdList) {
			removedSessionIdList.add(sessionId);
		}
			
	}
	
	/**
	 * Get List of available targetIds
	 * 
	 * @return a List&lt;String&gt; of targetIds
	 */
	List<String> getAvailableTargetIds() {
		
		List<String> targetIds = new ArrayList<String>();
		
		synchronized(targetDetailList) {
			
			for( TargetDetail targetDetail : targetDetailList ) 
				targetIds.add( targetDetail.getTargetId() );
			
		}
		
		return targetIds;
	}
	
	/**
	 * Get the URI of the Socket Description for the specified target Name.
	 * 
	 * @param targetName a String value of targetName
	 * @param socketName a String value of socketName
	 * @return String of Socket Description URI
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	String getSocketDescriptionUri(String targetName, String socketName) {		
		
		if ( (targetName == null) || (socketName == null) ) {
			
			logger.info("Input arguments are invalid.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetName(targetName);
		
		if ( targetDetail == null )
			return null;
		
		try {
			
			return targetDetail.getITA().getSocketDescriptionUri(targetName, socketName);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : getting socket description URI for target '"+targetName+"' and socket '"+socketName+"'.");
			return null;
		}
	}
	
	/**
	 * Get the Socket Friendly Name for the specified target Name.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @return String of Socket Friendly Name
	 */
	String getSocketFriendlyName(String targetId, String socketName) {
		
		if ( (targetId == null) || (socketName == null) ) {
			logger.info("Input arguments are invalid.");
			return null;
		}

		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null )
			return null;
		
		return targetDetail.getITA().getSocketFriendlyName(targetId, socketName);
	}
	
	/**
	 * Get String value of Socket Name specified by 'sessionId'.
	 * 
	 * @param sessionId a String value of sessionId
	 */
	String getSocketName(String sessionId) {	
		
		if ( sessionId == null ) {
			logger.warning("SessionId is null.");
			return null;
		}
		
		synchronized (sessionIdSocketNameMap) {
			
			return sessionIdSocketNameMap.get(sessionId);
		}
	}
	
	/**
	 * Get socket Names for the specified Target Name.
	 * 
	 * @param targetName a String value of targetName
	 * @return List&lt;String&gt; of socket Names
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	List<String> getSocketNames(String targetName) {
		
		if ( targetName == null ) {
			logger.warning("TargetName is null.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetName(targetName);
		
		if ( targetDetail == null )
			return null;
		
		try {
			
			return targetDetail.getITA().getSocketNames(targetName);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : getting socket names for target '"+targetName+"'.");
			return null;
		}
	}
	
	/**
	 * Get the URI of the Target Description for the specified Target Name.
	 * 
	 * @param targetName a String value of targetName
	 * @return String of TargetDescriptionUri
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	String getTargetDescriptionUri(String targetName) {
		
		if ( targetName == null ) {
			logger.warning("TargetName is null.");
			return null;
		}

		TargetDetail targetDetail = getTargetDetailByTargetName(targetName);
		
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetName '"+targetName+"'.");
			return null;
		}
		
		try {
			
			return targetDetail.getITA().getTargetDescriptionUri(targetName);
			
		} catch(TAFatalException e) {
			logger.warning("TAFatalException : getting target description URI for target '"+targetName+"'.");
			return null;
		}
		
	}
	
	/**
	 * Get the Target Name for specified targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @return String representing targetName
	 */
	public String getTargetName(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetId '"+targetId+"'.");
			return null;
		}
		
		return targetDetail.getTargetName();
	}
	
	/**
	 * Get Friendly Name of Target
	 * 
	 * @param targetId a String variable
	 * @return a String value of FriendlyName
	 */
	String getTargetFriendlyName( String targetId ) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetId '"+targetId+"'.");
			return null;
		}
		
		Object friendlyName = targetDetail.getTargetProps().get(Constants.PROPERTY_RES_TARGET_FRIENDLY_NAME);
		
		if ( friendlyName == null )
			return null;
		else 
			return friendlyName.toString();
		
	}
	
	/**
	 * Get Target Properties specified by 'targetId'
	 * 
	 * @param targetId a String value of targetId
	 * @return a Map&lt;String, Object&gt; of Target Properties
	 */
	Map<String, Object> getTargetProps(String targetId) {	
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return null;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
			
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetId '"+targetId+"'.");
			return null;
		}
		
		return targetDetail.getTargetProps();		

		/*		
  		try {
			return targetDetail.getITA().getTargetProps(targetId);			
		} catch (TAFatalException e) {		
			logger.warning("TAFatalException : getting target properties for target '"+targetId+"'.");
			return null;
		}
		*/
		
	}
	
	/**
	 * Return whether the IUISocketElement specified by 'elementId' is available in the target specified by 'targetId'
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param elementId a String value of elementId
	 * @return whether element specified by elementId is available or not
	 * 
	 * @throws TAFatalException an Object of TAFatalException
	 */
	boolean isElementAvailable(String targetId, String socketName, String elementId) {
		
		if ( (targetId == null) || (socketName == null) || (elementId == null) ) {
			logger.info("Input arguments are invalid.");
			return false;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetId '"+targetId+"'.");
			return false;
		}
		
		try {
			return targetDetail.getITA().isElementAvailable(targetId, socketName, elementId);
		} catch (TAFatalException e) {		
			logger.warning("TAFatalException : getting target properties for target '"+targetId+"'.");
			return false;
		}
	}
	
	/**
	 * Invoke the specified locator function on the specified target.
	 * 
	 * @param targetId a String value of TargetId
	 * @param locatorId a String value of LocatorId
	 */
	void invokeLocator(String targetId, String locatorId) {
		
		if ( (targetId == null) || (locatorId == null) ) {
			logger.info("Input arguments are invalid.");
			return;
		}
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
			
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetId '"+targetId+"'.");
			return;
		}
		
		try {
			
			targetDetail.getITA().invokeLocator(targetId, locatorId);
			
		} catch (TAException e) {
			//e.printStackTrace();
			logger.warning("TAException : Invoking locator for target '"+targetId+"' and locator '"+locatorId+"'.");
		} catch (TAFatalException e) {
			//e.printStackTrace();
			logger.warning("TAFatalException : Invoking locator for target '"+targetId+"' and locator '"+locatorId+"'.");
		}
		
	}
	
	/**
	 * Get a map with Locator ids and locator types for a specific target type.
	 * 
	 * @param targetName a String value of targetName
	 * @return a Map&lt;string, String&gt; of  LocatorID V/S Locator Type
	 */
	Map<String, String> getLocators(String targetName) {
		
		if ( targetName == null ) {
			logger.warning("Target Name is null.");
			return null;
		}
		TargetDetail targetDetail = getTargetDetailByTargetName(targetName);
			
		if ( targetDetail == null ) {
			logger.warning("Unable to get Target Detail for TargetName '"+targetName+"'.");
			return null;
		}
		
		return targetDetail.getITA().getLocators(targetName);
	}
	
	
	/**
	 * Get the Dynamic atomic Resources from the TA. 
	 * The UCH shall specify the properties of the requested resource as much as possible. 
	 * 
	 * @param sessionId a String value of targetId
	 * @param resProps a Map&lt;String, String&gt; value of resProps
	 * @return a List&lt;Object&gt; representing Matched Resources
	 */
	List<Map<String, Object>> getDynRes(String sessionId, List<Map<String, String>> resProps ) {
		
		if ( sessionId == null )
			return null;
		
		String targetId = sessionIdTargetIdMap.get(sessionId);
		
		if ( targetId == null )
			return null;
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("TA does not found for targetId '"+targetId+"'.");
			return null;
		}
		
		try {
			
			List<Map<String, Object>> dynResources = targetDetail.getITA().getDynRes(targetId, resProps);
			
			if ( dynResources == null ) {
				return null;
			}
			
			for ( Map<String, Object> dynResource : dynResources ) {
				
				Object contentAt = dynResource.get("contentAt");
				
				if ( contentAt == null )
					continue;
				
				contentAt = uch.convertURI( contentAt.toString() );
				
				dynResource.put("contentAt", contentAt);
			}
			
			return dynResources;
			
		} catch (TAFatalException e) {
			logger.warning("TAFatalException");
			//e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Find relative TA for specified targetId and add contexts to it.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt; specifies contexts
	 */
	void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) {
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("TA does not found for targetId '"+targetId+"'.");
			return;
		}
		
		try {
			targetDetail.getITA().targetContextsAdded(targetId, contexts);
		} catch (TAFatalException e) {
			logger.warning("TAFatalException : Trying to add target contexts for targetId '"+targetId+"'.");
		}

	}
	
	/**
	 * Find relative TA for specified targetId and remove contexts from it.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt; specifies contexts
	 */
	void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) {
		
		TargetDetail targetDetail = getTargetDetailByTargetId(targetId);
		
		if ( targetDetail == null ) {
			logger.warning("TA does not found for targetId '"+targetId+"'.");
			return;
		}
		
		try {
			targetDetail.getITA().targetContextsRemoved(targetId, contexts);
		} catch (TAFatalException e) {
			logger.warning("TAFatalException : Trying to remove target contexts for targetId '"+targetId+"'.");
		}
		
	}
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invoked from UCH Ends ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	/**
	 * Instantiates TA Object of the Class that is represented from the argument 'targetProps' and returns it.
	 * If already exists then return the existed object.
	 * 
	 * @param targetProps Map&lt;String, Object&gt;
	 * @param targetId a String value of TargetId
	 * @return a String value of TargetId 
	 */
	private String instantiateTA(String targetId, Map<String, Object> targetProps, Map<String, String> taProps, List<Map<String, IProfile>> contexts) {
		
		
		if ( (targetId == null) || (targetProps == null) || (taProps == null) ) {
			logger.severe("TargetId,Target Properties or TA Properties is null.");
			return null;
		}
		
		if ( isTargetIdExists(targetId) ) {
			logger.severe("TargetId '" + targetId + "' already exists.");
			return null;
		}
		
		// INstantiate TA from Resource Server using the ta properties
		ITA ta = getTA( taProps);
		
		if ( ta == null )
			return null;	
		
		try {
			
			ta.registerTarget(targetId, targetProps, contexts);
			String targetName = ta.getTargetName(targetId);
			
			String tdUri = ta.getTargetDescriptionUri(targetName);		
			uch.addResourceDir(tdUri);

			TargetDetail targetDetail = new TargetDetail();
			
			targetDetail.setTargetId( targetId );
			targetDetail.setITA( ta );
			targetDetail.setTargetProps( targetProps );
			targetDetail.setTargetName( targetName );
			
			synchronized (targetDetailList) {
				targetDetailList.add( targetDetail );
			}
			
			uch.instantiateUIPMs(targetId, targetName, contexts);
			uch.targetDiscovered(targetId, contexts);
			logger.info("No of Targets = "+targetDetailList.size() );
			
			return targetId;		
			
		} catch (TAException e) {
			logger.warning("TAException");
			e.printStackTrace();
		} catch (TAFatalException e) {
			logger.warning("TAFatalException");
		}
		
		return null;
		
	}
	
	/**
	 * Get an apropriate TA from the Resource Server using a givn set of TA Properties.
	 * 
	 * @param taProps an Object of Map&lt;String, String&gt; 
	 method searches for a TA that has the same TA properties 
	 
	 * @return an Object of ITA
	 * An ITA that mathces the given parameters of tht parameter taProps;
	 */
	@SuppressWarnings("unchecked")
	private ITA getTA(Map<String, String> taProps) {
		
		Map<String, List<String>> newTaProps = new HashMap<String, List<String>>();
		
		for ( String key : taProps.keySet() ) {
			
			String value = taProps.get(key);
			
			if ( (key != null) && (value != null) ) {
				newTaProps.put(key, CommonUtilities.convertToList(value) );
			}
		}
		
		newTaProps.put(Constants.PROPERTY_RUNTIME_PLATFORM, CommonUtilities.convertToList(Constants.PROPERTY_PLATFORM_VALUE_JAVA) );
		
		String conformsTo = uch.getConformsTo();
		
		if ( conformsTo != null )	
			newTaProps.put(Constants.PROPERTY_CONFORMS_TO, CommonUtilities.convertToList(conformsTo) );
		
		newTaProps.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		newTaProps.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		
		List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
		resProps.add(newTaProps);
		
		List<List<Map<String, List<String>>>> resRetProps = uch.getResources(null, resProps);
		
		if ( (resRetProps == null) || (resRetProps.size() == 0) ) {
			logger.warning("Unable to get TA List from resource server .");
			return null;
		}
			
		List<Map<String, List<String>>> resRetSubProps = resRetProps.get(0);
		
		if ( (resRetSubProps == null) || (resRetSubProps.size() == 0) ) {
			logger.warning("Unable to get TA Sub List from resource server.");
			return null;
		}
		
		Map<String, List<String>> taResProp = resRetSubProps.get(0);
		
		if( taResProp == null ) {
			logger.warning("Unable to get TA from resource server.");
			return null;
		} else {
			logger.info("TA Downloaded successfully.");
		}
		
		String taDLL = CommonUtilities.getFirstItem( taResProp.get(Constants.PROPERTY_RES_DYNAMIC_LIB_CLASS) );
		
		if ( taDLL == null ) {
			logger.info("Unable to get DLL Name for '"+CommonUtilities.getFirstItem( taResProp.get(Constants.PROPERTY_RES_NAME) )+"'.");
			return null;
		}
		
		synchronized(targetDetailList) {
			
			for (TargetDetail targetDetail : targetDetailList) {
				
				ITA ta = targetDetail.getITA();
				
				if ( taDLL.equals( ta.getClass().getName() ) ) 
					return ta;			
			}
		}
		
		String jarFilePathURI = ResourceUtil.getJarFileUri(taResProp);
		
		//Parikshit Thakur : 20110829. Changes to get ClassLoader from uch instead of uchStore.
		/*Object object = getLocalUCHStore().getValue(Constants.CLASS_PATH_VALUE_CLASS_LOADER);
		
		if ( !(object instanceof ClassLoader) ) {
			logger.warning("Unable to get object of '"+Constants.CLASS_PATH_VALUE_CLASS_LOADER+"' from Local UCH Store.");
			return null;
		}
		
		ClassLoader classLoader = (ClassLoader)object;*/
		
		ClassLoader classLoader = uch.getClassLoader();
		if(classLoader == null ){
			return null;
		}
		//Parikshit Thakur : 20110829. Changes end
		
		// Parikshit Thakur : 20110823. Changes to load multiple jars for a single resource.
		if(jarFilePathURI.endsWith(".jar")){
			if ( !classLoader.addJar( jarFilePathURI ) ){ 
logger.warning("Adding of file "  + jarFilePathURI + " to classloader failed" );
				return null;
			}
		}
		else{
			try {

				File resDir = new File(new URI(jarFilePathURI));
				
				loadJars(resDir, jarFilePathURI, classLoader);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
			 	
		try {
			
			Class taClass = classLoader.loadClass(taDLL);
			
			

			ITA ta = (ITA)taClass.newInstance();

			ta.init( this, taProps, uch.getUCHProps() );
			
			return ta;
			
		} catch (InstantiationException e) {
			logger.warning("InstantiationException : Loading class file '"+taDLL+"'.");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Loading class file '"+taDLL+"'.");
		} catch (TAFatalException e) {
			logger.warning("TAFatalException : Loading class file '"+taDLL+"'.");
		} catch (Exception e) {
			logger.warning("Exception : Loading class file '"+taDLL+"'.");
			e.printStackTrace();
		}
		
		logger.severe("TA is not found for TA Class Name '"+ taDLL + "'.");
	
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
	
	
	

	/**
	 * Checks whether targetId exists or not.
	 * 
	 * @param targetId a String value
	 * @return whether targetId is exists or not.
	 */
	private boolean isTargetIdExists(String targetId) {
		
		synchronized (targetDetailList) {
			
			for( TargetDetail targetDetail : targetDetailList ) {
				
				if ( targetDetail.getTargetId().equals(targetId) ) 
					return true;
			}
		}
		
		
		return false;
	}
	
	/**
	 * Return an object of TargetDetail by targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @return an Object of TargetDetail
	 */
	private TargetDetail getTargetDetailByTargetId(String targetId) {
		
		if ( targetId == null )
			return null;
		
		synchronized(targetDetailList) {
			
			for( TargetDetail targetDetail : targetDetailList ) {
				
				if ( targetDetail.getTargetId().equals(targetId) ) 
					return targetDetail;
				
			}
		}
		
		return null;
	}
	
	/**
	 * Return an object of TargetDetail by targetName.
	 * 
	 * @param targetId a String value of targetName
	 * @return an Object of TargetDetail
	 */
	private TargetDetail getTargetDetailByTargetName(String targetName) {
		
		if ( targetName == null )
			return null;
		
		synchronized(targetDetailList) {
			
			for( TargetDetail targetDetail : targetDetailList ) {
				
				if ( targetDetail.getTargetName().equals(targetName) ) 
					return targetDetail;
				
			}
		}
		
		return null;
	}
	
	/**
	 * Generates Random SessionId.
	 * 
	 * @return a String value of Session Id
	 */
	private String generateRandomSessionId() {
		
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int len = chars.length();
		
		int numberLength = 6;
		
		int loop = 0;
		while( true ) {
			
			loop++;
			String number = "";
			for(int i=0 ; i<numberLength ; i++) {
				
				int charPos = (int)(Math.random()*len);
				number += chars.charAt(charPos);		
				
			}
				
			if( !isSessionIdExists( Constants.CONSTANT_SESSION_ID_PREFIX_SESSION + "-" + number ) ) {
				
				return ( Constants.CONSTANT_SESSION_ID_PREFIX_SESSION + "-" + number );
				
			} else if(loop > Math.pow(20, numberLength) ){
				numberLength++;
			}
				
		}
		
	}
	
	/**
	 * Checks whether sessionId exists or not.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return whether the sessionId exists or not
	 */
	private boolean isSessionIdExists(String sessionId) {
		
		synchronized (sessionIdTargetIdMap) {
			
			for ( String sId : sessionIdTargetIdMap.keySet() ) {
				
				if ( sId.equals(sessionId) )
					return true;
				
			}
		}
		
		synchronized (removedSessionIdList) {
			
			for ( String sId : removedSessionIdList ) {
				
				if( sId.equals(sessionId) )
					return true;
			}
		}	
		
		return false;
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
	public void removeTA(ITA ta) throws UCHNotImplementedException {
		throw new UCHNotImplementedException();
		
	}

}
