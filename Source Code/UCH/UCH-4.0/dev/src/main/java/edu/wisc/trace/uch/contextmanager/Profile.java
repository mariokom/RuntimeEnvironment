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
package edu.wisc.trace.uch.contextmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide the implementation of IProfile.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class Profile implements IProfile {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	// A map of <password, <key, value> >
	private Map<String, Map<String, String>> values;
	
	
	/**
	 * Default Constructor.
	 */
	Profile() {
		
		values = new HashMap<String, Map<String,String>>();
	}
	
	
	/**
	 * @see IProfile#getValue(java.lang.String, java.lang.String)
	 */
	public synchronized String getValue(String key, String password) {
		
		if ( key == null ) {
			logger.warning("Key is null.");
			return null;
		}
		
		// Password may be null. null password means key-value pair belongs to the open core
		Map<String, String> valueMap = values.get(password);
		
		if ( valueMap == null ) {
			logger.warning("Unable to get key-value map for the password '"+password+"'.");
			return null;
		}
		
		return valueMap.get(key);
	}

	
	/**
	 * @see IProfile#removeValue(java.lang.String, java.lang.String)
	 */
	public synchronized boolean removeValue(String key, String password) {
		
		if ( key == null ) {
			logger.warning("Key is null.");
			return false;
		}
		
		Map<String, String> valueMap = values.get(password);
		
		if ( valueMap == null ) {
			logger.warning("Unable to get key-value map for the password '"+password+"'.");
			return true;
		}
		
		valueMap.remove(key);

		return true;
	}

	
	/**
	 * @see IProfile#setValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized boolean setValue(String key, String value, String password) {
		
		if ( key == null ) {
			logger.warning("Key is null.");
			return false;
		}
		
		if ( value == null ) {
			logger.warning("Value is null.");
			return false;
		}
		
		if ( isKeyExists(key) ) {
			
			// If key is exists then check whether password are matched or not.
			// If password are matched then save value, else don't save value.
			
			String existingPassword = findPassword(key);
			
			if ( password == null ) { 
				
				if ( existingPassword == null ) { // both password are null, so save the value
					
					Map<String, String> valueMap = values.get(password);
					valueMap.put(key, replaceValue(valueMap.get(key), value));
					
					return true;
					
				} else { // both password are not matched, so don't save the value
					
					logger.warning("New Password and existing password are not matched.");
					return false;
				}
				
			} else {
				
				if ( password.equals(existingPassword) ) { // both password are not matched, so save the value
					
					Map<String, String> valueMap = values.get(password);
					valueMap.put(key, replaceValue(valueMap.get(key), value));
					
					return true;
					
				} else { // both password are not matched, so don't save the value
					
					logger.warning("New Password and existing password are not matched.");
					return false;
				}
			}
			
		} else { 
			
			// If key doesn't exists and password is exists then save the value in relevant map, 
			// else create a new map for that password and save the value.
			
			if ( values.containsKey(password) ) {
				
				Map<String, String> valueMap = values.get(password);
				valueMap.put(key, value);
				
			} else {
				
				Map<String, String> valueMap = new TreeMap<String, String>();
				valueMap.put(key, value);
				values.put(password, valueMap);
			}
			
			return true;
		}
		
	}
	
	
	/**
	 * @see IProfile#getCoreKeys()
	 */
	public synchronized List<String> getCoreKeys() {
		
		List<String> keyList = new ArrayList<String>();
		
		for ( Map<String, String> valueMap : values.values() ) {
			
			if ( valueMap == null )
				continue;
			
			for ( String key : valueMap.keySet() )
				keyList.add(key);
		}
		
		return keyList;
	}

	
	/**
	 * Check whether the specified key is exists or not.
	 * 
	 * @param key a String value of key.
	 * 
	 * @return a boolean value
	 */
	private boolean isKeyExists( String key ) {
		
		if ( key == null )
			return false;
		
		for ( Map<String, String> valueMap : values.values() ) {
			
			if ( valueMap == null )
				continue;
			
			if ( valueMap.containsKey(key) )
				return true;
			
		}
	
		return false;
	}
	
	
	/**
	 * Find password for the specified key.
	 * 
	 * @param key a String value of key
	 * 
	 * @return a String value of password
	 */
	private String findPassword( String key ) {
		
		if ( key == null )
			return null;
		
		for ( Entry<String, Map<String, String>> entry : values.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			Map<String, String> valueMap = entry.getValue();
			
			if ( valueMap == null )
				continue;
			
			if ( valueMap.containsKey(key) )
				return entry.getKey();
		}
		
		return null;
	}
	/**
	 *  Creates/Updates CSV list.
	 *  If not exists in CSV list, then newValue is appended.
	 *  
	 * @param oldValue a String of oldValue.
	 * @param newValue a String of newValue. 
	 * @return replacedValue a String of new CSV list.
	 */
	private String replaceValue(String oldValue, String newValue) {
		
		if(!oldValue.contains(newValue)) { // check existance of new value in the list.
			
			StringBuilder returnValue = new StringBuilder(oldValue);
			
			returnValue.append(",").append(newValue);
			
			return returnValue.toString();
		}
		
		return oldValue;
	}
}
