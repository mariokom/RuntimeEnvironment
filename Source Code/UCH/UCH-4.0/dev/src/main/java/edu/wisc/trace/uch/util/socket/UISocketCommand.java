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
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * UISocketCommand is the command variant of UISocketElement.
 * It also serves as the base class for all of the specific command types.
 *
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/

public abstract class UISocketCommand extends AbstractUISocketElement{

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private HashMap<String, UISocketCommandParam> commandParams = new HashMap<String, UISocketCommandParam>();
	
	/**
	 * Sets Command Parameters.
	 * 
	 * @param commandParams Map&lt;String, UISocketCommandParam&gt;
	 */
	protected void setCommandParams(HashMap<String, UISocketCommandParam> commandParams) {
		this.commandParams = commandParams;
	}
	
	/**
	 * Get Command Parameters.
	 * 
	 * @return Map&lt;String, UISocketCommandParam&gt;
	 */
	protected HashMap<String, UISocketCommandParam> getCommandParams() {
		return commandParams;
	}
	
	/**
	 * Add 'commandParam' to commandParams map.
	 * 
	 * @param commandParam an Object of UISocketCommandParam
	 */
	public void addCommandParam(UISocketCommandParam commandParam) {
		
		
		if ( commandParam != null ) 
			commandParams.put(commandParam.getId(), commandParam);
		
	}
	
	/**
	 * Get a Map for commands.
	 * 
	 * @return Map&lt;String, UISocketCommandParam&gt;
	 */
	public HashMap<String, UISocketCommandParam> getCommandParamMap() {
			
		return commandParams;
	}
	
	/**
	 * Get Map for Command Param with specified 'indices'. 
	 * 
	 * @param indices a String value of indices
	 * @return Map&lt;String, UISocketCommandParam&gt;
	 */
	public HashMap<String, UISocketCommandParam> getCommandParamMap(String indices) {
		
		if ( !isDimensional() ) 
			return commandParams;
		else if( isDimensional() && indices != null )
			return getElementMapOnIndex(indices, getDimMap());
		else
			return null;
	}
	
	/**
	 * Get the Object of UISocketCommandParam for specified paramId.
	 * 
	 * @param paramId a String value of paramId
	 * 
	 * @return an Object of UISocketCommandParam
	 */
	public UISocketCommandParam getCommandParam(String paramId) {
		
		if ( paramId == null )
			return null;
		
		HashMap<String, UISocketCommandParam> paramMap = getCommandParamMap();
		
		if ( paramMap == null )
			return null;
		
		return paramMap.get(paramId);
	}
	
	/**
	 * Get the Object of UISocketCommandParam for specified paramId and indices.
	 * 
	 * @param indices a String value of indices
	 * @param paramId a String value of paramId
	 * 
	 * @return an Object of UISocketCommandParam
	 */
	public UISocketCommandParam getCommandParam(String indices, String paramId) {
		
		if ( paramId == null )
			return null;
		
		HashMap<String, UISocketCommandParam> paramMap ;
		
		if ( indices == null )
			paramMap = getCommandParamMap();
		else
			paramMap = getCommandParamMap(indices);
		
		if ( paramMap == null )
			return null;
		
		return paramMap.get(paramId);
		
	}
	
	
	public Map<String, CommandProps> getCommandProps() {
		return getCommandProps("[]");
	}
	
