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
import java.util.logging.Logger;

import org.openurc.uch.TAFatalException;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * UISocketVariable is the variable variant of UISocketElement.
 * It provides the functionality to set and get the variable value.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public class UISocketVariable extends AbstractUISocketElement{

	private Object value;
	private Logger logger = LoggerUtil.getSdkLogger();
	//If you are sure SocketVariable is nonDimensional
	/**
	 * Get Value of variable.
	 * 
	 * @return an Object
	 */
	public Object getValue() {
		
		if( isDimensional() )
			return null;
		else
			return value;
		
	}
	
	
	/**
	 * Get Value of variable with dimension.
	 * 
	 * @param indices a String value of indices
	 * @return an Object of Value
	 */
	public Object getValue(String indices) {
		
		if ( this.isDimensional() ) {
			return getValue(indices, getDimMap());		
		} else {	
			return value;
		}
		
	}

	
	/**
	 *  Returns range of values(multiple values) for range of indices(multiple indices)
	 * 
	 * @param indices a String value of indices
	 * @return Map&lt;String, Object&gt;
	 */
	public HashMap<String, Object> getRangeOfValues(String indices) {
		
		if ( indices == null )
			return null;
		
		if ( isDimensional() ) {
			
			HashMap<String, Object> returnMap = new HashMap<String, Object>();
			getRangeOfValues("", indices, getDimMap(), returnMap);
			//System.out.println("RETURN MAP : "+returnMap);
			if ( returnMap.size() == 0 )
				return null;
			else
				return returnMap;
			
		}
		
		return null;
	}
	
	/**
	 * Set Value of the variable.
	 * 
	 * @param value an Object type value
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean setValue(Object value) throws TAFatalException {
		
		if(isFinal()){
			logger.severe("******* Some problem occured. . ."+value);
			//throw new TAFatalException("The Socket Variable is final, can't update value.");
		}
		
		if ( isDimensional() ) {
			return false;
		} else {
			this.value = value;
			return true;
		}
	}
	
	/**
	 * Set Value of the variable with dimension.
	 * 
	 * @param value an Object type value
	 * @param indices a String value of indices
	 * @return boolean
	 * @throws TAFatalException 
	 */
	public boolean setValue(Object value, String indices) throws TAFatalException {
		
		if(isFinal()){
			logger.severe("Going to change final socket element");
			//throw new TAFatalException("The Socket Variable is final, can't update value.");
		}

		
		if ( this.isDimensional() ) {
			
			if(indices == null)
				return false;
			else
				return setValue(indices, value, getDimMap());
			
		} else {
			
			this.value = value;
			return true;
		}
		
	}

	// If you are sure SocketSet is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#addDimension(java.lang.String)
	 */
	
	public boolean addDimension(String index) {
		
		if ( index == null )
			return false;
		
		
		if( !isDimensional() ) 
			return false;
		else {

			if( getDimMap() == null )
				setDimMap( new LinkedHashMap<String, Object>() );
			
			return addNewDimension(index, getDimMap());
		}
	}
	
	// If you are sure SocketSet is dimensional
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#removeDimension(java.lang.String)
	 */
	
	public boolean removeDimension(String index) {
		
		if ( index == null )
			return false;
		
		
		if( !isDimensional() ) 
			return false;
		else 
			return removeExistingDimension(index, getDimMap());
	}
	
	//////////////////////////////////////////setValue///////////////////////////////////////////////////
	
	/**
	 * Set Value of the Dimension Specified by 'index'
	 * 
	 * @param index a String value of index
	 * @param value an Object type value
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 * @throws TAFatalException 
	 */
	@SuppressWarnings("unchecked")
	private boolean setValue(String index, Object value, LinkedHashMap<String, Object> dimMap) throws TAFatalException {
		
		if(isFinal()){
			logger.severe("Going to change final socket element");
			//throw new TAFatalException("The Socket Variable is final, can't update value.");
		}

			
		if ( dimMap == null )
			return false;
		
		//System.out.println("DIM MAP :"+dimMap);
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( dimMap.containsKey(index) ) {
				dimMap.put(index, value);
				return true;
			} else {
				return false;
			}
			
		} else { //e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);
			
			if ( !dimMap.containsKey(firstIndex) )
				return false;
			
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) {
				return setValue( index.substring( index.indexOf("[", index.indexOf("]"))), value, (LinkedHashMap)obj);
				//return removeExistingDimension(index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			} else {
				dimMap.remove(firstIndex);
				return false;
			}
			
		}
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	//////////////////////////////////////////addNewDimension////////////////////////////////////////////////////////

	/**
	 * Add a new dimension to the UISocketVariable specified by 'index'.
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
				
				dimMap.put(index, value);
				return true;
				
			} else {
				
				return false;
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
	
	
	//////////////////////////////////////////getValue//////////////////////////////////////

	/**
	 * Get Value of the Variable.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return an Object
	 */
	@SuppressWarnings("unchecked")
	private Object getValue(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( (index == null) || (index.indexOf("[") == -1) || (index.indexOf("]") == -1) || (dimMap == null) ) {
			return null;
		}
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			return dimMap.get(index);
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]")+1);
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getValue( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////getRangeOfValues//////////////////////////////////////////

	/**
	 * Get range of Values(more than one values) of Dimensional UISocketVariable.
	 * 
	 * @param prefix a String value of prefix
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @param returnMap Map&lt;String, Object&gt;
	 */
	@SuppressWarnings("unchecked")
	private void getRangeOfValues(String prefix, String index, LinkedHashMap<String, Object> dimMap, HashMap<String, Object> returnMap) {
		
		
		if ( (index == null) || (index.indexOf("[") == -1) || (index.indexOf("]") == -1) )
			return;
		
		if ( dimMap == null )
			return;
		
		index = index.trim();
		
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( index.substring(index.indexOf("[")+1, index.indexOf("]")).trim().equals("") ) { // e.g. index = []
				
				for( String ind : dimMap.keySet() )	
					returnMap.put(prefix+ind, dimMap.get(ind) );
				
				return;
				
			} else {// e.g. index = [ind] or [ind2 ind5]
				
				if ( index.indexOf(" ") == -1 ) {// e.g. index = [ind]
					
					returnMap.put(prefix+index, getValue(index, dimMap));
					
					return;
					
				} else {// e.g. index = [ind2 ind5]
					
					String startIndex = index.substring(1, index.indexOf(" ")).trim();
					String endIndex = index.substring(index.lastIndexOf(" "), index.indexOf("]")).trim();
					
					for( String ind : dimMap.keySet() ) {
						
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) )
							returnMap.put(prefix+ind, dimMap.get(ind) );
						
					}
					
					return;
				}
			}
			
		} else { // e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring( 0, index.indexOf("]")+1 );
			if ( firstIndex.substring(firstIndex.indexOf("[")+1, firstIndex.indexOf("]")).trim().equals("") ) { // e.g. firstIndex = []
				
				for( String ind : dimMap.keySet() ) {
					
					Object obj = dimMap.get(ind);
					
					if ( obj instanceof LinkedHashMap ) {
						getRangeOfValues(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					}
				}
				return;
				
			} else {// e.g. firstIndex = [ind] or [ind2 ind5]
				
				if ( firstIndex.indexOf(" ") == -1 ) {// e.g. firstIndex = [ind]
					
					String ind = firstIndex.substring(firstIndex.indexOf("["), firstIndex.indexOf("]")+1);
					Object obj = dimMap.get(ind);
					
					if ( obj instanceof LinkedHashMap )
						getRangeOfValues(prefix+firstIndex, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					else 
						return;
					
				} else {// e.g. firstIndex = [ind2 ind5]
					
					String startIndex = firstIndex.substring(1, firstIndex.indexOf(" ")).trim();
					String endIndex = firstIndex.substring(firstIndex.lastIndexOf(" "), firstIndex.indexOf("]")).trim();
					
					
					for ( String ind : dimMap.keySet() ) {
												
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) ) {
							
							Object obj = dimMap.get(ind);
							
							if ( obj instanceof LinkedHashMap ) 
								getRangeOfValues(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
							
						}
						/*
						if ( ind.equals(startIndex) )
							flag = true;
						
						if ( ind.equals(endIndex) )
							flag = false;
						
						if ( flag ) {
							
							Object obj = dimMap.get(ind);
							
							if ( obj instanceof LinkedHashMap ) 
								getRangeOfValues(prefix+ind, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
							
						}
						*/
					}
					/*
					Object obj = getDimMap().get(endIndex);
					
					if ( obj instanceof HashMap ) 
						getRangeOfValues(prefix+endIndex, index.substring(index.indexOf("]")+1), (LinkedHashMap)obj, returnMap);
					else
						return;
					*/
				}
			}
			
		}
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */
	public Object clone() {
		
		//System.out.println("I am cloning for Element '"+this.getElementId()+"'.");
		
		UISocketVariable clonedSocketVariable = new UISocketVariable();
		
		clonedSocketVariable.setElementId( new String( this.getElementId() ) );
		clonedSocketVariable.setDimensional( this.isDimensional() );
		
// 2012-09-17 : process final argument.
		clonedSocketVariable.setFinal(this.isFinal());
		
		clonedSocketVariable.setSocket( this.getSocket() );
		
		if ( this.getDimType() != null )
			clonedSocketVariable.setDimType( this.getDimType() );
		
		if ( this.getType() != null ) 
			clonedSocketVariable.setType( this.getType() );
		
		if ( this.getSession() != null ) 
			clonedSocketVariable.setSession( this.getSession() );
		
		
		return clonedSocketVariable;	
	}
}
