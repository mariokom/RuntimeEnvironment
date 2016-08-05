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
 * ITDM is responsible for Discovering & Discarding of the devices from the network 
 * and forwards call to ITDMListener. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public interface ITDM {

	/**
	 * When the TDM is installed and loaded, the UCH calls init() prior to any other TDM function
	 * and allows the TDM to initialize itself.  
	 * 
	 * @param tdmListener an Object of ITDMListener
	 * @param tdmProps a Map&lt;String, String&gt; of ITDM Properties
	 * @param uchProps a Map&lt;String, String&gt; of UCH Properties 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps, List<Map<String, IProfile>> contexts) throws TDMFatalException;
	

	/**
	 * It is called before it uninstalls or unloads the TDM.
	 * 
	 */
	public void finalize();
	
	
	/**
	 * Called when one or more contexts have been opened on the UCH.
	 * 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void contextsOpened(List<Map<String, IProfile>> contexts) throws TDMFatalException;
	
	
	/**
	 * Called when one or more contexts were closed or have expired on the UCH.
	 * 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void contextsClosed(List<Map<String, IProfile>> contexts) throws TDMFatalException;
	
	/**
	 * Called when one or more contexts  have been updated on the UCH.
	 * 
	 * @param contexts a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void contextsUpdated(List<Map<String, IProfile>> contexts) throws TDMFatalException;
	
	/**
	 * Get TDM Properties Map.
	 * 
	 * @return a object of Map&lt;String, String&gt; of TDM Properties
	 */
	public Map<String, String> getTDMProps();
	
	
	/**
	 * This function signals the TDM to start discovery of targets.
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void startDiscovery() throws TDMFatalException;
	
	/**
	 * This function signals the TDM to stop discovery of targets.
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
	 */
	public void stopDiscovery() throws TDMFatalException;
	
	
	/**
	 * Get list of contexts for specified target.
	 * 
	 * @param targetId a string value of targetId
	 * 
	 * @return a List&lt;Map&lt;String, IProfile&gt;&gt; of user and controller profile map
	 */
	public List<Map<String, IProfile>> getTargetContexts(String targetId);

	//------------------------------Optional Methods-------------------------------
	/**
	 * The UCH calls this function if it has received a request from a controller to a URI 
	 * that the TDM has claimed for itself by a previous call to startUriService().It call
	 * desired methods of ITDMListener and make changes to response object. 
	 * 
	 * @param protocol a String value of protocol
	 * @param request an Object representing request
	 * @param response an Object representing response
	 * @param context a Map&lt;String, IProfile&gt; of user and controller profile
	 * 
	 * @throws TDMFatalException an Object of {@link org.openurc.uch.TDMFatalException}
     * @throws TDMNotImplementedException an Object of {@link org.openurc.uch.TDMNotImplementedException}
	 */
	public void targetRequest(String protocol, Object request, Object response, Map<String, IProfile> context) throws TDMFatalException, TDMNotImplementedException;
}