	public Map<String, CommandProps> getCommandProps(String indices) {
		//System.out.println("UISocketCommand  : getCommandProps : indices : "+indices);
		if ( indices == null ) 
			return null;
		//System.out.println("UISocketCommand  : getCommandProps : getDimMap : "+getDimMap());
		if ( !CommonUtilities.isIndexValueValid(indices) ) {
			return null;
		}
		//System.out.println("UISocketCommand  : getCommandProps : getDimMap : "+getDimMap());
		if ( getDimMap() == null ) 
			return null;
		
		Map<String, CommandProps> returnMap = new HashMap<String, CommandProps>();
		
		getElementMaps(indices, "", getDimMap(), returnMap);
		//System.out.println("UISocketCommand  : getCommandProps : returnMap : "+returnMap.keySet());
		return returnMap;
	}
	/*	private void getElementMaps(String indices, String prefix, LinkedHashMap<String, Object> dimMap, Map<String, CommandProps> returnMap) {
		try{
		if ( (indices == null) || (dimMap == null) || (returnMap == null) )
			return;
		System.out.println("UISocketCommand  : getElementMaps : indices : "+indices);
		System.out.println("UISocketCommand  : getElementMaps : dimMap : "+dimMap.values());
		
		String currentIndex = null;
		String nextIndices = null;
		
		if ( indices.indexOf('[', 1) == -1 ) { // e.g. indices = [] or [idx1 idx2] or [idx1]
			
			currentIndex = indices;
			nextIndices = null;
			
		} else {
			
			currentIndex = indices.substring(0, indices.indexOf('[', 1) );
			nextIndices = indices.substring( indices.indexOf('[', 1) );
		}
		System.out.println("UISocketCommand  : getElementMaps : currentIndex : "+currentIndex);
		System.out.println("UISocketCommand  : getElementMaps : nextIndices : "+nextIndices);
		
		if ( currentIndex.substring(currentIndex.indexOf('[')+1, currentIndex.indexOf(']')).trim().equals("") ) { // e.g. currentIndex = []
			System.out.println("UISocketCommand  : getElementMaps : if  start: "+currentIndex.substring(currentIndex.indexOf('[')+1, currentIndex.indexOf(']')).trim());
			for ( String index : dimMap.keySet() ) {
				System.out.println("UISocketCommand  : getElementMaps : index : "+index);
				Object value = dimMap.get(index);
				
				if ( value == null )
					continue;
				System.out.println("UISocketCommand  : getElementMaps : value : "+value);
				if ( value instanceof LinkedHashMap ) {
					System.out.println("UISocketCommand  : getElementMaps : if nextIndices : "+nextIndices);
					if ( nextIndices == null ){
						//getElementMaps("[]", index, dimMap, returnMap);
					}
					else{
						getElementMaps(nextIndices, index, dimMap, returnMap);
					}
				} else if ( value instanceof CommandProps ) {
					System.out.println("UISocketCommand  : getElementMaps : prefix+index : "+prefix+index);
					System.out.println("UISocketCommand  : getElementMaps : (CommandProps)value : "+(CommandProps)value);
					returnMap.put(prefix+index, (CommandProps)value);
				}
				
			}
			System.out.println("UISocketCommand  : getElementMaps : if  end: "+currentIndex.substring(currentIndex.indexOf('[')+1, currentIndex.indexOf(']')).trim());
			
		} else if ( currentIndex.indexOf(' ') != -1 ) { // e.g. currentIndex = [idx1 idx2]
			
			System.out.println("UISocketCommand  : getElementMaps : else if  start: "+currentIndex.indexOf(' '));
			
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
							
							if ( nextIndices == null ){
								getElementMaps("[]", index, dimMap, returnMap);
							}else 
								getElementMaps(nextIndices, index, dimMap, returnMap);
							
						} else if ( value instanceof CommandProps ) {
							
							returnMap.put(prefix+index, (CommandProps)value);
						}
						
					}
				}
				
				if ( index.equals(endIndex) ) 
					break;
			}
			System.out.println("UISocketCommand  : getElementMaps : else if  end: "+currentIndex.indexOf(' '));
		} else { // e.g. currentIndex = [idx1]
			
			System.out.println("UISocketCommand  : getElementMaps : else if else start: "+currentIndex);
			String index = currentIndex.substring(0, currentIndex.indexOf('[') );
			System.out.println("UISocketCommand  : getElementMaps : else if else start : index : "+index);
			Object value = dimMap.get(index);
			
			if ( value != null ) {
				
				if ( value instanceof LinkedHashMap ) {
					
					if ( nextIndices == null ){
						getElementMaps("[]", index, dimMap, returnMap);
					}else 
						getElementMaps(nextIndices, index, dimMap, returnMap);
					
				} else if ( value instanceof CommandProps ) {
					
					returnMap.put(prefix+index, (CommandProps)value);
				}
				
			}
			System.out.println("UISocketCommand  : getElementMaps : else if else end: "+currentIndex);
		}
		}catch(Exception e){
			System.out.println("@@@@@@@@@@ Exception  "+e);
		}
	}
	*/
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
			return null;
		}
		
	}
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

