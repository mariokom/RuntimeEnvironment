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
 * UIPM - User Interface Protocol Module, takes care of communication from the end-client to the UCH.
 *
 * After getting information from ITDMListener regarding Discovery and Discarding of devices, 
 * UCH instantiates UIPM if it is not instantiated, if it is already instantiated then UCH informs UIPM about
 * Discovery and Discarding of the devices. UIPM forwards calls to the UCH and gets desired response.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public interface IUIPM {

	
	/**
	 * When the UIPM is installed and loaded, the UCH calls this method prior to any other UIPM function
	 * and allows the UIPM to initialize itself.  
	 * 
	 * @param uipmListener an Object of IUIPMListener
	 * @param uipmProps a Map&lt;String, String&gt; of IUIPM Properties
	 * @param uchProps a Map&lt;String, String&gt; of UCH Properties 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void init(IUIPMListener uipmListener, Map<String, String> uipmProps, Map<String, String> uchProps, List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	
	/**
	 * The UCH should call finalize() before it uninstalls or unloads the UIPM.  
	 * 
	 */
	public void finalize();
	
	
	/**
	 *  Called when one or more contexts have been opened on the UCH.
	 *  
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void contextsOpened(List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	
	/**
	 * Called when one or more contexts were closed or have expired on the UCH.
	 * 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void contextsClosed(List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	/**
	 * Called when one or more contexts have been updated on the UCH.
	 * 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void contextsUpdated(List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	/**
	 * Returns Properties of UIPM.
	 * 
	 * @return Map&lt;String, String&gt;
	 */
	public Map<String, String> getUIPMProps();
	
	
	/**
	 * This function is called by UCH when a new target has been discovered after initialization of the UIPM.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void targetDiscovered(String targetId, List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	/**
	 * Called when one or more contexts got added to an existing target.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
	
	/**
	 * Called when one or more contexts got removed to an existing target.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) throws UIPMFatalException;
	
    /**
	 * This function is called by UCH when a target has been discarded after initialization of the UIPM.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void targetDiscarded(String targetId) throws UIPMFatalException;

	
	/**
	 * The target requests the UIPM to open a session with a different socket.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param forwardInfo a Map&lt;String, String&gt; value
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void sessionForwardRequest(String sessionId, Map<String, String> forwardInfo)throws UIPMFatalException;
	
	
	/**
	 * The target has terminated a session.  
	 * 
	 * @param sessionId a String value of sessionId
	 * @param code a String value specifying reason of session abortion
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void sessionAborted(String sessionId, String code)throws UIPMFatalException;
	
	
	/**
	 * The target's status has changed, affecting one or more sessions. 
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds
	 * @param paths a List&lt;String&gt; of paths
	 * @param operations a List&lt;String&gt; of operations
	 * @param values a List&lt;String&gt; of values
	 * @param props a List&lt;Map&lt;String, String&gt;&gt; of attributes
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	public void updateValues(List<String> sessionIds, List<String> paths, List<String> operations, List<String> values, List<Map<String, String>> props)throws UIPMFatalException;

    /**
	 * UCH signals that the set of atomic resources pertaining to a specific session and socket element has changed. 
	 * 
	 * @param sessionIds a List&lt;String&gt; of sessionIds.
	 * @param eltIds a List&lt;String&gt; of elementIds.
	 * @param resources a List&lt;Map&lt;String, Object&gt;&gt; of resources
	 * 
	 */
	public void updateDynRes(List<String> sessionIds, List<String> eltIds, List<Map<String, Object>> resources);
	
	/**
     * Checks whether specified function is implemented or not.
     * 
     * @param functionName a String value of function Name
     * @return whether the function is implemented or not
     */
	public boolean isImplemented(String functionName);
	
	
	/**
	 * Get list of contexts for one of the UIPM's service URI.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
	 */
	List<Map<String, IProfile>> getUriServiceContexts(String uri) throws UIPMFatalException;
	
	
	/**
	 * The UCH calls this function if it has received a request from a controller to a URI 
	 * that the UIPM has claimed for itself by a previous call to startUriService().It call
	 * desired methods of IUIPMListener and make changes to response object. 
	 * 
	 * @param protocol a String value of protocol
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
     *
     * @throws UIPMFatalException an Object of {@link org.openurc.uch.UIPMFatalException}
     * @throws UIPMNotImplementedException an Object of {@link org.openurc.uch.UIPMNotImplementedException}
	 */
	public void controllerRequest(String protocol, Object request, Object response, Map<String, IProfile> context) throws UIPMFatalException, UIPMNotImplementedException;
}