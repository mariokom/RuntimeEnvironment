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

/**
 * UISocketXmlVariable is the variant of UISocketElement.
 * It provides the functionality to set and get the XML value.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UISocketXmlVariable extends AbstractUISocketElement {

	private Map<String, NodeDetail> nodeDetailMap;
	
	/**
	 * Default Constructor.
	 */
	public UISocketXmlVariable() {
		nodeDetailMap = new HashMap<String, NodeDetail>();	
	}
	////////////////////////////Temp. Methods/////////////////////////////////
	
	/**
	 * Get value of XML variable.
	 * 
	 * @return a String value of XML variable
	 */
	public String getValue() {
		//System.out.println("-------------getValue of UISocketXmlVariable------------");
		if( isDimensional() )
			return null;
		else {
			
			NodeDetail nodeDetail = nodeDetailMap.get("0");
			
			if (nodeDetail == null )
				return null;
			else
				return nodeDetail.getMetaData();
		}
		
	}
	
	/**
	 * Get value of XML variable on specified indices.
	 * .
	 * @param indices a String value of indices 
	 * 
	 * @return a String value of XML variable on specified indices
	 */
	public String getValue(String indices) {
		
		if ( (indices == null) && !isDimensional() )
			return getValue();
		else if( (indices != null) && isDimensional() ){
			
			Map<String, NodeDetail> nodeDetailMap = getNodeDetailMap(indices);
			
			if ( nodeDetailMap == null )
				return null;
			
			NodeDetail nodeDetail = nodeDetailMap.get("0");
			
			if (nodeDetail == null )
				return null;
			else
				return nodeDetail.getMetaData();
		}
		return null;
	}
	
	/**
	 * Set value of XML variable.
	 * 
	 * @param value a String value of XML variable
	 * 
	 * @return whether the value set successfully or not.
	 */
	public boolean setValue(String value) {
		
		if( isDimensional() )
			return false;
		else {
			
			NodeDetail nodeDetail = nodeDetailMap.get("0");
			
			if ( nodeDetail == null ) {
				
				nodeDetail = new NodeDetail();
				nodeDetail.setMetaData(value);
				nodeDetailMap.put("0", nodeDetail);
				
			} else {
				
				nodeDetail.setMetaData(value);
			}
			
			return true;
		}
	}
	
	/**
	 * Set value of XML variable on specified index.
	 * 
	 * @param value a String value of XML variable
	 * @param indices a String value indices
	 * 
	 * @return whether the value set successfully or not.
	 */
	public boolean setValue(String value, String indices) {
		
		if ( (indices == null) && !isDimensional() )
			return setValue(value);
		else {
			
			Map<String, NodeDetail> nodeDetailMap = getNodeDetailMap(indices);
			
			if ( nodeDetailMap == null )
				return false;
			
			NodeDetail nodeDetail = nodeDetailMap.get("0");
			
			if ( nodeDetail == null ) {
				
				nodeDetail = new NodeDetail();
				nodeDetail.setMetaData(value);
				nodeDetailMap.put("0", nodeDetail);
				
			} else {
				
				nodeDetail.setMetaData(value);
			}
			
			return true;
		}
		
	}
	/////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////setRoot Starts/////////////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public boolean setRoot(String id, String xmlMetaData, String index ) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return setRoot(id, xmlMetaData);
			
		} else if ( isDimensional() && (index != null) ) {
			
			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return false;
			else
				return setRoot(id, xmlMetaData, nDetailMap);
			
		}
		
		return false;
	}

	/**
	 * Not Implemented.
	 */
	public boolean setRoot(String id, String xmlMetaData) {
		
		if ( isDimensional() )
			return false;
		
		if ( (id==null) || (xmlMetaData==null) ) {
			return false;
		}
		
		return setRoot(id, xmlMetaData, nodeDetailMap);
	}
	
	/**
	 * Not Implemented.
	 */
	private boolean setRoot(String id, String xmlMetaData, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail = new NodeDetail();
		
		nodeDetail.setMetaData(xmlMetaData);

		nodeDetailMap.put(null, nodeDetail);
		nodeDetailMap.put(id, nodeDetail);
		
		return true;
	}
	//////////////////////////////////////////setRoot Ends///////////////////////////////////////////////
	
	
	///////////////////////////////////////setValueIdMetaData Starts/////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public boolean setValueIdMetaData(String id, String parentId, String xmlMetaData, String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return setValueIdMetaData(id, parentId, xmlMetaData);
			
		} else if( isDimensional() && (index != null) ) {
			
			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return false;
			else
				return setValueIdMetaData(id, parentId, xmlMetaData, nDetailMap);
		}
		
		return false;	
	}

	/**
	 * Not Implemented.
	 */
	public boolean setValueIdMetaData(String id, String parentId, String xmlMetaData) {
		
		if ( isDimensional() )
			return false;
		
		if ( (id==null) || (parentId==null) || (xmlMetaData==null) ) {
			return false;
		}
		
		return setValueIdMetaData(id, parentId, xmlMetaData, nodeDetailMap);
	}
	
	/**
	 * Not Implemented.
	 */
	private boolean setValueIdMetaData(String id, String parentId, String xmlMetaData, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail =  nDetailMap.get(id);
		
		if ( nodeDetail == null ) {
			
			nodeDetail = new NodeDetail();
			nodeDetail.setParentId(parentId);
			nodeDetail.setMetaData(xmlMetaData);
			nDetailMap.put(id, nodeDetail);
			
		} else {
			
			nodeDetail.setParentId(parentId);
			nodeDetail.setMetaData(xmlMetaData);
			
		}
			
		return true;
		
	}
	///////////////////////////////////////setValueIdMetaData Ends///////////////////////////////////////
	
	
	///////////////////////////////////////setValueChildren Starts///////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public boolean setValueChildren(String id, String xmlMetaData) {
		
		if ( isDimensional() )
			return false;
		
		if ( (id==null) || (xmlMetaData==null) ) {
			return false;
		}
		
		return setValueChildren(id, xmlMetaData, nodeDetailMap);
	}
	
	/**
	 * Not Implemented.
	 */
	public boolean setValueChildren(String id, String xmlMetaData, String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return setValueChildren(id, xmlMetaData);
			
		} else if( isDimensional() && (index != null) ) {
			
			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return false;
			else
				return setValueChildren(id, xmlMetaData, nDetailMap);
		}
		
		return false;
	}
	
	/**
	 * Not Implemented.
	 */
	private boolean  setValueChildren(String id, String xmlMetaData, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail =  nDetailMap.get(id);
		
		if ( nodeDetail == null ) {
			
			nodeDetail = new NodeDetail();
			nodeDetail.setDirectChildren(xmlMetaData);
			nDetailMap.put(id, nodeDetail);
			
		} else {
			
			nodeDetail.setDirectChildren(xmlMetaData);
			
		}
		
		return true;
	}
	///////////////////////////////////////setValueChildren Ends/////////////////////////////////////////
	
	
	///////////////////////////////////////////getRoot Starts////////////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public String getRoot() {
		
		if ( isDimensional() )
			return null;
		
		return getRoot(nodeDetailMap);
	}
	
	/**
	 * Not Implemented.
	 */
	public String getRoot(String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return getRoot();
			
		} else if( isDimensional() && (index != null) ) {
			
			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return null;
			else
				return getRoot(nDetailMap);
			
		}
		
		return null;		
	}
	
	/**
	 * Not Implemented.
	 */
	private String getRoot(Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail =  nDetailMap.get(null);
		
		if ( nodeDetail == null ) {
			return null;
		}
		
		return nodeDetail.getDirectChildren();
		
	}
	////////////////////////////////////////////getRoot Ends/////////////////////////////////////////////
	
	
	////////////////////////////////////////getIDMetaData Starts/////////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public String getIdMetaData(String id) {
		
		if ( isDimensional() )
			return null;
		
		if ( id==null ) {
			return null;
		}
		
		return getIdMetaData(id, nodeDetailMap);		
	}
	
	/**
	 * Not Implemented.
	 */
	public String getIdMetaData(String id, String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return getIdMetaData(id);
			
		} else if( isDimensional() && (index != null) ) {

			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return null;
			else
				return getIdMetaData(id, nDetailMap);
			
		}
		
		return null;		
	}
	
	private String getIdMetaData(String id, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail = nDetailMap.get(id);
		
		if ( nodeDetail == null )
			return null;
		
		return nodeDetail.getMetaData();
		
	}
	/////////////////////////////////////////getIDMetaData Ends//////////////////////////////////////////
	
	
	/////////////////////////////////////////getChildren Starts//////////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public String getChildren(String id) {
		
		if ( isDimensional() )
			return null;
		
		if ( id==null ) {
			return null;
		}
		
		return getChildren(id, nodeDetailMap);
	}
		
	/**
	 * Not Implemented.
	 */
	public String getChildren(String id, String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			
			return getChildren(id);
			
		} else if( isDimensional() && (index != null) ) {
			
			Map<String, NodeDetail> nDetailMap = getNodeDetailMap(index);
			
			if ( nDetailMap == null )
				return null;
			else
				return getChildren(id, nDetailMap);
			
		}
		return null;
		
	}
	
	private String getChildren(String id, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail = nDetailMap.get(id);
		
		if ( nodeDetail == null )
			return null;
		
		return nodeDetail.getDirectChildren();
		
	}
	//////////////////////////////////////////getChildren Ends///////////////////////////////////////////
	
	
	//////////////////////////////////////////getParent Starts///////////////////////////////////////////
	/**
	 * Not Implemented.
	 */
	public String getParent(String id) {
		
		if ( isDimensional() )
			return null;
		
		if ( id==null ) {
			return null;
		}
		
		return getParent(id, nodeDetailMap);
	}
	
	/**
	 * Not Implemented.
	 */
	public String getParent(String id, String index) {
		
		if( (!isDimensional()) && (index == null) ) {
			return getParent(id);
		} else if( isDimensional() && (index != null) ) {
			//code for dimensional
		}
		return null;
		
	}
	
	private String getParent(String id, Map<String, NodeDetail> nDetailMap) {
		
		NodeDetail nodeDetail = nDetailMap.get(id);
		
		if ( nodeDetail == null )
			return null;
		
		String parentId = nodeDetail.getParentId();
		
		if ( parentId == null )
			return null;
		
		nodeDetail = nodeDetailMap.get(parentId);
		
		if ( nodeDetail == null )
			return null;
		
		return nodeDetail.getMetaData();
		
	}
	///////////////////////////////////////////getParent Ends////////////////////////////////////////////
	
	
	//////////////////////////////////////////removeExistDimension///////////////////////////////////////////////////
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
	
	/**
	 * Removes existing dimension specified by index
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean removeExistingDimension(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( dimMap == null )
			return false;
		
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
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#addDimension(java.lang.String)
	 */
	public boolean addDimension(String index) {
		
		if ( index == null )
			return false;
		
		if ( !isDimensional() ) 
			return false;
		else {

			if( getDimMap() == null )
				setDimMap( new LinkedHashMap<String, Object>() );
			
			return addNewDimension(index, getDimMap());
		}
	}
	
	/**
	 * Add a new dimension to the UISocketVariable specified by 'index'.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean addNewDimension( String index, LinkedHashMap<String, Object> dimMap) {
	
		if ( dimMap == null )
			return false;
		
		if ( index.indexOf("[") == -1 || index.indexOf("]") == -1 )
			return false;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			if ( !dimMap.containsKey(index) ) 
				dimMap.put(index, new HashMap<String, NodeDetail>() );
			
			return true;
			
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
	
	
	/////////////////////////////////////////////////////getNodeDetailMap/////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	private Map<String, NodeDetail> getNodeDetailMap( String index) {
		
		if ( index == null )
			return null;
		
		Object object = getNodeDetailMap(index, getDimMap() );
		
		if ( object instanceof HashMap ) 
			return (HashMap)object;
		else
			return null;
	}
	
	/**
	 * Get Value of the Variable.
	 * 
	 * @param index a String value of index
	 * @param dimMap Map&lt;String, Object&gt;
	 * @return an Object
	 */
	@SuppressWarnings("unchecked")
	private Object getNodeDetailMap(String index, LinkedHashMap<String, Object> dimMap) {
		
		if ( dimMap == null )
			return null;
		
		if ( (index.indexOf("[") == -1) || (index.indexOf("]") == -1) || (dimMap == null) )
			return null;
		
		if ( index.indexOf("[", index.indexOf("]")) == -1 ) { //e.g. index = [ind]
		
			return dimMap.get(index);
			
		} else {// e.g. index = [ind1]...[indn]
			
			String firstIndex = index.substring(0, index.indexOf("]"));
			Object obj = dimMap.get(firstIndex);
			
			if ( obj instanceof LinkedHashMap ) 
				return getNodeDetailMap( index.substring(index.indexOf("]")+1), (LinkedHashMap)obj );
			
		}		
		
		return null;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */
	public Object clone() {
		
		//System.out.println("I am cloning for Element '"+this.getElementId()+"'.");
		
		UISocketVariable clonedSocketVariable = new UISocketVariable();
		
		clonedSocketVariable.setElementId( new String( this.getElementId() ) );
		clonedSocketVariable.setDimensional( this.isDimensional() );
		
		clonedSocketVariable.setSocket( this.getSocket() );
		
		if ( this.getDimType() != null )
			clonedSocketVariable.setDimType( this.getDimType() );
		
		if ( this.getType() != null ) 
			clonedSocketVariable.setType( this.getType() );
		
		if ( this.getSession() != null ) 
			clonedSocketVariable.setSession( this.getSession() );
		
		
		return clonedSocketVariable;	
	}
	
	
	
	
	private class NodeDetail {
		
		private String parentId;
		private String metaData;
		private String directChildren;
		
		
		private String getParentId() {
			return parentId;
		}
		private void setParentId(String parentId) {
			this.parentId = parentId;
		}
		private String getMetaData() {
			return metaData;
		}
		private void setMetaData(String metaData) {
			this.metaData = metaData;
		}
		private String getDirectChildren() {
			return directChildren;
		}
		private void setDirectChildren(String directChildren) {
			this.directChildren = directChildren;
		}
				
	}
}
