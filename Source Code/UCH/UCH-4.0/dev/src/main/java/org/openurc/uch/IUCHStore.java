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

/**
 * In some cases two different modules (UIPMs, TDMs, TAs) may wish to share data with each other. 
 * For this purpose, a shared Map is provided by the UCH, called "local UCH store".  Modules may store data
 * as key-value pairs in the local UCH store, which may be accessed by other (related) modules that know the
 * corresponding keys.  
 *
 * It is recommended that keys are used that will likely not conflict with data written by other modules at 
 * runtime.  Manufacturers of modules should use their domain name as a prefix for their keys, and use dots 
 * as delimiter between key subcomponents.  Example: "example.com.uipm.607.portno".
 *
 * Note that the local UCH store is not a safe place for communicating sensitive information such as 
 * authorization keys, since it can be read by all UIPMs, TDMs and TAs.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public interface IUCHStore {
	/**
	 * Get a stored value from the local UCH store 
	 * @param key a String value
	 * @return stored Object for the corresponding value
	 */
	Object getValue(String key);
	
	/**
	 * Set a (new) value for the given key.  If the key doesn’t exist in the map before, it will be created.  
	 * If the key is already existing, the map stores the new value under the key, and the old value is removed.
	 * 
	 * @param key a String value
	 * @param value an Object value to be stored
	 */
	void setValue(String key, Object value);
	
}