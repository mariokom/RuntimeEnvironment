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
 * IUIPMListener forwards call of the methods from UIPM to TA.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public interface IUIPMListener {

    /**
	 * A controller may request to open a user context to the UCH through the UIPM
	 * 
     * @param authorization a String value of user credentials as specified for the Basic Authentication Scheme [RFC 2617]
     *
	 * @return a boolean value. true=user authorized, false=user unauthorized
	 */
    boolean openUserContext(String authorization);
    
    /**
	 * A controller may request to close a user context to the UCH through the UIPM
	 * 
     * @param authorization a String value of user credentials as specified for the Basic Authentication Scheme [RFC 2617]
     *
	 * @return a boolean value. true=context closed, false=request failed
	 */
    boolean closeUserContext(String authorization);
	
    /**
	 * Get currently open contexts.
	 *  
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public List<Map<String, IProfile>> getContexts();
	
	
	/**
	 * Get the identifiers of the available targets.
	 * 
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @return List&lt;String&gt; 
	 */
	public List<String> getAvailableTargetIds(Map<String, IProfile> context);
	
	
	/**
	 * Get the name of a target for the specified targetId. 
	 * 
	 * @param targetId a String value of targetId
	 * @return a String
	 */
	public String getTargetName(String targetId);
	
	/**
	 * Get the target Properties for the specified targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @return Map&lt;String, Object&gt;
	 */
	public Map<String, Object> getTargetProps(String targetId);
	
	
	/**
	 * Get list of contexts for which a target is available.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public List<Map<String, IProfile>> getTargetContexts(String targetId);
	
	
	/**
	 * Get the URI for retrieving the Target Description for the specified targetName.
	 * 
	 * @param targetName a String value of targetName
	 * @return a Target Description URI String 
	 */
	public String getTargetDescriptionUri(String targetName);
	
	
	/**
	 * Get specified document through HTTP GET or POST.
	 * 
	 * @param uri a String value of URI
	 * @param postData a String value of postData
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @return a String value of requested Document
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public String getDocument(String uri, String postData, Map<String, IProfile> context) throws UCHException;
	
	
	/**
	 * Returns List of socket Names for the specified Target Name.
	 * 
	 * @param targetName a String value of targetName
	 * @return List&lt;String&gt;
	 */
	public List<String> getSocketNames(String targetName);
	
	
	/**
	 * Get the URI for retrieving the Socket Description of the specified targetName and socketName.
	 * 
	 * @param targetName a String value of targetName
	 * @param socketName a String value of socketName
	 * 
	 * @return a String representing Socket Description URI
	 */
	public String getSocketDescriptionUri(String targetName, String socketName);
	
	
	/**
	 * Get the SocketName for the specified sessionId.
	 * 
	 * @param sessionId a String value of sessionId
	 * @return a String of SocketName
	 */
	public String getSocketName(String sessionId);
	
	
	/**
	 * Get Friendly Name of the Socket for the specified socketName and targetId.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @return a String value of Socket Friendly Name
	 */
	public String getSocketFriendlyName(String targetId, String socketName);
	
	
	/**
	 * Get a map of locatores containing locatorId and locatorType for a specified target Name.
	 * 
	 * @param targetName a String value of targetName
	 * @return a Map&lt;string, String&gt; of  LocatorID V/S Locator Type
	 */
	public Map<String, String> getLocators(String targetName);
	
	
	/**
	 * Invoke the specified locator function of the specified targetId.
	 * 
	 * @param targetId a String value of TargetId
	 * @param locatorId a String value of LocatorId
	 */
	public void invokeLocator(String targetId, String locatorId);
	
	
	/**
	 * Checks whether a given socketElement is available in the specified Socket of specified Target or not.
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param eltId a String value of eltId
	 * 
	 * @return boolean whether specified socketElement is exists or not
	 */
	public boolean isElementAvailable(String targetId, String socketName, String eltId);
	
	
	/**
	 * Get resource from the UCH specified by Resource Properties.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param resProps a Map&lt;String, List&lt;String&gt;&gt; of Resource Properties
	 * @return a List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt; value
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps);
	
	
	/**
	 * Request for opening a session with the specified target and socket.  
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param clientProps Map&lt;String,String&gt;
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @return Map&lt;String,String&gt; 
	 */
	public Map<String, String> openSessionRequest(IUIPM uipm, String targetId, String socketName, Map<String,String> clientProps, Map<String, IProfile> context);
	
	
	/**
	 * Closes a Session for the specified sessionId.
	 * 
	 * @param sessionId a String value of sessionId
	 */
	public void closeSession(String sessionId);
	
	
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
	 */
	public List<Map<String, String>> getValues(String sessionId, List<String> paths, List<Boolean> includeSets, List<Integer> depths, List<Boolean> pruneIndices, List<Boolean> pruneXMLContent);
	
	
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
	 * The UIPM requests to change the target state in one or more operations on specified socket elements.  
	 * The requested operations are coded as cross-indexed arrays of paths, operation identifiers and requested new values.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths List&lt;String&gt; a List of elementPaths
	 * @param operations List&lt;String&gt; a List of operations (allowed operations are "S", "A", "R", "I" or "K" )
	 * @param reqValues List&lt;String&gt; a List of Requested Values
	 * 
	 * @return Map&lt;String, List&lt;String&gt;&gt; a  Map of set Values
	 */
	public Map<String, List<String>> setValuesRequest(String sessionId, List<String> paths, List<String> operations, List<String> reqValues);
	
	
	/**
	 * Get the IP address of the UCH. 
	 * 
	 * @return return a string value
	 */
	public String getIpAddress();
	
	
	/**
	 * The UIPM requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, the UCH will forward all messages that have this URI as its base, to controllerRequest().   
	 * 
	 * @param uipm an IUIPM object
	 * @param scheme a String value of schema
	 * @param port a port no
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public String startUriService(IUIPM uipm, String scheme, int port, boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Called when the UIPM wants to add one or more contexts to a URI service.
	 *  
	 * @param uipm an object of IUIPM 
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void addUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Called when the UIPM wants to close one or more contexts to a URI service.
	 *  
	 * @param uipm an object of IUIPM 
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void removeUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * The UIPM requests the UCH to stop servicing a URI that was requested to be serviced in a previous call to startUriService().  
	 * 
	 * @param uri a String value of URI
	 * @param uipm a UIPM object (caller) that is requesting to stop service for a URI with the UCH.
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void stopUriService(IUIPM uipm, String uri) throws UCHException;
	
	
	/**
	 * Tell the UCH to add a user interface item (advertised by the UIPM) to its UIList which is exposed to controllers on request. 
	 * 
	 * @param targetId a String value of targetId
	 * @param socketName a String value of socketName
	 * @param protocolShortName a String value of protocolShortName
	 * @param uris a List&lt;String&gt; value of uris
	 * @param protocolInfo a Map&lt;String, List&lt;String&gt;&gt; of protocol Information
	 * @param icons an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; to store icons information
     * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of Contexts for which the user interface shall be added to the UIList
	 * 
	 * @return whether CompatibleUI is added or not.
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public boolean addCompatibleUI(String targetId, String socketName, String protocolShortName, List<String> uris, Map<String, List<String>> protocolInfo, List<Map<String, List<String>>> icons, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Tells the UCH to remove user interface URIs from the UIList that were previously added by addCompatibleUI().  
	 * 
	 * @param uris a List&lt;String&gt; of URIs
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void removeCompatibleUIs(List<String> uris)throws UCHException;
	
	
	/**
	 * Get the Local UCH Store.
	 * 
	 * @return an Object of IUCHStore
	 */
	public IUCHStore getLocalUCHStore();
	
	
	/**
	 * Get a Map of UCH Properties.
	 * 
	 * @return an object of Map&lt;String, String&gt; or UCH Properties
	 */
	public Map<String, String> getUCHProps();
	
	
	 /**
     * Checks whether specified function is implemented or not.
     * 
     * @param functionName a String value of function Name
     * @return whether the function is implemented or not
     */
	public boolean isImplemented(String functionName);
	
	
	/**
     * Upload Resources on Resource Server.
     * 
     * @param props an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; each with Property-value pairs
     * @param resourceUri an Object of List&lt;String&gt; each specifies a file uri of the resource which is going to be uploaded
     * 
     * @return an Object of Map&lt;String, String&gt; each entry of Map contains key-value pair
     */
    public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> props, List<String> resourceUri);
    
	//------------------------------Optional Methods---------------------------------------//
	
    
	/**
	 * Checks whether value is valid or not.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths an array of String values of paths
	 * @param values an array of String values of values for respective paths
	 * 
	 * @return array of Booleans for each path-value pair whether the value is valid or not.
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public List<Boolean> validateValues(String sessionId, List<String> paths, List<String> values) throws UCHNotImplementedException;
	
	
	/**
	 * Get the actual value of a dependency.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param paths an array of String values of paths
	 * @param dependencies an array of String values of dependencies
	 * 
	 * @return array of Booleans for each path-dependency pair whether the dependency is true or false.
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public List<Boolean> getDependencyValues(String sessionId, List<String> paths, List<String> dependencies) throws UCHNotImplementedException;
	
	
	/**
	 * Get the elementRef URI that points to a particular socket element/set.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param path a String value of path
	 * 
	 * @return a String value of elementRef
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public String getElementRef(String sessionId, String path) throws UCHNotImplementedException;
	
	
	/**
	 * Check whether the given session can be suspended.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return whether the given session can be suspended or not
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public boolean isSessionSuspendable(String sessionId) throws UCHNotImplementedException;
	
	
	/**
	 * The target is requested to suspend the given session with a suggested timeout value.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param suggestedTimeout a long value
	 * 
	 * @return a long value of timeOut
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public long suspendSession(String sessionId, long suggestedTimeout) throws UCHNotImplementedException;
	
	
	/**
	 * Checks whether the given session can be resumed.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return whether the given session can be resumed or not
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public boolean isSessionResumable(String sessionId) throws UCHNotImplementedException;
	
	
	/**
	 * The target is requested to resume the given session.
	 * If IUIPMListener does not support this functionality then UCHNotImplementedException is thrown by this function.
	 * 
	 * @param sessionId a String value of sessionId
	 * 
	 * @return whether request for resume session is granted or not
	 * 
	 * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public boolean resumeSession(String sessionId) throws UCHNotImplementedException;
	
	
	/**
	 * Removes the specified uipm from the UCH.
	 * 
	 * @param uipm an Object of IUIPM
     *
     * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
	 */
	public void removeUIPM(IUIPM uipm) throws UCHNotImplementedException;


	
}	
	
