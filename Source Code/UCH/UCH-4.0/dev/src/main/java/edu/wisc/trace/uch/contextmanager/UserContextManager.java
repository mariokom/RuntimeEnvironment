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

package edu.wisc.trace.uch.contextmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITDM;
import org.openurc.uch.IUIPM;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Manage contexts, contexts bounded with targets and contexts bounded with URI.
 * Every context contains user profile and controller profile.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UserContextManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	
	private UserProfileManager userProfileManager;
	
	private ControllerProfileManager controllerProfileManager;
	
	// Map of SessionId vs. UserName
	private Map<String, String> activeSession = new TreeMap<String, String>();
	
	// Map of userName vs. Map of profileType vs. profile
	private Map<String, Map<String, IProfile>> contextMap = new TreeMap<String, Map<String, IProfile>>();
	
	// Map of targetId vs. List of Map of profileType vs. profile
	private Map<String, List<Map<String, IProfile>>> targetIdContextMap = new TreeMap<String, List<Map<String,IProfile>>>();
	
	// Map of uri vs. List of Map of profileType vs. profile
	private Map<String, List<Map<String, IProfile>>> uriContextMap = new TreeMap<String, List<Map<String,IProfile>>>();
	
	private UCH uch;
	
	/**
	 * Constructor.
	 * Provide the reference of UCH to local variable.
	 * Instantiate the object of UserProfileManager and ControllerProfileManager.
	 * 
	 * @param uch an Object of UCH
	 * @param loggedUserContextData an XML formed String specifies user context data
	 * @param userName a String value of userName
	 * @param password a String value of password
	 */
	public UserContextManager(UCH uch, String loggedUserContextData, String userName, String password) {
		
		this.uch = uch;
		userProfileManager = new UserProfileManager(uch, loggedUserContextData, userName, password);
		controllerProfileManager = new ControllerProfileManager();
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invoked from UCH Starts ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * Check whether specified userName and password is valid or not.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return a boolean value specifies whether userName and password is valid or not.
	 */
	public boolean validateUser(HttpServletRequest request, String userName, String password) {
		
		if ( request == null ) {
			logger.warning("Request is null.");
			return false;
		}
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return false;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return false;
		}
		
		if ( getContext(request, userName, password) != null )
			return true;
		else
			return false;
	}
	
	/**
	 * Open Context with specified User.
	 * Also open a session with same user.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return a boolean value specifies whether userName and password is valid or not.
	 */
	public boolean openUserContext(HttpServletRequest request, String userName, String password) {
		
		if ( request == null ) {
			logger.warning("Request Object is nulll.");
			return false;
		}
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return false;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return false;
		}
		
		Map<String, IProfile> context = getContext(request, userName, password);
		
		if ( context == null ) {
			logger.warning("Context is null.");
			return false;
		}
		
		synchronized(contextMap) {
			contextMap.put(userName, context);
		}
		
		HttpSession session = request.getSession();
		
		String sessionId = session.getId();
		
		synchronized(activeSession) {
			activeSession.put(sessionId, userName);
		}
		
		contextOpened(context);
		
		logger.info("User context is opened successfully with the sessionId '"+sessionId+"'.");
		return true;
	}
	
	/**
	 * Get session from request and found user from it and close context with that user.
	 * Also close same session.
	 * 
	 * @param request an Object of HttpServletRequest
	 */
	public void closeUserContext(HttpServletRequest request) {
		
		if ( request == null ) {
			logger.warning("Request Object is null.");
			return;
		}
		
		HttpSession session = request.getSession(false);
		
		if ( session == null ) {
			logger.warning("Unable to get active Session from Request Object");
			return;
		}
		
		String sessionId = session.getId();
		
		if ( sessionId == null ) {
			logger.warning("Unable to get sessionId from session.");
			return;
		}
		
		String userName = null;
		
		synchronized(activeSession) {
			userName = activeSession.remove(sessionId);
		}
		
		if ( userName == null ) {
			logger.warning("Unable to get User for the session '"+sessionId+"'.");
			return;
		}
		
		Map<String, IProfile> context = null;
		
		synchronized( contextMap ) {
			context = contextMap.remove(userName);
		}
		
		contextClosed(context);
	}
	
	
	/**
	 * Get all Contexts.
	 * 
	 * @return an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public List<Map<String, IProfile>> getContexts() {
		
		List<Map<String, IProfile>> contextList = new ArrayList<Map<String,IProfile>>();
		
		synchronized (contextMap) {
			
			for ( Map<String, IProfile> context : contextMap.values() ) {
				
				if ( context == null )
					contextList.add( cloneMap(context) );
			}
		}	
		
		return contextList;	
	}
	
	/**
	 * Get contexts for specified Target Id.
	 * 
	 * @param targetId  a String value of Target Id
	 * @return  an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	List<Map<String, IProfile>> getTargetContexts(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("Target Id is null.");
			return null;
		}
		
		synchronized (targetIdContextMap) {
			return cloneList( targetIdContextMap.get(targetId) );
		}
	}
	
	
	/**
	 * Add specified contexts for specified TargetId.
	 * 
	 *  Note : Empty contexts and null contexts has different meaning.
	 * If contexts is empty then no user/controller context is allowed to use that Target.
	 * If contexts is null then every user/controller context is allowed to use that Target.
	 * 
	 * @param targetId a String value of Target Id
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return;
		}
		
		
		synchronized(targetIdContextMap) {
			
			if ( contexts == null ) { // Allow for all contexts. So replace old contexts with null if exists. Else add a new entry with null.
				
				targetIdContextMap.put(targetId, null);
				
			} else {
				
				if ( targetIdContextMap.containsKey(targetId) ) { // request for updating contexts for existing TargetId
					
					List<Map<String, IProfile>> existingContexts = targetIdContextMap.get(targetId);
					
					if ( existingContexts == null ) { // specified URI is open for all context, so no need to add contexts.
						
						return;
						
					} else { // add new contexts for specified URI
						
						for ( Map<String, IProfile> context : contexts ) {
							
							if ( context == null )
								continue;
							
							if ( !exists(context, existingContexts) ) // check context is already exists.
								existingContexts.add(context);
						}
					}
					
				} else { // request for adding contexts for new URI
					
					targetIdContextMap.put(targetId, cloneList(contexts) );
				}
			}
			
		} // end of synchronized(targetIdContextMap)
		
	}
	
	
	/**
	 * Remove specified contexts for specified TargetId.
	 * 
	 * @param targetId a String value of Target Id
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) {
		
		if ( targetId == null ) {
			logger.warning("Target Id is null.");
			return;
		}
		
		if ( (contexts == null) || (contexts.size() == 0) ) {
			logger.warning("Contexts is null or no context is available to add.");
			return;
		}
		
		synchronized(targetIdContextMap) {
			
			List<Map<String, IProfile>> existingContexts = targetIdContextMap.get(targetId);
			
			if ( existingContexts == null ) {
				logger.warning("Context doesn't exists for targetId '"+targetId+"'.");
				return;
			}
			
			for ( Map<String, IProfile> context : contexts ) {
				
				if ( context == null )
					continue;
				
				remove(context, existingContexts); // remove specified context.
			}	
		}
	}
	
	
	/**
	 * Get available Target Ids for specified context.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getAvailableTargetIds(Map<String, IProfile> context) {
		
		if ( context == null ) {
			logger.warning("Context is null.");
			return null;
		}
		
		List<String> targetIds = new ArrayList<String>();
		
		synchronized(targetIdContextMap) {
			
			for ( Entry<String, List<Map<String, IProfile>>> entry : targetIdContextMap.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String targetId = entry.getKey();
				
				if ( targetId == null )
					continue;
				
				List<Map<String, IProfile>> contexts = entry.getValue();
				
				if ( contexts == null ) { // Target is available for all contexts. So add it.
					
					targetIds.add( entry.getKey() );
					
				} else { // Check whether Target is available for specified context. Then add it.
					
					if ( exists(context, entry.getValue()) )
						targetIds.add( entry.getKey() );
					
				}
			}
		}
		
		if ( targetIds.size() == 0 )
			return null;
		else
			return targetIds;
	}
	
	
	/**
	 * Add specified contexts for specified URI.
	 * 
	 * Note : Empty contexts and null contexts has different meaning.
	 * If contexts is empty then no user/controller context is allowed to use that URI.
	 * If contexts is null then every user/controller context is allowed to use that URI.
	 * 
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void addUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			if ( contexts == null ) { // Allow for all contexts. So replace old contexts with null if exists. Else add a new entry with null.
				
				uriContextMap.put(uri, null);
				
			} else {
				
				if ( uriContextMap.containsKey(uri) ) { // request for updating contexts for existing URI
					
					List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
					
					if ( existingContexts == null ) { // specified URI is open for all context, so no need to add contexts.
						
						return;
						
					} else { // add new contexts for specified URI
						
						for ( Map<String, IProfile> context : contexts ) {
							
							if ( context == null )
								continue;
							
							if ( !exists(context, existingContexts) ) // check context is already exists.
								existingContexts.add(context);
						}
					}
					
				} else { // request for adding contexts for new URI
					
					uriContextMap.put(uri, cloneList(contexts) );
				}
			}
			
		} // end of synchronized(uriContextMap)
	}
	
	/**
	 * Add specified contexts for specified URI.
	 * 
	 * Note : Empty contexts and null contexts has different meaning.
	 * If contexts is empty then no user/controller context is allowed to use that URI.
	 * If contexts is null then every user/controller context is allowed to use that URI.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void addUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			if ( contexts == null ) { // Allow for all contexts. So replace old contexts with null if exists. Else add a new entry with null.
				
				uriContextMap.put(uri, null);
				
			} else {
				
				if ( uriContextMap.containsKey(uri) ) { // request for updating contexts for existing URI
					
					List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
					
					if ( existingContexts == null ) { // specified URI is open for all context, so no need to add contexts.
						
						return;
						
					} else { // add new contexts for specified URI
						
						for ( Map<String, IProfile> context : contexts ) {
							
							if ( context == null )
								continue;
							
							if ( !exists(context, existingContexts) ) // check context is already exists.
								existingContexts.add(context);
						}
					}
					
				} else { // request for adding contexts for new URI
					
					uriContextMap.put(uri, cloneList(contexts) );
				}
			}
			
		} // end of synchronized(uriContextMap)
	}
	
	/**
	 * Add specified contexts for specified URI.
	 * 
	 * Note : Empty contexts and null contexts has different meaning.
	 * If contexts is empty then no user/controller context is allowed to use that URI.
	 * If contexts is null then every user/controller context is allowed to use that URI.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void addUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			if ( contexts == null ) { // Allow for all contexts. So replace old contexts with null if exists. Else add a new entry with null.
				
				uriContextMap.put(uri, null);
				
			} else {
				
				if ( uriContextMap.containsKey(uri) ) { // request for updating contexts for existing URI
					
					List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
					
					if ( existingContexts == null ) { // specified URI is open for all context, so no need to add contexts.
						
						return;
						
					} else { // add new contexts for specified URI
						
						for ( Map<String, IProfile> context : contexts ) {
							
							if ( context == null )
								continue;
							
							if ( !exists(context, existingContexts) ) // check context is already exists.
								existingContexts.add(context);
						}
					}
					
				} else { // request for adding contexts for new URI
					
					uriContextMap.put(uri, cloneList(contexts) );
				}
			}
			
		} // end of synchronized(uriContextMap)
	}
	
	
	/**
	 * Remove specified contexts for specified URI.
	 * 
	 * @param uipm an object of IUIPM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void removeUriServiceContexts(IUIPM uipm, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		if ( (contexts == null) || (contexts.size() == 0) ) {
			logger.warning("Contexts is null or no context is available to add.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
			
			if ( existingContexts == null ) {
				logger.warning("Context doesn't exists for URI '"+uri+"'.");
				return;
			}
			
			for ( Map<String, IProfile> context : contexts ) {
				
				if ( context == null )
					continue;
				
				remove(context, existingContexts); // remove specified context.
			}	
		}
	}
	
	/**
	 * Remove specified contexts for specified URI.
	 * 
	 * @param ta an object of ITA
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void removeUriServiceContexts(ITA ta, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		if ( (contexts == null) || (contexts.size() == 0) ) {
			logger.warning("Contexts is null or no context is available to add.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
			
			if ( existingContexts == null ) {
				logger.warning("Context doesn't exists for URI '"+uri+"'.");
				return;
			}
			
			for ( Map<String, IProfile> context : contexts ) {
				
				if ( context == null )
					continue;
				
				remove(context, existingContexts); // remove specified context.
			}	
		}
	}
	
	/**
	 * Remove specified contexts for specified URI.
	 * 
	 * @param tdm an object of ITDM
	 * @param uri a String value of URI
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	public void removeUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) {
		
		if ( uri == null ) {
			logger.warning("URI is null.");
			return;
		}
		
		if ( (contexts == null) || (contexts.size() == 0) ) {
			logger.warning("Contexts is null or no context is available to add.");
			return;
		}
		
		synchronized(uriContextMap) {
			
			List<Map<String, IProfile>> existingContexts = uriContextMap.get(uri);
			
			if ( existingContexts == null ) {
				logger.warning("Context doesn't exists for URI '"+uri+"'.");
				return;
			}
			
			for ( Map<String, IProfile> context : contexts ) {
				
				if ( context == null )
					continue;
				
				remove(context, existingContexts); // remove specified context.
			}	
		}
	}
	/**
	 * Get a List of URI that allow specified context.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getURIs(Map<String, IProfile> context) {
		
		synchronized(uriContextMap) {
			
			List<String> uris = new ArrayList<String>();
			
			for ( Entry<String, List<Map<String, IProfile>>> entry : uriContextMap.entrySet() ) {
				
				if ( entry == null )
					continue;
				
				String uri = entry.getKey();
				
				if ( uri == null )
					continue;
				
				List<Map<String, IProfile>> contexts = entry.getValue();
				
				if ( contexts == null ) { // URI is open for all users.
					
					uris.add(uri);
				
				} else {
					
					if ( exists(context, contexts) )
						uris.add(uri);
				}
				
			}
			
			return uris;
		}
	}
	
	/**
	 * Get user Context for specified Session Id.
	 * 
	 * @param sessionId a String value of SessionId
	 * 
	 * @return an Object of Map&lt;String, IProfile&gt;
	 */
	public Map<String, IProfile> getContext(String sessionId) {
	
		if ( sessionId == null ) {
			logger.warning("SessionId is null.");
			return null;
		}
		
		String userName = null;
		
		synchronized(activeSession) {
		
			userName = activeSession.get(sessionId);
		}
		
		if ( userName == null ) {
			logger.warning("Unable to get UserName for sessionId '" + sessionId + "'.");
			return null;
		}
		
		synchronized (contextMap) {
			
			return cloneMap( contextMap.get(userName) );
		} 
		
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invoked from UCH Ends ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------------- Utilities Methods Starts -------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Call the same method of UCH.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 */
	private void contextOpened(Map<String, IProfile> context) {
		
		if ( context == null ) 
			return;
		
		List<Map<String, IProfile>> contexts = new ArrayList<Map<String,IProfile>>();
		contexts.add( cloneMap(context) );
		uch.contextOpened(contexts);
	}
	
	/**
	 * Call the same method of UCH.
	 *  
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 */
	private void contextClosed(Map<String, IProfile> context) {
		
		if ( context == null ) 
			return;
		
		List<Map<String, IProfile>> contexts = new ArrayList<Map<String,IProfile>>();
		contexts.add( cloneMap(context) );
		uch.contextClosed(contexts);
	}
	
	/**
	 * Get User Profile and Controller Profile form UserProfileManager and ControllerProfileManager respectively.
	 * Create Context from that and return it.
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param userName a String value of userName
	 * @param password a String value of password
	 * 
	 * @return an Object of Map&lt;String, IProfile&gt;
	 */
	private Map<String, IProfile> getContext(HttpServletRequest request, String userName, String password) {
		
		if ( request == null ) {
			logger.warning("Request Object is null.");
			return null;
		}
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return null;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return null;
		}
		
		IProfile usrProfile = userProfileManager.getUserProfile(userName, password);
		
		if ( usrProfile == null ) {
			logger.warning("Unable to get User Profile for the user '"+userName+"' and password '"+password+"'.");
			return null;
		}
		
		IProfile controllerProfile = controllerProfileManager.getControllerProfile(request);
		
		if ( controllerProfile == null ) {
			logger.warning("Unable to get Controller Profile for the user '"+userName+"'.");
			return null;
		}
		
		Map<String, IProfile> context = new HashMap<String, IProfile>();
		
		context.put(Constants.PROFILE_USER, usrProfile);
		context.put(Constants.PROFILE_CONTROLLER, controllerProfile);
		
		return context;
	}
	
	/**
	 * Remove specified context from contexts.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 * 
	 * @return whether context removes successfully or not
	 */
	private boolean remove(Map<String, IProfile> context, List<Map<String, IProfile>> contexts) {
		
		if ( (context == null) || (contexts == null) )
			return false;
		
		for ( Map<String, IProfile> existingContext : contexts ) {
			
			if ( compare(context, existingContext) ) { // If both context are same, remove it
				contexts.remove(existingContext);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get user profile for supplied username and password.
	 * 
	 * @param userName Logged UCH user.
	 * @param password Logged UCH user password.
	 * @return Logged UCH User profile.
	 */
	public IProfile getUserProfile(String userName, String password) {
		
		return userProfileManager.getUserProfile(userName, password);
	}
	
	/**
	 * Return whether specified context exists in contexts.
	 * 
	 * @param context an Object of Map&lt;String, IProfile&gt;
	 * @param contexts an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 * 
	 * @return whether specified context exists in contexts
	 */
	private boolean exists(Map<String, IProfile> context, List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null )
			return false;
		
		if ( context == null ) 
			return false;
		
		for ( Map<String, IProfile> existingContext : contexts ) {
			
			if ( compare(context, existingContext) ) // If both context are same, return true.
				return true;
		}
		
		// No context matched, return false.
		return false;
	}
	
	
	/**
	 * Compare the contents of specified maps. 
	 * If both are same then return true else return false.
	 * 
	 * @param context1 an Object of Map&lt;String, IProfile&gt;
	 * @param context2 an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return whether both Object are same or not.
	 */
	private boolean compare(Map<String, IProfile> context1, Map<String, IProfile> context2) {
		
		if ( context1 == null ) { // if context1 is null.
			
			if ( context2 == null ) { // If both are null, return true.
				
				return true;
				
			} else { // If context1 is null and context2 is not null, return false.
				
				return false;
			}
			
		} else { // context1 is not null.
			
			if ( context2 == null ) { // If context1 is not null and context2 is null, return false.
				
				return false;
				
			} else { // If both are not null then check contents of both map.
				
				if ( context1.size() == context2.size() ) {
					
					for ( Entry<String, IProfile> entry1 : context1.entrySet() ) {
					
						String key1 = entry1.getKey();
						
						if ( context2.containsKey(key1) ) {
							
							if ( !compare(entry1.getValue(), context2.get(key1) ) ) // If both profile doesn't match,return false.
								return false;
							
						} else { // If context2 has not the same key, return false.
							
							return false;
						}
						
					}
					
					// All key-value pair are matched, return true.
					return true;
					
				} else { // If both maps size doesn't match, return false.
					
					return false;
				}	
			}
		}
		
	}
	
	
	/**
	 * Compare specified objects of IProfile.
	 * If both are same then return true else return false.
	 * 
	 * @param profile1 an Object of IProfile
	 * @param profile2 an Object of IProfile
	 * 
	 * @return whether both Object are same or not.
	 */
	private boolean compare(IProfile profile1, IProfile profile2) {
		
		if ( profile1 == null ) { // if profile1 is null.
			
			if ( profile2 == null ) { // If both are null, return true.
				
				return true;
				
			} else {  // If profile1 is null and profile2 is not null, return false.
				
				return false;
			}
			
		} else { // profile1 is not null.
			
			if ( profile2 == null ) { // If profile1 is not null and profile2 is null, return false.
				
				return false;
				
			} else {
				
				return profile1.equals(profile2);
			}
		}
		
	}
	
	
	
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
	
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------------- Utilities Methods Ends --------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
}
