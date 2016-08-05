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

package org.openurc.uch;

import java.util.List;

/**
 * Provide interface to save and retrieve profile properties.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version Revision: 1.0
 */
public interface IProfile {
	
	/**
	 * Get the value for specified key and password.
	 * Key must not be null.
	 * password may be null.
	 * 
	 * @param key a String value of key
	 * @param password a String value of password
	 * 
	 * @return a String value
	 */
	String getValue(String key, String password);
	
	/**
	 * Store the value of Key, Value and Password.
	 * If the value store successfully then returns true else returns false.
	 * Key and Value must not be null.
	 * Password may be null.
	 * If the key has not existed yet, the password is stored for future access to the key-value pair. 
	 * If the key has already existed, the password must match the password for the stored key-value pair.
	 * 
	 * @param key a String value of key
	 * @param value a String value of value
	 * @param password a String value of password
	 * 
	 * @return a boolean value indicates that the value stored successfully or not.
	 */
	boolean setValue(String key, String value, String password);
	
	/**
	 * Remove the specified value.
	 * Key must not be null.
	 * Password may be null.
	 * 
	 * @param key a String value of key
	 * @param password a String value of password
	 * 
	 * @return a boolean value indicates that the value removed successfully or not.
	 */
	boolean removeValue(String key, String password);
	
	/**
	 * Get an Array or core keys.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	List<String> getCoreKeys();
}