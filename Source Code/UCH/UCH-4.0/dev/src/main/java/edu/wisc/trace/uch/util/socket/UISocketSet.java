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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wisc.trace.uch.util.CommonUtilities;

/**
 * UISocketSet is the variant of UISocketElement.
 * It stores the information about the 'set' node of Socket Description File.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public class UISocketSet extends AbstractUISocketElement {

	

	private HashMap<String, IUISocketElement> elementMap = new HashMap<String, IUISocketElement>();
	
	/**
	 * Adds the Socket Element in the map.
	 * 
	 * @param socketElement an Object of IUISocketElement
	 */
	
	public void addIUISocketElement(IUISocketElement socketElement) {
		
		if ( socketElement != null ) 
			elementMap.put(socketElement.getElementId(), socketElement);
		
	}
	
	//If you are sure SocketSet is nonDimensional
	/**
	 * Return a Map containing Socket Elements.
	 * 
	 * @return Map&lt;String, IUISocketElement&gt; 
	 */
	public HashMap<String, IUISocketElement> getElementMap() {
		/*
		if ( !isDimensional() )
			return elementMap;
		else
			return null;
		*/
		return elementMap;
	}
	
	// If you are sure SocketSet is dimensional & index is not 'range of Index'.
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

	// If you are sure SocketSet is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#addDimension(java.lang.String)
	 */
	public boolean addDimension(String index) {
		
		if( !isDimensional() ) 
			return false;
		else {
			
			if( getDimMap() == null ) {
				setDimMap( new LinkedHashMap<String, Object>() );
			}
			 			 
			return addNewDimension(index, getDimMap());	
		}
	}
	
	// If you are sure SocketSet is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#removeDimension(java.lang.String)
	 */
	public boolean removeDimension(String index) {
		
		if( !isDimensional() ) 
			return false;
		else 
			return removeExistingDimension(index, getDimMap());
	}
	/**
	 * Get Value of variable with dimension.
	 * 
	 * @param indices a String value of indices
	 * @return an Object of Value
	 */
	public Object getValue(String indices) {
		System.out.println("UISocketVariable : getValue : getDimMap :: "+getDimMap());
		if ( this.isDimensional() ) {
			return getValue(indices, getDimMap());		
		} else {	
			return null;
		}
	}
	
	/**
	 * Get Value of the Variable.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return an Object
	 */
	@SuppressWarnings("unchecked")
	private Object getValue(String index, LinkedHashMap<String, Object> dimMap) {
		//System.out.println("UISocketVariable : getValue : index :: "+index);
		if ( (index == null) || (index.indexOf("[") == -1) || (index.indexOf("]") == -1) || (dimMap == null) ) {
			return null;
		}
		if(index.equals("[]")){
			return dimMap;
		}
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		System.out.println(dimMap);
			return dimMap.get(index);
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getValue( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	
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
	
	
	//////////////////////////////////////////removeExistingDimension///////////////////////////////////////////////////
	/**
	 * Remove Existing dimension on specified index.
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
	
	//////////////////////////////////////////addNewDimension////////////////////////////////////////////////////////

	/**
	 * Adds new Dimension.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean addNewDimension( String index, LinkedHashMap<String, Object> dimMap) {
	
		//System.out.println("-----------Index"+index);
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			if ( !dimMap.containsKey(index) ) {
				
				dimMap.put(index, getClonedElementMap());		
				return true;
				
			} else {
				
				return true;
			}
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);
			
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
	
	//////////////////////////////////////////getElementMapOnIndex////////////////////////////////////////////////////////

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
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */

	public Object clone() {
		
		UISocketSet clonedSocketSet = new UISocketSet();
		
		clonedSocketSet.setElementId( new String( this.getElementId() ) );
		clonedSocketSet.setDimensional( this.isDimensional() );
		clonedSocketSet.setSocket( this.getSocket() );
		
		if ( this.getSession() != null ) 
			clonedSocketSet.setSession( this.getSession() );
		
		if ( this.getDimType() != null )
			clonedSocketSet.setDimType( this.getDimType() );
		
		if ( this.getType() != null ) 
			clonedSocketSet.setType( this.getType() );
		
		clonedSocketSet.elementMap = getClonedElementMap();
		
		return clonedSocketSet;	
	}
	
	/**
	 * Get Cloned copy of Element Map.
	 * 
	 * @return a Map&lt;String, IUISocketElement&gt;
	 */
	private HashMap<String, IUISocketElement> getClonedElementMap() {
		
		HashMap<String, IUISocketElement> clonedElementMap = new HashMap<String, IUISocketElement>();
		
		for( String elementName : elementMap.keySet() ) 
			clonedElementMap.put(new String(elementName), (IUISocketElement)elementMap.get(elementName).clone() );
		
		return clonedElementMap;
	}
	
	
}
