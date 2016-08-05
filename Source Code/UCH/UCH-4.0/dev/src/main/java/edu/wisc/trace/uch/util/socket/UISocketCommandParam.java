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

/**
 * Maintains 'param' Element of 'Command' of the Socket Description File.
 * It also provides methods to access/change the Element thereof.
 *
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/


public class UISocketCommandParam extends AbstractUISocketElement{

	private String id ;
	private String dir ;
	private String type ;
	private Object value ;
	private boolean isIdRef ;
	
	/**
	 * Constructor
	 */
	
	public UISocketCommandParam() {
		
	}

	
	/**
	 * Returns Id.
	 * 
	 * @return a String
	 */
	
	public String getId() {
		return id;
	}

	/**
	 * Set Id.
	 * 
	 * @param id a String value of id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get Direction.
	 * 
	 * @return a String
	 */
	public String getDir() {
		return dir;
	}
	
	/**
	 * Set Direction.
	 * 
	 * @param dir a String value of dir
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	/**
	 * Get type.
	 * 
	 * @return a String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set type.
	 * 
	 * @param type a String value of type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get Value.
	 * 
	 * @return an Object
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Set Value.
	 * 
	 * @param value an Object type value
	 */
	public boolean setValue(Object value) {
		this.value = value;
		return true;
	}
	
	/**
	 * Check whether isIdRef exists or not.
	 * 
	 * @return boolean
	 */
	public boolean isIdRef() {
		return isIdRef;
	}
	
	/**
	 * Sets IdRef if it exists.
	 * 
	 * @param isIdRef a boolean value of isIdRef
	 */
	public void setIdRef(boolean isIdRef) {
		this.isIdRef = isIdRef;
	}
	
	
	//--------------------------------------Unnecessary overridden methods--------------------------------------//
	public boolean addDimension(String index) {
		return false;
	}


	public boolean removeDimension(String index) {
		return false;
	}
	//--------------------------------------Unnecessary overridden methods--------------------------------------//
	
	
	/**
	 * Prepares a Clone Copy of the Object of this class.
	 * 
	 * @return an Object
	 */
	
	public Object clone() {
		
		UISocketCommandParam clonedCmdParam = new UISocketCommandParam();
		
		clonedCmdParam.setDir( new String(this.dir) );
		clonedCmdParam.setId( new String(this.id) );
		clonedCmdParam.setType( new String(this.type) );
		clonedCmdParam.setIdRef( this.isIdRef );
				
		return clonedCmdParam;
	}


	
	
}
