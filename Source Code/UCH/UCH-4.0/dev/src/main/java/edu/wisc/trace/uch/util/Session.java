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

package edu.wisc.trace.uch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.StringTokenizer;

import org.openurc.uch.TAFatalException;

import edu.wisc.trace.uch.util.socket.IUISocketElement;
import edu.wisc.trace.uch.util.socket.UISocketBasicCommand;
import edu.wisc.trace.uch.util.socket.UISocketCommand;
import edu.wisc.trace.uch.util.socket.UISocketCommandParam;
import edu.wisc.trace.uch.util.socket.UISocketNotification;
import edu.wisc.trace.uch.util.socket.UISocketSet;
import edu.wisc.trace.uch.util.socket.UISocketTimedCommand;
import edu.wisc.trace.uch.util.socket.UISocketVariable;
import edu.wisc.trace.uch.util.socket.UISocketVoidCommand;

/**
 * Provides methods to maintain session for the elements of UI Socket Description.
 * 
* @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class Session {

private Logger logger = LoggerUtil.getSdkLogger();
	
	public static final int ELEMENT_TYPE_SET = 0;
	public static final int ELEMENT_TYPE_VARIABLE = 1;
	public static final int ELEMENT_TYPE_VOID_COMMAND = 2;
	public static final int ELEMENT_TYPE_BASIC_COMMAND = 3;
	public static final int ELEMENT_TYPE_TIMED_COMMAND =4;
	public static final int ELEMENT_TYPE_COMMAND_PARAM = 5;
	public static final int ELEMENT_TYPE_NOTIFICATION = 6;

	public static final int SUCCESSFUL = 0;
	public static final int INCORRECT_PATH = 1;
	public static final int INCORRECT_DIM_VALUE = 2;
	public static final int DIMENSION_ALREADY_EXISTS = 3;
	
	private String sessionId;
	private String targetId;
	private String socketName;
	private Map<String, Object> targetProps;
	private List<IUISocketElement> elements;
	
	/**
	 * Initializes all the local variables
	 * 
	 * @param sessionId a String value of sessionId
	 * @param targetId a String value of targetId
	 * @param targetProps a Map&lt;String, Object&gt; of Target Properties
	 * @param socketName a String value of socketName
	 * @param elements a List&lt;IUISocketElement&gt; of IUISocketElements
	 */
	
	public Session(String sessionId, String targetId, Map<String, Object> targetProps, String socketName, List<IUISocketElement> elements) {
		
		this.sessionId = sessionId;
		this.targetId = targetId;
		this.targetProps = targetProps;
		this.elements = elements;
		this.socketName = socketName;
		
		setSession();		
	}

	/**
	 * Set the reference of Session in all the IUISocketElements.
	 */
	
	private void setSession() {
		
		for ( IUISocketElement socketElement : elements ) {
			
			if ( socketElement instanceof UISocketSet ){ 
				setSession( (UISocketSet)socketElement );
			}else{
				socketElement.setSession(this);
			}
		}
	}
	
	/**
	 * Set the reference of Session in all IUISocketElement of UISocketSet.
	 * 
	 * @param socketSet an Object of UISocketSet
	 */
	
	private void setSession( UISocketSet socketSet ) {
		
		socketSet.setSession(this);
		
		Map<String, IUISocketElement> elementMap = socketSet.getElementMap();
		
		for ( IUISocketElement socketElement : elementMap.values() ) {
			
			if ( socketElement instanceof UISocketSet ) 
				setSession( (UISocketSet)socketElement );
			else 
				socketElement.setSession(this);
			
		}
	}
	
	/**
	 * Get the String value of sessionId.
	 * 
	 * @return a String value of sessionId
	 */
	
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Get an Object of propertyValue for 'propName' from Target Properties.
	 * 
	 * @param propName a String value of propName
	 * @return an Object for PropertyValue
	 */
	
	public Object getPropertyValue(String propName) {
		
		return targetProps.get(propName);
	}

	/**
	 * Get the String value of SocketName.
	 * 
	 * @return a String value of socket Name
	 */
	
	public String getSocketName() {
		return socketName;
	}
	
	/**
	 * Get the String value of SocketName.
	 * 
	 * @return a String value of socket Name
	 */
	public String getTargetId() {
		return targetId;
	}
	
	/////////////////////////////////////////isElementExists Starts/////////////////////////////////////////
	
	/**
	 * Check whether the specified Element by elementPath is exists or not.
	 * 
	 * @param elementPath a String value of elementPath
	 * 
	 * @return whether the specified Element by elementPath is exists or not
	 */
	public boolean isElementExists(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("ElementPath is null.");
			return false;
		}
		
		if ( (elementPath.indexOf(' ') != -1) || (elementPath.indexOf("[]") != -1) ) {
			logger.warning("ElementPath is for range of value.");
			return false;
		}
		
		IUISocketElement socketElement = null;
		
		if ( elementPath.indexOf('/') == -1 ) { // for elementId
			
			socketElement = getSocketElementByElementId(elementPath, true);
			
		} else {
			
			Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
			
			if ( getSocketElementsByElementPath(elementPath, elementMap) != SUCCESSFUL ) 
				return false;
			
			if ( (elementMap == null) || (elementMap.size() != 1) )
				return false;
			
			for( IUISocketElement socElement : elementMap.values() )
				socketElement = socElement;
		}
			
			
		if ( socketElement == null )
			return false;
		
		if ( elementPath.indexOf('/') == -1 ) 	 // elementPath is elementId 	
			return true;
		
		int dimStartIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/') );
		
		if ( dimStartIndex == -1 ) 	// non dimensional elementPath
			return true;
		
		String index = elementPath.substring(dimStartIndex);
		
		if ( !socketElement.addDimension(index) ) // if true then dimension is exists. 
			return true;
		
		// If it allow to add new dimension means elementPath does not exists so remove dimension and return false
		socketElement.removeDimension(index);
		return false;
		/*
		Map<String, String> valueMap = getValue(elementPath);
		
		if ( (valueMap == null) || (valueMap.size() == 1) ) {
			
			if ( elementPath.indexOf('/') == -1 ) {
				
				return true;
				
			} else {
				
				int dimStartIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/') );
				
				if ( dimStartIndex == -1 ) {
					
					return true;
					
				} else {
					
					String dimValue = elementPath.substring(dimStartIndex);
					
					
				}
				
			}
			
		} else { 
			return false;
		}
		*/
	}
	
	
	//////////////////////////////////////Get ChildElementPaths Starts//////////////////////////////////////
	
	/**
	 * Get the list of path from root of all the Element(excluding Set) of specified Set by elementPath.
	 * 
	 * @param elementPath a String value of elementPath
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getChildElementPaths(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return null;
		}
		
		String indices = null;
		int dimIndex = -1;
		
		if ( (dimIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/')) ) != -1 ) {
			
			indices = elementPath.substring( dimIndex );		
			elementPath = elementPath.substring(0, dimIndex);
			
			if ( !CommonUtilities.isIndexValueValid(indices) ) {
				logger.warning("'"+indices+"' is invalid");
				return null;
			}
		}
		
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		if ( getSocketElementsByElementPath(elementPath, elementMap) != SUCCESSFUL ) {
			
			logger.warning("Unable to get Socket Elements for the Element Path '"+elementPath+"'.");
			return null;
		}
		
		List<String> returnPaths = new ArrayList<String>();
		
		for ( String setPath : elementMap.keySet() ) {
			
			IUISocketElement socketElement = elementMap.get(setPath);
			
			if ( (socketElement == null) || !(socketElement instanceof UISocketSet) )
				continue;
			
			// Changes to return list of map containing element attributes. Parikshit Thakur : 20110727 
			List<Map<String, String>> valMapList = getValue(socketElement, setPath, indices, true, true);
			
			if ( valMapList != null ){
				Map<String, String> valMap = null;
				for(int i = 0; i < valMapList.size(); i++){
					valMap = valMapList.get(i);
					returnPaths.add(valMap.get("path"));
				}
				
				//returnPaths.addAll(valMap.keySet());
			}
			// Change ends. Parikshit Thakur : 20110727	
			
		}
		
		List<String> returnList = new ArrayList<String>();
		
		for ( String path : returnPaths ) {
			
			if ( path.endsWith("[state]") ) {
				
				Map<String, IUISocketElement> eltMap = new HashMap<String, IUISocketElement>();
				getSocketElementsByElementPath(path , eltMap);
				if ( (eltMap != null) && (eltMap.size() > 0) ) {
					
					
					for ( IUISocketElement element : eltMap.values() ) {
						
						if ( element instanceof UISocketCommand )
							returnList.add( path.substring(0, path.lastIndexOf('[')) );
						else
							returnList.add(path);
						
						break;
					}
						
				}
				
			} else  {
				returnList.add(path);
			}
		}
		
		return returnList;
	}
	///////////////////////////////////////Get ChildElementPaths Ends///////////////////////////////////////
	
	///////////////////////////////////////////Get Value Starts/////////////////////////////////////////////
	
	/**
	 * Get the List, containing value of the Socket Element or Socket Elements with their attributes specified by elementPath.
	 * Element Path can be any of the following.
	 *  (1) elementId (e.g. eltId)
	 *  (2) path from root(e.g. /set1/set2[index1]/id) 
	 *  (3) path for range of indices(e.g. /set1/set2[index1 index5]/id, /set1/set2[index1]/id[])
	 *  (4) root (e.g. /)
	 *  
	 * Return the List of Map of element Path from the root, its value and attributes.
	 * Map doesn't contain the value of sets and its child Elements(Applicable only in case of set path and root path).
	 *  
	 * @param elementPath a String value of elementPath
	 *  
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt; representing paths,its value and attributes.
	 */
	public List<Map<String, String>> getValue(String elementPath) { // changed signature to return list of map containing element attributes. Parikshit Thakur : 20110727 
		
		return getValue(elementPath, false);
	}
	
	/**
	 * Get the List, containing value of the Socket Element or Socket Elements with their attributes specified by elementPath.
	 * Element Path can be any of the following.
	 *  (1) elementId (e.g. eltId)
	 *  (2) path from root(e.g. /set1/set2[index1]/id) 
	 *  (3) path for range of indices(e.g. /set1/set2[index1 index5]/id, /set1/set2[index1]/id[])
	 *  (4) root (e.g. /)
	 *  
	 *  If the value of includeSets is true, then get the value child sets recursively.
	 *  Else don't include set in return Map.
	 *  includeSets is applicable to only Sets and root not any other Socket Elements.
	 *  
	 *  Return the List of Map of element Path from the root, its value and attributes.
	 *  
	 * @param elementPath a String value of elementPath
	 *  
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt; representing paths,its value and attributes
	 */
	public List<Map<String, String>> getValue(String elementPath, boolean includeSets) {  // changed signature to return list of map containing element attributes. Parikshit Thakur : 20110727
		
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return null;
		}
		
		elementPath = elementPath.trim();
		
		if( elementPath.equals("/") )
			return getValueRoot(true);
		else if ( elementPath.indexOf("/") == -1 ) // e.g. elementPath = eltId1
			return getValueByElementId(elementPath, includeSets);
		else								  // e.g. elementPath = /set1/eltId1
			return getValueByElementPath(elementPath, includeSets);	
	}
	
	
	/**
	 * Get value of all elements.
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	private List<Map<String, String>> getValueRoot(boolean includeSets) { // Changed signature to return list of map containing element attributes. Parikshit Thakur : 20110727
		
		if ( elements == null ) {
			logger.warning("Element Map is null.");
			return null;
		}
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		//Map<String, String> returnMap = new HashMap<String, String>();
		
		for( IUISocketElement socketElement : elements ) {
			if ( socketElement == null )
				continue;
			
			if ( socketElement.isDimensional() ) {
				
				// 2011-02-03 Added By Pragnesh for get dimension indices	
				StringTokenizer dimTypeTokens = new StringTokenizer(socketElement.getDimType());
				String indices = new String();
				while (dimTypeTokens.hasMoreTokens()) {
					indices += "[]"; 
			        dimTypeTokens.nextToken();
			     }
				//End by Pragnesh Patel
				List<Map<String, String>> valueMapList = getValue(socketElement, "/"+socketElement.getElementId(), indices, includeSets, false);
				
				if ( valueMapList == null )
					continue;
				returnList.addAll(valueMapList);
								
			} else {
			
				List<Map<String, String>> valueMapList = getValue(socketElement, "/"+socketElement.getElementId(), null, includeSets, false);
				if ( valueMapList == null )
					continue;
				returnList.addAll(valueMapList);
				
			}
			
		}
				 
		return returnList;
	}
	
	/**
	 * Get the List of value of the Element specified by elementId with attributes.
	 * 
	 * @param elementId a String value of ElementId
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	private List<Map<String, String>> getValueByElementId(String elementId, boolean includeSets) { // Changed signature to return list of map containing element attributes. Parikshit Thakur : 20110727
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		if ( elementId == null )
			return null;
		
		if ( elementId.indexOf('[') == -1 ) { // elementId is non-dimensional.
			IUISocketElement socketElement = getSocketElementByElementId(elementId, true);
			if ( socketElement == null ) {
				logger.warning("Unable to get Socket Element for elementId '"+elementId+"'.");
				return null;
			}
			
			List<Map<String, String>> valueMapList = getValue(socketElement, elementId, null, includeSets, false);
			returnList.addAll(valueMapList);
			
		} else if ( validateDimensionalElementId(elementId) ) { // elementId is dimensional.
			
			IUISocketElement socketElement = getSocketElementByElementId(elementId.substring(0, elementId.indexOf('[')), true);
			
			if ( socketElement == null )
				return null;
			
			String indices = elementId.substring( elementId.indexOf('[') );
			
			List<Map<String, String>> valueMapList =  getValue(socketElement, elementId, indices, includeSets, false);
			returnList.addAll(valueMapList);
			
		} else {
			
			logger.warning("'"+elementId+"' has invalid dimensional value.");
			return null;
		}
		return returnList;
	}
	
	/**
	 * Get the List of value of the Socket Element specified by elementPath with their attributes.
	 * Element Path can be any of the following.
	 *  (1) path from root(e.g. /set1/set2[index1]/id) 
	 *  (2) path for range of indices(e.g. /set1/set2[index1 index5]/id, /set1/set2[index1]/id[])
	 *  
	 *  Return the List of Map of element Path from the root,its value and attributes
	 *  
	 * @param elementPath a String value of elementPath
	 * 
	 * @return an Object of List&lt;Map&lt;String, String&gt;&gt;
	 */
	private List<Map<String, String>> getValueByElementPath(String elementPath, boolean includeSets) { // Changed signature to return list of map containing element attributes. Parikshit Thakur : 20110727
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		if ( elementPath == null )
			return null;
		
		String indices = null;
		int dimIndex = -1;
		
		if ( (dimIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/')) ) != -1 ) {
			
			indices = elementPath.substring( dimIndex );		
			elementPath = elementPath.substring(0, dimIndex);
		}
		
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		if ( getSocketElementsByElementPath(elementPath, elementMap) != SUCCESSFUL ) {
			
			logger.warning("Unable to get Socket Elements for the Element Path '"+elementPath+"'.");
			return null;
		}
		
		
		for ( String path : elementMap.keySet() ) {
			
			IUISocketElement socketElement = elementMap.get(path);
			
			if ( socketElement == null )
				continue;
			
			List<Map<String, String>> valueMapList = getValue(socketElement, path, indices, includeSets, false);
			
			if( valueMapList == null )
				continue;
			
			returnList.addAll(valueMapList); 
			
		}
		
		return returnList;
	}
	
	
	/**
	 * Get the map containing value, path and attributes of element.
	 * @param elementPath
	 * @param socketEltType
	 * @param value
	 * @return the map containing value, path and attributes of element
	 */
	private Map<String, String> getValueMap(String elementPath, String socketEltType, Object value ){
		
		Map<String, String> valueMap = new HashMap<String, String>();
		
		valueMap.put("path", elementPath);
		valueMap.put("socketEltType", socketEltType);
		if ( value == null ){						
			valueMap.put("value", null);
		}
		else{						
			valueMap.put("value", value.toString());
		}
		
		return valueMap;
	}
	
	
	
	// Changes have been done in the below method to append attributes of elements in the return Map. Added attributes to the return map.
	// Parikshit Thakur : 20110727
	private List<Map<String, String>> getValue(IUISocketElement socketElement, String elementPath, String indices, boolean includeSets, boolean includeParams) {
		
		if ( (socketElement == null) || (elementPath == null) )
			return null;
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		if ( socketElement instanceof UISocketVariable ) { // For Variable
			
			if ( indices == null ) {
				if ( !socketElement.isDimensional()  ) {
										
					returnList.add(getValueMap(elementPath,"var",((UISocketVariable)socketElement).getValue()));
					return returnList;
				}
				
			} else {
				
				if ( socketElement.isDimensional() ) {
					
					if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
						
						returnList.add(getValueMap(elementPath+indices, "var",((UISocketVariable)socketElement).getValue(indices)));
					}
					else {
						Map<String, Object> rangeMap = ((UISocketVariable)socketElement).getRangeOfValues(indices);
						if ( rangeMap != null ) {
							
							for ( String dim : rangeMap.keySet() ) {
								returnList.add(getValueMap(elementPath+dim, "var",rangeMap.get(dim)));
							}
						}
					}
					return returnList;
					
				}
			}
			
		} else if ( socketElement instanceof UISocketCommand ) { // For Command
			
			Map<String, String> valueMap = new HashMap<String, String>();
			
			if ( socketElement instanceof UISocketVoidCommand ) { // For Void Command
				
				if ( indices == null ) {
					
					if ( !socketElement.isDimensional() ) {
						returnList.add(getValueMap(elementPath+"[state]", "cmd", "initial"));	
					}
					
				} else {
					
					if ( socketElement.isDimensional() ) {
						returnList.add(getValueMap(elementPath+indices+"[state]", "cmd", "initial"));
					}
				}
			
				
			} else if ( socketElement instanceof UISocketBasicCommand ) { // For Basic Command	
				if ( indices == null ) {
					
					if ( !socketElement.isDimensional() ) {
						returnList.add(getValueMap(elementPath+"[state]", "cmd", ((UISocketBasicCommand)socketElement).getState()));
						
					}
					
				} else {
					if ( indices.equals("[state]") ) {
						
						if ( !socketElement.isDimensional() ) {
							returnList.add(getValueMap(elementPath+"[state]", "cmd", ((UISocketBasicCommand)socketElement).getState()));
							
						}
						
					} else {
						
						if ( indices.endsWith("[state]") )
							indices = indices.substring(0, indices.indexOf("[state]") );
							
						if ( socketElement.isDimensional() ) {
							
							if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
								returnList.add(getValueMap(elementPath+"[state]", "cmd", ((UISocketBasicCommand)socketElement).getState(indices)));
								
							}
							else {
								Map<String, String> rangeMap = ((UISocketBasicCommand)socketElement).getRangeOfState(indices);
								if ( rangeMap != null ) {
									
									for ( String dim : rangeMap.keySet() ) {
										returnList.add(getValueMap(elementPath+dim+"[state]", "cmd", rangeMap.get(dim)));
										
									}
								}
							}
						}
					}
				}
				
			} else if ( socketElement instanceof UISocketTimedCommand ) { // For Timed Command
				
				if ( indices == null ) {
					
					if ( !socketElement.isDimensional() ) {
						returnList.add(getValueMap(elementPath+"[state]", "cmd", ((UISocketTimedCommand)socketElement).getState()));
						
					}
					
				} else {
					
					if ( indices.equals("[state]") ) {
						
						if ( !socketElement.isDimensional() ) {
							returnList.add(getValueMap(elementPath+"[state]", "cmd", ((UISocketTimedCommand)socketElement).getState()));
							
						}
					
					} else if ( indices.equals("[ttc]") ) {
						
						if ( !socketElement.isDimensional() ) {
							returnList.add(getValueMap( elementPath+"[ttc]", "cmd", String.valueOf( ((UISocketTimedCommand)socketElement).getTtc() )));
							
						}
					
					} else if ( indices.endsWith("[ttc]") ) {
						
						if ( socketElement.isDimensional() ) {
							
							indices = indices.substring(0, indices.indexOf("[ttc]") );
							
							if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
								returnList.add(getValueMap( elementPath+"[ttc]", "cmd", String.valueOf( ((UISocketTimedCommand)socketElement).getTtc(indices) )));
								
							} 
							else {
								
								Map<String, Long> rangeMap = ((UISocketTimedCommand)socketElement).getRangeOfTtc(indices);
								
								if ( rangeMap != null ) {
									
									for ( String dim : rangeMap.keySet() ) {
										returnList.add(getValueMap( elementPath+dim+"[ttc]", "cmd", String.valueOf(rangeMap.get(dim))));
										
									}
								}
							}
						}
						
					} else {
						
						if ( indices.endsWith("[state]") )
							indices = indices.substring(0, indices.indexOf("[state]") );
							
						if ( socketElement.isDimensional() ) {
							
							if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
								returnList.add(getValueMap( elementPath+"[state]", "cmd", ((UISocketTimedCommand)socketElement).getState(indices)));
								
							} 
							else {
								
								Map<String, String> rangeMap = ((UISocketTimedCommand)socketElement).getRangeOfState(indices);
								
								if ( rangeMap != null ) {
									
									for ( String dim : rangeMap.keySet() ) {
										returnList.add(getValueMap( elementPath+dim+"[state]", "cmd", rangeMap.get(dim)));
										
									}
								}
							}
						}
					}
					
				}
			}
			
			if ( (returnList.size() != 0) && includeParams ) 			
				addComaandParams( (UISocketCommand)socketElement, elementPath, indices, returnList );
			
			return returnList;
			
		} else if ( socketElement instanceof UISocketCommandParam ) { // For Command Param
			
			
			if ( indices != null )
				return null;
			
			if ( ((UISocketCommandParam)socketElement).getId() == null )
				return null;
			
			Object value = ((UISocketCommandParam)socketElement).getValue();
			
			if(value != null)// Parikshit Thakur : added condition to check if value is null.
				returnList.add(getValueMap( elementPath, "var", value.toString()));
			
			return returnList;
			
			
		} else if ( socketElement instanceof UISocketNotification ) { // For Notification
			
			
			if ( (indices == null) || indices.equals("[state]") ) {
				
				if ( !socketElement.isDimensional() ) {
				
					Map<String, String> valueMap = new HashMap<String, String>();
					valueMap.put("path", elementPath+"[state]");
					valueMap.put("value", ((UISocketNotification)socketElement).getState() );
					valueMap.put("socketEltType", "ntf");
					valueMap.put("notifyCat", ((UISocketNotification)socketElement).getCategory());
					returnList.add(valueMap);
					//returnList.add(getValueMapFromSocketElement(socketElement, elementPath+"[state]", "ntf",((UISocketNotification)socketElement).getType() ));
					
					//return returnList;
					
				}
				
			} else {
				
				if ( indices.endsWith("[state]") )
					indices = indices.substring(0, indices.indexOf("[state]") );
				
				if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
					
					Map<String, String> valueMap = new HashMap<String, String>();
					valueMap.put("path", elementPath+indices+"[state]");
					valueMap.put("value", ((UISocketNotification)socketElement).getState(indices) );
					valueMap.put("socketEltType", "ntf");
					valueMap.put("notifyCat", ((UISocketNotification)socketElement).getCategory());
					returnList.add(valueMap);
					//returnList.add(getValueMapFromSocketElement(socketElement, elementPath+indices+"[state]", "ntf",((UISocketNotification)socketElement).getState(indices)));
					
					//return returnList;
					
					
				} else {
					
					
					
					Map<String, String> rangeMap = ((UISocketNotification)socketElement).getRangeOfState(indices);
					
					if ( rangeMap != null ) {
						
						for ( String dim : rangeMap.keySet() ) {
							Map<String, String> valueMap = new HashMap<String, String>();
							valueMap.put("path", elementPath+dim+"[state]");
							valueMap.put("value", rangeMap.get(dim) );
							valueMap.put("socketEltType", "ntf");
							valueMap.put("notifyCat", ((UISocketNotification)socketElement).getCategory());
							returnList.add(valueMap);
							//returnList.add(getValueMapFromSocketElement(socketElement, elementPath+dim+"[state]", "ntf",rangeMap.get(dim)));
						}
					}
					
					
					//return returnList;
					
				}
				
			}
	
			
			
			
			//Parikshit Thakur : 20111013. added to get value of variable or command in a notification.
			Map<String, IUISocketElement> childElementMap = null;
			
			if ( indices == null ) {
				
				if ( !socketElement.isDimensional()  ) {
					
					childElementMap = ((UISocketNotification)socketElement).getAllValues();
				}
				
			} else {
				
				if ( socketElement.isDimensional()  ) {
					
					childElementMap = ((UISocketNotification)socketElement).getAllValues(indices);
				
				}		
			}
			
			if ( childElementMap == null )
				return returnList;
			
			Map<String, String> valueMap = new HashMap<String, String>();
			
			if ( elementPath.indexOf('/') != -1 )
				elementPath = elementPath.substring(0, elementPath.lastIndexOf('/'));
			
			if ( includeSets ) { // get Value of Set recursively
				
				for ( String subPath : childElementMap.keySet() ) {
					IUISocketElement childElement = childElementMap.get(subPath);
					if ( childElement == null )
						continue;
				
					String indices1 = new String();
					if(childElement.getDimType() != null){  // add by pragnesh for getvalue of set1[]/var1[] and set1[]/var1 
						StringTokenizer dimTypeTokens = new StringTokenizer(childElement.getDimType());
						
						while (dimTypeTokens.hasMoreTokens()) {
							indices1 += "[]"; 
					        dimTypeTokens.nextToken();
					    }
					}
					List<Map<String, String>> childValueMapList = null;
					if ( childElement.isDimensional() )
						childValueMapList = getValue(childElement, elementPath+subPath, indices1, includeSets, includeParams);
					else
						childValueMapList = getValue(childElement, elementPath+subPath, null, includeSets, includeParams);
					if ( childValueMapList == null )
						continue;
					
					returnList.addAll(childValueMapList);
					
				}
				
			} else {  // don't get Value of Set recursively
			
				for ( String subPath : childElementMap.keySet() ) {
					
					IUISocketElement childElement = childElementMap.get(subPath);
					
					if ( (childElement == null) || (childElement instanceof UISocketSet) )
						continue;
					
					StringTokenizer dimTypeTokens = new StringTokenizer(childElement.getDimType());
					String indices1 = new String();
					while (dimTypeTokens.hasMoreTokens()) {
						indices1 += "[]"; 
				        dimTypeTokens.nextToken();
				     }					
					List<Map<String, String>> childValueMapList = null;
					
					if ( childElement.isDimensional() )
						childValueMapList = getValue(childElement, elementPath+subPath, indices, includeSets, includeParams);
					else
						childValueMapList = getValue(childElement, elementPath+subPath, null, includeSets, includeParams);
					
					if ( childValueMapList == null )
						continue;
					
					returnList.addAll(childValueMapList);
				}
			}
			
			return returnList;
			
			
		} else if ( socketElement instanceof UISocketSet ) { // For Set
			
			Map<String, IUISocketElement> childElementMap = null;
			
			if ( indices == null ) {
				
				if ( !socketElement.isDimensional()  ) {
					
					childElementMap = ((UISocketSet)socketElement).getAllValues();
				}
				
			} else {
				
				if ( socketElement.isDimensional()  ) {
					
					childElementMap = ((UISocketSet)socketElement).getAllValues(indices);
				
				}		
			}
			
			if ( childElementMap == null )
				return null;
			
			Map<String, String> valueMap = new HashMap<String, String>();
			
			if ( elementPath.indexOf('/') != -1 )
				elementPath = elementPath.substring(0, elementPath.lastIndexOf('/'));
			
			if ( includeSets ) { // get Value of Set recursively
				
				for ( String subPath : childElementMap.keySet() ) {
					IUISocketElement childElement = childElementMap.get(subPath);
					if ( childElement == null )
						continue;
				
					String indices1 = new String();
					if(childElement.getDimType() != null){  // add by pragnesh for getvalue of set1[]/var1[] and set1[]/var1 
						StringTokenizer dimTypeTokens = new StringTokenizer(childElement.getDimType());
						
						while (dimTypeTokens.hasMoreTokens()) {
							indices1 += "[]"; 
					        dimTypeTokens.nextToken();
					    }
					}
					List<Map<String, String>> childValueMapList = null;
					if ( childElement.isDimensional() )
						childValueMapList = getValue(childElement, elementPath+subPath, indices1, includeSets, includeParams);
					else
						childValueMapList = getValue(childElement, elementPath+subPath, null, includeSets, includeParams);
					if ( childValueMapList == null )
						continue;
					
					returnList.addAll(childValueMapList);
					
				}
				
			} else {  // don't get Value of Set recursively
			
				for ( String subPath : childElementMap.keySet() ) {
					
					IUISocketElement childElement = childElementMap.get(subPath);
					
					if ( (childElement == null) || (childElement instanceof UISocketSet) )
						continue;
					
					StringTokenizer dimTypeTokens = new StringTokenizer(childElement.getDimType());
					String indices1 = new String();
					while (dimTypeTokens.hasMoreTokens()) {
						indices1 += "[]"; 
				        dimTypeTokens.nextToken();
				     }					
					List<Map<String, String>> childValueMapList = null;
					
					if ( childElement.isDimensional() )
						childValueMapList = getValue(childElement, elementPath+subPath, indices, includeSets, includeParams);
					else
						childValueMapList = getValue(childElement, elementPath+subPath, null, includeSets, includeParams);
					
					if ( childValueMapList == null )
						continue;
					
					returnList.addAll(childValueMapList);
				}
			}
			
			return returnList;
		}
		
		return null;
	}
	
	
	// Changes to add element attributes in the getValue response. Parikshit Thakur : 20110727
	private void addComaandParams(UISocketCommand socketCommand, String commandPath, String indices, List<Map<String, String>> returnMapList) {
		
		if ( (socketCommand == null) || (commandPath == null) || (returnMapList == null) )
			return;
		
		if ( indices == null ) {
			
			if ( !socketCommand.isDimensional() ) {
				
				Map<String, UISocketCommandParam> paramMap = socketCommand.getCommandParamMap();
				
				if ( paramMap == null )
					return;
				
				for ( String paramName : paramMap.keySet() ) {
					
					UISocketCommandParam commandParam = paramMap.get(paramName);
					
					if ( (commandParam == null) || commandParam.isIdRef() ) 
						continue;
					
					Object value = commandParam.getValue();
					Map<String, String> returnMap = new HashMap<String, String>();
					if ( value == null ){
						returnMap.put("path", commandPath+"/"+paramName);
						returnMap.put("value", null); 
						returnMap.put("socketEltType", "var");
					}else{
						returnMap.put("path", commandPath+"/"+paramName);
						returnMap.put("value", value.toString());
						returnMap.put("socketEltType", "var");
					}
					returnMapList.add(returnMap);
					
				}
			}
			
		} else {
			
			if ( socketCommand.isDimensional() ) {
				
				Map<String, UISocketCommandParam> paramMap = socketCommand.getCommandParamMap(indices);
				
				if ( paramMap == null )
					return;
				
				for ( String paramName : paramMap.keySet() ) {
					
					UISocketCommandParam commandParam = paramMap.get(paramName);
					
					if ( (commandParam == null) || commandParam.isIdRef() ) 
						continue;
					
					Object value = commandParam.getValue();
					Map<String, String> returnMap = new HashMap<String, String>();

					if ( value == null ){
						returnMap.put("path", commandPath+indices+"/"+paramName);
						returnMap.put("value", null);
						returnMap.put("socketEltType", "var");
					}
					else{
						returnMap.put("path", commandPath+indices+"/"+paramName);
						returnMap.put("value", value.toString());
						returnMap.put("socketEltType", "var");
					}
					returnMapList.add(returnMap);
				}
			}
		}	
		
	}
	////////////////////////////////////////////Get Value Ends//////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////Set Value Starts/////////////////////////////////////////////
	/**
	 * Set the value of Specified Socket Element by elementPath.
	 * The element path must be for single socket element.
	 * If value of specified socket element set successfully then returns int value 0(SUCCESSFUL).
	 * If specified elementPath is invalid or such a Socket Element does not exists then returns int value 1(INCORRECT_PATH);
	 * If specified elementPath has invalid dimension value then returns int value 2(INCORRECT_DIM_VALUE).
	 * 
	 * @param elementPath String elementPath a String value of Element Path.
	 * @param value a String
	 * 
	 * @return an int value representing whether the value is set successfully or not.
	 */
	public int setValue(String elementPath, String value) {
		
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return INCORRECT_PATH;
		}
		
		elementPath = elementPath.trim();
		
		if ( elementPath.indexOf("/") == -1 ) // e.g. elementPath = eltId1
			return setValueByElementId(elementPath, value);
		else								  // e.g. elementPath = /set1/eltId1
			return setValueByElementPath(elementPath, value);

	}
	
	private int setValueByElementId(String elementId, String value) {
		
		if ( elementId == null )
			return INCORRECT_PATH;
		
		if ( elementId.indexOf('[') == -1 ) { // elementId is non-dimensional.
			
			IUISocketElement socketElement = getSocketElementByElementId(elementId, true);
			
			if ( socketElement == null )
				return INCORRECT_PATH;
			
			return setValue(socketElement, null, value);
			
		} else if ( validateDimensionalElementId(elementId) ) { // elementId is dimensional.
			
			IUISocketElement socketElement = getSocketElementByElementId(elementId.substring(0, elementId.indexOf('[')), true);
			
			if ( socketElement == null )
				return INCORRECT_PATH;
			
			String indices = elementId.substring( elementId.indexOf('[') );
			
			return setValue(socketElement, indices, value);
			
		} else {
			
			logger.warning("'"+elementId+"' has invalid dimensional value.");
			return INCORRECT_DIM_VALUE;
		}
		
	}
	
	
	private int setValueByElementPath(String elementPath, String value) {
		
		if ( elementPath == null )
			return INCORRECT_PATH;
		
		String indices = null;
		int dimIndex = -1;
		
		if ( (dimIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/')) ) != -1 ) {
			indices = elementPath.substring( dimIndex );
			
			if( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
				
				logger.warning("You can't set the value of more than one dimension at a time.");
				return INCORRECT_DIM_VALUE;
			}
			
			elementPath = elementPath.substring(0, dimIndex);
		}
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		int status = getSocketElementsByElementPath(elementPath, elementMap);
		if ( status != SUCCESSFUL ) {
			return status;
		}
			
		int size = elementMap.size();
		if ( size == 0 ) {
			return INCORRECT_PATH;
		}
		
		if ( size > 1) {
			logger.warning("You can't set the value of more than one dimension at a time.");
			return INCORRECT_DIM_VALUE;
		}
		
		for ( IUISocketElement socketElement : elementMap.values() ) {
			return setValue(socketElement, indices, value);
		}
		
		return INCORRECT_PATH;
	}
	
	
	private int setValue(IUISocketElement socketElement, String indices, String value) {
		if ( socketElement == null ) {
			return INCORRECT_PATH;
		}
		
		if ( socketElement instanceof UISocketVariable ) {
			
			if ( indices == null ) {
				
				try {
					if ( !socketElement.isDimensional() && ((UISocketVariable)socketElement).setValue(value) ) 
						return SUCCESSFUL;
					else
						return INCORRECT_PATH;
				} catch (TAFatalException e) {
					// TODO Auto-generated catch block
					logger.severe(e.getMessage());
					e.printStackTrace();
				}
				
			} else {
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
					
					logger.warning("You can't set the value of more than one dimension at a time.");
					return INCORRECT_DIM_VALUE;
				} 
				try {
					if ( socketElement.isDimensional() && ((UISocketVariable)socketElement).setValue(value, indices) )
						return SUCCESSFUL;
					else
						return INCORRECT_PATH;
				} catch (TAFatalException e) {
					// TODO Auto-generated catch block
					logger.severe(e.getMessage());
					e.printStackTrace();
				}
			}
			
			
		} else if ( socketElement instanceof UISocketCommand ) {
			
			
			if ( socketElement instanceof UISocketVoidCommand ) {
				
				return INCORRECT_PATH;
				
			} else if ( socketElement instanceof UISocketBasicCommand ) {
				
				if ( indices == null ) {
					
					if ( !socketElement.isDimensional() && ((UISocketBasicCommand)socketElement).setState(value) )
						return SUCCESSFUL;
					else
						return INCORRECT_PATH;
					
				} else {
					
					if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
						
						logger.warning("You can't set the value of more than one dimension at a time.");
						return INCORRECT_DIM_VALUE;
					} 
					
					if ( indices.equals("[state]") ) {
						
						if ( !socketElement.isDimensional() && ((UISocketBasicCommand)socketElement).setState(value) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
						
					} else {
						
						if ( indices.endsWith("[state]") )
							indices = indices.substring(0, indices.indexOf("[state]") );
						
						if ( socketElement.isDimensional() && ((UISocketBasicCommand)socketElement).setState(value, indices) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
					}
				}
				
			} else if ( socketElement instanceof UISocketTimedCommand ) {
				
				if ( indices == null ) {
					
					if ( !socketElement.isDimensional() && ((UISocketTimedCommand)socketElement).setState(value) )
						return SUCCESSFUL;
					else
						return INCORRECT_PATH;
					
				} else {
					
					if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
						
						logger.warning("You can't set the value of more than one dimension at a time.");
						return INCORRECT_DIM_VALUE;
					} 
					
					if ( indices.equals("[state]") ) {
						
						if ( !socketElement.isDimensional() && ((UISocketTimedCommand)socketElement).setState(value) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
						
					} else if ( indices.equals("[ttc]") ) {
					
						long ttc = 0;
						
						try {
							ttc = Long.valueOf(value);
						} catch(Exception e) {}
						
						if ( !socketElement.isDimensional() && ((UISocketTimedCommand)socketElement).setTtc(ttc) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
						
					} else if ( indices.endsWith("[state]") ) {
						
						indices = indices.substring(0, indices.indexOf("[state]") );
						
						if ( socketElement.isDimensional() && ((UISocketTimedCommand)socketElement).setState(value, indices) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
						
					} else if ( indices.endsWith("[ttc]") ) {
						
						long ttc = 0;
						
						try {
							ttc = Long.valueOf(value);
						} catch(Exception e) {}
						
						indices = indices.substring(0, indices.indexOf("[ttc]") );
						
						if ( socketElement.isDimensional() && ((UISocketTimedCommand)socketElement).setTtc(ttc, indices) )
							return SUCCESSFUL;
						else
							return INCORRECT_PATH;
					}
				}
			}
			
			
		} else if ( socketElement instanceof UISocketCommandParam ) {
			
			if ( indices != null )
				return INCORRECT_PATH;
			
			if ( ((UISocketCommandParam)socketElement).getId() == null )
				return INCORRECT_PATH;
			
			if ( ((UISocketCommandParam)socketElement).setValue(value) )
				return SUCCESSFUL;
			else
				return INCORRECT_PATH;
			
		} else if ( socketElement instanceof UISocketNotification ) {
			
			if ( indices == null ) {
				
				if ( ((UISocketNotification)socketElement).setState(value) )
					return SUCCESSFUL;
				else
					return INCORRECT_PATH;
				
			} else {
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
					
					logger.warning("You can't set the value of more than one dimension at a time.");
					return INCORRECT_DIM_VALUE;
				} 
				
				if ( ((UISocketNotification)socketElement).setState(value, indices) )
					return SUCCESSFUL;
				else
					return INCORRECT_PATH;
			}
			
		} else if ( socketElement instanceof UISocketSet ) {
			System.out.println("-------------------------In session setValue UISocketSet");
			return SUCCESSFUL;
		}
		
		return INCORRECT_PATH;
	}
	////////////////////////////////////////////Set Value Ends//////////////////////////////////////////////
	
	
	 
	//////////////////////////////////////Adding new Dimension Starts////////////////////////////////////////
	/**
	 * Add new Dimension to the Dimensional IUISocketElement specified by 'elementPath'.
	 * If dimension is added successfully then returns int value 0(SUCCESSFUL).
	 * If specified elementPath is invalid or such a Socket Element does not exists then returns int value 1(INCORRECT_PATH);
	 * If specified elementPath has invalid dimension value then returns int value 2(INCORRECT_DIM_VALUE).
	 * If the dimension specified by elementPath is already exists then return int value 3(DIMENSION_ALREADY_EXISTS).
	 * 
	 * @param elementPath a String value of elementPath
	 * @param value a String value
	 * 
	 * @return whether Dimension has been added successfully or not.
	 */
	public int addDimension( String elementPath, String value ) {
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return INCORRECT_PATH;
		}
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			logger.warning("'"+elementPath+"' is invalid elementPath.");
			return INCORRECT_PATH;
		}
		System.out.println("Session : addDimension : elementPath : "+elementPath);
		if ( elementPath.indexOf('[', elementPath.lastIndexOf('/') ) == -1 ) {
		//comment by pragnesh after complete open this
			//logger.warning("Dimension of last elementId is not mentioned for the Element Path '"+elementPath+"'.");
			//return INCORRECT_PATH;
		}
		
		elementPath = elementPath.substring(1);
		if ( elementPath.indexOf("/") == -1 ) { // e.g. elementPath = /id or /id[index]
			
			if ( validateDimensionalElementId(elementPath) ) {
				
				String indices = elementPath.substring( elementPath.indexOf("[") );
				String elementId = elementPath.substring(0, elementPath.indexOf("[") );				
				
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				if (socketElement == null) {
					logger.warning("'"+elementId+"' is not exists.");
					return INCORRECT_PATH;
				}
				/*
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				*/
				if ( !socketElement.addDimension(indices) ) {
					logger.warning("Unable to create dimension '"+indices+"' of '"+elementId+"'.");
					return DIMENSION_ALREADY_EXISTS;
				}
				return setValue(socketElement, indices, value);
				
			} else {
				
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}
			
			
		} else { // e.g. elementPath = /set1/.../id[index1] or /set1[index]/.../id[index1]
			String elementId = elementPath.substring(0, elementPath.indexOf("/") );
			
			if ( elementId.indexOf('[') == -1 ) { // e.g. elementPath = /set1/.../id[index1]
					
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement.isDimensional() ) {
					logger.warning("'"+elementId+"' is not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap();
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				return addDimension(elementPath, elementMap, value);
				
			} else if ( validateDimensionalElementId(elementId) ) {	// e.g. elementPath = /set1[index]/.../id[index1]
				String indices = elementId.substring( elementId.indexOf('[') );
				elementId = elementId.substring(0, elementId.indexOf('[') );	
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap(indices);
				
				if ( elementMap == null ) { // If indices is not exists then create new indices.
					
					if ( !socketElement.addDimension(indices) ) {
						logger.warning("Unable to create dimension '"+indices+"' of '"+elementId+"'.");
						return INCORRECT_DIM_VALUE;
					}
					
					elementMap = ((UISocketSet)socketElement).getElementMap(indices);
					
					if ( elementMap == null ) {
						logger.warning("Unable to get Element Map of The Set '"+elementId+"'.");
						return INCORRECT_PATH;
					}
					
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				return addDimension(elementPath, elementMap, value);
				
			} else {
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}
			
		}
		
		
	}
	
	
	
	/**
	 * Add new Dimension to the Dimensional IUISocketElement specified by 'elementPath'.
	 * 
	 * @param elementPath a String value of elementPath
	 * @param elementMap a Map&lt;String, IUISocketElement&gt; of elementMap
	 * @return whether Dimension has been added or not
	 */
	private int addDimension(String elementPath, Map<String, IUISocketElement> elementMap, String value){
		logger.info("Element Path : "+elementPath);
		
		if ( (elementPath == null) || (elementMap == null) ) {
			logger.warning("Input values are invalid.");
			return INCORRECT_PATH;
		}
		
		if ( elementPath.indexOf('/') == -1 ) { // e.g. elementPath = id or id[index]
			
			
			/*if ( elementPath.indexOf('[') == -1 ) { // e.g. elementPath = id[index]
				logger.warning("Dimension of last elementId is not mentioned.");
				return INCORRECT_DIM_VALUE; 
			}*/
			String indices=null;
			String elementId =elementPath;
			if(elementPath.indexOf('[') != -1 ){
				indices = elementPath.substring( elementPath.indexOf('[') );
				elementId = elementPath.substring(0, elementPath.indexOf('[') );
			}
			IUISocketElement socketElement = elementMap.get(elementId);
			if ( socketElement == null || !socketElement.isDimensional() ) {
				logger.warning("SocketElement is null or it is non-dimensional.");
				//return INCORRECT_PATH;
			}
			if ( indices != null && !indices.equals("") && !socketElement.addDimension(indices) ) {
				logger.warning("Unable to create dimension '"+indices+"' of '"+elementId+"'.");
				return DIMENSION_ALREADY_EXISTS;
			}
			return setValue(socketElement, indices, value);
			
			
		} else {  // e.g. elementPath = set2/.../id or set2[index]/.../id
			
			String elementId = elementPath.substring(0, elementPath.indexOf("/") );
			
			if ( elementId.indexOf('[') == -1 ) { // e.g. elementPath = set2/.../id[index1]
					
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement.isDimensional() ) {
					logger.warning("'"+elementId+"' is not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> newElementMap = ((UISocketSet)socketElement).getElementMap();
				
				if ( newElementMap == null ) {
					logger.warning("Unable to get Element Map of The Set '"+elementId+"'.");
					return INCORRECT_PATH;
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return addDimension(elementPath, newElementMap, value);
				
			} else if ( validateDimensionalElementId(elementId) ) {	// e.g. elementPath = set2[index]/.../id[index1]
				
				String indices = elementId.substring( elementId.indexOf("[") );
				elementId = elementId.substring(0, elementId.indexOf("[") );			
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> newElementMap = ((UISocketSet)socketElement).getElementMap(indices);
				
				if ( newElementMap == null ) { // If indices is not exists then create new indices.
					
					if ( !socketElement.addDimension(indices) ) {
						logger.warning("Unable to create dimension '"+indices+"' of '"+elementId+"'.");
						return INCORRECT_DIM_VALUE;
					}
					
					newElementMap = ((UISocketSet)socketElement).getElementMap(indices);
					
					if ( newElementMap == null ) {
						logger.warning("Unable to get Element Map of The Set '"+elementId+"'.");
						return INCORRECT_PATH;
					}
					
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return addDimension(elementPath, newElementMap, value);
				
			} else {
				
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}	
			
		}	
		
	}
	//////////////////////////////////////Adding new Dimension Ends////////////////////////////////////////
	
	
	
	
	///////////////////////////////////Remove existing Dimension Starts//////////////////////////////////////
	/**
	 * Remove the existing Dimension of the Dimensional IUISocketElement specified by 'elementPath'.
	 * If dimension is removed successfully then returns int value 0(SUCCESSFUL).
	 * If specified elementPath is invalid or such a Socket Element does not exists then returns int value 1(INCORRECT_PATH);
	 * If specified elementPath has invalid dimension value then returns int value 2(INCORRECT_DIM_VALUE).
	 * 
	 * @param elementPath a String value of elementPath
	 * @return whether Dimension is removed successfully or not
	 */
	public int removeDimension(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return INCORRECT_PATH;
		}
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			logger.warning("'"+elementPath+"' is invalid elementPath.");
			return INCORRECT_PATH;
		}
		
		if ( elementPath.indexOf('[', elementPath.lastIndexOf('/') ) == -1 ) {
		
			logger.warning("Dimension of last elementId is not mentioned for the Element Path '"+elementPath+"'.");
			return INCORRECT_PATH;
		}
		
		elementPath = elementPath.substring(1);
		
		if ( elementPath.indexOf("/") == -1 ) { // e.g. elementPath = /id or /id[index]
			
			
			if ( validateDimensionalElementId(elementPath) ) {
				
				String indices = elementPath.substring( elementPath.indexOf("[") );
				String elementId = elementPath.substring(0, elementPath.indexOf("[") );					
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				
				if (socketElement == null) {
					logger.warning("'"+elementId+"' is not exists.");
					return INCORRECT_PATH;
				}
				
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				if ( !socketElement.removeDimension(indices) ) {
					logger.warning("Unable to remove existing dimension '"+indices+"' of '"+elementId+"'.");
					return INCORRECT_DIM_VALUE;
				}
				
				return SUCCESSFUL;
				
			} else {
				
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}
			
			
		} else { // e.g. elementPath = /set1/.../id[index1] or /set1[index]/.../id[index1]
			
			String elementId = elementPath.substring(0, elementPath.indexOf("/") );
			
			if ( elementId.indexOf('[') == -1 ) { // e.g. elementPath = /set1/.../id[index1]
					
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement.isDimensional() ) {
					logger.warning("'"+elementId+"' is not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap();
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return removeDimension(elementPath, elementMap);
				
			} else if ( validateDimensionalElementId(elementId) ) {	// e.g. elementPath = /set1[index]/.../id[index1]
				
				String indices = elementId.substring( elementId.indexOf("[") );
				elementId = elementId.substring(0, elementId.indexOf("[") );					
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap(indices);
				
				if ( elementMap == null ) { // If indices is not exists then create new indices.
					
					logger.warning("Dimension '"+indices+"' of '"+elementId+"' is not exists.");
					return INCORRECT_DIM_VALUE;
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return removeDimension(elementPath, elementMap);
				
			} else {
				
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}
			
		}
	}
	
	
	private int removeDimension(String elementPath, Map<String, IUISocketElement> elementMap){
		
		if ( (elementPath == null) || (elementMap == null) ) {
			logger.warning("Input values are invalid.");
			return INCORRECT_PATH;
		}
		
		if ( elementPath.indexOf('/') == -1 ) { // e.g. elementPath = id or id[index]
			
			
			if ( elementPath.indexOf('[') == -1 ) { // e.g. elementPath = id[index]
				logger.warning("Dimension of last elementId is not mentioned.");
				return INCORRECT_DIM_VALUE;
			}
			
			String indices = elementPath.substring( elementPath.indexOf('[') );
			String elementId = elementPath.substring(0, elementPath.indexOf('[') );		
			
			IUISocketElement socketElement = elementMap.get(elementId);
			
			if ( socketElement == null || !socketElement.isDimensional() ) {
				logger.warning("SocketElement is null or it is non-dimensional.");
				return INCORRECT_PATH;
			}
			
			if ( !socketElement.removeDimension(indices) ) {
				logger.warning("Unable to remove existing dimension '"+indices+"' of '"+elementId+"'.");
				return INCORRECT_DIM_VALUE;
			}
			
			return SUCCESSFUL;
			
			
		} else {  // e.g. elementPath = set2/.../id or set2[index]/.../id
			
			String elementId = elementPath.substring(0, elementPath.indexOf("/") );
			
			if ( elementId.indexOf('[') == -1 ) { // e.g. elementPath = set2/.../id[index1]
					
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement.isDimensional() ) {
					logger.warning("'"+elementId+"' is not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> newElementMap = ((UISocketSet)socketElement).getElementMap();
				
				if ( newElementMap == null ) {
					logger.warning("Unable to get Element Map of The Set '"+elementId+"'.");
					return INCORRECT_PATH;
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return removeDimension(elementPath, newElementMap);
				
			} else if ( validateDimensionalElementId(elementId) ) {	// e.g. elementPath = set2[index]/.../id[index1]
				
				String indices = elementId.substring( elementId.indexOf("[") );
				elementId = elementId.substring(0, elementId.indexOf("[") );					
				
				if ( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {					
					logger.warning("Element Path '"+elementPath+"' is not for single dimension.");
					return INCORRECT_DIM_VALUE;
				}
				
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( (socketElement == null) || !(socketElement instanceof UISocketSet) ) {
					logger.warning("'"+elementId+"' is not exists or it is not a set.");
					return INCORRECT_PATH;
				}
				
				if ( !socketElement.isDimensional() ) {		
					logger.warning("'"+elementId+"' is invalid or not dimensional.");
					return INCORRECT_DIM_VALUE;
				}
				
				Map<String, IUISocketElement> newElementMap = ((UISocketSet)socketElement).getElementMap(indices);
				
				if ( newElementMap == null ) { // If indices is not exists then create new indices.
					
					logger.warning("Dimension '"+indices+"' of '"+elementId+"' is not exists.");
					return INCORRECT_DIM_VALUE;
				}
				
				elementPath = elementPath.substring( elementPath.indexOf("/")+1 );
				
				return removeDimension(elementPath, newElementMap);
				
			} else {
				
				logger.warning("'"+elementPath+"' is invalid.");
				return INCORRECT_DIM_VALUE;
			}	
			
		}	
		
	}
	////////////////////////////////////Remove existing Dimension Ends///////////////////////////////////////
	
	
	////////////////////////////////////////Get Element Type Starts/////////////////////////////////////////
	public int getElementType(String elementPath) {
		
		if ( elementPath == null )
			return -1;
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			
			if ( elementPath.indexOf('/') != -1 ) { 
				logger.warning("Element Path '"+elementPath+"' is invalid.");
				return -1;
			}
			
			elementPath = getElementPath(elementPath);
		}
		
		if ( elementPath == null )
			return -1;
		
		String indices = null;
		int dimIndex = -1;
		
		if ( (dimIndex = elementPath.indexOf('[', elementPath.lastIndexOf('/')) ) != -1 ) {
			indices = elementPath.substring( dimIndex );
			
			if( (indices.indexOf(' ') != -1) || (indices.indexOf("[]") != -1) ) {
				
				logger.warning("You can't set the value of more than one dimension at a time.");
				return -1;
			}
			
			elementPath = elementPath.substring(0, dimIndex);
		}
		
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		int status = getSocketElementsByElementPath(elementPath, elementMap);
		
		if ( status != SUCCESSFUL ) {
			return -1;
		}
			
		int size = elementMap.size();
		
		if ( size == 0 ) {
			return -1;
		}
		
		IUISocketElement element = null;
		
		for ( IUISocketElement socketElement : elementMap.values() ) {
			element = socketElement;
			break;
		}
		
		if ( element == null ) {
			logger.warning("Element specified by '"+elementPath+"' is not exists.");
			return -1;
		}
		
		if ( element instanceof UISocketSet )
			return 0;
		else if ( element instanceof UISocketVariable )
			return 1;
		else if ( element instanceof UISocketCommand ) {
		
			if ( element instanceof UISocketVoidCommand )
				return 2;
			else if( element instanceof UISocketBasicCommand )
				return 3;
			else if( element instanceof UISocketTimedCommand )
				return 4;
			else
				return -1;
			
		} else if ( element instanceof UISocketCommandParam )
			return 5;
		 else if ( element instanceof UISocketNotification )
			return 6;
		else 
			return -1;
	}
	/*
	public int getElementType(String elementPath) {
		
		if ( elementPath == null )
			return -1;
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			
			if ( elementPath.indexOf('/') != -1 ) { 
				logger.warning("Element Path '"+elementPath+"' is invalid.");
				return -1;
			}
			
			elementPath = getElementPath(elementPath);
		}
				
		List<IUISocketElement> elementList = getElements(elementPath);
		
		if ( (elementList == null) || (elementList.size() == 0) ) {
			logger.warning("Element specified by '"+elementPath+"' is not exists.");
			return -1;
		}
		
		if ( elementList.size() != 1 ) {
			logger.warning("Element specified by '"+elementPath+"' doesn't specify a single Element.");
			return -1;
		}
		
		IUISocketElement element = elementList.get(0);
		
		if ( element == null ) {
			logger.warning("Element specified by '"+elementPath+"' is not exists.");
			return -1;
		}
		
		if ( element instanceof UISocketSet )
			return 0;
		else if ( element instanceof UISocketVariable )
			return 1;
		else if ( element instanceof UISocketCommand ) {
		
			if ( element instanceof UISocketVoidCommand )
				return 2;
			else if( element instanceof UISocketBasicCommand )
				return 3;
			else if( element instanceof UISocketTimedCommand )
				return 4;
			else
				return -1;
			
		} else if ( element instanceof UISocketCommandParam )
			return 5;
		 else if ( element instanceof UISocketNotification )
			return 6;
		else 
			return -1;
		
	}
	*/
	/////////////////////////////////////////Get Element Type Ends//////////////////////////////////////////
	
	
	////////////////////////////////////Get Command Params Starts////////////////////////////////////////////
	/**
	 * Get Parameters and their values of specified Command.
	 * 
	 * @param elementPath a String value of Element Path
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	public Map<String, String> getCommandParams(String elementPath) {
		
		if ( elementPath == null )
			return null;
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			
			if ( elementPath.indexOf('/') != -1 ) { 
				logger.warning("Element Path '"+elementPath+"' is invalid.");
				return null;
			}
			
			elementPath = getElementPath(elementPath);
		}
				
		List<IUISocketElement> elementList = getElements(elementPath);
		
		if ( (elementList == null) || (elementList.size() == 0) ) {
			logger.warning("Element specified by '"+elementPath+"' is not exists.");
			return null;
		}
		
		if ( elementList.size() != 1 ) {
			logger.warning("Element specified by '"+elementPath+"' doesn't specify a single Element.");
			return null;
		}
		
		IUISocketElement element = elementList.get(0);
		
		if ( element == null ) {
			logger.warning("Element specified by '"+elementPath+"' is not exists.");
			return null;
		}
		
		if ( !(element instanceof UISocketCommand) ) {
			logger.warning("Element specified by '"+elementPath+"' is not a command.");
			return null;
		}
		
		UISocketCommand command = (UISocketCommand)element;
		
		if ( command.isDimensional() ) {
			
			if ( elementPath.indexOf('[', elementPath.lastIndexOf('/')) == -1 ) {
				logger.warning("Dimension doesn't specify in the Element Path '"+elementPath+"'.");
				return null;
			}
			
			Map<String, UISocketCommandParam> paramMap = command.getCommandParamMap( elementPath.substring( elementPath.indexOf('[', elementPath.lastIndexOf('/')) ) );
			
			if ( paramMap == null ) {
				logger.info("'"+elementPath+"' has no Command Parameters.");
				return new HashMap<String, String>();
			}
			
			return getParamValues(paramMap);
			
		} else {
			
			if ( elementPath.indexOf('[', elementPath.lastIndexOf('/')) != -1 ) {
				logger.warning("Element '"+elementPath+"' is not dimensional.");
				return null;
			}
			
			Map<String, UISocketCommandParam> paramMap = command.getCommandParamMap();
			
			if ( paramMap == null ) {
				logger.info("'"+elementPath+"' has no Command Parameters.");
				return new HashMap<String, String>();
			}
			
			return getParamValues(paramMap);
			
		}
		
	}
	
	private Map<String, String> getParamValues(Map<String, UISocketCommandParam> paramMap) {
		
		if ( paramMap == null )
			return null;
		
		Map<String, String> returnMap = new TreeMap<String, String>();
		
		for ( String paramName : paramMap.keySet() ) {
			
			if ( paramName == null )
				continue;
			
			UISocketCommandParam commandParam = paramMap.get(paramName);
			
			if ( commandParam == null )
				continue;
			
			Object value = commandParam.getValue();
			
			if ( value == null )
				returnMap.put( paramName, null);
			else
				returnMap.put( paramName, value.toString());
		}
		
		return returnMap;
	}
	/////////////////////////////////////Get Command Params Ends/////////////////////////////////////////////
	
	
	
	
	
	
	///////////////////////////////////////Get Indices Starts////////////////////////////////////////////////
	/*public Set<String> getIndices(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("Element Id is null.");
			return null;
		}
		
		
		 * comment by Pragnesh Patel on 20110124
		 * for auto index value by using elementpath 	
		 
		int indexNo = 0;
		List<String> pathDims = new ArrayList<String>();
		if(elementPath!=null){
			
			
			String tempElementPath = elementPath.substring(elementPath.lastIndexOf("/")+1,elementPath.length() );
			
			while(!(tempElementPath.equals(""))){
				pathDims.add(tempElementPath.substring(tempElementPath.indexOf("[") + 1 , tempElementPath.indexOf("]")));
				tempElementPath = tempElementPath.substring(tempElementPath.indexOf("]") + 1);
			}	
			
			
			String findIndexElementPath = elementPath;
			
			while(findIndexElementPath.contains("/")){
				findIndexElementPath = findIndexElementPath.substring(findIndexElementPath.indexOf("/")+1,findIndexElementPath.length() );
			}
			while(findIndexElementPath.contains("[_]")){
				indexNo++; 
				findIndexElementPath = findIndexElementPath.substring(findIndexElementPath.indexOf("[_]")+1);
			}
			//indexNo = --indexNo;
		}
		
		 *//** comment by Pragnesh Patel on 20110124
		 * for getIndices by element path *//*	
		 
		//String elementPath = getElementPath(elementId);

		
		if ( elementPath == null ) {
			logger.warning("Unable to get Element Path of the elementPath '"+elementPath+"'.");
			return null;
		}
		
		String indicePath =  elementPath.substring(elementPath.lastIndexOf("/"), elementPath.length()) ;
		indicePath =  indicePath.substring(indicePath.indexOf("["), indicePath.indexOf("[_]")) ;
		if( indicePath == null || indicePath.length() < 1){
			indicePath = "[]";
		}
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		Set<String> returnSet = new LinkedHashSet<String>();

		if ( getSocketElementsByElementPath(elementPath, elementMap) != SUCCESSFUL ) {
			logger.warning("Unable to get IUISocketElement of Element Path  '"+SUCCESSFUL+"'.");
		}else{
				for ( String path1 : elementMap.keySet() ) {
					IUISocketElement socketElement1 = elementMap.get(path1);
					 
					 * 2011-02-03 Added by Pragnesh Patel 
					 * For Get Multi Dimension Value by Element Path
					 
					//Object value = ((UISocketVariable)socketElement1).getValue("[]");

					Object value = new Object();
					if ( (socketElement1 instanceof UISocketCommand) ) {
						value = ((UISocketCommand)socketElement1).getValue(indicePath);
					}
					if( (socketElement1 instanceof UISocketVariable) ){
						//value = ((UISocketVariable)socketElement1).getValue(indicePath);
							value = ((UISocketVariable)socketElement1).getValue(indicePath);
							String indices =  indicePath; 
							if ( socketElement1.isDimensional() ) {
								
								Map<String, String> valueMap = new HashMap<String, String>();
								
								if ( (indices.indexOf(' ') != -1) && (indices.indexOf("[]") != -1) ) {
									
									value = ((UISocketVariable)socketElement1).getValue(indices);
									if ( value == null )
										valueMap.put(elementPath+indices, null);
									else
										valueMap.put(elementPath+indices, value.toString());
									
								} else {
									Map<String, Object> rangeMap = ((UISocketVariable)socketElement1).getRangeOfValues(indices);
									if ( rangeMap != null ) {
										String indexToAdd = null;
										getIndex((LinkedHashMap)rangeMap, pathDims, 0, returnSet, indexToAdd);
										for ( String dim : rangeMap.keySet() ) {
											value = dim;
											if ( value != null ){
												String dimValue = value.toString();
												dimValue = dimValue.substring(dimValue.indexOf("[")+1,dimValue.indexOf("]"));
												returnSet.add(dimValue);
											}
										}		
									}
									
								}
								return returnSet;
							}
						

					}
					if( (socketElement1 instanceof UISocketSet) ){
						//returnSet = ((UISocketSet)socketElement1).getIndices(0);
						try{
						value = ((UISocketSet)socketElement1).getValue(indicePath);
						}catch(Exception e){
							e.printStackTrace();
						}
						//returnSet = value;
					}
					//Object value = ((UISocketVariable)socketElement1).getValue(indicePath);
					if( (value instanceof LinkedHashMap) ){
						LinkedHashMap<String, Object> dimMap = (LinkedHashMap)value;
						for ( String indices : dimMap.keySet() ) {
							indices = indices.substring(indices.indexOf("[")+1,indices.indexOf("]"));
							returnSet.add(indices);
						}
					}
					
			}
			
		}
		return returnSet;

	}*/
	
	/**
	 * Returns the set of indices for the requested element path.
	 * 
	 * @param elementPath String value of elementPath.
	 * @return Set of indices. Set will contain error msg if some error is encountered during the processing of elementPath
	 * 			with error message "Error message : Current code supports comparison of numeric indices only".			
	 */
	// Parikshit Thakur : 20110929. Modified method according to the changes in ITA.getIndices method and implemented other remaining scenarios mentioned in UCH spec.
	public Set<String> getIndices(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("Element Id is null.");
			return null;
		}
		List<String> pathDims = new ArrayList<String>();
		
		String tempElementPath = elementPath.substring(elementPath.lastIndexOf("/")+1,elementPath.length() );
		
		while(!(tempElementPath.equals(""))){
			String dim = tempElementPath.substring(tempElementPath.indexOf("[") + 1 , tempElementPath.indexOf("]"));
			if(dim.equals("")){
				dim = " ";
			}
			pathDims.add(dim);
			tempElementPath = tempElementPath.substring(tempElementPath.indexOf("]") + 1);
		}	
				
		
		Map<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
		
		if ( getSocketElementsByElementPath(elementPath, elementMap) != SUCCESSFUL ) {
			logger.warning("Unable to get IUISocketElement of Element Path  '"+SUCCESSFUL+"'.");
		}
		
		
		Set<String> returnSet = new LinkedHashSet<String>();
		
		for ( IUISocketElement socketElement : elementMap.values() ) {
				
			LinkedHashMap<String, Object> elementDimMap = socketElement.getDimMap();
			String indexToAdd = null;
			addIndex(elementDimMap, pathDims, 0, returnSet, indexToAdd);
				
		}
		
		
		return returnSet;
	}
	
	
	
	/**
	 * Adds index to the returnSet.
	 * 
	 * @param dimMap an object of LinkedHashMap having dimMap for socketElement.
	 * @param pathDims List of dimensions in the requested path.
	 * @param currentIndex int value of index no being processed
	 * @param returnSet set of indices.
	 * @param indexToAdd stored value of a index to be added to returnSet.
	 */
	private void addIndex(LinkedHashMap<String, Object> dimMap, List<String> pathDims, int currentIndex, Set<String> returnSet, String indexToAdd ){
		for (String index : dimMap.keySet()) {

			index = index.substring(index.indexOf("[") + 1, index.indexOf("]"));

			if (index.equals(pathDims.get(currentIndex))) {
				if (pathDims.size() != currentIndex + 1) {
					addIndex((LinkedHashMap) dimMap.get("[" + index + "]"),
							pathDims, currentIndex + 1, returnSet, indexToAdd);
				} else {
					returnSet.add(indexToAdd);
				}

			} else if (pathDims.get(currentIndex).equals("_")) {
				indexToAdd = index;
				if (pathDims.size() != currentIndex + 1) {
					addIndex((LinkedHashMap) dimMap.get("[" + index + "]"),
							pathDims, currentIndex + 1, returnSet, indexToAdd);
				} else {
					returnSet.add(indexToAdd);
				}
			} else if (pathDims.get(currentIndex).indexOf(" ") != -1) {
				
				if(pathDims.get(currentIndex).equals(" ")){
					if (pathDims.size() != currentIndex + 1) {
						addIndex((LinkedHashMap) dimMap.get("[" + index + "]"),
								pathDims, currentIndex + 1, returnSet, indexToAdd);
					} else {
						returnSet.add(indexToAdd);
					}
				}
				else {
					String startIndex = pathDims.get(currentIndex).substring(0, pathDims.get(currentIndex).indexOf(" ")).trim();
					String endIndex = pathDims.get(currentIndex).substring(pathDims.get(currentIndex).lastIndexOf(" ")).trim();
					
					 if (startIndex.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)")) {  
				            //System.out.println("Is a number");  
						 if ( Integer.parseInt(index) >= Integer.parseInt(startIndex) && Integer.parseInt(index) <= Integer.parseInt(endIndex) ){
								if (pathDims.size() != currentIndex + 1) {
									addIndex((LinkedHashMap) dimMap.get("[" + index + "]"),
											pathDims, currentIndex + 1, returnSet, indexToAdd);
								} else {
									returnSet.add(indexToAdd);
								}
						}
				     } 
					 else { 
						   // System.out.println("Is not a number"); 
						 returnSet.add("Error message : Current code supports comparison of numeric indices only");
				     }  
					
					
				}
				

			}

		}
		
	}
	
	
	
	/**
	 * Get the List of IUISocketElements for the specified path.
	 * If the SocketElement is child or grant child or so on of a Dimensional set then get the UISocketElement from all Sets.
	 * 
	 * @param elementPath a String value of ElementPath.
	 * 
	 * @return an Object of List&lt;IUISocketElement&gt;
	 */
	private List<IUISocketElement> getElements(String elementPath) {
		
		if ( elementPath == null ) {
			logger.warning("Element Path is null.");
			return null;
		}
		
		elementPath = elementPath.trim();
		
		if ( !elementPath.startsWith("/") ) {
			logger.warning("'"+elementPath+"' Element path must start with '/'.");
			return null;
		}
		
		elementPath = elementPath.substring(1);
		
		if ( elementPath.indexOf('/') == -1) { // e.g. elementPath = /id
			
			List<IUISocketElement> returnList = new ArrayList<IUISocketElement>();
			
			IUISocketElement socketElement = getSocketElementFromList(elementPath, elements);
			
			if ( socketElement != null )
				returnList.add(socketElement);
			
			return returnList;
			
		} else { // e.g. elementPath = /set1/.../id 
			
			String elementId = elementPath.substring(0, elementPath.indexOf('/') );
			
			List<IUISocketElement> returnList = new ArrayList<IUISocketElement>();
			
			IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
			
			if ( (socketElement != null) && (socketElement instanceof UISocketSet) ) {
				
				if ( socketElement.isDimensional() ) {
					
					Map<String, Map<String, IUISocketElement>> elementMaps = ((UISocketSet)socketElement).getElementMaps();
					
					if ( elementMaps != null ) {
						
						String newElementPath = elementPath.substring(elementPath.indexOf('/')+1);
						
						for ( Map<String, IUISocketElement> elementMap : elementMaps.values() ) 
							getElements(newElementPath, elementMap, returnList);
					
					}
					
				} else {
					
					Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap();
					
					if ( elementMap != null ) 
						getElements(elementPath.substring(elementPath.indexOf('/')+1), elementMap, returnList);
								
				}
			}
			
			return returnList;
		}
		
	}
	
	private void getElements(String elementPath, Map<String, IUISocketElement> elementMap, List<IUISocketElement> returnList) {
		
		if ( (elementPath == null) || (elementMap == null) ) {
			logger.warning("Element Path is null.");
			return;
		}
		
		if ( elementPath.indexOf('/') == -1) { // e.g. elementPath = id
			
			IUISocketElement socketElement = elementMap.get(elementPath);
			
			if ( socketElement != null )
				returnList.add(socketElement);
			
			return;
			
		} else { // e.g. elementPath = set2/.../id 
			
			String elementId = elementPath.substring(0, elementPath.indexOf('/') );
			
			IUISocketElement socketElement = elementMap.get(elementId);
			
			if ( (socketElement != null) && (socketElement instanceof UISocketSet) ) {
				
				if ( socketElement.isDimensional() ) {
					
					Map<String, Map<String, IUISocketElement>> elementMaps = ((UISocketSet)socketElement).getElementMaps();
					
					if ( elementMaps != null ) {
						
						String newElementPath = elementPath.substring(elementPath.indexOf('/')+1);
						
						for ( Map<String, IUISocketElement> newElementMap : elementMaps.values() ) 
							getElements(newElementPath, newElementMap, returnList);			
					}
					
				} else {
					
					Map<String, IUISocketElement> newElementMap = ((UISocketSet)socketElement).getElementMap();
					
					if ( newElementMap != null ) 
						getElements(elementPath.substring(elementPath.indexOf('/')+1), newElementMap, returnList);						
				}
			}
			
		}
		
	}
	////////////////////////////////////////Get Indices Ends/////////////////////////////////////////////////
	
	
	
	
	////////////////////////////////////////Useful Functions Starts//////////////////////////////////////////
	public int getSocketElementsByElementPath(String elementPath, Map<String, IUISocketElement> returnMap) {
		 
		 if ( elementPath == null ) {
			 logger.warning("ElementPath is null.");
			 return INCORRECT_PATH;
		 }
		 
		 elementPath = elementPath.trim();
		 
		 if ( !elementPath.startsWith("/") ) {
			 logger.warning("ElementPath does not start with '/'.");
			 return INCORRECT_PATH;
		 }
		 
		 elementPath = elementPath.substring(1);
		 
		 
		 if ( elementPath.indexOf("/", 1) == -1 ) { // e.g. elementPath = /id or /id[index]
			 
			 
			 if ( elementPath.indexOf("[") != -1 ) //e.g. elementPath = /id[index]
				 elementPath = elementPath.substring(0, elementPath.indexOf("[") );
			 
			 IUISocketElement socketElement = getSocketElementFromList(elementPath, elements);	
				
			 if ( socketElement == null ){
				 logger.warning(elementPath+" is not found");
				 return INCORRECT_PATH;
			 }
				
			 returnMap.put("/"+elementPath, socketElement);
			 return SUCCESSFUL;
			 
		 } else { // e.g. elementPath = /set1/.../id or /set1[index]/.../id
			 
			 
			 String elementId = elementPath.substring(0, elementPath.indexOf("/"));
		
			 if ( elementId.indexOf("[") == -1 ) { // e.g. elementPath = /set1/.../id : set is not dimensional
				 
				 
				 IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				 
				 if ( socketElement == null ) {
					 logger.warning(elementId+" is not found");
					 return INCORRECT_PATH;
				 }
				 
				 if ( socketElement instanceof UISocketSet ) { // Current SocketElement is non-dimensional Set.
					 
					 Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap();
						
					if ( elementMap == null ) {
						logger.warning("Unable to get ElememtMap for the Set '"+elementId+"'.");
						return INCORRECT_PATH;
					}
					
					return findSocketElementFromMap("/"+elementId+"/", elementPath.substring( elementPath.indexOf("/")+1 ), elementMap, returnMap);
					 
				 } else if ( socketElement instanceof UISocketCommand ) { // Current SocketElement is non-dimensional Command.
					 
					 IUISocketElement commandParam = ((UISocketCommand)socketElement).getCommandParam( elementPath.substring( elementPath.indexOf("/")+1 ) );
					 
					 if ( commandParam == null ) {
						 logger.warning("Unable to get Command Parameter '"+elementPath.indexOf("/")+1+"' from the Command '"+elementId+"'.");
						 return INCORRECT_PATH;
					 }
					 
					 returnMap.put("/"+elementPath, commandParam);
					 return SUCCESSFUL;
					 
				 } 
				 //Parikshit Thakur : 20111012. Added code to get variable or command in a notification.
				 else if(socketElement instanceof UISocketNotification){
					 IUISocketElement element = ((UISocketNotification)socketElement).getNotifyElement(elementPath.substring( elementPath.indexOf("/")+1 ));
					 if(element == null){
						 return INCORRECT_PATH;
					 }
					 returnMap.put("/"+elementPath, element);
					 return SUCCESSFUL;
				 }
				 else { // Current SocketElement is not Command or Set.
					 
					 logger.warning("'"+elementId+"' is not Command or Set.");
					 return INCORRECT_PATH;
				 }
				 
				 
			 } else if ( validateDimensionalElementId(elementId) ) { // e.g. elementPath = /set1[index]/.../id
				 
				String indices = elementId.substring( elementId.indexOf("[") );
				elementId = elementId.substring(0, elementId.indexOf("[") );
				
				IUISocketElement socketElement = getSocketElementFromList(elementId, elements);
				 
				if ( socketElement == null || !socketElement.isDimensional() ) {
					logger.warning("'"+elementId+"' is null or not dimensional.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement instanceof UISocketSet ) { // Current SocketElement is Dimensional Set.
					 
					if ( (indices.indexOf(' ') == -1) && (indices.indexOf("[]") == -1) ) { // indices is for single Dimension.
						
						Map<String, IUISocketElement> elementMap = ((UISocketSet)socketElement).getElementMap(indices);
						
						if ( elementMap == null ) {
							logger.warning("Unable to get the elementMap of '"+elementId+indices+"'.");
							return INCORRECT_PATH;
						}
						
						return findSocketElementFromMap("/"+elementId+indices+"/", elementPath.substring( elementPath.indexOf("/")+1 ), elementMap, returnMap);
						
					} else { // indices is for range of dimension
						
						HashMap<String, HashMap<String, IUISocketElement>> elementMaps = ((UISocketSet)socketElement).getRangeOfElementMaps(indices);
						
						if ( elementMaps == null ) {
							logger.warning("Unable to get the elementMaps of '"+elementId+indices+"'.");
							return INCORRECT_DIM_VALUE;
						}
						
						for ( String dim : elementMaps.keySet() ) {
							
							HashMap<String, IUISocketElement> elementMap = elementMaps.get(dim);
							
							if ( elementMap == null )
								continue;
							
							int returnVal = findSocketElementFromMap("/"+elementId+dim+"/", elementPath.substring( elementPath.indexOf("/")+1 ), elementMap, returnMap);
							
							if ( returnVal != SUCCESSFUL ) {
								return returnVal;
							}
							
						}
						
						return SUCCESSFUL;
					}
					
				} else if ( socketElement instanceof UISocketCommand ) { // Current SocketElement is Dimensional Command.
					 
					IUISocketElement commandParam = ((UISocketCommand)socketElement).getCommandParam(indices, elementPath.substring( elementPath.indexOf("/")+1 ) );
					 
					if ( commandParam == null ) {
						 logger.warning("Unable to get Command Parameter '"+elementPath.indexOf("/")+1+"' from the Command '"+elementId+indices+"'.");
						return INCORRECT_PATH;
					}
					
					returnMap.put("/"+elementPath, commandParam);
					return SUCCESSFUL;
					
				}
				//Parikshit Thakur : 20111012. Added to get variable or command in a notification.
				else if(socketElement instanceof UISocketNotification){
					
					if ( (indices.indexOf(' ') == -1) && (indices.indexOf("[]") == -1) ) { // indices is for single Dimension.
						
						Map<String, IUISocketElement> elementMap = ((UISocketNotification)socketElement).getElementMap(indices);
						
						if ( elementMap == null ) {
							logger.warning("Unable to get the elementMap of '"+elementId+indices+"'.");
							return INCORRECT_PATH;
						}
						
						return findSocketElementFromMap("/"+elementId+indices+"/", elementPath.substring( elementPath.indexOf("/")+1 ), elementMap, returnMap);
						
					} else { // indices is for range of dimension
						
						HashMap<String, HashMap<String, IUISocketElement>> elementMaps = ((UISocketNotification)socketElement).getRangeOfElementMaps(indices);
						
						if ( elementMaps == null ) {
							logger.warning("Unable to get the elementMaps of '"+elementId+indices+"'.");
							return INCORRECT_DIM_VALUE;
						}
						
						for ( String dim : elementMaps.keySet() ) {
							
							HashMap<String, IUISocketElement> elementMap = elementMaps.get(dim);
							
							if ( elementMap == null )
								continue;
							
							int returnVal = findSocketElementFromMap("/"+elementId+dim+"/", elementPath.substring( elementPath.indexOf("/")+1 ), elementMap, returnMap);
							
							if ( returnVal != SUCCESSFUL ) {
								return returnVal;
							}
							
						}
						
						return SUCCESSFUL;
					}
					
				}
				else { // Current SocketElement is dimensional but not Command or Set.
					 
					logger.warning("'"+elementId+"' is not Command or Set.");
					return INCORRECT_PATH;
				}
				
			 } else { // e.g. elementPath = /set1[index/.../id or /set1[index1][index2 
				 
				 logger.warning("'"+elementPath+"' is invalid element path.");
				 return INCORRECT_DIM_VALUE;
			 }
			 
		 }
		 
	 }
	
	
	 /**
	 * Finds the IUISocketElement from 'elementMap' specified by 'path' and put in the 'returnMap'.
	 * 
	 * @param prefix a String value of prefix
	 * @param path a String value of path
	 * @param elementMap a HashMap&lt;String, IUISocketElement&gt; of elementMap
	 * @param returnMap a HashMap&lt;String, IUISocketElement&gt; of returnMap
	 */
	private int findSocketElementFromMap(String prefix, String path, Map<String, IUISocketElement> elementMap, Map<String, IUISocketElement> returnMap ) {
	
		if ( prefix == null || path == null || elementMap == null || returnMap == null ) {
			logger.warning("Input values are invalid.");
			return INCORRECT_PATH;
		}
		
		
		
		if ( path.indexOf("/") == -1 ) { //e.g. path = id or id[index]
			
			
			if ( path.indexOf("[") != -1 ) //e.g. path = id[index]	
				path = path.substring(0, path.indexOf("["));
			
			IUISocketElement socketElement = elementMap.get(path);
			
			if ( socketElement == null ) {
				logger.warning("Unable to get '"+path+"' from Element Map.");
				return INCORRECT_PATH;
			}
			
			returnMap.put(prefix+path, socketElement);
			return SUCCESSFUL;
			
			
		} else { //e.g. path = set2/.../id or set2[index]/.../id
			
			
			String elementId = path.substring(0, path.indexOf("/"));
			
			if ( elementId.indexOf("[") == -1 ) { //e.g. path = set2/.../id
				
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( socketElement == null ) {
					logger.warning("Unable to get '"+elementId+"' from Element Map.");
					return INCORRECT_PATH;
				}
				 
				if ( socketElement instanceof UISocketSet ) { // Current SocketElement is non-dimensional Set.
					 
					elementMap = ((UISocketSet)socketElement).getElementMap();
						
					if ( elementMap == null ) {
						logger.warning("Unable to get ElememtMap for the Set '"+elementId+"'.");
						return INCORRECT_PATH;
					}
					
					return findSocketElementFromMap(prefix+elementId+"/", path.substring( path.indexOf("/")+1 ), elementMap, returnMap);
					 			
				} else if ( socketElement instanceof UISocketCommand ) { // Current SocketElement is non-dimensional Command.
					 
					 IUISocketElement commandParam = ((UISocketCommand)socketElement).getCommandParam( path.substring( path.indexOf("/")+1 ) );
					 
					 if ( commandParam == null ) {
						 logger.warning("Unable to get Command Parameter '"+path.indexOf("/")+1+"' from the Command '"+elementId+"'.");
						 return INCORRECT_PATH;
					 }
					 
					 returnMap.put(prefix+path, commandParam);
					 return SUCCESSFUL;								
					
				}
				//Parikshit Thakur : 20111012. Added to get variable or command in a notification.
				else if(socketElement instanceof UISocketNotification){
					IUISocketElement element = ((UISocketNotification)socketElement).getNotifyElement(path.substring( path.indexOf("/")+1 ));
					
					if ( element == null ){
						return INCORRECT_PATH;
					}
					returnMap.put(prefix+path, element);
					return SUCCESSFUL;
						
				}
				else { // Current SocketElement is not Command or Set.
					 
					 logger.warning("'"+elementId+"' is not Command or Set.");
					 return INCORRECT_PATH;
				}
			
				
			} else if ( validateDimensionalElementId(elementId) ) { //e.g. path = set2[index]/.../id
				
				String indices = elementId.substring( elementId.indexOf("[") );
				elementId = elementId.substring(0, elementId.indexOf("["));
				
				IUISocketElement socketElement = elementMap.get(elementId);
				
				if ( socketElement == null ) {
					logger.warning("Unable to get '"+path+"' from Element Map.");
					return INCORRECT_PATH;
				}
				
				if ( socketElement instanceof UISocketSet ) { // Current SocketElement is Dimensional Set.
				
					if ( (indices.indexOf(' ') == -1) && (indices.indexOf("[]") == -1) ) { // indices is for single Dimension.
						
						elementMap = ((UISocketSet)socketElement).getElementMap(indices);
						
						if ( elementMap == null ) {
							logger.warning("Unable to get the elementMap of '"+prefix+elementId+indices+"'.");
							return INCORRECT_PATH;
						}
						
						return findSocketElementFromMap(prefix+elementId+indices+"/", path.substring( path.indexOf("/")+1 ), elementMap, returnMap);
						
					} else { // indices is for range of dimension
						
						HashMap<String, HashMap<String, IUISocketElement>> elementMaps = ((UISocketSet)socketElement).getRangeOfElementMaps(indices);
						
						if ( elementMaps == null ) {
							logger.warning("Unable to get the elementMaps of '"+elementId+indices+"'.");
							return INCORRECT_DIM_VALUE;
						}
						
						for ( String dim : elementMaps.keySet() ) {
							
							elementMap = elementMaps.get(dim);
							
							if ( elementMap == null )
								continue;
							
							int returnVal = findSocketElementFromMap(prefix+elementId+dim+"/", path.substring( path.indexOf("/")+1 ), elementMap, returnMap);
							
							if ( returnVal != SUCCESSFUL ) {
								return returnVal;
							}
							
						}
						
						return SUCCESSFUL;
					}
					
				} else if ( socketElement instanceof UISocketCommand ) { // Current SocketElement is Dimensional Command.
			
					IUISocketElement commandParam = ((UISocketCommand)socketElement).getCommandParam(indices, path.substring( path.indexOf("/")+1 ) );
					 
					if ( commandParam == null ) {
						 logger.warning("Unable to get Command Parameter '"+path.indexOf("/")+1+"' from the Command '"+prefix+elementId+indices+"'.");
						return INCORRECT_PATH;
					}
					
					returnMap.put(prefix+path, commandParam);
					return SUCCESSFUL;
					
				}
				//Parikshit Thakur : 20111012. Added to get variable or command in a notification.
				else if(socketElement instanceof UISocketNotification){
					IUISocketElement element = ((UISocketNotification)socketElement).getNotifyElement(path.substring( path.indexOf("/")+1 ));
					
					if ( element == null ){
						return INCORRECT_PATH;
					}
					returnMap.put(prefix+path, element);
					return SUCCESSFUL;
						
				}
				else { // Current SocketElement is not Command or Set.
					
					logger.warning("'"+elementId+"' is not Command or Set.");
					return INCORRECT_PATH;
				}
				
			} else { //e.g. path = set2[index]/.../id
				
				logger.warning("'"+path+"' is invalid element path.");
				return INCORRECT_DIM_VALUE;	
			}
			
			
		}
		
	}
	
	public String getElementPath(String elementId ) {
		
		if ( elementId == null ) {
			logger.warning("Element Path is null.");
			return null;
		}
		
		if ( elements == null ) {
			logger.warning("List of Socket Elements is null.");
			return null;
		}
		
		for( IUISocketElement  socketElement : elements ) {
			
			if ( elementId.equals( socketElement.getElementId() ) )
				return "/"+elementId;
			
			if ( socketElement instanceof UISocketSet ) {
				
				String path = getElementPath( (UISocketSet)socketElement, elementId);
				
				if ( path != null )
					return path;
				
			}
		}
			
		return null;
	}
	
	private String getElementPath( UISocketSet socketSet, String elementId ) {
		
		if ( elementId == null ) {
			logger.warning("Element Path is null.");
			return null;
		}
		
		if ( socketSet == null ) {
			logger.warning("Socket Set is null.");
			return null;
		}
		
		Map<String , IUISocketElement> elementMap = socketSet.getElementMap();
		
		if( elementMap == null ) {
			logger.warning("Element Map of '"+socketSet.getElementId()+"' is null.");
			return null;
		}
		
		for ( IUISocketElement element : elementMap.values() ) {
			
			if ( elementId.equals( element.getElementId() ) )
				return "/"+socketSet.getElementId()+"/"+elementId;
			
			if ( element instanceof UISocketSet ) {
				
				String path = getElementPath( (UISocketSet)element, elementId);
				
				if ( path != null ) 
					return "/"+socketSet.getElementId()+path;
			}
		}

		return null;
	}
	
	/**
	 * Get the IUISocketElement by ElementId.
	 * 
	 * @param elementId a String value of elementId
	 * @return an Object of IUISocketElement
	 */
	public IUISocketElement getSocketElementByElementId(String elementId, boolean considerDimension) {
		
		if ( elementId == null ) {
			logger.warning("Element Id is null.");
			return null;
		}
		
		if ( elements == null ) {
			logger.warning("List of Socket Elements is null.");
			return null;
		}
		
		for( IUISocketElement  socketElement : elements ) {
			
			if ( elementId.equals( socketElement.getElementId() ) )
				return socketElement;
			
			if ( socketElement instanceof UISocketSet ) {
				
				/*
				if(socketElement.isDimensional())
					continue;
				*/
				IUISocketElement element = getSocketElementByElementId( (UISocketSet)socketElement, elementId, considerDimension);
				
				if ( element != null )
					return element;
				
			} else if ( socketElement instanceof UISocketCommand ) {
				
				IUISocketElement element = ((UISocketCommand)socketElement).getCommandParam(elementId);
				
				if ( element != null )
					return element;
			}
			//Parikshit Thakur : 20111012. Added code to get variables and command in a notification.
			else if (socketElement instanceof UISocketNotification){
				IUISocketElement element = ((UISocketNotification)socketElement).getNotifyElement(elementId);
				
				if ( element != null )
					return element;
				
			}
		}
		
		return null;
	}
		
	
	/**
	 * Get IUISocketElement specified by 'elementId' from the UISocketSet specified by 'socketSet'.
	 * Return the IUISocketElement if and only if it is not member of any dimensional UISocketElement throughout.
	 * 
	 * @param socketSet an Object of UISocketSet
	 * @param elementId a String value of elementId
	 * @return an Object of IUISocketElement
	 */
	private IUISocketElement getSocketElementByElementId(UISocketSet socketSet, String elementId, boolean considerDimension) {
		
		Map<String , IUISocketElement> elementMap = socketSet.getElementMap();
		
		if( elementMap == null ) {
			logger.warning("Element Map of '"+socketSet.getElementId()+"' is null.");
			return null;
		}
		
		for( IUISocketElement element : elementMap.values() ) {
			
			if ( elementId.equals( element.getElementId() ) )
				return element;
			
			if ( element instanceof UISocketSet ) {
				
				if( considerDimension && element.isDimensional() )
					continue;
				
				IUISocketElement e = getSocketElementByElementId( (UISocketSet)element, elementId, considerDimension);
				
				if ( e != null )
					return e;
			}
			//Parikshit Thakur : 20111012. Added code to get variable or command in a notification.
			if(element instanceof UISocketNotification){
				IUISocketElement e = ((UISocketNotification)element).getNotifyElement(elementId);
				
				if ( e != null )
					return e;
			}
		}
		
		return null;
	}

	
	/**
	 * Check whether the dimensional value is in proper format or not.
	 * 
	 * @param elementId a String value of ElementId
	 * @return return whether the dimensional value is in proper format or not
	 */
	private boolean validateDimensionalElementId(String elementId) {
		
		if ( elementId == null )
			return false;
		
		if ( (elementId.indexOf('[') == -1) || (elementId.indexOf(']') == -1) )
			return false;
		
		String indexValue = elementId.substring(elementId.indexOf('[')+1);
		
		int nextPos = -1;
		
		while ( ( nextPos = indexValue.indexOf(']') ) != -1 ) {
			
			String dimValue = indexValue.substring(0, nextPos);
			
			if ( dimValue.startsWith(" ") || dimValue.endsWith(" ") ) { // dimValue does not start or end with space. 
				return false;
			}
			
			if ( indexValue.substring(0, nextPos).indexOf('[') != -1 ) { // indexValue is something like [inde[x]
				return false;
			}
			
			if ( (nextPos+1) == indexValue.length() ) // '[' is at last index and dimension value is in proper format.
				return true;
			
			if ( indexValue.charAt(nextPos+1) != '[' ) { // char next to ']' is not '['.
				return false;
			}
			
			indexValue = indexValue.substring(nextPos+2);
		}
		
		if ( indexValue.length() > 0 ) { // there is '[' and/or some text after last ']'. 
			return false;
		}
		
		return true; // dimension value is in proper format.
	}
	
	
	/**
	 * Get IUISocketElement specified by 'elementId' from the 'elementList'. 
	 * 
	 * @param elementId a String value of elementId
	 * @param elementList a List&lt;IUISocketElement&gt; of elementList
	 * @return an Object of IUISocketElement
	 */
	private IUISocketElement getSocketElementFromList(String elementId, List<IUISocketElement> elementList) {
		
		if ( elementList == null )
			return null;
		
		for ( IUISocketElement socketElement : elementList ) {
		//	logger.info(elementId+" "+socketElement.getElementId());
		//	System.out.println("Session : getSocketElementFromList : "+socketElement.getElementId()+" = "+elementId);
			if( socketElement.getElementId().equals(elementId) ){
				//logger.info(elementId+"="+socketElement.getElementId());
				return socketElement;
			}
		}
		return null;
	}
	/////////////////////////////////////////Useful Functions Ends///////////////////////////////////////////
	
}
