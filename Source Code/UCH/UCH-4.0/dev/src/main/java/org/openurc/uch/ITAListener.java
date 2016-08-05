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

/**
 * ITAListener receives calls from TA. UCH implements this interface.
 * The Listener receives calls from TA and forwards them to the UIPM and thereby to the client.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public interface ITAListener {
	
	
	/**
	 * Get list of contexts for specified target.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	List<Map<String, IProfile>> getTargetContexts(String targetId);
	
	
	/**
	 * The TA requests the client to open a session with different socket.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param forwardInfo Map&lt;String, String&gt;
	 */
	public void sessionForwardRequest(String sessionId, Map<String, String> forwardInfo);
	
	
	/**
	 * The TA has terminated a session, when the TA will have no knowledge about that session.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param code a String value specifying reason of session abortion
	 */
	public void abortSession(String sessionId, String code);
	
	
	/** 
	 * The TA signals a change in the target's status, affecting one or more sessions.  
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds
	 * @param paths a List&lt;String&gt; of paths
	 * @param operations a List&lt;String&gt; of operations
	 * @param values a List&lt;String&gt; of values
	 * @param props a List&lt;Map&lt;String, String&gt;&gt; of attributes
	 */
	public void updateValues(List<String> sessionIds, List<String> paths, List<String> operations, List<String> values, List<Map<String, String>> props);

	
	/**
	 * Signals that the set of resources pertaining to a specific socket element has been changed.
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds
	 * @param eltIds a List&lt;String&gt; of elementIds
	 * @param resources a List&lt;Map&lt;String, Object&gt;&gt;
	 */
	void updateDynRes(List<String> sessionIds, List<String> eltIds, List<Map<String, Object>> resources);
	
	/**
	 * Gets an Object of LocalUCHStore.
	 * 
	 * @return an Object of IUCHStore
	 */
	public IUCHStore getLocalUCHStore();
	
	
	/**
	 * Check whether to do validation for the TA or not.
	 * 
	 * @param ta an Object of ITA
	 * @param activate a boolean value of activate
	 * @return boolean whether to do validation for the TA or not
	 */
	public boolean setValidation(ITA ta, boolean activate);
	
	
	/**
	 * Get the IP address of the UCH. 
	 * 
	 * @return return a string value
	 */
	public String getIpAddress();
	
	
	/**
	 * The ITA requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, the UCH will forward all messages that have this URI as its base, to controllerRequest().   
	 * 
	 * @param ta an ITA object
	 * @param scheme a String value of schema
	 * @param port a port no
	 * @param portIsFlexible a boolean value
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible a boolean value
	 * @return a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public String startUriService(ITA ta, String scheme, int port, boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Called when the TA wants to add one or more contexts to a URI service.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	void addUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Called when the TA wants to remove one or more contexts to a URI service.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	void removeUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * The ITA requests the UCH to stop servicing a URI that was requested to be serviced in a previous call to startUriService().  
	 * 
	 * @param uri a String value of URI
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void stopUriService(ITA ta, String uri) throws UCHException;
	
	
	/**
	 * Get resource from the UCH specified by Resource Properties.
	 * 
	 * @param sessionId a String value of sessionId
	 * @param resProps a Map&lt;String, List&lt;String&gt;&gt; of Resource Properties
	 * @return a List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt; value
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps);
	
	
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
	 * Get a Map of UCH Properties.
	 * 
	 * @return an object of Map&lt;String, String&gt; or UCH Properties
	 */
    public Map<String, String> getUCHProps();
    
    
    /**
     * Returns whether specified function is implemented or not.
     * 
     * @param functionName a String value of function Name
     * @return whether the function is implemented or not
     */
    public boolean isImplemented(String functionName);
	
    
    /**
     * Upload Resources on Resource Server.
     * 
     * @param props an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
     * @param resourceUri an Object of List&lt;String&gt;
     * 
     * @return an Object of Map&lt;String, String&gt;
     */
    public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> props, List<String> resourceUri);
    
    //------------------------------Optional Methods-------------------------------
    
    /**
     *  Removes the specified ta from the UCH.
     * 
     * @param ta an Object of ITA
     *
     * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
     */
    public void removeTA(ITA ta) throws UCHNotImplementedException;

}