private void getElementMaps(String index,String prefix,  LinkedHashMap<String, Object> dimMap, Map<String, CommandProps> returnMap) {
		
/*	System.out.println("UISocketCommand  : getElementMaps : indices : "+index);
	System.out.println("UISocketCommand  : getElementMaps : prefix : "+prefix);
	System.out.println("UISocketCommand  : getElementMaps : dimMap 11111 keyset : "+dimMap.keySet());
	System.out.println("UISocketCommand  : getElementMaps : dimMap 11111 values : "+dimMap.values());*/
		if ( (index == null) || (index.indexOf("[") == -1) || (index.indexOf("]") == -1) )
			return;
		
		if ( dimMap == null )
			return;
		
		index = index.trim();
		
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
			
			if ( index.substring(index.indexOf("[")+1, index.indexOf("]")).trim().equals("") ) { // e.g. index = []
				
				for( String ind : dimMap.keySet() )	
					returnMap.put(prefix+ind, (CommandProps)dimMap.get(ind) );
				
				return;
				
			} else {// e.g. index = [ind] or [ind2 ind5]
				
				if ( index.indexOf(" ") == -1 ) {// e.g. index = [ind]
					
					returnMap.put(prefix+index, (CommandProps)getValue(index, dimMap));
					
					return;
					
				} else {// e.g. index = [ind2 ind5]
					
					String startIndex = index.substring(1, index.indexOf(" ")).trim();
					String endIndex = index.substring(index.lastIndexOf(" "), index.indexOf("]")).trim();
					
					for( String ind : dimMap.keySet() ) {
						
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) )
							returnMap.put(prefix+ind, (CommandProps)dimMap.get(ind) );
						
					}
					
					return;
				}
			}
			
		} else { // e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring( 0, index.indexOf("]")+1 );
			if ( firstIndex.substring(firstIndex.indexOf("[")+1, firstIndex.indexOf("]")).trim().equals("") ) { // e.g. firstIndex = []
				
				for( String ind : dimMap.keySet() ) {
					
					Object obj = dimMap.get(ind);
			//		System.out.println("UISocketCommand  : getElementMaps : 11111111  : prefix+ind : "+prefix+ind);
					if ( obj instanceof LinkedHashMap ) {
						getElementMaps(index.substring(index.indexOf("]")+1),prefix+ind, (LinkedHashMap)obj, returnMap);
					}
				}
				return;
				
			} else {// e.g. firstIndex = [ind] or [ind2 ind5]
				
				if ( firstIndex.indexOf(" ") == -1 ) {// e.g. firstIndex = [ind]
				//	System.out.println("UISocketCommand  : getElementMaps : 22222222  : firstIndex : "+firstIndex);
					String ind = firstIndex.substring(firstIndex.indexOf("["), firstIndex.indexOf("]")+1);
				//	System.out.println("UISocketCommand  : getElementMaps : 22222222  : ind : "+ind);
					Object obj = dimMap.get(ind);
				//	System.out.println("UISocketCommand  : getElementMaps : 22222222  : prefix+firstIndex : "+prefix+firstIndex);
					if ( obj instanceof LinkedHashMap )
						getElementMaps(index.substring(index.indexOf("]")+1), prefix+firstIndex, (LinkedHashMap)obj, returnMap);
					else 
						return;
					
				} else {// e.g. firstIndex = [ind2 ind5]
					
					String startIndex = firstIndex.substring(1, firstIndex.indexOf(" ")).trim();
					String endIndex = firstIndex.substring(firstIndex.lastIndexOf(" "), firstIndex.indexOf("]")).trim();
					
					
					for ( String ind : dimMap.keySet() ) {
												
						if ( (ind.compareTo("["+startIndex+"]") >= 0) && (ind.compareTo("["+endIndex+"]") <= 0) ) {
							
							Object obj = dimMap.get(ind);
					//		System.out.println("UISocketCommand  : getElementMaps : 33333333  : prefix+ind : "+prefix+ind);
							if ( obj instanceof LinkedHashMap ) 
								getElementMaps(index.substring(index.indexOf("]")+1),prefix+ind, (LinkedHashMap)obj, returnMap);
							
						}
					}
				}
			}
			
		}
		
	}	
	// If you are sure SocketSet is dimensional
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.AbstractUISocketElement#addDimension(java.lang.String)
	 */
	public boolean addDimension(String index) {
		//System.out.println("UISocketCommand : addDimension : index :" +index);
		logger.info(getElementId()+" is dimensional? "+isDimensional() );
		if( !isDimensional() ) 
			return false;
		else {
			
			if( getDimMap() == null )
				setDimMap( new LinkedHashMap<String, Object>() );
			
			boolean retValue =  addNewDimension(index, getDimMap());
			
			return retValue;
			
		}
	}
	
	// If you are sure SocketSet is dimensional
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.AbstractUISocketElement#removeDimension(java.lang.String)
	 */
	public boolean removeDimension(String index) {
		
		if( !isDimensional() ) 
			return false;
		else 
			return removeExistingDimension(index, getDimMap());
	}
	
	
	//////////////////////////////////////////getElementMapOnIndex////////////////////////////////////////////////////////
	/**
	 * Get Map of UISocketCommandParam on specified Index.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * 
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, UISocketCommandParam> getElementMapOnIndex(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 || dimMap == null )
			return null;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			Object obj = dimMap.get(index);
			
			if ( obj instanceof HashMap )
				return (HashMap)obj;
				
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getElementMapOnIndex( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////removeExistDimension///////////////////////////////////////////////////
	/**
	 * Remove existing dimension on specified index.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * 
	 * @return a boolean value
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
	
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */
	abstract public Object clone();

	/**
	 * Add new dimension for specified 'index' and put into dimMap.
	 * 
	 * @param index a String value of index
	 * @param dimMap a Map&lt;String, Object&gt;
	 */
	@SuppressWarnings("unchecked")
	private boolean addNewDimension( String index, LinkedHashMap<String, Object> dimMap) {
		//System.out.println("UISocketCommand : addNewDimension : index :" +index);
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			if ( !dimMap.containsKey(index) ) {
				
				dimMap.put(index, prepareCommandProps());			
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
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private CommandProps prepareCommandProps() {
		
		CommandProps commandProps = new CommandProps();
		
		commandProps.setCommandParams( getClonedCommandParams() );
		commandProps.setState("initial");
		commandProps.setTtc(0);
		
		return commandProps;
	}
	/**
	 * 
	 * Get Cloned copy of command Parameters.
	 * 
	 * @return HashMap&lt;String, UISocketCommandParam&gt; of ClonedCommandParameters
	 */
	private HashMap<String, UISocketCommandParam> getClonedCommandParams() {
		
		//System.out.println("Command Params to be cloned=");
		HashMap<String, UISocketCommandParam> clonedCmdParams = new HashMap<String, UISocketCommandParam>();
		
		for( String paramName : commandParams.keySet() ) 
			clonedCmdParams.put( new String(paramName), (UISocketCommandParam)commandParams.get(paramName).clone() );
		
		return clonedCmdParams;
	}
	
}
