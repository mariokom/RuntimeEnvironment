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

package edu.wisc.trace.uch.util.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.wisc.trace.uch.util.CommonUtilities;

/**
 * UISocketNotification is the notification variant of
 * UISocketElement. It provides the functionality for acknowledging
 * notifications.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UISocketNotification extends AbstractUISocketElement{

	
	private String state;
	private String category; // added to append category attribute to notifications. Parikshit Thakur : 20110727
	
	//Parikshit Thakur : 20111017. Modified code for the newly added elements in notification. Added new methods and variables.
	private HashMap<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>(); 
	
	/**
	 * returns elementMap for the notification.
	 * @return elementMap for the notification.
	 */
	public HashMap<String, IUISocketElement> getElementMap(){
		return elementMap;
	}
	/**
	 * Adds a socket element to element Map.	
	 * @param socketElement socketElement to be addded
	 */
	public void addIUISocketElement(IUISocketElement socketElement){
		if ( socketElement != null ) 
			elementMap.put(socketElement.getElementId(), socketElement);
	}
	/**
	 * Sets the sub elements of Notification in elementMap
	 * @param elementMap elementMap to be set.
	 */
	public void setNotifyElementMap(HashMap<String, IUISocketElement> elementMap){
		this.elementMap = elementMap;
	}
	/**
	 * Returns elment with the given id.
	 * @param elementId id of required element.
	 * @return Socket element for the id.
	 */
	public IUISocketElement getNotifyElement(String elementId) {
		
		return (IUISocketElement)elementMap.get(elementId);
	}
	
	/**
	 * Return a Map Containing Socket Element of Dimensional type.
	 * 
	 * @param indices a String value of indices
	 * @return Map&lt;String, IUISocketElement&gt;
	 */	
	public HashMap<String, IUISocketElement> getElementMap( String indices) {
		
		if ( isDimensional() && indices != null) {
			return getElementMapOnIndex(indices, getDimMap());
		} else if ( !isDimensional() && indices == null ) {
			return elementMap;
		} else {
			return null;
		}
		
	}

	public Map<String, Map<String, IUISocketElement>> getElementMaps() {
		return getElementMaps("[]");
	}
	
	public Map<String, Map<String, IUISocketElement>> getElementMaps(String indices) {
		
		if ( indices == null ) 
			return null;
		
		if ( !CommonUtilities.isIndexValueValid(indices) ) {
			return null;
		}
		
		if ( getDimMap() == null ) 
			return null;
		
		Map<String, Map<String, IUISocketElement>> returnMap = new HashMap<String, Map<String,IUISocketElement>>();
		
		getElementMaps(indices, "", getDimMap(), returnMap);
		
		return returnMap;
		
	}

	private void getElementMaps(String indices, String prefix, LinkedHashMap<String, Object> dimMap, Map<String, Map<String, IUISocketElement>> returnMap) {
		
		if ( (indices == null) || (dimMap == null) || (returnMap == null) )
			return;
		
		String currentIndex = null;
		String nextIndices = null;
		
		if ( indices.indexOf('[', 1) == -1 ) { // e.g. indices = [] or [idx1 idx2] or [idx1]
			
			currentIndex = indices;
			nextIndices = null;
			
		} else {
			
			currentIndex = indices.substring(0, indices.indexOf('[', 1) );
			nextIndices = indices.substring( indices.indexOf('[', 1) );
		}
		
		if ( currentIndex.substring(currentIndex.indexOf('[')+1, currentIndex.indexOf(']')).trim().equals("") ) { // e.g. currentIndex = []
			
			for ( String index : dimMap.keySet() ) {
				
				Object value = dimMap.get(index);
				
				if ( value == null )
					continue;
				
				if ( value instanceof LinkedHashMap ) {
					
					if ( nextIndices == null )
						getElementMaps("[]", index, dimMap, returnMap);
					else 
						getElementMaps(nextIndices, index, dimMap, returnMap);
					
				} else if ( value instanceof HashMap ) {
					
					returnMap.put(prefix+index, (HashMap)value);
				}
				
			}
			
		} else if ( currentIndex.indexOf(' ') != -1 ) { // e.g. currentIndex = [idx1 idx2]
			
			String startIndex = currentIndex.substring(1, currentIndex.indexOf(' ') );
			String endIndex = currentIndex.substring(currentIndex.lastIndexOf(' ')+1, currentIndex.lastIndexOf(']') );
			
			boolean flag = false;
			for ( String index : dimMap.keySet() ) {
				
				if ( index.equals(startIndex) ) 
					flag = true;
				
				if ( flag ) {
				
					Object value = dimMap.get(index);
					
					if ( value != null ) {
						
						if ( value instanceof LinkedHashMap ) {
							
							if ( nextIndices == null )
								getElementMaps("[]", index, dimMap, returnMap);
							else 
								getElementMaps(nextIndices, index, dimMap, returnMap);
							
						} else if ( value instanceof HashMap ) {
							
							returnMap.put(prefix+index, (HashMap)value);
						}
						
					}
				}
				
				if ( index.equals(endIndex) ) 
					break;
			}
			
		} else { // e.g. currentIndex = [idx1]
			
			String index = currentIndex.substring(1, currentIndex.indexOf('[') );
			
			Object value = dimMap.get(index);
			
			if ( value != null ) {
				
				if ( value instanceof LinkedHashMap ) {
					
					if ( nextIndices == null )
						getElementMaps("[]", index, dimMap, returnMap);
					else 
						getElementMaps(nextIndices, index, dimMap, returnMap);
					
				} else if ( value instanceof HashMap ) {
					
					returnMap.put(prefix+index, (HashMap)value);
				}
				
			}
		}
		
	}
	
	
	// If you are sure SocketSet is dimensional
	/**
	 * Get Map of IUISocketElements on specified indices of IUISocketSet.
	 * 
	 * @param indices a String value of indices
	 * @return Map&lt;String, HashMap&lt;String, IUISocketElement&gt;&gt;
	 */
	public HashMap<String, HashMap<String, IUISocketElement>> getRangeOfElementMaps( String indices ) {
		
		if ( isDimensional() && indices != null) {
			
			HashMap<String, HashMap<String, IUISocketElement>> returnMap = new HashMap<String, HashMap<String, IUISocketElement>>();
			getRangeOfElementMaps("", indices, getDimMap(), returnMap);
			
			if ( returnMap.size() == 0 )
				return null;
			else 
				return returnMap;
		} else {
			return null;
		}
	}

	
	
	/**
	 * Get the Map of IUISocketElement on specified index.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return Map&lt;String, IUISocketElement&gt;
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, IUISocketElement> getElementMapOnIndex(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 || dimMap == null )
			return null;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			Object obj = dimMap.get(index);
			
			if ( obj instanceof HashMap )
				return (HashMap)obj;
				
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getElementMapOnIndex( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////getRangeOfElementMaps//////////////////////////////////////////////////////////////////

	
	/**
	 * Get Map of IUISocketElements on specified indices of IUISocketSet.
	 * 
	 * @param prefix a String value of prefix
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @param returnMap Map&lt;String, HashMap&lt;String, IUISocketElement&gt;&gt;
	 */
	@SuppressWarnings("unchecked")
	private void getRangeOfElementMaps(String prefix, String index, 
			LinkedHashMap<String, Object> dimMap, HashMap<String, HashMap<String, IUISocketElement>> returnMap){
		
		if ( (prefix == null) || (index == null) || (dimMap==null) || (returnMap == null) )
			return;
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return;
		
		index = index.trim();
		
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [] or [ind] or [ind1 ind2]
			
			if ( index.substring(index.indexOf("[")+1, index.indexOf("]")).trim().equals("") ) { // e.g. index = []
				
				for( String ind : dimMap.keySet() ) {
					
					Object obj = dimMap.get(ind);
					
					if ( !(obj instanceof HashMap) ) 
						continue;
					
					returnMap.put(prefix+ind, (HashMap)obj );
					
				}
				
				return;
				
			} else {// e.g. index = [ind] or [ind2 ind5]
				
				if ( index.indexOf(" ") == -1 ) {// e.g. index = [ind]
					
					HashMap<String, IUISocketElement> elementMap = getElementMapOnIndex(index, dimMap);
					
					if( elementMap == null )
						return;
					
					returnMap.put(prefix+index, elementMap);
					
					return;
					
				} else {// e.g. index = [ind2 ind5]
					
					String startIndex = index.substring(1, index.indexOf(" ")).trim().trim();
					String endIndex = index.substring(index.lastIndexOf(" "), index.indexOf("]")).trim();
					
					for( String ind : dimMap.keySet() ) {
						
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) ) {
							
							Object obj = dimMap.get(ind);
							
							if ( !(obj instanceof HashMap) ) 
								continue;
							
							returnMap.put(prefix+ind, (HashMap)obj );
						}
						/*
						if ( ind.equals(startIndex) )
							flag = true;
						
						if ( ind.equals(endIndex) )
							flag = false;
						
						if ( flag ) {
							
							Object obj = dimMap.get(ind);
							
							if ( !(obj instanceof HashMap) ) 
								continue;
							
							returnMap.put(prefix+ind, (HashMap)obj );
							
						}
						*/
					}
					/*
					Object obj = dimMap.get(endIndex);
					
					if ( obj instanceof HashMap ) 
						returnMap.put(prefix+endIndex, (HashMap)obj );
					
					return;
					*/
				}
			}
			
		} else { // e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);

			if ( firstIndex.substring(firstIndex.indexOf("[")+1, firstIndex.indexOf("]")).trim().equals("") ) { // e.g. firstIndex = []
				
				for( String ind : dimMap.keySet() ) {
					
					Object obj = dimMap.get(ind);
					
					if ( !(obj instanceof LinkedHashMap) ) 
						getRangeOfElementMaps(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
				}
				return;
				
			} else {// e.g. firstIndex = [ind] or [ind2 ind5]
				
				if ( firstIndex.indexOf(" ") == -1 ) {// e.g. firstIndex = [ind]
					
					Object obj = dimMap.get(firstIndex);
				
					if ( obj instanceof LinkedHashMap ) {
						getRangeOfElementMaps(prefix+firstIndex, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					} else  {
						return;
					}
				} else {// e.g. firstIndex = [ind2 ind5]
					
					String startIndex = firstIndex.substring(1, firstIndex.indexOf(" ")).trim();
					String endIndex = firstIndex.substring(firstIndex.lastIndexOf(" "), firstIndex.indexOf("]")).trim();
					
					for( String ind : dimMap.keySet() ) {
						
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) ) {
							
							Object obj = dimMap.get(ind);
							
							if ( obj instanceof LinkedHashMap ) 
								getRangeOfElementMaps(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
						}
						/*
						if ( ind.equals(startIndex) )
							flag = true;
						
						if ( ind.equals(endIndex) )
							flag = false;
						
						if ( flag ) {
							
							Object obj = dimMap.get(ind);
							
							if ( obj instanceof LinkedHashMap ) 
								getRangeOfElementMaps(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
							
						}
						*/
					}
					/*
					Object obj = dimMap.get(endIndex);
					
					if ( obj instanceof HashMap ) 
						getRangeOfElementMaps(prefix+endIndex, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					else
						return;
					*/
				}
			}
			
		}
		
	}

	
	
	/**
	 * Get the value of state of Notification.
	 * 
	 * @return a String value of state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Get the value of state of specified dimensional Notification.
	 * 
	 * @return a String value of state
	 */
	public String getState(String indices) {

		if ( this.isDimensional() ) {
			return getState(indices, getDimMap());		
		} else {	
			return state;
		}
	}
	
	/**
	 * Set State of the Notification.
	 * 
	 * @param state a String value of state 
	 * @return whether state is set successfully or not
	 */
	public boolean setState(String state) {
		
		this.state = state;
		return true;	
	}
	
	/**
	 * Set State of the Notification on the dimension specified by 'indices'.
	 * 
	 * @param state a String value of state 
	 * @param indices a String value of indices
	 * @return whether state is set successfully or not
	 */
	public boolean setState(String state, String indices) {
		
		if ( this.isDimensional() ) {
			
			if(indices == null)
				return false;
			else
				return setState(indices, state, getDimMap());
			
		} else {
			
			this.state = state;
			return true;
		}
		
	}
	
	// added getter setter to append category attribute to notifications. Parikshit Thakur : 20110727
	/**
	 * Set Category of the Notification.
	 * 
	 * @param category a String value of category 
	 * 
	 */
	public void setCategory(String category) {
		this.category = category;
		
	}
	
	
	/**
	 * Get the value of category of specified Notification.
	 * 
	 * @return a String value of category
	 */
	public String getCategory() {
		return category;
	}
	
	
	
	
	
	// If you are sure UISocketNotification is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#addDimension(java.lang.String)
	 */
	
	public boolean addDimension(String index) {
		
		if( !isDimensional() ) 
			return false;
		else {

			if( getDimMap() == null )
				setDimMap( new LinkedHashMap<String, Object>() );
			
			return addNewDimension(index, getDimMap());
		}
	}
	
	
	// If you are sure UISocketNotification is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#removeDimension(java.lang.String)
	 */
	
	public boolean removeDimension(String index) {
		
		if( !isDimensional() ) 
			return false;
		else 
			return removeExistingDimension(index, getDimMap());
	}
	
	//If you are sure UISocketNotification is Dimensional
	/**
	 *  Returns range of state(multiple values of state) for range of indices(multiple indices)
	 * 
	 * @param indices a String value of indices
	 * @return Map&lt;String, String&gt;
	 */
	public HashMap<String, String> getRangeOfState(String indices) {
		
		if ( isDimensional() ) {
			
			HashMap<String, String> returnMap = new HashMap<String, String>();
			getRangeOfState("", indices, getDimMap(), returnMap);
			
			if ( returnMap.size() == 0 )
				return null;
			else
				return returnMap;
			
		}
		
		return null;
	}
	
	
	/////////////////////////////////getRangeOfState//////////////////////////////////////////

	/**
	 * Get the value of state of dimensional Notification on specified dimensions.
	 * 
	 * @param prefix a String value of prefix
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @param returnMap Map&lt;String, Object&gt;
	 */
	@SuppressWarnings("unchecked")
	private void getRangeOfState(String prefix, String index, LinkedHashMap<String, Object> dimMap, HashMap<String, String> returnMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return;
		
		index = index.trim();
		
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( index.substring(index.indexOf("[")+1, index.indexOf("]")).trim().equals("") ) { // e.g. index = []
				
				for( String ind : dimMap.keySet() )	{
					
					Object state = dimMap.get(ind);
					
					if ( state == null ) 
						returnMap.put(prefix+"["+ind+"]", null);
					else 
						returnMap.put(prefix+"["+ind+"]", state.toString() );
				}
				return;
				
			} else { // e.g. index = [ind] or [ind2 ind5]
				
				if ( index.indexOf(" ") == -1 ) {// e.g. index = [ind]
					
					returnMap.put(prefix+index, getState(index, dimMap));
					
					return;
					
				} else {// e.g. index = [ind2 ind5]
					
					String startIndex = index.substring(1, index.indexOf(" "));
					String endIndex = index.substring(index.lastIndexOf(" "), index.indexOf("]"));
					
					boolean flag = false;
					
					for( String ind : dimMap.keySet() ) {
						
						if ( ind.equals(startIndex) )
							flag = true;
						
						if ( ind.equals(endIndex) )
							flag = false;
						
						if ( flag ) {
							
							Object state = dimMap.get(ind);
							
							if ( state == null ) 
								returnMap.put(prefix+"["+ind+"]", null );
							else 
								returnMap.put(prefix+"["+ind+"]", state.toString() );					
							
						}
						
					}
					
					Object state = dimMap.get(endIndex);
					
					if ( state == null ) 
						returnMap.put(prefix+"["+endIndex+"]", null );
					else
						returnMap.put(prefix+"["+endIndex+"]", state.toString() );
					
					return;
				}
			}
			
		} else { // e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			
			if ( firstIndex.substring(firstIndex.indexOf("[")+1, firstIndex.indexOf("]")).trim().equals("") ) { // e.g. firstIndex = []
				
				for( String ind : dimMap.keySet() ) {
					
					Object obj = dimMap.get(ind);
					
					if ( !(obj instanceof LinkedHashMap) ) 
						getRangeOfState(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
				}
				return;
				
			} else {// e.g. firstIndex = [ind] or [ind2 ind5]
				
				if ( firstIndex.indexOf(" ") == -1 ) {// e.g. firstIndex = [ind]
					
					String ind = firstIndex.substring(firstIndex.indexOf("[")+1, firstIndex.indexOf("]")-1);
					Object obj = dimMap.get(ind);
					
					if ( obj instanceof LinkedHashMap )
						getRangeOfState(prefix+firstIndex, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					else 
						return;
					
				} else {// e.g. firstIndex = [ind2 ind5]
					
					String startIndex = firstIndex.substring(1, firstIndex.indexOf(" "));
					String endIndex = firstIndex.substring(firstIndex.lastIndexOf(" "), firstIndex.indexOf("]"));
					
					boolean flag = false;
					
					for( String ind : dimMap.keySet() ) {
						
						if ( ind.equals(startIndex) )
							flag = true;
						
						if ( ind.equals(endIndex) )
							flag = false;
						
						if ( flag ) {
							
							Object obj = dimMap.get(ind);
							
							if ( obj instanceof LinkedHashMap ) 
								getRangeOfState(prefix+"["+ind+"]", index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
							
						}
					
					}
					
					Object obj = getDimMap().get(endIndex);
					
					if ( obj instanceof HashMap ) 
						getRangeOfState(prefix+"["+endIndex+"]", index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					else
						return;
					
				}
			}
			
		}
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////getValue//////////////////////////////////////

	/**
	 * Get State of Notification of the dimension specified by 'index'.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return an Object
	 */
	@SuppressWarnings("unchecked")
	private String getState(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 || dimMap == null )
			return null;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			Object state = dimMap.get(index);
			
			if ( state == null )
				return null;
			else
				return state.toString();
			
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getState( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////setValue///////////////////////////////////////////////////
	
	/**
	 * Set Value of the Dimension Specified by 'index'
	 * 
	 * @param index a String value of index
	 * @param value an Object type value
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean setState(String index, String state, LinkedHashMap<String, Object> dimMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( dimMap.containsKey(index) ) {
				dimMap.put(index, state);
				return true;
			} else {
				return false;
			}
			
		} else { //e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			
			if ( !dimMap.containsKey(firstIndex) )
				return false;
			
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) {
				return removeExistingDimension(index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			} else {
				dimMap.remove(firstIndex);
				return false;
			}
			
		}
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////addNewDimension////////////////////////////////////////////////////////

	/**
	 * Add a new dimension to the UISocketNotification specified by 'index'.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */	
	@SuppressWarnings("unchecked")
	private boolean addNewDimension( String index, LinkedHashMap<String, Object> dimMap) {
	
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			if ( !dimMap.containsKey(index) ) {
				
				dimMap.put(index, state);	
				return true;
				
			} else {
				
				return false;
			}
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			
			LinkedHashMap<String, Object> innerDimMap = null;
			
			if ( !dimMap.containsKey(firstIndex) ) { 
				
				innerDimMap = new LinkedHashMap<String, Object>();
				dimMap.put(firstIndex, innerDimMap);
				
			} else {
				
				Object obj = dimMap.get(firstIndex);
				
				if ( obj instanceof LinkedHashMap ) {
					innerDimMap = (LinkedHashMap)obj;
				} else {
					innerDimMap = new LinkedHashMap<String, Object>();
					dimMap.put(firstIndex, innerDimMap);
				}
				
			}
			
			return addNewDimension(index.substring(index.indexOf("]")+1),innerDimMap);
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////removeExistDimension///////////////////////////////////////////////////

	/**
	 * Removes existing dimension specified by index
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean removeExistingDimension(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( dimMap.containsKey(index) )
				dimMap.remove(index);
			
			return true;
			
		} else { //e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			
			if ( !dimMap.containsKey(firstIndex) )
				return true;
			
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ){
				return removeExistingDimension(index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			} else {
				dimMap.remove(firstIndex);
				return true;
			}
				
		}
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Get all IUISocketElements of this IUISocketSet.
	 * 
	 * @return an Object of Map&lt;String, IUISocketElement&gt;
	 */
	@SuppressWarnings("unchecked")
	public Map<String, IUISocketElement> getAllValues() {
		
		if ( isDimensional() ) {
			
			LinkedHashMap<String, Object> dimMap = getDimMap();
			
			if ( dimMap == null )
				return null;
			
			Map<String, IUISocketElement> returnMap = new HashMap<String, IUISocketElement>();
			
			for ( String ind : dimMap.keySet() ) {
				
				Object obj = dimMap.get(ind);
				
				if ( obj instanceof LinkedHashMap ) {
					getAllValuesFromDimMap(ind, (LinkedHashMap)obj, returnMap);
				} else if ( obj instanceof HashMap ) {
					getAllValues(ind, (HashMap)obj, returnMap);
				}
				
			}
			
			if ( returnMap.size() == 0 )
				return null;
			else
				return returnMap;
			
		} else {
			
			Map<String, IUISocketElement> returnMap = new HashMap<String, IUISocketElement>();
			getAllValues(null, elementMap, returnMap);
			
			if ( returnMap.size() == 0 )
				return null;
			else
				return returnMap;
			
		}
	
	}
	
	/**
	 * Get all IUISocketElements on specified index.
	 * 
	 * @param index a String value of index
	 * @param dimMap an Object of LinkedHashMap&lt;String, Object&gt;
	 * @param returnMap an Object of LinkedHashMap&lt;String, String&gt;
	 */
	@SuppressWarnings("unchecked")
	private void getAllValuesFromDimMap(String index, LinkedHashMap<String, Object> dimMap, Map<String, IUISocketElement> returnMap) {
		
		if ( dimMap == null )
			return;
		
		for ( String ind : dimMap.keySet() ) {
			
			Object obj = dimMap.get(ind);
			
			if ( obj instanceof LinkedHashMap ) {
				getAllValuesFromDimMap(index+ind, (LinkedHashMap)obj, returnMap);
			} else if ( obj instanceof HashMap ) {
				getAllValues(index+ind, (HashMap)obj, returnMap);
			}
			
		}
		
	}
	/**
	 * Get all IUISocketElements of specified indices.
	 * 
	 * @param indices a String value of indices
	 * @return an Object of Map&lt;String, IUISocketElement&gt;
	 */
	@SuppressWarnings("unchecked")
	public Map<String, IUISocketElement> getAllValues(String indices) {
		
		//System.out.println("get Value of index "+indices);
		
		if( (indices.indexOf("[") == -1) || (indices.indexOf("]", indices.indexOf("[")) == -1) ) {
			return null;
		}
		
		if ( (indices == null) || !isDimensional() ) {
			
		} else {
			
			
			if ( indices.substring(indices.indexOf("[")+1,indices.indexOf("]")).trim().equals("") ) { //e.g. indices = [] 
				
				return getAllValues();
				
			} else {
				
				Object obj = getDimMap().get(indices);
				
				if( obj instanceof HashMap ) {
					
					Map<String, IUISocketElement> returnMap = new HashMap<String, IUISocketElement>();
					getAllValues(indices, (HashMap)obj, returnMap);
					return returnMap;
				}
			}
					
		}
			
		return null;
	}
	
	/**
	 * Get all IUISocketElements of this IUISocketSet.
	 * 
	 * @param index a String value of index
	 * @param elementMap an Object of Map&lt;String, IUISocketElement&gt;
	 * @param returnMap an Object of Map&lt;String, IUISocketElement&gt;
	 * 
	 */
	private void getAllValues(String index, Map<String, IUISocketElement> elementMap, Map<String, IUISocketElement> returnMap) {
		
		if ( elementMap == null ) 
			return;
		
		if ( index == null )
			index = "";
		
		String elementId = getElementId();
		
		for ( String eleId : elementMap.keySet() ) {
			
			IUISocketElement socketElement = elementMap.get(eleId);
			
			if ( socketElement == null )
				continue;
			
			returnMap.put("/"+elementId+index+"/"+socketElement.getElementId(), socketElement);
			/*
			if ( socketElement instanceof UISocketSet ) {
				
				if ( includeSets ) {
					
					returnMap.put("/"+elementId+index+"/"+socketElement.getElementId(), socketElement);
					
					Map<String, IUISocketElement> elementOfSet = ((UISocketSet)socketElement).getAllValues();
					
					if ( elementOfSet == null )
						continue;
					
					for ( String setId : elementOfSet.keySet() ) 					
						returnMap.put("/"+elementId+index+setId, elementOfSet.get(setId));
				}
				
			} else {
				returnMap.put("/"+elementId+index+"/"+socketElement.getElementId(), socketElement);
			}
			*/
		}
		
	}
	
	
	
	
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */
	public Object clone() {
		
		UISocketNotification clonedSocketNotification = new UISocketNotification();
		
		clonedSocketNotification.setState(new String(this.state) );
		clonedSocketNotification.setElementId( new String( this.getElementId() ) );
		clonedSocketNotification.setDimensional( this.isDimensional() );
		clonedSocketNotification.setSocket( this.getSocket() );
		
		//Parikshit Thakur : 20111012. Added new variable and command list to cloned notification.
		clonedSocketNotification.setNotifyElementMap((HashMap<String, IUISocketElement>)this.getElementMap());
		
		if ( this.getSession() != null ) 
			clonedSocketNotification.setSession( this.getSession() );
		
		if ( this.getDimType() != null )
			clonedSocketNotification.setDimType( this.getDimType() );
		
		if ( this.getType() != null ) 
			clonedSocketNotification.setType( this.getType() );
		
		if ( this.getCategory() != null )   // added to append category attribute to notifications. Parikshit Thakur : 20110727
			clonedSocketNotification.setCategory( this.getCategory()); 
		
		return clonedSocketNotification;	
	}
	

	
	
	
}
