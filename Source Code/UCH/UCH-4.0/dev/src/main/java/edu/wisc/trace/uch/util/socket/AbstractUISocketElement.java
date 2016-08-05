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

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

import edu.wisc.trace.uch.util.Session;

/**
 * AbstractUISocketElement is an abstract implementation of the 
 * IUISocketElement interface. It provides for the common functionality all
 * UI Socket Elements require. 
 *
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/
public abstract class AbstractUISocketElement implements IUISocketElement{

	// 2012-09-17 : final attribute
	private boolean isFinal;
	
	private String id;
	private boolean isDimensional;
	private boolean isIncludeRes;
	private String type;
	private String dimType;
	private SocketDescription socketDescription;
	private Session session;
	private LinkedHashMap<String, Object> dimMap;
	
	
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}


	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#getElementId()
	*/
	public String getElementId() {		
		return id;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#setElementId(java.lang.String)
	*/
	public void setElementId(String elementId) {
		this.id = elementId;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#isDimensional()
	*/
	
	public boolean isDimensional() {
		return isDimensional;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#setDimensional(boolean)
	*/
	public void setDimensional(boolean isDimensional) {
		this.isDimensional = isDimensional;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#getSocket()
	*/
	public SocketDescription getSocket() {
		return socketDescription;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#setSocket(edu.wisc.trace.uch.util.socket.SocketDescription)
	*/
	public void setSocket(SocketDescription socketDescription) {
		this.socketDescription = socketDescription;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#getSession()
	*/
	public Session getSession() {
		return session;
	}
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#setSession(edu.wisc.trace.uch.util.Session)
	*/
	public void setSession(Session session) {
		this.session = session;
	}
	
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#getDimMap()
	*/
	public LinkedHashMap<String, Object> getDimMap() {
		return dimMap;
	}
	
	/**
	 * Sets map for dimensional socket Elements.
	 * 
	 * @param dimMap LinkedHashMap&lt;String, Object&gt;
	 */
	public void setDimMap(LinkedHashMap<String, Object> dimMap) {
		this.dimMap = dimMap;
	}

	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#isIncludeRes()
	 */
	public boolean isIncludeRes() {
		return isIncludeRes;
	}

	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#setIncludeRes(boolean)
	 */
	public void setIncludeRes(boolean isIncludeRes) {
		this.isIncludeRes = isIncludeRes;
	}
	
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#getType()
	 */
	public String getType() {
		return type;
	}

	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#setType(String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#getDimType()
	 */
	public String getDimType() {
		return dimType;
	}

	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#setDimType(String)
	 */
	public void setDimType(String dimType) {
		this.dimType = dimType;
	}

	/**
	 * Get IndexNames available on specific level of dimension of dimensional Element.
	 * 
	 * @param indexNo an int value of index number.
	 * @return a Set&lt;String&gt; of Index Values
	 */
	public Set<String> getIndices(int indexNo) {
		
		if( isDimensional() ) {
			
			Set<String> returnSet = new TreeSet<String>();
			getIndices( 0, indexNo, getDimMap(), returnSet);
			
			if ( returnSet.size() == 0 )
				return null;
			else
				return returnSet;
			
		} else {
			return null;
		}
	}
	
	
	
	
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	*/
	abstract public Object clone();
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#addDimension(java.lang.String)
	*/
	abstract public boolean addDimension(String index);
	
	/**
	* @see edu.wisc.trace.uch.util.socket.IUISocketElement#removeDimension(java.lang.String)
	*/
	abstract public boolean removeDimension(String index);

	/**
	 * Get indices on specified level of dimension
	 * 
	 * @param currentIndexNo an int value of currentIndexNo
	 * @param requiredIndexNo an int value of requiredIndexNo
	 * @param dimMap an Object of LinkedHashMap&lt;String, Object&gt;
	 * @param returnSet an Object of Set&lt;String&gt;
	 */
	@SuppressWarnings("unchecked")
	private void getIndices(int currentIndexNo, int requiredIndexNo, LinkedHashMap<String, Object> dimMap, Set<String> returnSet) {
		
		if ( dimMap == null )
			return;
		
		if( returnSet == null ) 
			return;
		
		if( currentIndexNo == requiredIndexNo ) {
			
			for ( String key : dimMap.keySet() ) {
				
				if( (key.indexOf("[") != -1) && (key.indexOf("]") != -1) ) 
					returnSet.add( key.substring( key.indexOf("[")+1, key.indexOf("]") ) );
			}
			
		} else {
			
			int nextIndexNo = currentIndexNo+1;
			
			for( String key : dimMap.keySet() ) {
				
				Object obj = dimMap.get(key);
				
				if( !(obj instanceof LinkedHashMap) )
					continue;
				
				getIndices(nextIndexNo, requiredIndexNo, (LinkedHashMap)obj, returnSet);
			}
		}
	}
}
