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

package org.openurc.uch;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TA (Target Adapter) is the link in between UCH and final proprietory Target. It converts URC protocol to Target proprietory protocol and vice-versa.
 * A Target Adapter (TA) represents one or multiple targets to the UCH.
 * It is installed and loaded upon the discovery of a first target that it can represent.  
 * The UCH will register new targets with the TA as they are discovered by the TDM; and unregister after they have disappeared from the network.  
 * After having opened a session on a target’s socket, the TA is responsible for managing the values of the pertaining socket instance.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public interface ITA {
	
	/**
	 * When the TA is installed and loaded, this method is called by UCH prior to any other TA function. 
	 * Within this method the TA initializes itself.
	 * 
	 * @param taListener an Object of ITAListener
	 * @param taProps a Map&lt;String, String&gt; of ITA Properties
	 * @param uchProps a Map&lt;String, String&gt; of UCH Properties 
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void init(ITAListener taListener, Map<String, String> taProps, Map<String, String> uchProps) throws TAFatalException;
	
	
	/**
	 * This method is called by UCH before Un-installing or Unloading the TA.  
	 * 
	 */
	public void finalize();
	
	
	/**
	 * Get Target Adapter Properties Map.
	 * 
	 * @return an object of Map&lt;String, String&gt; of Target Adapter Properties
	 */
	public Map<String, String> getTAProps();
	
	
	/**
	 * Register Target Adapter with specified targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @param targetProps Map&lt;String, Object&gt;
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TAException an Object of {@link org.openurc.uch.TAException}
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void registerTarget(String targetId, Map<String, Object> targetProps, List<Map<String, IProfile>> contexts) throws TAException, TAFatalException;
	
	
	/**
	 * Called when one or more contexts got added to an existing target.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) throws TAFatalException;
	
	
	/**
	 * Called when one or more contexts got removed to an existing target.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) throws TAFatalException;
	
	
	/**
	 * Called when target has been disappeared from the network.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void unregisterTarget(String targetId)throws TAFatalException;
	
	
	/**
	 * Get the target identifiers that the TA is currently representing.
	 * 
	 * @return List&lt;String&gt;
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public List<String> getRegisteredTargetIds()throws TAFatalException;
	
	
	/**
	 * Get a map of locators containing locatorId and locatorType for a specific target.
	 * 
	 * @param targetName a String value of targetName
	 * @return a Map&lt;string, String&gt; of  LocatorID V/S Locator Type
	 */
	public Map<String, String> getLocators(String targetName);
	
	
	/**
	 * Invoke the specified locator function on a target.
	 * This requires a prior call to registerTarget on this target.  
	 * 
	 * @param targetId a String value of targetId
	 * @param locatorId a String value of locaterId
	 * 
	 * @throws TAException an Object of {@link org.openurc.uch.TAException}
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void invokeLocator(String targetId, String locatorId) throws TAException, TAFatalException;
	
	
	/**
	 * Get the targetName for specified targetId.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a String value of targetName
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public String getTargetName(String targetId)throws TAFatalException;
	
	
	/**
	 * Get the URI for retrieving the Target Description. 
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return a String value of Target Description URI
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public String getTargetDescriptionUri(String targetName)throws TAFatalException;
	
	
	/**
	 * Get the Target's Properties for specified targetId.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a Map&lt;String, Object&gt; value of Target Properties
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public Map<String, Object> getTargetProps(String targetId)throws TAFatalException;
	
	
	/**
	 * Get the List of SocketName for specified targetName.
	 * 
	 * @param targetName a String value of targetName
	 * 
	 * @return List&lt;String&gt; of SocketName
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public List<String> getSocketNames(String targetName)throws TAFatalException;
	
	
	/**
	 * Get the SocketName for the specified sessionId.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return a String of SocketName
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public String getSocketName(String sessionId)throws TAFatalException;
	
	
	/**
	 * Get the Socket Friendly Name for the specified socketName and targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * 
	 * @return a String value of Socket Friendly Name
	 */
	public String getSocketFriendlyName(String targetId, String socketName);
	
	
	/**
	 * Get the Session Identifiers for specified targetId and socketName.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * 
	 * @return List&lt;String&gt; of sessionId
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public List<String> getSessionIds(String targetId, String socketName)throws TAFatalException;
	
	/**
	 * 
	 * Get the URI for retrieving the Socket Description for specified targetName and socketName.
	 * 
	 * @param targetName a String value of targetName
	 * @param socketName a String value of socketName
	 * 
	 * @return a String representing Socket Description URI
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public String getSocketDescriptionUri(String targetName, String socketName)throws TAFatalException;
	
	
	/**
	 * The UCH asks for permission to open a session with a specified client on a specified socket. 
	 * If it is permitted then a map returns a keyValue pair {"Result", "A"} i.e. Accept 
	 * Else it returns a keyValue pair {"Result", "R"} i.e. rejection. 
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param clientProps Map&lt;String, String&gt;
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @return Map&lt;String, String&gt;
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public Map<String, String> openSessionRequest(String targetId, String socketName, Map<String, String> clientProps, Map<String, IProfile> context)throws TAFatalException;
	
	
	/**
	 * If the TA has accepted an open session request, and if the UCH succeeded in the necessary steps 
	 * to open a session with the client, the UCH will call sessionOpened and provide the sessionId 
	 * pertaining to the new session.
	 * It opens a new Session for specified socketName with the targetId mentioned. 
	 * 
	 * @param targetId a String value of targetId
	 * @param sessionId a String value of sessionId
	 * @param socketName a String value of socketName
	 * @param clientProps Map&lt;String, String&gt;
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void sessionOpened(String targetId, String sessionId, String socketName, Map<String, String> clientProps, Map<String, IProfile> context)throws TAFatalException;

	
	/**
	 * Closes the Session for specified sessionId and do the necessary clean-up for the target.  
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public void sessionClosed(String sessionId)throws TAFatalException;
	
	
	/**
	 * The client requests to change the target state in one or more operations on specified socket 
	 * elements. The requested operations are coded as cross-indexed arrays of paths,
	 * operations, identifiers and requested new values.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths List&lt;String&gt; a List of elementPaths
	 * @param operations List&lt;String&gt; a List of operations (allowed operations are "S", "A", "R", "I" or "K" )
	 * @param reqValues List&lt;String&gt; a List of Requested Values
	 * 
	 * @return Map&lt;String, List&lt;String&gt;&gt; of set Values
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public Map<String, List<String>> setValuesRequest(String sessionId, boolean isValidated, List<String> paths, List<String> operations, List<String> reqValues)throws TAFatalException;
	
	
	/**
	 * Returns a List containing value, state and attributes of the specified socket elements.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths a List&lt;String&gt; of elementPath
	 * @param includeSets a List&lt;Boolean&gt; of includeSets
	 * @param depths a List&lt;Integer&gt; of depths
	 * @param pruneIndices a List&lt;Boolean&gt; of pruneIndices
	 * @param pruneXMLContent a List&lt;Boolean&gt; of pruneXMLContent
	 * 
	 * @return a List&lt;Map&lt;String, String&gt;&gt; of values
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets, List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent)throws TAFatalException;
	
	
	
	/**
	 * Get Indices available on specific level of dimension of dimensional Element.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param eltPaths a List&lt;String&gt; of elementPaths
	 * 
	 * @return a List&lt;Set&lt;String&gt;&gt; of indices
	 */
	public List<Set<String>> getIndices(String sessionId, List<String> eltPaths);
	
	
	/**
	 * Get the Dynamic Resources from the TA. 
	 * The UCH shall specify the properties of the requested resource as many as possible.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param resProps  a List&lt;Map&lt;String, String&gt;&gt; value of Resource Properties
	 * @return a List&lt;Object&gt; of resources
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public List<Map<String, Object>> getDynRes( String sessionId, List<Map<String, String>> resProps )throws TAFatalException;
	
	
	/**
	 * Checks whether a given socketElement is available in specified Socket of specified Target or not.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param eltId a String value of eltId
	 * @return boolean whether specified socketElement is exists or not
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 */
	public boolean isElementAvailable(String targetId, String socketName, String eltId) throws TAFatalException;
	
	
	/**
	 * Checks whether specified function is implemented by the TA or not.
	 * 
	 * @param functionName a String value of functionName
	 * @return boolean whether specified function is implemented by the TA or not.
	 */
	public boolean isImplemented(String functionName);

	
	
	//------------------------------Optional Methods-------------------------------
	/**
	 * The UCH asks for permission to suspend a session. If TA does not support this function then throw TANotImplementedException.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return whether permission for session suspend is granted or not
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 * @throws TANotImplementedException an Object of {@link org.openurc.uch.TANotImplementedException}
	 */
	public boolean suspendSessionRequest(String sessionId) throws TAFatalException, TANotImplementedException;
	
	
	/**
	 * The UCH signals the TA that the client has suspended a session with one of the TA’s targets.  
	 * The TA do the necessary actions internally and with the target.  
	 * The session is now suspended until a subsequent call to resumeSessionRequest(), 
	 * or until the TA terminates the session for timing out or other reasons, in which case the TA shall call sessionAborted().
	 * 
	 * @param sessionId a String value of sessionId
	 * @param suggestedTimeout a long value of suggestedTimeout
	 * 
	 * @return whether permission is granted or not
	 * 
	 * @throws TAException an Object of {@link org.openurc.uch.TAException}
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 * @throws TANotImplementedException an Object of {@link org.openurc.uch.TANotImplementedException}
	 */
	public long sessionSuspended(String sessionId, long suggestedTimeout) throws TAException, TAFatalException, TANotImplementedException;
	
	
	/**
	 * The UCH asks for permission to resume a session.
	 * If TA does not support this function then throw TANotImplementedException.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return a a Long value of timeOut
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 * @throws TANotImplementedException an Object of {@link org.openurc.uch.TANotImplementedException}
	 */
	public boolean resumeSessionRequest(String sessionId) throws TAFatalException, TANotImplementedException;
	
	
	/**
	 * The UCH signals the TA that the session has resumed.  
	 * A call to this function shall only occur after a resume session request has been granted by the TA.
	 * 
	 * @param sessionId a string value of sessionId
	 * 
	 * @throws TAException an Object of {@link org.openurc.uch.TAException}
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
	 * @throws TANotImplementedException an Object of {@link org.openurc.uch.TANotImplementedException}
	 */
	public void sessionResumed(String sessionId) throws TAException, TAFatalException, TANotImplementedException;
	
	
	/**
	 * The UCH calls this function if it has received a request from a target to a URI 
	 * that the TA has claimed for itself by a previous call to startUriService().It modify the 
	 * response object according to request object.
	 *
	 * @param protocol a String value of protocol
	 * @param request an Object representing request
	 * @param response an Object representing response
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @throws TAFatalException an Object of {@link org.openurc.uch.TAFatalException}
     * @throws TANotImplementedException an Object of {@link org.openurc.uch.TANotImplementedException}
	 * 
	 */
	public void targetRequest( String protocol, Object request, Object response, Map<String, IProfile> context)throws TAFatalException, TANotImplementedException;
	
}