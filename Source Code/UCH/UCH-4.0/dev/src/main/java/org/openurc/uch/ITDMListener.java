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
 * ITDMListener receives calls from TDM. UCH implements this interface.
 * The Listener gets information from TDM regarding Discovery and Discarding of devices.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public interface ITDMListener {

	
	/**
	 * Get currently open contexts.
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public List<Map<String, IProfile>> getContexts();
	
	
	/**
	 * This function is called by a TDM when it has discovered a new target 
	 * and loads appropriate TA for the new target.
	 * If the corresponding TA is already installed, the new target should be registered with the already installed TA.
	 * 
	 * @param tdm an Object of ITDM
	 * @param targetProps a Map&lt;String, Object&gt; of discovered Target Properties
	 * @param taProps a Map&lt;String, String&gt; of TA(Target Adapter)'s Properties
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @return a String The UCH shall return the targetId (String) that it has assigned the newly discovered target.  This may be identical to the property ‘targetInstance’ in targetProps
	 */ 
	public String targetDiscovered(ITDM tdm, Map<String, Object> targetProps, Map<String, String> taProps, List<Map<String, IProfile>> contexts);
	
	
	/**
	 * Called when one or more contexts got added to an existing target.
	 *  
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts);
	
	
	/**
	 * Called when one or more contexts got removed to an existing target.
	 * 
	 * @param targetId a String value of targetId
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts);
	
	
	/**
	 * A TDM calls this function when a target has been disappeared from the network.  
	 * 
	 * @param targetId a String value of targetId
	 */
	public void targetDiscarded(String targetId);
	
	
	/**
	 * Get the Local UCH Store.
	 * 
	 * @return an Object of IUCHStore
	 */
	public IUCHStore getLocalUCHStore();

    
    /**
	 * Get the IP address of the UCH. 
	 * 
	 * @return return a string value
	 */
	public String getIpAddress();
	
	
	/**
	 * The ITDM requests the UCH to start servicing a URI which is made up of a given scheme, port, and base path.  
	 * If successful, then the UCH will forward all messages that have this URI as its base, to controllerRequest().   
	 * 
	 * @param tdm an Object of ITDM
	 * @param scheme a String value of scheme
	 * @param port an int value of port number
	 * @param portIsFlexible a boolean value 
	 * @param basePath a String value of basePath
	 * @param basePathIsFlexible  a boolean value
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @return a String value of URI
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public String startUriService(ITDM tdm, String scheme, int port, boolean portIsFlexible, String basePath, boolean basePathIsFlexible, List<Map<String, IProfile>> contexts) throws UCHException;

	
	/**
	 * Called when the TDM wants to add one or more contexts to a URI service.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void addUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * Called when the TDM wants to close one or more contexts to a URI service.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void removeUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) throws UCHException;
	
	
	/**
	 * The ITDM requests the UCH to stop servicing a URI that was requested to be serviced in a previous call to startUriService().  
	 * 
	 * @param uri a String value of URI
	 * 
	 * @throws UCHException an Object of {@link org.openurc.uch.UCHException}
	 */
	public void stopUriService(ITDM tdm, String uri) throws UCHException;
	
	
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
     * Checks whether specified function is implemented or not.
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
     *  Removes the specified tdm from the UCH.
     * 
     * @param tdm an Object of ITDM
     *
     * @throws UCHNotImplementedException an Object of {@link org.openurc.uch.UCHNotImplementedException}
     */
    public void removeTDM(ITDM tdm) throws UCHNotImplementedException;
}