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

import edu.wisc.trace.uch.util.Session;

/**
 * IUISocketElement is the interface used for creating 
 * UI Socket Element Objects. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/

public interface IUISocketElement {

	 /**
	  * Set the value of Element Id.
	  * 
	  * @param elementId a String value of Element Id
	  */
	 public void setElementId(String elementId);
	 
	 /**
	  * Get the value of Element Id.
	  * 
	  * @return a String value of Element Id
	  */
	 public String getElementId();
	 
	 /**
	  * Set the Reference of Socket Description. 
	  * 
	  * @param socketDescription an Object of SocketDescription
	  * 
	  * @see edu.wisc.trace.uch.util.socket.SocketDescription
	  */
	 public void setSocket(SocketDescription socketDescription);
	 
	 /**
	  * Get the Object of Socket Description.
	  * 
	  * @return an Object of SocketDescription
	  */
	 public SocketDescription getSocket();
	 
	 /**
	  * Set the Reference of Session.
	  * 
	  * @param session an Object of Session
	  */
	 public void setSession(Session session);
	 
	 /**
	  * Get the Reference of Session.
	  * 
	  * @return an Object of Session
	  */
	 public Session getSession();
	 
	 /**
	  * Set the boolean value of isDimensional.
	  * 
	  * @param isDimensional a boolean value of isDimensional
	  */
	 public void setDimensional(boolean isDimensional);
	 
	 /**
	  * Checks whether socket Element is dimensional or not.
	  * 
	  * @return boolean a boolean value of isDimensional
	  */
	 public boolean isDimensional();
	 
	 /**
	  * Get the Dimension Map.
	  * 
	  * @return LinkedHashMap&lt;String, Object&gt;
	  */
	 public LinkedHashMap<String, Object> getDimMap();
	 
	 /**
	  * Get the value of IncludeRes.
	  * 
	  * @return a boolean value of includeRes
	  */
	 public boolean isIncludeRes();
	 
	 /**
	  * Set the value of IncludeRes.
	  * 
	  * @param isIncludeRes a boolean value of includeRes
	  */
	 public void setIncludeRes(boolean isIncludeRes);
	 
	 /**
	  * Get the value of Type.
	  * 
	  * @return a String value of Type
	  */
	 public String getType();

	 /**
	  * Set the value of Type.
	  * 
	  * @param type a String value of Type
	  */
	 public void setType(String type);
	 
	 /**
	  * Get the value of Dimensional Type.
	  * 
	  * @return a String value of DimType
	  */
	 public String getDimType();
	 
	 /**
	  * Set the value of Dimensional Type.
	  * 
	  * @param dimType a String value of DimType
	  */
	 public void setDimType(String dimType);

	 /**
	  * Get indices exists on specified level of Dimension
	  * 
	  * @param indexNo an int value of Index No.
	  * 
	  * @return indices exists on specified level of Dimension
	  */
	 public Set<String> getIndices(int indexNo);
	 
	 /**
	  * Prepares a Clone Copy of the Object of this class.
	  * 
	  * @return an Object
	  */
	 public Object clone();
	 
	 /**
	  * Adds a dimension with specified string index.
	  * 
	  * @param index a String value of index
	  * 
	  * @return whether the dimension added successfully or not.
	  */
	 public boolean addDimension(String index);
		
	 /**
	  * Removes Dimension of specified index.
	  * 
	  * @param index a String value of index
	  * 
	  * @return whether the dimension removed successfully or not.
	  */
	 public boolean removeDimension(String index);
}
